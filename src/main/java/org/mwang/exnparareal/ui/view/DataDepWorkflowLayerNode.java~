/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mwang.exnparareal.ui.view;

import com.panemu.tiwulfx.control.DetachableTab;
import com.panemu.tiwulfx.control.DetachableTabPane;
import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.mwang.exnmonitor.ui.controller.EXNMonitorMainViewController;


/**
 *
 * @author mwang
 */
public class DataDepWorkflowLayerNode extends StackPane{
  
  private final String colorString;
  private final String caseName;
  private final String caseDirPath;
  private final EXNMonitorMainViewController exnmonitorControllerGlobal;
  private final DetachableTabPane detachEXNViewTabPane;
  private final DetachableTab exnmonitorTabGlobal;
  private final int it;
  private final int ts;
  
  
  
  /**
   * Constructor
   * @param progress
   * @param propType
   * @param colorString
   * @param exnmonitorControllerGlobal
   * @param detachEXNViewTabPane
   * @param exnmonitorTabGlobal
   * @param caseName
   * @param caseDirPath
   * @param it
   * @param ts
   * @param maxwin 
   */
  public DataDepWorkflowLayerNode(
          double progress, String propType, String colorString, int maxwin, 
          EXNMonitorMainViewController exnmonitorControllerGlobal, 
          DetachableTabPane detachEXNViewTabPane, DetachableTab exnmonitorTabGlobal, 
          String caseName, String caseDirPath, int it, int ts
  ){
    this.colorString = colorString;
    this.exnmonitorControllerGlobal = exnmonitorControllerGlobal;
    this.detachEXNViewTabPane = detachEXNViewTabPane;
    this.exnmonitorTabGlobal = exnmonitorTabGlobal;
    this.caseName = caseName;
    this.caseDirPath = caseDirPath;
    this.it = it;
    this.ts = ts;
    
    
    double topPad = 0;
    double rightPad = 0;
    double bottomPad = 45;
    double leftPad = 45;
    
    if (propType.equals("fine")) {
      topPad = 110;
      setPadding(new Insets(topPad, rightPad, bottomPad, leftPad));
      final ProgressIndicator pi = createDBProgressIndicator(progress/100.0, propType);
//      final Label propLabel = new Label("ts");
      getChildren().setAll(pi);
    }
    
    if(propType.equals("coarse")){
      topPad = 70;
      setPadding(new Insets(topPad, rightPad, bottomPad, leftPad));
      final ProgressIndicator pi = createDBProgressIndicator(progress/100.0, propType);
//      final Label propLabel = new Label("ts");
      getChildren().setAll(pi);
    }
    
    toFront();
    
  }

  
  
  private ProgressIndicator createDBProgressIndicator(double progress, String propType){
    final ProgressIndicator pi = new ProgressIndicator(progress);
    pi.getStylesheets().add(getClass().getResource("/org/mwang/exnparareal/resource/style/EXNPararealViewStyle.css").toExternalForm());
    pi.setMinSize(28, 28);
    pi.setStyle("-fx-progress-color: "+colorString+"; -fx-accent:"+colorString+";");
    pi.setId(it+"-ts_"+ts);
    
    final String[] dbNameList = getDBNameList(propType);
    final String dbName = dbNameList[0];
    final int endIdx = dbName.indexOf("-");
    final String piDBNamePrefix = dbName.substring(0, endIdx); 
    final String piDBName = piDBNamePrefix+"-it_"+pi.getId()+".sqldb";
    
    pi.setOnMouseEntered(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        setCursor(Cursor.HAND);
        //--install tooltip
        final Tooltip tooltip = new Tooltip(piDBName);
        hackTooltipStartTiming(tooltip);// Set tooltip delay-time          
        Tooltip.install(pi, tooltip);        
      }
    });
    
    pi.setOnMouseExited(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        setCursor(Cursor.DEFAULT);
        Runtime.getRuntime().gc();
        Runtime.getRuntime().freeMemory(); 
      }
    });
    
    pi.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        // use this prop id to find the related sql file.
        //System.out.println("Event source: "+event.getSource().toString());

        final String propagatorDBPath = caseDirPath+"/"+propType+"/"+piDBName;
        final String propagatorDBName = propagatorDBPath.substring(propagatorDBPath.lastIndexOf("/")+1);
        final List propagatorDBPathList = new ArrayList<>();
        propagatorDBPathList.add(propagatorDBPath);

        exnmonitorControllerGlobal.updateSqldbTreeItemColor(colorString);
        exnmonitorControllerGlobal.updateDBManagerTreeView(propagatorDBPathList);
        detachEXNViewTabPane.getSelectionModel().select(exnmonitorTabGlobal);
      }
    });
    
    return pi;
  }
  
  private String[] getDBNameList(String propType){
    //-- Get all coarse-and-fine database namelist for opening matching db in ExnMonitor
    final File dbFolder = new File(caseDirPath+"/"+propType); 
    final String[] dbNameList = dbFolder.list(new FilenameFilter() {
      @Override
      public boolean accept(File current, String name) {
        //System.out.println(name);
        return new File(current, name).isFile();
      }
    }); 
    
    return dbNameList;
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
//      Logger.getLogger(EXNMonitorMainViewController.class.getName()).log(Level.SEVERE, null, ex);
    }
  }    
  
}
