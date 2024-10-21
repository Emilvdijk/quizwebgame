package nl.emilvdijk.quizwebgame.entity;

import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.*;
import nl.emilvdijk.quizwebgame.enums.ApiChoiceEnum;

@Entity
@Getter
@Setter
@Builder(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserPreferences implements Serializable {

  @Serial private static final long serialVersionUID = -682768038923270772L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Enumerated(EnumType.STRING)
  private ApiChoiceEnum apiChoiceEnum;

  @ElementCollection(fetch = FetchType.EAGER)
  private Map<String, String> quizApiUriVariables = new HashMap<>();
}
