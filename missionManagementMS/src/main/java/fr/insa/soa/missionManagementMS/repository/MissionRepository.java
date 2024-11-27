package fr.insa.soa.missionManagementMS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import fr.insa.soa.missionManagementMS.model.Mission;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Integer> {
}
