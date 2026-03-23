package com.telusko.questionservice.service;

import com.telusko.questionservice.model.Question;
import com.telusko.questionservice.model.QuestionWrapper;
import com.telusko.questionservice.model.Response;
import com.telusko.questionservice.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    public ResponseEntity<List<Integer>> getQuestionsForQuiz(String categoryName, Integer numQuestions) {
        List<Integer> questions=questionRepository.findRandomQuestionsByCategory(categoryName,numQuestions);
        return new ResponseEntity<>(questions,HttpStatus.OK);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(List<Integer> questionIds) {
        if (questionIds == null || questionIds.isEmpty()) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.BAD_REQUEST);
        }
        List<Question> questions=questionRepository.findAllById(questionIds);
        List<QuestionWrapper> wrappers=questions.stream()
                .map(q->{
                    QuestionWrapper questionWrapper=new QuestionWrapper();
                    questionWrapper.setId(q.getId());
                    questionWrapper.setCategory(q.getCategory());
                    questionWrapper.setQuestionTitle(q.getQuestionTitle());
                    questionWrapper.setOption1(q.getOption1());
                    questionWrapper.setOption2(q.getOption2());
                    questionWrapper.setOption3(q.getOption3());
                    questionWrapper.setOption4(q.getOption4());
                    return questionWrapper;
                })
                .toList();
        return new ResponseEntity<>(wrappers,HttpStatus.OK);

    }

    public ResponseEntity<Integer> getScore(List<Response> responses) {

        int correctAnswers = 0;

        List<Integer> ids = responses.stream()
                .map(Response::getId)
                .toList();

        List<Question> questions = questionRepository.findAllById(ids);

        Map<Integer, Question> questionMap = questions.stream()
                .collect(Collectors.toMap(Question::getId, q -> q));

        for (Response response : responses) {
            Question question = questionMap.get(response.getId());

            if (question != null &&
                    Objects.equals(question.getRightAnswer(), response.getResponse())) {
                correctAnswers++;
            }
        }

        return ResponseEntity.ok(correctAnswers);
    }
}
