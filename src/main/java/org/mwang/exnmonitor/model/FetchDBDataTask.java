/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mwang.exnmonitor.model;

import org.mwang.exnmonitor.ui.controller.EXNMonitorMainViewController;
import com.mchange.v2.c3p0.DataSources;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.chart.XYChart;
import javax.sql.DataSource;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

/**
 *
 * @author mwang
 */
public class FetchDBDataTask extends DBTask<ObservableList> {
    
    private final int indicator;
    private final String sqldbPath;
    private final String dbTableName;
    
    private Connection sqldbConnection;
    private String dbXFieldName;
    private final List<String> selYFieldsNameList;
    private final String commaSelYFieldsNameString;
    
    private final int StaticLastRowIdIndex;
    private int DynamicLastRowIdIndex;
    
    
    
    
    public FetchDBDataTask(int indicator, String sqldbPath, List<String> selYFieldsNameList, String commaSelYFieldsNameString, String dbTableName, int DynamicLastRowIdInit, int StaticLastRowIdInit) {
      this.indicator = indicator;
      this.sqldbPath = sqldbPath;
      this.selYFieldsNameList = selYFieldsNameList;
      this.commaSelYFieldsNameString = commaSelYFieldsNameString;
      this.dbTableName = dbTableName;
      this.DynamicLastRowIdIndex = DynamicLastRowIdInit;
      this.StaticLastRowIdIndex = StaticLastRowIdInit;
    }

    
    
    
    @Override 
    protected ObservableList call(){
      // database connection for EXN/Aero is native and fast, unlike a connection to web server.
      // though this frequently connetion and disconnection operations are not time-consuming task, they can make UI unresponsive.
      // Thus, they should be deligated from UI thread to background worker thread.(javafx.concurrent package)
      updateMessage("Monitoring EXNAero Problem: sqldb Name");
      
      if (dbTableName.equals("Convergence")) {
        dbXFieldName = "Simulation_time_step";
      } else{
        dbXFieldName = "Simulation_time";
      }
      
      ObservableList xyDatalist = FXCollections.observableArrayList();
      try {
        sqldbConnection = getConnection(sqldbPath);
        if (indicator == 0) {
          xyDatalist = getMultipleChartSeriesMakeupDataObList(dbXFieldName); // getValue()
        } else if (indicator == 1) {
          xyDatalist = getMultipleChartSeriesDataObList(dbXFieldName); // getValue()
        } else if (indicator == 2) {
          xyDatalist = getSingleChartSeriesDataObList(dbXFieldName);
        }
        sqldbConnection.close();
      } catch (SQLException ex) {
        Logger.getLogger(FetchDBDataTask.class.getName()).log(Level.SEVERE, null, ex);
      }
      
      return xyDatalist;
    }

    
    
    
    private ObservableList getSingleChartSeriesDataObList(String dbXFieldName){
      /* 1.Create observablelists to store data */
      final ObservableList XYdata = FXCollections.observableArrayList();
      final ObservableList<XYChart.Data<Number, Number>> lineSeriesDataList = FXCollections.observableArrayList();
      Double avalibilityFlag = 0.0;
      
      /* 2.Fetch data and store data */
      try {
        //--setup database queries
        final String selYFieldName = selYFieldsNameList.get(0);
        sqldbConnection.setAutoCommit(false);
        final PreparedStatement prepDataSt = sqldbConnection.prepareStatement("SELECT "+dbXFieldName+","+selYFieldName+" FROM "+dbTableName+" WHERE rowid<="+StaticLastRowIdIndex+" AND "+selYFieldName+" !=''");
        final ResultSet DataRestultSet = prepDataSt.executeQuery();
        sqldbConnection.commit();
        //--collect data
        while(DataRestultSet.next()){
          avalibilityFlag = 1.0;
          lineSeriesDataList.add(new XYChart.Data<>(DataRestultSet.getDouble(dbXFieldName),DataRestultSet.getDouble(selYFieldName)));
        }
        //--update return list
        XYdata.addAll(avalibilityFlag,lineSeriesDataList);
      } catch (SQLException ex) {
        Logger.getLogger(FetchDBDataTask.class.getName()).log(Level.SEVERE, null, ex);
      }
      
      return XYdata;      
    }
    
    private ObservableList getMultipleChartSeriesMakeupDataObList(String dbXFieldName){
      /* 1.Create observablelists to store data */
      final ObservableList XYdata = FXCollections.observableArrayList();
      final ObservableList<Double> XFieldData = FXCollections.observableArrayList();
      final ObservableList<ObservableList<Double>> multiYFieldsDataList = FXCollections.observableArrayList();
      Double avalibilityFlag = 0.0;
      int updatedIndex;
      
      /* 2.MUST init  multiYFieldsDataList before using! */
      ObservableList<Double> yFiledDataList;
      for (int i = 0; i < selYFieldsNameList.size(); i++) {
        yFiledDataList = FXCollections.observableArrayList();
        multiYFieldsDataList.add(yFiledDataList);
      }
      
      /* 3.Fetch data and store data */
      try {
        //--setup database queries
        sqldbConnection.setAutoCommit(false);
        final PreparedStatement prepDataSt = sqldbConnection.prepareStatement("SELECT "+dbXFieldName+","+commaSelYFieldsNameString+" FROM "+dbTableName+" WHERE rowid>"+StaticLastRowIdIndex);
        ResultSet DataRestultSet = prepDataSt.executeQuery();
        final PreparedStatement prepLastIdxSt = sqldbConnection.prepareStatement("SELECT rowid FROM "+dbTableName+" ORDER BY rowid DESC LIMIT 1");
        ResultSet rsXLastRowId = prepLastIdxSt.executeQuery();        
        sqldbConnection.commit();
        //--collect data
        while(DataRestultSet.next()){
          avalibilityFlag = 1.0;
          // x-column
          XFieldData.add(DataRestultSet.getDouble(dbXFieldName));
          //System.out.println(DataRestultSet.getDouble(dbXFieldName));
          // y-columns
          for (int i = 0; i < selYFieldsNameList.size(); i++) {
            multiYFieldsDataList.get(i).add(DataRestultSet.getDouble(selYFieldsNameList.get(i))); // y-column i
            //System.out.println("DataRestultSet.getDouble(selYFieldsNameList.get(i)): "+DataRestultSet.getDouble(selYFieldsNameList.get(i)));
          }
        }
        
        //--update current sql-cursor DynamicLastRowIdIndex
        updatedIndex = rsXLastRowId.getInt("rowid");
        DynamicLastRowIdIndex = updatedIndex;
        if (updatedIndex > DynamicLastRowIdIndex) {
          System.out.println((updatedIndex-DynamicLastRowIdIndex)+" new data points aded to sqlite database, updated Last-Row-Index = "+updatedIndex);
        } else{
          System.out.println("No new data points added to sqlite database.");
        }
        
        //--update return list
        XYdata.addAll(avalibilityFlag,XFieldData, multiYFieldsDataList,updatedIndex);        
      } catch (SQLException ex) {
        Logger.getLogger(FetchDBDataTask.class.getName()).log(Level.SEVERE, null, ex);
      }
      
      return XYdata;
    }    
    
    private ObservableList getMultipleChartSeriesDataObList(String dbXFieldName) {
      /* 1.Create observablelists to store data */
      final ObservableList XYdata = FXCollections.observableArrayList();
      final ObservableList<Double> XFieldData = FXCollections.observableArrayList();
      final ObservableList<ObservableList<Double>> multiYFieldsDataList = FXCollections.observableArrayList();
      Double avalibilityFlag = 0.0;
      int updatedIndex;    
      
      /* 2.MUST init  multiYFieldsDataList before using! */
      ObservableList<Double> yFiledDataList;
      for (int i = 0; i < selYFieldsNameList.size(); i++) {
        yFiledDataList = FXCollections.observableArrayList();
        multiYFieldsDataList.add(yFiledDataList);
      }
      
      /* 3.Fetch data and store data */
      try {
        //--setup database queries
        sqldbConnection.setAutoCommit(false);
        final PreparedStatement prepDataSt = sqldbConnection.prepareStatement("SELECT "+dbXFieldName+","+commaSelYFieldsNameString+" FROM "+dbTableName+" WHERE rowid>"+DynamicLastRowIdIndex);
        final ResultSet DataRestultSet = prepDataSt.executeQuery();
        final PreparedStatement prepLastIdxSt = sqldbConnection.prepareStatement("SELECT rowid FROM "+dbTableName+" ORDER BY rowid DESC LIMIT 1");
        final ResultSet rsXLastRowId = prepLastIdxSt.executeQuery();
        sqldbConnection.commit();
        //--collect data
        while(DataRestultSet.next()){
          avalibilityFlag = 1.0;
          // x-column
          XFieldData.add(DataRestultSet.getDouble(dbXFieldName));
          //System.out.println(DataRestultSet.getDouble(dbXFieldName));
          // y-columns
          for (int i = 0; i < selYFieldsNameList.size(); i++) {
            multiYFieldsDataList.get(i).add(DataRestultSet.getDouble(selYFieldsNameList.get(i))); // y-column i
            //System.out.println("DataRestultSet.getDouble(selYFieldsNameList.get(i)): "+DataRestultSet.getDouble(selYFieldsNameList.get(i)));
          }
        }
        
        //--update current sql-cursor DynamicLastRowIdIndex
        updatedIndex = rsXLastRowId.getInt("rowid");
        DynamicLastRowIdIndex = updatedIndex;
        if (updatedIndex > DynamicLastRowIdIndex) {
          System.out.println((updatedIndex-DynamicLastRowIdIndex)+" new data points aded to sqlite database, updated Last-Row-Index = "+updatedIndex);
        } else{
          System.out.println("No new data points added to sqlite database.");
        }
        
        //--update return list
        XYdata.addAll(avalibilityFlag,XFieldData, multiYFieldsDataList, updatedIndex);
      } catch (SQLException ex) {
        Logger.getLogger(FetchDBDataTask.class.getName()).log(Level.SEVERE, null, ex);
      }
      
      return XYdata;
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
        Logger.getLogger(EXNMonitorMainViewController.class.getName()).log(Level.SEVERE, null, ex);
      }

      return pooledConn;
    } 
    
  }    



  
  abstract class DBTask<T> extends Task<T> {
    // set customized Task constructor
    public DBTask() {
      setOnFailed(new EventHandler<WorkerStateEvent>() {
        @Override
        public void handle(WorkerStateEvent event) {
          //logger.log(Level.SEVERE, null, getException());
        }
      });
    }
  }
