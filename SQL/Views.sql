/*
Purpose: Gets the total number of sales per genre
*/
create materialized view sales_per_genre(genre, sales) as
select genre,sum(price * units_sold) from book group by genre;
/*
Purpose: Gets the total number of sales per author
*/
create materialized view sales_per_author(author, sales) as
select author, sum(price * units_sold) from book group by author;
/*
Purpose: Gets the total number of sales and total number of expenditures
*/
create materialized view sales_vs_expenditure(sales, expenditure) as
select sum(price * units_sold), sum(number_of_pages / 100 * units_sold ) from book;