package com.kiloux.restopos.gui;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class BootPanel extends JPanel {

    private JTextArea term;
    private Timer timer;
    private int step = 0;
    private Runnable onComplete;

    public BootPanel(Runnable onComplete) {
        this.onComplete = onComplete;
        
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        
        term = new JTextArea();
        term.setBackground(Color.BLACK);
        term.setForeground(Color.WHITE);
        term.setFont(new Font("Monospaced", Font.BOLD, 14));
        term.setEditable(false);
        term.setMargin(new Insets(20, 20, 20, 20));
        add(term, BorderLayout.CENTER);
        
        // Boot Sequence Steps
        timer = new Timer(600, e -> nextStep());
        timer.start();
        
        print("KOU-BIOS (C) 1998 American Megatrends Inc.");
        print("BIOS Date: 01/11/98 14:00:00 Ver: 1.0.0");
        print("CPU: Pentium II 400MHz");
    }
    
    private void print(String txt) {
        term.append(txt + "\n");
        term.setCaretPosition(term.getDocument().getLength());
    }
    
    private void nextStep() {
        step++;
        switch(step) {
            case 1: print("Checking Memory : 64MB OK"); break;
            case 2: print("Detecting Primary Master ... KOU-HDD-20GB"); break;
            case 3: print("Detecting Primary Slave  ... None"); break;
            case 4: print("Detecting Secondary Master ... CD-ROM 24x"); break;
            case 5: print(""); break;
            case 6: print("Booting from Hard Disk..."); break;
            case 7: 
                term.setForeground(new Color(100, 200, 255)); // Longhorn Blue
                print("Starting RestoOS Longhorn (Build 4074)..."); 
                break;
            case 9:
                print("Loading Kernel (NT 6.0)...");
                break;
            case 11:
                print("Initializing DWM (Desktop Window Manager)...");
                break;
            case 13:
                print("Mounting /dev/sda1 (MySQL)... [OK]");
                break;
            case 15:
                print("Starting Aero Glass Interface...");
                break;
            case 17:
                timer.stop();
                try { Thread.sleep(500); } catch(Exception e){}
                onComplete.run();
                break;
        }
    }
}
