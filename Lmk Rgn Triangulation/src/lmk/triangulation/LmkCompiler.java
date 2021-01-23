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
public class LmkCompiler {
    private final String name;
    private ArrayList<Vertex> landmarks;
    private ArrayList<Region> regionset;
    private ArrayList<ArrayList<Region>> neighbors;
    private ArrayList<ArrayList<Double>> nbrangles;
    private double normalizeMin, normalizeMax;
    
    public LmkCompiler(RgnMapReader rgnmap, ObjReader sellmks) {
        name = sellmks.getName();
        regionset = new ArrayList<>(rgnmap.compileRegions().size());
        landmarks = sellmks.getVertices();
        for(Region rgn : rgnmap.compileRegions()) regionset.add(new Region(rgn.getvtxs(), sellmks.getVertices()));
        this.setMinMax();
        this.setNeighbors();
        this.setNbrAngles();
    }
    
    private void setMinMax(){
        normalizeMin = 9999;
        normalizeMax = 0;
        for(int i = 0; i < landmarks.size(); i ++){
            for(int j = i + 1; j < landmarks.size(); j ++){
                double dist = landmarks.get(i).calcDist(landmarks.get(j));
                if(dist < normalizeMin) normalizeMin = dist;
                if(dist > normalizeMax) normalizeMax = dist;
            }
        }
    }
    
    private void setNeighbors(){
        neighbors = new ArrayList<ArrayList<Region>>(regionset.size());
        for(int i = 0; i < regionset.size(); i ++){
            neighbors.add(new ArrayList<Region>());
            neighbors.get(i).add(regionset.get(i));
            for(int j = 0; j < regionset.size(); j ++){
                if(i != j) if(regionset.get(i).neighbors(regionset.get(j))) neighbors.get(i).add(regionset.get(j));
            }
        }
    }
    
    private void setNbrAngles(){
        nbrangles = new ArrayList<ArrayList<Double>>(neighbors.size());
        for(int i = 0; i < neighbors.size(); i ++){
            nbrangles.add(new ArrayList<Double>(neighbors.get(i).size()-1));
            for(int j = 1; j < neighbors.get(i).size(); j ++){
                nbrangles.get(i).add(neighbors.get(i).get(0).normAngles(neighbors.get(i).get(j)));
            }
        }
    }
    
    public String getName(){
        return name;
    }
    
    public double getNormalizeMax(){
        return normalizeMax;
    }
    
    public double getNormalizeMin(){
        return normalizeMin;
    }
    
    public ArrayList<Vertex> getLandmarks(){
        return landmarks;
    }
    
    public ArrayList<Region> getRegions(){
        return regionset;
    }
    
    public ArrayList<ArrayList<Double>> getNeighborAngles(){
        return nbrangles;
    }
}