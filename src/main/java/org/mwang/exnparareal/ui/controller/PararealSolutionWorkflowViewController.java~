/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mwang.exnparareal.ui.controller;

import com.jfoenix.controls.JFXButton;
import com.panemu.tiwulfx.control.DetachableTab;
import com.panemu.tiwulfx.control.DetachableTabPane;
import java.awt.image.BufferedImage;
import org.mwang.exnparareal.ui.view.TaskExecutionWorkflowLayer;
import org.mwang.exnparareal.ui.view.GanttChartVertical;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import javax.imageio.ImageIO;
import jfxtras.labs.scene.control.window.MinimizeIcon;
import jfxtras.labs.scene.control.window.Window;
import jfxtras.labs.scene.layout.ScalableContentPane;
import org.mwang.exnmonitor.ui.controller.EXNMonitorMainViewController;
import org.mwang.exnparareal.model.PararealProfileDataFetcher;
import org.mwang.exnparareal.model.PararealProfileDataParser;
import org.mwang.exnparareal.model.PararealWorkflowDataFilter;
import org.mwang.exnparareal.model.PararealWorkflowDataFilter.DepDataItem;
import org.mwang.exnparareal.model.PararealWorkflowDataFilter.TaskExeDataItem;
import org.mwang.exnparareal.ui.view.GanttChartVertical.ExtraData;
import org.mwang.exnparareal.ui.view.PropagatorIndexNode;

/**
 * @author mwang
 */
public class PararealSolutionWorkflowViewController implements Initializable{

  @FXML
  private ToolBar winBtnToolBar;
  @FXML
  private ToggleButton allinoneWinToggleBtn;
  @FXML
  private JFXButton snapshotBtn;
  @FXML
  private AnchorPane pararealWinAnchorPane;  

  private EXNMonitorMainViewController exnmonitorControllerGlobal;
  private DetachableTabPane detachEXNViewTabPane;
  private DetachableTab exnmonitorTabGlobal;
  private final String winStatusMark = "[finished]";  
  
  private String caseDirPath;
  private String casePath;
  private String caseName;
  private int maxwin;
  private int nit;
  private int nts;  
  
  public void setGlobalDetachEXNViewTabPane(DetachableTabPane detachEXNViewTabPane){
    this.detachEXNViewTabPane = detachEXNViewTabPane;
  }

  public void setEXNMonitorTabGlobal(DetachableTab exnmonitorTabGlobal) {
    this.exnmonitorTabGlobal = exnmonitorTabGlobal;
  }
  
  public void setEXNMonitorControllerGlobal(EXNMonitorMainViewController exnmonitorControllerGlobal) {
    this.exnmonitorControllerGlobal = exnmonitorControllerGlobal;
  }
  
  public void setCaseDirPath(String caseDirPath){
    this.caseDirPath = caseDirPath;
  } 
  
  public void setCaseName(String caseName){
    this.caseName = caseName;
  }  
  
  
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    casePath = caseDirPath+"/";
    //System.out.println("caseName from workflowViewController: "+caseName);
    
//    initEXNMonitorLocal();
  
    initPararealProfileGlobalInfo();
    
    initPararealWorkflowView();
    
  }
  
  
  private void exportChartAsPng(GanttChartVertical ExnChart){
    ExnChart.setAnimated(false);
    
    final FileChooser chartFileChooser = new FileChooser();
    chartFileChooser.setTitle("Export Chart As...");
    chartFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("png files (*.png)", "*.png"));
    chartFileChooser.setInitialDirectory(new File(System.getProperty("user.home")));   
    chartFileChooser.setInitialFileName("pararealWorkflowChart.png");
    
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

  
  //==============================================
  //* Parareal Solution Workflow Monitor UI view *
  //==============================================        
  /**
   * @param propTasks
   * @return 
   */
  private void initPararealWorkflowView(){
    
    createWinToggleBtnGroup();
    
  }
  
  private void createWinToggleBtnGroup(){
    
    for (int i = 0; i < maxwin; i++) {
      final ToggleButton winToggleBtn = new ToggleButton("win-"+(i+1));
      winToggleBtn.setStyle("-fx-text-fill: white;");
      winBtnToolBar.getItems().add(i+4, winToggleBtn);
      
      final int winIndex = i+1;
      final String fileName = "parareal_profile_win-"+winIndex+".txt";
      
      winToggleBtn.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
        if (newValue) {
          addSingleWorkflowWindow(fileName, winIndex);
        }
        if (!newValue){
          removeWorkflowWindow(winIndex);
          Runtime.getRuntime().gc();
          Runtime.getRuntime().freeMemory();          
        }
      });
    } 
    
    allinoneWinToggleBtn.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
      if (newValue) {
        addAllWorkflowWindow();
      }
      if (!newValue){
        removeWorkflowWindow(0);
        Runtime.getRuntime().gc();
        Runtime.getRuntime().freeMemory();
      }
    });
    allinoneWinToggleBtn.setSelected(true);
  }

  private void addSingleWorkflowWindow(String fileName, int winIndex){
    
    // create workflow window
    final StackPane workflowStackPane = createWorkflowWindow(winIndex);

    // create a vertical gantt chart as TaskExecutionWorkflowLayer in the window
    final TaskExecutionWorkflowLayer taskExeWorkflowViewLayer = new TaskExecutionWorkflowLayer(nts, maxwin, winIndex);
    final GanttChartVertical taskExeViewLayerChart = taskExeWorkflowViewLayer.getViewLayerChart();
    workflowStackPane.getChildren().add(taskExeViewLayerChart);          

    // get parser and init view model
    final File pararealprofileWinFile = new File(casePath+fileName);
    // ensure target file exist before fetching data from it.
    if (pararealprofileWinFile.length() > 0) {
      final PararealProfileDataParser parser = initDataFetcherParser(casePath, winIndex);
      initPararealWorkflowViewModel("single", taskExeViewLayerChart, parser, winIndex);
    } else{
      alertFileExist(fileName);
    }
    
  }
  
  private void addAllWorkflowWindow(){
    
    // create all-in-one view window
    final StackPane workflowStackPane = createWorkflowWindow(0);

    // create a vertical gantt chart as TaskExecutionWorkflowLayer in the window
    final TaskExecutionWorkflowLayer taskExeWorkflowViewLayer = new TaskExecutionWorkflowLayer(nts, maxwin, 0);
    final GanttChartVertical taskExeViewLayerChart = taskExeWorkflowViewLayer.getViewLayerChart();  
    workflowStackPane.getChildren().add(taskExeViewLayerChart); 
    
    // get parser and init view model
    for (int i = 0; i < maxwin; i++) {
      final String fileName = "parareal_profile_win-"+(i+1)+".txt";
      final File pararealprofileWinFile = new File(casePath+fileName);
      // ensure target file exist before fetching data from it.
      if (pararealprofileWinFile.length() > 0) {
        final PararealProfileDataParser parser = initDataFetcherParser(casePath, (i+1));
        initPararealWorkflowViewModel("all", taskExeViewLayerChart, parser, (i+1));
      } else{
        alertFileExist(fileName);
      }             
    }   
    
  }
  
  private void removeWorkflowWindow(int WinIndex){
    
    for (Node windowNode : pararealWinAnchorPane.getChildren()) {
      Window workflowWindow = (Window)windowNode;
      if (workflowWindow.getId().equals(String.valueOf(WinIndex))) {
        pararealWinAnchorPane.getChildren().remove(workflowWindow);
        break;
      }
    }    
    
  }
  
  private StackPane createWorkflowWindow(int winIndex){
    // create JFXtra Window
    final Window workflowWindow;
    if (winIndex == 0) {
      workflowWindow = new Window("Parareal Solution Workflow Chart: All Windows"+" "+winStatusMark);
//      workflowWindow = new Window("");
      workflowWindow.setMaxHeight(860);
      workflowWindow.setPrefSize(1500,860);
      workflowWindow.setLayoutX(20.0);
      workflowWindow.setLayoutY(15.0);
    } else{
      workflowWindow = new Window("Parareal Solution Workflow Chart: Window_"+winIndex+" "+winStatusMark);
//      workflowWindow = new Window("");
      workflowWindow.setMaxHeight(860);
      workflowWindow.setLayoutX(20+1050+40);
//      workflowWindow.setLayoutY(15+(winIndex-1)*(280+40));       
      workflowWindow.setLayoutY(15+(winIndex-1)*40);       
    }
    
    workflowWindow.setId(String.valueOf(winIndex));
    workflowWindow.setMovable(true);
    workflowWindow.setMinSize(650,380);    
    workflowWindow.setStyle("-fx-font-size:13px;-fx-background-color:#2C2E3D; -fx-border-color: rgba(128,128,128,0.3); -fx-border-radius:0;");
    workflowWindow.setTitleBarStyleClass("custom-window-titlebar"); 
    
    // add context menu control
    final MenuItem winDockMenuItem = new MenuItem("Dock this Window");
    final MenuItem winFloatMenuItem = new MenuItem("Float this Window");
    final ContextMenu win1ContextMenu = new ContextMenu(winDockMenuItem, winFloatMenuItem);
    workflowWindow.setContextMenu(win1ContextMenu);
    winDockMenuItem.setOnAction((ActionEvent event) -> {
      workflowWindow.setMovable(false);
    });
    winFloatMenuItem.setOnAction((ActionEvent event) -> {
      workflowWindow.setMovable(true);
    });     
    
    // add window title bar control
    final MinimizeIcon winMinimizeIcon = new MinimizeIcon(workflowWindow);
    workflowWindow.getLeftIcons().addAll(winMinimizeIcon);
    winMinimizeIcon.setOnAction((ActionEvent event) -> {
      workflowWindow.setPrefSize(650, 380);
    });
    
    // set ScalableContentPane
    final ScalableContentPane scalableContentPane = new ScalableContentPane();
    scalableContentPane.setStyle("-fx-border-color: transparent; -fx-background-color:transparent;"); 
//    scalableContentPane.autosize();
    workflowWindow.setContentPane(scalableContentPane);
    
    // add a stackpane to ScalableContentPane
    final StackPane workflowStackPane = new StackPane();
    scalableContentPane.getContentPane().getChildren().add(workflowStackPane);
    
    // append workflow window to pararealWinAnchorPane
    pararealWinAnchorPane.getChildren().add(workflowWindow); 
    
    return workflowStackPane;
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
//                  "propagator: " + propIndexString + "\n" +
                  "start_time: " + (-Double.parseDouble(dataPoint.getYValue().toString())) + "\n" + 
                  "duration: " + ((GanttChartVertical.ExtraData)dataPoint.getExtraValue()).height
          );
//          tooltip.getStyleClass().add("toolTipBorder"+propIndexNum);// Set tooltip color: .default-color#
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
  
  
  //=======================
  //* Feed Data into View *
  //=======================
  private void initPararealWorkflowViewModel(String winFlag, GanttChartVertical chart, PararealProfileDataParser parser, int winIndex){
    final List<String> styleStringList = Arrays.asList(
        "status-propC",
        "status-propF1","status-propF2","status-propF3","status-propF4",
        "status-propF5","status-propF6","status-propF7","status-propF8",
        "status-propF9","status-propF10","status-propF11","status-propF12",
        "status-propF13","status-propF14","status-propF15","status-propF16",
        "status-propF17","status-propF18","status-propF19","status-propF20");  
    
    final List<String> colorStringList = Arrays.asList(
        "rgb(56,69,92)",
        "#898FC6","#C9D37C","#57C4C4","#C6A2CD",
        "#A8C9E6","#F1D0A1","#8CCFA4","#E98CA2",
        "#0da777","#87ed89","#cfbfaa","#8faca6",
        "#42cdab","#acd1ce","#de5afe","#a5dcec",
        "#c4e781","#b05097","#fa8a2c","#ac8fbf");  
    
    
    
    // PropagatorScheduleDataFilter DataItem{ts; Start_time; (tsIndexFine, duration)}
    final ObservableList<TaskExeDataItem> coarseTaskExeDataObList = retrieveTaskExeObDataList(parser, "Prop_C");
    final ObservableList<TaskExeDataItem> fineTaskExeDataObList = retrieveTaskExeObDataList(parser, "Prop_F");

    
    // ts offset for different windows
    final int tsOffset = -nts*(winIndex-1);
    
    // ts series
    final ObservableList<XYChart.Series> tsseriesObList = FXCollections.observableArrayList();
    for (int i = 0; i < nts; i++) {
      tsseriesObList.addAll(new XYChart.Series<>());
    }    
    
    
    // fine prop task
    int tsIndexFine;
    XYChart.Data dataF;
    for (TaskExeDataItem nextFine : fineTaskExeDataObList) {
      if (nextFine.getStart_time() > 0) {
        tsIndexFine = nextFine.getTs();
        dataF = new XYChart.Data(
//                "ts"+tsIndexFine,
                ""+tsIndexFine,
                -nextFine.getStart_time(),
                new ExtraData(nextFine.getDuration(), styleStringList.get(nextFine.getProp_index()))
        );

        /**
         * parareal data dependency layer.
         */
//        if (winFlag.equals("single")) {
//          dataF.setNode(new DataDepWorkflowLayerNode(
//                  nextFine.getPercentage(), "fine", colorStringList.get(nextFine.getProp_index()), maxwin,
//                  exnmonitorControllerGlobal, detachEXNViewTabPane, exnmonitorTabGlobal, 
//                  caseName, caseDirPath, nextFine.getIt(), tsIndexFine)
//          );
//        }
        
        dataF.setNode(new PropagatorIndexNode(nextFine.getProp_index(), winFlag, maxwin));
        tsseriesObList.get(tsIndexFine-1+tsOffset).getData().add(dataF);        
      }
    }
    
    // coarse prop task
    int tsIndexCoarse;
    XYChart.Data dataC;
    for (TaskExeDataItem nextCoarse : coarseTaskExeDataObList) {
      if (nextCoarse.getStart_time() > 0) {
        tsIndexCoarse = nextCoarse.getTs();
        dataC = new XYChart.Data(
//                "ts"+tsIndexCoarse,
                ""+tsIndexCoarse,
                -nextCoarse.getStart_time(),
                new ExtraData(nextCoarse.getDuration(), "status-propC")
        );

        /**
         * parareal data dependency layer.
         */
//        if (winFlag.equals("single")) {
//          dataF.setNode(new DataDepWorkflowLayerNode(
//                  nextCoarse.getPercentage(), "coarse", "rgb(56,69,92)", maxwin,
//                  exnmonitorControllerGlobal, detachEXNViewTabPane, exnmonitorTabGlobal, 
//                  caseName, caseDirPath, nextCoarse.getIt(), tsIndexCoarse)
//          );
//        }
        tsseriesObList.get(tsIndexCoarse-1+tsOffset).getData().add(dataC);        
      }
    } 
    
    chart.getData().addAll(tsseriesObList);
    
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
  
  private void updatePararealWorkflowViewModel(GanttChartVertical chart, PararealProfileDataParser parser, int winIndex){
    
  }
  
  
  
  //=================================================
  //* Parareal Solution Workflow Monitor Data Model *
  //=================================================
  /**
   * @param parser
   * @param propType
   * @return 
   */
  private ObservableList<TaskExeDataItem> retrieveTaskExeObDataList(PararealProfileDataParser parser, String propType){
    
    // data filter 2: PropagatorScheduleDataFilter DataItem{Start_time; tsIndexFine; duration}
    final PararealWorkflowDataFilter workflowFilter = new PararealWorkflowDataFilter(parser); 
    
    ObservableList<TaskExeDataItem> taskExeObDataList = FXCollections.observableArrayList();
    if ("Prop_C".equals(propType)) {
      taskExeObDataList = workflowFilter.getCoarseTaskExeObDataList();
    }
    if ("Prop_F".equals(propType)) {
      taskExeObDataList = workflowFilter.getFineTaskExeObDataList();
    }
    
    return taskExeObDataList;
  }  
  
  private ObservableList<DepDataItem> retrieveDepObDataList(PararealProfileDataParser parser, String propType){
    
    // data filter 2: PropagatorScheduleDataFilter DataItem{Start_time; tsIndexFine; duration}
    final PararealWorkflowDataFilter workflowFilter = new PararealWorkflowDataFilter(parser); 
    
    ObservableList<DepDataItem> depObDataList = FXCollections.observableArrayList();
    if ("Prop_C".equals(propType)) {
      depObDataList = workflowFilter.getCoarseDepObDataList();
    }
    if ("Prop_F".equals(propType)) {
      depObDataList = workflowFilter.getFineDepObDataList();
    }
    
    return depObDataList;
  }   
  
  private PararealProfileDataParser initDataFetcherParser(String casePath, int winIdx){
    PararealProfileDataParser parser = null;
    try {
      // data fetcher:
      final PararealProfileDataFetcher fetcher = new PararealProfileDataFetcher(casePath, winIdx);
      String profile_in_mem_data = fetcher.fetchProfileData();
//      System.out.println("profile_in_mem_data :"+profile_in_mem_data.length());
      
      // data parser
      parser = new PararealProfileDataParser(1,profile_in_mem_data);
//      System.out.println(parser.getPararealProfileGlobalInfoObList().size());
      
    } catch (IOException ex) {
      Logger.getLogger(PararealSolutionWorkflowViewController.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    return parser;
  }  
 
  private void initPararealProfileGlobalInfo(){
    final PararealProfileDataParser parser = initDataFetcherParser(casePath, 1);
    nit = (int)parser.getPararealProfileGlobalInfoObList().get(0);
    nts = (int)parser.getPararealProfileGlobalInfoObList().get(1);
    maxwin = (int)parser.getPararealProfileGlobalInfoObList().get(3);
//    System.out.println("nit: "+nit);
  }
  
  private void alertFileExist(String fileName){
    final Alert alert = new Alert(Alert.AlertType.INFORMATION, fileName+" not exist yet.", ButtonType.OK);
    alert.setHeaderText("File Not Exist.");
    alert.showAndWait();    
  }
  
}
