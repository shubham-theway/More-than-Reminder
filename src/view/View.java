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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
import java.util.Date;
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

import model.NoteData;
import model.ReminderData;
import model.ReminderList;

import app.Application;

public class View implements MouseListener, ActionListener
{
    ApplicationPanel myFrame;
    private JMenuBar menuBar;
    private JMenu appMenu,helpMenu;
    private JMenuItem createReminder,exit,searchReminder,deleteReminder,showReminder;
    private JMenuItem addNote,showNotes;
    private JMenuItem about,help;
    private JMenu showBy;
    private JMenuItem showTodays,showCompleted,showIncomplete,showCustomRange;
    private JTextPane textPane;
    GroupLayout.ParallelGroup hzParallelGroup,pvgroup,editRemPanelHzPar;
    GroupLayout.SequentialGroup vtSequentialGroup,psgroup,editRemPanelVtSeq;
    JPanel rPanel,nPanel;
    JTextField rData,nDataTF,nSubTF;
    boolean firstTime;
    GroupLayout groupLayout,editRemPanelGroupLayout;
    JButton ok,cancel,clear,remove;
    private JSpinnerDateEditor dateField;
    private boolean mouseInside;
    private boolean alterSize;
    private Dimension lastLocation;
    private HashMap<Component,Integer> reminderIndex; // TODO utter nonsense; remove the Map as a whole
    private HashMap<Integer, Component> rReverseIndex;
    Vector<Component> addedReminders;
    private Vector<Integer> rComponentIndex;
    private JLabel rCurrentlySelected;

    private HashMap<Component,Integer> noteIndex; // TODO utter nonsense; remove the Map as a whole
    private HashMap<Integer, Component> nReverseIndex;
    Vector<Component> addedNotes;
    private Vector<Integer> nComponentIndex;
    private JLabel nCurrentlySelected;

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
	addNote.addActionListener(this);
	createReminder.addActionListener(this);
	searchReminder.addActionListener(this);
	deleteReminder.addActionListener(this);
	showReminder.addActionListener(this);
	showCompleted.addActionListener(this);
	showCustomRange.addActionListener(this);
	showIncomplete.addActionListener(this);
	showTodays.addActionListener(this);
	help.addActionListener(this);
	about.addActionListener(this);
	exit.addActionListener(this);
	firstTime=true;
	alterSize=true;
	myFrame.setResizable(false);
	myFrame.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE );
	lastLocation=new Dimension();
	rComponentIndex=new Vector<Integer>();
	addedReminders=new Vector<Component>();
	reminderIndex=new HashMap<Component,Integer>();
	rReverseIndex=new HashMap<Integer, Component>();

	nComponentIndex=new Vector<Integer>();
	addedNotes=new Vector<Component>();
	noteIndex=new HashMap<Component,Integer>();
	nReverseIndex=new HashMap<Integer, Component>();
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
	appMenu=new JMenu("Utility");
	helpMenu=new JMenu("Help");
	menuBar.add(appMenu);
	menuBar.add(helpMenu);
	addNote=new JMenuItem("Add Note");
	addNote.setAccelerator(KeyStroke.getKeyStroke('N', KeyEvent.CTRL_DOWN_MASK));
	showNotes=new JMenuItem("Show Notes");
	showNotes.addActionListener(this);
	showNotes.setAccelerator(KeyStroke.getKeyStroke('W', KeyEvent.CTRL_DOWN_MASK));

	createReminder=new JMenuItem("Create Reminder");
	createReminder.setAccelerator(KeyStroke.getKeyStroke('R', KeyEvent.CTRL_DOWN_MASK));
	showReminder=new JMenuItem("Show Reminders");
	showReminder.setAccelerator(KeyStroke.getKeyStroke('S', KeyEvent.CTRL_DOWN_MASK));
	searchReminder=new JMenuItem("Search Reminders");
	searchReminder.setAccelerator(KeyStroke.getKeyStroke('F', KeyEvent.CTRL_DOWN_MASK));
	deleteReminder=new JMenuItem("Delete Reminder");
	exit=new JMenuItem("Exit");
	appMenu.add(addNote);
	appMenu.add(createReminder);
	appMenu.add(showNotes);
	appMenu.add(showReminder);
	appMenu.add(searchReminder);

	showBy=new JMenu("Show By");
	showTodays=new JMenuItem("Todays Reminders");
	showCompleted=new JMenuItem("All Completed");
	showIncomplete=new JMenuItem("All Incomplete");
	showCustomRange=new JMenuItem("Custom Range");
	appMenu.add(showBy);
	showBy.add(showTodays);
	showBy.add(showCompleted);
	showBy.add(showIncomplete);
	showBy.add(showCustomRange);
	//appMenu.add(deleteReminder); commenting for time being as delete feature is available elsewhere
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
	nPanel=new JPanel(new FlowLayout());
	nPanel.setSize(250,10);
	nPanel.setMaximumSize(new Dimension(250,50));

	rData=new JTextField();
	nDataTF=new JTextField();
	nSubTF=new JTextField();

	editRemPanelGroupLayout=new GroupLayout(rPanel);
	editRemPanelHzPar=editRemPanelGroupLayout.createParallelGroup();
	editRemPanelVtSeq=editRemPanelGroupLayout.createSequentialGroup();
	editRemPanelGroupLayout.setHorizontalGroup(editRemPanelHzPar.addComponent(rData).addComponent(dateField).addComponent(nSubTF).addComponent(nDataTF));
	editRemPanelGroupLayout.setVerticalGroup(editRemPanelVtSeq.addComponent(rData).addComponent(dateField).addComponent(nSubTF).addComponent(nDataTF));
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

	 nDataTF.setVisible(false);
	 nSubTF.setVisible(false);

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
	System.out.println("obj name = "+comp.getName());
	if(comp.getClass().toString().equals("JSpinnerDateEditor"))
	{
	    System.out.println("mouse event in date field");
	}
	if(!comp.getClass().equals(javax.swing.JLabel.class))
	{
	    if(rPanel!=null && rPanel.isVisible()==true)
	    {
		rCurrentlySelected=null;
		closeEditReminder();
	    }
	    return;
	}
	//System.out.println(e.getSource());
	if(comp.getName().equals("Reminder"))
	    showEditReminder(comp);
	else if(comp.getName().equals("Note"))
	    showEditNote(comp);
	//rPanel.add(rData);
	//rPanel.add(dateField);
	//rData.setVisible(true);
	//rPanel.add(dateField);
	ok.setVisible(true);
	cancel.setVisible(true);

	myFrame.validate();

	changeSizeToRepaint();
    }
    /**
     * @param comp
     */
    private void showEditReminder(Component comp)
    {
	closeEditNote();
	List<ReminderData> remList= (Application.model.getReminders());
	int index=reminderIndex.get(comp);
	rCurrentlySelected=(JLabel) comp;
	System.out.println("index found in mouse click = " +index);
	ReminderData remData=Application.model.fetchReminder(index);
	String title,src;
	src=remData.getRemindAbout();
	dateField.setDate(remData.getRemindAt());
	dateField.addMouseListener(this);
	//rPanel=new JPanel(new BorderLayout());
	rData.setText(src);
	remove.setVisible(true);
	rPanel.setVisible(true);
	rData.setVisible(true);
	dateField.setVisible(true);
	rPanel.revalidate();
    }
    /**
     * @param comp
     */
    private void showEditNote(Component comp)
    {
	closeEditReminder();
	List<NoteData> noteList= (Application.model.getNotes());
	int index=noteIndex.get(comp);
	nCurrentlySelected=(JLabel) comp;
	System.out.println("n index found in mouse click = " +index);
	NoteData nData=Application.model.fetchNote(index);
	String sub,src;
	src=nData.getData();
	sub=nData.getSubject();
	//rPanel=new JPanel(new BorderLayout());
	nSubTF.setText(sub);
	nDataTF.setText(src);
	remove.setVisible(true);
	rPanel.setVisible(true);
	nDataTF.setVisible(true);
	nSubTF.setVisible(true);
	nPanel.revalidate();
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
    
    private void closeEditNote()
    {
	System.out.println("rpanel was visible..but not anymore");
	nPanel.setVisible(false);
	nDataTF.setVisible(false);
	nSubTF.setVisible(false);
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
	    myFrame.showPopup(e);
	    notShow();//if this is Add Reminder only	    
	    System.out.println("in view3");
	}
    }
    @Override
    public void actionPerformed(ActionEvent ae)
    {
	// TODO Auto-generated method stub
	String src=ae.getActionCommand();
	System.out.println("-> "+src);
	if(src.equals("Add Note"))
	{
	    System.out.println("add note");
	    Point pt=this.myFrame.getLocationOnScreen();
	    AddNoteFrame addNote=new AddNoteFrame(pt.x+20,pt.y+70);
	    addNote.setVisible(true);
	}
	else if(src.contains("Create"))
	{
	    notShow();
	    clearSearch();
	    Point pt=this.myFrame.getLocationOnScreen();
	    AddReminderFrame addRem=new AddReminderFrame(pt.x+20,pt.y+70);
	    // addRem=new AddReminderFrame(300,300);
	    addRem.setVisible(true);
	}
	else if (src.equals("Show Notes"))
	{

	    System.out.println("in show notes");
	    List<NoteData> nList= Application.model.getNotes();
	    int i=0;
	    notShow();
	    clearSearch();
	    if(firstTime)
	    {
		i=0;
		for(NoteData n : nList)//when u have addedComponents then why u iterate thru remList again and again
		{
		    i=showNote(n,i);
		    //remFrame.setResizable(false);
		    //myFrame.add(remFrame);
		    myFrame.repaint();
		    firstTime=false;
		    nComponentIndex.add(n.getIndex());
		}
	    }
	    else
	    {
		for(Component com : addedNotes)
		{
		    com.setVisible(true);
		}
		if(addedNotes.size()!=nList.size())
		{
		    int size=nList.size();
		    i=20;
		    for(int j=addedNotes.size();j<size;j++)
		    {
			NoteData nData=nList.get(j);
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
			i=showNote(nData,i);
			nComponentIndex.add(nData.getIndex());
			//remFrame.setResizable(false);
			//myFrame.add(remFrame);
			myFrame.repaint();
		    }
		}
	    }
	    //showNotes();
	    addClearMethod();
	    changeSizeToRepaint();

	}
	else if(src.contains("Show Reminders"))
	{
	    System.out.println("in show");
	    List<ReminderData> remList= (Application.model.getReminders());
	    int i=0;
	    notShow();
	    clearSearch();
	    if(firstTime)
	    {
		i=0;
		for(ReminderData remData : remList)//when u have addedComponents then why u iterate thru remList again and again
		{
		    i=showReminder(i, remData);
		    //remFrame.setResizable(false);
		    //myFrame.add(remFrame);
		    myFrame.repaint();
		    firstTime=false;
		    rComponentIndex.add(remData.getIndex());
		}
	    }
	    else
	    {
		for(Component com : addedReminders)
		{
		    com.setVisible(true);
		}
		if(addedReminders.size()!=remList.size())
		{
		    int size=remList.size();
		    i=20;
		    for(int j=addedReminders.size();j<size;j++)
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
			i=showReminder(i, remData);
			rComponentIndex.add(remData.getIndex());
			//remFrame.setResizable(false);
			//myFrame.add(remFrame);
			myFrame.repaint();
		    }
		}
	    }
	    //showNotes();
	    addClearMethod();
	    changeSizeToRepaint();
	}
	else if(src.equals("Search Reminders"))
	{
	    System.out.println("in search");
	    searchPanel.setVisible(true);
	    searchBy.setVisible(true);
	    searchByText.setVisible(true);
	    searchByDateFrom.setDate(new Date(1200000000000L));
	    searchByDateFrom.setVisible(true);
	    searchByDateTo.setDate(new Date(1500000000000L));
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
		Object nul=null;
		System.out.println("date-> "+"".equals(searchByDateFrom.getDate()));
		if(remData.getRemindAbout().contains(searchByText.getText())
			&& (!"".equals(searchByDateFrom.getDate().toString()) && searchByDateFrom.getDate() !=null &&
				remData.getRemindAt().after(searchByDateFrom.getDate()) && 
				!"".equals(searchByDateTo.getDate().toString()) && searchByDateTo.getDate()!=null && 
				remData.getRemindAt().before(searchByDateTo.getDate())))
		{
		    i = addReminderIfNotAlreadyAdded(i, remData);
		    System.out.println("found!");
		    continue;
		}
		/*else if(!"".equals(searchByDateFrom.getDate().toString()) && searchByDateFrom.getDate() !=null &&
			remData.getRemindAt().after(searchByDateFrom.getDate()) && 
			!"".equals(searchByDateTo.getDate().toString()) && searchByDateTo.getDate()!=null && 
			remData.getRemindAt().before(searchByDateTo.getDate()));
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
	    addClearMethod();
	    changeSizeToRepaint();
	}
	else if(src.equals("ClearSearch"))
	{
	    //clear the search stuff
	    System.out.println("clear search");
	    searchPanel.setVisible(false);
	    clearSearch();
	}
	else if(src.equals("Todays Reminders"))
	{
	    clearSearch();
	    notShow();
	    List todaysList=Application.model.getTodays();
	    System.out.println("todays rem = "+todaysList.size());
	    int i=20;
	    for(Object r : todaysList)
	    {
		ReminderData rem = (ReminderData) r;
		i = addReminderIfNotAlreadyAdded(i, rem);
	    }
	    addClearMethod();
	    changeSizeToRepaint();
	}
	else if(src.equals("All Completed"))
	{
	    clearSearch();
	    notShow();
	    List completedList=Application.model.getCompleted();
	    System.out.println("todays rem = "+completedList.size());
	    int i=20;
	    for(Object r : completedList)
	    {
		ReminderData rem = (ReminderData) r;
		i = addReminderIfNotAlreadyAdded(i, rem);
	    }
	    addClearMethod();
	    changeSizeToRepaint();
	}
	else if(src.equals("All Incomplete"))
	{
	    clearSearch();
	    notShow();
	    List completedList=Application.model.getIncomplete();
	    System.out.println("todays rem = "+completedList.size());
	    int i=20;
	    for(Object r : completedList)
	    {
		ReminderData rem = (ReminderData) r;
		i = addReminderIfNotAlreadyAdded(i, rem);
	    }
	    addClearMethod();
	    changeSizeToRepaint();
	}
	else if(src.contains("Remove"))
	{
	    int index=reminderIndex.get(rCurrentlySelected);
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
	    dontShowNotes();
	    if(clear!=null)
		clear.setVisible(false);
	    clear=null;
	}
	else
	    handleOkCancel(ae);
	myFrame.repaint();
    }
    private int showNote(NoteData n,int i)
    {
	// TODO Auto-generated method stub
	String data = n.getData();
	String sub=n.getSubject();
	JLabel remLabel=new JLabel(sub+" : "+data);
	remLabel.setName("Note");
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
	addedNotes.add(remLabel);
	noteIndex.put(remLabel,n.getIndex());
	nReverseIndex.put(n.getIndex(), remLabel);
	System.out.println("index="+n.getIndex());
	return i;
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
	remLabel.setName("Reminder");
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
	addedReminders.add(remLabel);
	reminderIndex.put(remLabel,remData.getIndex());
	rReverseIndex.put(remData.getIndex(), remLabel);
	System.out.println("index="+remData.getIndex());
	return i;
    }

    /**
     * 
     */
    private void addClearMethod()
    {
	clear=null;
	clear=new JButton("Clear");
	clear.addActionListener(this);
	vtSequentialGroup.addComponent(clear);
	hzParallelGroup.addComponent(clear);
    }

    private void dontShowNotes()
    {
	for(Component com : addedNotes)
	{
	    com.setVisible(false);
	}
    }

    /**
     * 
     */
    private void changeSizeToRepaint()
    {
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
    /**
     * pass this method a Reminder and it will publish it to the UI
     * if the Reminder is already added then it will just call the setVisible method
     * otherwise it'll add it to the added component list
     * @param i
     * @param remData
     * @return
     */
    private int addReminderIfNotAlreadyAdded(int i, ReminderData remData)
    {
	if(rComponentIndex!=null && rComponentIndex.contains(remData.getIndex()))
	{
	    rReverseIndex.get((Integer)remData.getIndex()).setVisible(true);
	}
	else
	{
	    i=showReminder(i,remData);
	    rComponentIndex.add(remData.getIndex());
	}
	return i;
    }
    /**


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
	Component removedComp=rReverseIndex.get(index);
	rReverseIndex.remove(index);
	reminderIndex.remove(removedComp);
	addedReminders.remove(removedComp);
	removedComp.setVisible(false);
	removedComp=null;
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
	if(addedReminders==null)
	    return;
	for(Component com : addedReminders)
	{
	    com.setVisible(false);
	    //com=null;
	}
	for(Component com : addedNotes)
	{
	    com.setVisible(false);
	}
	closeEditReminder();
	closeEditNote();
	if(clear!=null)
	    clear.setVisible(false);
	clear=null;
    }
    @SuppressWarnings("unchecked")
    private void handleOkCancel(ActionEvent ae)
    {
	// TODO Auto-generated method stub
	String src=ae.getActionCommand();
	if(src.equals("Ok"))
	{
	    // save and close
	    JLabel comp;
	    if(rCurrentlySelected!=null)
	    {
		comp=(JLabel)rCurrentlySelected;
		List<ReminderData> remList= (Application.model.getReminders());
		int index=0;
		if(comp.getName().equals("Reminder"))
		    index=reminderIndex.get(comp);
		ReminderData remData=Application.model.fetchReminder(index);
		remData.setRemindAbout(this.rData.getText());
		if(this.dateField.getDate()!=null)
		    remData.setRemindAt(this.dateField.getDate());
		updateLabel(comp,remData);
	    }
	    else
	    {
		comp=(JLabel)nCurrentlySelected;
		List<NoteData> nList=(Application.model.getNotes());
		int index;
		index=noteIndex.get(comp);
		NoteData nData=Application.model.fetchNote(index);
		nData.setSubject(this.nSubTF.getText());
		nData.setData(this.nDataTF.getText());
		System.out.println("index found in mouse click = " +index);
		updateNoteLabel(comp, nData);
	    }
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
    private void updateNoteLabel(JLabel comp,NoteData nData)
    {
	// TODO Auto-generated method stub
	comp.setText(nData.getSubject()+" : "+nData.getData());
    }
    public void updateReminder(ReminderData rData)
    {
	// TODO Auto-generated method stub
	JLabel comp=(JLabel)rReverseIndex.get(rData.getIndex());
	comp.setText(rData.getRemindAbout() + " " + rData.getRemindAt());
    }

}
