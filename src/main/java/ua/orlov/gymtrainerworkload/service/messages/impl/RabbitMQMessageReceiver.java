package ua.orlov.gymtrainerworkload.service.messages.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ua.orlov.gymtrainerworkload.dto.TrainerWorkload;
import ua.orlov.gymtrainerworkload.service.messages.MessageReceiver;
import ua.orlov.gymtrainerworkload.service.user.TrainerService;

import java.util.Map;

@Log4j2
@AllArgsConstructor
@Service
public class RabbitMQMessageReceiver implements MessageReceiver {

    private final TrainerService trainerService;
    private final ObjectMapper objectMapper;

    private static final String TRAINER_WORKLOAD_SUBJECT_NAME = "Trainer workload";

    public void receiveMessage(String message) {
        try {
            Map<String, String> messageContent = objectMapper.readValue(message, Map.class);

            if(!messageContent.containsKey("subject")
                    || !messageContent.get("subject").equals(TRAINER_WORKLOAD_SUBJECT_NAME)){
                throw new IllegalArgumentException("Message doesn't have subject = " + TRAINER_WORKLOAD_SUBJECT_NAME);
            }

            changeTrainerWorkload(messageContent.get("content"));
        } catch (Exception e) {
            log.error(e);
        }
    }

    public void receiveDLQMessage(String message) {
        log.error("Message sent to DLQ: {}", message);
    }

    void changeTrainerWorkload(String json) {
        try {
            TrainerWorkload trainerWorkload = objectMapper.readValue(json, TrainerWorkload.class);
            trainerService.changeTrainerWorkload(trainerWorkload);
        } catch (Exception e) {
            log.error(e);
        }
    }
}
