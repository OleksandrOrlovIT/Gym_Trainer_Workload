package ua.orlov.gymtrainerworkload.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.orlov.gymtrainerworkload.dto.TrainerSummary;
import ua.orlov.gymtrainerworkload.dto.TrainerWorkload;
import ua.orlov.gymtrainerworkload.model.ActionType;
import ua.orlov.gymtrainerworkload.service.user.TrainerService;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TrainerControllerTest {

    @Mock
    private TrainerService trainerService;

    @InjectMocks
    private TrainerController trainerController;

    private MockMvc mockMvc;

    private static ObjectMapper objectMapper;

    @BeforeAll
    static void setObjectMapper(){
        objectMapper = new ObjectMapper();
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(trainerController).build();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void changeWorkLoadThenSuccess() throws Exception {
        TrainerWorkload trainerWorkload = new TrainerWorkload();
        trainerWorkload.setUsername("username");
        trainerWorkload.setFirstName("firstName");
        trainerWorkload.setLastName("lastName");
        trainerWorkload.setIsActive(true);
        trainerWorkload.setTrainingDate(LocalDate.MIN);
        trainerWorkload.setTrainingDuration(50L);
        trainerWorkload.setActionType(ActionType.ADD);

        mockMvc.perform(post("/api/v1/trainer/workload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainerWorkload)))
                .andExpect(status().isOk())
                .andReturn();

        verify(trainerService, times(1)).changeTrainerWorkload(any());
    }

    @Test
    void getSummaryMonth() throws Exception {
        when(trainerService.getTrainerSummary(any())).thenReturn(new TrainerSummary());

        mockMvc.perform(get("/api/v1/trainer/summary/month?username=user"))
                .andExpect(status().isOk())
                .andReturn();

        verify(trainerService, times(1)).getTrainerSummary(any());
    }
}
