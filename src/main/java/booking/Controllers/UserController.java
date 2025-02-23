package booking.Controllers;


import booking.Dtos.*;
import booking.Entities.Reservation;
import booking.Entities.RoomType;
import booking.Entities.User;
import booking.Services.ReservationService;
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
    ReservationService reservationService;

    @Autowired
    public UserController(UserService userService, ReservationService reservationService) {
        this.userService = userService;
        this.reservationService = reservationService;
    }

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

    @PostMapping("/bookRoomByRoomType")
    public ResponseEntity<ReservationResponseDTObyRoomType> bookRoomByType(@RequestParam RoomType roomType, @RequestParam LocalDate firstDay, @RequestParam LocalDate lastDay, @RequestParam Long userId, @RequestParam Long hotelId) {

        try {
            // Apelăm serviciul pentru a rezerva o cameră
            ReservationResponseDTObyRoomType reservationResponseDTO = userService.bookRoomByType(roomType, firstDay, lastDay, userId, hotelId);

            // Dacă rezervarea a fost realizată cu succes, returnăm un răspuns 201 Created
            return ResponseEntity.status(HttpStatus.CREATED).body(reservationResponseDTO);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
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

    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelReservation(@PathVariable Long id) {
        try {
            reservationService.cancelReservation(id);
            return ResponseEntity.ok("Rezervarea cu id-ul " + id + " a fost ștearsă cu succes.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rezervarea cu ID-ul " + id + " nu a fost găsită.");

        }
    }

    @GetMapping("/findLoggedInUser")
   public ResponseEntity<UserResponseDTO> findLoggedInUser(){
        UserResponseDTO user= userService.findLoggedInUser();
        return ResponseEntity.ok(user);
    }
}

