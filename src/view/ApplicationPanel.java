package view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import app.Application;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;

public class ApplicationPanel extends JFrame implements ActionListener // ,MouseListener
, HotkeyListener
{
    private JPopupMenu rightMenu;
    private JMenuItem addReminderMenuItem,addNoteMenuItem; 
    private AddReminderFrame addRem;
    private AddNoteFrame addNote;
    private int lastX,lastY;
    // hot key
    JIntellitype jintel;
    public ApplicationPanel()
    {
	this.setTitle("Reminder");
	rightMenu=new JPopupMenu();
	addReminderMenuItem=new JMenuItem("Create Reminder");
	addNoteMenuItem=new JMenuItem("Add Note");	
	addReminderMenuItem.addActionListener(this);
	addNoteMenuItem.addActionListener(this);
	//hotkey
	jintel=JIntellitype.getInstance();
	jintel.registerHotKey(1, JIntellitype.MOD_ALT + JIntellitype.MOD_SHIFT, (int)'S');
	//jintel.registerHotKey(2, JIntellitype.MOD_ALT + JIntellitype.MOD_SHIFT, (int)'B');

	//or you can use the Swing constants instead
	//jintel.registerSwingHotKey(3, Event.CTRL_MASK + Event.SHIFT_MASK, (int)'C');
	//this.addWindowListener(this);
	// To unregister them just call unregisterHotKey with the unique identifier
	/*jintel.unregisterHotKey(1);
	jintel.unregisterHotKey(2);
	jintel.unregisterHotKey(3);*/
	JIntellitype.getInstance().addHotKeyListener(this);
	setDefaultCloseOperation(DO_NOTHING_ON_CLOSE );
	// scroll bar related
	/*JScrollPane scrollPane=new JScrollPane(this.getContentPane());
	scrollPane.setPreferredSize(new Dimension(300,240));
	scrollPane.setVerticalScrollBar(new JScrollBar());
	scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	scrollPane.setHorizontalScrollBar(new JScrollBar(JScrollBar.HORIZONTAL));
	scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	 */// scroll
    }

    public void actionPerformed(ActionEvent ae)
    {
	System.out.println(ae.getActionCommand());
	if(ae.getActionCommand().contains("Reminder"))
	{
	addRem=new AddReminderFrame(lastX,lastY);
	addRem.setVisible(true);
	}
	else if(ae.getActionCommand().contains("Note"))
	{
	    addNote=new AddNoteFrame(lastX, lastY);
	    addNote.setVisible(true);
	}
    }
    public void showPopup(MouseEvent invoker)
    {
	// TODO Auto-generated method stub
	System.out.println("in app panel");
	rightMenu.add(addReminderMenuItem);
	rightMenu.add(addNoteMenuItem);
	lastX=invoker.getXOnScreen();
	lastY=invoker.getYOnScreen();
	Component c=invoker.getComponent();

	rightMenu.show(c, invoker.getX(), invoker.getY());
	System.out.println("invoker = "+c.getClass().getName().toString());
	//rightMenu.show(c, invoker.getX(), invoker.getY());

    }

    @Override
    public void onHotKey(int aIdentifier)
    {
	// TODO Auto-generated method stub
	if (aIdentifier == 1)
	    this.setVisible(true);
    }
}

