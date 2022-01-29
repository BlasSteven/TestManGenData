package org.integration

class Run {

    static void main(String[] args){
        try {
            Propiedades props = new Propiedades()
            props.loadPropsTestPlan("com_testPlans_TestLink")
            TestManIntegration test = new TestManIntegration(props.getProyecto(), props.getPlanPruebas(),
                    props.getCasoPrueba(), props.getSuitePrueba(), props.getBuild(), props.getCategory(),
                    props.getAssigneTo(), false, "", 1,
                    "testlink")
            test.caseValidTest()
        }catch (Exception e){
            e.printStackTrace()
        }
    }
}
