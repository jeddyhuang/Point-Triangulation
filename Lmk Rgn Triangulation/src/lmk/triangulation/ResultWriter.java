/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lmk.triangulation;

import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author rxiao
 */
public class ResultWriter {
    private final String basedirectory;
    
    public ResultWriter(String directory, ScoreCompiler scoreset) throws Exception{
        String name1 = scoreset.getMainSet().getName();
        String name2 = scoreset.getComparedSet().getName();
        basedirectory = directory + "\\" + name1 + "v" + name2;
        this.setwriter(scoreset.getMainSet(), scoreset.getResults());
        this.setwriter(scoreset.getComparedSet(), scoreset.getResults());
    }
    
    private void setwriter(LmkCompiler pointset, ArrayList<Integer> resultset) throws Exception{
        if(resultset.contains(0)) this.finalwriter(pointset, resultset, 0);
        if(resultset.contains(1)) this.finalwriter(pointset, resultset, 1);
        if(resultset.contains(2)) this.finalwriter(pointset, resultset, 2);
    }
    
    private void finalwriter(LmkCompiler pointset, ArrayList<Integer> resultset, int bounds) throws Exception{
        String writerdir = basedirectory + " " + pointset.getName();
        if(bounds == 0) writerdir = writerdir + " Dissimilar.ply";
        if(bounds == 1) writerdir = writerdir + " Semisimilar.ply";
        if(bounds == 2) writerdir = writerdir + " Similar.ply";
        PrintWriter writer = new PrintWriter(writerdir, "UTF-8");
        writer.println("ply");
        writer.println("format ascii 1.0");
        writer.println("element vertex " + pointset.getLandmarks().size());
        writer.println("property float x");
        writer.println("property float y");
        writer.println("property float z");
        int faceset = 0;
        for(int result : resultset) if(result == bounds) faceset ++;
        writer.println("element face " + faceset);
        writer.println("property list uchar int vertex_index");
        writer.println("end_header");
        for(Vertex lmk : pointset.getLandmarks()) writer.println(lmk.getX() + " " + lmk.getY() + " " + lmk.getZ());
        for(int i = 0; i < pointset.getRegions().size(); i ++){
            if(resultset.get(i) == bounds){
                writer.print(pointset.getRegions().get(i).getids().size());
                for(int id : pointset.getRegions().get(i).getids()) writer.print(" " + id);
                writer.println();
            }
        }
        writer.close();
    }
}