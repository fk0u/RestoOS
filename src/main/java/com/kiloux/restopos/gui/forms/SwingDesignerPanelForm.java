package com.kiloux.restopos.gui.forms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.TransferHandler;
import javax.swing.border.TitledBorder;

public class SwingDesignerPanelForm extends JPanel {

    private final JPanel canvas;
    private JComponent selectedComponent;

    public SwingDesignerPanelForm() {
        setLayout(new BorderLayout(8, 8));

        JPanel leftPalette = buildPalette();
        add(leftPalette, BorderLayout.WEST);

        canvas = new JPanel(null);
        canvas.setBackground(new Color(245, 247, 250));
        canvas.setTransferHandler(new PaletteImportHandler());
        canvas.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(190, 200, 215)),
            "Design Canvas (Drop di sini)",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12)
        ));

        JScrollPane centerScroll = new JScrollPane(canvas);
        add(centerScroll, BorderLayout.CENTER);

        add(buildBottomTools(), BorderLayout.SOUTH);
    }

    private JPanel buildPalette() {
        JPanel palette = new JPanel(new BorderLayout());
        palette.setPreferredSize(new Dimension(220, 0));
        palette.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(190, 200, 215)),
            "Palette",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12)
        ));

        JPanel itemList = new JPanel();
        itemList.setLayout(new javax.swing.BoxLayout(itemList, javax.swing.BoxLayout.Y_AXIS));
        itemList.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        itemList.add(createDraggablePaletteItem("Button", "BUTTON"));
        itemList.add(createDraggablePaletteItem("Label", "LABEL"));
        itemList.add(createDraggablePaletteItem("TextField", "TEXTFIELD"));
        itemList.add(createDraggablePaletteItem("CheckBox", "CHECKBOX"));

        JLabel hint = new JLabel("Drag item ke canvas");
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        hint.setForeground(new Color(100, 110, 125));
        hint.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        palette.add(itemList, BorderLayout.NORTH);
        palette.add(hint, BorderLayout.SOUTH);
        return palette;
    }

    private JButton createDraggablePaletteItem(String label, String type) {
        JButton button = new JButton(label);
        button.setAlignmentX(LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 34));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setTransferHandler(new ValueExportTransferHandler(type));
        button.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                JComponent source = (JComponent) e.getSource();
                source.getTransferHandler().exportAsDrag(source, e, TransferHandler.COPY);
            }
        });
        return button;
    }

    private JPanel buildBottomTools() {
        JPanel tools = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));

        JButton deleteSelected = new JButton("Hapus Selected");
        deleteSelected.addActionListener(e -> {
            if (selectedComponent != null) {
                canvas.remove(selectedComponent);
                selectedComponent = null;
                canvas.repaint();
            }
        });

        JButton clearAll = new JButton("Clear Canvas");
        clearAll.addActionListener(e -> {
            canvas.removeAll();
            selectedComponent = null;
            canvas.revalidate();
            canvas.repaint();
        });

        JButton exportCode = new JButton("Generate Code");
        exportCode.addActionListener(e -> showGeneratedCode());

        tools.add(deleteSelected);
        tools.add(clearAll);
        tools.add(exportCode);
        return tools;
    }

    private void attachCanvasDrag(JComponent component) {
        MouseAdapter dragHandler = new MouseAdapter() {
            private Point offset;

            @Override
            public void mousePressed(MouseEvent e) {
                selectedComponent = component;
                offset = e.getPoint();
                component.setBorder(BorderFactory.createLineBorder(new Color(0, 120, 215), 2));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (selectedComponent == component) {
                    component.setBorder(null);
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                int nextX = component.getX() + e.getX() - offset.x;
                int nextY = component.getY() + e.getY() - offset.y;
                component.setLocation(Math.max(0, nextX), Math.max(0, nextY));
                canvas.repaint();
            }
        };

        component.addMouseListener(dragHandler);
        component.addMouseMotionListener(dragHandler);
    }

    private JComponent createWidget(String type) {
        JComponent widget;
        switch (type) {
            case "BUTTON" -> widget = new JButton("Button");
            case "LABEL" -> widget = new JLabel("Label");
            case "TEXTFIELD" -> widget = new JTextField("TextField");
            case "CHECKBOX" -> widget = new JCheckBox("CheckBox");
            default -> widget = new JLabel("Unknown");
        }

        if (widget instanceof JTextField textField) {
            textField.setColumns(12);
        }

        widget.setSize(widget.getPreferredSize());
        attachCanvasDrag(widget);
        return widget;
    }

    private void showGeneratedCode() {
        StringBuilder builder = new StringBuilder();
        builder.append("JPanel panel = new JPanel(null);\n");

        for (int i = 0; i < canvas.getComponentCount(); i++) {
            java.awt.Component component = canvas.getComponent(i);
            if (!(component instanceof JComponent swingComponent)) {
                continue;
            }

            String variable = "comp" + i;
            if (swingComponent instanceof JButton button) {
                builder.append("JButton ").append(variable).append(" = new JButton(\"")
                    .append(button.getText()).append("\");\n");
            } else if (swingComponent instanceof JLabel label) {
                builder.append("JLabel ").append(variable).append(" = new JLabel(\"")
                    .append(label.getText()).append("\");\n");
            } else if (swingComponent instanceof JTextField textField) {
                builder.append("JTextField ").append(variable).append(" = new JTextField(\"")
                    .append(textField.getText()).append("\");\n");
            } else if (swingComponent instanceof JCheckBox checkBox) {
                builder.append("JCheckBox ").append(variable).append(" = new JCheckBox(\"")
                    .append(checkBox.getText()).append("\");\n");
            }

            builder.append(variable)
                .append(".setBounds(")
                .append(component.getX()).append(", ")
                .append(component.getY()).append(", ")
                .append(component.getWidth()).append(", ")
                .append(component.getHeight()).append(");\n");
            builder.append("panel.add(").append(variable).append(");\n\n");
        }

        JTextArea output = new JTextArea(builder.toString(), 20, 65);
        output.setFont(new Font("Consolas", Font.PLAIN, 12));
        output.setEditable(false);

        JOptionPane.showMessageDialog(this, new JScrollPane(output), "Generated Swing Code", JOptionPane.INFORMATION_MESSAGE);
    }

    private final class ValueExportTransferHandler extends TransferHandler {
        private final String value;

        private ValueExportTransferHandler(String value) {
            this.value = value;
        }

        @Override
        protected Transferable createTransferable(JComponent c) {
            return new StringSelection(value);
        }

        @Override
        public int getSourceActions(JComponent c) {
            return COPY;
        }
    }

    private final class PaletteImportHandler extends TransferHandler {

        @Override
        public boolean canImport(TransferSupport support) {
            return support.isDrop() && support.isDataFlavorSupported(DataFlavor.stringFlavor);
        }

        @Override
        public boolean importData(TransferSupport support) {
            if (!canImport(support)) {
                return false;
            }

            try {
                String type = (String) support.getTransferable().getTransferData(DataFlavor.stringFlavor);
                Point dropPoint = support.getDropLocation().getDropPoint();
                JComponent widget = createWidget(type);
                widget.setLocation(dropPoint);
                canvas.add(widget);
                canvas.repaint();
                return true;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(SwingDesignerPanelForm.this,
                    "Gagal drop component: " + ex.getMessage(),
                    "Drag & Drop Error",
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
    }
}