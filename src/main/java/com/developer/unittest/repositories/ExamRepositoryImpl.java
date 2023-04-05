package com.developer.unittest.repositories;

import com.developer.unittest.models.Exam;
import com.developer.unittest.utils.ConstantsV;
import java.util.List;

public class ExamRepositoryImpl implements ExamRepository{
    @Override
    public Exam save(Exam exam) {
        return ConstantsV.EXAM;
    }

    @Override
    public List<Exam> findAll() {
        return ConstantsV.LIST_OF_EXAMS;
    }
}
