package UserInterface;

import Domain.Entities.Analysis;
import Domain.Entities.Patient;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.paint.Color;
import javafx.util.Callback;


public class ColorChanger {
    /**changes colors to red of an analyses table column if the result of the analysis is out of limits.
     *
     * @param tableColumn the analyses table column to be colored.
     */
    public static void makeAnalysesOutOfLimitsRed(TableColumn tableColumn) {
        tableColumn.setCellFactory(new Callback<TableColumn, TableCell>() {
            public TableCell call(TableColumn param) {
                return new TableCell<Analysis, String>() {

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!isEmpty() && item != null) {
                                Analysis analysis = (Analysis) this.getTableRow().getItem();
                                if (analysis != null && !analysis.isInLimits() && !analysis.getScore().equals("in course")) {
                                    this.setTextFill(Color.RED);
                                }

                            setText(item);
                        }
                    }
                };
            }
        });
    }

    /**Changes the color to red of a patient table column in case that a patient requires futher studies.
     *
     * @param tableColumn the patient table column to be colored.
     */
    public static void makeSickPatientsRed(TableColumn tableColumn) {
        tableColumn.setCellFactory(new Callback<TableColumn, TableCell>() {
            public TableCell call(TableColumn param) {
                return new TableCell<Patient, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!isEmpty() && item != null) {
                            Patient patient = (Patient) this.getTableRow().getItem();
                            if(patient!=null && patient.isSick()&& !patient.getDiagnose().equals("in course")){
                                this.setTextFill(Color.RED);
                            }
                            setText(item);
                        }
                    }
                };
            }
        });
    }
    /**Changes the color to blue of a patient table column in case that a patient has all the analyses done but
     *  the diagnostic is not given and to green if the analysis are done and the diagnostic is given
     *
     *
     * @param tableColumn the patient table column to be colored.
     */
    public static void colorFinishPatients(TableColumn tableColumn) {
        tableColumn.setCellFactory(new Callback<TableColumn, TableCell>() {
            public TableCell call(TableColumn param) {
                return new TableCell<Patient, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!isEmpty() && item != null) {
                            Patient patient = (Patient) this.getTableRow().getItem();
                            if(patient!=null ){
                                boolean areDone = true;
                                for(Analysis analysis:patient.getAnalyses()){
                                    if(analysis.getScore().trim().equals("in course")) {
                                        areDone = false;
                                        break;
                                    }
                                }
                                if(areDone){
                                    this.setTextFill(Color.BLUE);
                                    if(patient.getDiagnose() != null && !patient.getDiagnose().equals("in course")){
                                        this.setTextFill(Color.GREEN);
                                    }

                                }


                            }
                            setText(item);
                        }
                    }
                };
            }
        });
    }


}
