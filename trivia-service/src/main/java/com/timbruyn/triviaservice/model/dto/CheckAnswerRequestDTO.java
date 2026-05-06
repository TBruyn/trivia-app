package com.timbruyn.triviaservice.model.dto;

import lombok.Data;

@Data
public class CheckAnswerRequestDTO {
    private int questionId;
    private String answer;
}
