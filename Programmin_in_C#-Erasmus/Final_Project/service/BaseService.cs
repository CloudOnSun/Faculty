using Final_Project.Domain;
using Final_Project.repository.memoryRepo;
using Final_Project.Validators;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Final_Project.service
{
    public class BaseService<ID, E> : EntityService<ID, E> where E : Entity<ID>
    {
        private Validator<E> validator;
        protected Repository<ID, E> repository;


        public BaseService(ValidatorType valType, Repository<ID,E> repo) 
        {
            validator = ValidatorFactory.getInstance().createValidator<E>(valType);
            this.repository = repo;
        }

        public void addEntity(E entity)
        {
            validator.validate(entity);
            repository.save(entity);
        }

        public E deleteEntity(ID id)
        {
            return repository.delete(id);
        }

        public E get(ID id)
        {
            return repository.findOne(id);
        }

        public List<E> getAll()
        {
            return repository.findAll();
        }

        public E updateEntity(E entity)
        {
            validator.validate(entity);
            return repository.update(entity);
        }
    }
}
