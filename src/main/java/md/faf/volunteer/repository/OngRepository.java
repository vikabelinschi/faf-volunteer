package md.faf.volunteer.repository;
import md.faf.volunteer.domain.Ong;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Ong entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OngRepository extends JpaRepository<Ong, Long>, JpaSpecificationExecutor<Ong> {

}
