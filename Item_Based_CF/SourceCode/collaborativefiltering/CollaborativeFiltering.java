/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package collaborativefiltering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Vector;


/**
 *
 * @author Buce
 */

public class CollaborativeFiltering {

// Utility Matrix
public static HashMap<Integer,UserBean> MovieBean = new HashMap <Integer, UserBean>();
//public static HashMap<Integer,Integer> UserLookup = new HashMap<Integer,Integer>();
public static HashMap<Integer,RatingsBean> UserRatings = new HashMap<Integer, RatingsBean>();    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ReadFile oRead = new ReadFile();
        oRead.readfile("ratings.txt",1);
        
        Iterator it = MovieBean.entrySet().iterator();
        int i = 1;
        while(it.hasNext()){
            Map.Entry me = (Map.Entry<Integer, UserBean>) it.next();
            Similarity((Integer)me.getKey(),i);
            i++;
        }
           
        /*ArrayList<SimilarityBean> oa = MovieBean.get(840750).Similarity;
        for(SimilarityBean os : oa){
            System.out.println("Movie 77 "+os.MovieId+" "+os.SimilarityCoefficient);
        }*/
        
        oRead.readfile("ratings.txt",2);
        WriteFile owrite = new WriteFile("CF_Recommendation.txt");
        owrite.Recommendation();
    }
      
    public static void Similarity(int MovieId,int Count) {
        UserBean oUser = MovieBean.get(MovieId);
        double modulo1 = 0.0;
        Iterator iterator = oUser.oUser.entrySet().iterator();
        // Iterate on target MovieId to calculate modulo |U|
        while(iterator.hasNext()){
            Map.Entry obj = (Map.Entry<Integer, Float>) iterator.next();
            float U = ((Float) obj.getValue());
            modulo1+= U * U;
        }
        
        // Itreate over other MovieId  
        Iterator it = MovieBean.entrySet().iterator();
        for(int i=1;i<=Count;i++){
            it.next();
        }
        while (it.hasNext()) {
            double SimilarityCoefficient = -2.0;
            Map.Entry me = (Map.Entry<Integer, UserBean>) it.next();
            if (MovieId == me.getKey()) {
                continue;
            }
            double dot = 0.0;
            double modulo2 = 0.0;
            // Iterate over the other movie id's user bean to calculate dot product U.V and modulo2 |V|
            Iterator userIterator = ((UserBean)me.getValue()).oUser.entrySet().iterator();
            while(userIterator.hasNext()){
                Map.Entry userObj = (Map.Entry<Integer, Float>) userIterator.next();
                if(!oUser.oUser.containsKey((Integer)userObj.getKey())){
                    break;
                }
                float U = oUser.oUser.get((Integer)userObj.getKey());
                float V = (Float)userObj.getValue();
                dot+= (U*V);
                modulo2+= (V*V);
            }
            if(modulo1 != 0.0 && modulo2 !=0.0){
            SimilarityCoefficient = dot / ( (Math.sqrt(modulo1)) * (Math.sqrt(modulo2)) );
            }
            //Call function to fill in the set
            if(SimilarityCoefficient>oUser.minSimilarity){
               topmovies(oUser,(Integer)me.getKey(),SimilarityCoefficient);
            }
            if(SimilarityCoefficient>((UserBean)me.getValue()).minSimilarity){
               topmovies((UserBean)me.getValue(),MovieId,SimilarityCoefficient); 
            }
            
        }
        oUser.oUser = null;

    }
   
    public static void topmovies (UserBean oUser, int MovieId2, double SimilairtyCoefficient){
        double leastValue = oUser.minSimilarity;
        int flag=0;
        for(int i=0;i<oUser.Similarity.size();i++){
            Double value = oUser.Similarity.get(i).SimilarityCoefficient;
            if(value==oUser.minSimilarity&&flag==0){
               SimilarityBean oBean = oUser.Similarity.get(i);
               oBean.MovieId = MovieId2;
               oBean.SimilarityCoefficient = SimilairtyCoefficient;    
               value = SimilairtyCoefficient;
               flag=1;
            }
            if(value<leastValue){
                leastValue = value;
            }
            
        }
        oUser.minSimilarity = leastValue;
        
    }
}
