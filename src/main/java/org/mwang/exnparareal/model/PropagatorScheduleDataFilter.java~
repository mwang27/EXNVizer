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
public class PropagatorScheduleDataFilter {
  
  // input to PararealWorkflowDataFilter
  private final PararealProfileDataParser pararealProfileDataParser;
 
  
  
  
  /**
   * Constructor.
   * @param pararealProfileDataParser
   */
  public PropagatorScheduleDataFilter(PararealProfileDataParser pararealProfileDataParser) {
    this.pararealProfileDataParser = pararealProfileDataParser;
  }
  
  
  
  
  // Data Filtering Structure: {XData; YData; ExtraData}
  public ObservableList<PropSchedDataItem> getCoarsePropSchedObDataList() {
    final ObservableList<PropSchedDataItem> coarsePropSchedObDataList = FXCollections.observableArrayList();
    PropSchedDataItem coarsePropSchedObDataItem; // coarse: {Start_time; propIndex; duration}
    
    final ObservableList<Float> starttimeDataObList_C = pararealProfileDataParser.getStarttimeDataObList_C();
    final ObservableList<Integer> propIndexDataObList_C = pararealProfileDataParser.getPropIndexDataObList_C();
    final ObservableList<Float> durationDataObList_C = pararealProfileDataParser.getDurationDataObList_C();
    final ObservableList<Integer> tsDataObList_C = pararealProfileDataParser.getTsDataObList_C();
    final int dataSize = starttimeDataObList_C.size();
    
    for (int i = 0; i < dataSize; i++) {
      coarsePropSchedObDataItem = new PropSchedDataItem();
      coarsePropSchedObDataItem.setStart_time(starttimeDataObList_C.get(i));
      coarsePropSchedObDataItem.setProp_index(propIndexDataObList_C.get(i));
      coarsePropSchedObDataItem.setDuration(durationDataObList_C.get(i));
      coarsePropSchedObDataItem.setTs(tsDataObList_C.get(i));
      coarsePropSchedObDataList.addAll(coarsePropSchedObDataItem);
    }    
    
    return coarsePropSchedObDataList;
  }

  public ObservableList<PropSchedDataItem> getFinePropSchedObDataList() {
    final ObservableList<PropSchedDataItem> finePropSchedObDataList = FXCollections.observableArrayList();
    PropSchedDataItem finePropSchedObDataItem; // fine: {Start_time; propIndex; duration}
    
    final ObservableList<Float> starttimeDataObList_F = pararealProfileDataParser.getStarttimeDataObList_F();
    final ObservableList<Integer> propIndexDataObList_F = pararealProfileDataParser.getPropIndexDataObList_F();
    final ObservableList<Float> durationDataObList_F = pararealProfileDataParser.getDurationDataObList_F();
    final ObservableList<Integer> tsDataObList_F = pararealProfileDataParser.getTsDataObList_F();
    final int dataSize = pararealProfileDataParser.getStarttimeDataObList_F().size();
    for (int i = 0; i < dataSize; i++) {
      finePropSchedObDataItem = new PropSchedDataItem();
      finePropSchedObDataItem.setStart_time(starttimeDataObList_F.get(i));
      finePropSchedObDataItem.setProp_index(propIndexDataObList_F.get(i));
      finePropSchedObDataItem.setDuration(durationDataObList_F.get(i));
      finePropSchedObDataItem.setTs(tsDataObList_F.get(i));
      finePropSchedObDataList.addAll(finePropSchedObDataItem);
    }     
    
    return finePropSchedObDataList;
  }  
  
  public class PropSchedDataItem{
    
    private float start_time; // XData
    private int prop_index; // YData   
    private float duration; // ExtraData
    private int ts;
    
    public PropSchedDataItem() {
    }

    
    // getters and setters
    public float getStart_time() {
      return start_time;
    }

    public void setStart_time(float start_time) {
      this.start_time = start_time;
    }

    public int getProp_index() {
      return prop_index;
    }

    public void setProp_index(int prop_index) {
      this.prop_index = prop_index;
    }

    public float getDuration() {
      return duration;
    }

    public void setDuration(float duration) {
      this.duration = duration;
    }

    public int getTs() {
      return ts;
    }

    public void setTs(int ts) {
      this.ts = ts;
    }
    
    
    
  }
  
  
  
}
