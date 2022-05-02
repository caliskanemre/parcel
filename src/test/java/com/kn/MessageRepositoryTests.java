package com.kn;

import com.kn.data.Message;
import com.kn.data.MessageRepository;
import com.kn.request.MessageRequest;
import com.kn.service.SoapService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.dialect.H2Dialect;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataR2dbcTest
class MessageRepositoryTests {

    @Autowired
    private DatabaseClient databaseClient;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private SoapService soapService;

    @Test
    void testFindByDataId() {
        Message message = new Message(1, "message1");
        R2dbcEntityTemplate template = new R2dbcEntityTemplate(databaseClient, H2Dialect.INSTANCE);
        template.insert(Message.class).using(message).then().as(StepVerifier::create).verifyComplete();
        Flux<Message> findByDataId = messageRepository.findByDataId(message.getDataId());


        findByDataId.as(StepVerifier::create)
                .assertNext(actual -> assertThat(actual.getDataMessage()).isEqualTo("message1"))
                .verifyComplete();
    }

    @Test
    void testRequest(){
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setMessage("test message");
        messageRequest.setId(33);

        assertEquals(33, messageRequest.getId());
        assertEquals("test message", messageRequest.getMessage());

    }

}
