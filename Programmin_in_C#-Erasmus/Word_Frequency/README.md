# Word Frequency assignment
The goal is to implement a program capable of calculating the word frequencies of a provided file. The name of the file is given in the form of a single command-line argument. The calculated frequency count for each unique word contained in the input file should be written to standard output.

In case of invalid number of command-line arguments, the program should print the following string to standard output: "Argument Error". In case of problems encountered when opening (e.g. the file does not exist, insufficient access rights) or reading the file, the program should output the following string: "File Error"

Following rules must be observed when processing input and preparing output:

In the context of this task, line breaks, tabulators and spaces (i.e. '\n', '\t' and ' ') are considered white-space characters, all other characters are considered printable. You can assume that the character '\r' will not appear anywhere in the text.

A word is defined in this context as any sequence of non-whitespace characters that is surrounded by at least one whitespace character (or the beginning/end of the file) on both sides. Only the white-space characters can be changed, all words must appear in the output in the exact same form as they appear in the input file.

A single line containing the frequency should be printed to the standard output for each unique word in the input file. Note that casing matters, so e.g. world and World should be considered different words in this context, and the output should therefore contain a frequency line for each of them.

The output frequency lines have the following format:
word: frequency 
Where word is the word this line corresponds to and frequency is an integer representing the number of occurences for this word. It is safe to assume that no word in the input file will occur more than 1 billion times [1 000 000 000].

The output frequencies should be sorted according to the word they correspond to. Common .NET methods used for comparing two strings (e.g. .CompareTo string instance method) and methods that are using them should produce the correct ordering for this assignment.

Note that it is necessary to observe the provided rules carefully, as the output of the program will be compared with the correct output character-by- character. For n representing the length of the longest line in the input file, and m representing the sum of lengths of all unique words in the file (both in terms of character counts), it can be assumed that the available memory is O(n + m).

Example 1:

$>program.exe plain.txt
Input file plain.txt

If a train station is where the train stops, what is a work station?
std. output (see counts.txt)

a: 2
If: 1
is: 2
station: 1
station?: 1
stops,: 1
the: 1
train: 2
what: 1
where: 1
work: 1
Example 2:

$>program.exe
std. output

Argument Error
