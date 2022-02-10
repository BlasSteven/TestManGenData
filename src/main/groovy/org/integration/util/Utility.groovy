package org.integration.util

import javax.imageio.ImageIO
import java.awt.Graphics2D
import java.awt.Image
import java.awt.image.BufferedImage

/**
 * Utilidades las cuales se usan en las clases principales leer la descripción para saber su uso
 * @author Blas Steven Alarcon Rueda. BlasSteven - https://github.com/BlasSteven/TestManGenData
 * @since 1.0.0
 */

class Utility {

    /**
     * <b>trimCharacter</b>
     * <p>
     *  remplaza el caracter que se desee en ambos extremos de la frase
     * @param string la cadena a remplazar
     * @param chars cualquier caracter que se quiere remplazar al inicio y al final
     *
     * @return el string el cual se le hizo el trim
     * */
    static String trimCharacter(String string, String chars){
        String str = string.trim()
        StringBuilder newStr
        int mySize = str.size()
        char myCharFinal = str.charAt(mySize-1)
        char myCharInicio = str.charAt(0)
        if(myCharInicio.toString() == chars){
            newStr = new StringBuilder(str)
            newStr.setCharAt(0, ' 'as char)
            str = newStr
        }else{
            boolean trimChar = false
        }
        if(myCharFinal.toString() == chars){
            newStr = new StringBuilder(str)
            newStr.setCharAt(mySize-1,' ' as char)
            str = newStr
        }else {
            boolean trimChar = false
        }
        return str.trim()
    }

    /**
     * <b> convertFile </b>
     * <p>
     * remplaza caracteres especiales de una cadena.
     * @return nombre de la clase sin puntos ni espacios
     * */
    static String convertFile(String nameClass){
        String nameFile
        if(nameClass == null || nameClass == "null"){
            nameClass == ""
        }else {
            nameFile = nameClass.replace(".", "_")
                    .replace("class ", "")
                    .replace(" ", "_")
        }
        return nameFile
    }

    /**
     * <b>resizeFile</b>
     * <p>
     * cambia las resolución de una imagen
     * */
    static void resizeFile(String imagePathToRead, String imagePathToWrite, int resizeWidth, int resizeHeight){
        File fileToRead = new File(imagePathToRead)
        BufferedImage bufferedImageInput = ImageIO.read(fileToRead)
        BufferedImage bufferedImageOutput = new BufferedImage(resizeWidth,
                resizeHeight, bufferedImageInput.getType())
        Graphics2D g2d = bufferedImageOutput.createGraphics()
        g2d.drawImage(bufferedImageInput, 0, 0, resizeWidth, resizeHeight, null)
        g2d.dispose()
        String formatName = imagePathToWrite.substring(imagePathToWrite
                .lastIndexOf(".") + 1)
        ImageIO.write(bufferedImageOutput, formatName, new File(imagePathToWrite))
    }

    /**
     * <b>decreaseSize</b>
     * <p>
     * hacer un resize de la imagen con el algoritmo TYPE_INT_RGB
     * el cual ignora el canal alfa para crear una imagen mas ligera sin perder resolución
     * */
    static void decreaseSize(String input, String output){
        Image im = ImageIO.read(new File(input))
        BufferedImage thumbnailImage = new BufferedImage(1295,892, BufferedImage.TYPE_INT_RGB)
        thumbnailImage.getGraphics().drawImage(im, 0, 0, 1295, 892, null)
        ImageIO.write(thumbnailImage, "JPG", new File(output))
    }


}
