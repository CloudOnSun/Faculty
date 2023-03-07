using Final_Project.Domain;
using Final_Project.Exceptions;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Final_Project.Validators
{
    public class FriendshipValidator : BaseValidator<Friendship>
    {
        public override void validate(Friendship frship)
        {
            if (frship.getIdUser1().Equals(frship.getIdUser2()))
            {
                throw new ValidationException("Invalid friendship");
            }
        }
    }
}
