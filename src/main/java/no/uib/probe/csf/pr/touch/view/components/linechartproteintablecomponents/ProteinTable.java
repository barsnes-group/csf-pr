package no.uib.probe.csf.pr.touch.view.components.linechartproteintablecomponents;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JPanel;
import no.uib.probe.csf.pr.touch.logic.beans.QuantComparisonProtein;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import no.uib.probe.csf.pr.touch.view.core.ColumnHeaderLayout;
import no.uib.probe.csf.pr.touch.view.core.ExternalLink;
import no.uib.probe.csf.pr.touch.view.core.ProteinTrendLayout;
import no.uib.probe.csf.pr.touch.view.core.RadioButton;
import no.uib.probe.csf.pr.touch.view.core.TrendSymbol;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.encoders.ImageEncoder;
import org.jfree.chart.encoders.ImageEncoderFactory;
import org.jfree.chart.encoders.ImageFormat;
/**
 * This class represents the quant protein table container components including
 * the table and sorting layout (sorting buttons for comparisons).
 *
 * @author Yehia Farag.
 */
public abstract class ProteinTable extends VerticalLayout implements Property.ValueChangeListener {

    /**
     * Show selected proteins only.
     */
    private boolean selectedOnly = false;
    /**
     * Map of table item id and each row item contents.
     */
    private final Map<Object, Object[]> tableItemsMap;
    /**
     * Map of the current used table item id and each row item contents.
     */
    private final Map<Object, Object[]> activeTableItemsMap;
    /**
     * Map of protein key and its item id in the table.
     */
    private final Map<String, Object> tableProteinsToIDMap;
    /**
     * Set of sorting buttons layout.
     */
    private final Set<ColumnHeaderLayout> columnHeaderSet;
    /**
     * The width of the table.
     */
    private int availableProteinLayoutWidth;
    /**
     * Set of selected items in the table.
     */
    private final Set<Object> selectedItemIds = new HashSet<>();
    /**
     * The main proteins table.
     */
    private Table mainProteinTable;
    /**
     * Top comparisons layout is used to contain the comparisons
     * sorting/filtering buttons.
     */
    private final AbsoluteLayout topComparisonsContainer;
    /**
     * Map of quant disease comparisons and its items id map.
     */
    private final Map<QuantDiseaseGroupsComparison, Map<String, List<Object>>> filtersMap;
    /**
     * Spacer layout is used to padding the top comparison layout container.
     */
    private final VerticalLayout spacer;
    /**
     * The table wrapper layout is used to contain the table and top layout.
     */
    private final VerticalLayout tableWarpper;
    /**
     * The component height.
     */
    private final int height;
    /**
     * The component height.
     */
    private final int width;
    /**
     * The header used for sorting the table.
     */
    private int sortingColumnHeader;
    /**
     * The table is sorted ascending.
     */
    private boolean ascendingSort;
    /**
     * The line chart layout is providing location for different comparisons to
     * set the location of different comparisons filterProteinList components.
     */
    private AbsoluteLayout instanceOfLinechartComponentsLayout;
    /**
     * The filters applied.
     */
    private boolean filtereApplied = false;
    /**
     * List of selected comparisons to be updated based on user selection for
     * comparisons across the system.
     */
    private Set<QuantDiseaseGroupsComparison> selectedComparisonsList;
    /**
     * List of selected filtered proteins.
     */
    private Set<QuantComparisonProtein> inUseSselectedProteinsList;
    /**
     * List of full selected proteins list(no filters applied).
     */
    private Set<QuantComparisonProtein> fullSselectedProteinsList;
    /**
     * Sort/Filter component for customized user data (quant compare mode).
     */
    private ColumnHeaderLayout custUserComparisonSortingLayout;
    /**
     * Wrapper layout for Sort/Filter component for customized user data (quant
     * compare mode).
     */
    private final VerticalLayout userSortingHeaderWrapper;
    /**
     * Customized comparison based on user input data in quant comparison
     * layout.
     */
    private QuantDiseaseGroupsComparison userCustomizedComparison;

    /**
     * Set customized comparison based on user input data in quant comparison
     * layout.
     *
     * @param userCustomizedComparison Customized comparison based on user input
     * data in quant comparison layout
     */
    public void setUserCustomizedComparison(QuantDiseaseGroupsComparison userCustomizedComparison) {
        if (columnHeaderSet.contains(this.custUserComparisonSortingLayout)) {
            columnHeaderSet.remove(this.custUserComparisonSortingLayout);
        }
        this.userCustomizedComparison = userCustomizedComparison;

        userSortingHeaderWrapper.removeAllComponents();

        if (userCustomizedComparison != null) {

            mainProteinTable = generateMainTable(height, width, true);

            userCustomizedComparison.getQuantComparisonProteinMap().keySet().stream().forEach((accession) -> {
                Object itemId = tableProteinsToIDMap.get(accession);
                if (tableItemsMap.containsKey(itemId)) {
                    Object[] items = tableItemsMap.get(itemId);
                    ProteinTrendLayout protTrendLayout = (ProteinTrendLayout) items[5];
                    if (protTrendLayout != null) {
                        protTrendLayout.updateCustTrend(userCustomizedComparison.getQuantComparisonProteinMap().get(accession).getSignificantTrindCategory());
                    }
                }
            });
            activeTableItemsMap.clear();
            activeTableItemsMap.putAll(tableItemsMap);

            custUserComparisonSortingLayout = new ColumnHeaderLayout(userCustomizedComparison, -1) {

                @Override
                public void sort(boolean up, int index) {
                    sortOnComparison(up, index);
                }

                @Override
                public void dropComparison(QuantDiseaseGroupsComparison comparison) {

                    ProteinTable.this.dropComparison(comparison);
                }

                @Override
                public void filterTable(QuantDiseaseGroupsComparison comparison, int comparisonIndex, Set<Object> filterSet, boolean unselectfilter) {
                    filterTableSelection(comparison, new ArrayList<>(filterSet), unselectfilter);
                    sortOnComparison(ascendingSort, sortingColumnHeader);
                }

                @Override
                public void filterTable(QuantDiseaseGroupsComparison comparison, int comparisonIndex, double min, double max, boolean unselectfilter) {
                    filterTableSelection(comparison, min, max, unselectfilter);
                    sortOnComparison(ascendingSort, sortingColumnHeader);
                }

                @Override
                public void filterTable(QuantDiseaseGroupsComparison comparison, int comparisonIndex, long min, long max, boolean unselectfilter) {
                    filterTableSelection(comparison, min, max, unselectfilter);
                    sortOnComparison(ascendingSort, sortingColumnHeader);
                }

                @Override
                public void filterTable(QuantDiseaseGroupsComparison comparison, int comparisonIndex, double min, double max, double secMin, double secMax, boolean unselectfilter) {
                    filterTableSelection(comparison, min, max, secMin, secMax, unselectfilter);
                    sortOnComparison(ascendingSort, sortingColumnHeader);
                }
            };
            this.userSortingHeaderWrapper.addComponent(custUserComparisonSortingLayout);
            custUserComparisonSortingLayout.setAsDefault();

        } else {
            mainProteinTable = generateMainTable(height, width, true);
        }
    }

    /**
     * Generate thumb image to update the left side button icon.
     *
     * @return image encoded into base64 string to be used to update the left
     * side button icon.
     *
     */
    private String generateThumbImg() {
        JPanel proteinSequencePanel = new JPanel();
        proteinSequencePanel.setLayout(null);
        proteinSequencePanel.setSize(100, 100);
        proteinSequencePanel.setBackground(Color.WHITE);
        int dsIndex = 1;
        int y = 0;
        for (Object[] objArr : tableItemsMap.values()) {
            ProteinTrendLayout trend = (ProteinTrendLayout) objArr[5];
            if (trend.getSparkLine() != null) {

                ChartPanel lineChartPanel = new ChartPanel(trend.getSparkLine().generateThumbChart());
                lineChartPanel.setSize(100, 25);
                lineChartPanel.setLocation(0, y);
                lineChartPanel.setOpaque(true);
                proteinSequencePanel.add(lineChartPanel);
                y += 25;
                if (dsIndex > 4) {
                    break;
                }
            }
        }

        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();

        graphics.setPaint(Color.WHITE);
        graphics.setBackground(Color.WHITE);

        proteinSequencePanel.paint(graphics);
        byte[] imageData = null;

        try {

            ImageEncoder in = ImageEncoderFactory.newInstance(ImageFormat.PNG, 1);
            imageData = in.encode(image);
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }

        String base64 = "data:image/png;base64," + com.itextpdf.text.pdf.codec.Base64.encodeBytes(imageData);
        return base64;
    }

    /**
     * Get number of rows in the protein table.
     *
     * @return number of rows.
     *
     */
    public int getRowsNumber() {
        return this.mainProteinTable.getItemIds().size();
    }

    /**
     * Filter and update the protein table using the selected proteins list.
     *
     * @param selectedProteinsList list of proteins to be selected and viewed.
     */
    public void filterViewItemTable(Set<QuantComparisonProtein> selectedProteinsList) {
        selectedItemIds.clear();
        this.inUseSselectedProteinsList = selectedProteinsList;
        selectedProteinsList.stream().forEach((protein) -> {
            if (tableProteinsToIDMap.get(protein.getProteinAccession()).toString().contains("_-_")) {
                String[] arr = tableProteinsToIDMap.get(protein.getProteinAccession()).toString().split("_-_");
                for (String id : arr) {
                    selectedItemIds.add(Integer.valueOf(id));
                }
            } else {
                selectedItemIds.add(tableProteinsToIDMap.get(protein.getProteinAccession()));
            }

        });
        selectedOnly = true;
        showSelectedOnly();
        if (selectedProteinsList.isEmpty()) {
            Notification.show("No results found");
        }

    }

    /**
     * Remove all applied columns filters.
     */
    public void clearColumnFilters() {
        filtersMap.clear();
        Set<String> filteredProteinsList = new LinkedHashSet<>(this.tableProteinsToIDMap.keySet());
        mainProteinTable.removeAllItems();
        filteredProteinsList.stream().map((accession) -> tableProteinsToIDMap.get(accession)).forEachOrdered((itemId) -> {
            Object[] items = tableItemsMap.get(itemId);
            mainProteinTable.addItem(items, itemId);
        });
        if (mainProteinTable.getItemIds().size() == tableItemsMap.size()) {
            filtereApplied = false;
        }
        inUseSselectedProteinsList = new LinkedHashSet<>();
        fullSselectedProteinsList.stream().filter((prot) -> (filteredProteinsList.contains(prot.getProteinAccession()))).forEachOrdered((prot) -> {
            inUseSselectedProteinsList.add(prot);
        });
        int indexing = 1;
        for (Object id
                : mainProteinTable.getItemIds()) {
            Item item = mainProteinTable.getItem(id);
            item.getItemProperty("Index").setValue(indexing);
            indexing++;
        }
        updateIconRowNumber(mainProteinTable.getItemIds().size(), generateThumbImg());

        columnHeaderSet.stream().forEach((comparisonLayout) -> {
            comparisonLayout.noFilter();
        });
        if (userCustomizedComparison != null) {
            custUserComparisonSortingLayout.setAsDefault();
            custUserComparisonSortingLayout.sort(false, -1);
        }

    }

    /**
     * Update sorting buttons to filterProteinList buttons.
     */
    public void switchHeaderBtns() {

        columnHeaderSet.stream().forEach((comparisonLayout) -> {
            comparisonLayout.swichBtns();
        });

    }

    /**
     * Get the header used for sorting the table.
     *
     * @return sortingColumnHeader the name of header used for sorting the table
     */
    public int getSortingColumnHeader() {
        return sortingColumnHeader;
    }

    /**
     * Check if the table is sorted ascending.
     *
     * @return ascendingSort Sort ascending
     */
    public boolean isAscendingSort() {
        return ascendingSort;
    }

    /**
     * Sort table based on specific comparison.
     *
     * @param ascendingSort sort the table ascending.
     * @param comparisonIndex the comparison index to sort on.
     */
    public void sortOnComparison(boolean ascendingSort, int comparisonIndex) {
        mainProteinTable.addStyleName("hidesortingicon");
        this.ascendingSort = ascendingSort;
        if (comparisonIndex == -1) {
            mainProteinTable.setSortEnabled(true);
            mainProteinTable.sort(new String[]{"userdata", "Accession"}, new boolean[]{ascendingSort, true});
            mainProteinTable.commit();
            this.sortingColumnHeader = -1;
            int index = 0;
            for (ColumnHeaderLayout comparisonLayout : columnHeaderSet) {
                if (index == comparisonIndex || comparisonLayout.getComparisonIndex() == -1) {
                    index++;
                    continue;
                }
                comparisonLayout.noSort();
                index++;
            }

            int indexing = 1;
            for (Object id : mainProteinTable.getItemIds()) {
                Item item = mainProteinTable.getItem(id);
                item.getItemProperty("Index").setValue(indexing);
                indexing++;
            }
            mainProteinTable.setSortEnabled(false);
            return;

        }

        int index = 0;
        for (ColumnHeaderLayout comparisonLayout : columnHeaderSet) {
            if (index == comparisonIndex) {
                index++;
                continue;
            }
            comparisonLayout.noSort();
            index++;
        }
        tableItemsMap.values().stream().map((arr) -> (ProteinTrendLayout) arr[5]).forEach((protTrendLayout) -> {
            protTrendLayout.setSortableColumnIndex(comparisonIndex);

        });
        Set<Object> itemIds = new LinkedHashSet<>(mainProteinTable.getItemIds());
        this.mainProteinTable.removeValueChangeListener(ProteinTable.this);
        mainProteinTable.removeAllItems();
        tableItemsMap.keySet().stream().filter((object) -> (itemIds.contains(object))).forEachOrdered((object) -> {
            mainProteinTable.addItem(tableItemsMap.get(object), object);
        });
        this.sortingColumnHeader = comparisonIndex;//((QuantDiseaseGroupsComparison) this.selectedComparisonsList.toArray()[comparisonIndex]).getComparisonHeader();

        mainProteinTable.sort(new String[]{"Comparisons Overview", "Accession"}, new boolean[]{ascendingSort, true});
        mainProteinTable.commit();
        int indexing = 1;
        for (Object id : mainProteinTable.getItemIds()) {
            Item item = mainProteinTable.getItem(id);
            item.getItemProperty("Index").setValue(indexing);
            indexing++;
        }

        this.mainProteinTable.addValueChangeListener(ProteinTable.this);

    }

    /**
     * Constructor to initialize the main attributes.
     *
     * @param width the width of the protein table component.
     * @param height the height of the protein table component.
     */
    public ProteinTable(int width, int height) {

        ProteinTable.this.setWidth(100, Unit.PERCENTAGE);
        ProteinTable.this.setHeightUndefined();
        this.width = width;
        this.height = height;

        this.columnHeaderSet = new LinkedHashSet<>();
        this.filtersMap = new LinkedHashMap<>();

        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidthUndefined();
        topLayout.setHeight(20, Unit.PIXELS);
        spacer = new VerticalLayout();
        spacer.setHeight(100, Unit.PERCENTAGE);

        topLayout.addComponent(spacer);
        this.userSortingHeaderWrapper = new VerticalLayout();
        userSortingHeaderWrapper.setHeight(20, Unit.PIXELS);
        userSortingHeaderWrapper.addStyleName("custuserdataheader");
        topLayout.addComponent(userSortingHeaderWrapper);
        topLayout.setComponentAlignment(userSortingHeaderWrapper, Alignment.TOP_RIGHT);
        topComparisonsContainer = new AbsoluteLayout();
        topComparisonsContainer.setHeight(100, Unit.PERCENTAGE);
        topComparisonsContainer.addStyleName("toptablelayout");
        topComparisonsContainer.addStyleName("marginleft-12");
        topLayout.addComponent(topComparisonsContainer);

        ProteinTable.this.addComponent(topLayout);
        tableWarpper = new VerticalLayout();
        tableWarpper.setWidth(100, Unit.PERCENTAGE);
        ProteinTable.this.addComponent(tableWarpper);

        this.tableItemsMap = new LinkedHashMap<>();
        this.activeTableItemsMap = new LinkedHashMap<>();
        this.tableProteinsToIDMap = new HashMap<>();

    }

    /**
     * Create new protein table.
     *
     * @param height the height of the protein table component.
     * @param width the width of the protein table component.
     * @param quantCompareMode the system in quant compare mode.
     *
     */
    private Table generateMainTable(int height, int width, boolean quantCompareMode) {
        tableWarpper.removeAllComponents();
        this.mainProteinTable = new Table();

        int userDataColumnWidth;
        if (quantCompareMode) {
            userDataColumnWidth = 47;
        } else {
            userDataColumnWidth = 0;
        }

        mainProteinTable.setCacheRate(1);
        this.mainProteinTable.addValueChangeListener(ProteinTable.this);
        this.mainProteinTable.addStyleName(ValoTheme.TABLE_SMALL);
        this.mainProteinTable.setHeight(height, Unit.PIXELS);
        mainProteinTable.setStyleName(ValoTheme.TABLE_COMPACT);
        this.mainProteinTable.addStyleName("proteintablestyle");
        if (!Page.getCurrent().getWebBrowser().isChrome()) {
            this.mainProteinTable.addStyleName("notchromecorrector");
        }
        tableWarpper.addComponent(mainProteinTable);

        mainProteinTable.setSelectable(true);
        mainProteinTable.setSortEnabled(false);
        mainProteinTable.setColumnReorderingAllowed(false);

        mainProteinTable.setColumnCollapsingAllowed(true);
        mainProteinTable.setImmediate(true);
        mainProteinTable.setMultiSelect(false);

        mainProteinTable.addContainerProperty("Index", Integer.class, null, "", null, Table.Align.RIGHT);
        mainProteinTable.addContainerProperty("selectedRow", RadioButton.class, null, "", null, Table.Align.CENTER);
        mainProteinTable.addContainerProperty("Accession", ExternalLink.class, null, "Accession", null, Table.Align.CENTER);
        mainProteinTable.addContainerProperty("Name", String.class, null, "Name", null, Table.Align.LEFT);
        mainProteinTable.addContainerProperty("userdata", TrendSymbol.class, null, " ", null, Table.Align.CENTER);

        mainProteinTable.addContainerProperty("Comparisons Overview", ProteinTrendLayout.class, null, "", null, Table.Align.LEFT);
        mainProteinTable.setColumnCollapsed("userdata", quantCompareMode);

        mainProteinTable.setColumnWidth("selectedRow", 30);
        mainProteinTable.setColumnWidth("Index", 47);
        mainProteinTable.setColumnWidth("Accession", 87);
        mainProteinTable.setColumnWidth("Name", 187);
        mainProteinTable.setColumnWidth("userdata", userDataColumnWidth);

        spacer.setWidth((355), Unit.PIXELS);
        userSortingHeaderWrapper.setWidth(userDataColumnWidth, Unit.PIXELS);
        availableProteinLayoutWidth = width - 48 - 87 - 187 - 30 - userDataColumnWidth;//- 47
        topComparisonsContainer.setWidth(availableProteinLayoutWidth - 10, Unit.PIXELS);
        mainProteinTable.setColumnWidth("Comparisons Overview", availableProteinLayoutWidth);

        mainProteinTable.addHeaderClickListener((Table.HeaderClickEvent event) -> {
            if (event.getPropertyId() == null) {
                return;

            }
            if (event.getPropertyId().toString().equalsIgnoreCase("selected")) {
                if (selectedItemIds.isEmpty()) {
                    return;
                }
                selectedOnly = !selectedOnly;
                showSelectedOnly();

            } else if (!event.getPropertyId().toString().equalsIgnoreCase("Comparisons Overview") && !event.getPropertyId().toString().equalsIgnoreCase("Index") && !event.getPropertyId().toString().equalsIgnoreCase("userdata")) {//
                mainProteinTable.removeStyleName("hidesortingicon");
                mainProteinTable.setSortEnabled(true);

                if (mainProteinTable.getSortContainerPropertyId() == null || !mainProteinTable.getSortContainerPropertyId().toString().equalsIgnoreCase(event.getPropertyId().toString())) {
                    mainProteinTable.sort(new String[]{event.getPropertyId().toString()}, new boolean[]{false});
                } else {
                    mainProteinTable.sort(new String[]{event.getPropertyId().toString()}, new boolean[]{!mainProteinTable.isSortAscending()});

                }
                mainProteinTable.setSortEnabled(false);
                int indexing = 1;
                for (Object id : mainProteinTable.getItemIds()) {
                    Item item = mainProteinTable.getItem(id);
                    item.getItemProperty("Index").setValue(indexing);
                    indexing++;
                }
                columnHeaderSet.stream().map((columnHeader) -> {
                    columnHeader.reset();
                    return columnHeader;
                }).forEachOrdered((columnHeader) -> {
                    columnHeader.noSort();
                });
            }

        });
        mainProteinTable.addColumnResizeListener((Table.ColumnResizeEvent event) -> {
            mainProteinTable.setColumnWidth(event.getPropertyId(), event.getPreviousWidth());
        });
        return mainProteinTable;

    }

    /**
     * Show selected items only in protein table.
     */
    private void showSelectedOnly() {
        mainProteinTable.removeAllItems();
        for (Object itemId : selectedItemIds) {
            if (tableItemsMap.containsKey(itemId)) {
                mainProteinTable.addItem(tableItemsMap.get(itemId), itemId);
            }
        }
        int indexing = 1;
        for (Object id : mainProteinTable.getItemIds()) {
            Item item = mainProteinTable.getItem(id);
            item.getItemProperty("Index").setValue(indexing);
            indexing++;
        }
        updateIconRowNumber(mainProteinTable.getItemIds().size(), generateThumbImg());

    }

    /**
     * Get list of selected comparisons.
     *
     * @return selectedComparisonsList list of selected comparisons.
     */
    public Set<QuantDiseaseGroupsComparison> getSelectedComparisonsList() {
        return selectedComparisonsList;
    }

    /**
     * Get list of selected filtered proteins.
     *
     * @return current selected proteins list.
     */
    public Set<QuantComparisonProtein> getSelectedProteinsList() {
        return inUseSselectedProteinsList;
    }

    /**
     * Update protein table selection based on user comparison selection.
     *
     * @param selectedComparisonsList list of selected comparisons (selected
     * from the heat map component).
     * @param selectedProteinsList list of selected proteins if any (selected
     * from the bubble chart component).
     */
    public void updateTableData(Set<QuantDiseaseGroupsComparison> selectedComparisonsList, Set<QuantComparisonProtein> selectedProteinsList) {
        tableItemsMap.clear();
        tableProteinsToIDMap.clear();
        instanceOfLinechartComponentsLayout = null;
        filtersMap.clear();
        if (userCustomizedComparison != null) {
            spacer.removeAllComponents();
            mainProteinTable = generateMainTable(height, width, true);
            if (mainProteinTable.isColumnCollapsed("userdata")) {
                mainProteinTable.setColumnCollapsed("userdata", false);
                topComparisonsContainer.removeStyleName("marginleft-12");
                mainProteinTable.setColumnWidth("Comparisons Overview", availableProteinLayoutWidth);
                topComparisonsContainer.setWidth(availableProteinLayoutWidth - 10, Unit.PIXELS);

            }

        } else {
            mainProteinTable = generateMainTable(height, width, false);
            mainProteinTable.setColumnCollapsed("userdata", true);
            topComparisonsContainer.addStyleName("marginleft-12");
            mainProteinTable.setColumnWidth("Comparisons Overview", availableProteinLayoutWidth);
            topComparisonsContainer.setWidth(availableProteinLayoutWidth - 10, Unit.PIXELS);
        }

        this.fullSselectedProteinsList = selectedProteinsList;
        this.inUseSselectedProteinsList = fullSselectedProteinsList;
        int protId = 0;
        for (QuantComparisonProtein protein : selectedProteinsList) {
            String accession = protein.getProteinAccession();//.replace("(unreviewed)", " (Unreviewed)");           
            String name = protein.getProteinName();
            String url = protein.getUrl();
            String description = "Click to view in UniProt";
            if (url == null) {
                url = "";
                if (accession.contains("(")) {
                    description = "UniProt information is not available (" + accession.replace("(", "__").split("__")[1].toLowerCase();
                } else {
                    description = "UniProt information is not available (" + accession + ")";
                }
            } else if (accession.contains("(unreviewed)")) {
                description += " (unreviewed)";
            }
            url = url.replace("(UNREVIEWED)", "").replace("(unreviewed)", "");
            ExternalLink accessionObject = new ExternalLink(accession, new ExternalResource(url));
            accessionObject.setData(accession);
            accessionObject.setDescription(description);
            this.selectedComparisonsList = selectedComparisonsList;

            ProteinTrendLayout protTrendLayout = new ProteinTrendLayout(selectedComparisonsList, protein, availableProteinLayoutWidth, protId, (protId < 10)) {
                @Override
                public void selectTableItem(Object itemId) {
                    if (mainProteinTable.getValue() == itemId) {
                        mainProteinTable.unselect(itemId);
                    } else {
                        mainProteinTable.select(itemId);
                    }
                }
            };
            if (userCustomizedComparison != null) {
                if (userCustomizedComparison.getQuantComparisonProteinMap().containsKey(accession)) {
                    protTrendLayout.updateCustTrend(userCustomizedComparison.getQuantComparisonProteinMap().get(accession).getSignificantTrindCategory());
                }
            }
            if (protId == 0) {
                instanceOfLinechartComponentsLayout = protTrendLayout.getChartComponentsLayout();
            }
            
            RadioButton btn = new RadioButton(protId) {
                @Override
                public void selectItem(Object itemId) {
                    if (mainProteinTable.getValue() != null && mainProteinTable.getValue().equals(itemId)) {
                        mainProteinTable.unselect(itemId);
                    } else {
                        mainProteinTable.select(itemId);
                    }
                }
            };

            if (userCustomizedComparison == null || userCustomizedComparison.getQuantComparisonProteinMap() == null) {
                tableItemsMap.put(protId, new Object[]{protId + 1, btn, accessionObject, name, null, protTrendLayout});
            } else {
                int trend;
                switch ((int) userCustomizedComparison.getQuantComparisonProteinMap().get(accession).getOverallCellPercentValue()) {
                    case 100:
                        trend = 0;
                        break;
                    case 0:
                        trend = 3;
                        break;
                    default:
                        trend = 4;
                        break;
                }
                TrendSymbol trendSymbol = new TrendSymbol(trend);
                trendSymbol.setTrend((int) userCustomizedComparison.getQuantComparisonProteinMap().get(accession).getOverallCellPercentValue());
                trendSymbol.setWidth(12, Unit.PIXELS);
                trendSymbol.setHeight(12, Unit.PIXELS);
                trendSymbol.setDescription("" + userCustomizedComparison.getQuantComparisonProteinMap().get(accession).getSignificantTrindCategory());
                tableItemsMap.put(protId, new Object[]{protId + 1, btn, accessionObject, name, trendSymbol, protTrendLayout});
            }
            activeTableItemsMap.put(protId, tableItemsMap.get(protId));
            mainProteinTable.addItem(tableItemsMap.get(protId), protId);
            if (!tableProteinsToIDMap.containsKey(accession)) {
                tableProteinsToIDMap.put(accession, protId);
            } else {
                tableProteinsToIDMap.put(accession, tableProteinsToIDMap.get(accession) + "_-_" + protId);
            }
            protId++;

        }
        updateComparisonsHeader(selectedComparisonsList);
        ColumnHeaderLayout defaultSorting;
        if (userCustomizedComparison != null) {
            columnHeaderSet.iterator().next().sort(false, 0);

            defaultSorting = custUserComparisonSortingLayout;
            defaultSorting.sort(false, -1);
        } else {
            defaultSorting = columnHeaderSet.iterator().next();
            defaultSorting.sort(false, 0);
        }

        defaultSorting.setAsDefault();

        mainProteinTable.setSortEnabled(
                false);
        int indexing = 1;
        for (Object id
                : mainProteinTable.getItemIds()) {
            Item item = mainProteinTable.getItem(id);
            item.getItemProperty("Index").setValue(indexing);
            indexing++;
        }

        this.mainProteinTable.addValueChangeListener(ProteinTable.this);
        updateIconRowNumber(mainProteinTable.getItemIds().size(), generateThumbImg());
        mainProteinTable.refreshRowCache();
        mainProteinTable.setWidthUndefined();

    }

    /**
     * Update comparisons Sort/Filter layout components
     *
     * @param selectedComparisonsList list of selected comparisons (selected
     * from the heat map component).
     */
    private void updateComparisonsHeader(Set<QuantDiseaseGroupsComparison> selectedComparisonsList) {
        topComparisonsContainer.removeAllComponents();
        columnHeaderSet.clear();
        if (instanceOfLinechartComponentsLayout == null) {
            return;
        }
        int index = 0;
        ColumnHeaderLayout comparisonLayout;
        Iterator<Component> itr = this.instanceOfLinechartComponentsLayout.iterator();
        Map<Integer, Float> comparisonIndexLocation = new LinkedHashMap<>();
        while (itr.hasNext()) {
            TrendSymbol square = (TrendSymbol) itr.next();
            comparisonIndexLocation.put((Integer) square.getParam("comparisonIndex"), instanceOfLinechartComponentsLayout.getPosition(square).getLeftValue());
        }

        for (QuantDiseaseGroupsComparison comparison : selectedComparisonsList) {
            comparisonLayout = new ColumnHeaderLayout(comparison, index) {

                @Override
                public void sort(boolean up, int index) {
                    sortOnComparison(up, index);
                }

                @Override
                public void dropComparison(QuantDiseaseGroupsComparison comparison) {

                    ProteinTable.this.dropComparison(comparison);
                }

                @Override
                public void filterTable(QuantDiseaseGroupsComparison comparison, int comparisonIndex, Set<Object> filterSet, boolean unselectfilter) {
                    filterTableSelection(comparison, new ArrayList<>(filterSet), unselectfilter);
                    sortOnComparison(ascendingSort, sortingColumnHeader);
                }

                @Override
                public void filterTable(QuantDiseaseGroupsComparison comparison, int comparisonIndex, double min, double max, boolean unselectfilter) {
                    filterTableSelection(comparison, min, max, unselectfilter);
                    sortOnComparison(ascendingSort, sortingColumnHeader);
                }

                @Override
                public void filterTable(QuantDiseaseGroupsComparison comparison, int comparisonIndex, long min, long max, boolean unselectfilter) {
                    filterTableSelection(comparison, min, max, unselectfilter);
                    sortOnComparison(ascendingSort, sortingColumnHeader);
                }

                @Override
                public void filterTable(QuantDiseaseGroupsComparison comparison, int comparisonIndex, double min, double max, double secMin, double secMax, boolean unselectfilter) {
                    filterTableSelection(comparison, min, max, secMin, secMax, unselectfilter);
                    sortOnComparison(ascendingSort, sortingColumnHeader);
                }

            };
            filtersMap.put(comparison, null);

            topComparisonsContainer.addComponent(comparisonLayout, "left: " + comparisonIndexLocation.get(index) + "px; top: " + 0 + "px;");
            columnHeaderSet.add(comparisonLayout);
            index++;

        }
        if (userCustomizedComparison != null) {
            columnHeaderSet.add(custUserComparisonSortingLayout);
        }
    }

    /**
     * Get customized comparison based on user input data in quant comparison
     * layout
     *
     * @return userCustomizedComparison Customized comparison based on user
     * input data
     */
    public QuantDiseaseGroupsComparison getUserCustomizedComparison() {
        return userCustomizedComparison;
    }

    /**
     * Drop comparison (un select comparison)
     *
     * @param index the comparison index.
     */
    public abstract void dropComparison(QuantDiseaseGroupsComparison index);

    /**
     * Filter and update the protein table using the top comparison filters
     * (local filtering)
     *
     * @param comparison the comparison used for filtering the data.
     * @param filters set of the applied filters from this comparison.
     */
    private void filterTableSelection(QuantDiseaseGroupsComparison comparison, List<Object> filters, boolean unselectfilter) {

        if (filters == null || filters.isEmpty()) {
            filters = null;
        }
        if (unselectfilter) {
            filtersMap.get(comparison).remove("category");
            filtereApplied = false;
        } else {
            if (!filtersMap.containsKey(comparison) || filtersMap.get(comparison) == null) {
                Map<String, List<Object>> map = new HashMap<>();
                map.put("category", null);
                map.put("trend", null);
                map.put("studynum", null);
                filtersMap.put(comparison, map);
            }
            filtersMap.get(comparison).put("category", filters);
            filtereApplied = true;

        }
        Set<String> filteredProteinsList = new LinkedHashSet<>(this.tableProteinsToIDMap.keySet());

        for (QuantDiseaseGroupsComparison i : filtersMap.keySet()) {
            if (filtersMap.get(i) != null) {
                filteredProteinsList = filterProteinList(filteredProteinsList, i, filtersMap.get(i).get("category"));
                if (filtersMap.get(i).get("trend") != null) {
                    filteredProteinsList = filterProteinList(filteredProteinsList, i, filtersMap.get(i).get("trend"));
                }
                if (filtersMap.get(i).get("studynum") != null) {
                    filteredProteinsList = filterProteinList(filteredProteinsList, i, filtersMap.get(i).get("studynum"));
                }

            }

        }

        mainProteinTable.removeAllItems();
        Set<String> filteredDataList = new LinkedHashSet<>();
        for (String accession : filteredProteinsList) {
            Object itemId = tableProteinsToIDMap.get(accession);
            if (tableItemsMap.containsKey(itemId)) {
                Object[] items = tableItemsMap.get(itemId);
                filteredDataList.add(((ExternalLink) items[2]).getData() + "");
                mainProteinTable.addItem(items, itemId);
            }

        }
        if (mainProteinTable.getItemIds().size() == tableItemsMap.size()) {
            filtereApplied = false;
        }
        inUseSselectedProteinsList = new LinkedHashSet<>();
        for (QuantComparisonProtein prot : fullSselectedProteinsList) {
            if (filteredProteinsList.contains(prot.getProteinAccession())) {
                inUseSselectedProteinsList.add(prot);
            }

        }
        int indexing = 1;
        for (Object id
                : mainProteinTable.getItemIds()) {
            Item item = mainProteinTable.getItem(id);
            item.getItemProperty("Index").setValue(indexing);
            indexing++;
        }
        updateIconRowNumber(mainProteinTable.getItemIds().size(), generateThumbImg());
        for (ColumnHeaderLayout header : columnHeaderSet) {
//            if (header.getComparison() == comparison) {
//                continue;
//            }
            header.updateHeaderData(filteredDataList, comparison, 0);

        }

    }

    /**
     * Filter and update the protein table using the top comparison filters
     * (local filtering)
     *
     * @param comparison the comparison used for filtering the data.
     * @param filters set of the applied filters from this comparison.
     */
    private void filterTableSelection(QuantDiseaseGroupsComparison comparison, long min, long max, boolean unselectfilter) {
        if (unselectfilter) {
            filtersMap.get(comparison).remove("studynum");
            filtereApplied = false;
            return;
//            }
        } else {
            List<Object> filters = new ArrayList<>();
            filters.add(min);
            filters.add(max);
            if (!filtersMap.containsKey(comparison) || filtersMap.get(comparison) == null) {
                Map<String, List<Object>> map = new HashMap<>();
                map.put("category", null);
                map.put("trend", null);
                map.put("studynum", null);
                filtersMap.put(comparison, map);
            }
            filtersMap.get(comparison).put("studynum", filters);
            filtereApplied = true;

        }
        Set<String> filteredProteinsList = new LinkedHashSet<>(this.tableProteinsToIDMap.keySet());
        for (QuantDiseaseGroupsComparison i : filtersMap.keySet()) {
            if (filtersMap.get(i) != null) {
                filteredProteinsList = filterProteinList(filteredProteinsList, i, filtersMap.get(i).get("studynum"));
                if (filtersMap.get(i).get("category") != null) {
                    filteredProteinsList = filterProteinList(filteredProteinsList, i, filtersMap.get(i).get("category"));
                }
                if (filtersMap.get(i).get("trend") != null) {
                    filteredProteinsList = filterProteinList(filteredProteinsList, i, filtersMap.get(i).get("trend"));
                }
            }

        }

        mainProteinTable.removeAllItems();
        Set<String> filteredDataList = new LinkedHashSet<>();
        for (String accession : filteredProteinsList) {
            Object itemId = tableProteinsToIDMap.get(accession);
            if (tableItemsMap.containsKey(itemId)) {
                Object[] items = tableItemsMap.get(itemId);
                filteredDataList.add(((ExternalLink) items[2]).getData() + "");
                mainProteinTable.addItem(items, itemId);
            }

        }
        if (mainProteinTable.getItemIds().size() == tableItemsMap.size()) {
            filtereApplied = false;
        }
        inUseSselectedProteinsList = new LinkedHashSet<>();
        for (QuantComparisonProtein prot : fullSselectedProteinsList) {
            if (filteredProteinsList.contains(prot.getProteinAccession())) {
                inUseSselectedProteinsList.add(prot);
            }

        }
        int indexing = 1;
        for (Object id
                : mainProteinTable.getItemIds()) {
            Item item = mainProteinTable.getItem(id);
            item.getItemProperty("Index").setValue(indexing);
            indexing++;
        }
        updateIconRowNumber(mainProteinTable.getItemIds().size(), generateThumbImg());
        for (ColumnHeaderLayout header : columnHeaderSet) {
//            if (header.getComparison() == comparison) {
//                continue;
//            }
            header.updateHeaderData(filteredDataList, comparison, 2);

        }

    }

    /**
     * Filter and update the protein table using the top comparison filters
     * (local filtering)
     *
     * @param comparison the comparison used for filtering the data.
     * @param filters set of the applied filters from this comparison.
     */
    private void filterTableSelection(QuantDiseaseGroupsComparison comparison, double min, double max, boolean unselectfilter) {
        if (unselectfilter) {
            if (filtersMap.containsKey(comparison) && filtersMap.get(comparison) != null) {
                filtersMap.get(comparison).remove("trend");
            }
            filtereApplied = false;
        } else {
            List<Object> filters = new ArrayList<>();
            filters.add(min);
            filters.add(max);
            if (!filtersMap.containsKey(comparison) || filtersMap.get(comparison) == null) {
                Map<String, List<Object>> map = new HashMap<>();
                map.put("category", null);
                map.put("trend", null);
                map.put("studynum", null);
                filtersMap.put(comparison, map);
            }
            filtersMap.get(comparison).put("trend", filters);
            filtereApplied = true;

        }
        Set<String> filteredProteinsList = new LinkedHashSet<>(this.tableProteinsToIDMap.keySet());
        for (QuantDiseaseGroupsComparison i : filtersMap.keySet()) {
            if (filtersMap.get(i) != null) {
                filteredProteinsList = filterProteinList(filteredProteinsList, i, filtersMap.get(i).get("trend"));
                if (filtersMap.get(i).get("category") != null) {
                    filteredProteinsList = filterProteinList(filteredProteinsList, i, filtersMap.get(i).get("category"));
                }
                if (filtersMap.get(i).get("trend") != null) {
                    filteredProteinsList = filterProteinList(filteredProteinsList, i, filtersMap.get(i).get("studynum"));
                }
            }

        }

        mainProteinTable.removeAllItems();
        Set<String> filteredDataList = new LinkedHashSet<>();
        for (String accession : filteredProteinsList) {
            Object itemId = tableProteinsToIDMap.get(accession);

            if (tableItemsMap.containsKey(itemId)) {
                Object[] items = tableItemsMap.get(itemId);
                filteredDataList.add(((ExternalLink) items[2]).getData() + "");
                mainProteinTable.addItem(items, itemId);
            }

        }
        if (mainProteinTable.getItemIds().size() == tableItemsMap.size()) {
            filtereApplied = false;
        }
        inUseSselectedProteinsList = new LinkedHashSet<>();
        for (QuantComparisonProtein prot : fullSselectedProteinsList) {
            if (filteredProteinsList.contains(prot.getProteinAccession())) {
                inUseSselectedProteinsList.add(prot);
            }

        }
        int indexing = 1;
        for (Object id
                : mainProteinTable.getItemIds()) {
            Item item = mainProteinTable.getItem(id);
            item.getItemProperty("Index").setValue(indexing);
            indexing++;
        }
        updateIconRowNumber(mainProteinTable.getItemIds().size(), generateThumbImg());
        for (ColumnHeaderLayout header : columnHeaderSet) {
//            if (header.getComparison() == comparison) {
//                continue;
//            }
            header.updateHeaderData(filteredDataList, comparison, 1);

        }

    }

    /**
     * Filter and update the protein table using the top comparison filters
     * (local filtering)
     *
     * @param comparison the comparison used for filtering the data.
     * @param filters set of the applied filters from this comparison.
     */
    private void filterTableSelection(QuantDiseaseGroupsComparison comparison, double min, double max, double secMin, double secMax, boolean unselectfilter) {
        if (unselectfilter) {
            if (filtersMap.containsKey(comparison) && filtersMap.get(comparison) != null) {
                filtersMap.get(comparison).remove("trend");
            }
            filtereApplied = false;
        } else {
            List<Object> filters = new ArrayList<>();
            filters.add(min);
            filters.add(max);
            filters.add(secMin);
            filters.add(secMax);
            if (!filtersMap.containsKey(comparison) || filtersMap.get(comparison) == null) {
                Map<String, List<Object>> map = new HashMap<>();
                map.put("category", null);
                map.put("trend", null);
                map.put("studynum", null);
                filtersMap.put(comparison, map);
            }
            filtersMap.get(comparison).put("trend", filters);
            filtereApplied = true;

        }
        Set<String> filteredProteinsList = new LinkedHashSet<>(this.tableProteinsToIDMap.keySet());
        for (QuantDiseaseGroupsComparison i : filtersMap.keySet()) {
            if (filtersMap.get(i) != null) {
                filteredProteinsList = filterProteinList(filteredProteinsList, i, filtersMap.get(i).get("trend"));
                if (filtersMap.get(i).get("category") != null) {
                    filteredProteinsList = filterProteinList(filteredProteinsList, i, filtersMap.get(i).get("category"));
                }
                if (filtersMap.get(i).get("trend") != null) {
                    filteredProteinsList = filterProteinList(filteredProteinsList, i, filtersMap.get(i).get("studynum"));
                }
            }

        }

        mainProteinTable.removeAllItems();
        Set<String> filteredDataList = new LinkedHashSet<>();
        for (String accession : filteredProteinsList) {
            Object itemId = tableProteinsToIDMap.get(accession);
            if (tableItemsMap.containsKey(itemId)) {
                Object[] items = tableItemsMap.get(itemId);
                filteredDataList.add(((ExternalLink) items[2]).getData() + "");
                mainProteinTable.addItem(items, itemId);
            }

        }
        if (mainProteinTable.getItemIds().size() == tableItemsMap.size()) {
            filtereApplied = false;
        }
        inUseSselectedProteinsList = new LinkedHashSet<>();
        for (QuantComparisonProtein prot : fullSselectedProteinsList) {
            if (filteredProteinsList.contains(prot.getProteinAccession())) {
                inUseSselectedProteinsList.add(prot);
            }

        }
        int indexing = 1;
        for (Object id
                : mainProteinTable.getItemIds()) {
            Item item = mainProteinTable.getItem(id);
            item.getItemProperty("Index").setValue(indexing);
            indexing++;
        }
        updateIconRowNumber(mainProteinTable.getItemIds().size(), generateThumbImg());
        for (ColumnHeaderLayout header : columnHeaderSet) {
//            if (header.getComparison() == comparison) {
//                continue;
//            }
            header.updateHeaderData(filteredDataList, comparison, 1);

        }

    }

    /**
     * Filter current protein list using the applied filters from comparison.
     *
     * @param proteinsList current filtered proteins list.
     * @param comparison the comparison used for filtering the data.
     * @param filters set of the applied filters from this comparison.
     */
    private Set<String> filterProteinList(Set<String> proteinsList, QuantDiseaseGroupsComparison comparison, List<Object> filters) {
        if (filters == null || filters.isEmpty()) {
            return proteinsList;
        }
        Set<String> updatedProteinsList = new LinkedHashSet<>();
        Object objectValue = filters.iterator().next();
        if (objectValue instanceof Double) {
            double lower = (Double) filters.get(0);
            double upper = (Double) filters.get(1);
            List<Integer> filtesList = new ArrayList<>();
            if (lower < 0 || upper < 0) {
                filtesList.add(0);
                filtesList.add(1);
            }
            if ((lower == 0 || upper == 0) || (lower <= 0 && upper >= 0)) {
                filtesList.add(2);
            }
            if (lower > 0 || upper > 0) {
                filtesList.add(3);
                filtesList.add(4);
            }
            filtesList.add(5);
            if (filters.size() == 4) {
                double secLower = (Double) filters.get(2);
                double secUpper = (Double) filters.get(3);
                if (secLower < 0 || secUpper < 0) {
                    filtesList.add(0);
                    filtesList.add(1);
                }
                if ((secLower == 0 || secUpper == 0) || (secLower <= 0 && secUpper >= 0)) {
                    filtesList.add(2);
                }
                if (secLower > 0 || secUpper > 0) {
                    filtesList.add(3);
                    filtesList.add(4);
                }

                filtesList.stream().map((filter) -> (comparison.getProteinsByTrendMap().get(filter))).forEach((tempList) -> {
                    tempList.stream().filter((protein) -> (proteinsList.contains(protein.getProteinAccession()))).forEach((protein) -> {
                        double value = protein.getOverallCellPercentValue();
                        if ((value >= lower && value <= upper) || ((value >= secLower && value <= secUpper))) {
                            updatedProteinsList.add(protein.getProteinAccession());
                        }
                    });
                });
            } else {

                filtesList.stream().map((filter) -> (comparison.getProteinsByTrendMap().get(filter))).forEach((tempList) -> {
                    tempList.stream().filter((protein) -> (proteinsList.contains(protein.getProteinAccession()))).forEach((protein) -> {
                        double value = protein.getOverallCellPercentValue();
                        if (value >= lower && value <= upper) {
                            updatedProteinsList.add(protein.getProteinAccession());
                        }
                    });
                });
            }
            return updatedProteinsList;
        } else if (objectValue instanceof Long) {

            long min = (Long) objectValue;
            long max;
            if (filters.size() == 2) {
                max = (Long) filters.toArray()[1];
            } else {
                max = min;
            }
            List<Integer> filtesList = new ArrayList<>();
            filtesList.add(0);
            filtesList.add(1);
            filtesList.add(2);
            filtesList.add(3);
            filtesList.add(4);
            filtesList.add(5);
            filtesList.stream().map((filter) -> (comparison.getProteinsByTrendMap().get(filter))).forEach((tempList) -> {
                tempList.stream().filter((protein) -> (proteinsList.contains(protein.getProteinAccession()))).forEach((protein) -> {
                    int value = protein.getDsQuantProteinsMap().size();
//                    System.out.println("study number invoke " + min + "  " + max + "  value " + value);

                    if (value >= min && value <= max) {
                        updatedProteinsList.add(protein.getProteinAccession());
                    }
                });
            });
//            System.out.println("study number invoke " + min + "  " + max + "  filtered list size " + updatedProteinsList.size());
            return updatedProteinsList;
        }

        filters.stream().map((filter) -> (comparison.getProteinsByTrendMap().get((Integer) filter))).forEach((tempList) -> {
            tempList.stream().filter((protein) -> (proteinsList.contains(protein.getProteinAccession()))).forEach((protein) -> {
                updatedProteinsList.add(protein.getProteinAccession());
            });
        });

        return updatedProteinsList;

    }

    /**
     * Protein table selection action.
     *
     * @param event protein table selection event.
     */
    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        if (event.getProperty().getValue() == null) {
            selectProtein(null, -1);
            return;
        }
        String value = null;
        ExternalLink link = (ExternalLink) mainProteinTable.getItem(event.getProperty().getValue()).getItemProperty("Accession").getValue();
        if (link != null) {
            value = link.getData() + "";
        }
        if (userCustomizedComparison != null) {
            if (userCustomizedComparison.getQuantComparisonProteinMap().containsKey(value)) {
                selectProtein(value, userCustomizedComparison.getQuantComparisonProteinMap().get(value).getSignificantTrindCategory());
                return;
            }

        }
        selectProtein(value, -1);

    }

    /**
     * Select protein from the protein table to update the system.
     *
     * @param selectedProtein the selected protein key.
     * @param custTrend the customized user trend(in case of quant comparing
     * mode).
     */
    public abstract void selectProtein(String selectedProtein, int custTrend);

    /**
     * Update the left side button icon and text.
     *
     * @param rowNumber number of proteins (row number).
     * @param URl image encoded into Based64 string.
     */
    public abstract void updateIconRowNumber(int rowNumber, String URl);
}
