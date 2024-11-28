package nl.emilvdijk.quizwebgame.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.NonNull;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Dynamic quiz service responsible for returning the appropriate quiz service when service is
 * needed. the service classes for users are split in two separate classes because of the different
 * desired functionality between an authenticated user and an anonymous one.
 *
 * @author Emil van Dijk
 */
@Service
public class DynamicQuizService {

  @NonNull private final Map<String, QuizService> quizServiceByAuthentication;

  /**
   * default constructor for the class which fetches all quiz services to a list.
   *
   * @param quizServices list of quiz services
   */
  @Autowired
  public DynamicQuizService(List<QuizService> quizServices) {
    quizServiceByAuthentication =
        quizServices.stream()
            .collect(Collectors.toMap(QuizService::getApplicableRole, Function.identity()));
  }

  /**
   * returns the appropriate quiz service from the list using the user as reference.
   *
   * @param user user to check which roles he has
   * @return returns the appropriate quiz service
   */
  public QuizService getService(MyUser user) {
    if (user == null) {
      return quizServiceByAuthentication.get("ANONYMOUS");
    } else if (user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
      return quizServiceByAuthentication.get("ROLE_USER");
    } else {
      return quizServiceByAuthentication.get("ANONYMOUS");
    }
  }
}
