package com.kiloux.restopos.apps;

import com.kiloux.restopos.config.UIConfig;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.text.*;

/**
 * Terminal App - Simulated Command Line Interface
 */
public class TerminalApp extends JInternalFrame {

    private JTextPane console;
    private StyledDocument doc;
    private File currentDir;
    private String prompt;
    
    public TerminalApp() {
        super("Terminal - bash", true, true, true, true);
        setSize(600, 400);
        setFrameIcon(null);
        
        currentDir = new File(".");
        updatePrompt();
        
        console = new JTextPane();
        console.setFont(new Font("Consolas", Font.PLAIN, 14));
        console.setBackground(new Color(30, 30, 30)); // UBUNTU DARK
        console.setForeground(new Color(230, 230, 230));
        console.setCaretColor(Color.WHITE);
        
        doc = console.getStyledDocument();
        append("Welcome to RestoOS Terminal v1.0\nType 'help' for commands.\n\n");
        appendPrompt();
        
        console.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume();
                    String txt = console.getText();
                    int lastPrompt = txt.lastIndexOf(prompt);
                    if (lastPrompt >= 0) {
                        String cmd = txt.substring(lastPrompt + prompt.length()).trim();
                        append("\n");
                        processCommand(cmd);
                        appendPrompt();
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    // Prevent deleting prompt
                    String txt = console.getText();
                    if (txt.endsWith(prompt)) {
                        e.consume();
                    }
                }
            }
        });
        
        JScrollPane scroll = new JScrollPane(console);
        scroll.setBorder(null);
        add(scroll);
    }
    
    private void updatePrompt() {
        prompt = "guest@restoos:" + currentDir.getName() + "$ ";
    }
    
    private void append(String text) {
        try {
            doc.insertString(doc.getLength(), text, null);
            console.setCaretPosition(doc.getLength());
        } catch(Exception e){}
    }
    
    private void appendPrompt() {
        SimpleAttributeSet attrs = new SimpleAttributeSet();
        StyleConstants.setForeground(attrs, new Color(115, 210, 22)); // GREEN PROMPT
        StyleConstants.setBold(attrs, true);
        try {
            doc.insertString(doc.getLength(), prompt, attrs);
            console.setCaretPosition(doc.getLength());
        } catch(Exception e){}
    }
    
    private void processCommand(String cmd) {
        if (cmd.isEmpty()) return;
        
        String[] parts = cmd.split("\\s+");
        String base = parts[0].toLowerCase();
        
        switch(base) {
            case "help":
                append("Available commands:\n" +
                       "  ls           List directory contents\n" +
                       "  cd <dir>     Change directory\n" +
                       "  pwd          Print working directory\n" +
                       "  whoami       Show current user\n" +
                       "  date         Show system date\n" +
                       "  neofetch     Show system info\n" + 
                       "  clear        Clear screen\n" +
                       "  exit         Close terminal\n");
                break;
                
            case "ls":
                File[] files = currentDir.listFiles();
                if (files != null) {
                    for(File f : files) {
                        String name = f.getName();
                        if (f.isDirectory()) name += "/";
                        append(name + "  ");
                    }
                    append("\n");
                }
                break;
                
            case "cd":
                if (parts.length > 1) {
                    if ("..".equals(parts[1])) {
                         File p = currentDir.getParentFile();
                         if (p != null) currentDir = p;
                    } else {
                         File target = new File(currentDir, parts[1]);
                         if (target.exists() && target.isDirectory()) {
                             currentDir = target;
                         } else {
                             append("cd: " + parts[1] + ": No such directory\n");
                         }
                    }
                    updatePrompt();
                }
                break;
            
            case "pwd":
                append(currentDir.getAbsolutePath() + "\n");
                break;
                
            case "whoami":
                append(com.kiloux.restopos.service.UserService.getInstance().getCurrentUser() + "\n");
                break;
                
            case "date":
                append(new java.util.Date().toString() + "\n");
                break;
                
            case "neofetch":
                append("       _ \n");
                append("      / \\      OS: RestoOS Aero-Gnome v16.0\n");
                append("     /   \\     Kernel: Java " + System.getProperty("java.version") + "\n");
                append("    /  |  \\    Uptime: " + (System.currentTimeMillis() / 60000) + " mins\n");
                append("   /   |   \\   Shell: TerminalApp\n");
                append("  /____|____\\  Theme: Adwaita Dark Glass\n");
                append("               CPU: Simulated @ 3.5GHz\n");
                break;
                
            case "clear":
                console.setText("");
                break;
                
            case "exit":
                doDefaultCloseAction();
                break;
                
            default:
                append(cmd + ": command not found\n");
        }
    }
}
