package com.imuzio.nmuzio.service;

import com.imuzio.nmuzio.exception.DuplicatedCandidateTechnologyRelationException;
import com.imuzio.nmuzio.exception.DuplicatedNameAndVersionTechnologyException;
import com.imuzio.nmuzio.exception.EntityNotFoundException;
import com.imuzio.nmuzio.exception.InvalidNameTechnologyException;
import com.imuzio.nmuzio.model.dto.TechnologyDto;
import com.imuzio.nmuzio.model.entity.CandidateTechnology;
import com.imuzio.nmuzio.model.entity.Technology;
import com.imuzio.nmuzio.projection.CandidateProjection;

import java.util.List;
import java.util.Map;

public interface TechnologyService {

    List<TechnologyDto> getAll();

    TechnologyDto getById(Long id) throws EntityNotFoundException;

    Technology create (TechnologyDto technologyDto) throws InvalidNameTechnologyException, DuplicatedNameAndVersionTechnologyException;

    Technology update (TechnologyDto technologyDto, Long id) throws InvalidNameTechnologyException, DuplicatedNameAndVersionTechnologyException;

    void delete (Long id);

    CandidateTechnology addCandidate (Long technologyId, Long candidateId, Integer yearsOfExperience) throws DuplicatedCandidateTechnologyRelationException, EntityNotFoundException;

    public Map<String,List<CandidateProjection>> viewCandidatesByTechnologyName(String name) throws EntityNotFoundException;

    public Map<String,List<CandidateProjection>>  viewCandidatesByTechnologyNameAndVersion(String name, String version) throws EntityNotFoundException;
}
