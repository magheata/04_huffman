/* Created by Miruna Andreea Gheata & Rafael Adrián Gil Cañestro */
package Presentation.Panels;

import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

/**
 * Panel que contiene la información de uso del programa
 */
public class IntroductionPanel extends JEditorPane {

    public IntroductionPanel() {
        initComponents();
    }

    private void initComponents() {
        this.setContentType("text/html");
        this.setEditable(false);

        HTMLEditorKit kit = new HTMLEditorKit();
        this.setEditorKit(kit);

        StyleSheet styleSheet = kit.getStyleSheet();
        styleSheet.addRule("h1 {color: #FDFFFC; background-color: #01233F; text-align: center;}");
        styleSheet.addRule("h2 {color: #FDFFFC; background-color: #E71D36; text-align: center;}");
        styleSheet.addRule("h5 {color: #2EC4B6; text-align: right;}");

        styleSheet.addRule("body {background-color: #FDFFFC; text-align: justify;}");

        String title = "<h1>Compresor Huffman</h1>";
        String autores = "<h5> <i> Creado por Miruna Andreea Gheata y Rafael Adrián Gil Cañestro </i> </h5>";


        String introduccion = "<p> Programa que permite comprimir cualquier tipo de archivo. </p>";

        String ficherosFormato = "<h2> Formato de los ficheros </h2>" +
                "Los ficheros que se pueden comprimir pueden tener cualquier extensión (txt, docx, pdf, png), pero " +
                "el nombre del archivo no puede contener acentos, símbolos extraños o puntos (por ejemplo 1.Foto.png no " +
                "sería correcto. Ejemplo correcto: foto.png, Archivo.pdf, mi_text.txt). ";

        String ejemplos = "<h2> Archivos de ejemplo </h2>" +
                "Dentro de la carpeta de ejemplos se pueden encontrar distintos archivos de prueba. ";

        String comprimirArchivoInfo = "<h2> Comprimir un archivo </h2>";
        String listaPasosComprimirArchivo =
                "<ol>" +
                        "<li>Seleccionar archivo(s)" +
                            "<ul> " +
                                "<li>Mediante el selector de ficheros</li>" +
                                "<li>Arrastrando archivos dentro del contenedor</li>" +
                            "</ul>" +
                        "</li>" +
                        "<li>Pulsar el botón de comprimir archivos</li>" +
                "</ol>";
        String eliminarArchivoInfo = "<h2> Eliminar un archivo de la lista de archivos por comprimir </h2>";
        String listaPasosEliminarArchivo =
                "<ol>" +
                        "<li>Seleccionar un archivo de la lista de archivos cargados" +
                        "<li>Arrastrar el archivo hasta la Papelera</li>" +
                "</ol>";

        String visualizarArchivoInfo = "<h2> Visualizar información de la compresión de un archivo </h2>" +
                "Tras la compresión del archivo se puede acceder a los códigos de Huffman generados y al árbol resultante.";
        String listaPasosVisualizarArchivo =
                "<ol>" +
                        "<li>Ir a la pestaña de <i>Archivos comprimidos</i></li>" +
                        "<li>Elegir uno de los archivos de la tabla de archivos comprimidos</li>" +
                "</ol>" + "Los códigos Huffman y el árbol Huffman apareceran en las pestañas correspondientes.";

        String descomprimirArchivoInfo = "<h2> Descomprimir un archivo </h2>" +
                "Si es un archivo de texto (extensión .txt), saldrá el archivo original y el descomprimido para que se" +
                "aprecie la calidad de la descompresión. En el caso de los otros archivos, tras la descompresión se abrirá" +
                "dicho archivo.";

        String carpetasResultantes = "<h2> Ficheros resultantes de la compresión </h2>" +
                "Tras haber comprimido un archivo se crean distintos ficheros: " +
                    "<ul> " +
                        "<li>El archivo resultante comprimido dentro de la carpeta <i>compressed</i></li>" +
                        "<li>Los códigos Huffman dentro de la carpeta <i>huffmanCodes</i></li>" +
                        "<li>El árbol Huffman dentro de la carpeta <i>huffmanTrees</i></li>" +
                "</ul>";
        this.setText(
                title + autores +
                introduccion +
                ficherosFormato +
                ejemplos +
                comprimirArchivoInfo + listaPasosComprimirArchivo +
                eliminarArchivoInfo + listaPasosEliminarArchivo +
                visualizarArchivoInfo + listaPasosVisualizarArchivo +
                descomprimirArchivoInfo +
                carpetasResultantes);
    }
}
