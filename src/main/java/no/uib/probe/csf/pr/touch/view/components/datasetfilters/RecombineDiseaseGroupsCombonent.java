package no.uib.probe.csf.pr.touch.view.components.datasetfilters;

import com.vaadin.data.Property;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseCategoryObject;
import no.uib.probe.csf.pr.touch.view.core.DiseaseGroupLabel;
import no.uib.probe.csf.pr.touch.view.core.InformationButton;
import no.uib.probe.csf.pr.touch.view.core.PopupWindowFrameWithFunctionsBtns;

/**
 * This class represents disease sub group rename and re-combine into new
 * customized disease sub-groups
 *
 * @author Yehia Farag
 */
public abstract class RecombineDiseaseGroupsCombonent extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    /**
     *Main pop-up window frame with buttons control  .
     */
    private final PopupWindowFrameWithFunctionsBtns popupWindow;
    /**
     *Main window container layout.
     */
    private final VerticalLayout popupBodyLayout;
    /**
     * Drop down list of disease category (MS,AD,PD..etc)to let the user select disease groups to re-combine.
     */
    private final ComboBox diseaseTypeSelectionList;
    /**
     * Map of updated comparisons title and number of updated comparisons (used for adding '*' to the comparison title).
     */
    private final Map<String, Integer> captionAstrMap;
    /**
     *The window width.
     */
    private final int screenWidth = Math.min(Page.getCurrent().getBrowserWindowWidth(), 600);
    /**
     *The window height.
     */
    private final int screenHeight = Math.min(Page.getCurrent().getBrowserWindowHeight(), 800);
    /**
     *The maximum window height.
     */
    private final int maxHeight;
    /**
     *Scrolling panel to allow overflow scroll for comparisons table.
     */
    private final Panel diseaseGroupsNamesPanel;
    /**
     *Map of disease category  title and comparison container layout.
     */
    private final Map<String, HorizontalLayout> diseaseGroupsGridLayoutMap = new HashMap<>();
    /**
     *Map of disease category  title and map of sub disease group (title to drop down list).
     */
    private final Map<String, Map<String, ComboBox>> diseaseGroupsSelectionListMap = new HashMap<>();

    /**
     *A set of disease sub-group names list.
     */
    private final Set<String> subGroupList = new LinkedHashSet<>();

    /**
     * Constructor to initialize the main attributes
     *
     * @param diseaseCategorySet set of disease category objects that has all
     * disease category information and styling information
     */
    public RecombineDiseaseGroupsCombonent(Collection<DiseaseCategoryObject> diseaseCategorySet) {

        maxHeight = screenHeight - 220;
        this.setDescription("Recombine disease groups");
        this.setStyleName("filterbtn");
        Image icon = new Image();
        icon.setStyleName("combinegroupbtn");
        icon.setSource(new ThemeResource("img/connect-o.png"));
        this.addComponent(icon);
        this.setComponentAlignment(icon, Alignment.TOP_CENTER);
        icon.setWidth(100, Unit.PERCENTAGE);
        icon.setHeight(100, Unit.PERCENTAGE);
        this.addLayoutClickListener(RecombineDiseaseGroupsCombonent.this);

        //init window layout 
        popupBodyLayout = new VerticalLayout();
        HorizontalLayout btnsFrame = new HorizontalLayout();
        popupWindow = new PopupWindowFrameWithFunctionsBtns("Recombine Disease Groups", new VerticalLayout(popupBodyLayout), btnsFrame);
        popupWindow.setFrameWidth(screenWidth);

        diseaseTypeSelectionList = new ComboBox();

        captionAstrMap = new HashMap<>();
        diseaseGroupsNamesPanel = new Panel();
        diseaseGroupsNamesPanel.setWidth(100, Unit.PERCENTAGE);
        diseaseGroupsNamesPanel.setStyleName(ValoTheme.PANEL_BORDERLESS);
        initPopupLayout(diseaseCategorySet);
        HorizontalLayout leftsideWrapper = new HorizontalLayout();
        btnsFrame.addComponent(leftsideWrapper);
        btnsFrame.setComponentAlignment(leftsideWrapper, Alignment.TOP_LEFT);
        btnsFrame.setExpandRatio(leftsideWrapper, 30);
        leftsideWrapper.setSpacing(true);

        InformationButton info = new InformationButton("The disease group names can be altered by the user or reset to the names used in the original publications. If the same name is used for more than one group those groups will be merged into a single group in the other displays. After altering the names click the \"Update\" button.", true);
        leftsideWrapper.addComponent(info);

        HorizontalLayout bottomContainert = new HorizontalLayout();
        bottomContainert.setWidth(100, Unit.PERCENTAGE);
        bottomContainert.setHeight(100, Unit.PERCENTAGE);
        HorizontalLayout btnLayout = new HorizontalLayout();

        btnsFrame.addComponent(btnLayout);
        btnsFrame.setComponentAlignment(btnLayout, Alignment.TOP_RIGHT);
        btnsFrame.setExpandRatio(btnLayout, 330);
        btnLayout.setSpacing(true);

        Button resetFiltersBtn = new Button("Suggested Groups");
        resetFiltersBtn.setStyleName(ValoTheme.BUTTON_TINY);
        btnLayout.addComponent(resetFiltersBtn);

        resetFiltersBtn.setDescription("Use CSF-PR suggested group names");
        resetFiltersBtn.addClickListener((Button.ClickEvent event) -> {
            resetToDefault();
        });
        Button resetToOriginalBtn = new Button("Original Names");
        resetToOriginalBtn.setStyleName(ValoTheme.BUTTON_TINY);
        btnLayout.addComponent(resetToOriginalBtn);

        resetToOriginalBtn.setDescription("Reset group names to original publication names");
        resetToOriginalBtn.addClickListener((Button.ClickEvent event) -> {
            resetToPublicationsNames();
        });

        Button applyFilters = new Button("Apply");
        applyFilters.setDescription("Update disease groups with the selected names");
        applyFilters.setStyleName(ValoTheme.BUTTON_TINY);

        btnLayout.addComponent(applyFilters);
        applyFilters.addClickListener((Button.ClickEvent event) -> {
            updateGroups();
        });
    }

    /**
     * Initialize the pop up window layout
     *
     * @param diseaseCategorySet set of disease category objects that has all
     * disease category information and styling information
     */
    private void initPopupLayout(Collection<DiseaseCategoryObject> diseaseCategorySet) {

        diseaseTypeSelectionList.setDescription("Select disease category");
        for (DiseaseCategoryObject disease : diseaseCategorySet) {
       
            diseaseTypeSelectionList.addItem(disease.getDiseaseCategory());
            diseaseTypeSelectionList.setItemCaption(disease.getDiseaseCategory(), disease.getDiseaseCategory());

        }

        HorizontalLayout diseaseCategorySelectLayout = new HorizontalLayout();
        diseaseCategorySelectLayout.setWidthUndefined();
        diseaseCategorySelectLayout.setHeightUndefined();
        diseaseCategorySelectLayout.setSpacing(true);
        diseaseCategorySelectLayout.setMargin(false);

        popupBodyLayout.addComponent(diseaseCategorySelectLayout);
        popupBodyLayout.setComponentAlignment(diseaseCategorySelectLayout, Alignment.TOP_LEFT);

        Label title = new Label("Disease Category");
        title.setStyleName(ValoTheme.LABEL_SMALL);
        title.addStyleName(ValoTheme.LABEL_BOLD);
        title.addStyleName("lineheight25");
        title.setWidth(130, Unit.PIXELS);
        diseaseCategorySelectLayout.addComponent(title);
        diseaseCategorySelectLayout.setComponentAlignment(title, Alignment.TOP_CENTER);
        diseaseTypeSelectionList.setWidth((int) (screenWidth - 130 - 93), Unit.PIXELS);
        diseaseTypeSelectionList.setNullSelectionAllowed(false);
        diseaseTypeSelectionList.setValue("All Diseases");
        diseaseTypeSelectionList.setImmediate(true);
        diseaseTypeSelectionList.setNewItemsAllowed(false);
        diseaseTypeSelectionList.setTextInputAllowed(false);
        diseaseTypeSelectionList.setStyleName(ValoTheme.COMBOBOX_TINY);
        diseaseCategorySelectLayout.addComponent(diseaseTypeSelectionList);
        diseaseCategorySelectLayout.setComponentAlignment(diseaseTypeSelectionList, Alignment.TOP_LEFT);

        diseaseTypeSelectionList.addValueChangeListener((Property.ValueChangeEvent event) -> {
            boolean showAll = false;
            String value = event.getProperty().getValue().toString();
            if (value.equalsIgnoreCase("All Diseases")) {
                showAll = true;
            }
            int heightColc = 0;
            for (String dName : diseaseGroupsGridLayoutMap.keySet()) {
                if (dName.equalsIgnoreCase(value) || showAll) {
                    diseaseGroupsGridLayoutMap.get(dName).setVisible(true);
                    heightColc += (Integer) diseaseGroupsGridLayoutMap.get(dName).getData();
                } else {
                    diseaseGroupsGridLayoutMap.get(dName).setVisible(false);
                }
            }

            diseaseGroupsNamesPanel.setHeight(Math.min(maxHeight - 80, heightColc), Unit.PIXELS);
            popupWindow.setFrameHeight((int) diseaseGroupsNamesPanel.getHeight() + (305 - 60));
        });

        VerticalLayout diseaseGroupsNamesContainer = new VerticalLayout();
        diseaseGroupsNamesContainer.addStyleName("paddingleft20");
        diseaseGroupsNamesContainer.setWidth(100, Unit.PERCENTAGE);
        diseaseGroupsNamesContainer.setHeightUndefined();
        popupBodyLayout.addComponent(diseaseGroupsNamesContainer);
        diseaseGroupsNamesContainer.setStyleName("whitelayout");
        GridLayout diseaseNamesHeader = new GridLayout(2, 1);

        diseaseNamesHeader.setWidth((screenWidth - 90), Unit.PIXELS);
        diseaseNamesHeader.setHeightUndefined();
        diseaseNamesHeader.setSpacing(true);
        diseaseNamesHeader.setMargin(false);
        diseaseNamesHeader.addStyleName("margintop15");
        diseaseNamesHeader.addStyleName("marginbottom");
        diseaseGroupsNamesContainer.addComponent(diseaseNamesHeader);
        Label col1Label = new Label("Original  Name");

        col1Label.setStyleName(ValoTheme.LABEL_SMALL);
        col1Label.addStyleName(ValoTheme.LABEL_BOLD);
        col1Label.setWidthUndefined();
        diseaseNamesHeader.addComponent(col1Label, 0, 0);
        diseaseNamesHeader.setComponentAlignment(col1Label, Alignment.TOP_LEFT);

        Label col2Label = new Label("Group Name");

        col2Label.setStyleName(ValoTheme.LABEL_SMALL);
        col2Label.addStyleName(ValoTheme.LABEL_BOLD);
        diseaseNamesHeader.addComponent(col2Label, 1, 0);
        col2Label.setWidthUndefined();
        diseaseNamesHeader.setComponentAlignment(col2Label, Alignment.TOP_LEFT);

        diseaseGroupsNamesContainer.addComponent(diseaseGroupsNamesPanel);

        VerticalLayout diseaseNamesUpdateContainerLayout = new VerticalLayout();

        diseaseNamesUpdateContainerLayout.setWidth(100, Unit.PERCENTAGE);
        diseaseNamesUpdateContainerLayout.setMargin(false);
        diseaseCategorySet.stream().filter((diseaseCategory) -> !(diseaseCategory.getDiseaseCategory().equalsIgnoreCase("All Diseases"))).forEach((diseaseCategory) -> {
          
            HorizontalLayout diseaseNamesUpdateContainer = initDiseaseNamesUpdateContainer(diseaseCategory, (screenWidth - 110));
            diseaseNamesUpdateContainerLayout.addComponent(diseaseNamesUpdateContainer);
            diseaseNamesUpdateContainerLayout.setComponentAlignment(diseaseNamesUpdateContainer, Alignment.TOP_CENTER);
            diseaseGroupsGridLayoutMap.put(diseaseCategory.getDiseaseCategory(), diseaseNamesUpdateContainer);
        });
        diseaseGroupsNamesPanel.setContent(diseaseNamesUpdateContainerLayout);
        diseaseGroupsNamesPanel.setHeight((maxHeight - 80), Unit.PIXELS);
        popupWindow.setFrameHeight((int) diseaseGroupsNamesPanel.getHeight() + (305 - 60));

        resetToDefault();

    }

    /**
     * Initialize the sub disease group containers
     *
     * @param diseaseCategory the disease category objects has all disease
     * category information and styling information
     * @param width Available layout width
     */
    private HorizontalLayout initDiseaseNamesUpdateContainer(DiseaseCategoryObject diseaseCategory, int width) {
        GridLayout diseaseNamesUpdateContainer = new GridLayout(2, (diseaseCategory.getDiseaseSubGroups().size() * 2));
        diseaseNamesUpdateContainer.setWidth(width, Unit.PIXELS);
        diseaseNamesUpdateContainer.setHeightUndefined();
        diseaseNamesUpdateContainer.setSpacing(false);
        int labelWidth = (width - 25) / 2;
        int widthCalc = 0;
        int row = 0;
        int col = 0;
        subGroupList.clear();
        Map<String, ComboBox> diseaseGroupNameToListMap = new LinkedHashMap<>();
        for (String diseaseGroupName : diseaseCategory.getDiseaseSubGroups().keySet()) {
           
            VerticalLayout label = generateLabel(diseaseGroupName, diseaseCategory.getDiseaseSubGroupsToFullName().get(diseaseGroupName), diseaseCategory.getDiseaseStyleName(), labelWidth - 8);
            diseaseNamesUpdateContainer.addComponent(label, col, row);

            ComboBox list = generateLabelList(diseaseCategory, diseaseGroupName, labelWidth - 8);
            list.addStyleName("marginleft-24");
            diseaseNamesUpdateContainer.addComponent(list, col + 1, row);
            diseaseNamesUpdateContainer.setComponentAlignment(list, Alignment.TOP_LEFT);
            diseaseGroupNameToListMap.put(diseaseGroupName, list);

            col = 0;
            row++;

            VerticalLayout spacer1 = new VerticalLayout();
            spacer1.setHeight(2, Unit.PIXELS);
            spacer1.setWidth(10, Unit.PIXELS);
            spacer1.setStyleName("whitelayout");
            diseaseNamesUpdateContainer.addComponent(spacer1, col, row);
            VerticalLayout spacer2 = new VerticalLayout();
            spacer2.setHeight(2, Unit.PIXELS);
            spacer2.setWidth(10, Unit.PIXELS);
            spacer2.setStyleName("whitelayout");
            diseaseNamesUpdateContainer.addComponent(spacer2, col + 1, row);

            col = 0;
            row++;
            widthCalc += 26;

        }

        widthCalc -= 2;
        diseaseGroupsSelectionListMap.put(diseaseCategory.getDiseaseCategory(), diseaseGroupNameToListMap);
        VerticalLayout diseaseLabelContainer = new VerticalLayout();
        Label label = new Label("<center><font  color='#ffffff'>" + diseaseCategory.getDiseaseCategory() + "</font></center>");
        label.setContentMode(ContentMode.HTML);
        diseaseLabelContainer.setHeight(widthCalc, Unit.PIXELS);
        diseaseLabelContainer.setWidth(20, Unit.PIXELS);
        VerticalLayout rotateContainer = new VerticalLayout();
        rotateContainer.setWidth(widthCalc, Unit.PIXELS);
        rotateContainer.setHeight(24, Unit.PIXELS);
        diseaseLabelContainer.addComponent(rotateContainer);
        rotateContainer.setStyleName(diseaseCategory.getDiseaseStyleName());
        rotateContainer.addStyleName("rotateheader");
        rotateContainer.addComponent(label);
        rotateContainer.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidthUndefined();
        layout.setSpacing(true);
        layout.addComponent(diseaseLabelContainer);
        layout.addComponent(diseaseNamesUpdateContainer);
        layout.setData((((row + 2) / 2) * 25));

        return layout;
    }

    /**
     * Generate disease sub-group label layout
     *
     * @param strLabel the title of the disease sub group
     * @param description the sub-group full name
     * @param diseaseStyle the CSS style name for the disease
     * @param width  Width of the label
     */
    private VerticalLayout generateLabel(String strLabel, String description, String diseaseStyle, int width) {
        DiseaseGroupLabel container = new DiseaseGroupLabel(strLabel, diseaseStyle);
        container.setMargin(false);
        container.setWidth(width, Unit.PIXELS);
        container.setDescription(description);
        return container;
    }

    /**
     * Generate disease sub-group drop down list that allow user to re-combine
     * the disease groups or rename the disease sub-group name
     *
     * @param diseaseCategory the disease category objects has all disease
     * category information and styling information
     * @param diseaseSubGroup the title of the disease sub group
     * @param width Width of the label
     */
    private ComboBox generateLabelList(DiseaseCategoryObject diseaseCategory, String diseaseSubGroup, int width) {
        final ComboBox diseaseGroupsList = new ComboBox();
        diseaseGroupsList.setStyleName(ValoTheme.COMBOBOX_TINY);
        diseaseGroupsList.setStyleName(ValoTheme.COMBOBOX_SMALL);
        diseaseGroupsList.setWidth(width, Unit.PIXELS);
        diseaseGroupsList.setNullSelectionAllowed(false);
        diseaseGroupsList.setImmediate(true);
        diseaseGroupsList.setNewItemsAllowed(true);
        diseaseGroupsList.setPageLength(500);
        diseaseGroupsList.setInputPrompt("Select or enter new disease group name");
        diseaseGroupsList.setDescription("Select or enter new disease group name");

        String defaultValue = diseaseCategory.getDiseaseSubGroups().get(diseaseSubGroup);
        diseaseGroupsList.setData(defaultValue);

        diseaseGroupsList.addItem(defaultValue);
        captionAstrMap.put(defaultValue, 0);
        subGroupList.add(defaultValue);

        for (String name : diseaseCategory.getDiseaseSubGroups().keySet()) {
            subGroupList.add(name);
            diseaseGroupsList.addItem(name);
            if (!captionAstrMap.containsKey(name)) {
                captionAstrMap.put(name, 0);
            }

        }
        subGroupList.add("Healthy*");
        diseaseGroupsList.addItem("Healthy*");
        if (!captionAstrMap.containsKey("Healthy*")) {
            captionAstrMap.put("Healthy*", 0);
        }
        diseaseGroupsList.setNewItemHandler((String newItemCaption) -> {
            String ast = "";
            if (!newItemCaption.contains("*")) {
                ast = "*";
            }
            diseaseGroupsList.addItem(newItemCaption + ast);
            diseaseGroupsList.select(newItemCaption + ast);
            captionAstrMap.put(newItemCaption + ast, 1);
        });
        diseaseGroupsList.setHeight(24, Unit.PIXELS);
        diseaseGroupsList.addValueChangeListener(new Property.ValueChangeListener() {
            private String lastSelected = "";

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (event.getProperty().getValue() == null) {
                    return;
                }
                String selectedItem = event.getProperty().getValue().toString();
                if (!selectedItem.equalsIgnoreCase(diseaseGroupsList.getData().toString())) {
                    String astr = "";
                    if (!selectedItem.contains("*")) {
                        astr = "*";
                        captionAstrMap.put(selectedItem, captionAstrMap.get(selectedItem) + 1);
                        updateAllList(selectedItem, selectedItem + astr);
                    }
                    diseaseGroupsList.setItemCaption(selectedItem, selectedItem + astr);
                } else {
                    diseaseGroupsList.setItemCaption(diseaseGroupsList.getData().toString(), diseaseGroupsList.getData().toString());
                }
                if (!lastSelected.equalsIgnoreCase("")) {
                    captionAstrMap.put(lastSelected, Math.max(captionAstrMap.get(lastSelected) - 1, 0));
                }
                lastSelected = selectedItem;
            }
        });

        return diseaseGroupsList;
    }

    /**
     * Update the drop down list of disease sub-groups on entering new sub-group
     * name
     *
     * @param itemId the item in the drop down list to be updated
     * @param itemCap the item in the drop down list caption to be updated
     */
    private void updateAllList(String itemId, String itemCap) {
        for (String diseaseKey : diseaseGroupsSelectionListMap.keySet()) {
            Map<String, ComboBox> diseaseGroupNameToListMap = diseaseGroupsSelectionListMap.get(diseaseKey);
            for (String key : diseaseGroupNameToListMap.keySet()) {
                ComboBox list = diseaseGroupNameToListMap.get(key);
                list.setItemCaption(itemId, itemCap);
            }
        }

    }

    /**
     * On click on the re-combine button
     *
     * @param event User click event
     */
    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        popupWindow.view();
    }

    /**
     * Reset the sub-group comparisons in all disease categories to CSF-PR
     * recommended names.
     */
    private void resetToDefault() {
        String selectedDiseaseKey = diseaseTypeSelectionList.getValue().toString().trim();

        if (selectedDiseaseKey.equalsIgnoreCase("All Diseases")) {
            for (String diseaseKey : diseaseGroupsSelectionListMap.keySet()) {
                Map<String, ComboBox> diseaseGroupNameToListMap = diseaseGroupsSelectionListMap.get(diseaseKey);
                for (String key : diseaseGroupNameToListMap.keySet()) {
                    ComboBox list = diseaseGroupNameToListMap.get(key);
                    list.setValue(list.getData() + "");
                }
            }
        } else {
            Map<String, ComboBox> diseaseGroupNameToListMap = diseaseGroupsSelectionListMap.get(selectedDiseaseKey);
            for (String key : diseaseGroupNameToListMap.keySet()) {
                ComboBox list = diseaseGroupNameToListMap.get(key);
                list.setValue(list.getData() + "");

            }

        }

    }

    /**
     * update the drop down list of group names on applying filter.
     */
    private void updateGroups() {
        Map<String, Map<String, String>> updatedGroupsNamesMap = new HashMap<>();

        diseaseGroupsSelectionListMap.keySet().stream().forEach((diseaseKey) -> {           
            
            Map<String, ComboBox> diseaseGroupNameToListMap = diseaseGroupsSelectionListMap.get(diseaseKey);
            Map<String, String> updatedDiseaseGroupsMappingName = new LinkedHashMap<>();
            diseaseGroupNameToListMap.keySet().stream().forEach((key) -> {
                ComboBox list = diseaseGroupNameToListMap.get(key);
                String selection;
                if (list.getValue() != null) {
                    selection = list.getItemCaption(list.getValue()).trim();
                } else {
                    selection = list.getData().toString();
                }
                updatedDiseaseGroupsMappingName.put(key, selection);
            });

            updatedGroupsNamesMap.put(diseaseKey, updatedDiseaseGroupsMappingName);
        });

        updateSystem(updatedGroupsNamesMap);
        popupWindow.view();

    }

    /**
     * Reset the sub-group comparisons in all disease categories to the original
     * publication names.
     */
    private void resetToPublicationsNames() {
        updateSystem(null);
        popupWindow.view();

    }

    /**
     * Update the selection manager with the updated sup group comparisons names
     * for each disease category
     *
     * @param updatedGroupsNamesMap Map of disease category to new sub-disease category map 
     */
    public abstract void updateSystem(Map<String, Map<String, String>> updatedGroupsNamesMap);

    /**
     * Update the main icon button for the filters based on the container size
     *
     * @param resizeFactor Resize4 factor to resize the button layout
     */
    public void resizeFilter(double resizeFactor) {
        this.setWidth((int) (25 * resizeFactor), Unit.PIXELS);
        this.setHeight((int) (25 * resizeFactor), Unit.PIXELS);
    }

}
