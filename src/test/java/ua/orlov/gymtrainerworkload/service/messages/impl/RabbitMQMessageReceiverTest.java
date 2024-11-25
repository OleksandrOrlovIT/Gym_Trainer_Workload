package ua.orlov.gymtrainerworkload.service.messages.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.orlov.gymtrainerworkload.dto.TrainerWorkload;
import ua.orlov.gymtrainerworkload.service.user.TrainerService;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RabbitMQMessageReceiverTest {

    private static final String TRAINER_WORKLOAD_SUBJECT_NAME = "Trainer workload";

    @Mock
    private TrainerService trainerService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private RabbitMQMessageReceiver rabbitMQMessageReceiver;

    @Test
    void receiveMessageGivenNoSubjectThenException() throws JsonProcessingException {
        when(objectMapper.readValue(anyString(), eq(Map.class))).thenReturn(new HashMap<>());

        assertDoesNotThrow(() -> rabbitMQMessageReceiver.receiveMessage(""));

        verify(objectMapper, times(1)).readValue(anyString(), eq(Map.class));
        verify(trainerService, times(0)).changeTrainerWorkload(any());
    }

    @Test
    void receiveMessageGivenWrongSubjectThenException() throws JsonProcessingException {
        HashMap<String, String> map = new HashMap<>();
        map.put("subject", TRAINER_WORKLOAD_SUBJECT_NAME + "asdasd");
        when(objectMapper.readValue(anyString(), eq(Map.class))).thenReturn(map);

        assertDoesNotThrow(() -> rabbitMQMessageReceiver.receiveMessage(""));

        verify(objectMapper, times(1)).readValue(anyString(), eq(Map.class));
        verifyNoInteractions(trainerService);
    }

    @Test
    void receiveMessageThenSuccess() throws JsonProcessingException {
        HashMap<String, String> map = new HashMap<>();
        map.put("subject", TRAINER_WORKLOAD_SUBJECT_NAME);
        map.put("content", "");
        when(objectMapper.readValue(anyString(), eq(Map.class))).thenReturn(map);
        when(objectMapper.readValue(anyString(), eq(TrainerWorkload.class))).thenReturn(new TrainerWorkload());

        assertDoesNotThrow(() -> rabbitMQMessageReceiver.receiveMessage(""));

        verify(objectMapper, times(1)).readValue(anyString(), eq(Map.class));
        verify(objectMapper, times(1)).readValue(anyString(), eq(TrainerWorkload.class));
        verify(trainerService, times(1)).changeTrainerWorkload(any());
    }

    @Test
    void changeTrainerWorkloadThenException() throws JsonProcessingException {
        when(objectMapper.readValue(anyString(), eq(TrainerWorkload.class))).thenThrow(new RuntimeException());

        assertDoesNotThrow(() -> rabbitMQMessageReceiver.changeTrainerWorkload(""));

        verify(objectMapper, times(1)).readValue(anyString(), eq(TrainerWorkload.class));
        verify(trainerService, times(0)).changeTrainerWorkload(any());
    }

    @Test
    void receiveDLQMessageThenSuccess() {
        assertDoesNotThrow(() -> rabbitMQMessageReceiver.receiveDLQMessage(""));
    }
}