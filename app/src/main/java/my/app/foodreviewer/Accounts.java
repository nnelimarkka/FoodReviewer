package my.app.foodreviewer;

import android.content.Context;
import android.content.res.AssetManager;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class Accounts {

    private static Accounts accounts = new Accounts();

    private Accounts() {
    }

    public static Accounts getInstance() {
        return(accounts);
    }

    /* Creates new account into xml-file accounts.xml; returns 0 for empty password or username, -3 for attempt to create an account that already exists,
    -2 for attempt to create account with name Admin, -1 for incorrect password, 1 for successful account creation*/
    public int createNewAccount(Context context, String username, String password) {
        if (username.length() == 0 || password.length() == 0) {
            return (0);
        }
        if (this.accountExists(context, username)) {
            return(-3);
        }
        if (username.matches("Admin")) {
            return(-2);
        }
        if (this.checkPassword(password)) {
            try {
                boolean fileInputStreamUsed = false;
                Element root;
                Document document;
                File file = new File(context.getFilesDir().toString() + "/accounts.xml");
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

                FileInputStream fis = null;
                if (file.exists()) {
                    fileInputStreamUsed = true;
                    fis = new FileInputStream(file);
                    document = documentBuilder.parse(fis);
                    root = document.getDocumentElement();
                }
                else {
                    fileInputStreamUsed = false;
                    document = documentBuilder.newDocument();
                    root = document.createElement("Accounts");
                    document.appendChild(root);
                }

                Element account = document.createElement("Account");

                Element user = document.createElement("username");
                user.appendChild(document.createTextNode(username));
                account.appendChild(user);

                Element pword = document.createElement("password");
                pword.appendChild(document.createTextNode(password));
                account.appendChild(pword);

                root.appendChild(account);

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
            return(1);
        }
        return(-1);
    }

    /* Method used to check if given account already exists in accounts.xml. Used to prevent the user from creating identical accounts */
    private boolean accountExists(Context context, String username) {
        try {
            File file = new File(context.getFilesDir().toString() + "/accounts.xml");
            if (file.exists() == false) {
                return(false);
            }

            FileInputStream fis = new FileInputStream(file);
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(fis);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getDocumentElement().getElementsByTagName("Account");

            if (nList.getLength() == 0) {
                return(false);
            }

            for (int i = 0; i < nList.getLength(); i++) {
                String user;
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    user = element.getElementsByTagName("username").item(0).getTextContent();
                    if (user.matches(username)) {
                        return(true);
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
        return(false);
    }

    /* Checks if given account credentials are found in xml; returns -2 for account not found, -1 for no accounts found, 0 for incorrect password,
    1 for successful login (given credentials match to a created account), 2 for successful admin login */
    public int loginToAccount(Context context, String username, String password) {
        if (username.matches("Admin")) {
            if (adminLogin(context, password)) {
                return(2);
            }
            else {
                return(0);
            }
        }
        try {
            File file = new File(context.getFilesDir().toString() + "/accounts.xml");
            if (file.exists() == false) {
                return(-1);
            }

            FileInputStream fis = new FileInputStream(file);
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(fis);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getDocumentElement().getElementsByTagName("Account");

            if (nList.getLength() == 0) {
                return(-1);
            }

            for (int i = 0; i < nList.getLength(); i++) {
               String user;
               String pword;
               Node node = nList.item(i);
               if (node.getNodeType() == Node.ELEMENT_NODE) {
                   Element element = (Element) node;
                   user = element.getElementsByTagName("username").item(0).getTextContent();
                   if (user.matches(username)) {
                       pword = element.getElementsByTagName("password").item(0).getTextContent();
                       if (pword.matches(password)) {
                           fis.close();
                           return(1);
                       }
                       else {
                           fis.close();
                           return(0);
                       }
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
       return(-2);
    }

    /* Method returns true if given admin-password matches the password found in asset Admin.xml */
    private boolean adminLogin(Context context, String password) {
        AssetManager am = context.getAssets();
        String pword;
        try {
            InputStream is = am.open("Admin.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(is);
            document.getDocumentElement().normalize();

            Node node = document.getElementsByTagName("Admin").item(0);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                pword = element.getElementsByTagName("password").item(0).getTextContent();
                if (password.matches(pword)) {
                    return(true);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return(false);
    }

    private boolean checkPassword(String password) {
        if (password.length() > 11) {
            if (password.matches(".*\\d.*")) { //check if password has numbers in it
                if (!password.equals(password.toLowerCase())) { //check if password contains at least one capital letter (checks if password doesn't match it's lowercase version)
                    return(true);
                }
            }
        }
        return(false);
    }

    /* Method used to delete given account from accounts.xml (only accessible for Admin account) */
    public boolean deleteAccount(Context context, String username) {
        try {
            File file = new File(context.getFilesDir().toString() + "/accounts.xml");
            if (file.exists() == false) {
                return(false);
            }

            FileInputStream fis = new FileInputStream(file);
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(fis);
            doc.getDocumentElement().normalize();


            Node rootNode = doc.getDocumentElement();
            NodeList nList = rootNode.getChildNodes();

            if (nList.getLength() == 0) {
                return(false);
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
                        return(true);
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
        return(false);
    }
}
