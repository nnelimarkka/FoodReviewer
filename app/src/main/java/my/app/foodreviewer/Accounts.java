package my.app.foodreviewer;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
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

    private static Accounts accounts = null;
    private static Context context = null;

    private Accounts(Context cntx) {
        context = cntx;
    }

    public static Accounts getInstance(Context cntx) {
        if (accounts == null) {
            accounts = new Accounts(cntx);
        }
        return(accounts);
    }

    /* Creates new account into xml-file accounts.xml; returns 0 for empty password or username, -1 for incorrect password, 1 for successful account creation*/
    public int createNewAccount(String username, String password) {
        if (username.length() == 0 || password.length() == 0) {
            return (0);
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

    private boolean checkPassword(String password) {
        if (password.length() > 11) {
            if (password.matches(".*\\d.*")) { //check if password has numbers in it
                if (!password.equals(password.toLowerCase())) { //check if password contains at least one capital letter (checks if password doesn't match it's lowercase version
                    return(true);
                }
            }
        }
        return(false);
    }
}
