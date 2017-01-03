/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mwang.exnparareal.model;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author envenio
 */
public class PerformanceStatisticsDataFilter {
  
  // input
  private final String casePath;
  private final int finishedWinTotal;

  // inner var
  private int nts;
  private ObservableList<PararealProfileDataParser> parserObList;
  
  public PerformanceStatisticsDataFilter(String casePath, int finishedWinTotal) {
    this.casePath = casePath;
    this.finishedWinTotal = finishedWinTotal;
    initMultipleParsers();
  }

  
  private void initMultipleParsers(){
    try {
      parserObList = FXCollections.observableArrayList();
      PararealProfileDataFetcher fetcher;
      PararealProfileDataParser parser;
      
      fetcher = new PararealProfileDataFetcher(casePath, 1);
      parser = new PararealProfileDataParser(1, fetcher.fetchProfileData());
      nts = (int)parser.getPararealProfileGlobalInfoObList().get(1);
      
      for (int i = 0; i < finishedWinTotal; i++) {
        fetcher = new PararealProfileDataFetcher(casePath, i+1);
        parser = new PararealProfileDataParser(i+1, fetcher.fetchProfileData());
        parserObList.addAll(parser);
      }
      
    } catch (IOException ex) {
      Logger.getLogger(PerformanceStatisticsDataFilter.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  
  public ObservableList<Float> getWinTotalSoverTimeObDataList() {
    final ObservableList<Float> winTotalSolverTimeObDataList = FXCollections.observableArrayList();
    
    float winTotalSolverTimeDataItem = 0;
    for (int winIdx = 0; winIdx < finishedWinTotal; winIdx++) {
      winTotalSolverTimeDataItem = parserObList.get(winIdx).getWinTotalSolverTime();
      winTotalSolverTimeObDataList.addAll(winTotalSolverTimeDataItem);
      //System.out.println("winTotalSolverTimeDataItem: "+winTotalSolverTimeDataItem);
    }
    
    return winTotalSolverTimeObDataList;
  }

  public ObservableList<Float> getSequentialRuntimeObDataList() {
    final ObservableList<Float> sequentialRuntimeObDataList = FXCollections.observableArrayList();
    
    float sequentialRuntimeDataItem;
    for (int winIdx = 0; winIdx < finishedWinTotal; winIdx++) {
      sequentialRuntimeDataItem = 0;
      for (int i = 0; i < nts; i++) {
        sequentialRuntimeDataItem = sequentialRuntimeDataItem + parserObList.get(winIdx).getDurationDataObList_F().get(i);
      }
      //System.out.println("sequentialRuntimeDataItem: "+sequentialRuntimeDataItem);
      sequentialRuntimeObDataList.addAll(sequentialRuntimeDataItem);      
    }
    
    return sequentialRuntimeObDataList;
  }
  
}
