import Domain.Entities.Analysis;
import Domain.Entities.Patient;
import Domain.Entities.Sample;
import Domain.Entities.User;
import Domain.Validators.*;
import Repository.IRepository;
import Repository.JSONFileRepository;
import Service.AnalysisService;
import Service.PatientService;
import Service.SampleService;
import Service.UserService;
import UserInterface.ChoosePath;
import UserInterface.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Menu.fxml"));
        Parent root = fxmlLoader.load();
        Controller controller = fxmlLoader.getController();

        File directory = new File("C:\\files");
        if (!directory.exists()) directory.mkdir();
        File userFile = new File("C:\\files\\users.txt");
        userFile.createNewFile();
        IRepository<User> userIRepository = new JSONFileRepository<>(userFile.getPath(), User.class);
        File sampleFile = new File("C:\\files\\samples.txt");
        sampleFile.createNewFile();
        IRepository<Sample> sampleIRepository = new JSONFileRepository<>(sampleFile.getPath(), Sample.class);
        File analysesFile = new File("C:\\files\\analyses.txt");
        analysesFile.createNewFile();
        IRepository<Analysis> analysesIRepository = new JSONFileRepository<>(analysesFile.getPath(), Analysis.class);
        File patientsFile = new File("C:\\files\\patients.txt");
        patientsFile.createNewFile();
        IRepository<Patient> pacientIRepository = new JSONFileRepository<>(patientsFile.getPath(), Patient.class);
        IValidator<User> userValidator = new UserValidator();
        IValidator<Sample> sampleValidator = new SampleValidator();
        IValidator<Analysis> analysisValidator = new AnalysisValidator();
        IValidator<Patient> patientValidator = new PatientValidator();
        CnpValidator cnpValidator = new CnpValidator();
        UserService userService = new UserService(userIRepository, userValidator);
        if (userService.getAll().isEmpty()) {
            userService.addUser("Alin", "Hrenean", "Admin", true, true, true);
        }
        SampleService sampleService = new SampleService(sampleIRepository, sampleValidator);
        AnalysisService analysisService = new AnalysisService(analysesIRepository, sampleIRepository, analysisValidator);
        PatientService patientService = new PatientService(pacientIRepository, analysesIRepository, patientValidator,
                analysisValidator, cnpValidator);
        controller.setSetServices(userService, sampleService, analysisService, patientService);
        primaryStage.setTitle("Menu");
        primaryStage.setScene(new Scene(root, 485, 610));
        primaryStage.setMaximized(false);
        primaryStage.setResizable(false);
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
