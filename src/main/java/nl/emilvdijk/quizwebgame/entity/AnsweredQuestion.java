package nl.emilvdijk.quizwebgame.entity;

import jakarta.persistence.Entity;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnsweredQuestion extends BaseEntity implements Serializable {

  @Serial private static final long serialVersionUID = -7224721293078656698L;
  @NonNull private Long myUserId;
  @NonNull private Long questionId;
  @NonNull private String chosenAnswer;
  @NonNull private Boolean correct;
}
