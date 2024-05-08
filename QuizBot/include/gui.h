#ifndef GUI_H
#define GUI_H

#include "qa.h"
#include "qaSet.h"
#include "user.h"
#include "answerScorer.h"
#include <vector>
#include <tuple>
#include <string>
#include <iostream>
#include <fstream>
#include <sstream>
#include <algorithm>
#include <random>
#include <functional>
#include <unistd.h>
#include <filesystem>

#include <Wt/WStackedWidget.h>
#include <Wt/WApplication.h>
#include <Wt/WContainerWidget.h>
#include <Wt/WWidget.h>
#include <Wt/WGridLayout.h>
#include <Wt/WLayoutItem.h>
#include <Wt/WLineEdit.h>
#include <Wt/WTextArea.h>
#include <Wt/WPushButton.h>
#include <Wt/WImage.h>
#include <Wt/WText.h>
#include <Wt/WTable.h>
#include <Wt/WTableRow.h>
#include <Wt/WSignal.h>

/**
 * @class GUI
 * @brief A class representing the graphical user interface for a quiz application.
 */
class GUI : public Wt::WApplication{
public:

    /**
     * @brief Constructor to initialize the GUI
     * @param env
     */
    GUI(const Wt::WEnvironment &env);

    /**
     * @brief Destructor
     */
    ~GUI(void);

    /**
     * @brief Generates a navigation bar component
     * @param showPrivatePages used to customize the navbar links to reflect the user's current status (logged-in or anonymous)
     * @return The navbar widget
     */
    std::unique_ptr<Wt::WContainerWidget> generateNavBar(bool showPrivatePages);

    /**
     * @brief Initializes the main page of the application
     */
    void initializeMainPage(void);

    /**
     * @brief Initializes the login page
     */
    void initializeLoginPage(void);

    /**
     * @brief Initializes the register page
     */
    void initializeRegisterPage(void);

    /**
     * @brief Initializes the leaderboard page
     */
    void initializeLeaderboardPage(void);

    /**
     * @brief Initializes the difficulty page
     */
    void initializeDifficultyPage(void);

    /**
     * @brief Initializes the question page
     */
    void initializeQuestionPage(void);

    /**
     * @brief Initializes the profile page containing all information relevant to the currently logged in user
     */
    void initializeProfilePage(void);

    /**
     * @brief Fetches the current leaderboard data from the specified file
     * @param filePath
     */
    void loadLeaderboard(std::string filePath);

    /**
     * @brief Writes the current contents of the leaderboard to the specified file at the end of a user's session
     * @param filePath
     */
    void saveLeaderboard(std::string filePath);

    /**
     * @brief Display the question page.
     * @param difficulty The difficulty level chosen by the user
     */
    void displayQuestionPage(std::string difficulty);

    /**
     * @brief Randomly selects a set of questions of the correct category and difficulty level for a quiz
     * @param category The selected category
     * @param difficulty The selected difficulty level (1 = "easy", 2 = "medium", 3 = "hard")
     * @param amount The number of questions to be chosen for the quiz
     */
    void loadQuestionSet(std::string category, std::string difficulty, int amount = 5);

    /**
     * @brief Updates the question page.
     */
    void updateQuestionPage();

    /**
     * @brief Displays the current question's answer.
     */
    void displayAnswer();

    /**
     * @brief Display the difficulty page.
     * @param category The category chosen by the user
     */
    void displayDifficultyPage(std::string category);

    /**
     * @brief Display the user profile page.
     */
    void displayUserProfile();

    /**
     * @brief Display the leaderboard page.
     */
    void displayLeaderboard();

    /**
     * @brief Display the main/welcome page.
     */
    void displayMainPage();

    /**
     * @brief Display the login page.
     */
    void displayLoginPage();

    /**
     * @brief Display the register page.
     */
    void displayRegisterPage();

    /**
     * @brief Update the leaderboard.
     */
    void updateLeaderboard();

    /**
     * @brief Update the user's score.
     */
    void updateScore(int amount);

    /**
     * @brief Store the user's score.
     */
    void storeUserScore();

    /**
     * @brief Process the current answer submitted by the user.
     */
    void processCurrAnswer();

    /**
     * @brief Log in the user.
     */
    void loginUser();

    /**
     * @brief Log out the user.
     */
    void logoutUser();

    /**
     * @brief Register a new user.
     */
    void registerUser();

    /**
    * @brief Change password of a current user.
    */
    void changePW();

private:

    // High-level application objects/components
    QASet *answerKey; /**< The answer key for questions. */
    QASet *userAnswers; /**< The user's answers. */
    User *currentUser; /**< The currently logged-in user. */
    AnswerScorer *scoreAnswer; /**< evaluating user's answer. */
    Wt::WStackedWidget* pages; /**< Stores references to the various application pages. */
    std::vector<std::tuple<std::string, int, std::string, std::string>> leaderboard; /**< The leaderboard data. */

    // Additional attributes critical to the game logic
    int currentQuestionID; /**< The current question being displayed. */
    int finalScore; /**< The user's final score. */
    static bool pressed;
    std::string missingKeywords;
    std::string selectedCategory;

    // Containers representing the various pages of the application
    std::unique_ptr<Wt::WContainerWidget> loginPage;
    std::unique_ptr<Wt::WContainerWidget> registerPage;
    std::unique_ptr<Wt::WContainerWidget> mainPage;
    std::unique_ptr<Wt::WContainerWidget> difficultyPage;
    std::unique_ptr<Wt::WContainerWidget> profilePage;
    std::unique_ptr<Wt::WContainerWidget> leaderboardPage;
    std::unique_ptr<Wt::WContainerWidget> questionPage;
    std::unique_ptr<Wt::WContainerWidget> changePWPage;

    /*
     * Maintaining references to specific elements of the GUI to allow for them to be dynamically updated as necessary
     */
    Wt::WLineEdit* questionInput;
    Wt::WTextArea* answerArea;
    Wt::WPushButton* submitButton;
    Wt::WPushButton* answerButton;
    Wt::WText* scoreDisplay;
    Wt::WText* questionProgress;
    Wt::WTable* leaderboardTable;

    Wt::Signals::connection verifyAnswerOn;
    Wt::Signals::connection clickedGameOn;
    Wt::Signals::connection clickedGameOver;
    Wt::Signals::connection enterGameOn;
    Wt::Signals::connection enterGameOver;

    //private variables for login and registration
    Wt::WLineEdit* usernameField;
    Wt::WLineEdit* passwordField;
    Wt::WLineEdit* loginUsernameField;
    Wt::WLineEdit* loginPasswordField;
    Wt::WLineEdit* confirmPasswordField;
    Wt::WText* loginErrorMessage;
    Wt::WText* registerErrorMessage;

    // for changing passwords
    Wt::WLineEdit* changePassword;
    Wt::WLineEdit* confirmPassword;
    Wt::WText* changePWErrorMessage;
};

#endif
