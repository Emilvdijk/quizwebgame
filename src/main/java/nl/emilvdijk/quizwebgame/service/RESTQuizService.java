package nl.emilvdijk.quizwebgame.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.exceptions.QuestionNotFoundException;
import nl.emilvdijk.quizwebgame.repository.QuestionRepo;
import nl.emilvdijk.quizwebgame.service.api.QuestionApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RESTQuizService {

  QuestionRepo questionRepo;
  QuestionApiService questionApiService;

  // FIXME store in session?
  @Getter @Setter List<Question> questions = new ArrayList<>();

  public RESTQuizService(
      @Autowired QuestionRepo questionRepo, @Autowired QuestionApiService questionApiService) {
    this.questionRepo = questionRepo;
    this.questionApiService = questionApiService;
  }

  public Question getNewQuestion() {
    if (this.questions.isEmpty()) {
      getNewQuestions();
    }
    return this.questions.getFirst();
  }

  public void getNewQuestions() {
    if (questionRepo.count() < 10) {
      addNewQuestionsFromApi();
    }
    questions = questionRepo.findAll();
    questions.forEach(Question::prepareAnswers);
    Collections.shuffle(questions);
  }

  public void addNewQuestionsFromApi() {}

  public void removeAnsweredQuestion() {
    questions.removeFirst();
  }

  public Question getQuestionByMyid(Long myid) {
    return questionRepo.findById(myid).orElseThrow();
  }

  public List<Question> findAll() {
    return questionRepo.findAll();
  }

  public Question findById(Long id) {
    return questionRepo.findOptionalById(id).orElseThrow(() -> new QuestionNotFoundException(id));
  }
}
