package booking.Services;

import booking.Dtos.*;
import booking.Entities.*;
import booking.Exceptions.ResourceNotFoundException;
import booking.Repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private RoomRepository roomRepository;

    private ReservationRepository reservationRepository;

    private UserRepository userRepository;

    private HotelRepository hotelRepository;

    private RoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;

    private AuthenticationManager authenticationManager;

    private UserDetailsServiceImpl userDetailsService;

    private JwtTokenService jwtTokenService;

    private EmailService emailService;

    @Autowired
    public UserService(RoomRepository roomRepository, ReservationRepository reservationRepository, UserRepository userRepository, HotelRepository hotelRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService, JwtTokenService jwtTokenService,EmailService emailService) {
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.hotelRepository = hotelRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenService = jwtTokenService;
        this.emailService=emailService;
    }

    @Transactional
    public AuthResponseDTO register(AuthRequestDTO authRequestDTO) {
        User user = new User();
        user.setUsername(authRequestDTO.getUsername());
        user.setPassword(passwordEncoder.encode(authRequestDTO.getPassword()));

        // Caută rolul ROLE_USER în baza de date
        Role role = roleRepository.findByRoleType(RoleType.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Role USER not found"));

        // Adaugă rolul utilizatorului
        user.getRoles().add(role);

        // Adaugă utilizatorul în lista rolului (opțional)
        role.getUsers().add(user);
        user.setEmail(authRequestDTO.getEmail());
        User savedUser = userRepository.save(user);
        return mapFromUserToAuthResponseDTO(savedUser);
    }

    @Transactional
    public AuthResponseDTO mapFromUserToAuthResponseDTO(User user) {
        AuthResponseDTO authResponseDTO = new AuthResponseDTO();
        authResponseDTO.setId(user.getId());
        authResponseDTO.setUsername(user.getUsername());
        authResponseDTO.setPassword(user.getPassword());
        authResponseDTO.setEmail(user.getEmail());
        return authResponseDTO;
    }

    public String authenticate(AuthRequestDTO authRequestDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequestDTO.getUsername());
        String token = jwtTokenService.generateToken(userDetails);
        return token;
    }

    @Transactional
    public UserResponseDTO findLoggedInUser() {
        String usernameLoggedIn = SecurityContextHolder.getContext().getAuthentication().getName();

        // Căutăm utilizatorul în baza de date
        User user = userRepository.findByUsername(usernameLoggedIn)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Creăm și returnăm UserResponseDTO
        return new UserResponseDTO(user.getId(),user.getUsername(),user.getEmail());
    }

    @Transactional
    public ReservationResponseDTO bookRoom(ReservationRequestDTO request) {
        // Găsește utilizatorul
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Găsește hotelul
        Hotel hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));

        // Găsește camera
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        // Verifică dacă camera aparține hotelului specificat
        if (!room.getHotel().getHotelId().equals(hotel.getHotelId())) {
            throw new IllegalArgumentException("Camera nu aparține hotelului specificat!");
        }

        // Calculează prețul total
        long daysBetween = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate());
        double price = room.getPricePerNight() * daysBetween;

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setRoom(room);
        reservation.setHotel(hotel);
        reservation.setStartDate(request.getStartDate());
        reservation.setEndDate(request.getEndDate());
        reservationRepository.save(reservation);

        emailService.sendBookingConfirmationAsync(
                user.getEmail(),
                hotel.getName(),
                room.getRoomNumber(),
                request.getStartDate(),
                request.getEndDate(),
                price
        );

        return mapFromReservationToReservationResponseDTO(reservation);
    }

    @Transactional
    public boolean isAvailable(Long roomId, LocalDate firstDay, LocalDate lastDay) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        boolean hasOverlappingReservation = room.getReservations().stream()
                .anyMatch(reservation ->
                        !(reservation.getEndDate().isBefore(firstDay) || reservation.getStartDate().isAfter(lastDay)));

        return !hasOverlappingReservation;
    }



    @Transactional
    public ReservationResponseDTObyRoomType bookRoomByType(RoomType roomType, LocalDate firstDay, LocalDate lastDay, Long userId,Long hotelId) {


        Hotel hotel=hotelRepository.findById(hotelId).orElseThrow(() -> new IllegalArgumentException("Hotel not found"));
        List<Room> rooms = roomRepository.findByRoomType(roomType);

        for (Room room : rooms) {
            boolean available = isAvailable(room.getId(), firstDay, lastDay);
            if (available) {
                Reservation reservation = new Reservation();
                reservation.setRoom(room);
                reservation.setStartDate(firstDay);
                reservation.setEndDate(lastDay);
                reservation.setHotel(hotel);

                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new IllegalArgumentException("User not found"));
                reservation.setUser(user);

                Reservation savedReservation = reservationRepository.save(reservation);
                ReservationResponseDTObyRoomType reservationResponseDTObyRoomType = new ReservationResponseDTObyRoomType();
                reservationResponseDTObyRoomType.setRoomType(savedReservation.getRoom().getRoomType());
                reservationResponseDTObyRoomType.setId(savedReservation.getRoom().getId());  // Setăm ID-ul camerei
                reservationResponseDTObyRoomType.setStartDate(savedReservation.getStartDate());
                reservationResponseDTObyRoomType.setEndDate(savedReservation.getEndDate());
                reservationResponseDTObyRoomType.setUserId(savedReservation.getUser().getId());
                reservationResponseDTObyRoomType.setHotelId(savedReservation.getHotel().getHotelId());

                return reservationResponseDTObyRoomType;
            }
        }
        throw new IllegalArgumentException("No available rooms of type " + roomType + " for the selected period.");
    }
    @Transactional
    public ReservationResponseDTO mapFromReservationToReservationResponseDTO(Reservation reservation) {
        ReservationResponseDTO reservationResponseDTO = new ReservationResponseDTO();
        reservationResponseDTO.setId(reservation.getReservationId());
        reservationResponseDTO.setUserId(reservation.getUser().getId());
        reservationResponseDTO.setRoomId(reservation.getRoom().getId());
        reservationResponseDTO.setStartDate(reservation.getStartDate());
        reservationResponseDTO.setEndDate(reservation.getEndDate());
        reservationResponseDTO.setHotelId(reservation.getRoom().getHotel().getHotelId());
        return reservationResponseDTO;
    }


    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Transactional
    public List<ReservationResponseDTO> findAllReservationsByUser(Long userId) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(() -> new Exception("User not found"));
        List<Reservation> reservations = user.getReservations();
        if (reservations == null || reservations.isEmpty()) {
            throw new Exception("Nu exista rezervari pentru acest user");
        }
        List<ReservationResponseDTO> responseDTOs = reservations.stream()
                .map(reservation->mapFromReservationToReservationResponseDTO(reservation))
                .collect(Collectors.toList());

        return responseDTOs;
    }
}
