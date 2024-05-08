#include "qaSet.h"

/**
 * @brief Constructs a QA set
 * @param category The category of questions in the set
 * @param difficulty The difficulty level
 */
QASet::QASet(const std::string category, const std::string difficulty) {
    this->category = category;
    this->difficultyLevel = difficulty;
}

/**
 * @brief Inserts the given question into the collection
 * @param question The question to be inserted
 */
void QASet::addQuestion(QA question) {
    this->questions.insert({question.getQuestionId(), question});
}

/**
 * @brief Retrieves the size of the QA set (i.e., the number of QA pairs in the quiz)
 * @return The size of the quiz
 */
int QASet::getSize() {
    return this->questions.size();
}

/**
 * @brief Retrieves the category associated with this QA set
 * @return The category
 */
std::string QASet::getCategory() {
    return this->category;
}

/**
 * @brief Retrieves the difficulty level associated with this QA set
 * @return The difficulty level
 */
std::string QASet::getDifficultyLevel() {
    return this->difficultyLevel;
}

/**
 * @brief Get a specific question by its ID.
 */
QA QASet::getQuestion(int id) {
    // Check if the question ID exists in the map
    auto it = questions.find(id);
    if (it != questions.end()) {
        return it->second; // Return the QA object for the specified ID
    } else {
        // Return an empty QA object if the ID is not found
        return QA(0, "", "", "", "");
    }
}
