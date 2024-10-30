package nl.emilvdijk.quizwebgame.entity;

import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.*;
import lombok.experimental.SuperBuilder;
import nl.emilvdijk.quizwebgame.enums.ApiChoiceEnum;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserPreferences extends BaseEntity implements Serializable {

  @Serial private static final long serialVersionUID = -682768038923270772L;

  @Enumerated(EnumType.STRING)
  private ApiChoiceEnum apiChoiceEnum;

  @ElementCollection(fetch = FetchType.EAGER)
  private Map<String, String> quizApiUriVariablesTRIVIAAPI = new HashMap<>();

  @ElementCollection(fetch = FetchType.EAGER)
  private Map<String, String> quizApiUriVariablesOPENTDB = new HashMap<>();
}
