using Final_Project.Domain;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Final_Project.repository.memoryRepo
{
    public interface Repository <ID, E> where E : Entity<ID>
    {
        /***
         *
         * @param id -the id of the entity to be returned
         *           id must not be null
         * @return the entity with the specified id
         *          or null - if there is no entity with the given id
         */
        E findOne(ID id);

        /***
         *
         * @return all entities
         */
        List<E> findAll();


        /***
         *
         * @param entity
         *         entity must be not null
         * @return the saved entity - if the given entity is saved
         *         otherwise returns null (id already exists)
         * @throws ArgumentsOutOfBoundsException
         *             if the given entity is null.     *
         */
        E save(E entity);

        /***
         *  removes the entity with the specified id
         * @param id
         *      id must be not null
         * @return the removed entity or null if there is no entity with the given id
         */
        E delete(ID id);

        /***
         * updates the existing entity with the new one
         * @param entity
         *          entity must not be null
         * @return the updated entity - if the entity is updated,
         *                otherwise  returns null  - (e.g entity does not exist).
         * @throws ArgumentsOutOfBoundsException
         *             if the given entity is null.
         */
        E update(E entity);
    }
}
