using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;

namespace Final_Project.Validators
{
    public abstract class BaseValidator<T> : Validator<T>
    {
        public abstract void validate(T value);


        /***
         * Check if the given text if not null and not empty
         * @param text the given text string
         * @return true/false if the text is valid or not
         */
        protected bool validateText(string text)
        {
            return text != null && text.Length > 0 && Regex.Match(text, "[a-zA-Z]+").Success;
        }

        protected bool validateEmail(string email)
        {
            return Regex.Match(email, "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$").Success;
        }
    }
        
}
