package my.app.foodreviewer;

import android.content.Context;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class UserInformation {

    private String user;
    private static UserInformation userInformation = new UserInformation();

    private UserInformation() {
    }

    public static UserInformation getInstance() {
        return(userInformation);
    }

    /* Method creates userinfo.xml if it doesn't exist. If it does exist it checks if it already contains a node for the current user.
    If the node does exist its childnodes content is changed according to user input (name, age, e-mail).
    If the file exists, but there is no node for the current user, a new node is created for the users information. */
    public void setUserInformation(Context context, String name, String age, String email) {
        try {
            boolean fileInputStreamUsed = false;
            Element root;
            Document document;
            File file = new File(context.getFilesDir().toString() + "/userinfo.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            FileInputStream fis = null;
            if (file.exists()) {
                fileInputStreamUsed = true;
                fis = new FileInputStream(file);
                document = documentBuilder.parse(fis);
                root = document.getDocumentElement();

                document.getDocumentElement().normalize();

                NodeList nList = document.getDocumentElement().getElementsByTagName("User");

                for (int i = 0; i < nList.getLength(); i++) {
                    String username;
                    String userInfo;
                    Node node = nList.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;
                        username = element.getElementsByTagName("username").item(0).getTextContent();
                        if (user.matches(username)) {
                            NodeList childList = node.getChildNodes();
                            for (int j = 0; j < childList.getLength(); j++) {
                                Node childNode = childList.item(j);
                                if (childNode.getNodeName().matches("name")) {
                                   childNode.setTextContent(name);
                                }
                                if (childNode.getNodeName().matches("age")) {
                                    childNode.setTextContent(age);
                                }
                                if (childNode.getNodeName().matches("email")) {
                                    childNode.setTextContent(email);
                                }
                            }

                            TransformerFactory transformerFactory = TransformerFactory.newInstance();
                            Transformer transformer = transformerFactory.newTransformer();
                            DOMSource source = new DOMSource(document);

                            StreamResult result = new StreamResult(file);
                            transformer.transform(source, result);

                            fis.close();
                            return;
                        }
                    }
                }
            }
            else {
                fileInputStreamUsed = false;
                document = documentBuilder.newDocument();
                root = document.createElement("Users");
                document.appendChild(root);
            }

            Element User = document.createElement("User");

            Element username = document.createElement("username");
            username.appendChild(document.createTextNode(user));
            User.appendChild(username);

            Element Age = document.createElement("age");
            Age.appendChild(document.createTextNode(age));
            User.appendChild(Age);

            Element Name = document.createElement("name");
            Name.appendChild(document.createTextNode(name));
            User.appendChild(Name);

            Element Email = document.createElement("email");
            Email.appendChild(document.createTextNode(email));
            User.appendChild(Email);

            root.appendChild(User);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);

            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);

            if (fileInputStreamUsed) { // this was needed so fis can't close when it's null
                fis.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    /* Returns the user information of current user (name, age, e-mail) from userinfo.xml, if user has saved them */
    public String getUserInformation(Context context) {
        try {
            File file = new File(context.getFilesDir().toString() + "/userinfo.xml");
            if (file.exists() == false) {
                return("");
            }

            FileInputStream fis = new FileInputStream(file);
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(fis);
            document.getDocumentElement().normalize();

            NodeList nList = document.getDocumentElement().getElementsByTagName("User");

            if (nList.getLength() == 0) {
                return("");
            }

            for (int i = 0; i < nList.getLength(); i++) {
                String username;
                String userInfo;
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    username = element.getElementsByTagName("username").item(0).getTextContent();
                    if (user.matches(username)) {
                        userInfo = element.getElementsByTagName("name").item(0).getTextContent();
                        userInfo = userInfo + "\n" + element.getElementsByTagName("age").item(0).getTextContent();
                        userInfo = userInfo + "\n" + element.getElementsByTagName("email").item(0).getTextContent();

                        fis.close();
                        return (userInfo);
                    }
                }
            }
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return("");
    }

    public void setUser(String s) {
        user = s;
    }

    /* Method used to delete user information when admin deletes user */
    public void deleteUserInfo(Context context, String username) {
        try {
            File file = new File(context.getFilesDir().toString() + "/userinfo.xml");
            if (file.exists() == false) {
                return;
            }

            FileInputStream fis = new FileInputStream(file);
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(fis);
            doc.getDocumentElement().normalize();


           NodeList nList = doc.getElementsByTagName("User");

            if (nList.getLength() == 0) {
                return;
            }

            for (int i = 0; i < nList.getLength(); i++) {
                String user;
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    user = element.getElementsByTagName("username").item(0).getTextContent();
                    if (user.matches(username)) {
                        doc.getDocumentElement().removeChild(node);

                        TransformerFactory transformerFactory = TransformerFactory.newInstance();
                        Transformer transformer = transformerFactory.newTransformer();
                        DOMSource source = new DOMSource(doc);

                        StreamResult result = new StreamResult(file);
                        transformer.transform(source, result);

                        fis.close();
                        return;
                    }
                }
            }
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}
