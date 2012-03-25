package model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import view.AddReminderFrame;

public class ReminderList implements Serializable
{

    private static final long serialVersionUID = 1599056240926262504L;
    List<ReminderData> observableList;
    public ReminderList()
    {
	observableList=new LinkedList();
    }
    public void addReminder(ReminderData reminder)
    {
	observableList.add(reminder);
    }
    public void removeReminderByDate()
    {
	// TODO yet to be done
    }
    public ReminderData getReminderAtIndex(int index)
    {
	return observableList.get(index);
    }
    public void removeReminder(int index)
    {
	// TODO Auto-generated method stub
	AddReminderFrame.setIndex(AddReminderFrame.getIndex()-1);
	// because the reminder index was creating problem of arrayout of bound exception
	for(ReminderData rData : observableList)
	{
	    if(rData.getIndex()==index)
	    {
		observableList.remove(rData);
		return;
	    }
	}
    }
}
