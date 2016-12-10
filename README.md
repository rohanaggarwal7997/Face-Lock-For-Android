[VIDEO LINK –] (https://drive.google.com/open?id=0B17PvnJ9pHlfVnNVejByd2lucU0)






Introduction -

	Our Idea was to develop a security application using Face API, one of Microsoft's Cognitive Services

	Our App, is a phone locker which at the time of locking asks for a master image, and then makes the phone
	unusable until another picture is clicked with the same face as the master image.

	The video included demonstrates the usage of the app

*- Please Make sure you are connected to the internet before starting the app and please don't proceed in any part
of the app until the confirmation toast appears. -*

How We Did It -

	Since there is no provision to actually create a custom lock in android OS, we simulate a lock by creating a persistent
	activity which doesn't let the user go out of the activity. The screen can safely be turned off after the lock starts.
	We implement the persistent activity by using a service which recalls the activity whenever it goes out of focus. 
	The service terminates when the image matches, which removes the persistent nature of the activity, and therefore, disabling the lock.

Bugs - 

	* If the phone is switched off, then the application terminates and the phone is again usable on restart
	* There is a slight delay (varying from phone to phone) in between pressing the home button and the lock to restart
	* In some phones, on pressing the browse button, you can navigate between already running apps, so it is advisable to
  	  run the lock with no other applications running
	* The master image gets stored in the root directory which can be changed by connecting the phone to a PC
	* We have tried many combinations for trying to close the application, but there might still be some undiscovered combination
  	  which can get around the lock

Plans for further development -

	* Trying to detect whether the person the physically present or not (since it can be unlocked through a well clicked photograph of the person)
  	  Ideas - Analysing heat signatures and jerk motions
	* Click and store images of people who try to unlock but fail 
	* Resolve the home button and browse button issues and remove any other bypasses of the lock discovered
	* Use services to check faces in front of camera after certain intervals, to make the unlock automatic
	* Add speech recognition and other methods of authentication
	* Optimise for battery usage
	* Develop GUI further (Disable buttons until it can safely be clicked rather than just telling the user to wait for confirmation)

