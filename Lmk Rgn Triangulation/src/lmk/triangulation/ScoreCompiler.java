/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lmk.triangulation;

import java.util.ArrayList;

/**
 *
 * @author rxiao
 */
public class ScoreCompiler {
    private final LmkCompiler mainset, comparedset;
    private final ArrayList<int[]> scores;
    private final ArrayList<Integer> finalset;
    
    
    public ScoreCompiler(LmkCompiler mainset, LmkCompiler otherset){
        this.mainset = mainset;
        this.comparedset = otherset;
        this.scores = new ArrayList<int[]>(this.mainset.getRegions().size());
        this.finalset = new ArrayList<Integer>(this.mainset.getRegions().size());
    }
    
    public void scoreRgnSet(){
        for(int[] subset : scores){
            double compscore = (double)(subset[0])/(double)(subset[1]);
            if(compscore >= LmkTriangulation.simUpperLim) this.finalset.add(2);
            else if(compscore >= LmkTriangulation.simLowerLim) this.finalset.add(1);
            else this.finalset.add(0);
        }
    }
    
    public void compareRgnSet(){
        ArrayList<int[]> compang = this.RgnAngle();
        ArrayList<int[]> compmin = this.RgnRatioMin();
        ArrayList<int[]> compmax = this.RgnRatioMax();
        ArrayList<int[]> compnorm = this.NeighborRgnAngle();
        
        for(int i = 0; i < mainset.getRegions().size(); i ++){
            int[] subset = new int[2];
            subset[0] = compang.get(i)[0] + compmin.get(i)[0] + compmax.get(i)[0] + compnorm.get(i)[0];
            subset[1] = compang.get(i)[1] + compmin.get(i)[1] + compmax.get(i)[1] + compnorm.get(i)[1];
            scores.add(subset);
        }
    }
    
    private ArrayList<int[]> RgnAngle(){
        ArrayList<int[]> results = new ArrayList<int[]>(mainset.getRegions().size());
        for(int i = 0; i < mainset.getRegions().size(); i ++){
            int[] subset = {0, 0};
            for(int j = 0; j < mainset.getRegions().get(i).getangs().size(); j ++){
                double val1 = mainset.getRegions().get(i).getangs().get(j);
                double val2 = comparedset.getRegions().get(i).getangs().get(j);
                if(Math.abs(val1 - val2) <= LmkTriangulation.angThresh) subset[0]++;
                subset[1]++;
            }
            results.add(subset);
        }
        return results;
    }
    
    private ArrayList<int[]> RgnRatioMin(){
        ArrayList<int[]> results = new ArrayList<int[]>(mainset.getRegions().size());
        for(int i = 0; i < mainset.getRegions().size(); i ++){
            int[] subset = {0, 0};
            for(int j = 0; j < mainset.getRegions().get(i).getlens().size(); j ++){
                double val1 = mainset.getRegions().get(i).getlens().get(j)/mainset.getNormalizeMin();
                double val2 = comparedset.getRegions().get(i).getlens().get(j)/comparedset.getNormalizeMin();
                if(Math.abs(val1 - val2) <= LmkTriangulation.lenMinThresh) subset[0]++;
                subset[1]++;
            }
            results.add(subset);
        }
        return results;
    }
    
    private ArrayList<int[]> RgnRatioMax(){
        ArrayList<int[]> results = new ArrayList<int[]>(mainset.getRegions().size());
        for(int i = 0; i < mainset.getRegions().size(); i ++){
            int[] subset = {0, 0};
            for(int j = 0; j < mainset.getRegions().get(i).getlens().size(); j ++){
                double val1 = mainset.getRegions().get(i).getlens().get(j)/mainset.getNormalizeMax();
                double val2 = comparedset.getRegions().get(i).getlens().get(j)/comparedset.getNormalizeMax();
                if(Math.abs(val1 - val2) <= LmkTriangulation.lenMaxThresh) subset[0]++;
                subset[1]++;
            }
            results.add(subset);
        }
        return results;
    }
    
    private ArrayList<int[]> NeighborRgnAngle(){
        ArrayList<int[]> results = new ArrayList<int[]>(mainset.getNeighborAngles().size());
        for(int i = 0; i < mainset.getNeighborAngles().size(); i ++){
            int[] subset = {0, 0};
            for(int j = 0; j < mainset.getNeighborAngles().get(i).size(); j ++){
                double val1 = mainset.getNeighborAngles().get(i).get(j);
                double val2 = comparedset.getNeighborAngles().get(i).get(j);
                if(Math.abs(val1 - val2) <= LmkTriangulation.angNormThresh) subset[0]++;
                subset[1]++;
            }
            results.add(subset);
        }
        return results;
    }
    
    public LmkCompiler getMainSet(){
        return mainset;
    }
    
    public LmkCompiler getComparedSet(){
        return comparedset;
    }
    
    public ArrayList<Integer> getResults(){
        return finalset;
    }
}