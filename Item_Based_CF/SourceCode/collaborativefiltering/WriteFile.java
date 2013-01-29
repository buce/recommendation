/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package collaborativefiltering;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Buce
 */
public class WriteFile {
  File file;
  Writer output = null; 
  String newline = System.getProperty("line.separator");
  WriteFile(String fileName){
       file = new File(fileName);
  }
  public void Recommendation(){
        try {
          output = new BufferedWriter(new FileWriter(file));
          
         Iterator it = CollaborativeFiltering.UserRatings.entrySet().iterator();
         while(it.hasNext()){
          PriorityQueue<MovieSimilarity> SimiliarMovies = new PriorityQueue<MovieSimilarity>(15, new Comparator<MovieSimilarity>() {
          @Override     
          public int compare(MovieSimilarity o1, MovieSimilarity o2) {
                        if (o1.Coefficient > o2.Coefficient)
                                return -1;
                        else if (o1.Coefficient < o2.Coefficient )
                                return 1;
                        else
                                return 0;
            }
            });
             
            Map.Entry userIterator = (Map.Entry<Integer, RatingsBean>) it.next();
            RatingsBean userrating = (RatingsBean) userIterator.getValue();
            
            // Iterator to crawl through User Movie Ratings
            Iterator ratingit = userrating.Rating.entrySet().iterator();
            while(ratingit.hasNext()){
                Map.Entry ratingmap = (Map.Entry<Integer, Float>) ratingit.next();
                ArrayList<SimilarityBean> Similar = CollaborativeFiltering.MovieBean.get((Integer)ratingmap.getKey()).Similarity;
                
                for(SimilarityBean oS : Similar){
                   MovieSimilarity oM = new MovieSimilarity();
                   oM.MovieId = oS.MovieId;
                   oM.Coefficient = ((oS.SimilarityCoefficient)*((Float)ratingmap.getValue()));
                   SimiliarMovies.add(oM);
                }
                
            }
          ArrayList<Integer> dummy = new ArrayList <Integer>();  
          Outer:    while(true){
             MovieSimilarity oM = SimiliarMovies.poll();
             if(oM==null){
                 break;
             }
             for(Integer l : dummy){
                 if(l==oM.MovieId){
                     continue Outer;
                 }
             }
             dummy.add(oM.MovieId);
             String outputtxt = ((Integer) userIterator.getKey()).toString()+";"+oM.MovieId+";"+roundTwoDecimals(oM.Coefficient)+newline;
             output.write(outputtxt);
             if(dummy.size()==15){
                 break;
             }
         }   
            
        }   
            output.close();
        } catch (IOException ex) {
            Logger.getLogger(WriteFile.class.getName()).log(Level.SEVERE, null, ex);
        }
       
  }
  
  double roundTwoDecimals(double d) {
        	DecimalFormat twoDForm = new DecimalFormat("#.#####");
		return Double.valueOf(twoDForm.format(d));
}
}
