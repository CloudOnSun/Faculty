using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace NezarkaBookstore
{
    internal class Program
    {
        public static void Main(string[] args)
        {
            //StreamReader reader = new StreamReader("in.txt");
            //StreamWriter writer = new StreamWriter("out.txt");
            Controller ctrl = new Controller(Console.In, Console.Out);
            ctrl.executeCommands();
            //reader.Close();
            //writer.Close();
        }
    }
}
