package Domain.ViewModels;

import java.util.Objects;

/**
 * View model used for displaying an analyses names with their number of patients.
 */
public class PatientsPerAnalysesViewModel {
    String analysisName;
    int numberOfPatients;

    /**
     * @param analysisName     The name of the analysis.
     * @param numberOfPatients the number of patients that had the current analysis.
     */
    public PatientsPerAnalysesViewModel(String analysisName, int numberOfPatients) {
        this.analysisName = analysisName;
        this.numberOfPatients = numberOfPatients;
    }

    /**
     * @return The name of the analysis as String.
     */
    public String getAnalysisName() {
        return analysisName;
    }

    /**
     * @param analysisName The name of the analysis.
     */
    public void setAnalysisName(String analysisName) {
        this.analysisName = analysisName;
    }

    /**
     * @return the number of patients that had the current analysis as Integer.
     */
    public int getNumberOfPatients() {
        return numberOfPatients;
    }

    /**
     * @param numberOfPatients the number of patients that had the current analysis.
     */
    public void setNumberOfPatients(int numberOfPatients) {
        this.numberOfPatients = numberOfPatients;
    }

    /**
     * @param o An Object the current instance is compared to.
     * @return Whether the current instance is equal to the object.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PatientsPerAnalysesViewModel)) return false;
        PatientsPerAnalysesViewModel that = (PatientsPerAnalysesViewModel) o;
        return getNumberOfPatients() == that.getNumberOfPatients() &&
                Objects.equals(getAnalysisName(), that.getAnalysisName());
    }

    /**
     * @return a hashCode representation of the current instance.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getAnalysisName(), getNumberOfPatients());
    }
}
