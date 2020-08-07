package Service;

import Domain.Entities.Sample;
import Domain.Entities.User;
import Domain.Exceptions.InvalidSampleException;
import Domain.Exceptions.WrongIdException;
import Domain.Validators.IValidator;
import Domain.Validators.SampleValidator;
import Repository.IRepository;
import Repository.JSONFileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SampleServiceTest {
    IRepository<Sample> sampleIRepository = new JSONFileRepository<>("src/test/java/Service/samplesTest.txt", Sample.class);
    IValidator<Sample> sampleValidator = new SampleValidator();
    SampleService sampleService = new SampleService(sampleIRepository, sampleValidator);
    User user = new User("Admin", "AdminTest", "tester", true, true, true);
    Sample sample = new Sample("test", true, user);

    @BeforeEach
    void setUp() {
        sampleIRepository.clearAll();
        sampleService.addSample(sample.getName(), sample.isAssitantRequired(), sample.getUserThatAdded());
    }

    @Test
    void addValidSample_should_saveTheSampleToRepository() {
        Sample otherSample = new Sample("test2", true, user);
        otherSample.setId(1);
        sampleService.addSample(otherSample.getName(), otherSample.isAssitantRequired(), otherSample.getUserThatAdded());
        assertEquals(2, sampleService.getAll().size());
        Sample sameSample = (Sample) sampleService.getOne(otherSample.getId());
        assertEquals(otherSample, sameSample);

    }

    @Test
    void updateSample_should_updateTheSampleIfValid() {
        sampleService.updateSample(sample.getId(), "change", false, user);
        Sample sameSample = (Sample) sampleService.getOne(sample.getId());
        assertEquals("change", sameSample.getName());
        assertFalse(sameSample.isAssitantRequired());
    }

    @Test
    void addInvalidSample_should_throwInvalidSampleException() {
        assertThrows(InvalidSampleException.class, () ->
                sampleService.addSample("", true, user));

    }

    @Test
    void updateSample_should_throwInvalidSampleExceptionIfInvalid() {
        Sample sameSample = (Sample) sampleService.getOne(sample.getId());
        assertThrows(InvalidSampleException.class, () ->
                sampleService.updateSample(sameSample.getId(), "", true, user));
    }

    @Test
    void updateInexistingSample_should_throwWrongIdException() {

        assertThrows(WrongIdException.class, () ->
                sampleService.updateSample(99, "", true, user));
    }

    @Test
    void addingNewSampleWithOccupiedName_should_throwInvalidSampleException() {
        assertThrows(InvalidSampleException.class, () -> sampleService.addSample("test",
                true, user));
    }
}