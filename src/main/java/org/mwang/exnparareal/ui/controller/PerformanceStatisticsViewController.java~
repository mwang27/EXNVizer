/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mwang.exnparareal.ui.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import org.mwang.exnmonitor.helper.DragResizer;
import org.mwang.exnparareal.model.PararealProfileDataFetcher;
import org.mwang.exnparareal.model.PararealProfileDataParser;
import org.mwang.exnparareal.model.PerformanceStatisticsDataFilter;
import org.mwang.exnparareal.model.PerformanceStatisticsDataMiner;
import org.mwang.exnparareal.model.PerformanceStatisticsDataMiner.SpeedupDataItem;

/**
 *
 * @author mwang
 */
public class PerformanceStatisticsViewController implements Initializable{

  @FXML
  private AnchorPane performanceBarChartAPane;
  @FXML
  private ToggleButton speedupToggleBtn;
  @FXML
  private ToggleButton efficiencyToggleBtn;  

  
//  private String homeDir = System.getProperty("user.home");
//  private String casePath = homeDir+"/Desktop/exnmpi1/exn-aero/models/vizerTest-046/";
  private String casePath;
  private int maxwin;
  
  
  
  public void setCasePath(String casePath){
    this.casePath = casePath;
  }


  
  
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    
//    DragResizer.makeResizable(performanceBarChartAPane);
    initPararealProfileGlobalInfo();
    initPerformanceStatisticsBarChart();
    
  }
  


  
  
  /**
   * Performance Statistics UI view
   */
  private void initPerformanceStatisticsBarChart(){
    
    // chart axis settings
    final CategoryAxis xAxis = new CategoryAxis();
    final NumberAxis yAxis = new NumberAxis();
    final ObservableList<String> windowList = FXCollections.observableArrayList();
    windowList.add("Theoretical");
    for (int i = 0; i < maxwin; i++) {
      windowList.add("window-"+(i+1));
    }
    xAxis.setCategories(windowList);
    
    final BarChart<String, Number> performanceBarChart = new BarChart<>(xAxis,yAxis);
    DragResizer.makeResizable(performanceBarChart);
    performanceBarChart.setCategoryGap(130);
    performanceBarChart.getStylesheets().add(getClass().getResource("/org/mwang/exnparareal/resource/style/barChartDarkStyle.css").toExternalForm());
    performanceBarChart.setAnimated(true);
    performanceBarChart.setTitle("Parallel Performance of Parareal Solution");
    performanceBarChart.setLegendSide(Side.BOTTOM);

    
    speedupToggleBtn.selectedProperty().addListener(new ChangeListener<Boolean>() {
      @Override
      public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        if (newValue) {
          performanceBarChart.getData().add(initBarChartSeries("Speedup")); 
        }else{
          for (XYChart.Series<String, Number> series : performanceBarChart.getData()) {
            if (series.getName().equals("Speedup")) {
              performanceBarChart.getData().remove(series);
              break;
            }
          }
        }
      }
    });
    
    efficiencyToggleBtn.selectedProperty().addListener(new ChangeListener<Boolean>() {
      @Override
      public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        if (newValue) {
          performanceBarChart.getData().add(initBarChartSeries("Efficiency")); 
        }else{
          for (XYChart.Series<String, Number> series : performanceBarChart.getData()) {
            if (series.getName().equals("Efficiency")) {
              performanceBarChart.getData().remove(series);
              break;
            }
          }
        }
      }
    });    
       
    
    performanceBarChartAPane.getChildren().add(performanceBarChart);
    setAnchorPaneConstrains(performanceBarChart, 5.0, 5.0, 5.0, 5.0);
  }
  
  private void setAnchorPaneConstrains(XYChart chart, double top, double bottom, double left, double right){
    AnchorPane.setTopAnchor(chart, top);
    AnchorPane.setBottomAnchor(chart, bottom);
    AnchorPane.setLeftAnchor(chart, left);
    AnchorPane.setRightAnchor(chart, right);    
  }    
  



  
  /**
   * Feed Data into UI view
   * @param performanceBarChart 
   */
  private XYChart.Series<String, Number> initBarChartSeries(String seriesName){
    
    // create chart series
    final XYChart.Series<String, Number> series = new XYChart.Series<>();
    series.setName(seriesName);         

    // set series data
    int finishedWinTotal = 0;
    for (int i = 0; i < maxwin; i++) {
      if (checkFinishedWindow(casePath, (i+1))) {
        finishedWinTotal = finishedWinTotal + 1;
      }
    }
    
    if (finishedWinTotal > 0) {
      
      // speedup series
      series.getData().add(new XYChart.Data<>("Theoretical", 1.905));
      if (seriesName.equals("Speedup")) {
        final ObservableList<SpeedupDataItem> speedupYDataObList= retrieveSpeedupObDataList(finishedWinTotal);
        for (int i = 0; i < finishedWinTotal; i++) {
          series.getData().add(new XYChart.Data<>("window-"+(i+1), speedupYDataObList.get(i).getWinSpeedup()));
        }        
      } if (seriesName.equals("Efficiency")) {
        // efficiency series
        final ObservableList<SpeedupDataItem> efficiencyYDataObList= retrieveSpeedupObDataList(finishedWinTotal);
        for (int i = 0; i < finishedWinTotal; i++) {
          series.getData().add(new XYChart.Data<>("window-"+(i+1), 0.5));
        }        
      }

      
      
    } else{
      alertWindowFinish();
    }
   
    return series;
  }
  
  private void updateBarChartSeries(String seriesName){
    final Timeline tl = new Timeline();
    
  }

  
  
  
  
  /**
   * Performance Statistics Data Model
   * @return 
   */
  private ObservableList<SpeedupDataItem> retrieveSpeedupObDataList(int finishedWinTotal){
    
      // data filter 3: PerformanceStatisticsDataFilter DataItem{sequentialRuntime}
      PerformanceStatisticsDataFilter performanceStatisticsFilter = new PerformanceStatisticsDataFilter(casePath,finishedWinTotal); // finishedWinTotal = 2
      //System.out.println("sequentialRuntime window-1: "+performanceStatisticsFilter.getSequentialRuntimeObDataList().get(0));
      //System.out.println("winTotalSolverTime window-1: "+performanceStatisticsFilter.getWinTotalSoverTimeObDataList().get(0));
      //System.out.println("sequentialRuntime window-2 "+performanceStatisticsFilter.getSequentialRuntimeObDataList().get(1));
      //System.out.println("winTotalSolverTime window-2: "+performanceStatisticsFilter.getWinTotalSoverTimeObDataList().get(1));
      
      // data miner: PerformanceStatisticsDataMiner DataItem{winIdx; speedup}
      PerformanceStatisticsDataMiner performanceStatisticsMiner = new PerformanceStatisticsDataMiner(performanceStatisticsFilter);
      //System.out.println("speedup window-1: "+performanceStatisticsMiner.getSpeedupObDataList().get(0).getWinSpeedup());    
      //System.out.println("speedup window-2: "+performanceStatisticsMiner.getSpeedupObDataList().get(1).getWinSpeedup());    
    
      return performanceStatisticsMiner.getSpeedupObDataList();
  }
  
  private PararealProfileDataParser initDataFetcherParser(String casePath, int winIdx){
    // data fetcher:
    final PararealProfileDataFetcher fetcher = new PararealProfileDataFetcher(casePath, winIdx);
    String profile_in_mem_data = "";
    try {
      profile_in_mem_data = fetcher.fetchProfileData();
      //System.out.println("profile_in_mem_data :"+profile_in_mem_data.length());
    } catch (IOException ex) {
      Logger.getLogger(PropagatorScheduleTimelineViewController.class.getName()).log(Level.SEVERE, null, ex);
    }

    // data parser
    final PararealProfileDataParser parser = new PararealProfileDataParser(1,profile_in_mem_data);      
    //System.out.println("StarttimeDataObList_F size: "+parser.getStarttimeDataObList_F().size());
    
    return parser;
  }  
  
  private void initPararealProfileGlobalInfo(){
    final PararealProfileDataParser parser = initDataFetcherParser(casePath, 1);
    maxwin = (int)parser.getPararealProfileGlobalInfoObList().get(3);
  }   
  
  private boolean checkFinishedWindow(String casePath, int winIdx){
    boolean finishedWindow = false;
    
    final String fileName = "parareal_profile_win-"+winIdx+".txt";
    final File pararealprofileWinFile = new File(casePath+fileName);
    // ensure target file exist before fetching data from it.
    if (pararealprofileWinFile.length() > 0) {
      try {
        // data fetcher:
        PararealProfileDataFetcher fetcher = new PararealProfileDataFetcher(casePath, winIdx);
        String profile_in_mem_data = fetcher.fetchProfileData();
        // data parser
        PararealProfileDataParser parser = new PararealProfileDataParser(1,profile_in_mem_data);

        if (parser.getPercentageDataObList_C().get(parser.getPercentageDataObList_C().size()-1) == 100.00) {
          finishedWindow = true;
        }

      } catch (IOException ex) {
        Logger.getLogger(PerformanceStatisticsViewController.class.getName()).log(Level.SEVERE, null, ex);
      }      
    } 
    
    return finishedWindow;
  }
  
  private void alertWindowFinish(){
    final Alert alert = new Alert(Alert.AlertType.INFORMATION, "No parareal window finished yet.", ButtonType.OK);
    alert.setHeaderText("File Not Exist.");
    alert.showAndWait();    
  }    
  
}
