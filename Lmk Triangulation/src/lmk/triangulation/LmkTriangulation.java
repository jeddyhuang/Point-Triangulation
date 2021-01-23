/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lmk.triangulation;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author rxiao
 */
public class LmkTriangulation {
    private static final double simAngleThresh = 3.33, simMinThresh = 1.33, simMaxThresh = 0.02, simNormThresh = 0.07, simUpperLim = 2./3., simLowerLim = 1./3.;
    private static final int threads = 96;
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        JFileChooser trianglemap = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        trianglemap.setDialogTitle("Select the Triangulation Map for Comparison");
        trianglemap.setAcceptAllFileFilterUsed(false);
        trianglemap.addChoosableFileFilter(new FileNameExtensionFilter(".txt Files", "txt"));
        if(trianglemap.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) System.exit(0);
        
        JFileChooser targetobj = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        targetobj.setDialogTitle("Select the Target .obj File to Compare to:");
        targetobj.setAcceptAllFileFilterUsed(false);
        targetobj.addChoosableFileFilter(new FileNameExtensionFilter(".obj Files", "obj"));
        if(targetobj.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) System.exit(0);
        
        JFileChooser selectedobj = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        selectedobj.setDialogTitle("Select the .obj Files to be Compared:");
        selectedobj.setMultiSelectionEnabled(true);
        selectedobj.setAcceptAllFileFilterUsed(false);
        selectedobj.addChoosableFileFilter(new FileNameExtensionFilter(".obj Files", "obj"));
        if(selectedobj.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) System.exit(0);
        
        JFileChooser targetfolder = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        targetfolder.setDialogTitle("Where to Save your File:");
        targetfolder.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if(targetfolder.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) System.exit(0);
        
        TriMapReader trimap = new TriMapReader(trianglemap.getSelectedFile().getPath());
        ObjReader trgtlmks = new ObjReader(targetobj.getSelectedFile().getPath());
        LmkCompiler trgtcom = new LmkCompiler(trimap, trgtlmks);
        
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        for(File file : selectedobj.getSelectedFiles()){
            executor.execute(()->{
                try {
                    ObjReader sellmks = new ObjReader(file.getPath());
                    LmkCompiler selcom = new LmkCompiler(trimap, sellmks);
                    ScoreCompiler scoreset = new ScoreCompiler(trgtcom, selcom);
                    scoreset.compareTriSet(simAngleThresh, simMinThresh, simMaxThresh, simNormThresh);
                    scoreset.scoreTriSet(simUpperLim, simLowerLim);
                    ResultWriter writer = new ResultWriter(targetfolder.getSelectedFile().getPath(), scoreset);
                } catch (Exception e){}
            });
        }
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        
        JOptionPane.showMessageDialog(null, "Comparisons Complete", "Complete", JOptionPane.INFORMATION_MESSAGE);
    }
}