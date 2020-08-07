package Domain.Entities;

import java.util.Objects;

/**
 * Represents a clinical Analysis.
 */

public class Analysis extends Entity {
    String name;
    double price;
    double score;
    double lowLimit;
    double highLimit;
    boolean inLimits;
    Sample sample;
    User userThatAdded;
    User workedBy;

    /**
     * @param name          The name of the analysis.
     * @param price         the price of the analysis.
     * @param lowLimit      the low limit of the analysis's result to be considered in limits.
     * @param highLimit     the high limit of the analysis's result to be considered in limits
     * @param sample        the sample that the analysis requires.
     * @param userThatAdded the user that registered the analysis in the system
     */
    public Analysis(String name, double price, double lowLimit, double highLimit, Sample sample, User userThatAdded) {
        this.name = name;
        this.price = price;
        this.lowLimit = lowLimit;
        this.highLimit = highLimit;
        this.sample = sample;
        this.userThatAdded = userThatAdded;
        this.score = 0.0;
        this.inLimits = determineIfInLimits();
    }


    /**
     * @return The user that registered the analysis in the system
     */
    public User getUserThatAdded() {
        return userThatAdded;
    }

    /**
     * @param userThatAdded the user that registered the analysis in the system
     */
    public void setUserThatAdded(User userThatAdded) {
        this.userThatAdded = userThatAdded;
    }

    /**
     * @return the name of the analysis as String.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name of the analysis.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the price of the analysis as double.
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price of the analysis.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the analysis result as String.
     */
    public String getScore() {
        if (this.score == 0.0) return "in course";
        return Double.toString(score);
    }

    /**
     * @param score the analysis result as double.
     */
    public void setScore(double score) {

        this.score = score;
        this.inLimits = determineIfInLimits();
    }

    /**
     * @return the low limit of the analysis's result to be considered in limits as double.
     */
    public double getLowLimit() {
        return lowLimit;
    }

    /**
     * @param lowLimit the low limit of the analysis's result to be considered in limits.
     */
    public void setLowLimit(double lowLimit) {

        this.lowLimit = lowLimit;
        this.inLimits = determineIfInLimits();
    }

    /**
     * @return the high limit of the analysis's result to be considered in limits as double.
     */

    public double getHighLimit() {

        return highLimit;

    }

    /**
     * @param highLimit the high limit of the analysis's result to be considered in limits.
     */
    public void setHighLimit(double highLimit) {
        this.highLimit = highLimit;
        this.inLimits = determineIfInLimits();
    }

    /**
     * @return whether the results is in limits
     */
    public boolean isInLimits() {
        return inLimits;
    }

    /**
     * @return "Yes" if inLimits is true or "No" otherwise.
     */
    public String getInLimits() {
        if (inLimits) return "Yes";
        return "No";
    }


    /**
     * @return the sample that the analysis requires.
     */
    public Sample getSample() {
        return sample;
    }

    /**
     * @param sample the sample that the analysis requires.
     */
    public void setSample(Sample sample) {
        this.sample = sample;
    }

    /**
     * @return the User that wrote the analysis result.
     */
    public User getWorkedBy() {

        return workedBy;
    }

    /**
     * @param workedBy the User that wrote the analysis result.
     */
    public void setWorkedBy(User workedBy) {
        this.workedBy = workedBy;
    }

    /**
     * @return true if the result is in limits or false otherwise
     */
    private boolean determineIfInLimits() {
        if (score <= highLimit && score >= lowLimit) return true;
        return false;
    }

    /**
     * @return a copy of the analysis
     */
    @Override
    public Entity clone() {
        Analysis analysis = new Analysis(name, price, lowLimit, highLimit, sample, userThatAdded);
        analysis.setId(getId());
        return analysis;
    }

    /**
     * @return the name of the analysis as String
     */
    @Override
    public String toString() {
        return getName();
    }

    /**
     * @param o An Object the current instance is compared to.
     * @return Whether the current instance is equal to the object.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Analysis)) return false;
        Analysis analysis = (Analysis) o;
        return
                Double.compare(analysis.getLowLimit(), getLowLimit()) == 0 &&
                        Double.compare(analysis.getHighLimit(), getHighLimit()) == 0 &&
                        getName().equals(analysis.getName()) &&
                        getSample().equals(analysis.getSample()) &&
                        getUserThatAdded().equals(analysis.getUserThatAdded());
    }

    /**
     * @return a hashCode representation of the current instance.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getName(), getPrice(), getLowLimit(), getHighLimit(), isInLimits(), getSample());
    }
}
