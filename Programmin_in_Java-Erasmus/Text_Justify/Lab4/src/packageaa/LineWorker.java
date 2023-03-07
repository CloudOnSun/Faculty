package packageaa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

public class LineWorker {

    WordReader wordReader;
    PrintStream printer;
    BufferedReader reader;
    int lineLength;
    StringBuilder b1;
    StringBuilder b2;

    final StringBuilder endLine = new StringBuilder("\n");

    Queue<String> readWords = new ArrayDeque<>();

    public LineWorker(PrintStream printer, BufferedReader reader, int lineLength) {
        this.lineLength = lineLength;
        this.printer = printer;
        this.reader = reader;
        wordReader = new WordReader(reader);
    }

    /** writes the line with the given format of whiteSpaces
     * (param) int whiteSpaces: the lowest number of needed spaces between every word
     * (param) int irregularWhiteSpaces: the last non-regular white spaces needed to fill the line
     */
    private void writeIndentated(int whiteSpaces, int irregularWhiteSpaces) {
        int length = readWords.size();
        if (length > 0) {
            StringBuilder buffer = new StringBuilder();
            for (int i = 1; i < length; i++) {
                buffer.append(readWords.remove());
                buffer.append(" ".repeat(Math.max(0, whiteSpaces)));
                if (irregularWhiteSpaces > 0) {
                    buffer.append(" ");
                    irregularWhiteSpaces--;
                }
            }
            buffer.append(readWords.remove());
            printer.print(buffer);
        }
        printer.print("\n");
        readWords.clear(); // we clear the queue
    }

    /**
     * calculates the needed spaces between every word and calls writeIndentated function to write the line
     */
    private void calculateSpacesAndWriteIndentated(int length) {
        int missingWhiteSpaces = lineLength - length;
        int nrOfSpaces = readWords.size() - 1;
        int whiteSpaces;
        int irregularWhiteSpaces;
        if (nrOfSpaces == 0) {
            whiteSpaces = 0;
            irregularWhiteSpaces = 0;
        }
        else {
            whiteSpaces = 1 + (missingWhiteSpaces / nrOfSpaces);
            irregularWhiteSpaces = missingWhiteSpaces % nrOfSpaces;
        }
        writeIndentated(whiteSpaces, irregularWhiteSpaces);
    }

    private boolean endOfFileWithWord(int value) {
        return value == 2;
    }

    private boolean justWordInMiddle(int value) {
        return value == 0;
    }

    private boolean needOfEmptyLine(int value) {
        return value == 1;
    }

    private boolean endOfFile(int value) {
        return value == -1;
    }

    /**
     * if exists, adds the previous words to readWord
     *            returns the length of the entire words
     * if not,    returns 0
     */
    private int sizePreviousWords() {
        int count = readWords.size();
        int length = 0;
        for (int i = 0; i < count; i++) {
            String word = readWords.remove();
            readWords.add(word);
            length += word.length() + 1;
        }
        return length;
    }

    /**
     * verifies if a word is too big to be printed on a normal line and there are no words waiting to be written
     *
     * returns: -1 if word isn't to big
     *          0  if it is, but it's not end of file
     *          1  if it is, and it's end of file
     */
    private int check_PrintNextIsTooBig(int value) {

        if (readWords.size() == 1) {
            String word = readWords.remove();
            readWords.add(word);

            if (word.length() > lineLength) {
                writeIndentated(1, 0);
                if (endOfFileWithWord(value))
                    return 1;
                return 0;
            }
        }
        return -1;
    }

    /** we add the maximum amount of words in queue readWords
     * length becomes the length of all words + 1 space between each of them
     * word is the last read word which didn't fit in the line
     * value is the returned value when we read tha last word
     *
     * it can write the words if:
     *      1: there was a need of empty line: writes the read words a single space between them and the empty line
     *
     * returns: 0 if case 1)
     *          1 if case end of file
     *          -1 otherwise
     */
    private int[] processWords(int length, StringBuilder word, int value) throws IOException {

        value = wordReader.readWord(word);

        // we test if we can add the next word to the line;
        // if need of empty file, word(\n) has length=1, but we will never add it we need to test it with one space lower
        while (length + word.length() + 1 <= lineLength + 1 ||
                (word.compareTo(endLine) == 0 && length + word.length() <= lineLength + 1)) {

            if (needOfEmptyLine(value)) {
                if (readWords.size() > 0)
                    writeIndentated(1, 0); // write the last remaining words
                writeIndentated(1, 0); //write just an empty line
                return new int[]{0, length, value};
            }

            // we read a word, we add it to be processed now or for the next file
            if (endOfFileWithWord(value) || justWordInMiddle(value)) {
                readWords.add(word.toString());
            }

            // end of file
            if(endOfFile(value) || endOfFileWithWord(value)){
                return new int[]{1, length, value};
            }
            length += word.length() + 1;
            value = wordReader.readWord(word);
        }
        return new int[]{-1, length, value};
    }


    /* writes a line in the specific way
     * returns: 1 if it's end of file
     *          0 otherwise
     */
    private int writeLine() throws IOException {

        int length = 0;
        length += sizePreviousWords();

        StringBuilder lastWord = new StringBuilder();
        int valueWord = -2;
        int[] returnedValues = processWords(length, lastWord, valueWord);
        int returnedValue = returnedValues[0];
        length = returnedValues[1];
        valueWord = returnedValues[2];

        // if it's end of file, or we already wrote an empty line
        if (returnedValue != -1)
            return returnedValue;

        // only if we have something to be printed (could happen that we read a word too big and nothing was added)
        if (length > 0) {
            length--;
            calculateSpacesAndWriteIndentated(length);
        }

        //if we have a perfect amount of words (divided by only 1 space) and the next character needed is a blank line
        //  a old implementation (might be not needed because case needOfEmptyLine from processWord method
        if (needOfEmptyLine(valueWord)) {
            writeIndentated(1, 0);
        }

        if (lastWord.compareTo(endLine) != 0 && lastWord.length() > 0)
            readWords.add(lastWord.toString());

        //a word that is too big for a normal line
        int valueTooBig = check_PrintNextIsTooBig(valueWord);
        if (valueTooBig != -1)
            return valueTooBig;

        //we reached the end of file
        if (endOfFileWithWord(valueWord) || endOfFile(valueWord)) {
            return 1;
        }

        return 0;
    }

    public void writeAll() throws IOException {
        while (writeLine() != 1) {
            continue;
        }

        // we write the last words that exists in readWords
        if (readWords.size() > 0)
            writeIndentated(1, 0);
    }


}


