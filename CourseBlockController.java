package Dashboard;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;

public class CourseBlockController {

    @FXML private StackPane courseBlock; 
    @FXML private Label courseLabel;     
    @FXML private Label courseCredits; 
    @FXML private CheckBox checkbox;     

    //Setter method called by DashboardController
    public void setCourseDetails(String name, String credits) {
        //if != null keeps it from crashing if data isn't loaded properly
        if (courseLabel != null) courseLabel.setText(name);
        if (courseCredits != null) courseCredits.setText(credits);
    }

    
    //Drag & drop logic courtesy of Google Gemini,
    //Due to the fact that these functions were outside of the scope of CSIS 312, it was used to save time
    @FXML
    public void initialize() {
        if (courseBlock != null) {
            courseBlock.setOnDragDetected(event -> {
                // only alow dragging if course is not checked off
                if (checkbox != null && !checkbox.isSelected()) {
                    Dragboard db = courseBlock.startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent content = new ClipboardContent();
                    
                    String text = (courseLabel != null) ? courseLabel.getText() : "Course";
                    content.putString(text);
                    
                    db.setContent(content);
                    db.setDragView(courseBlock.snapshot(null, null));//create ghost courseBlock to follow cursor when dragging
                    event.consume();
                } else {
                    // print debugging error if course is already checked
                    System.out.println("Drag prevented: Course is marked as complete.");
                }
            });
        }
    }
}
