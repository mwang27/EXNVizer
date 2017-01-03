/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mwang.exnparareal.ui.view;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author mwang
 */
public class PropagatorIndexNode extends StackPane{
  
  private final int propIndex;
  private final String winFlag;
  private int maxwin;
  
  /**
   * Constructor
   * @param propIndex
   * @param winFlag
   */
  public PropagatorIndexNode(int propIndex, String winFlag, int maxwin){
    this.propIndex = propIndex;
    this.winFlag = winFlag;
    this.maxwin = maxwin;
    
    double rightPad = 0;
    double bottomPad = 45;
    
    double fontSizeAllWin;
    if (maxwin >= 3) {
      fontSizeAllWin = 12.0/(maxwin-2);
    } else {
      fontSizeAllWin = 12.0/maxwin;
    }
    
    final Label propIndexLabel = new Label("p"+propIndex);
    propIndexLabel.setMinWidth(25);
    
    
    if (winFlag.equals("single")) {
      setPadding(new Insets(95, rightPad, bottomPad, 45));
      propIndexLabel.setStyle("-fx-font-size: 12px;");
    }
    if (winFlag.equals("all")) {
      setPadding(new Insets(60, rightPad, bottomPad, 27));
      propIndexLabel.setStyle("-fx-font-size: "+fontSizeAllWin+"px;");
    }
    
    getChildren().setAll(propIndexLabel);
    
    toFront();
    
  }  
  
}
