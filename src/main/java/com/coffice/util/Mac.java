package com.coffice.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Mac {
	public static String getMac(){
	String mac = "";    
    String line = "";        
    String os = System.getProperty("os.name");
    if (os != null && os.startsWith("Windows"))   
    {     
        try   
        {     
            String command = "cmd.exe /c ipconfig /all";     
            Process p = Runtime.getRuntime().exec(command);     
              
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));     
              
            while((line = br.readLine()) != null)  
            {     
                if (line.indexOf("Physical Address") > 0)  
                {     
                    int index = line.indexOf(":") + 2;  
                      
                    mac = line.substring(index);  
                      
                    break;     
                }     
            }     
              
            br.close();     
              
        } catch (IOException e) {}     
    }     
      
    return mac;  
}
}
