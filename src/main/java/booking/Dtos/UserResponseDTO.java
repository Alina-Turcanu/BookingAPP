package booking.Dtos;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class UserResponseDTO {

    private Long id;
    private String name;

    private String email;
}
