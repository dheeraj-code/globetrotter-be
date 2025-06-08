package com.globetrotter.service;

import com.globetrotter.model.City;
import com.globetrotter.repository.QuestionPoolRepository;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.List;
import com.globetrotter.model.QuestionPool;
import jakarta.annotation.PostConstruct;

public class QuestionPoolService {
    private final QuestionPoolRepository questionPoolRepository;
    private final Random random = new Random();
    private final CityService cityService;

    public QuestionPoolService(QuestionPoolRepository questionPoolRepository, CityService cityService) {
        this.questionPoolRepository = questionPoolRepository;
        this.cityService = cityService;
    }
    public QuestionPool getRandomQuestions(){
        List<QuestionPool> availableQuestions = questionPoolRepository.findByLastUsedBefore(LocalDateTime.now().minusHours(24));
        if(availableQuestions.isEmpty()){
            resetUsageCount();
            availableQuestions = questionPoolRepository.findAll();
        }
        QuestionPool selectedQuestion = availableQuestions.get(random.nextInt(availableQuestions.size()));
        selectedQuestion.setLastUsed(LocalDateTime.now());
        selectedQuestion.setTimesUsed(selectedQuestion.getTimesUsed() + 1);
        questionPoolRepository.save(selectedQuestion);
        return selectedQuestion;
    }
    private void resetUsageCount(){
        List<QuestionPool> allQuestions = questionPoolRepository.findAll();
        for(QuestionPool questionPool: allQuestions){
            questionPool.setLastUsed(null);
            questionPool.setTimesUsed(0);
        }
            questionPoolRepository.saveAll(allQuestions);
    }

}
