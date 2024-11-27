package fr.insa.soa.missionService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import fr.insa.soa.missionService.model.Mission;

import java.util.List;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Integer> {

    // Custom method to find missions by status
    List<Mission> findByStatus(int status);

    // Alternatively, use a JPQL query
    @Query("SELECT m FROM Mission m WHERE m.status = :status")
    List<Mission> findMissionsByStatus(@Param("status") int status);
}
