using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.ConstrainedExecution;
using System.Text;
using System.Threading.Tasks;

namespace Lab9
{
    class MinimalParantheses : Algorithm<StringBuilder>
    {
        private static readonly Priority p = new Priority();

        public override StringBuilder VisitDivision(DivideExpression e)
        {
            var op0 = e.Op0;
            var op1 = e.Op1;
            var p_ = PriorityEnum.Multi_Div;
            var p0 = op0.Accept(p);
            var p1 = op1.Accept(p);
            var t0 = op0.Accept(this);
            var t1 = op1.Accept(this);
            if (p_ > p0)
            {
                t0.Insert(0, "(");
                t0.Append(")");
            }
            if (p_ >= p1)
            {
                t1.Insert(0, "(");
                t1.Append(")");
            }
            t0.Append("/");
            t0.Append(t1);
            return t0;
        }

        public override StringBuilder VisitMinus(MinusExpression e)
        {
            var op0 = e.Op0;
            var op1 = e.Op1;
            var p_ = PriorityEnum.Plus_Minus;
            var p0 = op0.Accept(p);
            var p1 = op1.Accept(p);
            var t0 = op0.Accept(this);
            var t1 = op1.Accept(this);
            if (p_ > p0)
            {
                t0.Insert(0, "(");
                t0.Append(")");
            }
            if (p_ >= p1)
            {
                t1.Insert(0, "(");
                t1.Append(")");
            }
            t0.Append("-");
            t0.Append(t1);
            return t0;
        }

        public override StringBuilder VisitMultiply(MultiplyExpression e)
        {
            var op0 = e.Op0;
            var op1 = e.Op1;
            var p_ = PriorityEnum.Multi_Div;
            var p0 = op0.Accept(p);
            var p1 = op1.Accept(p);
            var t0 = op0.Accept(this);
            var t1 = op1.Accept(this);
            if (p_ > p0)
            {
                t0.Insert(0, "(");
                t0.Append(")");
            }
            if (p_ > p1)
            {
                t1.Insert(0, "(");
                t1.Append(")");
            }
            t0.Append("*");
            t0.Append(t1);
            return t0;
        }

        public override StringBuilder VisitPlus(PlusExpression e)
        {
            var op0 = e.Op0;
            var op1 = e.Op1;
            var p_ = PriorityEnum.Plus_Minus;
            var p0 = op0.Accept(p);
            var p1 = op1.Accept(p);
            var t0 = op0.Accept(this);
            var t1 = op1.Accept(this);
            if (p_ > p0)
            {
                t0.Insert(0, "(");
                t0.Append(")");
            }
            if (p_ > p1)
            {
                t1.Insert(0, "(");
                t1.Append(")");
            }
            t0.Append("+");
            t0.Append(t1);
            return t0;
        }

        public override StringBuilder VisitUnaryMinus(UnaryMinusExpression e)
        {
            var op0 = e.Op;
            var p_ = PriorityEnum.Unari_Minus;
            var p0 = op0.Accept(p);
            var t0 = op0.Accept(this);
            if (p_ > p0)
            {
                t0.Insert(0, "(");
                t0.Append(")");
            }
            t0.Insert(0, "-");
            return t0;
        }

        public override StringBuilder VisitValue(ValueExpression e)
        {
            return new StringBuilder(e.Value.ToString());
        }
    }
}
