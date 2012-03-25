package view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;
import javax.swing.text.JTextComponent;

import model.ReminderData;
import model.Model;

import app.Application;
import com.toedter.calendar.*;



public class AddReminderFrame extends JFrame implements ActionListener,FocusListener, MouseListener, KeyListener 
{
    private JButton add,cancel;
    private int count;	// try to eliminate this... needed only to avoid wipe out the Remind About for the very first time.. and because when the app starts, text field automatically gets the focus.
    private JTextField remindAbout;
    private JTextField remindAt;
    private final Highlighter hili;
    private final HighlightPainter hipaint;
    private JLabel fakeLabel,repeat,repeatValue;
    private JRadioButton hourly,daily,weekly,monthly,yearly,noRepeat,custom;
    JSpinnerDateEditor jcal;
    ButtonGroup bGroup;
    private JPanel panel;
    static private int index=0;
    // must not be called from anywhere else except model and at the time of deserialization
    public static void setIndex(int ind)
    {
	index=ind;
    }
    public static int getIndex()
    {
	return index;
    }
    public AddReminderFrame(int x,int y) //throws BadLocationException
    {
	this.setTitle("Create Reminder");
	hili=new DefaultHighlighter();
	add=new JButton("Add");
	cancel=new JButton("Cancel");
	remindAbout=new JTextField("Text",20);
	remindAbout.setName("Text");
	remindAt=new JTextField("Time",20);
	remindAt.setName("Time");
	remindAbout.setHighlighter(hili);
	fakeLabel=new JLabel("             ");
	this.add(fakeLabel);
	repeat=new JLabel("Repeat");
	repeatValue=new JLabel("None");
	repeatValue.setVisible(true);
	hourly=new JRadioButton();
	hourly.addActionListener(this);
	daily=new JRadioButton();
	daily.addActionListener(this);
	weekly=new JRadioButton();
	weekly.addActionListener(this);
	monthly=new JRadioButton();
	monthly.addActionListener(this);
	yearly=new JRadioButton();
	yearly.addActionListener(this);
	noRepeat=new JRadioButton();
	noRepeat.addActionListener(this);
	noRepeat.setSelected(true);
	custom=new JRadioButton();
	custom.addActionListener(this);
	bGroup = new ButtonGroup();
	bGroup.add(noRepeat);
	bGroup.add(hourly);
	bGroup.add(daily);
	bGroup.add(weekly);
	bGroup.add(monthly);
	bGroup.add(custom);
	//fakeLabel.setVisible(false);
	hipaint=new DefaultHighlighter.DefaultHighlightPainter(Color.GRAY);
	panel=new JPanel();
	jcal=new JSpinnerDateEditor();//JDateChooser("MM/dd/yyyy","##/##/##",'_');
	this.getContentPane().setLayout(new FlowLayout());
	//jcal.
	GroupLayout groupLayout=new GroupLayout(this.panel);
	panel.setLayout(groupLayout);
	panel.setSize(10,40);
	GroupLayout.ParallelGroup hzGroup=groupLayout.createParallelGroup();
	GroupLayout.SequentialGroup vtGroup=groupLayout.createSequentialGroup();
	groupLayout.setHorizontalGroup(hzGroup);
	groupLayout.setVerticalGroup(vtGroup);
	hzGroup.addComponent(remindAbout).addComponent(jcal);
	hzGroup.addGroup(groupLayout.createSequentialGroup().addComponent(repeat).addComponent(noRepeat).addComponent(hourly).addComponent(daily)
		.addComponent(weekly).addComponent(monthly).addComponent(custom).addComponent(repeatValue));
	hzGroup.addGroup(groupLayout.createSequentialGroup().addComponent(fakeLabel).addComponent(add).addComponent(cancel));
	vtGroup.addComponent(remindAbout).addComponent(jcal);
	vtGroup.addGroup(groupLayout.createParallelGroup().addComponent(repeat).addComponent(noRepeat).addComponent(hourly).addComponent(daily)
		.addComponent(weekly).addComponent(monthly).addComponent(custom).addComponent(repeatValue));
	vtGroup.addGroup(groupLayout.createParallelGroup().addComponent(fakeLabel).addComponent(add).addComponent(cancel));
	this.add(panel);
	//this.add(remindAbout);
	//this.add(remindAt);
	//this.add(jcal);

	//remindAt.add(jcal);
	//this.add(add);
	//this.add(cancel);
	remindAbout.setText("remind about");
	try
	{
	    hili.addHighlight(0, remindAbout.getText().length(), hipaint);
	    /*{

	        public void paint(Graphics arg0, int arg1, int arg2, Shape arg3,
	    	    JTextComponent arg4)
	        {
	    	// TODO Auto-generated method stub

	        }
	    });*/
	} catch (BadLocationException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	//remindAbout.
	remindAt.setText("MM:HH@DD/MM/YY");
	remindAbout.addFocusListener(this);
	remindAbout.addKeyListener(this);
	remindAbout.addActionListener(this);
	//remindAt.addFocusListener(this);
	//remindAt.addActionListener(this);
	this.addMouseListener(this);
	count=0;
	add.addActionListener(this);
	cancel.addActionListener(this);
	this.setSize(260,160);
	this.setResizable(false);
	System.out.println("x = " + x + " y = " +y);
	this.setLocation(x, y);
	jcal.requestFocus();
	jcal.requestFocusInWindow();
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
	// TODO Auto-generated method stub
	String src=(ae.getSource()).getClass().toString();
	System.out.println(src);
	if(src.contains("JButton"))
	{
	    src=((JButton)ae.getSource()).toString();
	}
	/*else if(src.contains("Radio"))
	    src="Radio";*/
	System.out.println(src);
	if(src.contains("Add"))
	{
	    if(verifyTime())
	    {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date));
		if(jcal.getDate()==null)
		    jcal.setDate(date);
		int rep=0;
		if(hourly.isSelected())
		    rep=1;
		if(daily.isSelected())
		{
		    rep=2;
		    System.out.println("rep is daily");
		}
		else if(weekly.isSelected())
		{
		    rep=3;
		    System.out.println("rep is weekly");
		}
		else if(monthly.isSelected())
		    rep=3;
		else if(custom.isSelected())
		    rep=5;
		ReminderData reminder=new ReminderData(remindAbout.getText(),jcal.getDate(),rep,index++);
		((Model)getModel()).addReminder(reminder);
	    }
	    this.setVisible(false);
	    System.out.println("adding!");
	}
	else if(src.contains("Cancel"))
	{
	    System.out.println("canceling");
	    this.setVisible(false);
	}
	else if(src.contains("Text"))
	{
	    remindAbout.setText("");
	    System.out.println("text modified");
	}
	else if(src.contains("Time"))
	{
	    remindAt.setText("");
	    System.out.println("time modified");
	}
	else if(src.contains("Radio"))
	{
	    src=((JRadioButton)ae.getSource()).toString();
	    System.out.println("name = "+src);
	    //((JRadioButton)ae).get
	    if(src.contains(",40,40"))
	    {
		repeatValue.setText("None");
		System.out.println("no rep");
	    }
	    else if(src.contains(",61,"))
	    {
		repeatValue.setText("Hourly");
		System.out.println("hourly " + repeatValue.isVisible());
	    }
	    else if(src.contains(",82,"))
		repeatValue.setText("Daily");
	    else if(src.contains(",103,"))
		repeatValue.setText("Weekly");
	    else if(src.contains(",124,"))
		repeatValue.setText("Monthly");
	    else
		repeatValue.setText("Custom");
	    repeatValue.setVisible(true);
	}
    }
    private Date getDate(String text)
    {
	// TODO Auto-generated method stub

	return new Date(1232312313213L);
    }
    private Object getModel()
    {
	// TODO Auto-generated method stub
	return Application.model;
    }
    private boolean verifyTime()
    {
	// TODO Auto-generated method stub
	String time=remindAt.getText();
	boolean returnVal=true;
	int len=time.length();
	/*for(int i=0;i<len;i++)
	{
	    if(time[i]>5)
	}*/
	return returnVal;
    }
    @Override
    public void focusGained(FocusEvent fe)
    {
	// TODO Auto-generated method stub
	String src=((JTextField)fe.getSource()).getText();
	System.out.println(src);
	if(src.contains("about"))
	{
	    if(count++>0)
		remindAbout.setText("");
	    System.out.println("text modified");
	}
	else if(src.contains("HH"))
	{
	    remindAt.setText("");
	    System.out.println("time modified");
	}
    }
    @Override
    public void focusLost(FocusEvent fe)
    {
	// TODO Auto-generated method stub
	String src=((JTextField)fe.getSource()).getName();
	System.out.println("in lost focus " +src);
	if(src.contains("Text"))
	{
	    if(remindAbout.getText().equals(""))
		remindAbout.setText("remind about");
	}
	else if(src.contains("Time"))
	{
	    if(remindAt.getText().equals(""))
		remindAt.setText("MM:HH@DD/MM/YY");
	}
    }
    @Override
    public void mouseClicked(MouseEvent arg0)
    {
	// TODO Auto-generated method stub
	this.requestFocus();
	if(remindAbout.getText().equals(""))
	    remindAbout.setText("remind about");
	if(remindAt.getText().equals(""))
	    remindAt.setText("MM:HH@DD/MM/YY");
    }
    @Override
    public void mouseEntered(MouseEvent arg0)
    {
	// TODO Auto-generated method stub

    }
    @Override
    public void mouseExited(MouseEvent arg0)
    {
	// TODO Auto-generated method stub

    }
    @Override
    public void mousePressed(MouseEvent arg0)
    {
	// TODO Auto-generated method stub

    }
    @Override
    public void mouseReleased(MouseEvent arg0)
    {
	// TODO Auto-generated method stub

    }
    @Override
    public void keyPressed(KeyEvent arg0)
    {
	// TODO Auto-generated method stub
	if(remindAbout.getText().equals("remind about"))
	    remindAbout.setText("");
    }
    @Override
    public void keyReleased(KeyEvent arg0)
    {
	// TODO Auto-generated method stub

    }
    @Override
    public void keyTyped(KeyEvent arg0)
    {
	// TODO Auto-generated method stub
    }
}
