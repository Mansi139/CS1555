/*
 Written by Thao N. Pham.
 Updated by: Lory Al Moakar, Roxana Georghiu, Nick R. Katsipoulakis, Xiaoyu Ge
 Purpose: Demo JDBC for CS1555 Class
 
 IMPORTANT (otherwise, your code may not compile)
 Same as using sqlplus, you NEED TO SET oracle environment variables by
 sourcing bash.env or tcsh.env
 */
import java.util.*;
import java.sql.*;  //import the file containing definitions for the parts
import java.text.ParseException;
//needed by java for database connection and manipulation

public class CS1555_Project {
    private static Connection connection; //used to hold the jdbc connection to the DB
    private Statement statement; //used to create an instance of the connection
    private PreparedStatement prepStatement; //used to create a prepared statement, that will be later reused
    private ResultSet resultSet; //used to hold the result of your query (if one
    // exists)
    private String query;  //this will hold the query we are using
    
    
    public CS1555_Project(String login, int example_no) {
        
        switch ( example_no) {
            case 0:
            case 1:
                browsingMutualFund(login, example_no);
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
    
    /////////////////EXAMPLE 0 //////////////////////////
    public void browsingMutualFund(String login, int choice) {
        
        int counter = 1;
        /*We will now perform a simple query to the database, asking for all the
         records it has.  For your project, performing queries will be similar*/
        try{
            /**
             /**
             1)Customers are allowed to look at the list of all mutual funds
               String selectQuery = "select * from MUTUALFUND";
             2)Or those in a category of their choice;
                allocation join prefers using allocation_no
                "select symbol from allocation join prefers on allocation_no"
             3)they can also ask for funds to be sorted on the highest price for a given date, or alphabetically by fund name.
             "select symbol from closingPrice order by price DESC"
             "selecy symbol from closingPrice order by symbol ASC?;
             
             */
            
            /*
             Searching mutual fund by text
             Customers can specify up to two keywords; our system has to return the products that contain
             ALL these keywords in their mutual fund description.
             
             
             **/
            
            if(choice == 0){
            statement = connection.createStatement(); //create an instance
            String selectQuery = "select * from MUTUALFUND"; //sample query
            resultSet = statement.executeQuery(selectQuery); //run the query on the DB table
            
                System.out.println("size: " + resultSet.getFetchSize());
                while (resultSet.next()){
                   
                    System.out.println("Record " + counter + ": " +
                                       resultSet.getString(1) + ", " +
                                       resultSet.getString(2) + ", " +
                                       resultSet.getString(3) + ", " +
                                       resultSet.getString(4) + ", " +
                                       resultSet.getDate(5)); //since type date, getDate.
                    counter++;
                }
            }
            
             resultSet.close();
        }
        catch(SQLException Ex) {
            System.out.println("Error running the sample queries.  Machine Error: " +
                               Ex.toString());
        } /**catch (ParseException e) {
           System.out.println("Error parsing the date. Machine Error: " +
           e.toString());
           }*/
        finally{
            try {
                if (statement != null) statement.close();
                if (prepStatement != null) prepStatement.close();
            } catch (SQLException e) {
                System.out.println("Cannot close Statement. Machine error: "+e.toString());
            }
        }
    }
    
    public static int showSubMenu(int n){
        Scanner keyboard = new Scanner(System.in);
        int res = 0;
        
        if(n == 0){
            System.out.println("0. Look at list of all mutual funds");
            
            res = keyboard.nextInt();
        }else if(n == 1){
        }else if(n == 2){
        }else if(n == 3){
        }else if(n == 4){
        }else if(n == 5){
        }else if(n == 6){
        }else if(n == 7){
        }else{
            System.out.println("Invalid choice!");
        }
        
        return res;
    }
    
    
    public static void main(String args[]) throws SQLException
    {
        Scanner keyboard = new Scanner(System.in);
        
        System.out.println("Press 0 for administrator and 1 for customers");
        int mainChoice = keyboard.nextInt();
        int choice = 0, c = 0;
        String login = "";
        
        if(mainChoice == 0){
            //show administrator login
        }else if(mainChoice == 1){
            //show customer login
            System.out.println("Login: ");
            keyboard.nextLine();
            login = keyboard.nextLine();
            
            System.out.println("Choose any one of the options: ");
            System.out.println("0. Browsing mutual fund");
            System.out.println("1. Searching mutual fund by text");
            System.out.println("2. Investing");
            System.out.println("3. Selling shares");
            System.out.println("4. Buying shares");
            System.out.println("5. Conditional investment");
            System.out.println("6. Changing the allocation preference");
            System.out.println("7. Customer portofolio");
            
            c = keyboard.nextInt();
            
            choice = showSubMenu(c);
            
        }else{
            System.out.println("Invalid option");
        }
        
        /* Making a connection to a DB causes certain exceptions.  In order to handle
         these, you either put the DB stuff in a try block or have your function
         throw the Exceptions and handle them later.  For this demo I will use the
         try blocks */
      
        String username, password;
        username = "mat185"; //This is your username in oracle
        password = "3912590"; //This is your password in oracle
        
        try{
            
            // Register the oracle driver.
            DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
            
            //This is the location of the database.  This is the database in oracle
            //provided to the class
            String url = "jdbc:oracle:thin:@class3.cs.pitt.edu:1521:dbclass";
            
            //create a connection to DB on class3.cs.pitt.edu
            connection = DriverManager.getConnection(url, username, password);
            
            CS1555_Project demo = new CS1555_Project(login,choice);
            connection.close();
            
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
