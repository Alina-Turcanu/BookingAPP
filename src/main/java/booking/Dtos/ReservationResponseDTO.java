package booking.Dtos;

import booking.Entities.Room;
import booking.Entities.RoomType;

import java.time.LocalDate;

public class ReservationResponseDTO {

    private Long id;
    private Long roomId;

    private Long hotelId;

    private LocalDate startDate;

    private LocalDate endDate;

    private Long userId;
    public ReservationResponseDTO(){}

    public ReservationResponseDTO(Long id, Long roomId, Long hotelId, LocalDate startDate, LocalDate endDate, Long userId) {
        this.id = id;
        this.roomId = roomId;
        this.hotelId = hotelId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
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


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
