

using System.Net.Http.Headers;

namespace Final_Project.Domain
{
    public class User : Entity<long>
    {
        private string name;
        private string email;
        private List<string> friends;

        public User(long iD, string name, string email)
        {
            setID(iD);
            this.name = name;
            this.email = email;
            this.friends = new List<string>();
        }

        public User(string name, string email)
        {
            this.email = email;
            this.name = name;
        }

        public string getName()
        {
            return name;
        }

        public string getEmail()
        {
            return email;
        }

        public List<string> getFriends()
        {
            return friends;
        }

        public void setName(string name)
        {
            this.name = name;
        }

        public void setEmail(string email)
        {
            this.email = email;
        }

        public override string ToString()
        {
            return "User: id=" + getID() +
                ", name='" + name + '\'' +
                ", email='" + email + '\'';
        }


        /***
        * the definition of when 2 users are considered equals
        * @param o the user to compare to
        * @return true if they are equal, false otherwise
         */
        public override bool Equals(object? o)
        {
            if (this == o) return true;
            if (o == null) return false;
            if (o is User u)
            {
                return u.getID() == this.getID() && u.email.Equals(this.email);
            }
            else
            {
                return false;
            }
        }

        public override int GetHashCode()
        {
            return string.GetHashCode(this.getID() + email);
        }


    }
}
