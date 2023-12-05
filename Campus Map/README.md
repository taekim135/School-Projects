<h1> Western Geographic Information System </h1>
<h3> Created by: Oliver Clennan, Taegyun Kim, Sung Kim, and Jiho Choi</h3>

<h2> A Brief Overview </h2>
At its core, this application aims to provide an efficient, convienent way for Western students to navigate around popular buildings on campus, such as Alumni Hall, Middlesex College, and the Natural Sciences Centre. Compared to wufloorplans.uwo.ca, our application supports many additional features and functionalities to offer a more personal and customizable experience.

<h2> Core Features </h2>

<h3> Explore Maps </h3>
<ul>
<li> Freely browse, zoom, and scroll through any of the supported floor plan maps.</li>
<li> Seamlessly switch between different maps at any moment in time.</li>
</ul>

<h3> Create Points of Interest </h3>
<ul>
<li> Click on any valid position on the currently displayed map to add a new point of interest to the sytem.</li>
<li> Custom points of interest are user-specific to ensure a more personal user experience. </li>
</ul>

<h3> Discover Points of Interest  </h3>
<ul>
<li> Once viewing a map, click on the "Discover" button on the right hand side panel to view all points of interest presently associated with the map.</li>
<li> Click on an individual point of interest to view its metadata, and location on the map.</li>
</ul>

<h3> Favourite Points of Interest </h3>
<ul>
<li> View a list of favourited points of interest at any point in time to gain quick access to them.</li>
<li> Favourite, or un-favourite, any point of interest in the system. </li>
</ul>

<h3> Persistent Storage of Data </h3>
<ul>
<li> All data - system, and user - is stored persistently across multiple sessions with the application.</li>
<li> Application data is stored locally, and any changes are properly reflected upon termination of a session.  </li>
</ul>

<h3> View the Current Weather </h3>
<ul>
<li> Click on the "Weather" tab on the menu bar to view the current weather conditions on campus.</li>
<li> This information is accurate, up-to-date, and obtained from a reputable API service.  </li>
</ul>

<h3> Support for Multiple Users </h3>
<ul>
<li> The application supports any number of distinct users, who may access their account with their unique credentials.</li>
<li> Each user's data is stored privately in a separate file which is safe from corruption by other users in the system. </li>
</ul>

<h3> User Help Guide </h3>
<ul>
<li> Click on the "Help" tab on the menu bar to view an external PDF document containing detailed instructions on how to access and interact with all of the above features. </li>
</ul>

<h2> External Libraries & Tools </h2>
<ul>
<li> <b>org.json.simple</b> was used to access and modify application data stored in JSON files.</li>
<li> <b>JUnit 5</b> was used to conduct test cases on the various components of our application.</li>
<li> <b>Swing</b> was used to design, and implement the user interface.</li>
<li> <b>Maven</b> was used to automate project builds.</li>
</ul>

<h2> Compilation Instructions </h2>
<ul>
<li> First, clone the project repository to a local directory.</li>
<li> Then, ensure that you have the aforementioned libraries installed in your development environment. </li>
<li> Proceed to navigate to the src/main/java/app/GUI directory, and click on the GUI.java class.</li>
<li> Finally, run <b>GUI.main()</b> to launch the application. </li>
</ul>