package me.larrycarodenis.repository;

import me.larrycarodenis.domain.Classification;
import me.larrycarodenis.domain.ClassificationWithDuration;
import me.larrycarodenis.domain.enumeration.Emotion;
import me.larrycarodenis.domain.enumeration.Gender;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings({"unused", "unchecked"})
public class ClassificationRepositoryImpl implements ClassificationRepositoryCustom {
    @Override
    public List<ClassificationWithDuration> findAllGrouped(List<Classification> originalClassifications) {
        // group by "personId @ deviceId @ yyyy-MM-dd_hh:mm"
        Map<String, List<Classification>> classificationsPerPerson = originalClassifications.stream()
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
                        List<Classification> classifications = e.getValue();
                        ClassificationWithDuration c = new ClassificationWithDuration();

                        // set first and last timestamp
                        c.setTimestampFirst(classifications.get(0).getTimestamp());
                        c.setTimestampLast(classifications.get(classifications.size() - 1).getTimestamp());

                        // set personId
                        c.setPersonId(classifications.get(0).getPersonId());

                        // set device
                        c.setDeviceId(classifications.get(0).getDevice().getId());

                        // count of classifications
                        c.setClassificationCount(classifications.size());

                        // average age
                        double averageAge = classifications.stream()
                            .mapToDouble(i -> (double) i.getAge())
                            .average().orElse(-1);
                        c.setAge((int) averageAge);

                        // most common gender
                        Gender mostCommonGender = getMostCommonInClassification(classifications, Classification::getGender);
                        c.setGender(mostCommonGender);

                        // most common emotion
                        Emotion mostCommonEmotion = getMostCommonInClassification(classifications, Classification::getEmotion);
                        c.setEmotion(mostCommonEmotion);

                        return c;
                    })
                ).values());
    }

    private static <A,B> B getMostCommonInClassification(List<A> classifications, Function<A,B> f){
        return classifications.stream()
            .collect(Collectors.groupingBy(f1 -> f.apply(f1), Collectors.counting()))
            .entrySet()
            .stream()
            .max(Comparator.comparing(Map.Entry::getValue))
            .get().getKey();
    }
}
