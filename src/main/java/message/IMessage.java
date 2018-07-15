package message;

public interface IMessage {

    int getTaxiId();

    void setDispatched(String callerId);

    String getDispatched();
}
