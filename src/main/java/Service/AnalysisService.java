package Service;

import Domain.Entities.Analysis;
import Domain.Entities.Sample;
import Domain.Entities.User;
import Domain.Exceptions.InvalidAnalysisException;
import Domain.Exceptions.WrongIdException;
import Domain.Validators.IValidator;
import Repository.IRepository;

/**
 * A service that provides functionality for analyses operations.
 */
public class AnalysisService extends AbstractService<Analysis> {
    IRepository<Sample> sampleIRepository;

    /**
     * @param repository        The repository used for the service.
     * @param sampleIRepository the repository that stores the samples.
     * @param analysisValidator The validator used for analyses validation.
     */
    public AnalysisService(IRepository<Analysis> repository, IRepository<Sample> sampleIRepository,
                           IValidator<Analysis> analysisValidator) {
        super(repository, analysisValidator);
        this.sampleIRepository = sampleIRepository;
    }

    /**
     * Creates a new analysis if is valid and saves it to repository.
     *
     * @param name          The name of the analysis.
     * @param price         the price of the analysis.
     * @param lowLimit      the low limit of the analysis's result to be considered in limits.
     * @param highLimit     the high limit of the analysis's result to be considered in limits
     * @param sample        the sample that the analysis requires.
     * @param userThatAdded the user that registered the analysis in the system
     */
    public void addAnalysis(String name, double price, double lowLimit, double highLimit, Sample sample,
                            User userThatAdded) {
        Analysis analysis = new Analysis(name, price, lowLimit, highLimit, sample, userThatAdded);
        validator.validateBeforeRegistration(analysis);
        validateUniqueAnalysis(analysis);
        repository.create(analysis);
    }

    /**
     * Checks if the name was already given to another analysis.
     * throws InvalidAnalysisException if the name was already given.
     *
     * @param analysis The name that a user is associated with.
     */
    private void validateUniqueAnalysis(Analysis analysis) {
        for (Analysis analysis1 : repository.read()) {
            if (analysis.getName().trim().equals(analysis1.getName().trim())) {
                throw new InvalidAnalysisException("The name is already taken");
            }
        }
    }

    /**
     * Updates changes made in the sampleRepository to sample referances from an Analysis object.
     *
     * @param analysis The analysis to be updated.
     */
    public void updateAnalysesSample(Analysis analysis) {
        for (Sample sample : sampleIRepository.read()) {
            if (analysis.getSample().getId() == sample.getId() && !analysis.getSample().equals(sample)) {
                analysis.getSample().setName(sample.getName());
                analysis.getSample().setAssitantRequired(sample.isAssitantRequired());
                analysis.getSample().setUserThatAdded(sample.getUserThatAdded());
            }
        }
        repository.update(analysis);
    }

    /**
     * Updates a analysis from repository.
     * throws WrongIdExcepton if not found.
     *
     * @param id            the id of the analysis
     * @param name          The name of the analysis.
     * @param price         the price of the analysis.
     * @param lowLimit      the low limit of the analysis's result to be considered in limits.
     * @param highLimit     the high limit of the analysis's result to be considered in limits
     * @param sample        the sample that the analysis requires.
     * @param userThatAdded the user that registered the analysis in the system
     */
    public void updateAnalysis(int id, String name, double price, double lowLimit, double highLimit, Sample sample,
                               User userThatAdded) {
        for (Analysis analysis : repository.read()) {
            if (analysis.getId() == id) {
                analysis.setName(name);
                analysis.setHighLimit(highLimit);
                analysis.setLowLimit(lowLimit);
                analysis.setPrice(price);
                analysis.setSample(sample);
                analysis.setUserThatAdded(userThatAdded);
                validator.validateBeforeRegistration(analysis);
                repository.update(analysis);
                return;
            }
        }
        throw new WrongIdException("The analysis id does not exists");

    }

    /**
     * increases all analyses prices with a given number
     *
     * @param increase the number that will increase by.
     */
    public void increaseAllPrices(double increase) {
        for (Analysis analysis : repository.read()) {
            analysis.setPrice(analysis.getPrice() + increase);
            repository.update(analysis);
        }
    }

    /**
     * decreases all analyses prices with a given numer
     *
     * @param decrease the number that will decrease by.
     */

    public void decreaseAllPrices(double decrease) {
        for (Analysis analysis : repository.read()) {
            if (analysis.getPrice() > decrease) {
                analysis.setPrice(analysis.getPrice() - decrease);
            } else {
                analysis.setPrice(0.0);
            }
            repository.update(analysis);
        }
    }


}
