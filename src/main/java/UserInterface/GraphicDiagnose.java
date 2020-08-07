package UserInterface;

import Domain.Entities.Entity;
import Domain.Entities.Patient;
import Domain.Entities.User;
import Domain.Exceptions.InvalidPatientException;
import Service.PatientService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * the class that is used for writing diagnostics by users.
 */
public class GraphicDiagnose {
    public DatePicker datePickerWorkDay;
    public TextArea txtDiagnose;
    public TableView pacientTable;
    public TableView analysesTable;
    public CheckBox checkIsSick;
    public TableColumn colResults;
    public TableColumn colIsSick;
    public TableColumn colAnalysesName;
    public TableColumn colFname;
    public TableColumn colLname;
    public TableColumn colDiagnose;
    private User currentUser;
    private PatientService patientService;
    private ObservableList<Entity> patients = FXCollections.observableArrayList();
    private ObservableList<Entity> analysesForOne = FXCollections.observableArrayList();

    /**
     * @param patientService The service that has functionality for patients.
     * @param user           The current user who uses the aplication.
     */
    public void setAll(PatientService patientService, User user) {
        this.patientService = patientService;
        this.currentUser = user;
        datePickerWorkDay.setValue(LocalDate.now());
    }

    /**
     * the initialisation of the current scene.
     */
    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            refreshPacients();
            clearAndSetAll();

        });

    }

    /**
     * the refresh of all the Observable Lists and tables.
     */
    private void clearAndSetAll() {
        patients.clear();
        pacientTable.refresh();
        analysesTable.refresh();
        patients.addAll(patientService.getPacientsForADay(datePickerWorkDay.getValue()));
        pacientTable.setItems(patients);
        ColorChanger.makeSickPatientsRed(colIsSick);
        ColorChanger.makeSickPatientsRed(colDiagnose);
        ColorChanger.colorFinishPatients(colFname);
        ColorChanger.colorFinishPatients(colLname);
        ColorChanger.makeAnalysesOutOfLimitsRed(colResults);
        ColorChanger.makeAnalysesOutOfLimitsRed(colAnalysesName);

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

    /**
     * @param mouseEvent the event of selecting  a patient from the table.
     */
    public void showSelected(MouseEvent mouseEvent) {
        analysesForOne.clear();
        analysesTable.refresh();
        TableSelectionModel<Entity> tableSelectionModel = pacientTable.getSelectionModel();
        Patient patient = (Patient) tableSelectionModel.getSelectedItem();
        txtDiagnose.setText(patient.getDiagnose());
        checkIsSick.setSelected(patient.isSick());
        AtomicBoolean ok = new AtomicBoolean(patient.isSick());

        analysesForOne.addAll(patient.getAnalyses());
        analysesTable.setItems(analysesForOne);
    }

    /**
     * @param actionEvent the event of clicking the "add diagnostic" button.
     */
    public void addDiagnose(ActionEvent actionEvent) {
        try {
            TableSelectionModel<Entity> tableSelectionModel = pacientTable.getSelectionModel();
            Patient patient = (Patient) tableSelectionModel.getSelectedItem();
            String diagnose = txtDiagnose.getText();
            boolean isSick = checkIsSick.isSelected();
            patientService.setDiagnostic(patient, diagnose, isSick, currentUser);
            patients.clear();
            pacientTable.refresh();
            patients.addAll(patientService.getPacientsForADay(datePickerWorkDay.getValue()));
            pacientTable.setItems(patients);
        } catch (InvalidPatientException e) {
            e.printStackTrace();
            Alerts.showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            Alerts.showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * @param actionEvent the event of clicking on the "refresh" button.
     */

    public void refresh(ActionEvent actionEvent) {
        initialize();
    }

}

