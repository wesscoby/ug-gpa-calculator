package database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TestDB {
    public static void main(String[] args) throws SQLException {
        UndergraduateDatabase.createConnection();
//      UndergraduateDatabase.addCourse("CSIT301", "Accounting", 300, "CSIT");
//        UndergraduateDatabase.removeCourse("CSIT301");
        ResultSet result;
        result = UndergraduateDatabase.executeQuery("SELECT * FROM Course where level = 200 AND PROGRAMME = 'CSIT'");
        int count = 1;
        while (result.next()) {
            System.out.printf("%-5d %-20s %-5s %-11s %-60s \n ", count,
                    result.getString("programme"),
                    result.getString("level"),
                    result.getString("code"),
                    result.getString("title")
            );
//            System.out.println(result.getString("coursetitle"));
            count++;
        }
//        result = UndergraduateDatabase.getGradeTuple("B+");
//        while (result.next()) {
//            System.out.println(result.getString("grade") + ",   " + result.getDouble("gradepoint") + ",    " + result.getString("interpretation"));
//        }
//        result = UndergraduateDatabase.getCourseTuple("CSIT104");
//        while (result.next()) {
//            System.out.println(result.getString("code") + ",   " + result.getString("title") );
//        }

//        UndergraduateDatabase.addCourse("BSC", "Business Administration", 100, "UGBS104", "Principles of Management");
    }
}