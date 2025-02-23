package booking.Services;

import booking.Dtos.HotelRequestDTO;
import booking.Dtos.RoomRequestDTO;
import booking.Dtos.RoomResponseDTO;
import booking.Entities.Hotel;
import booking.Entities.Reservation;
import booking.Entities.Room;
import booking.Entities.RoomType;
import booking.Repositories.HotelRepository;
import booking.Repositories.ReservationRepository;
import booking.Repositories.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;


@Service
public class HotelAdministratorService {

    private RoomRepository roomRepository;

    private HotelRepository hotelRepository;

    private ReservationRepository reservationRepository;


    @Autowired
    public HotelAdministratorService(RoomRepository roomRepository, HotelRepository hotelRepository, ReservationRepository reservationRepository) {
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public Hotel addHotel(HotelRequestDTO hotelRequestDTO) {
        Hotel hotel = new Hotel();
        hotel.setName(hotelRequestDTO.getName());
        return hotelRepository.save(hotel);
    }

    @Transactional
    public RoomResponseDTO addRoom(Long hotelId, RoomRequestDTO roomRequestDTO) {
        Map<RoomType, Integer> roomTypeCapacities = Map.of(
                RoomType.singleRoom, 1,
                RoomType.doubleRoom, 2,
                RoomType.suiteRoom, 6,
                RoomType.tweenRoom, 2,
                RoomType.familyRoom, 4
        );

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new EntityNotFoundException("Hotelul cu ID-ul " + hotelId + " nu a fost găsit."));

        Room room = new Room();
        RoomType roomType = RoomType.valueOf(roomRequestDTO.getRoomType().toString());
        room.setRoomType(roomType);
        room.setRoomNumber(roomRequestDTO.getRoomNumber());
        room.setPricePerNight(roomRequestDTO.getPricePerNight());
        room.setCapacity(roomRequestDTO.getCapacity());

        int maxCapacity = roomTypeCapacities.getOrDefault(roomType, Integer.MAX_VALUE);
        if (roomRequestDTO.getCapacity() > maxCapacity) {
            throw new IllegalArgumentException(
                    "Capacitatea camerei depășește limita pentru tipul de cameră " + roomType +
                            ". Capacitate maximă permisă: " + maxCapacity
            );
        }
        room.setHotel(hotel);
        Room savedRoom = roomRepository.save(room);
        return mapFromRoomToRoomResponseDTO(savedRoom);
    }

    @Transactional
    public RoomResponseDTO mapFromRoomToRoomResponseDTO(Room room) {
        RoomResponseDTO roomResponseDTO = new RoomResponseDTO();
        roomResponseDTO.setId(room.getId());
        roomResponseDTO.setHotelId(room.getHotel().getHotelId());
        roomResponseDTO.setRoomType(room.getRoomType());
        roomResponseDTO.setPricePerNight(room.getPricePerNight());
        roomResponseDTO.setCapacity(room.getCapacity());
        return roomResponseDTO;
    }

    @Transactional
    public void deleteRoom(Long roomId) {
        roomRepository.deleteById(roomId);

    }


    @Transactional
    public Room changePrice(Long roomId, double newPrice) throws Exception {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new Exception("Room not found"));
        room.setPricePerNight(newPrice);
        Room updatedRoom = roomRepository.save(room);
        return updatedRoom;

    }

    @Transactional
    public List<RoomResponseDTO> findAllBusyRoomsBetween(LocalDate startDate, LocalDate endDate) {
        List<Room> allRooms = roomRepository.findAll();
        List<RoomResponseDTO> busyRooms = new ArrayList<>();
        for (Room room : allRooms) {
            boolean isBusy = room.getReservations().stream()
                    .anyMatch(reservation ->
                            !(reservation.getEndDate().isBefore(startDate) || reservation.getStartDate().isAfter(endDate)));
            if (isBusy) {
                RoomResponseDTO roomResponseDTO = new RoomResponseDTO(room.getId(), room.getRoomType(), room.getPricePerNight(), room.getCapacity(), room.getHotel().getHotelId());
                busyRooms.add(roomResponseDTO);
            }
        }
        return busyRooms;
    }

    @Transactional
    public double getIncomeFromAllReservationsBetween(LocalDate firstDate, LocalDate lastDate) {
        if (firstDate.isAfter(lastDate)) {
            throw new IllegalArgumentException("First day must be before last day");
        }
        double totalIncome = 0;
        List<Reservation> reservations = reservationRepository.findAll();
        for (Reservation reservation : reservations) {
            LocalDate overlapStart = reservation.getStartDate().isAfter(firstDate) ? reservation.getStartDate() : firstDate;
            LocalDate overlapEnd = reservation.getEndDate().isBefore(lastDate) ? reservation.getEndDate() : lastDate;

            if (!overlapStart.isAfter(overlapEnd)) {
                long overlapDays = DAYS.between(overlapStart, overlapEnd) + 1;
                double pricePerNight = reservation.getRoom().getPricePerNight();


                totalIncome += overlapDays * pricePerNight;
            }
        }
        return totalIncome;
    }
}

