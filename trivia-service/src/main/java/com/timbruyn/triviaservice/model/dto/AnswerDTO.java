package com.timbruyn.triviaservice.model.dto;

import lombok.Data;

@Data
public class AnswerDTO {
    private int questionId;
    private String answer;
}
