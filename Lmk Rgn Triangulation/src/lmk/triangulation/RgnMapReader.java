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
public class RgnMapReader{
    private final String filename, name, directory;
    private static ArrayList<Region> regions;
    
    public RgnMapReader(String directory) throws Exception{
        this.directory = directory;
        filename = new File(directory).getName();
        Pattern base = Pattern.compile("[^_ .]+");
        Matcher mth = base.matcher(filename.substring(0, filename.length()-4));
        if(mth.find()) name = mth.group();
        else name = filename.substring(0, filename.length()-4);
        regions = new ArrayList<Region>();
        FileReader File = new FileReader(directory);
        BufferedReader in = new BufferedReader(File);
        String line = null;
        while((line = in.readLine()) != null){
            if(line.charAt(0) != '#'){
                ArrayList<String> regionpts = new ArrayList<String>();
                StringTokenizer linecon = new StringTokenizer(line);
                if(linecon.countTokens() < 3) System.exit(0);
                while(linecon.hasMoreTokens()) regionpts.add(linecon.nextToken());
                regions.add(new Region(regionpts));
            }
        }
        in.close();
        File.close();
    }
    
    public ArrayList<Region> compileRegions(){
        return regions;
    }
    
    public String getName(){
        return name;
    }
    
    public String getDirectory(){
        return directory;
    }
}