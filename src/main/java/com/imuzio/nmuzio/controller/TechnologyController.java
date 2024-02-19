package com.imuzio.nmuzio.controller;

import com.imuzio.nmuzio.exception.DuplicatedCandidateTechnologyRelationException;
import com.imuzio.nmuzio.exception.DuplicatedNameAndVersionTechnologyException;
import com.imuzio.nmuzio.exception.EntityNotFoundException;
import com.imuzio.nmuzio.exception.InvalidNameTechnologyException;
import com.imuzio.nmuzio.model.dto.TechnologyDto;
import com.imuzio.nmuzio.model.entity.CandidateTechnology;
import com.imuzio.nmuzio.model.entity.Technology;
import com.imuzio.nmuzio.projection.CandidateProjection;
import com.imuzio.nmuzio.service.TechnologyService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/technologies")
public class TechnologyController {


    TechnologyService technologyService;

    public TechnologyController(TechnologyService technologyService) {
        this.technologyService = technologyService;
    }

    @GetMapping
    @Operation(summary = "Get all registered technologies", description = "this endpoint retrieves all the registered technologies in the database as dtos")
    public ResponseEntity<List<TechnologyDto>> getAll(){
        return new ResponseEntity<>(technologyService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{technologyId}")
    @Operation(summary = "Get a registered technology with a certain id", description = "this endpoint retrieves a technology with a certain id as a dto, in case of not finding it the response is a 404 not found")
    public ResponseEntity<TechnologyDto> getById(@PathVariable("technologyId") Long id) throws EntityNotFoundException {
        return new ResponseEntity<>(technologyService.getById(id), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Creates a new technology and version", description = "this endpoint creates a new technology and retrieves her, in case of duplication of info or invalid params throws a 400 bad request")
    public ResponseEntity<Technology> create (@Valid @RequestBody TechnologyDto technologyDto) throws InvalidNameTechnologyException, DuplicatedNameAndVersionTechnologyException {
        return new ResponseEntity<>(technologyService.create(technologyDto), HttpStatus.CREATED);
    }


    @PutMapping("/{techonologyId}")
    @Operation(summary = "Updates an existing technology", description = "this endpoint updates a technology and retreives the updated version, in case of not finding the technology by the id throws 404 not found, in case of invalid params throws 400 bad request")
    public ResponseEntity<Technology> update (@Valid @RequestBody TechnologyDto technologyDto, @PathVariable("techonologyId") Long id) throws InvalidNameTechnologyException, DuplicatedNameAndVersionTechnologyException {
        return new ResponseEntity<>(technologyService.update(technologyDto,id), HttpStatus.OK);
    }

    @DeleteMapping("/{technologyId}")
    @Operation(summary = "Deletes a technology", description = "this endpoint deletes a technology by her id.")
    public void delete (@PathVariable("technologyId") Long id){
        technologyService.delete(id);
    }

    @PutMapping("/{technologyId}/{candidateId}/{yearsExp}")
    @Operation(summary = "Adds a candidate to the technology's candidate list", description = "this endpoint adds a candidate to the technology's candidate list, with his years of experience. In case the relation already exists throws 400 bas request")
    public ResponseEntity<CandidateTechnology> addCandidate (@PathVariable("technologyId") Long technologyId, @PathVariable("candidateId") Long candidateId, @PathVariable("yearsExp") Integer yearsExp) throws DuplicatedCandidateTechnologyRelationException, EntityNotFoundException {
            return new ResponseEntity<>(technologyService.addCandidate(technologyId,candidateId,yearsExp), HttpStatus.OK);
    }

    @GetMapping("viewcandidates/{techName}")
    @Operation(summary = "Get all candidates from technology's candidate list", description = "this endpoint retrieves a list of projections of all the candidates that are related to her")
    public ResponseEntity<Map<String, List<CandidateProjection>>> viewCandidatesByTechName(@PathVariable("techName")String name) throws EntityNotFoundException {
        return new ResponseEntity<>(technologyService.viewCandidatesByTechnologyName(name), HttpStatus.OK);
    }

    @GetMapping("viewcandidates/{techName}/{techVersion}")
    @Operation(summary = "Get all candidates from technology's candidate list from a specific version.", description = "this endpoint retrieves a list of projections of all the candidates that are related to her")
    public ResponseEntity<Map<String, List<CandidateProjection>>> viewCandidatesByTechNameAndVersion(@PathVariable("techName")String name, @PathVariable("techVersion")String version) throws EntityNotFoundException {
        return new ResponseEntity<>(technologyService.viewCandidatesByTechnologyNameAndVersion(name,version), HttpStatus.OK);
    }
}
