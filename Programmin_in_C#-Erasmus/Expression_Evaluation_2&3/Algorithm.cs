using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Lab9
{
    abstract class Algorithm<T>
    {
        public abstract T VisitPlus(PlusExpression e);
        public abstract T VisitMinus(MinusExpression e);
        public abstract T VisitMultiply(MultiplyExpression e);
        public abstract T VisitDivision(DivideExpression e);
        public abstract T VisitUnaryMinus(UnaryMinusExpression e);
        public abstract T VisitValue(ValueExpression e);
    }
}
