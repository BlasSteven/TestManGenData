package org.integration

class Run {

    static void main(String[] args){
            Properties props = new Properties()
            props.loadPropsTestPlan("com_testPlans_TestLink")
            TestManIntegration test = new TestManIntegration(props.getProyecto(), props.getPlanPruebas(),
                    props.getCasoPrueba(), props.getSuitePrueba(), props.getBuild(), props.getCategory(),
                    props.getAssigneTo(), false, "", 1,
                    "org_testPlans_TestLink")
            test.caseValidTest()
    }
}
