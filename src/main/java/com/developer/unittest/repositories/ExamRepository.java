package com.developer.unittest.repositories;

import com.developer.unittest.models.Exam;

import java.util.List;

public interface ExamRepository {
    Exam save(Exam exam);
    List<Exam> findAll();
}
