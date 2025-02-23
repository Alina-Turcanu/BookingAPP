package booking.Repositories;

import booking.Dtos.RoomResponseDTO;
import booking.Entities.Hotel;
import booking.Entities.Room;
import booking.Entities.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    // List<Room> findAvailableRoomsSortedByPriceAndNumberOfPeople(LocalDate startDay,LocalDate endDay,int capacity,double pricePerNight);
    @Query("SELECT r FROM Room r WHERE r NOT IN " +
            "(SELECT res.room FROM Reservation res WHERE " +
            "(res.startDate <= :endDate AND res.endDate >= :startDate)) " +
            "AND r.capacity >= :capacity AND r.pricePerNight >= :pricePerNight " +
            "ORDER BY r.pricePerNight ASC")
    List<Room> findAvailableRoomsSortedByPriceAndNumberOfPeople(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("pricePerNight") double pricePerNight,
            @Param("capacity") int capacity
           );

    List<Room> findByRoomType( RoomType roomType);


}

