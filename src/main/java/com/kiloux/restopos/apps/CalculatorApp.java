package com.kiloux.restopos.apps;

import com.kiloux.restopos.ui.AeroPanel;
import com.kiloux.restopos.ui.RetroButton;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class CalculatorApp extends JInternalFrame {
    
    private JTextField display;
    private double first = 0;
    private String operator = "";
    private boolean startNew = true;

    public CalculatorApp() {
        super("Calculator", true, true, true, true);
        setSize(300, 400);
        setFrameIcon(null);
        
        // Main UI
        AeroPanel panel = new AeroPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setTintColor(new Color(240, 240, 240, 200)); // Opaque-ish Glass
        
        display = new JTextField("0");
        display.setFont(new Font("Consolas", Font.BOLD, 24));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        display.setBackground(Color.WHITE);
        display.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        display.setPreferredSize(new Dimension(280, 50));
        panel.add(display, BorderLayout.NORTH);
        
        JPanel grid = new JPanel(new GridLayout(4, 4, 5, 5));
        grid.setOpaque(false);
        
        String[] keys = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", "C", "=", "+"
        };
        
        for (String k : keys) {
            RetroButton btn = new RetroButton(k);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btn.addActionListener(createAction(k));
            grid.add(btn);
        }
        
        panel.add(grid, BorderLayout.CENTER);
        setContentPane(panel);
    }
    
    private ActionListener createAction(String key) {
        return e -> {
            if ("0123456789".contains(key)) {
                if (startNew) { display.setText(""); startNew = false; }
                display.setText(display.getText() + key);
            } else if ("C".equals(key)) {
                display.setText("0");
                first = 0;
                operator = "";
                startNew = true;
            } else if ("=".equals(key)) {
                calculate();
                operator = "";
                startNew = true;
            } else {
                // Operator
                if (!operator.isEmpty()) calculate();
                first = Double.parseDouble(display.getText());
                operator = key;
                startNew = true;
            }
        };
    }
    
    private void calculate() {
        try {
            double second = Double.parseDouble(display.getText());
            double res = 0;
            switch(operator) {
                case "+": res = first + second; break;
                case "-": res = first - second; break;
                case "*": res = first * second; break;
                case "/": res = first / second; break;
            }
            if (res % 1 == 0) display.setText(String.valueOf((int)res));
            else display.setText(String.valueOf(res));
        } catch(Exception e) {
            display.setText("Error");
        }
    }
}
