package com.coffice.attachment;

import java.io.File;

import com.coffice.util.Guid;
import com.oreilly.servlet.multipart.FileRenamePolicy;

public class UploadFileRenamePolicy implements   FileRenamePolicy{
	 public File rename(File file) {
	        // TODO Auto-generated method stub
	        String body="";
	        String ext="";
	        String guid="",fileAllName="";
	        String wjmc=file.getName();
	        int pot=file.getName().lastIndexOf(".");
	        ext=file.getName().substring(pot);
	        guid=Guid.get();
			fileAllName=guid + wjmc.substring(0,wjmc.lastIndexOf(".") )+ext;             
	        file=new File(file.getParent(),fileAllName);
	        return file;
	    }
}
