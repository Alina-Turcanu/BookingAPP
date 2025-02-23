package booking.Services;


import booking.Dtos.AvailableRoomDTO;
import booking.Dtos.RoomResponseDTO;
import booking.Entities.Room;
import booking.Repositories.RoomRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {

    RoomRepository roomRepository;

    ReservationService reservationService;


    @Autowired
    public RoomService(RoomRepository roomRepository, ReservationService reservationService) {
        this.roomRepository = roomRepository;
        this.reservationService = reservationService;
    }


    @Transactional
    public List<RoomResponseDTO> findAll() {
        return roomRepository.findAll().stream()
                .map(room -> mapFromRoomToRoomResponseDTO(room))
                .collect(Collectors.toList());
    }

    public RoomResponseDTO mapFromRoomToRoomResponseDTO(Room room) {
        RoomResponseDTO roomResponseDTO = new RoomResponseDTO();
        roomResponseDTO.setRoomType(room.getRoomType());
        roomResponseDTO.setCapacity(room.getCapacity());
        roomResponseDTO.setPricePerNight(room.getPricePerNight());
        roomResponseDTO.setHotelId(room.getHotel().getHotelId());
        roomResponseDTO.setId(room.getId());

        return roomResponseDTO;

    }

    public List<RoomResponseDTO> getAvailableRooms(LocalDate startDate, LocalDate endDate) {
        List<Room> allRooms = roomRepository.findAll();
        List<RoomResponseDTO> availableRooms = new ArrayList<>();
        for (Room room : allRooms) {
            boolean isAvailable = room.getReservations().stream()
                    .noneMatch(reservation ->
                            (reservation.getStartDate().isBefore(endDate)
                                    && reservation.getEndDate().isAfter(startDate)));
            if (isAvailable) {
                RoomResponseDTO roomResponseDTO = mapFromRoomToRoomResponseDTO(room);
                availableRooms.add(roomResponseDTO);
            }
        }
        return availableRooms;
    }

    //Sortarea disponibilităților (camere libere) după preț pentru o anumită perioadă și un anumit număr de locuri.
    @Transactional
    public List<Room> sortAvailableRoomsByPriceAndNumberOfPeople(AvailableRoomDTO availableRoomDTO){
        List<Room> availableRooms = roomRepository.findAvailableRoomsSortedByPriceAndNumberOfPeople(availableRoomDTO.getStartDate(),availableRoomDTO.getEndDate(),availableRoomDTO.getPricePerNight(),availableRoomDTO.getCapacity());
        List<Room> sortedRooms = availableRooms.stream()
                .filter(room -> room.getPricePerNight() > 0 && room.getCapacity() >=availableRoomDTO.getCapacity())
                .sorted(Comparator.comparingDouble(Room::getPricePerNight))
                .collect(Collectors.toList());

        return sortedRooms;
    }

    //Vizualizarea disponibilităților (camere libere) pentru o anumită perioadă și un anumit număr de locuri.
    @Transactional
    public List<Room> findAvailableRoomsByCapacity(LocalDate startDate, LocalDate endDate, int numberOfPersons) {
        List<Room> allRooms = roomRepository.findAll();
        List<Room> availableRooms = new ArrayList<>();
        for (Room room : allRooms) {
            boolean isAvailable = room.getReservations().stream()
                    .noneMatch(reservation ->
                            (reservation.getStartDate().isBefore(endDate)
                                    && reservation.getEndDate().isAfter(startDate)));
            if (isAvailable && room.getCapacity() >= numberOfPersons) {
                availableRooms.add(room);
            }
        }
        return availableRooms;
    }
}


