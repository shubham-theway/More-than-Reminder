package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.KeyboardFocusManager;
import java.awt.Label;
import java.awt.Point;
import java.awt.Scrollbar;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle;
import javax.swing.ScrollPaneLayout;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoableEdit;

import com.melloware.jintellitype.JIntellitype;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JSpinnerDateEditor;

import model.ReminderData;
import model.ReminderList;

import app.Application;

public class View implements MouseListener, ActionListener
{
    private ApplicationPanel myFrame;
    private JMenuBar menuBar;
    private JMenu appMenu,helpMenu;
    private JMenuItem createReminder,exit,searchReminder,deleteReminder,showReminder;
    private JMenuItem about,help;
    private JTextPane textPane;
    GroupLayout.ParallelGroup hzParallelGroup,pvgroup,editRemPanelHzPar;
    GroupLayout.SequentialGroup vtSequentialGroup,psgroup,editRemPanelVtSeq;
    JPanel rPanel;
    JTextField rData;
    boolean firstTime;
    GroupLayout groupLayout,editRemPanelGroupLayout ;
    JButton ok,cancel,clear,remove;
    private JSpinnerDateEditor dateField;
    private boolean mouseInside;
    private boolean alterSize;
    private Dimension lastLocation;
    private HashMap<Component,Integer> reminderIndex; // TODO utter nonsense; remove the Map as a whole
    private HashMap<Integer, Component> reverseIndex;
    Vector<Component> addedComponents;
    private Vector<Integer> componentIndex;
    private JLabel currentlySelected;
    //for search    
    private JLabel searchBy;
    private JTextField searchByText;
    private JDateChooser searchByDateFrom,searchByDateTo;
    private JButton search,searchCancel;
    private GroupLayout searchGroupLayout;
    private ParallelGroup searchRemPanelHzPar;
    private SequentialGroup searchRemPanelVtSeq;
    JPanel searchPanel;
    // for search
    

    public View()
    {
	createPane();
	createMenu();
	myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	myFrame.addMouseListener(this);
	createReminder.addActionListener(this);
	searchReminder.addActionListener(this);
	deleteReminder.addActionListener(this);
	showReminder.addActionListener(this);
	help.addActionListener(this);
	about.addActionListener(this);
	exit.addActionListener(this);
	firstTime=true;
	alterSize=true;
	myFrame.setResizable(false);
	myFrame.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE );
    }
    private void createTextPane()
    {
	textPane=new JTextPane();
	SimpleAttributeSet attributeSet=new SimpleAttributeSet();
	StyleConstants.setBackground(attributeSet, Color.green);
	StyleConstants.setForeground(attributeSet, Color.blue);
    }

    private void createMenu()
    {
	menuBar=new JMenuBar();
	appMenu=new JMenu("File");
	helpMenu=new JMenu("Help");
	menuBar.add(appMenu);
	menuBar.add(helpMenu);
	createReminder=new JMenuItem("Create Reminder");
	showReminder=new JMenuItem("Show Reminder");
	searchReminder=new JMenuItem("Search Reminder");
	deleteReminder=new JMenuItem("Delete Reminder");
	exit=new JMenuItem("Exit");
	appMenu.add(createReminder);
	appMenu.add(showReminder);
	appMenu.add(searchReminder);
	appMenu.add(deleteReminder);
	appMenu.add(exit);
	about=new JMenuItem("About");
	help=new JMenuItem("Help");
	helpMenu.add(about);
	helpMenu.add(help);
	myFrame.setJMenuBar(menuBar);
	myFrame.validate();
    }
    private void createPane()
    {
	createTextPane();
	JButton button=new JButton("button");
	JPanel myPanel = new JPanel();
	//comment below lines
	groupLayout =new GroupLayout(myPanel);
	groupLayout.setAutoCreateContainerGaps(true);
	groupLayout.setAutoCreateGaps(true);

	myPanel.setLayout(groupLayout);
	
	// scroll related  //moved it to applicationpanel class
	JScrollPane scrollPane=new JScrollPane(myPanel);
	scrollPane.setLayout(new ScrollPaneLayout());
	scrollPane.setPreferredSize(new Dimension(300,240));
	//scrollPane.setVerticalScrollBar(new JScrollBar());
	scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	//scrollPane.setHorizontalScrollBar(new JScrollBar(JScrollBar.HORIZONTAL));
	scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	
	// scroll related	
	
	hzParallelGroup=groupLayout.createParallelGroup();

	groupLayout.setHorizontalGroup(hzParallelGroup);
	//hzParallelGroup.addComponent(textPane,1,10,Integer.MAX_VALUE);
	//hzParallelGroup.addComponent(button);

	vtSequentialGroup=groupLayout.createSequentialGroup();
	
	groupLayout.setVerticalGroup(vtSequentialGroup);
	//vtParallelGroup.addComponent(textPane, 1, 30, Integer.MAX_VALUE);
	//vtParallelGroup.addComponent(button);

	JPanel jPanel=new JPanel();
	/*JButton b=new JButton("another");
	GroupLayout gLayout=new GroupLayout(jPanel);
	psgroup=gLayout.createSequentialGroup();
	gLayout.setHorizontalGroup(psgroup);
	psgroup.addComponent(b);
	pvgroup=gLayout.createParallelGroup();
	gLayout.setVerticalGroup(pvgroup);
	pvgroup.addComponent(b);*/
	// comment above lines

	ok=new JButton("Ok");
	cancel=new JButton("Cancel");
	ok.addActionListener(this);
	cancel.addActionListener(this);
	remove=new JButton("Remove");
	remove.addActionListener(this);
	dateField=new JSpinnerDateEditor();

	myFrame=new ApplicationPanel();
	
	//myFrame.addMouseListener(myFrame);
	
	// commenting this so that the scroll bar holding it could be added directly to panel
	//myFrame.add(myPanel);
	//myFrame.setContentPane(myPanel);
	myFrame.setLayout(new FlowLayout());
	//comment
	myFrame.add(scrollPane);//,BorderLayout.CENTER);
	scrollPane.addMouseListener(this);
	//myFrame.add(jPanel);
	myFrame.setSize(300, 300);
	//myFrame.setResizable(false);
	myFrame.setLocation(300, 300);
	myFrame.setVisible(true); 
	//rPanel=new JPanel(new BorderLayout());
	rPanel=new JPanel(new FlowLayout());
	rPanel.setSize(250,10);
	rPanel.setMaximumSize(new Dimension(250,50));
	rData=new JTextField();
	editRemPanelGroupLayout=new GroupLayout(rPanel);
	editRemPanelHzPar=editRemPanelGroupLayout.createParallelGroup();
	editRemPanelVtSeq=editRemPanelGroupLayout.createSequentialGroup();
	editRemPanelGroupLayout.setHorizontalGroup(editRemPanelHzPar.addComponent(rData).addComponent(dateField));
	editRemPanelGroupLayout.setVerticalGroup(editRemPanelVtSeq.addComponent(rData).addComponent(dateField));
	rPanel.setLayout(editRemPanelGroupLayout);
	//rPanel.add(rData);
	//rPanel.add(dateField);

	//search related
	searchBy=new JLabel("Search By:");
	searchByText=new JTextField(10);
	searchByDateFrom=new JDateChooser();
	searchByDateTo=new JDateChooser();
	search=new JButton("Search");
	search.setActionCommand("ExecuteSearch");
	searchCancel=new JButton("Cancel");
	searchCancel.setActionCommand("ClearSearch");
	search.addActionListener(this);
	searchCancel.addActionListener(this);
	searchBy.setVisible(false);
	searchByText.setVisible(false);
	searchByText.setToolTipText("Search by Reminder Text");
	searchByDateFrom.setVisible(false);
	searchByDateFrom.setToolTipText("Search by From Date");
	searchByDateTo.setVisible(false);
	searchByDateTo.setToolTipText("Search by To Date");
	search.setVisible(false);
	searchCancel.setVisible(false);
	//search related
	
	vtSequentialGroup.addGroup(groupLayout.createSequentialGroup().addComponent(rPanel)/*.addComponent(dateField)*/);
	hzParallelGroup.addGroup(groupLayout.createParallelGroup().addComponent(rPanel)/*.addComponent(dateField)*/);
	//vtSequentialGroup.addGroup(groupLayout.createSequentialGroup()).addComponent(b1).addComponent(b2);
	vtSequentialGroup.addGroup(groupLayout.createParallelGroup().addComponent(ok).addComponent(cancel).addComponent(remove));
	//hzParallelGroup.addGroup(groupLayout.createParallelGroup()).addComponent(b1).addComponent(b2);
	hzParallelGroup.addGroup(groupLayout.createSequentialGroup().addComponent(ok).addComponent(cancel).addComponent(remove));

	//search related
	searchPanel=new JPanel();
	searchGroupLayout=new GroupLayout(searchPanel);
	searchRemPanelHzPar=searchGroupLayout.createParallelGroup();
	searchRemPanelVtSeq=searchGroupLayout.createSequentialGroup();
	searchGroupLayout.setHorizontalGroup(searchRemPanelHzPar);
	searchGroupLayout.setVerticalGroup(searchRemPanelVtSeq);
	searchPanel.setLayout(searchGroupLayout);
	searchPanel.setMaximumSize(new Dimension(250,50));
	searchPanel.setVisible(false);
	//myFrame.add(searchPanel);
	vtSequentialGroup.addComponent(searchPanel);
	hzParallelGroup.addComponent(searchPanel);
	searchRemPanelHzPar.addComponent(searchBy).addComponent(searchByText);
	searchRemPanelHzPar.addGroup(searchGroupLayout.createSequentialGroup().addComponent(searchByDateFrom).addComponent(searchByDateTo));
	searchRemPanelVtSeq.addComponent(searchBy).addComponent(searchByText);
	searchRemPanelVtSeq.addGroup(searchGroupLayout.createParallelGroup().addComponent(searchByDateFrom).addComponent(searchByDateTo));
	/*vtSequentialGroup.addGroup(groupLayout.createParallelGroup().addComponent(searchBy).addComponent(searchByText));
	hzParallelGroup.addGroup(groupLayout.createSequentialGroup().addComponent(searchBy).addComponent(searchByText));
	vtSequentialGroup.addGroup(groupLayout.createParallelGroup().addComponent(searchByDateFrom).addComponent(searchByDateTo));
	hzParallelGroup.addGroup(groupLayout.createSequentialGroup().addComponent(searchByDateFrom).addComponent(searchByDateTo));
	*/vtSequentialGroup.addGroup(groupLayout.createParallelGroup().addComponent(search).addComponent(searchCancel));
	hzParallelGroup.addGroup(groupLayout.createSequentialGroup().addComponent(search).addComponent(searchCancel));
	
	
	rData.setVisible(false);
	rPanel.setVisible(false);
	dateField.setVisible(false);
	ok.setVisible(false);
	cancel.setVisible(false);
	remove.setVisible(false);

    }

    @SuppressWarnings("unchecked")
    @Override
    public void mouseClicked(MouseEvent e)
    {
	// TODO Auto-generated method stub
	Component comp=e.getComponent();
	if(comp.getClass().toString().equals("JSpinnerDateEditor"))
	{
	    System.out.println("mouse event in date field");
	}
	if(!comp.getClass().equals(javax.swing.JLabel.class))
	{
	    if(rPanel!=null && rPanel.isVisible()==true)
	    {
		currentlySelected=null;
		closeEditReminder();
	    }
	    return;
	}
	System.out.println(e.getSource());
	List<ReminderData> remList= (Application.model.getReminders());
	int index=reminderIndex.get(comp);
	currentlySelected=(JLabel) comp;
	System.out.println("index found in mouse click = " +index);
	ReminderData remData=Application.model.fetch(index);
	String title,src;
	src=remData.getRemindAbout();
	dateField.setDate(remData.getRemindAt());
	dateField.addMouseListener(this);
	//rPanel=new JPanel(new BorderLayout());
	rData.setText(src);
	//rPanel.add(rData);
	//rPanel.add(dateField);
	rData.setVisible(true);
	//rPanel.add(dateField);
	ok.setVisible(true);
	cancel.setVisible(true);
	remove.setVisible(true);
	rPanel.setVisible(true);
	dateField.setVisible(true);
	rPanel.revalidate();
	myFrame.validate();

	if(alterSize)
	{
	    myFrame.setSize(myFrame.getSize().width+1,myFrame.getSize().height);
	    alterSize=false;
	}
	else
	{
	    myFrame.setSize(myFrame.getSize().width-1,myFrame.getSize().height);
	    alterSize=true;
	}
	//rPanel.repaint();
	//myFrame.repaint();
    }
    /**
     * 
     */
    private void closeEditReminder()
    {
	System.out.println("rpanel was visible..but not anymore");
	rPanel.setVisible(false);
	rData.setVisible(false);
	dateField.setVisible(false);
	ok.setVisible(false);
	cancel.setVisible(false);
	remove.setVisible(false);
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
	// TODO Auto-generated method stub
	Component comp=e.getComponent();
	if(comp.getClass().equals(javax.swing.JLabel.class))
	{
	    mouseInside=true;
	    System.out.println("mouse event captured!");
	}
    }
    @Override
    public void mouseExited(MouseEvent e)
    {
	// TODO Auto-generated method stub
	if(!mouseInside)
	    return;
	mouseInside=false;
	System.out.println("mouse exitting!");
    }
    @Override
    public void mousePressed(MouseEvent e)
    {
	// TODO Auto-generated method stub
    }
    @Override
    public void mouseReleased(MouseEvent e)
    {
	// TODO Auto-generated method stub
	if(e.isPopupTrigger())
	{
	    System.out.println(e.getSource());
	    notShow();//if this is Add Reminder only
	    myFrame.showPopup(e);
	    System.out.println("in view3");
	}
    }
    @Override
    public void actionPerformed(ActionEvent ae)
    {
	// TODO Auto-generated method stub
	String src=ae.getActionCommand();
	System.out.println("-> "+src);
	if(src.contains("Create"))
	{
	    notShow();
	    clearSearch();
	    Point pt=this.myFrame.getLocationOnScreen();
	    AddReminderFrame addRem=new AddReminderFrame(pt.x+20,pt.y+70);
	    // addRem=new AddReminderFrame(300,300);
	    addRem.setVisible(true);
	}
	else if(src.contains("Show"))
	{
	    System.out.println("in show");
	    List<ReminderData> remList= (Application.model.getReminders());
	    int i=0;
	    if(addedComponents==null)
		addedComponents=new Vector<Component>();
	    notShow();
	    clearSearch();
	    if(reminderIndex==null)
		reminderIndex=new HashMap<Component,Integer>();
	    if(reverseIndex==null)
		reverseIndex=new HashMap<Integer, Component>();
	    if(firstTime)
	    {
		lastLocation=new Dimension();
		i=0;
		componentIndex=new Vector<Integer>();
		for(ReminderData remData : remList)//when u have addedComponents then why u iterate thru remList again and again
		{
		    i=showReminder(i, remData);
		    //remFrame.setResizable(false);
		    //myFrame.add(remFrame);
		    myFrame.repaint();
		    firstTime=false;
		    componentIndex.add(remData.getIndex());
		}
	    }
	    else
	    {
		for(Component com : addedComponents)
		{
		    com.setVisible(true);
		}
		if(addedComponents.size()!=remList.size())
		{
		    int size=remList.size();
		    i=20;
		    for(int j=addedComponents.size();j<size;j++)
		    {
			ReminderData remData=remList.get(j);
			/*String remString = remData.getRemindAbout() + " \n" + remData.getRemindAt();
			JLabel remLabel=new JLabel(remString);
			remLabel.addMouseListener(this);
			myFrame.add(remLabel);
			remLabel.setLocation(lastLocation.height, 500+i);
			i+=20;
			lastLocation.height=500;lastLocation.width=500+i;
			vtSequentialGroup.addComponent(remLabel);
			hzParallelGroup.addComponent(remLabel);
			remLabel.setVisible(true);
			addedComponents.add(remLabel);
			reminderIndex.put(remLabel,remData.getIndex());*/
			showReminder(i, remData);
			componentIndex.add(remData.getIndex());
			//remFrame.setResizable(false);
			//myFrame.add(remFrame);
			myFrame.repaint();
		    }
		}
	    }
	    clear=null;
	    clear=new JButton("Clear");
	    clear.addActionListener(this);
	    vtSequentialGroup.addComponent(clear);
		hzParallelGroup.addComponent(clear);
	    if(alterSize)
	    {
		myFrame.setSize(myFrame.getSize().width+1,myFrame.getSize().height);
		alterSize=false;
	    }
	    else
	    {
		myFrame.setSize(myFrame.getSize().width-1,myFrame.getSize().height);
		alterSize=true;
	    }
	}
	else if(src.equals("Search Reminder"))
	{
	    System.out.println("in search");
	    searchPanel.setVisible(true);
	    searchBy.setVisible(true);
	    searchByText.setVisible(true);
	    searchByDateFrom.setVisible(true);
	    searchByDateTo.setVisible(true);
	    search.setVisible(true);
	    searchCancel.setVisible(true);
	    notShow();
	}
	else if(src.equals("ExecuteSearch"))
	{
	    System.out.println("search it");
	    List remList=(Application.model.getReminders());
	    notShow();
	    int i=20;
	    for(Object rem : remList)
	    {
		ReminderData remData=(ReminderData)rem;
		//System.out.println("date-> "+searchByDateFrom.getDate().toString());
		if(remData.getRemindAbout().equals(searchByText.getText()))
		{
		    if(componentIndex.contains(remData.getIndex()))
		    {
			reverseIndex.get((Integer)remData.getIndex()).setVisible(true);
			continue;
		    }
		    else
			i=showReminder(i,remData);
		    System.out.println("found!");
		    continue;
		}
		/*else if(!"".equals(searchByDateFrom.getDate().toString()) && searchByDateFrom.getDate() !=null && remData.getRemindAt().after(searchByDateFrom.getDate()) && 
			!"".equals(searchByDateTo.getDate().toString()) && searchByDateTo.getDate()!=null && remData.getRemindAt().before(searchByDateTo.getDate()));
		{
		    if(componentIndex.contains(remData.getIndex()))
		    {
			reverseIndex.get((Integer)remData.getIndex()).setVisible(true);
			continue;
		    }
		    i=showReminder(i,remData);
		    System.out.println("found!");
		    continue;
		}*/
	    }
	}
	else if(src.equals("ClearSearch"))
	{
	    //clear the search stuff
	    System.out.println("clear search");
	    searchPanel.setVisible(false);
	    clearSearch();
	}
	else if(src.contains("Remove"))
	{
	    int index=reminderIndex.get(currentlySelected);
	    closeEditReminder();
	    Application.model.removeReminder(index);
	    removeUIComp(index);
	}
	else if(src.contains("Exit"))
	{
	    myFrame.setVisible(false);
	    Application.model.serializeList();
	    System.exit(0);
	}
	else if(src.contains("About"))
	{
	    JOptionPane.showMessageDialog(myFrame,
		    "Remind Me!\nVersion 0.1\n\njust drop a mail with +/-/neutral feedback.\namysteriousindian@gmail.com",
		    "About", JOptionPane.INFORMATION_MESSAGE);
	}
	else if(src.contains("Help"))
	{
	    JOptionPane.showMessageDialog(myFrame,
		    "I'm helpless! :P\nI mean you really don't need a 'help' to use me\nJust remember Alt+Shift+S :)",
		    "About", JOptionPane.PLAIN_MESSAGE);
	}
	else if(src.equals("Clear"))
	{
	    notShow();
	    clear.setVisible(false);
	}
	else
	    handleOkCancel(ae);
	myFrame.repaint();
    }
    /**
     * 
     */
    private void clearSearch()
    {
	searchPanel.setVisible(false);
	searchBy.setVisible(false);
	searchByText.setVisible(false);
	searchByDateFrom.setVisible(false);
	searchByDateTo.setVisible(false);
	search.setVisible(false);
	searchCancel.setVisible(false);
	notShow();
    }


    private void removeUIComp(int index)
    {
	// TODO Auto-generated method stub
	Component removedComp=reverseIndex.get(index);
	reverseIndex.remove(index);
	reminderIndex.remove(removedComp);
	addedComponents.remove(removedComp);
	removedComp.setVisible(false);
	removedComp=null;
    }
    /**
     * @param i
     * @param remData
     * @return
     */
    private int showReminder(int i, ReminderData remData)
    {
	String remString = remData.getRemindAbout() + " \n" + remData.getRemindAt();
	JLabel remLabel=new JLabel(remString);
	remLabel.addMouseListener(this);
	myFrame.add(remLabel);
	//JLabel dTime=new JLabel(remData.getRemindAt().toString());
	//remFrame.add(dTime);
	//remFrame.setSize(120, 80);
	remLabel.setLocation(500, 500+i);
	i+=20;
	lastLocation.height=500;lastLocation.width=500+i;
	vtSequentialGroup.addComponent(remLabel);
	hzParallelGroup.addComponent(remLabel);
	remLabel.setVisible(true);
	addedComponents.add(remLabel);
	reminderIndex.put(remLabel,remData.getIndex());
	reverseIndex.put(remData.getIndex(), remLabel);
	System.out.println("index="+remData.getIndex());
	return i;
    }
    private void print(ReminderData remData)
    {
	// TODO Auto-generated method stub

    }
    /**
     * 
     */
    private void notShow()
    {
	if(addedComponents==null)
	    return;
	for(Component com : addedComponents)
	{
	    com.setVisible(false);
	    //com=null;
	}
	closeEditReminder();
	if(clear!=null)
	    clear.setVisible(false);
	else
	    clear=null;
    }
    private void handleOkCancel(ActionEvent ae)
    {
	// TODO Auto-generated method stub
	String src=ae.getActionCommand();
	if(src.equals("Ok"))
	{
	    // save and close
	    JLabel comp=(JLabel)currentlySelected;
	    List<ReminderData> remList= (Application.model.getReminders());
	    int index=reminderIndex.get(comp);
	    System.out.println("index found in mouse click = " +index);
	    ReminderData remData=remList.get(index);
	    remData.setRemindAbout(this.rData.getText());
	    if(this.dateField.getDate()!=null)
		remData.setRemindAt(this.dateField.getDate());
	    updateLabel(comp,remData);
	}
	else if(src.equals("Cancel"))
	{
	    // just close
	}
	closeEditReminder();
    }
    private void updateLabel(JLabel comp,ReminderData rData)
    {
	// TODO Auto-generated method stub
	comp.setText(rData.getRemindAbout()+" "+rData.getRemindAt());
    }
    public void updateReminder(ReminderData rData)
    {
	// TODO Auto-generated method stub
	JLabel comp=(JLabel)reverseIndex.get(rData.getIndex());
	comp.setText(rData.getRemindAbout() + " " + rData.getRemindAt());
    }
}
