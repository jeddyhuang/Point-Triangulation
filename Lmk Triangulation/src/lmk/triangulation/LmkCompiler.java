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
    private final ArrayList<Vertex> landmarks;
    private final ArrayList<Triangle> triangleset;
    private ArrayList<ArrayList<Triangle>> neighbors;
    private ArrayList<ArrayList<Double>> nbrangles;
    private double normalizeMin, normalizeMax;
    
    public LmkCompiler(TriMapReader trimap, ObjReader sellmks) {
        name = sellmks.getName();
        triangleset = new ArrayList<>(trimap.compileTriangles().size());
        landmarks = sellmks.getVertices();
        for(Triangle triplet : trimap.compileTriangles()) triangleset.add(new Triangle(triplet.getv1(), triplet.getv2(), triplet.getv3(), sellmks.getVertices()));
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
        neighbors = new ArrayList<ArrayList<Triangle>>(triangleset.size());
        for(int i = 0; i < triangleset.size(); i ++){
            neighbors.add(new ArrayList<Triangle>());
            neighbors.get(i).add(triangleset.get(i));
            for(int j = 0; j < triangleset.size(); j ++){
                if(i != j) if(triangleset.get(i).neighbors(triangleset.get(j))) neighbors.get(i).add(triangleset.get(j));
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
    
    public ArrayList<Triangle> getTriangles(){
        return triangleset;
    }
    
    public ArrayList<ArrayList<Double>> getNeighborAngles(){
        return nbrangles;
    }
}