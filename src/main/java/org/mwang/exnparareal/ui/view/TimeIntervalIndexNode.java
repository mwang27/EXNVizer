/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mwang.exnparareal.ui.view;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**
 *
 * @author mwang
 */
public class TimeIntervalIndexNode extends StackPane{

  private final int ts;
  
  /**
   * Constructor
   * @param ts
   */
  public TimeIntervalIndexNode(int ts){

    this.ts = ts;
    
//    double topPad = 0;
//    double rightPad = 0;
//    double bottomPad = 55;
//    double leftPad = 35;
    
    setPadding(new Insets(70, 0, 55, 35));

    final Label tsIndexLabel = new Label("ts"+ts);
    tsIndexLabel.setMinWidth(30);    
    tsIndexLabel.setStyle("-fx-font-size: 12px;");
    
    getChildren().setAll(tsIndexLabel);
    
    toFront();
    
  }  

  
}
