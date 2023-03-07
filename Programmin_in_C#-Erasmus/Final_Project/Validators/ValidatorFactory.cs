using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Final_Project.Validators
{
    public class ValidatorFactory
    {
        private static ValidatorFactory instance = null;

        private ValidatorFactory() { }

        public Validator<T> createValidator<T>(ValidatorType type)
        {
            switch (type)
            {
                case ValidatorType.FRIENDSHIP:
                    {
                        return (Validator<T>) new FriendshipValidator();
                    }
                case ValidatorType.USER:
                    {
                        return (Validator<T>) new UserValidator();       
                    }
                default: return null;
            }
        }

        public static ValidatorFactory getInstance()
        {
            if (instance == null)
            {
                instance = new ValidatorFactory();
            }
            return instance;
        }
    }
}
