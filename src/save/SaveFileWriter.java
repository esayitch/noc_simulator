/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package save;

import gui.NOCSimulatorGUI;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 *
 * @author Suhail
 */
public class SaveFileWriter {
    
    private static SaveFileWriter writer;
    private String data;
    
    private SaveFileWriter(){}
    
    public static SaveFileWriter getInstance(){
        if (writer != null){
            return writer;
        }
        return new SaveFileWriter();
    }
    
    public void writeToFile(File file) throws FileNotFoundException{
        data = NOCSimulatorGUI.connection.getNetworkData();
        PrintWriter printer = new PrintWriter(file);
        printer.print(data);
        printer.close();
    }
  
}
