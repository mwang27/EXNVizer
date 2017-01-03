/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mwang.exnparareal.ui.controller;

import java.awt.image.BufferedImage;
import org.mwang.exnparareal.ui.view.GanttChart;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tooltip;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import javax.imageio.ImageIO;
import org.mwang.exnparareal.model.PararealProfileDataFetcher;
import org.mwang.exnparareal.model.PararealProfileDataParser;
import org.mwang.exnparareal.model.PropagatorScheduleDataFilter;
import org.mwang.exnparareal.model.PropagatorScheduleDataFilter.PropSchedDataItem;
import org.mwang.exnparareal.ui.view.GanttChart.ExtraData;
import org.mwang.exnparareal.ui.view.TimeIntervalIndexNode;

/**
 *
 * @author mwang
 */
public class PropagatorScheduleTimelineViewController implements Initializable{

  @FXML
  private AnchorPane pararealTaskAPane;  
  @FXML
  private AnchorPane pararealTaskAPane1;  
  @FXML
  private AnchorPane pararealTaskAPane2; 
  @FXML
  private Button refreshPropSchedTimelineBtn;   
  @FXML
  private Button snapshotBtn;

  
  
//  private String homeDir = System.getProperty("user.home");
//  private String casePath = homeDir+"/Desktop/exnmpi1/exn-aero/models/vizerTest-046/";
  private String casePath;
  private int maxwin;
  private int nts;
  
  
  public void setCasePath(String casePath){
    this.casePath = casePath;
  }  
  
  
  
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    
    initPararealProfileGlobalInfo();
    initPropagatorScheduleView();
    
  }
  
  
  private void exportChartAsPng(GanttChart ExnChart){
    ExnChart.setAnimated(false);
    
    final FileChooser chartFileChooser = new FileChooser();
    chartFileChooser.setTitle("Export Chart As...");
    chartFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("png files (*.png)", "*.png"));
    chartFileChooser.setInitialDirectory(new File(System.getProperty("user.home")));   
    chartFileChooser.setInitialFileName("propOverlapGanttChart.png");
    
    final Alert imageSelAlert = new Alert(Alert.AlertType.INFORMATION);
    imageSelAlert.setHeaderText(null);
    
    final File chartFile = chartFileChooser.showSaveDialog(null);
    final WritableImage chartImage = ExnChart.snapshot(new SnapshotParameters(), null);
    final BufferedImage ExnChartImage = SwingFXUtils.fromFXImage(chartImage, null);
    
    if (chartFile != null) {
      try {
        ImageIO.write(ExnChartImage, "png", chartFile);
      } catch (IOException ex) {// never throw.
      } finally {
        imageSelAlert.setContentText(chartFile.getName()+" is saved.");
        imageSelAlert.showAndWait();
      }
    } else{
      imageSelAlert.setTitle("Chart is not saved!");
      imageSelAlert.setContentText("Please specify a valid name and path for saving the chart.");
      imageSelAlert.showAndWait();
    }

  }  
  

  /**
   * Propagator Schedule Timeline UI view
   */
  private void initPropagatorScheduleView(){
    

    final GanttChart pararealWinGanttChartAll = createGanttChart();
    pararealTaskAPane.getChildren().add(pararealWinGanttChartAll);
    setAnchorPaneConstrains(pararealWinGanttChartAll, 0, 0, 0, 0);
   
    
    final GanttChart pararealWinGanttChart1 = createGanttChart();
    pararealTaskAPane1.getChildren().add(pararealWinGanttChart1);
    setAnchorPaneConstrains(pararealWinGanttChart1, 0, 0, 0, 0);
    
    final GanttChart pararealWinGanttChart2 = createGanttChart();
    pararealTaskAPane2.getChildren().add(pararealWinGanttChart2);
    setAnchorPaneConstrains(pararealWinGanttChart2, 0, 0, 0, 0);
    
    
    refreshPropSchedTimelineBtn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        // multi-windows have multi-parsers: synchronize data fetching and parsing
        ObservableList<PararealProfileDataParser> parserObList = FXCollections.observableArrayList();
        
        // ensure target file exist before fetching data from it.
        String fileName;
        File pararealprofileWinFile;
        
        for (int i = 0; i < maxwin; i++) {
          fileName = "parareal_profile_win-"+(i+1)+".txt";
          pararealprofileWinFile = new File(casePath+fileName);
          if (pararealprofileWinFile.length() > 0) {
            
            parserObList.add(initDataFetcherParser(casePath, i+1));
            
            initPropagatorScheduleViewModel(pararealWinGanttChartAll, parserObList.get(i));
            if (i == 0) {
              initPropagatorScheduleViewModel(pararealWinGanttChart1, parserObList.get(0));
            }
            if (i == 1) {
              initPropagatorScheduleViewModel(pararealWinGanttChart2, parserObList.get(1));  
            }
          } else{
            alertFileExist(fileName);
          }
        }   
        
      }
    });    
  }
  
  private GanttChart createGanttChart(){

    final String[] propTasks = new String[nts+1];
    for (int i = 0; i < nts; i++) {
      String propString = "Prop_F_";
      propString = propString+(nts-i);
      propTasks[i] = propString;
    }
    propTasks[nts] = "Prop_C_0";
    
    // chart axis settings
    final NumberAxis xAxis = new NumberAxis();
    final CategoryAxis yAxis = new CategoryAxis();

    xAxis.setLabel("Time [s]");
    xAxis.setSide(Side.TOP);
    xAxis.setTickLabelFill(Color.CHOCOLATE);
    xAxis.setForceZeroInRange(false);

    yAxis.setLabel("Processros");
    yAxis.setTickLabelFill(Color.CHOCOLATE);
    yAxis.setTickLabelGap(10);
    yAxis.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(propTasks)));  

    final GanttChart<Number,String> chart = new GanttChart<>(xAxis,yAxis);
    chart.setAnimated(false);
    chart.setTitle("Propagator Overlap Gantt Chart");
    chart.getStylesheets().add(getClass().getResource("/org/mwang/exnparareal/resource/style/ganttChartDarkStyle.css").toExternalForm());
    chart.setLegendVisible(false);
    chart.setBlockHeight(15);     
    
    return chart;
  } 

  private void applyHighlightEvent(XYChart.Series series) {
    // Browsing through the Data and applying ToolTip as well as the class on hover.
    /***********************************************************************************
    * NOTE:
    * JavaFX Scene Graph is NOT designed for rendering millions of JavaFX nodes!
    * Each XYChart.Data<Number,Number> object has its JavaFX node, for large time-series 
    * data ploting this highlight should be disabled. 
    ************************************************************************************/
    final int size = series.getData().size();
    for (int i = 0; i < size; i++) {
      final XYChart.Data dataPoint = (XYChart.Data)series.getData().get(i);
      dataPoint.getNode().setOnMouseEntered(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
          //--set hover
          dataPoint.getNode().setCursor(Cursor.HAND);           
          //--install tooltip
          String propIndexString= dataPoint.getYValue().toString(); // "propF_#"
          String propIndexNum = propIndexString.substring(propIndexString.lastIndexOf("_")+1);
          final Tooltip tooltip = new Tooltip(
                  "propagator: " + propIndexString + "\n" +
                  "start_time: " + dataPoint.getXValue().toString() + "\n" + 
                  "duration: " + ((ExtraData)dataPoint.getExtraValue()).length
          );
          tooltip.getStyleClass().add("toolTipBorder"+propIndexNum);// Set tooltip color: .default-color#
          hackTooltipStartTiming(tooltip);// Set tooltip delay-time          
          Tooltip.install(dataPoint.getNode(), tooltip);
        }
      });

      dataPoint.getNode().setOnMouseExited(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
          dataPoint.getNode().setStyle("-fx-fill:grey;");
          dataPoint.getNode().setCursor(Cursor.HAND); 
          //Tooltip.uninstall(dataPoint.getNode(), tooltip);
        }
      });         
    }
    
    //System.out.println(((XYChart.Data<Number, Number>)series.getData().get(0)).getNode().getStyleClass());
    
    Runtime.getRuntime().gc();
    Runtime.getRuntime().freeMemory(); 
  }   
  
  private void hackTooltipStartTiming(Tooltip tooltip) {
    try {
      final Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
      fieldBehavior.setAccessible(true);
      final Object objBehavior = fieldBehavior.get(tooltip);

      final Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
      fieldTimer.setAccessible(true);
      final Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);

      objTimer.getKeyFrames().clear();
      objTimer.getKeyFrames().add(new KeyFrame(new Duration(20)));
      
    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
      Logger.getLogger(PropagatorScheduleTimelineViewController.class.getName()).log(Level.SEVERE, null, ex);
    }

  }   
  
  private void setAnchorPaneConstrains(XYChart chart, double top, double bottom, double left, double right){
    AnchorPane.setTopAnchor(chart, top);
    AnchorPane.setBottomAnchor(chart, bottom);
    AnchorPane.setLeftAnchor(chart, left);
    AnchorPane.setRightAnchor(chart, right);    
  }  
  
  
  
  
  /**
   * Feed data into view
   * @param chart
   * @param parser 
   */
  private void initPropagatorScheduleViewModel(GanttChart chart, PararealProfileDataParser parser){
    
    final List<String> styleStringList = new ArrayList<>();
    styleStringList.add("status-propC");
    for (int i = 1; i <= nts; i++) {
      String propString = "status-propF";
      propString = propString+i;
      styleStringList.add(propString);
    }
    
    // PropagatorScheduleDataFilter DataItem{Start_time; propIndex; duration}
    final ObservableList<PropSchedDataItem> coarsePropSchedObDataList = retrievePropSchedObDataList(parser, "Prop_C");
    final ObservableList<PropSchedDataItem> finePropSchedObDataList = retrievePropSchedObDataList(parser, "Prop_F");
    
    // coarse prop task series
    final XYChart.Series propCseries = new XYChart.Series();
    XYChart.Data dataC;
    for (Iterator<PropSchedDataItem> iteratorCoarse = coarsePropSchedObDataList.iterator(); iteratorCoarse.hasNext();) {
      PropSchedDataItem nextCoarse = iteratorCoarse.next();
      
      if (nextCoarse.getStart_time() > 0) {
        dataC = new XYChart.Data(
          nextCoarse.getStart_time(),
          "Prop_C_"+"0",
          new ExtraData(nextCoarse.getDuration(), "status-propC"));
        propCseries.getData().add(dataC);
      }
      
    }
    chart.getData().add(propCseries);
    
    // fine prop task series
    final ObservableList<XYChart.Series> propFseriesObList = FXCollections.observableArrayList();
    for (int i = 0; i < nts; i++) {
      propFseriesObList.addAll(new XYChart.Series<>());
    }
    
    int propIndex;
    XYChart.Data dataF;
    for (Iterator<PropSchedDataItem> iteratorFine = finePropSchedObDataList.iterator(); iteratorFine.hasNext();) {
      PropSchedDataItem nextFine = iteratorFine.next();
      
      if (nextFine.getStart_time() > 0) {
        propIndex = nextFine.getProp_index();
        dataF = new XYChart.Data(
          nextFine.getStart_time(),
          "Prop_F_"+propIndex,
          new ExtraData(nextFine.getDuration(), styleStringList.get(propIndex)));
        dataF.setNode(new TimeIntervalIndexNode(nextFine.getTs()));//
        
        propFseriesObList.get(propIndex-1).getData().add(dataF);
      }
    }
    chart.getData().addAll(propFseriesObList);
    
    snapshotBtn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        exportChartAsPng(chart);
      }
    });    
    
    chart.getData().forEach(new Consumer() {
      @Override
      public void accept(Object t) {
        applyHighlightEvent((XYChart.Series)t);
      }
    });
    
  } 

  private void updatePropagatorScheduleViewModel(GanttChart chart, PararealProfileDataParser parser){
    final Timeline tl = new Timeline();
  }
  
  
  /**
   * Propagator Schedule Timeline Data Model
   * @param parser
   * @param propType
   * @return 
   */
  private ObservableList<PropSchedDataItem> retrievePropSchedObDataList(PararealProfileDataParser parser, String propType){
    
    // data filter 2: PropagatorScheduleDataFilter DataItem{Start_time; propIndex; duration}
    final PropagatorScheduleDataFilter propScheFilter = new PropagatorScheduleDataFilter(parser); 
    
    ObservableList<PropSchedDataItem> propSchedObDataList = FXCollections.observableArrayList();
    if (propType == "Prop_C") {
      propSchedObDataList = propScheFilter.getCoarsePropSchedObDataList();
      //System.out.println("coarsePropSchedObDataList size: "+propScheFilter.getCoarsePropSchedObDataList().size());
    }
    if (propType == "Prop_F") {
      propSchedObDataList = propScheFilter.getFinePropSchedObDataList();
    }
    
    return propSchedObDataList;
  }
  
  private PararealProfileDataParser initDataFetcherParser(String casePath, int winIdx){
    PararealProfileDataParser parser = null;
    try {
      // data fetcher:
      final PararealProfileDataFetcher fetcher = new PararealProfileDataFetcher(casePath, winIdx);
      String profile_in_mem_data = fetcher.fetchProfileData();
      
      // data parser
      parser = new PararealProfileDataParser(1,profile_in_mem_data);
//      System.out.println("StarttimeDataObList_F size: "+parser.getStarttimeDataObList_F().size());
      
    } catch (IOException ex) {
      Logger.getLogger(PropagatorScheduleTimelineViewController.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    return parser;
  } 
  
  private void initPararealProfileGlobalInfo(){
    final PararealProfileDataParser parser = initDataFetcherParser(casePath, 1);
    //nit = (int)parser.getPararealProfileGlobalInfoObList().get(0);
    nts = (int)parser.getPararealProfileGlobalInfoObList().get(1);
    maxwin = (int)parser.getPararealProfileGlobalInfoObList().get(3);
  }  
  
  private void alertFileExist(String fileName){
    final Alert alert = new Alert(Alert.AlertType.INFORMATION, fileName+" not exist yet.", ButtonType.OK);
    alert.setHeaderText("File Not Exist.");
    alert.showAndWait();    
  }  
  
}
