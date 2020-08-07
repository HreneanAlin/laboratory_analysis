package Service;

import Domain.Entities.Analysis;
import Domain.Entities.Sample;
import Domain.Entities.User;
import Domain.Exceptions.InvalidAnalysisException;
import Domain.Exceptions.WrongIdException;
import Domain.Validators.AnalysisValidator;
import Domain.Validators.IValidator;
import Domain.Validators.SampleValidator;
import Repository.IRepository;
import Repository.JSONFileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnalysisServiceTest {
    IRepository<Sample> sampleIRepository = new JSONFileRepository<>("src/test/java/Service/samplesTest.txt", Sample.class);
    IRepository<Analysis> analysisIRepository = new JSONFileRepository<>("src/test/java/Service/analysesTest.txt", Analysis.class);
    IValidator<Analysis> analysesValidator = new AnalysisValidator();
    IValidator<Sample> sampleValidator = new SampleValidator();
    SampleService sampleService = new SampleService(sampleIRepository, sampleValidator);
    AnalysisService analysisService = new AnalysisService(analysisIRepository, sampleIRepository, analysesValidator);
    User user = new User("Admin", "AdminTest", "tester", true, true, true);
    Sample sample = new Sample("test", true, user);
    Sample sample2 = new Sample("test2", true, user);
    Analysis analysis = new Analysis("TGP", 20, 4, 15, sample, user);
    Analysis analysis2 = new Analysis("TGO", 30, 4, 15, sample, user);

    @BeforeEach
    public void setUp() {
        sampleIRepository.clearAll();
        analysisIRepository.clearAll();
        analysis.setId(0);
        analysisService.addAnalysis(analysis.getName(), analysis.getPrice(), analysis.getLowLimit(),
                analysis.getHighLimit(), analysis.getSample(), analysis.getUserThatAdded());

    }

    @Test
    void addValidAnalysis_should_saveTheAnalysisToRepository() {
        analysis2.setId(1);
        analysisService.addAnalysis(analysis2.getName(), analysis2.getPrice(), analysis2.getLowLimit(),
                analysis2.getHighLimit(), analysis2.getSample(), analysis2.getUserThatAdded());
        assertEquals(2, analysisService.getAll().size());
        Analysis sameAnalysis = (Analysis) analysisService.getOne(analysis2.getId());
        assertEquals(analysis2, sameAnalysis);
    }

    @Test
    void updateAnalysis_should_updateTheAnalysisIfValid() {
        analysisService.updateAnalysis(analysis.getId(), "Change", 16, 12, 90, sample, user);
        Analysis sameAnalysis = (Analysis) analysisService.getOne(analysis.getId());
        assertEquals("Change", sameAnalysis.getName());
        assertEquals(16, sameAnalysis.getPrice());
        assertEquals(12, sameAnalysis.getLowLimit());
        assertEquals(90, sameAnalysis.getHighLimit());
    }

    @Test
    void addInvalidAnalysis_should_throwInvalidAnalysisException() {
        assertThrows(InvalidAnalysisException.class, () -> analysisService.addAnalysis("", -9, 10,
                5, sample, user));

    }

    @Test
    void updateAnalysis_should_throwInvalidAnalysisExceptionIfInvalid() {
        assertThrows(InvalidAnalysisException.class, () -> analysisService.updateAnalysis(analysis.getId(), "", -9,
                10, 5, sample, user));

    }

    @Test
    void addingNewAnalysisWithOccupiedName_should_throwInvalidAnalysisException() {
        assertThrows(InvalidAnalysisException.class, () -> analysisService.addAnalysis(analysis.getName(),
                analysis.getPrice(), analysis.getLowLimit(), analysis.getHighLimit(), analysis.getSample(),
                analysis.getUserThatAdded()));

    }

    @Test
    void increaseAllPrices_should_updateAllAnalysisPrices() {
        analysis2.setId(1);
        analysisService.addAnalysis(analysis2.getName(), analysis2.getPrice(), analysis2.getLowLimit(),
                analysis2.getHighLimit(), analysis2.getSample(), analysis2.getUserThatAdded());
        analysisService.increaseAllPrices(10);
        Analysis sameAnalysis = (Analysis) analysisService.getOne(analysis.getId());
        Analysis sameAnalysis2 = (Analysis) analysisService.getOne(analysis2.getId());
        assertEquals(30, sameAnalysis.getPrice());
        assertEquals(40, sameAnalysis2.getPrice());
    }

    @Test
    void decreaseAllPrices_should_updateAllAnalysisPrices() {
        analysis2.setId(1);
        analysisService.addAnalysis(analysis2.getName(), analysis2.getPrice(), analysis2.getLowLimit(),
                analysis2.getHighLimit(), analysis2.getSample(), analysis2.getUserThatAdded());
        analysisService.decreaseAllPrices(10);
        Analysis sameAnalysis = (Analysis) analysisService.getOne(analysis.getId());
        Analysis sameAnalysis2 = (Analysis) analysisService.getOne(analysis2.getId());
        assertEquals(10, sameAnalysis.getPrice());
        assertEquals(20, sameAnalysis2.getPrice());
        analysisService.decreaseAllPrices(11);
        Analysis sameAnalysis3 = (Analysis) analysisService.getOne(analysis.getId());
        Analysis sameAnalysis4 = (Analysis) analysisService.getOne(analysis2.getId());
        assertEquals(0, sameAnalysis3.getPrice());
        assertEquals(9, sameAnalysis4.getPrice());

    }

    @Test
    void updateInexistingAnalysis_should_throwWrongIdException() {

        assertThrows(WrongIdException.class, () -> analysisService.updateAnalysis(99, "Change", 16,
                12, 90, sample, user));
    }

    @Test
    void updateAnalysesFromSampleRepository_Should_updateChangesToAnalyses() {
        sample2.setId(1);
        sampleService.addSample(sample.getName(), sample.isAssitantRequired(), user);
        sampleService.addSample(sample2.getName(), sample2.isAssitantRequired(), user);
        analysisService.addAnalysis("Glicemia", 13, 65, 110, sample, user);
        analysisService.addAnalysis("K", 13, 65, 110, sample2, user);
        sampleService.updateSample(0, "change1", false, user);
        sampleService.updateSample(1, "change2", false, user);
        Analysis oAnalyses = (Analysis) analysisService.getOne(1);
        Analysis oAnalyses2 = (Analysis) analysisService.getOne(2);
        Sample oSample = (Sample) sampleService.getOne(0);
        Sample oSample2 = (Sample) sampleService.getOne(1);
        assertNotEquals(oSample, oAnalyses.getSample());
        assertNotEquals(oSample2, oAnalyses2.getSample());
        analysisService.updateAnalysesSample(oAnalyses);
        analysisService.updateAnalysesSample(oAnalyses2);
        assertEquals(oSample, oAnalyses.getSample());
        assertEquals(oSample2, oAnalyses2.getSample());
    }

}