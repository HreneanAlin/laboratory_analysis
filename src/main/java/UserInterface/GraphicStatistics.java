package UserInterface;

import Domain.Entities.Patient;
import Domain.ViewModels.NrOfRegistrationPerUserViewModel;
import Domain.ViewModels.PatientsPerAnalysesViewModel;
import Domain.ViewModels.PercentOfIncomePerAnalysesViewModel;
import Service.PatientService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import java.text.DecimalFormat;
import java.time.LocalDate;

/**
 * the class that is used for displaying different statistics .
 */
public class GraphicStatistics {
    public DatePicker dateUntil;
    public TableView tablePatientsPerAnalyses;
    public TableView tableProcentOfIncomePerAnalyses;
    public Label labelTotalIncome;
    public TableView tableNrOfRegistrationPerUser;
    private ObservableList<PatientsPerAnalysesViewModel> patientsPerAnalyses = FXCollections.observableArrayList();
    private ObservableList<PercentOfIncomePerAnalysesViewModel> procentOfIncomePerAnalyses = FXCollections.observableArrayList();
    private ObservableList<NrOfRegistrationPerUserViewModel> nrOfRegistrationPerUser = FXCollections.observableArrayList();
    private PatientService patientService;

    /**
     * @param patientService The service that has functionality for patients.
     */
    public void setAll(PatientService patientService) {
        this.patientService = patientService;
        dateUntil.setValue(LocalDate.now());
    }

    /**
     * the initialisation of the current scene.
     */
    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            tablePatientsPerAnalyses.refresh();
            tableProcentOfIncomePerAnalyses.refresh();
            tableNrOfRegistrationPerUser.refresh();
            patientsPerAnalyses.clear();
            procentOfIncomePerAnalyses.clear();
            nrOfRegistrationPerUser.clear();
            refreshPacients();
            LocalDate localDate = dateUntil.getValue();
            patientsPerAnalyses.addAll(patientService.getPatientsPerAnalyses(localDate));
            tablePatientsPerAnalyses.setItems(patientsPerAnalyses);
            showPercentOfTotalIncome(localDate);
            nrOfRegistrationPerUser.addAll(patientService.getNrOfRegistrationPerUserViewModels(localDate));
            tableNrOfRegistrationPerUser.setItems(nrOfRegistrationPerUser);


        });

    }

    /**
     * Displays to screen the percentage of total income of every analysis until the stopping date
     *
     * @param localDate the stopping date
     */
    private void showPercentOfTotalIncome(LocalDate localDate) {
        DecimalFormat df3 = new DecimalFormat("#.###");
        procentOfIncomePerAnalyses.addAll(patientService.getProcentOfIncomePerAnalyses(localDate));
        tableProcentOfIncomePerAnalyses.setItems(procentOfIncomePerAnalyses);
        String totalIncome = df3.format(patientService.getTotalIncome(localDate));
        labelTotalIncome.setText("Total income= " + totalIncome);
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
     * @param actionEvent the event of selecting a date from the date picker.
     */

    public void selectDate(ActionEvent actionEvent) {
        initialize();
    }
}
