package org.integration

import org.integration.util.Utility

/**
 * La clase es un constructor de clientes HTTP
 * @author Blas Steven Alarcon Rueda. BlasSteven - https://github.com/BlasSteven/TestManGenData
 * @since 1.0.0
 */
class HttpClientBuilder {
    private String typeMethod, protocol, server, path, body, port, url, response
    private def method

    /**
     * <b>HttpClientBuilder</b>
     *
     * @param typeMethod Tipo del método
     * @param protocol Protocolo que se utilizara en la petición
     * @param server Dirección del servidor
     * @param port Puerto que esta en escuche
     * @param path Ruta a consumir
     * @param body Cuerpo de la petición
     * */
    HttpClientBuilder(String typeMethod, String protocol, String server, String port, String path, String body) {
        this.typeMethod = typeMethod
        this.protocol = protocol
        this.server = Utility.trimCharacter(server, "/").replace("http://","")
        this.path = Utility.trimCharacter(path, "/")
        this.body = body
        this.port = port
    }

    /**
     * <b>urlConstructo</b>
     * <p>
     * crea la url dependiendo si viene con el puerto.
     * */
    private urlConstructor() {
        if (port == "" | port == null) {
            url = new URL(protocol + '://' + server + "/" + path + "/")
        }else{
            url = new URL(protocol + '://' + server +':'+ port + "/" + path + "/")
        }
    }

    /**
     * <b>methodConstructor</b>
     * <p>
     * Crea los parámetros de la petición dependiendo a su método
     * */
    private methodConstructor() {
        urlConstructor()
        if (typeMethod == "" | typeMethod == null) {
            println("¡No hay metodo!")
        }else{
            switch (typeMethod.toLowerCase().trim()) {
                case "get":
                    method = new URL(url)
                                .openConnection()
                    method.setRequestMethod("GET")
                    break
                case "post":
                    method = new URL(url)
                            .openConnection()
                    method.setRequestMethod("POST")
                    break
                case "patch":
                    method = new URL(url)
                            .openConnection()
                    break
            }//Cierre del switch
        }//Cierre de el if
    }//Cierre del método

    /**
     * <b>setheader</b>
     * <p>
     * Colocar los header en los request
     * @return EL método HTTP
     * @deprecated No exporta correctamente cuando son varios headers
     **/
    def setHeader(){
        methodConstructor()
        return method
    }

    /**
     * <b>sendRequest</b>
     * <p>
     * Envía la petición dependiendo al método que contenga
     * */
    private sendRequest(){
        method.setRequestProperty("Content-Type", "application/json")
        if(typeMethod.toLowerCase().trim() == "post") {
            method.setDoOutput(true)
            method.getOutputStream()
                    .write(body.getBytes("UTF-8"))
        }
    }

    /**
     * <b>exceptionStatus</b>
     * @return status code de la transacción
     * */
    private exceptionStatus(){
        sendRequest()
        def methodRC = method.getResponseCode()
        println("response code: "+methodRC+" "+url)
        if(methodRC >= 200 & methodRC <= 203) {
            response = method.getInputStream().getText()
        }else if(methodRC >= 400 & methodRC <= 499){
            println("Revisa el endpoint si es 404 $url, o el body  si  es 400: $body")
        }else{
            println("El servicio /o api esta respondiendo con un codigo de estado 500")
        }
    }

    /**
     * <b>responseRequest</b>
     * @return respuesta de la transacción
     * */
    def responseRequest(){
        exceptionStatus()
        return response
    }
}
