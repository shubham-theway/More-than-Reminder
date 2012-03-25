package model;

import java.io.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

import view.AddReminderFrame;
import model.*;


public class Model extends Observable
{
    private ReminderList reminderList;
    private File reminderFile;
    public Model()
    {
	reminderList=(new ReminderList());
	String name=System.getenv("APPDATA");
	name+="\\reminders.sdv";
	reminderFile=new File(name);
	deserializeListAndPopulateReminderList();
    }
    private void deserializeListAndPopulateReminderList()
    {
	// TODO Auto-generated method stub
	try
        {
	    if(!reminderFile.exists())
	    {
		FileWriter writer;
		writer=new FileWriter(reminderFile);
		return;
	    }
           FileInputStream fileIn =
                         new FileInputStream(reminderFile);
           
           ObjectInputStream in = new ObjectInputStream(fileIn);
           reminderList = (ReminderList) in.readObject();
           AddReminderFrame.setIndex(in.readInt());
           in.close();
           fileIn.close();
       }catch(IOException i)
       {
           i.printStackTrace();
           return;
       }catch(ClassNotFoundException c)
       {
           System.out.println(ReminderList.class + " not found!");
           c.printStackTrace();
           return;
       }
    }
    public void addReminder(ReminderData reminder)
    {
	((ReminderList) reminderList).addReminder(reminder);
	serializeList();
    }
    /*private ReminderList getReminderList()
    {
	return reminderList;
    }*/
    public void removeReminder(int index)
    {
	reminderList.removeReminder(index);
    }
    public void printReminders()
    {
	// TODO Auto-generated method stub
	Iterator<ReminderData> iter=reminderList.observableList.iterator();
	System.out.println("printing!");
	while(iter.hasNext())
	{
	    ReminderData rem=iter.next();
	    rem.show();
	}
    }
    public List getReminders()
    {
	// TODO Auto-generated method stub
	return reminderList.observableList;
    }

    public void serializeList()
    {
	try
	{
	    FileOutputStream fileOut =
		new FileOutputStream(reminderFile);
	    ObjectOutputStream out =
		new ObjectOutputStream(fileOut);
	    out.writeObject(this.reminderList);
	    out.writeInt(AddReminderFrame.getIndex());
	    out.close();
	    fileOut.close();
	}catch(IOException i)
	{
	    System.out.print("error = ");
	    i.printStackTrace();
	}
    }
    public ReminderData fetch(int ind)
    {
	// TODO Auto-generated method stub
	for(model.ReminderData rData : reminderList.observableList)
	{
	    if(rData.getIndex()==ind)
		return rData;
	}
	return null;
    }

}
