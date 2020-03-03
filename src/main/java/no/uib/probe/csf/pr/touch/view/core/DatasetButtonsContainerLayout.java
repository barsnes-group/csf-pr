package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.VerticalLayout;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDataset;

/**
 * This class represents dataset information buttons container pop-up window.
 *
 * @author Yehia Farag
 */
public class DatasetButtonsContainerLayout extends VerticalLayout {

    /**
     * Main button container.
     */
    private final GridLayout popUpBtnsContainerGridLayout;
    /**
     * CSS style name for even datasets or studies.
     */
    private final String styleI = "lightbluebackground";
    /**
     * CSS style name for odd datasets or studies.
     */
    private final String styleII = "lightredbackground";
    /**
     * CSS marker for last selected style name.
     */
    private String lastStyle = styleII;
    /**
     * Map of publication name and its CSS style name.
     */
    Map<String, String> publicationStyle;
    /**
     * Total number of columns (for dataset tab).
     */
    private final int colNum;
    /**
     * Counter of rows (for dataset tab).
     */
    private int rowcounter;
    /**
     * Total number of rows (for dataset tab).
     */
    private int rowNumb;

    /**
     * Set and update the pop-up buttons for datasets and publications.
     *
     * @param dsObjects collection of dataset objects required to initialize the
     * datasets information pop-up buttons,
     */
    public void setInformationData(Collection<QuantDataset> dsObjects) {

        popUpBtnsContainerGridLayout.removeAllComponents();
        publicationStyle.clear();

        rowNumb = Math.max(1, ((dsObjects.size() / popUpBtnsContainerGridLayout.getColumns()) + 1));
        popUpBtnsContainerGridLayout.setRows(rowNumb);
        if (rowNumb == 1) {
             popUpBtnsContainerGridLayout.setWidth(100, Unit.PERCENTAGE);
              popUpBtnsContainerGridLayout.setSpacing(true);
        } else {
            popUpBtnsContainerGridLayout.setWidth(100, Unit.PERCENTAGE);
              popUpBtnsContainerGridLayout.setSpacing(false);
        }

        Map<String, Set<QuantDataset>> sortMap = new TreeMap(Collections.reverseOrder());
        dsObjects.stream().forEach((ds) -> {
            String key = ds.getYear() + "_" + ds.getPubMedId();
            if (!sortMap.containsKey(key)) {
                sortMap.put(key, new HashSet<>());
            }
            Set<QuantDataset> set = sortMap.get(key);
            set.add(ds);
            sortMap.put(key, set);
        });

        int colcounter = 0;
        int tempRowcounter = 0;
        for (String quantDSKey : sortMap.keySet()) {
            for (QuantDataset quantDS : sortMap.get(quantDSKey)) {
                if (!publicationStyle.containsKey(quantDS.getPubMedId())) {
                    if (lastStyle.equalsIgnoreCase(styleI)) {
                        publicationStyle.put(quantDS.getPubMedId(), styleII);
                        lastStyle = styleII;
                    } else {
                        publicationStyle.put(quantDS.getPubMedId(), styleI);
                        lastStyle = styleI;
                    }

                }
                String btnName = "<font size=1 >" + quantDS.getYear() + "</font><br/>" + quantDS.getAuthor() + "<br/><font size=1 >PMID "+quantDS.getPubMedId()+ "</font>";
//                String btnName = "<font size=1 >" + quantDS.getYear() + "</font><br/>" + quantDS.getAuthor() + "<br/><font size=1 >#Proteins: " + quantDS.getTotalProtNum() + "   #Peptides: " + quantDS.getTotalPepNum() + "</font>";
                PopupWrapperBtn btn = new PopupWrapperBtn(quantDS, btnName, quantDS.getAuthor());
                btn.addStyleName(publicationStyle.get(quantDS.getPubMedId()));
                popUpBtnsContainerGridLayout.addComponent(btn, colcounter++, tempRowcounter);
                if (colcounter >= popUpBtnsContainerGridLayout.getColumns()) {
                    colcounter = 0;
                    tempRowcounter++;

                }
            }
        }
         if (popUpBtnsContainerGridLayout.getColumns() > sortMap.size() && popUpBtnsContainerGridLayout.getRows() == 1) {
            for (int x = colcounter; x < popUpBtnsContainerGridLayout.getColumns(); x++) {
                VerticalLayout emptyLayout = new VerticalLayout();
                emptyLayout.setHeight(20, Unit.PIXELS);
                emptyLayout.setWidth(200, Unit.PIXELS);
                popUpBtnsContainerGridLayout.addComponent(emptyLayout, x, 0);
            }

        }


    }

    /**
     * List of publication information where each element represent one
     * publication.
     *
     * @param publicationObjects list of publication data arrays.
     */
    public void setPublicationData(List<Object[]> publicationObjects) {

        popUpBtnsContainerGridLayout.removeAllComponents();
        publicationStyle.clear();
        rowNumb = Math.max(1, ((publicationObjects.size() / popUpBtnsContainerGridLayout.getColumns()) + 1));
        popUpBtnsContainerGridLayout.setRows(rowNumb);
        if (rowNumb == 1) {
//          popUpBtnsContainerGridLayout.setWidthUndefined();
            popUpBtnsContainerGridLayout.setWidth(100, Unit.PERCENTAGE);
            popUpBtnsContainerGridLayout.setSpacing(true);
        } else {
            popUpBtnsContainerGridLayout.setWidth(100, Unit.PERCENTAGE);
             popUpBtnsContainerGridLayout.setSpacing(false);
        }
        Map<String, Object[]> sortMap = new TreeMap(Collections.reverseOrder());
        publicationObjects.stream().forEach((obj) -> {
            String key = obj[2].toString() + "_" + obj[0].toString();
            sortMap.put(key, obj);
        });
        int colcounter = 0;
        rowcounter = 0;
        lastStyle = styleII;
        for (String quantDSKey : sortMap.keySet()) {
            Object[] obj = sortMap.get(quantDSKey);

            if (!publicationStyle.containsKey(obj[0].toString())) {
                if (lastStyle.equalsIgnoreCase(styleI)) {
                    publicationStyle.put(obj[0].toString(), styleII);
                    lastStyle = styleII;
                } else {
                    publicationStyle.put(obj[0].toString(), styleI);
                    lastStyle = styleI;
                }
            }
              String btnName = "<font size=1 >" + obj[2].toString() + "</font><br/>" + obj[1] + "<br/><font size=1 >PMID "+obj[0]+ "</font>";
//            String btnName = "<font size=1 >" + obj[2].toString() + "</font><br/>" + obj[1].toString() + "<br/><font size=1 >#Proteins: " + obj[5].toString() + "   #Peptides: " + obj[7].toString() + "</font>";
            PopupWrapperBtn btn = new PopupWrapperBtn(btnName, obj[1].toString(), obj);
            btn.addStyleName(publicationStyle.get(obj[0].toString()));
            popUpBtnsContainerGridLayout.addComponent(btn, colcounter++, rowcounter);
            if (colcounter >= popUpBtnsContainerGridLayout.getColumns()) {
                colcounter = 0;
                rowcounter++;

            }

        }
        if (popUpBtnsContainerGridLayout.getColumns() > sortMap.size() && popUpBtnsContainerGridLayout.getRows() == 1) {
            for (int x = sortMap.size(); x < popUpBtnsContainerGridLayout.getColumns(); x++) {
                VerticalLayout emptyLayout = new VerticalLayout();
                emptyLayout.setHeight(20, Unit.PIXELS);
                emptyLayout.setWidth(200, Unit.PIXELS);
                popUpBtnsContainerGridLayout.addComponent(emptyLayout, x, 0);
            }

        }

    }

    /**
     * Get final row number for the body grid layout.
     *
     * @return rowNumb Number of grid layout container rows.
     */
    public int getRowcounter() {
        return rowNumb;
    }

    /**
     * Constructor to initialize the main layout and the main attributes.
     *
     * @param width the available width for the layout.
     */
    public DatasetButtonsContainerLayout(int width) {
        this.publicationStyle = new HashMap<>();
        this.setWidth(100, Unit.PERCENTAGE);
        this.setHeightUndefined();
        this.setMargin(new MarginInfo(true, false, true, true));
        this.setSpacing(true);
        width = width * 90 / 100;
        colNum = Math.max(1, width / 210);
        popUpBtnsContainerGridLayout = new GridLayout();
        popUpBtnsContainerGridLayout.setWidth(100, Unit.PERCENTAGE);
        popUpBtnsContainerGridLayout.setColumns(colNum);
        for (int x = 0; x < colNum; x++) {
            popUpBtnsContainerGridLayout.setColumnExpandRatio(x, 200);
        }
        popUpBtnsContainerGridLayout.setStyleName("whitelayout");
        popUpBtnsContainerGridLayout.setHeightUndefined();
//        popUpBtnsContainerGridLayout.setSpacing(false);
        popUpBtnsContainerGridLayout.setHideEmptyRowsAndColumns(false);
        this.addComponent(popUpBtnsContainerGridLayout);

    }

    /**
     * Get final column number for the body grid layout.
     *
     * @return colNum Number of grid layout container columns.
     */
    public int getColNumber() {
        return colNum;
    }

    /**
     * Get the final height for the grid container layout.
     *
     * @return the calculated layout height
     */
    public int getLayoutHeight() {
        return popUpBtnsContainerGridLayout.getRows() * 100;
    }

}
