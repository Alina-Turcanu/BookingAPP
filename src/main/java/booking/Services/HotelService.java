package booking.Services;

import booking.Dtos.HotelRequestDTO;
import booking.Entities.Hotel;
import booking.Entities.Room;
import booking.Repositories.HotelRepository;
import booking.Repositories.RoomRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelService {

    private HotelRepository hotelRepository;

    private RoomRepository roomRepository;

    @Autowired
    public HotelService(HotelRepository hotelRepository,RoomRepository roomRepository) {
        this.hotelRepository = hotelRepository;
        this.roomRepository=roomRepository;
    }

    @Transactional
    public List<Room> findAllRoomsByHotelId(Long hotelId) throws Exception {
        //Varianta 1
//        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new Exception("Hotel not found"));
//        List<Room> rooms = hotel.getRooms();
//        return rooms;
        //Varianta 2
        return hotelRepository.findById(hotelId)
                .stream()
                .flatMap(hotel->hotel.getRooms().stream())
                .collect(Collectors.toList());
    }


}
