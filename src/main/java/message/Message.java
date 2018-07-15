package message;

import com.google.common.collect.Lists;

import java.util.ArrayList;

public class Message implements IMessage {

    //таксист
    private final int targetId;
    //id клиента
    private String dispatched;
    private final ArrayList<Object> sometags = Lists.newArrayList();

    public Message(final int id) {
        this.targetId = id;
    }

    public int getTargetId() {
        return targetId;
    }

    public ArrayList<Object> getSometags() {
        return sometags;
    }

    @Override
    public String getDispatched() {
        return dispatched;
    }

    @Override
    public void setDispatched(final String dispatched) {
        this.dispatched = dispatched;
    }

    @Override
    public int getTaxiId() {
        return this.getTargetId();
    }
}
