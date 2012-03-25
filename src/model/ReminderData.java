package model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import app.Application;

public class ReminderData implements Serializable
{
    private int index;
    private boolean isActive;
    private String remindAbout;
    private int repeat;
    private Date remindAt;

    public ReminderData(String data,Date date,int repeat,int index)
    {
	// TODO Auto-generated constructor stub
	System.out.println("in rem data!!!!");
	setRemindAbout(data);
	setRemindAt(date);
	this.setRepeat(repeat);
	this.index=index;
	setActive(true);
	Application.model.serializeList();
    }
    public void setRemindAbout(String remindAbout)
    {
	this.remindAbout = remindAbout;
	Application.model.serializeList();
    }
    public String getRemindAbout()
    {
	return remindAbout;
    }
    public void setRemindAt(Date remindAt)
    {
	this.remindAt = remindAt;
	Application.model.serializeList();
    }
    public Date getRemindAt()
    {
	return remindAt;
    }
    private void setActive(boolean isActive)
    {
	this.isActive = isActive;
	Application.model.serializeList();
    }
    private boolean isActive()
    {
	return isActive;
    }
    public void show()
    {
	// TODO Auto-generated method stub
	System.out.println(remindAbout+" "+remindAt+" "+isActive);
    }
    public Integer getIndex()
    {
	// TODO Auto-generated method stub
	return index;
    }
    void setRepeat(int repeat)
    {
	this.repeat = repeat;
    }
    public int getRepeat()
    {
	return repeat;
    }
    public void resetReminderRepeat(int rep)
    {
	// TODO Auto-generated method stub
	Date date=this.remindAt,newDate = null;
	switch(rep)
	{
	case 1:
	    // change it back to repeatEveryDay, for time being its been changed to repeatEveryMinute
	    newDate=new Date(remindAt.getYear(), remindAt.getMonth(), remindAt.getDate(), remindAt.getHours()+1, remindAt.getMinutes());
	    break;
	case 2:
	    newDate=new Date(remindAt.getYear(), remindAt.getMonth(), remindAt.getDate()+1, remindAt.getHours(), remindAt.getMinutes());
	    break;
	case 3:
	    newDate=new Date(remindAt.getYear(), remindAt.getMonth(), remindAt.getDate()+7, remindAt.getHours(), remindAt.getMinutes());
	    break;
	case 4:
	    newDate=new Date(remindAt.getYear(), remindAt.getMonth()+1, remindAt.getDate(), remindAt.getHours(), remindAt.getMinutes());
	    break;
	case 5:
	    newDate=new Date(remindAt.getYear(), remindAt.getMonth(), remindAt.getDate(), remindAt.getHours(), remindAt.getMinutes());
	    break;
	}
	System.out.println("date "+ newDate.toString());
	this.setRemindAt(newDate);
	Application.view.updateReminder(this);
    }

}
