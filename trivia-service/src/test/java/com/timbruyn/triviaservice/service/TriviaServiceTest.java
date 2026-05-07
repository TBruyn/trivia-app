package com.timbruyn.triviaservice.service;

import com.timbruyn.triviaservice.client.OpentdbClient;
import com.timbruyn.triviaservice.model.dto.CheckAnswerRequestDTO;
import com.timbruyn.triviaservice.model.dto.CheckAnswerResponseDTO;
import com.timbruyn.triviaservice.model.dto.QuestionDTO;
import com.timbruyn.triviaservice.model.opentdb.OpentdbQuestion;
import com.timbruyn.triviaservice.model.opentdb.OpentdbResponse;
import com.timbruyn.triviaservice.repository.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TriviaServiceTest {
    @Mock
    private OpentdbClient opentdbClient;

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private TriviaService triviaService;

    @Test
    void getQuestion_returnsQuestionDTO_andUsesQueuedQuestions() {
        OpentdbQuestion question1 = new OpentdbQuestion();
        question1.setQuestion("Question1");
        question1.setType("multiple");
        question1.setCorrectAnswer("foo");
        question1.setIncorrectAnswers(List.of("bar", "baz"));

        OpentdbQuestion question2 = new OpentdbQuestion();
        question2.setQuestion("Question2");
        question2.setType("boolean");
        question2.setCorrectAnswer("true");
        question2.setIncorrectAnswers(List.of("false"));

        OpentdbResponse opentdbResponse = new OpentdbResponse();
        opentdbResponse.setResults(List.of(question1, question2));

        when(opentdbClient.fetchQuestions()).thenReturn(opentdbResponse);
        when(questionRepository.save(question1)).thenReturn(1);
        when(questionRepository.save(question2)).thenReturn(2);

        QuestionDTO firstResult = triviaService.getQuestion();
        QuestionDTO secondResult = triviaService.getQuestion();

        assertEquals(1, firstResult.getId());
        assertEquals(2, secondResult.getId());

        assertEquals("Question1", firstResult.getQuestion());
        assertEquals("multiple", firstResult.getType());
        assertTrue(firstResult.getAnswers().contains("foo"));
        assertTrue(firstResult.getAnswers().contains("bar"));
        assertTrue(firstResult.getAnswers().contains("baz"));
        assertEquals(3, firstResult.getAnswers().size());

        verify(opentdbClient, times(1)).fetchQuestions();
        verify(questionRepository).save(question1);
        verify(questionRepository).save(question2);
    }

    @Test
    void getQuestion_throwsExceptionOnEmptyResponse() {

        OpentdbResponse opentdbResponse = new OpentdbResponse();
        opentdbResponse.setResults(List.of());

        when(opentdbClient.fetchQuestions()).thenReturn(opentdbResponse);

        assertThrows(IllegalStateException.class, () -> triviaService.getQuestion());
        verify(opentdbClient).fetchQuestions();
    }

    @Test
    void checkAnswer_returnsCheckAnswerResponseDTO() {
        CheckAnswerRequestDTO answer = new CheckAnswerRequestDTO();
        answer.setQuestionId(1);
        answer.setAnswer("bar");

        OpentdbQuestion question = new OpentdbQuestion();
        question.setQuestion("Question1");
        question.setType("multiple");
        question.setCorrectAnswer("foo");
        question.setIncorrectAnswers(List.of("bar", "baz"));

        when(questionRepository.findById(1)).thenReturn(Optional.of(question));

        CheckAnswerResponseDTO response = triviaService.checkAnswer(answer);

        assertFalse(response.isCorrect());
        verify(questionRepository).findById(1);
    }

    @Test
    void checkAnswer_returnsCheckAnswerResponseDTO_deletesOnCorrectAnswer() {
        CheckAnswerRequestDTO answer = new CheckAnswerRequestDTO();
        answer.setQuestionId(1);
        answer.setAnswer("foo");

        OpentdbQuestion question = new OpentdbQuestion();
        question.setQuestion("Question1");
        question.setType("multiple");
        question.setCorrectAnswer("foo");
        question.setIncorrectAnswers(List.of("bar", "baz"));

        when(questionRepository.findById(1)).thenReturn(Optional.of(question));

        CheckAnswerResponseDTO response = triviaService.checkAnswer(answer);

        assertTrue(response.isCorrect());
        verify(questionRepository).findById(1);
        verify(questionRepository).deleteById(1);
    }

    @Test
    void checkAnswer_throwsExceptionOnMissingId() {
        CheckAnswerRequestDTO answer = new CheckAnswerRequestDTO();
        answer.setQuestionId(1);
        answer.setAnswer("foo");

        when(questionRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> triviaService.checkAnswer(answer));
    }
}
