package com.imuzio.nmuzio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imuzio.nmuzio.exception.DuplicatedCandidateTechnologyRelationException;
import com.imuzio.nmuzio.exception.DuplicatedNameAndVersionTechnologyException;
import com.imuzio.nmuzio.exception.EntityNotFoundException;
import com.imuzio.nmuzio.exception.InvalidNameTechnologyException;
import com.imuzio.nmuzio.projection.CandidateProjection;
import com.imuzio.nmuzio.service.impl.TechnologyServiceImpl;
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
import static com.imuzio.nmuzio.TestUtil.Data.getCandidateProjectionList;
import static com.imuzio.nmuzio.TestUtil.Data.getCandidateTechnology;
import static com.imuzio.nmuzio.TestUtil.Data.getTechnology;
import static com.imuzio.nmuzio.TestUtil.Data.getTechnologyDto;
import static com.imuzio.nmuzio.TestUtil.Data.getTechnologyDtoList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@WebMvcTest(controllers = TechnologyController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class TechnologyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TechnologyServiceImpl technologyService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void getAllTechnologies_ReturnsTechnologiesListTest() throws Exception {

        when(technologyService.getAll()).thenReturn(getTechnologyDtoList());

        ResultActions response = mockMvc.perform(get("/technologies"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(getTechnologyDtoList().size())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getTechnologyById_ReturnsTechnologyDtoTest() throws Exception {
        when(technologyService.getById(1L)).thenReturn(getTechnologyDto());

        ResultActions response = mockMvc.perform(get("/technologies/1"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(getTechnologyDto().name())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getTechnologyById_ThrowsTechnologyNotFoundTest() throws Exception {
        when(technologyService.getById(1L)).thenThrow(EntityNotFoundException.class);

        ResultActions response = mockMvc.perform(get("/technologies/1"));

        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void createTechnology_returnsCreatedTest() throws Exception {
        when(technologyService.create(getTechnologyDto())).thenReturn(getTechnology());

        ResultActions response = mockMvc.perform(post("/technologies").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(getTechnologyDto())));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(getTechnology().getName())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void createTechnology_throwsInvalidNameTechnologyTest() throws Exception {
        when(technologyService.create(getTechnologyDto())).thenThrow(InvalidNameTechnologyException.class);

        ResultActions response = mockMvc.perform(post("/technologies").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(getTechnologyDto())));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void createTechnology_throwsDuplicatedNameAndVersionTest() throws Exception {
        when(technologyService.create(getTechnologyDto())).thenThrow(DuplicatedNameAndVersionTechnologyException.class);

        ResultActions response = mockMvc.perform(post("/technologies").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(getTechnologyDto())));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void updateTechnology_returnsUpdatedTest() throws Exception {
        when(technologyService.update(getTechnologyDto(),1L)).thenReturn(getTechnology());

        ResultActions response = mockMvc.perform(put("/technologies/1").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(getTechnologyDto())));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(getTechnology().getName())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void deleteTechnology_voidTest() throws Exception {

        ResultActions response = mockMvc.perform(delete("/technologies/1"));

        verify(technologyService).delete(1L);

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    void addCandidateToTechnologyList_ReturnCandidateTechnologyRelationCreatedTest() throws Exception {
        when(technologyService.addCandidate(getCandidate().getId(),getTechnology().getId(),1)).thenReturn(getCandidateTechnology());

        ResultActions response = mockMvc.perform(put("/technologies/1/1/1"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.yearsOfExperience", CoreMatchers.is(getCandidateTechnology().getYearsOfExperience())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void addCandidateToTechnologyList_throwsDuplicatedRelationTest() throws Exception {
        when(technologyService.addCandidate(getCandidate().getId(),getTechnology().getId(),1)).thenThrow(DuplicatedCandidateTechnologyRelationException.class);

        ResultActions response = mockMvc.perform(put("/technologies/1/1/1"));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void viewCandidatesByTechnologyName_returnCandidatesProjectionsTest() throws Exception {
        HashMap<String, List<CandidateProjection>> candidateProjectionHashMap = new HashMap<>();

        candidateProjectionHashMap.put(getTechnology().getName(),getCandidateProjectionList());

        when(technologyService.viewCandidatesByTechnologyName("java")).thenReturn(candidateProjectionHashMap);

        ResultActions response = mockMvc.perform(get("/technologies/viewcandidates/java"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()",CoreMatchers.is(candidateProjectionHashMap.size())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void viewCandidatesByTechnologyNameAndVersion_returnCandidatesProjectionsTest() throws Exception {
        HashMap<String, List<CandidateProjection>> candidateProjectionHashMap = new HashMap<>();

        candidateProjectionHashMap.put(getTechnology().getName()+ " " + getTechnology().getVersion(),getCandidateProjectionList());

        when(technologyService.viewCandidatesByTechnologyNameAndVersion("java","21")).thenReturn(candidateProjectionHashMap);

        ResultActions response = mockMvc.perform(get("/technologies/viewcandidates/java/21"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()",CoreMatchers.is(candidateProjectionHashMap.size())))
                .andDo(MockMvcResultHandlers.print());
    }
}