package booking.Dtos;

import booking.Entities.RoomType;

import java.time.LocalDate;

public class ReservationResponseDTObyRoomType {

    private Long id;

    private LocalDate startDate;

    private LocalDate endDate;

    private Long userId;
    private RoomType roomType;

    private Long hotelId;

    public ReservationResponseDTObyRoomType(){
    }

    public ReservationResponseDTObyRoomType(Long id, LocalDate startDate, LocalDate endDate, Long userId,RoomType roomType,Long hotelId) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userId = userId;
        this.roomType= roomType;
        this.hotelId=hotelId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }
}
