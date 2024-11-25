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

    public void receiveMessage(String message) {
        try {
            Map<String, String> messageContent = objectMapper.readValue(message, Map.class);

            String subject = messageContent.get("subject");
            if(!messageContent.get("subject").equals("Trainer workload")){
                throw new IllegalArgumentException("Wrong message format for subject = " + subject);
            }

            changeTrainerWorkload(messageContent.get("content"));
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid message JSON", e);
        }
    }

    private void changeTrainerWorkload(String json) {
        try {
            TrainerWorkload trainerWorkload = objectMapper.readValue(json, TrainerWorkload.class);
            trainerService.changeTrainerWorkload(trainerWorkload);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid TrainerWorkload JSON", e);
        }
    }
}
