package Domain.ViewModels;

import java.util.Objects;

/**
 * View model used for displaying the name of an analysis and its procent of the total income.
 */
public class PercentOfIncomePerAnalysesViewModel {
    String analysisName;
    String percent;

    /**
     * @param analysisName The name of the analysis.
     * @param percent      the percentage of the total income of the current analysis.
     */
    public PercentOfIncomePerAnalysesViewModel(String analysisName, String percent) {
        this.analysisName = analysisName;
        this.percent = percent;
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
     * @return the percentage of the total income of the current analysis as String.
     */
    public String getPercent() {
        return percent;
    }

    /**
     * @param percent the percentage of the total income of the current analysis.
     */
    public void setPercent(String percent) {
        this.percent = percent;
    }

    /**
     * @param o An Object the current instance is compared to.
     * @return Whether the current instance is equal to the object.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PercentOfIncomePerAnalysesViewModel)) return false;
        PercentOfIncomePerAnalysesViewModel that = (PercentOfIncomePerAnalysesViewModel) o;
        return Objects.equals(getAnalysisName(), that.getAnalysisName()) &&
                Objects.equals(getPercent(), that.getPercent());
    }

    /**
     * @return A string representation of the current instance
     */
    @Override
    public String toString() {
        return "PercentOfIncomePerAnalysesViewModel{" +
                "analysisName='" + analysisName + '\'' +
                ", percent='" + percent + '\'' +
                '}';
    }

    /**
     * @return a hashCode representation of the current instance.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getAnalysisName(), getPercent());
    }
}
