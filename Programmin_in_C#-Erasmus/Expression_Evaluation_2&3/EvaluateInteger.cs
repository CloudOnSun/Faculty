using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Lab9
{
    class EvaluateInteger : Algorithm<int>
    {

        public sealed override int VisitDivision(DivideExpression e)
        {
            var op0 = e.Op0;
            var op1 = e.Op1;
            var t0 = op0.Accept(this);
            var t1 = op1.Accept(this);
            checked { return t0 / t1; }
        }

        public sealed override int VisitMinus(MinusExpression e)
        {
            var op0 = e.Op0;
            var op1 = e.Op1;
            var t0 = op0.Accept(this);
            var t1 = op1.Accept(this);
            checked { return t0 - t1; }
        }

        public sealed override int VisitMultiply(MultiplyExpression e)
        {
            var op0 = e.Op0;
            var op1 = e.Op1;
            var t0 = op0.Accept(this);
            var t1 = op1.Accept(this);
            checked { return t0 * t1; }
        }

        public sealed override int VisitPlus(PlusExpression e)
        {
            var op0 = e.Op0;
            var op1 = e.Op1;
            var t0 = op0.Accept(this);
            var t1 = op1.Accept(this);
            checked { return t0 + t1; }
        }

        public sealed override int VisitUnaryMinus(UnaryMinusExpression e)
        {
            var op = e.Op;
            var t = op.Accept(this);
            checked { return -t; }
        }

        public sealed override int VisitValue(ValueExpression e)
        {
            return e.Value;
        }
    }
}
