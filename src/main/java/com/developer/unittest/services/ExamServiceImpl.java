package com.developer.unittest.services;

import com.developer.unittest.models.Exam;
import com.developer.unittest.repositories.ExamRepository;
import com.developer.unittest.repositories.QuestionRepository;

import java.util.List;
import java.util.Optional;

public class ExamServiceImpl implements ExamService{

    private ExamRepository examRepository;
    private QuestionRepository questionRepository;

    public ExamServiceImpl(ExamRepository examRepository, QuestionRepository questionRepository) {
        this.examRepository = examRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public Optional<Exam> findExamByName(String name) {
        return examRepository.findAll()
                .stream()
                .filter(ex -> ex.getName().contains(name))
                .findFirst();
    }

    @Override
    public Optional<Exam> finExamByNameWithAllTheQuestions(String name) {
        Optional<Exam> currentExam = findExamByName(name);
        if(currentExam.isPresent()){
            List<String> questions = questionRepository.findQuestionsById(currentExam.get().getId());
            currentExam.get().setQuestions(questions);
            return currentExam;
        }
        return Optional.empty();
    }

    @Override
    public Exam save(Exam exam) {
        if(exam.getQuestions().isEmpty()){
            questionRepository.saveQuestions(exam.getQuestions());
        }
        return examRepository.save(exam);
    }

}
