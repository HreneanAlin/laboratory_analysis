package UserInterface;

import Domain.Entities.Entity;
import Domain.Entities.Sample;
import Domain.Entities.User;
import Domain.Exceptions.InvalidSampleException;
import Service.SampleService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

/**
 * The class that manages CRUD functionality for Sample entities.
 */
public class GraphicSamplesManagement extends AbstractManagement {
    public TextField txtSampleName;
    public CheckBox checkAssistance;
    public TableView samplesTable;
    private SampleService sampleService;

    /**
     * @param sampleService The service that has functionality for samples.
     * @param currentUser   The current user who uses the aplication.
     */
    public void setAll(SampleService sampleService, User currentUser) {
        this.currentUser = currentUser;
        this.sampleService = sampleService;
    }

    /**
     * the initialisation of the current scene.
     */
    @FXML
    protected void initialize() {

        Platform.runLater(() -> {
            entities.clear();
            samplesTable.refresh();
            entities.addAll(sampleService.getAll());
            samplesTable.setItems(entities);
        });
    }

    /**
     * @param actionEvent The event of clicking the "add sample" button.
     */
    @Override
    public void addEntity(ActionEvent actionEvent) {
        try {
            String name = txtSampleName.getText();
            boolean assistance = checkAssistance.isSelected();
            sampleService.addSample(name, assistance, currentUser);
            entities.clear();
            entities.addAll(sampleService.getAll());
        } catch (InvalidSampleException e) {
            e.printStackTrace();
            Alerts.showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            Alerts.showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * @param mouseEvent the event of selecting a sample from the table.
     */
    @Override
    public void showSelected(MouseEvent mouseEvent) {
        TableSelectionModel<Entity> tableSelectionModel = samplesTable.getSelectionModel();
        Sample sample = (Sample) tableSelectionModel.getSelectedItem();
        txtSampleName.setText(sample.getName());
        checkAssistance.setSelected(sample.isAssitantRequired());
    }

    /**
     * @param actionEvent the event of clicking the "delete sample" button.
     */
    @Override
    public void deleteEntity(ActionEvent actionEvent) {
        if (Alerts.showQuestion("The sample will be deleted!", "Delete?").get()) {
            TableSelectionModel<Entity> tableSelectionModel = samplesTable.getSelectionModel();
            Entity abstractEntity = tableSelectionModel.getSelectedItem();
            sampleService.deleteOne(abstractEntity.getId());
            entities.clear();
            samplesTable.refresh();
            entities.setAll(sampleService.getAll());
            samplesTable.setItems(entities);
            txtSampleName.clear();
            checkAssistance.setSelected(false);
        }
    }

    /**
     * @param actionEvent the event of clicking the "update sample" button.
     */
    @Override
    public void updateEntity(ActionEvent actionEvent) {
        try {
            if (Alerts.showQuestion("The sample will be Updated!", "Update?").get()) {
                TableSelectionModel<Entity> tableSelectionModel = samplesTable.getSelectionModel();
                if (tableSelectionModel.isSelected(tableSelectionModel.getSelectedIndex())) {
                    Sample sample = (Sample) tableSelectionModel.getSelectedItem();
                    String name = txtSampleName.getText();
                    boolean assitance = checkAssistance.isSelected();
                    sampleService.updateSample(sample.getId(), name, assitance, currentUser);
                    entities.clear();
                    samplesTable.refresh();
                    entities.addAll(sampleService.getAll());
                }
            }
        } catch (InvalidSampleException e) {
            e.printStackTrace();
            Alerts.showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            Alerts.showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * @param actionEvent the event of clicking the "refresh" button
     */
    @Override
    public void refresh(ActionEvent actionEvent) {
        initialize();
    }
}
