package com.ortimdm.ortimdm.component;

import com.ortimdm.ortimdm.model.AnalyzeUrl;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;


public class AnalyzeListCell extends ListCell<AnalyzeUrl> {

    @Override
    protected void updateItem(AnalyzeUrl analyzeUrl, boolean b) {
        super.updateItem(analyzeUrl, b);
        if(b || analyzeUrl == null){
            setText(null);
            setGraphic(null);
        }else{
            VBox vBox = new VBox();
            Label filenameLabel = new Label("File name: " + analyzeUrl.getFilename());
            filenameLabel.setWrapText(true);

            Label fileFormatLabel = new Label("File format: " + analyzeUrl.getFormat());
            fileFormatLabel.setWrapText(true);

            Label fileDescriptionLabel = new Label("Description: " + analyzeUrl.getDescription());
            fileDescriptionLabel.setWrapText(true);

            Label thumbnailLabel = new Label("Thumbnail: " + analyzeUrl.getImageLink());
            thumbnailLabel.setWrapText(true);

            vBox.getChildren().addAll(filenameLabel, fileFormatLabel, fileDescriptionLabel, thumbnailLabel);
            vBox.setAlignment(Pos.CENTER_LEFT);

            setGraphic(vBox);
        }
    }
}
