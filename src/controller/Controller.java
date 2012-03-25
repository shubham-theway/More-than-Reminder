package controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import view.NewReminder;

import app.Application;

import model.ReminderData;
import model.ReminderList;

public class Controller implements Runnable
{
    List remList;
    ReminderData rData;
    Date date;

    public Controller()
    {
	remList=(Application.model.getReminders());
	Thread t=new Thread(this);
	t.start();
    }

    @SuppressWarnings("static-access")
    @Override
    public void run()
    {
	// TODO Auto-generated method stub
	while(true)
	{
	    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
	    date = new Date();
	    System.out.println("current = "+dateFormat.format(date));
	    for(Object rem : remList)
	    {
		rData=(ReminderData)rem;
		System.out.println(dateFormat.format((rData.getRemindAt())));
		if(dateFormat.format(date).equals(dateFormat.format(rData.getRemindAt())))
		{
		    new NewReminder(rData);
		    if(rData.getRepeat()!=0)
			rData.resetReminderRepeat(rData.getRepeat());
		    System.out.println("got it");
		}
	    }
	    try
	    {
		Thread.currentThread().sleep(1000*60);
	    } catch (InterruptedException e)
	    {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
    }

}
