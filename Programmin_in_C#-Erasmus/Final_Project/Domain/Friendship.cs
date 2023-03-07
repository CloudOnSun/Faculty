using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Final_Project.Domain
{
    public class Friendship : Entity<long>
    {
        private long idUser1;
        private long idUser2;
        DateTime friendsFrom;

        public Friendship(long id, long idUser1, long idUser2, DateTime friendsFrom)
        {
            setID(id);
            this.idUser1 = idUser1;
            this.idUser2 = idUser2;
            this.friendsFrom = friendsFrom;
        }

        public Friendship(long idUser1, long idUser2)
        {
            this.idUser1 = idUser1;
            this.idUser2 = idUser2;
        }


        /***
        * getter for the first user in the friendship
        *
        * @return the id of the first user
        */
        public long getIdUser1()
        {
            return idUser1;
        }

        public void setIdUser1(long idUser1)
        {
            this.idUser1 = idUser1;
        }

        /***
        * getter for the second user in the friendship
        *
        * @return the id of the second user
        */
        public long getIdUser2()
        {
            return idUser2;
        }

        public void setIdUser2(long idUser2)
        {
            this.idUser2 = idUser2;
        }

        public DateTime getFriendsFrom()
        {
            return friendsFrom;
        }

        public override string ToString()
        {
            return "Friendship{" +
                "id=" + getID() +
                "idUser1=" + idUser1 +
                ", idUser2=" + idUser2 +
                ", friendsFrom=" + friendsFrom +
                '}';
        }

        public override bool Equals(object? obj)
        {
            if (this == obj) return true;
            if (obj == null) return false;
            if (obj is Friendship f)
            {
                if (f.getIdUser1() == idUser1 && f.getIdUser2() == idUser2) return true;
                if (f.getID() == this.getID()) return true;
            }
            return false;
        }
    }
}
