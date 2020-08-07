package UserInterface;

import Domain.Entities.Analysis;
import Domain.Entities.Patient;
import Domain.Entities.User;
import Domain.ViewModels.AnalysisViewModel;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExportingJasperReports {
    /**
     * Exports patients information as a Jasper Report as PDF.
     *
     * @param patient   the current patient.
     * @param printedBy the user who is exporting.
     * @throws IOException in case the export cannot be done.
     * @throws JRException in case the export cannot be done.
     */
    public static void exportReportPDF(Patient patient, User printedBy) throws IOException, JRException {
        String path = getChosenPath();
        String fullName = patient.getLastName() + "_" + patient.getFirstName();
        String firstName = patient.getFirstName();
        String lastName = patient.getLastName();
        Integer id = patient.getId();
        String cnp = patient.getCnp();
        String diagnostic = patient.getDiagnose();
        String dateOfRegistration = patient.getCurrentDate().toString();
        String diagnosticBy = getDiagnosticIfItWasGiven(patient);
        //Image image1 = Toolkit.getDefaultToolkit().createImage(ExportingJasperReports.class.getResource("/logo.png"));
        //Image image2 = Toolkit.getDefaultToolkit().createImage(ExportingJasperReports.class.getResource("/renar.png"));
        Image imageLogo = new ImageIcon(ExportingJasperReports.class.getResource("/logo.png")).getImage();
        Image imageRenar = new ImageIcon(ExportingJasperReports.class.getResource("/renar.jpg")).getImage();


        if (diagnosticBy == null) return;
        String printedByString = printedBy.getJobTitle() + " " + printedBy.getName();
        //String outPutFile = "C:\\Users\\User\\Desktop\\My programs\\" + fullName + ".pdf";
        String outPutFile =path+"\\"+fullName+".pdf";
        List<AnalysisViewModel> analysisList = getAnalysisViewModels(patient);
        if (analysesAreNotDone(patient, analysisList)) return;
        JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(analysisList);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("AnalysesBeanParam", jrBeanCollectionDataSource);
        parameters.put("firstNameParam", firstName);
        parameters.put("lastNameParam", lastName);
        parameters.put("idParam", id);
        parameters.put("cnpParam", cnp);
        parameters.put("dateOfRegistrationParam", dateOfRegistration);
        parameters.put("diagnoseParam", diagnostic);
        parameters.put("diagnoticByParam", diagnosticBy);
        parameters.put("printedByparam", printedByString);
        parameters.put("logoImage", imageLogo);
        parameters.put("renarImage", imageRenar);
        JasperDesign jasperDesign = JRXmlLoader.load(ExportingJasperReports.class.getResourceAsStream("/ResultsReport_B.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
        OutputStream outputStream = new FileOutputStream(new File(outPutFile));
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        outputStream.close();
        File pdfFile = new File(outPutFile);
        Desktop.getDesktop().open(pdfFile);

    }

    /**
     *
     * @return the chosen path to export as String.
     */

    public static String getChosenPath() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        Stage stage = new Stage();
        File prefFile = new File("C:\\Users\\User\\Desktop\\Rezultate");
        if (prefFile.exists()) directoryChooser.setInitialDirectory(prefFile);
        File file = directoryChooser.showDialog(stage);
        return file.getPath();
    }

    /**
     * Exports patients information as a Jasper Report to html and opens it in default broswer.
     *
     * @param patient   the current patient.
     * @param printedBy the user who is exporting.
     * @throws JRException in case the export cannot be done.
     * @throws IOException in case the export cannot be done.
     */
    public static void exportReportWeb(Patient patient, User printedBy) throws JRException, IOException {
        String path = getChosenPath();
        String fullName = patient.getLastName() + "_" + patient.getFirstName();
        String firstName = patient.getFirstName();
        String lastName = patient.getLastName();
        Integer id = patient.getId();
        String cnp = patient.getCnp();
        String diagnostic = patient.getDiagnose();
        String dateOfRegistration = patient.getCurrentDate().toString();
        String diagnosticBy = getDiagnosticIfItWasGiven(patient);
        Image imageLogo = new ImageIcon(ExportingJasperReports.class.getResource("/logo.png")).getImage();
        Image imageRenar = new ImageIcon(ExportingJasperReports.class.getResource("/renar.jpg")).getImage();
        if (diagnosticBy == null) return;
        String printedByString = printedBy.getJobTitle() + " " + printedBy.getName();
        String outPutFile2 = path +"\\"+fullName + ".html";
        List<AnalysisViewModel> analysisList = getAnalysisViewModels(patient);
        if (analysesAreNotDone(patient, analysisList)) return;
        JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(analysisList);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("AnalysesBeanParam", jrBeanCollectionDataSource);
        parameters.put("firstNameParam", firstName);
        parameters.put("lastNameParam", lastName);
        parameters.put("idParam", id);
        parameters.put("cnpParam", cnp);
        parameters.put("dateOfRegistrationParam", dateOfRegistration);
        parameters.put("diagnoseParam", diagnostic);
        parameters.put("diagnoticByParam", diagnosticBy);
        parameters.put("printedByparam", printedByString);
        parameters.put("logoImage", imageLogo);
        parameters.put("renarImage", imageRenar);
        JasperDesign jasperDesign = JRXmlLoader.load(ExportingJasperReports.class.getResourceAsStream("/ResultsReport_B.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
        JasperExportManager.exportReportToHtmlFile(jasperPrint, outPutFile2);
        File webFile = new File(outPutFile2);
        Desktop.getDesktop().open(webFile);

//        Stage stage = new Stage();
//        WebView webView  = new WebView();
//        webView.getEngine().load("file:///C:/Users/User/Desktop/My%20programs/"+fullName+".html");
//        stage.setScene(new Scene(webView));
//        stage.show();
    }

    /**
     * @param patient the current patient
     * @return the given Diagnostic if it was given, as String, or "in course" if it was not given.
     */
    private static String getDiagnosticIfItWasGiven(Patient patient) {
        String diagnosticBy;
        if (patient.getUserThatDiagnosed() == null) {
            if (!Alerts.showQuestion("The patient does't have a diagnotstic!", "Proceed?").get()) {
                return null;
            } else {
                diagnosticBy = "in course";
            }
        } else {
            diagnosticBy = patient.getUserThatDiagnosed().getJobTitle() + " " + patient.getUserThatDiagnosed().getName();
        }
        return diagnosticBy;
    }

    /**
     * @param patient the current patient.
     * @return a List of AnalysisViewModel corresponding with the analyses of the current patient.
     */
    private static List<AnalysisViewModel> getAnalysisViewModels(Patient patient) {
        List<AnalysisViewModel> analysisList = new ArrayList<>();
        for (Analysis analysis : patient.getAnalyses()) {
            if (analysis.getWorkedBy() != null) {
                AnalysisViewModel analysisViewModel = new AnalysisViewModel(analysis.getName(), analysis.getScore(), analysis.getLowLimit()
                        , analysis.getHighLimit(), analysis.isInLimits(),
                        analysis.getWorkedBy().getJobTitle() + " " + analysis.getWorkedBy().getName());
                analysisList.add(analysisViewModel);
            }
        }
        return analysisList;
    }

    /**
     * @param patient      The current patient.
     * @param analysisList a List of AnalysisViewModel corresponding with the analyses of the current patient.
     * @return whether they are analyses that are not done yet.
     */
    private static boolean analysesAreNotDone(Patient patient, List<AnalysisViewModel> analysisList) {
        if (analysisList.size() < patient.getAnalyses().size()) {
            int difference = patient.getAnalyses().size() - analysisList.size();
            if (!Alerts.showQuestion("There are " + difference + " analyses that don't have results", "Proceed?").get()) {
                return true;
            }
        }
        return false;
    }
}
