package org.mwang.exnmonitor.ui.controller;

import com.google.common.base.Joiner;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.mchange.v2.c3p0.DataSources;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;
import com.vividsolutions.jts.simplify.DouglasPeuckerSimplifier;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import javafx.util.converter.NumberStringConverter;
import javax.imageio.ImageIO;
import javax.sql.DataSource;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.controlsfx.control.RangeSlider;
import org.gillius.jfxutils.chart.ChartPanManager;
import org.gillius.jfxutils.chart.ChartZoomManager;
import org.gillius.jfxutils.chart.FixedFormatTickFormatter;
import org.gillius.jfxutils.chart.JFXChartUtil;
import org.gillius.jfxutils.chart.StableTicksAxis;
import org.mwang.exnmonitor.model.FetchDBDataTask;
import org.mwang.exnmonitor.helper.DragResizer;
import org.mwang.exnmonitor.helper.RecentItems;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

/**
 * @author envenio
 */

public class EXNMonitorMainViewController1 implements Initializable {
  private static final Logger logger = Logger.getLogger(EXNMonitorMainViewController1.class.getName());
  
  // global data variables
  private String userhome;
  //private String selXFieldName;
  private ExecutorService databaseExecutor; 
  private ProgressBar progressBar;
  
  private TreeView<HBox> dbTreeView;
  private TreeItem<HBox> dbTreeRoot;  
  private String sqldbTreeItemColor;
  
  private final Preferences prefNode = Preferences.userRoot();
  private final RecentItems recentFilePaths = new RecentItems(4, prefNode);
  
  /* JavaFX control&layout UI elements */
  //--root pane
  @FXML
  private BorderPane rootBorderPane;

  //--FileMenu
  @FXML
  private MenuItem openMultipleDBMenuItem; 
  @FXML
  private Menu openRecentDBsSubMenu;  
  @FXML
  private MenuItem closeAllDBMenuItem;
  @FXML
  private MenuItem removeAllDBMenuItem;  
  //--EditMenu
  @FXML
  private MenuItem closeChartsMenuItem;
  //--ViewMenu
  @FXML
  private MenuItem basicViusalizerMenuItem;
  @FXML
  private MenuItem analyticalViusalizerMenuItem;   
  @FXML
  private MenuItem exportChartMenuItem;  
  //--ToolsMenu
  @FXML
  private MenuItem mergerDBMenuItem;
  
  //--Panes
  @FXML
  private VBox SQLiteTablesVBoxPane;
  @FXML
  private TabPane MonitorViewTabPane;  

  
  @FXML
  private Label fetchDataLabel;
  @FXML
  private Label ConnectStateLabel; 
  
  
  @FXML
  private Tab BasicViewTab;
  @FXML
  private TabPane VisualViewTabPane;


  

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    rootBorderPane.setId(("rootPane"));
    
    //--FileMenu
    openMultipleDBMenuItem.setDisable(false);
    openRecentDBsSubMenu.setDisable(false);
    closeAllDBMenuItem.setDisable(true);
    removeAllDBMenuItem.setDisable(true);
    //--EditMenu
    exportChartMenuItem.setDisable(true);
    closeChartsMenuItem.setDisable(true);
    //--ViewMenu
    basicViusalizerMenuItem.setDisable(true);
    analyticalViusalizerMenuItem.setDisable(true);
    //--ToolsMenu
    mergerDBMenuItem.setDisable(true);    
    
    //--MenuItem Actions Init
    openMultipleDBMenuItem.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        OpenMultipleDBFiles();
      }
    });
    closeAllDBMenuItem.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        closeAllDBFiles();
      }
    });
    removeAllDBMenuItem.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        removeAllDBFiles();
      }
    });
    closeChartsMenuItem.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        closeEXNCharts();
      }
    });
    
    //--BasicView Tab
    //TabPaneDetacher.create().makeTabsDetachable(VisualViewTabPane);
    VisualViewTabPane.getSelectionModel().select(BasicViewTab);
    BasicViewTab.setOnCloseRequest(new EventHandler<Event>() {
      @Override
      public void handle(Event event) {
        //----when closing chartTab, confirm with user if just hide chart instead of closing it.
        Alert closeVisualTabAlert = new Alert(Alert.AlertType.CONFIRMATION);
        closeVisualTabAlert.setTitle("Closing "+BasicViewTab.getText()+" chart:");
        closeVisualTabAlert.setHeaderText("Do you really want to close current chart?");
        closeVisualTabAlert.setContentText("NOTE:This will remove all your setting data on the chart!\nOtherwise, you can hide the chart instead.");
        ButtonType closeBtn = new ButtonType("Yes,close", ButtonBar.ButtonData.APPLY);
        ButtonType cancelBtn = new ButtonType("Cancel",ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType hideBtn = new ButtonType("Hide it",ButtonBar.ButtonData.BACK_PREVIOUS);
        closeVisualTabAlert.getButtonTypes().setAll(closeBtn, cancelBtn, hideBtn);
        Optional<ButtonType> result = closeVisualTabAlert.showAndWait();
        if (result.get() == closeBtn) {
          VisualViewTabPane.getTabs().remove(BasicViewTab);
        } else if (result.get() == hideBtn) {
          BasicViewTab.getContent().setVisible(false);
        } else {
          event.consume();
        }
        Runtime.getRuntime().gc();
        Runtime.getRuntime().freeMemory();        
      }
    });
    
    //--fetching indicator
    fetchDataLabel.setVisible(false);
    progressBar = new ProgressBar();
    progressBar.setPrefWidth(150);
    ((HBox)fetchDataLabel.getParent()).getChildren().add(progressBar);
    progressBar.setVisible(false);

    
    //--init database manager treeview
    userhome = System.getProperty("user.home");
    // tree root
    dbTreeRoot = new TreeItem<>();
    dbTreeRoot.setExpanded(true);
    sqldbTreeItemColor = "lightgrey";
    // set tree with tree root
    dbTreeView = new TreeView<>(dbTreeRoot);
    dbTreeView.setShowRoot(false);
    dbTreeView.setFocusTraversable(false);
    SQLiteTablesVBoxPane.getChildren().add(dbTreeView);
    VBox.setVgrow(dbTreeView, Priority.ALWAYS); 
    
    //--maintain a recently opened file list 
    ConnectStateLabel.setStyle("-fx-background-color:darkgrey;");
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
              sqldbTreeItemColor = "lightgrey";
              updateDBManagerTreeView(recentFilePathList);
              ConnectStateLabel.setText("Connected to: "+recentFilePath.substring(recentFilePath.lastIndexOf("/")+1));
              ConnectStateLabel.setStyle("-fx-text-fill:whitesmoke;");              
            }
          });          
        }
      });
    }
    
    //--executes database operations concurrent to JavaFX operations.
    databaseExecutor = Executors.newFixedThreadPool(4, new DatabaseThreadFactory()); 
    
    //createExnMonitorScript();
    
    
  }  
  
  private void createExnMonitorScript(){
    try {
      try (final PrintWriter writer = new PrintWriter(userhome+"/ExnVizerGUI/ExnMonitorTB/runExnMonitor.sh", "UTF-8")) {
        writer.println("#!/bin/bash");
        writer.println("cd \"/home/mwang/ExnVizerGUI/ExnMonitorTB/dist\"");
        //writer.println("cd \"/fast-nas/home/mwang/ExnVizerGUI/ExnMonitorTB/dist\"");
        writer.println("mkdir -p \"${HOME}/ExnVizer_v1.0/ExnMonitorTB/dist/java_tmp\"");
        writer.println("java -jar -Xms20m -XX:MinHeapFreeRatio=30 -XX:MaxHeapFreeRatio=50 -Djava.io.tmpdir=\"${HOME}/ExnVizer_v1.0/ExnMonitorTB/dist/java_tmp\" ExnMonitorTB.jar");
      }
    } catch (FileNotFoundException | UnsupportedEncodingException ex) {
      Logger.getLogger(EXNMonitorMainViewController1.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  
  /* MenuBar Actions */
  private void OpenMultipleDBFiles(){
    final FileChooser sqlDBChooser = new FileChooser();
    sqlDBChooser.setTitle("Open a SQLite Database File...");
    sqlDBChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("sqldb files", "*.sqldb"));
    sqlDBChooser.setInitialDirectory(new File(userhome));
    
    final Alert DBSelAlert = new Alert(Alert.AlertType.INFORMATION);
    DBSelAlert.setHeaderText(null);
    List<File> dbFiles;
    try {
      dbFiles = sqlDBChooser.showOpenMultipleDialog(null);
      // if dbFiles is not null
      final List<String> sqldbPathList = new ArrayList<>();
      //final List<String> sqldbNameList = new ArrayList<>();
      dbFiles.forEach(new Consumer<File>() {
        @Override
        public void accept(File t) {
          recentFilePaths.push(t.getPath());
          sqldbPathList.add(t.getPath());
          //sqldbNameList.add(t.getPath().substring(t.getPath().lastIndexOf("/")+1));
          //System.out.println(t.getPath());
        }
      }); 
      sqldbTreeItemColor = "#d6d6d6";
      updateDBManagerTreeView(sqldbPathList);
    } catch (NullPointerException ex) {
      DBSelAlert.setTitle("Please Select a Database File!");
      DBSelAlert.setContentText("No Database file is selected.");
      DBSelAlert.showAndWait();       
      ConnectStateLabel.setText("No Database Conncetion Found.");
      ConnectStateLabel.setStyle("-fx-text-fill:#9a9490;");       
    } finally{
      ConnectStateLabel.setText("Database Conncetions are successfully created.");
      ConnectStateLabel.setStyle("-fx-text-fill:#d6d6d6;");
    }
    
    Runtime.getRuntime().gc();
    Runtime.getRuntime().freeMemory();
  }  
  
  private void closeAllDBFiles(){
    closeAllDBMenuItem.setDisable(true);
    dbTreeRoot.getChildren().forEach(new Consumer<TreeItem<HBox>>() {
      @Override
      public void accept(TreeItem<HBox> sqldbTreeItem) {
        if (!sqldbTreeItem.getChildren().isEmpty()) {
          closeDatabase(sqldbTreeItem);  
        }
      }
    });
    Runtime.getRuntime().gc();
    Runtime.getRuntime().freeMemory();
  }
  
  private void removeAllDBFiles(){
    closeAllDBMenuItem.setDisable(true);
    removeAllDBMenuItem.setDisable(true);    
    closeAllDBFiles();
    dbTreeRoot.getChildren().clear();
    Runtime.getRuntime().gc();
    Runtime.getRuntime().freeMemory();
  }
  
  private void closeEXNCharts(){
    dbTreeRoot.getChildren().forEach(new Consumer<TreeItem<HBox>>() {
      @Override
      public void accept(TreeItem<HBox> sqldbTreeItem) {
        sqldbTreeItem.getChildren().forEach(new Consumer<TreeItem<HBox>>() {
          @Override
          public void accept(TreeItem<HBox> tblTreeItem) {
            final JFXCheckBox tblCheckBox= ((JFXCheckBox)(tblTreeItem.getValue().getChildren().get(0)));
            if (tblCheckBox.isSelected()) {
              tblCheckBox.setSelected(false);
            }
            Runtime.getRuntime().gc();
            Runtime.getRuntime().freeMemory();             
          }
        });
      }
    });
  }    
  
  /* update ConnectStateLabel */
  public void updateConnectStateLabel(String dbConnectStateString, String CaseName){
    if (CaseName == null) {
      ConnectStateLabel.setText("Connected to "+dbConnectStateString);
    } else{
      ConnectStateLabel.setText("Connected to "+dbConnectStateString+" from "+CaseName);
    }
    
    ConnectStateLabel.setStyle("-fx-text-fill:"+sqldbTreeItemColor+"; -fx-font-style:italic;");    
  }
  
  /* update DBManager TreeView */
  public void updateDBManagerTreeView(List<String> sqldbPathList){
    //--avoid opening already existed sqldb files in dbmanager
    final List<String> rootSqldbNameList = new ArrayList<>();
    dbTreeRoot.getChildren().forEach(new Consumer<TreeItem<HBox>>() {
      @Override
      public void accept(TreeItem<HBox> t) {
        final Label sqldbTreeItemLabel = (Label) (t.getValue().getChildren().get(0));
        rootSqldbNameList.add(sqldbTreeItemLabel.getText());
      }
    });
    
    final Alert duplicateDBAlert = new Alert(Alert.AlertType.INFORMATION);
    duplicateDBAlert.setHeaderText(null);
    final List<String> uniqueSqlPathList = new ArrayList<>();
    for (String sqldbPath : sqldbPathList) {
      if (!rootSqldbNameList.contains(sqldbPath.substring(sqldbPath.lastIndexOf("/")+1))) {
        uniqueSqlPathList.add(sqldbPath);
      }else{
        duplicateDBAlert.setTitle("Duplication!");
        duplicateDBAlert.setContentText(sqldbPath.substring(sqldbPath.lastIndexOf("/")+1)+" is already opened in Database Manager.");
        duplicateDBAlert.showAndWait();        
      }
    }
    
    uniqueSqlPathList.forEach(new Consumer<String>() {
      @Override
      public void accept(String sqldbPath) {  
        makeSqldbBranch(sqldbPath, dbTreeRoot); // dbTreeItem
      }
    });
  }
  
  public void updateSqldbTreeItemColor(String sqldbTreeItemColor){
    this.sqldbTreeItemColor = sqldbTreeItemColor;
  }

  
  /* Create Sqldb branches */
  private void makeSqldbBranch(String sqldbPath, TreeItem<HBox> dbTreeRoot) {
    // create sqldbTreeItem
    final String sqldbName = sqldbPath.substring(sqldbPath.lastIndexOf("/")+1);
    final Label sqldbTreeItemLabel = new Label(sqldbName); 
    final HBox sqldbTreeItemHBox = new HBox(sqldbTreeItemLabel);
    final TreeItem<HBox> sqldbTreeItem = new TreeItem<>(sqldbTreeItemHBox);
    dbTreeRoot.getChildren().add(sqldbTreeItem);
    sqldbTreeItem.setExpanded(false);
    sqldbTreeItemLabel.setTooltip(new Tooltip(sqldbPath));
    // style sqldbTreeItem
    final ImageView dbTreeIcon = new ImageView(new Image(getClass().getResourceAsStream("/org/mwang/exnmonitor/resource/dbConnected.png")));
    sqldbTreeItem.setGraphic(dbTreeIcon);
    sqldbTreeItemLabel.setStyle("-fx-text-fill:"+sqldbTreeItemColor+";"); 
    
    /* setup the db-context-menu */
    final MenuItem ConnectMenuItem = new MenuItem("Setup connection");
    final MenuItem closeDBMenuItem = new MenuItem("Close connection");
    final MenuItem removeDBMenuItem = new MenuItem("Remove this database");
    final ContextMenu dbContextMenu = new ContextMenu(ConnectMenuItem,closeDBMenuItem,removeDBMenuItem);        
    sqldbTreeItemLabel.setContextMenu(dbContextMenu);
    
    /* init contextmenuitems */
    ConnectMenuItem.setDisable(false);
    closeDBMenuItem.setDisable(true); 
    removeDBMenuItem.setDisable(false);  
    
    /* setup db connection after opening */
    connectDatabase(sqldbPath,sqldbTreeItem,sqldbTreeItemLabel,ConnectMenuItem,closeDBMenuItem);     
    Runtime.getRuntime().gc();
    Runtime.getRuntime().freeMemory();
    
    /* define menuitem events */
    ConnectMenuItem.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        connectDatabase(sqldbPath,sqldbTreeItem,sqldbTreeItemLabel,ConnectMenuItem,closeDBMenuItem); 
        Runtime.getRuntime().gc();
        Runtime.getRuntime().freeMemory();        
      }
    });
    
    closeDBMenuItem.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        closeDatabase(sqldbTreeItem);       
      }
    });
    
    removeDBMenuItem.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        closeDatabase(sqldbTreeItem);
        dbTreeRoot.getChildren().remove(sqldbTreeItem);
      }
    });

  }  
  
  private void connectDatabase(String sqldbPath, TreeItem<HBox> sqldbTreeItem, Label sqldbTreeItemLabel, MenuItem ConnectMenuItem, MenuItem closeDBMenuItem){
      // reset menu items
      closeAllDBMenuItem.setDisable(false);
      removeAllDBMenuItem.setDisable(false);      
      closeChartsMenuItem.setDisable(false);
      mergerDBMenuItem.setDisable(false); 
      
      sqldbTreeItemLabel.setStyle("-fx-font-weight: bold; -fx-text-fill:"+sqldbTreeItemColor+";");
      ConnectMenuItem.setDisable(true);
      closeDBMenuItem.setDisable(false);
      sqldbTreeItem.setExpanded(true);
      sqldbTreeItem.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/org/mwang/exnmonitor/resource/dbConnected.png"))));
      //----Get tblNameList and Create tblTreeItems
      //--Get tblNameList
      try (final Connection sqldbConnection = getConnection(sqldbPath)) {
        logger.info("Getting a database connection");
        System.out.println("current database connection is ReadOnly? "+sqldbConnection.isReadOnly());
        final PreparedStatement st = sqldbConnection.prepareStatement("SELECT name FROM sqlite_master WHERE type='table' ORDER BY name");
        final ResultSet SqlTblNameResultSet = st.executeQuery();
        final List<String> tblNameList = new ArrayList<>();
        while (SqlTblNameResultSet.next()) {
          tblNameList.add(SqlTblNameResultSet.getString("name"));
        }
        //--Create tblTreeItems
        int tblNameListSize = tblNameList.size();
        for (int i = 0; i < tblNameListSize; i++) {
          // create a tblTreeItem
          final JFXCheckBox tblTreeItemCheckBox = new JFXCheckBox(tblNameList.get(i));
          tblTreeItemCheckBox.setMnemonicParsing(false);
          final HBox tblTreeItemHBox = new HBox(tblTreeItemCheckBox);
          final TreeItem<HBox> tblTreeItem = new TreeItem<>(tblTreeItemHBox);
          final ImageView tblTreeIcon = new ImageView(new Image(getClass().getResourceAsStream("/org/mwang/exnmonitor/resource/dbtable.png")));
          tblTreeItem.setGraphic(tblTreeIcon);
          //tblTreeIcon.setStyle("-fx-padding: 2 0 0 0;");
          sqldbTreeItem.getChildren().add(tblTreeItem);          
        }
      } catch (SQLException ex) {
        Logger.getLogger(EXNMonitorMainViewController1.class.getName()).log(Level.SEVERE, null, ex);
      }

      sqldbTreeItem.getChildren().forEach(new Consumer<TreeItem<HBox>>() {
        @Override
        public void accept(TreeItem<HBox> tblTreeItem) {
          final JFXCheckBox tblCheckBox = ((JFXCheckBox)(tblTreeItem.getValue().getChildren().get(0)));
          tblCheckBox.selectedProperty().addListener(getTblCheckBoxChangeListener(sqldbPath,tblCheckBox,tblTreeItem,sqldbTreeItem));
        }
      });
      
      Runtime.getRuntime().gc();
      Runtime.getRuntime().freeMemory();       
  }
  
  private ChangeListener getTblCheckBoxChangeListener(String sqldbPath, JFXCheckBox tblCheckBox, TreeItem<HBox> tblTreeItem, TreeItem<HBox> sqldbTreeItem){
    final ChangeListener tblCheckBoxChangeListener = new ChangeListener<Boolean>() {
      @Override
      public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        if (newValue) {
          final Tab selTab = createSqlTableTab(sqldbPath, tblCheckBox);
          selTab.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/org/mwang/exnmonitor/resource/chartTabIcon.png"))));
          MonitorViewTabPane.getTabs().add(selTab);
          MonitorViewTabPane.getSelectionModel().select(MonitorViewTabPane.getTabs().size()-1);
          //----sync closing action with the sqldbTreeItem branch
          selTab.setOnCloseRequest(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
              //----when closing chartTab, confirm with user if just hide chart instead of closing it.
              final Alert closeDBAlert = new Alert(Alert.AlertType.CONFIRMATION);
              closeDBAlert.setTitle("Closing "+selTab.getText()+" chart:");
              closeDBAlert.setHeaderText("Do you really want to close current chart?");
              closeDBAlert.setContentText("NOTE:This will remove all your setting data on the chart!\nOtherwise, you can hide the chart instead.");
              final ButtonType closeBtn = new ButtonType("Yes,close", ButtonBar.ButtonData.APPLY);
              final ButtonType cancelBtn = new ButtonType("Cancel",ButtonBar.ButtonData.CANCEL_CLOSE);
              final ButtonType hideBtn = new ButtonType("Hide it",ButtonBar.ButtonData.BACK_PREVIOUS);
              closeDBAlert.getButtonTypes().setAll(closeBtn, cancelBtn, hideBtn);
              
              
              final Optional<ButtonType> result = closeDBAlert.showAndWait();
              if (result.get() == closeBtn) {
                for (TreeItem<HBox> tblTreeItem : sqldbTreeItem.getChildren()) {
                  final JFXCheckBox cb = ((JFXCheckBox)(tblTreeItem.getValue().getChildren().get(0)));
                  if (cb.getText().equals(selTab.getText())) {
                    cb.setSelected(false);
                    break;// TWO HOURS FOR DEBUGGING THIS: just add the break to avoid an entire for loop! amen!-=-0
                  }
                }
              } else if (result.get() == hideBtn) {
                selTab.getContent().setVisible(false);
              } else {
                event.consume();
              }
              
              
              Runtime.getRuntime().gc();
              Runtime.getRuntime().freeMemory();
            }
          });
        } else if (!newValue) {
          //((JFXCheckBox)(tblTreeItem.getValue().getChildren().get(0))).setStyle("-fx-text-fill: #d6d6d6;");
          for (Tab SQLTableTab : MonitorViewTabPane.getTabs()) {
            if ( SQLTableTab.getText().equals(((JFXCheckBox)(tblTreeItem.getValue().getChildren().get(0))).getText()) &&
                (SQLTableTab.getTooltip().getText().equals(sqldbPath.substring((sqldbPath.lastIndexOf("/")+1), sqldbPath.lastIndexOf("."))))) {
              if(SQLTableTab.isSelected()){
                MonitorViewTabPane.getTabs().remove(MonitorViewTabPane.getSelectionModel().getSelectedItem());
              }
              MonitorViewTabPane.getTabs().remove(SQLTableTab);
              break;// TWO HOURS FOR DEBUGGING THIS: just add the break to avoid an entire for loop! amen!
            }
            Runtime.getRuntime().gc();
            Runtime.getRuntime().freeMemory();                    
          }
        }          
      }
    }; 
    return tblCheckBoxChangeListener;
  }
  
  private void closeDatabase(TreeItem<HBox> sqldbTreeItem){
    sqldbTreeItem.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/org/mwang/exnmonitor/resource/dbClosed.png"))));
    ((Label)(sqldbTreeItem.getValue().getChildren().get(0))).setStyle("-fx-font-weight: regular; -fx-text-fill: #d6d6d6;");
    sqldbTreeItem.setExpanded(false);
    ((Label)(sqldbTreeItem.getValue().getChildren().get(0))).getContextMenu().getItems().get(0).setDisable(false);
    ((Label)(sqldbTreeItem.getValue().getChildren().get(0))).getContextMenu().getItems().get(1).setDisable(true);
    sqldbTreeItem.getChildren().forEach(new Consumer<TreeItem<HBox>>() {
      @Override
      public void accept(TreeItem<HBox> tblTreeItem) {
        final JFXCheckBox tblCheckBox= ((JFXCheckBox)(tblTreeItem.getValue().getChildren().get(0)));
        if (tblCheckBox.isSelected()) {
          tblCheckBox.setSelected(false);
        }
      }
    });
    sqldbTreeItem.getChildren().clear();
    Runtime.getRuntime().gc();
    Runtime.getRuntime().freeMemory();    
  }
  
  
  /* create SqlTable Tabs */
  private Tab createSqlTableTab(String sqldbPath, JFXCheckBox tblCheckBox){
    //---- Create subtabpane
    final String selTblName = tblCheckBox.getText();
    final Tab SqlTableTab = new Tab(selTblName);
    SqlTableTab.setStyle("-fx-focus-color: transparent;");
    SqlTableTab.setTooltip(new Tooltip(sqldbPath.substring((sqldbPath.lastIndexOf("/")+1), sqldbPath.lastIndexOf("."))));
    
    //----setup ChartSplitPane as container
    final ScrollPane ExnChartScrollPane = createExnChartScrollPane(sqldbPath,tblCheckBox);
    SqlTableTab.setContent(ExnChartScrollPane);
    SqlTableTab.selectedProperty().addListener(new ChangeListener<Boolean>() {
      @Override
      public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        if (!newValue) {
          Runtime.getRuntime().gc();          
          Runtime.getRuntime().freeMemory();
        }
      }
    });
    
    return SqlTableTab;
  }  
  
  
  /* create Chart SplitPane */
  private ScrollPane createExnChartScrollPane(String sqldbPath, JFXCheckBox tblCheckBox){
    int StaticLastRowIdInit = 0;
    try (final Connection initConnection = getConnection(sqldbPath)) {
      final PreparedStatement stXLastRowId = initConnection.prepareStatement("SELECT rowid FROM "+tblCheckBox.getText()+" ORDER BY rowid DESC LIMIT 1");
      final ResultSet rsXLastRowId = stXLastRowId.executeQuery();
      StaticLastRowIdInit = rsXLastRowId.getInt("rowid");
      System.out.println("lastRowId when opening current chart: "+StaticLastRowIdInit);
    } catch (SQLException ex) {
      Logger.getLogger(EXNMonitorMainViewController1.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    /****************************************************************
    * 0. Setup chart-split pane as the container 
    *****************************************************************/    
    final SplitPane ChartSplitPane = new SplitPane();
    ChartSplitPane.setStyle("-fx-background-color: transparent; -fx-padding: 8 8 8 8;"); 
    ChartSplitPane.getStyleClass().add(".split-pane:horizontal > .split-pane-divider");     
    //ChartSplitPane.getStylesheets().add(getClass().getResource("Styles/SegBtnBarStyle.css").toExternalForm());// for styling toolbar
    
    /*****************************************************************
    * 1. Setup LeftPane of ChartSplitPane for applying chart controls
    ******************************************************************/      
    //----1.2.0 create ChartControlVBox
    final VBox ChartControlVBox = new VBox(4);//Fields_VBox
    ChartControlVBox.setStyle("-fx-background-color:transparent;");
    ChartControlVBox.setMinWidth(150);
    ChartControlVBox.setMaxWidth(310);
    
    //----1.2.1 create chartSettingTitledPane
    //--add setChartTitle name
    final Label editChartTitle = new Label("Edit Chart Title");
    editChartTitle.setStyle("-fx-font-weight:bold; -fx-text-fill: #d6d6d6;");
    final JFXTextField chartTitleTextField = new JFXTextField();
    chartTitleTextField.setMaxWidth(291);
    //--add setXFiled name
    final Label editXLabel = new Label("Edit X-variable Name");
    editXLabel.setStyle("-fx-font-weight:bold; -fx-text-fill: #d6d6d6;");
    final JFXTextField XAxisNameTextField = new JFXTextField();
    XAxisNameTextField.setMaxWidth(291);
    //--add setYFiled names 
    final Label editYLabel = new Label("Edit Y-variable Names");
    editYLabel.setStyle("-fx-font-weight:bold; -fx-text-fill: #d6d6d6;");
    final TextArea YAxisNameTextArea = new TextArea();
    YAxisNameTextArea.setPrefHeight(60);
    YAxisNameTextArea.setWrapText(true);
    final VBox chartSettingVBox = new VBox(5, 
        editChartTitle,chartTitleTextField,
        editXLabel,XAxisNameTextField,
        editYLabel,YAxisNameTextArea
    ); 
    chartSettingVBox.setStyle("-fx-background-color: #2C2E3D;");//rgb(56, 59, 76)
    final TitledPane chartSettingTitledPane = new TitledPane("Basic Chart Settings", chartSettingVBox);
    chartSettingTitledPane.getStyleClass().add("titledpanewhiteText");
    chartSettingTitledPane.setStyle("-fx-border-color:  rgba(128,128,128,0.3);");
    
    //----1.2.2 create xyLUBoundTitlePane
    //--add setXLowerBound name
    final Label setXLowerBound = new Label("X-LowerBound");
    setXLowerBound.setStyle("-fx-font-weight:bold; -fx-text-fill: #d6d6d6;");
    final JFXTextField XLowerBoundTextField = new JFXTextField();
    XLowerBoundTextField.setMaxWidth(143);
    XLowerBoundTextField.setPromptText("x lowbound here");
    //--add setXUpperBound name
    final Label setXUpperBound = new Label("X-UpperBound");
    setXUpperBound.setStyle("-fx-font-weight:bold; -fx-text-fill: #d6d6d6;");
    final JFXTextField XUpperBoundTextField = new JFXTextField();
    XUpperBoundTextField.setMaxWidth(143);
    XUpperBoundTextField.setPromptText("x upperbound here");
    final VBox xLBoundVBox = new VBox(5,setXLowerBound,XLowerBoundTextField);
    final VBox xUBoundVBox = new VBox(5,setXUpperBound,XUpperBoundTextField);
    final HBox xLUBoundHBox = new HBox(8,xLBoundVBox,xUBoundVBox);
    //--add RangeSlider
    final RangeSlider xFieldRangeSlider = new RangeSlider();
    xFieldRangeSlider.setShowTickMarks(false);
    xFieldRangeSlider.setShowTickLabels(false);
    final NumberStringConverter converter = new NumberStringConverter();
    Bindings.bindBidirectional(XLowerBoundTextField.textProperty(),xFieldRangeSlider.lowValueProperty(),converter);
    Bindings.bindBidirectional(XUpperBoundTextField.textProperty(),xFieldRangeSlider.highValueProperty(),converter);
    
    
    //--add setYLowerBound name
    final Label setYLowerBound = new Label("Y-LowerBound");
    setYLowerBound.setStyle("-fx-font-weight:bold; -fx-text-fill: #d6d6d6;");
    final JFXTextField YLowerBoundTextField = new JFXTextField();
    YLowerBoundTextField.setMaxWidth(143);
    YLowerBoundTextField.setPromptText("y lowbound here");
    //--add setYUpperBound name
    final Label setYUpperBound = new Label("Y-UpperBound");
    setYUpperBound.setStyle("-fx-font-weight:bold; -fx-text-fill: #d6d6d6;");
    final JFXTextField YUpperBoundTextField = new JFXTextField();
    YUpperBoundTextField.setMaxWidth(143);
    YUpperBoundTextField.setPromptText("y upperbound here");
    final VBox yLBoundVBox = new VBox(5,setYLowerBound,YLowerBoundTextField);
    final VBox yUBoundVBox = new VBox(5,setYUpperBound,YUpperBoundTextField);
    final HBox yLUBoundHBox = new HBox(5,yLBoundVBox,yUBoundVBox); 
    final VBox xyLUBoundVBox = new VBox(8, xLUBoundHBox, xFieldRangeSlider, yLUBoundHBox);
    xyLUBoundVBox.setStyle("-fx-background-color: #2C2E3D;");
    final TitledPane xyLUBoundTitlePane = new TitledPane("Axis Bound Setting", xyLUBoundVBox);
    xyLUBoundTitlePane.getStyleClass().add("titledpanewhiteText");
    xyLUBoundTitlePane.setStyle("-fx-border-color:  rgba(128,128,128,0.3);");
    
    //----1.2.3 create XYFieldsTitledPane
    //--create XFields_VBox
    final VBox XFields_VBox = new VBox(8);
    XFields_VBox.setStyle("-fx-padding:0 0 0 -4;");
    final Label XPosLabel = new Label("X-Field");
    XPosLabel.setStyle("-fx-text-fill:#d6d6d6; -fx-font-weight:bold;");
    final ToggleGroup XFieldToggleGroup = new ToggleGroup();
    //--create YFields_VBox
    final VBox YFields_VBox = new VBox(8);
    final Label YPosLabel = new Label("Y-Field");
    YPosLabel.setStyle("-fx-text-fill:#d6d6d6; -fx-font-weight:bold;");
    //--create XYFieldsVBox
    final VBox XYFieldsVBox = new VBox(8, XPosLabel, XFields_VBox, YPosLabel, YFields_VBox);
    XYFieldsVBox.setStyle("-fx-background-color: #2C2E3D;");
    final TitledPane XYFieldsTitledPane = new TitledPane("X-Y Field Variables", XYFieldsVBox);
    XYFieldsTitledPane.getStyleClass().add("titledpanewhiteText");  
    XYFieldsTitledPane.setStyle("-fx-border-color:  rgba(128,128,128,0.3);");
    
    //----1.2.4 create MonitorSettingTitledPane
    //--refreshTime settting
    final JFXTextField refreshTimeTextField = new JFXTextField("10");
    refreshTimeTextField.setMaxWidth(80);
    refreshTimeTextField.setDisable(false);
    final JFXButton applyBtn = new JFXButton("Apply");
    final JFXButton resetBtn = new JFXButton("Reset");
    applyBtn.getStyleClass().add("custom-raised-button");
    resetBtn.getStyleClass().add("custom-raised-button");
    applyBtn.setDisable(false);
    resetBtn.setDisable(true);
    applyBtn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        applyBtn.setDisable(true);
        resetBtn.setDisable(false);
        refreshTimeTextField.setDisable(true);        
      }
    });
    resetBtn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        refreshTimeTextField.setDisable(false);
        refreshTimeTextField.setText("");
        applyBtn.setDisable(false);
        resetBtn.setDisable(true);        
      }
    });    
    final Label refreshRateLabel = new Label("Refresh Rate");
    refreshRateLabel.setTextFill(Color.web("#d6d6d6"));
    final Label secUnit = new Label("[second]");
    secUnit.setTextFill(Color.web("#d6d6d6"));
    final VBox MonitorSetVBox = new VBox(5, new HBox(5, applyBtn, resetBtn), new HBox(5, refreshRateLabel, refreshTimeTextField, secUnit));
    MonitorSetVBox.setStyle("-fx-background-color: #2C2E3D;");
    final TitledPane MonitorSettingTitledPane = new TitledPane("Monitor Settings", MonitorSetVBox);
    MonitorSettingTitledPane.getStyleClass().add("titledpanewhiteText");  
    MonitorSettingTitledPane.setStyle("-fx-border-color:  rgba(128,128,128,0.3);");  
    
    //----1.2.5 setup ChartControlVBox with chartSettingTitledPane, xyLUBoundTitlePane, XYFieldsTitledPane
    ChartControlVBox.getChildren().addAll(
        chartSettingTitledPane,
        xyLUBoundTitlePane,
        XYFieldsTitledPane,
        MonitorSettingTitledPane
    ); 

    
    
 
    /*****************************************************************
    * 2. Setup RightPane of ChartSplitPane for visualizing chart data
    ******************************************************************/    
    //----2.1.0 create ExnChartVBox and ExnChartScrollPane
    final VBox ExnChartVBox = new VBox();
    ExnChartVBox.setSpacing(8);
    ExnChartVBox.setPadding(new Insets(0, 0, 0, 0));
    //----2.1.1 Toolbar for chart controls
    final ToolBar toolBar = new ToolBar();
    toolBar.getStyleClass().add("SegToolBar");
    final Region spacer = new Region();
    spacer.getStyleClass().setAll("spacer");
    final HBox buttonBar = new HBox();
    buttonBar.getStyleClass().setAll("segmented-button-bar");
    //--Segment0
    final Button refreshChartUI = new Button("FreeMemory");
    refreshChartUI.getStyleClass().addAll("first");  
    final Separator separator0 = new Separator(Orientation.VERTICAL);    
    //--Segment1
    final Button exportBtn = new Button("Export");
    exportBtn.getStyleClass().addAll("first");
    final Button autoZoomBtn = new Button("Autozoom");
    final Separator separator1 = new Separator(Orientation.VERTICAL);
    //--Segment2
    final ToggleButton zoomBtn = new ToggleButton("Zoom");
    zoomBtn.getStyleClass().addAll("first");
    zoomBtn.setSelected(false);
    final ToggleButton moveBtn = new ToggleButton("Move");
    moveBtn.setSelected(false);
    moveBtn.getStyleClass().addAll("last");
    final Separator separator2 = new Separator(Orientation.VERTICAL);
    //--Segment3
    final ToggleButton symbolTog = new ToggleButton("Symbol Off");
    symbolTog.getStyleClass().addAll("first");
    symbolTog.setSelected(false);
    final ToggleButton dynamicTog = new ToggleButton("Monitor Off");
    dynamicTog.disableProperty().bind(resetBtn.disableProperty());
    dynamicTog.getStyleClass().addAll("last");
    dynamicTog.setSelected(false);
    final Separator separator3 = new Separator(Orientation.VERTICAL);
    //--setup toolBar with all buttons
    buttonBar.getChildren().addAll(refreshChartUI,separator0, exportBtn,autoZoomBtn,separator1, zoomBtn,moveBtn,separator2, symbolTog,dynamicTog,separator3);
    toolBar.getItems().addAll(spacer, buttonBar);     
    //----2.1.2 create ExnChartInfoHBox
    final Label currentLabel = new Label("Current RefreshTime:"); 
    currentLabel.setStyle("-fx-text-fill: #d6d6d6; -fx-font-weight: bold; -fx-font-size: 13px;");
    final Label refreshTimeLabel = new Label(); 
    refreshTimeLabel.setStyle("-fx-text-fill: #d6d6d6; -fx-font-weight: bold; -fx-font-size: 13px;");
    refreshTimeLabel.textProperty().bind(refreshTimeTextField.textProperty());
    final Label unitLabel = new Label("/sec"); 
    unitLabel.setStyle("-fx-text-fill: #d6d6d6; -fx-font-weight: bold; -fx-font-size: 13px;");
    final Label currentDataSizeLabel = new Label("     CurrentDataSize: ");
    currentDataSizeLabel.setStyle("-fx-text-fill: #d6d6d6; -fx-font-weight: bold; -fx-font-size: 13px;");
    final Label CurrentDataSize = new Label(String.valueOf(StaticLastRowIdInit));
    CurrentDataSize.setStyle("-fx-text-fill: #d6d6d6; -fx-font-weight: bold; -fx-font-size: 13px;");
    final HBox ExnChartInfoHBox = new HBox(currentLabel, refreshTimeLabel, unitLabel, currentDataSizeLabel,CurrentDataSize);
    ExnChartInfoHBox.setSpacing(5);
    ExnChartInfoHBox.setStyle("-fx-background-radius:4; -fx-background-color:rgba(128,128,128,0.3); -fx-border-radius:4; -fx-border-color:rgba(128,128,128,0.3); -fx-border-insets: 0 0 -1 0;");
    ExnChartInfoHBox.setPadding(new Insets(5, 0, 5, 20));
    //----2.1.3 create ExnChartStackPane for ExnChart
    final StackPane ExnChartStackPane = new StackPane();
    ExnChartStackPane.setStyle("-fx-background-color:transparent; -fx-border-color: rgba(128,128,128,0.3); -fx-border-radius: 10px;");
    DragResizer.makeResizable(ExnChartStackPane);
    ExnChartStackPane.setPrefHeight(650);
      
    //--setup ExnChartVBox
    VBox.setVgrow(toolBar, Priority.NEVER);
    VBox.setVgrow(ExnChartInfoHBox, Priority.NEVER);
    VBox.setVgrow(ExnChartStackPane, Priority.NEVER);    
    ExnChartVBox.getChildren().addAll(toolBar,ExnChartInfoHBox,ExnChartStackPane);  
    //--set both sides
    ChartSplitPane.getItems().addAll(ChartControlVBox,ExnChartVBox);    
    final ScrollPane ExnChartScrollPane = new ScrollPane(ChartSplitPane);
    ExnChartScrollPane.setStyle("-fx-background-color: rgb(56, 59, 76);");
    ExnChartScrollPane.setFitToHeight(true);
    ExnChartScrollPane.setFitToWidth(true); 
    
    
    
    
    
    //----1.2.1 init ExnChart//////this section objects dynamically depend on ExnChart object/////
    final String selTblName = tblCheckBox.getText();
    final StableTicksAxis xAxis = new StableTicksAxis();
    final StableTicksAxis yAxis = new StableTicksAxis();
    final LineChart<Number, Number> ExnChart = new LineChart<>(xAxis,yAxis);
    //Font.loadFont(EXNMonitorMainViewController.class.getResource("/org/mwang/ExnVizer/ExnMonitorTB/Styles/Comfortaa-Bold.ttf").toExternalForm(), 15);
    //ExnChart.setStyle("-fx-font-family:Comfortaa; -fx-font-size:1.3em;");
    ExnChart.setAnimated(false);
    ExnChart.setTitle(selTblName);
    ExnChart.setCreateSymbols(false);
    ExnChart.setLegendSide(Side.BOTTOM);
    ExnChart.setHorizontalGridLinesVisible(true);
    ExnChart.setVerticalGridLinesVisible(true);
    //--setup basic axis
//    xAxis.setAutoRangePadding(0);
    yAxis.setAutoRangePadding(0.1);
    xAxis.setAutoRanging(true);
    yAxis.setAutoRanging(true);
    xAxis.setForceZeroInRange(false);
    yAxis.setForceZeroInRange(false);
    yAxis.setUpperBound(60);
    yAxis.setLowerBound(-50);
    xAxis.setTickLabelRotation(30.0);
    yAxis.setTickLabelRotation(30.0);
    //--setup formatted Y-axis
    final NumberFormat formatY = new DecimalFormat("0.00E00");
		yAxis.setAxisTickFormatter(new FixedFormatTickFormatter(formatY));    
    //--setup ExnChartStackPane with ExnChart
    ExnChartStackPane.getChildren().add(ExnChart);
    //--setup ContextMenu Item events for chart: Export chart as image(*.png)
    final MenuItem exportChart = new MenuItem("Export this chart to...");
    exportChart.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        exportChartAsPng(ExnChart);
      }
    });
    exportChartMenuItem.setDisable(false);
    exportChartMenuItem.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        exportChartAsPng(ExnChart);
      }
    });
    ExnChart.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
      @Override
      public void handle(ContextMenuEvent event) {
        final ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().add(exportChart);
        contextMenu.show(ExnChart, event.getScreenX(), event.getScreenY());
      }
    });     
    
    
    chartTitleTextField.setText(ExnChart.getTitle());
    chartTitleTextField.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        ExnChart.setTitle(newValue);
      }
    });    
    XAxisNameTextField.textProperty().bindBidirectional(((StableTicksAxis)ExnChart.getXAxis()).labelProperty());    
    YAxisNameTextArea.textProperty().bindBidirectional(((StableTicksAxis)ExnChart.getYAxis()).labelProperty());    
    YLowerBoundTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent ke) {
        if (ke.getCode().equals(KeyCode.ENTER)){
          ((StableTicksAxis)ExnChart.getXAxis()).setAutoRanging(false);
          ((StableTicksAxis)ExnChart.getYAxis()).setAutoRanging(false);
          ((StableTicksAxis)ExnChart.getYAxis()).setLowerBound(Double.parseDouble(YLowerBoundTextField.getText()));
        }     
        Runtime.getRuntime().gc();
        Runtime.getRuntime().freeMemory();        
      }
    });    
    YUpperBoundTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent ke) {
        if (ke.getCode().equals(KeyCode.ENTER)){
          ((StableTicksAxis)ExnChart.getXAxis()).setAutoRanging(false);
          ((StableTicksAxis)ExnChart.getYAxis()).setAutoRanging(false);
          ((StableTicksAxis)ExnChart.getYAxis()).setUpperBound(Double.parseDouble(YUpperBoundTextField.getText()));
        }
        Runtime.getRuntime().gc();
        Runtime.getRuntime().freeMemory();          
      }
    });    
    

    //--init xFieldRange 
    double xMinValue;
    double xMaxValue; 
    try (final Connection initConnection = getConnection(sqldbPath)) {
      final PreparedStatement stXValue;
      if (tblCheckBox.getText().contains("Convergence")) {
        stXValue = initConnection.prepareStatement("SELECT Simulation_time_step FROM "+tblCheckBox.getText()+" WHERE rowid=1 OR rowid="+StaticLastRowIdInit);
        final ResultSet rsXValue = stXValue.executeQuery();
        rsXValue.next();
        xMinValue = (double)rsXValue.getInt("Simulation_time_step");
        rsXValue.next();
        xMaxValue = (double)rsXValue.getInt("Simulation_time_step");  
        xFieldRangeSlider.setBlockIncrement(1);
      } else{
        stXValue = initConnection.prepareStatement("SELECT Simulation_time FROM "+tblCheckBox.getText()+" WHERE rowid=1 OR rowid="+StaticLastRowIdInit);
        final ResultSet rsXValue = stXValue.executeQuery();
        rsXValue.next();
        xMinValue = rsXValue.getDouble("Simulation_time");
        rsXValue.next();
        xMaxValue = rsXValue.getDouble("Simulation_time");  
      }
      double delta = xMaxValue- xMinValue;
      xFieldRangeSlider.setMin(xMinValue - delta*0.01);
      xFieldRangeSlider.setMax(xMaxValue + delta*0.1);
      xFieldRangeSlider.setLowValue(xFieldRangeSlider.getMin());
      xFieldRangeSlider.setHighValue(xFieldRangeSlider.getMax());      
    } catch (SQLException ex) {
      Logger.getLogger(EXNMonitorMainViewController1.class.getName()).log(Level.SEVERE, null, ex);
    }    
    
    
    //----fetch XYPosition name
    List<String> YFieldNameList;
    try (final Connection sqldbConnection = getConnection(sqldbPath)) {
      final PreparedStatement st = sqldbConnection.prepareStatement("PRAGMA table_info(" + selTblName + ");");
      final ResultSet rsFieldsNameList = st.executeQuery();
      int index=0;
      while (rsFieldsNameList.next()) {
        if (rsFieldsNameList.getString("name").contains("Simulation_time")) {
          final JFXRadioButton rb = new JFXRadioButton(rsFieldsNameList.getString("name"));
          rb.setMnemonicParsing(false);
          rb.setId(String.valueOf(index));
          rb.setToggleGroup(XFieldToggleGroup);
          XFields_VBox.getChildren().add(rb);
        } else{
          final String YFieldName = rsFieldsNameList.getString("name");
          //System.out.println(YFieldName);
          YFieldNameList = new ArrayList<>();
          YFieldNameList.add(YFieldName);
          JFXCheckBox cb = new JFXCheckBox(YFieldName);
          cb.setMnemonicParsing(false);
          cb.setId(String.valueOf(index));  // set index as checkbox id
          YFields_VBox.getChildren().add(cb);
          index = index+1;
        }
      }
    } catch (SQLException ex) {
      Logger.getLogger(EXNMonitorMainViewController1.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    
    //----separating Convergence table and other monitor point table
    String selXFieldName;
    if (selTblName.equals("Convergence")) {
      ( (JFXRadioButton)(XFields_VBox.getChildren().get(1)) ).setSelected(true);
      selXFieldName = ( (JFXRadioButton)(XFields_VBox.getChildren().get(1)) ).getText();
      ((StableTicksAxis)ExnChart.getXAxis()).setLabel(selXFieldName);
    } else{
      ( (JFXRadioButton)(XFields_VBox.getChildren().get(0)) ).setSelected(true);
      selXFieldName = ((JFXRadioButton)(XFields_VBox.getChildren().get(0))).getText();
      ((StableTicksAxis)ExnChart.getXAxis()).setLabel(selXFieldName+" /sec");
    }    
    
    //---1.3 interaction between fieldpane selections and chart series 
    YFields_VBox.getChildren().forEach(new Consumer<Node>() {//field selection event
      @Override
      public void accept(Node t) {
        final JFXCheckBox cb = (JFXCheckBox)(t);
        cb.selectedProperty().addListener(new ChangeListener<Boolean>() {
          @Override
          public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            
            final List<String> yLabelString = new ArrayList<>();
            String yCommaLabelString;
            YFields_VBox.getChildren().forEach(new Consumer<Node>() {
              @Override
              public void accept(Node t) {
                JFXCheckBox cb = (JFXCheckBox)(t);
                if (cb.isSelected()) {
                  yLabelString.add(cb.getText());
                }
              }
            });
            if (newValue) {// if selected, add to chart with the series and add data points on it
              try {
                // update YAxis Label
                yCommaLabelString = Joiner.on(",  ").join(yLabelString);
                ((StableTicksAxis)ExnChart.getYAxis()).setLabel( yCommaLabelString.concat("\n") );
                //--update line series from databases based on StaticLastRowIdInit
                updateSingleFieldSeries(xFieldRangeSlider,symbolTog, cb, Integer.parseInt(CurrentDataSize.getText()), sqldbPath, selXFieldName, cb.getText(), selTblName, ExnChart);
              } catch (SQLException ex) {
                Logger.getLogger(EXNMonitorMainViewController1.class.getName()).log(Level.SEVERE, null, ex);
              }
              Runtime.getRuntime().gc();
              Runtime.getRuntime().freeMemory();              
            } else if (!newValue) {// if not selected, must clear data points of this series and remove it from chart
              //--remove xRange binding
              ((StableTicksAxis)ExnChart.getXAxis()).lowerBoundProperty().unbind();
              ((StableTicksAxis)ExnChart.getXAxis()).upperBoundProperty().unbind();               
              //System.out.println(cb.getStyleClass().get(2));
              cb.getStyleClass().remove(2);
              // update YAxis Label
              yLabelString.remove(cb.getText());
              yCommaLabelString = Joiner.on(",  ").join(yLabelString);
              ((StableTicksAxis)ExnChart.getYAxis()).setLabel( yCommaLabelString );   
              // clear data
              for (final XYChart.Series<Number,Number> lineSeries : ExnChart.getData()) {
                if (lineSeries.getName().equals(cb.getText())) {
                  lineSeries.getData().clear();
                  ExnChart.getData().remove(lineSeries);
                  break;
                }
              }
              //autoZoom(ExnChart, xFieldRangeSlider);
              Runtime.getRuntime().gc();
              Runtime.getRuntime().freeMemory();
            }
          }
        });   
      }
      
    });  
    
    
    
    
    
    
    //----SegBtnBar eventsCurrentDataSize
    // export chart event
    exportBtn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        exportChartAsPng(ExnChart);
        Runtime.getRuntime().gc();
        Runtime.getRuntime().freeMemory();
      }
    });
    
    // autoZoom event
    autoZoomBtn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        autoZoom(ExnChart,xFieldRangeSlider);
      }
    });
    
    // Panning 
    final ChartPanManager panner = new ChartPanManager( ExnChart );    
    moveBtn.selectedProperty().addListener(new ChangeListener<Boolean>() {
      @Override
      public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        if (newValue) {
          ExnChart.setCursor(Cursor.MOVE);
          zoomBtn.setSelected(false);
          dynamicTog.setSelected(false);
          //--start moving chart series
          panner.start(); 
          Runtime.getRuntime().gc();
          Runtime.getRuntime().freeMemory(); 
        } else if (!newValue) {
          ExnChart.setCursor(Cursor.DEFAULT);
          panner.stop();
          Runtime.getRuntime().gc();
          Runtime.getRuntime().freeMemory();          
        }
      }
    });    

    // Zooming
		ChartZoomManager chartZoomManager = JFXChartUtil.setupZooming(ExnChart, new EventHandler<MouseEvent>() {
			@Override
			public void handle( MouseEvent mouseEvent ) {
        if (!zoomBtn.isSelected()) {
          // let it filterd
          ExnChart.setCursor(Cursor.DEFAULT);
          mouseEvent.consume();
          Runtime.getRuntime().gc();
          Runtime.getRuntime().freeMemory();    
        } else if (zoomBtn.isSelected()) {
          ExnChart.setCursor(Cursor.CROSSHAIR);
          moveBtn.setSelected(false);
          dynamicTog.setSelected(false);
          Runtime.getRuntime().gc();
          Runtime.getRuntime().freeMemory();          
        }
			}
		} ); 
    chartZoomManager.setMouseWheelZoomAllowed(false);
    //xFieldRangeSlider.disableProperty().bind(Bindings.or(moveBtn.selectedProperty(), zoomBtn.selectedProperty()));
    
    // refresh GUI by freeing memory manually if not responding quickly
    refreshChartUI.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        Runtime.getRuntime().gc();
        Runtime.getRuntime().freeMemory();        
      }
    });
    
    // chart line symbol on-off
    symbolTog.selectedProperty().addListener(new ChangeListener<Boolean>() {
      @Override
      public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        if (newValue) {
          symbolTog.setText("Symbol On");
          ExnChart.setCreateSymbols(true);
          ExnChart.getData().forEach(new Consumer<XYChart.Series<Number,Number>>() {
            @Override
            public void accept(XYChart.Series<Number,Number> lineSeries) {
              applyHighlightEvent(lineSeries, selXFieldName, lineSeries.getName());             
            }
          });
          Runtime.getRuntime().gc();
          Runtime.getRuntime().freeMemory();
        } else if (!newValue) {
          symbolTog.setText("Symbol Off");
          ExnChart.setCreateSymbols(false);
          Runtime.getRuntime().gc();
          Runtime.getRuntime().freeMemory();
        }
      }
    });
    
    // monitor mode on-off
    final Timeline tl = new Timeline();// ONLY ONE timeline per table-chart!
    dynamicTog.selectedProperty().addListener(new ChangeListener<Boolean>() {
      @Override
      public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        if (newValue) {
          ExnChart.setAnimated(true);
          /* disable all YField checkboxes to make sure they have the same starting index. */
          XFields_VBox.setDisable(true);
          YFields_VBox.setDisable(true);
          xLUBoundHBox.setDisable(true);
          yLUBoundHBox.setDisable(true);
          MonitorSetVBox.setDisable(true);
          /* init button resource */
          ExnChart.getStylesheets().add(getClass().getResource("/org/mwang/exnmonitor/resource/dynamicChartMode.css").toExternalForm());
          dynamicTog.setText("Monitor On");
          zoomBtn.setDisable(true);
          moveBtn.setDisable(true);
          /* set autoZoom on */
          autoZoom(ExnChart,xFieldRangeSlider);
          ((StableTicksAxis)ExnChart.getXAxis()).setAutoRanging( true );
          Runtime.getRuntime().gc();
          Runtime.getRuntime().freeMemory();
          
          /* start monitoring */
          //----0. collect all the selected y-fields in this tblChart and check table type
          final List<String> selYFieldsNameList = getSelYFieldsNameList(ExnChart);
          final String commaSelYFieldsNameString = Joiner.on(",").join(selYFieldsNameList);
          String dbXFieldName;
          if (selTblName.equals("Convergence")) {
            dbXFieldName = "Simulation_time_step";
          } else {
            dbXFieldName = "Simulation_time";
          }
          System.out.println("Starting ExnMonitor timeline...");
          
          //----1.makeup missed data points
          logger.info("Getting a database connection for making-up missing data points");
          final int makeupIndex = ((XYChart.Series<Number,Number>)ExnChart.getData().get(0)).getData().size();
          System.out.println("Before making-up missing data points, CurrentDataSize= "+makeupIndex);
          final FetchDBDataTask fetchDBMakeupDataTask = new FetchDBDataTask(0, sqldbPath, selYFieldsNameList, commaSelYFieldsNameString, selTblName, makeupIndex, makeupIndex);
          fetchDBMakeupDataTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
              final ObservableList fetchDBMakeupDataList = fetchDBMakeupDataTask.getValue();
              final double flag = (double) (fetchDBMakeupDataList.get(0));
              if (flag == 1.0) {
                final ObservableList<Double> xDatalist = (ObservableList<Double>) fetchDBMakeupDataList.get(1);
                final ObservableList<ObservableList<Double>> multiYFieldsDataList = (ObservableList<ObservableList<Double>>) fetchDBMakeupDataList.get(2);
                ExnChart.getData().forEach(new Consumer<XYChart.Series<Number, Number>>() {
                  @Override
                  public void accept(XYChart.Series<Number, Number> ExnSeries) {
                    final ObservableList<XYChart.Data<Number,Number>> makeupedDatalist = ExnSeries.getData();
                    // chart must be cast as integer! or we'll get the acsii value of this char.
                    final int seriesIdx = Integer.parseInt(String.valueOf(ExnSeries.getNode().getStyleClass().get(1).charAt(6)));
                    final int length = xDatalist.size();
                    for (int i = 0; i < length; i++) {
                      makeupedDatalist.add(new XYChart.Data<>(xDatalist.get(i),multiYFieldsDataList.get(seriesIdx).get(i)));                          
                    }
                    ExnSeries.setData(makeupedDatalist);
                  }
                });                    
              }
              //--apply highlights
              if (symbolTog.isSelected()) {
                ExnChart.setCreateSymbols(true);
                ExnChart.getData().forEach(new Consumer<XYChart.Series<Number, Number>>() {
                  @Override
                  public void accept(XYChart.Series<Number, Number> ExnSeries) {
                    applyHighlightEvent(ExnSeries ,dbXFieldName, ExnSeries.getName());                    
                  }
                });
              }
            }
          });
          //--submit fetchDBDataTask to databaseExecutor to get a thread from the concurrent-thread-pool.
          databaseExecutor.submit(fetchDBMakeupDataTask);
          Runtime.getRuntime().gc();
          Runtime.getRuntime().freeMemory();
          
          //----2.start timeline
          logger.info("Getting a database connection for monitor timeline");
          fetchDataLabel.setVisible(true);
          progressBar.setVisible(true);
          final KeyFrame kf;
          kf = new KeyFrame(Duration.seconds(Double.parseDouble(refreshTimeTextField.getText())), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              final int DynamicLastRowIdInit = ((XYChart.Series<Number,Number>)ExnChart.getData().get(0)).getData().size();
              System.out.println("Before adding new data points, current DynamicLastRowIdInit= "+DynamicLastRowIdInit);
              final FetchDBDataTask fetchDBDataTask = new FetchDBDataTask(1, sqldbPath, selYFieldsNameList, commaSelYFieldsNameString, selTblName, DynamicLastRowIdInit, Integer.parseInt(CurrentDataSize.getText()));
              fetchDBDataTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent event) {
                  final int updatedIndex = (int) (fetchDBDataTask.getValue().get(3));
                  //--Update Chart ONLY when new data points generated
                  if (updatedIndex > DynamicLastRowIdInit) {
                    //--update chart series
                    final ObservableList fetchDBAddDataList = fetchDBDataTask.getValue();
                    CurrentDataSize.setText(String.valueOf((int)(fetchDBAddDataList.get(3))));
                    final ObservableList<Double> xDatalist = (ObservableList<Double>) fetchDBAddDataList.get(1);
                    final ObservableList<ObservableList<Double>> multiYFieldsDataList = (ObservableList<ObservableList<Double>>) fetchDBAddDataList.get(2);
                    ExnChart.getData().forEach(new Consumer<XYChart.Series<Number, Number>>() {
                      @Override
                      public void accept(XYChart.Series<Number, Number> ExnSeries) {
                        final ObservableList<XYChart.Data<Number,Number>> addDataList = ExnSeries.getData();
                        // chart must be cast as integer! or we'll get the acsii value of this char.
                        final int seriesIdx = Integer.parseInt(String.valueOf(ExnSeries.getNode().getStyleClass().get(1).charAt(6)));
                        System.out.println("seriesIdx: "+seriesIdx);
                        final int length = xDatalist.size();
                        for (int i = 0; i < length; i++) {
                          //double yField = ((ObservableList<Double>)multiYFieldsDataList.get(seriesIdx)).get(i);
                          addDataList.add(new XYChart.Data<>(xDatalist.get(i),multiYFieldsDataList.get(seriesIdx).get(i)));
                        }
                        ExnSeries.setData(addDataList);
                      }
                    });
                    //--apply highlights
                    if (symbolTog.isSelected()) {
                      ExnChart.setCreateSymbols(true);
                      ExnChart.getData().forEach(new Consumer<XYChart.Series<Number, Number>>() {
                        @Override
                        public void accept(XYChart.Series<Number, Number> ExnSeries) {
                          applyHighlightEvent(ExnSeries ,dbXFieldName, ExnSeries.getName());
                        }
                      });
                    }                    
                  }
                }
              });
              //--submit fetchDBDataTask to databaseExecutor to get a thread from the concurrent-thread-pool.
              databaseExecutor.submit(fetchDBDataTask);
              
//              Runtime.getRuntime().gc();
//              Runtime.getRuntime().freeMemory();
//              System.gc();
            }
          });
          tl.getKeyFrames().add(kf);
          tl.setCycleCount(Animation.INDEFINITE);
          tl.play();
          
        } else if (!newValue) {
          CurrentDataSize.setText(String.valueOf(((XYChart.Series<Number,Number>)ExnChart.getData().get(0)).getData().size()));
          System.out.println("Monitor is off, CurrentDataSize= "+CurrentDataSize.getText());
          ExnChart.setAnimated(false);
          tl.stop();
          tl.getKeyFrames().clear();// frames MUST be REOMVED after stopping the timeline!
          dynamicTog.setText("Monitor Off");
          zoomBtn.setDisable(false);
          moveBtn.setDisable(false);
          
          ((StableTicksAxis)ExnChart.getXAxis()).setAutoRanging( false );
          
          /* enable all YField checkboxes to make sure they have the same starting index. */
          XFields_VBox.setDisable(false);
          YFields_VBox.setDisable(false);
          xLUBoundHBox.setDisable(false);
          yLUBoundHBox.setDisable(false);
          MonitorSetVBox.setDisable(false);
          ExnChart.getStylesheets().remove(getClass().getResource("/org/mwang/exnmonitor/resource/dynamicChartMode.css").toExternalForm());
          fetchDataLabel.setVisible(false);
          progressBar.setVisible(false);
          logger.log(Level.INFO, "{0} and its Timeline are closed.", sqldbPath.substring(sqldbPath.lastIndexOf("/")+1));
          Runtime.getRuntime().gc();
          Runtime.getRuntime().freeMemory();          
        }
      }
    });
    
    tblCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
      @Override
      public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        if (!newValue) {
          dynamicTog.setSelected(false);
          Runtime.getRuntime().gc();
          Runtime.getRuntime().freeMemory();
        }
      }
    });
    
    
    return ExnChartScrollPane;
  }    
  
  private void autoZoom(LineChart ExnChart, RangeSlider xFieldRangeSlider){
    ObservableList<XYChart.Series<Number,Number>> lineSeriesList = ExnChart.getData();
    ExnChart.setData( lineSeriesList );
    
    ((StableTicksAxis)ExnChart.getXAxis()).setLowerBound(xFieldRangeSlider.getMin());
    ((StableTicksAxis)ExnChart.getXAxis()).setUpperBound(xFieldRangeSlider.getMax());
    ((StableTicksAxis)ExnChart.getXAxis()).setAutoRanging( false );
    ((StableTicksAxis)ExnChart.getYAxis()).setAutoRangePadding(0.1);
    ((StableTicksAxis)ExnChart.getYAxis()).setAutoRanging( true );    
    
    Runtime.getRuntime().gc();
    Runtime.getRuntime().freeMemory();    
  }
  
  private List<String> getSelYFieldsNameList(LineChart ExnChart){
    final List<String> selYFieldsNameList = new ArrayList<>();
    ExnChart.getData().forEach(new Consumer<XYChart.Series<Number, Number>>() {
      @Override
      public void accept(XYChart.Series<Number, Number> ExnSeries) {
        selYFieldsNameList.add(ExnSeries.getName());
      }
    });
    return selYFieldsNameList;//never will be null since ExnChart has at least one series selected.
  }
  
  
  
  /*******************************************************************************************************
  * Fetch Exn/Aero-CFDSolver-Data{ ConvergenceXYData + MonitorPointsXYData } from SQLite database files: * 
  * 1) ConvergenceExnXYData//////////////                                                                * 
  * {XYChart.Series<X,Y> convExnXYSeries}                                                                *
  *  method -> ExnChart.getData().add(convExnXYSeries);                                                  *
  * {ObservableList<XYChart.Data<X,Y>> convExnXYSeriesDataObservableList}                                *
  *  method -> convExnXYSeries.setData(convExnXYSeriesDataObservableList);                               *
  *                                                                                                      *
  * 2) MonitorPointsExnXYData//////                                                                      *
  * {XYChart.Series<X,Y> mPointExnXYSeries}                                                              *
  *  method -> ExnChart.getData().add(mPointExnXYSeries);                                                *
  * {ObservableList<XYChart.Data<X,Y>> mPointExnXYSeriesDataObservableList}                              *
  *  method -> mPointExnXYSeries.setData(mPointExnXYSeriesDataObservableList);                           *
  *                                                                                                      *
  * The "data-fetching" should create background tasks.                                                  *
  *******************************************************************************************************/
  private void updateSingleFieldSeries(RangeSlider xFieldRangeSlider,ToggleButton symbolTog,JFXCheckBox selCheckBox, int CurrentDataSize, String sqldbPath, String selXFieldName, String selYFieldName1, String selTblName, LineChart ExnChart) throws SQLException{
    if (CurrentDataSize >= 30000) {
      /* simplify line points for dealing with large data sets. */      
      simplifyLine(xFieldRangeSlider,ExnChart,selCheckBox,sqldbPath,CurrentDataSize,selXFieldName,selYFieldName1,selTblName);
    } else{
      /* NO simplifications. */
      noSimpliedLine(xFieldRangeSlider,symbolTog,ExnChart,selCheckBox,CurrentDataSize,sqldbPath,selXFieldName,selYFieldName1,selTblName);      
    }
    //chart-series-line series0 default-color0
    //System.out.println("series-style-class: "+ExnXYSeries.getNode().getStyleClass());
    //System.out.println("series default-color : "+ExnXYSeries.getNode().getStyleClass().get(2).charAt(13)); 
    //int seriesIdx = Integer.parseInt(String.valueOf(ExnXYSeries.getNode().getStyleClass().get(1).charAt(6)));
    //System.out.println("series index : "+seriesIdx); 
  }  
  
  private void noSimpliedLine(RangeSlider xFieldRangeSlider,ToggleButton symbolTog,LineChart ExnChart,JFXCheckBox selExnYDataCheckBox,int currentRowIndex, String sqldbPath, String selXFieldName, String selExnYDataName, String selTblName){
    final List<String> selExnYDataNameList = new ArrayList<>();
    selExnYDataNameList.add(selExnYDataCheckBox.getText());
    final String selExnYDataNameCommaString = Joiner.on(",").join(selExnYDataNameList); 
    final XYChart.Series<Number, Number> ExnXYSeries = new XYChart.Series<>();
    ExnXYSeries.setName(selExnYDataName);     
    
    final FetchDBDataTask fetchSingleExnXYSeriesDataTask = new FetchDBDataTask(2, sqldbPath, selExnYDataNameList, selExnYDataNameCommaString, selTblName, currentRowIndex, currentRowIndex);
    fetchSingleExnXYSeriesDataTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
      @Override
      public void handle(WorkerStateEvent event) {
        final ObservableList fetchSingleSeriesDataList = fetchSingleExnXYSeriesDataTask.getValue();
        final double flag = (double) (fetchSingleSeriesDataList.get(0));
        if (flag == 1.0) {
          final ObservableList<XYChart.Data<Number, Number>> ExnXYSeriesDataObservebleList = (ObservableList<XYChart.Data<Number, Number>>)fetchSingleSeriesDataList.get(1);
          //--set ExnXYSeries to chart
          ExnXYSeries.setData(ExnXYSeriesDataObservebleList);
          ExnChart.getData().add(ExnXYSeries);
          final String seriesIdx = String.valueOf(ExnXYSeries.getNode().getStyleClass().get(2).charAt(13));
          selExnYDataCheckBox.getStyleClass().add("selCheckBox"+seriesIdx); 
          //--init xRange binding
          ((StableTicksAxis)ExnChart.getXAxis()).setAutoRanging(false);
          ((StableTicksAxis)ExnChart.getXAxis()).lowerBoundProperty().bindBidirectional(xFieldRangeSlider.lowValueProperty());
          ((StableTicksAxis)ExnChart.getXAxis()).upperBoundProperty().bindBidirectional(xFieldRangeSlider.highValueProperty());
          ((StableTicksAxis)ExnChart.getYAxis()).setAutoRangePadding(0.1);
          ((StableTicksAxis)ExnChart.getYAxis()).setAutoRanging( true );          
          //--check symbols
          if (symbolTog.isSelected()) {
            ExnChart.setCreateSymbols(true);
            applyHighlightEvent(ExnXYSeries, selXFieldName, ExnXYSeries.getName());             
          }
        }                  
      }
    });
    //--submit fetchDBDataTask to databaseExecutor to get a thread from the concurrent-thread-pool.
    databaseExecutor.submit(fetchSingleExnXYSeriesDataTask);
    
    Runtime.getRuntime().gc();
    Runtime.getRuntime().freeMemory();
  }
  
  private void simplifyLine(RangeSlider xFieldRangeSlider,LineChart ExnChart,JFXCheckBox selCheckBox,String sqldbPath, int currentRowIndex, String selXFieldName, String selYFieldName1, String selTblName){
    //--Collect all data points firstly, then do the rendering work: with jts.jar lib
    final XYChart.Series<Number, Number> lineSeries = new XYChart.Series<>();
    lineSeries.setName(selYFieldName1); 
    
    final List<XYChart.Data<Number,Number>> update = new ArrayList<>();
    try (final Connection sqldbConnection = getConnection(sqldbPath)) {
      // data fetching--------------------------------------------------------------------------------
      sqldbConnection.setAutoCommit(false);
      final PreparedStatement stXField = sqldbConnection.prepareStatement("SELECT "+selXFieldName+" FROM "+selTblName+" WHERE rowid<="+currentRowIndex);
      final ResultSet rsXField = stXField.executeQuery();
      final PreparedStatement stYField = sqldbConnection.prepareStatement("SELECT "+selYFieldName1+" FROM "+selTblName+" WHERE rowid<="+currentRowIndex);
      final ResultSet rsYField = stYField.executeQuery();      
      sqldbConnection.commit();
      
      final GeometryFactory gf = new GeometryFactory();
      final long t0 = System.nanoTime();
      final Coordinate[] coordinates = new Coordinate[currentRowIndex];
      final int neglectedPointsStart = (int)Math.ceil(currentRowIndex*0.49);
      final int neglectedPointsEnd = (int)Math.ceil(currentRowIndex*0.51);
      final double[] yFieldValues = new double[neglectedPointsEnd-neglectedPointsStart];
      
      final int coordLength = coordinates.length;
      for (int i = 0; i < coordLength; i++) {
        rsXField.next();
        rsYField.next();
        //double xField = rsXField.getDouble(selXFieldName);
        //double yField = yMakeupRs.getDouble(selYFieldName1);
        //System.out.println("xField="+xField+", yField="+yField);
        coordinates[i] = new Coordinate(rsXField.getDouble(selXFieldName), rsYField.getDouble(selYFieldName1));
        if ( (i > neglectedPointsStart-1) && (i < neglectedPointsEnd) ) {
          yFieldValues[i-neglectedPointsStart] = rsYField.getDouble(selYFieldName1);
        }
      }

//      System.out.println("coordinates: "+coordinates.length);
      final Geometry geom = new LineString(new CoordinateArraySequence(coordinates), gf);
      
      //double distanceTolarance = Math.abs(coordinates[(int) Math.ceil(rsSize*0.900)].y-coordinates[(int) Math.ceil(rsSize*0.905)].y);
      
      final StandardDeviation stdTolarance = new StandardDeviation();
      final double distanceTolarance = stdTolarance.evaluate(yFieldValues);
//      System.out.println("yFieldValues length: "+yFieldValues.length);
//      System.out.println("tolarance-distance: "+distanceTolarance);
      
      final Geometry simplified = DouglasPeuckerSimplifier.simplify(geom, distanceTolarance);// distance-torance
//      System.out.println("simplified: "+simplified.getCoordinates().length);
//      System.out.println("simplified[0]: "+simplified.getCoordinates()[0]);
//      System.out.println("simplified[1]: "+simplified.getCoordinates()[1]);

      final int simplifiedCoordLength = simplified.getCoordinates().length;
      final Coordinate[] simplifiedCoords = simplified.getCoordinates();
      for (int i = 0; i < simplifiedCoordLength; i++) {
        update.add(new XYChart.Data<>((simplifiedCoords[i]).x, (simplifiedCoords[i]).y));
      }
      final long t1 = System.nanoTime();
      System.out.println(String.format("Reduces points from %d to %d in %.1f ms", coordinates.length, update.size(),(t1 - t0) / 1e6));
            
      final ObservableList<XYChart.Data<Number,Number>> lineSeriesDataList = FXCollections.observableArrayList(update);      
      lineSeries.setData(lineSeriesDataList);
      ExnChart.getData().add(lineSeries);
      final String seriesIdx = String.valueOf(lineSeries.getNode().getStyleClass().get(2).charAt(13));
      selCheckBox.getStyleClass().add("selCheckBox"+seriesIdx);  
      
      //--init xRange binding
      ((StableTicksAxis)ExnChart.getXAxis()).setAutoRanging(false);
      //((StableTicksAxis)ExnChart.getYAxis()).setAutoRanging(false);
      ((StableTicksAxis)ExnChart.getXAxis()).lowerBoundProperty().bind(xFieldRangeSlider.lowValueProperty());
      ((StableTicksAxis)ExnChart.getXAxis()).upperBoundProperty().bind(xFieldRangeSlider.highValueProperty());       
      
      Runtime.getRuntime().gc();
      Runtime.getRuntime().freeMemory();      
    } catch (SQLException ex) {
      Logger.getLogger(EXNMonitorMainViewController1.class.getName()).log(Level.SEVERE, null, ex);
    }

     
  }
  
  
  
  
  private void applyHighlightEvent(XYChart.Series<Number,Number> lineSeries, String selXFieldName, String selYFieldName1) {
    /* Get lineseries and seriesIdx */
    final Node lineSeriesNode = lineSeries.getNode();
    final String seriesIdx = String.valueOf(lineSeriesNode.getStyleClass().get(2).charAt(13));
    //System.out.println("seriesIdx= "+seriesIdx);
    /* Browsing through the Data and applying ToolTip as well as the class on hover. */
    final NumberFormat formatX = new DecimalFormat("#.000E0");
    final NumberFormat formatY = new DecimalFormat("0.000000E0");
    
    /***********************************************************************************
    * NOTE:
    * JavaFX Scene Graph is NOT designed for rendering millions of JavaFX nodes!
    * Each XYChart.Data<Number,Number> object has its JavaFX node, for large time-series 
    * data ploting this highlight should be disabled. 
    ************************************************************************************/
    final int length = lineSeries.getData().size();
    for (int i = 0; i < length; i++) {
      final XYChart.Data<Number, Number> dataPoint = lineSeries.getData().get(i);
      
//      if (i == lineSeries.getData().size()-1) {
//        dataPoint.getNode().getStyleClass().add("onHover"); 
//      } 
      
      dataPoint.getNode().setOnMouseEntered(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
          //--set hover
          lineSeriesNode.getStyleClass().add("seriesOnHover");
          dataPoint.getNode().getStyleClass().add("onHover"); 
          dataPoint.getNode().setCursor(Cursor.HAND);           
          //--install tooltip
          final Tooltip tooltip = new Tooltip(
                  selXFieldName+ ": " + new Double(formatX.format(dataPoint.getXValue())) + "\n" + 
                  selYFieldName1+ ": " + new Double(formatY.format(dataPoint.getYValue())) 
          );
          tooltip.getStyleClass().add("toolTipBorder"+seriesIdx);// Set tooltip color: .default-color#
          hackTooltipStartTiming(tooltip);// Set tooltip delay-time          
          Tooltip.install(dataPoint.getNode(), tooltip);
        }
      });

      dataPoint.getNode().setOnMouseExited(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
          lineSeriesNode.getStyleClass().remove("seriesOnHover");
          dataPoint.getNode().getStyleClass().remove("onHover");
          dataPoint.getNode().setCursor(Cursor.HAND); 
          //Tooltip.uninstall(dataPoint.getNode(), tooltip);
        }
      });         
    }
    
    //System.out.println(((XYChart.Data<Number, Number>)lineSeries.getData().get(0)).getNode().getStyleClass());
    
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
      Logger.getLogger(EXNMonitorMainViewController1.class.getName()).log(Level.SEVERE, null, ex);
    }
  }  
  
  private static double[] toPrimitive(ObservableList<Double> array) {
    final double[] result = new double[array.size()];
    for (int i = 0; i < array.size(); i++) {
      result[i] = array.get(i);
    }
    return result;
  }  
  
  private void exportChartAsPng(LineChart ExnChart){
    ExnChart.setAnimated(false);
    
    final FileChooser chartFileChooser = new FileChooser();
    chartFileChooser.setTitle("Export Chart As...");
    chartFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("png files (*.png)", "*.png"));
    chartFileChooser.setInitialDirectory(new File(userhome));   
    chartFileChooser.setInitialFileName(ExnChart.getTitle()+".png");
    
    final Alert imageSelAlert = new Alert(Alert.AlertType.INFORMATION);
    imageSelAlert.setHeaderText(null);
    
    final File chartFile = chartFileChooser.showSaveDialog(null);
    final WritableImage chartImage = ExnChart.snapshot(new SnapshotParameters(), null);
    final BufferedImage ExnChartImage = SwingFXUtils.fromFXImage(chartImage, null);
    
    if (chartFile != null) {
      try {
        ImageIO.write(ExnChartImage, ".png", chartFile);
      } catch (IOException ex) {// never throw.
      } 
      imageSelAlert.setTitle("ExnChart saved: "+chartFile.getName());
    } else{
      imageSelAlert.setTitle("ExnChart is not saved!");
      imageSelAlert.setContentText("Please specify a valid name and path for saving the chart.");
      imageSelAlert.showAndWait();
    }

  }
  

  
  /********************************************************************************************
  * sqlite-jdbc wrapper: set Read-Only flag when opening a connection to SQlite database files
  * 
  *********************************************************************************************/
  private Connection getUnPooledConnection(String sqldbPath){
    Connection conn = null;
    try {
      //logger.info("Getting a database connection");
      
//    SQLiteConfig dbConfig = new SQLiteConfig();
//    dbConfig.setReadOnly(true);
      
      final Properties config = new Properties();
      config.setProperty("open_mode", "1");  //1 == readonly
      
      Class.forName("org.sqlite.JDBC").newInstance();
      conn = DriverManager.getConnection("jdbc:sqlite:"+sqldbPath,config);
      
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
      Logger.getLogger(EXNMonitorMainViewController1.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    return conn;
  } 
  
  private Connection getConnection(String sqldbPath){
    Connection pooledConn = null;
    try {
      // configure SQLite
      final SQLiteConfig config = new org.sqlite.SQLiteConfig();
      config.setReadOnly(true);
//    config.setPageSize(4096); //in bytes
//    config.setCacheSize(2000); //number of pages
//    config.setSynchronous(SQLiteConfig.SynchronousMode.OFF);
//    config.setJournalMode(SQLiteConfig.JournalMode.OFF);
      
      // turn off logging c3p0's init info
      final Properties p = new Properties(System.getProperties());
      p.put("com.mchange.v2.log.MLog", "com.mchange.v2.log.FallbackMLog");
      p.put("com.mchange.v2.log.FallbackMLog.DEFAULT_CUTOFF_LEVEL", "OFF"); // Off or any other level
      System.setProperties(p);
      
      // get an unpooled SQLite DataSource with the desired configuration
      final SQLiteDataSource unpooled = new SQLiteDataSource( config );
      unpooled.setUrl("jdbc:sqlite:" + sqldbPath);
      
      // get a pooled c3p0 DataSource that wraps the unpooled SQLite DataSource
      final DataSource pooled = DataSources.pooledDataSource( unpooled );
      
      pooledConn = pooled.getConnection();
      
    } catch (SQLException ex) {
      Logger.getLogger(EXNMonitorMainViewController1.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    return pooledConn;
  }
 
  static class DatabaseThreadFactory implements ThreadFactory {

    static final AtomicInteger poolNumber = new AtomicInteger(1);

    @Override 
    public Thread newThread(Runnable runnable) {
      final Thread thread = new Thread(runnable, "Database-Connection-" + poolNumber.getAndIncrement() + "-thread");
      thread.setDaemon(true);
      return thread;
    }
  }  

}