package no.uib.probe.csf.pr.touch.view.components.datasetfilters;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import no.uib.probe.csf.pr.touch.view.core.ImageContainerBtn;
import no.uib.probe.csf.pr.touch.view.core.InformationButton;
import no.uib.probe.csf.pr.touch.view.core.PopupWindowFrameWithFunctionsBtns;

/**
 **
 * This class allow the users to switch/flip selected disease sub-groups
 * comparisons.
 *
 * @author Yehia Farag
 *
 */
public abstract class GroupSwitchBtn extends ImageContainerBtn {

    /**
     * Main pop-up window frame with buttons control.
     */
    private final PopupWindowFrameWithFunctionsBtns popupWindow;
    /**
     * Main window container layout.
     */
    private final VerticalLayout popupBodyLayout;
    /**
     * List of selected comparisons to be updated based on user selection for
     * comparisons across the system.
     */
    private final Set<QuantDiseaseGroupsComparison> selectedComparisonList;
    /**
     * List of equal comparison map to avoid double selection from heat map.
     */
    private final Map<QuantDiseaseGroupsComparison, QuantDiseaseGroupsComparison> equalComparisonMap;
    /**
     * Main group comparisons container (contain the switch button).
     */
    private final GridLayout table;
    /**
     * Header I label (Numerator) .
     */
    private final Label headerI;
    /**
     * Header II label (Denominator) .
     */
    private final Label headerII;
    /**
     * List of comparisons after user updates to update comparisons across the
     * system.
     */
    private final ArrayList<QuantDiseaseGroupsComparison> updatedComparisonList;
    /**
     * Button layout container for buttons in the bottom panel.
     */
    private final HorizontalLayout btnWrapper;
    /**
     * Scrolling panel to allow overflow scroll for comparisons.
     */
    private final Panel tablePanelWrapper;
    /**
     * Main switch comparison buttons listener to handle the user selection.
     */
    private final LayoutEvents.LayoutClickListener switchListener;

    /**
     * The window width
     */
    private final int screenWidth = Math.min(Page.getCurrent().getBrowserWindowWidth(), 800);
    /**
     * The window height.
     */
    private final int screenHeight = Math.min(Page.getCurrent().getBrowserWindowHeight(), 800);

    /**
     * On click method used to update the selection comparison list and view the
     * pop up window.
     */
    @Override
    public void onClick() {
        selectedComparisonList.clear();
        selectedComparisonList.addAll(getUpdatedComparsionList());
        equalComparisonMap.clear();
        equalComparisonMap.putAll(getEqualComparsionMap());
        if (selectedComparisonList.isEmpty()) {
            return;
        }
        updateSelectionList();
        popupWindow.view();
    }

    /**
     * Constructor to initialize the main attributes.
     */
    public GroupSwitchBtn() {
        this.setHeight(40, Unit.PIXELS);
        this.setWidth(40, Unit.PIXELS);
        this.updateIcon(new ThemeResource("img/flip-v-updated.png"));
        this.setEnabled(true);
        this.setReadOnly(false);
        this.addStyleName("pointer");
        this.addStyleName("midimg");
        this.setDescription("Switch disease groups");

        //init data structure
        updatedComparisonList = new ArrayList<>();
        this.selectedComparisonList = new LinkedHashSet<>();
        this.equalComparisonMap = new HashMap<>();

        //init popup window 
        VerticalLayout frame = new VerticalLayout();
        this.popupBodyLayout = new VerticalLayout();
        frame.addComponent(popupBodyLayout);
        popupBodyLayout.setWidth(100, Unit.PERCENTAGE);
        HorizontalLayout btnsFrame = new HorizontalLayout();
        popupWindow = new PopupWindowFrameWithFunctionsBtns("Switch Disease Groups", frame, btnsFrame);
        popupWindow.setFrameWidth(screenWidth);
        headerI = new Label("Numerator");
        headerI.setStyleName(ValoTheme.LABEL_SMALL);
        headerI.addStyleName(ValoTheme.LABEL_BOLD);

        headerII = new Label("Denominator");
        headerII.setStyleName(ValoTheme.LABEL_SMALL);
        headerII.addStyleName(ValoTheme.LABEL_BOLD);

//        init table
        table = new GridLayout();
        table.setColumnExpandRatio(0, 1);
        table.setColumnExpandRatio(1, 1);
        table.setColumnExpandRatio(2, 1);
        table.setWidth(100, Unit.PERCENTAGE);
        table.setSpacing(true);
        table.setColumns(3);
        table.setRows(1000);
        table.setHeightUndefined();
        table.setHideEmptyRowsAndColumns(true);
        tablePanelWrapper = new Panel();
        tablePanelWrapper.setSizeFull();

        tablePanelWrapper.setContent(table);
        tablePanelWrapper.addStyleName(ValoTheme.PANEL_BORDERLESS);
        popupBodyLayout.addComponent(tablePanelWrapper);

        HorizontalLayout leftsideWrapper = new HorizontalLayout();
        btnsFrame.addComponent(leftsideWrapper);
        btnsFrame.setComponentAlignment(leftsideWrapper, Alignment.TOP_LEFT);
        leftsideWrapper.setSpacing(true);

        InformationButton info = new InformationButton("The order of the groups in each comparison can be switched, i.e. A vs. B or B vs. A. To switch two groups click the icon with the two arrows in between the two groups. When the desired order is achieved click the \"Apply\" button.", true);
        leftsideWrapper.addComponent(info);

        Button applyFilters = new Button("Apply");
        applyFilters.setDescription("Apply the selected filters");
        applyFilters.setStyleName(ValoTheme.BUTTON_TINY);
        applyFilters.addClickListener((Button.ClickEvent event) -> {            
            GroupSwitchBtn.this.updateComparisons(new LinkedHashSet<>(updatedComparisonList));
            popupWindow.view();
        });
        btnWrapper = new HorizontalLayout();
        btnWrapper.setWidth(100, Unit.PERCENTAGE);
        btnWrapper.addComponent(applyFilters);
        btnWrapper.setComponentAlignment(applyFilters, Alignment.TOP_RIGHT);

        btnsFrame.addComponent(btnWrapper);

        this.switchListener = GroupSwitchBtn.this::switchClick;

    }

    /**
     * On click on switch button
     *
     * @param event switch group event
     */
    private void switchClick(LayoutEvents.LayoutClickEvent event) {
        if (event.getComponent() instanceof VerticalLayout) {
            VerticalLayout switchBtn = (VerticalLayout) event.getComponent();
            if (switchBtn.getStyleName().contains("switchbtnselected")) {
                switchBtn.removeStyleName("switchbtnselected");
            } else {
                switchBtn.addStyleName("switchbtnselected");
            }
            int row = (Integer) switchBtn.getData();
            Label labelI = (Label) table.getComponent(0, row);
            Label labelII = (Label) table.getComponent(2, row);
            table.removeComponent(labelI);
            table.removeComponent(labelII);
            table.addComponent(labelI, 2, row);
            table.addComponent(labelII, 0, row);
            QuantDiseaseGroupsComparison comp = equalComparisonMap.get(updatedComparisonList.get(row - 1));
            if (comp != null) {
                updatedComparisonList.set(row - 1, comp);
            }
        }

    }

    /**
     * Get updated comparison list from the central selection manager to update
     * the comparisons panel with the latest selected comparisons
     *
     * @return Set of selected comparisons
     */
    public abstract Set<QuantDiseaseGroupsComparison> getUpdatedComparsionList();

    /**
     * Get updated comparison list from the central selection manager to update
     * the comparisons panel with the latest selected comparisons
     *
     * @return Set of selected comparisons
     */
    public abstract Map<QuantDiseaseGroupsComparison, QuantDiseaseGroupsComparison> getEqualComparsionMap();

    /**
     * Update comparisons updated comparisons list in order to update the
     * system.
     */
    private void updateSelectionList() {
        table.removeAllComponents();
        updatedComparisonList.clear();
        int row = 1;
        setWindowHight(selectedComparisonList.size());

        table.addComponent(headerI, 0, 0);
        table.setComponentAlignment(headerI, Alignment.TOP_CENTER);

        VerticalLayout spacer = new VerticalLayout();
        spacer.setSizeFull();
        table.addComponent(spacer, 1, 0);
        table.addComponent(headerII, 2, 0);
        table.setComponentAlignment(headerII, Alignment.TOP_CENTER);

        for (QuantDiseaseGroupsComparison comparison : selectedComparisonList) {

            updatedComparisonList.add(comparison);
            if (!comparison.isSwitchable()) {
                continue;
            }
            String header = comparison.getComparisonHeader();
            String updatedHeaderI = header.split(" / ")[0].split("__")[0];
            String updatedHeaderII = header.split(" / ")[1].split("__")[0];
            String diseaseColor = comparison.getDiseaseCategoryColor();
            Label labelI = new Label("<font  style='!important; color:" + diseaseColor + "'>" + updatedHeaderI + "</font>");
            labelI.setStyleName(ValoTheme.LABEL_SMALL);
            labelI.addStyleName("bottomborder");
            labelI.setDescription(comparison.getComparisonFullName().split(" / ")[0].split("__")[0]);
            Label labelII = new Label("<font  style='color:" + diseaseColor + "'>" + updatedHeaderII + "</font>");
            labelII.setStyleName(ValoTheme.LABEL_SMALL);
            labelII.addStyleName("bottomborder");
            labelII.setDescription(comparison.getComparisonFullName().split(" / ")[1].split("__")[0]);
            labelI.setContentMode(ContentMode.HTML);
            labelII.setContentMode(ContentMode.HTML);
            VerticalLayout switchBtn = swichIconGenerator();
            table.addComponent(labelI, 0, row);
            table.addComponent(switchBtn, 1, row);
            switchBtn.setData(row);
            table.setComponentAlignment(switchBtn, Alignment.MIDDLE_CENTER);
            table.addComponent(labelII, 2, row++);
            table.setComponentAlignment(labelII, Alignment.TOP_RIGHT);
        }

    }

    /**
     * Update windows height and its container layout based on the number of
     * comparisons in the system
     *
     * @param itemsNumber number of rows
     */
    private void setWindowHight(int itemsNumber) {
        int itemH = (27 * (itemsNumber + 1));
        int height = Math.min(screenHeight - 230, itemH);
        tablePanelWrapper.setHeight(height, Unit.PIXELS);
        popupWindow.setFrameHeight((int) tablePanelWrapper.getHeight() + 239 - 65);

    }

    /**
     * Create Switch comparisons button
     *
     * @return Switch button (vertical layout)
     */
    private VerticalLayout swichIconGenerator() {

        VerticalLayout switchBtn = new VerticalLayout();
        switchBtn.setStyleName("switchbtn");
        switchBtn.setDescription("Click to switch groups");
        switchBtn.setWidth(50, Unit.PIXELS);
        switchBtn.setHeight(25, Unit.PIXELS);
        switchBtn.addLayoutClickListener(switchListener);
        return switchBtn;

    }

    /**
     * Update the selection manager with the updated list
     *
     * @param updatedComparisonList list of updated quant disease comparisons
     */
    public abstract void updateComparisons(LinkedHashSet<QuantDiseaseGroupsComparison> updatedComparisonList);

}
