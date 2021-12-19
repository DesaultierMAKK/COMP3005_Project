package Project;

import java.sql.*;
import java.util.*;

public class connectDB {
	public static List<String> books = new ArrayList<String>();
	public static List<String> checkoutBasket_name = new ArrayList<String>();// Stores the name of the books added to the basket
	public static List<String> checkoutBasket_ISBN = new ArrayList<String>(); // Stores the ISBN of the books added to the basket
	public static List<Integer> checkoutBasket_price = new ArrayList<Integer>();// Stores the price of the books added to the basket
	public static boolean loggedIn = false;
	public static boolean ownerLoggedIn = false;
	public static String loggedUser = "";
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		boolean menuFlag = true;
		int menuValue,storeValue,bookStoreValue;
		checkoutBasket_name.clear();
		checkoutBasket_price.clear();
		checkoutBasket_ISBN.clear();
		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "username", "password");
				Statement statement = connection.createStatement();)
		{
			//Clearing the console
			/*System.out.print("\033[H\033[2J");
        	System.out.flush();*/
			while(menuFlag) {
				menuValue = mainMenu();
				if(menuValue == 1) {
					if(loggedIn) {
						loggedUser = "";
						loggedIn = false;
						ownerLoggedIn = false;
					} else {
						login(connection);
					}
				}else if (menuValue == 2){
					if(ownerLoggedIn) {
						System.out.println("1. Add books");
						System.out.println("2. Remove books");
						System.out.println("3. Add Publisher");
						System.out.println("4. Search books");
						System.out.println("5. Back to Main Menu");
						System.out.print("Enter number: ");
						bookStoreValue = sc.nextInt();
						if(bookStoreValue == 1) {
							addBooks(connection);
						}else if(bookStoreValue == 2) {
							removeBooks(connection);
						}else if (bookStoreValue == 3) {
							addPublisher(connection);
						} else if (bookStoreValue == 4) {
							storeValue = storeMenu();
							searchStore(connection, storeValue);				
						}else {
							continue;
						}
					}else {
						storeValue = storeMenu();
						searchStore(connection, storeValue);
					}
				}else if(menuValue == 3) {
					viewBasket(connection);
				}else if (menuValue == 4) {
					tracker(connection);
				}else if (menuValue == 5) {
					if(ownerLoggedIn) {
						reportsMenu(connection);
					}else {
						System.exit(0);
					}
				}else{
					if(ownerLoggedIn && menuValue == 6) {
						System.exit(0);
					}else {
						System.out.println("Invalid Entry!");
					}
				}
				//System.out.print("Continue (yes / no): ");
//				String continueCheck = sc.nextLine();
//				if(continueCheck.equals("yes")) {
//					continue;
//				}else {
//					System.exit(0);
//				}
			}
		} catch (Exception sqle) {
			System.out.println("Exception: " + sqle);
		}
	}

	//Function that creates the SQL query to search the database based on the search value for a specific attribute entered by the user
	public static void searchStore(Connection connection, int storeValue) {
		Scanner sc = new Scanner(System.in);
		PreparedStatement ps =null;
		ResultSet resultSet = null;
		String searchValue,bookName,authorName, iSBN = "";
		int price;
		try {
			if(storeValue == 1) { // book name
				ps = connection.prepareStatement(" select book_name, author, isbn, price " + "from book " + "where book_name ~* ?");
			}else if (storeValue == 2) { //author name
				ps = connection.prepareStatement(" select book_name, author, isbn, price " + "from book " + "where author = ?");
			}else if(storeValue == 3) { //ISBN
				ps = connection.prepareStatement(" select book_name, author, isbn, price " + "from book " + "where ISBN = ?");
			}else if(storeValue == 4) { //genre
				ps = connection.prepareStatement(" select book_name, author, isbn, price " + "from book " + "where genre = ?");
			}else if(storeValue == 5) { // All books
				ps = connection.prepareStatement(" select book_name, author, isbn, price " + "from book");
			}else if (storeValue == 6) {
				return;
			}
			if(storeValue < 5) {
				System.out.print("Enter search value: ");
				searchValue = sc.nextLine();
				ps.setString(1, searchValue);
				System.out.println("");
			}
			resultSet = ps.executeQuery();
			int i = 0;
			books.clear();
			while(resultSet.next()) {
				++i;		
				bookName = resultSet.getString("book_name");
				authorName = resultSet.getString("author");
				iSBN = resultSet.getString("isbn");
				price = resultSet.getInt("price");
				books.add(iSBN);
				System.out.println(i + ". "+ bookName + "  by   " + authorName + "; Price: " + price);
			}
			//System.out.println(books.get(0));
			System.out.print("Enter the number of the book you wish to select (or press 1000 to return back to main menu): ");
			int bookValue = sc.nextInt();
			if(bookValue == 1000) {
				return;
			}
			giveBookInfo(connection, bookValue);
			//Maybe add a loop and exit here
		} catch (Exception sqle) {
			System.out.println("Exception: " + sqle);
		}
	}
	
	//Function for when a book is selected, information on the author, genre, ISBN, publisher id, number of pages, price can be viewed
	public static void giveBookInfo(Connection connection, int bookValue) {
		Scanner sc = new Scanner(System.in);
		PreparedStatement ps =null;
		ResultSet resultSet = null;
		String book_name="",author_name = "", genre = "", publisher="", iSBN = "", addToBasket;
		int pages = 0, price = 0, nsold = 0, navail = 0;
		try {
			ps = connection.prepareStatement("select book_name, author, genre, ISBN, publisher_id, number_of_pages, price from book where ISBN = ?");
			ps.setString(1,(books.get(bookValue - 1)));
			resultSet = ps.executeQuery();
			while(resultSet.next()) {
				book_name = resultSet.getString("book_name");
				author_name = resultSet.getString("author");
				genre = resultSet.getString("genre");
				iSBN = resultSet.getString("ISBN");
				publisher = resultSet.getString("publisher_id");
				pages = resultSet.getInt("number_of_pages");
				price = resultSet.getInt("price");
			}
			System.out.println("");
			System.out.println("Author: " + author_name);
			System.out.println("Genre: " + genre);
			System.out.println("Publisher ID:  " + publisher);
			System.out.println("Number of pages: " + pages);
			System.out.println("Price: " + price);
			System.out.print("Would you like to add this book to the basket? (yes/no): ");
			addToBasket = sc.nextLine();
			if(addToBasket.equals("yes")) {
				checkoutBasket_name.add(book_name);
				checkoutBasket_ISBN.add(iSBN);
				checkoutBasket_price.add(price);
			}
		}catch(Exception sqle) {
			System.out.println("Exception: " + sqle);
		}
	}
	
	
	//Function that outputs the login / sign up menu
	public static void login(Connection connection) {
		Scanner sc = new Scanner(System.in);
		int value = 0;
		String name, billInfo, address, userName, passWord;
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		System.out.println("1. Sign Up?");
		System.out.println("2. Login");
		System.out.println("3. Main Menu");
		System.out.print("Enter number: ");
		value = sc.nextInt();
		if(value == 1) {
			insertUser(connection);
		} else if(value == 2) {
			loggedIn = loginUser(connection);
		} else {
			return;
		}
	}

	//Function that handles the creation of user in the bookstore through user inputed information 
	public static void insertUser(Connection connection) {
		Scanner sc = new Scanner(System.in);
		String name, street_number, street_name, apt_number, city, state, postal_code, username = "", password;
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		boolean usernameExist = true, checkCode = false;
		try {
			System.out.print("Name: ");
			name = sc.nextLine();
			System.out.print("Street number: ");
			street_number = sc.nextLine();
			System.out.print("Street name: ");
			street_name = sc.nextLine();
			System.out.print("Apt number: ");
			apt_number = sc.nextLine();
			System.out.print("City: ");
			city = sc.nextLine();
			System.out.print("Province: ");
			state = sc.nextLine();
			System.out.print("Postal Code: ");
			postal_code = sc.nextLine();
			checkCode = checkPostalCode(connection, postal_code);
			while (usernameExist){
				System.out.print("Enter Username: ");
				username = sc.nextLine();
				usernameExist = usernameExists(connection, username);
			}
			System.out.print("Enter Password: ");
			password = sc.nextLine();
			if(checkCode == true) {
				ps = connection.prepareStatement("insert into users values(?, ?, ?, ?, ?, ?, ?)");
				ps.setString(1, name);
				ps.setString(2, street_number);
				ps.setString(3, street_name);
				ps.setString(4, apt_number);
				ps.setString(5, postal_code);
				ps.setString(6, username);
				ps.setString(7, password);
			}else {
				ps = connection.prepareStatement("insert into postal_code_mapping values (?, ?, ?);insert into users values(?, ?, ?, ?, ?, ?, ?)");
				ps.setString(1, postal_code);
				ps.setString(2, state);
				ps.setString(3, city);
				ps.setString(4, name);
				ps.setString(5, street_number);
				ps.setString(6, street_name);
				ps.setString(7, apt_number);
				ps.setString(8, postal_code);
				ps.setString(9, username);
				ps.setString(10, password);
			}
			resultSet = ps.executeQuery();
			System.out.println("Successfuly created user: " + username);
			loggedIn = true;
			loggedUser = username;
			return;
		}catch (Exception sqle) {
			System.out.println("Line 205: Exception: " + sqle);
		}
	}
	
	//Function that checks if a username already exists, as in any application no two users can have the same username
	public static boolean usernameExists(Connection connection, String username) {
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		try {
			ps = connection.prepareStatement("select username from users where username = ?");
			ps.setString(1, username);
			resultSet = ps.executeQuery();
			while(resultSet.next()) {
				if(resultSet.getString("username").equals(username)) {
					System.out.println("Username exists, pls enter another username");
					return true;
				}
			}
		} catch(Exception sqle) {
			System.out.println("Exception: " + sqle);
		}
		return false;
	}
	
	//Function that handles the logging in of the user to the bookstore
	public static boolean loginUser(Connection connection) {
		Scanner sc = new Scanner(System.in);
		String username, password, owner = "";
		String userName = "", passWord = "";
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		System.out.print("Enter Username: ");
		username = sc.nextLine();
		System.out.print("Enter Password: ");
		password = sc.nextLine();
		try {
			ps = connection.prepareStatement("select username, password, owner from users where username = ? and password = ?");
			ps.setString(1, username);
			ps.setString(2, password);
			resultSet = ps.executeQuery();
			while(resultSet.next()) {
				userName = resultSet.getString("username");
				passWord = resultSet.getString("password");
				owner = resultSet.getString("owner");
			}
			if(owner.equals("Yes")) {
				ownerLoggedIn = true;
				System.out.println("Owner logged in");
			} else {
				System.out.println("Logged in");
				ownerLoggedIn = false;
			}
			if(username.equals(userName) && password.equals(passWord)) {
				
				loggedUser = username;
				return true;
			}else {
				System.out.print("Invalid Username / Password");
			}
		}catch (Exception sqle) {
			System.out.println("Exception: " + sqle);
		}
		return false;
	}

	//Function that displays the different search options for the searching of books
	public static int storeMenu() {
		Scanner sc = new Scanner(System.in);
		int value = 0;
		System.out.println("Search books based on: ");
		System.out.println("1. Book Name");
		System.out.println("2. Author Name");
		System.out.println("3. ISBN");
		System.out.println("4. Genre");
		System.out.println("5. All books");
		System.out.println("6. Return to Main Menu");
		System.out.print("Enter number: ");
		value = sc.nextInt();
		return value;
	}

	//Function that displays the checkout basket at a given time
	public static void viewBasket(Connection connection) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Basket");
		String checkout = "";
		for(int i = 0; i < checkoutBasket_name.size(); i++) {
			System.out.println(i+1 + ". " + checkoutBasket_name.get(i) + "; Price: " + checkoutBasket_price.get(i));
		}
		System.out.print("Checkout? (yes/no): ");
		checkout = sc.nextLine();
		if(checkout.equals("yes")) {
			if(loggedIn) {
				checkout(connection);
				payPublishers(connection);
				for(int i = 0; i < checkoutBasket_ISBN.size(); i++) {
					checkThreshold(connection, checkoutBasket_ISBN.get(i));
				}
				checkoutBasket_ISBN.clear();
				checkoutBasket_name.clear();
				checkoutBasket_price.clear();
			}else {
				System.out.println("To checkout, please login / signup to the bookstore");
			}		
		}else if (checkout.equals("no")){
			System.out.println("Unlucky");
		}else {
			System.out.println("Invalid answer");
		}
	}

	/*
	  Function that checks the whether the number of books available in the bookstore is below the threshold
	  If the number is less than the threshold, it automatically orders books equal to number of books sold
	*/
	public static void checkThreshold(Connection connection, String iSBN) {
		PreparedStatement update = null, getUnits = null;
		ResultSet resultSet = null;
		int unitsSold = 0, unitsAvailable = 0;
		try {
			getUnits = connection.prepareStatement("select units_available, units_sold from book where ISBN = ?");
			getUnits.setString(1, iSBN);
			resultSet = getUnits.executeQuery();
			while(resultSet.next()) {
				unitsAvailable = resultSet.getInt("units_available");
				unitsSold = resultSet.getInt("units_sold");
			}
		} catch(Exception sqle) {
			System.out.println("Exception: " + sqle);
		}
		try {
			if(unitsAvailable < 10) {
				update = connection.prepareStatement("update book set units_available = units_available + ? where ISBN = ?");
				update.setInt(1, unitsSold);
				update.setString(2, iSBN);
				update.executeQuery();
			}
		}catch(Exception sqle) {
			System.out.println("Exception: " + sqle);
		}
	} 

	//Function that handles the payment of publishers when books are sold
	public static void payPublishers(Connection connection) {
		PreparedStatement getInfo = null, pay = null, getPublisherNumber = null;
		ResultSet bookResultSet = null, publisherResultSet;
		int pay_percentage = 0, price = 0, pages = 0;
		String publisher_id  = "", publisher_number = "";
		double transfer = 0.0;
		try {
			for(int i = 0; i < checkoutBasket_ISBN.size(); i++) {
				getInfo = connection.prepareStatement("select number_of_pages, price, publisher_id from book where ISBN = ?");
				getInfo.setString(1, checkoutBasket_ISBN.get(i));
				bookResultSet = getInfo.executeQuery();
				while(bookResultSet.next()) {
					pages = bookResultSet.getInt("number_of_pages");
					price = bookResultSet.getInt("price");					
					publisher_id = bookResultSet.getString("publisher_id");
					break;
				}
				getPublisherNumber = connection.prepareStatement("select account_number from publisher where id = ?");
				getPublisherNumber.setString(1,publisher_id);
				publisherResultSet = getPublisherNumber.executeQuery();
				while(publisherResultSet.next()) {
					publisher_number = publisherResultSet.getString("account_number");
					break;
				}
				transfer = (pages / 100);
				pay = connection.prepareStatement("update account set balance = balance + ? where account_number = ? ; update book set units_sold = units_sold + 1 where ISBN = ?; update book set units_available = units_available - 1 where ISBN = ? ");
				pay.setDouble(1, transfer);
				pay.setString(2, publisher_number);
				pay.setString(3, checkoutBasket_ISBN.get(i));
				pay.setString(4, checkoutBasket_ISBN.get(i));
				pay.executeQuery();
			}
		} catch(Exception sqle) {
			System.out.println("Exception: " + sqle);
		}
	}
	
	
	//Function that implements the checkout feature, creates an order number and adds the tuple to the orders relation
	public static void checkout(Connection connection) {
		Scanner sc = new Scanner(System.in);
		PreparedStatement ps = null;
		ResultSet info = null;
		String billInfo, shipInfo, orderNumber = "";
		int orderNum;
		System.out.print("Enter billing information: ");
		billInfo = sc.nextLine();
		System.out.print("Enter shipping information: ");
		shipInfo = sc.nextLine();
		try {
			String query = "select order_number from orders";
			Statement stmt = connection.createStatement(
			        ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			info = stmt.executeQuery(query);
			ps = connection.prepareStatement("insert into orders (order_number, username, billing_information, shipping_information) values(?, ?, ?, ?)");
			if(info.last()) {
				orderNumber = info.getString("order_number");
				orderNum = Integer.parseInt(orderNumber);
				orderNum++;
				orderNumber = String.valueOf(orderNum);
				ps.setString(1, orderNumber);
			}else {
				System.out.println("First order");
				orderNumber = "100000001";
				ps.setString(1, "100000001");
			}			
			ps.setString(2, loggedUser);
			ps.setString(3, billInfo);
			ps.setString(4, shipInfo);
			System.out.println("Order Number: " + orderNumber);
			ps.executeQuery();
		} catch(SQLException sqle) {
			System.out.println("Exception " + sqle);
		}
	}
	
	//Function that prints tracking information via order number
	public static void tracker(Connection connection) {
		try { 
			Scanner sc = new Scanner(System.in);
			String orderNumber, trackingStatus, username;
			System.out.print("Enter order number: ");
			orderNumber = sc.nextLine();
			PreparedStatement ps = null;
			ResultSet resultSet = null;
			ps = connection.prepareStatement("select * from orders where order_number = ?");
			ps.setString(1, orderNumber);
			resultSet = ps.executeQuery();
			while(resultSet.next()) {
				orderNumber = resultSet.getString(1);
				trackingStatus = resultSet.getString(2);
				username = resultSet.getString(3);
				System.out.println("Username: " + username);
				System.out.println("Order Number: " + orderNumber);
				System.out.println("Order Status: " + trackingStatus);
			}
		}catch(SQLException sqle) {
			System.out.println("Exception:" + sqle);
		}
	}

	//This function is for printing the main menu which is displayed on opening the application
	public static int mainMenu() {
		int value = 0;
		Scanner sc = new Scanner(System.in);
		System.out.println("Welcome to Look Inna Book");
		if(loggedIn) {
			System.out.println("1. Logout");
		}else {
			System.out.println("1. Sign Up / Login");
		}
		if(ownerLoggedIn) {
			System.out.println("2. Search / Change Bookstore");
		}else {
			System.out.println("2. Search Bookstore");
		}
		System.out.println("3. View Basket");
		System.out.println("4. Track Order");
		if(ownerLoggedIn) {
			System.out.println("5. Reports");
			System.out.println("6. Exit");
		}else {
			System.out.println("5. Exit");
		}
		System.out.print("Enter number: ");
		
		value = sc.nextInt();
		return value;
	}

	//Function that handles the addition of books feature to the exisitng collection based on the values inputted by the user
	public static void addBooks(Connection connection) {
		Scanner sc = new Scanner(System.in);
		PreparedStatement ps = null, update = null;
		String bookName,authorName,genre, publisher,iSBN;
		int pages,price,percentage, navail;
		try {
			System.out.println("Enter book information");
			System.out.print("Book Name: ");
			bookName = sc.nextLine();
			System.out.print("Author Name: ");
			authorName = sc.nextLine();
			System.out.print("Genre: ");
			genre = sc.nextLine();
			System.out.print("ISBN: ");
			iSBN = sc.nextLine();
			showPublishers(connection);
			System.out.print("Enter valid Publisher ID: ");
			publisher = sc.nextLine();
			System.out.print("Number of Pages: ");
			pages = sc.nextInt();
			System.out.print("Number of units available: ");
			navail = sc.nextInt();
			System.out.print("Price: ");
			price = sc.nextInt();
			addPercentage(connection, pages, price);
			ps = connection.prepareStatement("insert into book (book_name, author, genre, ISBN, publisher_id, number_of_pages, price, units_available) values (?,?,?,?,?,?,?,?)");
			ps.setString(1, bookName);
			ps.setString(2, authorName);
			ps.setString(3, genre);
			ps.setString(4, iSBN);
			ps.setString(5, publisher);
			ps.setInt(6, pages);
			ps.setInt(7, price);
			ps.setInt(8, navail);
			ps.executeQuery();
		} catch (SQLException sqle) {
			System.out.println("Exception: " + sqle);
		}
		System.out.println("Book added to store collection");
	}
	
	//Function that handles the insertion of the pay_percentage into publisher_payment_percentage
	public static void addPercentage(Connection connection, int pages, int price) {
		double percentage;
		PreparedStatement update = null;
		try {
			percentage = pages / price;
			update = connection.prepareStatement("insert into publisher_payment_percentage values (?, ?, ?)");
			update.setInt(1, price);
			update.setInt(2, pages);
			update.setDouble(3, percentage);
			update.executeQuery();
		} catch(SQLException sqle) {
			System.out.println("Exception: " + sqle);
		}
	}
	
	//Function that shows all the information from the publisher relation
	public static void showPublishers(Connection connection) {
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		String id = "", name = "";
		try {
			
			ps = connection.prepareStatement("select ID, name from publisher");
			resultSet = ps.executeQuery();
			while(resultSet.next()) {
				id = resultSet.getString("ID");
				name = resultSet.getString("name");
				System.out.println("ID: " + id + "; Name: " + name);
			}
		}catch(SQLException sqle) {
			System.out.println("Exception: " + sqle);
		}
	}
	
	//Function that handles the addition of publisher into the publisher relation based on the inputted values
	public static void addPublisher(Connection connection) {
		Scanner sc = new Scanner(System.in);
		PreparedStatement ps = null, getInfo  = null;
		ResultSet info  = null;
		boolean checkCode = false;
		String name, street_number, street_name, apt_number, city, state, postal_code, email, number, account, id ="";
		int integerID;
		try {
			getInfo = connection.prepareStatement("select ID from publisher where ID = (select max(ID) from publisher)");
			info = getInfo.executeQuery();
			while(info.next()) {
				id = info.getString(1);
				break;
			}
			integerID = Integer.parseInt(id);
			System.out.println("Integer: " + integerID);
			integerID++;
			id = String.valueOf(integerID);
			System.out.println("ID: " + id);
			System.out.println("Enter publisher information");
			System.out.print("Name: ");
			name = sc.nextLine();
			System.out.print("Street Number: ");
			street_number = sc.nextLine();
			System.out.print("Street Name: ");
			street_name = sc.nextLine();
			System.out.print("Apt Number: ");
			apt_number = sc.nextLine();
			System.out.print("City: ");
			city = sc.nextLine();
			System.out.print("Province: ");
			state = sc.nextLine();
			System.out.print("Postal code: ");
			postal_code = sc.nextLine();
			checkCode = checkPostalCode(connection, postal_code);
			System.out.print("Email: ");
			email = sc.nextLine();
			System.out.print("Phone Number: ");
			number = sc.nextLine();
			System.out.print("Enter valid Account Number: ");
			account = sc.nextLine();
			if(checkCode  == false) {
				ps = connection.prepareStatement("insert into postal_code_mapping values (?,?,?);insert into publisher values(?, ?, ?, ?, ?, ?, ?, ?, ?)");
				ps.setString(1, postal_code);
				ps.setString(2, state);
				ps.setString(3, city);
				ps.setString(4, id);
				ps.setString(5, name);
				ps.setString(6, street_number);
				ps.setString(7, street_name);
				ps.setString(8, apt_number);
				ps.setString(9, postal_code);
				ps.setString(10, email);
				ps.setString(11, number);
				ps.setString(12, account);
			}else {
				ps = connection.prepareStatement("insert into publisher values(?, ?, ?, ?, ?, ?, ?, ?, ?)");
				ps.setString(1, id);
				ps.setString(2, name);
				ps.setString(3, street_number);
				ps.setString(4, street_name);
				ps.setString(5, apt_number);
				ps.setString(6, postal_code);
				ps.setString(7, email);
				ps.setString(8, number);
				ps.setString(9, account);
			}
			ps.executeQuery();
		}catch(SQLException sqle) {
			System.out.println("Exception: " + sqle);
		}
		System.out.println("Added publisher: " + id);
	}
	
	//Function that checks whether the postal code value passed to the function is already present in the postal_code_mapping relation
	public static boolean checkPostalCode(Connection connection, String postal_code) {
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		String postalCode;
		try {
			ps = connection.prepareStatement("select postal_code from postal_code_mapping where postal_code = ?");
			ps.setString(1, postal_code);
			resultSet = ps.executeQuery();
			while(resultSet.next()) {
				postalCode = resultSet.getString("postal_code");
				if(postalCode.equals(postal_code)){
					System.out.println("Exists");
					return true;
				}
			}	
		}catch(SQLException sqle) {
			System.out.println("Exception: " + sqle);
		}
		return false;
	}
	
	//Function that handles the removal of books based on the ISBN value provided
	public static void removeBooks(Connection connection) {
		Scanner sc = new Scanner(System.in);
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		String iSBN;
		int pages = 0, price = 0;
		try {
			System.out.print("Enter the ISBN of the book to be removed: ");
			iSBN = sc.nextLine();
			ps = connection.prepareStatement("select number_of_pages, price from book where ISBN = ?");
			ps.setString(1, iSBN);
			resultSet = ps.executeQuery();
			while(resultSet.next()) {
				pages = resultSet.getInt("number_of_pages");
				price = resultSet.getInt("price");
			}
			ps = connection.prepareStatement("delete from book where ISBN = ?; delete from publisher_payment_percentage where number_of_pages = ? and price = ?");
			ps.setString(1, iSBN);
			ps.setInt(2, pages);
			ps.setInt(3, price);
			
			ps.executeQuery();
		}catch(SQLException sqle) {
			System.out.println("Exception: " + sqle);
		}
		System.out.println("Book removed from collection");
	}
	
	//Function that displays the reports menu and gets the appropriated information based on the report selected
	public static void reportsMenu(Connection connection) {
		Scanner sc = new Scanner(System.in);
		int reportMenuValue;
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		System.out.println("1. Sales vs Expenditures");
		System.out.println("2. Sales per genre");
		System.out.println("3. Sales by author");
		System.out.println("4. Main Menu");
		System.out.print("Enter the number associated with the report you would like to view: ");
		reportMenuValue = sc.nextInt();
		try {
			if(reportMenuValue == 1) {
				ps = connection.prepareStatement("select * from sales_vs_expenditure");
				resultSet = ps.executeQuery();
				int sales = 0, expenditure = 0;
				while(resultSet.next()) {
					sales = resultSet.getInt(1);
					expenditure = resultSet.getInt(2);
					System.out.println("Sales: " + sales);
					System.out.println( "Expenditure: " + expenditure);
					System.out.println("Profit: "+ (sales - expenditure));
				}
			}else if (reportMenuValue == 2) {
				ps = connection.prepareStatement("select * from sales_per_genre");
				resultSet = ps.executeQuery();
				while(resultSet.next()) {
					System.out.println("Genre: " + resultSet.getString(1) + "; Sales: " + resultSet.getInt(2));
				}
			} else if (reportMenuValue == 3) {
				ps = connection.prepareStatement("select * from sales_per_author");
				resultSet = ps.executeQuery();
				while(resultSet.next()) {
					System.out.println("Author: " + resultSet.getString(1) + "; Sales: " + resultSet.getInt(2));
				}
			}else if (reportMenuValue == 4) {
				return;
			}else {
				System.out.println("Inavlid entry !");
			}
		} catch(SQLException sqle) {
			System.out.println("Exception: " + sqle);
		}
	}
}