package org.integration

class Run {

    static void main(String[] args){
        Propiedades props = new Propiedades()
        props.loadPropsTestPlan("com_testPlans_TestLink")
        TestManIntegration test = new TestManIntegration(props.getProyecto(),props.getPlanPruebas(),
                props.getCasoPrueba(),props.getSuitePrueba(),props.getBuild(),props.getCategory(),
                props.getAssigneTo(),false,"",1,
                "com_testPlans_TestLink")
        test.caseValidTest()
    }
}
