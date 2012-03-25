package app;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import controller.Controller;
import model.Model;
import view.View;
import org.apache.*;
import org.apache.commons.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class Application
{
    public static View view;
    public static Model model;
    public static Controller controller;
    /**
     * @param args
     */
    public static void main(String[] args)
    {
	// TODO Auto-generated method stub
	loadLibrary();
	try
	{
	view = new View();
	model=new Model();
	controller=new Controller();
	}
	catch(Exception e)
	{
	    System.out.println("app error - "+e.getLocalizedMessage());
	    e.printStackTrace();
	}
	finally
	{
	    System.out.println("last breathe!");
	    model.serializeList();
	}
    }
    private static void loadLibrary()
    {
	// TODO Auto-generated method stub
	String name1,name=System.getenv("APPDATA");
	name1=name+"\\JIntellitype.dll";
	//System.out.println(name1);
	try{
	    File f = new File(name);
	    System.out.println(f + " \t" + f.exists());
	    f = new File(f, "JIntellitype.dll");
	    System.out.println(f + " \t" + f.exists());
	    System.loadLibrary("JIntellitype");
	   // System.load( f.toString() );
	//System.load(name2);
	}
	catch(Exception e)
	{System.out.println("error + "+e.getMessage());
	e.printStackTrace();}
	
	try
	{
	    System.out.println("copy reminderApp_lib\\JIntellitype.dll "+name);
	    Runtime.getRuntime().exec("copy reminderApp_lib\\JIntellitype.dll "+name);
	} catch (IOException e)
	{
	    // TODO Auto-generated catch block
	    System.out.println("error caught! "+e.getMessage());
	    e.printStackTrace();
	}
	
    }

}



/*
things TODO
1. use date everywhere 		done
2. design a show reminder	done
3. hotkey			done
4. controller			done
5. 


*/