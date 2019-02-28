package me.larrycarodenis.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import me.larrycarodenis.domain.enumeration.AgeCategory;

import me.larrycarodenis.domain.enumeration.Gender;

/**
 * A Slide.
 */
@Entity
@Table(name = "slide")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Slide implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    
    @Lob
    @Column(name = "image", nullable = false)
    private byte[] image;

    @Column(name = "image_content_type", nullable = false)
    private String imageContentType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "age_category", nullable = false)
    private AgeCategory ageCategory;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "slide_device",
               joinColumns = @JoinColumn(name = "slide_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "device_id", referencedColumnName = "id"))
    private Set<Device> devices = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public Slide image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public Slide imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public AgeCategory getAgeCategory() {
        return ageCategory;
    }

    public Slide ageCategory(AgeCategory ageCategory) {
        this.ageCategory = ageCategory;
        return this;
    }

    public void setAgeCategory(AgeCategory ageCategory) {
        this.ageCategory = ageCategory;
    }

    public Gender getGender() {
        return gender;
    }

    public Slide gender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Set<Device> getDevices() {
        return devices;
    }

    public Slide devices(Set<Device> devices) {
        this.devices = devices;
        return this;
    }

    public Slide addDevice(Device device) {
        this.devices.add(device);
        device.getSlides().add(this);
        return this;
    }

    public Slide removeDevice(Device device) {
        this.devices.remove(device);
        device.getSlides().remove(this);
        return this;
    }

    public void setDevices(Set<Device> devices) {
        this.devices = devices;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Slide slide = (Slide) o;
        if (slide.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), slide.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Slide{" +
            "id=" + getId() +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", ageCategory='" + getAgeCategory() + "'" +
            ", gender='" + getGender() + "'" +
            "}";
    }
}
