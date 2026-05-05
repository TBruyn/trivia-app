package com.timbruyn.triviaservice.client;

import com.timbruyn.triviaservice.model.OpentdbResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class OpentdbClient {

    private static final String BASE_URL = "https://opentdb.com";
    private static final String QUESTIONS_PATH = "/api.php";

    private static final int DEFAULT_QUESTION_AMOUNT = 1;
    private static final String DEFAULT_QUESTION_TYPE = "multiple";

    private final RestClient restClient;

    public OpentdbClient(RestClient.Builder builder) {
        this.restClient = builder.baseUrl(BASE_URL).build();
    }

    public OpentdbResponse fetchQuestions() {
        return fetchQuestions(DEFAULT_QUESTION_AMOUNT, DEFAULT_QUESTION_TYPE);
    }

    public OpentdbResponse fetchQuestions(int amount, String type) {

        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(QUESTIONS_PATH)
                        .queryParam("amount", amount)
                        .queryParam("type", type)
                        .build())
                .retrieve()
                .body(OpentdbResponse.class);
    }

}
