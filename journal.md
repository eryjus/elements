cba Business Applications -- Element Maintenance
---

I like to keep a journal of my process with large development projects.  It allows me to go back and revisit my decisions and reflect on how I got to whatever place I am at.  I see no reason to change that for this project.

cba has been a goal of mind for some time.  cba stands for Century Business Applications and is intended to be a database driven model for developing applications to mee real-world business needs.  "Century" comes from the CenturyOS that is another project of mine; and, yes, this application is intended to run on that OS.  Maybe sometime within a century of when I started (thus the name).


**05-Mar-2018**

I have been back and forth for a while on this, but I think this is going to be the first start that might gain traction.  First, I have struggled to identify a good development platform.  I have settled on MySQL (or MariaDB if you prefer) long ago since it is a good stable open-source database platform that can support the size implementation I am looking at.  However, the proper development language has been somewhat allusive until now.  I have considered PHP, Python, C, C++, C#, and several other languages, including Java.  Not one of them had an application framework that I felt I could work with and develop applications fast enough and provide robust enough connectivity to the database.  I prefer strongly typed languages.  And I wanted something that could run in many places.  I also believe that web design will be the future of development (I even plan on my desktop to be completely driven by HTML5 -- in a century or so).

A few days ago I stumbled on JavaFX, an application framework for Java that appeared to fit most of my needs.  I am not a strong Java developer (though I have developed several console applications), so I feel I'm up for several challenges.  With Scene Builder, I am able to rapidly prototype my screens and in a WYSIWYG development tool.  It also separates the presentation from the logic (and even further facilitates a MVC implementation).  The style of the presentation layer is further separated into .css files.  So, not only do I get a strongly typed language, but separation of concerns in the development.  And a GUI development tool for the GUI interface.

So, I have started to play around with the application framework.  I have found some sample applications to help me fit the puzzle pieces together.  And things are starting to get interesting.  At least for me.

Tomorrow, I will create a public repository and author a README.md and the LICENSE.md files.  I will commit this file and the other 2 to the repo.  And, I will start to lay out the .css for the login screen.  I have the screen itself written (with the exception of all the changes required due to my ignorance and stupidity).  Parts were started before I found JavaFX, and I will be rewriting them into the new framework.


**06-Mar-2018**

Well, I have been able to get the public repo set up and the initial commit completed.  I have branched my local repo so I am working on v0.1.0.  I will tickle the patch number once I have something I feel is ready to start inputting data for real.

So, as I noted yesterday, this is going to be a learning process for me since I am not strong in Java and I have never used JavaFX before with the exception of some basic tutorials.  So I have to go figure out how to integrate the .css with the .fxml.  I have a feeling that I will have to produce some simple Java to display my login screen to see the results.  Ultimately, this is what the experience is all about.  I figure this is a good a place to start as any: https://docs.oracle.com/javafx/2/css_tutorial/jfxpub-css_tutorial.htm.


**07-Mar-2018**

I was able to get the FXML form to load.  Granted, there was a lot of copy and paste involved (hey -- the example login form form Oracle is pretty close to what I need anyway!), but it compiles and runs.  And, it is very plain, which is what I really want because I want to be able to decorate it using the .css file I am going to include.

I'm pretty happy about where I am at now, so I am going to commit this code and push it to GitHub.  The point of this journal (and the project for that matter) is to allow others to learn from me and my mistakes. 

So..., now that the commit is done and pushed to the central repo, on to making the .css file work.  But before I even got there, I went into SceneBuilder and changed the label at the top of the form to brand it for 'cba Elements' and it changed the whole file layout.  So I had to compile and execute it again to make sure it will still run.  I have a feeling that as long as I write a file by hand and get into SceneBuilder to modify it, the layout of the file will change dramatically.  I have learned, and you have been warned.


**08-Mar-2018**

Ok, thinking about it overnight, Oracle has a pretty good example of using a .css to decorate a JavaFX form.  What I do not understand is how each element fits together.  So I am going to create an empty file to see if I can get it loaded and then add the decorations one at a time to see what they do.

So, a little Google time and I was able to get the login form to be sized properly, the background image set to my own desired one, locked the login form on the screen and added an exit button and backed it with a controller.  I'm very happy with this and will commit my changes again.  I am not able to actually authenticate yet, but that is coming up next.

I will start be making the effort to read the login information provided and present it to the console.... which was surprisingly easy to do.  I am going to commit this change as well.  

The next step for me is to make sure that the source so far is properly documented and then add logging (log4j) and the configuration file (cba.ini).

I have noticed a problem with my vscode configuration where my LoginController class is required to explicitly `extends Object`.  I will look back at this at a later time.

As I wrap up for the night, I have an IniController written but not tested.  I will take this on tomorrow.


**09-Mar-2018**

So, I'm camping this weekend and I am testing this from the trailer now that quiet hours have started.  Now to testing the IniController...  and it worked.  But I'm tired and I will document the class tomorrow.


**10-Mar-2018**

Today's first task is to document the IniController class.  This was fast enough to accomplish.  So what else is next on the list?  How about the following:

* Create a Security database and connect to it
* Validate the user ID and password to the Security Database
* Create the Elements database and connect to it

So, since I have limited internet access, I'm figuring the best thing will be to create the Security database.  There will be several tables in the database, but the first one will be a users table, which will be:
* User name (varchar(25))
* Real Name (varchar(120))
* Password (MD5) (char(32))
* Email address (varchar(120))
* Enabled tinyint(1)

With that, what is the best method to ensure I have a good database to work against?  There are several options I can utilize here:

1. Use a script to create the Security schema and populate the default data.  However, how to guarantee that the script has been run?
1. Try to access the database and if there is an error create the schema.  However, how to differentiate between a bad user/password in cba.ini versus a missing database?
1. Run a series of SQL statements on open that will perform the "if not exists" flavors of SQL statements.  However, how to make sure that performance is good.
1. Query the information_schema schema to determine if what we are looking for exists.  But can we guarantee that the user has access to information_schema?

I think some research is in order....

So, the first thing I find is that the permissions for information_schema correspond to the permissions a user has for the schema.table in question.

I am strongly considering using information_schema.schemata to determine if the database exists.  Actually, I will do the same for the tables I need.  If the database does not exist, I will create it and immediately populate it with the default data.  This will mean that the Security controller will need to have an initialize method that opens the connection, check for the existence, create the database and populate default data as necessary.  I think I will use the default schema for the user and explicitly query schemas with each SQL statement.

Continuing with the thinking above, I will need to create the cba_Security database, the cba_Security.users table, and populate the `admin` user with the default password.


**11-Mar-2018**

OK, now that I am back home and have a good internet connection, my first order of business is to get the syntax/semantic highlighting working again with vscode.  Compiling just to find a problem is getting tiresome.

I was able to solve the issues with a few steps:
1. I removed the ~/.settings/Code folder since it appeared corrupt
1. I then removed the .classpath file since it was also corrupt (which was regenerated)
1. I then set the JAVA_HOME global environment variable to the JDK 1.8.

This cleaned up several things allowed me to start removing many of the other work-around.

Overall I have been able to dynamically create and populate the cba_system database and the users table with the admin user.  However, what I have done requires some default setup to have been completed ahead of time:
* `default_time_zone` must be set in cnf.ini.  This might require the population of the time zone names.  This might require running `mysql_tzinfo_to_sql /usr/share/zoneinfo | mysql -u root -p mysql`.
* Each user that will be used to connect to each schema (as defined in the cba.ini file) needs to be granted privileges to the schema it will connect to.  The default configuration will be to use user `cba`, so this can easily be done with `grant all privileges on *.* to 'cba'@'localhost' identified by 'C00lApp$';` which also identifies the default password.

Finally, I am at a position I can actually authenticate a user.


**12-Mar-2018**

Well today I was able to write the authentication code.   I am currently passing the password in clear text to the MySQL server, so that is something I need to take care of one day.  I also documented the SecurityController class and I am ready to commit my changes.

Ok, so what's next?  Going back to my list from 10-Mar....

The last thing from that list was to connect to and create if necessary the Elements schema.  Well, I do not think it is proper for the LoginController to be responsible for that function.  It is more appropriate to open the main screen and let the main application take care of the Elements schema -- after all, that's what the application is all about.

So, as I wrap up for the night, I have a shell of an application.  It has: a stacked frame pane, an Elements sub-app icon, a help button, and an exit button.  Tomorrow, I will want to add a controller for the application so that I can at least login and use the application to exit.


**13-Mar-2018**

Well I have been able to get the main form to load, replacing the login form.  I'm actually pretty happy about this milestone.  Now I can work on the main application and get some real functionality started.

I do have a few things that are not quite right, so I am going to start a todo.md file to keep track of these before I get into any bug tracking on github.com.  I really do not want to clutter up the bug list with my own known todo list.

So, the point of the application is to maintain business data elements.  So, it's time to start setting that form in place.  What does that need to look like?

Well, I think the following features are in order:
* Open with a list of existing elements
    * Paginate?
    * Query By Example
    * Jump to...
    * When to search/refresh the list?
* Buttons to add new and change/delete existing elements
* Method to close the form (exit, but maybe not...)
* Of course, I need to check the database and table and create both as needed

I have been using https://sqldbm.com as a tools to lay out my data model.  


**14-Mar-2018**

Well, let's start with Happy Pi Day!!  

I started today be formatting some SQL statements and trying to make the code a bit more readable.  The goal is to have an MVC implementation.  MVC stands for Model, View, Controller.  I have the controller part and the view part.  But it dawned on me that I am mis-characterizing the SecurityController and would be better characterized as a SecurityModel.  It is, in short, a database model.  So, I will correct that now since it is clearly not right.  As a matter of fact, the same fact holds true for the IniController...  it should be IniModel.  I will commit these changes since it is a significant flaw in my initial thinking.

