using Final_Project.service;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Final_Project.ui
{
    public class ConsoleUI : UI
    {
        private UserService userService;
        private FriendshipService friendshipService;

        public ConsoleUI(UserService userService, FriendshipService friendshipService)
        {
            this.userService = userService;
            this.friendshipService = friendshipService;
        }

        public void showUI()
        {
            while (true)
            {
                showMenu();
                string cmdLine = Console.ReadLine();
                int cmd = -1;
                try
                {
                    cmd = int.Parse(cmdLine);
                }
                catch(Exception ex) { continue; }

                bool closing = false;

                switch (cmd)
                {
                    case 0:
                        {
                            closing = true;
                            break;
                        }

                    case 1:
                        {
                            showUsers();
                            showFriends();
                            break;
                        }
                    case 2:
                        {
                            Console.WriteLine("Enter:");
                            Console.WriteLine("id name email");
                            Console.WriteLine("== or ==");
                            Console.WriteLine("name email");
                            string line = Console.ReadLine();
                            var cmds = line.Split(" ", StringSplitOptions.RemoveEmptyEntries);
                            if (cmds.Length != 2 && cmds.Length != 3)
                            {
                                Console.WriteLine("Invalid number of commands");
                            }
                            if (cmds.Length == 2)
                            {
                                addUser(cmds[0], cmds[1]);
                            }
                            if(cmds.Length == 3)
                            {
                                addUser(cmds[0], cmds[1], cmds[2]);
                            }
                            break;
                        }
                    case 3:
                        {
                            Console.WriteLine("Enter:");
                            Console.WriteLine("IdFriendship IDUser1 IDUser2");
                            string line = Console.ReadLine();
                            var cmds = line.Split(" ", StringSplitOptions.RemoveEmptyEntries);
                            if(cmds.Length == 3)
                            {
                                addFriendship(cmds[0], cmds[1], cmds[2]);
                            }
                            else
                            {
                                Console.WriteLine("Invalid number of commands");
                            }
                            break;
                        }
                    case 4:
                        {
                            Console.WriteLine("Enter:");
                            Console.WriteLine("IDUser");
                            string line = Console.ReadLine();
                            removeUser(line);
                            break;
                        }
                    case 5:
                        {
                            Console.WriteLine("Enter:");
                            Console.WriteLine("IDFriendship");
                            string line = Console.ReadLine();
                            removeFriendship(line);
                            break;
                        }
                    case 6:
                        {
                            Console.WriteLine("Enter:");
                            Console.WriteLine("id name email");
                            string line = Console.ReadLine();
                            var cmds = line.Split(" ", StringSplitOptions.RemoveEmptyEntries);
                            if (cmds.Length != 3)
                            {
                                Console.WriteLine("Invalid number of commands");
                            }
                            if (cmds.Length == 3)
                            {
                                updateUser(cmds[0], cmds[1], cmds[2]);
                            }
                            break;
                        }
                    case 7:
                        {
                            Console.WriteLine("Enter:");
                            Console.WriteLine("IdFriendship IDUser1 IDUser2");
                            string line = Console.ReadLine();
                            var cmds = line.Split(" ", StringSplitOptions.RemoveEmptyEntries);
                            if (cmds.Length == 3)
                            {
                                updateFriendship(cmds[0], cmds[1], cmds[2]);
                            }
                            else
                            {
                                Console.WriteLine("Invalid number of commands");
                            }
                            break;
                        }
                    case 8:
                        {
                            showNumberOfCommunities();
                            break;
                        }
                    default:
                        {
                            Console.WriteLine("Invalid command");
                            break;
                        }
                }
                if (closing) break;
            }
        }

        private void showMenu()
        {
            Console.WriteLine("\nYour menu is:");
            Console.WriteLine("1 - show all users and relationships");
            Console.WriteLine("2 - add a User by name and email");
            Console.WriteLine("3 - add a friendship between 2 users");
            Console.WriteLine("4 - delete a user");
            Console.WriteLine("5 - delete a friendship");
            Console.WriteLine("6 - update a user");
            Console.WriteLine("7 - update a friendship");
            Console.WriteLine("8 - show number communities");
            Console.WriteLine("0 - exit\n");
        }

        private void showUsers()
        {
            Console.WriteLine("====    USERS    ====");
            userService.getAll().ForEach(u => Console.WriteLine(u));
            Console.WriteLine("=====================");
        }

        private void showFriends()
        {
            Console.WriteLine("====    FRIENDSHIPS    ====");
            var all = friendshipService.getAllUsersFriends();
            foreach (var user in all)
            {
                Console.Write(user.Key.getName() + " --> ");
                user.Value.ForEach(u => Console.Write(u.getName() + " "));
                Console.WriteLine("");
            }
            Console.WriteLine("===========================");
        }

        private void addUser(string id, string name, string email)
        {
            try
            {
                long iD = long.Parse(id);
                userService.addUser(iD, name, email);
            }
            catch(Exception ex) { Console.WriteLine(ex.Message); }
        }

        private void addUser(string name, string email)
        {
            try
            {
                userService.addUser(name, email);
            }
            catch(Exception ex) { Console.WriteLine(ex.Message); }
        }

        private void removeUser(string id)
        {
            try
            {
                long iD = long.Parse(id);
                var u = userService.deleteUser(iD);
                Console.WriteLine(u);
            }
            catch(Exception ex) { Console.WriteLine(ex.Message); }
        }

        private void addFriendship(string id, string idu1, string idu2)
        {
            try
            {
                long iD = long.Parse(id);
                long idU1 = long.Parse(idu1);
                long idU2 = long.Parse (idu2);
                DateTime frFrom = DateTime.UtcNow;
                friendshipService.addFriends(iD, idU1, idU2, frFrom);
            }
            catch(Exception ex) { Console.WriteLine(ex.Message); }
        }

        private void removeFriendship(string id)
        {
            try
            {
                var f = friendshipService.deleteEntity(long.Parse(id));
                Console.WriteLine(f);
            }
            catch(Exception ex) { Console.WriteLine(ex.Message); }

        }

        private void showNumberOfCommunities()
        {
            Console.WriteLine("Number of communities: " +
                friendshipService.ConnectedComponents().Count);
        }

        private void updateUser(string id, string name, string email)
        {
            try
            {
                long iD = long.Parse(id);
                var u = userService.updateUser(iD, name, email);
                Console.WriteLine(u);
            }
            catch (Exception ex) { Console.WriteLine(ex.Message); }
        }

        private void updateFriendship(string id, string idu1, string idu2)
        {
            try
            {
                long iD = long.Parse(id);
                long idU1 = long.Parse(idu1);
                long idU2 = long.Parse(idu2);
                DateTime frFrom = DateTime.UtcNow;
                var f = friendshipService.updateFriends(iD, idU1, idU2, frFrom);
            }
            catch (Exception ex) { Console.WriteLine(ex.Message); }
        }
    }
}
