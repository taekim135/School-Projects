#ifndef QASET_H
#define QASET_H

#include "qa.h"
#include <string>
#include <map>

/**
 * @class QASet
 * @brief A class to manage a set of questions and their answers.
 */
class QASet {
public:

    /**
     * @brief Constructs a QA set
     * @param category The category of questions in the set
     * @param difficulty The difficulty level of questions in the set
     */
    QASet(std::string category, std::string difficulty);

    /**
     * @brief Destructor
     */
    ~QASet(void);

    /**
     * @brief Adds the specified question to the question bank
     * @param question The QA object
     */
    void addQuestion(QA question);

    /**
     * @brief Retrieves the size of the QA set (i.e., the number of QA pairs in the quiz)
     * @return The size of the quiz
     */
    int getSize(void);

    /**
     * @brief Get a specific question by its ID.
     * @param id The ID of the question to retrieve.
     * @return The QA object for the specified question ID. Returns an empty QA object if the question ID is not found.
     */
    QA getQuestion(int id);

    /**
     * @brief Retrieves the category associated with this QA set
     * @return The category of the set
     */
    std::string getCategory(void);

    /**
     * @brief Retrieves the difficulty level associated with this QA set
     * @return The difficulty level of the set
     */
    std::string getDifficultyLevel(void);

private:
    std::map<int, QA> questions; /**< A map of question IDs to QA objects. */
    std::string category;
    std::string difficultyLevel;
};

#endif
