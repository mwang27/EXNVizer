/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mwang.exnvizer.main;

import com.panemu.tiwulfx.common.TiwulFXUtil;
import com.sun.deploy.uitoolkit.impl.fx.HostServicesFactory;
import com.sun.javafx.application.HostServicesDelegate;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.mwang.exnmonitor.ui.controller.EXNMonitorMainViewController;
import org.mwang.exnvizer.ui.controller.EXNVizerMainViewController;

/**
 *
 * @author mwang
 */
public class EXNVizer extends Application {
  
  private ExecutorService executorService;

  public EXNVizer() {
    this.executorService = Executors.newFixedThreadPool(2);
  }
  
  @Override
  public void start(Stage stage) throws Exception {
    
    final HostServicesDelegate hostServices = HostServicesFactory.getInstance(this);
    
    final FXMLLoader exnVizerMainLoader = new FXMLLoader(getClass().getResource("/org/mwang/exnvizer/ui/view/EXNVizerMainView.fxml"));
    
    Thread.currentThread().setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

      @Override
      public void uncaughtException(Thread t, Throwable e) {
        System.out.println("uncaught exception");
        e.printStackTrace();
      }
    });
    
    final EXNVizerMainViewController exnvizerMainController = new EXNVizerMainViewController();
    exnvizerMainController.setExecutorService(executorService);
    exnvizerMainController.setHostServices(hostServices);
    exnVizerMainLoader.setController(exnvizerMainController);
    
    final BorderPane exnVizerMainView = exnVizerMainLoader.load();
    final Scene scene = new Scene(exnVizerMainView, 1625, 1027);
    TiwulFXUtil.setTiwulFXStyleSheet(scene);


    stage.setTitle("EXN/Vizer - v0.8");    
    stage.setScene(scene);
    stage.show();
    
//    createEXNVizerRunScript("K40");
//    createEXNVizerRunScript("Y510p");
  }
  
  
  @Override
  public void stop() {
    if (executorService != null) {
        executorService.shutdown();
    }
  }
  
  
  private void createEXNVizerRunScript(String serverName){
    if (serverName.equals("Y510p")) {
      try {
        try (final PrintWriter writer = new PrintWriter(System.getProperty("user.home")+"/GradleProjects/EXNVizer_early05/runEXNVizer"+serverName, "UTF-8")) {
          writer.println("#!/bin/bash");
          writer.println("if [ ! -d \"/home/mwang/GradleProjects/EXNVizer_early05/EXNVizer/build/distributions/EXNVizer\" ]; then");
          writer.println("  unzip -q \"/home/mwang/GradleProjects/EXNVizer_early05/EXNVizer/build/distributions/EXNVizer.zip\" -d \"/home/mwang/GradleProjects/EXNVizer_early05/EXNVizer/build/distributions/\"");
          writer.println("fi");
          writer.println("cd \"/home/mwang/GradleProjects/EXNVizer_early05/EXNVizer/build/distributions/EXNVizer/bin\"");
          writer.println("mkdir -p \"${HOME}/GradleProjects/EXNVizer_early05/java_tmp\"");
          writer.println("./EXNVizer &");
        }
      } catch (FileNotFoundException | UnsupportedEncodingException ex) {
        Logger.getLogger(EXNMonitorMainViewController.class.getName()).log(Level.SEVERE, null, ex);
      }      
    } else {
      try {
        try (final PrintWriter writer = new PrintWriter(System.getProperty("user.home")+"/GradleProjects/EXNVizer_early05/runEXNVizer"+serverName, "UTF-8")) {
          writer.println("#!/bin/bash");
          writer.println("cd \"/home/landsailor/EXNVizer_early05/EXNVizer/bin\"");
          writer.println("mkdir -p \"${HOME}/EXNVizer_early05/java_tmp\"");
          writer.println("./EXNVizer &");
        }
      } catch (FileNotFoundException | UnsupportedEncodingException ex) {
        Logger.getLogger(EXNMonitorMainViewController.class.getName()).log(Level.SEVERE, null, ex);
      }      
    }

  }  

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }
  
}
