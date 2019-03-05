package me.larrycarodenis.web.rest;

import me.larrycarodenis.domain.Classification;
import me.larrycarodenis.domain.Ip;
import me.larrycarodenis.domain.Metrics;
import me.larrycarodenis.repository.ClassificationRepository;
import me.larrycarodenis.repository.DeviceRepository;
import me.larrycarodenis.repository.IpRepository;
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
import java.util.ArrayList;
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

        LocalTime classificationTime = LocalTime.from(classificationRepository.findTopByOrderByIdDesc().getTimestamp().atZone(ZoneId.of("GMT+1")));
        metrics.setLastClassification(classificationTime);

        metrics.setNumberOfStores(deviceRepository.findAll().size());

        int male = 0;
        int female = 0;

        for(Classification classification : classificationRepository.findAll())
        {
            if(classification.getGender().equals("MALE"))
            {
                male++;
            }
            else if(classification.getGender().equals("FEMALE"))
            {
                female++;
            }
        }
        if(female == 0)
        {
            metrics.setFemaleRatio(0);
        }
        else
        {
            int tot = male + female;
            metrics.setFemaleRatio(female / tot);
        }

        int sum = 0;

        for(Classification classification : classificationRepository.findAll())
        {
            sum = sum + classification.getAge();
        }
        metrics.setAgeMedian(sum/classificationRepository.findAll().size());

        List<Classification> result = classificationRepository.findAll().stream().filter(distinctByKey(classification -> classification.getPersonId() + classification.getDevice().getId())).collect(Collectors.toList());
        for(Classification cl : result)
        {
            System.out.println("Classification with: " + cl.getPersonId() + " At time: " + cl.getTimestamp());
        }
        metrics.setDistinctCustomers(result.size());


        return metrics;
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor)
    {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}

