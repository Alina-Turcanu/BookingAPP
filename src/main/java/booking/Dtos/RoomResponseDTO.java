package booking.Dtos;

import booking.Entities.Reservation;
import booking.Entities.RoomType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.List;

public class RoomResponseDTO {


    private Long id;

    private RoomType roomType;

    private double pricePerNight;

    private int capacity;


    private Long hotelId;

    public RoomResponseDTO(){}

    public RoomResponseDTO(Long id, RoomType roomType, double pricePerNight, int capacity, Long hotelId){
        this.id = id;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.capacity = capacity;
        this.hotelId = hotelId;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }


}
