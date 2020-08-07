package Service;

import Domain.Entities.Analysis;
import Domain.Entities.Patient;
import Domain.Entities.Sample;
import Domain.Entities.User;
import Domain.Exceptions.InvalidPatientException;
import Domain.Exceptions.WrongIdException;
import Domain.Validators.AnalysisValidator;
import Domain.Validators.CnpValidator;
import Domain.Validators.IValidator;
import Domain.Validators.PatientValidator;
import Domain.ViewModels.NrOfRegistrationPerUserViewModel;
import Domain.ViewModels.PatientsPerAnalysesViewModel;
import Domain.ViewModels.PercentOfIncomePerAnalysesViewModel;
import Repository.IRepository;
import Repository.JSONFileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PatientServiceTest {
    IRepository<Patient> patientIRepository = new JSONFileRepository<>("src/test/java/Service/patientsTest.txt", Patient.class);
    IRepository<Analysis> analysisIRepository = new JSONFileRepository<>("src/test/java/Service/analysesTest.txt", Analysis.class);
    IRepository<Sample> sampleIRepository = new JSONFileRepository<>("src/test/java/Service/samplesTest.txt", Sample.class);
    IValidator<Patient> patientValidator = new PatientValidator();
    IValidator<Analysis> analysesValidator = new AnalysisValidator();
    CnpValidator cnpValidator = new CnpValidator();
    AnalysisService analysisService = new AnalysisService(analysisIRepository, sampleIRepository, analysesValidator);
    PatientService patientService = new PatientService(patientIRepository, analysisIRepository,
            patientValidator, analysesValidator, cnpValidator);
    User user = new User("Tester", "AdminTest", "tester", true, true, true);
    User user2 = new User("Admin", "AdminTest", "tester", true, true, true);
    User user3 = new User("Other", "AdminTest", "tester", true, true, true);
    Sample sample = new Sample("test", false, user);
    Sample sample2 = new Sample("test2", false, user);
    Sample sample3 = new Sample("test3", true, user);
    Analysis analysis = new Analysis("TGP", 20, 4, 15, sample3, user);
    Analysis analysis2 = new Analysis("TGO", 30, 4, 15, sample, user);
    List<Analysis> analysisList = new ArrayList<>();
    List<Analysis> analysisList2 = new ArrayList<>();

    @BeforeEach
    void setUp() {
        patientIRepository.clearAll();
        analysisIRepository.clearAll();
        user2.setId(1);
        user3.setId(2);
        sample2.setId(1);
        sample3.setId(2);
        analysis2.setId(1);
        analysisList.add(analysis);
        analysisList.add(analysis2);
        analysisList2.add(analysis2);
        patientService.addPacient("1960505313805", "Ion", "Pop", analysisList, user);
        patientService.addPacient("1960505313815", "Iona", "Popa", analysisList, user);
        patientService.addPacient("1960505313825", "Ion", "Pop", analysisList2, user3);
    }

    @Test
    void addValidPacient_should_saveThePatientToRepository() {
        LocalDate localDate = LocalDate.of(1995, 5, 18);
        Patient patient = new Patient("1950518313707", "Alin", "Hrenean", localDate,
                analysisList, user);
        patient.setId(3);
        patient.setRequieresAssitance(true);
        patientService.addPacient(patient.getCnp(), patient.getFirstName(), patient.getLastName(),
                patient.getAnalyses(), patient.getUserThatAdded());
        assertEquals(4, patientService.getAll().size());
        Patient samePatient = (Patient) patientService.getOne(3);
        assertEquals(patient, samePatient);
    }

    @Test
    void addInvalidPatient_should_throwInvalidPatientException() {
        assertThrows(InvalidPatientException.class, () ->
                patientService.addPacient("19", "", "", analysisList, user));
    }

    @Test
    void updatePatient_should_updateThePatientIfValid() {
        patientService.update(0, "1980806313707", "Sandu", "Gheorghe",
                analysisList2, user2);
        Patient samePatient = (Patient) patientService.getOne(0);
        assertEquals("1980806313707", samePatient.getCnp());
        assertEquals("Sandu", samePatient.getFirstName());
        assertEquals("Gheorghe", samePatient.getLastName());
        assertEquals(analysisList2, samePatient.getAnalyses());
        assertEquals(user2, samePatient.getUserThatAdded());
    }

    @Test
    void updatePatient_should_throwInvalidPatientExceptionIfInvalid() {
        assertThrows(InvalidPatientException.class, () -> patientService.update(0, "19808063137", "",
                "", analysisList2, user2));
    }

    @Test
    void updateInexistingPatient_should_throwWrongIdException() {
        assertThrows(WrongIdException.class, () -> patientService.update(99, "1980806313707",
                "Sandu", "Gheorghe", analysisList2, user2));
    }

    @Test
    void setAnalysisResult_should_saveTheResultToPatientsAnalysis() {
        patientService.setResult(0, 0, 15.2, user);
        Patient samePatient = (Patient) patientService.getOne(0);
        double result = Double.parseDouble(samePatient.getAnalyses().get(0).getScore());
        assertEquals(15.2, result);
    }

    @Test
    void setAnalysisResult_should_throwWrongIdExceptionIfGivenToInexistingId() {
        assertThrows(WrongIdException.class, () -> patientService.setResult(0, 9, 15.2, user));
    }

    @Test
    void setDiagnotic_should_saveTheDiagnosticToPatient() {
        Patient samePatient = (Patient) patientService.getOne(0);
        patientService.setDiagnostic(samePatient, "sanatos", false, user2);
        assertEquals("sanatos", samePatient.getDiagnose());
        assertFalse(samePatient.isSick());
        assertEquals(user2, samePatient.getUserThatDiagnosed());
    }

    @Test
    void updateAnalyses_should_updateThePatientsAnalysesFromAnalysesRepository() {
        analysisService.addAnalysis(analysis.getName(), analysis.getPrice(), analysis.getLowLimit(),
                analysis.getHighLimit(), analysis.getSample(), analysis.getUserThatAdded());
        analysisService.updateAnalysis(0, "change", 12, 1, 10, sample, user);
        Analysis updatedAnalysis = (Analysis) analysisService.getOne(0);
        Patient samePatient = (Patient) patientService.getOne(0);
        Analysis patientAnalysis = samePatient.getAnalyses().get(0);
        assertNotEquals(updatedAnalysis, patientAnalysis);
        patientService.updateAnalysis(samePatient);
        assertEquals(updatedAnalysis, patientAnalysis);
    }

    @Test
    void getPatientsForADay_should_returnAListWithPatientsForThatDay() {
        Patient samePatient = (Patient) patientService.getOne(0);
        Patient samePatient2 = (Patient) patientService.getOne(1);
        Patient samePatient3 = (Patient) patientService.getOne(2);
        LocalDate localDate = LocalDate.of(2020, 1, 2);
        samePatient.setCurrentDate(localDate);
        patientIRepository.update(samePatient);
        List<Patient> controlList = new ArrayList<>();
        controlList.add(samePatient2);
        controlList.add(samePatient3);
        List<Patient> resultsList = patientService.getPacientsForADay(LocalDate.now());
        assertEquals(controlList, resultsList);
    }

    @Test
    void searchPatientWithKnownId_should_returnAListWithFoundThePatiest() {
        Patient samePatient = (Patient) patientService.getOne(0);
        Patient samePatient2 = (Patient) patientService.getOne(1);
        LocalDate localDate = LocalDate.of(2020, 1, 2);
        LocalDate localDate2 = LocalDate.of(2019, 1, 2);
        samePatient.setCurrentDate(localDate);
        patientIRepository.update(samePatient);
        samePatient2.setCurrentDate(localDate2);
        patientIRepository.update(samePatient2);
        List<Patient> controlList = new ArrayList<>();
        controlList.add(samePatient);
        List<Patient> resultsList = patientService.searchPatients(0, null, null, null,
                localDate, LocalDate.now());
        assertEquals(controlList, resultsList);
    }

    @Test
    void searchPatientWithUnknownId_should_returnAListWithFoundThePatiests() {
        Patient samePatient = (Patient) patientService.getOne(0);
        Patient samePatient2 = (Patient) patientService.getOne(1);
        Patient samePatient3 = (Patient) patientService.getOne(2);
        LocalDate localDate = LocalDate.of(2020, 1, 2);
        LocalDate localDate2 = LocalDate.of(2019, 1, 2);
        samePatient.setCurrentDate(localDate);
        patientIRepository.update(samePatient);
        samePatient2.setCurrentDate(localDate2);
        patientIRepository.update(samePatient2);
        List<Patient> controlList = new ArrayList<>();
        controlList.add(samePatient);
        List<Patient> resultsList = patientService.searchPatients(-1, "1960505313805", null, null,
                localDate, LocalDate.now());
        assertEquals(controlList, resultsList);
        controlList.add(samePatient3);
        List<Patient> resultsList2 = patientService.searchPatients(-1, null, "Ion", null,
                localDate, LocalDate.now());
        assertEquals(controlList, resultsList2);
        List<Patient> resultsList3 = patientService.searchPatients(-1, null, null, "Pop",
                localDate, LocalDate.now());
        assertEquals(controlList, resultsList3);
    }

    @Test
    void getProcentOfIncomePerAnalyses_should_returnAPercentOfIncomePerAnalysesViewModelList() {
        List<PercentOfIncomePerAnalysesViewModel> controlList = new ArrayList<>();
        controlList.add(new PercentOfIncomePerAnalysesViewModel("TGO", "69.231"));
        controlList.add(new PercentOfIncomePerAnalysesViewModel("TGP", "30.769"));
        List<PercentOfIncomePerAnalysesViewModel> resultsList =
                patientService.getProcentOfIncomePerAnalyses(LocalDate.now());
        assertEquals(controlList, resultsList);
    }

    @Test
    void getPatientsPerAnalyses_should_returnAPatientsPerAnalysesViewModelList() {
        List<PatientsPerAnalysesViewModel> controlList = new ArrayList<>();
        controlList.add(new PatientsPerAnalysesViewModel("TGO", 3));
        controlList.add(new PatientsPerAnalysesViewModel("TGP", 2));
        List<PatientsPerAnalysesViewModel> resultsList = patientService.getPatientsPerAnalyses(LocalDate.now());
        assertEquals(controlList, resultsList);
    }

    @Test
    void getNrOfRegistrationPerUser_should_returnANrOfRegistrationPerUserViewModelList() {
        Patient samePatient = (Patient) patientService.getOne(0);
        Patient samePatient2 = (Patient) patientService.getOne(1);
        patientService.setDiagnostic(samePatient, "bolnav", true, user2);
        patientService.setDiagnostic(samePatient2, "bolnav", true, user3);
        List<NrOfRegistrationPerUserViewModel> controlList = new ArrayList<>();
        controlList.add(new NrOfRegistrationPerUserViewModel("Admin", 0, 1));
        controlList.add(new NrOfRegistrationPerUserViewModel("Other", 1, 1));
        controlList.add(new NrOfRegistrationPerUserViewModel("Tester", 2, 0));
        List<NrOfRegistrationPerUserViewModel> resultList =
                patientService.getNrOfRegistrationPerUserViewModels(LocalDate.now());
        assertEquals(controlList, resultList);

    }

}