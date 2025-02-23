package booking.Entities;

import jakarta.persistence.Entity;


public enum RoomType {
    standardRoom(2),singleRoom(1),familyRoom(4),suiteRoom(6);

    private final int maxCapacity;

    RoomType(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }
}
