package org.integration.util

/**
 * La clase TestManIntegrationException contiene el llamado para las excepciones
 * @author Blas Steven Alarcon Rueda. BlasSteven - https://github.com/BlasSteven/TestManGenData
 * @since 1.2.0
 */

class TestManIntegrationException extends Exception {

    private Integer code;

    /**
     * <b>TestManIntegrationException</b>
     *     @param msg
     * */
    TestManIntegrationException(String msg){
        super(msg)
    }

    /**
     * <b>TestManIntegrationException</b>
     *     @param code
     *     @param msg
     * */
    TestManIntegrationException(Integer code, String msg){
        super(msg)
        this.code = code
    }
}
