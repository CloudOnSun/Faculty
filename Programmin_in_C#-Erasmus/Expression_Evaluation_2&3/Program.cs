using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Reflection;

namespace Lab9
{

    class Program
    { 
        static void Main(string[] args)
        {
            string line = Console.ReadLine();
            Expression expr = null;
            EvaluateInteger a = new EvaluateInteger();
            EvaluateDouble b = new EvaluateDouble();
            AllParantheses c = new AllParantheses();
            MinimalParantheses d = new MinimalParantheses();
            while (line != null && !line.Equals("end"))
            {
                if(line.Length == 0)
                {
                    line = Console.ReadLine();
                    continue;
                }
                if (line.Equals("i") || line.Equals("d") || line.Equals("p") || line.Equals("P"))
                {
                    if(expr == null)
                    {
                        Console.WriteLine("Expression Missing");
                    }
                    else if(line.Equals("i"))
                    { 
                        try
                        {
                            var res = expr.Accept(a);
                            Console.WriteLine(res);
                        }
                        catch (DivideByZeroException)
                        {
                            Console.WriteLine("Divide Error");
                        }
                        catch (OverflowException)
                        {
                            Console.WriteLine("Overflow Error");
                        }
                    }
                    else if (line.Equals("d"))
                    {
                        double res = expr.Accept(b);
                        Console.WriteLine(res.ToString("0.00000"));
                    }
                    else if (line.Equals("p"))
                    {
                        var res = expr.Accept(c);
                        Console.WriteLine(res);
                    }
                    else if (line.Equals("P"))
                    {
                        var res = expr.Accept(d);
                        Console.WriteLine(res);
                    }
                }
                else
                {
                    expr = Expression.ParsePrefixExpression(line);
                    if (expr == null)
                    {
                        Console.WriteLine("Format Error");
                    }
                }
                line = Console.ReadLine();
            }
        }
    }
}