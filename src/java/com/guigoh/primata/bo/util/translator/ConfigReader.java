/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bo.util.translator;

import com.guigoh.primata.bo.util.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.SAXException;

/**
 *
 * @author ipti
 */
public class ConfigReader implements Serializable{

    File xml;
    static Document doc = null;
    
    public ConfigReader() {
    }
    
    private static void loadXML() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = dbf.newDocumentBuilder();
            doc = builder.parse(new File(getPath()+"WEB-INF/classes/com/guigoh/primata/bo/util/translator/config.xml"));
        } catch (SAXException e) {
            System.exit(1);
        } catch (ParserConfigurationException e) {
            System.err.println(e);
            System.exit(1);
        } catch (IOException e) {
            System.err.println(e);
            System.exit(1);
        }
    }
    
    public static String getPath() {
        FacesContext context = FacesContext.getCurrentInstance();
        ServletContext sc = (ServletContext) context.getExternalContext().getContext();
        String path = sc.getRealPath("") + "/";
        return path;
    }

    public void editarTag(String tag, String titulo) {
        loadXML();
        Element dE = doc.getDocumentElement();
        NodeList nodeList = dE.getElementsByTagName(tag);
        Element element = (Element) nodeList.item(0);
        element.setTextContent(titulo);
        saveChanges();
    }

    public String getTag(String tag) {
        String txt = "";
        try {
        loadXML();
        Element dE = doc.getDocumentElement();
        NodeList nodeList = dE.getElementsByTagName(tag);
        Element element = (Element) nodeList.item(0);
        txt = element.getFirstChild().getNodeValue();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return txt;
    }
    
    private static void saveChanges() {
        DOMSource domSrc = new DOMSource(doc);
        StreamResult streamResult = null;
        try {
            streamResult = new StreamResult(new FileOutputStream(getPath()+"WEB-INF/classes/com/guigoh/primata/bo/util/translator/config.xml"));
        } catch (FileNotFoundException ex) {
        }
        TransformerFactory transformerFac = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transformerFac.newTransformer();
        } catch (TransformerConfigurationException ex) {
        }
        try {
            transformer.transform(domSrc, streamResult);
        } catch (TransformerException ex) {
        }
    }
    
}
