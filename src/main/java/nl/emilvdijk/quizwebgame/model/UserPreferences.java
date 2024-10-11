package nl.emilvdijk.quizwebgame.model;

import jakarta.persistence.*;
import java.util.HashMap;
import java.util.Map;
import lombok.*;
import nl.emilvdijk.quizwebgame.enums.ApiChoiceEnum;

@Entity
@Getter
@Builder(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@AllArgsConstructor
public class UserPreferences {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Enumerated(EnumType.STRING)
  private ApiChoiceEnum apiChoiceEnum;

  @ElementCollection(fetch = FetchType.EAGER)
  private Map<String, String> quizApiUriVariables = new HashMap<>();
}
