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
public class Vertex implements Comparable{
    private int index;
    private final double x, y, z;
    //private boolean setbefore = false;
    private ArrayList<Integer> facetidxs;
    private String name;
    //private Vector normal;
    
    public Vertex(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vertex(double x, double y, double z, int index){
        this(x, y, z);
        this.index = index;
        facetidxs = new ArrayList<Integer>();
    }
    
    public Vertex closestVtx(ArrayList<Vertex> pointset){
        Vertex closest = null;
        double dist = Double.POSITIVE_INFINITY;
        for(Vertex vtx : pointset){
            if(Math.abs(x-vtx.getX()) < 1) if(Math.abs(y-vtx.getY()) < 1) if(Math.abs(z-vtx.getZ()) < 1){
                double newdist = this.calcDist(vtx);
                if(newdist < dist){
                    closest = vtx;
                    dist = newdist;
                }
            }
        }
        return closest;
    }
    
    public double calcAngle(Vertex vtx1, Vertex vtx2){
        double a = this.calcDist(vtx1);
        double b = this.calcDist(vtx2);
        double c = vtx1.calcDist(vtx2);
        double rad = Math.acos((a*a + b*b - c*c)/(2*a*b));
        return Math.toDegrees(rad);
    }
    
    public double calcDist(Vertex vtx){
        double xSqd = Math.pow((x - vtx.getX()), 2);
        double ySqd = Math.pow((y - vtx.getY()), 2);
        double zSqd = Math.pow((z - vtx.getZ()), 2);
        return Math.sqrt(xSqd + ySqd + zSqd);
    }
    
    /*public void setVector(Vector vector){
        if(!setbefore){
            normal = vector;
            setbefore = true;
        }
    }*/
    
    public void addFacetidx(int index){
        facetidxs.add(index);
    }
    
    public double getX(){
        return x;
    }
    
    public double getY(){
        return y;
    }
    
    public double getZ(){
        return z;
    }
    
    public int getIndex(){
        return index;
    }
    
    public ArrayList<Integer> getFacetidxs(){
        return facetidxs;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
    
    /*public Vector getVector(){
        return normal;
    }*/
    
    @Override
    public int compareTo(Object o){
        return this.name.compareTo(((Vertex) o).getName());
    }
}
