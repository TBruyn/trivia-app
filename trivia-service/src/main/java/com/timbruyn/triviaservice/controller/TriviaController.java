package com.timbruyn.triviaservice.controller;

import com.timbruyn.triviaservice.model.dto.AnswerDTO;
import com.timbruyn.triviaservice.model.dto.QuestionDTO;
import com.timbruyn.triviaservice.service.TriviaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

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

    @PostMapping("/checkanswers")
    public ResponseEntity<Boolean> checkAnswers(@RequestBody AnswerDTO answer) {
        try {
            return ResponseEntity.ok(triviaService.checkAnswer(answer));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
