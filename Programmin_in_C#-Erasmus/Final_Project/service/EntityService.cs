using Final_Project.Domain;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Final_Project.service
{
    public  interface EntityService<ID, E> where E : Entity<ID>
    {
        /***
         * Adds the entity
         * @param e the entity to add
         * @throws ValidationException if the entity is invalid
         * @throws ExistingException if the entity already exists
         */
        void addEntity(E entity);

        /***
         * Remove the entity with the given id
         * @param id the given id of the entity
         * @return the id
         * @throws ExistingException if the entity doesn't exist
         */
        E deleteEntity(ID id);

        /***
         * Gets all the entites
         * @return a list with the entities
         */
        List<E> getAll();

        /***
         * Updates the given entity
         * @throws ExistingException if the entity doesn't exist
         * @throws ValidationException if the entity is invalid
         */
        E updateEntity(E entity);

        /***
         * finds the entity with the given id
         * @throws ExistingException if the entity doesn't exist
         */
        E get(ID id);
    }
}
