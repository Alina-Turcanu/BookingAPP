package booking.Entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
public class HotelAdministrator {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @OneToMany(mappedBy = "hotelAdministrator")
    private List<Reservation> reservatios;

    @OneToMany(mappedBy = "hotelAdministrator", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Hotel> hotels = new ArrayList<>();


    public HotelAdministrator() {
    }

    public HotelAdministrator(Long id, String firstName, String lastName, List<Reservation> reservatios, List<Hotel> hotels) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.reservatios = reservatios;
        this.hotels = hotels;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Reservation> getReservatios() {
        return reservatios;
    }

    public void setReservatios(List<Reservation> reservatios) {
        this.reservatios = reservatios;
    }

    public List<Hotel> getHotels() {
        return hotels;
    }

    public void setHotels(List<Hotel> hotels) {
        this.hotels = hotels;
    }
}


