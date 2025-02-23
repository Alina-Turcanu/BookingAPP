package booking.Repositories;

import booking.Entities.HotelAdministrator;
import booking.Entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelAdministratorRepository extends JpaRepository<HotelAdministrator,Long> {

}
