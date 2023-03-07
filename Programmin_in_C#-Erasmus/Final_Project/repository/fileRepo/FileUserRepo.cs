using Final_Project.Domain;
using Final_Project.Exceptions;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Final_Project.repository.fileRepo
{
    public class FileUserRepo : AbstractFileRepository<long, User>
    {

        private long maxID;

        public FileUserRepo(string fileName) : base(fileName) 
        {
            if(findAll().Count() == 0)
                maxID = 1;
            else
                maxID = findAll().Max(u => { return u.getID(); });
        }

        /***
         * Maps the user to the saved string
         * @param user - the user to convert
         * @return the string converted
         */
        protected override string entityToString(User entity)
        {
            return $"{entity.getID()};{entity.getName()};{entity.getEmail()}";
        }

        /***
         * Maps the saved string to the user
         * @param attributes - a list of string representing the arguments of the user class
         * @return the converted user
         */
        protected override User extractEntity(List<string> attributes)
        {
            try
            {
                long id = long.Parse(attributes[0]);
                return new User(
                    id,
                    attributes[1],
                    attributes[2]
                    );
            }
            catch(Exception ex) when (ex is ArgumentNullException || 
                                      ex is FormatException ||
                                      ex is OverflowException)
            {
                return null;
            } 
        }
        /***
         * Saves the user in the repository and then writes it in the file
         *
         * @param entity - the user to save and write in file
         * @return the saved user
         * @throws ArgumentOutOfRangeException if the user to save is null
         * @throws ExistingException if the user already exists
         */
        public override User save(User entity)
        {
            if (entity.getID() == 0)
            {
                maxID++;
                entity.setID(maxID);
                var u = base.save(entity);
                return u;
            }
            else
            {
                maxID = Math.Max(maxID, entity.getID());
                var user = base.save(entity);
                if (user == null)
                {
                    throw new ExistingExceptions("User already exists");
                }
                return user;
            }
        }

        /***
        *  removes the user with the specified id and rewrites the entire file
        * @param id
        * @return the removed user
        * @throws ExistingException if the user doesn't exist
        */
        public override User delete(long id)
        {
            var user = base.delete(id);
            if (user == null)
            {
                throw new ExistingExceptions("User doesn't exist");
            }
            return user;
        }

        /***
         * updates the existing user with the new one and rewrites the entire file
         * @param entity
         *          entity must not be null
         * @return the updated user - if the user is updated,
         * @throws ArgumentsOutOfBoundsException
         *             if the given user is null.
         * @throws ExistingException if the user doesn't exist
         */
        public override User update(User entity)
        {
            var user = base.update(entity);
            if (user == null)
            {
                throw new ExistingExceptions("User doesn't exist");
            }
            return user;
        }

        /***
         *
         * @param id -the id of the user to be returned
         * @return the entity with the specified id
         * @throws ExistingException if the user doesn't exist
         */
        public override User findOne(long id)
        {
            var user = base.findOne(id);
            if (user == null)
            {
                throw new ExistingExceptions("User doesn't exist");
            }
            return user;
        }
    }
}
