package md.faf.volunteer.repository;
import md.faf.volunteer.domain.VolunteerRequest;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the VolunteerRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VolunteerRequestRepository extends JpaRepository<VolunteerRequest, Long>, JpaSpecificationExecutor<VolunteerRequest> {

}
