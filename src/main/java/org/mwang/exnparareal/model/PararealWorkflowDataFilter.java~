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
public class PararealWorkflowDataFilter {
  
  // input to PararealWorkflowDataFilter
  private final PararealProfileDataParser pararealProfileDataParser;
  
  
  
  
  /**
   * Constructor.
   * @param pararealProfileDataParser
   */
  public PararealWorkflowDataFilter(PararealProfileDataParser pararealProfileDataParser) {
    this.pararealProfileDataParser = pararealProfileDataParser;
  }
  
  
  
  
  // Data Filtering Structure: {XData; YData; ExtraData}
  public ObservableList<TaskExeDataItem> getCoarseTaskExeObDataList() {
    final ObservableList<TaskExeDataItem> coarseTaskExeDataObList = FXCollections.observableArrayList();
    TaskExeDataItem coarseTaskExeDataItem; // coarse: {ts; Start_time; (propIndex,duration)}
    
    final ObservableList<Integer> itDataObList_C = pararealProfileDataParser.getItDataObList_C();
    final ObservableList<Integer> tsDataObList_C = pararealProfileDataParser.getTsDataObList_C();
    final ObservableList<Float> starttimeDataObList_C = pararealProfileDataParser.getStarttimeDataObList_C();
    final ObservableList<Integer> propIndexDataObList_C = pararealProfileDataParser.getPropIndexDataObList_C();
    final ObservableList<Float> durationDataObList_C = pararealProfileDataParser.getDurationDataObList_C();
    final ObservableList<Float> percentageDataObList_C = pararealProfileDataParser.getPercentageDataObList_C();
    final int dataSize = tsDataObList_C.size();
    
    for (int i = 0; i < dataSize; i++) {
      coarseTaskExeDataItem = new TaskExeDataItem();
      coarseTaskExeDataItem.setIt(itDataObList_C.get(i));
      coarseTaskExeDataItem.setTs(tsDataObList_C.get(i));
      coarseTaskExeDataItem.setStart_time(starttimeDataObList_C.get(i));
      coarseTaskExeDataItem.setProp_index(propIndexDataObList_C.get(i));
      coarseTaskExeDataItem.setDuration(durationDataObList_C.get(i));
      coarseTaskExeDataItem.setPercentage(percentageDataObList_C.get(i));
      coarseTaskExeDataObList.addAll(coarseTaskExeDataItem);
    }
    
    return coarseTaskExeDataObList;
  }

  public ObservableList<TaskExeDataItem> getFineTaskExeObDataList() {
    final ObservableList<TaskExeDataItem> fineTaskExeDataObList = FXCollections.observableArrayList();
    TaskExeDataItem fineTaskExeDataItem; // fine: {ts; Start_time; (propIndex,duration)}
    
    final ObservableList<Integer> itDataObList_F = pararealProfileDataParser.getItDataObList_F();
    final ObservableList<Integer> tsDataObList_F = pararealProfileDataParser.getTsDataObList_F();
    final ObservableList<Float> starttimeDataObList_F = pararealProfileDataParser.getStarttimeDataObList_F();
    final ObservableList<Integer> propIndexDataObList_F = pararealProfileDataParser.getPropIndexDataObList_F();
    final ObservableList<Float> durationDataObList_F = pararealProfileDataParser.getDurationDataObList_F();
    final ObservableList<Float> percentageDataObList_F = pararealProfileDataParser.getPercentageDataObList_F();
    final int dataSize = tsDataObList_F.size();
    
    for (int i = 0; i < dataSize; i++) {
      fineTaskExeDataItem = new TaskExeDataItem();
      fineTaskExeDataItem.setIt(itDataObList_F.get(i));
      fineTaskExeDataItem.setTs(tsDataObList_F.get(i));
      fineTaskExeDataItem.setStart_time(starttimeDataObList_F.get(i));
      fineTaskExeDataItem.setProp_index(propIndexDataObList_F.get(i));
      fineTaskExeDataItem.setDuration(durationDataObList_F.get(i));
      fineTaskExeDataItem.setPercentage(percentageDataObList_F.get(i));
      fineTaskExeDataObList.addAll(fineTaskExeDataItem);
    }    
    
    return fineTaskExeDataObList;
  }  

  public ObservableList<DepDataItem> getCoarseDepObDataList() {
    final ObservableList<DepDataItem> coarseDepDataObList = FXCollections.observableArrayList();
    DepDataItem coarseDepDataItem; // coarse: {ts; Start_time; (propIndex,percentage)}
    
    final ObservableList<Integer> tsDataObList_C = pararealProfileDataParser.getTsDataObList_C();
    final ObservableList<Float> starttimeDataObList_C = pararealProfileDataParser.getStarttimeDataObList_C();
    final ObservableList<Integer> propIndexDataObList_C = pararealProfileDataParser.getPropIndexDataObList_C();
    final ObservableList<Float> percentageDataObList_C = pararealProfileDataParser.getPercentageDataObList_C();
    final int dataSize = tsDataObList_C.size();
    
    for (int i = 0; i < dataSize; i++) {
      coarseDepDataItem = new DepDataItem();
      coarseDepDataItem.setTs(tsDataObList_C.get(i));
      coarseDepDataItem.setStart_time(starttimeDataObList_C.get(i));
      coarseDepDataItem.setProp_index(propIndexDataObList_C.get(i));
      coarseDepDataItem.setPercentage(percentageDataObList_C.get(i));
      coarseDepDataObList.addAll(coarseDepDataItem);
    }    
    
    return coarseDepDataObList;
  }  
  
  public ObservableList<DepDataItem> getFineDepObDataList() {
    final ObservableList<DepDataItem> fineDepDataObList = FXCollections.observableArrayList();
    DepDataItem fineDepDataItem;  // fine: {ts; Start_time; (propIndex,percentage)}
    
    final ObservableList<Integer> tsDataObList_F = pararealProfileDataParser.getTsDataObList_F();    
    final ObservableList<Float> starttimeDataObList_F = pararealProfileDataParser.getStarttimeDataObList_F();
    final ObservableList<Integer> propIndexDataObList_F = pararealProfileDataParser.getPropIndexDataObList_F();
    final ObservableList<Float> percentageDataObList_F = pararealProfileDataParser.getPercentageDataObList_F();
    final int dataSize = tsDataObList_F.size();

    for (int i = 0; i < dataSize; i++) {
      fineDepDataItem = new DepDataItem();
      fineDepDataItem.setTs(tsDataObList_F.get(i));
      fineDepDataItem.setStart_time(starttimeDataObList_F.get(i));
      fineDepDataItem.setProp_index(propIndexDataObList_F.get(i));
      fineDepDataItem.setPercentage(percentageDataObList_F.get(i));
      fineDepDataObList.addAll(fineDepDataItem);
    }     
    
    return fineDepDataObList;
  }
  
  
  // parareal data dependency workflow data item
  public class DepDataItem{

    private int ts; // XData
    private float start_time; // YData
    private int prop_index; // ExtraData
    private float percentage; // ExtraData

    public DepDataItem() {
    }    
    
    // getters and setters
    public int getTs() {
      return ts;
    }

    public void setTs(int ts) {
      this.ts = ts;
    }

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

    public float getPercentage() {
      return percentage;
    }

    public void setPercentage(float percentage) {
      this.percentage = percentage;
    }
  }
  
  // parareal task execution workflow data item
  public class TaskExeDataItem{

    private int it;
    private int ts;
    private float start_time;
    private int prop_index;
    private float duration;
    private float percentage;

    public TaskExeDataItem() {
    }    
    
    // getters and setters

    public int getIt() {
      return it;
    }

    public void setIt(int it) {
      this.it = it;
    }
    
    public int getTs() {
      return ts;
    }

    public void setTs(int ts) {
      this.ts = ts;
    }

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

    public float getPercentage() {
      return percentage;
    }

    public void setPercentage(float percentage) {
      this.percentage = percentage;
    }
    
    
  }  
  
}
