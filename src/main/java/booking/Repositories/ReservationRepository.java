package booking.Repositories;

import booking.Entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Long> {
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
            "FROM Reservation r WHERE r.room.id = :roomId " +
            "AND ((:startDate > r.startDate AND :startDate < r.endDate) " +
            "OR (:endDate > r.startDate AND :endDate < r.endDate) " +
            "OR (r.startDate >= :startDate AND r.startDate < :endDate))")
    boolean existsByRoomAndDateOverlap(@Param("roomId") Long roomId,
                                       @Param("startDate") LocalDate startDate,
                                       @Param("endDate") LocalDate endDate);
}
