package org.integration

import org.json.JSONArray
import org.json.JSONObject

/**
 * La clase es una integración de los servicios rest del aplicativo mantis
 * @author Blas Steven Alarcon Rueda. BlasSteven - https://github.com/BlasSteven/TestManGenData
 * @since 1.0.0
 */

class MantisIntegration {
    Propiedades props = new Propiedades()
    private String nameProject, nameCategory, asigneTo, responseProject, idProyectMantis, idCategoryMantis, response,
            issueId, nameProjectMantis, nameCategoryMantis
    private int notFoundCategory ,notFoundProject, notFoundCase

    /**
     *  <b>MantisIntegration</b>
     * @param nameProject nombre del proyecto en mantis
     * @param nameCategory nombre de la categoría
     * @param asigneTo nombre de la persona que se va ah asignar
     * */
    MantisIntegration(String nameProject, String nameCategory, String asigneTo){
        this.nameProject = nameProject
        this.nameCategory = nameCategory
        this.asigneTo = asigneTo
    }

    /**
     * <b>mantisAllProjects</b>
     * <p>
     * Consume el servicio que trae todos los proyectos de mantis
     * */
    private mantisAllProjects() {
        HttpClientBuilder http = new HttpClientBuilder("get", "http", props.getUrlMantis(),
                "", "/api/rest/projects/", null)
        http.setHeader().setRequestProperty(props.getKey(), props.getKeyValue())
        responseProject = http.responseRequest().toString()
    }

    /**
     * <b>getIdProjectMantis</b>
     * <p>
     * Extrae el id del proyecto de la respuesta de mantisAllProjects
     * */
    private getIdProjectMantis() {
        mantisAllProjects()
        JSONObject responseJson
        responseJson = new JSONObject(responseProject)
        JSONArray objectProyect = (JSONArray) responseJson.get("projects")
        for (int i = 0; i < objectProyect.length(); i++) {
            JSONObject object = objectProyect.getJSONObject(i)
            if (object.get("name") == nameProject) {
                idProyectMantis = object.get("id").toString()
                nameProjectMantis = object.get("name")
                break
            }//Cierre del "if"
        }//Cierre del "for"
        if(nameProject != nameProjectMantis){
            notFoundProject = 1
            notFoundCase = notFoundProject
            println("¡No existe projecto Mantis!")
        }
    }

    /**
     * <b>getIdCategoryMantis</b>
     * <p>
     * Extrae el id categoría de la respuesta de mantisAllProjects
     * */
    private getIdCategoryMantis() {
        getIdProjectMantis()
        JSONObject responseJson
        JSONObject jsonProject
        responseJson = new JSONObject(responseProject)
        JSONArray objectProyect = (JSONArray) responseJson.get("projects")
        jsonProject = objectProyect.get(0)
        JSONArray objectCategory = (JSONArray) jsonProject.get("categories")
        for (int i = 0; i < objectCategory.length(); i++) {
            JSONObject object = objectCategory.getJSONObject(i)
            if (object.get("name") == nameCategory) {
                idCategoryMantis = object.get("id").toString()
                nameCategoryMantis = object.get("name")
                break
            }
        }
        if(nameCategoryMantis != nameCategory){
            notFoundCategory = 1
            notFoundCase = notFoundCategory
            println("¡No existe Categoria Mantis!")
        }
    }

    /**
     * <b>mantisCreateIssue</b>
     * @param summary cabecera del issue
     * @param description descripcion general del issue
     * @param additional informacion adicional del issue
     * */
    void mantisCreateIssue(String summary, String description, String additional) {
        getIdCategoryMantis()
        if(notFoundCase != 1) {
            def body = """
            {
                "summary": "$summary",
                "description": "$description",
                "additional_information": "$additional",
                "project": {
                    "id": $idProyectMantis,
                    "name": "$nameProject"
                },
                "category": {
                    "id": $idCategoryMantis,
                    "name": "$nameCategory"
                },
                "handler": {
                    "name": "$asigneTo"
                },
                "view_state": {
                    "id": 10,
                    "name": "public"
                },
                "priority": {
                    "name": "normal"
                },
                "severity": {
                    "name": "major"
                },
                "reproducibility": {
                    "name": "always"
                },
                "sticky": false,
                "custom_fields": [],
                "tags": [
                    {
                        "name": "issueAutomatizacion"
                    }
                ]
            }       
                    """
            HttpClientBuilder http = new HttpClientBuilder("post", "http", props.getUrlMantis(),
                    null, "/api/rest/issues", body)
            http.setHeader().setRequestProperty(props.getKey(), props.getKeyValue())
            response = http.responseRequest()
        }else{
            println("No se ejecutara el api /api/rest/issues "+notFoundCase)
        }
    }

    /**
     * <b>getIssue</b>
     * @return trae el id del issue si se creo correctamente
     * */
    String getIssue() {
        JSONObject jsonResponse = null
        if (response == null) {
            println("No se creo el issue")
            issueId = 0
        }else{
            jsonResponse = new JSONObject(response)
        JSONObject objectIssue = (JSONObject) jsonResponse.get("issue")
        issueId = objectIssue.get("id").toString()
        }
            return issueId
    }
}