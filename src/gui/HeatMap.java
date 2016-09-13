/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import simulation.Results;

/**
 *
 * @author Suhail
 */
public class HeatMap {
    
    protected Results results;
    
    public HeatMap(Results res){
        results = res;
    }
    
    public void createAndShow(){
        Frame f = new JFrame("NOC Heat-Chart");
        JScrollPane scrollPane = new JScrollPane(new MapPanel(results));
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        f.add(scrollPane);
        f.pack();
        f.setVisible(true);
    }
    
   class MapPanel extends JPanel {
       
       int MAX_WINDOW_SIZE = 500;
       int MIN_TILE_SIZE = 20;
       int MAX_TILE_SIZE = 50;
       
       private int height;
       private int width;
       private double max;
       private Results results;
       
       public MapPanel(Results res){
           setBorder(BorderFactory.createLineBorder(Color.black));
           results = res;
           
           height = tileSize(results);
           width = height;
           iterateMax();
       }
       
       public Dimension getPreferredSize(){
           return new Dimension(results.size.y * height, results.size.x*width);
       }
       
       protected void paintComponent(Graphics g){
           super.paintComponent(g);
           
           for (int row = 0; row < results.size.x; row++){
               for (int col = 0; col < results.size.y; col++){
                    g.setColor(Color.BLACK);
                    g.drawRect(0+col*width,0+row*height,width,height);
                    float alpha = (float)results.traffic[row][col] / (float)max;
                    g.setColor(getCellColor(alpha));
                    g.fillRect(0+col*width,0+row*height,width,height);
               }
           }
       }
       
       protected Color getCellColor(float alpha){
           int r,g,b;
           
           r = (int)Math.ceil((-1 + alpha*2)*255); 
           b = (int)Math.ceil((1 - alpha*2)*255); 
           
           if (alpha < 0.5){
               g = 255 - b;
           }
           else {
               g = 255 - r;
           }
           
           if (r < 0){
               r = 0;
           }
           if (g < 0){
               g = 0;
           }
           if (b < 0){
               b = 0;
           }
           
           return new Color(r,g,b);
           /*alpha = 1 - alpha;
           float hue = alpha*0.67f;
           hue = 0.08f;
           System.out.println(hue);
           return Color.getHSBColor(hue, 150, 50);*/
       }
       
       protected void iterateMax(){
           max = 0;
           for (int row = 0; row < results.size.x; row++){
               for (int col = 0; col < results.size.y;col++){
                   if (results.traffic[row][col] > max){
                       max = results.traffic[row][col];
                   }
               }
           }
       }
       
       protected int tileSize(Results results){
           int decider = results.size.x;
           if (results.size.y > results.size.x){
               decider = results.size.y;
           }
           if (decider < MAX_WINDOW_SIZE/MIN_TILE_SIZE){
               return (int)Math.floor(MAX_WINDOW_SIZE/results.size.y);
           }
           return MIN_TILE_SIZE;
       }
       
       
   } 
}
