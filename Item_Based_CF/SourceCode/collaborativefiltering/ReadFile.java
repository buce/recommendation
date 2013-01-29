/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package collaborativefiltering;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;

/**
 *
 * @author Buce
 */
public class ReadFile {

    public void readfile(String fileName, int type) {
        FileInputStream fstream = null;
        try {
            fstream = new FileInputStream(fileName);
            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            //Read File Line By Line
            while ((strLine = br.readLine()) != null) {
                if (type == 1) {
                    parseText(strLine);
                } else if(type==2) {
                    parseTextUser(strLine);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ReadFile.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fstream.close();
            } catch (IOException ex) {
                Logger.getLogger(ReadFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void parseText(String text) {
        String[] aSplit = text.split("\\s+");
        int UserId = Integer.parseInt(aSplit[0]);
        int MovieId = Integer.parseInt(aSplit[1]);
        float Rating = Float.parseFloat(aSplit[2]);
        UserBean oTemp = CollaborativeFiltering.MovieBean.get(MovieId);
        if (oTemp == null) {
            oTemp = new UserBean();
            oTemp.oUser.put(UserId, Rating);
            CollaborativeFiltering.MovieBean.put(MovieId, oTemp);
        } else {
            oTemp.oUser.put(UserId, Rating);
        }
    }

    public void parseTextUser(String text) {
        String[] aSplit = text.split("\\s+");
        int UserId = Integer.parseInt(aSplit[0]);
        int MovieId = Integer.parseInt(aSplit[1]);
        float Rating = Float.parseFloat(aSplit[2]);

        RatingsBean rating = CollaborativeFiltering.UserRatings.get(UserId);
        if (rating == null) {
            rating = new RatingsBean();
            rating.Rating.put(MovieId, Rating);
            CollaborativeFiltering.UserRatings.put(UserId, rating);
        } else {
            rating.Rating.put(MovieId, Rating);
        }

    }
}
