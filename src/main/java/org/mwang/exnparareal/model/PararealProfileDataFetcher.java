/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mwang.exnparareal.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 *
 * @author envenio
 */
public class PararealProfileDataFetcher implements DataFetcher{
  
  // input for data fetcher
  private final int winIdx;
  private final String casePath;
  
  
  public PararealProfileDataFetcher(String casePath, int winIdx) {
    this.winIdx = winIdx;
    this.casePath = casePath;
  }
  
  
  @Override
  public String fetchProfileData() throws FileNotFoundException, IOException{
    final String fileName = "parareal_profile_win-"+String.valueOf(winIdx)+".txt";
    final RandomAccessFile file = new RandomAccessFile(casePath+fileName, "rw");
    // get: NIT, NTS
    final FileChannel fc = file.getChannel();
    final ByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
    final String profile_in_mem_data = Charset.forName("UTF-8").decode(bb).toString();
    return profile_in_mem_data;
  }
  
  @Override
  public int getWinIdx(){
    return winIdx;
  }
  
  
}
