package com.developer.unittest.repositories;

import java.util.List;

public interface QuestionRepository {
    List<String> findQuestionsById(Long id);
    void saveQuestions(List<String> questions);
}
