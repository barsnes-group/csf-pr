package no.uib.probe.csf.pr.touch.view.components.linechartproteintablecomponents;

import com.vaadin.data.Item;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.QuantComparisonProtein;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;

/**
 *
 * This class represents table is made for exporting protein table data data
 *
 * @author Yehia Farag
 *
 */
public class ExportProteinTable extends VerticalLayout {

    /**
     * The main table to export.
     */
    private Table exportTable;
    /*
     * Customized comparison based on user input data in quant comparison layout.
     */
    private QuantDiseaseGroupsComparison userCustomizedComparison;

    /**
     * Set customized comparison based on user input data in quant comparison
     * layout.
     *
     * @param userCustomizedComparison user input data in quant compare mode.
     */
    public void setUserCustomizedComparison(QuantDiseaseGroupsComparison userCustomizedComparison) {
        this.userCustomizedComparison = userCustomizedComparison;
    }

    /**
     * Constructor to initialize the main attributes.
     */
    public ExportProteinTable() {
        this.setVisible(false);
    }

    /**
     * Get generated export table.
     *
     * @return exportTable the table to be exported.
     */
    public Table getExportTable() {
        return exportTable;
    }

    /**
     * Update export table data.
     *
     * @param selectedComparisonsList list of quant disease comparisons objects.
     * @param selectedProteinsList list of the selected quant protein objects.
     * exporting.
     * @param upSort the sorting direction.
     */
    public void updateTableData(Set<QuantDiseaseGroupsComparison> selectedComparisonsList, Set<QuantComparisonProtein> selectedProteinsList, boolean upSort) {
        this.removeAllComponents();
        exportTable = new Table();
        this.addComponent(exportTable);
        int extracol = 0;
//        exportTable.addContainerProperty("Index", Integer.class, null, "", null, Table.Align.RIGHT);
        exportTable.addContainerProperty("Accession", String.class, null, "Accession", null, Table.Align.LEFT);
        exportTable.addContainerProperty("Name", String.class, null, "Name", null, Table.Align.LEFT);

        Object[] objects;
        int i = 0;
        if (userCustomizedComparison != null) {
            extracol = 1;
            objects = new Object[3 + selectedComparisonsList.size()];
            exportTable.addContainerProperty("userdata", Double.class, null, "User Data", null, Table.Align.CENTER);
        } else {
            objects = new Object[2 + selectedComparisonsList.size()];
        }
        for (QuantDiseaseGroupsComparison quantComp : selectedComparisonsList) {
            String compName = quantComp.getComparisonHeader().split(" / ")[0].split("__")[0] + " / " + quantComp.getComparisonHeader().split(" / ")[1].split("__")[0] + " (" + quantComp.getDiseaseCategory() + ")";
            exportTable.addContainerProperty(quantComp.getComparisonHeader(), Double.class, null, compName, null, Table.Align.CENTER);

        }

        int protId = 0;
        for (QuantComparisonProtein protein : selectedProteinsList) {
            String accession = protein.getProteinAccession();//.replace("(unreviewed)", " (Unreviewed)");
            String name = protein.getProteinName();
            objects[0] = accession;
            objects[1] = name;

            if (userCustomizedComparison != null) {
                if (userCustomizedComparison.getQuantComparisonProteinMap().containsKey(accession)) {
                    objects[2] = (userCustomizedComparison.getQuantComparisonProteinMap().get(accession).getOverallCellPercentValue());
                }

            }
            i = 2 + extracol;

            for (QuantDiseaseGroupsComparison quantComp : selectedComparisonsList) {
                String key = "";
                if (quantComp.getQuantComparisonProteinMap().containsKey("0_" + accession)) {
                    key = "0_" + accession;
                } else if (quantComp.getQuantComparisonProteinMap().containsKey("1_" + accession)) {
                    key = "1_" + accession;
                } else if (quantComp.getQuantComparisonProteinMap().containsKey("2_" + accession)) {
                    key = "2_" + accession;

                } else if (quantComp.getQuantComparisonProteinMap().containsKey("3_" + accession)) {

                    key = "3_" + accession;
                } else if (quantComp.getQuantComparisonProteinMap().containsKey("4_" + accession)) //                                    if (quantComp.getQuantComparisonProteinMap().containsValue(protien.getProtKey())) {
                {
                    key = "4_" + accession;

                }

                if (quantComp.getQuantComparisonProteinMap().containsKey(key)) {

                    objects[i] = quantComp.getQuantComparisonProteinMap().get(key).getOverallCellPercentValue();
                } else {
                    objects[i] = null;
                }
                i++;

            }
//            objects[0] = protId + 1;
            exportTable.addItem(objects, protId);
            protId++;

        }
//        if (!sortingHeader.equalsIgnoreCase("Comparisons Overview") && !sortingHeader.equalsIgnoreCase("Index") && !sortingHeader.equalsIgnoreCase("userdata")) {//
//            this.exportTable.sort(new String[]{sortingHeader}, new boolean[]{upSort});
//        } else {
            this.exportTable.sort(new String[]{"Accession"}, new boolean[]{true});
//        }
        this.exportTable.commit();
//        int indexing = 1;
//        for (Object id
//                : exportTable.getItemIds()) {
//            Item item = exportTable.getItem(id);
//            item.getItemProperty("Index").setValue(indexing);
//            indexing++;
//        }
//        System.out.println("at export table sort based on " + sortingHeader + "--------" + (userCustomizedComparison != null) + "  table has header " + exportTable.getColumnHeader(sortingHeader));

    }

}
