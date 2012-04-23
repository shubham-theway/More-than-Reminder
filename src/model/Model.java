package model;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import view.AddNoteFrame;
import view.AddReminderFrame;
import model.*;


public class Model extends Observable
{
    private ReminderList reminderList;
    private NoteList noteList;
    private File noteFile;
    private File remFile;
    public Model()
    {
	reminderList=(new ReminderList());
	noteList=new NoteList();
	String RemFileame=System.getenv("APPDATA");
	RemFileame+="\\rem.sdv";
	String noteFileName=System.getenv("APPDATA");
	noteFileName+="\\note.sdv";
	noteFile=new File(noteFileName);
	remFile=new File(RemFileame);
	deserializeListAndPopulateReminderList();
    }
    private void deserializeListAndPopulateReminderList()
    {
	// TODO Auto-generated method stub
	try
	{
	    if(!noteFile.exists())
	    {
		FileWriter writer;
		writer=new FileWriter(noteFile);
		return;
	    }
	    FileInputStream fileIn =
		new FileInputStream(noteFile);

	    ObjectInputStream in = new ObjectInputStream(fileIn);
	    noteList = (NoteList) in.readObject();
	    AddNoteFrame.setIndex((Integer) in.readObject());
	    //noteList=(NoteList)in.readObject();
	    //AddNoteFrame.setIndex(in.readInt());
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
	try
	{
	    if(!remFile.exists())
	    {
		FileWriter writer;
		writer=new FileWriter(remFile);
		return;
	    }
	    FileInputStream fileIn =
		new FileInputStream(remFile);

	    ObjectInputStream in = new ObjectInputStream(fileIn);
	    reminderList = (ReminderList) in.readObject();
	    AddReminderFrame.setIndex((Integer) in.readObject());
	    //noteList=(NoteList)in.readObject();
	    //AddNoteFrame.setIndex(in.readInt());
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
	serializeList();
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
		new FileOutputStream(noteFile);
	    ObjectOutputStream out =
		new ObjectOutputStream(fileOut);
	    //out.writeObject(this.reminderList);
	    //out.writeInt(AddReminderFrame.getIndex());
	    out.writeObject(this.noteList);
	    out.writeObject(AddNoteFrame.getIndex());
	    out.close();
	    fileOut.close();
	}catch(IOException i)
	{
	    System.out.print("error = ");
	    i.printStackTrace();
	}
	try
	{
	    FileOutputStream fileOut =
		new FileOutputStream(remFile);
	    ObjectOutputStream out =
		new ObjectOutputStream(fileOut);
	    //out.writeObject(this.noteList);
	    //out.writeInt(AddNoteFrame.getIndex());
	    out.writeObject(this.reminderList);
	    out.writeObject(AddReminderFrame.getIndex());
	    out.close();
	    fileOut.close();
	}catch(IOException i)
	{
	    System.out.print("error = ");
	    i.printStackTrace();
	}
    }
    public ReminderData fetchReminder(int ind)
    {
	// TODO Auto-generated method stub
	for(model.ReminderData rData : reminderList.observableList)
	{
	    if(rData.getIndex()==ind)
		return rData;
	}
	return null;
    }
    public NoteData fetchNote(int ind)
    {
	// TODO Auto-generated method stub
	for(model.NoteData nData : noteList.observableList)
	{
	    if(nData.getIndex()==ind)
		return nData;
	}
	return null;
    }
    public List getTodays()
    {
	// TODO Auto-generated method stub
	List todays=new LinkedList();
	List remList=this.reminderList.observableList;
	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	Date date = new Date();
	System.out.println("current = "+dateFormat.format(date));
	for(Object r : remList)
	{
	    ReminderData rem=(ReminderData)r;
	    String tdate=dateFormat.format(rem.getRemindAt()).toString();
	    System.out.println(tdate);
	    
	    if(dateFormat.format(date).toString().equals(tdate))
	    {
		System.out.println("added!!");
		todays.add(rem);
	    }
	}
	return todays;
    }
    
    public List getCompleted()
    {
	List completed=new LinkedList();
	List remList=this.reminderList.observableList;
	//DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
	Date date = new Date();
	System.out.println("current = "+date);
	for(Object r : remList)
	{
	    ReminderData rem=(ReminderData)r;
	    Date tdate=rem.getRemindAt();
	    System.out.println(tdate);
	    
	    if(date.after(tdate))
	    {
		System.out.println("added!!");
		completed.add(rem);
	    }
	}
	return completed;
    }
    
    public List getIncomplete()
    {
	List incomplete=new LinkedList();
	List remList=this.reminderList.observableList;
	//DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
	Date date = new Date();
	System.out.println("current = "+date);
	for(Object r : remList)
	{
	    ReminderData rem=(ReminderData)r;
	    Date tdate=rem.getRemindAt();
	    System.out.println(tdate);
	    
	    if(date.before(tdate))
	    {
		System.out.println("added!!");
		incomplete.add(rem);
	    }
	}
	return incomplete;
    }
    public void addNote(String text, String sub)
    {
	// TODO Auto-generated method stub
	if((text==null || text.length()==0) && (sub==null || sub.length()==0))
	    return;
	NoteData note=new NoteData(text,sub);
	this.noteList.addNote(note);
	serializeList();
    }
    public List<NoteData> getNotes()
    {
	// TODO Auto-generated method stub
	return noteList.observableList;
    }

}
