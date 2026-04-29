package gui_test;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;

public class CourseBlockController {

    @FXML private StackPane courseBlock; 
    @FXML private Label courseLabel;     

    public void setCourseName(String name) {
        if (courseLabel != null) {
            courseLabel.setText(name);
        }
    }

    public void initialize() {
        if (courseBlock != null) {
            courseBlock.setOnDragDetected(event -> {
                Dragboard db = courseBlock.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(courseLabel != null ? courseLabel.getText() : "Empty");
                db.setContent(content);
                db.setDragView(courseBlock.snapshot(null, null));
                event.consume();
            });
        }
    }
}