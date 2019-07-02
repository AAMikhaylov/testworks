package com.lifeIt.taxi;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

public class Message {
    private final static String messageTamplate = "<?xml version='1.0' encoding='UTF-8'?>\n" +
            "<message>\n" +
            "<target id=\"0\"/>\n" +
            "<sometags>\n" +
            "<data> </data>\n" +
            "<data> </data>\n" +
            "<data> </data>\n" +
            "</sometags>\n" +
            "</message>\n";
    private static int ID_GENERATOR = 1;
    private final int id;
    private final int targetId;
    private String body;

    public Message(int targetId) {
        id = ID_GENERATOR;
        ID_GENERATOR++;
        this.targetId = targetId;
        Document document = getDomDocument(messageTamplate);
        if (document != null) {
            NodeList nl = document.getElementsByTagName("target");
            ((Element) nl.item(0)).setAttribute("id", String.valueOf(targetId));
            body = getXML(document);
        } else
            body = null;
    }

    private String getXML(Document document) {
        try {
            Transformer tr = TransformerFactory.newInstance().newTransformer();
            DOMSource source = new DOMSource(document);
            StringWriter writer = new StringWriter();
            StreamResult streamResult = new StreamResult(writer);
            tr.transform(source, streamResult);
            return writer.toString();
        } catch (TransformerException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Document getDomDocument(String xmlString) {
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            return documentBuilder.parse(new InputSource(new StringReader(xmlString)));
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveTofile() {
        String fName = this.targetId + "\\" + this.id + ".xml";
        File file = new File(fName);
        file.getParentFile().mkdir();
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(body);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void addDispatcherId(int dispatcherId) {
        Document document = getDomDocument(body);
        Element element = document.createElement("dispatched");
        element.setAttribute("id", String.valueOf(dispatcherId));
        NodeList nl = document.getElementsByTagName("message");
        nl.item(0).insertBefore(element, nl.item(0).getFirstChild());
        body = getXML(document);
    }

    public int getTargetId() {
        return targetId;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", body='" + body.replace("\r\n", "  ") + '\'' +
                '}';
    }
}
