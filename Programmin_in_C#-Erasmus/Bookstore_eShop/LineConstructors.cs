using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Runtime.InteropServices.ComTypes;
using System.Text;
using System.Threading.Tasks;

namespace NezarkaBookstore
{

    public abstract class CommandConstructor
    {
        public static StringBuilder constructCommand(int tabs, String text = "") 
        {
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i <= tabs; i++)
            {
                sb.Append("\t");
            }
            if (text.Length > 0)
            {
                sb.Append(text);
            }
            return sb;
        }
    }

    public class OpenCommand 
    {
        public static StringBuilder constructCommand(int tabs, String command, String arg = "")
        {
            StringBuilder sb = CommandConstructor.constructCommand(tabs);
            sb.Append("<");
            sb.Append(command);
            if (arg.Length > 0)
            {
                sb.Append(" ");
                sb.Append(arg);
            }
            sb.Append(">");
            return sb;
        }
    }

    public class CloseCommand 
    {
        public static StringBuilder constructCommand(int tabs, String command, String arg = "")
        {
            StringBuilder sb = CommandConstructor.constructCommand(tabs);
            sb.Append("</");
            sb.Append(command);
            if (arg.Length > 0)
            {
                sb.Append(" ");
                sb.Append(arg);
            }
            sb.Append(">");
            return sb;
        }
    }

    public class ParaCnstr
    {
        public static List<StringBuilder> makePara(String command, bool oneLine, bool indentated, List<StringBuilder> context, String arg = "")
        {
            StringBuilder sb;
            List<StringBuilder> list = new List<StringBuilder>();
            if (arg.Length > 0)
            {
                sb = OpenCommand.constructCommand(0, command, arg);
            }
            else
            {
                sb = OpenCommand.constructCommand(0, command);
            }

            if (oneLine)
            {
                foreach (var sb2 in context)
                {
                    sb.Append(sb2);
                }
                sb.Append(CloseCommand.constructCommand(0, command));
                list.Add(sb);
                
            }
            else if(indentated)
            {
                list.Add(sb);
                foreach (var sb2 in context)
                {
                    list.Add(CommandConstructor.constructCommand(1, sb2.ToString()));
                }
                list.Add(CloseCommand.constructCommand(0, command));
            }
            else
            {
                list.Add(sb);
                foreach (var sb2 in context)
                {
                    list.Add(CommandConstructor.constructCommand(0, sb2.ToString()));
                }
                list.Add(CloseCommand.constructCommand(0, command));
            }
            return list;
        }

        public static List<StringBuilder> makePara(String command, bool oneLine, bool indentated, String context, String arg = "")
        {
            StringBuilder sb;
            List<StringBuilder> list = new List<StringBuilder>();
            if (arg.Length > 0)
            {
                sb = OpenCommand.constructCommand(0, command, arg);
            }
            else
            {
                sb = OpenCommand.constructCommand(0, command);
            }

            if (oneLine)
            {
                sb.Append(context);
                sb.Append(CloseCommand.constructCommand(0, command));
                list.Add(sb);

            }
            else if (indentated)
            {
                list.Add(sb);
                if (context.Length > 0)
                    list.Add(CommandConstructor.constructCommand(1, context));
                list.Add(CloseCommand.constructCommand(0, command));
            }
            else
            {
                list.Add(sb);
                list.Add(CommandConstructor.constructCommand(0, context));
                list.Add(CloseCommand.constructCommand(0, command));
            }
            return list;
        }

        public static List<StringBuilder> joinLists(List<StringBuilder>[] listOfLists)
        {
            int size = listOfLists.Length;
            List<StringBuilder> list = listOfLists[0];
            for (int i= 1; i < size; i++)
            {
                list.AddRange(listOfLists[i]);
            }
            return list;
        }
    }

    //public abstract class ParagraphConstructor
    //{
    //    public static List<StringBuilder> constructParagraph()
    //    {
    //        List<StringBuilder> list = new List<StringBuilder>();
    //        return list;
    //    }
    //}

    //public class HeadParagraph 
    //{
    //    public static List<StringBuilder> constructParagraph(int tabs)
    //    {
    //        List<StringBuilder> list = ParagraphConstructor.constructParagraph();
    //        list.Add(OpenCommand.constructCommand(tabs, "head"));
    //        list.Add(OpenCommand.constructCommand(tabs+1, "meta", "charset=\"-8\" /"));

    //        StringBuilder sb = OpenCommand.constructCommand(tabs+1, "title");
    //        sb.Append(CommandConstructor.constructCommand(0, "Nezarka.net: Online Shopping for Books"));
    //        sb.Append(CloseCommand.constructCommand(0, "title"));

    //        list.Add(sb);
    //        list.Add(CloseCommand.constructCommand(tabs, "head"));

    //        return list;
    //    }
    //}

    //public class StyleParagraph 
    //{
    //    public static List<StringBuilder> constructParagraph(int tabs)
    //    {
    //        List<StringBuilder> list = ParagraphConstructor.constructParagraph();
    //        list.Add(OpenCommand.constructCommand(tabs, "style", "type=\"text/css\""));

    //        list.Add(CommandConstructor.constructCommand(tabs+1, "table, th, td {"));
    //        list.Add(CommandConstructor.constructCommand(tabs+2, "border: 1px solid black;"));
    //        list.Add(CommandConstructor.constructCommand(tabs+2, "border-collapse: collapse;"));
    //        list.Add(CommandConstructor.constructCommand(tabs + 1, "}"));
    //        list.Add(CommandConstructor.constructCommand(tabs + 1, "table {"));
    //        list.Add(CommandConstructor.constructCommand(tabs + 2, "margin-bottom: 10px;"));
    //        list.Add(CommandConstructor.constructCommand(tabs + 1, "}"));
    //        list.Add(CommandConstructor.constructCommand(tabs + 1, "pre {"));
    //        list.Add(CommandConstructor.constructCommand(tabs + 2, "line-height: 70%;"));
    //        list.Add(CommandConstructor.constructCommand(tabs + 1, "}"));

    //        list.Add(CloseCommand.constructCommand(tabs, "style"));
    //        return list;
    //    }
    //}

    //public class TitleParagraph
    //{
    //    public static List<StringBuilder> constructParagraph(int tabs)
    //    {
    //        List<StringBuilder> list = ParagraphConstructor.constructParagraph();

    //        StringBuilder sb = OpenCommand.constructCommand(tabs, "h1");
    //        sb.Append(OpenCommand.constructCommand(0, "pre"));
    //        sb.Append(" v,");
    //        sb.Append(OpenCommand.constructCommand(0, "br", "/"));
    //        sb.Append("Nezarka.NET: Online Shopping for Books");
    //        sb.Append(CloseCommand.constructCommand(0, "pre"));
    //        sb.Append(CloseCommand.constructCommand(0, "h1"));

    //        list.Add(sb);
    //        return list;
    //    }
    //}

    //public class RowTableParagraph
    //{
    //    public static List<StringBuilder> constructParagraph(int tabs, List<StringBuilder> context)
    //    {

    //    }
    //}

    //public class MenuParagraph : ParagraphConstructor
    //{
    //    public static new List<StringBuilder> constructParagraph(String customer, int books)
    //}

    //public abstract class Opener : LineConstructor
    //{
    //    public static new StringBuilder constructLine(int tabs) 
    //    { 
    //        StringBuilder sb = LineConstructor.constructLine(tabs);
    //        sb.Append("<");
    //        return sb;
    //    }
    //}

    //public abstract class Closer : LineConstructor
    //{
    //    public static new StringBuilder constructLine(int tabs) 
    //    {
    //        StringBuilder sb = LineConstructor.constructLine(tabs);
    //        sb.Append("</");
    //        return sb;
    //    }
    //}

    //public class HTMLOpen : Opener
    //{
    //    public static StringBuilder constructLine(int tabs, String arg)
    //    {
    //        StringBuilder sb = Opener.constructLine(tabs);
    //        sb.Append("html");
    //        if (arg.Length > 0)
    //        {
    //            sb.Append(" ");
    //            sb.Append(arg);
    //        }
    //        sb.Append(">");
    //        return sb;
    //    }
    //}

    //public class HTMLClose : Closer
    //{
    //    public static new StringBuilder constructLine(int tabs)
    //    {
    //        StringBuilder sb = Closer.constructLine(tabs);
    //        sb.Append("html>");
    //        return sb;
    //    }
    //}

    //public class HeadOpen : Opener
    //{
    //    public static StringBuilder constructLine(int tabs, int headerIndex)
    //    {
    //        StringBuilder sb = Opener.constructLine(tabs);

    //        if(headerIndex == 0)
    //        {
    //            sb.Append("head>");
    //        }
    //        else
    //        {
    //            sb.Append("h");
    //            sb.Append(headerIndex.ToString());
    //            sb.Append(">");
    //         }
    //        return sb;
    //    }
    //}

    //public class HeadClose : Closer
    //{
    //    public static StringBuilder constructLine(int tabs, int headerIndex)
    //    {
    //        StringBuilder sb = Closer.constructLine(tabs);
    //        if (headerIndex == 0)
    //        {
    //            sb.Append("head>");
    //        }
    //        else
    //        {
    //            sb.Append("h");
    //            sb.Append(headerIndex.ToString());
    //            sb.Append(">");
    //        }
    //        return sb;
    //    }
    //}

    //public class BodyOpen : Opener
    //{
    //    public static new StringBuilder constructLine(int tabs)
    //    {
    //        StringBuilder sb = Opener.constructLine(tabs);
    //        sb.Append("body>");
    //        return sb;
    //    }
    //}

    //public class BodyClose : Closer
    //{
    //    public static new StringBuilder constructLine(int tabs)
    //    {
    //        StringBuilder sb = Closer.constructLine(tabs);
    //        sb.Append("body>");
    //        return sb;
    //    }
    //}

    //public class TableOpen : Opener
    //{
    //    public static new StringBuilder constructLine(int tabs)
    //    {
    //        StringBuilder sb = Opener.constructLine(tabs);
    //        sb.Append("table>");
    //        return sb;
    //    }
    //}

    //public class TableClose : Closer
    //{
    //    public static new StringBuilder constructLine(int tabs)
    //    {
    //        StringBuilder sb = Closer.constructLine(tabs);
    //        sb.Append("table>");
    //        return sb;
    //    }
    //}

    //public class RowOpen : Opener
    //{
    //    public static new StringBuilder constructLine(int tabs)
    //    { 
    //        StringBuilder sb = Opener.constructLine(tabs);
    //        sb.Append("tr>");
    //        return sb;
    //    }
    //}

    //public class RowClose : Closer
    //{
    //    public static new StringBuilder constructLine(int tabs)
    //    {
    //        StringBuilder sb = Closer.constructLine(tabs);
    //        sb.Append("tr>");
    //        return sb;
    //    }
    //}

    //public class DataTableOpen : Opener
    //{
    //    public static StringBuilder constructLine(int tabs, String arg)
    //    {
    //        StringBuilder sb = Opener.constructLine(tabs);
    //        sb.Append("tr");
    //        if (arg.Length > 0)
    //        {
    //            sb.Append(" ");
    //            sb.Append(arg);
    //        }
    //        sb.Append(">");
    //        return sb;
    //    }
    //}

    //public class DataTableClose : Closer
    //{
    //    public static new StringBuilder constructLine(int tabs)
    //    {
    //        StringBuilder sb = Closer.constructLine(tabs);
    //        sb.Append("td>");
    //        return sb;
    //    }
    //}

    //public class PreOpen : Opener
    //{
    //    public static new StringBuilder constructLine(int tabs)
    //    {
    //        StringBuilder sb = Opener.constructLine(tabs);
    //        sb.Append("pre>");
    //        return sb;
    //    }
    //}

    //public class PreClose : Closer
    //{
    //    public static new StringBuilder constructLine(int tabs)
    //    {
    //        StringBuilder sb = Closer.constructLine(tabs);
    //        sb.Append("pre>");
    //        return sb;
    //    }
    //}

    //public class AnchorOpen : Opener
    //{
    //    public static StringBuilder constructLine(int tabs, String command)
    //    {
    //        StringBuilder sb = Opener.constructLine(tabs);
    //        sb.Append("a");
    //        if (command.Length > 0)
    //        {
    //            sb.Append(" ");
    //            sb.Append(command);
    //        }
    //        sb.Append(">");
    //        return sb;
    //    }
    //}

    //public class AnchorClose : Closer
    //{
    //    public static new StringBuilder constructLine(int tabs)
    //    {
    //        StringBuilder sb = Closer.constructLine(tabs);
    //        sb.Append("a>");
    //        return sb;
    //    }
    //}


}
