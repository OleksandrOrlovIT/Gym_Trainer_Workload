package ua.orlov.gymtrainerworkload.service.message.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ua.orlov.gymtrainerworkload.dto.TrainerWorkload;
import ua.orlov.gymtrainerworkload.service.message.MessageReceiver;
import ua.orlov.gymtrainerworkload.service.user.TrainerService;

import java.util.Map;

@Log4j2
@AllArgsConstructor
@Service
public class RabbitMQMessageReceiver implements MessageReceiver {

    private final TrainerService trainerService;
    private final ObjectMapper objectMapper;

    private static final String TRAINER_WORKLOAD_SUBJECT_NAME = "Trainer workload";

    @Override
    public void receiveMessage(String json) throws JsonProcessingException {
        Map<String, String> messageContent = objectMapper.readValue(json, Map.class);

        if (!TRAINER_WORKLOAD_SUBJECT_NAME.equals(messageContent.get("subject"))) {
            throw new IllegalArgumentException(
                    "Subject mismatch with messageContent.get(\"subject\") = " + messageContent.get("subject")
            );
        }

        if(!messageContent.containsKey("content")) {
            throw new IllegalArgumentException("Passed json doesn't contain content");
        }

        changeTrainerWorkload(messageContent.get("content"));
    }

    @Override
    public void receiveDLQMessage(byte[] message) {
        if (message == null || message.length == 0) {
            log.error("Received empty DLQ message.");
            return;
        }

        String decodedMessage = new String(message);
        log.error("Processed DLQ message: {}", decodedMessage);
    }

    private void changeTrainerWorkload(String json) throws JsonProcessingException{
        TrainerWorkload trainerWorkload = objectMapper.readValue(json, TrainerWorkload.class);
        trainerService.changeTrainerWorkload(trainerWorkload);
    }
}
