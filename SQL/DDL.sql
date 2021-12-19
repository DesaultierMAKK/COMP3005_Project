/*
Purpose: BCNF decompositated relation that stores the postal code mappings to province and city
*/
create table postal_code_mapping(
	postal_code varchar(7),
	province varchar(15),
	city varchar(15),
	primary key(postal_code)
);
/*
Purpose: BCNF decompositated relation that stores the percentage of sales that is transferred
*/
create table publisher_payment_percentage(
	price numeric(5,2) check(price > 0),
	number_of_pages numeric(4,0) check(number_of_pages > 0),
	pay_percentage numeric(4,2),
	primary key(price, number_of_pages)
);
/*
Purpose: Creates the account relation in the database which stores the account information of the publishers in the application
*/
create table account(
	account_number varchar(12),
	name varchar(30) not null,
	branch_name varchar(30),
	balance numeric(8,2),
	primary key(account_number)
);
/*
Purpose: Creates the publisher relation in the database which stores the information of the different publishers of the books in the application
*/
create table publisher(
	ID varchar(5),
	name varchar(30) not null,
	street_number varchar(5),
	street_name varchar(20),
	apt_number varchar(10),
	postal_code varchar(7),
	email_address varchar(30) unique,
	phone_number varchar(14) not null,
	account_number varchar(10),
	primary key(id),
	foreign key(account_number) references account(account_number)
		on delete cascade,
	foreign key(postal_code) references postal_code_mapping
);
/*
Purpose: Creates the book relation in the database which stores the books information of the collection of books in the application
*/
create table book(
	ISBN varchar(13),
	book_name varchar(40) not null,
	author varchar(30) not null,
	genre varchar(20) check (genre in ('Fiction','Non-Fiction','Drama','Poetry','Romance')),
	publisher_id varchar(20) not null,
	number_of_pages numeric(4,0) check(number_of_pages > 0),
	price numeric(5,2) check(price > 0),
	units_sold numeric(4,0) check (units_sold >= 0) default 0,
	units_available numeric(4,0) check (units_available >= 0),
	primary key(ISBN),
	foreign key(publisher_id) references publisher(ID)
		on delete cascade,
	foreign key(number_of_pages, price) references publisher_payment_percentage(number_of_pages, price)
);
/*
Purpose: Creates the users relation in the database to store the user(s) information of the users registered in the application
*/
create table users(
	username varchar(12),
	name varchar(30) not null,
	street_number varchar(5),
	street_name varchar(20),
	apt_number varchar(10),
	postal_code varchar(7),
	password varchar(12),
	owner varchar(4) default 'No',
	primary key (username),
	foreign key(postal_code) references postal_code_mapping
);
/*
Purpose: Creates the orders relation in the database to store the information of the orders placed using the application
*/
create table orders(
	order_number varchar(9),
	order_status varchar(20) check (order_status in ('Order Placed', 'Shipped', 'In Transit', 'Delivered')) default 'Order Placed',
	username varchar(12),
	billing_information varchar(30) not null,
	shipping_information varchar(30) not null,
	primary key(order_number),
	foreign key(username) references users(username)
		on delete cascade
);
