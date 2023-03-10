# Multifile Text Justification
This exercise is an extension of previous task “Text Justification”. All of the former instructions still apply, below are noted only the differences.

The main goal of the extension is to enable processing of an arbitrary number of input files. Your implementation should manage at least 65535 input files. The data for individual files should be processed sequentially in the same order as the file names given on command line. Therefore a first file should be processed first completely, then second, ... , and nth as the last one.

One thing to note is that the aligned output (as in the task “Text Justification” i.e. separated paragraphs, each line aligned to block, the last one of each paragraph to left) should be just one and should be (almost) identical to a an output of the previous task if we concatenated all of the N input files to one. Where by concatenation we mean literal byte concatenation without inserting any new characters. The only exception to this rule is that the boundary of files serves as not-written word separator. Therefore one word can’t start in one file and end in another. Should that be the case the word is to be treated as two words cut by the file boundary.

The solution for this task must expect that the input files are being build in parallel. What it means is that while processing i-th file the solution can’t expect files i+1, i+2, … to be neither complete nor even present on the filesystem yet. After finishing i-th file in its entirety (processing all of its words), however, the solution can immediately start working on i+1th file. If such file doesn't exist, can’t be opened, … it should be treated as an empty file (file with the size of 0 bytes). The solution should, therefore, start processing i+2th file. And so on.

Therefore names of only non-existent or invalid files is a valid input (in contrast to “Text Justification” where it was an error that should’ve ended with “File Error” output). The output for such input would be one file with one empty line. I.e. the file would contain only the line-end characters specific for current operating system.

Generally the command line invocation looks following: Batch.exe arg0 arg1 arg2 … argM

where the number of arguments on command line is always at least three. Otherwise “Argument Error” should be outputted. The last argument argM is always an integer representing the width of alignment, the same way as in “Zarovnání do bloku”. Argument argM-1 is always the name of output file. Arg0 - argmM-2 are the names of input files 1 to N - with one exception noted below.

If the first argument (arg0) is equal to "--highlight-spaces" then we want to enable a function similar to “highlight whitespace” in MS Word. Its specification is following: Instead of each space, that would normally be outputted, print single character “.” (i.e one dot without the brackets). In front each new line (sequence of characters representing 1 new line) add two new characters “smaller than” and “dash” “<-”

Below are examples of program usage (name xx.in is a non-existent file) using different versions of command line invocation:

Batch.exe --highlight-spaces 01.in ex01.out 17


Batch.exe --highlight-spaces 01.in 01.in 01.in ex02.out 17


Batch.exe --highlight-spaces xx.in xx.in xx.in 01.in xx.in xx.in ex08.out 80


Batch.exe 01.in 01.in 01.in ex12.out 17
