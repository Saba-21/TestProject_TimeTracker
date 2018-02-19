# TestProject_TimeTracker
Test Project - Time Tracker

App consists of a single activity and a single layout that contains RecyclerView to display list.
List items have three views for task, description and timer data.
All the data is stored in SQLite database.
Database has one table with three columns for storing task, description and timer data.
Database helper class has implemented getData and addData methods for getting and storing data.
App has two dialog windows with its layouts.
First dialog has two fields for task name and description input and button for starting timer.
Second dialog has Textview to display timer and button to finish timer.
Main layout has two buttons, one for showing first dialog and 
another to start chooser activity for sending mail with intent that has all the data.
The onCreate method is setting up layout, database, Recyclerview, Dialogs and registering BroadcastReceivers.
After setup data is retrieved from SharedPreferences.
The solution for timer is Handler, which schedules timer code to run on every second.
Timer code consists of checking timer state, distributing time from incremented seconds to standard format and setting it to Textview.
App contains one service to run timer in background. service runs in main thread for not being killed or recreated by OS.
Activity has one registered Broadcast that listens one action and sends two Broadcasts.
service has one registered Broadcast thas listens to two actions and send one broadcast.
Activity saves timer state and input data in onStop method and retrieves it in onCreate.
Activity starts service when user starts a timer. Service starts timer when its created.
Activity sends broadcast with action for service to be stopped when user stops timer.
After stopping timer all the data is added to database and then to list, dialog window fields are cleaned and dismissed.
If timer state was runing state befor activity destruction, 
after activity recreating it send broadcast with action to get timer data from service,
when service receives this broadcast, it send broadcast with intent that has timer data,
after geting timer data from service activity starts runing timer and incrementing seconds 
that was passed after creating service when user started timer. after then dialog window is popped up automaticaly and showing runong timer.


