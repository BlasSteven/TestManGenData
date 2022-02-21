package org.integration

import br.eti.kinoshita.testlinkjavaapi.TestLinkAPI
import br.eti.kinoshita.testlinkjavaapi.constants.ExecutionStatus
import br.eti.kinoshita.testlinkjavaapi.constants.TestCaseDetails
import br.eti.kinoshita.testlinkjavaapi.model.Attachment
import br.eti.kinoshita.testlinkjavaapi.model.Build
import br.eti.kinoshita.testlinkjavaapi.model.Execution
import br.eti.kinoshita.testlinkjavaapi.model.TestCase
import br.eti.kinoshita.testlinkjavaapi.model.TestPlan
import br.eti.kinoshita.testlinkjavaapi.model.TestProject
import br.eti.kinoshita.testlinkjavaapi.model.TestSuite
import br.eti.kinoshita.testlinkjavaapi.util.TestLinkAPIException
import org.apache.commons.codec.binary.Base64
import org.apache.commons.io.FileUtils
import org.integration.util.Utility

/**
 * La clase es una integración a testlink para ejecutar y subir evidencia a los casos de prueba
 * @author Blas Steven Alarcon Rueda. BlasSteven https://github.com/BlasSteven/TestManGenData
 * @since 1.0.0
 */

class TestLinkIntegration {
    Utility util = new Utility()
    private String projectName, planName, buildName, suiteName, caseName, noteTestCase, bugId,
            fullExternalId,fileClass
    private ExecutionStatus resultTestCase
    private int projectId, planId, buildId, suiteId, caseId, caseExternalId, executionDuration,execResultId
    TestLinkAPI api

    /**
     * <b>TestLinkIntegration</b>
     *
     * @param projectName nombre del proyecto de testlink
     * @param planName nombre del test plan de testlink
     * @param buildName nombre del build de testlink
     * @param suiteName nombre de la suite de testlink
     * @param caseName nombre del caso de testlink
     * @param noteTestCase nota del caso
     * @param nameFile recibe el nombre de la clase para crear el archivo de evidencia para subir la evidencia al caso
     * @param executionDuration tiempo de ejecución de las prueba
     * */

    TestLinkIntegration(String projectName, String planName, String buildName, String suiteName, String caseName,
                        String noteTestCase, int executionDuration, String nameFile) {
        this.projectName = projectName
        this.planName = planName
        this.buildName = buildName
        this.suiteName = suiteName
        this.caseName = caseName
        this.noteTestCase = noteTestCase
        this.executionDuration = executionDuration
        this.fileClass = nameFile
    }

    /**
     * <b>api</b>
     * <p>
     * Autenticación a testlink
     * */
    private TestLinkAPI api() {
        Properties props = new Properties()
        api = null
        URL testlinkURL = null
        try {
            testlinkURL = new URL(props.getUrlTestLinkXMLRPC())
        } catch (MalformedURLException mue) {
            mue.printStackTrace(System.err)
            System.exit(-1)
        }
            api = new TestLinkAPI(testlinkURL,props.getPropsDekey())
    }

     /**
      * <b>testProjects</b>
      * <p>
      * Extrae el id del proyecto en testlink
      */
    private void testProjects(){
        api()
        TestProject[] testProjects = api.getProjects()
        for (TestProject pro : testProjects) {
            if (pro.getName() == projectName) {
                projectId = pro.getId()
                break
            }
        }
    }

    /**
     * <b>testPlans</b>
     * <p>
     * Extrae el id plan de pruebas en testlink
     */
    private void testPlans() {
        testProjects()
        TestPlan[] testPlans = api.getProjectTestPlans(projectId)
        for (TestPlan plans : testPlans) {
            if (plans.getName() == planName) {
                planId = plans.getId()
                break
            }
        }
    }

    /**
     * <b>testBuilds</b>
     * <p>
     * Extrae el id Build en testlink
     */
    private void testBuilds() {
        testPlans()
        Build[] testBuilds = api.getBuildsForTestPlan(planId)
        for (Build builds : testBuilds) {
            if (builds.getName() == buildName) {
                buildId = builds.getId()
                break
            }
        }
    }

    /**
     * <b>testSuites</b>
     * <p>
     * Extrae el id suite en testlink
     */
    private void testSuites() {
        testBuilds()
        TestSuite[] testSuites = api.getTestSuitesForTestPlan(planId)
        for (TestSuite suites : testSuites) {
            if (suites.getName() == suiteName) {
                suiteId = suites.getId()
                break
            }
        }
    }

    /**
     * <b>testCases</b>
     * <p>
     * Extrae el id del caso en testlink
     **/
    private void testCases() {
        testSuites()
        TestCase[] testCases = api.getTestCasesForTestSuite(suiteId, true, TestCaseDetails.SIMPLE)
        for (TestCase cases : testCases) {
            if (cases.getName() == caseName) {
                caseId = cases.getId()
                caseExternalId = cases.getParentId()
                fullExternalId = cases.getFullExternalId()
                break
            }
        }
    }

    /**
     * <b>execResult</b>
     * <p>
     * Extrae el id de la ejecución en testlink
     */
    private execResult(){
        testCases()
        Execution execResult = api.getLastExecutionResult(planId,caseId,fullExternalId,null,
                "",buildId,buildName,0)
        execResultId = execResult.getId()
    }

    /**
     * <b>getUrlExecResultId</b>
     * @return la url de la ejecución del caso, id de la ultima ejecución
     */
    String getUrlExecResultId(){
        String url ="http://localhost/testlink/lib/execute/execPrint.php?id=$execResultId"
        return url
    }

    /**
     * <b>reportTCResults</b>
     * <p>
     * Coloca los resultados de la prueba en testlink
     **/
    void reportTCResults(ExecutionStatus resultTestCase,String bugId,boolean overwrite) {
        try {
            this.resultTestCase = resultTestCase
            this.bugId = bugId
            testCases()
            api.reportTCResult(
                    caseId,
                    caseExternalId,
                    planId,
                    resultTestCase,
                    null,
                    buildId,
                    buildName,
                    noteTestCase,
                    executionDuration,
                    false,
                    bugId,
                    null,
                    "",
                    null,
                    overwrite,
                    "",
                    "")
            execResult()
        }catch(TestLinkAPIException e){
            e.printStackTrace()
            System.exit(-1)
        }
    }

    /**
     * <b>sendEvidence</b>
     * <p>
     *     Envía la evidencia al método uploadAttachmen en orden de creación
     **/
     void sendEvidence(){
        util.manageFolderUploadAttachment(fileClass)
        //Subir los archivos de mayor a menor
         if(util.getFileEmpty() != 1) {
             util.getMapEvidence().forEach((k, v) -> { uploadAttachment(v, v.replace(".png", ""), v) })
             println(" ¡Complete!")
             FileUtils.deleteDirectory(util.getDirEvidenceCopy())//Elimina el directorio cuando sube los archvivos
             FileUtils.deleteDirectory(util.getDirEvidence())//Elimina el directorio cuando sube los archvivos
         }
    }

    /**
     * <b>uploadAttachment</b>
     * <p>
     * Sube los archivos en el aplicativo testlink dependiendo al caso y ejecución de la prueba
     * deprecated Debido a que la librería "br.eti.kinoshita" es antigua genera la siguiente excepción
     * "(uploadExecutionAttachment) - Error inserting attachment on DB", la cual se solvento creando un try
     * en el método Attachment
     * */
    private void uploadAttachment(String archive, String tileAction,String step){
        File attachmentFile = new File(util.getDirEvidenceCopy(),archive)
        String fileContent = null
            byte[] byteArray = FileUtils.readFileToByteArray(attachmentFile)
            fileContent = new String(Base64.encodeBase64(byteArray))
       try {
            Attachment attachment = api.uploadExecutionAttachment(
                    execResultId, //executionId
                    tileAction, //title
                    "", //description
                    step, //fileName
                    "image/jpg", //fileType
                    fileContent)
            Thread.sleep(2000)
       }catch(Exception ignored){} //Se ignora excepción hasta que se encuentre posible solución
        //mapa de carga para los archivos
        def map = ["[ ― ]","[ \\ ]","[ | ]","[ / ]","[ ― ]","[ \\ ]","[ | ]","[ / ]","[ ― ]"]
        for (int i = 0; i<map.size(); i++){
            System.out.print("\r"+"Upload Attachment: "+map.get(i))
            Thread.sleep(500)
        }
    }
}