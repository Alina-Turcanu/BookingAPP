package booking.Controllers;

import booking.Dtos.AvailableRoomDTO;
import booking.Dtos.RoomResponseDTO;
import booking.Entities.Room;
import booking.Services.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/rooms")
public class RoomController {

    private RoomService roomService;


    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }


    @GetMapping("/findAllRooms")
    public ResponseEntity<?> findAllRooms() {
        try {
            List<RoomResponseDTO> allRooms = roomService.findAll();
            return ResponseEntity.ok(allRooms);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nu exista camere");
        }
    }

    @GetMapping("/availableRooms")
    public ResponseEntity<?> getAllAvailableRooms(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        try {
            List<RoomResponseDTO> availableRooms = roomService.getAvailableRooms(startDate, endDate);
            return ResponseEntity.ok(availableRooms); // va returna lista de RoomResponseDTO corect serializată
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nu există camere disponibile în perioada aleasă");
        }
    }

    @GetMapping("/availableRoomsBetweenDates")
    public ResponseEntity<?> findAvailableRoomsBetweenDates(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        try {
            List<RoomResponseDTO> availableRooms = roomService.getAvailableRooms(startDate, endDate);
            return ResponseEntity.ok(availableRooms);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nu exista camere disponibile in perioada aleasa");
        }
    }

    @GetMapping("/available/sort")
    public ResponseEntity<List<Room>> getSortedAvailableRooms(@RequestBody AvailableRoomDTO availableRoomDTO) {
        try {
            List<Room> sortedRooms = roomService.sortAvailableRoomsByPriceAndNumberOfPeople(availableRoomDTO);
            return ResponseEntity.ok(sortedRooms);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/availableRooms/capacity")
    public ResponseEntity<?> findAvailableRoomsByCapacity(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate, @RequestParam int numberOfPersons) {
        try {
            List<Room> availableRooms = roomService.findAvailableRoomsByCapacity(startDate, endDate, numberOfPersons);
            return ResponseEntity.ok(availableRooms);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nu exista camere disponibile cu numarul de persoane dorit");
        }
    }
}