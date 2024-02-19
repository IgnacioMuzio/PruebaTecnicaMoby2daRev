package com.imuzio.nmuzio.TestUtil;

import com.imuzio.nmuzio.model.dto.CandidateDto;
import com.imuzio.nmuzio.model.dto.TechnologyDto;
import com.imuzio.nmuzio.model.entity.Candidate;
import com.imuzio.nmuzio.model.entity.CandidateTechnology;
import com.imuzio.nmuzio.model.entity.Technology;
import com.imuzio.nmuzio.projection.CandidateProjection;
import com.imuzio.nmuzio.projection.TechnologyProjection;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class Data {

    public static Technology getTechnology(){
        return Technology.builder().id(1L).name("java").version("19").build();
    }

    public static TechnologyDto getWrongTechnologyDto(){
        return TechnologyDto.builder().name("javan").version("19").build();
    }

    public static TechnologyDto getTechnologyDto(){
        return TechnologyDto.builder().name("java").version("19").build();
    }

    public static List<TechnologyDto> getTechnologyDtoList(){
        return Arrays.asList(getTechnologyDto());
    }

    public static List<Technology> getTechnologiesList(){return Arrays.asList(getTechnology());}

    public static TechnologyProjection getTechnologyProjection(){
        return new TechnologyProjection() {
            @Override
            public String getName() {
                return "java";
            }

            @Override
            public String getVersion() {
                return "19";
            }

            @Override
            public Integer getYearsOfExperience() {
                return 3;
            }
        };
    }

    public static List<TechnologyProjection> getTechnologyProjectionList(){
        return Arrays.asList(getTechnologyProjection());
    }

    public static Candidate getCandidate(){
        return Candidate.builder().id(1L)
                .birthDate(LocalDate.parse("2000-06-15"))
                .documentType("DNI").documentNumber(42678588)
                .firstName("Nacho").lastName("Muzio")
                .build();
    }

    public static CandidateDto getWrongDateCandidate(){
        return CandidateDto.builder()
                .birthDate(LocalDate.parse("2010-06-15"))
                .documentType("DNI").documentNumber(42678588)
                .firstName("Nacho").lastName("Muzio")
                .build();
    }

    public static CandidateDto getWrongDocumentTypeCandidate(){
        return CandidateDto.builder()
                .birthDate(LocalDate.parse("2000-06-15"))
                .documentType("NDI").documentNumber(42678588)
                .firstName("Nacho").lastName("Muzio")
                .build();
    }

    public static Candidate getCandidateWrongBirthdate(){
        return Candidate.builder().id(1L)
                .birthDate(LocalDate.parse("2010-06-15"))
                .documentType("DNI").documentNumber(42678588)
                .firstName("Nacho").lastName("Muzio")
                .build();
    }

    public static CandidateDto getCandidateDto(){
        return CandidateDto.builder()
                .birthDate(LocalDate.parse("2000-06-15"))
                .documentType("DNI").documentNumber(42678588)
                .firstName("Nacho").lastName("Muzio")
                .build();
    }

    public static List<CandidateDto> getCandidateDtoList(){
        return Arrays.asList(getCandidateDto());
    }

    public static List<Candidate> getCandidateList(){return Arrays.asList(getCandidate());}

    public static CandidateTechnology getCandidateTechnology(){
        CandidateTechnology candidateTechnology = new CandidateTechnology();
        candidateTechnology.setId(1L);
        candidateTechnology.setCandidate(getCandidate());
        candidateTechnology.setTechnology(getTechnology());
        candidateTechnology.setYearsOfExperience(1);
        return candidateTechnology;
    }

    public static List<CandidateTechnology> getCandidateTechnologyList(){
        return Arrays.asList(getCandidateTechnology());
    }

    public static CandidateProjection getCandidateProjection(){
        return new CandidateProjection() {
            @Override
            public String getFirstNameAndLastName() {
                return "Ignacio Muzio";
            }

            @Override
            public String getDocumentTypeAndNumber() {
                return "DNI 42678588";
            }

            @Override
            public LocalDate getBirthDate() {
                return LocalDate.of(2000,06,15);
            }

            @Override
            public Integer getYearsOfExperience() {
                return 3;
            }

            @Override
            public String getVersion() {
                return "19";
            }
        };
    }

    public static List<CandidateProjection> getCandidateProjectionList(){
        return Arrays.asList(getCandidateProjection());
    }


}
