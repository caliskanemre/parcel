package com.kn.service;

import com.kn.request.MessageRequest;

public interface SoapService {
    int messageForwarder(MessageRequest messageRequest);

    String getPayload(String message);
}
