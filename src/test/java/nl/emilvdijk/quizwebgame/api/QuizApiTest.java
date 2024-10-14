package nl.emilvdijk.quizwebgame.api;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class QuizApiTest {

  // FIXME move test from this old class to the new "QuestionsApiServiceTest"
  //  @Test
  //  void getNewQuestion() {
  //
  //    List<Question> questions = questionsApiService.getNewQuestions();
  //
  //    assertEquals(10, questions.size());
  //
  //    for (Question question : questions) {
  //      System.out.println(question);
  //    }
  //  }

  //  @Test
  //  void getNewQuestionWithParams() {
  //    Map<String, String> map = new HashMap<>();
  //    map.put("difficulties", "easy");
  //    map.put("categories", "film_and_tv");
  //
  //    List<Question> questions = questionsApiService.getNewQuestions();
  //
  //    assertEquals(10, questions.size());
  //
  //    for (Question question : questions) {
  //      System.out.println(question);
  //    }
  //  }

  //  @Test
  //  void getNewQuestionsWithOtherApi() throws JSONException {
  //    // https://opentdb.com/
  //    RestTemplate restTemplate = new RestTemplate();
  //
  //    ResponseEntity<String> response =
  //        restTemplate.getForEntity("https://opentdb.com/api.php?amount=10", String.class);
  //
  //    JSONObject jsonob = new JSONObject(response.getBody());
  //    JSONArray json = (JSONArray) jsonob.get("results");
  //
  //    List<QuestionTriviaApi> questions = new ArrayList<>();
  //
  //    for (int i = 0; i < json.length(); i++) {
  //      JSONObject block = json.getJSONObject(i);
  //      QuestionTriviaApi question = new QuestionTriviaApi();
  //      HashMap<String, String> map = new HashMap<>();
  //      map.put("text", block.get("question").toString());
  //      question.setQuestion(map);
  //      question.setCorrectAnswer(block.get("correct_answer").toString());
  //      JSONArray sjonnetje = block.getJSONArray("incorrect_answers");
  //      List<String> listje = new ArrayList<>();
  //      for (int j = 0; j < sjonnetje.length(); j++) {
  //        listje.add(sjonnetje.get(j).toString());
  //      }
  //      question.setIncorrectAnswers(listje);
  //      question.setCategory(block.get("category").toString());
  //      question.setDifficulty(block.get("difficulty").toString());
  //      question.setType(block.get("type").toString());
  //      questions.add(question);
  //    }
  //    List<Question> mappedQuestions =
  //        questionsApiService.getQuestionApiMapperService().mapQuestions(questions);
  //    assertEquals(10, questions.size());
  //    for (Question question : mappedQuestions) {
  //      System.out.println(question);
  //    }
  //  }
}
