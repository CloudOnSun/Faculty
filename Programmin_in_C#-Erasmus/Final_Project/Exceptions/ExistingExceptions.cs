using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Final_Project.Exceptions
{
    public class ExistingExceptions : Exception
    {
        /***
        * Exception thrown when some data already exists in the repo (or doesn't exist at all)
        * @param message the error message
         */
        public ExistingExceptions(string message) : base(message) { }
        
    }
}
