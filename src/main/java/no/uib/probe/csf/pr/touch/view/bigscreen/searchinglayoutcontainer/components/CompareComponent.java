package no.uib.probe.csf.pr.touch.view.bigscreen.searchinglayoutcontainer.components;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.Data_Handler;
import no.uib.probe.csf.pr.touch.database.Query;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import no.uib.probe.csf.pr.touch.logic.beans.QuantProtein;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFPR_Central_Manager;
import no.uib.probe.csf.pr.touch.selectionmanager.QuantSearchSelection;
import no.uib.probe.csf.pr.touch.view.core.BigBtn;
import no.uib.probe.csf.pr.touch.view.core.CloseButton;
import no.uib.probe.csf.pr.touch.view.core.InformationButton;
import no.uib.probe.csf.pr.touch.view.core.PieChart;
import no.uib.probe.csf.pr.touch.view.core.PopupWindow;
import no.uib.probe.csf.pr.touch.view.core.TrendLegend;
import org.vaadin.teemu.VaadinIcons;

/**
 * This class represents quantitative comparison window where users can compare
 * their own data with the resource data, the class is a button with pop-up
 * panel layout
 *
 * @author Yehia Farag
 */
public abstract class CompareComponent extends BigBtn {

    /**
     * The main comparison pop-up window (the pop up container).
     */
    private final PopupWindow comparePanel;
    /**
     * List of quantitative proteins found in searching .
     */
    private List<QuantProtein> searchQuantificationProtList;
    /**
     * The quant results container layout.
     */
    private final HorizontalLayout quantCompareDataResult;
    /**
     * The id results container layout (the id link container for CSF-PR 1.0).
     */
    private final VerticalLayout idDataResult;
    /**
     * The quantitative data handler to work as controller layer to interact
     * between visualization and logic layer.
     */
    private final Data_Handler Data_handler;
    /**
     * The quant data results label (results and hits number).
     */
    private final Label resultsLabel;
    /**
     * The bottom buttons container it contains also the link to CSF-PR 1.0 id
     * results.
     */
    private final HorizontalLayout controlBtnsLayout;
    /**
     * The button is responsible for invoking the show results in system where
     * the user can visualize his data and compare it with the CSF-PR 2.0
     * results.
     */
    private final Button loadDataBtn;
    /**
     * The compare unit layout has the input data fields for user data.
     */
    private final ComparisonUnitComponent compareUnit;
    /**
     * The central selection manager for handling data across different
     * visualizations and managing all users selections.
     */
    private final CSFPR_Central_Manager CSFPR_Central_Manager;
    /**
     * The protein overview chart.
     */
    private PieChart proteinsChart;
    /**
     * The datasets overview chart.
     */
    private PieChart datasetsChart;
    /**
     * The size of screen is used to switch the data visualization mode between
     * normal and compact mode.
     */
    private final boolean smallScreen;
    /**
     * The results layout is the container for quant data results (the pie
     * charts).
     */
    private final VerticalLayout resultsLayout;
    /**
     * The middle layout has the results label and legend .
     */
    private final HorizontalLayout middleLayout;
    /**
     * Set of keywords to avoid duplicating the keywords.
     */
    private Set<String> filterKeywordSet;
    /**
     * Array of disease category names.
     */
    private final String[] diseaseCategoryNames = new String[]{"Alzheimer's", "Multiple Sclerosis", "Parkinson's", "Amyotrophic Lateral Sclerosis"};
    /**
     * Array of disease category AWT colors required for JFree charts.
     */
    private final Color[] diseaseCategoryColors = new Color[]{Color.decode("#4b7865"), Color.decode("#A52A2A"), Color.decode("#74716E"), Color.decode("#1b699f")};

    /**
     * Constructor to initialize the main attributes (data handler and selection
     * manager)
     *
     * @param Data_handler The quantitative data handler.
     * @param CSFPR_Central_Manager The central selection manager
     */
    public CompareComponent(final Data_Handler Data_handler, CSFPR_Central_Manager CSFPR_Central_Manager) {
        super("Compare", "Compare to your data", "img/compare.png");
        this.smallScreen = Page.getCurrent().getBrowserWindowHeight() <= 940;
        this.Data_handler = Data_handler;
        this.CSFPR_Central_Manager = CSFPR_Central_Manager;
        VerticalLayout popupbodyLayout = new VerticalLayout();
        popupbodyLayout.setSpacing(false);
        popupbodyLayout.setWidth(100, Unit.PERCENTAGE);
        popupbodyLayout.setHeight(100, Unit.PERCENTAGE);
        popupbodyLayout.setMargin(new MarginInfo(false, false, false, false));
        comparePanel = new PopupWindow(popupbodyLayout, "Compare Data");
        int h1 = 440;
        if (this.smallScreen) {
            comparePanel.setHeight(Page.getCurrent().getBrowserWindowHeight(), Unit.PIXELS);
            comparePanel.setWidth(Page.getCurrent().getBrowserWindowWidth() - 20, Unit.PIXELS);

        } else {
            comparePanel.setHeight(comparePanel.getHeight() - 50, Unit.PIXELS);
            comparePanel.setWidth(comparePanel.getWidth() - 20, Unit.PIXELS);
        }
        compareUnit = new ComparisonUnitComponent(Data_handler, (int) comparePanel.getWidth() - 24, h1) {

            @Override
            public void startComparing(Query query) {
                compareProteins(query);
            }

        };
        popupbodyLayout.addComponent(compareUnit);
        popupbodyLayout.setExpandRatio(compareUnit, h1 + 10);

        middleLayout = new HorizontalLayout();
        middleLayout.setMargin(new MarginInfo(false, true));
        middleLayout.setHeight(29, Unit.PIXELS);
        middleLayout.setWidth(100, Unit.PERCENTAGE);
        resultsLabel = new Label("Comparison Results");
        resultsLabel.setStyleName(ValoTheme.LABEL_BOLD);
        middleLayout.addComponent(resultsLabel);
        resultsLabel.setWidth(200, Unit.PIXELS);
        middleLayout.setComponentAlignment(resultsLabel, Alignment.BOTTOM_LEFT);
        middleLayout.setExpandRatio(resultsLabel, 230);

        HorizontalLayout legendContainer = new HorizontalLayout();
        legendContainer.setSpacing(true);

        middleLayout.addComponent(legendContainer);
        middleLayout.setComponentAlignment(legendContainer, Alignment.MIDDLE_RIGHT);

        TrendLegend legend2 = new TrendLegend("found_notfound");
        TrendLegend legend = new TrendLegend("diseaselegend");

        popupbodyLayout.addComponent(middleLayout);
        popupbodyLayout.setExpandRatio(middleLayout, 30f);

        resultsLayout = new VerticalLayout();
        resultsLayout.addStyleName("roundedborder");
        resultsLayout.addStyleName("whitelayout");
        resultsLayout.addStyleName("padding20");
        resultsLayout.addStyleName("marginleft");
        resultsLayout.addStyleName("marginbottom");
        resultsLayout.addStyleName("scrollable");
        resultsLayout.setWidth(compareUnit.getWidth(), Unit.PIXELS);
        resultsLayout.setHeight(Math.max(comparePanel.getHeight() - 30 - 10 - h1 - 30 - 10 - 50 - 10 - 10, 1), Unit.PIXELS);
        popupbodyLayout.addComponent(resultsLayout);
        popupbodyLayout.setExpandRatio(resultsLayout, resultsLayout.getHeight() + 10);

        quantCompareDataResult = new HorizontalLayout();

        resultsLayout.addComponent(quantCompareDataResult);
        resultsLayout.setComponentAlignment(quantCompareDataResult, Alignment.MIDDLE_CENTER);

        controlBtnsLayout = new HorizontalLayout();
        controlBtnsLayout.addStyleName("roundedborder");
        controlBtnsLayout.addStyleName("whitelayout");
        controlBtnsLayout.addStyleName("padding10");
        controlBtnsLayout.addStyleName("marginleft");
        controlBtnsLayout.addStyleName("marginbottom");
        controlBtnsLayout.setHeight(50, Unit.PIXELS);
        controlBtnsLayout.setWidth(compareUnit.getWidth(), Unit.PIXELS);
        popupbodyLayout.addComponent(controlBtnsLayout);
        popupbodyLayout.setExpandRatio(controlBtnsLayout, 70);

        HorizontalLayout leftsideWrapper = new HorizontalLayout();
        controlBtnsLayout.addComponent(leftsideWrapper);
        controlBtnsLayout.setComponentAlignment(leftsideWrapper, Alignment.TOP_LEFT);
        leftsideWrapper.setSpacing(true);

        InformationButton info = new InformationButton("To compare your own data against the protein information in CSF-PR upload your UniProt accession numbers (divided into the three categories Increased, Equal and Decreased), select (or name) the disease comparison categories for your input data at the top, and click the \"Compare\" button. A graphical overview of the results will be displayed at the bottom. You can either load all the results or select a subset via the charts before loading.", true);
        leftsideWrapper.addComponent(info);

        idDataResult = new VerticalLayout();
        idDataResult.addStyleName("marginleft");
        idDataResult.addStyleName("idresults");
        idDataResult.addStyleName("idsearchresults");
        leftsideWrapper.addComponent(idDataResult);

        controlBtnsLayout.addComponent(leftsideWrapper);
        controlBtnsLayout.setExpandRatio(leftsideWrapper, controlBtnsLayout.getWidth() - 130);

        HorizontalLayout btnsWrapper = new HorizontalLayout();
        controlBtnsLayout.addComponent(btnsWrapper);
        controlBtnsLayout.setComponentAlignment(btnsWrapper, Alignment.TOP_RIGHT);
        controlBtnsLayout.setExpandRatio(btnsWrapper, 130);
        btnsWrapper.setSpacing(true);

        Button resetBtn = new Button("Reset");
        resetBtn.setStyleName(ValoTheme.BUTTON_SMALL);
        resetBtn.setStyleName(ValoTheme.BUTTON_TINY);
        resetBtn.setWidth(60, Sizeable.Unit.PIXELS);
        resetBtn.setEnabled(true);
        btnsWrapper.addComponent(resetBtn);
        btnsWrapper.setComponentAlignment(resetBtn, Alignment.MIDDLE_CENTER);
        resetBtn.setDescription("Reset searching");
        resetBtn.addClickListener((Button.ClickEvent event) -> {
            resetComparison();
        });

        loadDataBtn = new Button("Load");
        loadDataBtn.setStyleName(ValoTheme.BUTTON_SMALL);
        loadDataBtn.setStyleName(ValoTheme.BUTTON_TINY);
        loadDataBtn.setWidth(60, Sizeable.Unit.PIXELS);
        loadDataBtn.setEnabled(false);
        btnsWrapper.addComponent(loadDataBtn);
        btnsWrapper.setComponentAlignment(loadDataBtn, Alignment.MIDDLE_CENTER);
        loadDataBtn.setDescription("Load data");
        loadDataBtn.addClickListener((Button.ClickEvent event) -> {
            loadComparison();
        });

        popupbodyLayout.setSpacing(true);
        if (this.smallScreen) {
            resultsLayout.setVisible(false);
            middleLayout.setVisible(false);
            comparePanel.setHeight(10 + h1 + 20 + 50 + 10 + 30, Unit.PIXELS);
            popupbodyLayout.setHeight(comparePanel.getHeight() - 30, Unit.PIXELS);

            resultsLayout.setWidth(compareUnit.getWidth(), compareUnit.getWidthUnits());
            resultsLayout.setHeight(Math.max(comparePanel.getHeight() - 30 - 10 - 30 - 10 - 50 - 10 - 10, 1), compareUnit.getHeightUnits());

            compareUnit.setHeight(resultsLayout.getHeight() + 30, Unit.PIXELS);

            popupbodyLayout.setExpandRatio(compareUnit, compareUnit.getHeight());
            popupbodyLayout.setExpandRatio(resultsLayout, compareUnit.getHeight());

        } else {
            popupbodyLayout.setHeight(10 + h1 + 30 + resultsLayout.getHeight() + 20 + 50 + 10, Unit.PIXELS);
        }
        if (comparePanel.getWidth() - 230 < 436) {
            middleLayout.removeComponent(legendContainer);
            CloseButton closeBtn = new CloseButton();
            VerticalLayout legendPopup = new VerticalLayout();
            legendPopup.addComponent(closeBtn);
            legendPopup.setExpandRatio(closeBtn, 2);
            Set<Component> set = new LinkedHashSet<>();
            Iterator<Component> itr = legend2.iterator();
            while (itr.hasNext()) {
                set.add(itr.next());
            }
            VerticalLayout spacer = new VerticalLayout();
            spacer.setHeight(15, Unit.PIXELS);
            spacer.setWidth(20, Unit.PIXELS);
            set.add(spacer);
            Iterator<Component> itr2 = legend.iterator();
            while (itr2.hasNext()) {
                set.add(itr2.next());
            }

            for (Component c : set) {
                legendPopup.addComponent(c);
                legendPopup.setExpandRatio(c, c.getHeight() + 5);
            }
            legend2.setSpacing(true);
            legendPopup.setWidth(150, Unit.PIXELS);
            legendPopup.setHeight(100, Unit.PIXELS);
            final PopupView popup = new PopupView("Legend", legendPopup);
            legendPopup.addStyleName("compactlegend");
            popup.setHideOnMouseOut(false);
            closeBtn.addLayoutClickListener((LayoutEvents.LayoutClickEvent event) -> {
                popup.setPopupVisible(false);

            });
            middleLayout.addComponent(popup);
            middleLayout.setComponentAlignment(popup, Alignment.MIDDLE_RIGHT);
            middleLayout.setExpandRatio(popup, comparePanel.getWidth() - 230);

        } else {
            middleLayout.setExpandRatio(legendContainer, comparePanel.getWidth() - 230);
            legendContainer.addComponent(legend2);
            legendContainer.setComponentAlignment(legend2, Alignment.MIDDLE_RIGHT);
            legend2.addStyleName("marginright");

            legendContainer.addComponent(legend);
            legendContainer.setComponentAlignment(legend, Alignment.MIDDLE_RIGHT);
            legend.addStyleName("marginright");

        }

    }

    /**
     * Reset comparison layout by clearing all input and results fields and
     * reset the system to default.
     */
    private void resetComparison() {
        compareUnit.reset();
        idDataResult.setVisible(false);
        quantCompareDataResult.removeAllComponents();
        loadDataBtn.setEnabled(false);
        if (smallScreen) {
            resultsLayout.setVisible(false);
            middleLayout.setVisible(false);
            compareUnit.setVisible(true);

        }
        CSFPR_Central_Manager.resetSearchSelection();

    }

    /**
     * Load and invoke comparison mode in the system to visualize the user input
     * data in the system.
     */
    private void loadComparison() {
        Set<String> diseaseCategories = new HashSet<>();
        Set<String> proteinList = new HashSet<>();
        Set<Integer> datasetIds = new HashSet<>();
        QuantSearchSelection selection = new QuantSearchSelection();
        Map<String, Set<Integer>> diseaseCategoriesIdMap = new HashMap<>();
        Set<String> proteinAccession = new HashSet<>();
        QuantDiseaseGroupsComparison userComparison = compareUnit.getUserCustomizedComparison();
        if (!datasetsChart.getSelectionSet().isEmpty() && !datasetsChart.getSelectionSet().contains("all")) {
            searchQuantificationProtList.stream().filter((protein) -> (datasetsChart.getSelectionSet().contains(protein.getDiseaseCategoryI()) || datasetsChart.getSelectionSet().contains(protein.getDiseaseCategoryII()))).map((protein) -> {
                datasetIds.add(protein.getQuantDatasetIndex());
                return protein;
            }).forEach((protein) -> {
                userComparison.getQuantComparisonProteinMap().get(protein.getUniprotAccessionNumber()).setProteinName(protein.getUniprotProteinName());
                proteinList.add(protein.getUniprotAccessionNumber());
                diseaseCategories.add(protein.getDiseaseCategoryI());
                diseaseCategories.add(protein.getDiseaseCategoryII());
                proteinAccession.add(protein.getUniprotAccessionNumber());
                if (!diseaseCategoriesIdMap.containsKey(protein.getDiseaseCategoryI())) {
                    diseaseCategoriesIdMap.put(protein.getDiseaseCategoryI(), new HashSet<>());
                }
                if (!diseaseCategoriesIdMap.containsKey(protein.getDiseaseCategoryII())) {
                    diseaseCategoriesIdMap.put(protein.getDiseaseCategoryII(), new HashSet<>());
                }
                Set<Integer> datasetIdSet = diseaseCategoriesIdMap.get(protein.getDiseaseCategoryI());
                datasetIdSet.add(protein.getQuantDatasetIndex());
                diseaseCategoriesIdMap.put(protein.getDiseaseCategoryI(), datasetIdSet);
                datasetIdSet = diseaseCategoriesIdMap.get(protein.getDiseaseCategoryII());
                datasetIdSet.add(protein.getQuantDatasetIndex());
                diseaseCategoriesIdMap.put(protein.getDiseaseCategoryII(), datasetIdSet);
            });
            selection.setKeyWords(proteinList);

        } else {
            searchQuantificationProtList.stream().forEach((protein) -> {
                proteinAccession.add(protein.getUniprotAccessionNumber());
                userComparison.getQuantComparisonProteinMap().get(protein.getUniprotAccessionNumber()).setProteinName(protein.getUniprotProteinName());
                if (!diseaseCategoriesIdMap.containsKey(protein.getDiseaseCategoryI())) {
                    diseaseCategoriesIdMap.put(protein.getDiseaseCategoryI(), new HashSet<>());
                }
                if (!diseaseCategoriesIdMap.containsKey(protein.getDiseaseCategoryII())) {
                    diseaseCategoriesIdMap.put(protein.getDiseaseCategoryII(), new HashSet<>());
                }
                Set<Integer> datasetIdSet = diseaseCategoriesIdMap.get(protein.getDiseaseCategoryI());
                datasetIdSet.add(protein.getQuantDatasetIndex());
                diseaseCategoriesIdMap.put(protein.getDiseaseCategoryI(), datasetIdSet);
                datasetIds.add(protein.getQuantDatasetIndex());
                diseaseCategories.add(protein.getDiseaseCategoryI());
                datasetIdSet = diseaseCategoriesIdMap.get(protein.getDiseaseCategoryII());
                datasetIdSet.add(protein.getQuantDatasetIndex());
                diseaseCategoriesIdMap.put(protein.getDiseaseCategoryII(), datasetIdSet);
                diseaseCategories.add(protein.getDiseaseCategoryII());

            });
        }
        selection.setKeyWords(filterKeywordSet);
        comparePanel.close();//
        selection.setDiseaseCategories(diseaseCategories);
        selection.setQuantDatasetIndexes(datasetIds);
        selection.setUserCustomizedComparison(userComparison);
        selection.setDiseaseCategoriesIdMap(diseaseCategoriesIdMap);
        selection.setSelectedProteinsList(proteinAccession);
        Data_handler.switchToSearchingMode(selection);
        CSFPR_Central_Manager.compareSelectionAction(selection);
        loadQuantComparison();

    }

    /**
     * Perform data query for user input data against the database
     *
     * @param query constructed query from user input data
     */
    private void compareProteins(Query query) {
        query.setValidatedProteins(false);
        query.setSearchDataset("");
        //searching quant data
        String defaultText = query.getSearchKeyWords();
        defaultText = defaultText.replace(",", "\n").replace(" ", "").trim();
        filterKeywordSet = new LinkedHashSet<>();
        filterKeywordSet.addAll(Arrays.asList(defaultText.split("\n")));

        defaultText = "";
        defaultText = filterKeywordSet.stream().map((str) -> str + "\n").reduce(defaultText, String::concat);
        query.setSearchKeyWords(defaultText);
        //searching quant data
        query.setSearchDataType("Quantification Data");
        searchQuantificationProtList = Data_handler.searchQuantificationProtein(query, true);
        String quantNotFound = Data_handler.filterQuantSearchingKeywords(searchQuantificationProtList, query.getSearchKeyWords(), query.getSearchBy());

        Integer[] quantHitsList = Data_handler.getQuantComparisonHitsList(searchQuantificationProtList, query.getSearchBy());
        Integer[] proteinsHitsList = Data_handler.getQuantComparisonProteinList(searchQuantificationProtList, query.getSearchBy());
        if (quantHitsList != null && searchQuantificationProtList != null) {
            Set<Integer> studiesSet;
            studiesSet = new HashSet<>();
            searchQuantificationProtList.stream().forEach((qp) -> {
                studiesSet.add(qp.getQuantDatasetIndex());
            });
            if (quantNotFound != null) {
                quantNotFound = quantNotFound.replace("\n", ",");
                for (String s : quantNotFound.split(",")) {
                    filterKeywordSet.remove(s.trim());
                }
            } else {
                quantNotFound = "";
            }

            initQuantComparisonresults(quantHitsList, proteinsHitsList, quantNotFound.split(","), filterKeywordSet.size());
            loadDataBtn.setEnabled(!filterKeywordSet.isEmpty());
        }

        query.setSearchDataType("Identification Data");

        //searching id data
        String idSearchIdentificationProtList = Data_handler.searchIdentificationProtein(query);
        ExternalResource idRes;
        if (idSearchIdentificationProtList != null && !idSearchIdentificationProtList.isEmpty()) {
            Base64.Encoder encURL = Base64.getUrlEncoder();

            String param = "searchby:" + query.getSearchBy().replace(" ", "*") + "___searchkey:" + query.getSearchKeyWords().replace("\n", "__").replace(" ", "*");
            String encoded64;
            if (param.length() < 10) {
                encoded64 = "list_" + encURL.encodeToString(param.getBytes());
            } else {
                int index = Data_handler.storeQuery(query);
                encoded64 = "query_" + index + "_" + VaadinSession.getCurrent().getCsrfToken();//file_" + encURL.encodeToString(initQueryFile(query).getBytes());
            }
            idRes = new ExternalResource(VaadinSession.getCurrent().getAttribute("csf_pr_Url") + encoded64);
            Link idSearchingLink = new Link(idSearchIdentificationProtList, idRes);
            idSearchingLink.setIcon(VaadinIcons.BAR_CHART);
            idSearchingLink.setTargetName("_blank");
            idSearchingLink.setStyleName(ValoTheme.LINK_SMALL);
            idSearchingLink.addStyleName("smalllink");
            idSearchingLink.setDescription("View protein id results in CSF-PR v1.0");
            idSearchingLink.setWidth(100, Unit.PERCENTAGE);
            idDataResult.addComponent(idSearchingLink);

        } else {
            idDataResult.setVisible(false);
        }

    }

    /**
     * Initialize quant searching results layout
     *
     * @param quantHitsList map of hits and main protein title
     * @param searchBy the searching by method
     * @param totalProtNum total number of hits
     * @param keywords the keywords used for the searching
     *
     */
    private void initQuantComparisonresults(Integer[] quantHitsList, Integer[] proteinsHitsList, String[] notFoundAcc, int found) {

        quantCompareDataResult.removeAllComponents();
        if (quantHitsList == null || quantHitsList.length == 0) {
            quantCompareDataResult.setVisible(false);
            loadDataBtn.setEnabled(false);
            return;
        }
        loadDataBtn.setEnabled(true);
        quantCompareDataResult.setVisible(true);

        PieChart notFoundChart = new PieChart(250, 200, "Found / Not Found", true) {

            @Override
            public void sliceClicked(Comparable sliceKey) {
                resetChart();
                if (sliceKey.toString().equalsIgnoreCase("Not Found")) {
                    if (notFoundAcc.length == 1 && notFoundAcc[0].trim().equalsIgnoreCase("")) {
                        return;
                    }
                    StreamResource proteinInformationResource = createProteinsExportResource(new HashSet<>(Arrays.asList(notFoundAcc)));
                    Page.getCurrent().open(proteinInformationResource, "_blank", false);

                } else if (sliceKey.toString().equalsIgnoreCase("Found")) {
                    if (filterKeywordSet.isEmpty()) {
                        return;
                    }
                    StreamResource proteinInformationResource = createProteinsExportResource(filterKeywordSet);
                    Page.getCurrent().open(proteinInformationResource, "_blank", false);

                }

            }

        };

        notFoundChart.getMiddleDonutLayout().addStyleName("defaultcursor");
        notFoundChart.getMiddleDonutLayout().setDescription("Click slice to export");
        notFoundChart.setDescription("Click slice to export");
        int notFoundLength = notFoundAcc.length;
        if (notFoundAcc.length == 1 && notFoundAcc[0].trim().equalsIgnoreCase("")) {
            notFoundLength = 0;
        }
        notFoundChart.initializeFilterData(new String[]{"Not Found", "Found"}, new Integer[]{notFoundLength, found, found + notFoundLength}, new Color[]{new Color(219, 169, 1), new Color(110, 177, 206),});
        notFoundChart.redrawChart();
        quantCompareDataResult.addComponent(notFoundChart);
        quantCompareDataResult.setComponentAlignment(notFoundChart, Alignment.MIDDLE_CENTER);

        proteinsChart = new PieChart(250, 200, "Proteins", true) {

            @Override
            public void sliceClicked(Comparable sliceKey) {
                datasetsChart.selectSlice(sliceKey);
            }

        };
        proteinsChart.initializeFilterData(diseaseCategoryNames, proteinsHitsList, diseaseCategoryColors);
        proteinsChart.redrawChart();
        quantCompareDataResult.addComponent(proteinsChart);
        quantCompareDataResult.setComponentAlignment(proteinsChart, Alignment.MIDDLE_CENTER);

        datasetsChart = new PieChart(250, 200, "# Hits", true) {

            @Override
            public void sliceClicked(Comparable sliceKey) {
                proteinsChart.selectSlice(sliceKey);

            }

        };
        datasetsChart.initializeFilterData(diseaseCategoryNames, quantHitsList, diseaseCategoryColors);
        datasetsChart.redrawChart();
        quantCompareDataResult.addComponent(datasetsChart);
        quantCompareDataResult.setComponentAlignment(datasetsChart, Alignment.MIDDLE_CENTER);

        resultsLabel.setValue("Results Overview (" + quantHitsList[3] + "/" + (quantHitsList[3] + notFoundAcc.length) + ")");

        if (smallScreen) {
            resultsLayout.setVisible(true);
            middleLayout.setVisible(true);
            compareUnit.setVisible(false);
        }

    }

    /**
     * On click view the comparison panel.
     */
    @Override
    public void onClick() {
        comparePanel.setVisible(true);

    }

    /**
     * Load and invoke comparison mode in the system to visualize the user input
     * data in the system.
     */
    public abstract void loadQuantComparison();

    /**
     * Create and initialize not found proteins file that has proteins
     * accessions list
     *
     * @param accessions set of not found protein accessions
     * @return a StreamResource for protein exporting file
     */
    private StreamResource createProteinsExportResource(Set<String> accessions) {
        return new StreamResource(() -> {
            byte[] csvFile = Data_handler.exportProteinsListToCSV(accessions);
            return new ByteArrayInputStream(csvFile);
        }, "Proteins_List.csv");
    }

}
