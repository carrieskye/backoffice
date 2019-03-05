package me.larrycarodenis.web.rest;


import me.larrycarodenis.domain.Classification;
import me.larrycarodenis.domain.GenderTotals;
import me.larrycarodenis.domain.enumeration.Gender;
import me.larrycarodenis.repository.ClassificationRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static java.time.temporal.ChronoUnit.MINUTES;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import java.time.*;
import java.util.*;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsResource {

    private ClassificationRepository classificationRepository;

    public StatisticsResource(ClassificationRepository classificationRepository)
    {
        this.classificationRepository = classificationRepository;
    }

    /**
     * GET  /devices : get all the devices.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of devices in body
     */
    @GetMapping("/activity")
    public Map<LocalTime, GenderTotals> getActivity(@RequestParam(name = "store", required = false, defaultValue = "-1") Long store, @RequestParam(name = "interval", required = false, defaultValue = "15") Integer interval)
    {
        LocalTime timeStart = LocalTime.of(9, 0);
        LocalTime timeEnd = LocalTime.of(19, 0);
        LocalTime timePointer = timeStart;
        Map<LocalTime, GenderTotals> data = new HashMap<>();

        List<Classification> classificationList;
        groupClassifications();

        if(store == -1)
        {
            classificationList = groupClassifications();
        }
        else
        {
            List<Classification> list = classificationRepository.findAll();

            classificationList = new ArrayList<>();

            for(Classification classification : list)
            {
                System.out.println(classification.getDevice().getId());
                if(classification.getDevice().getId().equals(store) )
                {
                    classificationList.add(classification);
                }
            }
        }

        while (timePointer.isBefore(timeEnd))
        {
            int male = 0;
            int female = 0;
            List<Classification> classifications = new ArrayList<>();

            for(Classification classification : classificationList)
            {
                LocalTime classificationTime = LocalTime.from(classification.getTimestamp().atZone(ZoneId.of("GMT+1")));
                if(classificationTime.isAfter(timePointer) && classificationTime.isBefore(timePointer.plusMinutes(interval)))
                {
                    classifications.add(classification);
                    if(classification.getGender() == Gender.MALE)
                    {
                        male++;
                    }
                    else
                    {
                        female++;
                    }
                }
            }
            data.put(timePointer, new GenderTotals(male, female));
            timePointer = timePointer.plusMinutes(interval);
        }
        return data;
    }

    @GetMapping("/agedistribution")
    public Map<Integer, Integer> getAgeDistribution(@RequestParam(name = "start", required = true) @DateTimeFormat(pattern="ddMMyyyy") Date startPeriod,
                                   @RequestParam(name = "end", required = true) @DateTimeFormat(pattern = "ddMMyyyy") Date endPeriod,
                                   @RequestParam(name = "interval", required = false, defaultValue = "10") Integer interval)
    {
        Map<Integer, Integer> data = new HashMap<>();

        LocalDate begin = startPeriod.toInstant().atZone(ZoneId.of("GMT+1")).toLocalDate();
        LocalDate end = endPeriod.toInstant().atZone(ZoneId.of("GMT+1")).toLocalDate();

        List<Classification> classificationsInTimeInterval = new ArrayList<>();

        for(Classification classification : groupClassifications())
        {
            if(LocalDate.from(classification.getTimestamp().atZone(ZoneId.of("GMT+1"))).isAfter(begin) && LocalDate.from(classification.getTimestamp().atZone(ZoneId.of("GMT+1"))).isBefore(end))
            {
                classificationsInTimeInterval.add(classification);
            }
        }

        int beginAge = 0;
        int endAge = 90;
        int pointer = beginAge;

        while(pointer < endAge)
        {
            int counter = 0;

            for(Classification classification : classificationsInTimeInterval)
            {
                System.out.println(classification.getAge());
                if(classification.getAge() > pointer && classification.getAge() < pointer + interval)
                {
                    counter ++;
                }
            }
            data.put(pointer, counter);
            pointer = pointer + interval;


        }
        return data;

    }

    public List<Classification> groupClassifications()
    {
        List<Classification> all = classificationRepository.findAll();

        LocalTime timeStart = LocalTime.of(9, 0);
        LocalTime timeEnd = LocalTime.of(19, 0);
        int interval = 5;
        LocalTime timePointer = timeStart;
        List<Classification> finalClassifications = new ArrayList<>();

        Map<LocalTime, List<Classification>> data = new HashMap<>();

        while (timePointer.isBefore(timeEnd))
        {
            List<Classification> classifications = new ArrayList<>();

            for(Classification classification : all)
            {
                LocalTime classificationTime = LocalTime.from(classification.getTimestamp().atZone(ZoneId.of("GMT+1")));
                if(classificationTime.isAfter(timePointer) && classificationTime.isBefore(timePointer.plusMinutes(interval)))
                {
                    classifications.add(classification);
                }
            }
            data.put(timePointer, classifications);
            timePointer = timePointer.plusMinutes(interval);
        }

        for (Map.Entry<LocalTime, List<Classification>> entry : data.entrySet()) {
            List<Classification> result = entry.getValue().stream().filter(distinctByKey(classification -> classification.getPersonId() + classification.getDevice().getId())).collect(Collectors.toList());
            for(Classification cl : result)
            {
                System.out.println("Classification with: " + cl.getPersonId() + " At time: " + cl.getTimestamp());
                finalClassifications.add(cl);
            }
        }
        return finalClassifications;
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

}
