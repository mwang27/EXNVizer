/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mwang.exnparareal.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author envenio
 */
public class PerformanceStatisticsDataMiner {
  
  // input
  private final PerformanceStatisticsDataFilter performanceStatisticsDataFilter;

  
  
  /**
   * Constructor
   * @param performanceStatisticsDataFilter
   */
  public PerformanceStatisticsDataMiner(PerformanceStatisticsDataFilter performanceStatisticsDataFilter) {
    this.performanceStatisticsDataFilter = performanceStatisticsDataFilter;
  }

  
  
  
  public ObservableList<SpeedupDataItem> getSpeedupObDataList() {
    final ObservableList<SpeedupDataItem> speedupObDataList = FXCollections.observableArrayList();
    SpeedupDataItem speedupDataItem; // {winIdx; speedupDataItem}
    
    final ObservableList<Float> sequentialRuntimeObDataList = performanceStatisticsDataFilter.getSequentialRuntimeObDataList();
    final ObservableList<Float> winTotalSolverTimeObDataList = performanceStatisticsDataFilter.getWinTotalSoverTimeObDataList();
    final int dataSize = sequentialRuntimeObDataList.size();

    for (int i = 0; i < dataSize; i++) {
      speedupDataItem = new SpeedupDataItem();
      speedupDataItem.setWinIdx(i+1);
      speedupDataItem.setWinSpeedup(sequentialRuntimeObDataList.get(i)/winTotalSolverTimeObDataList.get(i));
      speedupObDataList.addAll(speedupDataItem);
    }    
    
    return speedupObDataList;
  }

  public ObservableList<EfficiencyDataItem> getEfficiencyObDataList() {
    final ObservableList<EfficiencyDataItem> efficiencyObDataList = FXCollections.observableArrayList();
    
    return efficiencyObDataList;
  }
  
  
  
  
  
  
  public class SpeedupDataItem{
    
    private int winIdx;
    private float winSpeedup;

    public SpeedupDataItem() {
    }
    
    
    // gettters and setters
    public int getWinIdx() {
      return winIdx;
    }

    public void setWinIdx(int winIdx) {
      this.winIdx = winIdx;
    }

    public float getWinSpeedup() {
      return winSpeedup;
    }

    public void setWinSpeedup(float winSpeedup) {
      this.winSpeedup = winSpeedup;
    }
    
  }
  
  
  public class EfficiencyDataItem{
    
  }  
  
}
