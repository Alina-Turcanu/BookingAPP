package booking.Dtos;

import booking.Entities.RoomType;

public class RoomRequestDTO {

   private int roomNumber;
   private String roomType;

   private int pricePerNight;

   private int capacity;
  

   public RoomRequestDTO(int roomNumber,String roomType, int pricePerNight, int capacity) {
     this.roomNumber=roomNumber;
      this.roomType = roomType;
      this.pricePerNight = pricePerNight;
      this.capacity = capacity;
     // this.hotelId = hotelId;
   }

   public int getRoomNumber() {
      return roomNumber;
   }

   public void setRoomNumber(int roomNumber) {
      this.roomNumber = roomNumber;
   }

   public String getRoomType() {
      return roomType;
   }

   public void setRoomType(String roomType) {
      this.roomType = roomType;
   }

   public int getPricePerNight() {
      return pricePerNight;
   }

   public void setPricePerNight(int pricePerNight) {
      this.pricePerNight = pricePerNight;
   }

   public int getCapacity() {
      return capacity;
   }

   public void setCapacity(int capacity) {
      this.capacity = capacity;
   }

//   public Long getHotelId() {
//      return hotelId;
//   }
//
//   public void setHotel(Hotel hotel) {
//      this.hotelId= hotelId;
//   }
}
