package com.telusko.quizapp.service;

import com.telusko.quizapp.model.Question;
import com.telusko.quizapp.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
@RequiredArgsConstructor
@Service
public class QuestionService {
private final QuestionRepository questionRepository;
    public ResponseEntity<List<Question>> getAllQuestions() {
//        return new ResponseEntity<>(questionRepository.findAll(),HttpStatus.OK);
        return ResponseEntity.ok(questionRepository.findAll());
    }
    public List<Question> getByCategory(String  category) {
        List<Question> questions = questionRepository.findByCategoryIgnoreCase(category);

        if(questions.isEmpty()){
            throw new RuntimeException("Category not found");
        }

        return questions;
    }
    public ResponseEntity<String> addQuestion(Question question){
        questionRepository.save(question);
        return new ResponseEntity<>("success",HttpStatus.CREATED);
    }
}
