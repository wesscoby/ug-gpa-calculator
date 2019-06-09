package database;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CourseProperty {
    private final SimpleStringProperty courseCode = new SimpleStringProperty("");
    private final SimpleStringProperty courseTitle = new SimpleStringProperty("");
    private final SimpleStringProperty grade = new SimpleStringProperty("");
    private final SimpleDoubleProperty gradePointTotal = new SimpleDoubleProperty(0);
    private final SimpleDoubleProperty gradePoint = new SimpleDoubleProperty(0);
    private final SimpleIntegerProperty credit = new SimpleIntegerProperty(3);

    public CourseProperty() throws SQLException {
        this("", "");
    }


    public CourseProperty(String courseCode, String gradeLetter) throws SQLException {
        ResultSet result;
        result = UndergraduateDatabase.getCourseTuple(courseCode);
        while (result.next()) {
            setCourseCode(result.getString("code"));
            setCourseTitle(result.getString("title"));
            setCredit(result.getInt("credit"));
        }

        result = UndergraduateDatabase.getGradeTuple(gradeLetter);
        while (result.next()) {
            setGrade(result.getString("grade"));
            setGradePoint(result.getDouble("gradepoint"));
            setGradePointTotal(getGradePoint() * getCredit());
        }
    }

    public double getGradePointTotal() {
        return gradePointTotal.get();
    }

    public SimpleDoubleProperty gradePointTotalProperty() {
        return gradePointTotal;
    }

    private void setGradePointTotal(double gradePointTotal) {
        this.gradePointTotal.set(gradePointTotal);
    }

    public String getCourseCode() {
        return courseCode.get();
    }

    public SimpleStringProperty courseCodeProperty() {
        return courseCode;
    }

    private void setCourseCode(String courseCode) {
        this.courseCode.set(courseCode);
    }

    public String getCourseTitle() {
        return courseTitle.get();
    }

    public SimpleStringProperty courseTitleProperty() {
        return courseTitle;
    }

    private void setCourseTitle(String courseTitle) {
        this.courseTitle.set(courseTitle);
    }

    public String getGrade() {
        return grade.get();
    }

    public SimpleStringProperty gradeProperty() {
        return grade;
    }

    private void setGrade(String grade) {
        this.grade.set(grade);
    }

    public int getCredit() {
        return credit.get();
    }

    public SimpleIntegerProperty creditProperty() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit.set(credit);
    }

    public double getGradePoint() {
        return gradePoint.get();
    }

    public SimpleDoubleProperty gradePointProperty() {
        return gradePoint;
    }

    public void setGradePoint(double gradePoint) {
        this.gradePoint.set(gradePoint);
    }
}
