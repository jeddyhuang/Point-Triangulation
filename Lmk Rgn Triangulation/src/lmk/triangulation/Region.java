/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lmk.triangulation;

import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author rxiao
 */
public class Region{
    private final ArrayList<String> vertices;
    private ArrayList<Vertex> landmarks;
    private ArrayList<Integer> lmkindices;
    private ArrayList<Double> sidelens;
    private ArrayList<Double> angles;
    private ArrayList<Vertex> normals;
    private Vertex tnormal;
    
    public Region(ArrayList<String> vtxset){
        vertices = vtxset;
    }
    
    public Region(ArrayList<String> vtxset, ArrayList<Vertex> lmkset){
        this(vtxset);
        landmarks = new ArrayList<Vertex>(vertices.size());
        lmkindices = new ArrayList<Integer>(vertices.size());
        for(String name : vertices){
            for(int i = 0; i < lmkset.size(); i ++){
                if(lmkset.get(i).getName().equalsIgnoreCase(name)){
                    landmarks.add(lmkset.get(i));
                    lmkindices.add(i);
                }
            }
        }
        
        sidelens = new ArrayList<Double>(landmarks.size());
        for(int i = 0; i < landmarks.size()-1; i ++){
            sidelens.add(landmarks.get(i).calcDist(landmarks.get(i+1)));
        }
        sidelens.add(landmarks.get(landmarks.size()-1).calcDist(landmarks.get(0)));
        
        angles = new ArrayList<Double>(landmarks.size());
        angles.add(landmarks.get(0).calcAngle(landmarks.get(landmarks.size()-1), landmarks.get(1)));
        for(int i = 1; i < landmarks.size()-1; i ++){
            angles.add(landmarks.get(i).calcAngle(landmarks.get(i-1), landmarks.get(i+1)));
        }
        angles.add(landmarks.get(landmarks.size()-1).calcAngle(landmarks.get(landmarks.size()-2), landmarks.get(0)));
        
        normals = new ArrayList<Vertex>(landmarks.size());
        normals.add(this.generateNormal(landmarks.get(0), landmarks.get(landmarks.size()-1), landmarks.get(1)));
        for(int i = 1; i < landmarks.size()-1; i ++){
            normals.add(this.generateNormal(landmarks.get(i), landmarks.get(i-1), landmarks.get(i+1)));
        }
        normals.add(this.generateNormal(landmarks.get(landmarks.size()-1), landmarks.get(landmarks.size()-2), landmarks.get(0)));
        double xcoord = 0, ycoord = 0, zcoord = 0;
        for(Vertex norm : normals){
            xcoord += norm.getX();
            ycoord += norm.getY();
            zcoord += norm.getZ();
        }
        tnormal = new Vertex(xcoord/normals.size(), ycoord/normals.size(), zcoord/normals.size());
    }
    
    public boolean contains(Vertex otherlmk){
        for(Vertex lmk : landmarks) if(Objects.equals(lmk, otherlmk)) return true;
        return false;
    }
    
    public boolean neighbors(Region rgn){
        for(int i = 0; i < landmarks.size()-1; i ++){
            for(int j = 0; j < rgn.getlmks().size()-1; j ++){
                if(Objects.equals(landmarks.get(i), rgn.getlmks().get(j)) && Objects.equals(landmarks.get(i+1), rgn.getlmks().get(j+1))) return true;
                if(Objects.equals(landmarks.get(i), rgn.getlmks().get(j+1)) && Objects.equals(landmarks.get(i+1), rgn.getlmks().get(j))) return true;
            }
            if(Objects.equals(landmarks.get(i), rgn.getlmks().get(0)) && Objects.equals(landmarks.get(i+1), rgn.getlmks().get(rgn.getlmks().size()-1))) return true;
            if(Objects.equals(landmarks.get(i), rgn.getlmks().get(rgn.getlmks().size()-1)) && Objects.equals(landmarks.get(i+1), rgn.getlmks().get(0))) return true;
        }
        for(int j = 0; j < rgn.getlmks().size()-1; j ++){
            if(Objects.equals(landmarks.get(0), rgn.getlmks().get(j)) && Objects.equals(landmarks.get(landmarks.size()-1), rgn.getlmks().get(j+1))) return true;
            if(Objects.equals(landmarks.get(0), rgn.getlmks().get(j+1)) && Objects.equals(landmarks.get(landmarks.size()-1), rgn.getlmks().get(j))) return true;
        }
        if(Objects.equals(landmarks.get(0), rgn.getlmks().get(0)) && Objects.equals(landmarks.get(landmarks.size()-1), rgn.getlmks().get(rgn.getlmks().size()-1))) return true;
        if(Objects.equals(landmarks.get(0), rgn.getlmks().get(rgn.getlmks().size()-1)) && Objects.equals(landmarks.get(landmarks.size()-1), rgn.getlmks().get(0))) return true;
        return false;
    }
    
    public Vertex generateNormal(Vertex lmk1, Vertex lmk2, Vertex lmk3){
        double[] v1 = {lmk2.getX() - lmk1.getX(), lmk2.getY() - lmk1.getY(), lmk2.getZ() - lmk1.getZ()};
        double[] v2 = {lmk3.getX() - lmk1.getX(), lmk3.getY() - lmk1.getY(), lmk3.getZ() - lmk1.getZ()};
        return new Vertex(v1[1]*v2[2] - v1[2]*v2[1], v1[2]*v2[0] - v1[0]*v2[2], v1[0]*v2[1] - v1[1]*v2[0]);
    }
    
    public double normAngles(Region rgn){
        double angle = new Vertex(0,0,0).calcAngle(tnormal, rgn.getNormal());
        return angle;
    }
    
    public ArrayList<String> getvtxs(){
        return vertices;
    }
    
    public ArrayList<Vertex> getlmks(){
        return landmarks;
    }
    
    public ArrayList<Integer> getids(){
        return lmkindices;
    }
    
    public ArrayList<Double> getlens(){
        return sidelens;
    }
    
    public ArrayList<Double> getangs(){
        return angles;
    }
    
    public Vertex getNormal(){
        return tnormal;
    }
}