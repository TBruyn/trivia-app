package com.timbruyn.triviaservice.controller;

import com.timbruyn.triviaservice.model.dto.QuestionDTO;
import com.timbruyn.triviaservice.service.TriviaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TriviaController {

    private final TriviaService triviaService;

    public TriviaController(TriviaService triviaService) {
        this.triviaService = triviaService;
    }

    @GetMapping("/questions")
    public QuestionDTO getQuestions() {
        return triviaService.getQuestion();
    }
}
