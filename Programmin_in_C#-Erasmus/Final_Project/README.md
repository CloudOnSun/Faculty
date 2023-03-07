# Final Project C#
Name: Flaviu Catalin Florea

	The application will be a backend app for a “social network”, having a console UI. The goal is to create an object-oriented application using architecture stratification and Model-View-Controller.  
	It will have a domain layer, where all the entities are held. It will manage the next entities:

Entity (ID) – every entity in the app will have an ID.
User (Name, Email) – inherits from Entity.
Friendship (IDUser1, IDUser2, FriendsFromDate) – inherits from Entity.

	It will have a repository layer, where different repositories for different objects will save the data into files and offer the data to the upper layer (the service layer).
	It will have a service layer (controller layer), where all the commands from the UI will be passed and which will provide the UI with the useful information. It is the middle layer where all the data is being processed. It is where the entities are being validated. 
	It will have a validation layer, which will provide with the useful tools to validate the entities.
	And finally, it will have a console UI layer, where the menu is being shown to the user and where the commands are being read. The commands will be in the form of:
1 add…
2 delete…
3 do that…
and the user will just type the number of the command, followed by additional information if necessary.
	The UI passes the commands to the service. The service processes the commands using the domain, the repository, and the validators.
	The app can do the following tasks:
•	To add a user by its name and email
•	To add a friendship between 2 users, using the users’ ids
•	To show all the existing users
•	To show all the existing friendships
•	To delete a user
•	To delete a friendship
•	To show the number of communities (friends of friends)
