package me.larrycarodenis.web.rest;


import me.larrycarodenis.domain.Classification;
import me.larrycarodenis.domain.GenderTotals;
import me.larrycarodenis.domain.enumeration.Gender;
import me.larrycarodenis.repository.ClassificationRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static java.time.temporal.ChronoUnit.MINUTES;

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

        if(store == -1)
        {
            List<Classification> temp = classificationRepository.findAll();
            classificationList = groupedClassifications(temp);
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
            GenderTotals genderTotal = new GenderTotals();

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
            genderTotal.setM(male);
            genderTotal.setF(female);
            genderTotal.setClassificationList(classifications);
            data.put(timePointer, genderTotal);
            timePointer = timePointer.plusMinutes(interval);
        }
        return data;
    }

    @GetMapping("/agedistribution")
    public void getAgeDistribution(@RequestParam(name = "start", required = true) @DateTimeFormat(pattern="ddMMyyyy") Date startPeriod,
                                   @RequestParam(name = "end", required = true) @DateTimeFormat(pattern = "ddMMyyyy") Date endPeriod,
                                   @RequestParam(name = "interval", required = false, defaultValue = "10") Integer interval)
    {
        LocalDate begin = startPeriod.toInstant().atZone(ZoneId.of("GMT+1")).toLocalDate();
        LocalDate end = endPeriod.toInstant().atZone(ZoneId.of("GMT+1")).toLocalDate();




    }


    public List<Classification> groupedClassifications(List<Classification> raw)
    {
        List<Classification> grouped = new ArrayList<>();
        grouped.add(raw.get(0));
        for(Classification classification : raw)
        {
            innerloop:
            for(Classification classification2 : raw)
            {
                System.out.println(classification.getId());
                System.out.println(classification2.getId());
                LocalTime classificationTime1 = classification.getTimestamp().atZone(ZoneId.of("GMT+1")).toLocalTime();
                LocalTime classificationTime2 = classification2.getTimestamp().atZone(ZoneId.of("GMT+1")).toLocalTime();
                //System.out.println("classification time 1 " + classificationTime1);
                //System.out.println("classification time 2 " + classificationTime2);
                //System.out.println(MINUTES.between(classificationTime1, classificationTime2));
                if(MINUTES.between(classificationTime1, classificationTime2) > 5 || MINUTES.between(classificationTime1, classificationTime2) < 0)
                {
                    //System.out.println("difference in time, unique classification");

                    if(grouped.contains(classification)|| grouped.contains(classification2) || classification.getPersonId().equals(classification2.getPersonId()))
                    {
                        //break innerloop;
                    }
                    else
                    {
                        System.out.println("I'm doing something I should'nt be doing and I am an asshole");
                        grouped.add(classification);
                    }

                }
                else
                {
                    break innerloop;

                }
            }
        }
        for(Classification cl : grouped)
        {
            System.out.println(cl.getId());
        }


        return grouped;
    }

}
