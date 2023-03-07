using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Linq.Expressions;
using System.Runtime.ConstrainedExecution;
using System.Text;

namespace Lab7_1
{


    public class Cell
    {
        public String data { get; private set; }

        // to know on which cell this one depends in order to find cycles
        // needed only one because a cell can not be in 2 cycles
        public Cell nextCell { get; set; }

        public Cell(String data)
        {
            this.data = data;
        }

        public bool notResolved()
        {
            return data[0] == '=';
        }

        public void setData(String data)
        {
            this.data = data;
        }

    }

    // a formula cell that depends on 2 others
    public class CellNotResolved : Cell
    {
        // 0 -> +
        // 1 -> -
        // 2 -> *
        // 3 -> /
        public int operation { get; }
        // row of first cell
        public int leftIndex1 { get; }
        //coloumn of first cell
        public int leftIndex2 { get; }
        //row of second cell
        public int rightIndex1 { get; }
        //coloumn of second cell
        public int rightIndex2 { get; }

        public CellNotResolved(String data, int operation, int leftIndex1, int leftIndex2, int rightIndex1, int rightIndex2) : base(data)
        {
            this.operation = operation;
            this.leftIndex1 = leftIndex1;
            this.leftIndex2 = leftIndex2;
            this.rightIndex1 = rightIndex1;
            this.rightIndex2 = rightIndex2;
        }
    }

    public class CellAnotherFile : CellNotResolved
    {
        public List<List<Cell>> rightSheet = null;

        public List<List<Cell>> leftSheet = null;

        public CellAnotherFile(List<List<Cell>> rightSheet, String data, int operation, int leftIndex1, int leftIndex2, int rightIndex1, int rightIndex2)
            : base(data, operation, leftIndex1, leftIndex2, rightIndex1, rightIndex2)
        {
            this.rightSheet = rightSheet;
        }

    }

    public class Sheet // BUG nefolosit
    {
        public List<List<Cell>> sheet { get; set; }
        public string name { get; set; }
    }

    public class Errors
    {
        public static readonly string CYCLE = "#CYCLE";
        public static readonly string ERROR = "#ERROR";
        public static readonly string DIV0 = "#DIV0";
        public static readonly string EMPTY = "[]";
        public static readonly string FORMULA = "#FORMULA";
        public static readonly string INVAL = "#INVVAL";
        public static readonly string NOTRESOLVED = "=";
        public static readonly string MISSOP = "#MISSOP";
    }


    public class CellHandler
    {
        // returns the result of a given operation of 2 given numbers
        public static int calcCell(int leftOp, int rightOp, int operation)
        {
            int res = -1;
            switch (operation)
            {
                case 0:
                    res = leftOp + rightOp;
                    break;
                case 1:
                    res = leftOp - rightOp;
                    break;
                case 2:
                    res = leftOp * rightOp;
                    break;
                case 3:
                    res = leftOp / rightOp;
                    break;
            }
            return res;
        }
        //input: string[] cell - length = 2 - the data from a CellNotResolved under the format of a list of 2 strings
        //                                    the first one being the first cell in the formula, and the second, the second one (eg. ["A1", "C5"])
        //converts the format of the excel cells into ints (eg. "B5" -> 2, 5)
        //returns: a tuple of 1 string and 4 ints
        //         if the formula is wrong: <"#FORMULA", -1, -1, -1, -1> (eg. ["BB", "car12"])
        //         else: <"", first cell's row, first cell's coloumn, second cell's row, second cell's coloumn>
        public static Tuple<string, int, int, int, int, string, string> indexCalc(string[] cell)
        {
            var opL = cell[0];
            var opR = cell[1];
            int l1 = 0;
            int p = 1;
            int i = opL.Length - 1; // we start backwards for the first cell

            string file1 = "", file2 = "";

            //until we find numbers or it's not end of string
            while (i >= 0 && opL[i] >= 48 && opL[i] <= 57)
            {
                l1 = p * (opL[i] - 48) + l1;
                p = p * 10;
                i--;
            }
            p = 1;
            int l2 = 0;

            //until we find capital letters or it's not end of string
            while (i >= 0 && opL[i] >= 65 && opL[i] <= 90)
            {
                l2 = p * (opL[i] - 64) + l2;
                p = p * 26;
                i--;
            }

            if (i > -1)
            {
                if (opL[i] == '!')
                {

                    file1 = opL.Substring(0, i);
                }
            }

            //same thing for the second cell
            int r1 = 0;
            p = 1;
            int j = opR.Length - 1;
            while (j >= 0 && opR[j] >= 48 && opR[j] <= 57)
            {
                r1 = p * (opR[j] - 48) + r1;
                p = p * 10;
                j--;
            }
            p = 1;
            int r2 = 0;
            while (j >= 0 && opR[j] >= 65 && opR[j] <= 90)
            {
                r2 = p * (opR[j] - 64) + r2;
                p = p * 26;
                j--;
            }

            if (j > -1)
            {
                if (opR[j] == '!')
                {

                    file2 = opR.Substring(0, i);
                }
            }

            //if we didn't reach end of stream it means we have something else then capital letters and numbers, in this order
            //if we didn't have an index for every row and coloumn but we reached end of stream it means we have only capital letters or onlt numbers
            if (i != -1 || j != -1 || l1 == 0 || l2 == 0 || r1 == 0 || r2 == 0)
            {
                if (i != -1 && file1.Length>0)
                {
                    if (j != -1 && file2.Length>0)
                        return new Tuple<string, int, int, int, int, string, string>(Errors.NOTRESOLVED, l1, l2, r1, r2, file1, file2);
                    if (j != -1 && file2.Length == 0)
                        return new Tuple<string, int, int, int, int, string, string>(Errors.FORMULA, -1, -1, -1, -1, "", "");
                    return new Tuple<string, int, int, int, int, string, string>(Errors.NOTRESOLVED, l1, l2, r1, r2, file1, "");
                }
                if (i != -1 && file1.Length == 0)
                    return new Tuple<string, int, int, int, int, string, string>(Errors.FORMULA, -1, -1, -1, -1, "", "");

                if (j != -1 && file2.Length > 0)
                    return new Tuple<string, int, int, int, int, string, string>(Errors.NOTRESOLVED, l1, l2, r1, r2, "", file2);

                return new Tuple<string, int, int, int, int, string, string>(Errors.FORMULA, -1, -1, -1, -1, "", "");
            }
            else
                return new Tuple<string, int, int, int, int, string, string>("", l1, l2, r1, r2, "", "");

        }
    }

    public class SheetCalc
    {


        private List<List<Cell>> matrix;

        // a set where to store the members of a cycle or cells that depend on a cycle
        private HashSet<Cell> cycle = new HashSet<Cell>();

        public SheetCalc(List<List<Cell>> matrix)
        {
            this.matrix = matrix;
        }

        // we go through the linked list we created and set the data "#CYCLE" 
        private void resolveCycle(Cell cell)
        {
            cell.setData(Errors.CYCLE);
            cycle.Remove(cell);
            var next = cell.nextCell;
            if (cycle.Contains(next))
            {
                resolveCycle(next);
            }
        }

        // we calculate the data for a CellNotResolved
        private void calcCell(List<List<Cell>> sheet, CellNotResolved cell)
        {
            //we add the cell to the cycle set, because we are trying to resolve it
            cycle.Add(cell);
            int l1 = cell.leftIndex1;
            int l2 = cell.leftIndex2;
            int r1 = cell.rightIndex1;
            int r2 = cell.rightIndex2;
            int dataL = -1, dataR = -1;
            Cell lCell = null, rCell = null;

            //try to see if the cells exists
            try
            {
                lCell = sheet[l1][l2];
            }
            catch (ArgumentOutOfRangeException)
            {
                dataL = 0;
            }
            try
            {
                rCell = sheet[r1][r2];
            }
            catch (ArgumentOutOfRangeException)
            {
                dataR = 0;
            }

            //if the left cell is not resolved we resolved it
            if (lCell != null && lCell.notResolved())
            {
                // if we already tried to resolved it once it means we have a cycle
                if (cycle.Contains(lCell))
                {
                    resolveCycle(lCell);
                    return;
                }
                else // we set that this cells depends on the left one and we continue resolving the left one
                {
                    cell.nextCell = lCell;
                    calcCell(sheet, (CellNotResolved)lCell);
                }
            }
            // if we didn't find a cycle
            if (cell.notResolved())
            {
                // the same as the left cell
                if (rCell != null && rCell.notResolved())
                {
                    if (cycle.Contains(rCell))
                    {
                        resolveCycle(rCell);
                        return;
                    }
                    else
                    {
                        cell.nextCell = rCell;
                        calcCell(sheet, (CellNotResolved)rCell);
                    }
                }
            }

            // if we didn't find a cycle
            if (cell.notResolved())
            {
                //if the left cell exists
                if (dataL == -1)
                {
                    String textL = lCell.data;
                    //some kind of an error
                    if (textL[0] == '#')
                    {
                        cell.setData(Errors.ERROR);
                        cycle.Clear();
                        return;
                    }
                    if (textL.Equals("[]"))
                        dataL = 0;
                    else
                        dataL = Convert.ToInt32(textL.ToString());
                }
                // if the right cell exists
                if (dataR == -1)
                {
                    var textR = rCell.data;
                    //some kind of an error
                    if (textR[0] == '#')
                    {
                        cell.setData(Errors.ERROR);
                        cycle.Clear();
                        return;
                    }
                    if (textR.Equals("[]"))
                        dataR = 0;
                    else
                        dataR = Convert.ToInt32(textR.ToString());
                }
                int op = cell.operation;
                if (op == 3 && dataR == 0)
                {
                    cell.setData(Errors.DIV0);
                    cycle.Clear();
                    return;
                }

                var res = CellHandler.calcCell(dataL, dataR, op);
                cell.setData(res.ToString());
                cycle.Clear();

            }
        }

        // we resolved every CellNotResolved
        public void processSheet()
        {
            var sheet = matrix;
            for (int i = 0; i < sheet.Count; i++)
            {
                for (int j = 0; j < sheet[i].Count; j++)
                {
                    var cell = sheet[i][j];
                    if (cell.notResolved())
                    {
                        calcCell(sheet, (CellNotResolved)cell);
                    }
                }
            }
        }
    }

    public class SheetReader_Interpreter
    {
        private StreamReader inF;
        private List<List<Cell>> sheet;
        private List<List<List<Cell>>> otherSheets; // BUG nefolosit


        
        public SheetReader_Interpreter(StreamReader inF)
        {
            this.inF = inF;
            sheet = new List<List<Cell>>();
            
        }

        // we read every cell of the sheet
        // we create a Cell with data "[]", int, "#FORMULA", "#MISSOP" or "#INVAl" if possible
        //      else we create a CellNotResolved
        private void readSheet()
        {
            
            var line = inF.ReadLine();
            // reads every line
            while (line != null)
            {
                List<Cell> cellList = new List<Cell>();
                var cells = line.Split(' ', StringSplitOptions.RemoveEmptyEntries);
                foreach (var cell in cells) // process each cell from the line
                {
                    if (cell.Equals(Errors.EMPTY))
                    {
                        cellList.Add(new Cell(Errors.EMPTY));
                        continue;
                    }
                    if (cell[0] == '=') // a formula
                    {
                        int op = 0;
                        var operators = new char[] { '+', '-', '*', '/' };
                        foreach (var oper in operators) // try to find if it contains an operator
                        {
                            if (cell.Contains(oper))
                            {
                                
                                var cellCopy = cell.Remove(0, 1);
                                var operands = cellCopy.Split(oper, StringSplitOptions.RemoveEmptyEntries); // find the operands
                                if (operands.Length != 2)
                                {
                                    cellList.Add(new Cell(Errors.FORMULA));
                                }
                                else
                                {
                                    var tuple = CellHandler.indexCalc(operands); // find the indexes of the cell operands
                                    if (tuple.Item1.Length != 0) // we have an error with the operands
                                    {
                                        if (tuple.Item1.Equals(Errors.FORMULA))
                                            cellList.Add(new Cell(tuple.Item1));
                                        else
                                        {
                                            var file1 = tuple.Item6;
                                            var file2 = tuple.Item7;
                                            //TODO implement try to read files, dictionary<string, sheet>
                                            //TODO break into different methods
                                            //TODO class CellAnotherFile
                                            
                                        }
                                    }
                                    else
                                    {
                                        cellList.Add(new CellNotResolved(Errors.NOTRESOLVED, op, tuple.Item2 - 1, tuple.Item3 - 1, tuple.Item4 - 1, tuple.Item5 - 1));
                                    }
                                }
                                break;
                            }
                            op++;
                        }
                        if (op == 4) // if we didn't find any operator
                        {
                            cellList.Add(new Cell(Errors.MISSOP));
                        }
                    }
                    else
                    {
                        try // extract the number
                        {
                            cellList.Add(new Cell(Convert.ToInt32(cell).ToString()));
                        }
                        catch (FormatException) // not a number
                        {
                            cellList.Add(new Cell(Errors.INVAL));

                        }
                    }
                }
                sheet.Add(cellList);
                line = inF.ReadLine();
            }
        }

        public List<List<Cell>> getSheet()
        {
            readSheet();
            return sheet;
        }
    }

    public class SheetWriter
    {
        StreamWriter outF;

        public SheetWriter(StreamWriter outF)
        {
            this.outF = outF;
        }

        public void printSheet(List<List<Cell>> sheet)
        {
            StringBuilder line = new StringBuilder();
            foreach (var l in sheet)
            {
                line.Clear();
                foreach (Cell cell in l)
                {
                    line.Append(cell.data);
                    line.Append(" ");
                }
                line.Remove(line.Length - 1, 1);
                outF.WriteLine(line);
            }
        }
    }

    internal class Program
    {
        private static void Main(string[] args)
        {

            if (args.Length != 2)
            {
                Console.WriteLine("Argument Error");
            }
            else
            {
                try
                {
                    using (StreamReader inF = new StreamReader(args[0]))
                    {
                        using (StreamWriter outF = new StreamWriter(args[1]))
                        {
                            SheetReader_Interpreter sri = new SheetReader_Interpreter(inF);
                            var sheet1 = sri.getSheet();
                            SheetCalc sc = new SheetCalc(sheet1);
                            sc.processSheet();
                            SheetWriter sw = new SheetWriter(outF);
                            sw.printSheet(sheet1);
                        }
                    }
                }
                catch (IOException)
                {
                    Console.WriteLine("File Error");
                }
            }
        }
    }
}

// TODO: don't store strings for cell not resolved
// TODO: more types of cells
// TODO: dictionar pt sheeturi