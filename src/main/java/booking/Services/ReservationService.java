package booking.Services;

import booking.Dtos.ReservationRequestDTO;
import booking.Entities.Reservation;
import booking.Entities.Room;
import booking.Repositories.ReservationRepository;
import booking.Repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {

    RoomRepository roomRepository;
    ReservationRepository reservationRepository;


    @Autowired
    public ReservationService(RoomRepository roomRepository, ReservationRepository reservationRepository) {
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
    }



    public void cancelReservation(Long id){
      reservationRepository.deleteById(id);
    }
}



