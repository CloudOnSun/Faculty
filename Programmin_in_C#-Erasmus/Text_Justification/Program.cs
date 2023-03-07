using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.IO;
using System.Linq;

namespace Lab2
{

    class WordReader
    {
        StreamReader reader;
        char[] whiteSpaces = { ' ', '\t', '\n', '\r'};
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
            if (!whiteSpaces.Contains(previousChar) && previousChar != '\0') {
                word += previousChar;
            }

            //checks if character that was read the last, the previous time, was an "\n", in order to count the new lines
            if (previousChar == '\n') {
                emptyLines++;
            }

            char character = (char) reader.Read();
            while (character == '\r') {
                character = (char) reader.Read();
            }
            previousChar = character;

            //it's word made of a single letter between a new line and a white space
            if(word.Length > 0 && whiteSpaces.Contains(character)) {
                return 0;
            }

            //reads all the white characters before a word or before end of the stream
            //counts how many empty lines exists before finding a word
            while (whiteSpaces.Contains(character) && !reader.EndOfStream) {
                if(character == '\n') {
                    emptyLines++;
                }
                character = (char) reader.Read();
                while (character == '\r') {
                    character = (char)reader.Read();
                }
            }

            //checks if is the end of the stream and if
            //      1) the file ends with a single letter, surrounded by whitespaces
            //      2) the file ends in only white spaces
            if (reader.EndOfStream) {
                if (!whiteSpaces.Contains(character)) {
                    word += character.ToString();
                    return 2;
                }
                else if (word.Length > 0) { //previous character
                    return 2;
                }
                else {
                    return -1;
                }
            }

            //checks if there are 2 or more "\n", which means that we should have a empty lines between paragraphs
            if (emptyLines >= 2) {
                word = "\n";
                if (character != '\n') { // we store the last character if it's a letter 
                    previousChar = character;
                }
                else { // we make sure not to count the last '\n' next time
                    previousChar = '\0';
                }
                return 1;
            }

            //read all the not white characters and put them in the word
            while (!whiteSpaces.Contains(character) && !reader.EndOfStream) {
                word += character.ToString();
                character = (char) reader.Read();
                while (character == '\r') {
                    character = (char)reader.Read();
                }
            }

            //checks if we are at the end of file, if so we make sure not to lose the last letter
            if(reader.EndOfStream) {
                if(!whiteSpaces.Contains(character))
                    word += character.ToString();
                return 2;
            }
            previousChar = character; // we store the last read character, to check next time if it's a '\n' or not
            return 0;
        }

    }

    class LineWorker
    {
        WordReader wr;
        StreamWriter sw;
        int lineLength;
        string previousWord;

        public LineWorker(StreamReader sr, StreamWriter sw, int lineLength)
        { 
            this.sw = sw;
            wr = new WordReader(sr);
            this.lineLength = lineLength;
            previousWord = "";
        }


        /* writes the line with the given format of whiteSpaces
         * (param) int whiteSpaces: the lowest number of needed spaces between every word
         * (param) int irregularWhiteSpaces: the last non regular white spaces needed to fill the line
         */
        private void writeIndentated(Queue<string> words, int whiteSpaces, int irregularWhiteSpaces)
        {
            int length = words.Count;
            string buffer = "";
            for(int i = 1; i < length; i++)
            {
                buffer += words.Dequeue();
                for(int j = 1; j <= whiteSpaces; j++)
                {
                    buffer += ".";
                }
                if (irregularWhiteSpaces > 0)
                {
                    buffer += ".";
                    irregularWhiteSpaces--;
                }
            }
            buffer += words.Dequeue();
            for(int i = 0; i < buffer.Length; i++)
            {
                sw.Write(buffer[i]);
            }
            sw.Write("<-\n");

        }


        /*
         * calculets the needed spaces between every word and calls writeIndentated function to write the line
         */
        private void calculateSpacesAndWriteIndentated(int length, Queue<string> words)
        {
            int missingWhiteSpaces = lineLength - length;
            int nrOfSpaces = words.Count - 1;
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
            writeIndentated(words, whiteSpaces, irregularWhiteSpaces);
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
        private bool check_PrintPreviousWasTooBig()
        {
            Queue<string> readWords = new Queue<string>();
            if (previousWord.Length > lineLength)
            {
                readWords.Enqueue(previousWord);
                writeIndentated(readWords, 0, 0);
                previousWord = "";
                return true;
            }
            return false;
        }

        /*
         * if exists, adds the previous word to (param) readWord
         *            returns the length of the of the word
         * if not,    returns 0
         */
        private int addPreviousIfExists(Queue<string> readWords)
        {
            if (previousWord.Length > 0) 
            {
                readWords.Enqueue(previousWord);
                return previousWord.Length + 1;
            }
            return 0;
        }

        /*
         * verifies if a word is too big to be printed on a normal line and there are no words waiting to be written
         *                                                                              (it is the firs one in the line)
         * returns: -1 if word isn't to big
         *          0  if it is, but it's not end of file
         *          1  if it is and it's and of file
         */
        private int check_PrintNextIsTooBig(int length, ref string word, Queue<string> readWords, int value)
        {
            if (length == 0 && word.Length > lineLength)
            {
                readWords.Enqueue(word);
                writeIndentated(readWords, 1, 0);
                if (endOfFileWithWord(value))
                    return 1;
                return 0;
            }
            return -1;
        }


        /* we add the maximum amount of words in queue readWords
         * length becomes the length of all words + 1 space between each of them
         * lastWord is the last read word which didn't fit in the line
         * valueWord is the returned value when we read tha last word
         * 
         * it can writes the words if:
         *      1) there was a need of empty line: writes the read words a single space between them and the empty line
         *      2) it's end of file and there are words to be written
         * 
         * returns: 0 if case 1)
         *          1 if case 2)
         *          -1 otherwise
         */
        private int processWords(ref int length, ref string word, Queue<string> readWords, ref int value)
        {
            value = wr.readWord(ref word);
            while (length + word.Length + 1 <= lineLength + 1)
            {
                if (endOfFile(value))
                {
                    if (readWords.Count > 0) // print the last remaining words
                        writeIndentated(readWords, 1, 0);
                    return 1;
                }
                if (needOfEmptyLine(value))
                {
                    if (readWords.Count > 0)
                        writeIndentated(readWords, 1, 0); // write the last remaining words
                    sw.Write("<-\n"); //print the blank line
                    previousWord = "";
                    return 0;
                }
                if (endOfFileWithWord(value))
                {  // if end of file but there was a word read
                    readWords.Enqueue(word);
                    writeIndentated(readWords, 1, 0);
                    return 1;
                }
                if (justWordInMiddle(value))
                {
                    readWords.Enqueue(word);
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
            Queue<string> readWords = new Queue<string>();

            //there was a line written and the next word was too big to fit that line or even a normal line
            if (check_PrintPreviousWasTooBig()) {
                return 0;
            }

            length += addPreviousIfExists(readWords);

            string lastWord = "";
            int valueWord = -2;
            int returnedValue = processWords(ref length, ref lastWord, readWords, ref valueWord);
            if (returnedValue != -1) // if it's end of file or we already wrote an empty line
                return returnedValue;

            //a word that is too big for a normal line is the first one supposed to be written on the file
            int valueTooBig = check_PrintNextIsTooBig(length, ref lastWord, readWords, valueWord);
            if (valueTooBig != -1)
                return valueTooBig;

            //if we have a perfectly amount of words (divided by only 1 space) and the next character needed is a blank line
            if (needOfEmptyLine(valueWord)) {
                writeIndentated(readWords, 1, 0);
                sw.Write("<-\n");
                previousWord = "";
                return 0;
            }

            length--;
            calculateSpacesAndWriteIndentated(length, readWords);

            //we reached the end of file but there is still a word to be written
            if(endOfFileWithWord(valueWord)) {
                readWords.Clear();
                readWords.Enqueue(lastWord);
                writeIndentated(readWords, 1, 0);
                return 1;
            }

            if (endOfFile(valueWord))
                return 1;
            
            previousWord = lastWord;
            return 0;
        }
    }


    internal class Program
    {
        public static void Main(string[] args)
        {
            if (args.Length != 3)
            {
                Console.WriteLine("Argument Error");
            }
            else
            {
                try
                {
                    string inFile = args[0];
                    string outFile = args[1];
                    int lineLength = Int32.Parse(args[2]);
                    using (StreamReader sr = new StreamReader(inFile))
                    {
                        using (StreamWriter sw = new StreamWriter(outFile))
                        {

                            if (lineLength > 0)
                            {
                                var lineWorker = new LineWorker(sr, sw, lineLength);

                                while (lineWorker.writeLine() != 1)
                                {
                                    continue;
                                }
                            }
                            else
                            {
                                Console.WriteLine("Argument Error");
                            }
                        }
                    }
                }
                catch(IOException)
                {
                    Console.WriteLine("File Error");
                }
                catch(Exception ex) when (
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