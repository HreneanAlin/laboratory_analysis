package UserInterface;

import Domain.Entities.Analysis;
import Domain.Entities.Entity;
import Domain.Entities.Patient;
import Domain.Entities.User;
import Domain.Exceptions.InvalidPatientException;
import Service.AnalysisService;
import Service.PatientService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * The class that manages CRUD functionality for Patient entities.
 */
public class GraphicPacientsManagement extends AbstractManagement {
    public ListView listAnalysis;
    public TableView pacientTable;
    public TextField txtCnp;
    public TextField txtFirstName;
    public TextField txtLastName;
    public TableView analysesTable;
    public TableColumn colResults;
    public TableColumn colAnalysesName;
    public TableColumn colFname;
    public TableColumn colLname;
    public TableColumn colDiagnose;
    private PatientService patientService;
    private AnalysisService analysisService;
    private ObservableList<Entity> analysesAll = FXCollections.observableArrayList();
    private ObservableList<Entity> analysesForOne = FXCollections.observableArrayList();

    /**
     * @param patientService  The service that has functionality for patients.
     * @param analysisService The service that has functionality for analyses.
     * @param user            The current user who uses the aplication.
     */
    public void setAll(PatientService patientService, AnalysisService analysisService, User user) {
        this.patientService = patientService;
        this.analysisService = analysisService;
        currentUser = user;
    }

    /**
     * the initialisation of the current scene.
     */
    @Override
    protected void initialize() {
        Platform.runLater(() -> {
            clearAndSetAll();
            listAnalysis.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            refreshPacients();


        });

    }

    /**
     * the external call of initialisation
     */
    public void continueRefresh() {
        initialize();
    }

    /**
     * the refresh of all the Observable Lists and tables.
     */
    private void clearAndSetAll() {
        entities.clear();
        pacientTable.refresh();
        analysesAll.clear();
        listAnalysis.refresh();
        analysesTable.refresh();
        entities.addAll(patientService.getAll());
        pacientTable.setItems(entities);
        //pacientTable.sort();
        analysesAll.addAll(analysisService.getAll());
        listAnalysis.setItems(analysesAll);
        ColorChanger.colorFinishPatients(colFname);
        ColorChanger.colorFinishPatients(colLname);
        ColorChanger.makeAnalysesOutOfLimitsRed(colResults);
        ColorChanger.makeAnalysesOutOfLimitsRed(colAnalysesName);
        ColorChanger.makeSickPatientsRed(colDiagnose);
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
     * @param actionEvent The event of clicking the "add patient" button.
     */
    @Override
    public void addEntity(ActionEvent actionEvent) {
        try {
            String cnp = txtCnp.getText();
            String firstName = txtFirstName.getText();
            String lastName = txtLastName.getText();
            List<Analysis> analysisList = listAnalysis.getSelectionModel().getSelectedItems();
            patientService.addPacient(cnp, firstName, lastName, analysisList, currentUser);
            clearAndSetAll();
        } catch (InvalidPatientException e) {
            e.printStackTrace();
            Alerts.showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            Alerts.showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * @param mouseEvent the event of selecting a patient from the table of patients.
     */
    @Override
    public void showSelected(MouseEvent mouseEvent) {
        analysesForOne.clear();
        analysesTable.refresh();
        listAnalysis.getSelectionModel().clearSelection();
        TableSelectionModel<Entity> tableSelectionModel = pacientTable.getSelectionModel();
        Patient patient = (Patient) tableSelectionModel.getSelectedItem();
        txtCnp.setText(patient.getCnp());
        txtFirstName.setText(patient.getFirstName());
        txtLastName.setText(patient.getLastName());
        analysesForOne.addAll(patient.getAnalyses());
        analysesTable.setItems(analysesForOne);
        selectAll(patient);

    }

    /**
     * selects all the analyses that a patient has from the analysis table.
     *
     * @param patient The currently selected patient
     */
    private void selectAll(Patient patient) {
        for (Analysis analysis : patient.getAnalyses()) {
            listAnalysis.getSelectionModel().select(analysis);
        }
    }

    /**
     * @param actionEvent the event of clicking the "delete patient" button.
     */
    @Override
    public void deleteEntity(ActionEvent actionEvent) {
        String fullName = txtFirstName.getText() + " " + txtLastName.getText();
        if (Alerts.showQuestion("Patient " + fullName + " will be deleted!", "Delete?").get()) {
            TableSelectionModel<Entity> tableSelectionModel = pacientTable.getSelectionModel();
            Entity abstractEntity = tableSelectionModel.getSelectedItem();
            patientService.deleteOne(abstractEntity.getId());
            clearAndSetAll();
            txtCnp.clear();
            txtFirstName.clear();
            txtLastName.clear();
            listAnalysis.getSelectionModel().clearSelection();
            analysesTable.refresh();
            analysesForOne.clear();
            analysesTable.refresh();

        }

    }

    /**
     * @param actionEvent the event of clicking the "update patient" button.
     */
    @Override
    public void updateEntity(ActionEvent actionEvent) {
        try {
            String fullName = txtFirstName.getText() + " " + txtLastName.getText();
            if (Alerts.showQuestion("Patient " + fullName + " will be updated!", "Update?").get()) {
                TableSelectionModel<Entity> tableSelectionModel = pacientTable.getSelectionModel();
                Patient patient = (Patient) tableSelectionModel.getSelectedItem();
                String cnp = txtCnp.getText();
                String firstName = txtFirstName.getText();
                String lastName = txtLastName.getText();
                List<Analysis> analysisList = listAnalysis.getSelectionModel().getSelectedItems();
                patientService.update(patient.getId(), cnp, firstName, lastName, analysisList, currentUser);
                clearAndSetAll();
            }
        } catch (InvalidPatientException e) {
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

    /**
     * Exports patients information as a Jasper Report as PDF.
     *
     * @param actionEvent The event of clicking "export to PDF" button.
     * @throws IOException in case the export cannot be done.
     * @throws JRException in case the export cannot be done.
     */
    public void exportResultToPDF(ActionEvent actionEvent) throws IOException, JRException {



        TableSelectionModel<Entity> tableSelectionModel = pacientTable.getSelectionModel();
        Patient patient = (Patient) tableSelectionModel.getSelectedItem();
        if(patient != null) ExportingJasperReports.exportReportPDF(patient, currentUser);
        else Alerts.showAlert("Error","Please select a patient", Alert.AlertType.ERROR);
    }

    /**
     * Exports patients information as a Jasper Report to html and opens it in default broswer.
     *
     * @param actionEvent The event of clicking "export to WEB" button.
     * @throws IOException in case the export cannot be done.
     * @throws JRException in case the export cannot be done.
     */
    public void exportResultToWEB(ActionEvent actionEvent) throws IOException, JRException {
        TableSelectionModel<Entity> tableSelectionModel = pacientTable.getSelectionModel();
        Patient patient = (Patient) tableSelectionModel.getSelectedItem();
        if(patient != null) ExportingJasperReports.exportReportWeb(patient, currentUser);
        else Alerts.showAlert("Error","Please select a patient", Alert.AlertType.ERROR);

    }
}
