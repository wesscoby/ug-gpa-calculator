package main;

import database.CourseProperty;
import database.DegreeClass;
import database.UndergraduateDatabase;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.controlsfx.control.StatusBar;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.CHECK_SQUARE_ALT;
import static de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.INFO_CIRCLE;

public class Controller implements Initializable{

    @FXML private TableView<CourseProperty> tableView;
    @FXML private  ChoiceBox<String> selectProgramme, selectCode, selectGrade;
    @FXML private ChoiceBox<Integer> selectLevel;
    @FXML private Label cgpaLabel, fgpaLabel, degClassLabel;
    @FXML private FontAwesomeIconView statusIcon;
    @FXML private StatusBar statusBar;


    @FXML protected void addCourse() { // button action
        try {
            if (selectCode.getValue() == null || selectGrade.getValue() == null) {
                displayStatus("Please choose a Course Code and Grade", INFO_CIRCLE, Color.RED);
            } else {
                CourseProperty c = new CourseProperty(selectCode.getValue(), selectGrade.getValue());
                tableView.getItems().add(c);
                displayStatus(c.getCourseCode() + " added. CGPA Updated.", CHECK_SQUARE_ALT, Color.BLACK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            displayStatus("Error!", INFO_CIRCLE, Color.RED);
        } finally {
            selectCode.setValue(null);
            selectGrade.setValue(null);
        }
    }

    private void displayStatus(String updateMessage, FontAwesomeIcon icon, Color color) {
        statusBar.setText(updateMessage);
        statusIcon.setIcon(icon);
        statusIcon.setFill(color);
    }

    public void displayGPA() { // button action
        if (getCGPA() == 0) {
            displayStatus("No content in the table!", INFO_CIRCLE, Color.RED);
        } else {
            cgpaLabel.setVisible(true);
            cgpaLabel.setText("CGPA: " + getCGPA());
            fgpaLabel.setText("FGPA: " + getFGPA());
            degClassLabel.setText("DEGREE CLASS: " + getDegreeClass());
            displayStatus("CGPA Calculated!", INFO_CIRCLE, Color.BLUE);
            displayFinalStatus();
        }
    }

    private void displayFinalStatus() {
        if(getFGPA().equals("N/A")) {
            fgpaLabel.setVisible(false);
            degClassLabel.setVisible(false);
        } else {
            fgpaLabel.setVisible(true);
            degClassLabel.setVisible(true);
        }
    }

    private double getCGPA() {
        double cct = 0;
        double cgpt = 0;
        double cgpa;
        int row = 0;
        while (row < tableView.getItems().size()) {
            cct += tableView.getItems().get(row).getCredit();
            cgpt += tableView.getItems().get(row).getGradePointTotal();
            row++;
        }
        cgpa = Math.round((cgpt / cct) * 100.0) / 100.0;
        return cgpa;
    }

    private String getFGPA() {
        double cct = 0;
        String fgpa;
        int row = 0;
        while (row < tableView.getItems().size()) {
            cct += tableView.getItems().get(row).getCredit();
            row++;
        }
        if (cct > 100) {
            fgpa = String.valueOf(getCGPA());
        } else {
            fgpa = "N/A";
        }
        return fgpa;
    }
    private String getDegreeClass() {
        if (getFGPA().equals("N/A")) {
            return getFGPA();
        } else {
            return deriveDegreeClass();
        }
    }

    private String deriveDegreeClass() {
        DegreeClass degClass = null;
        double num;
        num = Double.parseDouble(getFGPA());
        if (num >= 3.60 && num <= 4.0) {
            degClass = DegreeClass.FC;
        }
        else if (num >= 3.0 && num< 3.60) {
            degClass = DegreeClass.SCU;
        }
        else if (num >= 2.0 && num<= 2.99) {
            degClass = DegreeClass.SCL;
        }
        else if (num >= 1.50 && num<= 1.99) {
            degClass = DegreeClass.TC;
        }
        else if (num >= 1.00 && num <= 1.49) {
            degClass = DegreeClass.Pass;
        }
        else if (num >= 0 && num <= 0.99) {
            degClass = DegreeClass.Fail;
        }
        assert degClass != null;
        return degClass.printClass();
    }

    @FXML public void clearTable() { // button action
        tableView.getItems().clear();
        cgpaLabel.setVisible(false);
        fgpaLabel.setVisible(false);
        degClassLabel.setVisible(false);
        displayStatus("Data Cleared!", INFO_CIRCLE, Color.BLUE);
    }

    private void loadData() throws SQLException {
        ResultSet resultSet;
//    Programme
       resultSet = UndergraduateDatabase.executeQuery("SELECT PROGCODE FROM PROGRAMME");
       while(resultSet.next()) {
           selectProgramme.getItems().add(resultSet.getString("progcode"));
       }
//    Grade
        resultSet = UndergraduateDatabase.executeQuery("SELECT GRADE FROM GRADE");
        while (resultSet.next()) {
            selectGrade.getItems().add(resultSet.getString("grade"));
        }
    }

    public void selectedProgramme () throws SQLException {
        String query = "SELECT DISTINCT LEVEL FROM COURSE WHERE PROGRAMME = '" + selectProgramme.getSelectionModel().getSelectedItem() + "'";
        ResultSet resultSet = UndergraduateDatabase.executeQuery(query);
        while (resultSet.next()) {
            if(selectLevel.getItems().contains(resultSet.getInt("level"))) { continue; }
            selectLevel.getItems().add(resultSet.getInt("level"));
        }
        selectLevel.getItems().sorted();
    }

    public void selectedLevel() throws SQLException {
            selectCode.getItems().clear();
            String query = "SELECT CODE FROM COURSE WHERE PROGRAMME = '" + selectProgramme.getSelectionModel().getSelectedItem() + "' AND LEVEL = " + selectLevel.getSelectionModel().getSelectedItem();
            ResultSet resultSet = UndergraduateDatabase.executeQuery(query);
            int index = 0;
            while (resultSet.next()) {
                selectCode.getItems().add(index, resultSet.getString("code"));
                index++;
            }
    }

    private void inputTestData() throws SQLException {
            tableView.getItems().add(new CourseProperty("CSIT102", "A"));
            tableView.getItems().add(new CourseProperty("CSIT103", "A"));
            tableView.getItems().add(new CourseProperty("CSIT104", "B+"));
            tableView.getItems().add(new CourseProperty("CSIT105", "D"));
            tableView.getItems().add(new CourseProperty("CSIT107", "A"));
            tableView.getItems().add(new CourseProperty("CSIT201", "B+"));
            tableView.getItems().add(new CourseProperty("CSIT202", "B+"));
            tableView.getItems().add(new CourseProperty("UGRC210", "C+"));
            tableView.getItems().add(new CourseProperty("UGRC110", "D"));
            tableView.getItems().add(new CourseProperty("UGBS104", "B"));
            tableView.getItems().add(new CourseProperty("UGBS201", "C+"));
            tableView.getItems().add(new CourseProperty("CSIT203", "B"));
            tableView.getItems().add(new CourseProperty("CSIT205", "A"));
            tableView.getItems().add(new CourseProperty("CSIT207", "A"));
            tableView.getItems().add(new CourseProperty("CSIT206", "B+"));
            tableView.getItems().add(new CourseProperty("CSIT208", "A"));
            tableView.getItems().add(new CourseProperty("CSIT301", "B"));
            tableView.getItems().add(new CourseProperty("CSIT302", "A"));
            tableView.getItems().add(new CourseProperty("CSIT303", "C+"));
            tableView.getItems().add(new CourseProperty("CSIT304", "B"));
            tableView.getItems().add(new CourseProperty("CSIT305", "B+"));
            tableView.getItems().add(new CourseProperty("CSIT306", "B+"));
            tableView.getItems().add(new CourseProperty("CSIT307", "B+"));
            tableView.getItems().add(new CourseProperty("CSIT308", "B"));
            tableView.getItems().add(new CourseProperty("CSIT310", "C+"));
            tableView.getItems().add(new CourseProperty("CSIT312", "C"));
            tableView.getItems().add(new CourseProperty("CSIT401", "A"));
            tableView.getItems().add(new CourseProperty("CSIT402", "B+"));
            tableView.getItems().add(new CourseProperty("CSIT403", "B"));
            tableView.getItems().add(new CourseProperty("CSIT404", "C+"));
            tableView.getItems().add(new CourseProperty("CSIT405", "A"));
            tableView.getItems().add(new CourseProperty("CSIT406", "B+"));
            tableView.getItems().add(new CourseProperty("CSIT407", "B+"));
            tableView.getItems().add(new CourseProperty("CSIT408", "A"));
            tableView.getItems().add(new CourseProperty("CSIT409", "B+"));
            tableView.getItems().add(new CourseProperty("CSIT410", "C+"));
            tableView.getItems().add(new CourseProperty("CSIT411", "B+"));
            tableView.getItems().add(new CourseProperty("CSIT413", "A"));
            tableView.getItems().add(new CourseProperty("CSIT321", "B+"));
    }

    @FXML public void courseWindow(ActionEvent event) {
        addCourseWindow();
//        addCourse();
    }

    private void addCourseWindow() {
        try {
            Stage courseStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            Pane root = fxmlLoader.load(getClass().getResource("/newCourseForm/courseFormDesigner.fxml").openStream());
            Scene scene = new Scene(root);
            courseStage.setScene(scene);
            courseStage.setTitle("Course CRUD Form");
            courseStage.show();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayStatus(UndergraduateDatabase.createConnection(), INFO_CIRCLE, Color.BLUE);
        try {
            loadData();
            inputTestData();
        } catch (SQLException e) {
            displayStatus(e.getMessage(), INFO_CIRCLE, Color.RED);
        }
        displayStatus("Select Course using the selection boxes above [Start from the Left]", INFO_CIRCLE, Color.BLUE);
        statusBar.getRightItems().add(cgpaLabel);
        statusBar.getRightItems().add(fgpaLabel);
        statusBar.getRightItems().add(degClassLabel);
    }
}
