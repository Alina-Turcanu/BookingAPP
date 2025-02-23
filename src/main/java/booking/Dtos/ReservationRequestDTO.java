package booking.Dtos;

import booking.Entities.Hotel;
import booking.Entities.Room;
import booking.Entities.RoomType;
import booking.Entities.User;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;

public class ReservationRequestDTO {

    private Long roomId;

    private Long userId;

    private LocalDate startDate;

    private LocalDate endDate;




    public ReservationRequestDTO(Long hotelId,Long roomId, Long userId, LocalDate startDate, LocalDate endDate) {
        this.roomId = roomId;
        this.userId = userId;
        this.startDate = startDate;
        this.endDate = endDate;
    }



    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

}

