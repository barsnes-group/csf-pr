package no.uib.probe.csf.pr.touch.view.components.peptideviewsubcomponents;

import com.vaadin.data.Item;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.QuantComparisonProtein;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDataset;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import no.uib.probe.csf.pr.touch.logic.beans.QuantPeptide;
import no.uib.probe.csf.pr.touch.logic.beans.QuantProtein;
import no.uib.probe.csf.pr.touch.view.core.ExternalLink;
import no.uib.probe.csf.pr.touch.view.core.ComparisonLable;
import no.uib.probe.csf.pr.touch.view.core.TrendSymbol;

/**
 * This class represents protein datasets table where each row represents the
 * protein information for each dataset including protein coverage and
 * peptidesSequenceSet information.
 *
 * @author Yehia Farag
 */
public class ProteinDatasetsTable extends VerticalLayout {

    /**
     * The main table component.
     */
    private final Table mainProteinDatasetsTable;
    /**
     * Set of selected dataset indexes.
     */
    private final Set<Object> selectedDatasetItemIds = new HashSet<>();
    /**
     * Show selected datasets only.
     */
    private boolean selectedOnly = false;
    /**
     * Map of table item id and each row item contents.
     */
    private final Map<Object, Object[]> tableItemsMap;

    /**
     * Map of dataset key (comparison title and index) to item id.
     */
    private final Map<String, Object> datasetToItemId;
    /**
     * Set of protein sequence coverage components.
     */
    private final Set<ProteinSequenceCoverageComponent> proteinSequenceCoverageSet;
    /**
     * The width of protein sequence coverage component (the protein coverage
     * column).
     */
    private final int proteinSequenceCoverageColumnWidth;
    /**
     * The protein sequence.
     */
    private String sequence = "";
    /**
     * Set of peptide sequence.
     */
    private final Set<String> peptidesSequenceSet = new LinkedHashSet<>();

    /**
     * Get number of rows in the protein table.
     *
     * @return number of rows.
     */
    public int getRowsNumber() {
        return this.mainProteinDatasetsTable.getItemIds().size();
    }

    /**
     * Constructor to initialize the main attributes.
     *
     * @param width the width of the protein datasets table layout.
     * @param height the height of the protein datasets table layout.
     */
    public ProteinDatasetsTable(int width, int height) {
        this.setWidth(width, Unit.PIXELS);
        this.setHeight(height, Unit.PIXELS);
        this.tableItemsMap = new LinkedHashMap<>();
        this.datasetToItemId = new HashMap<>();
        this.proteinSequenceCoverageSet = new HashSet<>();

        this.mainProteinDatasetsTable = new Table();
        this.mainProteinDatasetsTable.setWidthUndefined();
        this.mainProteinDatasetsTable.setStyleName(ValoTheme.TABLE_COMPACT);
        this.mainProteinDatasetsTable.addStyleName("peptidetablestyle");
        if (!Page.getCurrent().getWebBrowser().isChrome()) {
            this.mainProteinDatasetsTable.addStyleName("notchromecorrector");
        }
        this.mainProteinDatasetsTable.setHeight(height, Unit.PIXELS);
        this.mainProteinDatasetsTable.addStyleName("smallicons");
        this.mainProteinDatasetsTable.addStyleName(ValoTheme.TABLE_SMALL);

        this.mainProteinDatasetsTable.setSelectable(false);
        this.mainProteinDatasetsTable.setSortEnabled(false);
        this.mainProteinDatasetsTable.setColumnReorderingAllowed(false);
        this.mainProteinDatasetsTable.setColumnCollapsingAllowed(true);
        this.mainProteinDatasetsTable.setImmediate(true); // react at once when something is selected
        this.mainProteinDatasetsTable.setMultiSelect(false);

        this.mainProteinDatasetsTable.addContainerProperty("Index", Integer.class, null, "", null, Table.Align.RIGHT);
        this.mainProteinDatasetsTable.addContainerProperty("trend", TrendSymbol.class, null, "", null, Table.Align.CENTER);
        this.mainProteinDatasetsTable.addContainerProperty("Comparison", ComparisonLable.class, null, "Comparison", null, Table.Align.LEFT);
        this.mainProteinDatasetsTable.addContainerProperty("Publication", ExternalLink.class, null, "Publication", null, Table.Align.LEFT);

        this.mainProteinDatasetsTable.addContainerProperty("patientsNumber", Integer.class, null, "#Patients", new ThemeResource("img/p-numb.png"), Table.Align.RIGHT);
        this.mainProteinDatasetsTable.addContainerProperty("PeptideSequence", ProteinSequenceCoverageComponent.class, null, "Protein Coverage", null, Table.Align.CENTER);
        this.mainProteinDatasetsTable.setColumnWidth("trend", 47);
        this.mainProteinDatasetsTable.setColumnWidth("Index", 47);

        this.mainProteinDatasetsTable.setColumnWidth("patientsNumber", 47);
        this.mainProteinDatasetsTable.setColumnWidth("Comparison", 100);
        this.mainProteinDatasetsTable.setColumnWidth("Publication", 175);
        this.proteinSequenceCoverageColumnWidth = width - 47 - 47 - 47 - 87 - 187 - 10 - 25;//- 47
        this.mainProteinDatasetsTable.setColumnWidth("PeptideSequence", proteinSequenceCoverageColumnWidth);
        this.mainProteinDatasetsTable.addColumnResizeListener((Table.ColumnResizeEvent event) -> {
            this.mainProteinDatasetsTable.setColumnWidth(event.getPropertyId(), event.getPreviousWidth());
        });
        this.addComponent(mainProteinDatasetsTable);
    }

    /**
     * Filter the main protein dataset table and show selected dataset only.
     */
    private void showFilteredDatasetsOnly() {
        mainProteinDatasetsTable.removeAllItems();
        if (!selectedOnly) {
            for (Object itemId : tableItemsMap.keySet()) {
                mainProteinDatasetsTable.addItem(tableItemsMap.get(itemId), itemId);
            }
        } else {
            if (!selectedDatasetItemIds.isEmpty()) {
                for (Object itemId : selectedDatasetItemIds) {
                    mainProteinDatasetsTable.addItem(tableItemsMap.get(itemId), itemId);
                }
            } else {
                for (Object itemId : tableItemsMap.keySet()) {
                    mainProteinDatasetsTable.addItem(tableItemsMap.get(itemId), itemId);
                }
            }
        }
    }

    /**
     * Update protein datasets table based on user comparison or dataset
     * selection.
     *
     * @param selectedComparisonsList list of selected comparisons (selected
     * from the heat map component).
     * @param proteinKey the main protein key.
     */
    public void updateTableData(Set<QuantDiseaseGroupsComparison> selectedComparisonsList, String proteinKey) {
        mainProteinDatasetsTable.removeAllItems();
        tableItemsMap.clear();
        proteinSequenceCoverageSet.clear();
        peptidesSequenceSet.clear();
        int objectId = 0;
        sequence = "";
        String keyI = 0 + "_" + proteinKey;
        String keyII = 1 + "_" + proteinKey;
        String keyIII = 2 + "_" + proteinKey;
        String keyIV = 3 + "_" + proteinKey;
        String keyV = 4 + "_" + proteinKey;
        String key;
        for (QuantDiseaseGroupsComparison comparison : selectedComparisonsList) {
            key = "";
            if (comparison.getQuantComparisonProteinMap().containsKey(keyI)) {
                key = keyI;
            } else if (comparison.getQuantComparisonProteinMap().containsKey(keyII)) {
                key = keyII;
            } else if (comparison.getQuantComparisonProteinMap().containsKey(keyIII)) {
                key = keyIII;
            } else if (comparison.getQuantComparisonProteinMap().containsKey(keyIV)) {
                key = keyIV;
            } else if (comparison.getQuantComparisonProteinMap().containsKey(keyV)) {
                key = keyV;
            }

            if (!comparison.getQuantComparisonProteinMap().containsKey(key)) {
                continue;
            }
            QuantComparisonProtein protein = comparison.getQuantComparisonProteinMap().get(key);

            if (sequence == null || sequence.equalsIgnoreCase("")) {
                sequence = protein.getSequence().trim();
            }

            for (QuantPeptide pep : protein.getQuantPeptidesList()) {
                peptidesSequenceSet.add(pep.getPeptideSequence());
            }

            if (sequence.equalsIgnoreCase("") && !peptidesSequenceSet.isEmpty()) {
                Object[] pepStrObjArr = peptidesSequenceSet.toArray();
                for (Object pepStrObjArr1 : pepStrObjArr) {
                    String a = (String) pepStrObjArr1;
                    for (Object pepStrObjArr2 : pepStrObjArr) {
                        String b = (String) pepStrObjArr2;
                        if (!a.equalsIgnoreCase(b) && a.contains(b)) {
                            peptidesSequenceSet.remove(b);
                        } else if (!a.equalsIgnoreCase(b) && b.contains(a)) {
                            peptidesSequenceSet.remove(a);
                        }
                    }
                }
                sequence = "ZZZZZZZZ" + peptidesSequenceSet.toString().replace("[", "").replace(" ", "").replace(",", "").replace("]", "") + "ZZZZZZZZ";
            }

            for (QuantProtein quantProt : protein.getDsQuantProteinsMap().values()) {
                int trend = 2;
                if (quantProt.getString_p_value().equalsIgnoreCase("Significant")) {
                    if (quantProt.getString_fc_value().equalsIgnoreCase("Increased")) {
                        trend = 0;
                    } else if (quantProt.getString_fc_value().equalsIgnoreCase("Decreased")) {
                        trend = 4;
                    }
                }

                TrendSymbol symbol = new TrendSymbol(trend);
                symbol.addStyleName("pointer");
                symbol.setWidth(12, Unit.PIXELS);
                symbol.setHeight(12, Unit.PIXELS);

                QuantDataset ds = comparison.getDatasetMap().get(quantProt.getQuantDatasetIndex());
                String title = "<font size='2'>" + ds.getAuthor() + "<br/>(" + ds.getYear() + ")</font> ";

                ExternalLink publicationLink = new ExternalLink(title, new ExternalResource("http://www.ncbi.nlm.nih.gov/pubmed/" + ds.getPubMedId()));
                publicationLink.setDescription("View publication " + title);
                publicationLink.setWidth(100, Unit.PERCENTAGE);
                publicationLink.setCaptionAsHtml(true);
                publicationLink.addStyleName(ValoTheme.LABEL_SMALL);
                publicationLink.addStyleName(ValoTheme.LABEL_TINY);
                publicationLink.addStyleName("overflowtext");

                ComparisonLable comparisonLabelObject = new ComparisonLable(comparison, objectId, quantProt, ds);

                symbol.addLayoutClickListener((LayoutEvents.LayoutClickEvent event) -> {
                    comparisonLabelObject.openComparisonPopup();
                });
                symbol.setDescription(comparisonLabelObject.getDescription());

                String protName = quantProt.getUniprotProteinName().trim();
                if (protName.equalsIgnoreCase("")) {
                    protName = quantProt.getPublicationProteinName();
                }

                ProteinSequenceCoverageComponent proteinCoverageLayout = new ProteinSequenceCoverageComponent(sequence, protein.getQuantPeptidesList(), proteinSequenceCoverageColumnWidth, quantProt.getQuantDatasetIndex(), protName);
                proteinSequenceCoverageSet.add(proteinCoverageLayout);

                tableItemsMap.put(objectId, new Object[]{objectId + 1, symbol, comparisonLabelObject, publicationLink, ds.getDiseaseMainGroup1Number() + ds.getDiseaseMainGroup2Number(), proteinCoverageLayout});
                mainProteinDatasetsTable.addItem(tableItemsMap.get(objectId), objectId);
                datasetToItemId.put(comparison.getComparisonHeader() + "__" + quantProt.getQuantDatasetIndex(), objectId);
                objectId++;
            }
        }
        int indexing = 1;
        for (Object id : mainProteinDatasetsTable.getItemIds()) {
            Item item = mainProteinDatasetsTable.getItem(id);
            item.getItemProperty("Index").setValue(indexing);
            indexing++;
        }
    }

    /**
     * Get set of protein sequence coverage components.
     *
     * @return proteinSequenceCoverageSet set of protein sequence coverage
     * components.
     */
    public Set<ProteinSequenceCoverageComponent> getProteinSequenceCoverageSet() {
        return proteinSequenceCoverageSet;
    }

    /**
     * Sort the rows based on the dataset order in the selected comparisons list
     * (used on default-ordered by trend dataset line chart)
     *
     * @param selectedComparisonsList set of quant disease comparisons.
     */
    public void sortTable(Set<QuantDiseaseGroupsComparison> selectedComparisonsList) {
        mainProteinDatasetsTable.removeAllItems();
        Map<Object, Object[]> sortedTableItemsMap = new LinkedHashMap<>();
        for (QuantDiseaseGroupsComparison comparison : selectedComparisonsList) {
            for (QuantDataset ds : comparison.getDatasetMap().values()) {
                if (datasetToItemId.containsKey(comparison.getComparisonHeader() + "__" + ds.getQuantDatasetIndex())) {
                    Object itemId = datasetToItemId.get(comparison.getComparisonHeader() + "__" + ds.getQuantDatasetIndex());
                    if (tableItemsMap.containsKey(itemId)) {
                        mainProteinDatasetsTable.addItem(tableItemsMap.get(itemId), itemId);
                        sortedTableItemsMap.put(itemId, tableItemsMap.get(itemId));
                    }
                }
            }
        }

        tableItemsMap.clear();
        tableItemsMap.putAll(sortedTableItemsMap);
        selectedDatasetItemIds.clear();
        selectedOnly = false;
        showFilteredDatasetsOnly();
        int indexing = 1;
        for (Object id : mainProteinDatasetsTable.getItemIds()) {
            Item item = mainProteinDatasetsTable.getItem(id);
            item.getItemProperty("Index").setValue(indexing);
            indexing++;
        }
    }

    /**
     * Set selected comparisons datasets or single dataset
     *
     * @param comparison the selected comparison object in case of overview
     * comparison mode
     * @param dsKey the selected dataset index in case of detailed dataset mode
     */
    public void select(QuantDiseaseGroupsComparison comparison, int dsKey) {
        selectedDatasetItemIds.clear();
        if (comparison == null || dsKey == -100) {
            return;
        }
        Object itemId = null;
        if (datasetToItemId.containsKey(comparison.getComparisonHeader() + "__" + dsKey)) {
            itemId = datasetToItemId.get(comparison.getComparisonHeader() + "__" + dsKey);
        } else {
            return;
        }
        Item item = mainProteinDatasetsTable.getItem(itemId);
        ((ComparisonLable) item.getItemProperty("Comparison").getValue()).openComparisonPopup();

    }

    /**
     * Show/Hide peptidesSequenceSet components with not significant fold
     * change.
     *
     * @param showNotSigPeptides show the not significant fold change
     * peptidesSequenceSet component.
     */
    public void showNotSignificantPeptides(boolean showNotSigPeptides) {
        proteinSequenceCoverageSet.stream().forEach((proteinCoverageLayout) -> {
            proteinCoverageLayout.setShowNotSignificantPeptides(showNotSigPeptides);
        });
    }

    /**
     * Get protein sequence
     *
     * @return sequence of protein (imported from UniProt)
     */
    public String getSequence() {
        return sequence;
    }

    /**
     * Get set of peptide sequence.
     *
     * @return peptidesSequenceSet that has peptides sequences only
     */
    public Set<String> getPeptidesSequenceSet() {
        return peptidesSequenceSet;
    }

}
