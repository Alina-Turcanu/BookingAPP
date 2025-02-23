package booking.Controllers;

import booking.Dtos.HotelRequestDTO;
import booking.Dtos.RoomRequestDTO;
import booking.Dtos.RoomResponseDTO;
import booking.Entities.Hotel;
import booking.Entities.Room;
import booking.Services.HotelAdministratorService;
import booking.Services.RoomService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/hotelAdministrator")
public class HotelAdministratorController {

    private HotelAdministratorService hotelAdministratorService;
    private RoomService roomService;


    @Autowired
    public HotelAdministratorController(HotelAdministratorService hotelAdministratorService, RoomService roomService) {
        this.hotelAdministratorService = hotelAdministratorService;
        this.roomService = roomService;
    }

    @PostMapping("/addHotel")
    public ResponseEntity<?> addHotel(@RequestBody HotelRequestDTO hotelRequestDTO) {
        try {
            Hotel hotel = hotelAdministratorService.addHotel(hotelRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(hotel);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("hotel");
        }
    }

    @PostMapping("/{hotelId}/addRoom")
    public ResponseEntity<RoomResponseDTO> addRoom(@PathVariable Long hotelId, @RequestBody RoomRequestDTO roomRequestDTO) {
        RoomResponseDTO room = hotelAdministratorService.addRoom(hotelId, roomRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(room);
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<String> deleteRoom(@PathVariable Long roomId) {
        try {
            hotelAdministratorService.deleteRoom(roomId);
            return ResponseEntity.ok("Camera cu ID-ul " + roomId + " a fost ștearsă cu succes.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Camera cu ID-ul " + roomId + " nu a fost găsită.");
        }
    }


    @PutMapping("/{roomId}/newPrice")
    public ResponseEntity<?> changePrice(@PathVariable Long roomId, @RequestParam double newPrice) {
        try {
            Room room = hotelAdministratorService.changePrice(roomId, newPrice);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Pretul a fost schimbat");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Pretul nu a putut fi schimbat");
        }
    }

    @GetMapping("/findAllRooms")
    public ResponseEntity<List<RoomResponseDTO>> findAllRooms() {
        List<RoomResponseDTO> rooms = roomService.findAll();
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/findAllBusyRooms")
    public List<RoomResponseDTO> findAllBusyRoomsBetween(@RequestParam LocalDate startDate,@RequestParam LocalDate endDate){
        return hotelAdministratorService.findAllBusyRoomsBetween(startDate,endDate);
    }


    @GetMapping("/getIncome")
    public double getIncomeFromAllReservationsBetweenDates(@RequestParam LocalDate firstDate,@RequestParam LocalDate lastDate){
       return hotelAdministratorService.getIncomeFromAllReservationsBetween(firstDate,lastDate);
    }


}