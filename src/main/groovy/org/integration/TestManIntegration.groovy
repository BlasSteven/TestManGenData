package org.integration

import br.eti.kinoshita.testlinkjavaapi.constants.ExecutionStatus

/**
 * Integración de los aplicativos testlink y mantis
 * @author Blas Steven Alarcon Rueda. BlasSteven - https://github.com/BlasSteven/TestManGenData
 * @since 1.0.0
 */
class TestManIntegration {

    private int typeCase, executionDuration
    private ExecutionStatus result
    private String projectTestMan, testLinkPlan, testLinkTestCase, testLinkSuite, testLinkbuild, categoryMantis,
                   assigneTo, exception, notes, summary, bugId, UrlExecResultId, fileClass
    private boolean statusTest = true

    /**
     * <b>TestManIntegration</b>
     *
     * @param projectTestMan
     * @param testLinkPlan
     * @param testLinkTestCase
     * @param testLinkSuite
     * @param testLinkbuild
     * @param categoryMantis
     * @param assigneTo
     * @param statusTest
     * @param exception
     * @param executionDuration
     * @param fileClass
     * */

    TestManIntegration(projectTestMan, testLinkPlan, testLinkTestCase,
                       testLinkSuite, testLinkbuild, categoryMantis, assigneTo,
                       statusTest, exception, executionDuration, fileClass) {
        this.projectTestMan = projectTestMan
        this.testLinkPlan = testLinkPlan
        this.testLinkTestCase = testLinkTestCase
        this.testLinkSuite = testLinkSuite
        this.testLinkbuild = testLinkbuild
        this.categoryMantis = categoryMantis
        this.assigneTo = assigneTo
        this.statusTest = statusTest
        this.exception = exception
        this.executionDuration = executionDuration
        this.fileClass = fileClass
    }

    /**
     * <b>MantisIntegration</b>
     * <p>
     * <b>TestLinkIntegration</b>
     */
    MantisIntegration man = new MantisIntegration(projectTestMan, categoryMantis, assigneTo)
    TestLinkIntegration testLink = new TestLinkIntegration(projectTestMan, testLinkPlan, testLinkbuild, testLinkSuite,
            testLinkTestCase, notes, executionDuration, fileClass)

    /**
     * <b>caseValidTest</b>
     * <p>
     * Crear el assert test dependiendo al status test y los excepción de selenium y TestLinkApi
     * creando el issue en mantis y enviando el correo si no existe conexión
     * */
    void caseValidTest() {
        summary = "/" + projectTestMan + "/" + testLinkSuite + "/" + testLinkTestCase
        try {
            if (statusTest) {
                typeCase = 0
                result = ExecutionStatus.PASSED
                notes = "Executed successfully"
            } else {
                typeCase = 1
                result = ExecutionStatus.FAILED
                notes = "Executed not successfully"
            }
        } catch (Exception e) {
            typeCase = 2
            result = ExecutionStatus.FAILED
            notes = "Executed not successfully" + e
        } finally {
            testLink.reportTCResults(result, bugId, false)
            testLink.manageFolderUploadAttachment()
            if (statusTest) {
                bugId = null
            } else {
                UrlExecResultId = testLink.getUrlExecResultId()
                if (testLink.catcha() != 1 ){
                    switch (typeCase) {
                        case 1: man.mantisCreateIssue(summary, UrlExecResultId, notes)
                            break
                        case 2: man.mantisCreateIssue(summary, notes, assigneTo)
                            break
                    }
                    bugId = man.getIssue()
                    println("issue generate: " + bugId)
                    testLink.reportTCResults(result, bugId, true)
                }else{
                    println("No se pudo ejecutar el caso verifique que los datos esten en testlink")
                }
            }//cierre del segundo else
        }//cierre del finally catch
    }//cierre del método
}