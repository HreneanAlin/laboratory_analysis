package Service;

import Domain.Entities.Analysis;
import Domain.Entities.Cnp;
import Domain.Entities.Patient;
import Domain.Entities.User;
import Domain.Exceptions.WrongIdException;
import Domain.Validators.CnpValidator;
import Domain.Validators.IValidator;
import Domain.ViewModels.NrOfRegistrationPerUserViewModel;
import Domain.ViewModels.PatientsPerAnalysesViewModel;
import Domain.ViewModels.PercentOfIncomePerAnalysesViewModel;
import Repository.IRepository;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A service that provides functionality for patients operations.
 */
public class PatientService extends AbstractService<Patient> {
    IRepository<Analysis> analysisIRepository;
    IValidator<Analysis> analysisIValidator;
    CnpValidator cnpValidator;

    /**
     * @param repository          The repository used for the service.
     * @param analysisIRepository the repository that stores the analyses.
     * @param patientValidator    The validator used for patients validation.
     * @param analysisIValidator  The validator used for analyses validation.
     * @param cnpValidator        The validator used for CNP validation.
     */
    public PatientService(IRepository<Patient> repository, IRepository<Analysis> analysisIRepository,
                          IValidator<Patient> patientValidator, IValidator<Analysis> analysisIValidator, CnpValidator cnpValidator) {
        super(repository, patientValidator);
        this.analysisIRepository = analysisIRepository;
        this.analysisIValidator = analysisIValidator;
        this.cnpValidator = cnpValidator;
    }

    /**
     * Creates a new patient if is valid and saves it to repository.
     *
     * @param cnp       the CNP of the patient.
     * @param firstName the fist name of the patient.
     * @param lastName  the last name of the patient.
     * @param analyses  the list of analyses of the patient.
     * @param user      the user that registered the patient in the system.
     */
    public void addPacient(String cnp, String firstName, String lastName, List<Analysis> analyses,
                           User user) {

        List<Analysis> analysisList = new ArrayList<>();
        for (Analysis analysis : analyses) {
            analysisList.add((Analysis) analysis.clone());
        }
        cnpValidator.validate(cnp);
        Cnp calculatedCnp = new Cnp(cnp);
        Patient patient = new Patient(calculatedCnp.getValue(), firstName, lastName, calculatedCnp.getBirthdate(), analysisList, user);
        validator.validateBeforeRegistration(patient);
        determineAssistance(patient);
        repository.create(patient);

    }

    /**
     * Updates a patient from repository.
     * throws WrongIdExcepton if not found.
     *
     * @param id        the id of the patient.
     * @param cnp       the CNP of the patient.
     * @param firstName the fist name of the patient.
     * @param lastName  the last name of the patient.
     * @param analyses  the list of analyses of the patient.
     * @param user      the user that registered the patient in the system.
     */
    public void update(int id, String cnp, String firstName, String lastName, List<Analysis> analyses,
                       User user) {
        for (Patient patient : repository.read()) {
            if (id == patient.getId()) {
                cnpValidator.validate(cnp);
                Cnp calculatedCnp = new Cnp(cnp);
                patient.setCnp(calculatedCnp.getValue());
                patient.setFirstName(firstName);
                patient.setLastName(lastName);
                patient.setBirthDate(calculatedCnp.getBirthdate());
                patient.setAnalyses(analyses);
                patient.setUserThatAdded(user);
                determineAssistance(patient);
                validator.validateBeforeRegistration(patient);
                repository.update(patient);
                return;
            }

        }
        throw new WrongIdException("The patient Id does not exists!");
    }

    /**
     * Updates changes made in the analyses Repository to analyses referances from a patient object.
     *
     * @param patient the patient to be updated.
     */
    public void updateAnalysis(Patient patient) {
        for (Analysis analysis : analysisIRepository.read()) {
            for (Analysis pacientAnalysis : patient.getAnalyses()) {
                if (analysis.getId() == pacientAnalysis.getId() && !analysis.equals(pacientAnalysis)) {
                    pacientAnalysis.setName(analysis.getName());
                    pacientAnalysis.setUserThatAdded(analysis.getUserThatAdded());
                    pacientAnalysis.setSample(analysis.getSample());
                    pacientAnalysis.setLowLimit(analysis.getLowLimit());
                    pacientAnalysis.setHighLimit(analysis.getHighLimit());


                }
            }
        }
        determineAssistance(patient);
        repository.update(patient);
    }

    /**
     * determines if a patient requires medical assitance for sampling.
     *
     * @param patient the current patient.
     */
    private void determineAssistance(Patient patient) {
        boolean ok = false;
        for (Analysis analysis : patient.getAnalyses()) {
            if (analysis.getSample().isAssitantRequired()) {
                ok = true;
                break;

            }
        }
        patient.setRequieresAssitance(ok);

    }

    /**
     * sets given result to an analysis of the patient if is valid.
     * throws WrongIdException if the analysis id is not found.
     *
     * @param pacientId  the id of the patient.
     * @param analysesId the id of the analysis.
     * @param result     the result that is given.
     * @param workedBy   the user who wrote the result.
     */
    public void setResult(int pacientId, int analysesId, Double result, User workedBy) {
        Patient patient = repository.read(pacientId);
        for (Analysis analysis : patient.getAnalyses()) {
            if (analysis.getId() == analysesId) {
                analysis.setScore(result);
                analysis.setWorkedBy(workedBy);
                analysisIValidator.validateAfter(analysis);
                repository.update(patient);
                return;
            }
        }
        throw new WrongIdException("The Analysis id does not exists ");
    }

    /**
     * sets given diagnostic to a patient
     *
     * @param patient    the patient to be diagnosed.
     * @param diagnostic the diagnostic that is given.
     * @param isSick     Whether the patient requires futher studies.
     * @param diagsoneBy the user who wrote the diagnostic.
     */
    public void setDiagnostic(Patient patient, String diagnostic, boolean isSick, User diagsoneBy) {
        patient.setDiagnose(diagnostic);
        patient.setSick(isSick);
        patient.setUserThatDiagnosed(diagsoneBy);
        validator.validateAfter(patient);
        repository.update(patient);
    }

    /**
     * @param localDate the date that is given.
     * @return a list that contains all the patients that were registered in the given  date.
     */
    public List<Patient> getPacientsForADay(LocalDate localDate) {
        List<Patient> patientList = new ArrayList<>();
        for (Patient patient : repository.read()) {
            if (localDate.equals(patient.getCurrentDate())) {
                patientList.add(patient);
            }
        }
        return patientList;
    }

    /**
     * searches patients by diffrent queries.
     *
     * @param id        the id of the patient that is searched.
     * @param cnp       the CNP of the patient that is searched.
     * @param firstName the first name of the patient that is searched.
     * @param lastName  the last name of the patient that is searched.
     * @param start     starting date for the search.
     * @param stop      stopping date for the search.
     * @return a list that contains the corresponding patients.
     */
    public List<Patient> searchPatients(int id, String cnp, String firstName, String lastName,
                                        LocalDate start, LocalDate stop) {
        List<Patient> results = new ArrayList<>();
        if (ifIdExists(id, results, start, stop)) return results;
        for (Patient patient : repository.read()) {
            if (differentDay(start, stop, patient) || areEquals(start, stop, patient)) {
                if (cnp != null && !cnp.isEmpty() && patient.getCnp().equals(cnp)) results.add(patient);
                if (firstName != null && !firstName.isEmpty() && patient.getFirstName().equals(firstName))
                    results.add(patient);
                if (lastName != null && !lastName.isEmpty() && patient.getLastName().equals(lastName))
                    results.add(patient);
            }
        }
        return results;
    }

    /**
     * @param start   starting date for the search.
     * @param stop    stopping date for the search.
     * @param patient a patient that is checked for the search.
     * @return whether the starting date and the stoping date are the same as the patient's
     * date of registration.
     */
    private boolean areEquals(LocalDate start, LocalDate stop, Patient patient) {
        return patient.getCurrentDate().isEqual(start) || patient.getCurrentDate().isEqual(stop);
    }

    /**
     * @param start   starting date for the search.
     * @param stop    stopping date for the search.
     * @param patient a patient that is checked for the search.
     * @return whether the date of registration is after the starting date and before the stopping date
     */
    private boolean differentDay(LocalDate start, LocalDate stop, Patient patient) {
        return patient.getCurrentDate().isAfter(start) && patient.getCurrentDate().isBefore(stop);
    }

    /**
     * @param id      the id of the patient that is searched.
     * @param results The list of patients to be filled by the search.
     * @param start   starting date for the search.
     * @param stop    stopping date for the search.
     * @return whether the given id exists.
     */
    private boolean ifIdExists(int id, List<Patient> results, LocalDate start, LocalDate stop) {
        if (id != -1) {
            Patient patient = repository.read(id);
            if (patient == null) return false;
            if (differentDay(start, stop, patient) || areEquals(start, stop, patient)) {
                results.add(repository.read(id));
                return true;
            }
        }
        return false;
    }

    /**
     * @param untilDate the stopping date.
     * @return a List of PercentOfIncomePerAnalysesViewModel corresponding with patients
     * registered until the stopping date.
     */
    public List<PercentOfIncomePerAnalysesViewModel> getProcentOfIncomePerAnalyses(LocalDate untilDate) {

        List<PercentOfIncomePerAnalysesViewModel> results = new ArrayList<>();
        List<Analysis> analysesList = getAnalysisList(untilDate);
        double totalIncome = getTotalIncome(untilDate);
        fillPercentOfIncomePerAnalyses(untilDate, results, analysesList, totalIncome);

        results.sort(((o1, o2) -> Double.compare(Double.parseDouble(o2.getPercent()),
                                                 Double.parseDouble(o1.getPercent()))));

        return results;
    }

    /**
     * @param untilDate    the stopping date.
     * @param results      the List of PercentOfIncomePerAnalysesViewModel to be filled.
     * @param analysesList all the analyses given to all patients until the stopping date.
     * @param totalIncome  the total income until the stopping date.
     */
    private void fillPercentOfIncomePerAnalyses(LocalDate untilDate, List<PercentOfIncomePerAnalysesViewModel> results,
                                                List<Analysis> analysesList, double totalIncome) {
        DecimalFormat df3 = new DecimalFormat("#.###");
        for (Analysis analysis : analysesList) {
            String name = analysis.getName();
            double analysesIncome = getAnalysisIncome(analysis.getId(), untilDate);
            double percent = (analysesIncome * 100.0) / totalIncome;
            PercentOfIncomePerAnalysesViewModel percentOfIncomePerAnalysesViewModel =
                    new PercentOfIncomePerAnalysesViewModel(name, df3.format(percent));
            results.add(percentOfIncomePerAnalysesViewModel);
        }
    }

    /**
     * Determines the income of an analysis until the stopiing date.
     *
     * @param id        the id of the analysis.
     * @param untilDate the stopping date.
     * @return the analysis income as double.
     */
    private double getAnalysisIncome(int id, LocalDate untilDate) {
        double sum = 0.0;
        for (Patient patient : repository.read()) {
            if (untilDate.isAfter(patient.getCurrentDate()) || untilDate.isEqual(patient.getCurrentDate())) {
                for (Analysis analysis : patient.getAnalyses()) {
                    if (analysis.getId() == id) {
                        sum += analysis.getPrice();
                    }

                }
            }
        }

        return sum;
    }

    /**
     * @param untilDate the stopping date.
     * @return the total income of all analyses until the stopping date as double.
     */
    public double getTotalIncome(LocalDate untilDate) {
        double sum = 0.0;
        for (Patient patient : repository.read()) {
            if (untilDate.isAfter(patient.getCurrentDate()) || untilDate.isEqual(patient.getCurrentDate())) {
                sum += patient.getPriceToBePaid();
            }
        }
        return sum;
    }

    /**
     * @param untilDate the stopping date.
     * @return a List of PatientsPerAnalysesViewModel corresponding with patients
     * registered until the stopping date.
     */
    public List<PatientsPerAnalysesViewModel> getPatientsPerAnalyses(LocalDate untilDate) {
        List<PatientsPerAnalysesViewModel> results = new ArrayList<>();
        List<Analysis> analysesList = getAnalysisList(untilDate);
        fillPatientsPerAnalysesResults(untilDate, results, analysesList);
        results.sort(((o1, o2) -> Integer.compare(o2.getNumberOfPatients(), o1.getNumberOfPatients())));
        return results;
    }

    /**
     * @param untilDate    the stopping date.
     * @param results      the List of patientsPerAnalysesViewModels to be filled.
     * @param analysesList all the analyses given to all patients until the stopping date.
     */
    private void fillPatientsPerAnalysesResults(LocalDate untilDate, List<PatientsPerAnalysesViewModel> results, List<Analysis> analysesList) {
        for (Analysis analysis : analysesList) {
            String name = analysis.getName();
            int number = getNumberOfAnalyses(analysis.getId(), untilDate);
            PatientsPerAnalysesViewModel patientsPerAnalysesViewModel =
                    new PatientsPerAnalysesViewModel(name, number);
            results.add(patientsPerAnalysesViewModel);
        }
    }

    /**
     * @param untilDate the stopping date.
     * @return a List with all the analyses given to all patients until the stopping date.
     */
    private List<Analysis> getAnalysisList(LocalDate untilDate) {
        List<Analysis> analysesList = new ArrayList<>();
        for (Patient patient : repository.read()) {
            if (untilDate.isAfter(patient.getCurrentDate()) || untilDate.isEqual(patient.getCurrentDate())) {
                for (Analysis analysis : patient.getAnalyses()) {
                    if (!analysesList.contains(analysis)) {
                        analysesList.add(analysis);
                    }

                }

            }
        }
        return analysesList;
    }

    /**
     * @param analysisId the analysis id.
     * @param untilDate  the stopping date
     * @return the number of patients who had the analysis until the stopping date
     */
    private int getNumberOfAnalyses(int analysisId, LocalDate untilDate) {
        int contor = 0;
        for (Patient patient : repository.read()) {
            if (untilDate.isAfter(patient.getCurrentDate()) || untilDate.isEqual(patient.getCurrentDate())) {
                for (Analysis analysis : patient.getAnalyses()) {
                    if (analysis.getId() == analysisId) {
                        contor++;

                    }
                }
            }
        }


        return contor;
    }

    /**
     * @param untilDate the stopping date.
     * @return a List of nrOfRegistrationPerUserViewModels corresponding with patients
     * registered until the stopping date.
     */
    public List<NrOfRegistrationPerUserViewModel> getNrOfRegistrationPerUserViewModels(LocalDate untilDate) {
        List<NrOfRegistrationPerUserViewModel> results = new ArrayList<>();
        List<User> usersThatRegistrated = new ArrayList<>();
        List<User> usersThatValidated = new ArrayList<>();
        fillUsersThatRegistrated(untilDate, usersThatRegistrated);
        fillUsersThatValidated(untilDate, usersThatValidated);
        fillUsersThatRegistartedToViewModelResults(untilDate, results, usersThatRegistrated);
        fillUsersThatValidatedToViewModelResults(untilDate, results, usersThatRegistrated, usersThatValidated);
        results.sort((Comparator.comparing(NrOfRegistrationPerUserViewModel::getUserName)));
        return results;
    }

    /**
     * @param untilDate            the stopping date.
     * @param results              the List of nrOfRegistrationPerUserViewModels to be filled.
     * @param usersThatRegistrated the list of useres that wrote a least one diagnostic.
     * @param usersThatValidated   the list of useres that registered a least one patient.
     */
    private void fillUsersThatValidatedToViewModelResults(LocalDate untilDate, List<NrOfRegistrationPerUserViewModel> results, List<User> usersThatRegistrated, List<User> usersThatValidated) {
        for (User user : usersThatValidated) {
            String name = user.getName();
            int nrOfValidation = getNumberOfDiagnosticsForUser(user.getId(), untilDate);
            if (usersThatRegistrated.contains(user)) {
                for (NrOfRegistrationPerUserViewModel nrOfRegistrationPerUserViewModel : results) {
                    if (nrOfRegistrationPerUserViewModel.getUserName().equals(name)) {
                        nrOfRegistrationPerUserViewModel.setNrOfValidation(nrOfValidation);
                    }

                }
            } else {
                NrOfRegistrationPerUserViewModel nrOfRegistrationPerUserViewModel =
                        new NrOfRegistrationPerUserViewModel(name, 0, nrOfValidation);
                results.add(nrOfRegistrationPerUserViewModel);
            }
        }
    }

    /**
     * @param untilDate            the stopping date.
     * @param results              the List of nrOfRegistrationPerUserViewModels to be filled.
     * @param usersThatRegistrated the list of useres that registered a least one patient.
     */
    private void fillUsersThatRegistartedToViewModelResults(LocalDate untilDate, List<NrOfRegistrationPerUserViewModel> results, List<User> usersThatRegistrated) {
        for (User user : usersThatRegistrated) {
            String name = user.getName();
            int nrOfRegistration = getNumberOfRegistrationsForUser(user.getId(), untilDate);
            NrOfRegistrationPerUserViewModel nrOfRegistrationPerUserViewModel =
                    new NrOfRegistrationPerUserViewModel(name, nrOfRegistration, 0);
            results.add(nrOfRegistrationPerUserViewModel);
        }
    }

    /**
     * @param id        the id of the user that is checked.
     * @param untilDate the stopping date.
     * @return the number of diagnostics that current user made.
     */
    private int getNumberOfDiagnosticsForUser(int id, LocalDate untilDate) {
        int contor = 0;
        for (Patient patient : repository.read()) {
            if (untilDate.isAfter(patient.getCurrentDate()) || untilDate.isEqual(patient.getCurrentDate())) {
                if (patient.getUserThatDiagnosed() != null && patient.getUserThatDiagnosed().getId() == id) {
                    contor++;
                }
            }
        }

        return contor;
    }

    /**
     * @param id        the id of the user that is checked.
     * @param untilDate the stopping date.
     * @return the number of diagnostics that current user made.
     */
    private int getNumberOfRegistrationsForUser(int id, LocalDate untilDate) {
        int contor = 0;
        for (Patient patient : repository.read()) {
            if (untilDate.isAfter(patient.getCurrentDate()) || untilDate.isEqual(patient.getCurrentDate())) {
                if (patient.getUserThatAdded().getId() == id) {
                    contor++;
                }
            }
        }

        return contor;
    }

    /**
     * @param untilDate          the stopping date.
     * @param usersThatValidated the list of users that had given a least one diagnostic until the stopping date.
     */
    private void fillUsersThatValidated(LocalDate untilDate, List<User> usersThatValidated) {
        for (Patient patient : repository.read()) {
            if (untilDate.isAfter(patient.getCurrentDate()) || untilDate.isEqual(patient.getCurrentDate())) {
                if (patient.getUserThatDiagnosed() != null && !usersThatValidated.contains(patient.getUserThatDiagnosed())) {
                    usersThatValidated.add(patient.getUserThatDiagnosed());
                }
            }
        }
    }

    /**
     * @param untilDate            the stopping date.
     * @param usersThatRegistrated the list of users that registered at least one patient
     *                             until the stopiing date.
     */
    private void fillUsersThatRegistrated(LocalDate untilDate, List<User> usersThatRegistrated) {
        for (Patient patient : repository.read()) {
            if (untilDate.isAfter(patient.getCurrentDate()) || untilDate.isEqual(patient.getCurrentDate())) {
                if (!usersThatRegistrated.contains(patient.getUserThatAdded())) {
                    usersThatRegistrated.add(patient.getUserThatAdded());
                }
            }
        }
    }


}
