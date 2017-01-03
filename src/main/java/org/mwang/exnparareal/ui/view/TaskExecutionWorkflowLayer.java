/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mwang.exnparareal.ui.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.paint.Color;

/**
 *
 * @author mwang
 */
public class TaskExecutionWorkflowLayer {
  
  // input
  private final int nts;
  private final int maxwin;
  private final int winIndex;
  
  // output
  private GanttChartVertical<String,Number> chart;

  
  public TaskExecutionWorkflowLayer(int nts, int maxwin, int winIndex) {
    this.nts = nts;
    this.maxwin = maxwin;
    this.winIndex = winIndex;
    initViewLayerChart();
  }
  
  
  private void initViewLayerChart(){
    
    // xAxis set, ts offset for different windows
    final int tsOffset = nts*(winIndex-1);
    final ObservableList<String> tsStringObList = FXCollections.observableArrayList();
    
    if (winIndex == 0) {// all windows
      for (int i = 0; i < nts*maxwin; i++) {
//        tsStringObList.add("ts"+(i+1));
        tsStringObList.add(""+(i+1));
      }
    }else{// single window
      for (int i = tsOffset; i < nts*winIndex; i++) {
//        tsStringObList.add("ts"+(i+1));
        tsStringObList.add(""+(i+1));
      }      
    }

    
    // chart axis settings
    final CategoryAxis xAxis = new CategoryAxis();
    final NumberAxis yAxis = new NumberAxis();

    xAxis.setLabel("Time Intervals");
    xAxis.setSide(Side.TOP);
    xAxis.setTickLabelFill(Color.CHOCOLATE);
    xAxis.setTickLabelGap(2);
    xAxis.setCategories(tsStringObList); 
    
    yAxis.setLabel("Runtime [s]");
    yAxis.setSide(Side.LEFT);
    yAxis.setTickLabelFill(Color.CHOCOLATE);
    yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis) {// inverse yAxis
        @Override
        public String toString(Number value) {
            // note we are printing minus value
            //return String.format("%7.1f", -value.doubleValue());
            return String.format("%10.0f", -value.doubleValue());
        }
    });
    if (winIndex > 1) {
      yAxis.setForceZeroInRange(false);
    }

    chart = new GanttChartVertical<>(xAxis,yAxis);
//    chart.setTitle("Parareal Solution Workflow Chart");
    chart.getStylesheets().add(getClass().getResource("/org/mwang/exnparareal/resource/style/ganttChartVerticalDarkStyle.css").toExternalForm());
    chart.setLegendVisible(false);
    if (winIndex == 0) {
      chart.setBlockWidth((45*8)/(maxwin*nts));  
    } else{
      chart.setBlockWidth((45*8)/nts);  
    }
    
  }  
  
  
  public GanttChartVertical getViewLayerChart(){
    return chart;
  }
  
}
