package nl.emilvdijk.quizwebgame.entity;

import jakarta.persistence.Entity;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
@Getter
public class AnsweredQuestion extends BaseEntity implements Serializable {

  @Serial private static final long serialVersionUID = -7224721293078656698L;
  private Long myUserId;
  private Long questionId;
  private String chosenAnswer;
  private boolean correct;
}
