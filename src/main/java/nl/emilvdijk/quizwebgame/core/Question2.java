package nl.emilvdijk.quizwebgame.core;


import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

//@Component
public record Question2(
    String type,
    String difficulty,
    String category,
    String question,
    String correct_answer,
    List<String> incorrect_answers
) {

}
