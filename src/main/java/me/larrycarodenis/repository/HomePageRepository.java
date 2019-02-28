package me.larrycarodenis.repository;

import me.larrycarodenis.domain.HomePage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the HomePage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HomePageRepository extends JpaRepository<HomePage, Long> {

}
