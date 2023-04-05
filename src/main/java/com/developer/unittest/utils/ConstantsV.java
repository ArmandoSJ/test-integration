package com.developer.unittest.utils;

import com.developer.unittest.models.Exam;

import java.util.Arrays;
import java.util.List;

public class ConstantsV {
    public static final List<Exam> LIST_OF_EXAMS = Arrays.asList(new Exam(1L,"English"),
            new Exam(2L,"Spanish"),new Exam(3L,"Mathematics"));

    public static final List<Exam> LIST_OF_EXAMS_WITH_NULL_IDS = Arrays.asList(new Exam(0L,"English"),
            new Exam(0L,"Spanish"),new Exam(0L,"Mathematics"));

    public final static List<Exam> EXAMS_WITH_NEGATIVE_IDS = Arrays.asList(new Exam(-1L, "English"),
            new Exam(-2L, "Spanish"),
            new Exam(-3L, "Mathematics"));

    public final static List<String> QUESTIONS = Arrays.asList("Programming","Data science","Mathematics", "arithmetic", "English");

    public final static Exam EXAM = new Exam(4L, "JAVA");
}
