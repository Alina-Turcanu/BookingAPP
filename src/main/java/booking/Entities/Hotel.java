package booking.Entities;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hotels")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hotelId;

    @Column
    private String name;


    @OneToMany(mappedBy = "hotel",cascade = CascadeType.ALL)
    private List<Room> rooms=new ArrayList<>();


    @OneToMany(mappedBy = "hotel")
    private List<Reservation>reservations=new ArrayList<>();


    @ManyToOne
    @JoinColumn(name = "hotelAdministrator")
    private HotelAdministrator hotelAdministrator;


    public Hotel() {
    }

    public Hotel(Long hotelId, String name, List<Room> rooms, HotelAdministrator hotelAdministrator) {
        this.hotelId = hotelId;
        this.name = name;
        this.rooms = rooms;
        this.hotelAdministrator = hotelAdministrator;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public HotelAdministrator getHotelAdministrator() {
        return hotelAdministrator;
    }

    public void setHotelAdministrator(HotelAdministrator hotelAdministrator) {
        this.hotelAdministrator = hotelAdministrator;
    }
}
