package Dashboard;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DashboardController {

    @FXML private VBox remainingCourses, externalCourses;
    @FXML private VBox year1Fall, year1Spring, year2Fall, year2Spring;
    @FXML private VBox year3Fall, year3Spring, year4Fall, year4Spring;
    
    @FXML private Label remainingTotal, externalTotal;
    @FXML private Label year1FallTotal, year1SpringTotal, year2FallTotal, year2SpringTotal;
    @FXML private Label year3FallTotal, year3SpringTotal, year4FallTotal, year4SpringTotal;

    private Map<VBox, Label> totalMap = new HashMap<>();

    @FXML
    public void initialize() {
        VBox[] semesterBoxes = {
            remainingCourses, externalCourses, 
            year1Fall, year1Spring, year2Fall, year2Spring, 
            year3Fall, year3Spring, year4Fall, year4Spring
        };
        
        totalMap.put(remainingCourses, remainingTotal);
        totalMap.put(externalCourses, externalTotal);
        totalMap.put(year1Fall, year1FallTotal);
        totalMap.put(year1Spring, year1SpringTotal);
        totalMap.put(year2Fall, year2FallTotal);
        totalMap.put(year2Spring, year2SpringTotal);
        totalMap.put(year3Fall, year3FallTotal);
        totalMap.put(year3Spring, year3SpringTotal);
        totalMap.put(year4Fall, year4FallTotal);
        totalMap.put(year4Spring, year4SpringTotal);

        for (VBox box : semesterBoxes) {
            if (box != null) {
                setupDropTarget(box);
            }
        }

        
        
// --- SEMESTER 1 TEST DATA ---
addCourse("MATH 131", "4");  // Calculus I
addCourse("CSIS 100", "3");  // Intro to Information Sciences
addCourse("ENGL 101", "3");  // Composition & Rhetoric
addCourse("BIBL 105", "2");  // Old Testament Survey
addCourse("PSYC 101", "3");  // General Psychology

// --- SEMESTER 2 TEST DATA ---
addCourse("MATH 132", "4");  // Calculus II
addCourse("CSIS 110", "3");  // Introduction to Computing Sciences
addCourse("PHYS 231", "4");  // University Physics I
addCourse("ENGL 102", "3");  // Composition & Literature
addCourse("COMS 101", "3");  // Speech Communication

// --- SEMESTER 3 TEST DATA ---
addCourse("CSIS 212", "3");  // Object-Oriented Programming
addCourse("MATH 250", "3");  // Introduction to Discrete Mathematics
addCourse("PHYS 232", "4");  // University Physics II
addCourse("BIBL 110", "2");  // New Testament Survey
addCourse("HIEU 201", "3");  // History of Western Civilization I

// --- SEMESTER 4 TEST DATA ---
addCourse("CSIS 312", "3");  // Advanced Object-Oriented Programming
addCourse("CSIS 434", "3");  // Network Security
addCourse("MATH 350", "3");  // Discrete Mathematics
addCourse("RLGN 105", "2");  // Intro to Biblical Worldview
addCourse("GOVT 220", "3");  // American Government

// --- MISC / REMAINING COURSES ---
addCourse("CSIS 499", "3");  // Computer Science Capstone
addCourse("BUSI 310", "3");  // Principles of Management
addCourse("CHEM 121", "4");  // General Chemistry I
addCourse("SPAN 101", "3");  // Elementary Spanish I
addCourse("THEO 201", "2");  // Theology Survey I
        



        updateAllTotals();
    }

    public void addCourse(String name, String credits) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Dashboard/CourseBlock.fxml"));
            Parent courseNode = loader.load();
            
            CourseBlockController controller = loader.getController();
            controller.setCourseDetails(name, credits);
            
            // APPLY COLOR LOGIC HERE
            applyLevelColor(courseNode, name);
            
            if (remainingCourses != null) {
                remainingCourses.getChildren().add(courseNode);
                updateAllTotals();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void applyLevelColor(Parent node, String courseName) {
        // Base style to keep them from being "naked" (Radius and Border)
        String baseStyle = "-fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: #808080; -fx-border-width: 1;";
        String bgColor = "-fx-background-color: #D3D3D3;"; // Default Gray

        Pattern pattern = Pattern.compile("\\d");
        Matcher matcher = pattern.matcher(courseName);

        if (matcher.find()) {
            char firstDigit = matcher.group().charAt(0);
            switch (firstDigit) {
                case '1': bgColor = "-fx-background-color: #CCFFCC;"; break; // Light Blue
                case '2': bgColor = "-fx-background-color: #FFFFCC;"; break; // Green
                case '3': bgColor = "-fx-background-color: #FFCC99;"; break; // Yellow
                case '4': bgColor = "-fx-background-color: #FFCCCC;"; break; // Red
            }
        }
        // Combine them so we don't lose the "clothes" (borders)
        node.setStyle(bgColor + baseStyle);
    }

    private void setupDropTarget(VBox target) {
        target.setOnDragOver(event -> {
            if (event.getGestureSource() != target && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        target.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasString()) {
                StackPane source = (StackPane) event.getGestureSource();
                VBox oldParent = (VBox) source.getParent();
                if (oldParent != null) {
                    oldParent.getChildren().remove(source);
                    target.getChildren().add(source);
                    updateAllTotals(); 
                }
            }
            event.setDropCompleted(true);
            event.consume();
        });
    }

    private void updateAllTotals() {
        totalMap.forEach((box, label) -> {
            if (box != null && label != null) {
                updateTotalCredits(box, label);
            }
        });
    }

    private void updateTotalCredits(VBox semesterBox, Label totalLabel) {
        int total = 0;
        for (javafx.scene.Node node : semesterBox.getChildren()) {
            if (node instanceof StackPane) {
                // Safer lookup using the ID we set in FXML
                Label creditsLabel = (Label) node.lookup("#courseCredits");
                if (creditsLabel != null) {
                    try {
                        total += Integer.parseInt(creditsLabel.getText());
                    } catch (NumberFormatException e) { }
                }
            }
        }
        totalLabel.setText("Total Credits: " + total);
    }
}