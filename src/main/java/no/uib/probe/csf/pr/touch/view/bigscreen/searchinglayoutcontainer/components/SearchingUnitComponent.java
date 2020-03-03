package no.uib.probe.csf.pr.touch.view.bigscreen.searchinglayoutcontainer.components;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.Set;
import no.uib.probe.csf.pr.touch.database.Query;

/**
 * This Class represents searching unit layout where the user can enter his
 * input data to be searched and queried against the CSF-PR 2.0 resource
 * database
 *
 * @author Yehia Farag
 */
public abstract class SearchingUnitComponent extends VerticalLayout implements Button.ClickListener {

    /**
     * Input text area for letting users to enter keywords (protein name,
     * accession, or peptide sequence), the system support multiple keywords
     * from the same searching category.
     */
    private final TextArea searchingArea;
    /**
     * Input option group for searching type (protein name, accession, or
     * peptide sequence).
     */
    private final OptionGroup searchByOptionGroup;
    /**
     * Input option group for disease category (Alzheimer's, Multiple Sclerosis
     * or Parkinson's).
     */
    private final OptionGroup diseaseCategoryOption;
    /**
     * Main query object that has all information required for searching
     * process.
     */
    private final Query query;
    /**
     * Label to show user input data errors.
     */
    private final Label errorLabel;

    /**
     * Get main query object that has all information required for searching
     * process
     *
     * @return query Query object that has all information required for
     * searching process
     */
    public Query getQuery() {
        return query;
    }

    /**
     * Clear all input fields.
     */
    public void reset() {
        searchingArea.clear();
        searchByOptionGroup.setValue("Protein Name");
        errorLabel.setVisible(false);
        searchByOptionGroup.setRequired(false);
        resetSearching();
        removeStyleName("resizeto120");

    }

    /**
     * Clear all input fields.
     */
    public abstract void resetSearching();

    /**
     * Constructor to initialize the main attributes (width and height)
     *
     * @param width Available width for the layout
     * @param height Available height for the layout
     *
     */
    public SearchingUnitComponent(int width, int height) {
        this.setWidth(width, Unit.PIXELS);
        this.setHeight(height, Unit.PIXELS);
        this.addStyleName("roundedborder");
        this.addStyleName("whitelayout");
        this.addStyleName("padding20");
        this.addStyleName("scrollable");
        this.addStyleName("margin");
        this.setSpacing(true);

        VerticalLayout frame = new VerticalLayout();
        frame.setWidth(100, Unit.PERCENTAGE);
        frame.setHeightUndefined();
        this.addComponent(frame);

        searchingArea = new TextArea();
        searchingArea.setWidth(90, Unit.PERCENTAGE);

        searchingArea.setHeight(100, Unit.PIXELS);

        searchingArea.setStyleName(ValoTheme.TEXTAREA_ALIGN_CENTER);
        searchingArea.setInputPrompt("Use one key-word per line and choose the search by option");
        searchingArea.addStyleName("maxwidth600");
        frame.addComponent(searchingArea);
        frame.setComponentAlignment(searchingArea, Alignment.TOP_CENTER);

        HorizontalLayout btnsLayoutContainer = new HorizontalLayout();
        btnsLayoutContainer.setWidth(90, Unit.PERCENTAGE);
        btnsLayoutContainer.addStyleName("maxwidth600");
        btnsLayoutContainer.setHeight(70, Unit.PIXELS);
        frame.addComponent(btnsLayoutContainer);
        frame.setComponentAlignment(btnsLayoutContainer, Alignment.TOP_CENTER);

        searchByOptionGroup = new OptionGroup();
        searchByOptionGroup.addItem("Protein Accession");
        searchByOptionGroup.addItem("Protein Name");
        searchByOptionGroup.addItem("Peptide Sequence");
        searchByOptionGroup.select("Protein Name");
        searchByOptionGroup.setStyleName(ValoTheme.OPTIONGROUP_SMALL);
        btnsLayoutContainer.addComponent(searchByOptionGroup);
        searchByOptionGroup.setRequired(false);
        searchByOptionGroup.setRequiredError("You have to select an option");

        diseaseCategoryOption = new OptionGroup();
         diseaseCategoryOption.addItem("Multiple Sclerosis");
         diseaseCategoryOption.addItem("Parkinson's");
         diseaseCategoryOption.addItem("Alzheimer's");  
        diseaseCategoryOption.addItem("Amyotrophic Lateral Sclerosis");
        diseaseCategoryOption.setItemCaption("Amyotrophic Lateral Sclerosis", "ALS");
        diseaseCategoryOption.setStyleName(ValoTheme.OPTIONGROUP_SMALL);
        diseaseCategoryOption.setNullSelectionAllowed(true);
        btnsLayoutContainer.addComponent(diseaseCategoryOption);
        diseaseCategoryOption.setRequired(false);
        diseaseCategoryOption.setMultiSelect(true);

        VerticalLayout rightBtnContainer = new VerticalLayout();
        rightBtnContainer.setSizeFull();
        btnsLayoutContainer.addComponent(rightBtnContainer);
        btnsLayoutContainer.setComponentAlignment(rightBtnContainer, Alignment.TOP_RIGHT);

        HorizontalLayout btnWrapper = new HorizontalLayout();
        btnWrapper.setSpacing(true);
        rightBtnContainer.addComponent(btnWrapper);
        rightBtnContainer.setComponentAlignment(btnWrapper, Alignment.TOP_RIGHT);

        Button sampleBtn = new Button("Load Example Data");
        sampleBtn.setStyleName(ValoTheme.BUTTON_LINK);
        sampleBtn.addStyleName(ValoTheme.BUTTON_TINY);
        sampleBtn.addStyleName("nomargin");
        sampleBtn.addStyleName("nopadding");
        sampleBtn.addStyleName("margintop");
        sampleBtn.setDescription("Load example data");
        btnWrapper.addComponent(sampleBtn);
        btnWrapper.setComponentAlignment(sampleBtn, Alignment.BOTTOM_LEFT);

        sampleBtn.addClickListener((Button.ClickEvent event) -> {
            SearchingUnitComponent.this.reset();
            diseaseCategoryOption.select("Alzheimer's");
            diseaseCategoryOption.select("Multiple Sclerosis");
            searchByOptionGroup.select("Protein Name");
            searchingArea.setValue("serum albumin\n"
                    + "hemoglobin\n"
                    + "albumin\n"
                    + "random keyword");

        });

        VerticalLayout spacer = new VerticalLayout();
        spacer.setWidth(2, Unit.PIXELS);
        spacer.setHeight(10, Unit.PIXELS);
        btnWrapper.addComponent(spacer);
        btnWrapper.setComponentAlignment(spacer, Alignment.TOP_CENTER);

        Button searchingBtn = new Button("Search");
        searchingBtn.setStyleName(ValoTheme.BUTTON_SMALL);
        searchingBtn.addStyleName(ValoTheme.BUTTON_PRIMARY);
        searchingBtn.addStyleName("margintop");
        btnWrapper.addComponent(searchingBtn);
        btnWrapper.setComponentAlignment(searchingBtn, Alignment.TOP_RIGHT);
        searchingBtn.addClickListener(SearchingUnitComponent.this);

        HorizontalLayout bottomLayout = new HorizontalLayout();
        frame.addComponent(bottomLayout);
        frame.setComponentAlignment(bottomLayout, Alignment.TOP_CENTER);
        bottomLayout.setWidth(90, Unit.PERCENTAGE);
        bottomLayout.addStyleName("maxwidth600");

        HorizontalLayout bottomLayoutWrapper = new HorizontalLayout();
        bottomLayoutWrapper.setSpacing(true);
        bottomLayout.addComponent(bottomLayoutWrapper);
        bottomLayout.setComponentAlignment(bottomLayoutWrapper, Alignment.TOP_LEFT);

        errorLabel = new Label("error");
        errorLabel.setStyleName(ValoTheme.LABEL_FAILURE);
        errorLabel.addStyleName("smallerrorlabel");
        errorLabel.setVisible(false);
        rightBtnContainer.addComponent(errorLabel);
        rightBtnContainer.setComponentAlignment(errorLabel, Alignment.MIDDLE_CENTER);

        query = new Query();

    }

    /**
     * On click on searching button capture all the input data and construct
     * query to be used for comparison process
     *
     * @param event Searching button click event
     */
    @Override
    public void buttonClick(Button.ClickEvent event) {
        errorLabel.setVisible(false);
        searchByOptionGroup.setRequired(true);
        searchByOptionGroup.commit();
        if (searchingArea.getValue() != null && !searchingArea.getValue().trim().equalsIgnoreCase("") && searchByOptionGroup.isValid()) {

            searchByOptionGroup.setRequired(false);
            String searchKeyWords = searchingArea.getValue();
            String updatedSearchingKey = searchKeyWords.replace("\n", ",");
            String ser = "";
            String[] keyWordArr = updatedSearchingKey.trim().split(",");
            for (String str : keyWordArr) {
                if (str.trim().length() == 0) {
                    continue;
                }
                if (str.trim().length() < 4) {
                    if (keyWordArr.length == 1) {
                        errorLabel.setValue("Not valid keywords");
                        errorLabel.setVisible(true);
                        return;
                    }
                    ser += " -" + str.replace(" -", "") + " -\n";
                    continue;
                }
                ser += str.trim() + "\n";
            }
            searchKeyWords = ser;
            query.setSearchKeyWords(searchKeyWords);
            query.setSearchBy(searchByOptionGroup.getValue().toString());
            query.setDiseaseCategorys((Set<Object>) diseaseCategoryOption.getValue());
           
            search(query);

        } else {
            this.removeStyleName("resizeto120");
            errorLabel.setValue("Check keywords and searching by option");
            errorLabel.addStyleName("nomargin");
            errorLabel.setVisible(true);
        }

    }

    /**
     * Start searching process
     *
     * @param query Query object that has all information required for searching
     * process
     */
    public abstract void search(Query query);

    /**
     * Get input option group for disease category (AD,MS or PD..etc)
     *
     * @return diseaseCategoryOption disease category group option filter.
     */
    public OptionGroup getDiseaseCategoryOption() {
        return diseaseCategoryOption;
    }

   /**
     * Execute searching query      *
     * @param query string to be executed against the database
     */
    public void excuteExternalQuery(String query) {
        query = query.replace("*", " ");
        String searchBy = query.split("___")[0].replace("searchby:", "").replace("/", "");
        searchByOptionGroup.setValue(searchBy);        
        String skeyWord = query.split("___")[1].replace("searchkey:", "").replace("__", "\n");
        searchingArea.setValue(skeyWord);        
        buttonClick(null);
    }

}
