package com.timbruyn.triviaservice.service;

import com.timbruyn.triviaservice.client.OpentdbClient;
import com.timbruyn.triviaservice.model.dto.CheckAnswerRequestDTO;
import com.timbruyn.triviaservice.model.dto.CheckAnswerResponseDTO;
import com.timbruyn.triviaservice.model.dto.QuestionDTO;
import com.timbruyn.triviaservice.model.opentdb.OpentdbQuestion;
import com.timbruyn.triviaservice.model.opentdb.OpentdbResponse;
import com.timbruyn.triviaservice.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TriviaService {
    private final OpentdbClient opentdbClient;
    private final QuestionRepository questionRepository;

    public TriviaService(OpentdbClient opentdbClient, QuestionRepository questionRepository) {
        this.opentdbClient = opentdbClient;
        this.questionRepository = questionRepository;
    }

    public QuestionDTO getQuestion() {
        OpentdbResponse opentdbResponse = opentdbClient.fetchQuestions();

        if (opentdbResponse.getResults().isEmpty()) {
            throw new IllegalStateException("No questions returned from OpenTDB");
        }
        OpentdbQuestion opentdbQuestion = opentdbResponse.getResults().getFirst();

        int id = questionRepository.save(opentdbQuestion);

        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setId(id);
        questionDTO.setQuestion(opentdbQuestion.getQuestion());
        questionDTO.setType(opentdbQuestion.getType());
        List<String> answers = new ArrayList<>(opentdbQuestion.getIncorrectAnswers());
        answers.add(opentdbQuestion.getCorrectAnswer());
        Collections.shuffle(answers);
        questionDTO.setAnswers(answers);

        return questionDTO;
    }

    public CheckAnswerResponseDTO checkAnswer(CheckAnswerRequestDTO answer) {
        OpentdbQuestion question = questionRepository.findById(answer.getQuestionId())
                .orElseThrow(NoSuchElementException::new);
        boolean answerIsCorrect = question.getCorrectAnswer().equals(answer.getAnswer());
        if (answerIsCorrect) questionRepository.deleteById(answer.getQuestionId());

        CheckAnswerResponseDTO response = new CheckAnswerResponseDTO();
        response.setAnswerWasCorrect(answerIsCorrect);
        return response;
    }
}
