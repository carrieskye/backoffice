package me.larrycarodenis.domain;

import me.larrycarodenis.domain.enumeration.Emotion;
import me.larrycarodenis.domain.enumeration.Gender;

import java.time.Instant;

public class ClassificationWithDuration {
    private String personId;
    private Long deviceId;

    private Instant timestampFirst;
    private Instant timestampLast;
    private int classificationCount;

    private Integer age;

    private Gender gender;

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public Instant getTimestampFirst() {
        return timestampFirst;
    }

    public void setTimestampFirst(Instant timestampFirst) {
        this.timestampFirst = timestampFirst;
    }

    public Instant getTimestampLast() {
        return timestampLast;
    }

    public void setTimestampLast(Instant timestampLast) {
        this.timestampLast = timestampLast;
    }

    public int getClassificationCount() {
        return classificationCount;
    }

    public void setClassificationCount(int classificationCount) {
        this.classificationCount = classificationCount;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Emotion getEmotion() {
        return emotion;
    }

    public void setEmotion(Emotion emotion) {
        this.emotion = emotion;
    }

    private Emotion emotion;
}
