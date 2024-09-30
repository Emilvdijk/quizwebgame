package nl.emilvdijk.quizwebgame.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.repository.QuestionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuizServiceGuest implements QuizService {

  @Autowired QuestionRepo questionRepo;
  @Autowired QuestionsApiService questionsApiService;
  private final String APPLICABLE_ROLE = "ANONYMOUS";

  // FIXME store in session?
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

  /** gets new questions from the question api and saves them to the repo. */
  @Override
  public void addNewQuestionsFromApi() {
    List<Question> newQuestions = questionsApiService.getNewQuestions();
    newQuestions.forEach(Question::prepareAnswers);
    questionRepo.saveAll(newQuestions);
  }

  @Override
  public void removeAnsweredQuestion() {
    questions.removeFirst();
  }

  @Override
  public Question getQuestionByMyid(Long myid) {
    return questionRepo.findBymyId(myid);
  }

  @Override
  public String getApplicableRole() {
    return APPLICABLE_ROLE;
  }
}
