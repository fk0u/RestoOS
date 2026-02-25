package com.kiloux.restopos.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public final class IconAssets {

    private static final Map<String, ImageIcon> CACHE = new HashMap<>();

    private IconAssets() {
    }

    public static ImageIcon getMenuIcon(String key) {
        return getIcon(key, 24);
    }

    public static ImageIcon getFrameIcon(String key) {
        return getIcon(key, 16);
    }

    public static Icon getTaskbarIcon(String key) {
        return getIcon(key, 14);
    }

    private static ImageIcon getIcon(String key, int size) {
        String cacheKey = key + "@" + size;
        if (CACHE.containsKey(cacheKey)) {
            return CACHE.get(cacheKey);
        }

        ImageIcon icon = loadFromResources(key, size);
        if (icon == null) {
            icon = createVectorFallback(key, size);
        }

        CACHE.put(cacheKey, icon);
        return icon;
    }

    private static ImageIcon loadFromResources(String key, int size) {
        String[] candidates = {
            "/images/icons/" + key + ".png",
            "/images/icons/" + key + ".jpg",
            "/images/icons/" + key + ".jpeg"
        };

        for (String path : candidates) {
            try (InputStream stream = IconAssets.class.getResourceAsStream(path)) {
                if (stream == null) {
                    continue;
                }
                BufferedImage image = ImageIO.read(stream);
                if (image != null) {
                    return new ImageIcon(image.getScaledInstance(size, size, java.awt.Image.SCALE_SMOOTH));
                }
            } catch (IOException ignored) {
            }
        }

        return null;
    }

    private static ImageIcon createVectorFallback(String key, int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color c1 = colorForKey(key, true);
        Color c2 = colorForKey(key, false);
        g2.setPaint(new GradientPaint(0, 0, c1, size, size, c2));
        g2.fill(new RoundRectangle2D.Float(0.5f, 0.5f, size - 1f, size - 1f, size * 0.3f, size * 0.3f));

        g2.setColor(new Color(255, 255, 255, 180));
        g2.setStroke(new BasicStroke(Math.max(1f, size * 0.08f)));
        g2.draw(new RoundRectangle2D.Float(1f, 1f, size - 2f, size - 2f, size * 0.3f, size * 0.3f));

        String glyph = glyphForKey(key);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Segoe UI", Font.BOLD, Math.max(8, (int) (size * 0.55f))));
        int tw = g2.getFontMetrics().stringWidth(glyph);
        int th = g2.getFontMetrics().getAscent();
        g2.drawString(glyph, (size - tw) / 2, (size + th) / 2 - 2);

        g2.dispose();
        return new ImageIcon(img);
    }

    private static String glyphForKey(String key) {
        return switch (key) {
            case "browser" -> "W";
            case "mail" -> "@";
            case "pos" -> "$";
            case "settings" -> "⚙";
            case "notepad" -> "N";
            case "terminal" -> ">_";
            case "designer" -> "D";
            case "designer-frame" -> "F";
            case "netbeans" -> "NB";
            case "explorer" -> "E";
            case "control" -> "C";
            case "kitchen" -> "K";
            case "monitor" -> "M";
            case "admin" -> "A";
            case "user" -> "U";
            default -> "R";
        };
    }

    private static Color colorForKey(String key, boolean first) {
        return switch (key) {
            case "browser" -> first ? new Color(59, 130, 246) : new Color(30, 64, 175);
            case "mail" -> first ? new Color(249, 115, 22) : new Color(194, 65, 12);
            case "pos" -> first ? new Color(16, 185, 129) : new Color(4, 120, 87);
            case "settings" -> first ? new Color(107, 114, 128) : new Color(55, 65, 81);
            case "notepad" -> first ? new Color(56, 189, 248) : new Color(2, 132, 199);
            case "terminal" -> first ? new Color(22, 101, 52) : new Color(21, 128, 61);
            case "designer", "designer-frame", "netbeans" -> first ? new Color(99, 102, 241) : new Color(67, 56, 202);
            case "explorer" -> first ? new Color(14, 165, 233) : new Color(2, 132, 199);
            case "control" -> first ? new Color(234, 88, 12) : new Color(194, 65, 12);
            case "kitchen" -> first ? new Color(245, 158, 11) : new Color(217, 119, 6);
            case "monitor" -> first ? new Color(6, 182, 212) : new Color(8, 145, 178);
            case "admin" -> first ? new Color(168, 85, 247) : new Color(126, 34, 206);
            case "user" -> first ? new Color(59, 130, 246) : new Color(29, 78, 216);
            default -> first ? new Color(20, 184, 166) : new Color(15, 118, 110);
        };
    }
}