package nl.emilvdijk.quizwebgame.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import nl.emilvdijk.quizwebgame.dto.QuestionDto;

import java.io.Serializable;

public interface Question extends Serializable {

  QuestionDto getQuestionDto();

  Long getMyId();

    String getCorrectAnswer();
    void prepareAnswers();
}
