package com.telusko.quizservice.service;


import com.telusko.quizservice.QuizRepository;
import com.telusko.quizservice.feign.QuizInterface;
import com.telusko.quizservice.model.Question;
import com.telusko.quizservice.model.QuestionWrapper;
import com.telusko.quizservice.model.Quiz;
import com.telusko.quizservice.model.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class QuizService {
    private final QuizRepository quizRepository;
    private final QuizInterface quizInterface;
//    private final QuestionRepository  questionRepository;
    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {
        List<Integer> questions=quizInterface.getQuestionsForQuiz(category,numQ).getBody();
        Quiz quiz=new Quiz();
        quiz.setTitle(title);
        quiz.setQuestionIds(questions);
        quizRepository.save(quiz);
//        List<Question> questions=questionRepository.findRandomQuestionsByCategory(category,numQ);
//        Quiz quiz=new Quiz();
//        quiz.setTitle(title);
//        quiz.setQuestions(questions);
//        quizRepository.save(quiz);
        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(int id) {
//        Optional<Quiz> questions= quizRepository.findById(id);
//
//            List<Question> questionsFromDB=questions.get().getQuestions();
//            List<QuestionWrapper> questionForUser=questionsFromDB.stream()
//                    .map(question ->{
//                        QuestionWrapper qw = new QuestionWrapper();
//                        qw.setId(question.getId());
//                        qw.setQuestionTitle(question.getQuestionTitle());
//                        qw.setCategory(question.getCategory());
//                        qw.setOption1(question.getOption1());
//                        qw.setOption2(question.getOption2());
//                        qw.setOption3(question.getOption3());
//                        qw.setOption4(question.getOption4());
//                        return qw;
//                    } )
//                    .toList();
//        List<QuestionWrapper> questionForUser=new ArrayList<>();
        Quiz quiz=quizRepository.findById(id).get();
        List<Integer> questionIds=quiz.getQuestionIds();
        ResponseEntity<List<QuestionWrapper>> questions=quizInterface.getQuestionsFromId(questionIds);

            return questions;
    }

    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {
        ResponseEntity<Integer> score= quizInterface.getScore(responses);

        return score;
    }
}
