package com.timbruyn.triviaservice.controller;

import com.timbruyn.triviaservice.model.dto.CheckAnswerRequestDTO;
import com.timbruyn.triviaservice.model.dto.CheckAnswerResponseDTO;
import com.timbruyn.triviaservice.model.dto.QuestionDTO;
import com.timbruyn.triviaservice.service.TriviaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@CrossOrigin(origins = "http://localhost:5173")
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
    public ResponseEntity<CheckAnswerResponseDTO> checkAnswers(@RequestBody CheckAnswerRequestDTO answer) {
        try {
            return ResponseEntity.ok(triviaService.checkAnswer(answer));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
