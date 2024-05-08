#ifndef ANSWER_SCORER_H
#define ANSWER_SCORER_H

#include <iostream>
#include <string>
#include <vector>
#include <unordered_set>
#include <sstream>
#include <algorithm>
#include <cctype>
#include <locale>
#include "qa.h"

/**
 * @class AnswerScorer
 * @brief A class for scoring user answers based on keyword comparison.
 */
class AnswerScorer {
public:
    /**
     * @brief Calculate the score for a user's answer based on keyword matching.
     *
     * This function calculates a score for the user's answer based on keyword matching.
     *
     * @param userAnswer The user's answer represented as a QA object.
     * @param correctAnswer The correct answer represented as a QA object.
     * @return A tuple containing the user's score, and the keywords missing in their provided answer (if any)
     */
    std::tuple<double, std::string> calculateAnswerScore(const std::string userAnswer, const QA& correctAnswer);
private:

    /**
     * @brief Extract keywords from a given text.
     *
     * This function extracts keywords from the input text by tokenizing it and removing common stop words.
     *
     * @param text The input text from which keywords will be extracted.
     * @return A set of keywords.
     */
    std::unordered_set<std::string> extractKeywords(const std::string& text);

    /**
     * @brief Tokenize a string into words and remove punctuation.
     *
     * This function splits a string into individual words using space as a delimiter and removes punctuation.
     *
     * @param input The input string to tokenize.
     * @return A vector of tokens with punctuation removed.
     */
    std::vector<std::string> tokenize(const std::string& input);

    /**
     * @brief Standardizes tokens (to allow for case-insensitive keyword matching)
     *
     * @param token The token
     * @return An equivalent token, but entirely in lowercase
     */
    std::string toLowerCase(std::string& token);

    /**
     * @brief Remove punctuation from a string.
     *
     * This function removes punctuation characters from a string.
     *
     * @param str The input string.
     * @return The input string with punctuation characters removed.
     */
    std::string removePunctuation(const std::string& str);
};

#endif
