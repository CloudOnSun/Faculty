using Final_Project.Domain;
using Final_Project.Exceptions;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml;

namespace Final_Project.repository.fileRepo
{
    public class FileFriendshipRepo : AbstractFileRepository<long, Friendship>
    {

        private readonly FileUserRepo fileUserRepo;

        public FileFriendshipRepo(string fileName, FileUserRepo fur) : base(fileName)
        {
            this.fileUserRepo = fur;
        }

        /***
         * Maps the friendship to the saved string
         * @param friendship - the friendship to convert
         * @return the string converted
         */
        protected override string entityToString(Friendship entity)
        {
            return $"{entity.getID()};{entity.getIdUser1()};{entity.getIdUser2()};{entity.getFriendsFrom()}";
        }

        /***
         * Maps the saved string to the friendship
         * @param attributes - a list of string representing the arguments of the friendship class
         * @return the converted friendship
         */
        protected override Friendship extractEntity(List<string> attributes)
        {
            try
            {
                long id = long.Parse(attributes[0]);
                long idu1 = long.Parse(attributes[1]);
                long idu2 = long.Parse(attributes[2]);
                DateTime dateTime = DateTime.Parse(attributes[3]);
                return new Friendship(id, idu1, idu2, dateTime);
            }
            catch (Exception ex) when (ex is ArgumentNullException ||
                                      ex is FormatException ||
                                      ex is OverflowException)
            {
                return null;
            }
        }
        /***
         * Saves the friendship in the repository and then writes it in the file
         *
         * @param entity - the friendship to save and write in file
         * @return the saved friendship
         * @throws ArgumentOutOfRangeException if the friendship to save is null
         * @throws ExistingException if the friendship already exists
         *                              or one of the users or both do not exist
         */
        public override Friendship save(Friendship fr)
        {
            var u1 = fileUserRepo.findOne(fr.getIdUser1());
            var u2 = fileUserRepo.findOne(fr.getIdUser2());
            findAll().ForEach(a =>
            {
                if (a.getIdUser1() == fr.getIdUser1() && a.getIdUser2() == fr.getIdUser2())
                    throw new ExistingExceptions("Friendship already exists");
            });
            var f = base.save(fr);
            if (f == null)
            {
                throw new ExistingExceptions("Friendship already exists");
            }
            return f;
        }

        /***
        *  removes the friendship with the specified id and rewrites the entire file
        * @param id
        * @return the removed friendship 
        * @throws ExistingException if the friendship doesn't exist
        */
        public override Friendship delete(long id)
        {
            var fr = base.delete(id);
            if (fr == null)
            {
                throw new ExistingExceptions("Friendship doesn't exist");
            }
            return fr;
        }

        /***
         * updates the existing friendship with the new one and rewrites the entire file
         * @param entity
         *          entity must not be null
         * @return the updated friendship - if the friendship is updated,
         * @throws ArgumentsOutOfBoundsException
         *             if the given entity is null.
         * @throws ExistingException if the friendship doesn't
         *                              or one of the users or both do not exist
         */
        public override Friendship update(Friendship entity)
        {
            var u1 = fileUserRepo.findOne(entity.getIdUser1());
            var u2 = fileUserRepo.findOne(entity.getIdUser2());
            findAll().ForEach(a =>
            {
                if (a.getIdUser1 == entity.getIdUser1 && a.getIdUser2 == entity.getIdUser2)
                    throw new ExistingExceptions("Friendship already exists");
            });
            var fr = base.update(entity);
            if (fr == null)
            {
                throw new ExistingExceptions("Friendship doesn't exist");
            }
            return fr;
        }

        /***
         *
         * @param id -the id of the friendship to be returned
         * @return the friendship with the specified id
         * @throws ExistingException if the friendship doesn't exist
         */
        public override Friendship findOne(long id)
        {
            var fr = base.findOne(id);
            if (fr == null)
            {
                throw new ExistingExceptions("Friendship doesn't exist");
            }
            return fr;
        }
    }
}
