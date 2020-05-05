package my.app.foodreviewer;

import android.content.Context;
import android.content.res.AssetManager;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Menus {

    private static Menus menus = new Menus();

    private Menus(){
    }

    public static Menus getInstance() {

        return(menus);
    }

    /* Method returns the given restaurants menu in a list from asset menus.xml */
    public List<String> getRestaurantMenu(Context context, String restaurant) {
        List<String> menu = new ArrayList<String>();
        AssetManager am = context.getAssets();
        try {
            InputStream is = am.open("menus.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(is);
            document.getDocumentElement().normalize();

            Node node = document.getElementsByTagName(restaurant).item(0);
            NodeList childList = node.getChildNodes();

            for (int i = 0; i < childList.getLength(); i++) {
                String day;
                String option1;
                String option2;
                Node childNode = childList.item(i);

                if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) childNode;
                    day = element.getAttribute("name");
                    day = day + ": ";
                    option1 = day + element.getElementsByTagName("option1").item(0).getTextContent();
                    option2 = day + element.getElementsByTagName("option2").item(0).getTextContent();
                    menu.add(option1);
                    menu.add(option2);
                    menu.add("");
                }

            }
            return(menu);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return(menu);
    }
}
