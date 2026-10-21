package org.diegobercian.ui;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import java.util.List;
import java.util.stream.Collectors;

/** Reflows real controls into one, two or three columns; no coordinate scaling. */
public final class ResponsiveGrid extends Pane {
    private static final double GAP=16;
    public ResponsiveGrid() {getStyleClass().add("responsive-grid");setMinWidth(0);widthProperty().addListener((o,a,b)->requestLayout());}
    public int columnsFor(double width) {return width>=940?3:width>=600?2:1;}
    private List<Node> visibleChildren() {return getChildren().stream().filter(Node::isManaged).collect(Collectors.toList());}
    private double rowHeight(List<Node> nodes,int start,int cols,double width) {
        double height=0;
        for(int i=start;i<Math.min(start+cols,nodes.size());i++)height=Math.max(height,nodes.get(i).prefHeight(width));
        return height;
    }
    @Override public javafx.geometry.Orientation getContentBias(){return javafx.geometry.Orientation.HORIZONTAL;}
    @Override protected double computeMinWidth(double height) {return 0;}
    @Override protected double computeMinHeight(double width){return computePrefHeight(width);}
    @Override protected double computePrefWidth(double height) {return 1000;}
    @Override protected double computePrefHeight(double width) {
        double available=width<0?Math.max(getWidth(),300):width;
        int cols=columnsFor(available);double cell=Math.max(0,(available-GAP*(cols-1))/cols),height=0;
        List<Node> nodes=visibleChildren();
        for(int i=0;i<nodes.size();i+=cols)height+=rowHeight(nodes,i,cols,cell)+(i==0?0:GAP);
        return height;
    }
    @Override protected void layoutChildren() {
        int cols=columnsFor(getWidth());double width=Math.max(0,(getWidth()-GAP*(cols-1))/cols),y=0;
        List<Node> nodes=visibleChildren();
        for(int row=0;row<nodes.size();row+=cols) {
            double height=rowHeight(nodes,row,cols,width);
            for(int column=0;column<cols && row+column<nodes.size();column++)nodes.get(row+column).resizeRelocate(column*(width+GAP),y,width,height);
            y+=height+GAP;
        }
    }
}
