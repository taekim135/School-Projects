Instructions to Run the GUI:
1. Ensure you are in the root of the QuizBot directory
2. Execute the command [make gui] to build/compile the application
3. Execute the command [./gui --docroot . --http-listen 0.0.0.0:8080]
4. Go into your browser and type localhost:8080 to view the GUI

Note - if frequently testing GUI-related changes, make an alias to speed up the re-building process:
1. alias gui='make gui && ./gui --docroot . --http-listen 0.0.0.0:8080'
Note - if CSS changes are not updating properly in the browser, clear your browsers cache and try again





// vcpkg from GitHub won't allow use to commit and push to BitBucket
// External Library curl folder won't commit and push to BitBucket
// In class quizapi.cpp curl Library cannot be pushed to BitBucket due to authentication/credential issue

//api utilisation in cpp issue due to external lib issue




// ** BELOW is the ERROR messge we get when trying to push **

//22:43:40.365: [vcpkg] git -c credential.helper= -c core.quotepath=false -c log.showSignature=false push --progress --porcelain origin refs/heads/master:master
//        remote: Permission to Microsoft/vcpkg.git denied to taekim135.
//fatal: unable to access 'https://github.com/Microsoft/vcpkg.git/': The requested URL returned error: 403
//22:43:45.075: [vcpkg] git -c credential.helper= -c core.quotepath=false -c log.showSignature=false push --progress --porcelain origin refs/heads/master:master
//        remote: Permission to Microsoft/vcpkg.git denied to taekim135.
//fatal: unable to access 'https://github.com/Microsoft/vcpkg.git/': The requested URL returned error: 403