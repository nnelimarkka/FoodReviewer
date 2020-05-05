package my.app.foodreviewer;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class UserReviews {

    public static UserReviews userReviews = new UserReviews();

    private UserReviews() {
    }

    public static UserReviews getInstance() {
        return(userReviews);
    }

    /* Method saves users review into a csv-file, file name is in format: user+restaurant+food.csv */
    public void saveReview(Context context, ArrayList<String> review, String user, String restaurant, String food) {
        boolean firstValue = true;
        char separator = ',';
        char quote = '"';
        food = food.replaceAll("\\s+", "");
        File csvFile = new File(context.getFilesDir() + "/" + user + restaurant + food + ".csv");
        try {
            FileWriter fileWriter = new FileWriter(csvFile);
            StringBuilder stringBuilder = new StringBuilder();

            for (String value : review) {
                if (firstValue == false) {
                    stringBuilder.append(separator);
                }
                stringBuilder.append(quote).append(value).append(quote);
                firstValue = false;
            }
            stringBuilder.append("\n");
            fileWriter.append(stringBuilder.toString());

            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Method returns users review in an ArrayList if it has been saved to a xml-file */
    public ArrayList<String> getReview(Context context, String user, String restaurant, String food) {
        ArrayList<String> review = new ArrayList<>();
        food = food.replaceAll("\\s+", "");
        File csvFile = new File(context.getFilesDir() + "/" + user + restaurant + food + ".csv");
        if (csvFile.exists() == false) {
            return(review);
        }
        try {
            String line = "";
            BufferedReader bufferedReader = new BufferedReader(new FileReader(csvFile));
            while ((line = bufferedReader.readLine()) != null) {
                String[] s = line.split(",");
                for (int i = 0; i < s.length; i++) {
                    String temp = s[i];
                    temp = temp.replaceAll("^\"|\"$", "");
                    review.add(temp);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return(review);
    }

    /* Method used to delete the saved user review after user sends the review */
    public void deleteSavedReview(Context context, String filename) {
        File file = new File(context.getFilesDir() + "/" + filename);
        if (file.exists()) {
            file.delete();
        }
    }
}
