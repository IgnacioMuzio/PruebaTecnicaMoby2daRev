package com.imuzio.nmuzio.repository;

import com.imuzio.nmuzio.model.entity.CandidateTechnology;
import com.imuzio.nmuzio.projection.CandidateProjection;
import com.imuzio.nmuzio.projection.TechnologyProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateTechnologyRepository extends JpaRepository<CandidateTechnology,Long> {

    @Query("Select cs from CandidateTechnology cs where cs.candidate.id = ?1 and cs.technology.id = ?2")
    List<CandidateTechnology> findByCandidateIdAndTechnologyId(Long candidateId, Long technologyId);

    @Query(value = "Select tec.name,tec.version,c_t.years_of_experience " +
            "from technologies tec join candidate_technology c_t on tec.id=c_t.technology_id " +
            "where c_t.candidate_id = :id" ,nativeQuery = true)
    List<TechnologyProjection> getTechnologiesByCandidateId(Long id);

    @Query(value = "SELECT can.first_name, can.last_name, can.document_type, can.document_number, can.birth_date, " +
            "c_t.years_of_experience, tec.version " +
            "FROM candidates can " +
            "JOIN candidate_technology c_t ON can.id = c_t.candidate_id " +
            "JOIN technologies tec ON c_t.technology_id = tec.id " +
            "WHERE tec.name = :name", nativeQuery = true)
    List<CandidateProjection> getCandidatesByTecnologyName(String name);

    @Query(value = "SELECT can.first_name, can.last_name, can.document_type, can.document_number, can.birth_date, " +
            "c_t.years_of_experience, tec.version " +
            "FROM candidates can " +
            "JOIN candidate_technology c_t ON can.id = c_t.candidate_id " +
            "JOIN technologies tec ON c_t.technology_id = tec.id " +
            "WHERE tec.name = :name AND tec.version =:version", nativeQuery = true)
    List<CandidateProjection> getCandidatesByTecnologyNameAndVersion(String name, String version);
}
