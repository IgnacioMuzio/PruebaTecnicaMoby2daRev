package com.imuzio.nmuzio.service.impl;

import com.imuzio.nmuzio.exception.DuplicatedCandidateTechnologyRelationException;
import com.imuzio.nmuzio.exception.DuplicatedDocumentNumberAndTypeCandidateException;
import com.imuzio.nmuzio.exception.EntityNotFoundException;
import com.imuzio.nmuzio.exception.InvalidDateCandidateException;
import com.imuzio.nmuzio.exception.InvalidDocumentTypeCandidateException;
import com.imuzio.nmuzio.model.entity.Candidate;
import com.imuzio.nmuzio.projection.TechnologyProjection;
import com.imuzio.nmuzio.repository.CandidateRepository;
import com.imuzio.nmuzio.repository.CandidateTechnologyRepository;
import com.imuzio.nmuzio.repository.TechnologyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.imuzio.nmuzio.TestUtil.Data.getCandidate;
import static com.imuzio.nmuzio.TestUtil.Data.getCandidateDto;
import static com.imuzio.nmuzio.TestUtil.Data.getCandidateDtoList;
import static com.imuzio.nmuzio.TestUtil.Data.getCandidateList;
import static com.imuzio.nmuzio.TestUtil.Data.getCandidateTechnology;
import static com.imuzio.nmuzio.TestUtil.Data.getCandidateTechnologyList;
import static com.imuzio.nmuzio.TestUtil.Data.getTechnology;
import static com.imuzio.nmuzio.TestUtil.Data.getTechnologyProjectionList;
import static com.imuzio.nmuzio.TestUtil.Data.getWrongDateCandidate;
import static com.imuzio.nmuzio.TestUtil.Data.getWrongDocumentTypeCandidate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CandidateServiceImplTest {

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private TechnologyRepository technologyRepository;

    @Mock
    private CandidateTechnologyRepository candidateTechnologyRepository;

    @InjectMocks
    private CandidateServiceImpl candidateService;

    @Test
    void getAllCandidates_ReturnsCandidatesListTest(){
        when(candidateRepository.findAll()).thenReturn(getCandidateList());

        assertEquals(getCandidateDtoList(),candidateService.getAll());
    }

    @Test
    void getCandidateById_ReturnsCandidateDtoTest() throws EntityNotFoundException {
        when(candidateRepository.findById(1L)).thenReturn(Optional.ofNullable(getCandidate()));

        assertEquals(getCandidateDto(),candidateService.getById(1L));
    }

    @Test
    void getCandidateById_ThrowsCandidateNotFoundTest() throws EntityNotFoundException {

        when(candidateRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> candidateService.getById(1L));
    }

    @Test
    void createCandidate_ReturnsCandidateTest() throws DuplicatedDocumentNumberAndTypeCandidateException, InvalidDateCandidateException, InvalidDocumentTypeCandidateException {
        when(candidateRepository.save(getCandidate())).thenReturn(getCandidate());

        Candidate candidateCreated = candidateService.create(getCandidateDto());

        assertEquals(getCandidate(),candidateCreated);
    }

    @Test
    void createCandidate_ReturnsDuplicatedValuesException() throws DuplicatedDocumentNumberAndTypeCandidateException, InvalidDateCandidateException, InvalidDocumentTypeCandidateException {
        when(candidateRepository.findByDocumentTypeAndDocumentNumber(any(),any())).thenReturn(Optional.ofNullable(getCandidate()));

        assertThrows(DuplicatedDocumentNumberAndTypeCandidateException.class, ()->candidateService.create(getCandidateDto()));
    }

    @Test
    void createCandidate_ReturnsInvalidTypeException() throws DuplicatedDocumentNumberAndTypeCandidateException, InvalidDateCandidateException, InvalidDocumentTypeCandidateException {
        assertThrows(InvalidDocumentTypeCandidateException.class, ()->candidateService.create(getWrongDocumentTypeCandidate()));
    }

    @Test
    void createCandidate_ReturnsInvalidDateException() throws DuplicatedDocumentNumberAndTypeCandidateException, InvalidDateCandidateException, InvalidDocumentTypeCandidateException {
        assertThrows(InvalidDateCandidateException.class, ()->candidateService.create(getWrongDateCandidate()));
    }

    @Test
    void updateCandidate_ReturnsCandidateTest() throws DuplicatedDocumentNumberAndTypeCandidateException, InvalidDateCandidateException, InvalidDocumentTypeCandidateException {
        when(candidateRepository.save(getCandidate())).thenReturn(getCandidate());

        assertEquals(getCandidate(),candidateService.update(getCandidateDto(),1L));
    }

    @Test
    void deleteCandidate_VoidTest(){
        candidateService.delete(1L);
        verify(candidateRepository,times(1)).deleteById(1L);
    }

    @Test
    void candidateBuilder_ReturnsCandidateTest(){
        assertEquals(getCandidate(),candidateService.candidateBuilder(getCandidateDto(),1L));
    }

    @Test
    void candidateDtoBuilder_ReturnsCandidateDtoTest(){
        assertEquals(getCandidateDto(),candidateService.candidateDtoBuilder(getCandidate()));
    }

    @Test
    void addTechnology_ReturnsCandidateTechnologyTest() throws EntityNotFoundException, DuplicatedCandidateTechnologyRelationException {
        when(candidateRepository.findById(1L)).thenReturn(Optional.ofNullable(getCandidate()));
        when(technologyRepository.findById(1L)).thenReturn(Optional.ofNullable(getTechnology()));


        assertEquals(getCandidateTechnology(),candidateService.addTechnology(1L,1L,1));
    }


    @Test
    void addTechnology_ThrowsDuplicatedCandidateTechnologyRelationTest() throws EntityNotFoundException, DuplicatedCandidateTechnologyRelationException {
        when(candidateRepository.findById(1L)).thenReturn(Optional.ofNullable(getCandidate()));
        when(technologyRepository.findById(1L)).thenReturn(Optional.ofNullable(getTechnology()));
        when(candidateTechnologyRepository.findByCandidateIdAndTechnologyId(1L,1L)).thenReturn(getCandidateTechnologyList());
        assertThrows(DuplicatedCandidateTechnologyRelationException.class, () -> candidateService.addTechnology(1L,1L,1));
    }

    @Test
    void viewTechnologiesByCandidateDocumentTest() throws EntityNotFoundException, InvalidDocumentTypeCandidateException {
        List<TechnologyProjection> technologyProjections = getTechnologyProjectionList();
        when(candidateRepository.findByDocumentTypeAndDocumentNumber(getCandidate().getDocumentType(), getCandidate().getDocumentNumber())).thenReturn(Optional.ofNullable(getCandidate()));
        when(candidateTechnologyRepository.getTechnologiesByCandidateId(getCandidate().getId())).thenReturn(technologyProjections);

        Map<String,List<TechnologyProjection>> map = new HashMap<>();
        map.put(getCandidate().getFirstName()+ " " + getCandidate().getLastName(), technologyProjections);

        assertEquals(map,candidateService.viewTechnologiesByCandidateDocument("DNI",42678588));
    }
}