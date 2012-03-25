package view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import model.ReminderData;

public class NewReminder extends JFrame implements ActionListener
{
    JLabel remindAbout,remindAt;
    JButton gotIt;
    ReminderData remData;
    public NewReminder(ReminderData rdata)
    {
	this.setTitle("Reminder!");
	remData=rdata;
	remindAbout=new JLabel(remData.getRemindAbout());
	remindAt=new JLabel(remData.getRemindAt().toString());
	gotIt=new JButton("Got it!");
	gotIt.addActionListener(this);
	gotIt.setActionCommand("Done");
	this.setResizable(false);
	this.setLayout(new FlowLayout());
	this.add(remindAbout);
	this.add(remindAt);
	this.add(gotIt);
	this.setVisible(true);
	this.setLocation(500, 300);
	this.setSize(180,180);
    }
    
    @Override
    public void actionPerformed(ActionEvent ae)
    {
	// TODO Auto-generated method stub
	if(ae.getActionCommand().toString().contains("Done"))
	{
	    this.dispose();
	}
    }
}
