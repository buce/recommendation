/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package collaborativefiltering;


import java.util.ArrayList;
import java.util.HashMap;


/**
 *
 * @author Buce
 */
public class UserBean {
    public HashMap<Integer,Float> oUser = new HashMap<Integer,Float> ();
    ArrayList<SimilarityBean> Similarity = new ArrayList<SimilarityBean>();
    public double minSimilarity = -2.0;
  
        
    UserBean(){
        for(int i=0;i<15;i++){
            Similarity.add(new SimilarityBean());
        }
    }
}
