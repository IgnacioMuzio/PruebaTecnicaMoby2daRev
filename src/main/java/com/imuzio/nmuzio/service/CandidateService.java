package com.imuzio.nmuzio.service;

import com.imuzio.nmuzio.exception.DuplicatedCandidateTechnologyRelationException;
import com.imuzio.nmuzio.exception.DuplicatedDocumentNumberAndTypeCandidateException;
import com.imuzio.nmuzio.exception.EntityNotFoundException;
import com.imuzio.nmuzio.exception.InvalidDateCandidateException;
import com.imuzio.nmuzio.exception.InvalidDocumentTypeCandidateException;
import com.imuzio.nmuzio.model.dto.CandidateDto;
import com.imuzio.nmuzio.model.entity.Candidate;
import com.imuzio.nmuzio.model.entity.CandidateTechnology;
import com.imuzio.nmuzio.projection.TechnologyProjection;

import java.util.List;
import java.util.Map;

public interface CandidateService {
    List<CandidateDto> getAll();

    CandidateDto getById(Long id) throws EntityNotFoundException;

    Candidate create (CandidateDto candidateDto) throws DuplicatedDocumentNumberAndTypeCandidateException, InvalidDocumentTypeCandidateException, InvalidDateCandidateException;

    Candidate update (CandidateDto candidateDto, Long id) throws DuplicatedDocumentNumberAndTypeCandidateException, InvalidDocumentTypeCandidateException, InvalidDateCandidateException;

    void delete (Long id);

    CandidateTechnology addTechnology (Long candidateId, Long technologyId, Integer yearsOfExperience) throws EntityNotFoundException, DuplicatedCandidateTechnologyRelationException;

    Map<String,List<TechnologyProjection>> viewTechnologiesByCandidateDocument(String docType, Integer docNumber) throws EntityNotFoundException, InvalidDocumentTypeCandidateException;
}
