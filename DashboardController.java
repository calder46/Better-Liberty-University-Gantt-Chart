package gui_test;

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

    public void initialize() {
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

        // Standard course load initialization
        addCourseToRemaining("MATH 101");
        addCourseToRemaining("CSIS 312");
        addCourseToRemaining("ENGL 101");
        addCourseToRemaining("PHYS 231");
        addCourseToRemaining("MATH 101");
        addCourseToRemaining("CSIS 312");
        addCourseToRemaining("ENGL 101");
        addCourseToRemaining("PHYS 231");
        addCourseToRemaining("MATH 101");
        addCourseToRemaining("CSIS 312");
        addCourseToRemaining("ENGL 101");
        addCourseToRemaining("PHYS 231");
        addCourseToRemaining("MATH 101");
        addCourseToRemaining("CSIS 312");
        addCourseToRemaining("ENGL 101");
        addCourseToRemaining("PHYS 231");
        addCourseToRemaining("MATH 101");
        addCourseToRemaining("CSIS 312");
        addCourseToRemaining("ENGL 101");
        addCourseToRemaining("PHYS 231");
        addCourseToRemaining("MATH 101");
        addCourseToRemaining("CSIS 312");
        addCourseToRemaining("ENGL 101");
        addCourseToRemaining("PHYS 231");
        addCourseToRemaining("MATH 101");
        addCourseToRemaining("CSIS 312");
        addCourseToRemaining("ENGL 101");
        addCourseToRemaining("PHYS 231");
        addCourseToRemaining("MATH 101");
        addCourseToRemaining("CSIS 312");
        addCourseToRemaining("ENGL 101");
        addCourseToRemaining("PHYS 231");
        // Add more courses as needed for your scroll test
    }

    private void addCourseToRemaining(String courseName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui_test/CourseBlock.fxml"));
            Parent courseNode = loader.load();
            
            CourseBlockController controller = loader.getController();
            controller.setCourseName(courseName);
            
            if (remainingCourses != null) {
                remainingCourses.getChildren().add(courseNode);
            }
        } catch (IOException e) {
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