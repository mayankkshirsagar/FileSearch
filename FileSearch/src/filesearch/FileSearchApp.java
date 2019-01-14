package filesearch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileSearchApp {
	
	String path;
	String regex;
	String zipFileName;
	
	public static void main(String args[]) throws IOException
	{
		Scanner sc=new Scanner(System.in);
		FileSearchApp app=new FileSearchApp();
		app.setPath(sc.next());
		app.setRegex(sc.next());
		app.setZipFileName(sc.next());
		ArrayList<File> result=app.readFiles(app.getPath());
		System.out.println("----files that match are----");
		
		for(int i=0;i<result.size();i++) {
			System.out.println(result.get(i).getName());
		}
		// create a zip file to store the result:
		
		sc.close();
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public String getZipFileName() {
		return zipFileName;
	}

	public void setZipFileName(String zipFileName) {
		this.zipFileName = zipFileName;
	}
	
	public ArrayList<File> readFiles(String filepath) throws IOException
	{
		File file=new File(this.getPath());
		File[] files=file.listFiles();
		ArrayList<File> filelist=new ArrayList<>();
		FileOutputStream f=new FileOutputStream(zipFileName);
		ZipOutputStream zos=new ZipOutputStream(f);
		
		for(File onefile:files)
		{	
			if(onefile.isDirectory())
			{
				this.readFiles(onefile.getAbsolutePath());
			}
			else 
			{
			FileReader fr=new FileReader(onefile);
			BufferedReader br=new BufferedReader(fr);
			
			if(this.searchFile(br,this.getRegex()))
			{
				System.out.println("file contains specifed regex, file name: "+onefile.getName());
				filelist.add(onefile);
				System.out.println("stored in result array");
				
				File aFile = new File(onefile.getAbsolutePath()); 
				FileInputStream fis = new FileInputStream(aFile); 
				ZipEntry zipEntry = new ZipEntry(onefile.getAbsolutePath()); 
				zos.putNextEntry(zipEntry); 
				byte[] bytes = new byte[1024]; 
				int length; while ((length = fis.read(bytes)) >= 0) 
				{ 
					zos.write(bytes, 0, length);
				} 
				zos.closeEntry(); 
				fis.close();
				System.out.println("file zipped");
				//Read more: http://www.java67.com/2016/12/how-to-create-zip-file-in-java-zipentry-example.html#ixzz5cYi1webC
				
				
			}
			else
			{
				System.out.println("file does not contain specified regex, file name: "+onefile.getName());
			}
			}
		}
		zos.close();
		return filelist;
	}
	
	public boolean searchFile(BufferedReader br,String fileregex) throws IOException
	{	
		
		String line="";
		while((line=br.readLine())!=null) {
		Pattern p=Pattern.compile(fileregex);
		Matcher m=p.matcher(line);
		if(m.find())
		return true;
		else
			return false;
		}
		return false;
	}
	
	

}
