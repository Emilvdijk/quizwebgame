package nl.emilvdijk.quizwebgame.core;

import java.util.Arrays;
import java.util.Map;

//@Component
public class Question {
  String category;
  String id;
  String correctAnswer;
  String[] incorrectAnswers;
  Map<String,Object> question;
  String[] tags;
  String type;
  String difficulty;
  String[] regions;
  String isNiche;

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCorrectAnswer() {
    return correctAnswer;
  }

  public void setCorrectAnswer(String correctAnswer) {
    this.correctAnswer = correctAnswer;
  }

  public String[] getIncorrectAnswers() {
    return incorrectAnswers;
  }

  public void setIncorrectAnswers(String[] incorrectAnswers) {
    this.incorrectAnswers = incorrectAnswers;
  }

  public Map<String, Object> getQuestion() {
    return question;
  }

  public void setQuestion(Map<String, Object> question) {
    this.question = question;
  }

  public String[] getTags() {
    return tags;
  }

  public void setTags(String[] tags) {
    this.tags = tags;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(String difficulty) {
    this.difficulty = difficulty;
  }

  public String[] getRegions() {
    return regions;
  }

  public void setRegions(String[] regions) {
    this.regions = regions;
  }

  public String getIsNiche() {
    return isNiche;
  }

  public void setIsNiche(String isNiche) {
    this.isNiche = isNiche;
  }

  @Override
  public String toString() {
    return "Question{" +
        "category='" + category + '\'' +
        ", id='" + id + '\'' +
        ", correctAnswer='" + correctAnswer + '\'' +
        ", incorrectAnswers=" + Arrays.toString(incorrectAnswers) +
        ", question=" + question +
        ", tags=" + tags +
        ", type='" + type + '\'' +
        ", difficulty='" + difficulty + '\'' +
        ", regions=" + Arrays.toString(regions) +
        ", isNiche=" + isNiche +
        '}';
  }

//  private  String type;
//  private String difficulty;
//  private String category;
//  private String question;
//  private String correct_answer;
//  private List<String> incorrect_answers;

}
