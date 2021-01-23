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
public class Triangle {
    private final String v1, v2, v3;
    private Vertex pt1, pt2, pt3;
    private int id1, id2, id3;
    private double A, B, C;
    private double a, b, c;
    private Vertex normal;
    
    public Triangle(String v1, String v2, String v3){
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }
    
    public Triangle(String v1, String v2, String v3, ArrayList<Vertex> landmarks){
        this(v1, v2, v3);
        for(int i = 0; i < landmarks.size(); i ++){
            if(landmarks.get(i).getName().equalsIgnoreCase(v1)){
                pt1 = landmarks.get(i);
                id1 = i + 1;
            }
            if(landmarks.get(i).getName().equalsIgnoreCase(v2)){
                pt2 = landmarks.get(i);
                id2 = i + 1;
            }
            if(landmarks.get(i).getName().equalsIgnoreCase(v3)){
                pt3 = landmarks.get(i);
                id3 = i + 1;
            }
        }
        a = pt1.calcDist(pt2);
        b = pt1.calcDist(pt3);
        c = pt2.calcDist(pt3);
        C = pt1.calcAngle(pt2, pt3);
        A = pt3.calcAngle(pt1, pt2);
        B = 180.00 - A - C;
        normal = this.generateNormal(pt1, pt2, pt3);
    }
    
    public boolean contains(Vertex lmk){
        if(Objects.equals(pt1, lmk)) return true;
        if(Objects.equals(pt2, lmk)) return true;
        if(Objects.equals(pt3, lmk)) return true;
        return false;
    }
    
    public boolean neighbors(Triangle t){
        if(this.contains(t.getpt1()) && this.contains(t.getpt2())) return true;
        if(this.contains(t.getpt1()) && this.contains(t.getpt3())) return true;
        if(this.contains(t.getpt2()) && this.contains(t.getpt3())) return true;
        return false; 
    }
    
    public Vertex generateNormal(Vertex lmk1, Vertex lmk2, Vertex lmk3){
        double[] v1 = {lmk2.getX() - lmk1.getX(), lmk2.getY() - lmk1.getY(), lmk2.getZ() - lmk1.getZ()};
        double[] v2 = {lmk3.getX() - lmk1.getX(), lmk3.getY() - lmk1.getY(), lmk3.getZ() - lmk1.getZ()};
        return new Vertex(v1[1]*v2[2] - v1[2]*v2[1], v1[2]*v2[0] - v1[0]*v2[2], v1[0]*v2[1] - v1[1]*v2[0]);
    }
    
    public double normAngles(Triangle triangle){
        return new Vertex(0,0,0).calcAngle(normal, triangle.getNormal());
    }
    
    public String getv1(){
        return v1;
    }
    
    public String getv2(){
        return v2;
    }
    
    public String getv3(){
        return v3;
    }
    
    public int getId1(){
        return id1;
    }
    
    public int getId2(){
        return id2;
    }
    
    public int getId3(){
        return id3;
    }
    
    public Vertex getpt1(){
        return pt1;
    }
    
    public Vertex getpt2(){
        return pt2;
    }
    
    public Vertex getpt3(){
        return pt3;
    }
    
    public double getlen1(){
        return a;
    }
    
    public double getlen2(){
        return b;
    }
    
    public double getlen3(){
        return c;
    }
    
    public double getang1(){
        return A;
    }
    
    public double getang2(){
        return B;
    }
    
    public double getang3(){
        return C;
    }
    
    public Vertex getNormal(){
        return normal;
    }
}