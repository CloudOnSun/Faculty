using Final_Project.Domain;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Final_Project.service
{
    public class EntitySingletone
    {
        private static EntitySingletone instance = null;

        private EntitySingletone() { }

        public static EntitySingletone getInstance()
        {
            if(instance == null)
            {
                instance = new EntitySingletone();
            }
            return instance;
        }

        /***
         * creates an user
         * @param email the new user's email
         * @param name the new user's name
         * @return the new user
         */
        public User createUser(string name, string email) 
        {
            return new User(name, email);
        }

        /***
         * creates an user
         * @param ID the new user's id
         * @param email the new user's email
         * @param name the new user's name
         * @return the new user
         */
        public User createUser(long id, string name, string email)
        {
            return new User(id, name, email);
        }

        /***
         * creates a new instance of Friendship
         * @param id the id of the Friendship created
         * @param idu1 the id of the first user
         * @param idu2 the id of the second user
         * @param frFrom the date since they were friends
         * @return the created Friendship
         */
        public Friendship createFriendship(long id, long idu1, long idu2, DateTime fsFrom)
        {
            return new Friendship(id, idu1, idu2, fsFrom);
        }
    }
}
