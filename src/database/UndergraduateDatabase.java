package database;

import java.sql.*;

public class UndergraduateDatabase {

    private static final String DB_URL = "jdbc:derby:src/database/undergraduate;create=true";
    private static Connection connect = null;
    private static Statement statement = null;
    private static ResultSet result = null;


    public static String createConnection() {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            connect = DriverManager.getConnection(DB_URL);
            return "Connected!";
        } catch (Exception e) {
            return e.getMessage() + "... Error connecting to Database";
        }
    }

    public static ResultSet executeQuery(String query) {
        return executeSpecificQuery(query);
    }

    public static ResultSet getGradeTuple(String grade) {
        String query = "Select * from Grade where grade = ";
        query += "'" + grade + "'";
        return executeSpecificQuery(query);
    }

    public static ResultSet getCourseTuple(String courseCode) {
        String query = "Select * from Course where code = ";
        query += "'" + courseCode + "'";
        return executeSpecificQuery(query);
    }

    private static ResultSet executeSpecificQuery(String query) {
        try {
            statement = connect.createStatement();
            result = statement.executeQuery(query);
            return result;
        } catch (SQLException e) {
            System.out.println("Exception at executeQuery:DataHandler " + e.getLocalizedMessage());
            return null;
        } finally { }
    }

    public static void addCourseToTable (String code, String title, int level, String programme) {
        String query ="INSERT INTO COURSE (CODE, TITLE, LEVEL, PROGRAMME) VALUES ( '" +  code + "', '" + title + "', " +  level + ", '" + programme + "')";
        executeAction(query);
    }

    private static void executeAction(String query) {
        try {
            statement = connect.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            System.out.println("Error occurred executeAction! " + e.getLocalizedMessage() );
        }
    }
}
