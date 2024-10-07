package nl.emilvdijk.quizwebgame.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import nl.emilvdijk.quizwebgame.dto.QuestionTriviaApi;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.enums.ApiChoiceEnum;
import nl.emilvdijk.quizwebgame.repository.QuestionRepo;
import nl.emilvdijk.quizwebgame.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class QuizServiceAuthenticated implements QuizService {

  @Autowired QuestionRepo questionRepo;
  @Autowired UserRepo userRepo;
  @Autowired QuestionApiMapperService questionApiMapperService;
  private static final String APPLICABLE_ROLE = "ROLE_USER";

  /**
   * returns question held by quiz service.
   *
   * @return question held by quiz service
   */
  @Override
  public Question getNewQuestion() {
    MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (user.getQuestions().isEmpty()) {
      getNewQuestions();
    }
    return user.getQuestions().getFirst();
  }

  @Override
  public void getNewQuestions() {
    // FIXME remove these tests
    List<Question> questionList1 = questionRepo.findByOrigin(ApiChoiceEnum.TRIVIAAPI);
    List<Question> questionList2 = questionRepo.findByOrigin(ApiChoiceEnum.OPENTDB);

    MyUser testUser = MyUser.builder().apiChoiceEnum(ApiChoiceEnum.TRIVIAAPI).build();
    List<Long> longListTest = new ArrayList<>();
    List<Question> questionListTest =
        questionRepo.findBymyIdNotInAndOrigin(longListTest, testUser.getApiChoiceEnum());

    // end of tests
    MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    MyUser myUser = userRepo.findByUsername(user.getUsername());

    List<Long> questionIdList = new ArrayList<>();
    myUser.getAnsweredQuestions().forEach(question -> questionIdList.add(question.getMyId()));
    List<Question> questions = getQuestionsByChoice(myUser, questionIdList);

    if (questions.size() < 10) {
      addNewQuestionsFromApi();
      questions = getQuestionsByChoice(myUser, questionIdList);
    }
    questions.forEach(Question::prepareAnswers);
    Collections.shuffle(questions);
    user.setQuestions(questions);
  }

  public List<Question> getQuestionsByChoice(MyUser myUser, List<Long> questionIdList) {
    return questionRepo.findBymyIdNotInAndOrigin(questionIdList, myUser.getApiChoiceEnum());
  }

  /** gets new questions from the question api and saves them to the repo. */
  @Override
  public void addNewQuestionsFromApi() {
    MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    MyUser myUser = userRepo.findByUsername(user.getUsername());

    QuestionsApiService questionsApiService =
        new QuestionsApiService<>(new ParameterizedTypeReference<List<QuestionTriviaApi>>() {});
    List<Question> newQuestions =
        questionApiMapperService.mapQuestions(questionsApiService.getNewQuestions(myUser));
    questionRepo.saveAll(newQuestions);
  }

  @Override
  public void removeAnsweredQuestion() {
    MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    user.getQuestions().removeFirst();
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
