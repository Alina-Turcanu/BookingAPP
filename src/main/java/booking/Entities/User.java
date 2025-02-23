package booking.Entities;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String username;

    @Column
    private String email;

private String password;

    @OneToMany(mappedBy = "user")
    private List<Reservation> reservations = new ArrayList<>();


    @ManyToMany
    @JoinTable(
            name="user_role",
            joinColumns= @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id")
    )

    private List<Role> roles;

    public List<Role> getRoles() {
        if(this.roles==null){
            roles = new ArrayList<>();
        }
        return roles;
    }

    public User() {
    }

    public User(Long id, String username, String email,String password, List<Reservation> reservations) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password=password;
        this.reservations = reservations;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
