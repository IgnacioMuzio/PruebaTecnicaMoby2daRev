package com.imuzio.nmuzio.service.impl;

import com.imuzio.nmuzio.exception.DuplicatedCandidateTechnologyRelationException;
import com.imuzio.nmuzio.exception.DuplicatedNameAndVersionTechnologyException;
import com.imuzio.nmuzio.exception.EntityNotFoundException;
import com.imuzio.nmuzio.exception.InvalidNameTechnologyException;
import com.imuzio.nmuzio.projection.CandidateProjection;
import com.imuzio.nmuzio.repository.CandidateRepository;
import com.imuzio.nmuzio.repository.CandidateTechnologyRepository;
import com.imuzio.nmuzio.repository.TechnologyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.imuzio.nmuzio.TestUtil.Data.getCandidate;
import static com.imuzio.nmuzio.TestUtil.Data.getCandidateProjectionList;
import static com.imuzio.nmuzio.TestUtil.Data.getCandidateTechnology;
import static com.imuzio.nmuzio.TestUtil.Data.getCandidateTechnologyList;
import static com.imuzio.nmuzio.TestUtil.Data.getTechnologiesList;
import static com.imuzio.nmuzio.TestUtil.Data.getTechnology;
import static com.imuzio.nmuzio.TestUtil.Data.getTechnologyDto;
import static com.imuzio.nmuzio.TestUtil.Data.getTechnologyDtoList;
import static com.imuzio.nmuzio.TestUtil.Data.getWrongTechnologyDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TechnologyServiceImplTest {


    @Mock
    TechnologyRepository technologyRepository;

    @Mock
    CandidateRepository candidateRepository;

    @Mock
    CandidateTechnologyRepository candidateTechnologyRepository;

    @InjectMocks
    TechnologyServiceImpl technologyService;


    @Test
    void getAllTechs_ReturnsTechnologiesListTest(){

        when(technologyRepository.findAll()).thenReturn(getTechnologiesList());

        assertEquals(getTechnologyDtoList(),technologyService.getAll());
    }

    @Test
    void getTechnologyById_ReturnsTechnologyDtoTest() throws EntityNotFoundException {
        when(technologyRepository.findById(1L)).thenReturn(Optional.ofNullable(getTechnology()));

        assertEquals(getTechnologyDto(),technologyService.getById(1L));
    }

    @Test
    void getTechnologyById_ThrowsEntityNotFoundTest(){
        when(technologyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, ()-> technologyService.getById(1L));
    }

    @Test
    void createTechnology_ReturnsTechnologyCreatedTest() throws InvalidNameTechnologyException, DuplicatedNameAndVersionTechnologyException {
        when(technologyRepository.save(getTechnology())).thenReturn(getTechnology());

        assertEquals(getTechnology(),technologyService.create(getTechnologyDto()));
    }

    @Test
    void checkTechName_ThrowsInvalidNameTechnologyTest(){
        assertThrows(InvalidNameTechnologyException.class,()->technologyService.create(getWrongTechnologyDto()));
    }

    @Test
    void checkTech_ThrowsDuplicatedNameAndVersionTechnologyTest(){
        when(technologyRepository.findByNameAndVersion(getTechnology().getName(), getTechnology().getVersion()))
                .thenReturn(Optional.ofNullable(getTechnology()));

        assertThrows(DuplicatedNameAndVersionTechnologyException.class,()->technologyService.update(getTechnologyDto(),2L));
    }

    @Test
    void updateTechnology_ReturnsTechUpdatedTest() throws InvalidNameTechnologyException, DuplicatedNameAndVersionTechnologyException {
        when(technologyRepository.save(getTechnology())).thenReturn(getTechnology());

        assertEquals(getTechnology(),technologyService.update(getTechnologyDto(),1L));
    }

    @Test
    void deleteTechnology_ReturnsVoidTest(){
        technologyService.delete(1L);
        verify(technologyRepository).deleteById(1L);
    }

    @Test
    void technologyBuilder_ReturnsTechnologyTest(){
        assertEquals(getTechnology(),technologyService.technologyBuilder(getTechnologyDto(),1L));
    }

    @Test
    void technologyDtoBuilder_ReturnsTechnologyDtoTest(){
        assertEquals(getTechnologyDto(),technologyService.technologyDtoBuilder(getTechnology()));
    }

    @Test
    void addCandidate_ReturnsCandidateTechnologyTest() throws EntityNotFoundException, DuplicatedCandidateTechnologyRelationException {
        when(candidateRepository.findById(1L)).thenReturn(Optional.ofNullable(getCandidate()));
        when(technologyRepository.findById(1L)).thenReturn(Optional.ofNullable(getTechnology()));
        when(candidateTechnologyRepository.save(getCandidateTechnology())).thenReturn(getCandidateTechnology());
        assertEquals(getCandidateTechnology(),technologyService.addCandidate(1L,1L,1));
    }

    @Test
    void addTechnology_ThrowsDuplicatedCandidateTechnologyRelationTest() throws EntityNotFoundException, DuplicatedCandidateTechnologyRelationException {
        when(candidateRepository.findById(1L)).thenReturn(Optional.ofNullable(getCandidate()));
        when(technologyRepository.findById(1L)).thenReturn(Optional.ofNullable(getTechnology()));
        when(candidateTechnologyRepository.findByCandidateIdAndTechnologyId(1L,1L)).thenReturn(getCandidateTechnologyList());
        assertThrows(DuplicatedCandidateTechnologyRelationException.class, () -> technologyService.addCandidate(1L,1L,3));
    }

    @Test
    void viewCandidatesByTechnologyName_returnsCandidateProjectionsTest() throws EntityNotFoundException {
        List<CandidateProjection> candidateProjectionList = getCandidateProjectionList();
        when(technologyRepository.findAllByName("java")).thenReturn(getTechnologiesList());
        when(candidateTechnologyRepository.getCandidatesByTecnologyName("java")).thenReturn(candidateProjectionList);

        Map<String, List<CandidateProjection>> map = new HashMap<>();
        map.put(getTechnologiesList().getFirst().getName(),candidateProjectionList);

        assertEquals(map,technologyService.viewCandidatesByTechnologyName("java"));
    }

    @Test
    void viewCandidatesByTechnologyName_throwsEntityExceptionTest() throws EntityNotFoundException {
        when(technologyRepository.findAllByName("java")).thenReturn(Collections.emptyList());

        assertThrows(EntityNotFoundException.class,()-> technologyService.viewCandidatesByTechnologyName("java"));
    }

    @Test
    void viewCandidatesByTechnologyNameAndVersion_returnsCandidateProjectionsTest() throws EntityNotFoundException {
        List<CandidateProjection> candidateProjectionList = getCandidateProjectionList();
        when(technologyRepository.findByNameAndVersion("java","19")).thenReturn(Optional.ofNullable(getTechnology()));
        when(candidateTechnologyRepository.getCandidatesByTecnologyNameAndVersion("java","19")).thenReturn(candidateProjectionList);

        Map<String, List<CandidateProjection>> map = new HashMap<>();
        map.put(getTechnology().getName() +" "+getTechnology().getVersion() ,candidateProjectionList);

        assertEquals(map,technologyService.viewCandidatesByTechnologyNameAndVersion("java","19"));
    }
}