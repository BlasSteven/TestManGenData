package org.integration.constants

/**
 * La clase TestManIntegrationErrors contiene los códigos de error que se aplicaran como excepciones en las clases
 * que lo requieran
 * @author Blas Steven Alarcon Rueda. BlasSteven - https://github.com/BlasSteven/TestManGenData
 * @since 1.2.0
 */

enum TestManIntegrationErrors {

    FILE_FAILED_EMPTY_NAME(10,"[X] El nombre esta vacío"),
    FILE_FAILED_DIR(11,"[X] No se creo el directorio"),
    FILE_FAILED_EMPTY(12,"[!] Las carpetas no tienen contenido"),
    MANTIS_PROJECT_NOT_FOUND(20,"[!] No existe proyecto en Mantis"),
    MANTIS_CATEGORY_NOT_FOUND(21,"[!] No existe categoría en Mantis"),
    HTTP_RESPONSE_NOT_FOUND(404,"[!] No se encuentra el recurso. Revisa el endpoint"),
    HTTP_RESPONSE_BAD_REQUEST(400,"[!] No se proceso la petición. Revisa el Body"),
    HTTP_RESPONSE_INTERNAL_SERVER_ERROR(500,"[!] Error interno del servidor"),
    HTTP_NOT_METHOD(600,"[!] El método no esta en la petición"),
    HTTP_RESPONSE_EMPTY_ID_CATEGORY(601,"[!] Hubo una respuesta vacía al extraer el id de la categoría (revisa la key api)"),
    HTTP_RESPONSE_EMPTY_ID_PROJECT(602,"[!] Hubo una respuesta vacía al extraer el id del proyecto (revisa la key api)")

    private final Integer code
    private final String message

    /**
     * <b>TestManIntegrationErrors</b>
     * <p>
     *     Coloca el código y el mensaje en los parametros del método.
     *     @param code código del error
     *     @param message EL mensaje del código de error
     * */
    TestManIntegrationErrors(int code, String message){
        this.code = code
        this.message = message
    }

    /**
     * <b>getCode</b>
     * @return code el código de error
     * */
    Integer getCode(){
        return code
    }

    /**
     * <b>getName</b>
     * @return Un String con el mensaje y el código.
     * */
    String getName(){
        return "$message, codigo: $code"
    }

}