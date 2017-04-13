/*
  Written by Thao N. Pham. 
  Updated by: Lory Al Moakar, Roxana Georghiu, Nick R. Katsipoulakis, Xiaoyu Ge
  Purpose: Demo JDBC for CS1555 Class
  
  IMPORTANT (otherwise, your code may not compile)	
  Same as using sqlplus, you NEED TO SET oracle environment variables by 
  sourcing bash.env or tcsh.env
*/

import java.io.IOException;
import java.sql.*;  //import the file containing definitions for the parts
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
//needed by java for database connection and manipulation

public class Admin {
    private static Connection connection; //used to hold the jdbc connection to the DB
    private Statement statement; //used to create an instance of the connection
    private PreparedStatement prepStatement; //used to create a prepared statement, that will be later reused
    private ResultSet resultSet; //used to hold the result of your query (if one
    // exists)
	Scanner input = new Scanner(System.in);
    private String query;  //this will hold the query we are using
	SimpleDateFormat d = new SimpleDateFormat("dd-MMM-YYYY");
    
    
    public Admin(int example_no) throws SQLException, ParseException {
	
	switch ( example_no) {
	case 0:
	    Example0();
	    break;
	case 1:
	    Example1();
	    break;
	case 2: 
	    Example2(2);
	    break;
	case 3:
	    Example2(3);
	    break;
	case 4: 
	    Example4();
	    break;
	case 5:
	    Example5();
	    break;

/////////////////////////////////////////////START OF MY WORK/////////////////////////////////////////

	case 6:
		newCust(input);
		break;
	case 7:
		updateShareQuotes(input);
		break;
	case 8:
		addMutualFund(input);
		break;
	case 9:
		updateTimeDate(input);
		break;
	case 10:
		stats(input);
		break;



	default:
	    System.out.println("Example not found for your entry: " + example_no);
	    try {
		connection.close();
	    }
	    catch(Exception Ex)  {
		System.out.println("Error connecting to database.  Machine Error: " +
				   Ex.toString());
	    }
	    break;
	}
			
    }


    public void stats(Scanner input) throws SQLException {

		int numMonths = 0;
		int numCategories = 0;
		int numInvestors = 0;
		String months = "0";
		String highestInvested = "0";
		String mostInvestors = "0";

			System.out.print( "Enter how many months far back to go");
		months = input.nextLine();
		numMonths = Integer.parseInt( months );

		System.out.print( "Enter number of highest volume categories");
		highestInvested = input.nextLine();
		numCategories = Integer.parseInt( highestInvested );

		System.out.print( "Enter number of most investors");
		mostInvestors = input.nextLine();
		numInvestors = Integer.parseInt( mostInvestors );


			statement = connection.createStatement();
			String query = "select max(c_date) from MUTUALDATE";

			resultSet = statement.executeQuery( query );
			resultSet.next();

			Date date = resultSet.getDate(1);
			query = "select * from ( select * from ( select category, sum(sum_shares) as sh_count from MUTUALFUND, ( select symbol as sym, sum(num_shares) as sum_shares from TRXLOG where action = 'buy' and t_date >= add_months( ( select to_date( '" + date.toString() + "', 'yyyy-mm-dd') from dual ), -" + numMonths + ") group by symbol )where symbol = sym group by category )order by sh_count desc ) where rownum <= " + numCategories;
			resultSet = statement.executeQuery( query );

			System.out.println("Category\tNumber of shares\n--------------------------------------");

			while ( resultSet.next() ) {
				System.out.println( resultSet.getString(1) + "\t\t" + resultSet.getInt(2) );
			}

			query = "select * from ( select name, sum_amounts from customer, ( select * from ( select login as username, sum(amount) as sum_amounts from trxlog where action = 'buy' and t_date >= add_months( ( select to_date( '" + date.toString() + "', 'yyyy-mm-dd' ) from dual ), -" + numMonths + ") group by login ) order by sum_amounts desc ) where login = username ) where rownum <= " + numInvestors;
			resultSet = statement.executeQuery( query );

			System.out.println("\nName\t\tInvested Amount\n------------------------------------");

			while ( resultSet.next() ) {
				System.out.println( resultSet.getString(1) + "\t\t" + resultSet.getFloat(2) );
			}

	}




    public void updateTimeDate(Scanner input) throws SQLException, ParseException {

		System.out.println("Enter the date as mm-dd-yyy");
		String date = input.nextLine();
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("MM-dd-yyyy");
		java.sql.Date date_reg = new java.sql.Date (df.parse(date).getTime());

		query = "update MUTUALDATE set c_date = ?";

		prepStatement = connection.prepareStatement(query);
		prepStatement.setDate(1, date_reg);

		prepStatement.executeUpdate();
		connection.commit();

	}






public void addMutualFund(Scanner input) throws SQLException,ParseException{

	System.out.println("Enter a symbol");
	String symbol = input.nextLine();

	System.out.println("Enter a name");
	String name = input.nextLine();

	System.out.println("Enter a description");
	String description = input.nextLine();

	System.out.println("Enter a category");
	String category = input.nextLine();

	System.out.println("Enter the date as mm-dd-yyy");
	String date = input.nextLine();


java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("MM-dd-yyyy");
java.sql.Date date_reg = new java.sql.Date (df.parse(date).getTime());

	query = "insert into MUTUALFUND values (?,?,?,?,?)";
	prepStatement = connection.prepareStatement(query);

	    prepStatement.setString(1, symbol);
	    prepStatement.setString(2, name);
	    prepStatement.setString(3, description);
	    prepStatement.setString(4, category);
	    prepStatement.setDate(5, date_reg);

	    prepStatement.executeUpdate();

	float price=0;


	query = "insert into CLOSINGPRICE values (?,?,?)";
	prepStatement = connection.prepareStatement(query);

	prepStatement.setString(1, symbol);
	prepStatement.setFloat(2, price);
	prepStatement.setDate(3, date_reg);


	prepStatement.executeUpdate();

connection.commit();
}






public void updateShareQuotes(Scanner input) throws SQLException {

	query = "select symbol,price from CLOSINGPRICE where p_date = ?";

	System.out.println("Enter the date as: mm-dd-yyyy");
	String date = input.nextLine();

	prepStatement = connection.prepareStatement(query);
	prepStatement.setString(1, date);

	resultSet = prepStatement.executeQuery();



	while (resultSet.next()) {

		String symbol = resultSet.getString(1);
		//float oldPrice = resultSet.getFloat(2);

		System.out.println("Enter new price");
		float price = input.nextFloat();


		query = "update CLOSINGPRICE set price = ? where p_date = ? and symbol = ?";

		prepStatement = connection.prepareStatement(query);
		prepStatement.setFloat(1, price);
		prepStatement.setString(2, symbol);
		prepStatement.setString(3, "curr Date");    // DATE
		prepStatement.executeUpdate();

	}

	connection.commit();
}





public void newCust(Scanner input) throws SQLException,ParseException {


		statement = connection.createStatement();

	String query;

	System.out.println("Enter Username");
	String login = input.nextLine();

	//Check if username exists
	query = "select * from CUSTOMER where login = ?";

	prepStatement = connection.prepareStatement(query);
	prepStatement.setString(1,login);
	resultSet = prepStatement.executeQuery();

	if(resultSet.getString(1).equals(login)){
		System.out.println("Customer username already in use");
		return;
	}

	query = "select * from ADMINISTRATOR where login = ?";

	prepStatement = connection.prepareStatement(query);
	prepStatement.setString(1,login);
	resultSet = prepStatement.executeQuery();

	if(resultSet.getString(1).equals(login)){
		System.out.println("Admin username already in use");
		return;
	}



	System.out.println("Enter a name");
	String name = input.nextLine();

	System.out.println("Enter email");
	String email = input.nextLine();

	System.out.println("Enter address");
	String address = input.nextLine();

	System.out.println("Enter a password");
	String password = input.nextLine();

	float balance = 0;



	System.out.println("Are you an admin? Y/N");
	String choice = input.nextLine();

	if(choice.toLowerCase().equals("y")){
		//Admin
		query = "insert into ADMINISTRATOR values(?,?,?,?,?)";
		prepStatement = connection.prepareStatement(query);

		prepStatement.setString(1, login);
		prepStatement.setString(2, name);
		prepStatement.setString(3, email);
		prepStatement.setString(4, address);
		prepStatement.setString(5, password);
		prepStatement.executeUpdate();


	}else{
		//Customer
		query = "insert into CUSTOMER values(?,?,?,?,?,?)";
		prepStatement = connection.prepareStatement(query);

		prepStatement.setString(1, login);
		prepStatement.setString(2, name);
		prepStatement.setString(3, email);
		prepStatement.setString(4, address);
		prepStatement.setString(5, password);
		prepStatement.setFloat(6, balance);
		prepStatement.executeUpdate();

	}

	}

	////////////////////////////////////////////////////////////////END OF MY WORK///////////////////////////////////////////////////



    /////////////////EXAMPLE 0 //////////////////////////
    public void Example0() {
	
	int counter = 1;
	/*We will now perform a simple query to the database, asking for all the
	  records it has.  For your project, performing queries will be similar*/
	try{
	    statement = connection.createStatement(); //create an instance
	    String selectQuery = "SELECT * FROM register"; //sample query
	    
	    resultSet = statement.executeQuery(selectQuery); //run the query on the DB table
	   
	    /*the results in resultSet have an odd quality. The first row in result
	      set is not relevant data, but rather a place holder.  This enables us to
	      use a while loop to go through all the records.  We must move the pointer
	      forward once using resultSet.next() or you will get errors*/

	    while (resultSet.next()) //this not only keeps track of if another record
		//exists but moves us forward to the first record
	    {
		   System.out.println("Record " + counter + ": " +
		      resultSet.getString(1) + ", " + //since the first item was of type
		      //string, we use getString of the
		      //resultSet class to access it.
		      //Notice the one, that is the
		      //position of the answer in the
		      //resulting table
		      resultSet.getLong(2) + ", " +   //since second item was number(10),
		      //we use getLong to access it
		      resultSet.getDate(3)); //since type date, getDate.
		  counter++;
	    }
	    
	    
	    /*Now, we show an insert, using preparedStatement. 
	      Of course for this you can also write the query 
	      directly as the above case with select, and vice versa. */
	    
	 // a string that stores the query. Put question marks as placeholders for the 
	    // values you need to enter. Each question mark will later be replaced with 
	    // the value specified by the set* method
	    query = "insert into Register values (?,?,?)";
	    prepStatement = connection.prepareStatement(query);
      
	    String name = "student 2";
	    long classid = 1;
	    // This is how you can specify the format for the dates you will use
	    java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
	    // This is how you format a date so that you can use the setDate format below
	    java.sql.Date date_reg = new java.sql.Date (df.parse("2012-02-24").getTime());
	    
	    
	    // You need to specify which question mark to replace with a value.
	    // They are numbered 1 2 3 etc..
	    prepStatement.setString(1, name); 
	    prepStatement.setLong(2, classid);
	    prepStatement.setDate(3, date_reg);
	    // Now that the statement is ready. Let's execute it. Note the use of 
	    // executeUpdate for insertions and updates instead of executeQuery for 
	    // selections.
	    prepStatement.executeUpdate();
    	
	    //I will show the insert worked by selecting the content of the table again
	    //statement = connection.createStatement();
	    //query = "SELECT * FROM Register";
	    
	    resultSet = statement.executeQuery(selectQuery);
	    System.out.println("\nAfter the insert, data is...\n");
	    counter=1;
	    while(resultSet.next()) {
		System.out.println("Record " + counter + ": " +
				   resultSet.getString(1) + ", " +
				   resultSet.getLong(2) + ", " +
				   resultSet.getDate(3));
		counter ++;
	    }
	    resultSet.close();
	    /*
	     * The preparedStatement can be and should be reused instead of creating a new object.
	     * NOTE that when you have many insert statements (more than 300), creating a new statement 
	     * for every insert will end up in throwing an error.
	     */
	    
	    //Reuse of the prepare statement
	   
	    prepStatement.setString(1, "student 3"); 
	    prepStatement.setLong(2, 2);
	    prepStatement.setDate(3, date_reg);
	    prepStatement.executeUpdate();
	    
	    resultSet = statement.executeQuery(selectQuery);
	    System.out.println("\nAfter the insert, data is...\n");
	    counter=1;
	    while(resultSet.next()) {
		System.out.println("Record " + counter + ": " +
				   resultSet.getString(1) + ", " +
				   resultSet.getLong(2) + ", " +
				   resultSet.getDate(3));
		counter ++;
	    }  
	    resultSet.close();
	}
	catch(SQLException Ex) {
	    System.out.println("Error running the sample queries.  Machine Error: " +
			       Ex.toString());
	} catch (ParseException e) {
		System.out.println("Error parsing the date. Machine Error: " +
		e.toString());
	}
	finally{
		try {
			if (statement != null) statement.close();
			if (prepStatement != null) prepStatement.close();
		} catch (SQLException e) {
			System.out.println("Cannot close Statement. Machine error: "+e.toString());
		}
	}
 }
    

    /*
     * @desc: Multi-version concurrency control in Oracle
     */

    ///////////////////EXAMPLE 1////////////////////////
    public void Example1() {
	try {
	    connection.setAutoCommit(false); //the default is true and every statement executed is considered a transaction.
	    connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
	    statement = connection.createStatement();
	    
	    query = "update class set max_num_students = 5 where classid = 1";
	    int result = statement.executeUpdate(query); 
	    
	    //sleep for 5 seconds, so that we have time to switch to the other transaction
	    Thread.sleep(5000);
	    
	    query ="select * from CLASS";
	    ResultSet resultSet =statement.executeQuery(query);
	    System.out.println("ClassID\tMAX_NUM_STUDENTS\tCUR_NUM_STUDENTS");
	    while(resultSet.next())
	    {
	    	System.out.println(resultSet.getLong(1)+"\t"+resultSet.getLong(2)+"\t"+resultSet.getDouble(3));
	    }
	    
	    /*
	     * Releases this ResultSet object's database and JDBC resources immediately instead of waiting for this to happen when it is automatically closed.
	     */
	    resultSet.close();
	    //now rollback to end the transaction and release the lock on data. 
	    //You can use connection.commit() instead for this example, I just don't want to change the value
	    connection.rollback();
	    System.out.println("Transaction Rolled Back!");
	}	
	catch(Exception Ex)  
	{
		System.out.println("Machine Error: " +
				   Ex.toString());
	}
	finally{
		try {
			if (statement!=null) statement.close();
		} catch (SQLException e) {
			System.out.println("Cannot close Statement. Machine error: "+e.toString());
		}
	}
	
    }
    
    /*
     * @desc: Example2: (Implicit) Unrepeatable read problem
     * Example3: Serializable Is@rec7db.sqlolation Level
     */
    //////////EXAMPLE 2 + 3//////////////////////////////
    
    public void Example2(int mode ) {
	
	try {
	    connection.setAutoCommit(false); //the default is true and every statement executed is considered a transaction.
	    if ( mode == 2 ) 
		connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED); //which is the default
	    else 
		connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE); 
	    statement = connection.createStatement();

	
	    //read the maximum and current number of students in the class
	    query = "SELECT max_num_students, cur_num_students FROM class where classid = 1";
	    resultSet = statement.executeQuery(query);
	    
	    //note that there is no sleep here in this transaction
	    
	    int max, cur;
	    if(resultSet.next()) {	
		max = resultSet.getInt(1);
		cur = resultSet.getInt(2);
		System.out.println( "Max is: " + max + " Cur is: " + cur);
		//sleep for 5 seconds, so that we have time to switch to the other transaction
		Thread.sleep(5000);
		
		if(cur<max) {
		    
		    query = "update class set cur_num_students = cur_num_students +1 where classid = 1";
		    int result = statement.executeUpdate(query); 
		    if (result == 1) 
			System.out.println("Update is successful " + result);
		    else 
			System.out.println("No rows were updated");
		}
		else { 
		    System.out.println("The class is full");
		}
		
	    }  
	    connection.commit();
	    resultSet.close();
	}	
	catch(Exception Ex)  {
	    System.out.println("Machine Error: " +
			       Ex.toString());
	}
	finally{
		try {
			if (statement!=null) statement.close();
		} catch (SQLException e) {
			System.out.println("Cannot close Statement. Machine error: "+e.toString());
		}
	}
    }

    /*
     * @desc: Exclusive Lock acquired by using FOR UPDATE OF
     */
    ////// EXAMPLE 4 /////////////////////////////////////////////////////

    public void Example4() {
	try{
	    connection.setAutoCommit(false); //the default is true and every statement executed is considered a transaction.
	    connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED); //which is the default
	    statement = connection.createStatement();
	    
	    
	    //read the maximum and current number of students in the class
	    query = "SELECT max_num_students, cur_num_students "+
	    		"FROM class where classid = 1 "+
	    		"FOR UPDATE OF cur_num_students";
	    resultSet = statement.executeQuery(query);
	    
	    //note that there is no sleep here in this transaction
	    
	    int max, cur;
	    if(resultSet.next()) {	
		max = resultSet.getInt(1);
		cur = resultSet.getInt(2);
		System.out.println( "Max is: " + max + " Cur is: " + cur);
		//sleep for 5 seconds, so that we have time to switch to the other transaction
		Thread.sleep(5000);
		
		if(cur<max) {

		    query = "update class set cur_num_students = cur_num_students +1 where classid = 1";
		    int result = statement.executeUpdate(query); 
		    
		    if (result == 1) 
			System.out.println("Update is successful " + result);
		    else 
			System.out.println("No rows were updated");
		}
		else{ System.out.println("The class is full");}

	    }
	
	    //We need this because the connection was set with auto-commit=false
	    connection.commit();
	    resultSet.close();
	}	
	catch(Exception Ex) {
	    System.out.println("Machine Error: " +
			       Ex.toString());
	}
	finally{
		try {
			if (statement !=null) statement.close();
		} catch (SQLException e) {
			System.out.println("Cannot close Statement. Machine error: "+e.toString());
		}
	}
   }

	// //// EXAMPLE 5 /////////////////////////////////////////////////////

	public void Example5() {
		try {
			connection.setAutoCommit(false); // the default is true and every
												// statement executed is
												// considered a transaction.
			statement = connection.createStatement();

			query = "update class set max_num_students = 10 where classid = 1";
			int result = statement.executeUpdate(query);
			if (result == 1)
				System.out.println("Update1 is successful " + result);
			else
				System.out.println("No rows were updated");

			Thread.sleep(5000);

			query = "update class set max_num_students = 10 where classid = 2";
			result = statement.executeUpdate(query);

			if (result == 1)
				System.out.println("Update2 is successful " + result);
			else
				System.out.println("No rows were updated");

			connection.commit();
			
		} catch (Exception Ex) {
			System.out.println("Machine Error: " + Ex.toString());
		}
		finally{
			try {
				if (statement!=null) statement.close();
			} catch (SQLException e) {
				System.out.println("Cannot close Statement. Machine error: "+e.toString());
			}
		}

	}
    
  public static void main(String args[]) throws SQLException
  {
    /* Making a connection to a DB causes certain exceptions.  In order to handle
	   these, you either put the DB stuff in a try block or have your function
	   throw the Exceptions and handle them later.  For this demo I will use the
	   try blocks */

    String username, password;
	username = "mss133"; //This is your username in oracle
//	username = "username"; This is your username in oracle
	password = "4028362"; //This is your password in oracle
//	password = "password"; This is your password in oracle
	
	
	try{
	    // Register the oracle driver.  
	    DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
	    
	    //This is the location of the database.  This is the database in oracle
	    //provided to the class
	    String url = "jdbc:oracle:thin:@class3.cs.pitt.edu:1521:dbclass"; 
	    
	    //create a connection to DB on class3.cs.pitt.edu
	    connection = DriverManager.getConnection(url, username, password); 
	    TranDemo1 demo = new TranDemo1(Integer.parseInt(args[0]));
	    
	}
	catch(Exception Ex)  {
	    System.out.println("Error connecting to database.  Machine Error: " +
			       Ex.toString());
	}
	finally
	{
		/*
		 * NOTE: the connection should be created once and used through out the whole project;
		 * Is very expensive to open a connection therefore you should not close it after every operation on database
		 */
		connection.close();
	}
  }
}
