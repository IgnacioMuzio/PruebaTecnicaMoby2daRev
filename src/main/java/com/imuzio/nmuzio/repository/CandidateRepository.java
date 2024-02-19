package com.imuzio.nmuzio.repository;

import com.imuzio.nmuzio.model.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate,Long> {
    Optional<Candidate> findByDocumentTypeAndDocumentNumber(String documentType, Integer documentNumber);
}
