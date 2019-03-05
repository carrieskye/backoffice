package me.larrycarodenis.web.rest;
import me.larrycarodenis.domain.Classification;
import me.larrycarodenis.domain.Device;
import me.larrycarodenis.domain.Ip;
import me.larrycarodenis.repository.ClassificationRepository;
import me.larrycarodenis.repository.DeviceRepository;
import me.larrycarodenis.repository.IpRepository;
import me.larrycarodenis.web.rest.errors.BadRequestAlertException;
import me.larrycarodenis.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.omg.CORBA.ServerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.*;

/**
 * REST controller for managing Device.
 */
@RestController
@RequestMapping("/api")
public class DeviceResource {

    private final Logger log = LoggerFactory.getLogger(DeviceResource.class);

    private static final String ENTITY_NAME = "device";

    private final DeviceRepository deviceRepository;

    private final ClassificationRepository classificationReposistory;

    private IpRepository ipRepository = new IpRepository();

    public DeviceResource(DeviceRepository deviceRepository, ClassificationRepository classificationReposistory) {
        this.deviceRepository = deviceRepository;
        this.classificationReposistory = classificationReposistory;
    }

    /**
     * POST  /devices : Create a new device.
     *
     * @param device the device to create
     * @return the ResponseEntity with status 201 (Created) and with body the new device, or with status 400 (Bad Request) if the device has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/devices")
    public ResponseEntity<Device> createDevice(@Valid @RequestBody Device device) throws URISyntaxException {
        log.debug("REST request to save Device : {}", device);
        if (device.getId() != null) {
            throw new BadRequestAlertException("A new device cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Device result = deviceRepository.save(device);
        return ResponseEntity.created(new URI("/api/devices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/devices/{id}/export")
    public ResponseEntity<Classification> saveClassification(HttpServletRequest request, @PathVariable Long id, @RequestBody ArrayList<Classification> data)
    {
        System.out.println(request.getRemoteAddr());
        ipRepository.save(new Ip(request.getRemoteHost(), id));


        for(Classification classification : data)
        {
            Device device = deviceRepository.findById(id).get();
            classification.setDevice(device);
            System.out.println("classification:");
            System.out.println(classification.toString());
            classificationReposistory.save(classification);

        }
        return null;
    }

    @GetMapping("/devices/ip")
    public Map<Long, String> getIp()
    {
        System.out.println("ik ben hier");
        Map<Long, String> ipMap = new HashMap<>();
        List<Ip> ips = ipRepository.findAll();
        System.out.println(ips);
        for(Ip ip : ips)
        {

            ipMap.put(ip.getDeviceId(), ip.getIp());
        }
        return ipMap;
    }

    /**
     * PUT  /devices : Updates an existing device.
     *
     * @param device the device to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated device,
     * or with status 400 (Bad Request) if the device is not valid,
     * or with status 500 (Internal Server Error) if the device couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/devices")
    public ResponseEntity<Device> updateDevice(@Valid @RequestBody Device device) throws URISyntaxException {
        log.debug("REST request to update Device : {}", device);
        if (device.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Device result = deviceRepository.save(device);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, device.getId().toString()))
            .body(result);
    }

    /**
     * GET  /devices : get all the devices.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of devices in body
     */
    @GetMapping("/devices")
    public List<Device> getAllDevices() {
        log.debug("REST request to get all Devices");
        return deviceRepository.findAll();
    }

    /**
     * GET  /devices/:id : get the "id" device.
     *
     * @param id the id of the device to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the device, or with status 404 (Not Found)
     */
    @GetMapping("/devices/{id}")
    public ResponseEntity<Device> getDevice(@PathVariable Long id) {
        log.debug("REST request to get Device : {}", id);
        Optional<Device> device = deviceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(device);
    }

    /**
     * DELETE  /devices/:id : delete the "id" device.
     *
     * @param id the id of the device to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/devices/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
        log.debug("REST request to delete Device : {}", id);
        deviceRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
