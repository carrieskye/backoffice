package me.larrycarodenis.web.rest;

import me.larrycarodenis.domain.Classification;
import me.larrycarodenis.domain.Ip;
import me.larrycarodenis.domain.Metrics;
import me.larrycarodenis.domain.enumeration.Gender;
import me.larrycarodenis.repository.ClassificationRepository;
import me.larrycarodenis.repository.DeviceRepository;
import me.larrycarodenis.repository.IpRepository;
import me.larrycarodenis.repository.PersonelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.List;

@RestController()
@RequestMapping("/api/admin")
public class AdminResource {

    @Autowired
    private ClassificationRepository classificationRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private PersonelRepository personelRepository;

    @GetMapping("/metrics")
    public Metrics getMetrics()
    {
        Metrics metrics = new Metrics();

        // get list of labels to ignore
        List<String> ignoredPersonal = personelRepository
            .getAllByIsIgnored(true)
            .stream().map(personel -> personel.getName()).collect(Collectors.toList());

        // Last classification
        metrics.setLastClassification(classificationRepository.findTopByOrderByIdDesc());

        // get all classifications
        List<Classification> classifications = classificationRepository.findAll().stream()
            .filter(classification -> !ignoredPersonal.contains(classification.getPersonId()))
            .collect(Collectors.toList());

        // counts
        int numberOfClassifications = classifications.size();
        int numberOfDevices = (int)deviceRepository.count();
        metrics.setNumberOfClassifications(numberOfClassifications);
        metrics.setNumberOfDevices(numberOfDevices);

        // ratio female/total
        long females = classifications.stream()
            .filter(classification -> classification.getGender().equals(Gender.FEMALE))
            .collect(Collectors.counting());

        long males = classifications.stream()
            .filter(classification -> classification.getGender().equals(Gender.MALE))
            .collect(Collectors.counting());

        double ratio =  (double)females / (females + males);
        metrics.setFemaleRatio(ratio);

        // average age
        double averageAge = classifications.stream()
            .mapToDouble(i -> (double) i.getAge())
            .average().orElse(-1);
        metrics.setAverageAge((int) averageAge);

        // distinct customers
        long distinctCustomers = classificationRepository.findAll().stream()
            .filter(classification -> !ignoredPersonal.contains(classification.getPersonId()))
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

