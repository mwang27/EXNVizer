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
public class PararealProfileDataParser implements DataParser{
  
  // input for data parser
  private final String profile_in_mem_data;
  private final int winIdx;
  
  // parse data variables
  private enum ParseParameter{
    IT_IDX_START(1),IT_IDX_END(5),
    TS_IDX_START(6),TS_IDX_END(10),
    PROP_TYPE_IDX_START(11),PROP_TYPE_IDX_END(13),
    PROP_IDX_START(14), PROP_IDX_END(16),
    PERCENT_IDX_START(17), PERCENT_IDX_END(24),
    START_TIME_IDX_START(25), START_TIME_IDX_END(40),
    DURATION_IDX_START(41), DURATION_IDX_END(54);
    
    private final int value;
    
    private ParseParameter(int value){
      this.value = value;
    }
  }  
  
  // output from data parser
  // parareal profile global vars
  //--line#1
  private int nit;
  private int nts;
  private int nprop;
  //--line#2
  private int maxwin;
  private float xtime;
  private float xspace;
  //--line#3
  private float desblend;
  private float wscblend;  


  
  
  /**
   * Constructor
   * @param winIdx
   * @param profile_in_mem_data
   */
  public PararealProfileDataParser(int winIdx, String profile_in_mem_data) {
    this.winIdx = winIdx;
    this.profile_in_mem_data = profile_in_mem_data;
    initDataParser();
  }
  
  // parse parareal profile global info data
  private void initDataParser(){
    //--line#1
    nit = Integer.parseInt(profile_in_mem_data.substring(6, 9).trim());
    nts = Integer.parseInt(profile_in_mem_data.substring(14, 19).trim());
    nprop = Integer.parseInt(profile_in_mem_data.substring(26, 30).trim());
    //--line#2
    maxwin = Integer.parseInt(profile_in_mem_data.substring(18+60, 21+60).trim());
    xtime = Float.parseFloat(profile_in_mem_data.substring(29+60, 35+60).trim());
    xspace = Float.parseFloat(profile_in_mem_data.substring(44+60, 50+60).trim());
    //--line#3
    desblend = Float.parseFloat(profile_in_mem_data.substring(11+120, 17+120).trim());
    wscblend = Float.parseFloat(profile_in_mem_data.substring(28+120, 34+120).trim());
//    System.out.println("nts: "+nts);
//    System.out.println("nprop: "+nprop);
//    System.out.println("maxwin: "+maxwin);
    
  }  
  
  
  @Override
  public int getWinIdx(){
    return winIdx;
  }  
  
  
  // global info data
  @Override
  public ObservableList getPararealProfileGlobalInfoObList(){
    // must init ObservableList before usage
    final ObservableList pararealProfileGlobalInfoObList = FXCollections.observableArrayList();
    pararealProfileGlobalInfoObList.addAll(nit,nts,nprop,maxwin,xtime,xspace,desblend,wscblend);
    return pararealProfileGlobalInfoObList;
  }

  public float getWinTotalSolverTime() {
    final int pos_w = 89;
    final float winTotalSolverTime = Float.parseFloat(profile_in_mem_data.substring(pos_w*60+20, pos_w*60+35).trim());
    
    return winTotalSolverTime;
  }
  
  
  
  
  // parsing coarse propagator profile data
  @Override
  public ObservableList<Integer> getItDataObList_C() {
    // must init ObservableList before usage
    final ObservableList<Integer> ItDataObList_C = FXCollections.observableArrayList();
    
    // set temp vars to store data values
    int pos_c = 0;
    int prop_c_it = 0;
    
    // iteration 1 to nit
    for(int it=1; it<=nit; it++){
      for(int ts=it+(winIdx-1)*nts; ts<=nts+(winIdx-1)*nts; ts++){
        pos_c = 4+(2*nts-it+2)*(it-1)+((ts-nts*(winIdx-1))-it)*2;// line number of the coarse propagator
        // get prop_c_it
        prop_c_it = Integer.parseInt(profile_in_mem_data.substring(
            pos_c*60+ParseParameter.IT_IDX_START.value, 
            pos_c*60+ParseParameter.IT_IDX_END.value).trim()); 
        
        ItDataObList_C.add(prop_c_it);
      }
    }
    
    // extra iteration: NExtraProp=nts-nit
    pos_c = 4+(2*nts-(nit+1)+2)*((nit+1)-1)+(((nit+1+(winIdx-1)*nts)-nts*(winIdx-1))-(nit+1))*2;
    for(int i=1; i<=nts-nit; i++){
      
      // get prop_c_it
      prop_c_it = Integer.parseInt(profile_in_mem_data.substring(
          (pos_c+(i-1))*60+ParseParameter.IT_IDX_START.value, 
          (pos_c+(i-1))*60+ParseParameter.IT_IDX_END.value).trim()); 
      ItDataObList_C.add(prop_c_it);
    }     
    
    
    return ItDataObList_C;  
  }

  @Override
  public ObservableList<Integer> getTsDataObList_C() {
    // must init ObservableList before usage
    final ObservableList<Integer> tsDataObList_C = FXCollections.observableArrayList();
    
    // set temp vars to store data values
    int pos_c = 0;
    int prop_c_ts = 0;
    
    // iteration 1 to nit
    for(int it=1; it<=nit; it++){
      for(int ts=it+(winIdx-1)*nts; ts<=nts+(winIdx-1)*nts; ts++){
        pos_c = 4+(2*nts-it+2)*(it-1)+((ts-nts*(winIdx-1))-it)*2;// line number of the coarse propagator
        // get prop_c_ts
        prop_c_ts = Integer.parseInt(profile_in_mem_data.substring(
            pos_c*60+ParseParameter.TS_IDX_START.value, 
            pos_c*60+ParseParameter.TS_IDX_END.value).trim()); 
        tsDataObList_C.add(prop_c_ts);
      }
    }
    
    // extra iteration: NExtraProp=nts-nit
    pos_c = 4+(2*nts-(nit+1)+2)*((nit+1)-1)+(((nit+1+(winIdx-1)*nts)-nts*(winIdx-1))-(nit+1))*2;
    for(int i=1; i<=nts-nit; i++){
      // get prop_c_ts
      prop_c_ts = Integer.parseInt(profile_in_mem_data.substring(
          (pos_c+(i-1))*60+ParseParameter.TS_IDX_START.value, 
          (pos_c+(i-1))*60+ParseParameter.TS_IDX_END.value).trim()); 
      tsDataObList_C.add(prop_c_ts);
    }
    
    return tsDataObList_C;
  }

  @Override
  public ObservableList<String> getPropTypeDataObList_C() {
    // must init ObservableList before usage
    final ObservableList<String> propTypeDataObList_C = FXCollections.observableArrayList();
    
    return propTypeDataObList_C;
  }

  @Override
  public ObservableList<Integer> getPropIndexDataObList_C() {   
    // must init ObservableList before usage
    final ObservableList<Integer> propIndexDataObList_C = FXCollections.observableArrayList();
    
    // set temp vars to store data values
    int pos_c = 0;
    int prop_c_index = 0;
    
    // iteration 1 to nit
    for(int it=1; it<=nit; it++){
      for(int ts=it+(winIdx-1)*nts; ts<=nts+(winIdx-1)*nts; ts++){
        pos_c = 4+(2*nts-it+2)*(it-1)+((ts-nts*(winIdx-1))-it)*2;// line number of the coarse propagator
        // get prop_c_index
        prop_c_index = Integer.parseInt(profile_in_mem_data.substring(
            pos_c*60+ParseParameter.PROP_IDX_START.value, 
            pos_c*60+ParseParameter.PROP_IDX_END.value).trim());         
        propIndexDataObList_C.add(prop_c_index);
      }
    }
    
    // extra iteration: NExtraProp=nts-nit
    pos_c = 4+(2*nts-(nit+1)+2)*((nit+1)-1)+(((nit+1+(winIdx-1)*nts)-nts*(winIdx-1))-(nit+1))*2;
    for(int i=1; i<=nts-nit; i++){
      // get prop_c_index
      prop_c_index = Integer.parseInt(profile_in_mem_data.substring(
          (pos_c+(i-1))*60+ParseParameter.PROP_IDX_START.value, 
          (pos_c+(i-1))*60+ParseParameter.PROP_IDX_END.value).trim());      
      propIndexDataObList_C.add(prop_c_index); 
    }
    
    return propIndexDataObList_C;
  }  
  
  @Override
  public ObservableList<Float> getPercentageDataObList_C() {
    // must init ObservableList before usage
    final ObservableList<Float> percentageDataObList_C = FXCollections.observableArrayList();    
    
    int pos_c = 0;
    float prop_c_percent = 0;
    
    // iteration 1 to nit
    for(int it=1; it<=nit; it++){
      for(int ts=it+(winIdx-1)*nts; ts<=nts+(winIdx-1)*nts; ts++){
        pos_c = 4+(2*nts-it+2)*(it-1)+((ts-nts*(winIdx-1))-it)*2;// line number of the coarse propagator
        prop_c_percent = Float.parseFloat(profile_in_mem_data.substring(
            pos_c*60+ParseParameter.PERCENT_IDX_START.value, 
            pos_c*60+ParseParameter.PERCENT_IDX_END.value));

        percentageDataObList_C.add(prop_c_percent);
      }
    }
    
    // extra iteration: NExtraProp=nts-nit
    pos_c = 4+(2*nts-(nit+1)+2)*((nit+1)-1)+(((nit+1+(winIdx-1)*nts)-nts*(winIdx-1))-(nit+1))*2;
    for(int i=1; i<=nts-nit; i++){
      prop_c_percent = Float.parseFloat(profile_in_mem_data.substring(
          (pos_c+(i-1))*60+ParseParameter.PERCENT_IDX_START.value, 
          (pos_c+(i-1))*60+ParseParameter.PERCENT_IDX_END.value));

      percentageDataObList_C.add(prop_c_percent);
    }    
    
    return percentageDataObList_C;
  }
  
  @Override
  public ObservableList<Float> getStarttimeDataObList_C() {
    // must init ObservableList before usage
    final ObservableList<Float> StarttimeDataObList_C = FXCollections.observableArrayList();
    
    // set temp vars to store data values
    int pos_c = 0;
    float prop_c_Start_time = 0;
    
    // iteration 1 to nit
    for(int it=1; it<=nit; it++){
      for(int ts=it+(winIdx-1)*nts; ts<=nts+(winIdx-1)*nts; ts++){
        pos_c = 4+(2*nts-it+2)*(it-1)+((ts-nts*(winIdx-1))-it)*2;// line number of the coarse propagator
        
        // get prop_c_Start_time
        prop_c_Start_time = Float.parseFloat(profile_in_mem_data.substring(
            pos_c*60+ParseParameter.START_TIME_IDX_START.value, 
            pos_c*60+ParseParameter.START_TIME_IDX_END.value));
        StarttimeDataObList_C.add(prop_c_Start_time);
      }
    }
    
    // extra iteration: NExtraProp=nts-nit
    pos_c = 4+(2*nts-(nit+1)+2)*((nit+1)-1)+(((nit+1+(winIdx-1)*nts)-nts*(winIdx-1))-(nit+1))*2;
    for(int i=1; i<=nts-nit; i++){
      // get prop_c_Start_time
      prop_c_Start_time = Float.parseFloat(profile_in_mem_data.substring(
          (pos_c+(i-1))*60+ParseParameter.START_TIME_IDX_START.value, 
          (pos_c+(i-1))*60+ParseParameter.START_TIME_IDX_END.value));
      StarttimeDataObList_C.add(prop_c_Start_time);
    } 
    
    return StarttimeDataObList_C;
  }

  @Override
  public ObservableList<Float> getDurationDataObList_C() {
    // must init ObservableList before usage
    final ObservableList<Float> durationDataObList_C = FXCollections.observableArrayList();     
    
    int pos_c = 0;
    float prop_c_duration = 0;
    
    // iteration 1 to nit
    for(int it=1; it<=nit; it++){
      for(int ts=it+(winIdx-1)*nts; ts<=nts+(winIdx-1)*nts; ts++){
        pos_c = 4+(2*nts-it+2)*(it-1)+((ts-nts*(winIdx-1))-it)*2;// line number of the coarse propagator
        prop_c_duration = Float.parseFloat(profile_in_mem_data.substring(
            pos_c*60+ParseParameter.DURATION_IDX_START.value, 
            pos_c*60+ParseParameter.DURATION_IDX_END.value));

        durationDataObList_C.add(prop_c_duration);
      }
    }
    
    // extra iteration: NExtraProp=nts-nit
    pos_c = 4+(2*nts-(nit+1)+2)*((nit+1)-1)+(((nit+1+(winIdx-1)*nts)-nts*(winIdx-1))-(nit+1))*2;
    for(int i=1; i<=nts-nit; i++){
      prop_c_duration = Float.parseFloat(profile_in_mem_data.substring(
          (pos_c+(i-1))*60+ParseParameter.DURATION_IDX_START.value, 
          (pos_c+(i-1))*60+ParseParameter.DURATION_IDX_END.value));

      durationDataObList_C.add(prop_c_duration);
    }     
    
    return durationDataObList_C;
  }  
  
  
  
  
  // parsing fine propagator profile data
  @Override
  public ObservableList<Integer> getItDataObList_F() {
    // must init ObservableList before usage
    final ObservableList<Integer> ItDataObList_F = FXCollections.observableArrayList();
    
    // set temp vars to store data values
    int pos_c = 0;
    int prop_c_it = 0;
    
    // iteration 1 to nit
    for(int it=1; it<=nit; it++){
      for(int ts=it+(winIdx-1)*nts; ts<=nts+(winIdx-1)*nts; ts++){
        pos_c = 4+(2*nts-it+2)*(it-1)+((ts-nts*(winIdx-1))-it)*2;// line number of the coarse propagator
        // get prop_c_it
        prop_c_it = Integer.parseInt(profile_in_mem_data.substring(
            pos_c*60+ParseParameter.IT_IDX_START.value, 
            pos_c*60+ParseParameter.IT_IDX_END.value).trim()); 
        
        ItDataObList_F.add(prop_c_it);
      }
    }
    
    return ItDataObList_F;
  }

  @Override
  public ObservableList<Integer> getTsDataObList_F() {
    // must init ObservableList before usage
    final ObservableList<Integer> tsDataObList_F = FXCollections.observableArrayList();
    
    // set temp vars to store data values
    int pos_c = 0;
    int prop_c_ts = 0;
    
    // iteration 1 to nit
    for(int it=1; it<=nit; it++){
      for(int ts=it+(winIdx-1)*nts; ts<=nts+(winIdx-1)*nts; ts++){
        pos_c = 4+(2*nts-it+2)*(it-1)+((ts-nts*(winIdx-1))-it)*2;// line number of the coarse propagator
        // get prop_c_ts
        prop_c_ts = Integer.parseInt(profile_in_mem_data.substring(
            pos_c*60+ParseParameter.TS_IDX_START.value, 
            pos_c*60+ParseParameter.TS_IDX_END.value).trim()); 
        tsDataObList_F.add(prop_c_ts);
      }
    }
    
    return tsDataObList_F;
  }

  @Override
  public ObservableList<String> getPropTypeDataObList_F() {
    final ObservableList<String> propTypeDataObList_F = FXCollections.observableArrayList();
    
    return propTypeDataObList_F;
  }

  @Override
  public ObservableList<Integer> getPropIndexDataObList_F() {
    // must init ObservableList before usage
    final ObservableList<Integer> propIndexDataObList_F = FXCollections.observableArrayList();
    
    // set temp vars to store data values    
    int pos_f = 0;
    int prop_f_index = 0;    
    
    for(int it=1; it<=nit; it++){
      for(int ts=it+(winIdx-1)*nts; ts<=nts+(winIdx-1)*nts; ts++){
        pos_f = 4+(2*nts-it+2)*(it-1)+((ts-nts*(winIdx-1))-it)*2 + 1;// line number of the coarse propagator
        prop_f_index = Integer.parseInt(profile_in_mem_data.substring(
            pos_f*60+ParseParameter.PROP_IDX_START.value, 
            pos_f*60+ParseParameter.PROP_IDX_END.value).trim()); 
        propIndexDataObList_F.add(prop_f_index); 
      }
    }
    
    return propIndexDataObList_F;
  }
  
  @Override
  public ObservableList<Float> getPercentageDataObList_F() {
    // must init ObservableList before usage
    final ObservableList<Float> percentageDataObList_F = FXCollections.observableArrayList(); 
    
    int pos_f = 0;
    float prop_f_percent = 0;
    for(int it=1; it<=nit; it++){
      for(int ts=it+(winIdx-1)*nts; ts<=nts+(winIdx-1)*nts; ts++){
        pos_f = 4+(2*nts-it+2)*(it-1)+((ts-nts*(winIdx-1))-it)*2 + 1;// line number of the coarse propagator
        prop_f_percent = Float.parseFloat(profile_in_mem_data.substring(
            pos_f*60+ParseParameter.PERCENT_IDX_START.value, 
            pos_f*60+ParseParameter.PERCENT_IDX_END.value));
        percentageDataObList_F.add(prop_f_percent);
      }
    }     
    
    return percentageDataObList_F;
  }  
  
  @Override
  public ObservableList<Float> getStarttimeDataObList_F() {
    // must init ObservableList before usage
    final ObservableList<Float> StarttimeDataObList_F = FXCollections.observableArrayList();
    
    // set temp vars to store data values    
    int pos_f = 0;
    float prop_f_Start_time = 0;
    
    for(int it=1; it<=nit; it++){
      for(int ts=it+(winIdx-1)*nts; ts<=nts+(winIdx-1)*nts; ts++){
        pos_f = 4+(2*nts-it+2)*(it-1)+((ts-nts*(winIdx-1))-it)*2 + 1;// line number of the coarse propagator
        
        prop_f_Start_time = Float.parseFloat(profile_in_mem_data.substring(
            pos_f*60+ParseParameter.START_TIME_IDX_START.value, 
            pos_f*60+ParseParameter.START_TIME_IDX_END.value));
        StarttimeDataObList_F.add(prop_f_Start_time);
      }
    }    
    
    return StarttimeDataObList_F;
  }

  @Override
  public ObservableList<Float> getDurationDataObList_F() {
    // must init ObservableList before usage
    final ObservableList<Float> durationDataObList_F = FXCollections.observableArrayList();    
    
    int pos_f = 0;
    float prop_f_duration = 0;
    for(int it=1; it<=nit; it++){
      for(int ts=it+(winIdx-1)*nts; ts<=nts+(winIdx-1)*nts; ts++){
        pos_f = 4+(2*nts-it+2)*(it-1)+((ts-nts*(winIdx-1))-it)*2 + 1;// line number of the coarse propagator
        prop_f_duration = Float.parseFloat(profile_in_mem_data.substring(
            pos_f*60+ParseParameter.DURATION_IDX_START.value, 
            pos_f*60+ParseParameter.DURATION_IDX_END.value));

        durationDataObList_F.add(prop_f_duration);
      }
    }      
    
    return durationDataObList_F;
  }

}
