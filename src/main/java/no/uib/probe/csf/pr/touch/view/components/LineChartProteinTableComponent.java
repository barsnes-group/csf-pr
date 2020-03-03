package no.uib.probe.csf.pr.touch.view.components;

import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.QuantComparisonProtein;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFListener;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFPR_Central_Manager;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFSelection;
import no.uib.probe.csf.pr.touch.view.components.datasetfilters.GroupSwitchBtn;
import no.uib.probe.csf.pr.touch.view.components.linechartproteintablecomponents.ExportProteinTable;
import no.uib.probe.csf.pr.touch.view.components.linechartproteintablecomponents.FilterColumnButton;
import no.uib.probe.csf.pr.touch.view.components.linechartproteintablecomponents.ProteinTable;
import no.uib.probe.csf.pr.touch.view.core.CloseButton;
import no.uib.probe.csf.pr.touch.view.core.ImageContainerBtn;
import no.uib.probe.csf.pr.touch.view.core.InformationButton;
import no.uib.probe.csf.pr.touch.view.core.SearchingField;
import no.uib.probe.csf.pr.touch.view.core.TrendLegend;

/**
 * This class represents both protein table that include line chart component
 * the protein line chart represents the overall protein trend across different
 * comparisons
 *
 * @author Yehia Farag
 */
public abstract class LineChartProteinTableComponent extends VerticalLayout implements CSFListener {

    /**
     * The central manager for handling data across different visualizations and
     * managing all users selections.
     */
    private final CSFPR_Central_Manager CSFPR_Central_Manager;
    /**
     * The protein table right side control buttons container.
     */
    private final VerticalLayout proteinTableToolsContainer;
    /**
     * The protein table that has protein information.
     */
    private final ProteinTable quantProteinTable;
    /**
     * Map of protein accession and protein name with protein item inside the
     * table to allow users search inside table using name or accession.
     */
    private final Map<String, QuantComparisonProtein> proteinSearchingMap;
    /**
     * Remove any protein table applied filter button.
     */
    private final ImageContainerBtn removeFiltersBtn;
    /**
     * Switch between sorting and filtering icons in the protein table
     * comparisons headers button.
     */
    private final FilterColumnButton filterSortSwichBtn;

    /**
     * Constructor to initialise the main attributes ( selection manage ..etc).
     *
     * @param CSFPR_Central_Manager Central selection manager
     * @param width main body layout width (the container)
     * @param height main body layout height (the container)
     */
    public LineChartProteinTableComponent(CSFPR_Central_Manager CSFPR_Central_Manager, int width, int height) {

        this.CSFPR_Central_Manager = CSFPR_Central_Manager;
        this.proteinSearchingMap = new HashMap<>();

        LineChartProteinTableComponent.this.setWidth(width, Unit.PIXELS);
        LineChartProteinTableComponent.this.setHeight(height, Unit.PIXELS);

        VerticalLayout bodyContainer = new VerticalLayout();
        bodyContainer.setWidth(100, Unit.PERCENTAGE);
        bodyContainer.setHeightUndefined();
        bodyContainer.setSpacing(true);
        LineChartProteinTableComponent.this.addComponent(bodyContainer);

        //init toplayout
        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setHeight(25, Unit.PIXELS);
        topLayout.setWidth(100, Unit.PERCENTAGE);
        topLayout.setSpacing(true);
        topLayout.setMargin(new MarginInfo(false, false, false, true));
        bodyContainer.addComponent(topLayout);

        HorizontalLayout titleLayoutWrapper = new HorizontalLayout();
        titleLayoutWrapper.setHeight(25, Unit.PIXELS);
        titleLayoutWrapper.setWidth(213, Unit.PIXELS);
        titleLayoutWrapper.setSpacing(true);

        titleLayoutWrapper.setMargin(false);
        titleLayoutWrapper.addStyleName("margintop7");
        topLayout.addComponent(titleLayoutWrapper);
        topLayout.setExpandRatio(titleLayoutWrapper, 250);

        Label overviewLabel = new Label("Proteins");
        overviewLabel.setStyleName(ValoTheme.LABEL_BOLD);
        overviewLabel.addStyleName(ValoTheme.LABEL_SMALL);
        overviewLabel.addStyleName(ValoTheme.LABEL_TINY);

        overviewLabel.setWidth(55, Unit.PIXELS);
        titleLayoutWrapper.addComponent(overviewLabel);
        titleLayoutWrapper.setComponentAlignment(overviewLabel, Alignment.TOP_LEFT);
        titleLayoutWrapper.setExpandRatio(overviewLabel, 55);

        SearchingField searchingFieldLayout = new SearchingField() {

            @Override
            public void textChanged(String text) {
                quantProteinTable.filterViewItemTable(getSearchingProteinsList(text));
                this.updateLabel("(" + quantProteinTable.getRowsNumber() + ")");

            }

        };
        titleLayoutWrapper.addComponent(searchingFieldLayout);
        titleLayoutWrapper.setComponentAlignment(searchingFieldLayout, Alignment.TOP_LEFT);
        titleLayoutWrapper.setExpandRatio(searchingFieldLayout, 166);

        TrendLegend legendLayout = new TrendLegend("linechart");
        legendLayout.setWidthUndefined();
        if (width - 260 < 860) {
            CloseButton closeBtn = new CloseButton();
            VerticalLayout legendPopup = new VerticalLayout();
            legendPopup.addComponent(closeBtn);
            legendPopup.setExpandRatio(closeBtn, 1);
            Set<Component> set = new LinkedHashSet<>();
            VerticalLayout spacer = new VerticalLayout();
            spacer.setHeight(5, Unit.PIXELS);
            spacer.setWidth(20, Unit.PIXELS);
            set.add(spacer);
            Iterator<Component> itr = legendLayout.iterator();
            while (itr.hasNext()) {
                set.add(itr.next());
            }

            for (Component c : set) {
                legendPopup.addComponent(c);
                legendPopup.setExpandRatio(c, c.getHeight() + 5);
            }

            legendPopup.setWidth(230, Unit.PIXELS);
            legendPopup.setHeight(150, Unit.PIXELS);
            final PopupView popup = new PopupView("Legend", legendPopup);
            legendPopup.addStyleName("compactlegend");
            popup.addStyleName("marginright20");
            popup.setHideOnMouseOut(false);
            closeBtn.addLayoutClickListener((LayoutEvents.LayoutClickEvent event) -> {
                popup.setPopupVisible(false);

            });
            topLayout.addComponent(popup);
            topLayout.setComponentAlignment(popup, Alignment.MIDDLE_RIGHT);
            topLayout.setExpandRatio(popup, width - 260);
        } else {
            legendLayout.setHeight(25, Unit.PIXELS);
            legendLayout.addStyleName("margintop10");
            topLayout.addComponent(legendLayout);
            topLayout.setComponentAlignment(legendLayout, Alignment.TOP_RIGHT);
            topLayout.setExpandRatio(legendLayout, width - 220);
        }

        //end of toplayout
        //start chart layout
        VerticalLayout tableLayoutFrame = new VerticalLayout();
        height = height - 24;

        width = width - 50;
        tableLayoutFrame.setWidth(width, Unit.PIXELS);
        tableLayoutFrame.setHeightUndefined();
        tableLayoutFrame.addStyleName("roundedborder");
        tableLayoutFrame.addStyleName("whitelayout");
        tableLayoutFrame.addStyleName("paddingtop25");
        tableLayoutFrame.addStyleName("paddingleft10");
        tableLayoutFrame.addStyleName("paddingbottom10");
        bodyContainer.addComponent(tableLayoutFrame);
        bodyContainer.setComponentAlignment(tableLayoutFrame, Alignment.MIDDLE_CENTER);
        height = height - 60;
        width = width - 50;

        quantProteinTable = new ProteinTable(width, height) {

            @Override
            public void dropComparison(QuantDiseaseGroupsComparison comparison) {
                Set<QuantDiseaseGroupsComparison> updatedComparisonList = CSFPR_Central_Manager.getSelectedComparisonsList();
                updatedComparisonList.remove(comparison);
                CSFSelection selection = new CSFSelection("comparisons_selection", getListenerId(), updatedComparisonList, null);
                CSFPR_Central_Manager.setSelection(selection);

            }

            @Override
            public void selectProtein(String selectedProtein, int custTrend) {
                CSFSelection selection = new CSFSelection("peptide_selection", getListenerId(), null, null);
                selection.setSelectedProteinAccession(selectedProtein);
                selection.setCustProteinSelectionTrend(custTrend);
                CSFPR_Central_Manager.setSelection(selection);

            }

            @Override
            public void updateIconRowNumber(int rowNumber, String url) {
                searchingFieldLayout.updateLabel("(" + rowNumber + ")");
                LineChartProteinTableComponent.this.updateThumbIconRowNumber(rowNumber, url);
            }

        };
        tableLayoutFrame.addComponent(quantProteinTable);
        tableLayoutFrame.setComponentAlignment(quantProteinTable, Alignment.MIDDLE_CENTER);

        HorizontalLayout controlsLayout = new HorizontalLayout();
        controlsLayout.setWidth(100, Unit.PERCENTAGE);
        controlsLayout.setHeight(20, Unit.PIXELS);

        Label clickcommentLabel = new Label("Click a row to select data");
        clickcommentLabel.setStyleName(ValoTheme.LABEL_SMALL);
        clickcommentLabel.addStyleName(ValoTheme.LABEL_TINY);
        clickcommentLabel.addStyleName("italictext");
        clickcommentLabel.setWidth(182, Unit.PIXELS);

        controlsLayout.addComponent(clickcommentLabel);
        controlsLayout.setComponentAlignment(clickcommentLabel, Alignment.BOTTOM_RIGHT);
        tableLayoutFrame.addComponent(controlsLayout);

        //init side control btns layout 
        proteinTableToolsContainer = new VerticalLayout();
        proteinTableToolsContainer.setHeightUndefined();
        proteinTableToolsContainer.setWidthUndefined();
        proteinTableToolsContainer.setSpacing(true);

        GroupSwitchBtn groupSwichBtn = new GroupSwitchBtn() {
            @Override
            public Set<QuantDiseaseGroupsComparison> getUpdatedComparsionList() {
                return CSFPR_Central_Manager.getSelectedComparisonsList();
            }

            @Override
            public void updateComparisons(LinkedHashSet<QuantDiseaseGroupsComparison> updatedComparisonList) {

                CSFSelection selection = new CSFSelection("comparisons_selection_update", getListenerId(), updatedComparisonList, null);
                CSFPR_Central_Manager.setSelection(selection);

            }

            @Override
            public Map<QuantDiseaseGroupsComparison, QuantDiseaseGroupsComparison> getEqualComparsionMap() {
                return CSFPR_Central_Manager.getEqualComparisonMap();
            }

        };

        proteinTableToolsContainer.addComponent(groupSwichBtn);
        proteinTableToolsContainer.setComponentAlignment(groupSwichBtn, Alignment.MIDDLE_CENTER);
        ExportProteinTable exportTable = new ExportProteinTable();
        proteinTableToolsContainer.addComponent(exportTable);
        removeFiltersBtn = new ImageContainerBtn() {
            @Override
            public void onClick() {
                quantProteinTable.clearColumnFilters();
            }

            @Override
            public void setEnabled(boolean enabled) {
                if (enabled) {
                    this.removeStyleName("unapplied");
                } else {
                    this.addStyleName("unapplied");
                }
                super.setEnabled(enabled);
            }
        };
        removeFiltersBtn.setEnabled(false);
        removeFiltersBtn.setHeight(40, Unit.PIXELS);
        removeFiltersBtn.setWidth(40, Unit.PIXELS);
        removeFiltersBtn.addStyleName("midimg");
        removeFiltersBtn.updateIcon(new ThemeResource("img/filter_disables.png"));
        proteinTableToolsContainer.addComponent(removeFiltersBtn);
        proteinTableToolsContainer.setComponentAlignment(removeFiltersBtn, Alignment.MIDDLE_CENTER);
        removeFiltersBtn.setDescription("Clear all applied filters");
        filterSortSwichBtn = new FilterColumnButton() {
            @Override
            public void onClickFilter(boolean isFilter) {
                removeFiltersBtn.setEnabled(isFilter);
                quantProteinTable.switchHeaderBtns();
            }
        };
        proteinTableToolsContainer.addComponent(filterSortSwichBtn);
        proteinTableToolsContainer.setComponentAlignment(filterSortSwichBtn, Alignment.MIDDLE_CENTER);

        ImageContainerBtn exportPdfBtn = new ImageContainerBtn() {
            @Override
            public void onClick() {
                exportTable.setUserCustomizedComparison(quantProteinTable.getUserCustomizedComparison());
                exportTable.updateTableData(quantProteinTable.getSelectedComparisonsList(), quantProteinTable.getSelectedProteinsList(), quantProteinTable.isAscendingSort());
                ExcelExport csvExport = new ExcelExport(exportTable.getExportTable(), "CSF-PR  Protein Information");
                csvExport.setReportTitle("CSF-PR / Quant Protein Information ");
                csvExport.setExportFileName("CSF-PR - Quant Protein Information" + ".xls");
                csvExport.setMimeType(ExcelExport.EXCEL_MIME_TYPE);
                csvExport.setDisplayTotals(false);
                csvExport.setDateDataFormat("0");
                csvExport.setExcelFormatOfProperty("Index", "0");
                csvExport.export();
            }
        };
        exportPdfBtn.setHeight(40, Unit.PIXELS);
        exportPdfBtn.setWidth(40, Unit.PIXELS);
        exportPdfBtn.updateIcon(new ThemeResource("img/xls-text-o-2.png"));
        exportPdfBtn.setEnabled(true);
        proteinTableToolsContainer.addComponent(exportPdfBtn);
        proteinTableToolsContainer.setComponentAlignment(exportPdfBtn, Alignment.MIDDLE_CENTER);
        exportPdfBtn.setDescription("Export table");
        proteinTableToolsContainer.addComponent(exportPdfBtn);
        InformationButton info = new InformationButton("The protein table provides an overview of the quantitative information available for each protein, classified into Increased, Decreased or Equal. If the quantitative data for a given comparison is not exclusively in the same direction an average value will be shown. To find proteins of interest use the search field at the top, or sort/filter on the individual comparisons using the options above the table. The icons at the lower right enables further modification of the table. Select a row in the table to show the protein details.", false);
        proteinTableToolsContainer.addComponent(info);
        CSFPR_Central_Manager.registerListener(LineChartProteinTableComponent.this);
    }

    /**
     * Selection changed in the selection manager
     *
     * @param type type of selection
     */
    @Override
    public void selectionChanged(String type) {
        if (type.equalsIgnoreCase("protein_selection")) {
            Set<QuantComparisonProtein> selectedProteinsList;
            filterSortSwichBtn.reset();
            if (CSFPR_Central_Manager.getSelectedProteinsList() == null) {
                proteinSearchingMap.clear();
                selectedProteinsList = new LinkedHashSet<>();
                Map<String, QuantComparisonProtein> proteinsFilterMap = new LinkedHashMap<>();
                CSFPR_Central_Manager.getSelectedComparisonsList().stream().forEach((comparison) -> {
                    selectedProteinsList.addAll(comparison.getQuantComparisonProteinMap().values());
                    comparison.getQuantComparisonProteinMap().values().forEach((prot) -> {
                        proteinsFilterMap.put(prot.getProteinAccession() + "__" + prot.getProteinName(), prot);
                    });
                });
                quantProteinTable.updateTableData(CSFPR_Central_Manager.getSelectedComparisonsList(), new LinkedHashSet<>(proteinsFilterMap.values()));
            } else {
                Set<QuantComparisonProtein> searchSet = new LinkedHashSet<>();
                selectedProteinsList = CSFPR_Central_Manager.getSelectedProteinsList();
                selectedProteinsList.stream().forEach((key) -> {
                    searchSet.addAll(getSearchingProteinsList(key.getProteinAccession() + "__" + key.getProteinName()));
                });
                quantProteinTable.updateTableData(CSFPR_Central_Manager.getSelectedComparisonsList(), searchSet);
            }
            selectedProteinsList.stream().forEach((protein) -> {
                proteinSearchingMap.put(protein.getProteinAccession() + "__" + protein.getProteinName(), protein);
            });
        } else if (type.equalsIgnoreCase("comparisons_selection")) {
            removeFiltersBtn.setEnabled(false);
            filterSortSwichBtn.reset();
        }
    }

    /**
     * Get registered listener id
     *
     * @return listener id
     */
    @Override
    public String getListenerId() {
        return this.getClass().getName();
    }

    /**
     * Get side buttons container that has all the protein table control buttons
     *
     * @return proteinTableToolsContainer
     */
    public VerticalLayout getProteinTableToolsContainer() {
        return proteinTableToolsContainer;
    }

    /**
     * Searching for proteins using name or accessions within the quant
     * comparisons table
     *
     * @param keyword query keyword
     * @return list of found quant proteins
     */
    private Set<QuantComparisonProtein> getSearchingProteinsList(String keyword) {
        Set<QuantComparisonProtein> subAccessionMap = new HashSet<>();
        if (keyword.trim().equalsIgnoreCase("")) {
            subAccessionMap.addAll(proteinSearchingMap.values());
        } else {
            proteinSearchingMap.keySet().stream().filter((key) -> (key.trim().toLowerCase().contains(keyword.toLowerCase().trim()))).forEach((key) -> {
                subAccessionMap.add(proteinSearchingMap.get(key));
            });
        }
        return subAccessionMap;
    }

    /**
     * Update thumb button icon and text for the left side button
     *
     * @param rowNumber Number of rows in the table(table items)
     * @param imgURl URL for image encoded as Base64 string
     */
    public abstract void updateThumbIconRowNumber(int rowNumber, String imgURl);

    /**
     * Add User Customized Comparison to the system (activating quant compare
     * mode)
     *
     * @param userCustomizedComparison Customized comparison based on user input
     * data in quant comparison layout
     */
    public void setUserCustomizedComparison(QuantDiseaseGroupsComparison userCustomizedComparison) {
        quantProteinTable.setUserCustomizedComparison(userCustomizedComparison);
    }

}
