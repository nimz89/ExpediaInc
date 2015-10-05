import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by nikhan on 04/10/15.
 */
public class StripingTrieTest {


    static ArrayList<StripingOrderNode> stripingOrderFromConfig = new ArrayList();
    static XPath xPath;
    static Document doc;
    static String configKeyFromInput = "";
    static DocumentBuilderFactory factory;
    static DocumentBuilder builder;
    public static void main(String[] args) throws FileNotFoundException, IOException, ParserConfigurationException, SAXException, XPathExpressionException {

        createStripingOrder();
        setStripingOrderValuesFromRequestContext();
        ArrayList<PrecedenceTrie> trieList=createTrieList();

        for(PrecedenceTrie trie : trieList)
        {
            if (trie.getRoot().getValue().toString().equals(configKeyFromInput)) {
                trie.getMatchingConfigKeyValue(stripingOrderFromConfig);
                break;
            }
        }


    }
    public static Document loadxml() throws FileNotFoundException, IOException,ParserConfigurationException,SAXException, XPathExpressionException {
        FileInputStream fstream = new FileInputStream("/Users/nikhan/IdeaProjects/StripingPrecedenceTrie/src/TestConfigProperties.xml");
        factory = DocumentBuilderFactory.newInstance();

        builder = factory.newDocumentBuilder();

        doc = builder.parse(fstream);

        return doc;

    }
    public static void createStripingOrder() throws FileNotFoundException, IOException, ParserConfigurationException, SAXException, XPathExpressionException
    {
        Document doc = loadxml();


        XPath xPath = XPathFactory.newInstance().newXPath();
        String stripingOrderRegEx = "/properties/entry[@key='order']/text()";


        String stripingOrderText = "";
        Node node = (Node) xPath.compile(stripingOrderRegEx).evaluate(doc, XPathConstants.NODE);

        stripingOrderText = node.getTextContent().trim().replaceAll("\n", "");


        for (String orderkey : stripingOrderText.split(",")) {
            StripingOrderNode stripingOrderAtrribute = new StripingOrderNode(orderkey.trim(), "");
            stripingOrderFromConfig.add(stripingOrderAtrribute);
        }


    }

    public static void setStripingOrderValuesFromRequestContext() throws FileNotFoundException,IOException

    {

        File file = new File("/Users/nikhan/IdeaProjects/StripingPrecedenceTrie/src/RequestContextInput.txt");
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        boolean right = false;
        boolean configKeyLine = true;
        int setValue = 0;

        while ((line = bufferedReader.readLine()) != null) {
            if (configKeyLine) {
                configKeyFromInput = line;
                configKeyLine = false;
                continue;
            }
            for (String requestKeyValue : line.trim().split("="))

            {
                if (!right) {
                    for (int i = 0; i < stripingOrderFromConfig.size(); i++)

                    {
                        if (stripingOrderFromConfig.get(i).getStripingKey().equals(requestKeyValue)) {
                            setValue = i;
                            right = true;
                            break;

                        }


                    }

                } else {
                    stripingOrderFromConfig.get(setValue).setStripingValue(requestKeyValue);
                    right = false;
                }
            }
        }
        fileReader.close();
        stripingOrderFromConfig.add(new StripingOrderNode("value", "test"));

    }



    public static ArrayList<PrecedenceTrie> createTrieList() throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        Document doc = loadxml();

        xPath = XPathFactory.newInstance().newXPath();
        String entryKeyNodeList = "/properties/entry[position()>1]";
        NodeList entryNodes = (NodeList) xPath.compile(entryKeyNodeList).evaluate(doc, XPathConstants.NODESET);
        boolean nextEntry = false;
        PrecedenceTrie defaultTrie;
        ArrayList<PrecedenceTrie> trieList = new ArrayList<>();
        int nodeCounter = 0;
        do {

            nextEntry = false;
            defaultTrie = new PrecedenceTrie(new AttributeNode(entryNodes.item(nodeCounter).getAttributes().item(0).getNodeValue(),false,-1));
            LinkedList sentence = new LinkedList();
            sentence.add("value");
            sentence.add(entryNodes.item(nodeCounter++).getTextContent());
            defaultTrie.insert(sentence,stripingOrderFromConfig);

            for (int x = 1, size = entryNodes.getLength(); nodeCounter < size; ) {

                sentence.clear();
                String entryValue = "";
                String configKey = "";
                String keyAttributeString = entryNodes.item(nodeCounter).getAttributes().item(0).getNodeValue();
                if (keyAttributeString.contains("?"))
                    configKey = keyAttributeString.trim().substring(0, keyAttributeString.indexOf("?") - 1);
                else
                    configKey = keyAttributeString;
                if (!defaultTrie.getRoot().getValue().toString().equals(configKey)) {
                    nextEntry = true;
                    break;

                }
                boolean isKey=false;
                String test = keyAttributeString.trim().replaceAll("and", "").substring(keyAttributeString.indexOf("?") + 2);
                entryValue = entryNodes.item(nodeCounter++).getTextContent();
                for (String stripingKeyValue : test.trim().split("\\s+")) {


                    for (String KeyValue : stripingKeyValue.trim().split("=")) {
                        isKey=true;
                        sentence.add(KeyValue.trim());
                        System.out.println(KeyValue.trim());
                        if(isKey)
                        {


                        }
                    }


                }
                sentence.add("value");
                sentence.add(entryValue);
                defaultTrie.insert(sentence,stripingOrderFromConfig);
            }
            trieList.add(defaultTrie);

        } while (nextEntry);





        return trieList;


    }
}
