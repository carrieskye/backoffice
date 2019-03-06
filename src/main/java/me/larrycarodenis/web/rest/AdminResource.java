package me.larrycarodenis.web.rest;

import me.larrycarodenis.domain.Classification;
import me.larrycarodenis.domain.Metrics;
import me.larrycarodenis.domain.enumeration.Gender;
import me.larrycarodenis.repository.ClassificationRepository;
import me.larrycarodenis.repository.DeviceRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.List;

@RestController()
@RequestMapping("/api/admin")
public class AdminResource {

    private ClassificationRepository classificationRepository;
    private DeviceRepository deviceRepository;

    public AdminResource(ClassificationRepository classificationRepository, DeviceRepository deviceRepository)
    {
        this.classificationRepository = classificationRepository;
        this.deviceRepository = deviceRepository;
    }


    @GetMapping("/metrics")
    public Metrics getMetrics()
    {
        Metrics metrics = new Metrics();

        // Last classification
        metrics.setLastClassification(classificationRepository.findTopByOrderByIdDesc());

        // counts
        int numberOfClassifications = (int)classificationRepository.count();
        int numberOfDevices = (int)deviceRepository.count();
        metrics.setNumberOfClassifications(numberOfClassifications);
        metrics.setNumberOfDevices(numberOfDevices);

        // ratio female/total
        long females = classificationRepository.countByGender(Gender.FEMALE);
        long males = classificationRepository.countByGender(Gender.MALE);
        double ratio =  (double)females / (females + males);
        metrics.setFemaleRatio(ratio);

        // average age
        metrics.setAverageAge((int) classificationRepository.averageAge());

        // distinct customers
        long distinctCustomers = classificationRepository.findAll().stream()
            .filter(distinctByKey(classification -> classification.getPersonId() + classification.getDevice().getId()))
            .collect(Collectors.counting());
        metrics.setDistinctCustomers((int)distinctCustomers);

        return metrics;
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor)
    {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}

