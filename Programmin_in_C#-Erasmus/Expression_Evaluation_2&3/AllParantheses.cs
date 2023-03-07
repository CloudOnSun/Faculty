using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Lab9
{
    class AllParantheses : Algorithm<StringBuilder>
    {
        public override StringBuilder VisitDivision(DivideExpression e)
        {
            var op0 = e.Op0;
            var op1 = e.Op1;
            var t0 = op0.Accept(this);
            var t1 = op1.Accept(this);
            t0.Insert(0, "(");
            t0.Append("/");
            t0.Append(t1);
            t0.Append(")");
            return t0;
        }

        public override StringBuilder VisitMinus(MinusExpression e)
        {
            var op0 = e.Op0;
            var op1 = e.Op1;
            var t0 = op0.Accept(this);
            var t1 = op1.Accept(this);
            t0.Insert(0, "(");
            t0.Append("-");
            t0.Append(t1);
            t0.Append(")");
            return t0;
        }

        public override StringBuilder VisitMultiply(MultiplyExpression e)
        {
            var op0 = e.Op0;
            var op1 = e.Op1;
            var t0 = op0.Accept(this);
            var t1 = op1.Accept(this);
            t0.Insert(0, "(");
            t0.Append("*");
            t0.Append(t1);
            t0.Append(")");
            return t0;
        }

        public override StringBuilder VisitPlus(PlusExpression e)
        {
            var op0 = e.Op0;
            var op1 = e.Op1;
            var t0 = op0.Accept(this);
            var t1 = op1.Accept(this);
            t0.Insert(0, "(");
            t0.Append("+");
            t0.Append(t1);
            t0.Append(")");
            return t0;
        }

        public override StringBuilder VisitUnaryMinus(UnaryMinusExpression e)
        {
            var op = e.Op;
            var t = op.Accept(this);
            t.Insert(0, "(-");
            t.Append(")");
            return t;
        }

        public override StringBuilder VisitValue(ValueExpression e)
        {
            return new StringBuilder(e.Value.ToString());
        }
    }
}
