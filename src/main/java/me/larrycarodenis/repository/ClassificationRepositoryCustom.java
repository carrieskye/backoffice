package me.larrycarodenis.repository;

import me.larrycarodenis.domain.Classification;
import me.larrycarodenis.domain.ClassificationWithDuration;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassificationRepositoryCustom {
    List<ClassificationWithDuration> findAllGrouped(List<Classification> classifications, List<String> ignoredLabels);
    List<ClassificationWithDuration> findAllGrouped(List<Classification> classifications);
}
