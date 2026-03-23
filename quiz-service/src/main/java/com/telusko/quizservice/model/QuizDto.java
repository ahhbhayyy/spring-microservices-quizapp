package com.telusko.quizservice.model;

import lombok.Data;

@Data
public class QuizDto {
    private String categoryName;
    private Integer numQuestions;
    private String title;
}
