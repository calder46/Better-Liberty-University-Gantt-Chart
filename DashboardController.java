package dashboard;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import java.io.IOException;

public class DashboardController {

    @FXML private VBox remainingCourses, externalCourses;
    @FXML private VBox year1Fall, year1Spring, year2Fall, year2Spring;
    @FXML private VBox year3Fall, year3Spring, year4Fall, year4Spring;

    @FXML
    public void initialize() {
        // Set up drop targets for all semester columns
        VBox[] semesterBoxes = {
            remainingCourses, externalCourses, 
            year1Fall, year1Spring, year2Fall, year2Spring, 
            year3Fall, year3Spring, year4Fall, year4Spring
        };
        
        for (VBox box : semesterBoxes) {
            if (box != null) {
                setupDropTarget(box);
            }
        }

        // Populate the initial list
        addCourseToRemaining("MATH 101", "3");
        addCourseToRemaining("CSIS 312", "3");
        addCourseToRemaining("ENGL 101", "3");
        addCourseToRemaining("PHYS 231", "4");
    }

    private void addCourseToRemaining(String name, String credits) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard/CourseBlock.fxml"));
            Parent courseNode = loader.load();
            
            CourseBlockController controller = loader.getController();
            controller.setCourseDetails(name, credits);
            
            if (remainingCourses != null) {
                remainingCourses.getChildren().add(courseNode);
            }
        } catch (IOException e) {
            System.err.println("Error loading CourseBlock.fxml: " + e.getMessage());
            e.printStackTrace();
        }
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
            boolean success = false;
            
            if (db.hasString()) {
                StackPane source = (StackPane) event.getGestureSource();
                Pane parent = (Pane) source.getParent();
                if (parent != null) {
                    parent.getChildren().remove(source);
                    target.getChildren().add(source);
                    success = true;
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }
}