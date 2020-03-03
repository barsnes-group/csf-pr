package no.uib.probe.csf.pr.touch.view.components.heatmapsubcomponents;

import com.itextpdf.text.pdf.codec.Base64;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import no.uib.probe.csf.pr.touch.logic.beans.HeatMapHeaderCellInformationBean;
import org.jfree.chart.encoders.ImageEncoder;
import org.jfree.chart.encoders.ImageEncoderFactory;
import org.jfree.chart.encoders.ImageFormat;

/**
 * This class represents heat map image generator that is use Swing to generate
 * image that is used for thumb left side button also used to provide the heat
 * map Absolute layout for the different cells and headers location
 *
 * @author Yehia Farag
 */
public class HeatMapImgGenerator {

    /**
     * Transparent AWT color.
     */
    private final Color transparent = new Color(255, 255, 255, 0);
    /**
     * White AWT color.
     */
    private final Color white = new Color(255, 255, 255);
    /**
     * Generated image width.
     */
    private int imageWidth;
    /**
     * Generated image height.
     */
    private int imageHeight;
    /**
     * Heat map cell width.
     */
    private int cellWidth;
    /**
     * Resize factor that is used for ratios.
     */
    private double resizeFactor;
    /**
     * Map of headers and size of each label.
     */
    private final Map<String, Rectangle> headerLabelMap = new LinkedHashMap<>();
    /**
     * Show/hide labels values.
     */
    private boolean printLabels;
    /**
     * Main heat map swing JPanel.
     */
    private JPanel heatmapPanelLayout;
    /**
     * Main heat map cell border (Swing border).
     */
    private final Border fullBorder = new LineBorder(new Color(220, 224, 224), 1);

    /**
     * Get map of headers and size of each label
     *
     * @return headerLabelMap map of header labels title to the label dimensions
     */
    public Map<String, Rectangle> getHeaderLabelMap() {
        return headerLabelMap;
    }

    /**
     * Generate heat map image encoded as Base64 String that is used as image
     * URL
     *
     * @param rows Set of header cell information for rows
     * @param columns Set of header cell information for columns
     * @param data 2D array of values for heat map
     * @param heatmapWidth Generated image width
     * @param heatmaHeight Generated image height
     * @param resetRowLabels Reset disease category row labels
     * @param restColumnLabels Reset disease category column labels
     * @param printLabels Print the value of each label
     * @return URL for heat map image
     */
    public String generateHeatmap(Collection<HeatMapHeaderCellInformationBean> rows, Collection<HeatMapHeaderCellInformationBean> columns, String[][] data, int heatmapWidth, int heatmaHeight, boolean resetRowLabels, boolean restColumnLabels, boolean printLabels) {
        this.printLabels = printLabels;
        headerLabelMap.clear();
        cellWidth = 20;

        heatmapPanelLayout = new JPanel();

        heatmapPanelLayout.setLayout(null);
        heatmapPanelLayout.setVisible(true);
        heatmapPanelLayout.setBackground(transparent);

        imageWidth = (columns.size() * cellWidth) + 150;
        imageHeight = (rows.size() * cellWidth) + 150;
        cellWidth = 20;
        resizeFactor = 1;

        cellWidth = (int) (20 * resizeFactor);
        heatmapPanelLayout.setSize(imageWidth, imageHeight);
        JPanel cornerCell = generateHeatmapCell("transparent", 0, 0, fullBorder);
        cornerCell.setSize((int) (150 * resizeFactor), (int) (150 * resizeFactor));
        heatmapPanelLayout.add(cornerCell);
        int x = (int) (150 * resizeFactor);
        int y = (int) (20 * resizeFactor);

        for (HeatMapHeaderCellInformationBean bean : columns) {

            headerLabelMap.put("col__" + bean.getDiseaseCategory(), new Rectangle(0, 0, 0, 0));

        }
        for (HeatMapHeaderCellInformationBean bean : rows) {

            headerLabelMap.put("row__" + bean.getDiseaseCategory(), new Rectangle(0, 0, 0, 0));

        }

        for (HeatMapHeaderCellInformationBean headerCell : columns) {

            if (!restColumnLabels) {
                Rectangle rectangle = headerLabelMap.get("col__" + headerCell.getDiseaseCategory());
                rectangle.setBounds(0, 0, (int) rectangle.getWidth() + cellWidth, (int) (20 * resizeFactor));
            }
            JPanel cell = generateHeatmapCell(headerCell.getDiseaseHashedColor(), x, y, fullBorder);
            cell.setSize(cellWidth, (int) (130 * resizeFactor));
            JComponent label2 = generateHeatmapCellLabel(headerCell.getDiseaseGroupName(), cell.getSize().width, cell.getSize().height, true, cell.getBackground(), fullBorder);
            cell.add(label2);
            x += cellWidth;
            heatmapPanelLayout.add(cell);
        }

        x = (int) (150 * resizeFactor);
        int topLabelContainerWidth = (columns.size() * cellWidth);
        int countLimit = columns.size() / 3;
        int counter = 0;

        for (String dCat : headerLabelMap.keySet()) {

            HeatMapHeaderCellInformationBean bean = null;
            for (HeatMapHeaderCellInformationBean tbean : columns) {
                if (("col__" + tbean.getDiseaseCategory()).equalsIgnoreCase(dCat)) {
                    bean = tbean;
                    break;

                }
            }
            if (restColumnLabels && bean != null) {
                int width = countLimit * cellWidth;
                topLabelContainerWidth = topLabelContainerWidth - width;
                if (imageWidth != 0 && counter == headerLabelMap.size() - 1) {
                    width += topLabelContainerWidth;
                }

                JComponent label = generateHeatmapCellLabel(dCat.split("__")[1], width, (int) (20 * resizeFactor), false, Color.decode(bean.getDiseaseHashedColor()), fullBorder);
                label.setLocation(x, 0);
                heatmapPanelLayout.add(label);
                x += label.getSize().width;
                counter++;

            } else if (bean != null) {
                JComponent label = generateHeatmapCellLabel(dCat.split("__")[1], headerLabelMap.get(dCat).width, (int) (20 * resizeFactor), false, Color.decode(bean.getDiseaseHashedColor()), fullBorder);
                label.setLocation(x, 0);
                heatmapPanelLayout.add(label);
                x += headerLabelMap.get(dCat).width;
            }

        }
        y = (int) (150 * resizeFactor);

        for (HeatMapHeaderCellInformationBean headerCell : rows) {

            if (!resetRowLabels) {

                Rectangle rectangle = headerLabelMap.get("row__" + headerCell.getDiseaseCategory());
                rectangle.setBounds(0, 0, (int) (20 * resizeFactor), (int) rectangle.getHeight() + cellWidth);
            }

            JPanel cell = generateHeatmapCell(headerCell.getDiseaseHashedColor(), (int) (20 * resizeFactor), y, fullBorder);
            cell.setSize((int) (130 * resizeFactor), cellWidth);
            JComponent label2 = generateHeatmapCellLabel(headerCell.getDiseaseGroupName(), cell.getSize().width, cell.getSize().height, false, cell.getBackground(), fullBorder);
            cell.add(label2);
            y += cellWidth;
            heatmapPanelLayout.add(cell);

        }
        y = (int) (150 * resizeFactor);
        topLabelContainerWidth = (rows.size() * cellWidth);
        countLimit = rows.size() / 3;
        counter = 0;

        for (String dCat : headerLabelMap.keySet()) {

            HeatMapHeaderCellInformationBean bean = null;
            for (HeatMapHeaderCellInformationBean tbean : rows) {
                if (("row__" + tbean.getDiseaseCategory()).equalsIgnoreCase(dCat)) {
                    bean = tbean;
                    break;

                }
            }
            if (resetRowLabels && bean != null) {
                int width = countLimit * cellWidth;
                topLabelContainerWidth = topLabelContainerWidth - width;
                if (imageHeight != 0 && counter == headerLabelMap.size() - 1) {
                    width += topLabelContainerWidth;
                }

                JComponent label = generateHeatmapCellLabel(dCat.split("__")[1], (int) (20 * resizeFactor), width, true, Color.decode(bean.getDiseaseHashedColor()), fullBorder);
                label.setLocation(0, y);
                heatmapPanelLayout.add(label);
                y += label.getSize().height;
                counter++;

            } else if (bean != null) {
                JComponent label = generateHeatmapCellLabel(dCat.split("__")[1], (int) (20 * resizeFactor), headerLabelMap.get(dCat).height, true, Color.decode(bean.getDiseaseHashedColor()), fullBorder);
                label.setLocation(0, y);
                heatmapPanelLayout.add(label);
                y += headerLabelMap.get(dCat).height;
            }

        }

        x = (int) (150 * resizeFactor);
        y = (int) (150 * resizeFactor);
        for (String[] row : data) {
            for (String color : row) {
                JPanel cell = generateHeatmapCell(color.split("__")[0], x, y, fullBorder);
                cell.setOpaque(true);
                JComponent label2 = generateHeatmapCellLabel(color.split("__")[1], cell.getSize().width, cell.getSize().height, false, cell.getBackground(), fullBorder);
                cell.add(label2);
                x += cellWidth;
                heatmapPanelLayout.add(cell);
            }
            x = (int) (150 * resizeFactor);
            y += cellWidth;
        }

        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();

        graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        heatmapPanelLayout.paint(graphics);
        byte[] imageData = null;

        try {

            ImageEncoder in = ImageEncoderFactory.newInstance(ImageFormat.PNG, 1);
            imageData = in.encode(image);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

        String base64 = Base64.encodeBytes(imageData);
        base64 = "data:image/png;base64," + base64;
        return base64;

    }

    /**
     * Get main heat map panel height
     *
     * @return imageHeight current panel height
     */
    public int getPanelHeight() {
        return imageHeight;
    }

    /**
     * Generate heat map cell
     *
     * @return cell JPanel for heat map cell
     */
    private JPanel generateHeatmapCell(String color, int x, int y, Border border) {
        JPanel cell = new JPanel(new FlowLayout(FlowLayout.CENTER));
        cell.setSize(cellWidth, cellWidth);
        Color c;

        if (color == null) {
            c = white;
        } else if (color.equalsIgnoreCase("RGB(255,255,255)")) {
            c = white;
        } else if (color.contains("#")) {
            c = Color.decode(color);
        } else if (color.toLowerCase().contains("rgb")) {
            String rgb = color.toLowerCase().replace("rgb", "").replace("(", "").replace(")", "").replace(" ", "");
            String[] stringRGBArr = rgb.split(",");
            c = new Color(Integer.valueOf(stringRGBArr[0]), Integer.valueOf(stringRGBArr[1]), Integer.valueOf(stringRGBArr[2]));
        } else {
            c = white;
        }

        cell.setBackground(c);
        cell.setBorder(border);
        cell.setOpaque(true);
        cell.setLocation(x, y);

        return cell;

    }

    /**
     * Generate heat map cell label
     *
     * @return label JComponent for label content
     */
    private JComponent generateHeatmapCellLabel(String text, int w, int h, boolean rotate, Color background, Border border) {
        JLabel label = new JLabel("");
        if (this.printLabels) {
            label.setText(text);
            label.setFont(new Font("Helvetica Neue", Font.PLAIN, (int) (12 * resizeFactor)));
            label.setVerticalTextPosition(JLabel.CENTER);
            label.setHorizontalTextPosition(JLabel.CENTER);
            label.setForeground(Color.WHITE);
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setVerticalAlignment(JLabel.CENTER);
        }
        label.setSize(w, h);
        label.setBackground(background);
        label.setBorder(border);
        label.setOpaque(true);

        if (rotate) {
            if (this.printLabels) {
                label.setFont(new Font("Helvetica Neue", Font.PLAIN, (int) (12 * resizeFactor)));
                label.setVerticalTextPosition(JLabel.TOP);
                label.setHorizontalTextPosition(JLabel.CENTER);
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setVerticalAlignment(JLabel.TOP);
            }
            label.setSize(h, w);
            RotatedJPanel rotPanel = new RotatedJPanel();
            rotPanel.setSize(h, h);
            rotPanel.add(label);
            JPanel panel = rotPanel.getRotatedJPanel();
            panel.setOpaque(false);
            panel.setSize(w, h);
            rotPanel.setBackground(Color.blue);
            rotPanel.setSize(h, h);
            rotPanel.add(label);
            return (panel);
        }
        return label;

    }

    /**
     * Get main heat map panel width
     *
     * @return imageWidth width of the panel
     */
    public int getPanelWidth() {
        return imageWidth;
    }

    /**
     * Get resize factor that is used for ratios
     *
     * @return resizeFactor Resize factor to correct the components size
     */
    public double getResizeFactor() {
        return resizeFactor;
    }

}
