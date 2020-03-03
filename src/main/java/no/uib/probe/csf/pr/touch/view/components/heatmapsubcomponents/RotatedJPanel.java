package no.uib.probe.csf.pr.touch.view.components.heatmapsubcomponents;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 * This class represents the rotated JPanel that is used for heat map column
 * cell header and disease category the class is used for generating images in
 * swing panels
 *
 * @author Yehia Farag
 */
public class RotatedJPanel extends JPanel {

    /**
     * The image that is used for rotation process.
     */
    private BufferedImage componentImg;

    /**
     * Convert the main JPAnel into a buffered image in order to rotate it.
     */
    private void updateComponentImg() {
        BufferedImage img = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = (Graphics2D) img.getGraphics();
        this.paint(g2d);
        componentImg = createFlipped(img);

    }

    /**
     * Rotate the image 270 degrees.
     *
     * @param image Image of the panel to rotate
     * @return image rotated for panel
     */
    private BufferedImage createFlipped(BufferedImage image) {

        double rotationRequired = Math.toRadians(270);
        double locationX = image.getWidth() / 2;
        double locationY = image.getHeight() / 2;
        AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

        AffineTransform at = new AffineTransform();
        at.concatenate(AffineTransform.getScaleInstance(1, -1));
        at.concatenate(AffineTransform.getTranslateInstance(0, -image.getHeight()));

        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = newImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.drawImage(op.filter(image, null), 0, 0, null);
        return newImage;
    }

    /**
     * Get rotated JPanel
     *
     * @return panel after being rotated 270 degree
     */
    public JPanel getRotatedJPanel() {
        updateComponentImg();
        JPanel panel = new JPanel() {

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(componentImg, 0, 0, this);
            }

        };
        panel.setSize(this.getWidth(), this.getHeight());
        panel.setLocation(this.getLocation());
        panel.setOpaque(false);
        return panel;

    }
}
