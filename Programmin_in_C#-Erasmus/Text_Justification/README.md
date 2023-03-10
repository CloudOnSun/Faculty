# Text Justification assignment
The goal is to implement a program capable of justifying text paragraphs. The program receives three command-line arguments: the name of the input file, the name of the output file and a number representing the maximum text width. The output file should contain all text from the input file formatted in such a way that each line has maximum width, if possible.

In case of invalid number of command-line arguments, or if the third argument is not a valid number greater than 0, the program should print the following string to standard output: "Argument Error". In case of problems encountered when opening (the file does not exist, invalid filename, insufficient access rights, etc.) or reading the files, the program should output the following string: "File Error"

Following rules must be observed when preparing and formatting output:

In the context of this task, line breaks, tabulators and spaces (i.e. '\n', '\t' and ' ') are considered white-space characters, all other characters are considered printable. You can assume that the character '\r' will not appear anywhere in the text.
A word is defined in this context as any sequence of non-whitespace characters that is surrounded by at least one whitespace character (or the beginning/end of the file) on both sides. Only the white-space characters can be changed (added, removed), all words must appear in the output in the exact same form as they appear in the input file.
An empty line, a line containing only white-space characters, or a sequence of such lines are interpreted as paragraph delimiters. Each paragraph is formatted separately, and the output paragraphs are always delimited by a single empty line. The last line of the last paragraph must be terminated with a line break, but with no empty line following.
A paragraph should be formatted in such a way that each line contains as many words as possible (while maintaining their order) without exceeding the maximum text width. All words must be separated by at least one space. If some there is still some empty space remaining to achieve the maximum text width, this space is uniformly distributed among the word gaps by adding space characters. If the extra spaces cannot be distributed uniformly, they should be added from the left, i.e. wider gaps should be to the left of the narrower ones. The final line of each paragraph should be aligned to the left, i.e. all words should be separated by exactly one space.
For every line of the output file there can be no white-space characters between the last character of the last word and the line break character.
If the input text contains a word with more characters than the maximum text width, this word should be printed out by itself on a single line. If a line contains only one word, this word should be aligned to the left.
Note that the rules must be strictly followed, as the output of your implementation is compared to the correct solution character by character. Also do not make any assumptions about the size of the input. Neither the input nor a single line thereof are guaranteed to fit in the memory.

Example 1:

$>program.exe plain.txt format.txt 17
Input file plain.txt

If a train station is where the train stops, what is a work station?
Output file format.txt

If     a    train
station  is where
the  train stops,
what  is  a  work
station?
Example 2:

$>program.exe plain.txt format.txt abc
Std. output

Argument Error
Example 3:
If you need a longer text to test the correctness of your implementation, it is allowed (and recommended) to use a Lorem ipsum text generator, such as the following one, or any other one found via googling: http://generator.lorem-ipsum.info/. An example Lorem ipsum file can be downloaded here: LoremIpsum.txt (Unix-style line breaks, i.e. a single "\n" character). A corresponding output for a 40 character text width can be downloaded here: LoremIpsum_Aligned.txt (Unix-style line breaks).

Caution: If you run your solution in a Windows environment, property Environment.NewLine contains the Windows-style line breaks, i.e. the "\r\n" dual character sequence. Therefore, all environment-related output methods (such as TextWriter.WriteLine) will also produce Windows-style line breaks. The same goes for the Lorem ipsum generator referenced above.

Hint: If you need to convert a file with Windows-style line breaks to Unix- style and vice versa, it is possible to do so e.g. via the Visual Studio editor. After opening a file in VS (the file should be displayed correctly, as VS detects which line break style is used, assuming the style is consistent in the whole file), select File - Save * as..., click the arrow next to the Save button in the dialog, choose Save with Encoding... and then set the target line break style in the Line endings property.

