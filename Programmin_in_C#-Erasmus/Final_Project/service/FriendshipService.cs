using Final_Project.Domain;
using Final_Project.graph;
using Final_Project.repository.memoryRepo;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Final_Project.service
{
    public class FriendshipService : BaseService<long, Friendship>
    {
        private Repository<long, Friendship> friendshipRepo;
        private Repository<long, User> userRepo;

        public FriendshipService(Repository<long, Friendship> friendshipRepo, Repository<long, User> userRepo)
            : base(Validators.ValidatorType.FRIENDSHIP, friendshipRepo)
        {
            this.friendshipRepo = friendshipRepo;
            this.userRepo = userRepo;
        }

        /***
         * Returns a map with all users and their friends - used for printing the friendships
         *
         * @return a map where with keys as users and values as their friends
         */
        public Dictionary<User, List<User>> getAllUsersFriends()
        {
            Dictionary<User, List<User>> map = new Dictionary<User, List<User>>();
            List<Friendship> all = getAll();
            all.ForEach(fr =>
            {
                User user1 = userRepo.findOne(fr.getIdUser1());
                User user2 = userRepo.findOne(fr.getIdUser2());
                if (!map.ContainsKey(user1))
                {
                    map.Add(user1, new List<User>());
                }
                if (!map.ContainsKey(user2))
                {
                    map.Add(user2, new List<User>());
                }
                map[user1].Add(user2);
                map[user2].Add(user1);
            });

            return map;
        }

        /***
         * Creates a new Friendship between two users and adds it to the repo
         *
         * @param id - the id of the friendship
         * @param id1 the id of the first user
         * @param id2 the id of the second user
         * @param frFrom - date and time since they are friends
         * @throws ValidationException if the relation is invalid
         * @throws ExistingException if the friendship already exists
         */
        public void addFriends(long id, long idu1, long idu2, DateTime frFrom)
        {
            Friendship fr = EntitySingletone.getInstance().createFriendship(id, idu1, idu2, frFrom);
            addEntity(fr);
        }

        /***
         * Creates a new Friendship between two users and adds it to the repo
         *
         * @param id - the id of the friendship
         * @param id1 the new id of the first user
         * @param id2 the new id of the second user
         * @param frFrom - new date and time since they are friends
         * @throws ValidationException if the relation is invalid
         * @throws ExistingException if the friendship doesn't exists
         */
        public Friendship updateFriends(long id, long idu1, long idu2, DateTime frFrom)
        {
            Friendship fr = EntitySingletone.getInstance().createFriendship(id, idu1, idu2, frFrom);
            return updateEntity(fr);
        }

        /***
         * Computes all the communities of users
         * @return a list of lists of users' ids
         */
        public List<List<long>> ConnectedComponents()
        {
            List<Tuple<long, long>> nodesList = new List<Tuple<long, long>>();
            foreach (var fr in repository.findAll())
            {
                nodesList.Add(new Tuple<long, long>(fr.getIdUser1(), fr.getIdUser2()));
            }
            Graph<long> graph = new Graph<long>(nodesList);
            Components<long> graphUtils = new Components<long>(graph);

            foreach (var user in userRepo.findAll())
            {
                graph.addNode(user.getID());
            }

            return graphUtils.ConnectedComponents();

        }

    }
}
