#include "user.h"

/**
 * @brief Constructor for the User class.
 */
User::User(const std::string& userID, const std::string& userPW, int userScore, int ranking) : userID(userID), userPW(userPW), userScore(userScore), ranking(ranking) {
    // Constructor code here
}

/**
 * @brief Get the user's ID.
 */
std::string User::getID() const {
    return userID;
}

/**
 * @brief Get the user's password.
 */
std::string User::getPW() const {
    return userPW;
}

/**
 * @brief Set the user's password.
 * @param newPassword The new password to set.
 */
void User::setPW(const std::string& newPassword) {
    userPW = newPassword;
}

/**
 * @brief Set the user's ID.
 * @param newID The new user ID to set.
 */
void User::setID(const std::string& newID) {
    userID = newID;
}

/**
 * @brief Get the user's score.
 */
int User::getUserScore() const {
    return userScore;
}

/**
 * @brief Set the user's score.
 * @param newScore The new score to set.
 */
void User::setUserScore(int newScore) {
    userScore = newScore;
}

int User::getUserRank() const {
    return ranking;
}

void User::setUserRank(int newRank) {
    ranking = newRank;
}