

using Final_Project.Domain;
using Final_Project.repository.fileRepo;
using Final_Project.repository.memoryRepo;
using Final_Project.service;
using Final_Project.ui;
using Final_Project.Validators;

namespace Final_Project
{
    public class MainClass
    {
        public static void Main(string[] args)
        {
            FileUserRepo fileUserRepo = new FileUserRepo("users.txt");
            FileFriendshipRepo fileFriendshipRepo = new FileFriendshipRepo("friendships.txt", fileUserRepo);

            UserService userService = new UserService(fileUserRepo, fileFriendshipRepo);
            FriendshipService friendshipService = new FriendshipService(fileFriendshipRepo, fileUserRepo);

            ConsoleUI console = new ConsoleUI(userService, friendshipService);

            console.showUI();
        }
    }
}