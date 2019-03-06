package me.larrycarodenis.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Device.
 */
@Entity
@Table(name = "device")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Device implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "postal_code", nullable = false)
    private Integer postalCode;

    @ManyToOne
    @JsonIgnoreProperties("devices")
    private HomePage homepage;

    @ManyToMany(mappedBy = "devices")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Slide> slides = new HashSet<>();

    @Transient
    private String lastIp = null;
    @Transient
    private LocalDateTime lastConnection = null;

    @JsonProperty()
    public String getLastIp() {
        return lastIp;
    }

    public void setLastIp(String lastIp) {
        this.lastIp = lastIp;
    }

    @JsonProperty()
    public LocalDateTime getLastConnection() {
        return lastConnection;
    }

    public void setLastConnection(LocalDateTime lastConnection) {
        this.lastConnection = lastConnection;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Device name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPostalCode() {
        return postalCode;
    }

    public Device postalCode(Integer postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }

    public HomePage getHomepage() {
        return homepage;
    }

    public Device homepage(HomePage homePage) {
        this.homepage = homePage;
        return this;
    }

    public void setHomepage(HomePage homePage) {
        this.homepage = homePage;
    }

    public Set<Slide> getSlides() {
        return slides;
    }

    public Device slides(Set<Slide> slides) {
        this.slides = slides;
        return this;
    }

    public Device addSlide(Slide slide) {
        this.slides.add(slide);
        slide.getDevices().add(this);
        return this;
    }

    public Device removeSlide(Slide slide) {
        this.slides.remove(slide);
        slide.getDevices().remove(this);
        return this;
    }

    public void setSlides(Set<Slide> slides) {
        this.slides = slides;
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
        Device device = (Device) o;
        if (device.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), device.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Device{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", postalCode=" + getPostalCode() +
            "}";
    }
}
