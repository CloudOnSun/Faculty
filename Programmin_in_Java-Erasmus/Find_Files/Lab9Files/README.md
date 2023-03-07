# UNIX Program Find

The goal is to implement a program similar to the Unix program FIND, i.e., a program that based on its command line parameters searches files matching the given parameters and prints out their filenames.

The first parameter is always a path to the directory where the files are searched for; other parameters are constraints for searching. Always the whole directory subtree is searched.

The number of constraints is arbitrary, and all of these constraints have to be always satisfied. Individual constraints are given as a word starting with the dash and possibly an additional parameter.

The program prints out names of all matching files to the standard output with their entire path relative to the specified path (i.e., if, for example, the path /home/petr/java is searched, and a found file has an absolute path /home/petr/java/cz/cuni/Hello.java, the program prints out cz/cuni/Hello.java). The order of the printed-out files is arbitrary.

List of constraints

-name mask (the file name has to match the given mask (a mask is in the common sense, i.e., a star represents an arbitrary number of arbitrary characters and a question-mark represents a single arbitrary character))

-iname mask (as -name, but case insensitive)

-regex regular_expression (the file name has to match the given regular expression)

-size size (the files has to be of exactly the specified size (possibilities to set the size: 5 => 5 bytes, 5k => 5 kilobytes, 5M => 5 megabytes))

-ssize size (the file has to be smaller than the given size)

-bsize size (the file has to be larger than the given size)

If some parameters are wrong, or there are unknown parameters, etc., the program prints out ERROR and terminates.

If the given path does not exist, the program does not print anything.

Example

java cz.cuni.mff.java.homework.jfind.Main /home/petr/java -name *.java -bsize 1M

in the directory /home/petr/java, the program searches all the files that end with .java and are larger than one megabyte
