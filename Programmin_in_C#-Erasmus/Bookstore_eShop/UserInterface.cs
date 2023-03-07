using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Reflection.Metadata.Ecma335;
using System.Text;
using System.Threading.Tasks;

namespace NezarkaBookstore
{
    public class CommandReader
    {
        TextReader reader;

        
        public CommandReader(TextReader reader)
        {
            this.reader = reader;           
        }

        private Tuple<bool, int> verifyId(String idString)
        {
            try
            {
                int id = Int32.Parse(idString);
                if (id < 0)
                    return new Tuple<bool, int>(false, -1);
                return new Tuple<bool, int>(true, id); ;
            }
            catch (Exception ex) when (
                    ex is FormatException
                    || ex is ArgumentNullException
                    || ex is OverflowException
                )
            {
                return new Tuple<bool, int>(false, -1);
            }
        }

        private bool verifyCommand(String cmd)
        {
            if (cmd.Equals("GET"))
            {
                return true;
            }
            else
                return false;
        }

        private Tuple<bool, int, int> verifyURL(String uml)
        {
            String[] parts = uml.Split("/", StringSplitOptions.RemoveEmptyEntries);
            if (parts.Length < 3)
            {
                return new Tuple<bool, int, int>(false, -1, -1);
            }
            if (!parts[0].Equals("http:"))
            {
                return new Tuple<bool, int, int>(false, -1, -1);
            }
            if (!parts[1].Equals("www.nezarka.net"))
            {
                return new Tuple<bool, int, int>(false, -1, -1);
            }
            if (parts[2].Equals("Books"))
            {
                if (parts.Length > 3)
                {
                    if (parts.Length != 5)
                        return new Tuple<bool, int, int>(false, -1, -1);
                    if (!parts[3].Equals("Detail"))
                        return new Tuple<bool, int, int>(false, -1, -1);
                    Tuple<bool, int> t = verifyId(parts[4]);
                    return new Tuple<bool, int, int>(t.Item1, t.Item2, 2);
                }
                else
                    return new Tuple<bool, int, int>(true, -1, 1);
            }
            else if (parts[2].Equals("ShoppingCart"))
            {
                if (parts.Length > 3)
                {
                    if (parts.Length < 5)
                        return new Tuple<bool, int, int>(false, -1, -1);
                    if (!parts[3].Equals("Add") && !parts[3].Equals("Remove"))
                        return new Tuple<bool, int, int>(false, -1, -1);
                    Tuple<bool, int> t = verifyId(parts[4]);
                    if (parts[3].Equals("Add"))
                        return new Tuple<bool, int, int>(t.Item1, t.Item2, 4);
                    else 
                        return new Tuple<bool, int, int>(t.Item1, t.Item2, 5);
                }
                else
                    return new Tuple<bool, int, int>(true, -1, 3);
            }
            else
                return new Tuple<bool, int, int>(false, -1, -1);
        }

        public Tuple<bool, int, int, int> readCommand()
        {
            String line = reader.ReadLine();
            if (line == null)
                return new Tuple<bool, int, int, int>(false, -1, -1, -1);
            String[] param = line.Split(" ", StringSplitOptions.RemoveEmptyEntries);
            if (param.Length < 3)
            {
                return new Tuple<bool, int, int, int>(true, -1, -1, -1);
            }
            else
            {
                String request = param[0];
                String custId = param[1];
                String url = param[2];
                var t1 = verifyCommand(request);
                var t2 = verifyId(custId);
                var t3 = verifyURL(url);
                if (t1 && t2.Item1 && t3.Item1)
                {
                    return new Tuple<bool, int, int, int>(true, t2.Item2, t3.Item2, t3.Item3);
                }
                else
                {
                    return new Tuple<bool, int, int, int>(true, -1, -1, -1);
                }
            }
            
        }

    }

    public class Printer
    {
        TextWriter writer;
        public Printer(TextWriter writer)
        {
            this.writer = writer;
        }
        public void printHTML(List<StringBuilder> htmlPage)
        {
            foreach (var line in htmlPage)
            {
                writer.WriteLine(line);
            }
            writer.WriteLine("====");
        }
    }
}
