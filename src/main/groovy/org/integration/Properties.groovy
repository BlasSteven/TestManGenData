package org.integration

import java.util.regex.Matcher
import java.util.regex.Pattern
/**
 * La clase extrae las propiedades del servidor, ruta, y test
 * @author Blas Steven Alarcon Rueda. BlasSteven - https://github.com/BlasSteven/TestManGenData
 * @since 1.0.0
 */
class Properties {

    java.util.Properties properties = new java.util.Properties()
    private String test, devKey,keyValue, key
    private String proyecto
    private String casoPrueba
    private String suitePrueba
    private String build
    private String category
    private String assigneTo
    private String planPruebas
    private String baseUrl1
    private String baseUrl2
    private String baseUrl3
    private String baseUrl4
    private String baseUrl5
    private String dir
    private static final String path = "testManGen.properties"

    /**
     * <b>loadPropsTestPlan</b>
     * @param nameTestCase nombre del caso de prueba (Utilizando la clase es mas efectivo).
     * */
    void loadPropsTestPlan(String nameTestCase){
            properties.load(new FileInputStream(path))
            test = properties.get(nameTestCase).toString()
            Pattern pattern = Pattern.compile("(.+),(.+),(.+),(.+),(.+),(.+),(.+)")
            Matcher matcher = pattern.matcher(test)
            matcher.find()
            proyecto = matcher.group(1)
            planPruebas = matcher.group(2)
            casoPrueba = matcher.group(3)
            suitePrueba = matcher.group(4)
            build = matcher.group(5)
            category = matcher.group(6)
            assigneTo = matcher.group(7)
    }

    /**
     * @return Variable del proyecto
     * */
    String getProyecto(){
        return proyecto
    }

    /**
     * @return Variable del plan de pruebas
     * */
    String getPlanPruebas(){
        return planPruebas
    }

    /**
     * @return Variable de los casos de prueba
     * */
    String getCasoPrueba(){
        return casoPrueba
    }

    /**
     * @return Variable del suite de pruebas
     * */
    String getSuitePrueba(){
        return suitePrueba
    }

    /**
     * @return Variable de la built
     * */
    String getBuild(){
        return build
    }

    /**
     * @return Variable de la categoría
     * */
    String getCategory(){
        return category
    }

    /**
     * @return Variable ha quien asigna
     * */
    String getAssigneTo(){
        return assigneTo
    }

    /**
     * @return Variable devKey (testlink)
     * */
    String getPropsDekey(){
            properties.load(new FileInputStream(path))
            devKey = properties.get("devKey").toString()
        return devKey
    }

    /**
     * @return Variable Url XMLRPC (testlink)
     * */
    String getUrlTestLinkXMLRPC(){
            properties.load(new FileInputStream(path))
            baseUrl1 = properties.get("BASE_URL1").toString()
        return baseUrl1
    }

    /**
     * @return Variable url Mantis (Mantis)
     * */
    String getUrlMantis(){
        properties.load(new FileInputStream(path))
        baseUrl2 = properties.get("BASE_URL2").toString()
        return baseUrl2
    }

    /**
     * @return Variable keyValue mantis (mantis)
     * */
    String getKeyValue(){
        properties.load(new FileInputStream(path))
        keyValue = properties.get("keyValue").toString()
        return keyValue
    }

    /**
     * @return Variable key mantis (mantis)
     * */
    String getKey(){
        properties.load(new FileInputStream(path))
        key = properties.get("key").toString()
        return key
    }

    /**
     * @return Variable dir (ubicación temporal de las carpetas que se crean)
     * */
    String getDir(){
        properties.load(new FileInputStream(path))
        dir = properties.get("dir").toString()
        return dir
    }
}
