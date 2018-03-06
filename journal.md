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

