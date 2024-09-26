package nl.emilvdijk.quizwebgame.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DynamicQuizService {
  private final Map<String, QuizService> quizServiceByAuthentication;

  @Autowired
  public DynamicQuizService(List<QuizService> regionServices) {
    quizServiceByAuthentication =
        regionServices.stream()
            .collect(Collectors.toMap(QuizService::getApplicableRole, Function.identity()));
  }

  public QuizService getService(MyUser user) {
    if (user == null) {
      return quizServiceByAuthentication.get("ANONYMOUS");
    } else {
      return quizServiceByAuthentication.get("ROLE_USER");
    }
  }
}