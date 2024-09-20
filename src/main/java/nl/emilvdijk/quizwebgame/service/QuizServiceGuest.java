package nl.emilvdijk.quizwebgame.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import nl.emilvdijk.quizwebgame.api.QuestionsApi;
import nl.emilvdijk.quizwebgame.dto.QuestionDto;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.entity.QuestionTriviaApi;
import nl.emilvdijk.quizwebgame.repository.QuestionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuizServiceGuest implements QuizService {

  @Autowired QuestionRepo questionRepo;

  @Getter @Setter List<Question> questions = new ArrayList<>();

  @Override
  public QuestionDto getNewQuestion() {
    if (this.questions.isEmpty()) {
      getNewQuestions();
    }
    return this.questions.getFirst().getQuestionDto();
  }

  @Override
  public void getNewQuestions() {
    if (questionRepo.count() < 10) {
      addNewQuestionsFromApi();
    }
    questions = questionRepo.findAll();
    Collections.shuffle(questions);
  }

  /** gets new questions from the question api and saves them to the repo. */
  @Override
  public void addNewQuestionsFromApi() {
    List<Question> newQuestions = QuestionsApi.getNewQuestion();
    newQuestions.forEach(Question::prepareAnswers);
    questionRepo.saveAll(newQuestions);
  }

  @Override
  public void removeAnsweredQuestion() {
    questions.removeFirst();
  }
}
