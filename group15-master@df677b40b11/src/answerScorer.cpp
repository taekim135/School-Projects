#include "answerScorer.h"

/**
 * @brief Extract keywords from a given text.
 *
 * This function extracts keywords from the input text by tokenizing it and removing common stop words.
 *
 * @param text The input text from which keywords will be extracted.
 * @return A set of keywords.
 */
std::unordered_set<std::string> AnswerScorer::extractKeywords(const std::string& text) {

    std::vector<std::string> tokens = tokenize(text);
    std::unordered_set<std::string> keywords;

    std::unordered_set<std::string> stopWords = {"a", "an", "the", "and", "or", "this", "that", "is", "of", "not"};// add more?

    for (const std::string& token : tokens) {
        if (stopWords.count(token) == 0) {
            keywords.insert(token);
        }
    }

    return keywords;
}

/**
 * @brief Calculate the score for a user's answer based on keyword matching.
 *
 * This function calculates a score for the user's answer based on keyword matching.
 *
 * @param userAnswer The user's answer represented as a QA object.
 * @param correctAnswer The correct answer and the question.
 * @return A tuple containing the user's score, and the keywords missing in their provided answer (if any)
 */
std::tuple<double, std::string> AnswerScorer::calculateAnswerScore(std::string userAnswer, const QA& correctAnswer) {

    // Extract keywords from the user's and correct answers.
    std::vector<std::string> userTokens = tokenize(userAnswer);
    std::unordered_set<std::string> correctKeywords = extractKeywords(correctAnswer.getAnswerText());

    // Keep track of the keywords present (and not present) in the user's answer
    std::unordered_set<std::string> commonKeywords;
    std::string missingKeywords = "";

    // Process all keywords in the user's answer
    for (const std::string& keyword : correctKeywords) {
        if (std::find(userTokens.begin(), userTokens.end(), keyword) != userTokens.end()) {
            commonKeywords.insert(keyword);
        }
        else {
            missingKeywords += keyword + ", ";
        }
    }
    missingKeywords.erase(missingKeywords.find_last_not_of(", ") + 1);

    // Calculate the score as a percentage based on the number of common keywords.
    double score = (static_cast<double>(commonKeywords.size()) / correctKeywords.size()) * 100.0;

    return std::make_tuple(score, missingKeywords);
}

/**
 * @brief Standardizes tokens (to allow for case-insensitive keyword matching)
 * @param token The token
 * @return An equivalent token, but entirely in lowercase
 */
std::string AnswerScorer::toLowerCase(std::string& token) {
    std::string res = "";
    for (char& c : token) {
        res += tolower(c);
    }
    return res;
}

/**
 * @brief Tokenize a string into words.
 *
 * This function splits a string into individual words using space as a delimiter, and removes punctuation.
 *
 * @param input The input string to tokenize.
 * @return A vector of tokens.
 */
std::vector<std::string> AnswerScorer::tokenize(const std::string& input) {
    std::vector<std::string> tokens;
    std::istringstream tokenStream(input);
    std::string token;
    while (std::getline(tokenStream, token, ' ')) {
        if (token != "") {
            token = toLowerCase(token);
            tokens.push_back(removePunctuation(token));
        }
    }
    return tokens;
}

/**
 * @brief Remove punctuation from a string.
 *
 * This function removes punctuation characters from a string.
 *
 * @param str The input string.
 * @return The input string with punctuation characters removed.
 */
std::string AnswerScorer::removePunctuation(const std::string& str) {
    std::string result;
    for (char ch : str) {
        if (!std::ispunct(ch)) {
            result += ch;
        }
    }
    return result;
}