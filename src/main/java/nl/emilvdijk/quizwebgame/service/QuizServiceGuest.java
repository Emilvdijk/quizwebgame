package nl.emilvdijk.quizwebgame.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.repository.QuestionRepo;
import nl.emilvdijk.quizwebgame.service.api.QuestionApiService;
import org.springframework.stereotype.Service;

/**
 * quiz service for all operations involving quiz questions for anonymous users.
 *
 * @author Emil van Dijk
 */
@Service
@Slf4j
@AllArgsConstructor
public class QuizServiceGuest implements QuizService {

  @NonNull private static final String APPLICABLE_ROLE = "ANONYMOUS";
  @NonNull QuestionRepo questionRepo;
  @NonNull QuestionApiService questionApiService;
  @Getter @Setter List<Question> questions = new ArrayList<>();

  /**
   * return a question from the questions list held by this class. if the list is empty
   * getNewQuestions will be called.
   *
   * @return a new question
   */
  @Override
  public Question getNewQuestion() {
    if (this.questions.isEmpty()) {
      getNewQuestions();
    }
    return this.questions.getFirst();
  }

  /**
   * fetches new questions from the repository and adds them to the list held by this class. if
   * there are too few questions addNewQuestionsFromApi will be called to retrieve more questions
   * before trying again.
   */
  public void getNewQuestions() {
    if (questionRepo.count() < 10) {
      addNewQuestionsFromApi();
    }
    questions = questionRepo.findAll();
    questions.forEach(Question::prepareAnswers);
    Collections.shuffle(questions);
  }

  /** calls for the api service to retrieve more questions from external apis. */
  @Override
  public void addNewQuestionsFromApi() {
    List<Question> questionList = questionApiService.getDefaultQuestions();
    questionRepo.saveAll(questionList);
    log.debug("{} questions added to question repo", questionList.size());
  }

  /** removes the first question in the list held by this class after it has been answered. */
  @Override
  public void removeAnsweredQuestion() {
    questions.removeFirst();
  }

  /**
   * returns the question from the repository with the desired id.
   *
   * @param id the id of the desired question
   * @return the question with the corresponding id
   */
  @Override
  public Question getQuestionById(Long id) {
    return questionRepo.findById(id).orElseThrow();
  }

  /**
   * returns the applicable role used by the dynamic quiz service.
   *
   * @see DynamicQuizService
   * @return the role this service is applicable to
   */
  @Override
  public String getApplicableRole() {
    return APPLICABLE_ROLE;
  }
}
