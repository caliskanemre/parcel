package com.kn.controller;

import com.kn.request.MessageRequest;
import com.kn.service.SoapService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class WebFluxController {

    final
    SoapService soapService;

    public WebFluxController(SoapService soapService) {
        this.soapService = soapService;
    }


    @PostMapping(value = "/message")
    public int messageForwarder(@RequestBody MessageRequest messageRequest) {
        return soapService.messageForwarder(messageRequest);
    }
}