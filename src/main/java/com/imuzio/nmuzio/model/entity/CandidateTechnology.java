package com.imuzio.nmuzio.model.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "candidate_technology")
public class CandidateTechnology {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    @JsonBackReference
    private Candidate candidate;

    @ManyToOne
    @JoinColumn (name = "technology_id")
    @JsonBackReference
    private Technology technology;

    @NotNull(message = "Years of experience are mandatory")
    @Min(value = 1, message = "The amount of years has to be higher or equal to 1")
    @JoinColumn(name = "years_of_experience")
    private Integer yearsOfExperience;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        CandidateTechnology that = (CandidateTechnology) object;
        return Objects.equals(candidate, that.candidate) && Objects.equals(technology, that.technology) && Objects.equals(yearsOfExperience, that.yearsOfExperience);
    }

    @Override
    public int hashCode() {
        return Objects.hash(candidate, technology, yearsOfExperience);
    }
}
