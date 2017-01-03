package org.mwang.exnparareal.ui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.panemu.tiwulfx.control.DetachableTab;
import com.panemu.tiwulfx.control.DetachableTabPane;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.util.Callback;
import org.fxmisc.richtext.StyleClassedTextArea;
import org.fxmisc.richtext.StyledTextArea;
import org.fxmisc.richtext.model.EditableStyledDocument;
import org.mwang.exnmonitor.ui.controller.EXNMonitorMainViewController;

/**
 * @author envenio
 */

public class EXNPararealMainViewController implements Initializable{
 
  private int nit;
  private int nts;
  private int nprop; 
  private int maxwin;
  private float xtime;
  private float xspace;
  private float desblend;
  private float wscblend;
  
  @FXML
  private JFXButton openEXNCaseBtn;
  @FXML
  private VBox EXNCaseVBox;
  @FXML
  private TabPane exnpararealTabPane;
  
  private TreeView<String> exncaseTreeView;  
  
  //-- global vars  
  private String caseDirPath;
  private String caseName;
  private String casePath;
  private DetachableTabPane detachEXNViewTabPane;
  private DetachableTab exnpararealTabGlobal;
  private DetachableTab exnmonitorTabGlobal;
  private EXNMonitorMainViewController exnmonitorControllerGlobal;
  
  private Map exncasePathMap;
  private Map exncaseTabMap;

  public void setEXNCasePathMap(Map exncasePathMap) {
    this.exncasePathMap = exncasePathMap;
  }

  public void setGlobalDetachEXNViewTabPane(DetachableTabPane detachEXNViewTabPane){
    this.detachEXNViewTabPane = detachEXNViewTabPane;
  }  

  public void setEXNPararealTabGlobal(DetachableTab exnpararealTabGlobal) {
    this.exnpararealTabGlobal = exnpararealTabGlobal;
  }
  
  public void setEXNMonitorControllerGlobal(EXNMonitorMainViewController exnmonitorControllerGlobal){
    this.exnmonitorControllerGlobal = exnmonitorControllerGlobal;
  }

  public void setEXNMonitorTabGlobal(DetachableTab exnmonitorTabGlobal) {
    this.exnmonitorTabGlobal = exnmonitorTabGlobal;
  }
  
  
  
  public void setCaseDirPath(String caseDirPath){
    this.caseDirPath = caseDirPath;
  }
  
  public void setCaseName(String CaseName){
    this.caseName = CaseName;
  }

  
  
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    exncaseTabMap = new HashMap();
    openEXNCaseBtn.setOnAction((ActionEvent event) -> {
      if (exncasePathMap == null) {
        exncasePathMap = new HashMap();
        openEXNCase();
      } else {
        openEXNCase();
      }
    });
    
    casePath = caseDirPath+"/";

    initEXNCaseExplorer();
    
    exncaseTreeView.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
          final TreeItem profileTextTreeItem = exncaseTreeView.getSelectionModel().getSelectedItem();
          openTextViewer(profileTextTreeItem);
        }
      }
    });

  }
  
  
  
  //=============================
  //* EXNVizer MenuItem Methods *
  //============================= 
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
      
      updateEXNCaseTreeView(exncasePath);
    } catch (NullPointerException ex) {// should log the null pointer!
      EXNCaseSelAlert.setTitle("Please Select an EXNCase Folder!");
      EXNCaseSelAlert.setContentText("No EXNCase Selected.");
      EXNCaseSelAlert.showAndWait();       
    }
  }


  
  //=========================
  //* Init EXNCase TreeView *
  //=========================
  private void initEXNCaseExplorer(){
    final TreeItem<String> exncaseTreeroot = new TreeItem<>("exncaseTreeroot");
    exncaseTreeView = new TreeView<>(exncaseTreeroot);
    exncaseTreeView.setShowRoot(false);
    EXNCaseVBox.getChildren().add(exncaseTreeView);
    VBox.setVgrow(exncaseTreeView, Priority.ALWAYS);    
  } 
  

  
  //===========================
  //* Update EXNCase TreeView *
  //===========================  
  public void updateEXNCaseTreeView(String exncasePath) {
    // treeview level#1: exncase name treeitems
    final String exncaseName = exncasePath.substring(exncasePath.lastIndexOf("/")+1);
    final TreeItem<String> exncaseTreeItem = new TreeItem<>(exncaseName);
    exncaseTreeItem.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/org/mwang/exnparareal/resource/image/exncase.png"))));
    exncaseTreeView.getRoot().getChildren().add(exncaseTreeItem);
    setEXNCaseTreeCellFactory();
    
    // treeview level#2: parareal profilind text file treeitems
    updateProfileTreeItems(exncaseTreeItem, exncasePath);
    
    // treeview level#2: coarse sqldb directory
    updateSqldbTreeItems("coarse", exncaseTreeItem, exncasePath);
    
    // treeview level#2: fine sqldb directory
    updateSqldbTreeItems("fine", exncaseTreeItem, exncasePath);
    
    exncaseTreeItem.setExpanded(true);
  }  
  
  private void updateProfileTreeItems(TreeItem<String> exncaseTreeItem, String exncasePath){
    final File exncaseDir = new File(exncasePath);
    final File[] profileFiles = exncaseDir.listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return name.contains("parareal_profile")&&name.toLowerCase().endsWith(".txt")&&(!name.contains("bak")); 
      }
    });
    for (File profileFile : profileFiles) {
      final String profileName = profileFile.getPath().substring(profileFile.getPath().lastIndexOf("/")+1);
      final TreeItem<String> profileTreeItem = new TreeItem<>(profileName);
      profileTreeItem.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/org/mwang/exnparareal/resource/image/exnProfile.png"))));
      exncaseTreeItem.getChildren().add(profileTreeItem);
    }    
  }
  
  private void updateSqldbTreeItems(String dbDirName, TreeItem<String> exncaseTreeItem, String exncasePath){
    final TreeItem<String> sqldbDirTreeItem = new TreeItem<>(dbDirName);
    sqldbDirTreeItem.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/org/mwang/exnparareal/resource/image/folder.png"))));
    setExpandProperty(sqldbDirTreeItem);
    exncaseTreeItem.getChildren().add(sqldbDirTreeItem);
    
    final File sqldbDir = new File(exncasePath+"/"+dbDirName);
    final File[] sqldbFiles = sqldbDir.listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return name.toLowerCase().endsWith(".sqldb");
      }
    });

    final SortedList sqldbFileSorted = FXCollections.observableArrayList(sqldbFiles).sorted();
    for (Iterator iterator = sqldbFileSorted.iterator(); iterator.hasNext();) {
      File sqldbFile = (File) iterator.next();
      final String sqldbName = sqldbFile.getPath().substring(sqldbFile.getPath().lastIndexOf("/")+1);
      final TreeItem<String> sqldbFileTreeItem = new TreeItem<>(sqldbName);
      sqldbFileTreeItem.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/org/mwang/exnparareal/resource/image/sqldb.png"))));
      sqldbDirTreeItem.getChildren().add(sqldbFileTreeItem);
    }     
  }
  
  private File[] sortByNumber(File[] files) {
    Arrays.sort(files, new Comparator<File>() {
      @Override
      public int compare(File f1, File f2) {
        try {
            int i1 = Integer.parseInt(f1.getName());
            int i2 = Integer.parseInt(f2.getName());
            return i1 - i2;
        } catch(NumberFormatException e) {
            throw new AssertionError(e);
        }
      }
    });
    
    return files;
  }
  
  private void setExpandProperty(TreeItem<String> dirTreeItem){
    dirTreeItem.expandedProperty().addListener(new ChangeListener<Boolean>() {
      @Override
      public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        if (newValue) {
          dirTreeItem.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/org/mwang/exnparareal/resource/image/folderOpen.png"))));
        } else if (!newValue) {
          dirTreeItem.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/org/mwang/exnparareal/resource/image/folder.png"))));
        }
      }
    });    
  }
  
  private void setEXNCaseTreeCellFactory(){
    exncaseTreeView.setCellFactory(new Callback<TreeView<String>, TreeCell<String>>() {
      @Override
      public TreeCell<String> call(TreeView<String> param) {
        return new EXNTreeCell();
      }
    });    
  }

  
  
  //===================================
  //* Load EXNParareal workflow views *
  //===================================  
  private VBox loadWorkflowView(String exncasePath, String exncaseName){
    // load FXML file and set custom Controller
    final FXMLLoader workflowViewFXMLLoader = new FXMLLoader(getClass().getResource("/org/mwang/exnparareal/ui/view/PararealSolutionWorkflowView.fxml"));
    final PararealSolutionWorkflowViewController workflowController= new PararealSolutionWorkflowViewController();
    workflowController.setGlobalDetachEXNViewTabPane(detachEXNViewTabPane);
    workflowController.setCaseDirPath(exncasePath);
    workflowController.setCaseName(exncaseName);
    workflowViewFXMLLoader.setController(workflowController);
      
    VBox workflowViewRootNode = null;
    try {
      // load rootNode
      workflowViewRootNode = (VBox)workflowViewFXMLLoader.load();
    } catch (IOException ex) {
      Logger.getLogger(EXNPararealMainViewController.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    return workflowViewRootNode;
  }
  
  private VBox loadScheduleView(String exncasePath){
    // load FXML file and set custom Controller
    final FXMLLoader scheduleViewFXMLLoader = new FXMLLoader(getClass().getResource("/org/mwang/exnparareal/ui/view/PropagatorScheduleTimelineView.fxml"));
    final PropagatorScheduleTimelineViewController scheduleController= new PropagatorScheduleTimelineViewController();
    scheduleController.setCasePath(exncasePath+"/");
    scheduleViewFXMLLoader.setController(scheduleController);

    // load rootNode
    VBox scheduleViewRootNode =  null;
    try {
      scheduleViewRootNode = (VBox)scheduleViewFXMLLoader.load();
    } catch (IOException ex) {
      Logger.getLogger(EXNPararealMainViewController.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    return scheduleViewRootNode;
  }
  
  private VBox loadStatisticsView(String exncasePath){
    // load FXML file and set custom Controller
    final FXMLLoader statisticsViewFXMLLoader = new FXMLLoader(getClass().getResource("/org/mwang/exnparareal/ui/view/PerformanceStatisticsView.fxml"));
    final PerformanceStatisticsViewController statisticsController= new PerformanceStatisticsViewController();
    statisticsController.setCasePath(exncasePath+"/");
    statisticsViewFXMLLoader.setController(statisticsController);

    // load rootNode
    VBox statisticsViewRootNode = null;
    try {
      statisticsViewRootNode = (VBox)statisticsViewFXMLLoader.load();
    } catch (IOException ex) {
      Logger.getLogger(EXNPararealMainViewController.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    return statisticsViewRootNode;
  }    
  
  
  
  //==========================
  //* EXNCase TreeView Class *
  //========================== 
  private class EXNTreeCell extends TextFieldTreeCell<String> {
    
    private final ContextMenu exncaseDirContextMenu;
    private final ContextMenu exncaseNullDirContextMenu;
    private final ContextMenu sqldbContextMenu;
    private final ContextMenu profilingFileContextMenu;
    private boolean flag = false;
    
    public EXNTreeCell() {
      // instantiate the root context menu
      final MenuItem runEXNPararealMenuItem = new MenuItem("Run Within EXNParareal");
      final MenuItem runEXNMonitorMenuItem = new MenuItem("Run Within EXNMonitor");
      final MenuItem removeEXNCaseMenuItem = new MenuItem("Remove this EXNCase");
      final MenuItem removeNullEXNCaseMenuItem = new MenuItem("Remove this EXNCase");
      final MenuItem viewProfilingFileMenuItem = new MenuItem("View Profiling Text File");
      exncaseDirContextMenu = new ContextMenu(runEXNPararealMenuItem,removeEXNCaseMenuItem);
      sqldbContextMenu = new ContextMenu(runEXNMonitorMenuItem);
      exncaseNullDirContextMenu = new ContextMenu(removeNullEXNCaseMenuItem);
      profilingFileContextMenu = new ContextMenu(viewProfilingFileMenuItem);
      
      // define menuitem event
      runEXNPararealMenuItem.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
          // update 
          detachEXNViewTabPane.getSelectionModel().select(exnpararealTabGlobal);
          final String exncaseName = getTreeItem().getValue();
          final String exncasePath = exncasePathMap.get(exncaseName).toString();
          updateEXNPararealScene(exncasePath, exncaseName);
        }
      });
      
      runEXNMonitorMenuItem.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
          final String exncaseName = getTreeItem().getParent().getParent().getValue();
          System.out.println("exncaseName: "+exncaseName);
          final String exncasePath = exncasePathMap.get(exncaseName).toString();     
          System.out.println("exncasePath: "+exncasePath);
          final String sqldbPath = exncasePath+"/"+getTreeItem().getParent().getValue()+"/"+getTreeItem().getValue(); //full abs path of a sqldb file
          System.out.println(sqldbPath); 
          getTreeItem().setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/org/mwang/exnparareal/resource/image/sqldbLive.png"))));
          // update EXNMonitor Global
          detachEXNViewTabPane.getSelectionModel().select(exnmonitorTabGlobal);            
          updateEXNMonitorScene(sqldbPath, getTreeItem().getValue());
        }
      });
      
      removeEXNCaseMenuItem.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
          exnpararealTabPane.getTabs().remove(exncaseTabMap.get(getTreeItem().getValue()));
          exncaseTreeView.getRoot().getChildren().remove(getTreeItem());
        }
      });
      
      removeNullEXNCaseMenuItem.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
          exnpararealTabPane.getTabs().remove(exncaseTabMap.get(getTreeItem().getValue()));
          exncaseTreeView.getRoot().getChildren().remove(getTreeItem());
        }
      });      
      
      viewProfilingFileMenuItem.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
          openTextViewer(getTreeItem());
        }
      });
    }
    
    
    private void updateEXNPararealScene(String exncasePath, String exncaseName){
      setCaseDirPath(exncasePath);
      setCaseName(exncaseName);
      
      final Tab caseTab = (Tab)createEXNPararealCaseTab(exncasePath,exncaseName);
      exncaseTabMap.put(exncaseName,caseTab);
      exnpararealTabPane.getTabs().addAll(caseTab);
      exnpararealTabPane.getSelectionModel().select(caseTab);
      
    }
    
    private Tab createEXNPararealCaseTab(String exncasePath, String exncaseName){
      final Tab exnCaseTab = new Tab(exncaseName);
      exnCaseTab.setContent(createCaseTabPane(exncasePath, exncaseName));
      
      return exnCaseTab;
    }
    
    private TabPane createCaseTabPane(String exncasePath, String exncaseName){
      final TabPane exncaseTabPane = new TabPane();
      exncaseTabPane.setSide(Side.BOTTOM);
      
      // create workflow, propagator timeline, and performance tabs
      final ToggleButton workflowToggleButton = new ToggleButton("Off-");
      workflowToggleButton.getStyleClass().add("caseTogBtn");
      final Tab workflowTab = new Tab("Parareal Workflow View");
      workflowTab.setClosable(false);
      workflowTab.setGraphic(workflowToggleButton);
      workflowToggleButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
          if (newValue) {
            workflowToggleButton.setText("On-");
            workflowTab.setContent(loadWorkflowView(exncasePath, exncaseName));
          }else{
            workflowToggleButton.setText("Off-");
            workflowTab.setContent(null);
          }
        }
      });      
      workflowToggleButton.setSelected(true);
      
      final ToggleButton propScheduleToggleButton = new ToggleButton("Off-");
      propScheduleToggleButton.getStyleClass().add("caseTogBtn");
      final Tab propScheduleTab = new Tab("Propagator Scheduling View");
      propScheduleTab.setClosable(false);
      propScheduleTab.setGraphic(propScheduleToggleButton);
      propScheduleToggleButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
          if (newValue) {
            propScheduleToggleButton.setText("On-");
            propScheduleTab.setContent(loadScheduleView(exncasePath));
          }else{
            propScheduleToggleButton.setText("Off-");
            propScheduleTab.setContent(null);
          }
        }
      });      
      
      final ToggleButton performanceToggleButton = new ToggleButton("Off-");
      performanceToggleButton.getStyleClass().add("caseTogBtn");
      final Tab performanceTab = new Tab("Parareal Performance View");  
      performanceTab.setClosable(false);
      performanceTab.setGraphic(performanceToggleButton);
      performanceToggleButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
          if (newValue) {
            performanceToggleButton.setText("On-");
            performanceTab.setContent(loadStatisticsView(exncasePath));
          }else{
            performanceToggleButton.setText("Off-");
            performanceTab.setContent(null);
          }
        }
      });      
      
      exncaseTabPane.getTabs().addAll(workflowTab, propScheduleTab, performanceTab);
      
      return exncaseTabPane;
    }
    
    
    private void updateEXNMonitorScene(String sqldbPath, String sqldbName){
      final List<String> sqldbPathList = new ArrayList<>();
      sqldbPathList.add(sqldbPath);
      exnmonitorControllerGlobal.updateSqldbTreeItemColor("whitesmoke");
      exnmonitorControllerGlobal.updateDBManagerTreeView(sqldbPathList);
//      System.out.println("sqldbPath for EXNMonitor: "+sqldbPath);
//      System.out.println("sqldbName for EXNMonitor: "+sqldbName);
    }     

    @Override
    public void updateItem(String item, boolean empty) {
      super.updateItem(item, empty);

      // if the item is not empty and is a exncase dir...
      if ( !empty ) {
        getTreeItem().getChildren().forEach(new Consumer<TreeItem<String>>() {
          @Override
          public void accept(TreeItem<String> t) {
            if (t.getValue().contains("parareal_profile")){
              flag = true;
            }
          }
        });
        if (getTreeItem().getValue().contains("sqldb")) {
          setContextMenu(sqldbContextMenu);
        }
        else if(flag) {
          setContextMenu(exncaseDirContextMenu);
        } 
        else if(getTreeItem().getValue().contains("parareal_profile")) {
          // the parareal profiling text file
          setContextMenu(profilingFileContextMenu);
          
        }
        else {
          setContextMenu(exncaseNullDirContextMenu);
        } 
      }
    }
  }

  private void openTextViewer(TreeItem<String> profileTreeItem){
    try {
      // update
      detachEXNViewTabPane.getSelectionModel().select(exnpararealTabGlobal);
      final Tab profileTextTab = new Tab(profileTreeItem.getValue());
      exnpararealTabPane.getTabs().addAll(profileTextTab);
      exnpararealTabPane.getSelectionModel().select(profileTextTab);

      final String exncaseName = profileTreeItem.getParent().getValue();
      final String exncasePath = exncasePathMap.get(exncaseName).toString();
      final String fileName = profileTreeItem.getValue();
      final RandomAccessFile file = new RandomAccessFile(exncasePath+"/"+fileName, "rw");
      // get: NIT, NTS
      final FileChannel fc = file.getChannel();
      final ByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
      final String profile_in_mem_data = Charset.forName("UTF-8").decode(bb).toString();

      final JFXTextArea textArea = new JFXTextArea(profile_in_mem_data);
      profileTextTab.setContent(textArea);

    } catch (FileNotFoundException ex) {
      Logger.getLogger(EXNPararealMainViewController.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      Logger.getLogger(EXNPararealMainViewController.class.getName()).log(Level.SEVERE, null, ex);
    }    
  }
  
  
  
  
  private void openDbDataViewer(){
    

    
    
    
  }
}
