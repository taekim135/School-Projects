#ifndef USER_H
#define USER_H

#include <string>

/**
 * @class User
 * @brief A class representing a user in the application.
 */
class User {
public:
    /**
     * @brief Constructor for the User class.
     * @param userID The user's ID.
     * @param userPW The user's password.
     * @param userScore The user's score.
     * @param ranking The user's ranking.
     */
    User(const std::string& userID, const std::string& userPW, int userScore, int ranking);

    /**
     * @brief Get the user's ID.
     * @return The user's ID.
     */
    std::string getID() const;

    /**
     * @brief Get the user's password.
     * @return The user's password.
     */
    std::string getPW() const;

    /**
     * @brief Set the user's password.
     * @param newPassword The new password to set.
     */
    void setPW(const std::string& newPassword);

    /**
     * @brief Set the user's ID.
     * @param newID The new user ID to set.
     */
    void setID(const std::string& newID);

    /**
     * @brief Get the user's score.
     * @return The user's score.
     */
    int getUserScore() const;

    /**
     * @brief Set the user's score.
     * @param newScore The new score to set.
     */
    void setUserScore(int newScore);

    /**
     * @brief Get the user's rank.
     * @return The user's rank.
     */
    int getUserRank() const;

    /**
     * @brief Set the user's rank.
     * @param newRank The new rank to set.
     */
    void setUserRank(int newRank);

private:
    std::string userID; /**< The user's ID. */
    std::string userPW; /**< The user's password. */
    int userScore; /**< The user's score. */
    int ranking; /**< The user's ranking. */
};

#endif
