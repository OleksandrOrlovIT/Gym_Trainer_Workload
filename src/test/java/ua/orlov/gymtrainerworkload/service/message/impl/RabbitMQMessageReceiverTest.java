package ua.orlov.gymtrainerworkload.service.message.impl;

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

        var e = assertThrows(IllegalArgumentException.class, () -> rabbitMQMessageReceiver.receiveMessage(""));

        assertEquals("Subject mismatch with messageContent.get(\"subject\") = null", e.getMessage());

        verify(objectMapper, times(1)).readValue(anyString(), eq(Map.class));
        verify(trainerService, times(0)).changeTrainerWorkload(any());
    }

    @Test
    void receiveMessageGivenWrongSubjectThenException() throws JsonProcessingException {
        HashMap<String, String> map = new HashMap<>();
        String diffSubjectName = TRAINER_WORKLOAD_SUBJECT_NAME + "asdasd";
        map.put("subject", diffSubjectName);

        when(objectMapper.readValue(anyString(), eq(Map.class))).thenReturn(map);

        var e = assertThrows(IllegalArgumentException.class, () -> rabbitMQMessageReceiver.receiveMessage(""));

        assertEquals("Subject mismatch with messageContent.get(\"subject\") = " + diffSubjectName, e.getMessage());

        verify(objectMapper, times(1)).readValue(anyString(), eq(Map.class));
        verifyNoInteractions(trainerService);
    }

    @Test
    void receiveMessageGivenNoContentThenException() throws JsonProcessingException {
        HashMap<String, String> map = new HashMap<>();
        map.put("subject", TRAINER_WORKLOAD_SUBJECT_NAME);

        when(objectMapper.readValue(anyString(), eq(Map.class))).thenReturn(map);

        var e = assertThrows(IllegalArgumentException.class, () -> rabbitMQMessageReceiver.receiveMessage(""));

        assertEquals("Passed json doesn't contain content", e.getMessage());

        verify(objectMapper, times(1)).readValue(anyString(), eq(Map.class));
        verifyNoInteractions(trainerService);
    }

    @Test
    void receiveMessageGivenWrongContentThenException() throws JsonProcessingException {
        HashMap<String, String> map = new HashMap<>();
        map.put("subject", TRAINER_WORKLOAD_SUBJECT_NAME);
        map.put("content", "asdahjsdkajhsdkasd");

        when(objectMapper.readValue(anyString(), eq(Map.class))).thenReturn(map);
        when(objectMapper.readValue(anyString(), eq(TrainerWorkload.class))).thenThrow(new JsonProcessingException(""){});

        var e = assertThrows(JsonProcessingException.class, () -> rabbitMQMessageReceiver.receiveMessage(""));

        assertEquals("", e.getMessage());

        verify(objectMapper, times(1)).readValue(anyString(), eq(Map.class));
        verify(objectMapper, times(1)).readValue(anyString(), eq(TrainerWorkload.class));
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
    void receiveDLQMessageGivenEmptyThenSuccess() {
        assertDoesNotThrow(() -> rabbitMQMessageReceiver.receiveDLQMessage(new byte[]{}));
    }

    @Test
    void receiveDLQMessageGivenNullThenSuccess() {
        assertDoesNotThrow(() -> rabbitMQMessageReceiver.receiveDLQMessage(null));
    }

    @Test
    void receiveDLQThenSuccess() {
        byte[] input = "Text".getBytes();
        assertDoesNotThrow(() -> rabbitMQMessageReceiver.receiveDLQMessage(input));
    }


}