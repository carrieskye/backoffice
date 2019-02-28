package me.larrycarodenis.web.rest;
import me.larrycarodenis.domain.Slide;
import me.larrycarodenis.repository.SlideRepository;
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
 * REST controller for managing Slide.
 */
@RestController
@RequestMapping("/api")
public class SlideResource {

    private final Logger log = LoggerFactory.getLogger(SlideResource.class);

    private static final String ENTITY_NAME = "slide";

    private final SlideRepository slideRepository;

    public SlideResource(SlideRepository slideRepository) {
        this.slideRepository = slideRepository;
    }

    /**
     * POST  /slides : Create a new slide.
     *
     * @param slide the slide to create
     * @return the ResponseEntity with status 201 (Created) and with body the new slide, or with status 400 (Bad Request) if the slide has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/slides")
    public ResponseEntity<Slide> createSlide(@Valid @RequestBody Slide slide) throws URISyntaxException {
        log.debug("REST request to save Slide : {}", slide);
        if (slide.getId() != null) {
            throw new BadRequestAlertException("A new slide cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Slide result = slideRepository.save(slide);
        return ResponseEntity.created(new URI("/api/slides/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /slides : Updates an existing slide.
     *
     * @param slide the slide to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated slide,
     * or with status 400 (Bad Request) if the slide is not valid,
     * or with status 500 (Internal Server Error) if the slide couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/slides")
    public ResponseEntity<Slide> updateSlide(@Valid @RequestBody Slide slide) throws URISyntaxException {
        log.debug("REST request to update Slide : {}", slide);
        if (slide.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Slide result = slideRepository.save(slide);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, slide.getId().toString()))
            .body(result);
    }

    /**
     * GET  /slides : get all the slides.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of slides in body
     */
    @GetMapping("/slides")
    public List<Slide> getAllSlides(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Slides");
        return slideRepository.findAllWithEagerRelationships();
    }

    /**
     * GET  /slides/:id : get the "id" slide.
     *
     * @param id the id of the slide to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the slide, or with status 404 (Not Found)
     */
    @GetMapping("/slides/{id}")
    public ResponseEntity<Slide> getSlide(@PathVariable Long id) {
        log.debug("REST request to get Slide : {}", id);
        Optional<Slide> slide = slideRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(slide);
    }

    /**
     * DELETE  /slides/:id : delete the "id" slide.
     *
     * @param id the id of the slide to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/slides/{id}")
    public ResponseEntity<Void> deleteSlide(@PathVariable Long id) {
        log.debug("REST request to delete Slide : {}", id);
        slideRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
