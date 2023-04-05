package com.developer.unittest.services;

import com.developer.unittest.utils.ConstantsV;
import com.developer.unittest.models.Exam;
import com.developer.unittest.repositories.ExamRepository;
import com.developer.unittest.repositories.QuestionRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamServiceImplTest {

    @Mock
    private ExamRepository examRepository;

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private ExamServiceImpl service;

    @Test
    void find_exam_by_name() {
        Mockito.when(examRepository.findAll()).thenReturn(ConstantsV.LIST_OF_EXAMS);
        Optional<Exam> exam = service.findExamByName("English");
        assertTrue(exam.isPresent());
        assertEquals(1L, exam.orElseThrow(NullPointerException::new).getId());
        assertEquals("English", exam.get().getName());
    }

    @Test
    void validate_if_the_list_of_exams_is_empty() {
        List<Exam> listOfExams = Collections.emptyList();
        Mockito.when(examRepository.findAll()).thenReturn(listOfExams);
        Optional<Exam> exam = service.findExamByName("English");
        assertFalse(exam.isPresent());
    }

    @Test
    void getting_exam_questions(){
        Mockito.when(examRepository.findAll()).thenReturn(ConstantsV.LIST_OF_EXAMS);
        Mockito.when(questionRepository.findQuestionsById(Mockito.anyLong())).thenReturn(ConstantsV.QUESTIONS);
        Optional<Exam> exam = service.finExamByNameWithAllTheQuestions("English");
        assertTrue(exam.isPresent());
        assertEquals(5, exam.get().getQuestions().size());
    }

    @Test
    void getting_exam_questions_verify(){
        Mockito.when(examRepository.findAll()).thenReturn(ConstantsV.LIST_OF_EXAMS);
        Mockito.when(questionRepository.findQuestionsById(Mockito.anyLong())).thenReturn(ConstantsV.QUESTIONS);
        Optional<Exam> exam = service.finExamByNameWithAllTheQuestions("English");
        assertTrue(exam.isPresent());
        assertEquals(5, exam.get().getQuestions().size());
        Mockito.verify(examRepository).findAll();
        Mockito.verify(questionRepository).findQuestionsById(1L);
    }

    @Test
    @Disabled
    void validate_if_exam_list_is_empty_and_verify_it(){
        Mockito.when(examRepository.findAll()).thenReturn(Collections.emptyList());
        Mockito.when(questionRepository.findQuestionsById(Mockito.anyLong())).thenReturn(ConstantsV.QUESTIONS);
        Optional<Exam> exam = service.finExamByNameWithAllTheQuestions("English");
        assertNull(exam.orElse(null));
        Mockito.verify(examRepository).findAll();
        Mockito.verify(questionRepository).findQuestionsById(1L);
    }

    @Test
    void validate_save_exam_function(){
        Mockito.when(examRepository.save(Mockito.any(Exam.class))).thenReturn(ConstantsV.EXAM);
        Exam exam = service.save(ConstantsV.EXAM);

        assertNotNull(exam.getId());
        assertEquals(4L, exam.getId());
        assertEquals("JAVA", exam.getName());
        Mockito.verify(examRepository).save(Mockito.any(Exam.class));
    }

    @Test
    void checking_null_id(){
        Mockito.when(examRepository.findAll()).thenReturn(ConstantsV.LIST_OF_EXAMS_WITH_NULL_IDS);
        Mockito.when(questionRepository.findQuestionsById(0L)).thenThrow(IllegalArgumentException.class);
        Exception exception = assertThrows(IllegalArgumentException.class, ()->{
           service.finExamByNameWithAllTheQuestions("English");
        });

        assertEquals(IllegalArgumentException.class, exception.getClass());
    }

    @Test
    @Disabled
    void do_throw_example(){
        Exam exam = ConstantsV.EXAM;
        exam.setQuestions(ConstantsV.QUESTIONS);
        // its used when the method is void, it doesn't return anything
        Mockito.doThrow(IllegalArgumentException.class).when(questionRepository).saveQuestions(Mockito.anyList());
        assertThrows(IllegalArgumentException.class, ()->{
            service.save(exam);
        });
    }

    @Test
    @Disabled
    void do_answer_example(){
        Mockito.when(examRepository.findAll()).thenReturn(ConstantsV.LIST_OF_EXAMS);
        Mockito.doAnswer(invocation -> {
           Long id = invocation.getArgument(0);
           return id == 1L ? ConstantsV.QUESTIONS: Collections.emptyList();
        }).when(questionRepository).findQuestionsById(Mockito.anyLong());

        Exam exam = service.finExamByNameWithAllTheQuestions("English").get();
        assertEquals(1L, exam.getId());
        assertEquals("English", exam.getName());

    }

    @Test
    void validate_order_of_invoke() {
        when(examRepository.findAll()).thenReturn(ConstantsV.LIST_OF_EXAMS);

        service.finExamByNameWithAllTheQuestions("English");
        service.finExamByNameWithAllTheQuestions("Spanish");

        InOrder inOrder = inOrder(questionRepository);
        inOrder.verify(questionRepository).findQuestionsById(1L);
        inOrder.verify(questionRepository).findQuestionsById(2L);
    }

    @Test
    void validate_order_of_invoke_2() {
        when(examRepository.findAll()).thenReturn(ConstantsV.LIST_OF_EXAMS);

        service.finExamByNameWithAllTheQuestions("English");
        service.finExamByNameWithAllTheQuestions("Spanish");

        InOrder inOrder = inOrder(examRepository, questionRepository);
        inOrder.verify(examRepository).findAll();
        inOrder.verify(questionRepository).findQuestionsById(1L);

        inOrder.verify(examRepository).findAll();
        inOrder.verify(questionRepository).findQuestionsById(2L);

    }

    @Test
    void validate_number_of_invoke() {
        when(examRepository.findAll()).thenReturn(ConstantsV.LIST_OF_EXAMS);
        service.finExamByNameWithAllTheQuestions("English");

        verify(questionRepository).findQuestionsById(1L);
        verify(questionRepository, times(1)).findQuestionsById(1L);
        verify(questionRepository, atLeast(1)).findQuestionsById(1L);
        verify(questionRepository, atLeastOnce()).findQuestionsById(1L);
        verify(questionRepository, atMost(1)).findQuestionsById(1L);
        verify(questionRepository, atMostOnce()).findQuestionsById(1L);
    }

    @Test
    void validate_number_of_invoke_2() {
        when(examRepository.findAll()).thenReturn(ConstantsV.LIST_OF_EXAMS);
        service.finExamByNameWithAllTheQuestions("English");

        //verify(questionRepository).findQuestionsById(1L); fail
        verify(questionRepository, times(1)).findQuestionsById(1L);
        verify(questionRepository, atLeast(1)).findQuestionsById(1L);
        verify(questionRepository, atLeastOnce()).findQuestionsById(1L);
        verify(questionRepository, atMost(20)).findQuestionsById(1L);
        //verify(questionRepository, atMostOnce()).findQuestionsById(1L); fail
    }

    @Test
    void validate_number_of_invoke_3() {
        when(examRepository.findAll()).thenReturn(Collections.emptyList());
        service.finExamByNameWithAllTheQuestions("English");

        verify(questionRepository, never()).findQuestionsById(1L);
        verifyNoInteractions(questionRepository);

        verify(examRepository).findAll();
        verify(examRepository, times(1)).findAll();
        verify(examRepository, atLeast(1)).findAll();
        verify(examRepository, atLeastOnce()).findAll();
        verify(examRepository, atMost(10)).findAll();
        verify(examRepository, atMostOnce()).findAll();
    }


    @Test
    void validate_arguments_matchers() {
        when(examRepository.findAll()).thenReturn(ConstantsV.LIST_OF_EXAMS);
        when(questionRepository.findQuestionsById(anyLong())).thenReturn(ConstantsV.QUESTIONS);
        service.finExamByNameWithAllTheQuestions("English");

        verify(examRepository).findAll();
        //verify(questionRepository).findQuestionsById(argThat(arg -> arg != null && arg.equals(1L)));
        verify(questionRepository).findQuestionsById(argThat(arg -> arg != null && arg >= 1L));
        //verify(questionRepository).findQuestionsById(eq(1L));

    }

    @Test
    void validate_arguments_matchers_2() {
        when(examRepository.findAll()).thenReturn(ConstantsV.EXAMS_WITH_NEGATIVE_IDS);
        when(questionRepository.findQuestionsById(anyLong())).thenReturn(ConstantsV.QUESTIONS);
        service.finExamByNameWithAllTheQuestions("English");

        verify(examRepository).findAll();
        verify(questionRepository).findQuestionsById(argThat(new ArgsMatchers()));

    }

    @Test
    void validate_arguments_matchers_3() {
        when(examRepository.findAll()).thenReturn(ConstantsV.EXAMS_WITH_NEGATIVE_IDS);
        when(questionRepository.findQuestionsById(anyLong())).thenReturn(ConstantsV.QUESTIONS);
        service.finExamByNameWithAllTheQuestions("English");

        verify(examRepository).findAll();
        verify(questionRepository).findQuestionsById(argThat( (argument) -> argument != null && argument < 1L));

    }

    public static class ArgsMatchers implements ArgumentMatcher<Long> {

        private Long argument;

        @Override
        public boolean matches(Long argument) {
            this.argument = argument;
            return argument != null && argument < 1L;
        }

        @Override
        public String toString() {
            return "This is a personalized error message " +
                    "if the test fails the message will be shown "
                    + argument + " must be integer";
        }
    }
}