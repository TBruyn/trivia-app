package com.timbruyn.triviaservice.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class QuestionDTO {
    private int id;
    private String type;
    private String question;
    private List<String> answers;
}
