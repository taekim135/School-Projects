#include "gui.h"

/**
 *Just a dummy variable to check if a button is pressed
 */
bool GUI::pressed = true;

/**
 * @brief Initializes the Wt GUI for the QuizBot application
 * @param env the Wt environment
 * @author Oliver Clennan
 */
GUI::GUI(const Wt::WEnvironment &env): WApplication(env) {

    scoreAnswer = new AnswerScorer();

    // Configure metadata
    setTitle("QuizBot");
    useStyleSheet("src/styles.css");

    // Initialize a stack-based widget to store the different pages of the application
    pages = root()->addWidget(std::make_unique<Wt::WStackedWidget>());

    // Initialize the primary pages of the application
    this->initializeLoginPage();
    this->initializeRegisterPage();
    this->initializeMainPage();
    this->initializeLeaderboardPage();
    this->initializeDifficultyPage();
    this->initializeQuestionPage();

    // Display the login/register page
    pages->setCurrentIndex(0);
}

GUI::~GUI() {}


/**
 * @brief Display the current question's answer.
 * Once the answer is check, the user can proceed to next Q
 * @todo Make sure the answer doesn't appear when pressed multiple times at once
 * @author Taegyun Kim
 */
void GUI::displayAnswer() {
    if (pressed){
        QA currQuestion = answerKey->getQuestion(currentQuestionID);
        answerButton->setText(currQuestion.getAnswerText());

        // Show the missing keywords in the user's answer
        std::string userAnswer = answerArea->valueText().toUTF8();
        std::string res = missingKeywords == "" ? "None" : missingKeywords;
        answerArea->setText(userAnswer + "\n\n\n" + "Missing keywords: " + res);

        std::cout << "Q Answer: " << currQuestion.getAnswerText() << std::endl;
        std::cout << "User Answer: " << answerArea->valueText().toUTF8() << std::endl;
        pressed = false;
    }
}

/**
 * @brief Display the user profile page.
 */
void GUI::displayUserProfile() {
    pages->setCurrentIndex(6);
}

/**
 * @brief Displays the question page
 */
void GUI::displayQuestionPage(std::string difficulty) {

    // Load the appropriate question set
    this->loadQuestionSet(selectedCategory, difficulty);
    currentQuestionID = 1;
    finalScore = 0;
    answerButton->clicked().disconnect(verifyAnswerOn);

    this->updateQuestionPage();
    pages->setCurrentIndex(5);

}

/**
 * @brief Displays the difficulty page
 */
void GUI::displayDifficultyPage(std::string category) {
    selectedCategory = category;
    selectedCategory[0] = std::tolower(selectedCategory[0]);
    pages->setCurrentIndex(4);
}

/**
 * @brief Display the leaderboard page.
 */
void GUI::displayLeaderboard() {
    pages->setCurrentIndex(3);
}

/**
 * @brief Display the main/welcome page.
 */
void GUI::displayMainPage() {
    pages->setCurrentIndex(2);
}

/**
 * @brief Display the register page.
 */
void GUI::displayRegisterPage() {
    pages->setCurrentIndex(1);
}

/**
 * @brief Display the login page.
 */
void GUI::displayLoginPage() {
    pages->setCurrentIndex(0);
}

/**
 * @brief Update the user's score.
 * @author Taegyun Kim
 */
void GUI::updateScore(int amount) {
    finalScore += amount;
}

/**
 * @brief Store the user's score.
 * @author Taegyun Kim
 */
void GUI::storeUserScore() {
    currentUser->setUserScore(finalScore);
}

/**
 * @brief Process the current answer submitted by the user.
 * @brief Partial marks given depending on capitalization, wording etc.
 * @authors Taegyun Kim
 */
void GUI::processCurrAnswer() {

    std::tuple<double, std::string> results = scoreAnswer->calculateAnswerScore(answerArea->valueText().toUTF8(), answerKey->getQuestion(currentQuestionID));
    double score = std::get<0>(results);
    missingKeywords = std::get<1>(results);

    userAnswers->getQuestion(currentQuestionID).setAnswerText(answerArea->valueText().toUTF8());
    updateScore(score);
    storeUserScore();

    std::cout << "Per Q Score: " << score << std::endl;
    std::cout << "Current Total Score: " << finalScore << std::endl;

    currentQuestionID++;

    this->displayAnswer();
    // Update the question page GUI to reflect the next question in the quiz
    // This is done directly by the submitButton in initializeQuestionPage()
}

/**
 * @brief Log in the user.
 * @author Sung Kim
 */
void GUI::loginUser() {
    //Taking in the values from registration page
    bool loginOK = false;
    std::string username = loginUsernameField->text().toUTF8();
    std::string password = loginPasswordField->text().toUTF8();
    int score;
    int rank;

    std::string filename = "user/" + username + ".txt";
    std::fstream file;
    file.open(filename.c_str(), std::ios::in | std::ios::out);
    if(!file) {
        std::cout << "Error: user does not exist" << std::endl;
        loginErrorMessage->setText("Username or Password does not exist");
    } else {
        std::string file_line;
        std::getline(file, file_line);
        std::vector<std::string> tokens;
        std::stringstream ss(file_line);
        std::string token;

        while (std::getline(ss, token, ',')) {
            token.erase(0, token.find_first_not_of(' ')); // leading spaces
            token.erase(token.find_last_not_of(' ') + 1); // trailing spaces
            tokens.push_back(token);
        }

        if (tokens.size() >= 2 && tokens[1] == password) {
            score = std::stoi(tokens[2]);
            rank = std::stoi(tokens[3]);
            loginOK = true;
        } else {
            loginOK = false;
            std::cout << "Error: password does not match" << std::endl;
            loginErrorMessage->setText("Username or Password does not exist");
        }
    }

    // Implementation for user login
    // If log in successful
    if (loginOK == true) {
        currentUser = new User(username, password, score, rank);
        this->initializeProfilePage();
        this->displayMainPage();
    }
}

/**
 * @brief Log out the user.
 */
void GUI::logoutUser() {

    // Implementation for user logout
    std::cout << "User successfully logged out of the application." << std::endl;
    currentUser = nullptr;
    loginUsernameField->setText("");
    loginPasswordField->setText("");

    saveLeaderboard("content/leaderboardData.txt");

    this->displayLoginPage();

}

/**
 * @brief Register a new user.
 * @author Sung Kim
 */
void GUI::registerUser() {
    //Taking in the values from registration page
    bool registerOK = false;
    std::string username = usernameField->text().toUTF8();
    std::string password = passwordField->text().toUTF8();
    std::string confirmPassword = confirmPasswordField->text().toUTF8();

    std::string filename = "user/" + username + ".txt";
    std::fstream file;
    file.open(filename.c_str(), std::ios::in | std::ios::out);
    if(!file) {
        std::cout << "Username available." << std::endl;
        if (confirmPassword == password) {
            registerOK = true;
            registerErrorMessage->setText("");
        } else {
            std::cout << "Error: Password does not match" << std::endl;
            registerErrorMessage->setText("Error: Password does not match");
        }
    } else {
        std::cout << "Error: Username already taken." << std::endl;
        registerErrorMessage->setText("Error: Username already taken.");
        registerOK = false; // set regOK to false if username exists
    }
    file.close();


    // Implementation for user registration
    // If registration successful
    if (registerOK == true) {
        currentUser = new User(username, password, 0, 0);
        this->initializeProfilePage();
        this->displayMainPage();

        // Create a text file in the 'user' directory
        std::string filename = "user/" + currentUser->getID() + ".txt";
        std::ofstream outfile(filename);

        // Write user details to the file
        outfile << currentUser->getID() << ", " << currentUser->getPW() << ", " << currentUser->getUserScore() << ", " << currentUser->getUserRank();
        outfile.close();

    }
}

/**
 * @brief Randomly selects a set of questions of the correct category and difficulty level for a quiz
 * @param category The selected category
 * @param difficulty The selected difficulty level ("easy", "medium", or "hard")
 * @param amount The number of questions to be chosen for the quiz (default = 5)
 * @author Oliver Clennan
 */
void GUI::loadQuestionSet(std::string category, std::string difficulty, int amount) {

    difficulty[0] = std::tolower(difficulty[0]);

    // Define a mapping of difficulty levels to difficulty codes
    std::map<std::string, char> difficultyCodes = {
            {"easy", '0'},
            {"medium", '1'},
            {"hard", '2'}
    };

    // Open the appropriate question bank file for reading (i.e., the one corresponding to the specified category)
    std::string filePath = "questions/" + category + ".txt";
    std::ifstream inFile(filePath);
    if (!inFile.is_open()) {
        std::cerr << "Error - failed to locate question bank at: " << filePath << std::endl;
        return;
    }

    std::vector<std::string> allQuestions;
    std::string currQuestion;

    // Retrieve all questions in the category which match the desired difficulty level
    while (std::getline(inFile, currQuestion)) {
        if (currQuestion[0] == difficultyCodes[difficulty]) {
            allQuestions.push_back(currQuestion);
        }
    }

    // Randomly shuffle the question collection
    std::shuffle(allQuestions.begin(), allQuestions.end(), std::random_device());

    // Select the desired amount of questions from the set
    std::vector<std::string> chosenQuestions;
    for (int i = 0; i < amount; i++) {
        chosenQuestions.push_back(allQuestions[i]);
    }

    std::vector<std::string> currRow;
    std::string currToken;

    answerKey = new QASet(category, difficulty);
    userAnswers = new QASet(category, difficulty);

    // Iterate through all questions selected for the quiz
    for (int i = 0; i < chosenQuestions.size(); i++) {

        currRow.clear();
        std::stringstream ss(chosenQuestions[i]);

        while (std::getline(ss, currToken, ',')) {
            currRow.push_back(currToken);
        }

        // Construct a QA object from the contents of the current row, and update the question sets accordingly
        QA question(i + 1, currRow[1], currRow[2], difficulty, category);
        answerKey->addQuestion(question);
        userAnswers->addQuestion(question);

    }

    // Close the input file
    inFile.close();

}

/**
 * @brief Writes the current contents of the leaderboard to the specified output file
 * @param filePath The path to the output file (relative to the root directory of the project)
 * @author Oliver Clennan
 */
void GUI::saveLeaderboard(std::string filePath) {

    // Opens the specified file for writing
    std::ofstream outFile(filePath);
    if (!outFile.is_open()) {
        std::cerr << "Error - failed to locate the output file." << std::endl;
        return;
    }

    // Write all the leaderboard entries to the file (one row per line)
    for (int i = 0; i < leaderboard.size(); i++) {
        outFile << std::get<0>(leaderboard[i]) << ",";
        outFile << std::to_string(std::get<1>(leaderboard[i])) << ",";
        outFile << std::get<2>(leaderboard[i]) << ",";
        outFile << std::get<3>(leaderboard[i]) << std::endl;
    }

    // Close the file
    outFile.close();

}

/**
 * @brief Fetches the current leaderboard data from the specified file
 * @param filePath The path to the input data file (relative to the root directory of the project)
 * @author Oliver Clennan
 */
void GUI::loadLeaderboard(std::string filePath) {

    // Opens the provided file for reading
    std::ifstream dataFile(filePath);
    if (!dataFile.is_open()) {
        std::cerr << "Error - failed to locate the leaderboard data file." << std::endl;
        return;
    }
    std::vector<std::string> currRow;
    std::string currLine, currToken;

    // Iterate through all lines (i.e., leaderboard entries) contained in the file
    while (std::getline(dataFile, currLine)) {
        currRow.clear();
        std::stringstream ss(currLine);
        while (std::getline(ss, currToken, ',')) {
            currRow.push_back(currToken);
        }
        // Add each leaderboard entry to the leaderboard collection
        std::tuple<std::string, int, std::string, std::string> newEntry = make_tuple(currRow[0], std::stoi(currRow[1]), currRow[2], currRow[3]);
        leaderboard.push_back(newEntry);
    }

    // Close the file
    dataFile.close();

}

/**
 * @brief Updates the leaderboard with the current user's score
 * @author Oliver Clennan
 */
void GUI::updateLeaderboard() {

    std::tuple<std::string, int, std::string, std::string> newEntry = make_tuple(currentUser->getID(), currentUser->getUserScore(), answerKey->getCategory(), answerKey->getDifficultyLevel());

    bool highScore = true;
    for (int i = 0; i < leaderboard.size(); i++) {
        if ((std::get<0>(leaderboard[i]) == currentUser->getID()) && (std::get<2>(leaderboard[i]) == answerKey->getCategory()) && (std::get<3>(leaderboard[i]) == answerKey->getDifficultyLevel())) {
            if (std::get<1>(leaderboard[i]) > currentUser->getUserScore()) {
                highScore = false;
            }
            // If this is the user's high score in the category and difficulty level, remove the previous entry from the leaderboard
            else {
                leaderboard.erase(leaderboard.begin() + i);
            }
        }
    }

    // If it is a high score for the user, update the leaderboard accordingly
    if (highScore) {
        leaderboard.push_back(newEntry);
    }

    // Re-sort the leaderboard entries in descending order by the score field
    std::sort(leaderboard.begin(), leaderboard.end(), [](const std::tuple<std::string, int, std::string, std::string> left, std::tuple<std::string, int, std::string, std::string> right) {
        return std::get<1>(left) > std::get<1>(right);
    });

    // Reset the contents of the leaderboard table element
    leaderboardTable->clear();

    // Re-attach the newly updated entries to the leaderboard
    for (int i = 0; i < leaderboard.size(); i++) {
        Wt::WTableRow* leaderboardEntry = leaderboardTable->insertRow(i, std::make_unique<Wt::WTableRow>());
        leaderboardEntry->setStyleClass("leaderboard-entry");
        leaderboardEntry->elementAt(0)->addWidget(std::make_unique<Wt::WText>(std::to_string(i + 1)));
        leaderboardEntry->elementAt(1)->addWidget(std::make_unique<Wt::WText>(std::get<0>(leaderboard[i])));
        leaderboardEntry->elementAt(2)->addWidget(std::make_unique<Wt::WText>(std::to_string(std::get<1>(leaderboard[i]))));
        leaderboardEntry->elementAt(3)->addWidget(std::make_unique<Wt::WText>(std::get<2>(leaderboard[i])));
        leaderboardEntry->elementAt(4)->addWidget(std::make_unique<Wt::WText>(std::get<3>(leaderboard[i])));
    }

}

/**
 * Creates a navigation bar component (allows for the easy reuse of the navbar across all pages in the application)
 * Note: this method is necessary because Wt does not directly support the sharing of widgets (due to unique pointers)
 * @param showPrivatePages used to customize the navbar links to reflect the user's current status (logged-in or anonymous)
 *
 * Public pages:  Login Register
 * Private pages: Home Profile Leaderboard Logout
 *
 * @return The resulting navbar widget
 * @author Oliver Clennan
 */
std::unique_ptr<Wt::WContainerWidget> GUI::generateNavBar(bool showPrivatePages) {

    std::unique_ptr<Wt::WContainerWidget> navBar = std::make_unique<Wt::WContainerWidget>();
    navBar->setStyleClass("nav-bar");

    // Add two wrapper containers for the title/logo and navbar items
    Wt::WContainerWidget* titleContainer = navBar->addWidget(std::make_unique<Wt::WContainerWidget>());
    Wt::WContainerWidget* navItems = navBar->addWidget(std::make_unique<Wt::WContainerWidget>());

    // Define the application name and logo
    Wt::WText* appTitle = titleContainer->addWidget(std::make_unique<Wt::WText>("QuizBot"));
    Wt::WImage* appIcon = titleContainer->addWidget(std::make_unique<Wt::WImage>("content/appIcon.png"));

    // Add additional styling for the elements in the navbar
    appTitle->setStyleClass("title");
    titleContainer->setStyleClass("title-container");
    navItems->setStyleClass("nav-items");

    // Provide links only to the pages accessible to anonymous users (i.e., public pages)
    if (!showPrivatePages) {

        // Define the appropriate page links
        Wt::WPushButton* loginButton = navItems->addWidget(std::make_unique<Wt::WPushButton>("Login"));
        Wt::WPushButton* registerButton = navItems->addWidget(std::make_unique<Wt::WPushButton>("Register"));

        // Make the links functional
        loginButton->clicked().connect(this, &GUI::displayLoginPage);
        registerButton->clicked().connect(this, &GUI::displayRegisterPage);

    }

        // Provide links to all pages accessible to logged-in users (i.e., private pages)
    else {

        // Define the appropriate page links
        Wt::WPushButton* homeButton = navItems->addWidget(std::make_unique<Wt::WPushButton>("Home"));
        Wt::WPushButton* profileButton = navItems->addWidget(std::make_unique<Wt::WPushButton>("Profile"));
        Wt::WPushButton* leaderboardButton = navItems->addWidget(std::make_unique<Wt::WPushButton>("Leaderboard"));
        Wt::WPushButton* logoutButton = navItems->addWidget(std::make_unique<Wt::WPushButton>("Logout"));

        // Make the links functional
        homeButton->clicked().connect(this, &GUI::displayMainPage);
        profileButton->clicked().connect(this, &GUI::displayUserProfile);
        leaderboardButton->clicked().connect(this, &GUI::displayLeaderboard);
        logoutButton->clicked().connect(this, &GUI::logoutUser);
        titleContainer->clicked().connect(this, &GUI::displayMainPage);

    }

    return navBar;

}

/**
 * @brief Updates the question page with the information relevant to the next question in the quiz
 * @author Oliver Clennan
 */
void GUI::updateQuestionPage() {

    // Access the next question in the quiz
    QA nextQuestion = answerKey->getQuestion(currentQuestionID);
    bool isLastQuestion = currentQuestionID == answerKey->getSize();
    bool isFirstQuestion = currentQuestionID == 1;

    // If this is the first question in the quiz, initiate button and enter key connections
    if (isFirstQuestion) {
        verifyAnswerOn = answerButton->clicked().connect(this, &GUI::processCurrAnswer);
        submitButton->clicked().disconnect(clickedGameOver);
        clickedGameOn = submitButton->clicked().connect(this, &GUI::updateQuestionPage);
        answerArea->enterPressed().disconnect(enterGameOver);
        enterGameOn = answerArea->enterPressed().connect(this, &GUI::updateQuestionPage);
    }

    std::string buttonText = isLastQuestion ? "Submit" : "Next";
    std::string questionText = nextQuestion.getQuestionText();
    std::string currentProgress = std::to_string(currentQuestionID) + "/" + std::to_string(answerKey->getSize());
    std::string currentScore = "Current Score: " + std::to_string(finalScore) + "/500";

    // Update the relevant elements in the GUI to reflect the new question
    questionInput->setPlaceholderText(questionText);
    answerArea->setText("");
    answerButton->setText("Check Answer");
    pressed = true;
    submitButton->setText(buttonText);
    questionProgress->setText(currentProgress);
    scoreDisplay->setText(currentScore);

    // If this is the last question in the quiz, reset attributes, and update button/enter connections
    if (isLastQuestion) {

        this->updateLeaderboard();
        answerArea->setText("");
        questionInput->setText("");

        submitButton->clicked().disconnect(clickedGameOn);
        clickedGameOver = submitButton->clicked().connect(this, &GUI::displayLeaderboard);
        answerArea->enterPressed().disconnect(enterGameOver);
        enterGameOver = answerArea->enterPressed().connect(this, &GUI::displayLeaderboard);

        pressed = true;
    }

}

/**
 * @brief Initialize the profile page which shows all information of the current user logged in.
 * @author Jiho Choi
 */
void GUI::initializeProfilePage() {

    profilePage = std::make_unique<Wt::WContainerWidget>();
    profilePage->addWidget(this->generateNavBar(true));

    Wt::WContainerWidget* pageContent = profilePage->addWidget(std::make_unique<Wt::WContainerWidget>());
    pageContent->setStyleClass("profile-display");

    // Add elements to the pageContent container
    // Current user information.
    Wt::WText* userInfo = pageContent->addWidget(std::make_unique<Wt::WText>("User Information"));
    userInfo->setStyleClass("profile-title");

    // user ID
    Wt::WText* userID = pageContent->addWidget(std::make_unique<Wt::WText>("User ID: " + currentUser->getID()));

    // change password section
    Wt::WText* changePWTitle = pageContent->addWidget(std::make_unique<Wt::WText>("Change Password"));
    changePWTitle->setStyleClass("profile-sub");

    Wt::WText* changePasswordTitle = pageContent->addWidget(std::make_unique<Wt::WText>("New Password: "));
    Wt::WLineEdit* changePassword = pageContent->addWidget(std::make_unique<Wt::WLineEdit>());
    changePassword->setStyleClass("form");
    changePassword->setPlaceholderText("Password");

    Wt::WText* confirmPasswordTitle = pageContent->addWidget(std::make_unique<Wt::WText>("Confirm New Password: "));
    Wt::WLineEdit* confirmPassword = pageContent->addWidget(std::make_unique<Wt::WLineEdit>());
    confirmPassword->setStyleClass("form");
    confirmPassword->setPlaceholderText("Password");

    // Change Password button; once clicked, leads to the changePW method, where
    Wt::WPushButton* changePWButton = pageContent->addWidget(std::make_unique<Wt::WPushButton>("Change Password"));
    changePWButton->setStyleClass("profile-button");
    changePWButton->clicked().connect(this, &GUI::changePW);


    // Quiz history of the current user.
    Wt::WText* userHistory = pageContent->addWidget(std::make_unique<Wt::WText>("Best User Score History"));
    userHistory->setStyleClass("profile-title");

    // number of tries the current user have tried.
    int count = 1;

    // if same id found in the leaderboard, it prints the record the user have tried in order of the best to the worst
    for (int i = 0; i < leaderboard.size(); i++) {
        if ((std::get<0>(leaderboard[i]) == currentUser->getID())) {
            Wt::WText* printScore = pageContent->addWidget(std::make_unique<Wt::WText>(std::to_string(count) + ". " + std::to_string(std::get<1>(leaderboard[i]))));
            count++;
        }
    }
    pages->addWidget(std::move(profilePage));
}

/**
 * @brief Initialize the change password page where the user can change the password.
 *
 * @author Jiho Choi
*/
void GUI::changePW() {
    bool changePWSucceed = false;
    std::string changePW = changePassword->text().toUTF8();
    std::string confirmPW = confirmPassword->text().toUTF8();

    std::string filename = "user/" + currentUser->getID() + ".txt";
    std::fstream file;
    file.open(filename.c_str(), std::ios::in | std::ios::out);

    if (file) {
        if (confirmPW == changePW) {
            changePWSucceed = true;
            changePWErrorMessage->setText("");
            std::filesystem::remove(filename);
            std::ofstream outfile(filename);
            currentUser = new User(currentUser->getID(), changePW, 0, 0);

            // Write user details to the file
            outfile << currentUser->getID() << ", " << currentUser->getPW() << ", " << currentUser->getUserScore() << ", " << currentUser->getUserRank();
            outfile.close();
        } else {
            std::cout << "Error: Password does not match" << std::endl;
            changePWErrorMessage->setText("Error: Password does not match");
        }
        file.close(); // Close the file before attempting to remove or recreate
    } else {
        changePWErrorMessage->setText("Error: cannot change the password");
        changePWSucceed = false;
    }
}

/**
 * @brief Initializes the question page (where the user can view and answer a question in the quiz)
 * @author Oliver Clennan
 */
void GUI::initializeQuestionPage() {

    // Creating the question page, and generating/attaching the navbar
    questionPage = std::make_unique<Wt::WContainerWidget>();
    questionPage->addWidget(this->generateNavBar(true));

    Wt::WContainerWidget* pageContent = questionPage->addWidget(std::make_unique<Wt::WContainerWidget>());
    pageContent->setStyleClass("question");

    // Configuring the question label and input
    Wt::WContainerWidget* questionWrapper = pageContent->addWidget(std::make_unique<Wt::WContainerWidget>());
    questionWrapper->setStyleClass("question-wrapper");
    Wt::WText* questionLabel = questionWrapper->addWidget(std::make_unique<Wt::WText>("Question"));
    questionInput = questionWrapper->addWidget(std::make_unique<Wt::WLineEdit>());
    questionInput->setPlaceholderText("");
    questionInput->setDisabled(true);

    // Configuring the answer label and textarea
    Wt::WContainerWidget* answerWrapper = pageContent->addWidget(std::make_unique<Wt::WContainerWidget>());
    answerWrapper->setStyleClass("answer-wrapper");
    Wt::WText* answerLabel = answerWrapper->addWidget(std::make_unique<Wt::WText>("Answer"));
    answerArea = answerWrapper->addWidget(std::make_unique<Wt::WTextArea>());

    // Configuring the answer button
    Wt::WContainerWidget* answerButtonWrapper = pageContent->addWidget(std::make_unique<Wt::WContainerWidget>());
    answerButtonWrapper->setStyleClass("button-wrapper");
    answerButton = answerButtonWrapper->addWidget(std::make_unique<Wt::WPushButton>("Check Answer"));

    // Configuring the submit button
    Wt::WContainerWidget* buttonWrapper = pageContent->addWidget(std::make_unique<Wt::WContainerWidget>());
    buttonWrapper->setStyleClass("button-wrapper");
    submitButton = buttonWrapper->addWidget(std::make_unique<Wt::WPushButton>("Next"));
    submitButton->show();

    // Configuring score to be displayed per question
    scoreDisplay = pageContent->addWidget(std::make_unique<Wt::WText>("Current Score " + std::to_string(finalScore)));
    scoreDisplay->setStyleClass("question-progress");

    // Attaching the current question number to illustrate the users progress through the quiz
    questionProgress = pageContent->addWidget(std::make_unique<Wt::WText>(std::to_string(currentQuestionID) + "/" + std::to_string(answerKey->getSize())));
    questionProgress->setObjectName("questionProgress");
    questionProgress->setStyleClass("question-progress");

    pages->addWidget(std::move(questionPage));
}

/**
 * @brief Initializes the difficulty page (where the user selects a difficulty level for the quiz)
 * @author Oliver Clennan
 */
void GUI::initializeDifficultyPage() {

    // Defining the page content
    const std::string SECTION_HEADER = "Select your preferred difficulty level, and let the games begin!";
    const std::string SECTION_NOTE = "Note: you can always change the difficulty level later on, in case you want to try something more challenging, or more relaxed.";
    const std::vector<std::string> DIFFICULTY_LEVELS = {"Easy", "Medium", "Hard"};

    // Creating the difficulty page, and generating/attaching the navbar
    difficultyPage = std::make_unique<Wt::WContainerWidget>();
    difficultyPage->addWidget(this->generateNavBar(true));

    Wt::WContainerWidget* pageContent = difficultyPage->addWidget(std::make_unique<Wt::WContainerWidget>());
    pageContent->setStyleClass("difficulty");

    // Defining the page header
    Wt::WText* sectionHeader = pageContent->addWidget(std::make_unique<Wt::WText>(SECTION_HEADER));
    sectionHeader->setStyleClass("difficulty-header");

    Wt::WContainerWidget* levelsWrapper = pageContent->addWidget(std::make_unique<Wt::WContainerWidget>());
    levelsWrapper->setStyleClass("levels-wrapper");

    // Attach all buttons (one per difficulty level)
    for (int i = 0; i < DIFFICULTY_LEVELS.size(); i++) {
        Wt::WPushButton* difficultyButton = levelsWrapper->addWidget(std::make_unique<Wt::WPushButton>(DIFFICULTY_LEVELS[i]));
        difficultyButton->setStyleClass("primary-button");
        difficultyButton->clicked().connect(std::bind(&GUI::displayQuestionPage, this, DIFFICULTY_LEVELS[i]));
    }

    // Defining the side note
    Wt::WText* sideNote = pageContent->addWidget(std::make_unique<Wt::WText>(SECTION_NOTE));

    pages->addWidget(std::move(difficultyPage));

}

/**
 * @brief Initializes the leaderboard page
 * @author Oliver Clennan
 */
void GUI::initializeLeaderboardPage() {

    // Load the leaderboard data and add the navbar to the leaderboard widget
    this->loadLeaderboard("content/leaderboardData.txt");
    leaderboardPage = std::make_unique<Wt::WContainerWidget>();
    leaderboardPage->addWidget(this->generateNavBar(true));

    // Re-sort the leaderboard entries in descending order by the score field
    std::sort(leaderboard.begin(), leaderboard.end(), [](const std::tuple<std::string, int, std::string, std::string> left, std::tuple<std::string, int, std::string, std::string> right) {
        return std::get<1>(left) > std::get<1>(right);
    });

    Wt::WContainerWidget* pageContent = leaderboardPage->addWidget(std::make_unique<Wt::WContainerWidget>());
    pageContent->setStyleClass("leaderboard");

    Wt::WText* leaderboardTitle = pageContent->addWidget(std::make_unique<Wt::WText>("Leaderboard"));
    leaderboardTitle->setStyleClass("leaderboard-title");
    leaderboardTable = pageContent->addWidget(std::make_unique<Wt::WTable>());

    // Attach all entries to the leaderboard
    for (int i = 0; i < leaderboard.size(); i++) {
        Wt::WTableRow* leaderboardEntry = leaderboardTable->insertRow(i, std::make_unique<Wt::WTableRow>());
        leaderboardEntry->setStyleClass("leaderboard-entry");
        leaderboardEntry->elementAt(0)->addWidget(std::make_unique<Wt::WText>(std::to_string(i + 1)));
        leaderboardEntry->elementAt(1)->addWidget(std::make_unique<Wt::WText>(std::get<0>(leaderboard[i])));
        leaderboardEntry->elementAt(2)->addWidget(std::make_unique<Wt::WText>(std::to_string(std::get<1>(leaderboard[i]))));
        leaderboardEntry->elementAt(3)->addWidget(std::make_unique<Wt::WText>(std::get<2>(leaderboard[i])));
        leaderboardEntry->elementAt(4)->addWidget(std::make_unique<Wt::WText>(std::get<3>(leaderboard[i])));
    }

    pages->addWidget(std::move(leaderboardPage));

}

/**
 * @brief Initializes the main page of the GUI
 * @author Oliver Clennan
 */
void GUI::initializeMainPage() {

    // Defining the welcome messages to be displayed on the page
    const std::vector<std::string> WELCOME_MESSAGES = {
            "Welcome to QuizBot, your go-to destination for trivia fun and excitement! Our web application is designed with trivia enthusiasts in mind, offering a seamless and enjoyable experience for users of all levels. With a sleek and user-friendly interface, QuizBot opens the door to a world of trivia quizzes spanning a wide range of categories, ensuring there's something for everyone.",
            "What sets QuizBot apart is its commitment to accessibility and engagement. We believe that trivia should be a delightful experience for all, and that's why we provide both text input and speech recognition options for answering questions. Whether you prefer typing out your answers or speaking them aloud, QuizBot has you covered.",
            "At the heart of QuizBot is our mission to make trivia enjoyable and hassle-free. You can easily browse and select quiz categories that pique your interest, dive into quizzes, and keep track of your scores. With an extensive and diverse database of trivia questions meticulously organized by topics, finding quizzes that match your interests has never been easier.",
            "Join us on a journey of knowledge, fun, and friendly competition with QuizBot. Let's get started!"
    };

    // Defining the image and name of each category supported by the application
    const std::vector<std::tuple<std::string, std::string>> CATEGORIES = {
            std::make_tuple("content/science.png", "Science"),
            std::make_tuple("content/culture.jpg", "Culture"),
            std::make_tuple("content/geography.png", "Geography"),
            std::make_tuple("content/history.jpeg", "History"),
            std::make_tuple("content/sports.jpeg", "Sports"),
            std::make_tuple("content/politics.jpg", "Politics"),
    };

    // Attach the navbar to the main page
    mainPage = std::make_unique<Wt::WContainerWidget>();
    mainPage->addWidget(this->generateNavBar(true));
    Wt::WContainerWidget* pageContent = mainPage->addWidget(std::make_unique<Wt::WContainerWidget>());
    pageContent->setStyleClass("main");

    Wt::WContainerWidget* welcomeText = pageContent->addWidget(std::make_unique<Wt::WContainerWidget>());
    welcomeText->setStyleClass("welcome-text");

    // Attach the welcome messages
    for (int i = 0; i < WELCOME_MESSAGES.size(); i++) {
        welcomeText->addWidget(std::make_unique<Wt::WText>(WELCOME_MESSAGES[i]));
    }

    // Attach the start quiz button, and make it functional (redirect to the difficult page when clicked)
    Wt::WPushButton* startButton = pageContent->addWidget(std::make_unique<Wt::WPushButton>("Start Quiz"));
    startButton->setStyleClass("primary-button");
    startButton->clicked().connect(std::bind(&GUI::displayDifficultyPage, this, "science"));

    // Create and style the category grid
    Wt::WContainerWidget* categoryGrid = pageContent->addWidget(std::make_unique<Wt::WContainerWidget>());
    categoryGrid->setStyleClass("category-grid");

    // Defining the category options
    // When a category is clicked/selected, it will redirect the user to the difficulty page
    for (int i = 0; i < CATEGORIES.size(); i++) {
        Wt::WContainerWidget* categoryWrapper = categoryGrid->addWidget(std::make_unique<Wt::WContainerWidget>());
        Wt::WContainerWidget* category = categoryWrapper->addWidget(std::make_unique<Wt::WContainerWidget>());
        category->addWidget(std::make_unique<Wt::WContainerWidget>());
        category->addWidget(std::make_unique<Wt::WImage>(std::get<0>(CATEGORIES[i])));
        category->addWidget(std::make_unique<Wt::WText>(std::get<1>(CATEGORIES[i])));
        category->clicked().connect(std::bind(&GUI::displayDifficultyPage, this, std::get<1>(CATEGORIES[i])));
    }

    pages->addWidget(std::move(mainPage));

}

/**
 * @brief Initializes the register page
 * @author Oliver Clennan, Sung Kim
 */
void GUI::initializeRegisterPage() {

    registerPage = std::make_unique<Wt::WContainerWidget>();
    registerPage->addWidget(this->generateNavBar(false));

    Wt::WContainerWidget* pageContent = registerPage->addWidget(std::make_unique<Wt::WContainerWidget>());
    pageContent->setStyleClass("form-wrapper");

    Wt::WContainerWidget* registerForm = pageContent->addWidget(std::make_unique<Wt::WContainerWidget>());
    registerForm->setStyleClass("form");

    Wt::WText* registerHeader = registerForm->addWidget(std::make_unique<Wt::WText>("Create Account"));
    registerHeader->setStyleClass("form-header");

    //being tested by sung. Original altered so the values can be passed on
    usernameField = registerForm->addWidget(std::make_unique<Wt::WLineEdit>());
    usernameField->setPlaceholderText("Username");
    passwordField = registerForm->addWidget(std::make_unique<Wt::WLineEdit>());
    passwordField->setPlaceholderText("Password");
    confirmPasswordField = registerForm->addWidget(std::make_unique<Wt::WLineEdit>());
    confirmPasswordField->setPlaceholderText("Password");

    //error message for registration error being tested
    registerErrorMessage = registerForm->addWidget(std::make_unique<Wt::WText>());
    registerErrorMessage->setStyleClass("form-errormessage");

    Wt::WPushButton* registerButton = registerForm->addWidget(std::make_unique<Wt::WPushButton>("Register"));
    registerButton->clicked().connect(this, &GUI::registerUser);

    Wt::WContainerWidget* redirectWrapper = registerForm->addWidget(std::make_unique<Wt::WContainerWidget>());
    redirectWrapper->setStyleClass("form-redirect");
    Wt::WText* redirectMessage = redirectWrapper->addWidget(std::make_unique<Wt::WText>("Already have an account?"));
    Wt::WPushButton* redirectLink = redirectWrapper->addWidget(std::make_unique<Wt::WPushButton>("Login"));
    redirectLink->clicked().connect(this, &GUI::displayLoginPage);

    pages->addWidget(std::move(registerPage));

}

/**
 * @brief Initializes the login page
 * @author Oliver Clennan, Sung Kim, Taegyun Kim
 */
void GUI::initializeLoginPage() {

    loginPage = std::make_unique<Wt::WContainerWidget>();
    loginPage->addWidget(this->generateNavBar(false));

    Wt::WContainerWidget* pageContent = loginPage->addWidget(std::make_unique<Wt::WContainerWidget>());
    pageContent->setStyleClass("form-wrapper");

    Wt::WContainerWidget* loginForm = pageContent->addWidget(std::make_unique<Wt::WContainerWidget>());
    loginForm->setStyleClass("form");

    Wt::WText* loginHeader = loginForm->addWidget(std::make_unique<Wt::WText>("Login"));
    loginHeader->setStyleClass("form-header");

    loginUsernameField = loginForm->addWidget(std::make_unique<Wt::WLineEdit>());
    loginUsernameField->setPlaceholderText("Username");
    loginPasswordField = loginForm->addWidget(std::make_unique<Wt::WLineEdit>());
    loginPasswordField->setPlaceholderText("Password");

    //error message for login error being tested
    loginErrorMessage = loginForm->addWidget(std::make_unique<Wt::WText>());
    loginErrorMessage->setStyleClass("form-errormessage");

    Wt::WPushButton* loginButton = loginForm->addWidget(std::make_unique<Wt::WPushButton>("Login"));
    loginButton->clicked().connect(this, &GUI::loginUser);
    loginPasswordField->enterPressed().connect(this, &GUI::loginUser);

    Wt::WContainerWidget* redirectWrapper = loginForm->addWidget(std::make_unique<Wt::WContainerWidget>());
    redirectWrapper->setStyleClass("form-redirect");
    Wt::WText* redirectMessage = redirectWrapper->addWidget(std::make_unique<Wt::WText>("Don't have an account?"));
    Wt::WPushButton* redirectLink = redirectWrapper->addWidget(std::make_unique<Wt::WPushButton>("Register"));
    redirectLink->clicked().connect(this, &GUI::displayRegisterPage);

    pages->addWidget(std::move(loginPage));

}
