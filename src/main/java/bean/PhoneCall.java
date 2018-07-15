package bean;

import message.IMessage;

public class PhoneCall {

    private final String clientId;
    private final IMessage message;

    public PhoneCall(final String clientId, final IMessage message) {
        this.clientId = clientId;
        this.message = message;
    }

    public String getClientId() {
        return clientId;
    }

    public IMessage getMessage() {
        return message;
    }
}
