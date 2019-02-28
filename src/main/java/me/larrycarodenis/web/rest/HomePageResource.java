package me.larrycarodenis.web.rest;
import me.larrycarodenis.domain.HomePage;
import me.larrycarodenis.repository.HomePageRepository;
import me.larrycarodenis.web.rest.errors.BadRequestAlertException;
import me.larrycarodenis.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing HomePage.
 */
@RestController
@RequestMapping("/api")
public class HomePageResource {

    private final Logger log = LoggerFactory.getLogger(HomePageResource.class);

    private static final String ENTITY_NAME = "homePage";

    private final HomePageRepository homePageRepository;

    public HomePageResource(HomePageRepository homePageRepository) {
        this.homePageRepository = homePageRepository;
    }

    /**
     * POST  /home-pages : Create a new homePage.
     *
     * @param homePage the homePage to create
     * @return the ResponseEntity with status 201 (Created) and with body the new homePage, or with status 400 (Bad Request) if the homePage has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/home-pages")
    public ResponseEntity<HomePage> createHomePage(@Valid @RequestBody HomePage homePage) throws URISyntaxException {
        log.debug("REST request to save HomePage : {}", homePage);
        if (homePage.getId() != null) {
            throw new BadRequestAlertException("A new homePage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HomePage result = homePageRepository.save(homePage);
        return ResponseEntity.created(new URI("/api/home-pages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /home-pages : Updates an existing homePage.
     *
     * @param homePage the homePage to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated homePage,
     * or with status 400 (Bad Request) if the homePage is not valid,
     * or with status 500 (Internal Server Error) if the homePage couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/home-pages")
    public ResponseEntity<HomePage> updateHomePage(@Valid @RequestBody HomePage homePage) throws URISyntaxException {
        log.debug("REST request to update HomePage : {}", homePage);
        if (homePage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        HomePage result = homePageRepository.save(homePage);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, homePage.getId().toString()))
            .body(result);
    }

    /**
     * GET  /home-pages : get all the homePages.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of homePages in body
     */
    @GetMapping("/home-pages")
    public List<HomePage> getAllHomePages() {
        log.debug("REST request to get all HomePages");
        return homePageRepository.findAll();
    }

    /**
     * GET  /home-pages/:id : get the "id" homePage.
     *
     * @param id the id of the homePage to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the homePage, or with status 404 (Not Found)
     */
    @GetMapping("/home-pages/{id}")
    public ResponseEntity<HomePage> getHomePage(@PathVariable Long id) {
        log.debug("REST request to get HomePage : {}", id);
        Optional<HomePage> homePage = homePageRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(homePage);
    }

    /**
     * DELETE  /home-pages/:id : delete the "id" homePage.
     *
     * @param id the id of the homePage to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/home-pages/{id}")
    public ResponseEntity<Void> deleteHomePage(@PathVariable Long id) {
        log.debug("REST request to delete HomePage : {}", id);
        homePageRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
