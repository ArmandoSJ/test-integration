package com.developer.unittest.services;

import com.developer.unittest.models.Exam;
import com.developer.unittest.repositories.ExamRepositoryImpl;
import com.developer.unittest.repositories.QuestionRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ExamServiceSpyTest {

    @Spy
    private ExamRepositoryImpl examRepositoryImpl;

    @Spy
    private QuestionRepositoryImpl questionRepositoryImpl;

    @InjectMocks
    private ExamServiceImpl examServiceImpl;

    @Test
    void testSpy() {
        List<String> questions = Collections.singletonList("arithmetic");
        //Mockito.when(questionRepositoryImpl.findQuestionsById(anyLong())).thenReturn(questions);
        doReturn(questions).when(questionRepositoryImpl).findQuestionsById(anyLong());

        Exam exam = examServiceImpl.finExamByNameWithAllTheQuestions("English").get();
        assertEquals(1L, exam.getId());
        assertEquals("English", exam.getName());
        assertEquals(1, exam.getQuestions().size());
        assertTrue(exam.getQuestions().contains("arithmetic"));

        verify(examRepositoryImpl).findAll();
        verify(questionRepositoryImpl).findQuestionsById(anyLong());
    }
}
