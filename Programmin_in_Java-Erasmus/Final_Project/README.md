# Social Network

Name: Flaviu Catalin Florea

The application will be a backend app for a “social network”, having a console UI. The goal is to create a server-client, object-oriented application using architecture stratification and Model-View-Controller.  
	
The server will be constructed like this:

It will have a domain layer, where all the entities are held. It will manage the next entities:

Entity (ID) – every entity in the app will have an ID.

User (Name, Email) – inherits from Entity.

Friendship (IDUser1, IDUser2, FriendsFromDate) – inherits from Entity.

It will have a repository layer, where different repositories for different objects will save the data into files and offer the data to the upper layer (the service layer).

It will have a service layer (controller layer), where all the commands from the UI will be passed and which will provide the UI with the useful information. It is the middle layer where all the data is being processed. It is where the entities are being validated. 

It will have a validation layer, which will provide with the useful tools to validate the entities.

And it will have a UI layer, where it will respond to the client’s requests. It passes the requests to the service layer where they are being processed.

The client can do one of the following tasks:

•	To log in with the email in the app, if he already was registered

•	To register with name and email

•	To ask for his menu (the possible actions)

•	To ask for all the existing users

•	To create a friendship with another user

•	To ask for all his friendships

•	To delete his account

•	To delete a friendship
