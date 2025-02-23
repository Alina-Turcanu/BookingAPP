package booking.Entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity

public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate endDate;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @ManyToOne
    @JoinColumn(name = "hotelAdministrator")
    private HotelAdministrator hotelAdministrator;


    public Reservation() {
    }

    public Reservation(long reservationId, Room room, LocalDate startDate, LocalDate endDate, User user, Hotel hotel, HotelAdministrator hotelAdministrator) {
        this.reservationId = reservationId;
        this.room = room;
        this.startDate = startDate;
        this.endDate = endDate;
        this.user = user;
        this.hotel = hotel;
        this.hotelAdministrator = hotelAdministrator;
    }

    public long getReservationId() {
        return reservationId;
    }

    public void setReservationId(long reservationId) {
        this.reservationId = reservationId;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public HotelAdministrator getHotelAdministrator() {
        return hotelAdministrator;
    }

    public void setHotelAdministrator(HotelAdministrator hotelAdministrator) {
        this.hotelAdministrator = hotelAdministrator;
    }
}

