package Dashboard;

// Gemini: Imported the other package classes to link the GUI to the Scraper
import JavaFinalProject.Course; 
import JavaFinalProject.Ganttural_Resolution;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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
import java.util.ArrayList;

public class DashboardController {

    // Gemini: Added UI elements to handle the scraping input
    @FXML private TextField loadURL;
    @FXML private Button loadButton;

    // Semester and Storage Columns
    @FXML private VBox remainingCourses, externalCourses;
    @FXML private VBox year1Fall, year1Spring, year2Fall, year2Spring;
    @FXML private VBox year3Fall, year3Spring, year4Fall, year4Spring;
    
    // Total Credit Labels
    @FXML private Label remainingTotal, externalTotal;
    @FXML private Label year1FallTotal, year1SpringTotal, year2FallTotal, year2SpringTotal;
    @FXML private Label year3FallTotal, year3SpringTotal, year4FallTotal, year4SpringTotal;

    private Map<VBox, Label> totalMap = new HashMap<>();

    @FXML
    public void initialize() {
        // Map columns to their total labels for easy updating
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

        VBox[] semesterBoxes = {
            remainingCourses, externalCourses, 
            year1Fall, year1Spring, year2Fall, year2Spring, 
            year3Fall, year3Spring, year4Fall, year4Spring
        };

        for (VBox box : semesterBoxes) {
            if (box != null) setupDropTarget(box);
        }
        updateAllTotals();
    }

    // Gemini: Logic to handle the clicking of the Load Button
    @FXML
    private void handleLoadAction() {
        String url = loadURL.getText();
        if (url == null || url.trim().isEmpty()) {
            System.out.println("Gemini: URL field is empty.");
            return;
        }

        // Gemini: Trigger the scraper logic in the other package
        Ganttural_Resolution.runScraper(url);
        
        // Gemini: Clear existing courses from the list columns before importing new ones
        remainingCourses.getChildren().clear();
        externalCourses.getChildren().clear();
        
        importData();
        updateAllTotals();
    }

    // Gemini: Pulls data from the scraper's static list and creates the UI blocks
    public void importData() {
        ArrayList<Course> importedCourses = Ganttural_Resolution.courses;
        if (importedCourses == null || importedCourses.isEmpty()) return;
        
        for (Course c : importedCourses) {
            // Sort into columns based on if they are "External" (0 credits) or standard
            if (c.getHours() == 0) {
                addCourseToColumn(externalCourses, c.getName(), "0");
            } else {
                addCourseToColumn(remainingCourses, c.getName(), String.valueOf(c.getHours()));
            }
        }
    }

    private void addCourseToColumn(VBox column, String name, String credits) {
        try {
            // Gemini: Use relative path (no leading slash) to avoid NullPointerException
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CourseBlock.fxml"));
            Parent courseNode = loader.load();
            
            CourseBlockController controller = loader.getController();
            controller.setCourseDetails(name, credits);
            
            // Color code based on course level (100s, 200s, etc)
            applyLevelColor(courseNode, name);
            
            if (column != null) column.getChildren().add(courseNode);
        } catch (IOException e) {
            System.err.println("Gemini Error: Could not load CourseBlock.fxml - " + e.getMessage());
        }
    }

    private void applyLevelColor(Parent node, String courseName) {
        String baseStyle = "-fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: #808080; -fx-border-width: 1;";
        String bgColor = "-fx-background-color: #D3D3D3;"; // Default grey
        
        Pattern pattern = Pattern.compile("\\d");
        Matcher matcher = pattern.matcher(courseName);

        if (matcher.find()) {
            char firstDigit = matcher.group().charAt(0);
            switch (firstDigit) {
                case '1': bgColor = "-fx-background-color: #CCFFFF;"; break; // Blue-ish
                case '2': bgColor = "-fx-background-color: #CCFFCC;"; break; // Green-ish
                case '3': bgColor = "-fx-background-color: #FFFFCC;"; break; // Yellow-ish
                case '4': bgColor = "-fx-background-color: #FFCCCC;"; break; // Red-ish
            }
        }
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
            if (box != null && label != null) updateTotalCredits(box, label);
        });
    }

    private void updateTotalCredits(VBox semesterBox, Label totalLabel) {
        int total = 0;
        for (javafx.scene.Node node : semesterBox.getChildren()) {
            if (node instanceof StackPane) {
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
