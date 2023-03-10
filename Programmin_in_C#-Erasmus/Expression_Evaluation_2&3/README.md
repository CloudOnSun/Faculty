# Expression Evaluation 2 and 3 assignments

1
----

The goal is write a program capable of serving as a simple expression calculator. The program processes commands from standard input and prints results (or error messages) to standard output. There is only one command per line. Commands are processed sequentially; the previous command is fully executed before the next one starts loading. The calculator should terminate after it processes all input data (attempting to read a line results in null), or when it encounters a line containing only the string "end". The calculator recognizes the following commands:

A line starting with the "=" symbol followed by exactly one space and an expression in preorder format (see below). Such a line should be interpreted by parsing the expression and storing it; following operations will be done over the last parsed expression. If an expression was already stored, the previous expression should be discarded and replaced by the new expression. The previous expression will be discarded even if an error was encountered when processing the "=" command.
A single string "i" should be interpreted by evaluating the last expression using integer arithmetic and printing out the result (one integer) on a single output line.
A single string "d" should be interpreted by evaluating the last expression using double-precision floating-point arithmetic (64 bits) and printing out the result on a single output line using 5 decimal places.
The expression that follows the "=" symbol may contain positive integers that should fit in a 32-bit signed integer type (i.e. are smaller than 231 = 2147483648), binary operators +, -, * and /, as well as the unary minus operator ~ representing additive inverse. Operators and numbers are separated with spaces.

When evaluating using the integer arithmetic ("i" command), it is safe to assume that all subresults will fit in a 32-bit signed integer. If an overflow was encountered when evaluating the expression (i.e. subresult will not fit in a signed int), the program should print the following line as a result: "Overflow Error". If division by zero was encountered, the following string should be printed: "Divide Error".

Evaluation of the expression using floating-point arithmetic is done in accordance with standard IEEE rules, i.e. including special values such as "infinity", "not a number", etc. If any such value is the result of an expression evaluation, it should be printed out in the standard text representation provided by the .NET libraries.

If an invalid command is encountered, or if the textual representation of the expression is invalid for any reason (unknown tokens, not in preorder notation, etc.), the program should print the following line: "Format Error". If the commands "i" or "d" are encountered and no valid expression is loaded, the program should print "Expression Missing" on a single line. If an empty line is encountered when processing the input, the program should ignore it without printing out any error.

When reading from standard input and writing to standard output, it is safe to assume that your application will be run in CodEx with suitable culture and regional settings.

Note:

The marking process will put strong emphasis on the (object-oriented) application design. The purpose of the assignment is to allow you to gain experience with the Visitor design pattern. Correct use of the pattern will be rewarded with up to bonus 5 points.

Example (lines starting with "$>" represent input):

\**$>** i

Expression Missing

\**$>** $#!%

Format Error

\**$>** = + 2 3

\**$>** i

5
\**$>** d

5.00000

\**$>** = / 5 2

\**$>** i

2
\**$>** d

2.50000

\**$>** = +

Format Error

\**$>** i

Expression Missing

\**$>** end

2
----

The goal is write a program capable of serving as a simple expression calculator. The program processes commands from standard input and prints results (or error messages) to standard output. There is only one command per line (ignore empty lines). Commands are processed sequentially; the previous command is fully executed before the next one starts loading. The calculator should terminate after it processes all input data (attempting to read a line results in null), or when it encounters a line containing only the string "end". The calculator recognizes the following commands:

A line starting with the "=" symbol followed by exactly one space and an expression in preorder format (see below). Such a line should be interpreted by parsing the expression and storing it; following operations will be done over the last parsed expression. If an expression was already stored, the previous expression should be discarded and replaced by the new expression. The previous expression will be discarded even if an error was encountered when processing the "=" command.
A single string "i" should be interpreted by evaluating the last expression using integer arithmetic and printing out the result (one integer) on a single output line.
A single string "d" should be interpreted by evaluating the last expression using double-precision floating-point arithmetic (64 bits) and printing out the result on a single output line using 5 decimal places.
A single string "p" should be interpreted by printing out a line containing the last expression in infix notation, while explicitly denoting the operator priorities by enclosing each individual operator and its operands in parentheses (which implies that even the whole expression is contained in parentheses).
A single string "P" should be interpreted by printing out a line containing the last expression in infix notation, while using minimal number of parentheses. That is, parentheses will only be used to enclose those operations that would otherwise have lower priority than required for the infix representation to correspond with the loaded expression when evaluated using standard arithmetic rules. The highest operator priority is assigned to unary minus, followed by multiplication and division, with addition and subtraction having the lowest priority. We also assume left-associativity when evaluating the expression, i.e. a sequence of operators with the same priority is applied from left to right.
The expression that follows the "=" symbol may contain positive integers that should fit in a 32-bit signed integer type (i.e. are smaller than 231 = 2147483648), binary operators +, -, * and /, as well as the unary minus operator ~ representing additive inverse. Operators and numbers are separated with spaces.

When evaluating using the integer arithmetic ("i" command), it is safe to assume that all subresults will fit in a 32-bit signed integer. If an overflow was encountered when evaluating the expression (i.e. subresult will not fit in a signed int), the program should print the following line as a result: "Overflow Error". If division by zero was encountered, the following string should be printed: "Divide Error".

Evaluation of the expression using floating-point arithmetic is done in accordance with standard IEEE rules, i.e. including special values such as "infinity", "not a number", etc. If any such value is the result of an expression evaluation, it should be printed out in the standard text representation provided by the .NET libraries.

If an invalid command is encountered, or if the textual representation of the expression is invalid for any reason (unknown tokens, not in preorder notation, etc.), the program should print the following line: "Format Error". If the commands "i", "d", "p" or "P" are encountered and no valid expression is loaded, the program should print "Expression Missing" on a single line.

Use the same symbol for unary and binary minus when printing out the expressions: "-". Do not insert any additional characters in the infix expression representation (not even spaces).

When reading from standard input and writing to standard output, it is safe to assume that your application will be run in CodEx with suitable culture and regional settings.

Note:

The marking process will put strong emphasis on the (object-oriented) application design. The purpose of the assignment is to allow you to gain experience with the Visitor design pattern. Correct use of the pattern will be rewarded with up to 5 bonus points.

Example (lines starting with "$>" represent input):

\**$>** i

Expression Missing

\**$>** p

Expression Missing

\**$>** $#!%

Format Error

\**$>** = + 2 3

\**$>** i

5
\**$>** d

5.00000
\**$>** = / 5 2

\**$>** i

2

\**$>** d

2.50000

\**$>** = + 4 * 2 3

\**$>** p

(4+(2*3))

\**$>** P

4+2*3

\**$>** = + + 1 1 1

\**$>** p

((1+1)+1)

\**$>** P

1+1+1

\**$>** = +

Format Error

\**$>** i

Expression Missing

\**$>** end
