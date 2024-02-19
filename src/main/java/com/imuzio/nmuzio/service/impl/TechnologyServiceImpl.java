package com.imuzio.nmuzio.service.impl;

import com.imuzio.nmuzio.exception.DuplicatedCandidateTechnologyRelationException;
import com.imuzio.nmuzio.exception.DuplicatedNameAndVersionTechnologyException;
import com.imuzio.nmuzio.exception.EntityNotFoundException;
import com.imuzio.nmuzio.exception.InvalidNameTechnologyException;
import com.imuzio.nmuzio.model.dto.TechnologyDto;
import com.imuzio.nmuzio.model.entity.Candidate;
import com.imuzio.nmuzio.model.entity.CandidateTechnology;
import com.imuzio.nmuzio.model.entity.Technology;
import com.imuzio.nmuzio.model.enumerator.TechnologyName;
import com.imuzio.nmuzio.projection.CandidateProjection;
import com.imuzio.nmuzio.repository.CandidateRepository;
import com.imuzio.nmuzio.repository.CandidateTechnologyRepository;
import com.imuzio.nmuzio.repository.TechnologyRepository;
import com.imuzio.nmuzio.service.TechnologyService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class TechnologyServiceImpl implements TechnologyService {


    private TechnologyRepository technologyRepository;

    private CandidateRepository candidateRepository;

    private CandidateTechnologyRepository candidateTechnologyRepository;

    Logger logger = Logger.getLogger(TechnologyServiceImpl.class);

    public TechnologyServiceImpl(TechnologyRepository technologyRepository, CandidateRepository candidateRepository, CandidateTechnologyRepository candidateTechnologyRepository) {
        this.technologyRepository = technologyRepository;
        this.candidateRepository = candidateRepository;
        this.candidateTechnologyRepository = candidateTechnologyRepository;
    }

    public List<TechnologyDto> getAll(){
        return technologyRepository.findAll().stream().map(this::technologyDtoBuilder).toList();
    }

    public TechnologyDto getById (Long id) throws EntityNotFoundException {
        Optional<Technology> technology = technologyRepository.findById(id);
        if(!technology.isPresent()){
            logger.error("tech not found");
            throw new EntityNotFoundException("Technology not found.");
        }
        logger.info("Tech " + technology.get().getName() + "found");
        return technologyDtoBuilder(technology.get());
    }

    public Technology create (TechnologyDto technologyDto) throws InvalidNameTechnologyException, DuplicatedNameAndVersionTechnologyException {
        Technology technology = technologyBuilder(technologyDto,null);
        checkTech(technology);
        logger.info("saving tech");
        technologyRepository.save(technology);
        return technology;
    }

    public Technology update (TechnologyDto technologyDto, Long id) throws InvalidNameTechnologyException, DuplicatedNameAndVersionTechnologyException {
        Technology technology = technologyBuilder(technologyDto,id);
        checkTech(technology);
        logger.info("saving tech");
        technologyRepository.save(technology);
        return technology;
    }

    public void delete (Long id){
        technologyRepository.deleteById(id);
    }

    public TechnologyDto technologyDtoBuilder (Technology technology){
        TechnologyDto technologyDto = new TechnologyDto(technology.getName(),technology.getVersion());
        return technologyDto;
    }

    public Technology technologyBuilder (TechnologyDto technologyDto, Long id){
        Technology technology = new Technology();
        technology.setId(id);
        technology.setName(technologyDto.name().toLowerCase());
        technology.setVersion(technologyDto.version());
        return technology;
    }

    private void checkTech (Technology technology) throws InvalidNameTechnologyException, DuplicatedNameAndVersionTechnologyException {
        checkTechName(technology.getName());
        Optional<Technology> technologyCheck = technologyRepository.findByNameAndVersion(technology.getName(),technology.getVersion());
        if(technologyCheck.isPresent()){
            if(technology.equals(technologyCheck.get()) && !Objects.equals(technology.getId(),technologyCheck.get().getId())){
                throw new DuplicatedNameAndVersionTechnologyException("This version is already registered");
            }
        }
    }

    private void checkTechName (String name) throws InvalidNameTechnologyException {
        for(TechnologyName nameEnum : TechnologyName.values()){
            if(name.equalsIgnoreCase(nameEnum.toString())){
                return;
            }
        }
        throw new InvalidNameTechnologyException("This technology does not fit with what we are looking for...");
    }

    public CandidateTechnology addCandidate (Long technologyId, Long candidateId,Integer yearsOfExperience) throws DuplicatedCandidateTechnologyRelationException, EntityNotFoundException {
        Technology technology = technologyBuilder(getById(technologyId),technologyId);
        Candidate candidate = candidateRepository.findById(candidateId).orElseThrow(()-> new EntityNotFoundException("Candidate not found"));

        if(!candidateTechnologyRepository.findByCandidateIdAndTechnologyId(candidateId,technologyId).isEmpty()){
            logger.error("already used");
            throw new DuplicatedCandidateTechnologyRelationException("This candidate is already registered on this technology");
        }

        CandidateTechnology candidateTechnology = new CandidateTechnology();

        candidateTechnology.setTechnology(technology);
        candidateTechnology.setCandidate(candidate);
        candidateTechnology.setYearsOfExperience(yearsOfExperience);
        candidateTechnologyRepository.save(candidateTechnology);
        technology.getCandidates().add(candidateTechnology);
        logger.info("association saved");
        technologyRepository.save(technology);
        return candidateTechnology;
    }

    public Map<String,List<CandidateProjection>> viewCandidatesByTechnologyName(String name) throws EntityNotFoundException {
        List<Technology> technologies = technologyRepository.findAllByName(name);
        if(technologies.isEmpty()){
            throw new EntityNotFoundException("Tech not found");
        }
        List<CandidateProjection> candidateProjections = new ArrayList<>();
        candidateProjections.addAll(candidateTechnologyRepository.getCandidatesByTecnologyName(technologies.get(0).getName()));
        Map<String,List<CandidateProjection>> map = new HashMap<>();
        map.put(technologies.get(0).getName(), candidateProjections);
        logger.info("retrieving candidate from technology db");
        return map;
    }

    public Map<String,List<CandidateProjection>> viewCandidatesByTechnologyNameAndVersion(String name, String version) throws EntityNotFoundException {
        Technology technology= technologyRepository.findByNameAndVersion(name,version).orElseThrow(()->new EntityNotFoundException("Technology not found."));
        List<CandidateProjection> candidateProjections = new ArrayList<>();
        candidateProjections.addAll(candidateTechnologyRepository.getCandidatesByTecnologyNameAndVersion(name,version));
        Map<String,List<CandidateProjection>> map = new HashMap<>();
        map.put(technology.getName() + " " + technology.getVersion() , candidateProjections);
        logger.info("retrieving candidate from technology db");
        return map;
    }
}
