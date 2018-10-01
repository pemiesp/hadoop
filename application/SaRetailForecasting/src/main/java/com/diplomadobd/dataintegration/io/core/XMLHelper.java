/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diplomadobd.dataintegration.io.core;

import java.io.File;
import java.util.LinkedList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 *
 * @author mario
 */
public class XMLHelper {

    /**
     * Method that eliminates the text nodes outside the XML tags. This nodes
     * often are spaces or new line characters. This nodes make noise that is
     * why we should eliminate them.
     *
     * @param nl The NodeList to be pruned
     * @return A LinkedList pruned of noisy Text nodes
     */
    public static LinkedList<Node> cleanTextNodes(NodeList nl) {
        LinkedList<Node> ll = new LinkedList();
        for (int i = 0; i < nl.getLength(); i++) {
            Node theNode = nl.item(i);
            if (!(theNode instanceof Text)) {
                ll.addLast(theNode);
            }
        }
        return ll;
    }

    /**
     * Method that brings XML document from a file name
     *
     * @param theFilename
     * @return
     */
    public static Document getXmlDocument(String theFilename) {
        Document doc = null;
        try {
            File xmlFile = new File(theFilename);            
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(xmlFile);
        } catch (Exception e) {
            throw new IllegalStateException("Error reading the xml: " + theFilename, e);
        }
        return doc;
    }
}
