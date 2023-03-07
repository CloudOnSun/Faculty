using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Lab9
{
    class EvaluateDouble : Algorithm<double>
    {
        public sealed override double VisitDivision(DivideExpression e)
        {
            var op0 = e.Op0;
            var op1 = e.Op1;
            var t0 = op0.Accept(this);
            var t1 = op1.Accept(this);
            return t0 / t1;
        }

        public sealed override double VisitMinus(MinusExpression e)
        {
            var op0 = e.Op0;
            var op1 = e.Op1;
            var t0 = op0.Accept(this);
            var t1 = op1.Accept(this);
            return t0 - t1;
        }

        public sealed override double VisitMultiply(MultiplyExpression e)
        {
            var op0 = e.Op0;
            var op1 = e.Op1;
            var t0 = op0.Accept(this);
            var t1 = op1.Accept(this);
            return t0 * t1;
        }

        public sealed override double VisitPlus(PlusExpression e)
        {
            var op0 = e.Op0;
            var op1 = e.Op1;
            var t0 = op0.Accept(this);
            var t1 = op1.Accept(this);
            return t0 + t1;
        }

        public sealed override double VisitUnaryMinus(UnaryMinusExpression e)
        {
            var op = e.Op;
            var t = op.Accept(this);
            return -t;
        }

        public sealed override double VisitValue(ValueExpression e)
        {
            return e.Value;
        }
    }
}
