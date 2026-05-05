package com.timbruyn.triviaservice.model.opentdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class OpentdbResponse {
    @JsonProperty("response_code")
    private int responseCode;

    private List<OpentdbQuestion> results;
}
