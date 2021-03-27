/**
 * the Registration program implements an application that edits the INVENTORY database in mySQL
 *
 * @author Adam Abouelhassan
 * @version 1.1
 * @since 1.0
 */

import java.sql.*;
import java.io.*;

class Inventory {
    public final String DBURL = "jdbc:mysql://localhost/inventory";       // store the database url information
    public final String USERNAME = "root";         // store the user's account username
    public final String PASSWORD = "kaumil";       // store the user's account password

    private Connection dbConnect;
    private ResultSet results;

    private String category;
    private String type;
    private int amount;

    public static void main(String[] args) {
        Inventory test = new Inventory();
        test.initializeConnection();

        BufferedReader reader = new BufferedReader(         // for reading input
                new InputStreamReader(System.in));
        String entry = null;
        try {
            System.out.print("User request:");
            entry = reader.readLine();                     // read input line
        } catch (IOException e) {
            e.printStackTrace();
        }
        test.AnalyzeEntry(entry);
    }

    public Inventory() {
        String DBURL = "jdbc:mysql://localhost/inventory";
        String USERNAME = "adam";
        String PASSWORD = "ensf409";
    }

    // getter
    public String getDBURL() {
        return DBURL;
    }

    // getter
    public String getUSERNAME() {
        return USERNAME;
    }

    // getter
    public String getPASSWORD() {
        return PASSWORD;
    }

    // Initialize connection to mysql
    public void initializeConnection() {
        try {
            dbConnect = DriverManager.getConnection(this.DBURL, this.USERNAME, this.PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void AnalyzeEntry(String entry) {
    	
    }

    public void pullData() {
        try {                    
            Statement myStmt = dbConnect.createStatement();
            results = myStmt.executeQuery("SELECT * FROM "+category+" WHERE type = "+type);
            
            while (results.next()){
                System.out.println("Print results: " + results.getString("id"));
            }
            myStmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }






}
