/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package save;

import gui.NOCSimulatorGUI;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.util.Scanner;
import simulation.GUIConnection;
import simulation.Network;
import simulation.Point;
import simulation.Sender;

/**
 *
 * @author Suhail
 */
public class SaveFileReader {
    
    public int errorFlag;
    private static SaveFileReader reader;
    
    private SaveFileReader(){}
    
    public static SaveFileReader getInstance(){
        if (reader != null){
            return reader;
        }
        return new SaveFileReader();
    }
    
    public String readFile(File file) throws FileNotFoundException{
        
       StringBuilder fileContents = new StringBuilder((int)file.length());
       Scanner scanner = new Scanner(file);
       try {
           while(scanner.hasNextLine()) {        
               fileContents.append(scanner.nextLine());
           }
           return fileContents.toString();
       } finally {
           scanner.close();
       } 
    }
    
    public int buildNetwork(String spec){
        errorFlag = 0;
        String[] contents = spec.split(":");
        String size = contents[0];
        if (createNetwork(size) != 0){
            errorFlag = -1;
            return errorFlag;
        }
        if (contents.length > 1){
            errorFlag = addTransmissions(contents[1]);
            if (errorFlag != 0){
                return errorFlag;
            }
        }
        return 0;
    }
    
    private int createNetwork(String size){
        String[] rowCol = size.split(",");
        if (rowCol.length != 2){
            return -1;
        }
        NOCSimulatorGUI.connection.buildSimulator(rowCol[0], rowCol[1]);
        return 0;
    }
    
    private int addTransmissions(String trans){
        String[] tranArray = trans.split("-");
        int i = 0;
        while (i < tranArray.length){
            errorFlag = addTransmission(tranArray[i]);
            if (errorFlag != 0){
                return errorFlag;
            }
            i++;
        }
        return 0;
    }
    
    private int addTransmission(String tran){
        String[] contents = tran.split(",");
        if (contents.length != 5){
            return -2;
        }
        try {
            Point from = new Point(Integer.parseInt(contents[0]),Integer.parseInt(contents[1]));
            Point to = new Point(Integer.parseInt(contents[2]),Integer.parseInt(contents[3]));
            int freq = Integer.parseInt(contents[4]);
            NOCSimulatorGUI.connection.addSender(from, to, freq);
        }
        catch (NumberFormatException e) {
            return -3;
        }
        return 0;
    }
}
