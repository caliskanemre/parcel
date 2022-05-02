package com.kn.soap.impl;

import com.kn.data.Message;
import com.kn.data.MessageRepository;
import com.kn.request.MessageRequest;
import com.kn.service.SoapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class SoapServiceImpl implements SoapService {

    final MessageRepository messageRepository;

    private static final String SOAP_MOCK_URL = "http://localhost:8088/mockmy_forum_webservices";

    public SoapServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public int messageForwarder(MessageRequest messageRequest) throws ResponseStatusException{

        Logger log = LoggerFactory.getLogger("logger");

        WebClient webclient = WebClient.builder()
                .baseUrl(SOAP_MOCK_URL)
                .build();

        webclient.post()
                .contentType(MediaType.TEXT_XML)
                .bodyValue(getPayload(messageRequest.getMessage()))
                .retrieve()
                .onStatus(
                        HttpStatus::isError,
                        clientResponse ->
                                clientResponse
                                        .bodyToMono(String.class)
                                        .flatMap(
                                                errorResponseBody ->
                                                        Mono.error(
                                                                new ResponseStatusException(
                                                                        clientResponse.statusCode(),
                                                                        errorResponseBody))))

                .bodyToMono(String.class)
                .publishOn(Schedulers.boundedElastic())
                .doOnSuccess( (String response) -> {
                    log.info(response);
                    messageRepository.save(new Message(messageRequest.getId(), messageRequest.getMessage())).subscribe();
                })
                .doOnError(ResponseStatusException.class, error -> log.error(String.format("error : %s", error)))
                .doOnError(Exception.class, error -> log.error(String.format("error : %s", error)))
                .subscribe();

            return messageRequest.getId();
    }

    public String getPayload(String message) {
        return "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:xmethods-delayed-quotes\">\n" +
                "<soapenv:Header/>\n" +
                "<soapenv:Body>\n" +
                "<urn:post_message soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\n" +
                "<message>" +
                message +
                "</message> " +
                "</urn:post_message>\n" +
                "</soapenv:Body>\n" +
                "</soapenv:Envelope>";
    }
}

