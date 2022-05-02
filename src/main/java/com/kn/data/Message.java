package com.kn.data;

import org.springframework.data.annotation.Id;

public class Message {

    @Id
    private long id;

    private final int dataId;

    private final String dataMessage;


    public Message(int dataId, String dataMessage) {
        this.dataId = dataId;
        this.dataMessage = dataMessage;
    }


    public int getDataId() {
        return dataId;
    }

    public String getDataMessage() {
        return this.dataMessage;
    }


    @Override
    public String toString() {
        return String.format(
            "Message[id=%d, data='%s']",
            dataId, dataMessage);
    }
}
