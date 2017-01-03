///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package org.mwang.exnparareal.ui.controller;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FilenameFilter;
//import java.io.IOException;
//import java.io.RandomAccessFile;
//import java.net.URL;
//import java.nio.ByteBuffer;
//import java.nio.channels.FileChannel;
//import java.nio.charset.Charset;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.ResourceBundle;
//import javafx.animation.Animation;
//import javafx.animation.KeyFrame;
//import javafx.animation.ScaleTransition;
//import javafx.animation.Timeline;
//import javafx.application.Platform;
//import javafx.beans.value.ChangeListener;
//import javafx.beans.value.ObservableValue;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.geometry.Pos;
//import javafx.scene.Cursor;
//import javafx.scene.Group;
//import javafx.scene.control.Button;
//import javafx.scene.control.ContextMenu;
//import javafx.scene.control.Label;
//import javafx.scene.control.MenuItem;
//import javafx.scene.control.ProgressIndicator;
//import javafx.scene.control.ToggleButton;
//import javafx.scene.control.ToolBar;
//import javafx.scene.control.Tooltip;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.layout.AnchorPane;
//import javafx.scene.layout.GridPane;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.StackPane;
//import javafx.scene.layout.VBox;
//import javafx.scene.paint.Color;
//import javafx.scene.paint.Paint;
//import javafx.scene.shape.Circle;
//import javafx.scene.shape.CubicCurve;
//import javafx.scene.shape.Line;
//import javafx.scene.shape.Polygon;
//import javafx.scene.shape.Rectangle;
//import javafx.scene.text.TextAlignment;
//import javafx.util.Duration;
//import javax.swing.SwingUtilities;
//import jfxtras.labs.scene.control.window.CloseIcon;
//import jfxtras.labs.scene.control.window.Window;
//import jfxtras.labs.scene.control.window.WindowIcon;
//import jfxtras.labs.scene.layout.ScalableContentPane;
//import org.mwang.exnmonitor.EXNMonitorTopComponent;
//import org.mwang.exnmonitor.ui.controller.EXNMonitorMainViewController;
//import org.openide.util.Exceptions;
//
///**
// *
// * @author mwang
// */
//public class PararealSolutionWorkflowViewController1 implements Initializable{
//
//  
//  /* general offset*/
//  private int totalOffcetX_Prop;
//  private int totalOffcetY_Prop;   
//  
//  private int totalOffcetX_BG;
//  private int totalOffcetY_BG;   
// 
//  /* variables for drawing propagators */
//  private int propX0;
//  private int propdeltaX;
//  private float propY0C;
//  private float propY0F;
//  private int propdeltaY;
//  private int propX;
//  private float propYC;
//  private float propYF;
//  
//  private int corrX0;
//  private int corrY0;
//  private int corrdeltaX;
//  private int corrdeltaY;
//  private int corrX;
//  private float corrY;
//  
//  
//  /* variables for fetching parareal profile data */
//
//  //private int winIdx = 1;
//  private int nit;
//  private int nts;
//  private int nprop; 
//  private int maxwin;
//  private float xtime;
//  private float xspace;
//  private float desblend;
//  private float wscblend;
//  
//  private int percent_idx_start;
//  private int percent_idx_end;  
//  private int start_time_idx_start;  
//  private int start_time_idx_end;  
//  private int duration_idx_start;
//  private int duration_idx_end;
//  private int prop_f_idx_start;
//  private int prop_f_idx_end;
//  
//  
//  /* variables for data dependency*/
//  // delta
//  private int dependDeltaX;
//  private int dependDeltaY;  
//  // coarse
//  private int dependStartX0;
//  private int dependStartY0;
//  private int dependEndX0;
//  private int dependEndY0;
//  // coarse offect
//  private int EndOffectXC;
//  private int EndOffectYC;
//  // fine
//  private int dependStartX0F;
//  private int dependStartY0F;
//  private int dependEndX0F;
//  private int dependEndY0F;
//  // corr_simple
//  private int corrStartX0;
//  private int corrStartY0;
//  private int corrEndX0C;
//  private int corrEndY0C;  
//  private int corrEndX0F;
//  private int corrEndY0F; 
//
//  @FXML
//  private AnchorPane pararealWinAnchorPane;  
//  @FXML
//  private Button startPararealView;  
//  @FXML
//  private Button stopPararealView;  
//  @FXML
//  private Label pararealMsgLabel;
//
//  @FXML
//  private GridPane pararealElementGridPane;
//
//  @FXML
//  private ToolBar winBtnToolBar;
//  @FXML
//  private ToggleButton allinoneWinToggleBtn;
//
//  private String winStatusMark;
//  private Timeline tl;   
//  
//  private final String color_c = "rgba(67,77,90,0.85)";
//  private final String color_f = "rgba(150,153,242,0.9)";
//  private final String color_corr = "rgba(114, 192, 244,0.8)";  
//  
//  private String caseDirPath;
//  private String caseName;
//  private String casePath;
//  private EXNMonitorTopComponent ExnMonitorTC;  
//  
//  
//  public void setCaseDirPath(String caseDirPath){
//    this.caseDirPath = caseDirPath;
//  }
//  
//  public void setCaseName(String CaseName){
//    this.caseName = CaseName;
//  }
//
//  
//  
//  @Override
//  public void initialize(URL location, ResourceBundle resources) {
//    maxwin = 2;
//    casePath = caseDirPath+"/";
//    // init ExnMonitor for current parareal case
//    SwingUtilities.invokeLater(new Runnable() {
//      @Override
//      public void run() {
//        ExnMonitorTC = new EXNMonitorTopComponent();// singleton mode
//      }
//    });    
//    
//    // init Toolbar actions
//    startPararealView.setDisable(false);
//    stopPararealView.setDisable(false);    
//    startPararealView.setOnAction(new EventHandler<ActionEvent>() {
//      @Override
//      public void handle(ActionEvent event) {
//        startPararealTimeline();
//      }
//    });
//    stopPararealView.setOnAction(new EventHandler<ActionEvent>() {
//      @Override
//      public void handle(ActionEvent event) {
//        stopPararealTimeline();
//      }
//    });
//
//    
//    initPararealSolutionWorkflowViewer();
//    createPararealElementColumns();    
//    
//    
//  }
//  
//  
//  
//  /* Timeline actions */
//  private void startPararealTimeline(){
//    startPararealView.setDisable(true);
//    stopPararealView.setDisable(false);    
//    pararealMsgLabel.setText("ExnParareal Monitor is running...");
//    pararealMsgLabel.setTextFill(Paint.valueOf("rgba(90,201,132,0.9)"));    
//    tl.play(); 
//  }
//  
//  private void stopPararealTimeline(){
//    startPararealView.setDisable(false);
//    stopPararealView.setDisable(true);     
//    pararealMsgLabel.setText("ExnParareal Monitor stopped.");
//    pararealMsgLabel.setTextFill(Paint.valueOf("rgba(211,136,106,0.9)"));    
//    tl.stop();
//    tl.getKeyFrames().clear();
//    Runtime.getRuntime().gc();
//    Runtime.getRuntime().freeMemory();    
//  }    
//  
//  
//  /**
//   * EXNParareal View#1: Parareal_Solution_Workflow_Monitor .
//   */
//  private void initPararealSolutionWorkflowViewer(){
//    initDrawGlobalVariables();
//    initPararealSolutionWorkflowGlobalInfo();
//    
//    for (int i = 0; i < maxwin; i++) {
//      final ToggleButton winToggleBtn = new ToggleButton("win-"+(i+1));
//      winToggleBtn.setStyle("-fx-text-fill: white;");
//      winBtnToolBar.getItems().add(i+4, winToggleBtn);
//      
//      final int winIndex = i+1;
//      final String fileName = "parareal_profile_win-"+winIndex+".txt";
//      
//      winToggleBtn.selectedProperty().addListener(new ChangeListener<Boolean>() {
//        @Override
//        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//          if (newValue) {
//            
//            final File pararealprofileWinFile = new File(casePath+fileName);
//
//            if (pararealprofileWinFile.exists()) {
//              
//              // create PropagatorList
//              ObservableList<ObservableList<ProgressIndicator>> propUpdateList = getPropagatorList(winIndex);
//              initPararealData(propUpdateList,winIndex);              
//              final List<Float> prop_f_percentage = (List<Float>)(getPropFineInfo(winIndex).get(0));
//              if (prop_f_percentage.get(prop_f_percentage.size()-1) == 100) {//finished window
//                winStatusMark = "Finished";
//                createPararealWindowEnlarge(winIndex);
//              }else{// not finished window
//                winStatusMark = "Running...";
//                createPararealWindowEnlarge(winIndex);
//                updatePararealDataTimeline(propUpdateList, winIndex);
//                startPararealTimeline();
//              }
//
//            }           
//          }
//        }
//      });
//      
//    }
//
//  }
//  
//  
//  private ObservableList getPropagatorList(int winIndex){
//    
//  final ObservableList<ObservableList> propUpdateList = FXCollections.observableArrayList();
//  final ObservableList<ProgressIndicator> propList_c = FXCollections.observableArrayList();
//  final ObservableList<ProgressIndicator> propList_f = FXCollections.observableArrayList();
//    
//    //-- coarse props
//    final int xOffset = (winIndex-1)*nts;
//    
//    for(int i=1; i<=nit; i++){
//      for(int j=i+xOffset; j<=nts*winIndex; j++){
//        // coarse propagators
//        drawPropCoarse(i,j-xOffset,xOffset,propList_c,winIndex);         
//      }
//    }
//    
//    //-- extra iteration for coarse props
//    for(int k=nit+1+xOffset; k<=nts*winIndex; k++){
//      drawPropCoarse(nit+1,k-xOffset,xOffset,propList_c,winIndex);
//    }
//    
//    //-- fine props
//    for(int i=1; i<=nit; i++){
//      for(int j=i+xOffset; j<=nts*winIndex; j++){
//        drawPropFine(i,j-xOffset,xOffset,propList_f, winIndex); // prop_f
//      }
//    } 
//    
//    propUpdateList.addAll(propList_c,propList_f);
//    
//    return propUpdateList;
//  }   
//
//  
//  private ObservableList initPararealWindowControl(int winIndex){
//    // create parareal window UI component
//    final AnchorPane propPane = new AnchorPane();
//    final Window pararealWindowThumbnail = new Window("Parareal Window_"+winIndex+": "+winStatusMark); 
//    
//    final WindowIcon maxmizeIcon = new WindowIcon();
//    pararealWindowThumbnail.getLeftIcons().addAll(maxmizeIcon);
//    maxmizeIcon.setOnAction(new EventHandler<ActionEvent>() {
//      @Override
//      public void handle(ActionEvent event) {
//        final ScaleTransition st = new ScaleTransition(Duration.millis(600), createPararealWindowEnlarge(winIndex));
//        st.setFromX(0.0);
//        st.setFromY(0.0);
//        st.setToX(1.0);
//        st.setToY(1.0);
//        st.setCycleCount(1);
//        st.play();
//        Runtime.getRuntime().gc();
//        Runtime.getRuntime().freeMemory();        
//      }
//    });    
//    
//    // create PropagatorList
//    final ObservableList<ObservableList<ProgressIndicator>> propUpdateList = createPropagatorList(propPane,winIndex);
//    initPararealData(propUpdateList,winIndex);
//    
//    return propUpdateList;
//  } 
//  
//  private Window createPararealWindowEnlarge(int winIndex){
//    //--1--create parareal window UI component
//    final AnchorPane propPane = new AnchorPane();
//    final Window pararealWindowLarge = new Window("Parareal Window_"+winIndex+": "+winStatusMark);
//    pararealWinAnchorPane.getChildren().add(pararealWindowLarge);  
//    pararealWindowLarge.setMovable(false);
//    pararealWindowLarge.setLayoutX(20.0);
//    pararealWindowLarge.setLayoutY(15.0);
//    
//    pararealWindowLarge.setMinSize(560,300);
//    //pararealWindowLarge.setMaxSize(1050,535);
//    
//    pararealWindowLarge.setStyle("-fx-font-size:13px;-fx-background-color:#2C2E3D; -fx-border-color: rgba(128,128,128,0.3); -fx-border-radius:0;");
//    pararealWindowLarge.setTitleBarStyleClass("custom-window-titlebar");    
//    
//    ScalableContentPane scalableContentPane = new ScalableContentPane();
//    scalableContentPane.setStyle("-fx-border-color: transparent; -fx-background-color:transparent;");
//    scalableContentPane.getContentPane().getChildren().add(propPane);
//    scalableContentPane.autosize();
//    pararealWindowLarge.setContentPane(scalableContentPane);   
//    
//    final MenuItem win1DockMenuItem = new MenuItem("Dock this Window");
//    final MenuItem win1FloatMenuItem = new MenuItem("Float this Window");
//    final ContextMenu win1ContextMenu = new ContextMenu(win1DockMenuItem, win1FloatMenuItem);
//    pararealWindowLarge.setContextMenu(win1ContextMenu);
//    win1DockMenuItem.setOnAction(new EventHandler<ActionEvent>() {
//      @Override
//      public void handle(ActionEvent event) {
//        pararealWindowLarge.setMovable(false);
//      }
//    });
//    win1FloatMenuItem.setOnAction(new EventHandler<ActionEvent>() {
//      @Override
//      public void handle(ActionEvent event) {
//        pararealWindowLarge.setMovable(true);
//      }
//    }); 
//    
//    final CloseIcon closeWindowIcon = new CloseIcon(pararealWindowLarge);
//    pararealWindowLarge.getLeftIcons().addAll(closeWindowIcon);
//    closeWindowIcon.setOnAction(new EventHandler<ActionEvent>() {
//      @Override
//      public void handle(ActionEvent event) {
//        pararealWinAnchorPane.getChildren().remove(pararealWindowLarge); 
//        Runtime.getRuntime().gc();
//        Runtime.getRuntime().freeMemory();         
//      }
//    }); 
//    
//    //--2--create parareal window axises
//    createTimeslabHBox(propPane,winIndex);//create timeslab horizontal Axis
//    createIterationVBox(propPane,winIndex);//create iteration vertical Axis   
//    
//    //--3--create propagator background rectangles
//    createPropBackgorundList(propPane,winIndex);   
//    
//    //--4--create PropagatorList
//    final ObservableList<ObservableList<ProgressIndicator>> propUpdateList = createPropagatorList(propPane,winIndex); 
//    
//    initPararealData(propUpdateList,winIndex);
//    
//    return pararealWindowLarge;    
//  } 
//  
//  
//  
//  
//  
//  private ObservableList createPropagatorList(AnchorPane propPane, int winIndex){
//    
//  final ObservableList<ObservableList> propUpdateList = FXCollections.observableArrayList();
//  final ObservableList<ProgressIndicator> propList_c = FXCollections.observableArrayList();//////shoule be local var///////
//  final ObservableList<ProgressIndicator> propList_f = FXCollections.observableArrayList();//////shoule be local var///////
//  final ObservableList<Polygon> corrList = FXCollections.observableArrayList();//////shoule be local var/////// 
//    
//    // draw propagators with arrowline showing data dependencies in a parareal CFD run.
//    //-- coarse props
//    final int xOffset = (winIndex-1)*nts;
//    final int itOffset = (nit+1)*(winIndex-1);
//    
//    for(int i=1; i<=nit; i++){
//      
//      for(int j=i+xOffset; j<=nts*winIndex; j++){
//        // coarse propagators
//        propPane.getChildren().add(drawPropCoarse(i,j-xOffset,xOffset,propList_c,winIndex));
//        // edge dependency
//        if ((i+xOffset)==j) { 
//          propPane.getChildren().add(drawDataDependencyCoarse(i, j-xOffset, winIndex));
//        }
//        // internal dependency
//        if ((i+xOffset)<j && j<winIndex*nts) { 
//          propPane.getChildren().add(drawDataDependencyCoarseAddition(i, j-xOffset, winIndex));
//        }
//        // end dependency in second_last iteration
//        if((i==nit) && (j==winIndex*nts)){
//          propPane.getChildren().add(drawDataDependencyCoarseAdditionEnd(i, j-xOffset, winIndex));
//        }
//        // end dependency in last iteration
//        if((i>nit) && (j==winIndex*nts)){
//          propPane.getChildren().add(drawDataDependencyCoarse(i, j-xOffset, winIndex));
//        }          
//      }
//      
//    }
//    
//    //-- extra iteration for coarse props
//    for(int k=nit+1+xOffset; k<=nts*winIndex; k++){
//      propPane.getChildren().add(drawPropCoarse(nit+1,k-xOffset,xOffset,propList_c,winIndex));
//      propPane.getChildren().add(drawDataDependencyCoarse(nit+1, k-xOffset, winIndex));
//    }
//    
//    //-- fine props
//    final List<Float> prop_f_percentage = (List<Float>)(getPropFineInfo(winIndex).get(0));
//    for(int i=1; i<=nit; i++){
//      for(int j=i+xOffset; j<=nts*winIndex; j++){
//        propPane.getChildren().add(drawPropFine(i,j-xOffset,xOffset,propList_f, winIndex)); // prop_f
//        if (!(i<nit && j==nts*winIndex)) {
//          if (j == (i+nts*(winIndex-1))) {
//            propPane.getChildren().add(drawDataDependencyFineEdge(i,j-xOffset, winIndex));// line_f_edge
//          } else{
//            propPane.getChildren().add(drawDataDependencyFine(i,j-xOffset,winIndex));// line_f
//          }         
//        }
//      }
//    } 
//    
//    //-- correctors
//    for(int k=1+xOffset; k<=(nts-1)+(winIndex-1)*nts; k++){// iteration 1
//      propPane.getChildren().add(drawSimpleBlender(1, k-xOffset,corrList,winIndex));
//      propPane.getChildren().add(drawDataPassingCorrector(1,k-xOffset,color_c,color_f, winIndex));
//    }
//    for(int i=2; i<=nit+1; i++){
//      for(int j=i-1+xOffset; j<=(nts-1)+(winIndex-1)*nts; j++){
//        
//        if ( (j+1) == (i+(winIndex-1)*nts) ) {//edge corr_simple
//          propPane.getChildren().add(drawSimpleBlender(i, j-xOffset,corrList,winIndex));
//          if (i==nit+1) {// last iteration
//            propPane.getChildren().add(drawDataPassingCorrectorExtraIt(i,j-xOffset,color_c,winIndex));
//          } else{
//            propPane.getChildren().add(drawDataPassingCorrector(i,j-xOffset,color_c,color_f, winIndex)); 
//          }           
//        } else{//inner corr_simple
//          propPane.getChildren().add(drawComplexBlender(i, j-xOffset,corrList,winIndex));
//          if (i==nit+1) {// last iteration
//            propPane.getChildren().add(drawDataPassingCorrectorExtraIt(i,j-xOffset,color_c,winIndex));
//          } else{
//            propPane.getChildren().add(drawDataPassingCorrector(i,j-xOffset,color_c,color_f, winIndex)); 
//          }           
//        }
//        
//      }
//    }
//    propPane.getChildren().add(drawComplexBlender(nit+1, nts*winIndex-xOffset, corrList,winIndex));// cross window corr_simple
////    propPane.getChildren().add(drawCrossWinDependency(nit+1, nts*winIdx));// cross window data dependency
//    
//    propUpdateList.addAll(propList_c,propList_f);
//    
//    return propUpdateList;
//  }    
//  
//  
//  private void initPararealData(ObservableList<ObservableList<ProgressIndicator>> propUpdateList, int winIndex){
//    /**-1- Fetch propagator data. */
//    final List<Float> prop_c_percentage = (List<Float>)(getPropCoarseInfo(winIndex).get(0));
//    final List<Float> prop_f_percentage = (List<Float>)(getPropFineInfo(winIndex).get(0));
//
//
//    /**-2- Update Percentage info. */
//    System.out.println("current window: "+winIndex);
//    //--update coarse percentage data
//    for (int i=0; i<propUpdateList.get(0).size(); i++) {
//      ((ProgressIndicator)propUpdateList.get(0).get(i)).progressProperty().set(prop_c_percentage.get(i)/100);
//    }
//    //--update fine percentage data
//    for (int i=0; i<propUpdateList.get(1).size(); i++) {
//      ((ProgressIndicator)propUpdateList.get(1).get(i)).progressProperty().set(prop_f_percentage.get(i)/100);
//    } 
//    
//  }
//  
//  private void updatePararealDataTimeline(ObservableList<ObservableList<ProgressIndicator>> propUpdateList, int winIndex){
//    pararealMsgLabel.setTextFill(Paint.valueOf("rgb(0,183,150)")); 
//    
//    final KeyFrame kf = new KeyFrame(Duration.millis(3000), new EventHandler<ActionEvent>() {
//      @Override
//      public void handle(ActionEvent actionEvent) {
//        /**-1- Fetch propagator data. */
//        final List<Float> prop_c_percentage = (List<Float>)(getPropCoarseInfo(winIndex).get(0));
//        final List<Float> prop_f_percentage = (List<Float>)(getPropFineInfo(winIndex).get(0));
//
//        
//        /**-2- Update Percentage info. */
//        System.out.println("current window: "+winIndex);        
//        //--update coarse percentage data
//        for (int i=0; i<prop_c_percentage.size(); i++) {
//          ((ProgressIndicator)propUpdateList.get(0).get(i)).progressProperty().set(prop_c_percentage.get(i)/100);
//        }
//        //--update fine percentage data
//        for (int i=0; i<prop_f_percentage.size(); i++) {
//          ((ProgressIndicator)propUpdateList.get(1).get(i)).progressProperty().set(prop_f_percentage.get(i)/100);
//        }
//        
//        /**-3- Update Temporal-overlap info with 'start time' and 'duration' */
//        
//
//        
//        /**-4- Stop/Create next window when previous PararealWindow is finished. */
//        if (prop_f_percentage.get(prop_f_percentage.size()-1) == 100) {
//          if (winIndex == maxwin) {
//            stopPararealTimeline();
//          } else{
//            final int updateWinIndex = winIndex+1;
//            final String fileName = "parareal_profile_win-"+String.valueOf(updateWinIndex)+".txt";
//            final File pararealWinFile = new File(casePath+fileName);
//            if (pararealWinFile.exists()) {
//              refreshPropUpdateList(updateWinIndex,propUpdateList);
//              startPararealTimeline(); 
//            } else {
//              startPararealTimeline();  
//            }  
//          }
//        }
//
//      }
//    });
//    
//    tl = new Timeline(kf); 
//    tl.setCycleCount(Animation.INDEFINITE);
//    
//    Runtime.getRuntime().gc();
//    Runtime.getRuntime().freeMemory();    
//  }  
//  
//  private void refreshPropUpdateList(int updateWinIndex, ObservableList<ObservableList<ProgressIndicator>> propUpdateList){
//    final ObservableList winAndPropList = (ObservableList)initPararealWindowControl(updateWinIndex);
//    final Window pararealWindowThumbnail = (Window)winAndPropList.get(0);
//    final ObservableList<ObservableList<ProgressIndicator>> newPropList = (ObservableList<ObservableList<ProgressIndicator>>)winAndPropList.get(1);
//    propUpdateList.clear();
//    propUpdateList.addAll(newPropList.get(0),newPropList.get(1));
//  }  
//  
//  
//  private HBox createTimeslabHBox(AnchorPane propPane, int winIdx){
//    final HBox tsHBox = new HBox();
//    if (winIdx >1) {
//      tsHBox.setSpacing(125);
//    } else{
//      tsHBox.setSpacing(133);
//    }
//    
//    tsHBox.setPrefSize(1338,35);
//    tsHBox.setAlignment(Pos.CENTER_LEFT);
//    tsHBox.setStyle("-fx-padding:0 0 0 77;-fx-background-color:#434d5a; -fx-border-color:transparent transparent whitesmoke transparent; -fx-opacity:1.0; -fx-background-radius:5px;");
//    Label tsLabel;
//    for (int i = 1; i <= nts; i++) {
//      tsLabel = new Label("ts_"+String.valueOf(i+nts*(winIdx-1)));
//      tsLabel.setStyle("-fx-font-size:14px; -fx-font-weight: bold; -fx-text-fill:whitesmoke; -fx-background-color: rgb(5, 5, 5),rgb(87, 89, 92),linear-gradient(rgba(56,65,71,0.3),rgba(56,65,71,0.3),rgba(56,65,71,0.3));");
//      tsHBox.getChildren().add(tsLabel);
//    }
//    AnchorPane.setTopAnchor(tsHBox, -60.0+totalOffcetY_BG);
//    AnchorPane.setLeftAnchor(tsHBox, 25.0+totalOffcetX_BG);    
//    propPane.getChildren().add(tsHBox);   
//    
//    return tsHBox;
//  }
//  
//  private VBox createIterationVBox(AnchorPane propPane, int winIdx){
//    final VBox itVBox = new VBox(40);
//    itVBox.setPrefHeight(720);
//    itVBox.setStyle("-fx-padding:0 4 0 4; -fx-background-color:#434d5a; -fx-border-width:2px; ;-fx-border-color:transparent whitesmoke transparent transparent; -fx-background-radius:5; -fx-opacity:0.8;");
//    
//    Label itLabel;
//    if (winIdx == 1) {
//      for (int i = 0; i < 17; i++) {
//        itLabel = new Label(i*60+"-");
//        itLabel.setTextAlignment(TextAlignment.RIGHT);
//        itLabel.setStyle("-fx-text-fill:whitesmoke; ;-fx-font-size:14px; -fx-font-weight:Bold; -fx-background-color: rgb(5, 5, 5),rgb(87, 89, 92),linear-gradient(rgba(56,65,71,0.3),rgba(56,65,71,0.3),rgba(56,65,71,0.3));");
//        itVBox.getChildren().add(itLabel);
//      } 
//      AnchorPane.setLeftAnchor(itVBox, 25.0);
//    } else{
//      final List<Float> prop_c_Start_time = (List<Float>)(getPropCoarseInfo(winIdx).get(3));
//      int prop_f_start_offsetY =  (60*( (int)(prop_c_Start_time.get(0)/60) - 1));
//      System.out.println(prop_f_start_offsetY);
//      for (int i = 0; i < 17+3; i++) {
//        itLabel = new Label(prop_f_start_offsetY+i*60+"-");
//        itLabel.setTextAlignment(TextAlignment.RIGHT);
//        itLabel.setStyle("-fx-text-fill:whitesmoke; ;-fx-font-size:14px; -fx-font-weight:Bold; -fx-background-color: rgb(5, 5, 5),rgb(87, 89, 92),linear-gradient(rgba(56,65,71,0.3),rgba(56,65,71,0.3),rgba(56,65,71,0.3));");
//        itVBox.getChildren().add(itLabel);
//      }
//      AnchorPane.setLeftAnchor(itVBox, 15.0);
//    }
//
//    
//    AnchorPane.setTopAnchor(itVBox, 60.0);
//    AnchorPane.setBottomAnchor(itVBox, 20.0);
//    propPane.getChildren().add(itVBox);  
//    
//    return itVBox;
//  }  
//
//  private ObservableList<Rectangle> createPropBackgorundList(AnchorPane propPane, int winIndex){
//    final ObservableList<Rectangle> prop_background_list = FXCollections.observableArrayList();
//    Rectangle prop_background;
//
//    
//    final List<Float> prop_c_Start_time = (List<Float>)(getPropCoarseInfo(winIndex).get(3));
//    final List<Float> prop_f_Start_time = (List<Float>)(getPropFineInfo(winIndex).get(3));
//    final List<Float> prop_c_duration_list = (List<Float>)(getPropCoarseInfo(winIndex).get(4));
//    final List<Float> prop_f_duration_list = (List<Float>)(getPropFineInfo(winIndex).get(4));
//    
//    float prop_f_start_offsetY = 0;
//    if (winIndex > 1) {
//      prop_f_start_offsetY = -(60*( (int)(prop_c_Start_time.get(0)/60) - 1));//-60*(prop_c_Start_time_list.get(0)/60 - 1);
//    }    
//    
//    // fine propagator background colors
//    final List<String> prop_bg_color = Arrays.asList("#E98CA2","#8CCFA4","#F1D0A1","#A8C9E6","#C6A2CD","#57C4C4","#C9D37C","#898FC6");
//    int prop_f_bg_offsetY = 0;
//    
//    for(int i=0; i<nit; i++){
//      for(int j=0; j<nts-i; j++){
//        // set X
//        prop_background = new Rectangle();
//        prop_background.setWidth(160);
//        prop_background.setArcHeight(5);
//        prop_background.setArcWidth(5);        
//        prop_background.setX(30+totalOffcetX_BG+165*(j+i));
//        
//        // set Y
//        prop_background.setY(prop_f_Start_time.get(j+prop_f_bg_offsetY)+60+prop_f_start_offsetY);
//        prop_background.setHeight(prop_f_duration_list.get(j+prop_f_bg_offsetY));
//        
//        prop_background.setFill(Paint.valueOf(prop_bg_color.get(j)));
//        prop_background.setStyle("-fx-stroke: transparent; -fx-opacity:0.85;");
//        
//        prop_background.toBack();
//        prop_background_list.addAll(prop_background); 
//      }
//      prop_f_bg_offsetY = prop_f_bg_offsetY+nts-i;
//    }
//    
//    // coarse propagator background colors
//    int prop_c_bg_offsetY = 0;
//    for(int i=0; i<nit+1; i++){
//      for(int j=0; j<nts-i; j++){
//        // set X
//        prop_background = new Rectangle();
//        prop_background.setWidth(160);
//        prop_background.setArcHeight(5);
//        prop_background.setArcWidth(5);        
//        prop_background.setX(30+totalOffcetX_BG+165*(j+i));
//        
//        // set Y
//        prop_background.setY(prop_c_Start_time.get(j+prop_c_bg_offsetY)+60+prop_f_start_offsetY);
//        prop_background.setHeight(prop_c_duration_list.get(j+prop_c_bg_offsetY));
//        
//        prop_background.setStyle("-fx-fill: #434d5a; -fx-stroke: transparent; -fx-opacity:0.85;");//#38455C #2C2E3D
//        
//        prop_background.toBack();
//        prop_background_list.addAll(prop_background); 
//      }    
//      prop_c_bg_offsetY = prop_c_bg_offsetY+nts-i;
//    }    
//    
//    
//    
//
//    
//    
//    propPane.getChildren().addAll(prop_background_list);
//    
//    return prop_background_list;
//  }  
//
//  
//  private void createPararealElementColumns(){
//    
//    //--draw coarse propagator
//    final ProgressIndicator prop_c = new ProgressIndicator(1.0);
//    prop_c.setStyle("-fx-progress-color: "+color_c+"; -fx-accent:"+color_c+";");
//    prop_c.setMinSize(45,45);    
//    prop_c.setMaxSize(45,45); 
//    prop_c.setScaleX(0.75);
//    prop_c.setScaleY(0.75);
//    
//    //--draw fine propagator
//    final ProgressIndicator prop_f = new ProgressIndicator(1.0);
//    prop_f.setStyle("-fx-progress-color: "+color_f+"; -fx-accent:"+color_f+";");
//    prop_f.setMinSize(45,45); 
//    prop_f.setMaxSize(45,45); 
//    final StackPane propfPane = new StackPane(prop_f);
//    propfPane.setMinSize(45,45);
//    propfPane.setMaxSize(45,45);
//    propfPane.setStyle("-fx-background-color: linear-gradient(rgba(150,153,242,0.4),rgba(150,153,242,0.55),rgba(150,153,242,0.4));");    
//    propfPane.setScaleX(0.75);
//    propfPane.setScaleY(0.75);
//    
//    //--draw simple blender
//    final Polygon corr_simple = new Polygon(-10.0,8.0,10.0,8.0,0.0,-12.0);
//    corr_simple.setFill(Paint.valueOf("linear-gradient(white,"+color_corr+",white)"));
//    corr_simple.setScaleX(1.2);
//    corr_simple.setScaleY(1.2); 
//    
//    //--draw complex blender
//    final Polygon corr_complex = new Polygon(-10.0,8.0,10.0,8.0,0.0,-12.0);
//    corr_complex.setFill(Paint.valueOf("linear-gradient(white,"+color_c+","+color_corr+","+color_f+")"));
//    corr_complex.setScaleX(1.2);
//    corr_complex.setScaleY(1.2);  
//    
//    //--draw coarse correction data
//    final Line corr_lineC = createLine(5, 30, 45, 30, color_c);
//    corr_lineC.getStrokeDashArray().addAll(4d, 15d);
//    // circleC
//    final Circle corr_circleC = getCircle(45, 30);   
//    corr_circleC.setStroke(Color.valueOf(color_c));
//    // arrowline
//    final Group corr_arrowlineC = new Group(corr_lineC, corr_circleC);    
//    
//    //--draw fine correction data
//    final Line corr_lineF = createLine(5, 30, 45, 30, color_f);
//    corr_lineF.getStrokeDashArray().addAll(4d, 15d);
//    // circleC
//    final Rectangle corr_rectangleF = getRectangle(45, 35);   
//    corr_rectangleF.setStroke(Color.valueOf(color_f));
//    // arrowline
//    final Group corr_arrowlineF = new Group(corr_lineF, corr_rectangleF); 
//    
//    //--draw coarse prediction data
//    final CubicCurve pred_curveC = createCurvedLineMark(5, 30, 45, 30, color_c);    
//    // circleC
//    final Circle pred_circleC = getCircle(50, 30);   
//    pred_circleC.setStroke(Color.valueOf(color_c));
//    pred_circleC.setStyle("-fx-fill:linear-gradient("+color_c+",transparent,"+color_c+");");
//    // arrowline
//    final Group pred_arrowlineC = new Group(pred_curveC, pred_circleC);    
//    
//    //--draw fine prediction data
//    final CubicCurve pred_curveF = createCurvedLineMark(5, 30, 45, 30, color_f);
//    // rectangelF
//    final Rectangle pred_rectangleF = getRectangle(50, 35);
//    pred_rectangleF.setStyle("-fx-fill:linear-gradient("+color_f+",transparent,"+color_f+");");
//    // arrowline
//    final Group pred_arrowlineF = new Group(pred_curveF, pred_rectangleF);   
//    
//    //--draw blending result coarse
//    final CubicCurve blend_curveC = createCurvedLineMark(5, 30, 45, 30, color_c);
//    // arrowC
//    final Polygon blend_arrowC = getArrow(5, 32, 50, 32,color_c);
//    blend_arrowC.setFill(Color.valueOf(color_c));
//    // arrowlineC
//    final Group blend_arrowlineC = new Group(blend_curveC, blend_arrowC);    
//    
//    //--draw blending result fine
//    final CubicCurve blend_curveF = createCurvedLineMark(5, 30, 45, 30, color_f);
//    // arrowC
//    final Polygon blend_arrowF = getArrow(5, 32, 50, 32,color_f);
//    blend_arrowF.setFill(Color.valueOf(color_f));
//    // arrowlineC
//    final Group blend_arrowlineF = new Group(blend_curveF, blend_arrowF);    
//    
//    pararealElementGridPane.add(prop_c, 1, 0);
//    pararealElementGridPane.add(propfPane, 1, 1);
//    pararealElementGridPane.add(corr_complex, 3, 0);
//    pararealElementGridPane.add(corr_simple, 3, 1);
//    pararealElementGridPane.add(corr_arrowlineC, 5, 0);
//    pararealElementGridPane.add(corr_arrowlineF, 5, 1);
//    pararealElementGridPane.add(pred_arrowlineC, 7, 0);
//    pararealElementGridPane.add(pred_arrowlineF, 7, 1);
//    pararealElementGridPane.add(blend_arrowlineC, 9, 0);
//    pararealElementGridPane.add(blend_arrowlineF, 9, 1);    
//
//  }  
//  
//
//
//  
//  
//  private void initDrawGlobalVariables(){
//    /** general offset*/
//    totalOffcetX_Prop =89;
//    totalOffcetY_Prop = 71;   
//
//    totalOffcetX_BG = 50;
//    totalOffcetY_BG = 75;    
//    
//    /** variables for drawing propagators */
//    propX0 = 45+totalOffcetX_Prop;
//    propdeltaX = 165;
//    propY0C = 5+totalOffcetY_Prop;
//    propY0F = 85+totalOffcetY_Prop;
//    propdeltaY = 165;
//
//    corrX0 = 155+totalOffcetX_Prop;
//    corrY0 = 70+totalOffcetY_Prop;
//    corrdeltaX = 165;
//    corrdeltaY = 165;
//
//    /** variables for fetching parareal profile data */
//    prop_f_idx_start = 14;
//    prop_f_idx_end = 16;    
//    percent_idx_start = 17;
//    percent_idx_end = 24; 
//    start_time_idx_start = 25;
//    start_time_idx_end = 40;
//    duration_idx_start = 41;
//    duration_idx_end = 54;
//    
//    
//    /** variables for data dependency*/
//    // delta
//    dependDeltaX = 165;
//    dependDeltaY = 165;  
//    // coarse
//    dependStartX0 = 87+totalOffcetX_Prop;
//    dependStartY0 = 22+totalOffcetY_Prop;
//    dependEndX0 = 140+totalOffcetX_Prop;
//    dependEndY0 = 60+totalOffcetY_Prop;
//    // coarse offect
//    EndOffectXC = 10;
//    EndOffectYC = -20;
//    // fine
//    dependStartX0F = 87+totalOffcetX_Prop;
//    dependStartY0F = 105+totalOffcetY_Prop;
//    dependEndX0F = 140+totalOffcetX_Prop;
//    dependEndY0F = 215+totalOffcetY_Prop;
//    // corr_simple
//    corrStartX0 = 166+totalOffcetX_Prop;
//    corrStartY0 = 60+totalOffcetY_Prop;
//    corrEndX0C = 218+totalOffcetX_Prop;
//    corrEndY0C = 24+totalOffcetY_Prop;  
//    corrEndX0F = 218+totalOffcetX_Prop;
//    corrEndY0F = 104+totalOffcetY_Prop;     
//  }
//  
//  /***********************************************************
//  * Draw data dependency arrowed-lines. 
//  * 
//  ************************************************************/
//  //-- coarse dependency 
//  private Group drawDataDependencyCoarse(int it, int ts, int winIndex){
//    // cubic curved line
//    final float startX_c = dependStartX0+(ts-1)*dependDeltaX;
//    final float endX_c = dependEndX0+(ts-1)*dependDeltaX;
//    
////    final float startY_c = dependStartY0+(it-1)*dependDeltaY;
////    final float endY_c = dependEndY0+(it-1)*dependDeltaY;
//    
//    final List<Float> prop_c_Start_time = (List<Float>)(getPropCoarseInfo(winIndex).get(3));
//    float prop_c_start_offsetY = 0;
//    if (winIndex > 1) {
//      prop_c_start_offsetY = -(60*( (int)(prop_c_Start_time.get(0)/60) - 1));
//    }     
//    int prop_c_offsetY = 0;
//    if (it > 1) {
//      for (int i = 0; i < it-1; i++) {
//        prop_c_offsetY = prop_c_offsetY+nts-i;
//      }       
//    }
//    
//    float startY_c = prop_c_Start_time.get(ts+prop_c_offsetY-it)+60+prop_c_start_offsetY+25;    
//    float endY_c = startY_c+38;
//    
//    
//    final CubicCurve curveC = createCurvedLine(startX_c, startY_c, endX_c, endY_c, color_c);    
//    
//    // circleC
//    final Circle circleC = getCircle(endX_c, endY_c);   
//    circleC.setStroke(Color.valueOf(color_c));
//    circleC.setStyle("-fx-fill:linear-gradient("+color_c+",transparent,"+color_c+");");
//    
//    // arrowline
//    final Group arrowline = new Group(curveC, circleC);
//    
//    return arrowline;
//  }   
//  
//  //-- coarse dependency additional
//  private Group drawDataDependencyCoarseAddition(int it, int ts, int winIndex){
//    // line
//    final float startX_c = dependStartX0+(ts-1)*dependDeltaX;
//    final float endX_c = dependEndX0+(ts-1)*dependDeltaX+EndOffectXC;
//    
////    final float startY_c = dependStartY0+(it-1)*dependDeltaY;
////    final float endY_c = dependEndY0+(it)*dependDeltaY+EndOffectYC;  
//    
//    final List<Float> prop_c_Start_time = (List<Float>)(getPropCoarseInfo(winIndex).get(3));
//    float prop_c_start_offsetY = 0;
//    if (winIndex > 1) {
//      prop_c_start_offsetY = -(60*( (int)(prop_c_Start_time.get(0)/60) - 1));
//    }     
//    int prop_c_offsetY = 0;
//    if (it > 1) {
//      for (int i = 0; i < it-1; i++) {
//        prop_c_offsetY = prop_c_offsetY+nts-i;
//      }       
//    }
//    
//    float startY_c = prop_c_Start_time.get(ts+prop_c_offsetY-it)+60+prop_c_start_offsetY+25;    
//    float endY_c = prop_c_Start_time.get(ts+prop_c_offsetY-it+nts-it)+60+prop_c_start_offsetY+10+(propY0F-propY0C)/2;       
//    
//    final Line line = createLine(startX_c, startY_c, endX_c, endY_c, color_c);
//    line.getStrokeDashArray().addAll(4d, 15d);
//    // circleC
//    final Circle circleC = getCircle(endX_c, endY_c);   
//    circleC.setStroke(Color.valueOf(color_c));
//    // arrowline
//    final Group arrowline = new Group(line, circleC, drawDataDependencyCoarse(it, ts, winIndex));
//    
//    return arrowline;
//  }  
//  
//  //-- coarse dependency additional: for the last iteration
//  private Group drawDataDependencyCoarseAdditionEnd(int it, int ts, int winIndex){
//    // line
//    final float startX_c = dependStartX0+(ts-1)*dependDeltaX;
//    final float endX_c = dependEndX0+(ts-1)*dependDeltaX+EndOffectXC;
//    
////    final float startY_c = dependStartY0+(it-1)*dependDeltaY;
////    final float endY_c = dependEndY0+(it)*dependDeltaY+EndOffectYC;  
//    
//    final List<Float> prop_c_Start_time = (List<Float>)(getPropCoarseInfo(winIndex).get(3));
//    float prop_c_start_offsetY = 0;
//    if (winIndex > 1) {
//      prop_c_start_offsetY = -(60*( (int)(prop_c_Start_time.get(0)/60) - 1));
//    }     
//    int prop_c_offsetY = 0;
//    if (it > 1) {
//      for (int i = 0; i < it-1; i++) {
//        prop_c_offsetY = prop_c_offsetY+nts-i;
//      }       
//    }
//    
//    float startY_c = prop_c_Start_time.get(ts+prop_c_offsetY-it)+60+prop_c_start_offsetY+25;    
//    float endY_c = prop_c_Start_time.get(ts+prop_c_offsetY-it+nts-it)+60+prop_c_start_offsetY+10+(propY0F-propY0C)/2;//startY_c+180;    
//    
//    final Line line = createLine(startX_c, startY_c, endX_c, endY_c, color_c);
//    line.getStrokeDashArray().addAll(4d, 15d);
//    // circleC
//    final Circle circleC = getCircle(endX_c, endY_c);   
//    circleC.setStroke(Color.valueOf(color_c));
//    // arrowline
//    final Group arrowline = new Group(line, circleC);
//    
//    return arrowline;
//  }  
//  
//  //-- fine dependency
//  private Group drawDataDependencyFine(int it, int ts, int winIndex){
//    // line
//    final float startX_f = dependStartX0F+(ts-1)*dependDeltaX+8;
//    final float endX_f = dependEndX0F+(ts-1)*dependDeltaX+3;
//    
////    final float startY_f = dependStartY0F+(it-1)*dependDeltaY;
////    final float endY_f = dependEndY0F+(it-1)*dependDeltaY;
//    
//    final List<Float> prop_c_Start_time = (List<Float>)(getPropCoarseInfo(winIndex).get(3));
//    final List<Float> prop_f_Start_time = (List<Float>)(getPropFineInfo(winIndex).get(3));
//    float prop_f_start_offsetY = 0;
//    if (winIndex > 1) {
//      prop_f_start_offsetY = -(60*( (int)(prop_f_Start_time.get(0)/60) - 1));
//    }     
//    int prop_f_offsetY = 0;
//    if (it > 1) {
//      for (int i = 0; i < it-1; i++) {
//        prop_f_offsetY = prop_f_offsetY+nts-i;
//      }       
//    }
//    
//    float startY_f = prop_f_Start_time.get(ts+prop_f_offsetY-it)+60+prop_f_start_offsetY+25+propY0F-propY0C;    
//    float endY_f = prop_c_Start_time.get(ts+prop_f_offsetY-it+nts-it)+60+prop_f_start_offsetY+25+(propY0F-propY0C)/2;  
//    
//    final Line line = createLine(startX_f, startY_f, endX_f-4, endY_f-8, color_f);
//    line.getStrokeDashArray().addAll(4d, 15d);
//    
//    // rectangelF
//    final Rectangle rectangleF = getRectangle(endX_f, endY_f);
//    rectangleF.setStroke(Color.valueOf(color_f));  
//    
//    // arrowline
//    final Group arrowline = new Group(line, rectangleF);
//    
//    return arrowline;
//  } 
//  
//  private Group drawDataDependencyFineEdge(int it, int ts, int winIndex){
//    // line
//    final float startX_f = dependStartX0F+(ts-1)*dependDeltaX+8;
//    final float endX_f = dependEndX0F+(ts-1)*dependDeltaX+3;
//    
////    final float startY_f = dependStartY0F+(it-1)*dependDeltaY;
////    final float endY_f = dependEndY0F+(it-1)*dependDeltaY;
//    
//    final List<Float> prop_c_Start_time = (List<Float>)(getPropCoarseInfo(winIndex).get(3));
//    final List<Float> prop_f_Start_time = (List<Float>)(getPropFineInfo(winIndex).get(3));
//    float prop_f_start_offsetY = 0;
//    if (winIndex > 1) {
//      prop_f_start_offsetY = -(60*( (int)(prop_f_Start_time.get(0)/60) - 1));
//    }     
//    int prop_f_offsetY = 0;
//    if (it > 1) {
//      for (int i = 0; i < it-1; i++) {
//        prop_f_offsetY = prop_f_offsetY+nts-i;
//      }       
//    }
//    
//    float startY_f = prop_f_Start_time.get(ts+prop_f_offsetY-it)+60+prop_f_start_offsetY+25+propY0F-propY0C;
//    float endY_f = prop_c_Start_time.get(ts+prop_f_offsetY-it+nts-it)+60+prop_f_start_offsetY+25+(propY0F-propY0C)/2;
//    //float endY_f = startY_f+130;    
//    
//    
//    final CubicCurve curveFEdge = createCurvedLine(startX_f, startY_f, endX_f-8, endY_f+2, color_f);
//    
//    // rectangelF
//    final Rectangle rectangleF = getRectangle(endX_f-4, endY_f+10);
//    rectangleF.setStyle("-fx-fill:linear-gradient("+color_f+",transparent,"+color_f+");");
//    
//    // arrowline
//    final Group arrowline = new Group(curveFEdge, rectangleF);
//    
//    return arrowline;    
//    
//  }
//  
//  //-- corretor data passing
//  private Group drawDataPassingCorrector(int it, int ts, String color_c, String color_f, int winIndex){
//    //--cubicC curved line
//    final float startX_c = corrStartX0+(ts-1)*dependDeltaX;
//    final float endX_c = corrEndX0C+(ts-1)*dependDeltaX;
//    
////    final float startY_c = corrStartY0+(it-1)*dependDeltaY;
////    final float endY_c = corrEndY0C+(it-1)*dependDeltaY;
//    
//    final List<Float> prop_corr_Start_time = (List<Float>)(getPropCoarseInfo(winIndex).get(3));
//    float prop_corr_start_offsetY = 0;
//    if (winIndex > 1) {
//      prop_corr_start_offsetY = -(60*( (int)(prop_corr_Start_time.get(0)/60) - 1));
//    }     
//    int prop_corr_offsetY = 0;
//    if (it > 1) {
//      for (int i = 0; i < it-1; i++) {
//        prop_corr_offsetY = prop_corr_offsetY+nts-i;
//      }       
//    }
//    float startY_c = propY0F-propY0C + prop_corr_Start_time.get(ts+prop_corr_offsetY-it)+60+prop_corr_start_offsetY;      
//    float endY_c = prop_corr_Start_time.get(ts+prop_corr_offsetY-it+1)+60+prop_corr_start_offsetY+15;//startY_c - 25;
//        
//    final CubicCurve curveC = createCurvedLine(startX_c, startY_c, endX_c, endY_c, color_c);
//    // arrowC
//    final Polygon arrowC = getArrow(startX_c, startY_c, endX_c, endY_c,color_c);
//    arrowC.setFill(Color.valueOf(color_c));
//    // arrowlineC
//    final Group arrowlineC = new Group(curveC, arrowC);
//    
//    //--cubicF curved line
//    final float startX_f = corrStartX0+(ts-1)*dependDeltaX;
//    final float endX_f = corrEndX0F+(ts-1)*dependDeltaX;
//    
////    final float startY_f = corrStartY0+(it-1)*dependDeltaY;
////    final float endY_f = corrEndY0F+(it-1)*dependDeltaY;
//    
//    float startY_f = startY_c;    
//    float endY_f = propY0F-propY0C + prop_corr_Start_time.get(ts+prop_corr_offsetY-it+1)+60+prop_corr_start_offsetY+15;    
//    
//    final CubicCurve curveF = createCurvedLine(startX_f, startY_f, endX_f, endY_f, color_f);    
//    // arrowF
//    final Polygon arrowF = getArrow(startX_f, startY_f, endX_f, endY_f,color_f);
//    arrowF.setFill(Color.valueOf(color_f));
//    // arrowlineF
//    final Group arrowlineF = new Group(curveF, arrowF);
//    
//    
//    final Group arrowlineCF = new Group(arrowlineC, arrowlineF);
//    
//    return arrowlineCF;    
//  }
//  
//  //-- corr_simple data passing: extra iteration
//  private Group drawDataPassingCorrectorExtraIt(int it, int ts, String color_c, int winIndex){
//    //--curveC curved line
//    final float startX_c = corrStartX0+(ts-1)*dependDeltaX;
//    final float endX_c = corrEndX0C+(ts-1)*dependDeltaX;
//    
////    final float startY_c = corrStartY0+(it-1)*dependDeltaY;
////    final float endY_c = corrEndY0C+(it-1)*dependDeltaY;
//    
//    final List<Float> prop_corr_Start_time = (List<Float>)(getPropCoarseInfo(winIndex).get(3));
//    float prop_corr_start_offsetY = 0;
//    if (winIndex > 1) {
//      prop_corr_start_offsetY = -(60*( (int)(prop_corr_Start_time.get(0)/60) - 1));
//    }     
//    int prop_corr_offsetY = 0;
//    if (it > 1) {
//      for (int i = 0; i < it-1; i++) {
//        prop_corr_offsetY = prop_corr_offsetY+nts-i;
//      }       
//    }
//    float startY_c = propY0F-propY0C + prop_corr_Start_time.get(ts+prop_corr_offsetY-it)+60+prop_corr_start_offsetY;      
//    float endY_c = prop_corr_Start_time.get(ts+prop_corr_offsetY-it+1)+60+prop_corr_start_offsetY+15;    
//    
//    final CubicCurve curveC = createCurvedLine(startX_c, startY_c, endX_c, endY_c, color_c);     
//    
//    // arrowC
//    final Polygon arrowC = getArrow(startX_c, startY_c, endX_c, endY_c,color_c);
//    arrowC.setFill(Color.valueOf(color_c));
//    
//    // arrowlineC
//    final Group arrowlineC = new Group(curveC, arrowC);
//    
//    return arrowlineC;    
//  } 
//  
//  
//  
//  private Line createLine(double startX,double startY,double endX,double endY,String colorString){
//    final Line line = new Line(startX, startY, endX, endY);
//    line.setStroke(Color.valueOf(colorString));
//    line.setStyle("-fx-fill:linear-gradient("+colorString+",transparent,"+colorString+");");    
//    line.setStrokeWidth(2.5);
//    
//    return line;
//  }
//  
//  private CubicCurve createCurvedLine(double startX,double startY,double endX,double endY,String colorString){
//    final double delX = (2999999*(endX-startX)/6000000);
//    final double controlX1 = startX+delX;
//    final double controlY1 = startY;
//    final double controlX2 = endX-delX;
//    final double controlY2 = endY;
//    final CubicCurve curve = new CubicCurve();
//    curve.setStartX(startX);
//    curve.setStartY(startY);
//    curve.setControlX1(controlX1);
//    curve.setControlY1(controlY1);
//    curve.setControlX2(controlX2);
//    curve.setControlY2(controlY2);
//    curve.setEndX(endX);
//    curve.setEndY(endY);
//    curve.setStroke(Color.valueOf(colorString));
//    curve.setStyle("-fx-fill:linear-gradient("+colorString+",transparent,"+colorString+");");
//    curve.setStrokeWidth(1);
//    
//    return curve;
//  }  
//  
//  private CubicCurve createCurvedLineMark(double startX,double startY,double endX,double endY,String colorString){
//    final double delX = (endX-startX)/4;
//    final double delY = 15.0;
//    final double controlX1 = startX+delX;
//    final double controlY1 = startY-delY;
//    final double controlX2 = endX-delX;
//    final double controlY2 = endY+delY;
//    final CubicCurve curve = new CubicCurve();
//    curve.setStartX(startX);
//    curve.setStartY(startY);
//    curve.setControlX1(controlX1);
//    curve.setControlY1(controlY1);
//    curve.setControlX2(controlX2);
//    curve.setControlY2(controlY2);
//    curve.setEndX(endX);
//    curve.setEndY(endY);
//    curve.setStroke(Color.valueOf(colorString));
//    curve.setStyle("-fx-fill:linear-gradient("+colorString+",transparent,"+colorString+");");
//    curve.setStrokeWidth(1);
//    
//    return curve;
//  } 
//  
//  
//  private Polygon getArrow(float startX, float startY, float endX, float endY, String colorString){
//    final Polygon arrow = new Polygon();
//    arrow.getPoints().addAll(new Double[]{0.0, 5.0, -5.0, -5.0, 5.0, -5.0});
//    double angle = Math.atan2(endY - startY, endX - startX) * 180 / 3.14;
//    arrow.setRotate((angle - 90));
//    arrow.setTranslateX(startX);
//    arrow.setTranslateY(startY);
//    arrow.setTranslateX(endX);
//    arrow.setTranslateY(endY); 
//    arrow.setStyle("-fx-fill:linear-gradient("+colorString+",transparent);");
//        
//    return arrow;
//  }  
//  
//  private Circle getCircle(float endX, float endY){
//    final Circle circle = new Circle(endX, endY, 4);
//    circle.setStrokeWidth(1.5);
//    return circle;
//  }
//  
//  private Rectangle getRectangle(float endx, float endy){
//    final Rectangle rectangle = new Rectangle(endx-3, endy-10, 8, 8);
//    rectangle.setStrokeWidth(1.5);
//    return rectangle;
//  }
// 
//  
//  
//  
//  /***********************************************************
//  *  Draw propagators 
//  * 
//  ************************************************************/
//  //-- coarse prop
//  private VBox drawPropCoarse(int it, int ts, int xOffset, ObservableList<ProgressIndicator> propList_c, int winIndex){
//    // create a propagator
//    final VBox propagator = new VBox();
//    final ProgressIndicator propC = new ProgressIndicator();
//    final int ts_updated = ts+xOffset;
//    propC.setId(it+"-ts_"+ts_updated);
//    propC.setStyle("-fx-progress-color: "+color_c+"; -fx-accent:"+color_c+";");
//    propC.setPrefSize(50, 50);
//    propList_c.add(propC);// save ProgressIndicator for further reference
//    propagator.getChildren().addAll(propC);
//    // basic layout
//    propagator.setSpacing(5);
//    propagator.setAlignment(Pos.CENTER);
//    // set xy coordinates
//    propX = (propX0+(it-1)*propdeltaX)+(ts-it)*propdeltaX;
//    //propYC = propY0C+(it-1)*propdeltaY;
//    final List<Float> prop_c_Start_time = (List<Float>)(getPropCoarseInfo(winIndex).get(3));
//    float prop_c_start_offsetY = 0;
//    if (winIndex > 1) {
//      prop_c_start_offsetY = -(60*( (int)(prop_c_Start_time.get(0)/60) - 1));
//    }     
//    int prop_c_offsetY = 0;
//    if (it > 1) {
//      for (int i = 0; i < it-1; i++) {
//        prop_c_offsetY = prop_c_offsetY+nts-i;
//      }       
//    }
//    //System.out.println("ts: "+ts);
//    if (prop_c_Start_time.get(ts+prop_c_offsetY-it) == 0.0) {
//      propYC = prop_c_Start_time.get(ts+prop_c_offsetY-it-1)+60+prop_c_start_offsetY;
//    } else{
//      propYC = prop_c_Start_time.get(ts+prop_c_offsetY-it)+60+prop_c_start_offsetY;
//    }
//    
//    
//    propagator.setLayoutX(propX);
//    propagator.setLayoutY(propYC);
//    
//    propC.setOnMouseEntered(new EventHandler<MouseEvent>() {
//      @Override
//      public void handle(MouseEvent event) {
//        propC.setCursor(Cursor.HAND);
//        //((ProgressIndicator)event.getSource()).setTooltip(new Tooltip("Duration Time"));
//        propC.setTooltip(new Tooltip("Duration Time"));
//        //popOver.show(propC, (propX0+(it-1)*propdeltaX)+(ts-it)*propdeltaX, propY0C+(it-1)*propdeltaY, Duration.millis(300));
//        propC.setScaleX(1.1f);
//        propC.setScaleY(1.1f);
//      }
//    });
//    propC.setOnMouseExited(new EventHandler<MouseEvent>() {
//      @Override
//      public void handle(MouseEvent event) {
//        propC.setCursor(Cursor.DEFAULT);
//        //popOver.hide(Duration.millis(100));
//        propC.setScaleX(1);
//        propC.setScaleY(1);
//      }
//    });
//
//    propC.setOnMouseClicked(new EventHandler<MouseEvent>() {
//      @Override
//      public void handle(MouseEvent event) {
//        // use this prop id to find the related sql file.
//        System.out.println("Event source: "+event.getSource().toString());
//        SwingUtilities.invokeLater(new Runnable() {
//          @Override
//          public void run() {
//            // figure out dbpathstring
//            final String[] coarseDBNameList = (String[]) (getDBNameList().get(0));
//            final int length = coarseDBNameList.length;
//            String coarseDBName = null;
//            String subString;
//            int beginIdx;
//            int endIdx;
//            for (int i = 0; i < length; i++) {
//              coarseDBName = coarseDBNameList[i];
//              beginIdx = coarseDBName.indexOf("_")+1;
//              endIdx = coarseDBName.lastIndexOf(".");
//              subString = coarseDBName.substring(beginIdx, endIdx);
//              if (propC.getId().equals(subString)) {
//                System.out.println("propC.getId(): "+propC.getId());
//                System.out.println("subString: "+subString);
//                System.out.println("coarseDBName: "+coarseDBName);
//                break;
//              }
//            }
//            // caseDirPath:"/home/mwang/Desktop/exnmpi1/exn-aero/models/cylinder_parareal-014"
//            //ExnMonitorTC.specifyPropagatorDBPath(caseDirPath+"/coarse/"+coarseDBName);
//            final String propagatorDBPath = caseDirPath+"/coarse/"+coarseDBName;
//            final String propagatorDBName = propagatorDBPath.substring(propagatorDBPath.lastIndexOf("/")+1);
//            final List propagatorDBPathList = new ArrayList<>();
//            propagatorDBPathList.add(propagatorDBPath);
//
//            ExnMonitorTC.setDisplayName("ExnMonitor TrackBoard--"+caseName);
//            ExnMonitorTC.open();
//            ExnMonitorTC.requestActive();
//            EXNMonitorMainViewController monitorController = ExnMonitorTC.getExnMonitorTBController();
//            Platform.runLater(new Runnable() {
//              @Override
//              public void run() {
//                monitorController.updateSqldbTreeItemColor(color_c);
//                monitorController.updateConnectStateLabel(propagatorDBName,caseName);
//                monitorController.updateDBManagerTreeView(propagatorDBPathList);
//              }
//            });
//            
//          }
//        });
//      }
//    });    
//    
//    return propagator;
//  }
//  //-- fine prop
//  private VBox drawPropFine(int it, int ts, int xOffset, ObservableList<ProgressIndicator> propList_f, int winIndex){
//    // create a propagator
//    final VBox propagator = new VBox();
//    final ProgressIndicator propF = new ProgressIndicator();
//    final int ts_updated = ts+xOffset;
//    propF.setId(it+"-ts_"+ts_updated);
//    final List<String> prop_color_f = Arrays.asList("#E98CA2","#8CCFA4","#F1D0A1","#A8C9E6","#C6A2CD","#57C4C4","#C9D37C","#898FC6");
//    propF.setStyle("-fx-progress-color: "+prop_color_f.get(ts-it)+"; -fx-accent:"+prop_color_f.get(ts-it)+";");
//    propF.setPrefSize(50, 50);
//    propList_f.add(propF);// save ProgressIndicator for further reference
//    // create a propfPane
//    final StackPane propfPane = new StackPane(propF);
//    propfPane.setStyle("-fx-background-color: linear-gradient(rgba(91,92,155,0.5),rgba(91,92,155,0.55),rgba(91,92,155,0.6));");
//    propagator.getChildren().addAll(propfPane);
//    // basic layout
//    propagator.setSpacing(5);
//    propagator.setAlignment(Pos.TOP_CENTER);
//    // set xy coordinates
//    propX = (propX0+(it-1)*propdeltaX)+(ts-it)*propdeltaX;
//    //propYF = propY0F+(it-1)*propdeltaY;
//    final List<Float> prop_f_Start_time = (List<Float>)(getPropFineInfo(winIndex).get(3));
//    float prop_f_start_offsetY = 0;
//    if (winIndex > 1) {
//      prop_f_start_offsetY = -60*(prop_f_Start_time.get(0)/60 - 1);
//    }     
//    int prop_f_offsetY = 0;
//    if (it > 1) {
//      for (int i = 0; i < it-1; i++) {
//        prop_f_offsetY = prop_f_offsetY+nts-i;
//      }       
//    }
//    //System.out.println("ts: "+ts);
//    if ( prop_f_Start_time.get(ts+prop_f_offsetY-it) == 0.0 ) {
//      System.out.println(prop_f_Start_time.get(ts+prop_f_offsetY-it-1));
//      propYF = propY0F-propY0C+prop_f_Start_time.get(ts+prop_f_offsetY-it-1)+60+prop_f_start_offsetY;  
//    } else{
//      propYF = propY0F-propY0C+prop_f_Start_time.get(ts+prop_f_offsetY-it)+60+prop_f_start_offsetY;  
//    }
//    
//    
//    
//    
//    propagator.setLayoutX(propX);
//    propagator.setLayoutY(propYF);
//    
//    //--propagator events
//    propF.setOnMouseEntered(new EventHandler<MouseEvent>() {
//      @Override
//      public void handle(MouseEvent event) {
//        propF.setCursor(Cursor.HAND);
//        propF.setTooltip(new Tooltip("Duration Time"));
//        propF.setScaleX(1.1f);
//        propF.setScaleY(1.1f);
//      }
//    });
//    propF.setOnMouseExited(new EventHandler<MouseEvent>() {
//      @Override
//      public void handle(MouseEvent event) {
//        propF.setCursor(Cursor.DEFAULT);
//        propF.setScaleX(1);
//        propF.setScaleY(1);
//      }
//    });
//
//    propF.setOnMouseClicked(new EventHandler<MouseEvent>() {
//      @Override
//      public void handle(MouseEvent event) {
//        SwingUtilities.invokeLater(new Runnable() {
//          @Override
//          public void run() {
//            // figure out dbpathstring
//            final String[] fineDBNameList = (String[]) (getDBNameList().get(1));
//            final int length = fineDBNameList.length;
//            String fineDBName = null;
//            String subString;
//            int beginIdx;
//            int endIdx;
//            for (int i = 0; i < length; i++) {
//              fineDBName = fineDBNameList[i];
//              beginIdx = fineDBName.indexOf("_")+1;
//              endIdx = fineDBName.lastIndexOf(".");
//              subString = fineDBName.substring(beginIdx, endIdx);
//              if (propF.getId().equals(subString)) {
//                System.out.println("propF.getId(): "+propF.getId());
//                System.out.println("subString: "+subString);
//                System.out.println("fineDBName: "+fineDBName);
//                break;
//              }
//            }
//            // caseDirPath:"/home/mwang/Desktop/exnmpi1/exn-aero/models/cylinder_parareal-014"
//            //ExnMonitorTC.specifyPropagatorDBPath(caseDirPath+"/fine/"+fineDBName);
//            final String propagatorDBPath = caseDirPath+"/fine/"+fineDBName;
//            final String propagatorDBName = propagatorDBPath.substring(propagatorDBPath.lastIndexOf("/")+1);
//            final List propagatorDBPathList = new ArrayList<>();
//            propagatorDBPathList.add(propagatorDBPath);
//
//            ExnMonitorTC.setDisplayName("ExnMonitor TrackBoard--"+caseName);
//
//            Platform.runLater(new Runnable() {
//              @Override
//              public void run() {
//                ExnMonitorTC.getExnMonitorTBController().updateSqldbTreeItemColor(color_f);
//                ExnMonitorTC.getExnMonitorTBController().updateConnectStateLabel(propagatorDBName,caseName);
//                ExnMonitorTC.getExnMonitorTBController().updateDBManagerTreeView(propagatorDBPathList);                             //
//              }
//            });
//
//            ExnMonitorTC.open();
//            ExnMonitorTC.requestActive();
//          }
//        });
//      }
//    });    
//    
//    return propagator;
//  } 
//  
//  private List<String[]> getDBNameList(){
//    //-- Get all coarse-and-fine database namelist for opening matching db in ExnMonitor
//    final List<String[]> DBNameList = new ArrayList<>();
//    final File coarseDBFolder = new File(caseDirPath+"/coarse"); 
//    final File fineDBFolder = new File(caseDirPath+"/fine");
//    final String[] coarseDBNameList = coarseDBFolder.list(new FilenameFilter() {
//      @Override
//      public boolean accept(File current, String name) {
//        return new File(current, name).isFile();
//      }
//    }); 
//    System.out.println("coarseDBNameList: "+coarseDBNameList[0]);
//    final String[] fineDBNameList = fineDBFolder.list(new FilenameFilter() {
//      @Override
//      public boolean accept(File current, String name) {
//        return new File(current, name).isFile();
//      }
//    }); 
//    System.out.println("fineDBNameList: "+fineDBNameList[0]); 
//    
//    DBNameList.add(coarseDBNameList);
//    DBNameList.add(fineDBNameList);
//    
//    return DBNameList;
//  }  
//  
//  //-- corr_simple
//  private Polygon drawSimpleBlender(int it, int ts, ObservableList<Polygon> corrList, int winIndex){
//    // create a propagator
//    final Polygon corrector = new Polygon(-50.0,40.0,50.0,40.0,0.0,-60.0);
//    corrector.setFill(Paint.valueOf("linear-gradient(white,"+color_corr+",white)"));
//    corrList.add(corrector);// save Polygon for further reference
//    // set scale
//    corrector.setScaleX(0.4);
//    corrector.setScaleY(0.4);
//    // set xy coordinates
//    if (it<2) {
//      corrX = corrX0+(ts-it)*corrdeltaX;
//      //corrY = corrY0;
//    } else if(it >=2){
//      corrX = (corrX0+(it-2)*corrdeltaX)+(ts+1-it)*corrdeltaX;
//      //corrY = corrY0+(it-1)*corrdeltaY;
//    }
//    corrector.setLayoutX(corrX);
//    
//    final List<Float> prop_corr_Start_time = (List<Float>)(getPropCoarseInfo(winIndex).get(3));
//    float prop_corr_start_offsetY = 0;
//    if (winIndex > 1) {
//      prop_corr_start_offsetY = -(60*( (int)(prop_corr_Start_time.get(0)/60) - 1));
//    }     
//    int prop_corr_offsetY = 0;
//    if (it > 1) {
//      for (int i = 0; i < it-1; i++) {
//        prop_corr_offsetY = prop_corr_offsetY+nts-i;
//      }       
//    }
//    //System.out.println("ts: "+ts);
//    corrY = propY0F-propY0C + prop_corr_Start_time.get(ts+prop_corr_offsetY-it)+60+prop_corr_start_offsetY;    
//    
//    
//    corrector.setLayoutY(corrY);
//    
//    return corrector;
//  }   
//  
//  private Polygon drawComplexBlender(int it, int ts, ObservableList<Polygon> corrList, int winIndex){
//    // create a propagator
//    final Polygon corrector = new Polygon(-50.0,40.0,50.0,40.0,0.0,-60.0);
//    corrector.setFill(Paint.valueOf("linear-gradient(white,"+color_c+","+color_corr+","+color_f+")"));
//    
//    corrList.add(corrector);// save Polygon for further reference
//    // set scale
//    corrector.setScaleX(0.4);
//    corrector.setScaleY(0.4);
//    // set xy coordinates
//    corrX = (corrX0+(it-2)*corrdeltaX)+(ts+1-it)*corrdeltaX;
//    //corrY = corrY0+(it-1)*corrdeltaY;
//    corrector.setLayoutX(corrX);
//    
//    final List<Float> prop_corr_Start_time = (List<Float>)(getPropCoarseInfo(winIndex).get(3));
//    float prop_corr_start_offsetY = 0;
//    if (winIndex > 1) {
//      prop_corr_start_offsetY = -(60*( (int)(prop_corr_Start_time.get(0)/60) - 1));
//    }     
//    int prop_corr_offsetY = 0;
//    if (it > 1) {
//      for (int i = 0; i < it-1; i++) {
//        prop_corr_offsetY = prop_corr_offsetY+nts-i;
//      }       
//    }
//    //System.out.println("ts: "+ts);
//    corrY = propY0F-propY0C + prop_corr_Start_time.get(ts+prop_corr_offsetY-it)+60+prop_corr_start_offsetY;       
//    
//    
//    corrector.setLayoutY(corrY);
//    
//    return corrector;
//  }  
//  
//  
//  private ObservableList getPropCoarseInfo(int winIdx){
//    // save the percentage info of coarse props
//    final ObservableList prop_c_info = FXCollections.observableArrayList();
//    final List<Float> prop_c_percent_list = new ArrayList<>();
//    final List<Float> prop_c_Start_time_list = new ArrayList<>();
//    final List<Float> prop_c_duration_list = new ArrayList<>();
//    float prop_c_duration = 0;
//    int prop_c_nts = 0;
//    
//    try {
//      // access a single parareal_profile_win-###.txt file every time interval in timeline
//      final String fileName = "parareal_profile_win-"+String.valueOf(winIdx)+".txt";
//      final RandomAccessFile file = new RandomAccessFile(casePath+fileName, "rw");
//      
//      final FileChannel fc = file.getChannel();
//      final ByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
//      final String in_mem_data = Charset.forName("UTF-8").decode(bb).toString();
//      
//      int pos_c = 0;
//      float duration = 0;
//      for(int it=1; it<=nit; it++){
//        for(int ts=it+(winIdx-1)*nts; ts<=nts+(winIdx-1)*nts; ts++){
//          pos_c = 4+(2*nts-it+2)*(it-1)+((ts-nts*(winIdx-1))-it)*2;// line number of the coarse propagator
//          prop_c_percent_list.add(Float.parseFloat(in_mem_data.substring(pos_c*60+percent_idx_start, pos_c*60+percent_idx_end)));
//          prop_c_Start_time_list.add(Float.parseFloat(in_mem_data.substring(pos_c*60+start_time_idx_start, pos_c*60+start_time_idx_end)));
//          
//          duration = Float.parseFloat(in_mem_data.substring(pos_c*60+duration_idx_start, pos_c*60+duration_idx_end));
//          prop_c_duration_list.add(duration);
//          
//          prop_c_duration = prop_c_duration + duration;
//          if (duration != 0) {
//            prop_c_nts = prop_c_nts+1;
//          }
//        }
//      }
//      // extra iteration: NExtraProp=nts-nit
//      pos_c = 4+(2*nts-(nit+1)+2)*((nit+1)-1)+(((nit+1+(winIdx-1)*nts)-nts*(winIdx-1))-(nit+1))*2;
//      for(int i=1; i<=nts-nit; i++){
//        prop_c_percent_list.add(Float.parseFloat(in_mem_data.substring((pos_c+(i-1))*60+percent_idx_start, (pos_c+(i-1))*60+percent_idx_end)));
//        prop_c_Start_time_list.add(Float.parseFloat(in_mem_data.substring((pos_c+(i-1))*60+start_time_idx_start, (pos_c+(i-1))*60+start_time_idx_end)));
//        
//        duration = Float.parseFloat(in_mem_data.substring((pos_c+(i-1))*60+duration_idx_start, (pos_c+(i-1))*60+duration_idx_end));
//        prop_c_duration_list.add(duration);
//        
//        prop_c_duration = prop_c_duration + duration;
//      }
//      
//    } catch (FileNotFoundException ex) { 
//      Exceptions.printStackTrace(ex);
//    } catch (IOException ex) {
//      Exceptions.printStackTrace(ex);
//    }
//    
//    prop_c_info.addAll(prop_c_percent_list,prop_c_duration,prop_c_nts,prop_c_Start_time_list,prop_c_duration_list);
//    return prop_c_info;
//  }
//  
//  private ObservableList getPropFineInfo(int winIdx){
//    // save the percentage info of coarse props
//    final ObservableList prop_f_info = FXCollections.observableArrayList();
//    final List<Float> prop_f_percent = new ArrayList<>();  
//    final List<Float> prop_f_Start_time = new ArrayList<>();
//    final List<Float> prop_f_duration_list = new ArrayList<>();    
//    final float[] prop_f_duration = new float[8];
//    final int[] prop_f_nts = new int[8];
//    
//    
//    
//    
//    try {
//      // access a single parareal_profile_win-###.txt file every time interval in timeline
//      final String fileName = "parareal_profile_win-"+String.valueOf(winIdx)+".txt";
//      final RandomAccessFile file = new RandomAccessFile(casePath+fileName, "rw");
//      
//      final FileChannel fc = file.getChannel();
//      final ByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
//      final String in_mem_data = Charset.forName("UTF-8").decode(bb).toString();
//      
//      int pos_f = 0;
//      int prop_f_idx = 0;
//      float duration = 0;
//      for(int it=1; it<=nit; it++){
//        for(int ts=it+(winIdx-1)*nts; ts<=nts+(winIdx-1)*nts; ts++){
//          pos_f = 4+(2*nts-it+2)*(it-1)+((ts-nts*(winIdx-1))-it)*2 + 1;// line number of the coarse propagator
//          prop_f_percent.add(Float.parseFloat(in_mem_data.substring(pos_f*60+percent_idx_start, pos_f*60+percent_idx_end)));
//          prop_f_Start_time.add(Float.parseFloat(in_mem_data.substring(pos_f*60+start_time_idx_start, pos_f*60+start_time_idx_end)));
//          
//          duration = Float.parseFloat(in_mem_data.substring(pos_f*60+duration_idx_start, pos_f*60+duration_idx_end));
//          prop_f_duration_list.add(duration);
//          
//          prop_f_idx = Integer.parseInt(in_mem_data.substring(pos_f*60+prop_f_idx_start, pos_f*60+prop_f_idx_end).trim());
//          
//          if (prop_f_idx != 0) {
//            duration = Float.parseFloat(in_mem_data.substring(pos_f*60+duration_idx_start, pos_f*60+duration_idx_end));
//            prop_f_duration[prop_f_idx-1] = prop_f_duration[prop_f_idx-1] + duration;
//            if (duration != 0) {
//              prop_f_nts[prop_f_idx-1] = prop_f_nts[prop_f_idx-1]+1;
//            }            
//          }
//        }
//      }
//    } catch (FileNotFoundException ex) {
//      Exceptions.printStackTrace(ex);
//    } catch (IOException ex) {
//      Exceptions.printStackTrace(ex);
//    }
//    prop_f_info.addAll(prop_f_percent,prop_f_duration,prop_f_nts,prop_f_Start_time,prop_f_duration_list);
//    
//    return prop_f_info;
//  }  
//  
//  private void initPararealSolutionWorkflowGlobalInfo(){
//    try {
//      final String fileName = "parareal_profile_win-1.txt";// winIdx = 1
//      final RandomAccessFile file = new RandomAccessFile(casePath+fileName, "rw");
//      // get: NIT, NTS
//      final FileChannel fc = file.getChannel();
//      final ByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
//      final String in_mem_data_parareal_init = Charset.forName("UTF-8").decode(bb).toString();
//      // set global vars
//      //--line#1
//      nit = Integer.parseInt(in_mem_data_parareal_init.substring(6, 9).trim());
//      nts = Integer.parseInt(in_mem_data_parareal_init.substring(16, 19).trim());   
//      nprop = Integer.parseInt(in_mem_data_parareal_init.substring(27, 30).trim());
//      //--line#2
//      maxwin = Integer.parseInt(in_mem_data_parareal_init.substring(18+60, 21+60).trim());
//      xtime = Float.parseFloat(in_mem_data_parareal_init.substring(29+60, 35+60).trim());
//      xspace = Float.parseFloat(in_mem_data_parareal_init.substring(44+60, 50+60).trim());
//      //--line#3
//      desblend = Float.parseFloat(in_mem_data_parareal_init.substring(11+120, 17+120).trim());
//      wscblend = Float.parseFloat(in_mem_data_parareal_init.substring(28+120, 34+120).trim());
//      
//    } catch (FileNotFoundException ex) {
//      Exceptions.printStackTrace(ex);
//    } catch (IOException ex) {
//      Exceptions.printStackTrace(ex);
//    }
//      
//
//  }  
//  
//}
