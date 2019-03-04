package me.larrycarodenis.web.rest;


import me.larrycarodenis.domain.Classification;
import me.larrycarodenis.domain.GenderTotals;
import me.larrycarodenis.domain.enumeration.Gender;
import me.larrycarodenis.repository.ClassificationRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            classificationList = classificationRepository.findAll();
        }
        else
        {
            List<Classification> list = classificationRepository.findAll();

            classificationList = new ArrayList<>();

            for(Classification classification : list)
            {
                System.out.println(classification.getDevice().getId());
                System.out.println(store);
                if(classification.getDevice().getId().equals(store) )
                {
                    System.out.println("True");
                    classificationList.add(classification);
                }
            }
        }

        while (timePointer.isBefore(timeEnd))
        {
            int male = 0;
            int female = 0;

            for(Classification classification : classificationList)
            {
                LocalTime classificationTime = LocalTime.from(classification.getTimestamp().atZone(ZoneId.of("GMT+1")));
                if(classificationTime.isAfter(timePointer) && classificationTime.isBefore(timePointer.plusMinutes(interval)))
                {
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
    public void getAgeDistribution(@RequestParam(name = "start", required = true) @DateTimeFormat(pattern="ddMMyyyy") Date startPeriod,
                                   @RequestParam(name = "end", required = true) @DateTimeFormat(pattern = "ddMMyyyy") Date endPeriod,
                                   @RequestParam(name = "interval", required = false, defaultValue = "10") Integer interval)
    {
        LocalDate begin = startPeriod.toInstant().atZone(ZoneId.of("GMT+1")).toLocalDate();
        LocalDate end = endPeriod.toInstant().atZone(ZoneId.of("GMT+1")).toLocalDate();




    }

}
