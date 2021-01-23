/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lmk.triangulation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author rxiao
 */
public class TriMapReader {
    private final String filename, name, directory;
    private final ArrayList<Triangle> triangles;
    
    public TriMapReader(String directory) throws Exception{
        this.directory = directory;
        filename = new File(directory).getName();
        Pattern base = Pattern.compile("[^_ .]+");
        Matcher mth = base.matcher(filename.substring(0, filename.length()-4));
        if(mth.find()) name = mth.group();
        else name = filename.substring(0, filename.length()-4);
        triangles = new ArrayList<Triangle>();
        FileReader File = new FileReader(directory);
        BufferedReader in = new BufferedReader(File);
        String line = null;
        while((line = in.readLine()) != null) {
            String pt1, pt2, pt3;
            StringTokenizer linecon = new StringTokenizer(line);
            pt1 = linecon.nextToken();
            pt2 = linecon.nextToken();
            pt3 = linecon.nextToken();
            triangles.add(new Triangle(pt1, pt2, pt3));
        }
        in.close();
        File.close();
    }
    
    public ArrayList<Triangle> compileTriangles(){
        return triangles;
    }
    
    public String getName(){
        return name;
    }
    
    public String getDirectory(){
        return directory;
    }
}