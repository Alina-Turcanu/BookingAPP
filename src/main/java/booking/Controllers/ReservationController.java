package booking.Controllers;

import booking.Services.ReservationService;
import booking.Services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    private ReservationService reservationService;

    private UserService userService;

    @Autowired
    public ReservationController(ReservationService reservationService, UserService userService) {
        this.reservationService = reservationService;
        this.userService = userService;
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
}
