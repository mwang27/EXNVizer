/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mwang.exnvizer.ui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import com.mchange.v2.c3p0.DataSources;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javax.sql.DataSource;
import org.mwang.exnmonitor.ui.controller.EXNMonitorMainViewController;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

/**
 *
 * @author mwang
 */
public class MergeSqldbDialogController implements Initializable {

  @FXML
  private ListView sqldbListView;
  @FXML
  private JFXButton addDBBtn;
  @FXML
  private JFXButton removeDBBtn;
  @FXML
  private JFXButton clearAllDBBtn;
  @FXML
  private JFXButton moveUpBtn;
  @FXML
  private JFXButton moveDownBtn;
  @FXML
  private JFXButton mergeDBBtn;
  @FXML
  private JFXButton cancelMergeDBBtn;
  
  @FXML
  private JFXProgressBar mergeDBProgressBar;
  @FXML
  private Label mergeTaskStatusLabel;
  @FXML
  private JFXButton visualizeSqldbBtn;
          
  @FXML
  private TextField mergedDBPathTextField;
  @FXML
  private JFXButton browserBtn;
  
  private String userhome;
  private String defaultMergedDBName;
  private ObservableList<String> sqldbNameList;
  private ObservableList<String> sqldbPathList;
  
  private ExecutorService executorService;
  private EXNMonitorMainViewController exnmonitorControllerGlobal;

  public void setEXNMonitorControllerGlobal(EXNMonitorMainViewController exnmonitorControllerGlobal) {
    this.exnmonitorControllerGlobal = exnmonitorControllerGlobal;
  }

  public void setExecutorService(ExecutorService executorService) {
    this.executorService = executorService;
  }
  
  
  
  @Override
  public void initialize(URL location, ResourceBundle resources) {
  
    userhome = System.getProperty("user.home");
    defaultMergedDBName = "mergedDB.sqldb";
    initMergeDBDialogPane();
    
  }
  
  
  
  private void initMergeDBDialogPane(){
    
    sqldbNameList = FXCollections.observableArrayList();
    sqldbPathList = FXCollections.observableArrayList();
    
    mergedDBPathTextField.setText(userhome);
    mergeDBProgressBar.setVisible(false);
    
    addDBBtn.setOnAction((ActionEvent event) -> {
      openMultipleDBFiles();
    });

    removeDBBtn.setOnAction((ActionEvent event) -> {
      final String selDBItem = sqldbListView.getSelectionModel().getSelectedItem().toString();
      final int selDBIndex = sqldbNameList.indexOf(selDBItem);
      sqldbNameList.remove(selDBIndex);
      sqldbPathList.remove(selDBIndex);
    });
    
    clearAllDBBtn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        sqldbNameList.clear();
        sqldbPathList.clear();
        sqldbListView.getItems().clear();
        Runtime.getRuntime().gc();
        Runtime.getRuntime().freeMemory();        
      }
    });
    
    moveUpBtn.setOnAction((ActionEvent event) -> {
      final String selDBItem = sqldbListView.getSelectionModel().getSelectedItem().toString();
      final int selDBIndex = sqldbNameList.indexOf(selDBItem);
      if ((selDBIndex-1) >= 0) {
        swapListItem(sqldbNameList, selDBIndex,"up");
        swapListItem(sqldbPathList, selDBIndex,"up");
        sqldbListView.getSelectionModel().select(selDBIndex-1);
        System.out.println("selected sqldbPath: "+(selDBIndex-1)+" -- "+sqldbPathList.get(selDBIndex-1));
      } else {
        event.consume();
      }
    });
    
    moveDownBtn.setOnAction((ActionEvent event) -> {
      final String selDBItem = sqldbListView.getSelectionModel().getSelectedItem().toString();
      final int selDBIndex = sqldbNameList.indexOf(selDBItem);
      if ((selDBIndex+1) < sqldbNameList.size()) {
        swapListItem(sqldbNameList, selDBIndex,"down");
        swapListItem(sqldbPathList, selDBIndex,"down");
        sqldbListView.getSelectionModel().select(selDBIndex+1);
        System.out.println("selected sqldbPath: "+(selDBIndex+1)+" -- "+sqldbPathList.get(selDBIndex+1));
      } else {
        event.consume();
      }
    });
    
    mergeDBBtn.setOnAction((ActionEvent event)->{
      if (mergedDBPathTextField.getText().contains(".sqldb")) {
      
        mergeDBProgressBar.setVisible(true);
        // long-time task -> should be run on non-GUI thread.
        Task mergeDBTask = new Task() {
          @Override
          protected Object call() throws Exception {
            System.out.println("start mergeDB worker task: ");
            mergeMultipleDBFiles();
            updateProgress(1, 1);
            return null;
          }
        };
        mergeDBProgressBar.progressProperty().bind(mergeDBTask.progressProperty());

        mergeDBTask.runningProperty().addListener(new ChangeListener<Boolean>() {
          @Override
          public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            if (newValue) {
              mergeTaskStatusLabel.setText("Merging task running...");
              mergeDBBtn.setText("Merging...");
              mergeDBBtn.setDisable(true);
            }else{
              mergeDBBtn.setText("Merge");
              mergeDBBtn.setDisable(false);
            }
          }
        });

        mergeDBTask.progressProperty().addListener(new ChangeListener<Number>() {
          @Override
          public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            if (newValue.intValue() == 1) {
              mergeTaskStatusLabel.setText("Merging task Done.");
            }
          }
        });

        executorService.submit(mergeDBTask);
      
      } else {
        final Alert DBSelAlert = new Alert(Alert.AlertType.INFORMATION);
        DBSelAlert.setHeaderText(null);
        DBSelAlert.setTitle("Please specify a valid path for the merged database file!");
        DBSelAlert.setContentText("No Database File Selected.");
        DBSelAlert.showAndWait();
      }
    });
    
    cancelMergeDBBtn.setOnAction((ActionEvent event) -> {
      executorService.shutdownNow();
      mergeDBProgressBar.setDisable(true);
      mergeTaskStatusLabel.setText("Merging task cancelled.");
    });
    
    visualizeSqldbBtn.setOnAction((ActionEvent event) -> {
      final List<String> mergedDBPath = new ArrayList<>();
      mergedDBPath.add(mergedDBPathTextField.getText());
      exnmonitorControllerGlobal.updateDBManagerTreeView(mergedDBPath);
    });
    
    browserBtn.setOnAction((ActionEvent event) -> {
      saveMergedDBFile();
    });
    
  } 
  
  private void swapListItem(ObservableList<String> obList, int selDBIndex, String flag){
    if (flag.equals("up")) {
      final String tempDBItem = obList.get(selDBIndex);
      final String upItem = obList.get(selDBIndex-1);
      obList.set(selDBIndex-1, tempDBItem);
      obList.set(selDBIndex, upItem);
    } else {
      final String tempDBItem = obList.get(selDBIndex);
      final String downItem = obList.get(selDBIndex+1);
      obList.set(selDBIndex+1, tempDBItem);
      obList.set(selDBIndex, downItem);      
    }
  }

  private void openMultipleDBFiles(){
    sqldbNameList = FXCollections.observableArrayList();
    sqldbPathList = FXCollections.observableArrayList();
    
    final FileChooser sqlDBChooser = new FileChooser();
    sqlDBChooser.setTitle("Add SQLite Database Files...");
    sqlDBChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("sqldb files", "*.sqldb"));
    sqlDBChooser.setInitialDirectory(new File(userhome));
    
    final Alert DBSelAlert = new Alert(Alert.AlertType.INFORMATION);
    DBSelAlert.setHeaderText(null);
    List<File> dbFiles;
    try {
      dbFiles = sqlDBChooser.showOpenMultipleDialog(null);
      // if dbFiles is not null
      dbFiles.forEach(new Consumer<File>() {
        @Override
        public void accept(File t) {
          sqldbNameList.addAll(t.getName());
          sqldbPathList.addAll(t.getPath());
        }
      }); 
      sqldbListView.setItems(sqldbNameList);
    } catch (NullPointerException ex) {
      DBSelAlert.setTitle("Please Select a Database File!");
      DBSelAlert.setContentText("No Database File Selected.");
      DBSelAlert.showAndWait();       
    }
    
    Runtime.getRuntime().gc();
    Runtime.getRuntime().freeMemory();
  }  
  
  private void mergeMultipleDBFiles(){
    
    initMergedDBFile();
    insertMergeData();
    
  }

  private void initMergedDBFile(){
    ObservableList<String> sqlStringList = FXCollections.observableArrayList();
    sqlStringList = getCreateTableSqlStringList();
    
    Connection conn = null;
    
    try {
      
      conn = getWritableConnection(mergedDBPathTextField.getText());
      Statement statement = conn.createStatement();
      statement.setQueryTimeout(10);
      
//      for (String sqlCreate : sqlStringList) {
//        System.out.println(sqlCreate);
//      }

      // create tables for mergeDB.sqldb
      for (String sqlCreate : sqlStringList) {
        statement.executeUpdate(sqlCreate);
      }
      
      conn.close();
    } catch (SQLException ex) {
      Logger.getLogger(MergeSqldbDialogController.class.getName()).log(Level.SEVERE, null, ex);
    } finally
    {
      try
      {
        if(conn != null)
          conn.close();
      }
      catch(SQLException e)
      {
        // connection close failed.
        System.err.println(e);
      }
    }      
    
  }
  
  private void insertMergeData(){

    ObservableList<String> tblNameList = FXCollections.observableArrayList();
    tblNameList = getDBTableNameList();
    
    Connection conn = null;
    
    try {
      
      conn = getWritableConnection(mergedDBPathTextField.getText());
      Statement statement = conn.createStatement();
      statement.setQueryTimeout(10);

      // insert data into mergeDB.sqldb
      final int size = tblNameList.size();
      final int totalDBsToMerge = sqldbPathList.size();
      String sqlAttach;
      String sqlDetach;
      String sqlInsert;
      String tblName;
      
      // SQLITE_LIMIT_ATTACHED = 10
      for (int j=0; j<totalDBsToMerge; j++) {
        sqlAttach = "ATTACH '"+sqldbPathList.get(j)+"' AS toMergeDB;";
        sqlDetach = "DETACH DATABASE toMergeDB;";
        statement.executeUpdate(sqlAttach);
        for (int i=0; i<size; i++) {
          tblName = tblNameList.get(i);
          sqlInsert = "INSERT OR IGNORE INTO "+tblName+" SELECT * FROM toMergeDB."+tblName+";";
          statement.executeUpdate(sqlInsert);
        }
        statement.executeUpdate(sqlDetach);
      }
      
      conn.close();
    } catch (SQLException ex) {
      Logger.getLogger(MergeSqldbDialogController.class.getName()).log(Level.SEVERE, null, ex);
    } finally
    {
      try
      {
        if(conn != null)
          conn.close();
      }
      catch(SQLException e)
      {
        // connection close failed.
        System.err.println(e);
      }
    }     
  }
  
  private void saveMergedDBFile(){
    
    final FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Sava merged sqlite database file as...");
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("sqldb files (*.sqldb)", "*.sqldb"));
    fileChooser.setInitialDirectory(new File(userhome));   
    fileChooser.setInitialFileName("mergedDB.sqldb");
    
    final Alert sqldbSaveAlert = new Alert(Alert.AlertType.INFORMATION);
    sqldbSaveAlert.setHeaderText(null);
    
    final File fileSaved = fileChooser.showSaveDialog(null);
    
    if (fileSaved != null) {
      mergedDBPathTextField.setText(fileSaved.getPath()); 
      sqldbSaveAlert.setTitle("Sqlite database file saved: "+fileSaved.getName());
    } else{
      sqldbSaveAlert.setTitle("Sqlite database file is not speficied.");
      sqldbSaveAlert.setContentText("Please specify a valid name and path.");
      sqldbSaveAlert.showAndWait();
    }

  }  
  
  
  
  
  private ObservableList<String> getCreateTableSqlStringList(){
    final ObservableList<String> sqlStringList = FXCollections.observableArrayList();
    try {
      final Connection conn = getConnection(sqldbPathList.get(0));
      final String sql = "SELECT sql FROM sqlite_master WHERE type='table';";
      
      conn.setAutoCommit(false);
      final PreparedStatement prepDataSt = conn.prepareStatement(sql);
      final ResultSet rs = prepDataSt.executeQuery();
      conn.commit();
      
      while (rs.next()) {
        sqlStringList.addAll(rs.getString("sql"));
      }
      
    } catch (SQLException ex) {
      Logger.getLogger(MergeSqldbDialogController.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    return sqlStringList;    
  }
  
  private ObservableList<String> getDBTableNameList(){
    final ObservableList<String> tblNameList = FXCollections.observableArrayList();
    try {
      final Connection conn = getConnection(sqldbPathList.get(0));
      final String sql = "SELECT DISTINCT tbl_name FROM sqlite_master;";
      
      conn.setAutoCommit(false);
      final PreparedStatement prepDataSt = conn.prepareStatement(sql);
      final ResultSet rs = prepDataSt.executeQuery();
      conn.commit();
      
      while (rs.next()) {
        tblNameList.addAll(rs.getString("tbl_name"));
      }
      
    } catch (SQLException ex) {
      Logger.getLogger(MergeSqldbDialogController.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    return tblNameList;
  }
  
  private Connection getConnection(String sqldbPath){
    Connection pooledConn = null;
    try {
      // configure SQLite
      final SQLiteConfig config = new org.sqlite.SQLiteConfig();
      config.setReadOnly(true);
      
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
      Logger.getLogger(EXNMonitorMainViewController.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    return pooledConn;
  } 
  
  private Connection getWritableConnection(String sqldbPath){
    Connection conn = null;
    try {
      String url = "jdbc:sqlite:" + sqldbPath;
      conn = DriverManager.getConnection(url);
      
    } catch (SQLException ex) {
      Logger.getLogger(MergeSqldbDialogController.class.getName()).log(Level.SEVERE, null, ex);
    }
    return conn;
  }   
  
}
