package com.kn;

import com.kn.request.MessageRequest;
import com.kn.service.SoapService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataR2dbcTest
class MessageSoapTests {

    @Autowired
    private SoapService soapService;

    @Test
    void testSoapService(){
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setMessage("test message");
        messageRequest.setId(34);

        assertEquals(34, soapService.messageForwarder(messageRequest));
    }
}
