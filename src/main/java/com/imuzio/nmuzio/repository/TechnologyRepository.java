package com.imuzio.nmuzio.repository;

import com.imuzio.nmuzio.model.entity.Technology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TechnologyRepository extends JpaRepository<Technology,Long> {

    Optional<Technology> findByNameAndVersion(String name, String version);

    List<Technology> findAllByName(String name);
}
