package UserInterface;

import Domain.Entities.Analysis;
import Domain.Entities.Entity;
import Domain.Entities.Sample;
import Domain.Entities.User;
import Domain.Exceptions.InvalidAnalysisException;
import Service.AnalysisService;
import Service.SampleService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

/**
 * The class that manages CRUD functionality for Analysis entities.
 */
public class GraphicAnalysesManagement extends AbstractManagement {


    public TextField txtName;
    public TextField txtPrice;
    public TextField txtLowLimit;
    public TextField txtHighLimit;
    public TableView analysesTable;
    public ComboBox comboBoxSample;
    public TextField txtIncreasePrices;
    public TextField txtDecreasePrices;
    private SampleService sampleService;
    private AnalysisService analysisService;
    private ObservableList<Entity> samples = FXCollections.observableArrayList();

    /**
     * @param analysisService The service that has functionality for analyses.
     * @param sampleService   The service that has functionality for samples.
     * @param user            The current user who uses the aplication.
     */
    public void setAll(AnalysisService analysisService, SampleService sampleService, User user) {
        this.analysisService = analysisService;
        this.sampleService = sampleService;
        currentUser = user;
    }

    /**
     * the initialisation of the current scene.
     */
    @Override
    protected void initialize() {
        Platform.runLater(() -> {
            entities.clear();
            analysesTable.refresh();
            samples.clear();
            comboBoxSample.getItems().clear();
            updateSamples();
            entities.addAll(analysisService.getAll());
            analysesTable.setItems(entities);
            samples.addAll(sampleService.getAll());
            comboBoxSample.setItems(samples);

        });
    }

    /**
     * the external call of initialisation
     */
    public void continueRefresh() {
        initialize();
    }

    /**
     * Updates changes made in the sampleRepository to sample referances from an Analysis object.
     */
    private void updateSamples() {
        for (Analysis analysis : analysisService.getAll()) {
            analysisService.updateAnalysesSample(analysis);
        }
    }

    /**
     * @param actionEvent The event of clicking the "add analysis" button.
     */

    @Override
    public void addEntity(ActionEvent actionEvent) {
        try {
            String name = txtName.getText();
            double price = Double.parseDouble(txtPrice.getText());
            double lowLimit = Double.parseDouble(txtLowLimit.getText());
            double highLimit = Double.parseDouble(txtHighLimit.getText());
            Sample sample = (Sample) comboBoxSample.getSelectionModel().getSelectedItem();
            analysisService.addAnalysis(name, price, lowLimit, highLimit, sample, currentUser);
            entities.clear();
            entities.addAll(analysisService.getAll());
        } catch (InvalidAnalysisException e) {
            e.printStackTrace();
            Alerts.showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            Alerts.showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * @param mouseEvent the event of selecting an analysis from the table.
     */
    @Override
    public void showSelected(MouseEvent mouseEvent) {
        TableSelectionModel<Entity> tableSelectionModel = analysesTable.getSelectionModel();
        Analysis analysis = (Analysis) tableSelectionModel.getSelectedItem();
        txtName.setText(analysis.getName());
        txtPrice.setText(Double.toString(analysis.getPrice()));
        txtLowLimit.setText(Double.toString(analysis.getLowLimit()));
        txtHighLimit.setText(Double.toString(analysis.getHighLimit()));
        comboBoxSample.getSelectionModel().select(analysis.getSample());
    }

    /**
     * @param actionEvent the event of clicking the "delete analysis" button.
     */
    @Override
    public void deleteEntity(ActionEvent actionEvent) {
        if (Alerts.showQuestion("The analysis will be deleted!", "Delete?").get()) {
            TableSelectionModel<Entity> tableSelectionModel = analysesTable.getSelectionModel();
            Entity abstractEntity = tableSelectionModel.getSelectedItem();
            analysisService.deleteOne(abstractEntity.getId());
            entities.clear();
            analysesTable.refresh();
            entities.addAll(analysisService.getAll());
            analysesTable.setItems(entities);
            txtName.clear();
            txtPrice.clear();
            txtLowLimit.clear();
            txtHighLimit.clear();
            comboBoxSample.getSelectionModel().clearSelection();
        }
    }

    /**
     * @param actionEvent the event of clicking the "update analysis" button.
     */
    @Override
    public void updateEntity(ActionEvent actionEvent) {
        try {
            if (Alerts.showQuestion("The analysis will be updated!", "Update?").get()) {
                TableSelectionModel<Entity> tableSelectionModel = analysesTable.getSelectionModel();
                Analysis analysis = (Analysis) tableSelectionModel.getSelectedItem();
                String name = txtName.getText();
                double price = Double.parseDouble(txtPrice.getText());
                double lowLimit = Double.parseDouble(txtLowLimit.getText());
                double highLimit = Double.parseDouble(txtHighLimit.getText());
                Sample sample = (Sample) comboBoxSample.getSelectionModel().getSelectedItem();
                analysisService.updateAnalysis(analysis.getId(), name, price, lowLimit, highLimit, sample, currentUser);
                entities.clear();
                analysesTable.refresh();
                entities.addAll(analysisService.getAll());
                analysesTable.setItems(entities);
            }
        } catch (InvalidAnalysisException e) {
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
     * @param actionEvent the event of clicking the "increase price" button.
     */
    public void increasePrices(ActionEvent actionEvent) {
        double increase = Double.parseDouble(txtIncreasePrices.getText());
        if (Alerts.showQuestion("All prices are about to be increased by " + increase, "Are you sure?").get()) {
            analysisService.increaseAllPrices(increase);
            txtIncreasePrices.clear();
            initialize();
        }
    }

    /**
     * @param actionEvent the event of clicking the "decrease price" button.
     */
    public void decreasePrices(ActionEvent actionEvent) {
        double decrease = Double.parseDouble(txtDecreasePrices.getText());
        if (Alerts.showQuestion("All prices are about to be deacresed by " + decrease, "Are you sure?").get()) {
            analysisService.decreaseAllPrices(decrease);
            txtDecreasePrices.clear();
            initialize();
        }
    }
}
