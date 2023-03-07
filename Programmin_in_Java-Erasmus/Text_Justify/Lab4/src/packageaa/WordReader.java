package packageaa;

import java.io.*;
import java.util.Scanner;

public class WordReader {
    char previousChar;
    BufferedReader reader;
    public WordReader(BufferedReader reader)  {

        previousChar = '\0';
        this.reader = reader;//new BufferedReader(new InputStreamReader(System.in)); //new BufferedReader(fileReader);

    }

    /**
     * this method reads a word at a time with "reader" and returns:
     * -1 if it's the end of the file, and it only read whitespaces           -- (param) word = ""
     * 0  if it read a word, and it's not the end of the file                 -- (param) word = the read word
     * 1 if it read one or more empty lines before, and it's not end of file  -- (param) word = "\n"
     * 2  if it read a word, and it's the end of the file                     -- (param) word = the read word
     *
     */
    public int readWord(StringBuilder word) throws IOException{
        word.delete(0,word.length());
        int emptyLines = 0;

        //checks if we didn't miss a character, when we returned 1 with word = "\n"
        if (!Character.isWhitespace(previousChar) && previousChar != '\0') {
            word.append(previousChar);
        }

        //checks if character that was read the last, the previous time, was an "\n", in order to count the new lines
        if (previousChar == '\n') {
            emptyLines++;
        }
        int eofValue = reader.read();
        if (eofValue == -1)
        {
            if (word.length() > 0)
                return 2;
            return -1;
        }

        char character = (char) eofValue;
        previousChar = character;

        //it's word made of a single letter between a new line and a white space
        if(word.length() > 0 && Character.isWhitespace(character)) {
            return 0;
        }

        //reads all the white characters before a word or before end of the stream
        //counts how many empty lines exists before finding a word
        while (Character.isWhitespace(character) && eofValue != -1) {
            if (character == '\n') {
                emptyLines++;
            }
            eofValue = reader.read();
            if(eofValue != -1)
                character = (char) eofValue;
        }

        //checks if is the end of the stream and if
        //      1) the file ends with a single letter, surrounded by whitespaces
        //      2) the file ends in only white spaces
        if (eofValue == -1) {
            if (!Character.isWhitespace(character)) {
                word.append(character);
                return 2;
            }
            //from previous character
            else if (word.length() > 0) {
                return 2;
            }
            else {
                return -1;
            }
        }

        //checks if there are 2 or more "\n", which means that we should have a empty lines between paragraphs
        if (emptyLines >= 2) {
            word.delete(0, word.length());
            word.append('\n');
            if (character != '\n') {
                previousChar = character; // we store the last character if it's a letter
            }
            else {
                previousChar = '\0'; // we make sure not to count the last '\n' next time
            }
            return 1;
        }

        //read all the not white characters and put them in the word
        while (!Character.isWhitespace(character) && eofValue != -1) {
            word.append(character);
            eofValue = reader.read();
            if (eofValue != -1)
                character = (char) eofValue;
        }

        //checks if we are at the end of file, if so we make sure not to lose the last letter
        if (eofValue == -1) {
            return 2;
        }
        previousChar = character; // we store the last read character, to check next time if it's a '\n' or not
        return 0;
    }
}
