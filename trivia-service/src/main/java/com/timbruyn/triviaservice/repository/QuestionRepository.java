package com.timbruyn.triviaservice.repository;

import com.timbruyn.triviaservice.model.opentdb.OpentdbQuestion;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class QuestionRepository {
    private final ConcurrentHashMap<Integer, OpentdbQuestion> questions = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger();

    public int save(OpentdbQuestion question) {
        int id = idGenerator.incrementAndGet();
        questions.put(id, question);
        return id;
    }

    public Optional<OpentdbQuestion> findById(int id) {
        return Optional.ofNullable(questions.get(id));
    }

    public void deleteById(int id) {
        questions.remove(id);
    }
}
