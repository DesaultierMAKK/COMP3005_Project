/* Purpose: Deletes all tuples from the below relaltions */
delete from book;
delete from publisher;
delete from account;
delete from orders;
delete from users;
delete from postal_code_mapping;
delete from publisher_payment_percentage;
/*
Purpose: Inserts tuples into the account relation
*/
insert into account values ('0040678919', 'Studio1', 'Scotia', 0);
insert into account values ('0040678520', 'Studio2', 'Scotia', 0);
insert into account values ('0040678652', 'Studio3', 'TD', 0);
insert into account values ('0040679451', 'Studio4', 'Scotia', 0);
insert into account values ('0040679521', 'Studio5', 'TD', 0);
/*
Purpose: Inserts into the postal_code_mapping values
*/
insert into postal_code_mapping values ('K2G 7F2', 'ON', 'Ottawa');
insert into postal_code_mapping values('K1K 3K4', 'ON', 'Ottawa');
insert into postal_code_mapping values('K2K 3K4', 'ON', 'Ottawa');
insert into postal_code_mapping values('K3G 6K4', 'ON', 'Ottawa');
insert into postal_code_mapping values('K2H 9K5', 'ON', 'Ottawa');
insert into postal_code_mapping values('K2K 1K8', 'ON', 'Ottawa');
/*
Purpose: Inserts tuples into the publisher relation
*/
insert into publisher values ('1001','Studio1', '20', 'Slater st', '1',  'K1K 3K4','studio1@gmail.com','(613) 129-5012','0040678919');
insert into publisher values ('1002','Studio2', '21', 'Queens st','2','K2K 3K4','studio2@gmail.com','(613) 541-1912','0040678520');
insert into publisher values ('1003','Studio3', '22', 'Rideau st','3', 'K3G 6K4','studio3@gmail.com','(613) 851-2634','0040678652');
insert into publisher values ('1004','Studio4', '23', 'Sommerset st','4','K2H 9K5','studio4@gmail.com','(613) 954-5836','0040679451');
insert into publisher values ('1005','Studio5', '24', 'Kintyre Pt', '5', 'K2K 1K8','studio5@gmail.com','(613) 156-4972', '0040679521');
/*
Purpose: Inserts tuples into the users relation
*/
insert into users values ( 'MAK','Muhammed Khan', '1233', 'Colonel By Dr', '1', 'K2G 7F2','12345','Yes');
insert into users values ( 'Chew', 'Coral Chew', '1233', 'Colonel By Dr', '1', 'K2G 7F2','12345','Yes');
insert into users values ( 'Hughes', 'Dylan Hughes', '1233', 'Colonel By Dr', '1', 'K2G 7F2','12345', 'No');
insert into users values ( 'Zhang','Pengbo Zhang', '1233', 'Colonel By Dr', '2', 'K2G 7F2', '12345', 'No');
insert into users values ( 'Mari','Rahul Mari', '1233' , 'Colonel By Dr', '2', 'K2G 7F2', '12345');
/*
Purpose: Inserts tuples into the publisher_payment_percentage relation
*/
insert into publisher_payment_percentage values(70, 857, 12.24);
insert into publisher_payment_percentage values(70, 903, 12.9);
insert into publisher_payment_percentage values(65, 356, 12.24);
insert into publisher_payment_percentage values(60, 554, 9.23);
insert into publisher_payment_percentage values(72, 402, 5.58);
insert into publisher_payment_percentage values(55, 356, 5.58);
insert into publisher_payment_percentage values(90, 857, 9.52);
insert into publisher_payment_percentage values(100, 1014, 10.14);
insert into publisher_payment_percentage values(45, 606, 13.46);
insert into publisher_payment_percentage values(55, 702, 9.52);
/*
Purpose: Inserts tuples into the book relation
*/
insert into book values('1000000000001','Harry-Potter1','J.K. Rowling', 'Fiction', '1001',857, 70, 0, 15);
insert into book values('1000000000002','Harry-Potter2','J.K. Rowling', 'Fiction', '1001', 903, 70, 0, 15);
insert into book values('1000000000003','A World on the Wing','Scott Weid', 'Non-Fiction', '1002', 356, 55, 0, 15);
insert into book values('1000000000004','Wayfinding','Michael Bond', 'Non-Fiction', '1002', 554, 60, 0, 15);
insert into book values('1000000000005','Nox','Anne Carson', 'Poetry', '1003',402, 72, 0, 15);
insert into book values('1000000000006','Thrall','Natasha Trethewey', 'Poetry', '1003', 356, 65, 0, 15);
insert into book values('1000000000007','The Crucible','Arthur Miller', 'Drama', '1004', 857, 90, 0, 15);
insert into book values('1000000000008','Macbeth','William Shakespeare', 'Drama','1004', 1014, 100, 0, 15);
insert into book values('1000000000009','It Ends With Us','Colleen Hoover', 'Romance', '1005', 606, 45, 0, 15);
insert into book values('1000000000010','The Proposal','Jasmine Guillory', 'Romance', '1005', 702, 55, 0, 15);
