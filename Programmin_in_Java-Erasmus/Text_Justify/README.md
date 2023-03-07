# Text Justify

The goal is to create a program that justifies a text to the block with a given number of characters per line, i.e., each line has exactly the given number of characters.

The text is read from the standard input and printed out to the standard output. First line of the input contains the given length of the line (i.e., the number of characters per line); this line is not copied to the output. If first line does not contain a number, the program prints out the string Error and finishes.

There are the following rules how to format the output. The words are separated by white spaces. For a white space detection use the Character.isWhitespace(char ch) method. The empty line (or several empty lines or a line with white spaces only) makes a paragraph separator. In the output, paragraphs are separated by a single empty line. There is at least one space between words. If a particular line would be shorter than the given length, the spaces are filled equally between the words. If the spaces cannot be filled equally, the additional spaces are added one by one from left. The last line of a paragraph is justified to left, i.e., there is exactly a single space between words and the line has maximally the given length. If there is a word longer than the given length of the line, then the word is printed on its own line and overruns the given length. If there is only one word on a line, then it is justified to left.

The whole input may not fit to the memory. A single output line always fits to the memory.
