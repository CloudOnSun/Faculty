# Expression Evaluation 1 assignment

The goal is write a program capable of parsing and evaluating an arithmetic expression, intended as a basis of an arithmetic expression library. The application should read the expression from a single line of the standard input, textually represented in preorder notation. The program should then evaluate the expression and print the result to standard output. The expression can contain integer constants that fit in a 32-bit signed integer (i.e. are smaller than 231 = 2147483648), as well as binary operators +, -, * a / and the unary operator ~ representing additive inverse - negation. Operators and numbers are separated with spaces. The result should be printed out as a decimal integer.

Use exclusively integer arithmetic when evaluating the expression. All subresults should fit in a 32-bit signed integer. If an overflow was encountered when evaluating the expression (i.e. subresult will not fit in int), the program should print the following string as a result: "Overflow Error". If division by zero was encountered, the following string should be printed: "Divide Error". And finally, if the textual representation of the expression is invalid for any reason (unknown tokens, not in preorder notation, etc.), the program should print the following string: "Format Error".

The expressions being evaluated will be relatively small, so there is no need to particularly focus on performance. The emphasis will instead be on the quality of object oriented design and code reusability. As noted above, you should anticipate that the code you write will be used as the core of an expression library, and the users of such a library will typically perform many different operations on a single expression (repeated evaluation, simplifying the expression, etc.). A solution of very high quality will net you some additional points, as well as aid you in completing the subsequent assignments.

Examples:

Input: + ~ 1 3
Output: 2

Input: / + - 5 2 * 2 + 3 3 ~ 2
Output: -7

Input: - - 2000000000 2100000000 2100000000
Output: Overflow Error

Input: / 100 - + 10 10 20
Output: Divide Error

Input: + 1 2 3
Output: Format Error

Input: - 2000000000 4000000000
Output: Format Error

Note: the final example results in a Format Error, because the number 4000000000 will not be encountered as a subresult during evaluation, but is instead present in the textual input.
