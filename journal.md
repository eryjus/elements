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

The next thing I am going to do is abstract the database driver loading and connection.  I am going to have several database connections to manage and it makes sense to federate this code into a parent class.  This class will be DatabaseModel.

Finally, I took care of changing the classes to be able to be instantiated.  I feel that this is a better implementation to follow.  It gives me the ability to reuse several classes (such as in DatabaseModel).

I'm not ready to commit this change yet, but it have impacted nearly everything as a result.  Once I all my changes properly documented (I'm not sure I am going to keep up with the java documentation standard -- gotta think about that), I will work on that commit.


**15-Mar-2018**

Well, I'm still torn on the whole javadoc thing.  The system is generally for me, but I am publishing it in case someone else wants to follow it and learn about what I am doing.  Overall, this is a standalone application and not a business library.  All of these things make me waffle back and forth on utilizing javadoc.  Overall, the way I am leaning at this point is to drop the javadoc standards for now and add them back in if I reconsider this decision.  I prefer to document the code really well and therefore think it lends to better understanding.  Javadoc in my opinion is far better suited for documenting and publishing API documentation.  Well, I think I have made a decision.  I will go back through all the sources and remove the javadoc elements and document them in my own style.


**16-Mar-2018**

I finished up the documentation changes today and committed my changes.  My next step is to update the SqlDBM site with the proper field names I used (I had CamelCase before and ended up with lower_case in the implementation).  This is not publicly visible since I cannot publish a read-only version of the design at this time.

I found a bug in the IniModel that would not create the cba.ini file if it does not exist.  I am not tracking that file in the repo anymore, so I changed the code to create it on first use.  Seems like I should not crash the system for a file that is not in the project.

OK, so on to the listing of the elements in that table and what to display on the screen.  I have limited real-estate, so I am certain I will not be able to display everything. 

The obvious choices are:
* element_name (well, duh!)
* element_type (pretty much the same as above)
* element_size (nice to know when searching)
* element_short_description (nice to see a description)

Of the rest of the fields, the should not be needed on the summary list.  Since not all types will have all the the rest of the fields filled in, the extra data can be reviewed in a details panel.

So, time to research how to create this form and place it in the main screen.


**17-Mar-2018**

Ok, so the model system I am using to figure out how to fit pieces together (StoreKeeper: https://github.com/kmrifat/StoreKeeper) uses an anchor pane to hold the working content and replaces the children with each new view that is opened.  Seems simple enough.  For me, I have a few different views I will be writing (list view, add/display/edit view, help view, and perhaps more as I get into it further), and I have 2 elements that I will need to manage for each view.  One is the obvious business content and the other is the screen title at the top of the overall application.  I just have to remember to call them 'views' or 'panes' rather than 'screens'.

So, I replaced the StackPane for the main application view with an AnchorPane and called it `idMainAnchor`.  Now, it should be relatively simple to drop a view into this pane as a view and open a new view at will.  I need a view for which I can do this.  Let's start with the list view since I can add Elements to the database using interactive SQL for testing.

As I finish up the day, I am able to open the Add Element pane and change the title appropriately.  Tomorrow, I will work on the controller for `elemview.fxml` and laying out the foundation for handling all the events.


**18-Mar-2018**

Today I will work on the following things that require actions to be developed:
* The Cancel button (needs to ask if OK to cancel and lose work)
* The OK button (need to audit all the fields and display an error if there are issues or write to the database if really OK and then clear the fields)
* Element Name on change (needs to check the database to see if the name is unique)
* Element Type on change (needs to display all the proper additional fields and check boxes according to the value selected)
* Element Size on change (needs to be audited for a proper numeric value)
* Element Decimals on change (needs to be audited for a proper numeric value)

These functions are stubbed out and tracing to the log and now to fill them in.


**20-Mar-2018**

Well, I really did not get to doo much yesterday except take a quick look at the code.  I am still working on filling in the method details....

So there are a couple of things that I came across while I was detailing out the methods:
1. The Unsigned attribute is missing from the form and I need to check that
1. Size and default values are not a common attributes
1. Disabling the control only grays it out and does may not be the best choice
1. Whatever I do with the one above, I will likely need to make the labels follow suit

So, it makes sense to detail out in a table what can be edited (Name, Short Description, Business Description, Not Null, and Type are always open): see [design/elements.md](design/elements.md).

*Notes:*
^1^ - The size here indicates the "Fractional Seconds Part", or "fsp"

I am also pulling the National Flag from the spec at this point.  It will likely come back since I will likely need it, but for now I cannot find it in the MySQL documentation.

Well, this triggers several changes to make....

While I am at it, I am going to eliminate the ENUM type from the list and it will drop back to a VARCHAR type.  Ultimately, I will back with a Code Definition table that will be added at a later date.

So, I end today with a test to insert a record, which failed.  Debugging and improving robustness is on the agenda tomorrow.  Hopefully, I can write a commit tomorrow.


**21-Mar-2018**

The exception from last night was that the numeric value for decimals was not a proper number.  Well, the problem is that not all are really required and therefore a blank value is throwing an exception.  The trivial access methods are not going to work since not every element will have a value.

I am able to now get records inserted.  However, I am not getting `null` values passed to the persistent table when I use setNull() on an integer type.  This leaves me in a quandary about allowing nulls in my application.  In particular, null is allowed in the database but I cannot enforce it, what value is that?

Well, debugging was able to determine that I am actually setting the value based on the contents of the form.  Some more digging to do to get this all worked out.


**22-Mar-2018**

So, to restate my closing concern from yesterday...  The Database will allow a value to be `null` while the FXML view does not.  At least that I have been able to determine.  So, my decision is to try to get the JavaFX front end to handle nulls or to eliminate null support from the database side.  Oh, Google....

So, I was not able to determine a quick method to properly handle nulls in the form (not without overriding several types anyway, but I may get there at some point for other reasons).  The easier method at this point is to make every field have `NOT NULL` and a defined default value (even when the database says it is not supported, an empty string is a value).

So, that now bring up my data model...  what to do about the database values.  For now, I was using String and Integer and Boolean since they are closely defined in the language.  However, there are attributes that I would like to be able to incorporate into the data model that might be of some value such as the size limit of a string and setting it to something too big truncates the string.  Or....  do I just do that truncation when committing to the database.

Ultimately, I guess, best practice would be to have class for each SQL data type that can be used to handle the Business Data -- basically each `element_name` is its own instance of a class while each `element_type` is its own class definition.  If I was to go down this road, this would end up as a separate project as .jar library as I would want to be able to leverage this in other applications.  This is a bit disappointing because I was wanting to get the application done quickly, but I think I have my path forward.

At this point I am going to commit my changes since I have the most basic write functionality working.
