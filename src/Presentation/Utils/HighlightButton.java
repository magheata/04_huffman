/* Created by andreea on 13/04/2020 */
package Presentation.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class HighlightButton extends JButton {

    private Color highlight;

    public HighlightButton(String text) {
        super(text);
        setOpaque(false);
    }

    public void setHighlight(Color color) {
        if (color != highlight) {
            Color old = highlight;
            this.highlight = color;
            firePropertyChange("highlight", old, highlight);
            repaint();
        }
    }

    public Color getHighlight() {
        return highlight;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Color highlight = getHighlight();
        if (highlight != null) {
            BufferedImage img = createCompatibleImage(getWidth(), getHeight(), Transparency.TRANSLUCENT);
            Graphics2D g2d = img.createGraphics();
            super.paintComponent(g2d);
            g2d.dispose();
            BufferedImage mask = generateMask(img, getHighlight(), 1f);
            g.drawImage(img, 0, 0, this);
            g.drawImage(mask, 0, 0, this);
        } else {
            super.paintComponent(g);
        }
    }

    public BufferedImage createCompatibleImage(int width, int height, int transparency) {
        BufferedImage image = getGraphicsConfiguration().createCompatibleImage(width, height, transparency);
        image.coerceData(true);
        return image;
    }

    public GraphicsConfiguration getGraphicsConfiguration() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
    }

    public BufferedImage generateMask(BufferedImage imgSource, Color color, float alpha) {

        int imgWidth = imgSource.getWidth();
        int imgHeight = imgSource.getHeight();

        BufferedImage imgMask = createCompatibleImage(imgWidth, imgHeight, Transparency.TRANSLUCENT);
        Graphics2D g2 = imgMask.createGraphics();
        applyQualityRenderingHints(g2);

        g2.drawImage(imgSource, 0, 0, null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN, alpha));
        g2.setColor(color);

        g2.fillRect(0, 0, imgSource.getWidth(), imgSource.getHeight());
        g2.dispose();

        return imgMask;

    }

    public static void applyQualityRenderingHints(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }
}
