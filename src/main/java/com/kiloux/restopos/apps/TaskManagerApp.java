package com.kiloux.restopos.apps;

import com.kiloux.restopos.ui.MetroUtils;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 * Task Manager - Metro Style
 * Shows running windows and system resources
 */
public class TaskManagerApp extends JInternalFrame {

    private JTable processTable;
    private DefaultTableModel tableModel;
    private JProgressBar memoryBar;
    private JProgressBar cpuBar;
    private Timer refreshTimer;
    
    public TaskManagerApp(JDesktopPane desktop) {
        super("Task Manager", true, true, true, true);
        setSize(600, 450);
        setFrameIcon(null);
        
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(MetroUtils.METRO_BG_DARK);
        setContentPane(main);
        
        // --- Header ---
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                MetroUtils.drawMetroHeader((Graphics2D)g, getWidth(), getHeight(), new Color(100, 65, 165)); // Purple
            }
        };
        header.setPreferredSize(new Dimension(getWidth(), 60));
        header.setBorder(new EmptyBorder(15, 20, 10, 20));
        
        JLabel title = new JLabel("Task Manager");
        title.setFont(new Font("Segoe UI Light", Font.PLAIN, 28));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.WEST);
        main.add(header, BorderLayout.NORTH);
        
        // --- Process Table ---
        String[] cols = {"Name", "Status", "Memory (MB)"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        processTable = new JTable(tableModel);
        processTable.setBackground(new Color(40, 40, 40));
        processTable.setForeground(Color.WHITE);
        processTable.setGridColor(new Color(60, 60, 60));
        processTable.setRowHeight(28);
        processTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        processTable.getTableHeader().setBackground(new Color(50, 50, 50));
        processTable.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scroll = new JScrollPane(processTable);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(new Color(40, 40, 40));
        main.add(scroll, BorderLayout.CENTER);
        
        // --- Footer (Performance) ---
        JPanel footer = new JPanel(new GridLayout(2, 1, 5, 5));
        footer.setBackground(MetroUtils.METRO_BG_DARK);
        footer.setBorder(new EmptyBorder(10, 20, 15, 20));
        
        // Memory Bar
        JPanel memRow = new JPanel(new BorderLayout(10, 0));
        memRow.setOpaque(false);
        JLabel memLbl = new JLabel("Memory Usage:");
        memLbl.setForeground(Color.GRAY);
        memLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        memoryBar = new JProgressBar(0, 100);
        memoryBar.setStringPainted(true);
        memoryBar.setForeground(MetroUtils.ACCENT_PURPLE);
        memRow.add(memLbl, BorderLayout.WEST);
        memRow.add(memoryBar, BorderLayout.CENTER);
        footer.add(memRow);
        
        // CPU Bar (Simulated)
        JPanel cpuRow = new JPanel(new BorderLayout(10, 0));
        cpuRow.setOpaque(false);
        JLabel cpuLbl = new JLabel("CPU Usage:");
        cpuLbl.setForeground(Color.GRAY);
        cpuLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cpuBar = new JProgressBar(0, 100);
        cpuBar.setStringPainted(true);
        cpuBar.setForeground(MetroUtils.ACCENT_CYAN);
        cpuRow.add(cpuLbl, BorderLayout.WEST);
        cpuRow.add(cpuBar, BorderLayout.CENTER);
        footer.add(cpuRow);
        
        main.add(footer, BorderLayout.SOUTH);
        
        // --- Refresh Logic ---
        refreshTimer = new Timer(2000, e -> refreshData(desktop));
        refreshTimer.start();
        refreshData(desktop);
        
        // Stop timer when closed
        addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent e) {
                refreshTimer.stop();
            }
        });
    }
    
    private void refreshData(JDesktopPane desktop) {
        tableModel.setRowCount(0);
        
        // List open frames
        if (desktop != null) {
            for (JInternalFrame f : desktop.getAllFrames()) {
                String name = f.getTitle();
                String status = f.isSelected() ? "Active" : "Running";
                int mem = (int)(Math.random() * 50) + 10; // Simulated
                tableModel.addRow(new Object[]{name, status, mem});
            }
        }
        
        // System process placeholders
        tableModel.addRow(new Object[]{"System", "Running", 80});
        tableModel.addRow(new Object[]{"Desktop Window Manager", "Running", 45});
        tableModel.addRow(new Object[]{"RestoOS Core", "Running", 120});
        
        // Memory
        Runtime rt = Runtime.getRuntime();
        long used = (rt.totalMemory() - rt.freeMemory()) / (1024 * 1024);
        long max = rt.maxMemory() / (1024 * 1024);
        int pct = (int) ((used * 100) / max);
        memoryBar.setValue(pct);
        memoryBar.setString(used + " MB / " + max + " MB");
        
        // CPU (Simulated)
        int cpu = (int)(Math.random() * 30) + 5;
        cpuBar.setValue(cpu);
        cpuBar.setString(cpu + "%");
    }
}
