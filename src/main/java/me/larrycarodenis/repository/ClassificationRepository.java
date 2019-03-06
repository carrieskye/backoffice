package me.larrycarodenis.repository;

import me.larrycarodenis.domain.Classification;
import me.larrycarodenis.domain.enumeration.Gender;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Classification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClassificationRepository extends JpaRepository<Classification, Long>, ClassificationRepositoryCustom {

    List<Classification> getAllByDevice_Id(long id);
    Long countByGender(Gender gender);
    Classification findTopByOrderByIdDesc();
    List<Classification> findAll();

    @Query(value = "select avg(c.age) from Classification c")
    double averageAge();

}
