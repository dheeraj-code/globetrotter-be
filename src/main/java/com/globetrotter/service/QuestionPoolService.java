package com.globetrotter.service;

import com.globetrotter.model.QuestionPool;
import com.globetrotter.repository.QuestionPoolRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class QuestionPoolService {
    private final QuestionPoolRepository questionPoolRepository;
    private final Random random = new Random();

    public QuestionPoolService(QuestionPoolRepository questionPoolRepository) {
        this.questionPoolRepository = questionPoolRepository;
    }

    public QuestionPool getRandomQuestion(QuestionPool.DifficultyLevel difficultyLevel){

        List<QuestionPool> availableQuestions = questionPoolRepository.findByDifficultyLevel(difficultyLevel);
        if(availableQuestions.isEmpty()){
            availableQuestions = questionPoolRepository.findAll();
        }


        QuestionPool selectedQuestion = availableQuestions.get(random.nextInt(availableQuestions.size()));
        return selectedQuestion;
    }
}
