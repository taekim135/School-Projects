#include "gui.h"
#include "answerScorer.h"
#include "qa.h" 

int main(int argc, char **argv)
{
    // use for testing for now

//   // Create a QA object for the question and answer.
//   QA questionAnswer(1, "What is the capital of France?", "the capital of France is Paris", "Medium", "Geography");
//
//   // Set up a set of keywords for the question. <-- check where to get this and where to store it
//   std::unordered_set<std::string> questionKeywords = {"Paris"};
//
//   // Create an instance of AnswerScorer.
//   AnswerScorer scorer;
//
//   // User's answer to the question.
//   QA userAnswer(1,"" , "it is Paris.", "", "");
//   QA userAnswer1(1,"" , "The capital of France is Paris", "", "");
//   QA userAnswer2(1,"" , "capital France Paris", "", "");
//
//   // Calculate the score based on the user's answer and question keywords.
//   double score = scorer.calculateAnswerScore(userAnswer, questionAnswer);
//   double score1 = scorer.calculateAnswerScore(userAnswer1, questionAnswer);
//   double score2 = scorer.calculateAnswerScore(userAnswer2, questionAnswer);

//   // Display the calculated score.
//   std::cout << "Score for user's answers: " << score << "%" << std::endl<< score1 << "%" << std::endl<< score2 << "%" << std::endl;

      return Wt::WRun(argc, argv, [](const Wt::WEnvironment &env)
                      {
                          return std::make_unique<GUI>(env);
                      });

}
