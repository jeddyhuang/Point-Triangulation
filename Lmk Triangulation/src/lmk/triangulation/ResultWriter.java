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
        System.out.println(name1 + "v" + name2);
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
        if(bounds == 0) writerdir = writerdir + " Dissimilar.obj";
        if(bounds == 1) writerdir = writerdir + " Semisimilar.obj";
        if(bounds == 2) writerdir = writerdir + " Similar.obj";
        PrintWriter writer = new PrintWriter(writerdir, "UTF-8");
        for(Vertex lmk : pointset.getLandmarks()){
            writer.println("# " + lmk.getName());
            writer.println("v " + lmk.getX() + " " + lmk.getY() + " " + lmk.getZ());
        }
        for(int i = 0; i < pointset.getTriangles().size(); i ++){
            if(resultset.get(i) == bounds){
                writer.println("# " + pointset.getTriangles().get(i).getpt1().getName() + " " + pointset.getTriangles().get(i).getpt2().getName() + " " + pointset.getTriangles().get(i).getpt3().getName());
                writer.println("f " + pointset.getTriangles().get(i).getId1() + " " + pointset.getTriangles().get(i).getId2() + " " + pointset.getTriangles().get(i).getId3());
            }
        }
        writer.close();
    }
}