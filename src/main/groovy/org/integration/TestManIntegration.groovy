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
    private String projectTestMan, testLinkPlan, testLinkTestCase, testLinkSuite, testLinkBuild, categoryMantis,
                   assigneTo, exception, notes, summary, bugId, UrlExecResultId, nameFile
    private boolean statusTest = true

    /**
     * <b>TestManIntegration</b>
     *
     * @param projectTestMan
     * @param testLinkPlan
     * @param testLinkTestCase
     * @param testLinkSuite
     * @param testLinkBuild
     * @param categoryMantis
     * @param assigneTo
     * @param statusTest
     * @param exception
     * @param executionDuration
     * @param nameFile
     * */

    TestManIntegration(projectTestMan, testLinkPlan, testLinkTestCase,
                       testLinkSuite, testLinkBuild, categoryMantis, assigneTo,
                       statusTest, exception, executionDuration, nameFile) {
        this.projectTestMan = projectTestMan
        this.testLinkPlan = testLinkPlan
        this.testLinkTestCase = testLinkTestCase
        this.testLinkSuite = testLinkSuite
        this.testLinkBuild = testLinkBuild
        this.categoryMantis = categoryMantis
        this.assigneTo = assigneTo
        this.statusTest = statusTest
        this.exception = exception
        this.executionDuration = executionDuration
        this.nameFile = nameFile
    }

    /**
     * <b>MantisIntegration</b>
     * <p>
     * <b>TestLinkIntegration</b>
     */
    MantisIntegration man = new MantisIntegration(projectTestMan, categoryMantis, assigneTo)
    TestLinkIntegration testLink = new TestLinkIntegration(projectTestMan, testLinkPlan, testLinkBuild, testLinkSuite,
            testLinkTestCase, notes, executionDuration, nameFile)

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
            testLink.sendEvidence()
            if (statusTest) {
                bugId = null
            } else {
                UrlExecResultId = testLink.getUrlExecResultId()
                    switch (typeCase) {
                        case 1: man.mantisCreateIssue(summary, UrlExecResultId, notes)
                            break
                        case 2: man.mantisCreateIssue(summary, notes, assigneTo)
                            break
                    }
                    bugId = man.getIssue()
                    println("issue generate: " + bugId)
                    testLink.reportTCResults(result, bugId, true)
            }//cierre del segundo else
        }//cierre del finally catch
    }//cierre del método
}