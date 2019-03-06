package me.larrycarodenis.web.rest;


import me.larrycarodenis.domain.Classification;
import me.larrycarodenis.domain.ClassificationWithDuration;
import me.larrycarodenis.domain.GenderTotals;
import me.larrycarodenis.domain.enumeration.Gender;
import me.larrycarodenis.repository.ClassificationRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import static java.time.temporal.ChronoUnit.DAYS;
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

    public StatisticsResource(ClassificationRepository classificationRepository) {
        this.classificationRepository = classificationRepository;
    }

    /**
     * GET  /activity : get amount of male and female classification for a day
     *
     * @return the ResponseEntity with status 200 (OK) and the list of devices in body
     */

    @GetMapping("/agetimedistribution")
    public Map<LocalTime, Map<Integer, Integer>> getAgeTimeDistribution(@RequestParam(name = "start", required = true) @DateTimeFormat(pattern="ddMMyyyy") Date startPeriod,
                                                    @RequestParam(name = "end", required = true) @DateTimeFormat(pattern = "ddMMyyyy") Date endPeriod,
                                                    @RequestParam(name = "ageinterval", required = false, defaultValue = "10") Integer ageInterval,
                                                    @RequestParam(name = "timeinterval", required = false, defaultValue = "10") Integer timeInterval,
                                                    @RequestParam(name = "store", required = false, defaultValue = "-1") Long store)
    {


        LocalTime timeStart = LocalTime.of(9, 0);
        LocalTime timeEnd = LocalTime.of(19, 0);

        LocalDate beginDate  = LocalDate.now();
        LocalDate endDate = LocalDate.of(1970, 01, 01);

        LocalTime timePointer = timeStart;
        Map<LocalTime, Map<Integer, Integer>> data = new HashMap<>();

        List<Classification> classificationList;
        groupClassifications();

        if(store == -1)
        {
            classificationList = groupClassifications();
        }
        else
        {
            List<Classification> list = groupClassifications();

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

        for(Classification classification : classificationList)
        {
            LocalDate classificationDate = LocalDate.from(classification.getTimestamp().atZone(ZoneId.of("GMT+1")));

            if (classificationDate.isBefore(beginDate)) {
                beginDate = classificationDate;
            }
            if (classificationDate.isAfter(endDate)) {
                endDate = classificationDate;
            }
        }

        int totalDays = (int) DAYS.between(beginDate, endDate);





        while (timePointer.isBefore(timeEnd))
        {
            int male = 0;
            int female = 0;
            Map<Integer, Integer> ageDistr = new HashMap<>();
            for(Classification classification : classificationList)
            {

                LocalTime classificationTime = LocalTime.from(classification.getTimestamp().atZone(ZoneId.of("GMT+1")));

                if (classificationTime.isAfter(timePointer) && classificationTime.isBefore(timePointer.plusMinutes(timeInterval)))
                {

                    int beginAge = 0;
                    int endAge = 90;
                    int pointer = beginAge;


                    while(pointer < endAge)
                    {
                        int counter = 0;

                        for(Classification c : classificationList)
                        {
                            if(c.getAge() > pointer && c.getAge() < pointer + ageInterval)
                            {
                                counter ++;
                            }
                        }
                        ageDistr.put(pointer, counter);
                        pointer = pointer + ageInterval;


                    }
                }
            }
            data.put(timePointer, ageDistr);
            timePointer = timePointer.plusMinutes(timeInterval);
        }
        return data;
    }




    @GetMapping("/activity")
    public Map<LocalTime, GenderTotals> getActivity(
        @RequestParam(required = false, defaultValue = "-1") Long store,
        @RequestParam(required = false, defaultValue = "15") Integer interval,
        @RequestParam(required = false, defaultValue = "09:00") LocalTime timeStart,
        @RequestParam(required = false, defaultValue = "19:00") LocalTime timeEnd
    ) {
        Map<LocalTime, GenderTotals> data = new HashMap<>();

        // get classifications
        List<Classification> classifications = store == -1 ? classificationRepository.findAll() : classificationRepository.getAllByDevice_Id(store);

        // group by deviceid & personid
        List<ClassificationWithDuration> classificationsGrouped = classificationRepository.findAllGrouped(classifications);

        // create intervals
        int amountOfIntervals = (int) Math.ceil(MINUTES.between(timeStart, timeEnd) / interval);
        for (int i = 0; i < amountOfIntervals; i++) {
            LocalTime intervalStart = timeStart.plusMinutes(i * interval);
            LocalTime intervalEnd = timeStart.plusMinutes((i + 1) * interval);

            // count females
            int females = (int) classificationsGrouped.stream().filter(
                classification -> isClassificationWithDuractionOfGenderAndBetweenInterval(classification, Gender.FEMALE, intervalStart, intervalEnd)
            ).count();

            // count males
            int males = (int) classificationsGrouped.stream().filter(
                classification -> isClassificationWithDuractionOfGenderAndBetweenInterval(classification, Gender.MALE, intervalStart, intervalEnd)
            ).count();

            // add to map
            data.put(
                timeStart.plusMinutes(i * interval),
                new GenderTotals(males, females)
            );
        }
        return data;
    }

    /**
     * Check if moment is between start and end
     *
     * @param moment
     * @param start
     * @param end
     * @return
     */
    private boolean isBetween(LocalTime moment, LocalTime start, LocalTime end) {
        return (moment.isAfter(start) && moment.isBefore(end));
    }

    private boolean isBetween(Instant moment, LocalTime start, LocalTime end) {
        return isBetween(LocalTime.from(moment.atZone(ZoneId.of("GMT+1"))), start, end);
    }

    private boolean isClassificationWithDuractionOfGenderAndBetweenInterval(ClassificationWithDuration classification, Gender gender, LocalTime intervalStart, LocalTime intervalEnd) {
        return classification.getGender().equals(gender) && (
            isBetween(classification.getTimestampFirst(), intervalStart, intervalEnd) ||
                isBetween(classification.getTimestampLast(), intervalStart, intervalEnd)
        );
    }

    @GetMapping("/agedistribution")
    public Map<Integer, Integer> getAgeDistribution(@RequestParam(name = "start", required = true) @DateTimeFormat(pattern = "ddMMyyyy") Date startPeriod,
                                                    @RequestParam(name = "end", required = true) @DateTimeFormat(pattern = "ddMMyyyy") Date endPeriod,
                                                    @RequestParam(name = "interval", required = false, defaultValue = "10") Integer interval) {
        Map<Integer, Integer> data = new HashMap<>();

        LocalDate begin = startPeriod.toInstant().atZone(ZoneId.of("GMT+1")).toLocalDate();
        LocalDate end = endPeriod.toInstant().atZone(ZoneId.of("GMT+1")).toLocalDate();

        List<Classification> classificationsInTimeInterval = new ArrayList<>();

        for (Classification classification : groupClassifications()) {
            if (LocalDate.from(classification.getTimestamp().atZone(ZoneId.of("GMT+1"))).isAfter(begin) && LocalDate.from(classification.getTimestamp().atZone(ZoneId.of("GMT+1"))).isBefore(end)) {
                classificationsInTimeInterval.add(classification);
            }
        }

        int beginAge = 0;
        int endAge = 90;
        int pointer = beginAge;

        while (pointer < endAge) {
            int counter = 0;

            for (Classification classification : classificationsInTimeInterval) {
                System.out.println(classification.getAge());
                if (classification.getAge() > pointer && classification.getAge() < pointer + interval) {
                    counter++;
                }
            }
            data.put(pointer, counter);
            pointer = pointer + interval;


        }
        return data;

    }

    public List<Classification> groupClassifications() {
        List<Classification> all = classificationRepository.findAll();

        LocalTime timeStart = LocalTime.of(9, 0);
        LocalTime timeEnd = LocalTime.of(19, 0);
        int interval = 5;
        LocalTime timePointer = timeStart;
        List<Classification> finalClassifications = new ArrayList<>();

        Map<LocalTime, List<Classification>> data = new HashMap<>();

        while (timePointer.isBefore(timeEnd)) {
            List<Classification> classifications = new ArrayList<>();

            for (Classification classification : all) {
                LocalTime classificationTime = LocalTime.from(classification.getTimestamp().atZone(ZoneId.of("GMT+1")));
                if (classificationTime.isAfter(timePointer) && classificationTime.isBefore(timePointer.plusMinutes(interval))) {
                    classifications.add(classification);
                }
            }
            data.put(timePointer, classifications);
            timePointer = timePointer.plusMinutes(interval);
        }

        for (Map.Entry<LocalTime, List<Classification>> entry : data.entrySet()) {
            List<Classification> result = entry.getValue().stream().filter(distinctByKey(classification -> classification.getPersonId() + classification.getDevice().getId())).collect(Collectors.toList());
            for (Classification cl : result) {
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
