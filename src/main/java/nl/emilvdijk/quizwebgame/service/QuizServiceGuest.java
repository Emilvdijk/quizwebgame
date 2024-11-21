package nl.emilvdijk.quizwebgame.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.repository.QuestionRepo;
import nl.emilvdijk.quizwebgame.service.api.QuestionApiService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class QuizServiceGuest implements QuizService {

  private static final String APPLICABLE_ROLE = "ANONYMOUS";
  QuestionRepo questionRepo;
  QuestionApiService questionApiService;
  @Getter @Setter List<Question> questions = new ArrayList<>();

  @Override
  public Question getNewQuestion() {
    if (this.questions.isEmpty()) {
      getNewQuestions();
    }
    return this.questions.getFirst();
  }

  @Override
  public void getNewQuestions() {
    if (questionRepo.count() < 10) {
      addNewQuestionsFromApi();
    }
    questions = questionRepo.findAll();
    questions.forEach(Question::prepareAnswers);
    Collections.shuffle(questions);
  }

  @Override
  public void addNewQuestionsFromApi() {
    List<Question> questionList = questionApiService.getDefaultQuestions();
    questionRepo.saveAll(questionList);
    log.debug("{} questions added to question repo", questionList.size());
  }

  @Override
  public void removeAnsweredQuestion() {
    questions.removeFirst();
  }

  @Override
  public Question getQuestionByMyid(Long myid) {
    return questionRepo.findById(myid).orElseThrow();
  }

  @Override
  public String getApplicableRole() {
    return APPLICABLE_ROLE;
  }
}
