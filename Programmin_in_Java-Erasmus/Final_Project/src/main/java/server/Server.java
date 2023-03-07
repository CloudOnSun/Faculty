package main.java.server;

import main.java.flags.Responses;
import main.java.flags.Tasks;
import main.java.server.domain.User;
import main.java.server.exceptions.ExistingException;
import main.java.server.exceptions.ValidationException;
import main.java.server.repository.fileRepo.FileFriendshipRepo;
import main.java.server.repository.fileRepo.FileUserRepo;
import main.java.server.service.FriendshipService;
import main.java.server.service.UserService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Main class for the server, accepts the connection from a client and process one's reguests
 */
public class Server {

    private static Long ID = null;
    private static FileUserRepo userRepo;
    private static FileFriendshipRepo friendshipRepo;
    private static UserService userService;
    private static FriendshipService friendshipService;

    public static void main(String[] args) {
        userRepo = new FileUserRepo("users.txt");
        friendshipRepo = new FileFriendshipRepo("friendships.txt", userRepo);
        userService = new UserService(userRepo, friendshipRepo);
        friendshipService = new FriendshipService(friendshipRepo, userRepo);


        try (ServerSocket ss = new ServerSocket(6666)) {
            while (true) {
                Socket s = ss.accept();
                Thread t = new ServeConnection(s);
                t.start();
            }
        } catch (IOException ex) {
            System.out.println(ex);
            System.exit(1);
        }
    }

    /**
     * a thread for every connection the server receives
     */
    private static class ServeConnection extends Thread {

        private final Socket s;

        public ServeConnection(Socket s) {
            this.s = s;
        }

        /**
         * it reads the input command and based on this performs different tasks
         */
        @Override
        public void run() {
            try (ObjectInputStream is = new ObjectInputStream(s.getInputStream());
                 ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream())) {
                try {
                    Object o = is.readObject();
                    if (o instanceof Tasks) {
                        switch ((Tasks) o) {
                            case LOGIN -> logIn(is, os);
                            case LOGOUT -> logOut(is, os);
                            case REGISTER -> register(is, os);
                            case MENU -> menu(is, os);
                            case ALLUSERS -> allUsers(is, os);
                            case ADDFRIEND -> addFriend(is, os);
                            case MYFRIENDS -> myFriends(is, os);
                            case DELFRIEND -> deleteFriend(is, os);
                            case DELACC -> deleteAccount(is, os);
                        }
                    } else {
                        System.out.println("Unexpected object received");
                    }
                } catch (ClassNotFoundException ex) {
                    System.out.println("Unknown object received");
                }
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }

        /**
         * logs in the user
         * expects to read a long from @param is
         * @param is reads objects given by client - Long id
         * @param os writes objects to the client
         * @throws IOException
         */
        private synchronized void logIn(ObjectInputStream is, ObjectOutputStream os) throws IOException {
            try {
                Long id = (Long) is.readObject();
                if (ID != null) {
                    os.writeObject(Responses.FAIL);
                    os.writeObject("Already logged in");
                }
                else {
                    var user = userService.findOne(id);
                    ID = id;
                    os.writeObject(Responses.SUCCESS);
                }
            } catch (ClassNotFoundException e) {
                os.writeObject(Responses.FAIL);
                os.writeObject("Unkown object received");
            }
            catch (ExistingException | ValidationException ex) {
                os.writeObject(Responses.FAIL);
                os.writeObject(ex.toString());
            }
        }

        /**
         * logs out the user
         * @param is reads objects given by client
         * @param os writes objects to the client - e.g. FAIL or SUCCESS
         * @throws IOException
         */
        private synchronized void logOut(ObjectInputStream is, ObjectOutputStream os) throws IOException {
                ID = null;
                os.writeObject(Responses.SUCCESS);
        }
        /**
         * register a new user and sends back to the client a unique id
         * expects to receive a name and a email from the client
         * @param is reads objects given by client - String name, String email
         * @param os writes objects to the client - e.g. FAIL or SUCCESS; Long id
         * @throws IOException
         */
        private synchronized void register(ObjectInputStream is, ObjectOutputStream os) throws IOException {
            try {
                String name = (String) is.readObject();
                String email = (String) is.readObject();
                Long id = userService.addUser(name, email);
                ID = id;
                os.writeObject(Responses.SUCCESS);
                os.writeObject(ID);
            } catch (ClassNotFoundException e) {
                os.writeObject(Responses.FAIL);
                os.writeObject("Unkown object received");
            }
            catch (ExistingException | ValidationException ex) {
                os.writeObject(Responses.FAIL);
                os.writeObject(ex.toString());
            }
        }

        /**
         * sends the possible commands to the client as a list of strings
         * @param is reads objects given by client
         * @param os writes objects to the client - e.g. FAIL or SUCCESS; List<\String\> menu
         * @throws IOException
         */
        private synchronized void menu(ObjectInputStream is, ObjectOutputStream os) throws IOException {
            List<String> men = new ArrayList<>();
            men.add("Your menu is:");
            men.add("-in <Long>id :(Log in with an id)");
            men.add("-out :(Log out)");
            men.add("-reg <String>name <String>email :(Register with a name and a email)");
            men.add("-men :(Ask for your menu)");
            men.add("-all :(Ask for all users)");
            men.add("-addF <Long>id :(Add a friend with their id)");
            men.add("-myF :(Ask for all your friends)");
            men.add("-delF <Long>id :(Delete a friend)");
            men.add("-del :(Delete your account)");
            men.add("exit :(Exit the app)");
            os.writeObject(Responses.SUCCESS);
            os.writeObject(men);
        }

        /**
         * sends all existing users to the client as a list of User
         * @param is reads objects given by client
         * @param os writes objects to the client - e.g. FAIL or SUCCESS; List<\User\> allUsers
         * @throws IOException
         */
        private synchronized void allUsers(ObjectInputStream is, ObjectOutputStream os) throws IOException {
            if (ID == null) {
                os.writeObject(Responses.FAIL);
                os.writeObject("NOT LOGGED IN!");
                return;
            }
            List<User> all = userRepo.findAll();
            os.writeObject(Responses.SUCCESS);
            os.writeObject(all);
        }

        /**
         * adds a friend to the logged-in user
         * expects to read a Long id of the wanted friend
         * @param is reads objects given by client - Long friendID
         * @param os writes objects to the client - e.g. FAIL or SUCCESS
         * @throws IOException
         */
        private synchronized void addFriend(ObjectInputStream is, ObjectOutputStream os) throws IOException {
            if (ID == null) {
                os.writeObject(Responses.FAIL);
                os.writeObject("NOT LOGGED IN!");
                return;
            }
            try {
                Long idF = (Long) is.readObject();
                var friendsFrom = LocalDateTime.now().format(new DateTimeFormatterBuilder()
                        .appendPattern("yyyy-MM-dd[ HH:mm:ss]")
                        .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                        .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                        .toFormatter());
                friendshipService.addFriends(ID, idF, friendsFrom);
                os.writeObject(Responses.SUCCESS);
            } catch (ClassNotFoundException e) {
                os.writeObject(Responses.FAIL);
                os.writeObject("Unkown object received");
            }
            catch (ExistingException | ValidationException ex) {
                os.writeObject(Responses.FAIL);
                os.writeObject(ex.toString());
            }
        }

        /**
         * sends the client all friends of the logged-in user
         * @param is reads objects given by client
         * @param os writes objects to the client - e.g. FAIL or SUCCESS; List<\User> myFriends
         * @throws IOException
         */
        private synchronized void myFriends(ObjectInputStream is, ObjectOutputStream os) throws IOException {
            if (ID == null) {
                os.writeObject(Responses.FAIL);
                os.writeObject("NOT LOGGED IN!");
                return;
            }
            var allFriendships = friendshipService.getAllUsersFriends();
            boolean found = false;
            for (var user_Fr : allFriendships.entrySet()){
                if(user_Fr.getKey().getId().equals(ID)){
                    found = true;
                    os.writeObject(Responses.SUCCESS);
                    os.writeObject(user_Fr.getValue());
                }
            }
            if (!found) {
                os.writeObject(Responses.FAIL);
                os.writeObject("You don't have any friends");
            }
        }

        /**
         * deletes a friend of the logged-in user
         * expects to read a Long friendID of the wanted friend
         * @param is reads objects given by client - Long friendID
         * @param os writes objects to the client - e.g. FAIL or SUCCESS
         * @throws IOException
         */
        private synchronized void deleteFriend(ObjectInputStream is, ObjectOutputStream os) throws IOException {
            if (ID == null) {
                os.writeObject(Responses.FAIL);
                os.writeObject("NOT LOGGED IN!");
                return;
            }
            try {
                Long idF = (Long) is.readObject();
                var allFriendships = friendshipService.getAll();
                boolean found = false;
                for (var fr : allFriendships) {
                    if ((fr.getIdUser1().equals(ID) && fr.getIdUser2().equals(idF)) ||
                            (fr.getIdUser2().equals(ID) && fr.getIdUser1().equals(idF))) {
                        found = true;
                        friendshipService.deleteEntity(fr.getId());
                        os.writeObject(Responses.SUCCESS);
                    }
                }
                if (!found) {
                    os.writeObject(Responses.FAIL);
                    os.writeObject("One is not your friend!");
                }
            } catch (ClassNotFoundException e) {
                os.writeObject(Responses.FAIL);
                os.writeObject("Unkown object received");
            }
            catch (ExistingException | ValidationException ex) {
                os.writeObject(Responses.FAIL);
                os.writeObject(ex.toString());
            }
        }

        /**
         * deletes the account of the logged-in user
         * @param is reads objects given by client
         * @param os writes objects to the client - e.g. FAIL or SUCCESS
         * @throws IOException
         */
        private synchronized void deleteAccount(ObjectInputStream is, ObjectOutputStream os) throws IOException {
            try {
                userService.deleteEntity(ID);
                ID = null;
                os.writeObject(Responses.SUCCESS);
            }
            catch (ExistingException | ValidationException ex) {
                os.writeObject(Responses.FAIL);
                os.writeObject(ex.toString());
            }
        }
    }
}
