package me.larrycarodenis.repository;

import me.larrycarodenis.domain.Classification;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Classification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClassificationRepository extends JpaRepository<Classification, Long> {

    public List<Classification> getAllByDevice_Id(long id);

}
