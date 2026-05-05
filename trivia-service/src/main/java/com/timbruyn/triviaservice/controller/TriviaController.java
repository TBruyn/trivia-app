package com.timbruyn.triviaservice.controller;

import com.timbruyn.triviaservice.client.OpentdbClient;
import com.timbruyn.triviaservice.model.opentdb.OpentdbResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TriviaController {

    private final OpentdbClient opentdbClient;

    public TriviaController(OpentdbClient opentdbClient) {
        this.opentdbClient = opentdbClient;
    }

    @GetMapping("/questions")
    public OpentdbResponse getQuestions() {
        return opentdbClient.fetchQuestions();
    }
}
