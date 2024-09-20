package nl.emilvdijk.quizwebgame.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class QuestionDto {
    Long myId;
    String question;
    List<String> answers;


}
