package no.uib.probe.csf.pr.touch.view.components.heatmapsubcomponents;

import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseGroupComparison;
import no.uib.probe.csf.pr.touch.logic.beans.HeatMapHeaderCellInformationBean;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDSIndexes;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDataset;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import no.uib.probe.csf.pr.touch.view.bigscreen.popupwindows.DatasetInformationBtn;
import no.uib.probe.csf.pr.touch.view.components.QuantDatasetsfullStudiesTableLayout;
import no.uib.probe.csf.pr.touch.view.core.ImageContainerBtn;
import no.uib.probe.csf.pr.touch.view.core.InformationButton;

/**
 * This class represents the main body for disease comparisons heat map
 *
 * @author Yehia Farag
 */
public abstract class HeatMapLayout extends VerticalLayout {

    /**
     * Set of selected cells.
     */
    private final Set<HeatmapCell> selectedCells = new HashSet<>();

    /**
     * Array of of Column header cells.
     */
    private Map<String, HeaderCell> columnHeaderCells;
    /**
     * Array of of Row header cells.
     */
    private Map<String, HeaderCell> rowHeaderCells;
    /**
     * Map of comparisons title to heat-map cell.
     */
    private final Map<String, HeatmapCell> comparisonsCellsMap = new LinkedHashMap<>();
    /**
     * Set of selected quant disease groups comparisons.
     */
    private final Set<QuantDiseaseGroupsComparison> selectedQuantDiseaseGroupsComparisonList;
    /**
     * Set of all quant disease groups comparisons available in the heat map.
     */
    private final Set<QuantDiseaseGroupsComparison> availableQuantDiseaseGroupsComparisonList;
    /**
     * Multiple selection is allowed.
     */
    private boolean singleSelection = false;
    /**
     * Heat map cell width.
     */
    private int heatmapCellWidth;
    /**
     * Array of ordered HTML row colors for thumb image (required by swing
     * panel).
     */
    private String[] rowsColors;
    /**
     * Array of ordered HTML column colors for thumb image (required by swing
     * panel)
     */
    private String[] columnsColors;
    /**
     * 2D Array of ordered HTML rows and columns colors of each cell for thumb
     * image (required by swing panel).
     */
    private String[][] dataValuesColors;
    /**
     * Set of current available dataset indexes.
     */
    private final Set<Integer> currentDsIds;
    /**
     * "D Array of the heat map cells.
     */
    private HeatmapCell[][] cellTable;
    /**
     * Map of quant dataset to its index in the database.
     */
    private final Map<Integer, QuantDataset> updatedDatasetMap;
    /**
     * The max number of dataset is used for color code of the heat map color
     * generator.
     */
    private int maxDatasetNumber;
    /**
     * 2D Array of quant dataset re-indexing object that used to re-index the
     * datasets in the heat-map.
     */
    private QuantDSIndexes[][] values;
    /**
     * The set of heatmap headers cell information objects required for
     * initialize the row headers.
     */
    private Map<String, HeatMapHeaderCellInformationBean> fullRowheadersSet;
    /**
     * The set of heatmap headers cell information objects required for
     * initialize the column headers.
     */
    private Map<String, HeatMapHeaderCellInformationBean> fullColheadersSet;
    private Set<DiseaseGroupComparison> fullPatientsGroupComparisonsSet;
    private Set<DiseaseGroupComparison> activePatientsGroupComparisonsSet;
    private Map<Integer, QuantDataset> fullQuantDsMap;
    private final Set<HeaderCell> diseaseCategoryHeadersSet;
    /**
     * Reset the disease category labels on the left side.
     */
    private boolean resetLeftSideDiseaseCategoriesLabel = false;
    /**
     * Reset the disease category labels on the top side.
     */
    private boolean resetTopSideDiseaseCategoriesLabel = false;
    /**
     * The map of equal heat map cell to the flipped comparisons.
     */
    private final Map<QuantDiseaseGroupsComparison, QuantDiseaseGroupsComparison> equalComparisonMap;
    /**
     * The heat map image generator is used to generate heat map image that is
     * used for the thumb icon (developed in swing)
     */
    private final HeatMapImgGenerator gen = new HeatMapImgGenerator();
    /**
     * Layout initialization.
     */
    /**
     * The main layout for the heat map (the main container contains heat-map
     * panel and filters).
     */
    private final AbsoluteLayout heatmapComponentContainer;
    /**
     * The main heat-map panel.
     */
    private final Panel heatMapContainerPanel;
    /**
     * The main heat-map layout (to be placed inside scrolling panel).
     */
    private final AbsoluteLayout heatmapPanelLayout;
    /**
     * The main heat-map control buttons layout.
     */
    private final HorizontalLayout controlsLayout;
    /**
     * The main heat-map control buttons container layout.
     */
    private final VerticalLayout heatmapToolsContainer;
    /**
     * The top left corner cell that contains the heat map filters buttons.
     */
    private final VerticalLayout cornerCell;
    /**
     * The top left heat map filters container.
     */
    private final HeatmapFiltersContainerResizeControl filterResizeController;
    /**
     * Available height for the heat map component.
     */
    private final int availableHMHeight;
    /**
     * Available width for the heat map component.
     */
    private final int availableHMWidth;
    /**
     * Reset filters button.
     */
    private final ImageContainerBtn clearFilterBtn;
    /**
     * Heat-map dataset counter.
     */
    private final Set<Integer> currentDsCounter;
    private Map<Integer, QuantDataset> activeQuantDsMap;
    private int activeHeatmapCellCount;
    private final Map<String, HeatMapHeaderCellInformationBean> collapsedRowheadersSet = new LinkedHashMap<>();
    private Set<String> fullDiseaseCategory;

    /**
     * Get reset filters button.
     *
     * @return clearFilterBtn reset filters button.
     */
    public ImageContainerBtn getClearFilterBtn() {
        return clearFilterBtn;
    }

    /**
     * Constructor to initialize the main attributes
     *
     * @param heatMapContainerWidth Main container layout width
     * @param availableHMHeight Available height for the container layout
     * @param activeColumnHeaders Array of the column header available to
     * dataset export table
     * @param filterResizeController top left corner cell that has the dataset
     * filters
     * @param fullPublicationList List of publication objects array
     */
    public HeatMapLayout(int heatMapContainerWidth, int availableHMHeight, boolean[] activeColumnHeaders, HeatmapFiltersContainerResizeControl filterResizeController, List<Object[]> fullPublicationList) {
        this.filterResizeController = filterResizeController;
        this.availableQuantDiseaseGroupsComparisonList = new LinkedHashSet<>();
        this.updatedDatasetMap = new LinkedHashMap<>();
        this.selectedQuantDiseaseGroupsComparisonList = new LinkedHashSet<>();
        this.currentDsCounter = new HashSet<>();
        this.currentDsIds = new LinkedHashSet<>();

        HeatMapLayout.this.setWidthUndefined();
        HeatMapLayout.this.setHeightUndefined();
        HeatMapLayout.this.setSpacing(false);
        HeatMapLayout.this.addStyleName("whitelayout");
        HeatMapLayout.this.addStyleName("roundedborder");
        HeatMapLayout.this.addStyleName("padding20");
        HeatMapLayout.this.addStyleName("minimumwidth350");

        this.availableHMHeight = availableHMHeight - 45;
        this.availableHMWidth = heatMapContainerWidth - 40;
        heatMapContainerPanel = new Panel();

        heatMapContainerPanel.setStyleName(ValoTheme.PANEL_BORDERLESS);

        HeatMapLayout.this.addComponent(heatMapContainerPanel);
        HeatMapLayout.this.setComponentAlignment(heatMapContainerPanel, Alignment.MIDDLE_CENTER);

        this.heatmapPanelLayout = new AbsoluteLayout();
        heatMapContainerPanel.setContent(heatmapPanelLayout);
        heatmapPanelLayout.setStyleName("lightgray");

        this.heatmapComponentContainer = new AbsoluteLayout();
        heatmapComponentContainer.setSizeFull();
        this.heatmapPanelLayout.addComponent(heatmapComponentContainer, "left: " + 0 + "px; top: " + 0 + "px");

        //heatmap controllers
        //init heatmap filters buttons 
        controlsLayout = new HorizontalLayout();
        controlsLayout.setVisible(true);

        controlsLayout.setHeightUndefined();
        controlsLayout.setStyleName("margintop10");
        controlsLayout.setHeight(20, Unit.PIXELS);

        controlsLayout.setSpacing(true);
        HeatMapLayout.this.addComponent(controlsLayout);

        Label commentLabel = new Label("<b>*</b> Multiple groups");
        commentLabel.setStyleName(ValoTheme.LABEL_SMALL);
        commentLabel.addStyleName(ValoTheme.LABEL_TINY);
        commentLabel.setContentMode(ContentMode.HTML);
        commentLabel.addStyleName("minwidth100");
        controlsLayout.addComponent(commentLabel);
        controlsLayout.setComponentAlignment(commentLabel, Alignment.TOP_LEFT);

        Label clickcommentLabel = new Label("Click in the table to select data");
        clickcommentLabel.setStyleName(ValoTheme.LABEL_SMALL);
        clickcommentLabel.addStyleName(ValoTheme.LABEL_TINY);
        clickcommentLabel.addStyleName("italictext");
        clickcommentLabel.setWidth(182, Unit.PIXELS);

        controlsLayout.addComponent(clickcommentLabel);
        controlsLayout.setComponentAlignment(clickcommentLabel, Alignment.TOP_RIGHT);

        heatmapToolsContainer = new VerticalLayout();
        heatmapToolsContainer.setHeightUndefined();
        heatmapToolsContainer.setWidthUndefined();
        heatmapToolsContainer.setSpacing(true);

        final DatasetInformationBtn showStudiesBtn = new DatasetInformationBtn() {

            @Override
            public List<Object[]> updatePublicationsData(Set<String> pumedId) {
                List<Object[]> updatedList = new ArrayList<>();
                fullPublicationList.stream().filter((objArr) -> (pumedId.contains(objArr[0].toString()))).forEachOrdered((objArr) -> {
                    updatedList.add(objArr);
                });

                return updatedList;
            }

        };
        heatmapToolsContainer.addComponent(showStudiesBtn);
        heatmapToolsContainer.setComponentAlignment(showStudiesBtn, Alignment.MIDDLE_CENTER);
        showStudiesBtn.addLayoutClickListener((LayoutEvents.LayoutClickEvent event) -> {
             updatedDatasetMap.clear();
                for (int i : activeQuantDsMap.keySet()) {                    
                    if (updatedExpandedList.contains(activeQuantDsMap.get(i).getDiseaseCategoryI()) || updatedExpandedList.contains(activeQuantDsMap.get(i).getDiseaseCategoryII())) {
                        updatedDatasetMap.put(i, activeQuantDsMap.get(i));
                    }

                }
            showStudiesBtn.updateData(updatedDatasetMap.values());
            showStudiesBtn.view();
        });

        final QuantDatasetsfullStudiesTableLayout quantStudiesTable = new QuantDatasetsfullStudiesTableLayout(activeColumnHeaders);
        heatmapToolsContainer.addComponent(quantStudiesTable);

        ImageContainerBtn selectAllBtn = new ImageContainerBtn() {

            @Override
            public void onClick() {
                selectAll();
            }

        };
        selectAllBtn.updateIcon(new ThemeResource("img/grid-small.png"));
        selectAllBtn.setEnabled(true);
        selectAllBtn.addStyleName("smallimg");

        selectAllBtn.setWidth(40, Unit.PIXELS);
        selectAllBtn.setHeight(40, Unit.PIXELS);

        heatmapToolsContainer.addComponent(selectAllBtn);
        heatmapToolsContainer.setComponentAlignment(selectAllBtn, Alignment.MIDDLE_CENTER);
        selectAllBtn.setDescription("Select all disease group comparisons");

        ImageContainerBtn unselectAllBtn = new ImageContainerBtn() {

            @Override
            public void onClick() {
                unselectAll();
            }

        };
        unselectAllBtn.updateIcon(new ThemeResource("img/grid-small-o.png"));
        unselectAllBtn.setEnabled(true);

        unselectAllBtn.setWidth(40, Unit.PIXELS);
        unselectAllBtn.setHeight(40, Unit.PIXELS);
        unselectAllBtn.addStyleName("smallimg");

        heatmapToolsContainer.addComponent(unselectAllBtn);
        heatmapToolsContainer.setComponentAlignment(unselectAllBtn, Alignment.MIDDLE_CENTER);
        unselectAllBtn.setDescription("Unselect all disease group comparisons");

        final ImageContainerBtn selectMultiBtn = new ImageContainerBtn() {

            @Override
            public void onClick() {
                if (this.getStyleName().contains("selectmultiselectedbtn")) {
                    singleSelection = true;
                    this.removeStyleName("selectmultiselectedbtn");

                } else {
                    singleSelection = false;
                    this.addStyleName("selectmultiselectedbtn");

                }
            }

        };
        selectMultiBtn.addStyleName("selectmultiselectedbtn");
        selectMultiBtn.addStyleName("smallimg");
        heatmapToolsContainer.addComponent(selectMultiBtn);
        heatmapToolsContainer.setComponentAlignment(selectMultiBtn, Alignment.MIDDLE_CENTER);
        selectMultiBtn.setDescription("Multiple selection");
        selectMultiBtn.updateIcon(new ThemeResource("img/grid-small-multi.png"));
        selectMultiBtn.setEnabled(true);

        selectMultiBtn.setWidth(40, Unit.PIXELS);
        selectMultiBtn.setHeight(40, Unit.PIXELS);

        clearFilterBtn = new ImageContainerBtn() {

            @Override
            public void onClick() {

            }

        };
        clearFilterBtn.setDescription("Clear filters");
        clearFilterBtn.addStyleName("midimg");
        clearFilterBtn.updateIcon(new ThemeResource("img/filter_clear.png"));
        clearFilterBtn.setEnabled(true);
        clearFilterBtn.setWidth(40, Unit.PIXELS);
        clearFilterBtn.setHeight(40, Unit.PIXELS);
        heatmapToolsContainer.addComponent(clearFilterBtn);
        heatmapToolsContainer.setComponentAlignment(clearFilterBtn, Alignment.MIDDLE_CENTER);

        cornerCell = new VerticalLayout();
        cornerCell.setWidth(150, Unit.PIXELS);
        cornerCell.setHeight(150, Unit.PIXELS);
        cornerCell.setStyleName("whitelayout");
        if (filterResizeController != null) {

            cornerCell.addComponent(filterResizeController.getFilterContainerLayout());
            cornerCell.setComponentAlignment(filterResizeController.getFilterContainerLayout(), Alignment.MIDDLE_CENTER);
            filterResizeController.getFilterContainerLayout().addStyleName("heatmapcorner");
        }

        ImageContainerBtn exportTableBtn = new ImageContainerBtn() {

            @Override
            public void onClick() {
                updatedDatasetMap.clear();
                for (int i : activeQuantDsMap.keySet()) {                    
                    if (updatedExpandedList.contains(activeQuantDsMap.get(i).getDiseaseCategoryI()) || updatedExpandedList.contains(activeQuantDsMap.get(i).getDiseaseCategoryII())) {
                        updatedDatasetMap.put(i, activeQuantDsMap.get(i));
                    }

                }
                quantStudiesTable.updateCombinedQuantDatasetTableRecords(updatedDatasetMap);
                ExcelExport csvExport = new ExcelExport(quantStudiesTable, "CSF-PR  Quant Datasets Information");
                csvExport.setReportTitle("CSF-PR / Quant Datasets Information ");
                csvExport.setExportFileName("CSF-PR - Quant Datasets Information" + ".xls");
                csvExport.setMimeType(ExcelExport.EXCEL_MIME_TYPE);
                csvExport.setDisplayTotals(false);
                csvExport.setDateDataFormat("0");
                csvExport.setExcelFormatOfProperty("Index", "0");
                csvExport.setExcelFormatOfProperty("#Quantified Proteins", "0");
                csvExport.setExcelFormatOfProperty("patientsGroup2Number", "0");
                csvExport.setExcelFormatOfProperty("#patientsGroup1Number", "0");
                csvExport.export();
            }

        };

        exportTableBtn.setHeight(40, Unit.PIXELS);
        exportTableBtn.setWidth(40, Unit.PIXELS);

        exportTableBtn.updateIcon(new ThemeResource("img/xls-text-o-2.png"));
        exportTableBtn.setEnabled(true);
        heatmapToolsContainer.addComponent(exportTableBtn);
        heatmapToolsContainer.setComponentAlignment(exportTableBtn, Alignment.MIDDLE_CENTER);
        exportTableBtn.setDescription("Export heatmap dataset data");

        InformationButton info = new InformationButton("The disease group comparison table provides an overview of the number of datasets available for each comparison. Hover over a given cell to get additional details about the comparison. Selecting one or more cells in the table will display the corresponding protein details. To filter the data use the options in the upper left corner.", false);
        heatmapToolsContainer.addComponent(info);
        equalComparisonMap = new HashMap<>();
        this.diseaseCategoryHeadersSet = new LinkedHashSet<>();
    }

    /**
     * Get side buttons container that has all the heat map control buttons
     *
     * @return heatmapToolsContainer Right buttons layout container
     */
    public VerticalLayout getHeatmapToolsContainer() {
        return heatmapToolsContainer;
    }

    /**
     * Get set of current available dataset indexes
     *
     * @return currentDsIds Set of dataset indexes
     */
    public Set<Integer> getCurrentDsIds() {
        return currentDsIds;
    }

    /**
     * This method responsible for updating the heat-map data
     *
     *
     * @param fullDiseaseCategory Set of all disease category names
     * @param fullRowheadersSet Set of header cell information for rows
     * @param fullColheadersSet Set of header cell information for columns
     * @param fullPatientsGroupComparisonsSet Set of disease group comparisons
     * @param fullQuantDsMap Map of datasets and its indexes
     */
    public void initialiseHeatMapData(Set<String> fullDiseaseCategory, Set<HeatMapHeaderCellInformationBean> fullRowheadersSet, Set<HeatMapHeaderCellInformationBean> fullColheadersSet, Set<DiseaseGroupComparison> fullPatientsGroupComparisonsSet, Map<Integer, QuantDataset> fullQuantDsMap) {
        this.fullRowheadersSet = new LinkedHashMap<>();
        this.fullColheadersSet = new LinkedHashMap<>();
        for (HeatMapHeaderCellInformationBean bean : fullRowheadersSet) {
            this.fullRowheadersSet.put(bean.getDiseaseCategory() + "-_-" + bean.getDiseaseGroupFullName(), bean);
        }
        for (HeatMapHeaderCellInformationBean bean : fullColheadersSet) {
            this.fullColheadersSet.put(bean.getDiseaseCategory() + "-_-" + bean.getDiseaseGroupFullName(), bean);
        }
        this.fullPatientsGroupComparisonsSet = fullPatientsGroupComparisonsSet;
        this.activePatientsGroupComparisonsSet = fullPatientsGroupComparisonsSet;

        this.updatedExpandedList.clear();
        this.collapsedRowheadersSet.clear();
        this.fullQuantDsMap = fullQuantDsMap;
        this.activeQuantDsMap = fullQuantDsMap;
        this.fullDiseaseCategory = fullDiseaseCategory;
        this.updatedExpandedList.addAll(fullDiseaseCategory);
        //draw full heatmap
        drawFullHeatMapLayout();
    }

    private void drawFullHeatMapLayout() {
//    , 
        Collection<HeatMapHeaderCellInformationBean> rowHeaders = fullRowheadersSet.values();
        Collection<HeatMapHeaderCellInformationBean> colHeaders = fullColheadersSet.values();
        updateDiseaseCategoriesLabels(rowHeaders, colHeaders);
        cellTable = new HeatmapCell[colHeaders.size()][colHeaders.size()];
        columnHeaderCells = new LinkedHashMap<>();
        columnsColors = new String[colHeaders.size()];
        for (int i = 0; i < colHeaders.size(); i++) {
            HeatMapHeaderCellInformationBean cellInfo = (HeatMapHeaderCellInformationBean) colHeaders.toArray()[i];
            String title = cellInfo.getDiseaseGroupName().replace("ALS", "Amyotrophic Lateral Sclerosis");
            String headerKey = cellInfo.getDiseaseCategory() + "-_-" + cellInfo.getDiseaseGroupFullName();
            if (title.equalsIgnoreCase("")) {
                title = "Not Available";
            }
            HeaderCell headerCell = new HeaderCell(null, title, cellInfo.getDiseaseGroupFullName(), cellInfo.getDiseaseCategory(), true) {

                @Override
                public void selectData(String valueLabel) {
                    if (this.isCollapsedHeader()) {
                        HeatMapLayout.this.activeQuantDsMap = HeatMapLayout.this.fullQuantDsMap;
                        HeatMapLayout.this.activePatientsGroupComparisonsSet = HeatMapLayout.this.fullPatientsGroupComparisonsSet;
                        collapsedRowheadersSet.remove(valueLabel.split("__")[0]);
                        updateExpandedHeaders(true);
                    } else {
                        addRowSelectedDs(valueLabel);
                    }
                }

                @Override
                public void unSelectData(String valueLabel) {
                    if (!this.isCollapsedHeader()) {
                        removeRowSelectedDs(valueLabel);
                    }
                }
            };
            headerCell.setCollapsedHeader(cellInfo.isCollapseDiseseCategory());
            columnsColors[i] = cellInfo.getDiseaseHashedColor();
            columnHeaderCells.put(headerKey, headerCell);
        }

        //init row headers
        rowHeaderCells = new LinkedHashMap<>();
        rowsColors = new String[rowHeaders.size()];
        for (int i = 0; i < rowHeaders.size(); i++) {
            HeatMapHeaderCellInformationBean cellInfo = (HeatMapHeaderCellInformationBean) rowHeaders.toArray()[i];
            String title = cellInfo.getDiseaseGroupName().replace("ALS", "Amyotrophic Lateral Sclerosis");

            if (title.equalsIgnoreCase("")) {
                title = "Not Available";
            }
            HeaderCell headerCell = new HeaderCell(null, title, cellInfo.getDiseaseGroupFullName(), cellInfo.getDiseaseCategory(), false) {

                @Override
                public void selectData(String valueLabel) {
                    if (this.isCollapsedHeader()) {

                        HeatMapLayout.this.activeQuantDsMap = HeatMapLayout.this.fullQuantDsMap;
                        HeatMapLayout.this.activePatientsGroupComparisonsSet = HeatMapLayout.this.fullPatientsGroupComparisonsSet;
                        collapsedRowheadersSet.remove(valueLabel.split("__")[0]);
                        updateExpandedHeaders(true);
                    } else {
                        addRowSelectedDs(valueLabel);
                    }
                }

                @Override
                public void unSelectData(String valueLabel) {
                    if (!this.isCollapsedHeader()) {
                        removeRowSelectedDs(valueLabel);
                    }
                }
            };
            headerCell.setCollapsedHeader(cellInfo.isCollapseDiseseCategory());
            String headerKey = cellInfo.getDiseaseCategory() + "-_-" + cellInfo.getDiseaseGroupFullName();
            rowHeaderCells.put(headerKey, headerCell);
            rowsColors[i] = cellInfo.getDiseaseHashedColor();

        }
//        init cell table
        for (int x = 0; x < rowHeaders.size(); x++) {
            for (int y = 0; y < colHeaders.size(); y++) {
                HeatmapCell cell = new HeatmapCell() {
                    @Override
                    public void selectData(HeatmapCell cell) {
                        if (collapsedRowheadersSet.containsKey(cell.getDiseaseCategory())) {
                            HeatMapLayout.this.activeQuantDsMap = HeatMapLayout.this.fullQuantDsMap;
                            HeatMapLayout.this.activePatientsGroupComparisonsSet = HeatMapLayout.this.fullPatientsGroupComparisonsSet;
                            collapsedRowheadersSet.remove(cell.getDiseaseCategory());
                            updateExpandedHeaders(true);
                        } else {
                            addSelectedDs(cell);
                        }
                    }

                    @Override
                    public void unSelectData(HeatmapCell cell) {
                        if (!collapsedRowheadersSet.containsKey(cell.getDiseaseCategory())) {
                            removerSelectedDs(cell);
                        }
                    }

                };
                cellTable[x][y] = cell;
            }

        }

    }

    /**
     * This method responsible for updating the heat-map data
     *
     * @param activePatientsGroupComparisonsSet Set of disease group comparisons
     * @param selectedDiseaseCategory set of disease categories
     * @param filteredQuantDsMap Map of datasets and its indexes
     */
    public void updateHeatMapFilterSelection(Set<DiseaseGroupComparison> activePatientsGroupComparisonsSet, Map<Integer, QuantDataset> filteredQuantDsMap, Set<String> selectedDiseaseCategory) {
        if (fullColheadersSet == null || fullRowheadersSet == null) {
            return;
        }
        this.activeQuantDsMap = filteredQuantDsMap;
        this.activePatientsGroupComparisonsSet = activePatientsGroupComparisonsSet;
        if (selectedDiseaseCategory == null) {
            selectDiseaseCategory(this.updatedExpandedList, false, true);
        } else {
            selectDiseaseCategory(selectedDiseaseCategory, false, true);
        }

    }

    /**
     *Reorder disease sub categories in heat-map based on user reorder action
     * @param rowHeaders ordered row headers (disease sub categories)
     * @param colHeaders ordered column headers (disease sub categories)
     */
    public void reorderHeatmap(LinkedHashSet<HeatMapHeaderCellInformationBean> rowHeaders, LinkedHashSet<HeatMapHeaderCellInformationBean> colHeaders) {

        Map<String, HeatMapHeaderCellInformationBean> tempRowheadersSet = new LinkedHashMap<>();
        Map<String, HeatMapHeaderCellInformationBean> tempColheadersSet = new LinkedHashMap<>();
        fullDiseaseCategory.stream().map((diseaseCate) -> {
            rowHeaders.stream().filter((bean) -> (bean.getDiseaseCategory().equalsIgnoreCase(diseaseCate))).map((bean) -> bean.getDiseaseCategory() + "-_-" + bean.getDiseaseGroupFullName()).forEachOrdered((key) -> {
                tempRowheadersSet.put(key, fullRowheadersSet.get(key));
            });
            return diseaseCate;
        }).map((diseaseCate) -> {
            fullRowheadersSet.keySet().stream().filter((key) -> (!tempRowheadersSet.containsKey(key) && key.split("-_-")[0].equalsIgnoreCase(diseaseCate))).forEachOrdered((key) -> {
                tempRowheadersSet.put(key, fullRowheadersSet.get(key));
            });
            return diseaseCate;
        }).map((diseaseCate) -> {
            colHeaders.stream().filter((bean) -> (bean.getDiseaseCategory().equalsIgnoreCase(diseaseCate))).map((bean) -> bean.getDiseaseCategory() + "-_-" + bean.getDiseaseGroupFullName()).forEachOrdered((key) -> {
                tempColheadersSet.put(key, fullColheadersSet.get(key));
            });
            return diseaseCate;
        }).forEachOrdered((diseaseCate) -> {
            for (String key : fullColheadersSet.keySet()) {
                if (!tempColheadersSet.containsKey(key) && key.split("-_-")[0].equalsIgnoreCase(diseaseCate)) {
                    tempColheadersSet.put(key, fullColheadersSet.get(key));
                }
            }
        });
        fullColheadersSet = tempColheadersSet;
        fullRowheadersSet = tempRowheadersSet;
        Set<String> diseaseCategories = new LinkedHashSet<>();
        for (String dc : fullDiseaseCategory) {
            if (!collapsedRowheadersSet.containsKey(dc)) {
                diseaseCategories.add(dc);
            }
        }
        selectDiseaseCategory(diseaseCategories, false, true);

    }


    /**
     *Update heat map selection (expand / collapse) heat-map disease categories 
     * @param diseaseCategories set of selected disease categories
     * @param collapsExpandAction  the action happened throw user selection on disease category header
     * @param forceUpdate force to update / redraw the heat-map
     */
    public void selectDiseaseCategory(Set<String> diseaseCategories, boolean collapsExpandAction, boolean forceUpdate) {

        if (!forceUpdate && !collapsExpandAction && updatedExpandedList != null && diseaseCategories != null && updatedExpandedList.containsAll(diseaseCategories) && updatedExpandedList.size() == diseaseCategories.size()) {
            return;
        }
        if (diseaseCategories == null) {
            updateExpandedHeaders(false);
            return;
        }
        collapsedRowheadersSet.clear();
        if (diseaseCategories.containsAll(fullDiseaseCategory)) {
            updateHeatMapLayout(fullRowheadersSet, fullColheadersSet, activePatientsGroupComparisonsSet, activeQuantDsMap);
            updateHMThumb(this.reDrawHeatMap(fullRowheadersSet.values(), fullColheadersSet.values()), activeHeatmapCellCount / 2, 0, equalComparisonMap, "All Diseases", collapsExpandAction);
        } else {
            Map<String, HeatMapHeaderCellInformationBean> rowheadersSet = new LinkedHashMap<>();
            Map<String, HeatMapHeaderCellInformationBean> colheadersSet = new LinkedHashMap<>();
            Set<DiseaseGroupComparison> patientsGroupComparisonsSet = new LinkedHashSet<>();

            for (HeatMapHeaderCellInformationBean cellInfoBean : fullRowheadersSet.values()) {
                if (diseaseCategories.contains(cellInfoBean.getDiseaseCategory())) {
                    String headerKey = cellInfoBean.getDiseaseCategory() + "-_-" + cellInfoBean.getDiseaseGroupFullName();
                    rowheadersSet.put(headerKey, cellInfoBean);
                } else {
                    if (!collapsedRowheadersSet.containsKey(cellInfoBean.getDiseaseCategory())) {
                        HeatMapHeaderCellInformationBean newCollapsedDiseaseCategory = new HeatMapHeaderCellInformationBean();
                        newCollapsedDiseaseCategory.setCollapseDiseseCategory(true);
                        newCollapsedDiseaseCategory.setDiseaseCategory(cellInfoBean.getDiseaseCategory());
                        newCollapsedDiseaseCategory.setDiseaseGroupFullName(cellInfoBean.getDiseaseCategory());
                        newCollapsedDiseaseCategory.setDiseaseGroupOreginalName(cellInfoBean.getDiseaseCategory());
                        newCollapsedDiseaseCategory.setDiseaseGroupName(cellInfoBean.getDiseaseCategory());
                        newCollapsedDiseaseCategory.setDiseaseHashedColor(cellInfoBean.getDiseaseHashedColor());
                        newCollapsedDiseaseCategory.setDiseaseStyleName(cellInfoBean.getDiseaseStyleName());
                        String headerKey = cellInfoBean.getDiseaseCategory() + "-_-" + cellInfoBean.getDiseaseGroupFullName();
                        rowheadersSet.put(headerKey, newCollapsedDiseaseCategory);
                        collapsedRowheadersSet.put(cellInfoBean.getDiseaseCategory(), newCollapsedDiseaseCategory);
                    }

                }

            }
            collapsedRowheadersSet.clear();
            for (HeatMapHeaderCellInformationBean cellInfoBean : fullColheadersSet.values()) {
                if (diseaseCategories.contains(cellInfoBean.getDiseaseCategory())) {
                    String headerKey = cellInfoBean.getDiseaseCategory() + "-_-" + cellInfoBean.getDiseaseGroupFullName();
                    colheadersSet.put(headerKey, cellInfoBean);
                } else {
                    if (!collapsedRowheadersSet.containsKey(cellInfoBean.getDiseaseCategory())) {
                        HeatMapHeaderCellInformationBean newCollapsedDiseaseCategory = new HeatMapHeaderCellInformationBean();
                        newCollapsedDiseaseCategory.setCollapseDiseseCategory(true);
                        newCollapsedDiseaseCategory.setDiseaseCategory(cellInfoBean.getDiseaseCategory());
                        newCollapsedDiseaseCategory.setDiseaseGroupFullName(cellInfoBean.getDiseaseCategory());
                        newCollapsedDiseaseCategory.setDiseaseGroupOreginalName(cellInfoBean.getDiseaseCategory());
                        newCollapsedDiseaseCategory.setDiseaseGroupName(cellInfoBean.getDiseaseCategory());
                        newCollapsedDiseaseCategory.setDiseaseHashedColor(cellInfoBean.getDiseaseHashedColor());
                        newCollapsedDiseaseCategory.setDiseaseStyleName(cellInfoBean.getDiseaseStyleName());
                        String headerKey = cellInfoBean.getDiseaseCategory() + "-_-" + cellInfoBean.getDiseaseGroupFullName();
                        colheadersSet.put(headerKey, newCollapsedDiseaseCategory);
                        collapsedRowheadersSet.put(cellInfoBean.getDiseaseCategory(), newCollapsedDiseaseCategory);
                    }

                }

            }
            Set<DiseaseGroupComparison> crossDiseaseGroupComparisonsSet = new LinkedHashSet<>();
            for (DiseaseGroupComparison comp : activePatientsGroupComparisonsSet) {
                if (comp.isCrossDisease()) {
                    crossDiseaseGroupComparisonsSet.add(comp);
                    continue;
                }
//else 

                if (collapsedRowheadersSet.containsKey(comp.getDiseaseCategoryI())) {
                    DiseaseGroupComparison diseaseGroup = new DiseaseGroupComparison();
                    diseaseGroup.setDiseaseCategory(comp.getDiseaseCategoryI(), comp.getDiseaseCategoryI());
                    diseaseGroup.setDiseaseStyleName(comp.getDiseaseStyleName());
                    diseaseGroup.setDiseaseMainGroupI(comp.getDiseaseCategoryI());
                    diseaseGroup.setCrossDisease(comp.isCrossDisease());
                    diseaseGroup.setOriginalDiseaseSubGroupI(comp.getDiseaseCategoryI());
                    diseaseGroup.setActiveDiseaseSubGroupI(comp.getDiseaseCategoryI());
                    diseaseGroup.setDiseaseMainGroupII(comp.getDiseaseCategoryI());
                    diseaseGroup.setOriginalDiseaseSubGroupII(comp.getDiseaseCategoryI());
                    diseaseGroup.setActiveDiseaseSubGroupII(comp.getDiseaseCategoryI());
                    diseaseGroup.setQuantDatasetIndex(comp.getQuantDatasetIndex());
                    patientsGroupComparisonsSet.add(diseaseGroup);
                    comp = diseaseGroup;
                }
                patientsGroupComparisonsSet.add(comp);
            }
            for (DiseaseGroupComparison comp : crossDiseaseGroupComparisonsSet) {

                if ((collapsedRowheadersSet.containsKey(comp.getDiseaseCategoryI()) && (collapsedRowheadersSet.containsKey(comp.getDiseaseCategoryII())))) {
                    patientsGroupComparisonsSet.add(comp);
                } else if ((collapsedRowheadersSet.containsKey(comp.getDiseaseCategoryI()))) {

                    DiseaseGroupComparison diseaseGroup = new DiseaseGroupComparison();
                    diseaseGroup.setDiseaseCategory(comp.getDiseaseCategoryI(), comp.getDiseaseCategoryII());
                    diseaseGroup.setDiseaseStyleName(comp.getDiseaseStyleName());
                    diseaseGroup.setDiseaseMainGroupI(comp.getDiseaseCategoryI());
                    diseaseGroup.setCrossDisease(comp.isCrossDisease());
                    diseaseGroup.setOriginalDiseaseSubGroupI(comp.getDiseaseCategoryI());
                    diseaseGroup.setActiveDiseaseSubGroupI(comp.getDiseaseCategoryI());
                    diseaseGroup.setDiseaseMainGroupII(comp.getDiseaseMainGroupII());
                    diseaseGroup.setOriginalDiseaseSubGroupII(comp.getOriginalDiseaseSubGroupII());
                    diseaseGroup.setActiveDiseaseSubGroupII(comp.getActiveDiseaseSubGroupII());
                    diseaseGroup.setQuantDatasetIndex(comp.getQuantDatasetIndex());
                    patientsGroupComparisonsSet.add(diseaseGroup);

                } else if ((collapsedRowheadersSet.containsKey(comp.getDiseaseCategoryII()))) {
                    DiseaseGroupComparison diseaseGroup = new DiseaseGroupComparison();
                    diseaseGroup.setDiseaseCategory(comp.getDiseaseCategoryI(), comp.getDiseaseCategoryII());
                    diseaseGroup.setDiseaseStyleName(comp.getDiseaseStyleName());
                    diseaseGroup.setDiseaseMainGroupII(comp.getDiseaseCategoryII());
                    diseaseGroup.setCrossDisease(comp.isCrossDisease());
                    diseaseGroup.setOriginalDiseaseSubGroupII(comp.getDiseaseCategoryII());
                    diseaseGroup.setActiveDiseaseSubGroupII(comp.getDiseaseCategoryII());
                    diseaseGroup.setDiseaseMainGroupI(comp.getDiseaseMainGroupI());
                    diseaseGroup.setOriginalDiseaseSubGroupI(comp.getOriginalDiseaseSubGroupI());
                    diseaseGroup.setActiveDiseaseSubGroupI(comp.getActiveDiseaseSubGroupI());
                    diseaseGroup.setQuantDatasetIndex(comp.getQuantDatasetIndex());
                    patientsGroupComparisonsSet.add(diseaseGroup);

                }

            }

            updateHeatMapLayout(rowheadersSet, colheadersSet, patientsGroupComparisonsSet, activeQuantDsMap);
            updatedExpandedList.removeAll(collapsedRowheadersSet.keySet());
            int selectionSize = updatedExpandedList.size();
            switch (selectionSize) {
                case 0:
                    updateHMThumb(this.reDrawHeatMap(rowheadersSet.values(), colheadersSet.values()), activeHeatmapCellCount / 2, 0, equalComparisonMap, "No Diseases", collapsExpandAction);
                    break;
                case 1:
                    updateHMThumb(this.reDrawHeatMap(rowheadersSet.values(), colheadersSet.values()), activeHeatmapCellCount / 2, 0, equalComparisonMap, updatedExpandedList.iterator().next(), collapsExpandAction);
                    break;
                case 4:
                    updateHMThumb(this.reDrawHeatMap(rowheadersSet.values(), colheadersSet.values()), activeHeatmapCellCount / 2, 0, equalComparisonMap, "All Diseases", collapsExpandAction);
                    break;
                default:
                    updateHMThumb(this.reDrawHeatMap(rowheadersSet.values(), colheadersSet.values()), activeHeatmapCellCount / 2, 0, equalComparisonMap, "Multiple Diseases", collapsExpandAction);
                    break;
            }

        }
        setCollapsedDiseaseCategories(collapsedRowheadersSet.keySet());

    }
    private HeaderCell[] tempColumnHeaderCells;
    private HeaderCell[] tempRowHeaderCells;

    /**
     *Get Set of expanded disease category
     * @return Set of expanded disease category
     */
    public Set<String> getUpdatedExpandedList() {
        return updatedExpandedList;
    }

    private void setCollapsedDiseaseCategories(Set<String> diseaseCategories) {
        this.diseaseCategoryHeadersSet.forEach((headerCell) -> {
            if (diseaseCategories.contains(headerCell.getDiseaseCategory())) {
                headerCell.setCollapsedHeader(true);
                headerCell.addStyleName("collapsedDCheadercell");
            } else {
                headerCell.setCollapsedHeader(false);
                headerCell.removeStyleName("collapsedDCheadercell");
            }
        });
    }

    /**
     * This method responsible for updating the heat-map layout based on updated
     * data
     *
     * @param rowheaders Set of header cell information for rows
     * @param colheaders Set of header cell information for columns
     * @param patientsGroupComparisonsSet Set of disease group comparisons
     * @param fullQuantDsMap Map of datasets and its indexes
     */
    private void updateHeatMapLayout(Map<String, HeatMapHeaderCellInformationBean> rowheaders, Map<String, HeatMapHeaderCellInformationBean> colheaders, Set<DiseaseGroupComparison> patientsGroupComparisonsSet, Map<Integer, QuantDataset> fullQuantDsMap) {//, Map<String, String> diseaseFullNameMap, ) {
        activeHeatmapCellCount = 0;
        currentDsIds.clear();
        //init columnHeaders  
        tempColumnHeaderCells = new HeaderCell[colheaders.size()];
        columnsColors = new String[colheaders.size()];
        int i = 0;
        for (HeatMapHeaderCellInformationBean cellInfo : colheaders.values()) {

//            HeatMapHeaderCellInformationBean cellInfo = (HeatMapHeaderCellInformationBean) colheaders.values().toArray()[i];
            String headerKey = cellInfo.getDiseaseCategory() + "-_-" + cellInfo.getDiseaseGroupFullName();
            HeaderCell headerCell = columnHeaderCells.get(headerKey);
            if (headerCell == null) {
                String title = cellInfo.getDiseaseGroupName().replace("ALS", "Amyotrophic Lateral Sclerosis");
                if (title.equalsIgnoreCase("")) {
                    title = "Not Available";
                }
                headerCell = new HeaderCell(null, title, cellInfo.getDiseaseGroupFullName(), cellInfo.getDiseaseCategory(), true) {

                    @Override
                    public void selectData(String valueLabel) {
                        if (this.isCollapsedHeader()) {
                            HeatMapLayout.this.activeQuantDsMap = HeatMapLayout.this.fullQuantDsMap;
                            HeatMapLayout.this.activePatientsGroupComparisonsSet = HeatMapLayout.this.fullPatientsGroupComparisonsSet;
                            collapsedRowheadersSet.remove(valueLabel.split("__")[0]);
                            updateExpandedHeaders(true);
                        } else {
                            addRowSelectedDs(valueLabel);
                        }
                    }

                    @Override
                    public void unSelectData(String valueLabel) {
                        if (!this.isCollapsedHeader()) { 
                            removeRowSelectedDs(valueLabel);
                        }
                    }
                };
                columnHeaderCells.put(headerKey, headerCell);
                headerCell.setCollapsedHeader(true);
            } else {
                headerCell.resetHeader();
                headerCell.setCollapsedHeader(cellInfo.isCollapseDiseseCategory());
            }
            columnsColors[i] = cellInfo.getDiseaseHashedColor();
            tempColumnHeaderCells[i++] = headerCell;
        }
//        init row headers
        tempRowHeaderCells = new HeaderCell[rowheaders.size()];
        rowsColors = new String[rowheaders.size()];
        i = 0;
        for (HeatMapHeaderCellInformationBean cellInfo : rowheaders.values()) {
//            HeatMapHeaderCellInformationBean cellInfo = (HeatMapHeaderCellInformationBean) rowheaders.values().toArray()[i];
            String headerKey = cellInfo.getDiseaseCategory() + "-_-" + cellInfo.getDiseaseGroupFullName();
            HeaderCell headerCell = rowHeaderCells.get(headerKey);
            if (headerCell == null) {
                String title = cellInfo.getDiseaseGroupName().replace("ALS", "Amyotrophic Lateral Sclerosis");
                if (title.equalsIgnoreCase("")) {
                    title = "Not Available";
                }
                headerCell = new HeaderCell(null, title, cellInfo.getDiseaseGroupFullName(), cellInfo.getDiseaseCategory(), false) {

                    @Override
                    public void selectData(String valueLabel) {
                        if (this.isCollapsedHeader()) {
                            HeatMapLayout.this.activeQuantDsMap = HeatMapLayout.this.fullQuantDsMap;
                            HeatMapLayout.this.activePatientsGroupComparisonsSet = HeatMapLayout.this.fullPatientsGroupComparisonsSet;
                            collapsedRowheadersSet.remove(valueLabel.split("__")[0]);
                            updateExpandedHeaders(true);
                        } else {
                            addRowSelectedDs(valueLabel);
                        }
                        System.out.println("at collapsed header invoked II " + valueLabel + "  " + this.isCollapsedHeader());
                    }

                    @Override
                    public void unSelectData(String valueLabel) {
                        if (!this.isCollapsedHeader()) {
                            removeRowSelectedDs(valueLabel);
                        }
                    }
                };
                rowHeaderCells.put(headerKey, headerCell);
                headerCell.setCollapsedHeader(true);
            } else {
                headerCell.resetHeader();
                headerCell.setCollapsedHeader(cellInfo.isCollapseDiseseCategory());
            }

            tempRowHeaderCells[i] = headerCell;
            rowsColors[i++] = cellInfo.getDiseaseHashedColor();

        }
        for (HeatmapCell[] cells : cellTable) {
            for (HeatmapCell cell : cells) {
                cell.resetCell();
            }
        }
        //init data
        calcHeatMapMatrix(rowheaders.values(), colheaders.values(), patientsGroupComparisonsSet, fullQuantDsMap);
        HeatmapColorGenerator hmColorGen = new HeatmapColorGenerator(maxDatasetNumber);
        comparisonsCellsMap.clear();
        currentDsCounter.clear();
        dataValuesColors = new String[rowheaders.size()][colheaders.size()];
        for (int x = 0; x < values.length; x++) {
            for (int y = 0; y < values[x].length; y++) {
                HeatMapHeaderCellInformationBean grI = (HeatMapHeaderCellInformationBean) rowheaders.values().toArray()[x];
                HeatMapHeaderCellInformationBean grII = (HeatMapHeaderCellInformationBean) colheaders.values().toArray()[y];
                String updatedComparisonTitle = grI.toString() + " / " + grII.toString();
                double value = values[x][y].getValue();
                String color = "#EFF2FB";

                if (!rowheaders.values().toArray()[x].toString().equalsIgnoreCase(colheaders.values().toArray()[y].toString())) {
                    color = hmColorGen.getColor((float) value);

                }
                if (grI.isCollapseDiseseCategory() && grII.isCollapseDiseseCategory() && value > 0) {
                    color = "#EFF2FB";
                }

                Map<Integer, QuantDataset> datasetMap = values[x][y].getDatasetMap();
                Set<String> pubCounter = new HashSet<>();
                datasetMap.values().forEach((ds) -> {
                    pubCounter.add(ds.getPubMedId());
                });

                String fullGrI = grI.getDiseaseGroupFullName();
                if (grI.getDiseaseGroupName().contains("*")) {
                    fullGrI = grI.toString();
                }
                String fullGrII = grII.getDiseaseGroupFullName();
                if (grII.getDiseaseGroupName().contains("*")) {
                    fullGrII = grII.toString();
                }
                String fullComparisonTitle = fullGrI + " / " + fullGrII;
                String orginalComparisonName = grI.getDiseaseGroupOreginalName() + " / " + grII.getDiseaseGroupOreginalName();
                String diseaseCategory = ((HeatMapHeaderCellInformationBean) rowheaders.values().toArray()[x]).getDiseaseCategory();
                if (!color.equalsIgnoreCase("#EFF2FB")) {
                    activeHeatmapCellCount += value;
                    currentDsIds.addAll(datasetMap.keySet());
                }
                HeatmapCell cell = cellTable[x][y];

                cell.updateHeatmapCell(collapsedRowheadersSet.containsKey(diseaseCategory), value, color, grI.getDiseaseHashedColor(), datasetMap, x, y, heatmapCellWidth, pubCounter.size(), updatedComparisonTitle, fullComparisonTitle, orginalComparisonName, diseaseCategory, ((HeatMapHeaderCellInformationBean) rowheaders.values().toArray()[x]).getDiseaseStyleName());

                comparisonsCellsMap.put(updatedComparisonTitle, cell);
                dataValuesColors[x][y] = color + "__" + ((int) cell.getValue() == 0 ? " " : "" + (int) cell.getValue());
                currentDsCounter.addAll(cell.getComparison().getDatasetMap().keySet());
                if (cell.getComparison().getDatasetMap().size() > 0) {
                    tempColumnHeaderCells[y].addComparison(cell.getComparison(), cell);
                    tempRowHeaderCells[x].addComparison(cell.getComparison(), cell);
                    if (!cell.getComparison().getOreginalComparisonHeader().trim().equalsIgnoreCase("/")) {
                        availableQuantDiseaseGroupsComparisonList.add(cell.getComparison());
                    }
                }

            }

        }
        equalComparisonMap.clear();
        comparisonsCellsMap.values().forEach((cell) -> {
            String kI = cell.getComparison().getComparisonHeader();
            String[] k1Arr = kI.split(" / ");
            if (!(k1Arr.length == 0)) {
                String kII = k1Arr[1] + " / " + k1Arr[0];
                if (comparisonsCellsMap.containsKey(kII)) {
                    equalComparisonMap.put(cell.getComparison(), comparisonsCellsMap.get(kII).getComparison());
                    equalComparisonMap.put(comparisonsCellsMap.get(kII).getComparison(), cell.getComparison());
                }
            }
        });

    }

    /**
     * This method responsible for updating heat map layout upon user selection
     * and update the selection manager
     *
     * @param cell Heat-map cell
     */
    protected void addSelectedDs(HeatmapCell cell) {
        if (selectedQuantDiseaseGroupsComparisonList.isEmpty()) {
            comparisonsCellsMap.values().stream().forEach((hmcell) -> {
                hmcell.unselect();
            });

        }
        for (HeaderCell header : columnHeaderCells.values()) {
            header.unselect();
        }
        for (HeaderCell header : rowHeaderCells.values()) {
            header.unselect();
        }
        if (singleSelection) {
            selectedCells.stream().forEach((tcell) -> {
                tcell.unselect();
            });
            selectedQuantDiseaseGroupsComparisonList.clear();
            selectedCells.clear();
        }

        this.selectedQuantDiseaseGroupsComparisonList.add(cell.getComparison());
        String kI = cell.getComparison().getComparisonHeader();
        String[] k1Arr = kI.split(" / ");
        String kII = k1Arr[1] + " / " + k1Arr[0];
        HeatmapCell equalCall = comparisonsCellsMap.get(kII);
        if (equalCall != null) {
            equalCall.select();
            this.selectedCells.add(equalCall);
            equalCall.getComparison().setSwitchable(true);
            cell.getComparison().setSwitchable(true);
        } else {
            cell.getComparison().setSwitchable(false);
        }
        cell.select();
        this.selectedCells.add(cell);

        updateSelectionManagerIndexes();

    }

    /**
     * This method is responsible for un-select comparison and update heat map
     * layout and update selection manager
     *
     * @param cell Heat-map cell
     */
    protected void removerSelectedDs(HeatmapCell cell) {
        for (HeaderCell header : columnHeaderCells.values()) {
            header.unselect();
        }
        for (HeaderCell header : rowHeaderCells.values()) {
            header.unselect();
        }
        this.selectedQuantDiseaseGroupsComparisonList.remove(cell.getComparison());
        this.selectedCells.remove(cell);
        String kI = cell.getComparison().getComparisonHeader();
        String[] k1Arr = kI.split(" / ");
        String kII = k1Arr[1] + " / " + k1Arr[0];
        HeatmapCell equalCall = comparisonsCellsMap.get(kII);
        if (equalCall != null) {
            equalCall.unselect();
            this.selectedQuantDiseaseGroupsComparisonList.remove(equalCall.getComparison());
            this.selectedCells.remove(equalCall);
        } else {
            System.out.println("at equal cell was null " + kII);
            equalCall = cell;
        }
        updateSelectionManagerIndexes();
        if (selectedCells.isEmpty()) {
            comparisonsCellsMap.values().stream().forEach((hmcell) -> {
                hmcell.resetCellStyle();
            });

        } else {
            cell.unselect();
            equalCall.unselect();

        }

    }

    /**
     * This method is responsible for selecting a row or column and update heat
     * map layout and update selection manager
     *
     * @param selectedheader HEader cell title
     */
    protected void addRowSelectedDs(String selectedheader) {
        if (selectedQuantDiseaseGroupsComparisonList.isEmpty()) {
            //reset all cells to transpairent
            comparisonsCellsMap.values().stream().forEach((hmcell) -> {
                hmcell.unselect();
            });

        }
        if (singleSelection) {
            selectedCells.stream().forEach((tcell) -> {
                tcell.unselect();
            });
            selectedQuantDiseaseGroupsComparisonList.clear();
            selectedCells.clear();
            for (HeaderCell header : columnHeaderCells.values()) {
               

                if (header.getValueLabel().equalsIgnoreCase(selectedheader)) {
                    header.getIncludedCells().stream().forEach((tcell) -> {
                        tcell.select();
                        String kI = tcell.getComparison().getComparisonHeader();
                        System.out.println("at kI "+kI);
                        String[] k1Arr = kI.split(" / ");
                        String kII = k1Arr[1] + " / " + k1Arr[0];
                        HeatmapCell equalCall = comparisonsCellsMap.get(kII);
                         System.out.println("at kI "+kI+"  "+kII);
                        if (equalCall != null) {
                            equalCall.unselect();
                            this.selectedQuantDiseaseGroupsComparisonList.remove(equalCall.getComparison());
                            this.selectedCells.remove(equalCall);
                        } else {
                            System.out.println("at equal cell was null " + kII);
                        }

                        if (equalCall != null) {
                            equalCall.select();
                            this.selectedCells.add(equalCall);
                            equalCall.getComparison().setSwitchable(true);
                            tcell.getComparison().setSwitchable(true);
                        } else {
                            tcell.getComparison().setSwitchable(false);
                        }

                    });
                    header.select();
                          
                    selectedCells.addAll(header.getIncludedCells());
                    header.getIncludedComparisons().stream().forEach((qdComp) -> {
                        String kI = qdComp.getComparisonHeader();
                  
                        if (kI.startsWith(selectedheader.split("__")[0] + " / ")) {
                            selectedQuantDiseaseGroupsComparisonList.add(qdComp);
                        }
                    });

                    continue;
                }
                header.unselect();
            }
            for (HeaderCell header : rowHeaderCells.values()) {
                if (header.getValueLabel().equalsIgnoreCase(selectedheader)) {
                    header.getIncludedCells().stream().forEach((tcell) -> {
                        tcell.select();
                        String kI = tcell.getComparison().getComparisonHeader();
                        String[] k1Arr = kI.split(" / ");
                        String kII = k1Arr[1] + " / " + k1Arr[0];
                        HeatmapCell equalCall = comparisonsCellsMap.get(kII);
                        if (equalCall != null) {
                            equalCall.unselect();
                            this.selectedQuantDiseaseGroupsComparisonList.remove(equalCall.getComparison());
                            this.selectedCells.remove(equalCall);
                        } else {
                            System.out.println("at equal cell was null " + kII);
                        }

                        if (equalCall != null) {
                            equalCall.select();
                            this.selectedCells.add(equalCall);
                            equalCall.getComparison().setSwitchable(true);
                            tcell.getComparison().setSwitchable(true);
                        } else {
                            tcell.getComparison().setSwitchable(false);
                        }

                    });
                    header.select();
                    System.out.println("at included cells "+header.getValueLabel()+"  "+ header.getIncludedCells().size());
                    selectedCells.addAll(header.getIncludedCells());
                    header.getIncludedComparisons().stream().forEach((qdComp) -> {
                        String kI = qdComp.getComparisonHeader();
                        if (kI.startsWith(selectedheader.split("__")[0] + " / ")) {
                            selectedQuantDiseaseGroupsComparisonList.add(qdComp);
                        }
                        System.out.println("at KI "+kI+"  "+selectedheader);
                    });

                    continue;
                }
                header.unselect();
            }

        } else {

            for (HeaderCell header : columnHeaderCells.values()) {
                
                if (header.getValueLabel().equalsIgnoreCase(selectedheader)) { 
                   
                    header.getIncludedCells().stream().forEach((tcell) -> {
                        tcell.select();
                        String kI = tcell.getComparison().getComparisonHeader();
                        String[] k1Arr = kI.split(" / ");
                        String kII = k1Arr[1] + " / " + k1Arr[0];
                        HeatmapCell equalCall = comparisonsCellsMap.get(kII);
                        
                   
                        if (equalCall != null) {
                            equalCall.unselect();
                            this.selectedQuantDiseaseGroupsComparisonList.remove(equalCall.getComparison());
                            this.selectedCells.remove(equalCall);
                        }

                        if (equalCall != null) {
                            equalCall.select();
                            this.selectedCells.add(equalCall);
                            equalCall.getComparison().setSwitchable(true);
                            tcell.getComparison().setSwitchable(true);
                        } else {
                            tcell.getComparison().setSwitchable(false);
                        }

                    });
                    header.select();
                    selectedCells.addAll(header.getIncludedCells());
                    header.getIncludedComparisons().stream().forEach((qdComp) -> {
                        String kI = qdComp.getComparisonHeader(); 
                        if (kI.startsWith(selectedheader + " / ")) {
                            selectedQuantDiseaseGroupsComparisonList.add(qdComp);
                        }
                    });
                    break;
                }
            }
            for (HeaderCell header : rowHeaderCells.values()) {
                if (header.getValueLabel().equalsIgnoreCase(selectedheader)) {
                    header.getIncludedCells().stream().forEach((tcell) -> {
                        tcell.select();

                        String kI = tcell.getComparison().getComparisonHeader();
                        String[] k1Arr = kI.split(" / ");
                        String kII = k1Arr[1] + " / " + k1Arr[0];
                        HeatmapCell equalCall = comparisonsCellsMap.get(kII);
                        if (equalCall != null) {
                            equalCall.unselect();
                            this.selectedQuantDiseaseGroupsComparisonList.remove(equalCall.getComparison());
                            this.selectedCells.remove(equalCall);
                        } else {
                            System.out.println("at equal cell was null " + kII);
                        }

                        if (equalCall != null) {
                            equalCall.select();
                            this.selectedCells.add(equalCall);
                            equalCall.getComparison().setSwitchable(true);
                            tcell.getComparison().setSwitchable(true);
                        } else {
                            tcell.getComparison().setSwitchable(false);
                        }

                    });
                    header.select();
                    selectedCells.addAll(header.getIncludedCells());
                    header.getIncludedComparisons().stream().forEach((qdComp) -> {
                        String kI = qdComp.getComparisonHeader();
                        if (kI.startsWith(selectedheader + " / ")) {
                            selectedQuantDiseaseGroupsComparisonList.add(qdComp);
                        }
                    });
                    break;
                }
            }

        }
        updateSelectionManagerIndexes();
    }

    /**
     * This method responsible for un-selecting a row or column and update heat
     * map layout and update selection manager
     *
     * @param selectedHeadercell Header cell title
     */
    protected void removeRowSelectedDs(String selectedHeadercell) {

        for (HeaderCell header : columnHeaderCells.values()) {
            if (header.getValueLabel().equalsIgnoreCase(selectedHeadercell)) {
                header.getIncludedCells().stream().forEach((tcell) -> {
                    tcell.unselect();
                });
                header.unselect();
                selectedCells.removeAll(header.getIncludedCells());
                selectedQuantDiseaseGroupsComparisonList.removeAll(header.getIncludedComparisons());
                break;
            }
        }
        for (HeaderCell header : rowHeaderCells.values()) {
            if (header.getValueLabel().equalsIgnoreCase(selectedHeadercell)) {
                header.getIncludedCells().stream().forEach((tcell) -> {
                    tcell.unselect();
                });
                header.unselect();
                selectedCells.removeAll(header.getIncludedCells());
                selectedQuantDiseaseGroupsComparisonList.removeAll(header.getIncludedComparisons());
                break;
            }
        }
        updateSelectionManagerIndexes();
        if (selectedQuantDiseaseGroupsComparisonList.isEmpty()) {
//            reset all cells to inital state
            comparisonsCellsMap.values().stream().forEach((hmcell) -> {
                hmcell.resetCellStyle();
            });
        }

    }

    /**
     * This method responsible for updating the disease categories top/left
     * labels
     *
     * @param rowheaders Set of header cell information for rows
     * @param colheaders Set of header cell information for columns
     */
    private void updateDiseaseCategoriesLabels(Collection<HeatMapHeaderCellInformationBean> rowheaders, Collection<HeatMapHeaderCellInformationBean> colheaders) {

        Map<String, TreeSet<Integer>> diseaseRowsMap = new LinkedHashMap<>();
        int index = 0;
        for (HeatMapHeaderCellInformationBean header : rowheaders) {
            String dName_dStyle = header.getDiseaseCategory() + "__" + header.getDiseaseStyleName();
            if (!diseaseRowsMap.containsKey(dName_dStyle)) {
                diseaseRowsMap.put(dName_dStyle, new TreeSet<>());
            }
            TreeSet<Integer> rowIndex = diseaseRowsMap.get(dName_dStyle);
            rowIndex.add(index);
            diseaseRowsMap.put(dName_dStyle, rowIndex);
            index++;

        }

        diseaseRowsMap.keySet().stream().map((dName) -> diseaseRowsMap.get(dName)).forEach((rowIndex) -> {
            int y = rowIndex.first();

            for (int x : rowIndex) {
                if (x == y) {
                    y++;
                } else {
                    resetLeftSideDiseaseCategoriesLabel = true;
                    break;

                }

            }
        });

        Map<String, TreeSet<Integer>> diseaseColumnMap = new LinkedHashMap<>();
        index = 0;

        for (HeatMapHeaderCellInformationBean header : colheaders) {
            String dName_dStyle = header.getDiseaseCategory() + "__" + header.getDiseaseStyleName();
            if (!diseaseColumnMap.containsKey(dName_dStyle)) {
                diseaseColumnMap.put(dName_dStyle, new TreeSet<>());
            }
            TreeSet<Integer> colIndex = diseaseColumnMap.get(dName_dStyle);
            colIndex.add(index);
            diseaseColumnMap.put(dName_dStyle, colIndex);
            index++;

        }

        for (String dName : diseaseColumnMap.keySet()) {
            TreeSet<Integer> rowIndex = diseaseColumnMap.get(dName);
            int y = rowIndex.first();
            for (int x : rowIndex) {
                if (x == y) {
                    y++;
                } else {
                    resetTopSideDiseaseCategoriesLabel = true;
                    break;

                }

            }

        }
    }
    private final Map<String, Set<Integer>> comparisonsNumberMap = new HashMap<>();

    /**
     * This method responsible for calculating the heat-map matrix values
     *
     * @param rowheaders Set of header cell information for rows
     * @param colheaders Set of header cell information for columns
     */
    private void calcHeatMapMatrix(Collection<HeatMapHeaderCellInformationBean> rowheaders, Collection<HeatMapHeaderCellInformationBean> colheaders, Set<DiseaseGroupComparison> patientsGroupComparisonsSet, Map<Integer, QuantDataset> fullQuantDsMap) {
        maxDatasetNumber = -1;
        values = new QuantDSIndexes[rowheaders.size()][colheaders.size()];
        comparisonsNumberMap.clear();
        for (DiseaseGroupComparison comp : patientsGroupComparisonsSet) {
            String keyI = comp.getActiveDiseaseSubGroupI() + "__" + comp.getDiseaseCategoryI();
            String keyII = comp.getActiveDiseaseSubGroupII() + "__" + comp.getDiseaseCategoryII();
            if (!comparisonsNumberMap.containsKey(keyI + "-_-" + keyII)) {
                comparisonsNumberMap.put(keyI + "-_-" + keyII, new HashSet<>());
                comparisonsNumberMap.put(keyII + "-_-" + keyI, new HashSet<>());
            }
            comparisonsNumberMap.get(keyI + "-_-" + keyII).add(comp.getQuantDatasetIndex());
            comparisonsNumberMap.get(keyII + "-_-" + keyI).add(comp.getQuantDatasetIndex());

        }
        for (int x = 0; x < rowheaders.size(); x++) {
            for (int y = 0; y < colheaders.size(); y++) {
                Set<Integer> value = comparisonsNumberMap.get(rowheaders.toArray()[x].toString() + "-_-" + colheaders.toArray()[y].toString()); //calcDsNumbers(rowheaders.toArray()[x].toString(), colheaders.toArray()[y].toString());
                if (value == null) {
                    value = new HashSet<>();
                }
                Map<Integer, QuantDataset> datasetMap = new LinkedHashMap<>();
                value.forEach((i) -> {
                    datasetMap.put(i, fullQuantDsMap.get(i));
                });
                QuantDSIndexes qDataset = new QuantDSIndexes();
                qDataset.setValue(value.size());
                qDataset.setDatasetMap(datasetMap);
                boolean ignor = rowheaders.toArray()[x].toString().equalsIgnoreCase(colheaders.toArray()[y].toString());
                values[x][y] = qDataset;
                if (!ignor && value.size() > maxDatasetNumber) {
                    maxDatasetNumber = value.size();
                }
            }

        }
    }

    /**
     * This method responsible for calculating the available datasets number
     *
     * @param PGI Disease sub group 1
     * @param PGII Disease sub group 2
     * @param patientsGroupComparisonsSet Set of disease group comparisons
     * @return Set of dataset indexes
     */
    private Set<Integer> calcDsNumbers(String PGI, String PGII) {
        Set<Integer> indexes = new HashSet<>();
        String key = PGI.split("__")[0];
        String diseaseGroupII = PGII.split("__")[0];

        return indexes;
    }

    /**
     * This method responsible filtering the user selection in the heat map and
     * remove duplicates before updating the central selection manager.
     */
    private void updateSelectionManagerIndexes() {
        Map<String, QuantDiseaseGroupsComparison> filteredComp = new LinkedHashMap<>();
        selectedQuantDiseaseGroupsComparisonList.stream().forEach((comp) -> {
            String kI = comp.getComparisonHeader();
            String[] k1Arr = kI.split(" / ");
            String kII = k1Arr[1] + " / " + k1Arr[0];
            if (!(filteredComp.containsKey(kI) || filteredComp.containsKey(kII))) {
                filteredComp.put(kI, comp);

            }
        });
        Set<QuantDiseaseGroupsComparison> filteredSelectedDsList = new LinkedHashSet<>();
        filteredSelectedDsList.addAll(filteredComp.values());
        updateSelectionManager(filteredSelectedDsList);
    }

    /**
     * Update the central selection manager
     *
     * @param selectedQuantDiseaseGroupsComparisonList the final selected quant
     * disease groups comparison.
     */
    public void updateSelectionManager(Set<QuantDiseaseGroupsComparison> selectedQuantDiseaseGroupsComparisonList) {
        ///to be overided
    }

    /**
     * Get set of all quant disease groups comparisons available in the heat map
     *
     * @return availableQuantDiseaseGroupsComparisonList
     */
    public Set<QuantDiseaseGroupsComparison> getAvailableQuantDiseaseGroupsComparisonList() {
        return availableQuantDiseaseGroupsComparisonList;
    }

    /**
     * Select all quant disease group comparisons in the datasets.
     */
    public void selectAll() {
        comparisonsCellsMap.values().stream().filter((cell) -> (cell.isVisible() && cell.getValue() != 0 && !cell.getComparison().getComparisonHeader().trim().equalsIgnoreCase("/") && availableQuantDiseaseGroupsComparisonList.contains(cell.getComparison()))).map((cell) -> {

            cell.select();
            return cell;
        }).forEach((cell) -> {
            String kI = cell.getComparison().getComparisonHeader();
            String[] k1Arr = kI.split(" / ");
            String kII = k1Arr[1] + " / " + k1Arr[0];
            HeatmapCell equalCall = comparisonsCellsMap.get(kII);
            if (equalCall != null) {
                equalCall.select();
                cell.getComparison().setSwitchable(true);
                equalCall.getComparison().setSwitchable(true);
            } else {
                cell.getComparison().setSwitchable(false);
            }
            selectedCells.add(cell);
            selectedQuantDiseaseGroupsComparisonList.add(cell.getComparison());
        });
        updateSelectionManagerIndexes();
    }

    /**
     * Un-select all quant disease group comparisons in the datasets
     */
    public void unselectAll() {
        clearSelection();
        updateSelectionManagerIndexes();

    }

    /**
     * Reset selection on dataset layout no selection manager update.
     */
    public void clearSelection() {
        if (columnHeaderCells == null || fullColheadersSet.isEmpty()) {
            return;
        }
        for (HeaderCell header : columnHeaderCells.values()) {
            header.unselect();
        }
        for (HeaderCell header : rowHeaderCells.values()) {
            header.unselect();
        }

        selectedCells.clear();
        comparisonsCellsMap.values().stream().forEach((cell) -> {
            cell.resetCellStyle();
        });
        selectedQuantDiseaseGroupsComparisonList.clear();
    }

    /**
     * Reset selection on dataset layout no selection manager update
     *
     * @param comparisonsToSelect Set of quant disease comparisons.
     */
    public void selectComparisons(Set<QuantDiseaseGroupsComparison> comparisonsToSelect) {
        selectedCells.clear();
        selectedQuantDiseaseGroupsComparisonList.clear();
        comparisonsCellsMap.values().stream().forEach((cell) -> {
            if (comparisonsToSelect.contains(cell.getComparison())) {
                this.selectedQuantDiseaseGroupsComparisonList.add(cell.getComparison());
                this.selectedCells.add(cell);
                String kI = cell.getComparison().getComparisonHeader();
                String[] k1Arr = kI.split(" / ");
                String kII = k1Arr[1] + " / " + k1Arr[0];
                HeatmapCell equalCall = comparisonsCellsMap.get(kII);
                if (equalCall != null) {
                    equalCall.select();
                    this.selectedCells.add(equalCall);
                    cell.getComparison().setSwitchable(true);
                    equalCall.getComparison().setSwitchable(true);
                } else {
                    cell.getComparison().setSwitchable(false);
                }
                cell.select();
                this.selectedCells.add(cell);

            }

        });

    }

    /**
     * This method to generate heat map Thumb image on updating.
     *
     * @return URL for image encode as 64Based string
     */
    private String reDrawHeatMap(Collection<HeatMapHeaderCellInformationBean> rowheaders, Collection<HeatMapHeaderCellInformationBean> colheaders) {
        gen.generateHeatmap(rowheaders, colheaders, dataValuesColors, availableHMWidth, availableHMHeight, resetLeftSideDiseaseCategoriesLabel, resetTopSideDiseaseCategoriesLabel, false);
        heatmapPanelLayout.setWidth(gen.getPanelWidth(), Unit.PIXELS);
        heatmapPanelLayout.setHeight(gen.getPanelHeight(), Unit.PIXELS);
        heatMapContainerPanel.setHeight(Math.min(this.availableHMHeight - 40, gen.getPanelHeight() + 15), Unit.PIXELS);
        if (this.availableHMWidth >= gen.getPanelWidth() + 40) {
            heatMapContainerPanel.setWidth(gen.getPanelWidth() + 25, Unit.PIXELS);
        } else {
            heatMapContainerPanel.setWidth(availableHMWidth - 40, Unit.PIXELS);
        }
        controlsLayout.setWidth(heatMapContainerPanel.getWidth(), heatMapContainerPanel.getWidthUnits());
        controlsLayout.markAsDirty();
        updateHeatmapComponents();
        return gen.generateHeatmap(rowheaders, colheaders, dataValuesColors, 100, 100, resetLeftSideDiseaseCategoriesLabel, resetTopSideDiseaseCategoriesLabel, false);

    }

    /**
     * This method to select all disease group comparisons related to selected
     * disease category
     *
     * @param diseaseCategoryName disease category name (AD,MS or PD...etc)
     */
    private void selectComparisonsDiseaseCategory(String diseaseCategoryName) {

        if (singleSelection) {
            selectedCells.stream().forEach((tcell) -> {
                tcell.unselect();
            });
            selectedQuantDiseaseGroupsComparisonList.clear();
            selectedCells.clear();

        }
        for (HeaderCell rowHeaderCell : rowHeaderCells.values()) {
            if (rowHeaderCell.getDiseaseCategory().equalsIgnoreCase(diseaseCategoryName)) {
                rowHeaderCell.select();
            } else if (singleSelection) {
                rowHeaderCell.unselect();
            }

        }
        for (HeaderCell colHeaderCell : columnHeaderCells.values()) {
            if (colHeaderCell.getDiseaseCategory().equalsIgnoreCase(diseaseCategoryName)) {
                colHeaderCell.select();
            } else if (singleSelection) {
                colHeaderCell.unselect();
            }

        }

        comparisonsCellsMap.values().stream().filter((cell) -> (cell.getDiseaseCategory().equalsIgnoreCase(diseaseCategoryName) && cell.isVisible() && cell.getValue() != 0 && !cell.getComparison().getComparisonHeader().trim().equalsIgnoreCase("/") && availableQuantDiseaseGroupsComparisonList.contains(cell.getComparison()))).map((cell) -> {

            cell.select();
            return cell;
        }).forEach((cell) -> {
            String kI = cell.getComparison().getComparisonHeader();
            String[] k1Arr = kI.split(" / ");
            String kII = k1Arr[1] + " / " + k1Arr[0];
            HeatmapCell equalCall = comparisonsCellsMap.get(kII);
            if (equalCall != null) {
                equalCall.select();
                cell.getComparison().setSwitchable(true);
                equalCall.getComparison().setSwitchable(true);
            } else {
                cell.getComparison().setSwitchable(false);
            }
            selectedCells.add(cell);
            selectedQuantDiseaseGroupsComparisonList.add(cell.getComparison());
        });
        updateSelectionManagerIndexes();

    }

    /**
     * This method to update the location of heat map headers and cells on
     * updating the data.
     *
     */
    private void updateHeatmapComponents() {
        int calcWidth = 0, calcHeight = 0;
        this.heatmapComponentContainer.removeAllComponents();
        this.diseaseCategoryHeadersSet.clear();

        cornerCell.setWidth((int) (150 * gen.getResizeFactor()), Unit.PIXELS);
        cornerCell.setHeight(cornerCell.getWidth(), cornerCell.getWidthUnits());

        double resizeFilterBtn = 1;
        if (cornerCell.getWidth() < 115) {
            resizeFilterBtn = cornerCell.getWidth() / 115;

        }
        filterResizeController.resizeFilters(resizeFilterBtn);

        this.heatmapComponentContainer.addComponent(cornerCell, "left: 0px; top: 0px");

        Map<String, Rectangle> headerLabelMap = gen.getHeaderLabelMap();
        int i = (int) cornerCell.getWidth(), j = (int) cornerCell.getHeight();
        for (String dcat : headerLabelMap.keySet()) {
            String title = dcat.split("__")[1].replace("ALS", "Amyotrophic Lateral Sclerosis");;
            String shortName = title.replace("Alzheimer's", "AD").replace("Multiple Sclerosis", "MS").replace("Parkinson's", "PD").replace("Amyotrophic Lateral Sclerosis", "ALS");
            if (dcat.contains("col__")) {
                HeaderCell headerLabel = new HeaderCell(shortName, title, title, title, false) {

                    @Override
                    public void selectData(String diseaseCategoryName) {
                        HeatMapLayout.this.activeQuantDsMap = HeatMapLayout.this.fullQuantDsMap;
                        HeatMapLayout.this.activePatientsGroupComparisonsSet = HeatMapLayout.this.fullPatientsGroupComparisonsSet;
                        if (this.isCollapsedHeader()) {
                            collapsedRowheadersSet.remove(diseaseCategoryName.split("__")[0]);
                            updateExpandedHeaders(true);

                        } else {
                            collapsedRowheadersSet.put(diseaseCategoryName.split("__")[0], null);
                            updateExpandedHeaders(true);
                        }
                    }

                    @Override
                    public void unSelectData(String cellHeader) {
                        System.out.println("at un selected header " + cellHeader + "  " + this.isCollapsedHeader());
                    }
                };
                headerLabel.addStyleName("pointer");
                headerLabel.setWidth((int) (headerLabelMap.get(dcat).width - 2), Unit.PIXELS);
                headerLabel.setHeight((int) (headerLabelMap.get(dcat).height), Unit.PIXELS);

                this.heatmapComponentContainer.addComponent(headerLabel, "left: " + (i + 1) + "px; top: " + 0 + "px");
                i += headerLabel.getWidth() + 2;
                this.diseaseCategoryHeadersSet.add(headerLabel);

            } else {
                HeaderCell headerLabel = new HeaderCell(shortName, title, title, title, true) {

                    @Override
                    public void selectData(String diseaseCategoryName) {
                        HeatMapLayout.this.activeQuantDsMap = HeatMapLayout.this.fullQuantDsMap;
                        HeatMapLayout.this.activePatientsGroupComparisonsSet = HeatMapLayout.this.fullPatientsGroupComparisonsSet;
                        if (this.isCollapsedHeader()) {
                            collapsedRowheadersSet.remove(diseaseCategoryName.split("__")[0]);
                            updateExpandedHeaders(true);

                        } else {
                            collapsedRowheadersSet.put(diseaseCategoryName.split("__")[0], null);
                            updateExpandedHeaders(true);
                        }
                    }

                    @Override
                    public void unSelectData(String cellHeader) {
                    }
                };
                headerLabel.addStyleName("pointer");
                headerLabel.addStyleName("topbottomborder");
                headerLabel.setWidth((int) (headerLabelMap.get(dcat).width) + 2, Unit.PIXELS);
                headerLabel.setHeight((int) (headerLabelMap.get(dcat).height), Unit.PIXELS);
                this.heatmapComponentContainer.addComponent(headerLabel, "left: " + (0) + "px; top: " + (j) + "px");
                j += headerLabel.getHeight();
                this.diseaseCategoryHeadersSet.add(headerLabel);

            }
        }

        int x = (int) (150 * gen.getResizeFactor());
        int y = (int) (20 * gen.getResizeFactor());
        int cellWidth = (int) (20 * gen.getResizeFactor());

        for (HeaderCell headerCell : tempColumnHeaderCells) {
            headerCell.setHeight((int) (130 * gen.getResizeFactor()), Unit.PIXELS);
            headerCell.setWidth(cellWidth, Unit.PIXELS);
            headerCell.addStyleName("heatmapcell");

            this.heatmapComponentContainer.addComponent(headerCell, "left: " + x + "px; top: " + y + "px");
            x += cellWidth;

        }
        calcWidth += x;
        y = (int) (150 * gen.getResizeFactor());
        x = (int) (20 * gen.getResizeFactor());
        for (HeaderCell headerCell : tempRowHeaderCells) {
            headerCell.setWidth((int) (130 * gen.getResizeFactor()), Unit.PIXELS);
            headerCell.setHeight(cellWidth, Unit.PIXELS);
            headerCell.addStyleName("heatmapcell");

            this.heatmapComponentContainer.addComponent(headerCell, "left: " + x + "px; top: " + y + "px");
            y += cellWidth;

        }
        calcHeight += y;

        x = (int) (150 * gen.getResizeFactor());
        y = (int) (150 * gen.getResizeFactor());
        for (HeatmapCell[] row : cellTable) {
            for (HeatmapCell cell : row) {
                cell.setWidth(cellWidth, Unit.PIXELS);
                cell.setHeight(cellWidth, Unit.PIXELS);
                cell.addStyleName("heatmapcell");
                this.heatmapComponentContainer.addComponent(cell, "left: " + x + "px; top: " + y + "px");
                x += cellWidth;
            }
            x = (int) (150 * gen.getResizeFactor());
            y += cellWidth;
        }

        heatmapPanelLayout.setHeight(calcHeight, Unit.PIXELS);
        heatmapPanelLayout.setWidth(calcWidth, Unit.PIXELS);

    }
    private final Set<String> updatedExpandedList = new LinkedHashSet<>();

    private void updateExpandedHeaders(boolean collapsExpandAction) {
        updatedExpandedList.clear();
        for (HeatMapHeaderCellInformationBean diseaseCat : fullColheadersSet.values()) {
            if (!collapsedRowheadersSet.containsKey(diseaseCat.getDiseaseCategory())) {
                updatedExpandedList.add(diseaseCat.getDiseaseCategory());
            }
        }
        selectDiseaseCategory(updatedExpandedList, collapsExpandAction, false);
    }

    /**
     * Update heat map Thumb button on updating the heat map thumb image and
     * number of datasets
     *
     * @param imgUrl URL for encode 64Based string image
     * @param datasetNumber Number of datasets
     * @param deactivatedDatasetNumber Number of filtered datasets
     * @param equalComparisonMap The map of equal heat map cell to the flipped
     * comparisons
     * @param selectedDiseaseCategory selected disease category
     * @param expandCollapsAction  the action happened throw user selection on disease category header
     */
    public abstract void updateHMThumb(String imgUrl, int datasetNumber, int deactivatedDatasetNumber, Map<QuantDiseaseGroupsComparison, QuantDiseaseGroupsComparison> equalComparisonMap, String selectedDiseaseCategory, boolean expandCollapsAction);

}
