package no.uib.probe.csf.pr.touch.view.components.heatmapsubcomponents;

import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import org.vaadin.teemu.VaadinIcons;

/**
 * This class give the control on the header label style and events
 *
 * @author Yehia Farag
 */
public abstract class HeaderCell extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    /**
     * The main header caption label.
     */
    private final Label valueLabel;
    /**
     * The header is selected.
     */
    private boolean selected = false;
    /*
     *List of included comparisons to be updated on select this cell.
     */
    private final Set<QuantDiseaseGroupsComparison> includedComparisons = new LinkedHashSet<>();
    /*
     *List of included heat map cells to be updated on select this cell.
     */
    private final List<HeatmapCell> includedCells;
    /**
     * The main header caption string value.
     */
    private final String title;
    /**
     * The HTML hashed color code for this cell (based on disease category).
     */
    private String color;
    /**
     * The main disease category that that cell belong to.
     */
    private final String diseaseCategory;
    /**
     * Is the cell is a column header cell.
     */
    private final boolean rotate;
    /**
     * Is the header is for combined-renamed disease sub group.
     */
    private boolean combinedHeader = false;
    /**
     * Disease category sort name
     */
    private final String shortName;
    /**
     * Is the disease category c.
     */
    private boolean collapsedHeader = false;

    /**
     * Get the main disease category that that cell belong to
     *
     * @return diseaseCategory Disease category name
     */
    public String getDiseaseCategory() {
        return diseaseCategory;
    }

    /**
     * Get list of included heat map cells to be updated on select this cell
     *
     * @return includedCells Set of heat map cells
     */
    public List<HeatmapCell> getIncludedCells() {
        return includedCells;
    }

    /**
     * Get list of included comparisons to be updated on select this cell
     *
     * @return includedComparisons Set of disease comparisons
     */
    public Set<QuantDiseaseGroupsComparison> getIncludedComparisons() {
        return includedComparisons;
    }

    /**
     * Get the overall value of the header cell
     *
     * @return disease sub group title + "__" + diseaseCategory
     */
    public String getValueLabel() {
        return title + "__" + diseaseCategory;
    }

    /**
     * Constructor to initialize the main attributes
     *
     *
     * @param shortName disease short name
     * @param diseaseSubgroupName the disease sub group diseaseSubgroupName
     * @param fullDiseaseSubgroupName full name of disease subgroup
     * @param diseaseCategory the main disease category (MS, AD, PD..etc)
     * @param rotate is the cell is column or row header cell
     *
     */
    public HeaderCell(String shortName, String diseaseSubgroupName, String fullDiseaseSubgroupName, String diseaseCategory, boolean rotate) {
        this.includedCells = new ArrayList<>();
        this.diseaseCategory = diseaseCategory;
        this.rotate = rotate;
        this.title = diseaseSubgroupName;

        valueLabel = new Label();
        valueLabel.setContentMode(ContentMode.HTML);

        String allStyle = "hm" + diseaseCategory.toLowerCase().replace("'", "").replace(" ", "") + "style";

        if (shortName == null) {
            this.shortName = null;
            valueLabel.setValue(diseaseSubgroupName.replace("Amyotrophic Lateral Sclerosis", "ALS"));
        } else {
            this.shortName = shortName;
            valueLabel.setValue(shortName.replace("Amyotrophic Lateral Sclerosis", "ALS"));
        }
        valueLabel.setStyleName(allStyle);
        valueLabel.setWidth(100, Unit.PERCENTAGE);
        valueLabel.setHeight(100, Unit.PERCENTAGE);
        if (rotate) {
            valueLabel.addStyleName("rotateheader");
        }
        valueLabel.addStyleName(ValoTheme.LABEL_SMALL);
        valueLabel.addStyleName(ValoTheme.LABEL_TINY);
        HeaderCell.this.addComponent(valueLabel);
        HeaderCell.this.setComponentAlignment(valueLabel, Alignment.TOP_CENTER);

        String fullName;
        HeaderCell.this.addLayoutClickListener(HeaderCell.this);
        if (fullDiseaseSubgroupName == null) {
            fullName = diseaseSubgroupName;
        } else {
            fullName = fullDiseaseSubgroupName;
        }
        String combinedGroup = "";
        if (diseaseSubgroupName.contains("*")) {
            fullName = diseaseSubgroupName;
            combinedGroup = " - Combined disease groups";
        }
        HeaderCell.this.setDescription(fullName + combinedGroup);
//        this.valueLabel.setValue(allStyle);

    }

    /**
     * Check if header is collapsed / expanded
     *
     * @return the header is collapsed
     */
    public boolean isCollapsedHeader() {
        return collapsedHeader;
    }

    /**
     * Set header to be collapsed
     *
     * @param collapsedHeader header is collapsed
     */
    public void setCollapsedHeader(boolean collapsedHeader) {

        if (collapsedHeader) {
            valueLabel.setValue(title.replace("Amyotrophic Lateral Sclerosis", "ALS").replace("Multiple Sclerosis", "MS") + " " + VaadinIcons.EXPAND_SQUARE.getHtml());
            this.addStyleName("diseasecategorylabel");
        } else {
            if (shortName == null) {
                valueLabel.setValue(title.replace("Amyotrophic Lateral Sclerosis", "ALS").replace("Multiple Sclerosis", "MS"));
            } else {
                valueLabel.setValue(title.replace("Amyotrophic Lateral Sclerosis", "ALS") + " " + VaadinIcons.COMPRESS_SQUARE.getHtml());
            }
            this.removeStyleName("diseasecategorylabel");
        }
        this.collapsedHeader = collapsedHeader;
        if (shortName != null) {
            if ((rotate && valueLabel.getWidth() <= 100 && valueLabel.getWidth() > 80)) {
                valueLabel.setValue(valueLabel.getValue().replace("Multiple Sclerosis", "MS"));
            } else if ((rotate && valueLabel.getWidth() <= 80)) {
                valueLabel.setValue(valueLabel.getValue().replace("Multiple Sclerosis", "MS").replace("Alzheimer's", "AD").replace("Parkinson's", "PD"));
            } else if (!rotate && this.getWidth() <= 100 && this.getWidth() > 80) {
                valueLabel.setValue(valueLabel.getValue().replace("Multiple Sclerosis", "MS"));
            } else if (!rotate && this.getWidth() <= 80) {
                valueLabel.setValue(valueLabel.getValue().replace("Multiple Sclerosis", "MS").replace("Alzheimer's", "AD").replace("Parkinson's", "PD"));
            }
        }
    }

    /**
     * On click the header cell select/un-select
     *
     * @param event user click event
     */
    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        if (selected) {
            selected = false;
            unselect();
            unSelectData(getValueLabel());
        } else {
            selected = true;
            select();
            selectData(getValueLabel());
        }
    }

    /**
     * Select the header cell
     */
    public void select() {
        this.addStyleName("hmselectedcell");

    }

    /**
     * Un-select the header cell.
     */
    public void unselect() {

        this.removeStyleName("hmselectedcell");

    }

    /**
     * Get the HTML hashed color code for this cell (based on disease category)
     *
     * @return color HTML hashed color code
     */
    public String getColor() {
        return color;
    }

    /**
     * Remove the header data (related heat-map cells and related disease
     * comparisons).
     */
    public void resetHeader() {
        this.includedComparisons.clear();
        includedCells.clear();
    }

    /**
     * Add comparison and heat map cell to included comparison set and included
     * heat map cells list
     *
     * @param groupComp Quant disease comparison
     * @param cell Heat map cell
     */
    public void addComparison(QuantDiseaseGroupsComparison groupComp, HeatmapCell cell) {
        this.includedComparisons.add(groupComp);
        if (!combinedHeader && cell.isCombinedHeader()) {
            combinedHeader = true;
            this.setDescription(title + " (" + "Combined group)");
        }
        if (!cell.isCombinedHeader()) {
            this.includedCells.add(cell);
        }
    }

    /**
     * Update the system (select)using the header title
     *
     * @param cellheader header cell title
     */
    public abstract void selectData(String cellheader);

    /**
     * Update the system (un-select)using the header title
     *
     * @param cellHeader Header cell title
     */
    public abstract void unSelectData(String cellHeader);

    /**
     * Set the height and update group disease label height as well as the font
     * size of the labels
     *
     * @param height The header cell height
     * @param unit The height unit
     */
    @Override
    public void setHeight(float height, Unit unit) {
        super.setHeight(height, unit); //To change body of generated methods, choose Tools | Templates.

        if (rotate) {
            valueLabel.setWidth(height - 2, unit);
        } else {
            valueLabel.removeStyleName("linehight200");
            valueLabel.removeStyleName("linehight180");
            if (height < 15) {
                valueLabel.addStyleName("xxsmallfont");
                valueLabel.addStyleName("linehight180");
            } else if (height >= 15 && height < 18) {
                valueLabel.addStyleName("xsmallfont");
                valueLabel.addStyleName("linehight180");
            } else {
                valueLabel.addStyleName("smallfont");
                valueLabel.addStyleName("linehight200");
            }
        }
    }

    /**
     * Set the width and update group disease label width as well as the font
     * size of the labels
     *
     * @param width The header cell width
     * @param unit The width unit
     */
    @Override
    public void setWidth(float width, Unit unit) {
        super.setWidth(width, unit); //To change body of generated methods, choose Tools | Templates
        if (rotate) {
            valueLabel.removeStyleName("linehight200");
            valueLabel.removeStyleName("linehight180");
            if (width < 15) {
                valueLabel.addStyleName("xxsmallfont");
                valueLabel.addStyleName("linehight180");
            } else if (width >= 15 && width < 20) {
                valueLabel.addStyleName("xsmallfont");
                valueLabel.addStyleName("linehight180");
            } else {
                valueLabel.addStyleName("smallfont");
                valueLabel.addStyleName("linehight200");
            }
            valueLabel.setHeight(width - 2, unit);
        }

    }

}
