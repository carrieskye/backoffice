package me.larrycarodenis.repository;

import me.larrycarodenis.domain.Slide;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Slide entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SlideRepository extends JpaRepository<Slide, Long> {

    @Query(value = "select distinct slide from Slide slide left join fetch slide.devices",
        countQuery = "select count(distinct slide) from Slide slide")
    Page<Slide> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct slide from Slide slide left join fetch slide.devices")
    List<Slide> findAllWithEagerRelationships();

    @Query("select slide from Slide slide left join fetch slide.devices where slide.id =:id")
    Optional<Slide> findOneWithEagerRelationships(@Param("id") Long id);

}
