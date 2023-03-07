using System;
using System.Collections.Generic;
using System.Data;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace NezarkaBookstore
{
    public class Controller
    {
        ModelStore store;
        CommandReader cmdReader;
        Printer printer;
        bool execute;

        public Controller(TextReader reader, TextWriter writer)
        {
            
            store = ModelStore.LoadFrom(reader);
            if (store == null)
            {
                writer.WriteLine("Data error.");
                execute = false;
            }
            else
                execute = true;          
            cmdReader = new CommandReader(reader);
            printer = new Printer(writer);
        }

        private bool checkBookId(int id)
        {
            var value = store.GetBook(id);
            return value != null;
        }

        private bool checkCustId(int id)
        {
            var value = store.GetCustomer(id);
            return value != null;
        }

        public void executeCommands()
        {
            if (execute)
            {
                var cmd = cmdReader.readCommand();
                while(cmd.Item1)
                {
                    int custId = cmd.Item2;
                    int bookId = cmd.Item3;
                    int casE = cmd.Item4;
                    cmd = cmdReader.readCommand();
                    if (casE == -1)
                    {
                        printer.printHTML(htmlInvalidRequest());
                        continue;
                    }
                    if (!checkCustId(custId))
                    {
                        printer.printHTML(htmlInvalidRequest());
                        continue;
                    }
                    if (bookId != -1)
                    {
                        if (!checkBookId(bookId))
                        {
                            printer.printHTML(htmlInvalidRequest());
                            continue;
                        }
                    }
                    if (casE == 1)
                    {
                        printer.printHTML(htmlTableOfBooks(custId));
                    }
                    else if (casE == 2)
                    {
                        if (!checkBookId(bookId))
                        {
                            printer.printHTML(htmlInvalidRequest());
                            continue;
                        }
                        printer.printHTML(htmlBookDetails(custId, bookId));
                    }
                    else if (casE == 3)
                    {
                        printer.printHTML(htmlShopCart(custId));
                    }
                    else if (casE == 4)
                    {
                        store.GetCustomer(custId).ShoppingCart.add(bookId);
                        printer.printHTML(htmlShopCart(custId));
                    }
                    else if (casE == 5)
                    {
                        if (store.GetCustomer(custId).ShoppingCart.remove(bookId))
                            printer.printHTML(htmlShopCart(custId));
                        else
                            printer.printHTML(htmlInvalidRequest());
                    }
                    
                }
            }
        }

        private List<StringBuilder> headConstructor()
        {
            

            List<StringBuilder> head = new List<StringBuilder>();
            head.Add(OpenCommand.constructCommand(0, "meta", "charset=\"utf-8\" /"));
            
            var l1 = ParaCnstr.makePara("title", true, false, "Nezarka.net: Online Shopping for Books");
            
            var context = ParaCnstr.joinLists(new List<StringBuilder>[] { head, l1 });
            return ParaCnstr.makePara("head", false, true, context);

        }

        private List<StringBuilder> style()
        {
            List<StringBuilder> commands = new List<StringBuilder>();
            commands.Add(CommandConstructor.constructCommand(0, "table, th, td {"));
            commands.Add(CommandConstructor.constructCommand(1, "border: 1px solid black;"));
            commands.Add(CommandConstructor.constructCommand(1, "border-collapse: collapse;"));
            commands.Add(CommandConstructor.constructCommand(0, "}"));
            commands.Add(CommandConstructor.constructCommand(0, "table {"));
            commands.Add(CommandConstructor.constructCommand(1, "margin-bottom: 10px;"));
            commands.Add(CommandConstructor.constructCommand(0, "}"));
            commands.Add(CommandConstructor.constructCommand(0, "pre {"));
            commands.Add(CommandConstructor.constructCommand(1, "line-height: 70%;"));
            commands.Add(CommandConstructor.constructCommand(0, "}"));

            return ParaCnstr.makePara("style", false, true, commands, "type=\"text/css\"");

        }

        private List<StringBuilder> title(String title)
        {
            return ParaCnstr.makePara("h1", true, false,
                ParaCnstr.makePara("pre", true, false, title));
        }

        private static List<StringBuilder> menu(String customer, int items)
        {
            List<StringBuilder> menu = new List<StringBuilder>();

            menu.Add(CommandConstructor.constructCommand(0, customer + ", here is your menu:"));
            var d1 = ParaCnstr.makePara("td", true, false,
                ParaCnstr.makePara("a", true, false, "Books", "href=\"/Books\""));

            var d2 = ParaCnstr.makePara("td", true, false,
                ParaCnstr.makePara("a", true, false, "Cart (" + items.ToString() + ")", "href=\"/ShoppingCart\""));

            var row = ParaCnstr.makePara("tr", false, true,
                ParaCnstr.joinLists(new List<StringBuilder>[] { d1, d2 }));

            var table = ParaCnstr.makePara("table", false, true, row);

            return ParaCnstr.joinLists(new List<StringBuilder>[] { menu, table });

        }

        private List<StringBuilder> bookForTable(Book b)
        { 
            return new List<StringBuilder>()
            {
                ParaCnstr.makePara("a", true, false, b.Title, "href=\"/Books/Detail/" + b.Id.ToString() + "\"")[0].Append(OpenCommand.constructCommand(0, "br", "/")),
                CommandConstructor.constructCommand(0, "Author: " + b.Author + OpenCommand.constructCommand(0, "br", "/")),
                CommandConstructor.constructCommand(0, "Price: " + b.Price.ToString() + " EUR &lt;" +
                    ParaCnstr.makePara("a", true, false, "Buy", "href=\"/ShoppingCart/Add/" + b.Id.ToString() + "\"")[0] + "&gt;")
            };
        }

        private List<StringBuilder> tableOfBooks()
        {
            var books = store.GetBooks();
            int size = books.Count;
            var rows = new List<List<StringBuilder>>();
            for (int i = 0; i < size - size%3; i++)
            { 
                rows.Add(ParaCnstr.makePara("tr", false, true,
                    ParaCnstr.joinLists(new List<StringBuilder>[] {
                        ParaCnstr.makePara("td", false, true, 
                            bookForTable(books[i]), "style=\"padding: 10px;\""),
                        ParaCnstr.makePara("td", false, true, bookForTable(books[i+1]), "style=\"padding: 10px;\""),
                        ParaCnstr.makePara("td", false, true, bookForTable(books[i+2]), "style=\"padding: 10px;\"") })));
                i = i + 2;      
            }
            var datas = new List<List<StringBuilder>>();
            for (int i = size - size % 3; i < size; i++ )
            {
                datas.Add(ParaCnstr.makePara("td", false, true, bookForTable(books[i]), "style=\"padding: 10px;\""));
            }
            if (datas.Count > 0)
            {
                for (int i = 1; i < datas.Count; i++)
                {
                    ParaCnstr.joinLists(new List<StringBuilder>[] { datas[0], datas[i] });
                }
                rows.Add(ParaCnstr.makePara("tr", false, true, datas[0]));
            }
            for (int i = 1; i < rows.Count; i++)
            {
                ParaCnstr.joinLists(new List<StringBuilder>[] { rows[0], rows[i] });
            }
            
            if (rows.Count > 0)
                return ParaCnstr.makePara("table", false, true, rows[0]);
            else
                return ParaCnstr.makePara("table", false, true, "");
            
        }

        private List<StringBuilder> bodyTableOfBooks(int customerID)
        {
            int cartSize = store.GetCustomer(customerID).ShoppingCart.size();
            return
                ParaCnstr.makePara("body", false, true, ParaCnstr.joinLists(new List<StringBuilder>[]
                {
                    style(),
                    title("  v,<br />Nezarka.NET: Online Shopping for Books"),
                    menu(store.GetCustomer(customerID).FirstName, cartSize),
                    new List<StringBuilder>() { CommandConstructor.constructCommand(0, "Our books for you:") },
                    tableOfBooks()

                }));        
        }

        public List<StringBuilder> htmlTableOfBooks(int customerID)
        {
            var allLines = new List<StringBuilder>();
            var docType = CommandConstructor.constructCommand(0, "<!DOCTYPE html>");
            allLines.Add(docType);
            return ParaCnstr.joinLists(new List<StringBuilder>[]
            {
                allLines,
                ParaCnstr.makePara("html", false, false, ParaCnstr.joinLists( new List<StringBuilder>[]
                {
                    headConstructor(),
                    bodyTableOfBooks(customerID)
                }), "lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\"")
            });
        }

        private List<StringBuilder> bodyInvalidRequest()
        {
            return ParaCnstr.makePara("body", false, false,
                ParaCnstr.makePara("p", true, false, "Invalid request."));
        }

        public List<StringBuilder> htmlInvalidRequest()
        {
            var allLines = new List<StringBuilder>();
            var docType = CommandConstructor.constructCommand(0, "<!DOCTYPE html>");
            allLines.Add(docType);
            return ParaCnstr.joinLists(new List<StringBuilder>[]
            {
                allLines,
                ParaCnstr.makePara("html", false, false, ParaCnstr.joinLists( new List<StringBuilder>[]
                {
                    headConstructor(),
                    bodyInvalidRequest()
                }), "lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\"")
            });
        }

        private List<StringBuilder> bookDetails(int bookID)
        {
            var book = store.GetBook(bookID);
            return ParaCnstr.joinLists(new List<StringBuilder>[]
            {
                ParaCnstr.makePara("h2", true, false, book.Title),
                ParaCnstr.makePara("p", false, false, new List<StringBuilder>()
                {
                    CommandConstructor.constructCommand(0, "Author: " + book.Author).Append(OpenCommand.constructCommand(0, "br", "/")),
                    CommandConstructor.constructCommand(0, "Price: " + book.Price.ToString() + " EUR").Append(OpenCommand.constructCommand(0, "br", "/")),
                }, "style=\"margin-left: 20px\""),
                ParaCnstr.makePara("h3", true, false, "&lt;" +
                    ParaCnstr.makePara("a", true, false, "Buy this book", "href=\"/ShoppingCart/Add/" + bookID.ToString() + "\"")[0] + "&gt;")
            });
        }

        private List<StringBuilder> bodyBookDetail(int customerID, int bookID)
        {
            int cartSize = store.GetCustomer(customerID).ShoppingCart.size();
            return
                ParaCnstr.makePara("body", false, true, ParaCnstr.joinLists(new List<StringBuilder>[]
                {
                    style(),
                    title("  v,<br />Nezarka.NET: Online Shopping for Books"),
                    menu(store.GetCustomer(customerID).FirstName, cartSize),
                    new List<StringBuilder>() { CommandConstructor.constructCommand(0, "Book details:") },
                    bookDetails(bookID)

                }));
        }

        public List<StringBuilder> htmlBookDetails(int customerID, int bookID)
        {
            var allLines = new List<StringBuilder>();
            var docType = CommandConstructor.constructCommand(0, "<!DOCTYPE html>");
            allLines.Add(docType);
            return ParaCnstr.joinLists(new List<StringBuilder>[]
            {
                allLines,
                ParaCnstr.makePara("html", false, false, ParaCnstr.joinLists( new List<StringBuilder>[]
                {
                    headConstructor(),
                    bodyBookDetail(customerID, bookID)
                }), "lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\"")
            });
        }

        private List<StringBuilder> tblHeadCart()
        {
            return ParaCnstr.makePara("tr", false, true, ParaCnstr.joinLists(new List<StringBuilder>[]
                    {
                        ParaCnstr.makePara("th", true, false, "Title"),
                        ParaCnstr.makePara("th", true, false, "Count"),
                        ParaCnstr.makePara("th", true, false, "Price"),
                        ParaCnstr.makePara("th", true, false, "Actions"),
            }));
        }

        private List<StringBuilder> rowCart(ShoppingCartItem item)
        {
            String price;
            decimal bPrice = store.GetBook(item.BookId).Price;
            if (item.Count == 1)
            {
                price = bPrice.ToString() + " EUR";
            }
            else
                price = item.Count.ToString() + " * " + bPrice.ToString() + " = " + (bPrice * item.Count).ToString() + " EUR";
            return ParaCnstr.makePara("tr", false, true, ParaCnstr.joinLists(new List<StringBuilder>[]
            {
                ParaCnstr.makePara("td", true, false,
                    ParaCnstr.makePara("a", true, false, store.GetBook(item.BookId).Title, "href=\"/Books/Detail/" + store.GetBook(item.BookId).Id.ToString() + "\"")),
                ParaCnstr.makePara("td", true, false, item.Count.ToString()),
                ParaCnstr.makePara("td", true, false, price),
                ParaCnstr.makePara("td", true, false, "&lt;" +
                    ParaCnstr.makePara("a", true, false, "Remove", "href=\"/ShoppingCart/Remove/" + item.BookId.ToString() + "\"")[0]
                    + "&gt;")
            }));
        }

        private List<StringBuilder> tableShopCart(ShoppingCart cart)
        {
            var items = cart.getItems();
            if (items.Count == 0)
                return new List<StringBuilder> { CommandConstructor.constructCommand(0, "Your shopping cart is EMPTY.") };

            decimal price = 0;


            var rows = new List<List<StringBuilder>>();
            rows.Add(tblHeadCart());
            foreach (var item in items)
            {
                rows.Add(rowCart(item));
                price += store.GetBook(item.BookId).Price * item.Count;
            }

            for (int i = 1; i < rows.Count; i++)
            {
                ParaCnstr.joinLists(new List<StringBuilder>[] { rows[0], rows[i] });
            }

            return ParaCnstr.joinLists(new List<StringBuilder>[] {
                new List<StringBuilder>() { CommandConstructor.constructCommand(0, "Your shopping cart:") },
                ParaCnstr.makePara("table", false, true, rows[0]),
                new List<StringBuilder>() { new StringBuilder("Total price of all items: " + price.ToString() + " EUR") } });

            
        }

        private List<StringBuilder> bodyShopCart(int customerID)
        {
            int cartSize = store.GetCustomer(customerID).ShoppingCart.size();
            return
                ParaCnstr.makePara("body", false, true, ParaCnstr.joinLists(new List<StringBuilder>[]
                {
                    style(),
                    title("  v,<br />Nezarka.NET: Online Shopping for Books"),
                    menu(store.GetCustomer(customerID).FirstName, cartSize),
                    tableShopCart(store.GetCustomer(customerID).ShoppingCart)

                }));
        }

        public List<StringBuilder> htmlShopCart(int customerID)
        {
            var allLines = new List<StringBuilder>();
            var docType = CommandConstructor.constructCommand(0, "<!DOCTYPE html>");
            allLines.Add(docType);
            return ParaCnstr.joinLists(new List<StringBuilder>[]
            {
                allLines,
                ParaCnstr.makePara("html", false, false, ParaCnstr.joinLists( new List<StringBuilder>[]
                {
                    headConstructor(),
                    bodyShopCart(customerID)
                }), "lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\"")
            });
        }


    }
}
