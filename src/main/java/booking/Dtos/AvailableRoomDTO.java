package booking.Dtos;

import java.time.LocalDate;

public class AvailableRoomDTO {

    private LocalDate startDate;

    private LocalDate endDate;

    private double pricePerNight;

    private int capacity;

    public AvailableRoomDTO(LocalDate startDate, LocalDate endDate, double pricePerNight, int capacity) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.pricePerNight = pricePerNight;
        this.capacity = capacity;
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
}
