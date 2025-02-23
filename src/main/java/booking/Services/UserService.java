package booking.Services;

import booking.Dtos.ReservationRequestDTO;
import booking.Dtos.ReservationResponseDTO;
import booking.Dtos.ReservationResponseDTObyRoomType;
import booking.Dtos.UserRequestDTO;
import booking.Entities.*;
import booking.Repositories.HotelRepository;
import booking.Repositories.ReservationRepository;
import booking.Repositories.RoomRepository;
import booking.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private RoomRepository roomRepository;

    private ReservationRepository reservationRepository;

    private UserRepository userRepository;
    private HotelRepository hotelRepository;

    @Autowired
    public UserService(RoomRepository roomRepository, ReservationRepository reservationRepository, UserRepository userRepository, HotelRepository hotelRepository) {
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.hotelRepository = hotelRepository;
    }

    @Transactional
    public User addUser(UserRequestDTO userRequestDTO) {
        User user = new User();
        user.setName(userRequestDTO.getName());
        user.setEmail(userRequestDTO.getEmail());
        return userRepository.save(user);
    }

    @Transactional
    public ReservationResponseDTO bookRoom(ReservationRequestDTO reservationRequestDTO) {
        LocalDate startDate = reservationRequestDTO.getStartDate();
        LocalDate endDate = reservationRequestDTO.getEndDate();

        // Verificare dacă startDate este înainte de endDate
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Data de început trebuie să fie înainte de data de sfârșit.");
        }

        Long roomId = reservationRequestDTO.getRoomId();

        boolean isReserved = reservationRepository.existsByRoomAndDateOverlap(roomId, startDate, endDate);
        if (isReserved) {
            throw new IllegalArgumentException("Camera este deja rezervată pentru perioada specificată.");
        }

        Reservation reservation = new Reservation();
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Camera cu ID-ul " + roomId + " nu există."));
        User user = userRepository.findById(reservationRequestDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Utilizatorul cu ID-ul " + reservationRequestDTO.getUserId() + " nu există."));
        reservation.setRoom(room);
        reservation.setUser(user);
        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);
        reservation.setHotel(room.getHotel());

        Reservation savedReservation = reservationRepository.save(reservation);
        return mapFromReservationToReservationResponseDTO(savedReservation);

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
