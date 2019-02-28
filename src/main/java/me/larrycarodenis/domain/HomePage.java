package me.larrycarodenis.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A HomePage.
 */
@Entity
@Table(name = "home_page")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class HomePage implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "young_male_url", nullable = false)
    private String youngMaleUrl;

    @NotNull
    @Column(name = "young_female_url", nullable = false)
    private String youngFemaleUrl;

    @NotNull
    @Column(name = "adult_male_url", nullable = false)
    private String adultMaleUrl;

    @NotNull
    @Column(name = "adult_female_url", nullable = false)
    private String adultFemaleUrl;

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

    public HomePage name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYoungMaleUrl() {
        return youngMaleUrl;
    }

    public HomePage youngMaleUrl(String youngMaleUrl) {
        this.youngMaleUrl = youngMaleUrl;
        return this;
    }

    public void setYoungMaleUrl(String youngMaleUrl) {
        this.youngMaleUrl = youngMaleUrl;
    }

    public String getYoungFemaleUrl() {
        return youngFemaleUrl;
    }

    public HomePage youngFemaleUrl(String youngFemaleUrl) {
        this.youngFemaleUrl = youngFemaleUrl;
        return this;
    }

    public void setYoungFemaleUrl(String youngFemaleUrl) {
        this.youngFemaleUrl = youngFemaleUrl;
    }

    public String getAdultMaleUrl() {
        return adultMaleUrl;
    }

    public HomePage adultMaleUrl(String adultMaleUrl) {
        this.adultMaleUrl = adultMaleUrl;
        return this;
    }

    public void setAdultMaleUrl(String adultMaleUrl) {
        this.adultMaleUrl = adultMaleUrl;
    }

    public String getAdultFemaleUrl() {
        return adultFemaleUrl;
    }

    public HomePage adultFemaleUrl(String adultFemaleUrl) {
        this.adultFemaleUrl = adultFemaleUrl;
        return this;
    }

    public void setAdultFemaleUrl(String adultFemaleUrl) {
        this.adultFemaleUrl = adultFemaleUrl;
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
        HomePage homePage = (HomePage) o;
        if (homePage.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), homePage.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "HomePage{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", youngMaleUrl='" + getYoungMaleUrl() + "'" +
            ", youngFemaleUrl='" + getYoungFemaleUrl() + "'" +
            ", adultMaleUrl='" + getAdultMaleUrl() + "'" +
            ", adultFemaleUrl='" + getAdultFemaleUrl() + "'" +
            "}";
    }
}
