package no.uib.probe.csf.pr.touch.view.bigscreen.welcomepagecontainer;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.Data_Handler;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDataset;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFListener;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFPR_Central_Manager;
import no.uib.probe.csf.pr.touch.view.LogoLayout;
import no.uib.probe.csf.pr.touch.view.LayoutViewManager;
import no.uib.probe.csf.pr.touch.view.bigscreen.popupwindows.DatasetInformationWindow;
import no.uib.probe.csf.pr.touch.view.bigscreen.searchinglayoutcontainer.components.CompareComponent;
import no.uib.probe.csf.pr.touch.view.bigscreen.searchinglayoutcontainer.components.SearchingComponent;
import no.uib.probe.csf.pr.touch.view.core.BigBtn;
import no.uib.probe.csf.pr.touch.view.core.ImageContainerBtn;
import no.uib.probe.csf.pr.touch.view.core.OverviewInfoBean;

/**
 * This class represents welcome page main components and buttons welcome
 * message resource overview information.
 *
 * @author Yehia Farag
 */
public class WelcomeLayoutComponents extends VerticalLayout implements Serializable {

    /**
     * The top right thumb buttons container.
     */
    private HorizontalLayout topRightThumbBtnsLayoutContainer;
    /**
     * The top right reset system thumb button.
     */
    private ImageContainerBtn resetThumbBtn;
    /**
     * The top right searching thumb button.
     */
    private ImageContainerBtn searchThumbBtn;
    /**
     * The top right compare user data thumb button.
     */
    private ImageContainerBtn compareThumbBtn;

    /**
     * Get the top right thumb buttons container
     *
     * @return topRightThumbBtnsLayoutContainer main layout view container
     */
    public HorizontalLayout getTopRightThumbBtnsLayoutContainer() {
        return topRightThumbBtnsLayoutContainer;
    }

    /**
     * Constructor to initialize main attributes (data handler, central manager
     * ..etc)
     *
     * @param Data_handler Quant data handler
     * @param CSFPR_Central_Manager The central selection manager
     * @param View_Manager view manager to control the current application view
     * @param overviewInfoBean resource overview information
     * @param bodyHeight current screen height
     * @param publicationList list of available publications
     * @param dsObjects list of dataset information
     * @param bodyWidth current screen with
     */
    public WelcomeLayoutComponents(final Data_Handler Data_handler, CSFPR_Central_Manager CSFPR_Central_Manager, final LayoutViewManager View_Manager, int bodyWidth, int bodyHeight, OverviewInfoBean overviewInfoBean, Map<String,Object[]> publicationList, Set<QuantDataset> dsObjects) {

        this.setWidth(980, Unit.PIXELS);
        this.setHeight(100, Unit.PERCENTAGE);
        this.addStyleName("hideoverflow");
        this.addStyleName("whitelayout");
        boolean smallScreen = bodyHeight <= 200;
        float expHeaderRatio;
        String breakline;
        if (!smallScreen) {
            expHeaderRatio = 45f;
            this.setMargin(false);
            this.addStyleName("padding10x45");
            breakline = "<br/>";
        } else {
            breakline = "";
            expHeaderRatio = 45f;
            this.addStyleName("padding10x45");
        }
        LogoLayout header = new LogoLayout();
        this.addComponent(header);
        float headerRatio = expHeaderRatio / (float) bodyHeight;
        int bottomHeight = bodyHeight - (int) expHeaderRatio;
        float bodyRatio = (float) bottomHeight / (float) bodyHeight;
        this.setExpandRatio(header, headerRatio);

        int rightPanelWidth = 700;
        HorizontalLayout mainBodyHLayout = new HorizontalLayout();
        mainBodyHLayout.setWidthUndefined();
        this.addComponent(mainBodyHLayout);
        this.setExpandRatio(mainBodyHLayout, bodyRatio);

        //init left panel (Resource overview)
        VerticalLayout leftPanelWrapper = new VerticalLayout();
        leftPanelWrapper.setWidth(160, Unit.PIXELS);
//        leftPanelWrapper.setHeight(100,Unit.PERCENTAGE);
        leftPanelWrapper.setSpacing(true);
        mainBodyHLayout.addComponent(leftPanelWrapper);

        VerticalLayout leftPanelResorceStatContainer = new VerticalLayout();
        leftPanelResorceStatContainer.setWidth(100, Unit.PERCENTAGE);
        leftPanelResorceStatContainer.setHeightUndefined();
        leftPanelWrapper.addComponent(leftPanelResorceStatContainer);

        // the stat layout
        Label statLabel = new Label("Resource  Status");
        statLabel.setStyleName(ValoTheme.LABEL_H3);
        statLabel.addStyleName(ValoTheme.LABEL_BOLD);

        statLabel.setWidthUndefined();
        statLabel.setHeight(20, Unit.PIXELS);
        leftPanelResorceStatContainer.addComponent(statLabel);

        Label quantStatLabel = new Label("Quantitative  Data");
        quantStatLabel.setHeight(20, Unit.PIXELS);
        quantStatLabel.addStyleName(ValoTheme.LABEL_BOLD);
        quantStatLabel.addStyleName(ValoTheme.LABEL_H4);
        quantStatLabel.addStyleName(ValoTheme.LABEL_TINY);
        quantStatLabel.addStyleName("nomargin");
        leftPanelResorceStatContainer.addComponent(quantStatLabel);

        GridLayout subQuantStatLayout = new GridLayout(2, 3);
        subQuantStatLayout.setMargin(new MarginInfo(false, false, false, false));
        subQuantStatLayout.setWidth(100, Unit.PERCENTAGE);
        leftPanelResorceStatContainer.addComponent(subQuantStatLayout);

        DatasetInformationWindow sub2quantStatLabelWrapper = new DatasetInformationWindow(publicationList) {

            @Override
            public Map<String,Object[]> getPublicationsInformation(Set<String> pumedId) {
                return publicationList;
            }

        };
        if (dsObjects == null) {
            return;
        }

        CSFPR_Central_Manager.setFullPublicationList(publicationList);
        sub2quantStatLabelWrapper.updateData(dsObjects);
        subQuantStatLayout.addComponent(sub2quantStatLabelWrapper, 0, 0);
        sub2quantStatLabelWrapper.setDescription("Click to view datasets information");

        Label sub2quantStatLabel = new Label("#Datasets");
        sub2quantStatLabel.addStyleName("link");
        sub2quantStatLabel.addStyleName(ValoTheme.LABEL_SMALL);
        sub2quantStatLabel.addStyleName(ValoTheme.LABEL_TINY);

        sub2quantStatLabel.addStyleName("nomargin");
        sub2quantStatLabelWrapper.addStyleName("nomargin");
        sub2quantStatLabelWrapper.addComponent(sub2quantStatLabel);

        Label sub2QuantStatValue = new Label(overviewInfoBean.getNumberOfQuantDatasets() + "");
        sub2QuantStatValue.addStyleName(ValoTheme.LABEL_SMALL);
        sub2QuantStatValue.addStyleName(ValoTheme.LABEL_TINY);
        sub2QuantStatValue.addStyleName("nomargin");
        sub2QuantStatValue.addStyleName("rightaligntext");
        sub2QuantStatValue.setWidth(40, Unit.PIXELS);
        subQuantStatLayout.addComponent(sub2QuantStatValue, 1, 0);
        subQuantStatLayout.setComponentAlignment(sub2QuantStatValue, Alignment.MIDDLE_RIGHT);

        Label sub3quantStatLabel = new Label("#Proteins");
        sub3quantStatLabel.addStyleName(ValoTheme.LABEL_SMALL);
        sub3quantStatLabel.addStyleName(ValoTheme.LABEL_TINY);
        sub3quantStatLabel.addStyleName("nomargin");
        subQuantStatLayout.addComponent(sub3quantStatLabel, 0, 1);

        Label sub3QuantStatValue = new Label("" + overviewInfoBean.getNumberOfQuantProteins());
        sub3QuantStatValue.addStyleName(ValoTheme.LABEL_SMALL);
        sub3QuantStatValue.addStyleName(ValoTheme.LABEL_TINY);
        sub3QuantStatValue.addStyleName("nomargin");
        sub3QuantStatValue.addStyleName("rightaligntext");
        subQuantStatLayout.addComponent(sub3QuantStatValue, 1, 1);
        subQuantStatLayout.setComponentAlignment(sub3QuantStatValue, Alignment.MIDDLE_RIGHT);

        Label sub4quantStatLabel = new Label("#Peptides");
        sub4quantStatLabel.addStyleName(ValoTheme.LABEL_SMALL);
        sub4quantStatLabel.addStyleName("nomargin");
        sub4quantStatLabel.addStyleName(ValoTheme.LABEL_TINY);
        subQuantStatLayout.addComponent(sub4quantStatLabel, 0, 2);

        Label sub4QuantStatValue = new Label("" + overviewInfoBean.getNumberOfQuantPeptides());
        sub4QuantStatValue.addStyleName(ValoTheme.LABEL_SMALL);
        sub4QuantStatValue.addStyleName("rightaligntext");
        subQuantStatLayout.addComponent(sub4QuantStatValue, 1, 2);
        subQuantStatLayout.setComponentAlignment(sub4QuantStatValue, Alignment.MIDDLE_RIGHT);

        subQuantStatLayout.setColumnExpandRatio(0, 2);
        subQuantStatLayout.setColumnExpandRatio(1, 1);

        Label idStatLabel = new Label("Identification Data");
        idStatLabel.setHeight(20, Unit.PIXELS);
        idStatLabel.addStyleName(ValoTheme.LABEL_BOLD);
        idStatLabel.addStyleName(ValoTheme.LABEL_H4);
        idStatLabel.addStyleName(ValoTheme.LABEL_TINY);
        idStatLabel.addStyleName("nomargin");
        idStatLabel.addStyleName("margintop15");
        leftPanelResorceStatContainer.addComponent(idStatLabel);

        GridLayout subIdStatLayout = new GridLayout(2, 4);
        subIdStatLayout.setMargin(new MarginInfo(false, false, false, false));
        subIdStatLayout.setWidth(100, Unit.PERCENTAGE);
        subIdStatLayout.setSpacing(true);
        leftPanelResorceStatContainer.addComponent(subIdStatLayout);

        Label sub2IdStatLabel = new Label("#Datasets");
        sub2IdStatLabel.addStyleName(ValoTheme.LABEL_SMALL);
        sub2IdStatLabel.addStyleName(ValoTheme.LABEL_TINY);
        sub2IdStatLabel.addStyleName("nomargin");
        subIdStatLayout.addComponent(sub2IdStatLabel, 0, 1);
        subIdStatLayout.setColumnExpandRatio(0, 2);
        subIdStatLayout.setColumnExpandRatio(1, 1);

        Label sub2IdStatValue = new Label("" + overviewInfoBean.getNumberOfIdStudies());
        sub2IdStatValue.addStyleName(ValoTheme.LABEL_SMALL);
        sub2IdStatValue.addStyleName(ValoTheme.LABEL_TINY);
        sub2IdStatValue.addStyleName("nomargin");
        sub2IdStatValue.addStyleName("rightaligntext");
        sub2IdStatValue.setWidth(60, Unit.PIXELS);
        subIdStatLayout.addComponent(sub2IdStatValue, 1, 1);
        subIdStatLayout.setComponentAlignment(sub2IdStatValue, Alignment.MIDDLE_RIGHT);

        Label sub3IdStatLabel = new Label("#Proteins");
        sub3IdStatLabel.addStyleName(ValoTheme.LABEL_SMALL);
        sub3IdStatLabel.addStyleName(ValoTheme.LABEL_TINY);
        sub3IdStatLabel.addStyleName("nomargin");
        subIdStatLayout.addComponent(sub3IdStatLabel, 0, 2);

        Label sub3IdStatValue = new Label("" + overviewInfoBean.getNumberOfIdProteins());
        sub3IdStatValue.addStyleName(ValoTheme.LABEL_SMALL);
        sub3IdStatValue.addStyleName(ValoTheme.LABEL_TINY);
        sub3IdStatValue.addStyleName("nomargin");
        sub3IdStatValue.addStyleName("rightaligntext");
        sub3IdStatValue.setWidth(60, Unit.PIXELS);
        subIdStatLayout.addComponent(sub3IdStatValue, 1, 2);
        subIdStatLayout.setComponentAlignment(sub3IdStatValue, Alignment.MIDDLE_RIGHT);

        Label sub4IdStatLabel = new Label("#Peptides");
        sub4IdStatLabel.addStyleName(ValoTheme.LABEL_SMALL);
        sub4IdStatLabel.addStyleName(ValoTheme.LABEL_TINY);
        sub4IdStatLabel.addStyleName("nomargin");
        subIdStatLayout.addComponent(sub4IdStatLabel, 0, 3);

        Label sub4IdStatValue = new Label("" + overviewInfoBean.getNumberOfIdPeptides());
        sub4IdStatValue.addStyleName(ValoTheme.LABEL_SMALL);
        sub4IdStatValue.addStyleName(ValoTheme.LABEL_TINY);
        sub4IdStatValue.addStyleName("nomargin");
        sub4IdStatValue.addStyleName("rightaligntext");
        sub4IdStatValue.setWidth(60, Unit.PIXELS);
        subIdStatLayout.addComponent(sub4IdStatValue, 1, 3);
        subIdStatLayout.setComponentAlignment(sub4IdStatValue, Alignment.MIDDLE_RIGHT);

        VerticalLayout relaseNoteInfo = updateRelaseNotesLayout();
        PopupView relLabel = new PopupView("Release <font>" + relaseNoteInfo.getData() + "</font>", relaseNoteInfo);
        relLabel.setCaptionAsHtml(true);
        relLabel.setDescription("Click to open release notes");
        relLabel.setHideOnMouseOut(false);
        relLabel.setHeight(20, Unit.PIXELS);
        relLabel.setWidth(100, Unit.PERCENTAGE);
        relLabel.addStyleName("link");;
        relLabel.addStyleName("lightlabel");
        leftPanelWrapper.addComponent(relLabel);

        //init spacer
        VerticalLayout spacer = new VerticalLayout();
        spacer.setHeight(100, Unit.PERCENTAGE);
        spacer.setWidth(10, Unit.PIXELS);
        spacer.setStyleName("spacer");
        mainBodyHLayout.addComponent(spacer);

        //end of spacer
        //init rightlayout top
        VerticalLayout rightPanelWrapper = new VerticalLayout();
        rightPanelWrapper.setWidth(rightPanelWidth, Unit.PIXELS);
        rightPanelWrapper.setHeightUndefined();
        rightPanelWrapper.setStyleName("framelayout");
        mainBodyHLayout.addComponent(rightPanelWrapper);

        Label infoLable = new Label("Welcome to the CSF Proteome Resource (CSF-PR)");
        infoLable.setStyleName(ValoTheme.LABEL_H3);
        infoLable.addStyleName(ValoTheme.LABEL_BOLD);
        rightPanelWrapper.addComponent(infoLable);

        Label para_1 = new Label("CSF Proteome Resource "
                + "is an online repository of mass spectrometry" + breakline + "based proteomics "
                + "experiments on human cerebrospinal fluid (CSF)<br/>" + breakline
        );
        para_1.setContentMode(ContentMode.HTML);
        rightPanelWrapper.addComponent(para_1);
        para_1.setWidth(rightPanelWidth, Unit.PIXELS);

        para_1.addStyleName(ValoTheme.LABEL_H4);

        para_1.addStyleName("nomargin");
        para_1.addStyleName("marginbottom");
        para_1.addStyleName("linehight25");

        HorizontalLayout rightPanel = new HorizontalLayout();
        rightPanel.setWidth(100, Unit.PERCENTAGE);
        rightPanelWrapper.addComponent(rightPanel);

        VerticalLayout middleLayout = new VerticalLayout();
        middleLayout.setWidth(100, Unit.PERCENTAGE);
        middleLayout.setHeightUndefined();
        middleLayout.setMargin(false);
        rightPanel.addComponent(middleLayout);

        GridLayout middlePanelServicesLayout = new GridLayout(2, 2);
        middlePanelServicesLayout.setSpacing(true);

        if (!smallScreen) {
            middlePanelServicesLayout.setMargin(new MarginInfo(false, false, false, false));
            middlePanelServicesLayout.setHeight(180, Unit.PIXELS);
        } else {
            middlePanelServicesLayout.setHeight(100, Unit.PIXELS);
        }
        middleLayout.addComponent(middlePanelServicesLayout);

        BigBtn quantDatasetBtn = new BigBtn("Quantification", "Browse quantitative data", "img/scatter_plot_applied.png") {

            @Override
            public void onClick() {
                View_Manager.viewLayout("quantview");
            }
        };
        if (!smallScreen) {
            quantDatasetBtn.addStyleName("padding12");
            quantDatasetBtn.addStyleName("margintop10");
        } else {
            quantDatasetBtn.addStyleName("margintop5");
        }

        quantDatasetBtn.getThumbBtn().setDescription("Click to browse protein quantitative data");
        middlePanelServicesLayout.addComponent(quantDatasetBtn, 0, 0);
        middlePanelServicesLayout.setComponentAlignment(quantDatasetBtn, Alignment.TOP_LEFT);
        BigBtn idDatasetBtn = new BigBtn("Identification", "Browse identification data", "img/bar-chart.png") {

            @Override
            public void onClick() {
                Page.getCurrent().open(VaadinSession.getCurrent().getAttribute("csf_pr_Url").toString(), "_blank");//setLocation("http://129.177.231.63/csf-pr-v1.0");

            }
        };
        if (!smallScreen) {
            idDatasetBtn.addStyleName("padding12");
            idDatasetBtn.addStyleName("margintop10");
        } else {
            idDatasetBtn.addStyleName("margintop5");
        }
        idDatasetBtn.getThumbBtn().setDescription("Click to browse protein identification data");

        middlePanelServicesLayout.addComponent(idDatasetBtn, 0, 1);
        middlePanelServicesLayout.setComponentAlignment(idDatasetBtn, Alignment.TOP_LEFT);

        SearchingComponent searchingDatasetBtn = new SearchingComponent(Data_handler, CSFPR_Central_Manager) {

            @Override
            public void loadQuantSearching() {                
                View_Manager.viewLayout("quantview");

            }

        };
        if (!smallScreen) {
            searchingDatasetBtn.addStyleName("padding12");
            searchingDatasetBtn.addStyleName("margintop10");
        } else {
            searchingDatasetBtn.addStyleName("margintop5");
        }
        searchingDatasetBtn.getThumbBtn().setDescription("Click to search quantified and identified protein data");
        middlePanelServicesLayout.addComponent(searchingDatasetBtn, 1, 0);

        CompareComponent compareBtn = new CompareComponent(Data_handler, CSFPR_Central_Manager) {

            @Override
            public void loadQuantComparison() {               
                View_Manager.viewLayout("quantview");
            }

        };
        if (!smallScreen) {
            compareBtn.addStyleName("padding12");
            compareBtn.addStyleName("margintop10");
        } else {
            compareBtn.addStyleName("margintop5");
        }
        compareBtn.getThumbBtn().setDescription("Click to compare with your own protein quantification data");
        middlePanelServicesLayout.addComponent(compareBtn, 1, 1);
        middlePanelServicesLayout.setComponentAlignment(compareBtn, Alignment.TOP_LEFT);

        BigBtn homeBtn = new BigBtn("", "", "img/home-o.png") {

            @Override
            public void onClick() {
                View_Manager.viewLayout("welcomeview");
            }
        };
        this.addComponent(homeBtn);
        homeBtn.setVisible(false);
        homeBtn.getThumbBtn().setDescription("Home page");

        Label para_2 = new Label(breakline + "<p align='justify' Style='margin-bottom:2px;width:650px;text-align: justify;text-justify: inter-word;font-size: 12px;color: black;/* font-weight: bold; */line-height: 12px;'><font>Publications:</font></p><ul align='justify' Style='margin-bottom:2px;margin-top:2px;padding-left:20px;text-align: justify;text-justify: inter-word;font-size: 12px;color: black;/* font-weight: bold; */line-height: 20px;'><li style='width:650px !important'><a class='link' href='http://www.mcponline.org/content/16/2/300' target='_blank'>Guldbrandsen et al.: CSF-PR 2.0: an interactive literature guide to quantitative cerebrospinal fluid mass spectrometry data from neurodegenerative disorders. Mol Cell Proteomics. 2017 Feb;16(2):300-309.</font></li><li style='width:650px !important'><a class='link' href='http://www.mcponline.org/content/13/11/3152.full.pdf+html' target='_blank'>Guldbrandsen et al.: In-depth Characterization of the Cerebrospinal Fluid (CSF) Proteome Displayed Through the CSF Proteome Resource (CSF-PR). Mol Cell Proteomics. 2014 Nov;13(11):3152-63.</a></li></ul>");
        para_2.setContentMode(ContentMode.HTML);

        middleLayout.addComponent(para_2);
        middleLayout.setComponentAlignment(para_2, Alignment.TOP_LEFT);

        this.resetThumbBtn = new ImageContainerBtn() {

            @Override
            public void onClick() {
                CSFPR_Central_Manager.resetSearchSelection();

            }
        };

        final ThemeResource resetSystemIconRes = new ThemeResource("img/ban.png");

        resetThumbBtn.setDescription("Back to browse quantitative data");
        resetThumbBtn.updateIcon(resetSystemIconRes);
        resetThumbBtn.setEnabled(true);
        resetThumbBtn.setReadOnly(false);
        resetThumbBtn.setVisible(false);

        if (smallScreen) {
            resetThumbBtn.setWidth(25, Unit.PIXELS);
            resetThumbBtn.setHeight(25, Unit.PIXELS);
            resetThumbBtn.addStyleName("nopaddingimg");
        } else {
            resetThumbBtn.setWidth(40, Unit.PIXELS);
            resetThumbBtn.setHeight(40, Unit.PIXELS);
        }

        this.searchThumbBtn = searchingDatasetBtn.getThumbBtn();
        this.compareThumbBtn = compareBtn.getThumbBtn();
        VerticalLayout miniLayoutContainer = new VerticalLayout(homeBtn.getThumbBtn(), idDatasetBtn.getThumbBtn(), searchingDatasetBtn.getThumbBtn(), compareBtn.getThumbBtn(), resetThumbBtn);
        topRightThumbBtnsLayoutContainer = new HorizontalLayout(miniLayoutContainer);
        miniLayoutContainer.setSpacing(true);
        miniLayoutContainer.setStyleName("toprightbtnscontainer");

        AbsoluteLayout footerLayout = new AbsoluteLayout();
        footerLayout.setWidth(100, Unit.PERCENTAGE);
        footerLayout.setHeight(70, Unit.PIXELS);
        this.addComponent(footerLayout);
        VerticalLayout lineThrough = new VerticalLayout();
        lineThrough.setWidth(100, Unit.PERCENTAGE);
        lineThrough.setHeight(2, Unit.PIXELS);
        lineThrough.setStyleName("lightgraylayout");

        VerticalLayout rightHeaderLayout = new VerticalLayout();
        rightHeaderLayout.setWidth(100, Unit.PERCENTAGE);

        HorizontalLayout linksIconsLayout = new HorizontalLayout();
        linksIconsLayout.setStyleName("whitelayout");
        rightHeaderLayout.addComponent(linksIconsLayout);
        rightHeaderLayout.setComponentAlignment(linksIconsLayout, Alignment.MIDDLE_RIGHT);
        linksIconsLayout.setHeight(30, Unit.PIXELS);
        Link probe_ico = new Link(null, new ExternalResource("http://www.uib.no/rg/probe"));
        probe_ico.setIcon(new ThemeResource("img/probe-updated.png"));
        probe_ico.setTargetName("_blank");
        probe_ico.setWidth(215, Unit.PIXELS);
        probe_ico.setHeight(56, Unit.PIXELS);
        probe_ico.addStyleName("relativelocation");
        probe_ico.addStyleName("probelogo");
        linksIconsLayout.addComponent(probe_ico);

        Link uib_ico = new Link(null, new ExternalResource("http://www.uib.no/"));
        uib_ico.setIcon(new ThemeResource("img/uib-logo.svg"));
        uib_ico.setTargetName("_blank");
        uib_ico.setWidth(87, Unit.PIXELS);
        uib_ico.addStyleName("uiblogo");
        uib_ico.setHeight(58, Unit.PIXELS);
        linksIconsLayout.addComponent(uib_ico);

        Link kgj_ico = new Link(null, new ExternalResource("http://www.uib.no/en/rg/kgj-ms"));
        kgj_ico.setIcon(new ThemeResource("img/kgj.svg"));
        kgj_ico.setTargetName("_blank");
        kgj_ico.addStyleName("kgjlogo");
        kgj_ico.setHeight(58, Unit.PIXELS);
        linksIconsLayout.addComponent(kgj_ico);
        linksIconsLayout.setComponentAlignment(probe_ico, Alignment.MIDDLE_RIGHT);
        linksIconsLayout.setComponentAlignment(uib_ico, Alignment.MIDDLE_RIGHT);
        linksIconsLayout.setComponentAlignment(kgj_ico, Alignment.MIDDLE_RIGHT);

        Label para_3 = new Label("<p align='justify' Style='text-align: justify;text-justify: inter-word;font-size: 10px;color: black;/* font-weight: bold; */line-height: 20px;'><font>CSF-PR is being developed by the <a class='link' href='http://www.uib.no/rg/probe' target='_blank'>Proteomics Unit</a> at the <a class='link' href='http://www.uib.no/biomedisin/en' target='_blank'>Department of Biomedicine at the University of Bergen, Norway</a>, in close collaboration with <a class='link' href='http://www.uib.no/en/rg/kgj-ms' target='_blank'> the Kristian Gerhard Jebsen Centre for MS Research, Haukeland University Hospital, Bergen, Norway</a>.</font></p>");
        para_3.setContentMode(ContentMode.HTML);
        para_3.setWidth(45, Unit.PERCENTAGE);
        footerLayout.addComponent(para_3, "left: 0px; top: " + 0 + "px");

        CSFPR_Central_Manager.registerListener(new CSFListener() {

            @Override
            public void selectionChanged(String type) {
                if (type.equalsIgnoreCase("reset_quant_searching")) {
                    resetThumbBtn.setVisible(false);
                    searchThumbBtn.removeStyleName("selectedbtn");
                    compareThumbBtn.removeStyleName("selectedbtn");
                } else if (type.equalsIgnoreCase("quant_searching")) {
                    compareThumbBtn.removeStyleName("selectedbtn");
                    resetThumbBtn.setVisible(true);
                    searchThumbBtn.addStyleName("selectedbtn");
                } else if (type.equalsIgnoreCase("quant_compare")) {
                    resetThumbBtn.setVisible(true);
                    searchThumbBtn.removeStyleName("selectedbtn");
                    compareThumbBtn.addStyleName("selectedbtn");

                }
            }

            @Override
            public String getListenerId() {
                return "welcomehomecomponent";
            }

        });
        footerLayout.addComponent(rightHeaderLayout, "left: 0px; top: " + 5 + "px");

        String requestSearching = VaadinService.getCurrentRequest().getPathInfo();
        if (!requestSearching.trim().endsWith("/") && requestSearching.contains("searchby:") && requestSearching.contains("searchkey:")) {
            searchingDatasetBtn.excuteExternalQuery(requestSearching);
        }

    }

    private VerticalLayout updateRelaseNotesLayout() {

        VerticalLayout relaseNoteInfo = new VerticalLayout();
        relaseNoteInfo.setWidth(540, Unit.PIXELS);
//        relaseNoteInfo.setHeight(230, Unit.PIXELS);
        relaseNoteInfo.setSpacing(true);

        relaseNoteInfo.setMargin(new MarginInfo(false, true, true, true));
        relaseNoteInfo.addStyleName("lightlabel");

        Label relaseNoteHeader = new Label("Release notes");
        relaseNoteHeader.setSizeFull();
        relaseNoteHeader.addStyleName(ValoTheme.LABEL_SMALL);
        relaseNoteHeader.addStyleName(ValoTheme.LABEL_TINY);
        relaseNoteHeader.addStyleName(ValoTheme.LABEL_BOLD);
        relaseNoteInfo.addComponent(relaseNoteHeader);
        relaseNoteInfo.setExpandRatio(relaseNoteHeader, 30);
        initReleaseNote(relaseNoteInfo);

//        relaseNoteInfo.setHeight((relaseNoteInfo.getComponentCount() * 40), Unit.PIXELS);
        return relaseNoteInfo;

    }

    private HorizontalLayout initNote(String line) {
        HorizontalLayout hlo = new HorizontalLayout();
        hlo.setSizeFull();
        hlo.setSpacing(true);
        String[] textArr = line.split(" ; ");
        String date = "";
        String text = "";
        date = textArr[0];
        if (textArr.length > 1) {
            text = textArr[1].trim();
        }

        Label dateLabel = new Label(date, ContentMode.HTML);
        dateLabel.addStyleName(ValoTheme.LABEL_SMALL);
        dateLabel.addStyleName(ValoTheme.LABEL_TINY);
        hlo.addComponent(dateLabel);
        hlo.setComponentAlignment(dateLabel, Alignment.TOP_LEFT);
        hlo.setExpandRatio(dateLabel, 1.5f);
        Label textLabel = new Label(text);
        textLabel.setSizeFull();
        textLabel.setContentMode(ContentMode.HTML);
        textLabel.addStyleName(ValoTheme.LABEL_SMALL);
        textLabel.addStyleName(ValoTheme.LABEL_TINY);
        hlo.addComponent(textLabel);
        hlo.setComponentAlignment(textLabel, Alignment.TOP_RIGHT);
        hlo.setExpandRatio(textLabel, 8.5f);

        return hlo;
    }

    private void initReleaseNote(VerticalLayout relaseNoteInfo) {
        String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
        File file = new File(basepath + "/VAADIN/releasenotes.txt");
        try {
            FileReader fileReader = new FileReader(file);
            try ( // Always wrap FileReader in BufferedReader.
                    BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                String line = bufferedReader.readLine();
                HorizontalLayout relaseNote = initNote(line);//new Label("2017.01.18<font>Change disease sub-groups default order for multiple sclerosis disease in the disease sub group comparisons heat-map.<br/>Fixed proteins name and accession error in the protein table.</font>");
                relaseNoteInfo.addComponent(relaseNote);
                relaseNoteInfo.setExpandRatio(relaseNote, 30);
                relaseNoteInfo.setData(line.split(" ; ")[0].trim());
                while ((line = bufferedReader.readLine()) != null) {
                    relaseNote = initNote(line);
                    relaseNoteInfo.addComponent(relaseNote);
                    relaseNoteInfo.setExpandRatio(relaseNote, 30);
                }
                // Always close files.
            }
        } catch (FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" + "'");
        } catch (Exception ex) {
            System.out.println("Error reading file '" + "'");
        }

    }

}
