package com.imuzio.nmuzio.service.impl;

import com.imuzio.nmuzio.exception.DuplicatedCandidateTechnologyRelationException;
import com.imuzio.nmuzio.exception.DuplicatedDocumentNumberAndTypeCandidateException;
import com.imuzio.nmuzio.exception.EntityNotFoundException;
import com.imuzio.nmuzio.exception.InvalidDateCandidateException;
import com.imuzio.nmuzio.exception.InvalidDocumentTypeCandidateException;
import com.imuzio.nmuzio.model.dto.CandidateDto;
import com.imuzio.nmuzio.model.entity.Candidate;
import com.imuzio.nmuzio.model.entity.CandidateTechnology;
import com.imuzio.nmuzio.model.entity.Technology;
import com.imuzio.nmuzio.model.enumerator.DocumentType;
import com.imuzio.nmuzio.projection.TechnologyProjection;
import com.imuzio.nmuzio.repository.CandidateRepository;
import com.imuzio.nmuzio.repository.CandidateTechnologyRepository;
import com.imuzio.nmuzio.repository.TechnologyRepository;
import com.imuzio.nmuzio.service.CandidateService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class CandidateServiceImpl implements CandidateService {


    private CandidateRepository candidateRepository;


    private TechnologyRepository technologyRepository;


    private CandidateTechnologyRepository candidateTechnologyRepository;

    Logger logger = Logger.getLogger(CandidateServiceImpl.class);

    public CandidateServiceImpl(CandidateRepository candidateRepository, TechnologyRepository technologyRepository, CandidateTechnologyRepository candidateTechnologyRepository) {
        this.candidateRepository = candidateRepository;
        this.technologyRepository = technologyRepository;
        this.candidateTechnologyRepository = candidateTechnologyRepository;
    }

    public List<CandidateDto> getAll(){
        return candidateRepository.findAll().stream().map(this::candidateDtoBuilder).toList();
    }

    public CandidateDto getById(Long id) throws EntityNotFoundException {
        Optional<Candidate> candidate = candidateRepository.findById(id);
        if(!candidate.isPresent()){
            logger.error("candidate not found");
            throw new EntityNotFoundException("Candidate not found");
        }
        logger.info("Candidate " + candidate.get().getLastName() + "found");
        return candidateDtoBuilder(candidate.get());
    }

    public Candidate create (CandidateDto candidateDto) throws DuplicatedDocumentNumberAndTypeCandidateException, InvalidDocumentTypeCandidateException, InvalidDateCandidateException {
        Candidate candidate = candidateBuilder(candidateDto,null);
        checkDocument(candidate);
        checkBirthDate(candidate);
        logger.info("saving candidate");
        candidateRepository.save(candidate);
        return candidate;
    }

    public Candidate update (CandidateDto candidateDto, Long id) throws DuplicatedDocumentNumberAndTypeCandidateException, InvalidDocumentTypeCandidateException, InvalidDateCandidateException {
        Candidate candidate = candidateBuilder(candidateDto,id);
        checkDocument(candidate);
        checkBirthDate(candidate);
        logger.info("updating candidate");
        candidateRepository.save(candidate);

        return candidate;
    }

    public void delete (Long id){
        candidateRepository.deleteById(id);
    }

    public CandidateDto candidateDtoBuilder (Candidate candidate){
        CandidateDto candidateDto =new CandidateDto(candidate.getFirstName(), candidate.getLastName(), candidate.getDocumentType(), candidate.getDocumentNumber(), candidate.getBirthDate());
        return candidateDto;
    }

    public Candidate candidateBuilder (CandidateDto candidateDto, Long id){
        Candidate candidate = new Candidate();
        candidate.setId(id);
        candidate.setFirstName(candidateDto.firstName());
        candidate.setLastName(candidateDto.lastName());
        candidate.setDocumentType(candidateDto.documentType().toUpperCase());
        candidate.setDocumentNumber(candidateDto.documentNumber());
        candidate.setBirthDate(candidateDto.birthDate());
        return candidate;
    }

    private void checkDocumentType(String documentType) throws InvalidDocumentTypeCandidateException {
        for(DocumentType type : DocumentType.values()){
            if(documentType.equalsIgnoreCase(type.toString())){
                return;
            }
        }
        throw new InvalidDocumentTypeCandidateException("Invalid document type. Try with DNI, LE or LC");
    }

    private void checkDocument (Candidate candidate) throws InvalidDocumentTypeCandidateException, DuplicatedDocumentNumberAndTypeCandidateException {
        checkDocumentType(candidate.getDocumentType());
        Optional<Candidate> candidateOptional = candidateRepository.findByDocumentTypeAndDocumentNumber(candidate.getDocumentType(), candidate.getDocumentNumber());
        if(candidateOptional.isPresent()){
            if(candidate.equals(candidateOptional.get()) && !Objects.equals(candidate.getId(),candidateOptional.get().getId())){
                throw new DuplicatedDocumentNumberAndTypeCandidateException("This document type and number are already registered");
            }
        }
    }

    private void checkBirthDate (Candidate candidate) throws InvalidDateCandidateException {
        if(candidate.getBirthDate().isAfter(LocalDate.now().minusYears(18))){
            throw new InvalidDateCandidateException("The candidate needs to be at least 18yo");
        }
    }

    public CandidateTechnology addTechnology (Long candidateId, Long technologyId, Integer yearsOfExperience) throws EntityNotFoundException, DuplicatedCandidateTechnologyRelationException {
        Candidate candidate = candidateBuilder(getById(candidateId),candidateId);
        Technology technology = technologyRepository.findById(technologyId).orElseThrow(() -> new EntityNotFoundException("Tech was not found"));
        if(!candidateTechnologyRepository.findByCandidateIdAndTechnologyId(candidateId,technologyId).isEmpty()){
            logger.error("already used");
            throw new DuplicatedCandidateTechnologyRelationException("This technology and version are already registered on this candidate");
        }
        CandidateTechnology candidateTechnology = new CandidateTechnology();
        candidateTechnology.setCandidate(candidate);
        candidateTechnology.setTechnology(technology);
        candidateTechnology.setYearsOfExperience(yearsOfExperience);
        candidateTechnologyRepository.save(candidateTechnology);
        candidate.getTechnologies().add(candidateTechnology);
        logger.info("association saved");
        candidateRepository.save(candidate);
        return candidateTechnology;
    }

    public Map<String,List<TechnologyProjection>> viewTechnologiesByCandidateDocument(String docType, Integer docNumber) throws EntityNotFoundException, InvalidDocumentTypeCandidateException {
        checkDocumentType(docType);
        Candidate candidateSearched = candidateRepository.findByDocumentTypeAndDocumentNumber(docType,docNumber).orElseThrow(()->new EntityNotFoundException("Candidate not found"));
        List<TechnologyProjection> technologyProjections = new ArrayList<>();
        technologyProjections.addAll(candidateTechnologyRepository.getTechnologiesByCandidateId(candidateSearched.getId()));
        Map<String,List<TechnologyProjection>> map = new HashMap<>();
        map.put(candidateSearched.getFirstName()+ " " + candidateSearched.getLastName(), technologyProjections);
        logger.info("retrieving technologies from candidate db");
        return map;
    }
}
