package booking.Controllers;


import booking.Dtos.ReservationRequestDTO;
import booking.Entities.Reservation;
import booking.Entities.Room;
import booking.Entities.RoomType;
import booking.Services.HotelService;
import booking.Services.RoomService;
import booking.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/hotel")
public class HotelController {

    private HotelService hotelService;

    private RoomService roomService;

    private UserService userService;

    @Autowired
    public HotelController(HotelService hotelService, RoomService roomService,UserService userService) {
        this.hotelService = hotelService;
        this.roomService = roomService;
        this.userService=userService;
    }





}

