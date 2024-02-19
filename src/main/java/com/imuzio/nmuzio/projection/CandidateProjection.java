package com.imuzio.nmuzio.projection;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;

public interface CandidateProjection {

    @Value("#{target.first_name + ' ' +  target.last_name}")
    String getFirstNameAndLastName();

    @Value("#{target.document_type + ' ' +  target.document_number}")
    String getDocumentTypeAndNumber();

    @Value("#{target.birth_date}")
    LocalDate getBirthDate();

    @Value("#{target.years_of_experience}")
    Integer getYearsOfExperience();

    @Value("#{target.version}")
    String getVersion();
}
