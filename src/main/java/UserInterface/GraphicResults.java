package UserInterface;

import Domain.Entities.Analysis;
import Domain.Entities.Entity;
import Domain.Entities.Patient;
import Domain.Entities.User;
import Domain.Exceptions.InvalidAnalysisException;
import Service.PatientService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * the class that is used for writing results by users.
 */
public class GraphicResults {
    public TextField txtFirstName;
    public TextField txtLastName;
    public TableView analysesTable;
    public TextField txtResult;
    public DatePicker datePickerWorkDay;
    public TableColumn colResults;
    public TableColumn colAnalysesName;
    private PatientService patientService;
    private List<Patient> patientList = new ArrayList<>();
    private ObservableList<Entity> analyses = FXCollections.observableArrayList();
    private User user;
    private int current = 0;

    /**
     * @param patientService The service that has functionality for patients.
     * @param user           The current user who uses the aplication.
     */
    public void setAll(PatientService patientService, User user) {
        this.patientService = patientService;
        this.user = user;
    }

    /**
     * the initialisation of the current scene.
     */
    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            refreshPacients();
            datePickerWorkDay.setValue(LocalDate.now());
            showCurrentPacient();
            ColorChanger.makeAnalysesOutOfLimitsRed(colResults);
            ColorChanger.makeAnalysesOutOfLimitsRed(colAnalysesName);


        });

    }

    /**
     * Displays the current patient to the screen.
     */
    public void showCurrentPacient() {
        try {
            analyses.clear();
            analysesTable.refresh();
            patientList.clear();
            LocalDate currentDay = datePickerWorkDay.getValue();
            patientList.addAll(patientService.getPacientsForADay(currentDay));
            Patient patient = patientList.get(current);
            analyses.addAll(patient.getAnalyses());
            analysesTable.setItems(analyses);
            txtFirstName.setText(patient.getFirstName());
            txtLastName.setText(patient.getLastName());
        } catch (IndexOutOfBoundsException e) {
            Alerts.showAlert("", "There are no pacients", Alert.AlertType.INFORMATION);
            txtResult.clear();
            txtFirstName.clear();
            txtLastName.clear();
        }
    }

    /**
     * @param actionEvent the event of clicking the "back" button.
     */
    public void backClick(ActionEvent actionEvent) {
        if (current > 0) {
            current--;
            showCurrentPacient();
        } else {
            Alerts.showAlert("", "no more pacients", Alert.AlertType.WARNING);
        }
    }

    /**
     * @param actionEvent the event of clicking the "next" button.
     */
    public void nextClick(ActionEvent actionEvent) {
        if (current < patientList.size() - 1) {
            current++;
            showCurrentPacient();
        } else {
            Alerts.showAlert("", "no more pacients", Alert.AlertType.WARNING);
        }

    }

    /**
     * @param mouseEvent the event of clicking on an analyses from the table.
     */
    public void showSelected(MouseEvent mouseEvent) {
        Patient patient = patientList.get(current);
        TableSelectionModel<Entity> tableSelectionModel = analysesTable.getSelectionModel();
        Analysis analysis = (Analysis) tableSelectionModel.getSelectedItem();
        txtResult.setText(analysis.getScore());
    }

    /**
     * @param actionEvent the event of clicking the "add result" button.
     */
    public void addResult(ActionEvent actionEvent) {
        try {
            Double result = Double.parseDouble(txtResult.getText());
            Patient patient = patientList.get(current);
            TableSelectionModel<Entity> tableSelectionModel = analysesTable.getSelectionModel();
            Analysis analysis = (Analysis) tableSelectionModel.getSelectedItem();
            patientService.setResult(patient.getId(), analysis.getId(), result, user);
            patientList.clear();
            patientList.addAll(patientService.getPacientsForADay(datePickerWorkDay.getValue()));
            patient = patientList.get(current);
            analyses.clear();
            analyses.addAll(patient.getAnalyses());
            txtResult.clear();
            tableSelectionModel.clearSelection();
        } catch (InvalidAnalysisException e) {
            e.printStackTrace();
            Alerts.showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            Alerts.showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Updates changes made in the analyses Repository to analyses referances from a patient object.
     */
    private void refreshPacients() {
        for (Patient patient : patientService.getAll()) {
            patientService.updateAnalysis(patient);
        }
    }

    /**
     * the external call of initialisation
     */
    public void continueRefresh() {
        initialize();
    }


}
