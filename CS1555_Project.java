/*
 Written by Manasi Thakkar
 */

import java.util.*;
import java.sql.*;  //import the file containing definitions for the parts
import java.text.ParseException;
import java.sql.CallableStatement;
//needed by java for database connection and manipulation

public class CS1555_Project {
    private static Connection connection; //used to hold the jdbc connection to the DB
    private Statement statement; //used to create an instance of the connection
    private PreparedStatement prepStatement; //used to create a prepared statement, that will be later reused
    private ResultSet resultSet; //used to hold the result of your query (if one
    // exists)
    private String query;  //this will hold the query we are using
    
    public CS1555_Project(Scanner keyboard, String login, int example_no) throws SQLException, ParseException {
        
        switch ( example_no) {
            case 0:
            case 1:
                browsingMutualFund(login, example_no);
                break;
            case 8:
                newCust(keyboard);
                break;
            case 9:
                updateShareQuotes(keyboard);
                break;
            case 10:
                addMutualFund(keyboard);
                break;
            case 11:
                updateTimeDate(keyboard);
                break;
            case 12:
                stats(keyboard);
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
    
    public CS1555_Project(String login, int example_no) {
        
 
        switch ( example_no) {
            case 0:
            case 1:
                browsingMutualFund(login, example_no);
                break;
            case 4:
                buyShare(login);
                break;
            case 5:
                conditionalInvestment(login);
            case 6:
                changePreference(login);
                break;
            case 7:
                customerPortfolio(login);
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
    public CS1555_Project(String login, String s1, String s2){
        searchMutualFund(login,s1,s2);
        
    }
    
    public CS1555_Project(String login){
        changePreference(login);
        
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
    
    public void stats(Scanner input) throws SQLException, ParseException {
        
        
        System.out.println("Enter the date you would like to start at as mm-dd-yyy");
        String date = input.nextLine();
        
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("MM-dd-yyyy");
        java.sql.Date date_reg = new java.sql.Date (df.parse(date).getTime());
        
        System.out.print("How many rows? ");
        int k = input.nextInt();
        input.nextLine();
        
        query = "SELECT * FROM (Select category, sum(num_shares) FROM FUNDSBOUGHT ";
        query += "WHERE t_date >= ?  GROUP BY category ORDER BY sum(num_shares) desc)";
        query += "WHERE rownum <= ?";
        
        prepStatement = connection.prepareStatement(query);
        prepStatement.setDate(1, date_reg);
        prepStatement.setInt(2, k);
        resultSet = prepStatement.executeQuery();
        
        System.out.println("\nHighest " + k + " Volume Categories");
        System.out.printf("%-10S %-10S%n", "CATEGORY", "SHARES");
        
        while(resultSet.next()) {
            System.out.printf("%-10s %-10S%n", resultSet.getString(1), resultSet.getInt(2));
        }
        
        query = "SELECT * FROM (SELECT login, sum(amount) FROM TRXLOG WHERE action = ";
        query += "'buy' AND t_date >= ? GROUP BY login ORDER BY sum(amount) desc)";
        query += "WHERE rownum <= ?";
        
        prepStatement = connection.prepareStatement(query);
        prepStatement.setDate(1, date_reg);
        prepStatement.setInt(2, k);
        resultSet = prepStatement.executeQuery();
        
        System.out.println("\nTop " + k + " Investors");
        System.out.printf("%-10S %-10S%n", "USER", "AMOUNT");
        
        while(resultSet.next()) {
            System.out.printf("%-10s %-10S%n", resultSet.getString(1), resultSet.getDouble(2));
        }
        connection.commit();
        
    }

    
    public void updateTimeDate(Scanner input) throws SQLException, ParseException {
        
        //Enter date
        
        System.out.println("Enter the date as mm-dd-yyy");
        String date = input.nextLine();
        
        
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("MM-dd-yyyy");
        java.sql.Date date_reg = new java.sql.Date (df.parse(date).getTime());
        
        query = "update MUTUALDATE set c_date = ?";
        //Update database
        
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
        
        
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("mm-dd-yyyy");
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

    
    public void updateShareQuotes(Scanner input) throws SQLException, ParseException {
        
        
        System.out.println("Enter the date as: mm-dd-yyyy");
        String date = input.nextLine();
        
        query = "select symbol,price from CLOSINGPRICE where p_date = ?";
        
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("mm-dd-yyyy");
        java.sql.Date date_reg = new java.sql.Date (df.parse(date).getTime());
        
        
        prepStatement = connection.prepareStatement(query);
        prepStatement.setDate(1, date_reg);
        
        resultSet = prepStatement.executeQuery();
        
        
        
        while (resultSet.next()) {
            
            String symbol = resultSet.getString(1);
            
            System.out.println("Enter new price");
            float price = input.nextFloat();
            
            
            query = "update CLOSINGPRICE set price = ? where p_date = ? and symbol = ?";
            
            prepStatement = connection.prepareStatement(query);
            prepStatement.setFloat(1, price);
            prepStatement.setString(2, symbol);
            prepStatement.setDate(3, date_reg);
            prepStatement.executeUpdate();
            
        }
        
        connection.commit();
    }

    
    public void buyShare(String in){
        System.out.println("helloooooo111");
        
        Scanner keyboard = new Scanner(System.in);
        
        System.out.println("    0. Provide mutualfund Symbol and number of shares");
        System.out.println("    1. Provide mutualfund symbol and amount");
        
        int whichOne = keyboard.nextInt();
        
        System.out.println("Enter mutual fund: ");
        keyboard.next();
        String mutualFund = keyboard.nextLine();
        try{
            if(whichOne == 0){
                
                
                System.out.println("Enter number of shares: ");
                int numShares = keyboard.nextInt();
                
                CallableStatement cStmt = connection.prepareCall("{call buyShare(?, ?, ?)}");
                
                cStmt.setString(1,in );
                cStmt.setString(2,mutualFund );
                cStmt.setInt(3,numShares );
                
                cStmt.executeUpdate();
                
                
            }else if(whichOne == 1){
            }else{
                System.out.println("Invalid option");
            }
        }catch(SQLException Ex) {
            System.out.println("Error running the sample queries.  Machine Error: " +
                               Ex.toString());
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
    public void customerPortfolio(String in){
        
        System.out.println("customer portfolio");
        
        try{
            Scanner keyboard = new Scanner(System.in);
            System.out.println("Enter date (MM/DD/YY) for which you would like to create portfolio: ");
            
            String date = keyboard.nextLine();
            
            //report mutualfund info for this user at this date:
            /**For a specific date, the report will list the mutual funds the customer owns shares of: their symbols, their prices, the number of shares, their current values on the specific date, the cost value and the yield as well as the total value of the portfolio*/
            
            //how to get The cost value of a stock is the total amount paid to purchase the shares?
            
            
            statement = connection.createStatement(); //create an instance
            System.out.println("\n");
            
            //TODO change date as a variable
            String selectQuery = " select first.login,first.symbol,first.shares,cp.price,cp.p_date from (select * from OWNS where login='mike') first JOIN closingPrice cp on first.symbol = cp.symbol where cp.p_date < TO_DATE('04-FEB-14','DD-MM-YY')"; //sample query
            
            resultSet = statement.executeQuery(selectQuery); //run the query on the DB table
            
            while (resultSet.next()){
                
                System.out.println("Login: " + resultSet.getString(1) + ", " +
                                   "Symbol: " + resultSet.getString(2) + ", " +
                                   "Shares: " + resultSet.getInt(3) + ", " +
                                   "Price: " + resultSet.getFloat(4) + ", " +
                                   "current value: " + (resultSet.getInt(3)) * (resultSet.getFloat(4)) + ", " +
                                   "Date: " + resultSet.getDate(5)); //since type date, getDate.
            }
        }catch(Exception Ex) {
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
    //buy sub, sell add,
    public void conditionalInvestment(String in){
        
        Scanner keyboard = new Scanner(System.in);
        
        System.out.println("Which investment action to perform: ");
        String s1 = keyboard.nextLine();
        
        
        
        //call a procedure from here.
        /**   cs = this.con.prepareCall("{call SHOW_SUPPLIERS()}");
         ResultSet rs = cs.executeQuery();
         
         while (rs.next()) {
         String supplier = rs.getString("SUP_NAME");
         String coffee = rs.getString("COF_NAME");
         System.out.println(supplier + ": " + coffee);
         }
         
         */
        
    }
    
    public void changePreference(String in){
        Scanner keyboard = new Scanner(System.in);
        PreparedStatement prepStatement = null;
        
        try{
            
            //first ask the user for mutual fund and its percentage
            System.out.println("Enter mutual fund symbol");
            String getMutualFund = keyboard.nextLine();
            
            System.out.println("Enter percentage that you would like to update");
            float a = keyboard.nextFloat();
            
            int saveNum = 0;
            
            statement = connection.createStatement(); //create an instance
            System.out.println("user: " + in);
            
            String selectQuery = " select * from allocation where login = '"+ in + "' order by p_date desc fetch first 1 row only"; //sample query
            resultSet = statement.executeQuery(selectQuery); //run the query on the DB table
            
            System.out.println("size: " + resultSet.getFetchSize());
            while (resultSet.next()){
                System.out.println(
                                   resultSet.getInt(1) + ", " +
                                   resultSet.getString(2) + ", " +
                                   resultSet.getDate(3)); //since type date, getDate.
                
                
                saveNum =resultSet.getInt(1);
            }
            
            System.out.println("allocation : " + saveNum);
            
            
            String selectQuery2 = " update PREFERS SET percentage=0.1 where allocation_no = " +  saveNum  + " and symbol= '" + getMutualFund + "' ";
            resultSet = statement.executeQuery(selectQuery2); //run the query on the DB table
            
            System.out.println("size: " + resultSet.getFetchSize());
            
            
        }catch(Exception Ex) {
            System.out.println("Error running the sample queries.  Machine Error: " +
                               Ex.toString());
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
    
    public void browsingMutualFund(String in, int choice) {
        
        int counter = 1;
        /*We will now perform a simple query to the database, asking for all the
         records it has.  For your project, performing queries will be similar*/
        try{
            
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
            }else if(choice == 1){
                
                /** first get allocation_no for my login using ALLOCATION
                 then for that login get symbol using PREFERS
                 */
                //TODO1: how to check string against variable.
                
                statement = connection.createStatement(); //create an instance
                System.out.println("\n");
                String selectQuery = " select login, symbol,percentage from allocation a join prefers p on a.allocation_no = p.allocation_no where a.login = 'mike'"; //sample query
                
                resultSet = statement.executeQuery(selectQuery); //run the query on the DB table
                
                
                while (resultSet.next()){
                    
                    System.out.println(
                                       resultSet.getString(1) + ", " +
                                       resultSet.getString(2) + ", " +
                                       resultSet.getFloat(3)); //since type date, getDate.
                    counter++;
                }
                
                System.out.println("\n");
                System.out.println("        0. Sort it on the highest price for a given date");
                System.out.println("        1. Sort it alphabetically by fund name");
                
                Scanner keyboard = new Scanner(System.in);
                int subChoice = keyboard.nextInt();
                
                
                
                if(subChoice == 0){
                    
                    //TODO:2 change mike here as well
                    //TODO:3 closing price is the price we want?
                    
                    System.out.println("\n");
                    String sortQuery = " select login,sy,price,percentage  from (select login, symbol as sy, percentage from allocation a join prefers p on a.allocation_no = p.allocation_no where a.login = 'mike') np INNER JOIN closingPrice CP on np.sy=cp.symbol order by cp.price DESC";
                    
                    resultSet = statement.executeQuery(sortQuery); //run the query on the DB table
                    
                    while (resultSet.next()){
                        
                        System.out.println(
                                           resultSet.getString(1) + ", " +
                                           resultSet.getString(2) + ", " +
                                           resultSet.getFloat(3) + ", " +
                                           resultSet.getFloat(4)); //since type date, getDate.
                        counter++;
                    }
                    System.out.println("\n");
                    
                }else if( subChoice == 1){
                    //sort it alphabetically by fund name
                    
                    System.out.println("\n");
                    
                    String sortQuery = " select login,sy,price,percentage  from (select login, symbol as sy, percentage from allocation a join prefers p on a.allocation_no = p.allocation_no where a.login = 'mike') np INNER JOIN closingPrice CP on np.sy=cp.symbol order by cp.symbol ASC";
                    
                    
                    resultSet = statement.executeQuery(sortQuery); //run the query on the DB table
                    
                    while (resultSet.next()){
                        
                        System.out.println(resultSet.getString(1) + ", " +
                                           resultSet.getString(2) + ", " +
                                           resultSet.getFloat(3) + ", " +
                                           resultSet.getFloat(4)); //since type date, getDate.
                        counter++;
                    }
                    System.out.println("\n");
                    
                }else{
                    System.out.println("Invalid option");
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
    public void searchMutualFund(String in, String s1, String s2){
        
        try{
            statement = connection.createStatement(); //create an instance
            System.out.println("\n");
            
            //TODO: 4 how to set it to variable = s1
            String searchQuery = "select name, symbol,description, category,c_date from mutualFund where description LIKE '%market%'"; //sample query
            
            resultSet = statement.executeQuery(searchQuery); //run the query on the DB table
            
            
            while (resultSet.next()){
                
                System.out.println(
                                   resultSet.getString(1) + ", " +
                                   resultSet.getString(2) + ", " +
                                   resultSet.getString(3) + ", " +
                                   resultSet.getString(4) + ", " +
                                   resultSet.getDate(5)); //since type date, getDate.
            }
            
            
            //TODO: 4 how to set it to variable = s2
            String searchQuery2 = "select name, symbol,description, category,c_date from mutualFund where description LIKE '%real%'"; //sample query
            
            resultSet = statement.executeQuery(searchQuery2); //run the query on the DB table
            
            
            while (resultSet.next()){
                
                System.out.println(
                                   resultSet.getString(1) + ", " +
                                   resultSet.getString(2) + ", " +
                                   resultSet.getString(3) + ", " +
                                   resultSet.getString(4) + ", " +
                                   resultSet.getDate(5)); //since type date, getDate.
            }
            resultSet.close();
            System.out.println("\n");
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
    
    public static void main(String args[]) throws SQLException
    {
        Scanner keyboard = new Scanner(System.in);
        
        System.out.println("Press 0 for administrator and 1 for customers");
        int mainChoice = keyboard.nextInt();
        int choice = 0, c = 0;
        String login = "",  temp1 = "",  temp2 = "";
        
        if(mainChoice == 0){
            //show administrator login
            System.out.println("Login: ");
            keyboard.nextLine();
            login = keyboard.nextLine();
            
            System.out.println();
            System.out.println("Choose any one of the options: ");
            System.out.println("8. New Customer Registration");
            System.out.println("9. Updating share quotes for a day");
            System.out.println("10. Add new mutual fund");
            System.out.println("11. Update time and date");
            System.out.println("12. Show Statistics");
            System.out.println();
            
            c = keyboard.nextInt();
            
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
            
            
            if(c == 0){
                System.out.println("    0. Look at list of all mutual funds");
                System.out.println("    1. List mutual funds in a category of your choice");
                
                choice = keyboard.nextInt();
                
                
            }else if(c == 1){
                
                System.out.println("    Specify up to two keywords to search mutual fund: ");
                keyboard.nextLine();
                String s= keyboard.nextLine();
                
                String[] a = s.split(" ");
                temp1 = a[0];
                temp2 = a[1];
                System.out.println("1st: " + a[0]);
                System.out.println("2nd: " + a[1]);
                
                
            }else if(c == 2){
                //investing.
                // CallableStatement cStmt = conn.prepareCall("{call demoSp(?, ?)}");
                
            }else if(c == 3){
                choice = 3;
                //selling
                
            }else if(c == 4){
                //buying for the first one
                choice = 4;
                
                // CallableStatement cStmt = conn.prepareCall("{call demoSp(?, ?)}");
                //cStmt.setString(1, "abcdefg");
            }else if(c == 5){
                choice = 5;
                //conditional investment
                //First, a customer may set up a date, so that on that date,
                //certain investment action (invest/buy/sell) will be executed
                //allocation -> p_date
                choice = 5;
            }else if(c == 6){
                //allocation preference
                
                choice = 6;
                
            }else if(c == 7){
                //customer portofloia
                //needs just the login
                choice = 7;
            }else{
                System.out.println("Invalid choice!");
            }
            
            
            //choice = showSubMenu(c);
            
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
            
            if(c == 0 || c == 6 || c == 5 || c == 7 || c == 4){
                System.out.println("c: " + c);
                CS1555_Project demo = new CS1555_Project(login,choice);
            }else if(c ==1 ){
                CS1555_Project demo = new CS1555_Project(login,temp1, temp2);
                
            }
            else{
                System.out.println("Invalid!!!");
            }
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

