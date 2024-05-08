#ifndef QA_H
#define QA_H

#include <string>

/**
 * @class QA
 * @brief A class representing a question and its answer.
 */
class QA {
public:
    /**
     * @brief Constructor for the QA class.
     * @param questionId The unique identifier for the question.
     * @param questionText The text of the question.
     * @param answerText The text of the answer.
     * @param difficultyLevel The difficulty level of the question.
     * @param category The category to which the question belongs.
     */
    QA(int questionId, const std::string& questionText, std::string answerText, const std::string& difficultyLevel, const std::string& category);

    /**
     * @brief Get the question ID.
     * @return The unique identifier for the question.
     */
    int getQuestionId() const;

    /**
     * @brief Get the text of the question.
     * @return The text of the question.
     */
    std::string getQuestionText() const;

    /**
     * @brief Get the text of the answer.
     * @return The text of the answer.
     */
    std::string getAnswerText() const;

    /**
     * @brief Update the answer text (only applicable to the user's QA set)
     * @param userAnswer the user's answer for this question
     */
    void setAnswerText(std::string userAnswer);

    /**
     * @brief Get the difficulty level of the question.
     * @return The difficulty level of the question.
     */
    std::string getDifficultyLevel() const;

    /**
     * @brief Get the category to which the question belongs.
     * @return The category of the question.
     */
    std::string getCategory() const;

private:
    int _questionId; /**< The unique identifier for the question. */
    std::string _questionText; /**< The text of the question. */
    std::string _answerText; /**< The text of the answer. */
    std::string _difficultyLevel; /**< The difficulty level of the question. */
    std::string _category; /**< The category of the question. */
};

#endif
