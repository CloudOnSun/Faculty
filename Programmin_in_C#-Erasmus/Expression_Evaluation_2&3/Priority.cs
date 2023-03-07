using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Lab9
{
    class Priority : Algorithm<PriorityEnum>
    {
        public override PriorityEnum VisitDivision(DivideExpression e)
        {
            return PriorityEnum.Multi_Div;
        }

        public override PriorityEnum VisitMinus(MinusExpression e)
        {
            return PriorityEnum.Plus_Minus;
        }

        public override PriorityEnum VisitMultiply(MultiplyExpression e)
        {
            return PriorityEnum.Multi_Div;
        }

        public override PriorityEnum VisitPlus(PlusExpression e)
        {
            return PriorityEnum.Plus_Minus;
        }

        public override PriorityEnum VisitUnaryMinus(UnaryMinusExpression e)
        {
            return PriorityEnum.Unari_Minus;
        }

        public override PriorityEnum VisitValue(ValueExpression e)
        {
            return PriorityEnum.Value;
        }
    }
}
