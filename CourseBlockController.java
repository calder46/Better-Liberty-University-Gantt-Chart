package dashboard;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;

public class CourseBlockController {

    @FXML private StackPane courseBlock; // Matches fx:id="courseBlock"
    @FXML private Label courseLabel;     // Matches fx:id="courseLabel"
    @FXML private Label courseCredits;   // Matches fx:id="courseCredits"
    @FXML private CheckBox checkbox;     // Matches fx:id="checkbox" (Now a CheckBox object)

    public void setCourseDetails(String name, String credits) {
        if (courseLabel != null) courseLabel.setText(name);
        if (courseCredits != null) courseCredits.setText(credits);
    }

    @FXML
    public void initialize() {
        if (courseBlock != null) {
            courseBlock.setOnDragDetected(event -> {
                Dragboard db = courseBlock.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                
                String text = (courseLabel != null) ? courseLabel.getText() : "Course";
                content.putString(text);
                
                db.setContent(content);
                db.setDragView(courseBlock.snapshot(null, null));
                event.consume();
            });
        }
    }
}