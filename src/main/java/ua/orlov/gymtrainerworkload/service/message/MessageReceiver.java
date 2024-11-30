package ua.orlov.gymtrainerworkload.service.message;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface MessageReceiver {

    void receiveMessage(String json) throws JsonProcessingException;

    void receiveDLQMessage(byte[] message);
}
