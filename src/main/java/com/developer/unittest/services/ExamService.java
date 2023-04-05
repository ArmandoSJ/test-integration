package com.developer.unittest.services;

import com.developer.unittest.models.Exam;

import java.util.Optional;

public interface ExamService {
    Optional<Exam> findExamByName(String name);
    Optional<Exam> finExamByNameWithAllTheQuestions(String name);
    Exam save(Exam exam);
}
