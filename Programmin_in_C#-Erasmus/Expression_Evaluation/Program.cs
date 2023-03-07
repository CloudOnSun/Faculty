using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;

namespace Lab8
{
    public class FormatErrorException : Exception
    {
        public FormatErrorException(string message) : base(message) { }
    }

    public class OverFlow2Exception : Exception
    {
        public OverFlow2Exception(string message) : base(message) { }
    }

    public class DivideBy0Exception : Exception
    {
        public DivideBy0Exception(string message) : base(message) { }
    }

    public class Calculator
    {
        public const string ADD = "+";
        public const string SUB = "-";
        public const string MUL = "*";
        public const string DIV = "/";
        public const string NEG = "~";
        public static readonly string[] BINARY_OPERATORS = { ADD, SUB, MUL, DIV};
        public static readonly string[] UNARY_OPERATORS = { NEG };


        private Stack<int> _stackOp = new Stack<int>();

        public int unaryOpCalc(int op, string oper)
        {
            switch (oper)
            {
                case NEG: return -op;
                
                default: throw new FormatErrorException("Format Error");
            }
        }

        public int binaryOpCalc(int op1, int op2, string oper)
        {
            switch(oper) {
                case ADD: 
                    try {
                        checked {
                            int res = op1 + op2;
                            return res;
                        }
                    }
                    catch (OverflowException) {
                        throw new OverFlow2Exception("Overflow Error");
                    }

                case SUB:
                    try {
                        checked {
                            int res = op1 - op2;
                            return res;
                        }
                    }
                    catch (OverflowException) {
                        throw new OverFlow2Exception("Overflow Error");
                    }

                case MUL:
                    try {
                        checked {
                            int res = op1 * op2;
                            return res;
                        }
                    }
                    catch (OverflowException) {
                        throw new OverFlow2Exception("Overflow Error");
                    }

                case DIV: 
                    if (op2 == 0) {
                        throw new DivideBy0Exception("Divide Error");
                    }
                    return op1/op2;

                default:
                    throw new FormatErrorException("Format Error");
            }
        }

        public int calculatePreFix(String[] elements)
        {
            _stackOp.Clear();
            int op1, op2;

            for (int i = elements.Length - 1; i >= 0; i--)
            {
                var el = elements[i];
                if (UNARY_OPERATORS.Contains(el))
                {
                    var nrEl = _stackOp.Count;
                    if (nrEl < 1)
                    {
                        throw new FormatErrorException("Format Error");
                    }
                    op1 = _stackOp.Pop();
                    int res = unaryOpCalc(op1, el);
                    _stackOp.Push(res);
                }
                else if (BINARY_OPERATORS.Contains(el))
                {
                    var nrEl = _stackOp.Count;
                    if (nrEl < 2)
                    {
                        throw new FormatErrorException("Format Error");
                    }
                    op1 = _stackOp.Pop();
                    op2 = _stackOp.Pop();
                    int res = binaryOpCalc(op1, op2, el);
                    _stackOp.Push(res);
                }
                else
                {
                    try {
                        int operand = Convert.ToInt32(el);
                        _stackOp.Push(operand);
                    }
                    catch (OverflowException) {
                        throw new FormatErrorException("Format Error");
                    }
                    catch (FormatException) {
                        throw new FormatErrorException("Format Error");
                    }
                }
            }

            if (_stackOp.Count != 1)
            {
                throw new FormatErrorException("Format Error");
            }
            return _stackOp.Pop();
        }
    }

    internal class Program
    {
        private static void Main(string[] args)
        {
            string line = Console.ReadLine();
            var elements = line.Split(" ", StringSplitOptions.RemoveEmptyEntries);
            Calculator calc = new Calculator();
            try
            {
                var res = calc.calculatePreFix(elements);
                Console.WriteLine(res);
            }
            catch(OverFlow2Exception e)
            {
                Console.WriteLine(e.Message);
            }
            catch(FormatErrorException e)
            {
                Console.WriteLine(e.Message);
            }
            catch(DivideBy0Exception e)
            {
                Console.WriteLine(e.Message);
            }

        }
    }
}