package ua.foxminded.yakovlev.university.jpaDao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.foxminded.yakovlev.university.entity.Position;

public interface PositionRepository extends JpaRepository<Position, Long>{
	
	@Query("select p from Position p where p.name = ?1")
    Position findPositionByName(String name);
}