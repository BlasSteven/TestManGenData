package org.integration.util

import org.apache.commons.io.FileUtils
import org.integration.Properties
import org.integration.constants.TestManIntegrationErrors

import javax.imageio.ImageIO
import java.awt.Graphics2D
import java.awt.Image
import java.awt.image.BufferedImage

/**
 * Utilidades las cuales se usan en las clases principales leer la descripción para saber su uso
 * @author Blas Steven Alarcon Rueda. BlasSteven - https://github.com/BlasSteven/TestManGenData
 * @since 1.2.0
 */

class Utility {

    String dir
    int fileEmpty = 0
    File filesEvidence, filesEvidenceCopy
    Map<Long, String> mapEvidence = new TreeMap<>(Collections.reverseOrder())
    Properties props = new Properties()

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
        ImageIO.write(thumbnailImage, "PNG", new File(output))
    }

    /**
     * <b>evidenceFolder</b>
     * <p>
     * Crea el folder con la variable fileClass que es el nombre del directorio
     * en el directorio definido en el archivo de propiedades
     * */
    private void evidenceFolder(String fileClass) throws TestManIntegrationException{
        String nameFolder =  convertFile(fileClass)
        if (props.getDir() == "null" || props.getDir() == ""){
            dir =  System.getProperty("user.dir")
        }else {
            dir =  props.getDir()
        }
        if (fileClass == "" || fileClass == "null" || fileClass == null) {

            throw new TestManIntegrationException(TestManIntegrationErrors.FILE_FAILED_EMPTY_NAME.getName())
        }else{
            filesEvidenceCopy = new File(dir,nameFolder+System.currentTimeMillis())
            filesEvidence = new File(dir,nameFolder)
            if (!filesEvidence.exists() || !filesEvidenceCopy.exists()) {
                if (filesEvidence.mkdirs() || filesEvidenceCopy.mkdirs()) {
                } else {
                    throw new TestManIntegrationException(TestManIntegrationErrors.FILE_FAILED_DIR.getName())
                }
            }//Cierre del tercer if
        }//Cierre del segundo else
    }//Cierre del método

    /**
     * <b>manageFolderUploadAttachment</b>
     *
     * Subir los archivos en orden de creación, identificando los con la palabra clave "Step_X" X que hace alusión
     * al numero del paso realizando, validaciones al fichero, peso de la imagen y resolución {@link Utility #decreaseSize(String input, String output) decreaseSize}
     *
     * @param nameFile Es el nombre del archivo
     * */
    void manageFolderUploadAttachment(String nameFile) {
        evidenceFolder(nameFile)
            def Map = [:]
            File carpeta
            String nameImg
            carpeta = new File(filesEvidence as String)
            if (carpeta.listFiles().length == 0 ) {//Validación que la carpeta tenga contenido
                fileEmpty = 1
                println(TestManIntegrationErrors.FILE_FAILED_EMPTY.getName())
                FileUtils.deleteDirectory(filesEvidence)//Elimina el directorio cuando sube los archvivos
                FileUtils.deleteDirectory(filesEvidenceCopy)//Elimina el directorio cuando sube los archvivos
            } else {
                for (int i = 0; i < carpeta.listFiles().length; i++) {
                    File fileList = carpeta.listFiles()[i]
                    long size = fileList.length()
                    nameImg = carpeta.list()[i]
                    Map.put(fileList.lastModified(), nameImg)//colocar el lastModified y el nombre en un mapa para ordenar
                    if (size != 0) {
                        //Redimensión y ajustes de propiedades para evitar que supere los bytes admitidos
                        decreaseSize(fileList.toString(), filesEvidenceCopy.toString() + "/" + nameImg)
                    }//Cierre del if
                }//Cierre del for
                //Ordenar de mayor a menor los archivos para subierlos
                java.util.Map<Long, String> map = new TreeMap<>(Collections.reverseOrder())
                map.putAll(Map)
                mapEvidence = map
                }
        }//Cierre del metodo

    /**
     * <b>getMapEvidence</b>
     * @return La lista del orden de la evidencia
     */
    Map<Long, String> getMapEvidence(){
        return mapEvidence
    }

    /**
     * <b>getDirEvidenceCopy</b>
     * @return La ruta del directorio de evidencias(out)
     */
        File getDirEvidenceCopy(){
            return filesEvidenceCopy
        }

    /**
     * <b>getDirEvidence</b>
     * @return La ruta del directorio de evidencias(int)
     */
        File getDirEvidence(){
            return filesEvidence
        }

    /**
     * <b>getFileEmpty</b>
     * @return EL estado del contenido de los archivos
     */
    Integer getFileEmpty(){
        return fileEmpty
    }
}
