package com.telusko.quizapp.service;

import com.telusko.quizapp.QuestionRepository;
import com.telusko.quizapp.QuizRepository;
import com.telusko.quizapp.model.Question;
import com.telusko.quizapp.model.QuestionWrapper;
import com.telusko.quizapp.model.Quiz;
import com.telusko.quizapp.model.Response;
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
    private final QuestionRepository  questionRepository;
    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {
        List<Question> questions=questionRepository.findRandomQuestionsByCategory(category,numQ);
        Quiz quiz=new Quiz();
        quiz.setTitle(title);
        quiz.setQuestions(questions);
        quizRepository.save(quiz);
        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    public ResponseEntity<List<QuestionWrapper>> getAllQuizQuestions(int id) {
        Optional<Quiz> questions= quizRepository.findById(id);

            List<Question> questionsFromDB=questions.get().getQuestions();
            List<QuestionWrapper> questionForUser=questionsFromDB.stream()
                    .map(question ->{
                        QuestionWrapper qw = new QuestionWrapper();
                        qw.setId(question.getId());
                        qw.setQuestionTitle(question.getQuestionTitle());
                        qw.setCategory(question.getCategory());
                        qw.setOption1(question.getOption1());
                        qw.setOption2(question.getOption2());
                        qw.setOption3(question.getOption3());
                        qw.setOption4(question.getOption4());
                        return qw;
                    } )
                    .toList();

            return new ResponseEntity<>(questionForUser,HttpStatus.OK);
    }

    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {
        Quiz quiz=quizRepository.findById(id).get();
        List<Question> questions=quiz.getQuestions();
        int right=0;
        int i=0;
        for(Response response:responses){
            if(response.getResponse().equals(questions.get(i).getRightAnswer())){
                right++;
            }
            i++;
        }
        return new ResponseEntity<>(right,HttpStatus.OK);
    }
}
