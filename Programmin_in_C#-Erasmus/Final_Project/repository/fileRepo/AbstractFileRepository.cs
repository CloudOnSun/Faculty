using Final_Project.Domain;
using Final_Project.repository.memoryRepo;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;

namespace Final_Project.repository.fileRepo
{
    /***
    * An abstract class that has the needed method for getting entities from
    * a file and writing them back
    *
    * @param <ID> the type of the id of the entity
    * @param <E> the type of the entity
    */
    public abstract class AbstractFileRepository<ID, E> : InMemoryRepo<ID, E> where E : Entity<ID>
    {
        private readonly string fileName;

        public AbstractFileRepository(string fileName)
        {
            this.fileName = fileName;
            loadData();
        }

        /***
         * abstract method to get the entity from a string
         *
         * @param attributes - a list of string representing the arguments of the entity class
         * @return the entity
         */
        protected abstract E extractEntity(List<string> attributes);


        /***
         * Gets the data from the file
         * Iterates over each line and used the abstract method extractEntity
         * to map the String line to the needed entity
         */
        private void loadData()
        {
            using var reader = new StreamReader(fileName);
            string line = reader.ReadLine() ;
            while (line != null) 
            {
                List<string> attributes = line.Split(";").ToList();
                E entity = extractEntity(attributes);
                if (entity != null) 
                {
                    base.save(entity);
                }
                line = reader.ReadLine();
            }
        }

        /***
         * abstract method to get the string from an entity
         *
         * @param e - the entity to map to a string
         * @return the string
         */
        protected abstract string entityToString(E entity);

        /***
         * Gets an entity and uses the entityToString method
         * to map the entity to a String we'll put in the file
         *
         * @param e the entity to write in the file
         */
        private void writeEntityToFile(E entity)
        {
            using var writer = new StreamWriter(fileName, true);
            writer.WriteLine(entityToString(entity));
        }

        /***
         * Clear the file we have in the repo
         */
        private void clearFile()
        {
            using var writer = new StreamWriter(fileName);
            writer.Write("");
        }

        /***
         * Saves the entity in the repository and then writes it in the file
         *
         * @param entity - the entity to save and write in file
         * @return the saved entity
         *          null if the entity already exists
         * @throws ArgumentOutOfRangeException if the entity to save is null
         */
        public override E save(E entity)
        {
            var e = base.save(entity);
            if (e == null)
            {
                return null;
            }
            writeEntityToFile(e);
            return e;
        }

        /***
        *  removes the entity with the specified id and rewrites the entire file
        * @param id
        * @return the removed entity or null if there is no entity with the given id
        */
        public override E delete(ID id)
        {
            E e = base.delete(id);
            if (e == null)
            {
                return null;
            }
            clearFile();
            findAll().ForEach(a => { writeEntityToFile(a); });
            return e;
        }


        /***
         * updates the existing entity with the new one and rewrites the entire file
         * @param entity
         *          entity must not be null
         * @return the updated entity - if the entity is updated,
         *                otherwise  returns null  - (e.g enitity does not exist).
         * @throws ArgumentsOutOfBoundsException
         *             if the given entity is null.
         */
        public override E update(E entity)
        {
            E e = base.update(entity);
            if (e == null)
            {
                return null;
            }
            clearFile();
            findAll().ForEach(a => { writeEntityToFile(a); });
            return e;
        }
    }

}
