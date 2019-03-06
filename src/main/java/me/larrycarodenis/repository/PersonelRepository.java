package me.larrycarodenis.repository;

import me.larrycarodenis.domain.Personel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Personel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonelRepository extends JpaRepository<Personel, Long> {

}
