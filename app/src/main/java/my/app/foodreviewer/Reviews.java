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
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class Reviews {

    private static Reviews reviews = new Reviews();

    private Reviews() {
    }

    public static Reviews getInstance() {
        return(reviews);
    }

    /* Used to save the review string into reviews.xml file */
    public void saveReview(Context context, String review, String tag) {
        try {
            boolean fileInputStreamUsed = false;
            Element root;
            Document document;
            File file = new File(context.getFilesDir().toString() + "/reviews.xml");
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
                root = document.createElement("Reviews");
                document.appendChild(root);
            }

            Element Review = document.createElement("Review");
            Review.setAttribute("tag", tag);
            Review.appendChild(document.createTextNode(review));

            root.appendChild(Review);

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

    /* This method returns an ArrayList of all reviews that have the same tag-attribute as the tag given (tag is restaurant+food) */
    public ArrayList<String> getReviews(Context context, String tag) {
        ArrayList<String> reviewList = new ArrayList<>();
        try {
            File file = new File(context.getFilesDir().toString() + "/reviews.xml");
            if (file.exists() == false) {
                return(reviewList);
            }

            FileInputStream fis = new FileInputStream(file);
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(fis);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getDocumentElement().getElementsByTagName("Review");

            if (nList.getLength() == 0) {
                return(reviewList);
            }

            for (int i = 0; i < nList.getLength(); i++) {
                String reviewString;
                String nodeTag;
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    nodeTag = element.getAttribute("tag");
                    if (nodeTag.matches(tag)) {
                        reviewString = element.getTextContent();
                        reviewList.add(reviewString);
                    }
                }
            }
            fis.close();
            return(reviewList);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return(reviewList);
    }

    /* Method used to delete review.xml (only accessible for Admin account) */
    public boolean deleteReviews(Context context) {
        File file = new File(context.getFilesDir().toString() + "/reviews.xml");
        if (file.exists()) {
            file.delete();
            return(true);
        }
        return(false);
    }

    /* Method used to remove a review from reviews.xml by tag and username (only accessible for Admin account) */
    public boolean deleteReviewByTag(Context context, String tag, String username) {
        try {
            File file = new File(context.getFilesDir().toString() + "/reviews.xml");
            if (file.exists() == false) {
                return(false);
            }

            FileInputStream fis = new FileInputStream(file);
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(fis);
            doc.getDocumentElement().normalize();


            NodeList nList = doc.getElementsByTagName("Review");

            if (nList.getLength() == 0) {
                return(false);
            }

            for (int i = 0; i < nList.getLength(); i++) {
                String nodeTag;
                Node node = nList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    nodeTag = element.getAttribute("tag");
                    if (nodeTag.matches(tag)) {
                        String review = element.getTextContent();
                        if (review.indexOf(username) != -1) { // Check if the review contains the given username.
                            doc.getDocumentElement().removeChild(node);

                            TransformerFactory transformerFactory = TransformerFactory.newInstance();
                            Transformer transformer = transformerFactory.newTransformer();
                            DOMSource source = new DOMSource(doc);

                            StreamResult result = new StreamResult(file);
                            transformer.transform(source, result);

                            fis.close();
                            return (true);
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
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return(false);
    }
}
