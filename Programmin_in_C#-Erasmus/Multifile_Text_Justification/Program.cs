using Lab3;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.IO;
using System.Linq;
namespace Lab3
{

    public class WordReader
    {
        StreamReader reader;
        char[] whiteSpaces = { ' ', '\t', '\n', '\r' };
        char previousChar;

        public WordReader(StreamReader sr)
        {
            reader = sr;
            previousChar = '\0';
        }


        /*
         * this method reads a word at a time with "reader" and returns:
         * -1 if it's the end of the file and it only read whitespaces           -- (param) word = ""
         * 0  if it read a word and it's not the end of the file                 -- (param) word = the read word
         * 1 if it read one or more empty lines before and it's not end of file  -- (param) word = "\n" 
         * 2  if it read a word and it's the end of the file                     -- (param) word = the read word
         * 
         */
        public int readWord(ref string word)
        {
            word = "";
            int emptyLines = 0;

            //checks if we didn't miss a character, when we returned 1 with word = "\n"
            if (!whiteSpaces.Contains(previousChar) && previousChar != '\0')
            {
                word += previousChar;
            }

            //checks if character that was read the last, the previous time, was an "\n", in order to count the new lines
            if (previousChar == '\n')
            {
                emptyLines++;
            }

            char character = (char)reader.Read();
            while (character == '\r')
            {
                character = (char)reader.Read();
            }
            previousChar = character;

            //it's word made of a single letter between a new line and a white space
            if (word.Length > 0 && whiteSpaces.Contains(character))
            {
                return 0;
            }

            //reads all the white characters before a word or before end of the stream
            //counts how many empty lines exists before finding a word
            while (whiteSpaces.Contains(character) && !reader.EndOfStream)
            {
                if (character == '\n')
                {
                    emptyLines++;
                }
                character = (char)reader.Read();
                while (character == '\r')
                {
                    character = (char)reader.Read();
                }
            }

            //checks if is the end of the stream and if
            //      1) the file ends with a single letter, surrounded by whitespaces
            //      2) the file ends in only white spaces
            if (reader.EndOfStream)
            {
                if (!whiteSpaces.Contains(character))
                {
                    word += character.ToString();
                    return 2;
                }
                else if (word.Length > 0)
                { //previous character
                    return 2;
                }
                else
                {
                    return -1;
                }
            }

            //checks if there are 2 or more "\n", which means that we should have a empty lines between paragraphs
            if (emptyLines >= 2)
            {
                word = "\n";
                if (character != '\n')
                { // we store the last character if it's a letter 
                    previousChar = character;
                }
                else
                { // we make sure not to count the last '\n' next time
                    previousChar = '\0';
                }
                return 1;
            }

            //read all the not white characters and put them in the word
            while (!whiteSpaces.Contains(character) && !reader.EndOfStream)
            {
                word += character.ToString();
                character = (char)reader.Read();
                while (character == '\r')
                {
                    character = (char)reader.Read();
                }
            }

            //checks if we are at the end of file, if so we make sure not to lose the last letter
            if (reader.EndOfStream)
            {
                if (!whiteSpaces.Contains(character))
                    word += character.ToString();
                return 2;
            }
            previousChar = character; // we store the last read character, to check next time if it's a '\n' or not
            return 0;
        }

    }

    public class LineWorker
    {
        StreamReader sr;
        WordReader wr;
        StreamWriter sw;
        int lineLength;
        string previousWord;
        string[] files;
        bool highlight;
        Queue<string> readWords = new Queue<string>();

        public LineWorker(StreamWriter sw, int lineLength, string[] files, bool highlight)
        {
            this.sw = sw;
            //wr = new WordReader(sr);
            this.lineLength = lineLength;
            previousWord = "";
            this.files = files;
            this.highlight = highlight;
        }


        /* writes the line with the given format of whiteSpaces
         * (param) int whiteSpaces: the lowest number of needed spaces between every word
         * (param) int irregularWhiteSpaces: the last non regular white spaces needed to fill the line
         */
        private void writeIndentated(int whiteSpaces, int irregularWhiteSpaces)
        {
            int length = readWords.Count;
            if (length > 0)
            {
                string space = "";
                if (highlight)
                    space = ".";
                else
                    space = " ";
                string buffer = "";
                for (int i = 1; i < length; i++)
                {
                    buffer += readWords.Dequeue();
                    for (int j = 1; j <= whiteSpaces; j++)
                    {
                        buffer += space;
                    }
                    if (irregularWhiteSpaces > 0)
                    {
                        buffer += space;
                        irregularWhiteSpaces--;
                    }
                }
                buffer += readWords.Dequeue();
                for (int i = 0; i < buffer.Length; i++)
                {
                    sw.Write(buffer[i]);
                }
            }
            if (highlight)
                sw.Write("<-\n");
            else
                sw.Write("\n");
            readWords.Clear(); // we clear the queue

        }


        /*
         * calculets the needed spaces between every word and calls writeIndentated function to write the line
         */
        private void calculateSpacesAndWriteIndentated(int length)
        {
            int missingWhiteSpaces = lineLength - length;
            int nrOfSpaces = readWords.Count - 1;
            int whiteSpaces;
            int irregularWhiteSpaces;
            if (nrOfSpaces == 0)
            {
                whiteSpaces = 0;
                irregularWhiteSpaces = 0;
            }
            else
            {
                whiteSpaces = 1 + (missingWhiteSpaces / nrOfSpaces);
                irregularWhiteSpaces = missingWhiteSpaces % nrOfSpaces;
            }
            writeIndentated(whiteSpaces, irregularWhiteSpaces);
        }


        private bool endOfFileWithWord(int value)
        {
            if (value == 2)
            {
                return true;
            }
            else
                return false;
        }


        private bool justWordInMiddle(int value)
        {
            if (value == 0)
                return true;
            return false;
        }


        private bool needOfEmptyLine(int value)
        {
            if (value == 1)
                return true;
            return false;
        }


        private bool endOfFile(int value)
        {
            if (value == -1)
                return true;
            return false;
        }

        /*
         * checks if the previous word is too big to write on any line and prints it if so
         * return: false if it's not too big
         *         true if it is too big
         */
        //private bool check_PrintPreviousWasTooBig()
        //{
        //    if (previousWord.Length > lineLength)
        //    {
        //        readWords.Enqueue(previousWord);
        //        writeIndentated(0, 0);
        //        previousWord = "";
        //        return true;
        //    }
        //    return false;
        //}

        /*
         * if exists, adds the previous word to (param) readWord
         *            returns the length of the of the word
         * if not,    returns 0
         */
        private int sizePreviousWords()
        {
            int count = readWords.Count;
            int length = 0;
            for (int i = 0; i < count; i++)
            {
                string word = readWords.Dequeue();
                readWords.Enqueue(word);
                length += word.Length + 1;
            }
            return length;
        }

        /*
         * verifies if a word is too big to be printed on a normal line and there are no words waiting to be written
         *                                                                              (it is the firs one in the line)
         * returns: -1 if word isn't to big
         *          0  if it is, but it's not end of file
         *          1  if it is and it's and of file
         */
        private int check_PrintNextIsTooBig(int value)
        {

            if (readWords.Count == 1)
            {
                string word = readWords.Dequeue();
                readWords.Enqueue(word);

                if (word.Length > lineLength)
                {
                    writeIndentated(1, 0);
                    if (endOfFileWithWord(value))
                        return 1;
                    return 0;
                }
            }
            return -1;
        }


        /* we add the maximum amount of words in queue readWords
         * length becomes the length of all words + 1 space between each of them
         * word is the last read word which didn't fit in the line
         * value is the returned value when we read tha last word
         * 
         * it can writes the words if:
         *      1: there was a need of empty line: writes the read words a single space between them and the empty line
         *      
         * 
         * returns: 0 if case 1)
         *          1 if case end of file
         *          -1 otherwise
         */
        private int processWords(ref int length, ref string word, ref int value)
        {
            value = wr.readWord(ref word);
            // we test if we can add the next word to the line; if need of emtpy file, word(\n) has length=1 but we will never add it we need to test it with one space lower
            while (length + word.Length + 1 <= lineLength + 1 || (word == "\n" && length + word.Length <= lineLength + 1))
            {
                if (needOfEmptyLine(value))
                {
                    if (readWords.Count > 0)
                        writeIndentated(1, 0); // write the last remaining words
                    writeIndentated(1, 0); //write just a empty line
                    return 0;
                }
                if (endOfFileWithWord(value) || justWordInMiddle(value)) // we read a word, we add it to be processed now or for the next file
                {  
                    readWords.Enqueue(word);
                }
                if(endOfFile(value) || endOfFileWithWord(value)) // end of file 
                {
                    return 1;
                }
                length += word.Length + 1;
                value = wr.readWord(ref word);
            }
            return -1;
        }

        /* writes a line in the specific way
         * returns: 1 if it's end of file
         *          0 otherwise
         */
        public int writeLine()
        {
            
            int length = 0;

            length += sizePreviousWords();

            string lastWord = "";
            int valueWord = -2;
            int returnedValue = processWords(ref length, ref lastWord, ref valueWord);
            if (returnedValue != -1) // if it's end of file or we already wrote an empty line
                return returnedValue;

            if (length > 0) // only if we have something to be printed (could happen that we read a word too big and nothing was added)
            {
                length--;
                calculateSpacesAndWriteIndentated(length);
            }

            //if we have a perfectly amount of words (divided by only 1 space) and the next character needed is a blank line
            //  a old implementation (might be not needed because case needOfEmptyLine from processWord method
            if (needOfEmptyLine(valueWord))
            {
                writeIndentated(1, 0);
            }

            if (lastWord != "\n")
                readWords.Enqueue(lastWord);

            //a word that is too big for a normal line
            int valueTooBig = check_PrintNextIsTooBig(valueWord);
            if (valueTooBig != -1)
                return valueTooBig;


            //we reached the end of file
            if (endOfFileWithWord(valueWord) || endOfFile(valueWord))
            {
                return 1;
            }

            return 0;
        }


        public void processAllFiles()
        {
            int i = 0;
            if (highlight)
                i = 1;
            int nrOfFiles = 0;
            for (; i < files.Length; i++)
            {
                try
                {
                    sr = new StreamReader(files[i]);
                    
                    wr = new WordReader(sr);
                    while (writeLine() != 1)
                    {
                        continue;
                    }
                    nrOfFiles++;
                    
                }
                catch(IOException)
                {
                    continue;
                }
            }
           
            writeIndentated(1, 0); // we write the last words that exists in readWords from the last file
           

        }


    }



    internal class Program
    {
        public static void Main(string[] args)
        {
            if (args.Length < 3 || (args.Length == 3 && args[0] == "--highlight-spaces")) {
                Console.WriteLine("Argument Error");
            }
            else {
                try {

                    bool highlight = false;
                    if (args[0] == "--highlight-spaces")
                        highlight = true;
                    string outFile = args[args.Length - 2];
                    int lineLength = Int32.Parse(args[args.Length - 1]);
                    using (StreamWriter sw = new StreamWriter(outFile)) {

                        if (lineLength > 0) {
                            string[] infiles = new string[args.Length - 2];
                            int i = 0;
                            if (highlight)
                                i = 1;
                            else i = 0;
                            for (; i < args.Length - 2; i++) {
                                infiles[i] = args[i];
                            }
                            var lineWorker = new LineWorker(sw, lineLength, infiles, highlight);
                            lineWorker.processAllFiles();
                        }
                        else
                        {
                            Console.WriteLine("Argument Error");
                        }
                    }
                }
                //catch (IOException)
                //{
                //    Console.WriteLine("File Error");
                //}
                catch (Exception ex) when (
                    ex is FormatException
                    || ex is ArgumentNullException
                    || ex is OverflowException
                )
                {
                    Console.WriteLine("Argument Error");
                }

            }
        }
    }
}


////////////        TESTS        ///////////////////////
///using Lab3;

//namespace TestLab3
//{
//    [TestClass]
//    public class UnitTest1
//    {

//        string[] files = new string[1];
//        string[] files2 = new string[2];
//        string[] files3 = new string[6];

//        private void addFile(int position, string fileName, string[] files)
//        {
//            files[position] = fileName;
//        }


//        [TestMethod]
//        public void WordReader_readWord_reads_correct_words()
//        {
//            using (StreamWriter sw = new StreamWriter("testin.txt"))
//            {
//                sw.Write("  dsas ffkm km \n \n \n  sadjn as   nfjn \n as    \t asd s  ");
//            }

//            StreamReader sr = new StreamReader("testin.txt");

//            WordReader wordReader = new WordReader(sr);

//            string word = "";
//            int value = wordReader.readWord(ref word);
//            Assert.AreEqual("dsas", word);
//            Assert.AreEqual(0, value);

//            value = wordReader.readWord(ref word);
//            Assert.AreEqual("ffkm", word);
//            Assert.AreEqual(0, value);

//            value = wordReader.readWord(ref word);
//            Assert.AreEqual("km", word);
//            Assert.AreEqual(0, value);

//            value = wordReader.readWord(ref word);
//            Assert.AreEqual("\n", word);
//            Assert.AreEqual(1, value);

//            value = wordReader.readWord(ref word);
//            Assert.AreEqual("sadjn", word);
//            Assert.AreEqual(0, value);

//            value = wordReader.readWord(ref word);
//            Assert.AreEqual("as", word);
//            Assert.AreEqual(0, value);

//            value = wordReader.readWord(ref word);
//            Assert.AreEqual("nfjn", word);
//            Assert.AreEqual(0, value);

//            value = wordReader.readWord(ref word);
//            Assert.AreEqual("as", word);
//            Assert.AreEqual(0, value);

//            value = wordReader.readWord(ref word);
//            Assert.AreEqual("asd", word);
//            Assert.AreEqual(0, value);

//            value = wordReader.readWord(ref word);
//            Assert.AreEqual("s", word);
//            Assert.AreEqual(0, value);

//            value = wordReader.readWord(ref word);
//            Assert.AreEqual("", word);
//            Assert.AreEqual(-1, value);

//            sr.Close();

//            using (StreamWriter sw = new StreamWriter("testin.txt"))
//            {
//                sw.Write("s");
//            }

//            StreamReader sr2 = new StreamReader("testin.txt");
//            WordReader wordReader2 = new WordReader(sr2);

//            value = wordReader2.readWord(ref word);
//            Assert.AreEqual("s", word);
//            Assert.AreEqual(2, value);

//            sr2.Close();

//        }

//        [TestMethod]
//        public void LineWorker_writeLine_writes_without_highlight()
//        {
//            using (StreamWriter sw = new StreamWriter("testin2.txt"))
//            {
//                sw.Write("aa a\nm mm\n \t \n\naaaaaa a\nds a a a as d");
//            }
//            int lineLength = 5;
//            using (StreamWriter sw = new StreamWriter("testout.txt"))
//            {
//                addFile(0, "testin2.txt", files);
//                int nrOfFiles = 1;
//                LineWorker lineWorker = new LineWorker(sw, lineLength, files, false);
//                lineWorker.processAllFiles();
//            }
//            using (StreamReader sr = new StreamReader("testout.txt"))
//            {
//                string line = sr.ReadLine();
//                Assert.AreEqual("aa  a", line);

//                line = sr.ReadLine();
//                Assert.AreEqual("m mm", line);

//                line = sr.ReadLine();
//                Assert.AreEqual("", line);

//                line = sr.ReadLine();
//                Assert.AreEqual("aaaaaa", line);

//                line = sr.ReadLine();
//                Assert.AreEqual("a  ds", line);

//                line = sr.ReadLine();
//                Assert.AreEqual("a a a", line);

//                line = sr.ReadLine();
//                Assert.AreEqual("as d", line);
//            }
//        }


//        [TestMethod]
//        public void LineWorker_writeLine_writes_with_highlight()
//        {
//            using (StreamWriter sw = new StreamWriter("testin3.txt"))
//            {
//                sw.Write("aa a\nm mm\n \t \n\naaaaaa a\nds a a a as d");
//            }
//            int lineLength = 5;
//            using (StreamWriter sw = new StreamWriter("testout.txt"))
//            {
//                addFile(0, "--highilght", files2);
//                addFile(1, "testin3.txt", files2);
//                int nrOfFiles = 1;
//                LineWorker lineWorker = new LineWorker(sw, lineLength, files2, true);
//                lineWorker.processAllFiles();
//            }
//            StreamReader sr = new StreamReader("testout.txt");
//            string line = sr.ReadLine();
//            Assert.AreEqual("aa..a<-", line);

//            line = sr.ReadLine();
//            Assert.AreEqual("m.mm<-", line);

//            line = sr.ReadLine();
//            Assert.AreEqual("<-", line);

//            line = sr.ReadLine();
//            Assert.AreEqual("aaaaaa<-", line);

//            line = sr.ReadLine();
//            Assert.AreEqual("a..ds<-", line);

//            line = sr.ReadLine();
//            Assert.AreEqual("a.a.a<-", line);

//            line = sr.ReadLine();
//            Assert.AreEqual("as.d<-", line);

//            sr.Close();
//        }

//        [TestMethod]
//        public void LineWorker_writeLine_writes_with_highlight_more_files()
//        {
//            using (StreamWriter sw = new StreamWriter("testin4.txt"))
//            {
//                sw.Write("aa a\nm mm\n \t \n\naaaaaa a\nds a a a a");
//            }
//            int lineLength = 5;
//            using (StreamWriter sw = new StreamWriter("testout.txt"))
//            {
//                addFile(0, "--highilght", files3);
//                addFile(1, "xx.txt", files3);
//                addFile(2, "testin4.txt", files3);
//                addFile(3, "xx.txt", files3);
//                addFile(4, "testin4.txt", files3);
//                addFile(5, "xx.txt", files3);
//                LineWorker lineWorker = new LineWorker(sw, lineLength, files3, true);
//                lineWorker.processAllFiles();
//            }
//            StreamReader sr = new StreamReader("testout.txt");
//            string line = sr.ReadLine();
//            Assert.AreEqual("aa..a<-", line);

//            line = sr.ReadLine();
//            Assert.AreEqual("m.mm<-", line);

//            line = sr.ReadLine();
//            Assert.AreEqual("<-", line);

//            line = sr.ReadLine();
//            Assert.AreEqual("aaaaaa<-", line);

//            line = sr.ReadLine();
//            Assert.AreEqual("a..ds<-", line);

//            line = sr.ReadLine();
//            Assert.AreEqual("a.a.a<-", line);

//            line = sr.ReadLine();
//            Assert.AreEqual("a..aa<-", line);

//            line = sr.ReadLine();
//            Assert.AreEqual("a...m<-", line);

//            line = sr.ReadLine();
//            Assert.AreEqual("mm<-", line);

//            line = sr.ReadLine();
//            Assert.AreEqual("<-", line);

//            line = sr.ReadLine();
//            Assert.AreEqual("aaaaaa<-", line);

//            line = sr.ReadLine();
//            Assert.AreEqual("a..ds<-", line);

//            line = sr.ReadLine();
//            Assert.AreEqual("a.a.a<-", line);

//            line = sr.ReadLine();
//            Assert.AreEqual("a<-", line);

//            sr.Close();
//        }

//    }
//}