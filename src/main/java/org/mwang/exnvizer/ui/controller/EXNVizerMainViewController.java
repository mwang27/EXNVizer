/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mwang.exnvizer.ui.controller;

import com.panemu.tiwulfx.control.DetachableTab;
import com.panemu.tiwulfx.control.DetachableTabPane;
import com.sun.deploy.uitoolkit.impl.fx.HostServicesFactory;
import com.sun.javafx.application.HostServicesDelegate;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.util.Callback;
import org.mwang.exnmonitor.helper.RecentItems;
import org.mwang.exnmonitor.ui.controller.EXNMonitorMainViewController;
import org.mwang.exnparareal.ui.controller.EXNPararealMainViewController;

/**
 * @author mwang
 */
public class EXNVizerMainViewController implements Initializable {
  

  @FXML
  private DetachableTabPane detachEXNViewTabPane;
  @FXML
  private MenuItem openEXNCaseMenuItem;
  @FXML
  private MenuItem openMultipleDBMenuItem;
  @FXML
  private Menu openRecentDBsSubMenu; 
  @FXML
  private MenuItem closeAllDBConnectionsMenuItem;
  @FXML
  private MenuItem removeAllDBFilesMenuItem;
  @FXML
  private MenuItem mergeDBMenuItem;
  @FXML
  private CheckMenuItem showEXNMonitorView;
  @FXML
  private CheckMenuItem showEXNPararealView; 
  @FXML
  private MenuItem wikiMenuItem;

  
  private final Preferences prefNode = Preferences.userRoot();
  private final RecentItems recentFilePaths = new RecentItems(4, prefNode);
  
  private DetachableTab exnmonitorTabGlobal;
  private EXNMonitorMainViewController exnmonitorControllerGlobal;
  
  private DetachableTab exnpararealTabGlobal;
  private EXNPararealMainViewController exnpararealControllerGlobal;
  
  private MergeSqldbDialogController mergeSqldbController;
  private AnchorPane mergeDBContentPane;
  private ExecutorService executorService;
  private HostServicesDelegate hostServices;
  
  private Map exncasePathMap;

  
  public void setExecutorService(ExecutorService executorService) {
    this.executorService = executorService;
  }

  public void setHostServices(HostServicesDelegate hostServices) {
    this.hostServices = hostServices;
  }
  
  
  public RecentItems getRecentFilePaths() {
    return recentFilePaths;
  }
  
  
  
  
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    exncasePathMap = new HashMap();
    
    
    initEXNViewTabPaneSceneFactory();
    
    initEXNMonitorGlobalView();
    
    initEXNPararealGlobalView();
         
    initMenuItemActions(executorService);
  }  
  
  
  //============================ 
  //* EXNVizer TabPane Methods *
  //============================ 
  private void initEXNViewTabPaneSceneFactory(){
    detachEXNViewTabPane.setSceneFactory(new Callback<DetachableTabPane, Scene>() {
      @Override
      public Scene call(DetachableTabPane p) {
        p.setPrefSize(400, 343);
        StackPane stackpane = new StackPane();
        stackpane.setStyle("-fx-background-color:rgb(56, 59, 76);");
        stackpane.getChildren().add(p);

        Scene scene = new Scene(stackpane);
        return scene;
      }
    });
  }  
  
  
  //===============================
  //* Init EXNMonitor Global View *
  //===============================
  private void initEXNMonitorGlobalView(){
    try {
      // create EXNMonitor Global View Tab
      exnmonitorTabGlobal = new DetachableTab("EXNMonitor Global");
      exnmonitorTabGlobal.setDetachable(false);
      exnmonitorTabGlobal.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/org/mwang/exnvizer/resource/image/exnmonitorTab.png"))));
      exnmonitorTabGlobal.setStyle("-fx-text-fill:#dad1d1;");
      detachEXNViewTabPane.getTabs().addAll(exnmonitorTabGlobal);
      
      // load FXML file, set custom Controller, and load rootNode
      final FXMLLoader fxloader = new FXMLLoader(getClass().getResource("/org/mwang/exnmonitor/ui/view/EXNMonitorMainView.fxml"));
      exnmonitorControllerGlobal= new EXNMonitorMainViewController();
      exnmonitorControllerGlobal.setExnvizerMainViewController(this);
      fxloader.setController(exnmonitorControllerGlobal);
      final BorderPane rootNode = (BorderPane)fxloader.load();
      exnmonitorTabGlobal.setContent(rootNode);    
      detachEXNViewTabPane.getSelectionModel().select(exnmonitorTabGlobal);
      
    } catch (IOException ex) {
      Logger.getLogger(EXNVizerMainViewController.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  

  //================================
  //* Init EXNParareal Global View *
  //================================
  private void initEXNPararealGlobalView(){
    try {
      // create EXNParareal Global View Tab
      exnpararealTabGlobal = new DetachableTab("EXNParareal Global");
      exnpararealTabGlobal.setDetachable(false);
      exnpararealTabGlobal.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/org/mwang/exnvizer/resource/image/exnpararealTab.png"))));
      exnpararealTabGlobal.setStyle("-fx-text-fill:#dad1d1;");
      detachEXNViewTabPane.getTabs().addAll(exnpararealTabGlobal);
      
      // load FXML file, set custom Controller, and load rootNode
      final FXMLLoader fxloader = new FXMLLoader(getClass().getResource("/org/mwang/exnparareal/ui/view/EXNPararealMainView.fxml"));
      exnpararealControllerGlobal= new EXNPararealMainViewController();
      exnpararealControllerGlobal.setGlobalDetachEXNViewTabPane(detachEXNViewTabPane);
      exnpararealControllerGlobal.setEXNMonitorControllerGlobal(exnmonitorControllerGlobal);
      exnpararealControllerGlobal.setEXNMonitorTabGlobal(exnmonitorTabGlobal);
      exnpararealControllerGlobal.setEXNPararealTabGlobal(exnpararealTabGlobal);
      fxloader.setController(exnpararealControllerGlobal);
      final SplitPane rootNode = (SplitPane)fxloader.load();
      exnpararealTabGlobal.setContent(rootNode);    
      
    } catch (IOException ex) {
      Logger.getLogger(EXNVizerMainViewController.class.getName()).log(Level.SEVERE, null, ex);
    }
  }  
  
  
  //=============================
  //* EXNVizer MenuItem Methods *
  //=============================   
  private void initMenuItemActions(ExecutorService executorService){
    
    showEXNMonitorView.setSelected(true);
    showEXNMonitorView.selectedProperty().addListener(new ChangeListener<Boolean>() {
      @Override
      public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        if (!newValue) {
          detachEXNViewTabPane.getTabs().remove(exnmonitorTabGlobal);
        }
        if (newValue) {
          detachEXNViewTabPane.getTabs().addAll(0, FXCollections.observableArrayList(exnmonitorTabGlobal));
        }
      }
    });
    
    showEXNPararealView.setSelected(true);
    showEXNPararealView.selectedProperty().addListener(new ChangeListener<Boolean>() {
      @Override
      public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        if (!newValue) {
          detachEXNViewTabPane.getTabs().remove(exnpararealTabGlobal);
        }
        if (newValue) {
          if (detachEXNViewTabPane.getTabs().size() > 0) {
            detachEXNViewTabPane.getTabs().addAll(1, FXCollections.observableArrayList(exnpararealTabGlobal));
          } else{
            detachEXNViewTabPane.getTabs().addAll(0, FXCollections.observableArrayList(exnpararealTabGlobal));
          }
        }
      }
    });
    
    openMultipleDBMenuItem.setOnAction((ActionEvent event) -> {
      openMultipleDBFiles();
    });
    
    openEXNCaseMenuItem.setOnAction((ActionEvent event) -> {
      openEXNCase();
    });
    
    openRecentDBFiles();
    
    closeAllDBConnectionsMenuItem.setOnAction((ActionEvent event) -> {
      closeAllDBConnections();
    });
    
    removeAllDBFilesMenuItem.setOnAction((ActionEvent event) -> {
      removeAllDBFiles();
    });
    
    wikiMenuItem.setOnAction((ActionEvent event) ->{
      goToWikiSite();
    });
    
    
    final FXMLLoader fxloader = new FXMLLoader(getClass().getResource("/org/mwang/exnvizer/ui/view/MergeSqldbDialog.fxml"));
    mergeSqldbController = new MergeSqldbDialogController();
    mergeSqldbController.setEXNMonitorControllerGlobal(exnmonitorControllerGlobal);
    mergeSqldbController.setExecutorService(executorService);
    fxloader.setController(mergeSqldbController);
    try {
      mergeDBContentPane = fxloader.load();
    } catch (IOException ex) {
      Logger.getLogger(EXNVizerMainViewController.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    mergeDBMenuItem.setOnAction((ActionEvent event) -> {
      mergeDBFiles();
    });
    
  }
  
  private void openMultipleDBFiles(){
    detachEXNViewTabPane.getSelectionModel().select(exnmonitorTabGlobal);
    exnmonitorControllerGlobal.openMultipleDBFiles();
  }

  private void openEXNCase(){
    final DirectoryChooser exncaseDirChooser = new DirectoryChooser();
    exncaseDirChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    
    final Alert EXNCaseSelAlert = new Alert(Alert.AlertType.INFORMATION);
    EXNCaseSelAlert.setHeaderText(null);
    final File exncaseDir;
    try {
      exncaseDir = exncaseDirChooser.showDialog(null);
      // if dbFiles is not null
      final String exncasePath = exncaseDir.getPath();
      final String exncaseName = exncasePath.substring(exncasePath.lastIndexOf("/")+1);
//      System.out.println("exncaseName: "+exncaseName);
      exncasePathMap.put(exncaseName, exncasePath);
      exnpararealControllerGlobal.setEXNCasePathMap(exncasePathMap);
      exnpararealControllerGlobal.updateEXNCaseTreeView(exncasePath);
      detachEXNViewTabPane.getSelectionModel().select(exnpararealTabGlobal);
    } catch (NullPointerException ex) {// should log the null pointer!
      EXNCaseSelAlert.setTitle("Please Select an EXNCase Folder!");
      EXNCaseSelAlert.setContentText("No EXNCase Selected.");
      EXNCaseSelAlert.showAndWait();       
    }
  }
  
  private void openRecentDBFiles(){
    //--maintain a recently opened file list 
    if (!recentFilePaths.getItems().isEmpty()) {
      recentFilePaths.getItems().forEach(new Consumer<String>() {
        @Override
        public void accept(String recentFilePath) {
          final MenuItem openRecentDBMenuItem = new MenuItem(recentFilePath);
          openRecentDBsSubMenu.getItems().add(openRecentDBMenuItem);
          openRecentDBMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              final List recentFilePathList = new ArrayList<>();
              recentFilePathList.add(recentFilePath);
              detachEXNViewTabPane.getSelectionModel().select(exnmonitorTabGlobal);
              exnmonitorControllerGlobal.updateDBManagerTreeView(recentFilePathList);
            }
          });          
        }
      });
    }    
  }
  
  private void closeAllDBConnections(){
    exnmonitorControllerGlobal.closeAllDBConnections();
  }
  
  private void removeAllDBFiles(){
    exnmonitorControllerGlobal.removeAllDBFiles();
  }
  
  private void goToWikiSite(){
    
    String url = "http://blog.envenio.ca/wiki";
    
    Task wikiTask = new Task() {
      @Override
      protected Object call() throws Exception {
        hostServices.showDocument(url);
        return 0;
      }
    };
    
    executorService.submit(wikiTask);

  }
  
  public void mergeDBFiles(){
    final Alert mergeDBDialog = new Alert(Alert.AlertType.INFORMATION);
    mergeDBDialog.setResizable(true);
    mergeDBDialog.getDialogPane().setStyle("-fx-background-color: rgb(56, 59, 76);");
    mergeDBDialog.getDialogPane().setContent(mergeDBContentPane);
    mergeDBDialog.setTitle("Add Sqldb Files to Merge:");
    mergeDBDialog.setHeaderText(null);
    mergeDBDialog.setGraphic(null);
    
//    final ButtonType okBtn = new ButtonType("Done", ButtonBar.ButtonData.OK_DONE);
//    mergeDBDialog.getButtonTypes().setAll(okBtn);
//    final Optional<ButtonType> result = mergeDBDialog.showAndWait();
//    if (result.get() == okBtn) {
//      System.out.println("Done.");
//    } 
    
    mergeDBDialog.showAndWait();
  } 
  
  
  
}
