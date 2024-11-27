package nl.emilvdijk.quizwebgame.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/**
 * this class is made and attached to a MyUser when a user answers a quiz question. it is used to
 * persist a user history of questions the user has answered.
 *
 * @see MyUser
 * @author Emil van Dijk
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnsweredQuestion extends BaseEntity implements Serializable {

  @Serial private static final long serialVersionUID = -7224721293078656698L;
  @NonNull @ManyToOne private Question question;
  @NonNull private String chosenAnswer;
  @NonNull private Boolean correct;
}
