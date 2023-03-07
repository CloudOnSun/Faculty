using Final_Project.Domain;
using Final_Project.repository.memoryRepo;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Final_Project.service
{
    public class UserService : BaseService<long, User>
    {
        private Repository<long, Friendship> friendshipRepo;

        public UserService(Repository<long, User> repo, Repository<long, Friendship> frRepo) : base(Validators.ValidatorType.USER, repo)
        {
            friendshipRepo= frRepo;
        }

        /***
         * creates a new instance of the user and adds it to the repo
         * @param email the email of the user
         * @param name the name of the user
         * @throws ValidationException if the user is invalid
         * @throws ExistingException if the entity exists
         */
        public void addUser(string name, string email)
        {
            User user = EntitySingletone.getInstance().createUser(name, email);
            base.addEntity(user);
        }

        /***
         * creates a new instance of the user and adds it to the repo
         * @param ID the id of the user
         * @param email the email of the user
         * @param name the name of the user
         * @throws ValidationException if the user is invalid
         * @throws ExistingException if the entity exists
         */
        public void addUser(long id, string name, string email)
        {
            User user = EntitySingletone.getInstance().createUser(id, name, email);
            base.addEntity(user);
        }

        /***
         * Removes an user and the removes all the friendships related to him
         * @param userId the id of the removed user
         * @return the removed user
         * @throws ExistingException if the entity doesn't exists
         */
        public User deleteUser(long id)
        {
            User user = base.deleteEntity(id);
            friendshipRepo.findAll().ForEach(fr =>
            {
                if(fr.getIdUser1().Equals(id) || fr.getIdUser2().Equals(id))
                {
                    friendshipRepo.delete(fr.getID());
                }
            });
            return user;
        }

        /***
         * updates a user
         * @param ID the id of the user
         * @param email new the email of the user
         * @param name new the name of the user
         * @throws ValidationException if the user is invalid
         * @throws ExistingException if the entity doesn't exists
         */
        public User updateUser(long id, string name, string email)
        {
            User user = EntitySingletone.getInstance().createUser(id, name, email);
            return base.updateEntity(user);
        }
    }
}
