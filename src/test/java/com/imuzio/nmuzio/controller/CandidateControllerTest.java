package com.imuzio.nmuzio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imuzio.nmuzio.exception.DuplicatedDocumentNumberAndTypeCandidateException;
import com.imuzio.nmuzio.exception.EntityNotFoundException;
import com.imuzio.nmuzio.exception.InvalidDateCandidateException;
import com.imuzio.nmuzio.exception.InvalidDocumentTypeCandidateException;
import com.imuzio.nmuzio.projection.TechnologyProjection;
import com.imuzio.nmuzio.service.CandidateService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.List;

import static com.imuzio.nmuzio.TestUtil.Data.getCandidate;
import static com.imuzio.nmuzio.TestUtil.Data.getCandidateDto;
import static com.imuzio.nmuzio.TestUtil.Data.getCandidateDtoList;
import static com.imuzio.nmuzio.TestUtil.Data.getCandidateTechnology;
import static com.imuzio.nmuzio.TestUtil.Data.getTechnology;
import static com.imuzio.nmuzio.TestUtil.Data.getTechnologyProjectionList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@WebMvcTest(controllers = CandidateController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class CandidateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CandidateService candidateService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllCandidates_ReturnsCandidatesTest() throws Exception {
        when(candidateService.getAll()).thenReturn(getCandidateDtoList());

        ResultActions response = mockMvc.perform(get("/candidates"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(getCandidateDtoList().size())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getCandidateById_ReturnsCandidateFoundTest() throws Exception {
        when(candidateService.getById(any())).thenReturn(getCandidateDto());

        ResultActions response = mockMvc.perform(get("/candidates/1"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(getCandidateDto().firstName())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getCandidateById_ThrowsCandidateNotFoundTest() throws Exception {
        when(candidateService.getById(any())).thenThrow(EntityNotFoundException.class);

        ResultActions response = mockMvc.perform(get("/candidates/1"));

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void createCandidate_ReturnsCreatedTest() throws Exception {
        when(candidateService.create(any())).thenReturn(getCandidate());

        ResultActions response = mockMvc.perform(post("/candidates").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(getCandidateDto())));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(getCandidate().getFirstName())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void createCandidate_throwsInvalidDocumentTypeTest() throws Exception {
        when(candidateService.create(any())).thenThrow(InvalidDocumentTypeCandidateException.class);

        ResultActions response = mockMvc.perform(post("/candidates").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(getCandidateDto())));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    void createCandidate_throwsInvalidDateTest() throws Exception {
        when(candidateService.create(any())).thenThrow(InvalidDateCandidateException.class);

        ResultActions response = mockMvc.perform(post("/candidates").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(getCandidateDto())));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    void createCandidate_throwsDuplicatedTypeAndNumberTest() throws Exception {
        when(candidateService.create(any())).thenThrow(DuplicatedDocumentNumberAndTypeCandidateException.class);

        ResultActions response = mockMvc.perform(post("/candidates").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(getCandidateDto())));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    void updateCandidate_ReturnsCandidateUpdatedTest() throws Exception {
        Long candidateId=1L;

        when(candidateService.update(getCandidateDto(),candidateId)).thenReturn(getCandidate());

        ResultActions response = mockMvc.perform(put("/candidates/1").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(getCandidateDto())));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(getCandidate().getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(getCandidate().getLastName())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void deleteCandidate_voidTest() throws Exception {
        ResultActions response = mockMvc.perform(delete("/candidates/1"));

        verify(candidateService).delete(1L);

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void addTechonologyToCandidate_ReturnsCandidateTechnologyRelationTest() throws Exception {
        when(candidateService.addTechnology(getCandidate().getId(),getTechnology().getId(),1)).thenReturn(getCandidateTechnology());

        ResultActions response = mockMvc.perform(put("/candidates/1/1/1"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.yearsOfExperience", CoreMatchers.is(getCandidateTechnology().getYearsOfExperience())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void viewTechnologiesByCandidateTypeAndNumberTest() throws Exception {
        HashMap<String, List<TechnologyProjection>> map = new HashMap<>();
        map.put(getCandidate().getFirstName() + " " + getCandidate().getLastName(),getTechnologyProjectionList());

        when(candidateService.viewTechnologiesByCandidateDocument(getCandidate().getDocumentType(),getCandidate().getDocumentNumber())).thenReturn(map);

        ResultActions response = mockMvc.perform(get("/candidates/DNI/42678588"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()",CoreMatchers.is(map.size())))
                .andDo(MockMvcResultHandlers.print());
    }
}