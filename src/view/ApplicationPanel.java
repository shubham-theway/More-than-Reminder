package view;

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

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;

public class ApplicationPanel extends JFrame implements ActionListener // ,MouseListener
, HotkeyListener
{
    private JPopupMenu rightMenu;
    private JMenuItem rightMenuItem; 
    private AddReminderFrame addRem;
    private int lastX,lastY;
    // hot key
    JIntellitype jintel;
    public ApplicationPanel()
    {
	this.setTitle("Reminder");
	rightMenu=new JPopupMenu();
	rightMenuItem=new JMenuItem("Create Reminder");
	rightMenuItem.addActionListener(this);
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
	addRem=new AddReminderFrame(lastX,lastY);
	addRem.setVisible(true);
    }
    public void showPopup(MouseEvent invoker)
    {
	// TODO Auto-generated method stub
	System.out.println("in app panel");
	rightMenu.add(rightMenuItem);
	lastX=invoker.getXOnScreen();
	lastY=invoker.getYOnScreen();
	
	rightMenu.show(invoker.getComponent(), invoker.getX(), invoker.getY());
	
    }

    @Override
    public void onHotKey(int aIdentifier)
    {
	// TODO Auto-generated method stub
	if (aIdentifier == 1)
            this.setVisible(true);
    }
}

