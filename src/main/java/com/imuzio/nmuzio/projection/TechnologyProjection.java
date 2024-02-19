package com.imuzio.nmuzio.projection;

import org.springframework.beans.factory.annotation.Value;

public interface TechnologyProjection {

    @Value("#{target.name}")
    String getName();

    @Value("#{target.version}")
    String getVersion();

    @Value("#{target.years_of_experience}")
    Integer getYearsOfExperience();
}
