/* Purpose: Gets book_name, author, ISBN, price information based on value book_name */
select book_name, author, isbn, price " + "from book " + "where book_name ~* ?;
/* Purpose: Gets book_name, author, ISBN, price information based on value of author */
select book_name, author, isbn, price " + "from book " + "where author = ?;
/* Purpose: Gets book_name, author, ISBN, price information based on value of ISBN */
select book_name, author, isbn, price " + "from book " + "where ISBN = ?;
/* Purpose: Gets book_name, author, ISBN, price information based on value of genre */
select book_name, author, isbn, price " + "from book " + "where genre = ?;
/* Purpose: Gets book_name, author, ISBN, price information of all books */
select book_name, author, isbn, price " + "from book;
/* Purpose: Gets book_name, author, ISBN, publisher_id, number_of_pages, price information based on value of ISBN */
select book_name, author, genre, ISBN, publisher_id, number_of_pages, price from book where ISBN = ?
/* Purpose: Insert into users relation with the values which will replace ? */
insert into users values(?, ?, ?, ?, ?, ?, ?);
/* Purpose: Insert into postal_code_mapping relation with the values which will replace ? */
insert into postal_code_mapping values (?, ?, ?);
/* Purpose: Gets the username attribute based on whether the username attribute value  */
select username from users where username = ?;
/* Purpose: Gets the username, password, owner information from the book relation based on username and password values */
select username, password, owner from users where username = ? and password = ?;
/* Purpose: Gets the units_available, units_sold from book relation based on ISBN value */
select units_available, units_sold from book where ISBN = ?;
/* Purpose: Updates the units_available attribute from the book relation based on ISBN value */
update book set units_available = units_available + ? where ISBN = ?;
/* Purpose: Gets the pages, price and publisher_id from book relation based on ISBN value */
select pages, price, publisher_id from book where ISBN = ?;
/* Purpose: Gets the account_number from the publisher relation based on the ID of the publisher */
select account_number from publisher where id = ?;
/* Purpose: Updates the balance amount by a given amount based on the account number value */
update account set balance = balance + ? where account_number = ?;
/* Purpose: Updates the number of units_sold from the book relation based on ISBN value */
update book set units_sold = units_sold + 1 where ISBN = ?;
/* Purpose: Updates the number of units_available from the book relation based on ISBN value */
update book set units_available = units_available - 1 where ISBN = ?;
/* Purpose: Insert into orders relation with the values provided '?' */
insert into orders (order_number, username, billing_information, shipping_information) values(?, ?, ?, ?);
/* Purpose: Gets all the information from the orders relation based on the value of order_number */
select * from orders where order_number = ?;
/* Purpose: Insert into the book relation with the values provided '?' */
insert into book (book_name, author, genre, ISBN, publisher, number_of_pages, price, units_avail) values (?,?,?,?,?,?,?,?);
/* Purpose: Insert into publisher_payment_percentage with the values provided  */
insert into publisher_payment_percentage values (?, ?, ?);
/* Purpose: Gets the ID, name attribute from publisher relation */
select ID, name from publisher;
/* Purpose: Gets the max ID from the publisher relation */
select ID from publisher where ID = (select max(ID) from publisher);
/* Purpose: Inserts into the publisher relation with the values provided */
insert into publisher values(?, ?, ?, ?, ?, ?, ?, ?, ?);
/* Purpose: Gets the postal_code attribute from the postal_code_mapping relation based on the provided value of postal_code*/
select postal_code from postal_code_mapping where postal_code = ?;
/* Purpose: Gets the number_of_pages, price based on the provided value of ISBN */
select number_of_pages, price where ISBN = ?;
/* Purpose: Delete a tuple from publisher_payment_percentage relation based on the provided values of number_of_pages and price */
delete from publisher_payment_percentage where number_of_pages = ? and price = ?;
/* Purpose: Delete a tuple from book relation based on the provided value of ISBN */
delete from book where ISBN = ?;
/* Purpose: Gets all the attribute information from sales_vs_expenditure materialized view */
select * from sales_vs_expenditure;
/* Purpose: Gets all the attribute information from sales_per_genre materialized view  */
select * from sales_per_genre;
/* Purpose: Gets all the attribute information from sales_per_author materialized view */
select * from sales_per_author;