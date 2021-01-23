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
        this.scores = new ArrayList<int[]>(this.mainset.getTriangles().size());
        this.finalset = new ArrayList<Integer>(this.mainset.getTriangles().size());
    }
    
    public void scoreTriSet(double simUpperThresh, double simLowerThresh){
        for(int[] subset : scores){
            double compscore = (double)(subset[0])/(double)(subset[1]);
            if(compscore >= simUpperThresh) this.finalset.add(2);
            else if(compscore >= simLowerThresh) this.finalset.add(1);
            else this.finalset.add(0);
        }
    }
    
    public void compareTriSet(double simAngleThresh, double simMinThresh, double simMaxThresh, double simNormThresh){
        ArrayList<int[]> compang = this.TriAngle(simAngleThresh);
        ArrayList<int[]> compmin = this.TriRatioMin(simMinThresh);
        ArrayList<int[]> compmax = this.TriRatioMax(simMaxThresh);
        ArrayList<int[]> compnorm = this.NeighborTriAngle(simNormThresh);
        
        for(int i = 0; i < mainset.getTriangles().size(); i ++){
            int[] subset = new int[2];
            subset[0] = compang.get(i)[0] + compmin.get(i)[0] + compmax.get(i)[0] + compnorm.get(i)[0];
            subset[1] = compang.get(i)[1] + compmin.get(i)[1] + compmax.get(i)[1] + compnorm.get(i)[1];
            scores.add(subset);
        }
    }
    
    private ArrayList<int[]> TriAngle(double threshhold){
        ArrayList<int[]> results = new ArrayList<int[]>(mainset.getTriangles().size());
        for(int i = 0; i < mainset.getTriangles().size(); i ++){
            int[] subset = {0, 0};
            double compval = Math.abs(mainset.getTriangles().get(i).getang1() - comparedset.getTriangles().get(i).getang1());
            if(compval <= threshhold) subset[0]++;
            subset[1]++;
            compval = Math.abs(mainset.getTriangles().get(i).getang2() - comparedset.getTriangles().get(i).getang2());
            if(compval <= threshhold) subset[0]++;
            subset[1]++;
            compval = Math.abs(mainset.getTriangles().get(i).getang3() - comparedset.getTriangles().get(i).getang3());
            if(compval <= threshhold) subset[0]++;
            subset[1]++;
            results.add(subset);
        }
        return results;
    }
    
    private ArrayList<int[]> TriRatioMin(double threshhold){
        ArrayList<int[]> results = new ArrayList<int[]>(mainset.getTriangles().size());
        for(int i = 0; i < mainset.getTriangles().size(); i ++){
            int[] subset = {0, 0};
            double compval = Math.abs((mainset.getTriangles().get(i).getlen1()/mainset.getNormalizeMin()) - (comparedset.getTriangles().get(i).getlen1()/comparedset.getNormalizeMin()));
            if(compval <= threshhold) subset[0]++;
            subset[1]++;
            compval = Math.abs((mainset.getTriangles().get(i).getlen2()/mainset.getNormalizeMin()) - (comparedset.getTriangles().get(i).getlen2()/comparedset.getNormalizeMin()));
            if(compval <= threshhold) subset[0]++;
            subset[1]++;
            compval = Math.abs((mainset.getTriangles().get(i).getlen3()/mainset.getNormalizeMin()) - (comparedset.getTriangles().get(i).getlen3()/comparedset.getNormalizeMin()));
            if(compval <= threshhold) subset[0]++;
            subset[1]++;
            results.add(subset);
        }
        return results;
    }
    
    private ArrayList<int[]> TriRatioMax(double threshhold){
        ArrayList<int[]> results = new ArrayList<int[]>(mainset.getTriangles().size());
        for(int i = 0; i < mainset.getTriangles().size(); i ++){
            int[] subset = {0, 0};
            double compval = Math.abs((mainset.getTriangles().get(i).getlen1()/mainset.getNormalizeMax()) - (comparedset.getTriangles().get(i).getlen1()/comparedset.getNormalizeMax()));
            if(compval <= threshhold) subset[0]++;
            subset[1]++;
            compval = Math.abs((mainset.getTriangles().get(i).getlen2()/mainset.getNormalizeMax()) - (comparedset.getTriangles().get(i).getlen2()/comparedset.getNormalizeMax()));
            if(compval <= threshhold) subset[0]++;
            subset[1]++;
            compval = Math.abs((mainset.getTriangles().get(i).getlen3()/mainset.getNormalizeMax()) - (comparedset.getTriangles().get(i).getlen3()/comparedset.getNormalizeMax()));
            if(compval <= threshhold) subset[0]++;
            subset[1]++;
            results.add(subset);
        }
        return results;
    }
    
    private ArrayList<int[]> NeighborTriAngle(double threshhold){
        ArrayList<int[]> results = new ArrayList<int[]>(mainset.getNeighborAngles().size());
        for(int i = 0; i < mainset.getNeighborAngles().size(); i ++){
            int[] subset = {0, 0};
            for(int j = 0; j < mainset.getNeighborAngles().get(i).size(); j ++){
                double compval = Math.abs(mainset.getNeighborAngles().get(i).get(j) - comparedset.getNeighborAngles().get(i).get(j));
                if(compval <= threshhold) subset[0]++;
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