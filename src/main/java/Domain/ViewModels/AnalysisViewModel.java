package Domain.ViewModels;

/**
 * View model used for sending a patient's analyses information to be  filled as a Jasper report
 */

public class AnalysisViewModel {
    String name;
    String score;
    Double lowLimit;
    Double highLimit;
    Boolean inLimits;
    String workedBy;

    /**
     * @param name      the name of the analysis.
     * @param score     the analysis result.
     * @param lowLimit  the low limit of the analysis's result to be considered in limits.
     * @param highLimit the high limit of the analysis's result to be considered in limits.
     * @param inLimits  whether the results is in limits.
     * @param workedBy  the name of the User that wrote the analysis result.
     */
    public AnalysisViewModel(String name, String score, Double lowLimit, Double highLimit, Boolean inLimits, String workedBy) {
        this.name = name;
        this.score = score;
        this.lowLimit = lowLimit;
        this.highLimit = highLimit;
        this.inLimits = inLimits;
        this.workedBy = workedBy;
    }

    /**
     * @return the name of the analysis as String.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name of the analysis.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the analysis result as String.
     */
    public String getScore() {
        return score;
    }

    /**
     * @param score the analysis result.
     */
    public void setScore(String score) {
        this.score = score;
    }

    /**
     * @return the low limit of the analysis's result to be considered in limits as double.
     */
    public Double getLowLimit() {
        return lowLimit;
    }

    /**
     * @param lowLimit the low limit of the analysis's result to be considered in limits.
     */
    public void setLowLimit(Double lowLimit) {
        this.lowLimit = lowLimit;
    }

    /**
     * @return the high limit of the analysis's result to be considered in limits as double.
     */
    public Double getHighLimit() {
        return highLimit;
    }

    /**
     * @param highLimit the low limit of the analysis's result to be considered in limits.
     */
    public void setHighLimit(Double highLimit) {
        this.highLimit = highLimit;
    }

    /**
     * @return whether the results is in limits.
     */
    public Boolean getInLimits() {
        return inLimits;
    }

    /**
     * @param inLimits whether the results is in limits.
     */
    public void setInLimits(Boolean inLimits) {
        this.inLimits = inLimits;
    }

    /**
     * @return the name of the User that wrote the analysis result as String.
     */
    public String getWorkedBy() {
        return workedBy;
    }

    /**
     * @param workedBy the name of the User that wrote the analysis result.
     */
    public void setWorkedBy(String workedBy) {
        this.workedBy = workedBy;
    }
}
