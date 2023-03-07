
package main.java.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Scanner;

import main.java.flags.Responses;
import main.java.flags.Tasks;
import main.java.server.domain.Friendship;
import main.java.server.domain.User;


/**
 * Main class for client, sends requests to the server
 */
public class Client {

    private static final String HOSTNAME = null;

    /***
     * when run, it waits for commands until "exit" command it introduced
     */
    public static void main(String[] args) {

        Scanner reader = new Scanner(System.in);

        while (true) {

            String line = reader.nextLine();
            var commands = line.split(" ");

            boolean closing = false;

            switch (commands[0]) {
                case "-in" -> {
                    if (commands.length != 2) {
                        System.out.println("Number of commands invalid");
                    }
                    else
                        try {
                            logIn(Long.parseLong(commands[1]));
                        }
                        catch (NumberFormatException ex) {
                            System.out.println("ID must be an integer");
                        }
                }
                case "-out" -> {
                    if (commands.length != 1) {
                        System.out.println("Number of commands invalid");
                    }
                    else
                        logOut();
                }
                case "-reg" -> {
                    if (commands.length != 3) {
                        System.out.println("Number of commands invalid");
                    }
                    else
                        register(commands[1], commands[2]);
                }
                case "-men" -> {
                    if (commands.length != 1) {
                        System.out.println("Number of commands invalid");
                    }
                    else
                        menu();
                }
                case "-all" -> {
                    if (commands.length != 1) {
                        System.out.println("Number of commands invalid");
                    }
                    else
                        getAllUsers();
                }
                case "-addF" -> {
                    if (commands.length != 2) {
                        System.out.println("Number of commands invalid");
                    }
                    else
                        try {
                            addFriend(Long.parseLong(commands[1]));
                        }
                        catch (NumberFormatException ex) {
                            System.out.println("ID must be an integer");
                        }
                }
                case "-myF" -> {
                    if (commands.length != 1) {
                        System.out.println("Number of commands invalid");
                    }
                    else
                        myFriends();
                }
                case "-delF" -> {
                    if (commands.length != 2) {
                        System.out.println("Number of commands invalid");
                    }
                    else
                        try {
                            deleteFriend(Long.parseLong(commands[1]));
                        }
                        catch (NumberFormatException ex) {
                            System.out.println("ID must be an integer");
                        }
                }
                case "-del" -> {
                    if (commands.length != 1) {
                        System.out.println("Number of commands invalid");
                    }
                    else
                        deleteAccount();
                }
                case "exit" -> {
                    closing = true;
                }

            }
            if (closing) break;
        }
    }

    /**
     * sends the id to the server and waits to see if one can log in or not
     * @param id - the id of the user
     */
    public static void logIn(Long id) {
        try (Socket s = new Socket(InetAddress.getByName(HOSTNAME), 6666);
             ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
             ObjectInputStream is = new ObjectInputStream(s.getInputStream())) {

            os.writeObject(Tasks.LOGIN);
            os.writeObject(id);

            Responses res = (Responses) is.readObject();
            if (res == Responses.FAIL) {
                String error = (String) is.readObject();
                System.out.println(error);
            }
            else {
                System.out.println("Succesfully logged in!");
            }

        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        } catch (ClassNotFoundException e) {
            System.out.println("Unknown object received");
            System.exit(1);
        }
    }

    /**
     * sends request to the server and expects to be logged out
     */
    public static void logOut() {
        try (Socket s = new Socket(InetAddress.getByName(HOSTNAME), 6666);
             ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
             ObjectInputStream is = new ObjectInputStream(s.getInputStream())) {

            os.writeObject(Tasks.LOGOUT);

            Responses res = (Responses) is.readObject();
            if (res == Responses.FAIL) {
                String error = (String) is.readObject();
                System.out.println(error);
            }
            else {
                System.out.println("Succesfully logged out!");
            }

        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        } catch (ClassNotFoundException e) {
            System.out.println("Unknown object received");
            System.exit(1);
        }

    }

    /**
     * it registers a new user via name and email and expects a unique id from the server
     * it also expects the user to be logged in afterwards
     * @param name the name of the user
     * @param email the email of the user
     */
    public static void register(String name, String email) {

        try (Socket s = new Socket(InetAddress.getByName(HOSTNAME), 6666);
             ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
             ObjectInputStream is = new ObjectInputStream(s.getInputStream())) {

            os.writeObject(Tasks.REGISTER);
            os.writeObject(name);
            os.writeObject(email);

            Responses res = (Responses) is.readObject();
            if (res == Responses.FAIL) {
                String error = (String) is.readObject();
                System.out.println(error);
            }
            else {
                Long id = (Long) is.readObject();
                System.out.println("Succesfully registered!");
                System.out.println("Your id is: " + id);
            }

        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        } catch (ClassNotFoundException e) {
            System.out.println("Unknown object received");
            System.exit(1);
        }
    }

    /***
     * asks server for possible commands and it prints them
     */
    public static void menu() {
        try (Socket s = new Socket(InetAddress.getByName(HOSTNAME), 6666);
             ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
             ObjectInputStream is = new ObjectInputStream(s.getInputStream())) {

            os.writeObject(Tasks.MENU);

            Responses res = (Responses) is.readObject();
            if (res == Responses.FAIL) {
                String error = (String) is.readObject();
                System.out.println(error);
            }
            else {
                List<String> menu = (List<String>) is.readObject();
                menu.forEach(System.out::println);
            }

        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        } catch (ClassNotFoundException e) {
            System.out.println("Unknown object received");
            System.exit(1);
        }
    }

    /**
     * asks server for all existing users and prints them
     */
    public static void getAllUsers() {
        try (Socket s = new Socket(InetAddress.getByName(HOSTNAME), 6666);
             ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
             ObjectInputStream is = new ObjectInputStream(s.getInputStream())) {

            os.writeObject(Tasks.ALLUSERS);

            Responses res = (Responses) is.readObject();
            if (res == Responses.FAIL) {
                String error = (String) is.readObject();
                System.out.println(error);
            }
            else {
                List<User> menu = (List<User>) is.readObject();
                menu.forEach(u -> System.out.println(u.toString()));
            }

        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        } catch (ClassNotFoundException e) {
            System.out.println("Unknown object received");
            System.exit(1);
        }
    }

    /**
     * adds a friend to the logged-in user
     * @param id the id of the wanted friend
     */
    public static void addFriend(Long id){
        try (Socket s = new Socket(InetAddress.getByName(HOSTNAME), 6666);
             ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
             ObjectInputStream is = new ObjectInputStream(s.getInputStream())) {

            os.writeObject(Tasks.ADDFRIEND);
            os.writeObject(id);

            Responses res = (Responses) is.readObject();
            if (res == Responses.FAIL) {
                String error = (String) is.readObject();
                System.out.println(error);
            }
            else {
                System.out.println("Friend added!");
            }

        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        } catch (ClassNotFoundException e) {
            System.out.println("Unknown object received");
            System.exit(1);
        }
    }

    /**
     * asks for all existing friends of the logged-in user
     */
    public static void myFriends(){
        try (Socket s = new Socket(InetAddress.getByName(HOSTNAME), 6666);
             ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
             ObjectInputStream is = new ObjectInputStream(s.getInputStream())) {

            os.writeObject(Tasks.MYFRIENDS);

            Responses res = (Responses) is.readObject();
            if (res == Responses.FAIL) {
                String error = (String) is.readObject();
                System.out.println(error);
            }
            else {
                List<User> menu = (List<User>) is.readObject();
                menu.forEach(u -> System.out.println(u.toString()));
            }

        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        } catch (ClassNotFoundException e) {
            System.out.println("Unknown object received");
            System.exit(1);
        }
    }

    /**
     * deletes a friend from logged-in user's list of friends
     * @param id the id of the friend we want to delete
     */
    public static void deleteFriend(Long id){
        try (Socket s = new Socket(InetAddress.getByName(HOSTNAME), 6666);
             ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
             ObjectInputStream is = new ObjectInputStream(s.getInputStream())) {

            os.writeObject(Tasks.DELFRIEND);
            os.writeObject(id);

            Responses res = (Responses) is.readObject();
            if (res == Responses.FAIL) {
                String error = (String) is.readObject();
                System.out.println(error);
            }
            else {
                System.out.println("Friend deleted");
            }

        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        } catch (ClassNotFoundException e) {
            System.out.println("Unknown object received");
            System.exit(1);
        }
    }

    /**
     * delete the entire account of the logged-in user
     */
    public static void deleteAccount(){
        try (Socket s = new Socket(InetAddress.getByName(HOSTNAME), 6666);
             ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
             ObjectInputStream is = new ObjectInputStream(s.getInputStream())) {

            os.writeObject(Tasks.DELACC);

            Responses res = (Responses) is.readObject();
            if (res == Responses.FAIL) {
                String error = (String) is.readObject();
                System.out.println(error);
            }
            else {
                System.out.println("Account deleted");
            }

        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        } catch (ClassNotFoundException e) {
            System.out.println("Unknown object received");
            System.exit(1);
        }
    }
}
