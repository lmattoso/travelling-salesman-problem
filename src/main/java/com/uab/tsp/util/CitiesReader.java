package com.uab.tsp.util;

import com.uab.tsp.model.City;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CitiesReader {

    public List<City> getCitiesFromFile(String fileName) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        List<City> cities = new ArrayList<>();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc = db.parse(getFileFromResourceAsStream(fileName));
            doc.getDocumentElement().normalize();

            NodeList vertex = doc.getDocumentElement()
                .getElementsByTagName("vertex");

            for (int i = 0; i < vertex.getLength(); i++) {
                Node node = vertex.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    Integer id = i;

                    NodeList edges = element.getElementsByTagName("edge");

                    Map<Integer, BigDecimal> distances = new HashMap<>();
                    for (int j = 0; j < edges.getLength(); j++) {
                        Node edgeNode = edges.item(j);

                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            Element edge = (Element)edgeNode;
                            distances.put(Integer.valueOf(edge.getTextContent()), BigDecimal.valueOf(Double.parseDouble(edge.getAttribute("cost"))));
                        }
                    }
                    cities.add(City.builder().id(id).distances(distances).build());
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return cities;
    }

    private InputStream getFileFromResourceAsStream(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }
    }
}