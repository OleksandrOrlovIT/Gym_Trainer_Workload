package ua.orlov.gymtrainerworkload.service.messages;

public interface MessageReceiver {

    void receiveMessage(String message);

    void receiveDLQMessage(String message);
}
