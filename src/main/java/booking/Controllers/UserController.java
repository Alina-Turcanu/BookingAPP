package booking.Controllers;


import booking.Dtos.ReservationRequestDTO;
import booking.Dtos.ReservationResponseDTO;
import booking.Dtos.ReservationResponseDTObyRoomType;
import booking.Dtos.UserRequestDTO;
import booking.Entities.Reservation;
import booking.Entities.RoomType;
import booking.Entities.User;
import booking.Services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/addUser")
    public ResponseEntity<?> addUser(@RequestBody UserRequestDTO userRequestDTO) {
        try {
            User user = userService.addUser(userRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (Exception e) {
            // Logăm excepția
            e.printStackTrace();
            // Returnăm un răspuns de eroare
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("A apărut o eroare: " + e.getMessage());
        }
    }

//    public List<Room> sortAvailableRoomsByPriceAndNumberOfPeople(LocalDate startDay, LocalDate endDay, double pricePerNight, int capacity) {
//        return userService.sortAvailableRoomsByPriceAndNumberOfPeople(startDay, endDay, pricePerNight, capacity);
//    }

//   @PostMapping("/createReservation")
//   public ResponseEntity<Reservation> makeReservationByRoomType(@RequestBody ReservationRequestDTO reservationRequestDTO){
//        Reservation reservation=userService.makeReservationByRoomType(reservationRequestDTO);
//        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
//   }

    @PostMapping("/bookRoom")
    public ResponseEntity<ReservationResponseDTO> bookRoom(@RequestBody ReservationRequestDTO reservationRequestDTO) {
        ReservationResponseDTO reservation = userService.bookRoom(reservationRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }

    @GetMapping("/allUsers")
    public List<User> findAll() {
        return userService.findAllUsers();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        // userService.deleteUser(userId);
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok("Userul cu ID-ul " + userId + " a fost ștears cu succes.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Userul cu ID-ul " + userId + " nu a fost găsit.");
        }
    }

//
//    @GetMapping("/sortRooms/startDate/endDate/pricePerNight/capacity")
//    public List<Room> sortAvailableRooms(@RequestParam LocalDate startDate,@RequestParam LocalDate endDate,@RequestParam double pricePerNight,@RequestParam int capacity) {
//        return userService.sortAvailableRoomsByPriceAndNumberOfPeople(startDate,endDate,pricePerNight,capacity);
//    }

//    @GetMapping("/availableRooms")
//    public ResponseEntity<List<Room>> getAvailableRooms(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate,@RequestParam int numberOfPersons) {
//        List<Room> availableRooms =userService.sortAvailableRoomsByPriceAndNumberOfPeople(startDate,endDate,numberOfPersons);
//        return ResponseEntity.ok(availableRooms);
//    }

    @PostMapping("/bookRoomByRoomType")
    public ResponseEntity<ReservationResponseDTObyRoomType> bookRoomByType(@RequestParam RoomType roomType,@RequestParam LocalDate firstDay,@RequestParam LocalDate lastDay,@RequestParam Long userId,@RequestParam Long hotelId) {

        try {
            // Apelăm serviciul pentru a rezerva o cameră
            ReservationResponseDTObyRoomType reservationResponseDTO = userService.bookRoomByType(roomType, firstDay, lastDay, userId, hotelId);

            // Dacă rezervarea a fost realizată cu succes, returnăm un răspuns 201 Created
            return ResponseEntity.status(HttpStatus.CREATED).body(reservationResponseDTO);

        } catch (IllegalArgumentException e) {
            // Dacă nu au fost camere disponibile sau dacă a apărut o altă eroare de validare
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            // Dacă apare o eroare neașteptată
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/reservationsByUser")
    public ResponseEntity<?> findAllReservationsByUser(@RequestParam Long userId) {
        try {
            List<ReservationResponseDTO> reservations = userService.findAllReservationsByUser(userId);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(reservations);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Nu exista rezervari pentru acest user");
        }
    }
}

