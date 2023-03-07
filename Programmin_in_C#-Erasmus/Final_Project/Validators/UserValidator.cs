using Final_Project.Domain;
using Final_Project.Exceptions;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http.Headers;
using System.Text;
using System.Threading.Tasks;

namespace Final_Project.Validators
{
    public class UserValidator : BaseValidator<User>
    {
        public override void validate(User user)
        {
            string text = "";
            text += validateEmail(user.getEmail()) ? "" : "Email is invalid";
            text += validateText(user.getName()) ? "" : "Name is empty";
            if (text.Length > 0)
            {
                throw new ValidationException(text);
            }
        }
    }
    
}
