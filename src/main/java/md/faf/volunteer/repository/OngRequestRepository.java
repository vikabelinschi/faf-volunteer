package md.faf.volunteer.repository;
import md.faf.volunteer.domain.OngRequest;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the OngRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OngRequestRepository extends JpaRepository<OngRequest, Long>, JpaSpecificationExecutor<OngRequest> {

}
