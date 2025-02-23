package booking.Dtos;

public class HotelRequestDTO {

    private String name;

    public HotelRequestDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
