package UserInterface;

import Domain.Entities.Entity;
import Domain.Entities.Patient;
import Domain.Entities.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.ScrollEvent;

import java.time.LocalDate;

/**
 * a class that displays to screen information of a patient.
 */
public class GraphicOneResult {
    public TextField txtID;
    public TextField txtCnp;
    public TextField txtFirstName;
    public TextField txtLastName;
    public TextField txtDateOfRegistation;
    public TableView analysesTable;
    public TableColumn colAnalysesName;
    public TableColumn colAnalysesResults;
    public TextArea txtDiagnostic;
    public TextField txtUserDiagnostic;
    public TextField txtPrintedBy;
    public TextField txtCurrentDate;
    public TableColumn colLimits;
    private Patient patient;
    private User user;
    private ObservableList<Entity> analyses = FXCollections.observableArrayList();

    /**
     * @param patient the current patient.
     * @param user    the user who uses the aplication
     */
    public void setAll(Patient patient, User user) {
        this.patient = patient;
        this.user = user;
    }

    /**
     * the initialisation of the current scene.
     */
    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            analyses.addAll(patient.getAnalyses());
            analysesTable.setItems(analyses);
            ColorChanger.makeAnalysesOutOfLimitsRed(colAnalysesName);
            ColorChanger.makeAnalysesOutOfLimitsRed(colAnalysesResults);
            ColorChanger.makeAnalysesOutOfLimitsRed(colLimits);
            txtID.setText(Integer.toString(patient.getId()));
            txtCnp.setText(patient.getCnp());
            txtFirstName.setText(patient.getFirstName());
            txtLastName.setText(patient.getLastName());
            txtDateOfRegistation.setText(patient.getCurrentDate().toString());
            txtDiagnostic.setText(patient.getDiagnose());
            if(patient.getUserThatDiagnosed() != null) {
                txtUserDiagnostic.setText(patient.getUserThatDiagnosed().toString());
            }else txtUserDiagnostic.setText("in course");
            txtPrintedBy.setText(user.toString());
            txtCurrentDate.setText(LocalDate.now().toString());

        });

    }
}