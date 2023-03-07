# Bookstore eShop
The goal is to implement the core of the Nežárka.NET online bookstore - your application will be used as the service backend on the webserver side. After starting, the application uses standard input to receive the textual representation of the e-shop data (essentially relational database entities) in the following format (each data record on a separate line, with its members separated by 1 semicolon; first element on each line denotes either the data record type, or the beginning/end of the input data):

DATA-BEGIN
BOOK;_BookId_;_BookTitle_;_BookAuthor_;_BookPrice_
CUSTOMER;_CustId_;_CustName_;_CustSurname_
CART-ITEM;_CustId_;_BookId_;_BookCount_
DATA-END
Note thal all data read by your application from the standard input are case- sensitive!

The input is ordered by data record type, with BOOK records followed by CUSTOMER records, and finally CART-ITEM records. An arbitrary amount of records of each type is allowed in the input (including 0).

The BOOK record represents information about a single book offered by the Nežárka.NET bookstore. The CUSTOMER record represents information about a single registered customer of the bookstore. Each customer is assigned exactly one virtual shopping cart. The CART-ITEM record represents information about a single item in the shopping cart of a particular customer (i.e. for each CustId-BookId tuple, at most one CART-ITEM record is present in the input). If the shopping cart of a particular customer is empty, no CART-ITEM record with CustId equal to the id of this customer is present in the input. BookCount represents the quantity of a specific book in the customer's shopping cart. It is safe to assume that O(n) memory is available, with n being the total number of data records in the input (i.e. the number of records represented in the application at any given moment) - which means it is assumed that all book, customer or shopping cart related data will be stored in memory.

BookId, CustId, BookCount, BookPrice are arbitrary non-negative integers (guaranteed to be within int type range)
BookTitle, BookAuthor, CustName, CustSurname can be any strings (including spaces) that do not contain a semicolon or a newline.
Neither the BookId nor the CustId values are guaranteed to form a continuous sequence.
If the input data contain any error (text where a number was expected, unknown keyword, etc.) the program should print out the following string to standard output and terminate:

Data error.
A basic data model representing the described entities in a C# program can be downloaded from here: NezarkaModel.cs
Use this code in your implementation. You can modify and extend the provided code (e.g. by including the business logic described in the following section).

----
After initializing and loading the data model, the application should read client requests (generated for example by their web-browsers) from the standard input, with a single request per line. An end of input should result in the closing of the application. The requests are processed one-by-one, the result of each should be printed in the form of HTML code to the standard output. The result of each request in the standard output is followed by a separate line containing four copies of the equality sign character (====).

Your Nežárka.NET implementation must support the following 5 types of commands:

GET _CustId_ http://www.nezarka.net/Books
GET _CustId_ http://www.nezarka.net/Books/Detail/_BookId_
GET _CustId_ http://www.nezarka.net/ShoppingCart
GET _CustId_ http://www.nezarka.net/ShoppingCart/Add/_BookId_
GET _CustId_ http://www.nezarka.net/ShoppingCart/Remove/_BookId_
The result of these requests should be one of the following HTML pages (for more info see the templates below):

BooksListing (template/example: 02-Books.html) - contains the common header; books are listed in a HTML table with 3 books per row (with the exception of the last row, which contains as many books aligned to the left as there are available); the order by which the books are presented corresponds to their ordering in the data model (i.e. the order in which they are returned from the GetBooks() implementation), with the next book being on the right, or in the following row if neccessary. If the list of books is empty, the output HTML document should contain only an empty HTML element <table>, with no <tr> and <td> tags inside:
...  
<table>  
</table>  
...
BookInfo (template/example: 03-BooksDetail.html) - contains the common header
CartContents (template/example: 04-ShoppingCart.html) - contains the common header (showing the cart state after executing the command, if any); if no items are in the cart, a notice is shown instead of the table (template/example: 05-ShoppingCart-Empty.html).
InvalidRequest (template/example: 09-InvalidRequest.html) - does NOT contain the common header (it is a valid HTML document, however)
The common header contains the first name of the current customer (specified by CustId) and a menu containing links to /Books and /ShoppingCart commands (this command should also show the number of items in the shopping cart of the current customer).
The commands carry the following meaning:

/Books - no data changes, shows BooksListing
/Books/Detail/_BookId_ - no data changes, shows BookInfo for the book associated with BookId
/ShoppingCart - no data changes, shows CartContents for the current customer (specified by CustId)
/ShoppingCart/Add/_BookId_ - adds 1 piece of the book associated with BookId to the shopping cart of the current customer (specified by CustId); if the book is already in the cart, its quantity is increased by one; shows CartContents
/ShoppingCart/Remove/_BookId_ - removes 1 piece of the book associated with BookId from the shopping cart (decreasing its quantity by 1) of the current customer (specified by CustId); if only a single copy of the book was present, the entire record is removed; shows CartContents
If any aspect of the request is invalid (e.g. a different command than GET, wrong request format, invalid customer number, invalid book number, removing a book not present in the cart, etc.), InvalidRequest should be shown.

CAUTION: The formatting of the generated HTML files must exactly correspond with the templates provided above.

The following Example.zip archive contains an example of the input (NezarkaTest.in) and the corresponding output (NezarkaTest.out). For easier understanding and testing the archive also contains the files 01.html ... 11.html, which contain the NezarkaTest.out output cut by the results of the individual requests (the ==== separator has been removed). Notice: These are only for testing, you should not generate such files; all output of your application should be written sequentially to standard output in the format used in NezarkaTest.out.

HINT: When designing your solution, DO consider the possibility of applying the Model-View-Controller (MVC) architectural pattern.

----
Q: How should I represent and print HTML code in a C# program?

A: Probably the easiest and most readable option is to have a single .WriteLine() call on suitable "writer" per one output line. For example, if we should implement an application that always prints an HTML document with current date and time using the following template/example (TimeServiceExample.out.html):

<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8" />
    <title>Nezarka.NET: Also a Time Service!</title>
</head>
<body>
    <style type="text/css">
        pre {
            line-height: 70%;
        }
    </style>
    <h1><pre>  v,<br />Nezarka.NET: Also a Time Service!</pre></h1>
    It is 21. 10. 2014 16:20:22 on our server.<br />
    Enjoy!
</body>
</html>
