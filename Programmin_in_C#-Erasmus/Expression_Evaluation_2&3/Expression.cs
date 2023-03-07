using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;

namespace Lab9
{
    static class ExprFactory
    {
        private static IEnumerable<Type> subTypesOperExpr = Assembly.GetAssembly(typeof(OperatorExpression))
                                            .GetTypes()
                                            .Where(type => type.IsAssignableTo(typeof(OperatorExpression)) && !type.IsAbstract);

        private static Dictionary<string, Func<OperatorExpression>> ExprMap = new()
        {
            {"+", () => new PlusExpression() },
            {"-", () => new MinusExpression() },
            {"*", () => new MultiplyExpression() },
            {"/", () => new DivideExpression() },
            {"~", () => new UnaryMinusExpression() },
        };

        public static bool addOperator(string oper)
        {

            var operType = typeof(OperatorExpression);

            foreach(var type in subTypesOperExpr)
            {
                var symbolField = type.GetProperty("Symbol");
                var value = symbolField.GetValue(Activator.CreateInstance(type));
                if (value.Equals(oper))
                {
                    ExprMap.Add(oper, () => (OperatorExpression)Activator.CreateInstance(type));
                    return true;
                }
            }


            return false;
        }

        public static OperatorExpression CreateExpression(string oper)
        {
            if (ExprMap.ContainsKey(oper))
                return ExprMap[oper]();
            else if (addOperator(oper))
                return ExprMap[oper]();
            else return null;
        }
    }

    abstract class Expression
    {
        public static Expression ParsePrefixExpression(string exprString)
        {
            string[] tokens = exprString.Split(new char[] { ' ' }, StringSplitOptions.RemoveEmptyEntries);

            if (tokens.Length > 0 && !tokens[0].Equals("="))
                return null;

            Expression result = null;
            Stack<OperatorExpression> unresolved = new Stack<OperatorExpression>();
            for (int i = 1; i < tokens.Length; i++)
            {
                var token = tokens[i];
                if (result != null)
                {
                    // We correctly parsed the whole tree, but there was at least one more unprocessed token left.
                    // This implies incorrect input, thus return null.

                    return null;
                }
                OperatorExpression op = ExprFactory.CreateExpression(token);
                if (op != null)
                    unresolved.Push(op);
                else
                {
                    int value;
                    if (!int.TryParse(token, out value))
                    {
                        return null;    // Invalid token format
                    }

                    Expression expr = new ConstantExpression(value);
                    while (unresolved.Count > 0)
                    {
                        OperatorExpression oper = unresolved.Peek();
                        if (oper.AddOperand(expr))
                        {
                            unresolved.Pop();
                            expr = oper;
                        }
                        else
                        {
                            expr = null;
                            break;
                        }
                    }

                    if (expr != null)
                    {
                        result = expr;
                    }
                }
            }

            return result;
        }

        public abstract T Accept<T>(Algorithm<T> a);
    }

    abstract class ValueExpression : Expression
    {
        public abstract int Value
        {
            get;
        }

        public sealed override T Accept<T>(Algorithm<T> a)
        {
            return a.VisitValue(this);
        }
    }

    sealed class ConstantExpression : ValueExpression
    {
        private int value;

        public ConstantExpression(int value)
        {
            this.value = value;
        }

        public override int Value
        {
            get { return this.value; }
        }
    }

    abstract class OperatorExpression : Expression
    {
        public abstract bool AddOperand(Expression op);

        public abstract string Symbol { get; }
    }

    abstract class UnaryExpression : OperatorExpression
    {
        protected Expression op;

        public Expression Op
        {
            get { return op; }
            set { op = value; }
        }

        public override bool AddOperand(Expression op)
        {
            if (this.op == null)
            {
                this.op = op;
            }
            return true;
        }
    }

    abstract class BinaryExpression : OperatorExpression
    {
        protected Expression op0, op1;

        public Expression Op0
        {
            get { return op0; }
            set { op0 = value; }
        }

        public Expression Op1
        {
            get { return op1; }
            set { op1 = value; }
        }

        public override bool AddOperand(Expression op)
        {
            if (op0 == null)
            {
                op0 = op;
                return false;
            }
            else if (op1 == null)
            {
                op1 = op;
            }
            return true;
        }
    }

    sealed class PlusExpression : BinaryExpression
    {
        public override string Symbol
        {
            get { return "+"; }
        }

        public sealed override T Accept<T>(Algorithm<T> a)
        {
            return a.VisitPlus(this);
        }
    }

    sealed class MinusExpression : BinaryExpression
    {
        public override string Symbol
        {
            get { return "-"; }
        }

        public sealed override T Accept<T>(Algorithm<T> a)
        {
            return a.VisitMinus(this);
        }
    }

    sealed class MultiplyExpression : BinaryExpression
    {
        public override string Symbol
        {
            get { return "*"; }
        }
        public sealed override T Accept<T>(Algorithm<T> a)
        {
            return a.VisitMultiply(this);
        }
    }

    sealed class DivideExpression : BinaryExpression
    {
        public override string Symbol
        {
            get { return "/"; }
        }
        public sealed override T Accept<T>(Algorithm<T> a)
        {
            return a.VisitDivision(this);
        }
    }

    sealed class UnaryMinusExpression : UnaryExpression
    {
        public override string Symbol
        {
            get { return "~"; }
        }
        public sealed override T Accept<T>(Algorithm<T> a)
        {
            return a.VisitUnaryMinus(this);
        }
    }
}
