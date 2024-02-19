package com.imuzio.nmuzio.controller;

import com.imuzio.nmuzio.exception.DuplicatedCandidateTechnologyRelationException;
import com.imuzio.nmuzio.exception.DuplicatedDocumentNumberAndTypeCandidateException;
import com.imuzio.nmuzio.exception.EntityNotFoundException;
import com.imuzio.nmuzio.exception.InvalidDateCandidateException;
import com.imuzio.nmuzio.exception.InvalidDocumentTypeCandidateException;
import com.imuzio.nmuzio.model.dto.CandidateDto;
import com.imuzio.nmuzio.model.entity.Candidate;
import com.imuzio.nmuzio.model.entity.CandidateTechnology;
import com.imuzio.nmuzio.projection.TechnologyProjection;
import com.imuzio.nmuzio.service.CandidateService;
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
@RequestMapping("/candidates")
public class CandidateController {


    private CandidateService candidateService;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @GetMapping
    @Operation(summary = "Get all registered candidates", description = "this endpoint retrieves all the registered candidates in the database as dtos")
    public ResponseEntity<List<CandidateDto>> getAll(){
        return new ResponseEntity<>(candidateService.getAll(), HttpStatus.OK);
    }


    @GetMapping("/{candidateId}")
    @Operation(summary = "Get a registered candidate with a certain id", description = "this endpoint retrieves a candidate with a certain id as a dto, in case of not finding it the response is a 404 not found")
    public ResponseEntity<CandidateDto> getById(@PathVariable("candidateId") Long candidateId) throws EntityNotFoundException {
        return new ResponseEntity<>(candidateService.getById(candidateId), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Creates a new candidates", description = "this endpoint creates a new Candidate and retrieves him, in case of duplication of info or invalid params throws a 400 bad request")
    public ResponseEntity<Candidate> create (@Valid @RequestBody CandidateDto candidateDto) throws DuplicatedDocumentNumberAndTypeCandidateException, InvalidDocumentTypeCandidateException, InvalidDateCandidateException {
        return new ResponseEntity<>(candidateService.create(candidateDto),HttpStatus.CREATED);
    }

    @PutMapping("/{candidateId}")
    @Operation(summary = "Updates an existing candidate", description = "this endpoint updates a candidate and retreives the updated version, in case of not finding the candidate by the id throws 404 not found, in case of invalid params throws 400 bad request")
    public ResponseEntity<Candidate> update (@Valid @RequestBody CandidateDto candidateDto, @PathVariable("candidateId") Long candidateId) throws DuplicatedDocumentNumberAndTypeCandidateException, InvalidDocumentTypeCandidateException, InvalidDateCandidateException {
        return new ResponseEntity<>(candidateService.update(candidateDto,candidateId),HttpStatus.OK);
    }

    @DeleteMapping("/{candidateId}")
    @Operation(summary = "Deletes a candidate", description = "this endpoint deletes a candidate by his id.")
    public void delete (@PathVariable("candidateId") Long candidateId){
        candidateService.delete(candidateId);
    }

    @PutMapping("/{candidateId}/{technologyId}/{yearsExp}")
    @Operation(summary = "Adds a technology to the candidate's technology list", description = "this endpoint adds a technology to the candidate's technology list, with his years of experience. In case the relation already exists throws 400 bas request")
    public ResponseEntity<CandidateTechnology> addTechnology (@PathVariable("candidateId") Long candidateId, @PathVariable("technologyId") Long technologyId, @PathVariable("yearsExp") Integer yearsExp) throws EntityNotFoundException, DuplicatedCandidateTechnologyRelationException{
            return new ResponseEntity<>(candidateService.addTechnology(candidateId,technologyId,yearsExp),HttpStatus.OK);
    }

    @GetMapping("/{docType}/{docNumber}")
    @Operation(summary = "Get all technologies from candidate's technology list.", description = "this endpoint retrieves a list of projections of all the technologies that are related to him")
    public ResponseEntity<Map<String, List<TechnologyProjection>>> viewTechsByCandidateDocument(@PathVariable("docType") String docType, @PathVariable("docNumber") Integer docNumber) throws EntityNotFoundException, InvalidDocumentTypeCandidateException {
            return new ResponseEntity<>(candidateService.viewTechnologiesByCandidateDocument(docType,docNumber),HttpStatus.OK);
    }
}
