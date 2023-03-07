using Final_Project.Domain;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http.Headers;
using System.Text;
using System.Threading.Tasks;

namespace Final_Project.repository.memoryRepo
{
    public class InMemoryRepo<ID, E> : Repository<ID, E> where E : Entity<ID>
    {
        protected Dictionary<ID, E> entities;

        public InMemoryRepo()
        {
            entities = new Dictionary<ID, E>();
        }
        public virtual E delete(ID id)
        {
            if (!entities.ContainsKey(id)) 
            {
                return null;
            }
            else
            {
                var e = entities[id];
                entities.Remove(id);
                return e;
            }
        }

        public List<E> findAll()
        {
            return entities.Values.ToList();
        }

        public virtual E findOne(ID id)
        {
            if (!entities.ContainsKey(id))
            {
                return null;
            }
            else
            {
                return entities[id];
            }
        }

        public virtual E save(E entity)
        {
            if (entity == null) throw new ArgumentOutOfRangeException("Entity must not be null");
            if (entities.ContainsKey(entity.getID()))
            {
                return null;
            }
            else
            {
                entities.Add(entity.getID(), entity);
                return entity;
            }
        }

        public virtual E update(E entity)
        {
            if (entity == null) 
            {
                throw new ArgumentOutOfRangeException("Entity must not be null");
            }
            if (!entities.ContainsKey(entity.getID()))
            {
                return null;
            }
            else
            {
                entities[entity.getID()] = entity;
                return entity;
            }
        }
    }

}
