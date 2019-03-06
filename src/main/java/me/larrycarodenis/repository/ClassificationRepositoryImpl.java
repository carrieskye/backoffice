package me.larrycarodenis.repository;

import me.larrycarodenis.domain.Classification;
import me.larrycarodenis.domain.ClassificationWithDuration;
import me.larrycarodenis.domain.Device;
import me.larrycarodenis.domain.enumeration.Emotion;
import me.larrycarodenis.domain.enumeration.Gender;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClassificationRepositoryImpl implements ClassificationRepositoryCustom {
    @Autowired
    private EntityManager entityManager;

    @Override
    public List<ClassificationWithDuration> findAllGrouped() {
        // get all classifications
        List<Classification> classifications = entityManager.createQuery("Select c from Classification c", Classification.class).getResultList();

        // group by "personId @ deviceId @ yyyy-MM-dd_hh:mm"
        Map<String, List<Classification>> classificationsPerPerson = classifications.stream()
            .collect(Collectors.groupingBy(
                classification -> classification.getPersonId() + '@'
                    + classification.getDevice().getId() + '@'
                    + DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.ofInstant(classification.getTimestamp(), ZoneId.of("GMT+1")))
            ));

        // for each group get the average age, most common gender, last emotion

        return new ArrayList(
        classificationsPerPerson.entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey,
                e -> {
                    List<Classification> classifications1 = e.getValue();
                    ClassificationWithDuration c = new ClassificationWithDuration();

                    // set first and last timestamp
                    c.setTimestampFirst(classifications1.get(0).getTimestamp());
                    c.setTimestampLast(classifications1.get(classifications1.size() - 1).getTimestamp());

                    // set personId
                    c.setPersonId(classifications1.get(0).getPersonId());

                    // set device
                    c.setDeviceId(classifications1.get(0).getDevice().getId());

                    // count of classifications
                    c.setClassificationCount(classifications1.size());

                    // average age
                    double averageAge = classifications1.stream()
                        .mapToDouble(i -> (double) i.getAge())
                        .average().getAsDouble();
                    c.setAge((int) averageAge);

                    // most common gender
                    Gender mostCommonGender = classifications1.stream()
                        .collect(Collectors.groupingBy(s -> s.getGender(), Collectors.counting()))
                        .entrySet()
                        .stream()
                        .max(Comparator.comparing(Map.Entry::getValue))
                        .get().getKey();
                    c.setGender(mostCommonGender);

                    // most common emotion
                    Emotion mostCommonEmotion = classifications1.stream()
                        .collect(Collectors.groupingBy(s -> s.getEmotion(), Collectors.counting()))
                        .entrySet()
                        .stream()
                        .max(Comparator.comparing(Map.Entry::getValue))
                        .get().getKey();
                    c.setEmotion(mostCommonEmotion);

                    return c;
                })
            ).values());
    }
}
