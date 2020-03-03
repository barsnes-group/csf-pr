package no.uib.probe.csf.pr.touch.logic.dataset;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import no.uib.probe.csf.pr.touch.logic.CoreLogic;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseCategoryObject;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseGroupComparison;
import no.uib.probe.csf.pr.touch.logic.beans.HeatMapHeaderCellInformationBean;
import no.uib.probe.csf.pr.touch.logic.beans.InitialInformationObject;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDataset;
import no.uib.probe.csf.pr.touch.selectionmanager.QuantSearchSelection;

/**
 * This class responsible for maintaining the active dataset do all calculations
 * this class interact with Data_Handler and CoreLogic.
 *
 * @author Yehia Farag
 */
public class DatasetUtility implements Serializable {

    /**
     * Map of disease category (MS,AD,PD..etc)to quant data included for each of
     * these diseases.
     */
    private final Map<String, QuantData> quantDataMap;
    /**
     * Map of disease category (MS,AD,PD..etc)to DiseaseCategoryObject that has
     * all disease category information.
     */
    private final LinkedHashMap<String, DiseaseCategoryObject> fullDiseaseCategoryMap;
    /**
     * Map of disease category (MS,AD,PD..etc)to DiseaseCategoryObject that has
     * all disease category information.
     */
    private LinkedHashMap<String, DiseaseCategoryObject> activeDiseaseCategoryMap;
    /**
     * Map of disease category (MS,AD,PD..etc)to DiseaseCategoryObject that has
     * all disease category information.
     */
    private final LinkedHashMap<String, DiseaseCategoryObject> searchCompareDiseaseCategoryMap;
    /**
     * Map of disease category (MS,AD,PD..etc)to initial information object that
     * has initial information for each disease category.
     */
    private Map<String, InitialInformationObject> quantDatasetInitialInformationObject;
    /**
     * Map of disease category (MS,AD,PD..etc)to the initial disease categories
     * updated by CSF-PR 2.0 administrator.
     */
    private Map<String, Map<String, String>> default_DiseaseCat_DiseaseGroupMap;
    /**
     * The current used map of disease category (MS,AD,PD..etc) information.
     */
    private Map<String, Map<String, String>> inUse_DiseaseCat_DiseaseGroupMap;
    /**
     * Map of disease category (MS,AD,PD..etc)to original publication
     * information.
     */
    private Map<String, Map<String, String>> oreginal_DiseaseCat_DiseaseGroupMap;
    /**
     * Disease group A created with random word.
     */
    private String userDiseaseGroupA = "VeryHårdToExistByChanceøæå";
    /**
     * Disease group B created with random word.
     */
    private String userDiseaseGroupB = "VeryHårdToExistByChanceøæå";
    /**
     * Map of short to full disease sub-group names map.
     */
    private Map<String, String> diseaseGroupFullNameMap;
    /**
     * Re-indexing map for MS disease used to sort disease groups in the
     * heat-map.
     */
    private List<String> msReindexMap;
    /**
     * Re-index map for AD disease used to sort disease groups in the heat-map.
     */
    private List<String> adReindexMap;
    /**
     * Re-index map for PD disease used to sort disease groups in the heat-map.
     */
    private List<String> pdReindexMap;

    /**
     * Re-index map for Amyotrophic Lateral Sclerosis disease used to sort
     * disease groups in the heat-map.
     */
    private List<String> amyReindexMap;

    /**
     * Suggested sub-groups name as default (updated by CSF-PR 2.0).
     */
    private final Map<String, String> suggestedSubGroupMap;

    /**
     * Current active quant data object that interact in the system.
     */
    private Map<String, QuantData> activeData;
    private Set<Integer> activeQuantDatasetsIndexes;
    /**
     * The system is in initialising state.
     */
    private boolean init = true;
    private boolean databaseOffline = false;

    /**
     * Check if database is available
     *
     * @return database is not available
     */
    public boolean isDatabaseOffline() {
        return databaseOffline;
    }

    /**
     * Constructor to initialize the main attributes
     *
     * @param Core_Logic main logic layer.
     */
    public DatasetUtility(CoreLogic Core_Logic) {
        quantDataMap = new LinkedHashMap<>();
        this.suggestedSubGroupMap = new LinkedHashMap<>();
        this.fullDiseaseCategoryMap = new LinkedHashMap<>(Core_Logic.getDiseaseCategorySet());
        this.activeDiseaseCategoryMap = fullDiseaseCategoryMap;
        this.searchCompareDiseaseCategoryMap = new LinkedHashMap<>();
        if (fullDiseaseCategoryMap == null) {
            databaseOffline = true;
            return;
        }
        fullDiseaseCategoryMap.keySet().forEach((diseaseCategory) -> {
            this.searchCompareDiseaseCategoryMap.put(diseaseCategory, this.cloneDiseaseCategoryObject(diseaseCategory));
        });

        suggestedSubGroupMap.put("Alzheimer's", "Alzheimer's");
        suggestedSubGroupMap.put("CIS-MS(CIS)", "CIS-MS(CIS)");
        suggestedSubGroupMap.put("CIS-CIS", "CIS-CIS");
        suggestedSubGroupMap.put("CIS-MS", "CIS-MS");
        suggestedSubGroupMap.put("Aged healthy", "Healthy*");
        suggestedSubGroupMap.put("Healthy controls", "Healthy*");
        suggestedSubGroupMap.put("MCI", "MCI");
        suggestedSubGroupMap.put("MCI nonprogressors", "MCI nonprogressors");
        suggestedSubGroupMap.put("MCI progressors", "MCI progressors");
        suggestedSubGroupMap.put("RRMS Nataliz.", "RRMS Nataliz.");
        suggestedSubGroupMap.put("SPMS Lamotri.", "SPMS Lamotri.");
        suggestedSubGroupMap.put("Non MS", "Non MS");
        suggestedSubGroupMap.put("OIND", "OIND");
        suggestedSubGroupMap.put("OIND + OND", "OIND + OND");
        suggestedSubGroupMap.put("OND", "OND");
        suggestedSubGroupMap.put("Sympt. controls", "Sympt. controls");
        suggestedSubGroupMap.put("Aged controls", "Aged controls");
        suggestedSubGroupMap.put("NDC", "NDC");
        suggestedSubGroupMap.put("Non-neurodeg.", "Non-neurodeg.");
        suggestedSubGroupMap.put("Parkinson's", "Parkinson's");
        suggestedSubGroupMap.put("PDD", "PDD");
        suggestedSubGroupMap.put("PMS", "PMS");
        suggestedSubGroupMap.put("SPMS", "SPMS");
        suggestedSubGroupMap.put("Healthy", "Healthy*");
        suggestedSubGroupMap.put("RRMS", "RRMS");

        this.quantDatasetInitialInformationObject = Core_Logic.getQuantDatasetInitialInformationObject();

        default_DiseaseCat_DiseaseGroupMap = new LinkedHashMap<>();
        oreginal_DiseaseCat_DiseaseGroupMap = new LinkedHashMap<>();
        quantDatasetInitialInformationObject.keySet().stream().filter((str) -> !(str.equalsIgnoreCase("All Diseases"))).forEach((str) -> {
            Set<String> diseaseGroupsName = Core_Logic.getDiseaseGroupNameMap(str);

            Map<String, String> diseaseGroupMap = new LinkedHashMap<>();
            Map<String, String> oreginalDiseaseGroupMap = new LinkedHashMap<>();
            diseaseGroupsName.stream().forEach((diseaseGroupName) -> {
                oreginalDiseaseGroupMap.put(diseaseGroupName, diseaseGroupName);
                if (suggestedSubGroupMap.containsKey(diseaseGroupName)) {
                    diseaseGroupMap.put(diseaseGroupName, suggestedSubGroupMap.get(diseaseGroupName));
                } else {
                    diseaseGroupMap.put(diseaseGroupName, diseaseGroupName);
                }
            });

            default_DiseaseCat_DiseaseGroupMap.put(str, diseaseGroupMap);
            oreginal_DiseaseCat_DiseaseGroupMap.put(str, oreginalDiseaseGroupMap);
        });

        this.diseaseGroupFullNameMap = Core_Logic.getDiseaseGroupsFullNameMap();
        inUse_DiseaseCat_DiseaseGroupMap = default_DiseaseCat_DiseaseGroupMap;
        msReindexMap = new ArrayList<>();
        msReindexMap.add("MS__Multiple Sclerosis__multiplesclerosisstyle");
        msReindexMap.add("CDMS__Multiple Sclerosis__multiplesclerosisstyle");
        msReindexMap.add("RRMS__Multiple Sclerosis__multiplesclerosisstyle");
        msReindexMap.add("RRMS + CIS__Multiple Sclerosis__multiplesclerosisstyle");
        msReindexMap.add("CIS-MS__Multiple Sclerosis__multiplesclerosisstyle");
        msReindexMap.add("CIS-MS(CIS)__Multiple Sclerosis__multiplesclerosisstyle");
        msReindexMap.add("CIS-CIS__Multiple Sclerosis__multiplesclerosisstyle");
        msReindexMap.add("CIS__Multiple Sclerosis__multiplesclerosisstyle");
        msReindexMap.add("PMS__Multiple Sclerosis__multiplesclerosisstyle");
        msReindexMap.add("SPMS__Multiple Sclerosis__multiplesclerosisstyle");
        msReindexMap.add("MCI__Multiple Sclerosis__multiplesclerosisstyle");
        msReindexMap.add("OIND__Multiple Sclerosis__multiplesclerosisstyle");
        msReindexMap.add("OND__Multiple Sclerosis__multiplesclerosisstyle");
        msReindexMap.add("PPMS__Multiple Sclerosis__multiplesclerosisstyle");
        msReindexMap.add("RRMS Nataliz.__Multiple Sclerosis__multiplesclerosisstyle");
        msReindexMap.add("SPMS Lamotri.__Multiple Sclerosis__multiplesclerosisstyle");
        msReindexMap.add("Non MS__Multiple Sclerosis__multiplesclerosisstyle");
        msReindexMap.add("OIND + OND__Multiple Sclerosis__multiplesclerosisstyle");
        msReindexMap.add("Sympt. controls__Multiple Sclerosis__multiplesclerosisstyle");
        msReindexMap.add("Neurol. healthy__Multiple Sclerosis__multiplesclerosisstyle");
        msReindexMap.add("Cong.healthy__Multiple Sclerosis__multiplesclerosisstyle");
        msReindexMap.add("Healthy*__Multiple Sclerosis__multiplesclerosisstyle");

//        msReindexMap.add("Progressive MS__Multiple Sclerosis__multiplesclerosisstyle");
//        msReindexMap.add("MS treated__Multiple Sclerosis__multiplesclerosisstyle");
//        msReindexMap.add("Healthy controls__Multiple Sclerosis__multiplesclerosisstyle");
        pdReindexMap = new ArrayList<>();
        pdReindexMap.add("Parkinson's__Parkinson's__parkinsonstyle");
        pdReindexMap.add("PDD__Parkinson's__parkinsonstyle");
//        pdReindexMap.add("Alzheimer's__Parkinson's__parkinsonstyle");

        pdReindexMap.add("Non Demented__Parkinson's__parkinsonstyle");
        pdReindexMap.add("Non-neurodeg.__Parkinson's__parkinsonstyle");
        pdReindexMap.add("Non Neurodeg.__Parkinson's__parkinsonstyle");
        pdReindexMap.add("Healthy*__Parkinson's__parkinsonstyle");
        pdReindexMap.add("Healthy controls__Parkinson's__parkinsonstyle");
        pdReindexMap.add("NDC__Parkinson's__parkinsonstyle");

        adReindexMap = new ArrayList<>();
        adReindexMap.add("Alzheimer's__Alzheimer's__alzheimerstyle");
        adReindexMap.add("AD dementia__Alzheimer's__alzheimerstyle");
        adReindexMap.add("Preclinical AD__Alzheimer's__alzheimerstyle");
        adReindexMap.add("Prodomal AD__Alzheimer's__alzheimerstyle");
        adReindexMap.add("MCI-AD__Alzheimer's__alzheimerstyle");
        adReindexMap.add("MCI-MCI__Alzheimer's__alzheimerstyle");
        adReindexMap.add("MCI__Alzheimer's__alzheimerstyle");
        adReindexMap.add("FTD__Alzheimer's__alzheimerstyle");
        adReindexMap.add("LBD__Alzheimer's__alzheimerstyle");

        adReindexMap.add("Non-AD; healthy ex.__Alzheimer's__alzheimerstyle");
        adReindexMap.add("Non-AD__Alzheimer's__alzheimerstyle");
        adReindexMap.add("Aged non-AD__Alzheimer's__alzheimerstyle");
        adReindexMap.add("Non-demented__Alzheimer's__alzheimerstyle");

//        adReindexMap.add("Parkinson's__Alzheimer's__alzheimerstyle");
//        adReindexMap.add("Non Alzheimer's\n" + "Alzheimer-s_Disease");
        adReindexMap.add("Non Neurodeg.__Alzheimer's__alzheimerstyle");
        adReindexMap.add("Non-neurodeg.__Alzheimer's__alzheimerstyle");
        adReindexMap.add("Ment. healthy__Alzheimer's__alzheimerstyle");
        adReindexMap.add("Cogn. healthy__Alzheimer's__alzheimerstyle");
        adReindexMap.add("Aged healthy__Alzheimer's__alzheimerstyle");
        adReindexMap.add("Healthy*__Alzheimer's__alzheimerstyle");
        adReindexMap.add("Healthy controls__Alzheimer's__alzheimerstyle");

        amyReindexMap = new ArrayList<>();
        amyReindexMap.add("ALS__Amyotrophic Lateral Sclerosis__amyotrophicstyle");
        amyReindexMap.add("sALS__Amyotrophic Lateral Sclerosis__amyotrophicstyle");
        amyReindexMap.add("OND__Amyotrophic Lateral Sclerosis__amyotrophicstyle");

        amyReindexMap.add("Healthy*__Amyotrophic Lateral Sclerosis__amyotrophicstyle");
        amyReindexMap.add("Control__Amyotrophic Lateral Sclerosis__amyotrophicstyle");

    }

    /**
     * get Current active disease categories
     *
     * @return set of disease categories
     */
    public Set<String> getActiveDiseaseCategorySet() {
        return activeData.keySet();
    }

    /**
     * Get set of available disease categories that has all disease category
     * information
     *
     * @return fullDiseaseCategoryMap values - disease category set
     */
    public Collection<DiseaseCategoryObject> getDiseaseCategorySet() {
        if (activeDiseaseCategoryMap == null) {
            return null;
        }
        return activeDiseaseCategoryMap.values();
    }

    /**
     * Set main disease category (MS,AD,PD or all)based on user selection
     *
     * @param mainDiseaseCategories Disease category name
     */
    public void setMainDiseaseCategory(Set<String> mainDiseaseCategories) {
        if (init) {
            init = false;
            LinkedHashMap<String, HeatMapHeaderCellInformationBean> oreginalColumnIds = new LinkedHashMap<>();
            LinkedHashMap<String, HeatMapHeaderCellInformationBean> oreginalRowIds = new LinkedHashMap<>();
            LinkedHashMap<String, HeatMapHeaderCellInformationBean> activeColumnIds = new LinkedHashMap<>();
            LinkedHashMap<String, HeatMapHeaderCellInformationBean> activeRowIds = new LinkedHashMap<>();
            Set<DiseaseGroupComparison> diseaseComparisonSet = new LinkedHashSet<>();
            QuantData allQuantData = new QuantData();
            if (quantDatasetInitialInformationObject == null) {
                return;
            }
            quantDatasetInitialInformationObject.keySet().forEach((diseaseCategoryname) -> {
                InitialInformationObject initQuantData = quantDatasetInitialInformationObject.get(diseaseCategoryname);
                updateQuantDatasetsList(diseaseCategoryname, initQuantData.getQuantDatasetsMap());
                if (!(diseaseCategoryname.equalsIgnoreCase("All Diseases"))) {
                    QuantData quantData = updateDiseaseGroups(initQuantData.getQuantDatasetsMap(), diseaseCategoryname);
                    quantData.setDiseaseCategories(diseaseCategoryname);
                    quantData.setActiveDatasetPieChartsFilters(initQuantData.getActiveDatasetPieChartsFilters());
                    updateGroupsNames(diseaseCategoryname, quantData);
                    quantDataMap.put(diseaseCategoryname, quantData);
                    oreginalColumnIds.putAll(quantData.getOreginalColumnIds());
                    oreginalRowIds.putAll(quantData.getOreginalRowIds());
                    activeColumnIds.putAll(quantData.getActiveColumnIds());
                    activeRowIds.putAll(quantData.getActiveRowIds());
                    diseaseComparisonSet.addAll(quantData.getDiseaseGroupComparisonsSet());
                    allQuantData.setActiveDatasetPieChartsFilters(quantData.getActiveDatasetPieChartsFilters());
                }
            });
            allQuantData.setOreginalColumnIds(oreginalColumnIds);
            allQuantData.setOreginalRowIds(oreginalRowIds);
            allQuantData.setActiveColumnIds(activeColumnIds);
            allQuantData.setActiveRowIds(activeRowIds);
            allQuantData.setDiseaseComparisonSet(diseaseComparisonSet);
            allQuantData.setDiseaseCategories("All Diseases");
            quantDataMap.put("All Diseases", allQuantData);

            default_DiseaseCat_DiseaseGroupMap = sortDiseaseCategoryGroups(default_DiseaseCat_DiseaseGroupMap);
            oreginal_DiseaseCat_DiseaseGroupMap = sortDiseaseCategoryGroups(oreginal_DiseaseCat_DiseaseGroupMap);
            inUse_DiseaseCat_DiseaseGroupMap = default_DiseaseCat_DiseaseGroupMap;
            fullDiseaseCategoryMap.values().stream().filter((diseaseCategory) -> !(diseaseCategory.getDiseaseCategory().equalsIgnoreCase("All Diseases"))).forEach((diseaseCategory) -> {
                diseaseCategory.setDiseaseSubGroups(inUse_DiseaseCat_DiseaseGroupMap.get(diseaseCategory.getDiseaseCategory()));
                diseaseCategory.setDiseaseSubGroupsToFullName(diseaseGroupFullNameMap);
            });
            activeData = quantDataMap;
        }

    }

    /**
     * Get the disease group row labels for the current active disease category
     *
     * @return active row labels category set
     */
    public LinkedHashMap<String, HeatMapHeaderCellInformationBean> getRowLabels() {
        LinkedHashMap<String, HeatMapHeaderCellInformationBean> rowLabelList = new LinkedHashMap<>();
        activeData.keySet().forEach((key) -> {
            rowLabelList.putAll(activeData.get(key).getActiveRowIds());
        });
        return rowLabelList;
    }

    /**
     * Get the disease group column labels for the current active disease
     * category
     *
     * @return active column labels category set
     */
    public LinkedHashMap<String, HeatMapHeaderCellInformationBean> getColumnLabels() {
        LinkedHashMap<String, HeatMapHeaderCellInformationBean> columnLabelList = new LinkedHashMap<>();
        activeData.keySet().forEach((key) -> {
            columnLabelList.putAll(activeData.get(key).getActiveColumnIds());
        });
        return columnLabelList;

    }

    /**
     * Get the disease group comparisons for the current active disease category
     *
     * @return active disease group comparisons
     */
    public Set<DiseaseGroupComparison> getDiseaseGroupComparisonsSet() {
        LinkedHashSet<DiseaseGroupComparison> groupComparisonsSet = new LinkedHashSet<>();
        activeData.keySet().forEach((key) -> {
            groupComparisonsSet.addAll(activeData.get(key).getDiseaseGroupComparisonsSet());
        });
        return groupComparisonsSet;

    }

    /**
     * Update heat-map column and row labels and disease comparison set
     *
     * @param quantDSArr Array of datasets.
     * @param diseaseCategory Disease category name.
     * @return updated quantData object for the disease category
     */
    private QuantData updateDiseaseGroups(Map<Integer, QuantDataset> quantDSArr, String diseaseCategory) {
        QuantData quantData = new QuantData();

        String[] diseaseGroupsI = new String[quantDSArr.size()];
        String[] diseaseGroupsII = new String[quantDSArr.size()];
        Set<DiseaseGroupComparison> diseaseComparisonSet = new LinkedHashSet<>();
        int i = 0;
        for (QuantDataset ds : quantDSArr.values()) {
            if (ds == null) {
                continue;
            }

            DiseaseGroupComparison diseaseGroup = new DiseaseGroupComparison();
            diseaseGroup.setDiseaseCategory(ds.getDiseaseCategoryI(), ds.getDiseaseCategoryII());
            diseaseGroup.setDiseaseStyleName(ds.getDiseaseStyleName());
            String pgI = ds.getDiseaseMainGroupI();
            diseaseGroup.setDiseaseMainGroupI(pgI);
            diseaseGroup.setCrossDisease(ds.isCrossDisease());
            String subpgI = ds.getDiseaseSubGroup1();
            diseaseGroup.setOriginalDiseaseSubGroupI(subpgI);

            String label1;
            if (pgI.equalsIgnoreCase("Not Available") || pgI.equalsIgnoreCase("control")) {
                pgI = "";
            }
            if (!subpgI.equalsIgnoreCase("") && !subpgI.equalsIgnoreCase("Not Available")) {
                pgI = subpgI;
            }
            label1 = pgI;
            diseaseGroup.setActiveDiseaseSubGroupI(label1);
            String pgII = ds.getDiseaseMainGroup2();
            diseaseGroup.setDiseaseMainGroupII(pgII);
            String subpgII = ds.getDiseaseSubGroup2();
            diseaseGroup.setOriginalDiseaseSubGroupII(subpgII);
            String label2;
            if (pgII.equalsIgnoreCase("Not Available") || pgII.equalsIgnoreCase("control")) {
                pgII = "";
            }

            if (!subpgII.equalsIgnoreCase("") && !subpgII.equalsIgnoreCase("Not Available")) {
                pgII = subpgII;
            }
            label2 = pgII;
            diseaseGroup.setActiveDiseaseSubGroupII(label2);
            diseaseGroupsI[i] = label1 + "__" + diseaseGroup.getDiseaseCategoryI() + "__" + diseaseGroup.getDiseaseStyleName();
            diseaseGroupsII[i] = label2 + "__" + diseaseGroup.getDiseaseCategoryII() + "__" + diseaseGroup.getDiseaseStyleName();

            diseaseGroup.setQuantDatasetIndex(ds.getQuantDatasetIndex());
            diseaseComparisonSet.add(diseaseGroup);
            i++;
        }
        if (diseaseGroupsI == null || diseaseGroupsII == null) {
            System.out.println("error at 85 class " + this.getClass().getName() + "   ");
            return null;
        }
        String[] pgArr = sortGroups(merge(diseaseGroupsI, diseaseGroupsII), diseaseCategory);
        LinkedHashMap<String, HeatMapHeaderCellInformationBean> selectedHeatMapRows = new LinkedHashMap<>();
        for (String str : pgArr) {
            if (!str.equalsIgnoreCase("") && !str.contains(userDiseaseGroupB)) {
                HeatMapHeaderCellInformationBean headerCellInfo = new HeatMapHeaderCellInformationBean();
                headerCellInfo.setDiseaseGroupName(str.split("__")[0]);
                headerCellInfo.setDiseaseGroupOreginalName(str.split("__")[0]);
                headerCellInfo.setDiseaseCategory(str.split("__")[1]);
                headerCellInfo.setDiseaseStyleName(str.split("__")[2]);
                headerCellInfo.setDiseaseCategory(str.split("__")[1]);
                headerCellInfo.setDiseaseHashedColor(fullDiseaseCategoryMap.get(headerCellInfo.getDiseaseCategory()).getDiseaseHashedColor());
                headerCellInfo.setDiseaseGroupFullName(diseaseGroupFullNameMap.get(headerCellInfo.getDiseaseGroupName()));
                selectedHeatMapRows.put(headerCellInfo.toString(), headerCellInfo);
            }
        }
        LinkedHashMap<String, HeatMapHeaderCellInformationBean> selectedHeatMapColumns = new LinkedHashMap<>();
        for (String str : pgArr) {
            if (!str.equalsIgnoreCase("") && !str.contains(userDiseaseGroupA)) {
                HeatMapHeaderCellInformationBean headerCellInfo = new HeatMapHeaderCellInformationBean();
                headerCellInfo.setDiseaseGroupName(str.split("__")[0]);
                headerCellInfo.setDiseaseGroupOreginalName(str.split("__")[0]);
                headerCellInfo.setDiseaseCategory(str.split("__")[1]);
                headerCellInfo.setDiseaseStyleName(str.split("__")[2]);
                headerCellInfo.setDiseaseHashedColor(fullDiseaseCategoryMap.get(headerCellInfo.getDiseaseCategory()).getDiseaseHashedColor());
                headerCellInfo.setDiseaseGroupFullName(diseaseGroupFullNameMap.get(headerCellInfo.getDiseaseGroupName()));
                selectedHeatMapColumns.put(headerCellInfo.toString(), headerCellInfo);
            }
        }

//        
        quantData.setOreginalColumnIds(selectedHeatMapColumns);
        quantData.setOreginalRowIds(selectedHeatMapRows);
        quantData.setDiseaseComparisonSet(diseaseComparisonSet);
//          if(diseaseCategory.equalsIgnoreCase("Parkinson's")  ){
//        
//        for(HeatMapHeaderCellInformationBean str :selectedHeatMapRows)
//                System.out.println("at pg "+str.getDiseaseCategoryI()+"  "+str.getDiseaseGroupFullName());
//        
//        }
        return quantData;

    }

    /**
     * Sort the groups to be ready to view in the heat map
     *
     * @param subGroupNamesArray Array of sub-group names
     * @param diseaseCategory Disease category name.
     * @return updated array of string for the disease category labels (columns
     * and rows)
     */
    private String[] sortGroups(String[] subGroupNamesArray, String diseaseCategory) {
        String[] sortedData = new String[subGroupNamesArray.length];
        List<String> quantDataSet = new ArrayList<>(Arrays.asList(subGroupNamesArray));
        int count = 0;

        if (diseaseCategory.equalsIgnoreCase("Multiple Sclerosis")) {
            for (String str : msReindexMap) {
                if (quantDataSet.contains(str)) {
                    sortedData[count] = str;
                    quantDataSet.remove(str);
                    count++;
                }
            }
            for (String str : quantDataSet) {
                sortedData[count] = str;
                count++;
            }
            quantDataSet.clear();
            quantDataSet.addAll(Arrays.asList(sortedData));
        } else if (diseaseCategory.equalsIgnoreCase("Alzheimer's")) {
            for (String str : adReindexMap) {
                if (quantDataSet.contains(str)) {
                    sortedData[count] = str;
                    quantDataSet.remove(str);
                    count++;
                }
            }
            for (String str : quantDataSet) {
                sortedData[count] = str;
                count++;
            }
            quantDataSet.clear();
            quantDataSet.addAll(Arrays.asList(sortedData));
        } else if (diseaseCategory.equalsIgnoreCase("Parkinson's")) {
            for (String str : pdReindexMap) {
                if (quantDataSet.contains(str)) {
                    sortedData[count] = str;
                    quantDataSet.remove(str);
                    count++;
                }
            }
            for (String str : quantDataSet) {
                sortedData[count] = str;
                count++;
            }
            quantDataSet.clear();
            quantDataSet.addAll(Arrays.asList(sortedData));
        } else if (diseaseCategory.equalsIgnoreCase("Amyotrophic Lateral Sclerosis")) {
            for (String str : amyReindexMap) {
                if (quantDataSet.contains(str)) {
                    sortedData[count] = str;
                    quantDataSet.remove(str);
                    count++;
                }
            }
            for (String str : quantDataSet) {
                sortedData[count] = str;
                count++;
            }
            quantDataSet.clear();
            quantDataSet.addAll(Arrays.asList(sortedData));
        } else if (diseaseCategory.contains("_")) {
            System.out.println("have no idea what is going on here ---->>> " + diseaseCategory);

        } else {
            //All Diseases

            for (String str : msReindexMap) {
                if (quantDataSet.contains(str)) {
                    sortedData[count] = str;
                    quantDataSet.remove(str);
                    count++;
                }
            }
            for (String str : quantDataSet) {
                if (str.contains("Multiple Sclerosis")) {
                    sortedData[count] = str;
                    count++;
                }

            }

            for (String str : pdReindexMap) {
                if (quantDataSet.contains(str)) {
                    sortedData[count] = str;
                    quantDataSet.remove(str);
                    count++;
                }
            }
            for (String str : quantDataSet) {
                if (str.contains("Parkinson's")) {
                    sortedData[count] = str;
                    count++;
                }

            }
            for (String str : adReindexMap) {
                if (quantDataSet.contains(str)) {
                    sortedData[count] = str;
                    quantDataSet.remove(str);
                    count++;
                }
            }
            for (String str : quantDataSet) {
                if (str.contains("Alzheimer's")) {
                    sortedData[count] = str;
                    count++;
                }

            }
            for (String str : amyReindexMap) {
                if (quantDataSet.contains(str)) {
                    sortedData[count] = str;
                    quantDataSet.remove(str);
                    count++;
                }
            }
            for (String str : quantDataSet) {
                if (str.contains("Amyotrophic Lateral Sclerosis")) {
                    sortedData[count] = str;
                    count++;
                }

            }
            quantDataSet.clear();
            quantDataSet.addAll(Arrays.asList(sortedData));

        }

        return sortedData;

    }

    /**
     * Merge two string array into one array and remove doubled data then
     * sorting the data after that
     *
     * @param arr1 Disease sub-group I names array
     * @param arr2Disease sub-group II names array
     * @return sorted string array
     */
    private String[] merge(String[] arr1, String[] arr2) {
        String[] newArr = new String[arr1.length + arr2.length];

        int i = 0;
        for (String str : arr1) {
            newArr[i] = str;
            i++;
        }
        for (String str : arr2) {
            newArr[i] = str;
            i++;
        }

        Arrays.sort(newArr);

        Map<String, ArrayList<String>> sortOnGroupMap = new LinkedHashMap<>();
        for (int x = newArr.length - 1; x >= 0; x--) {
            String s = newArr[x];
            String diseaseCat = s.split("__")[1];
            if (!sortOnGroupMap.containsKey(diseaseCat)) {
                sortOnGroupMap.put(diseaseCat, new ArrayList<>());
            }
            ArrayList<String> set = sortOnGroupMap.get(diseaseCat);
            set.add(s);
            sortOnGroupMap.put(diseaseCat, set);

        }
        String[] sortedArr = new String[newArr.length];
        int index = 0;
        for (String diseaseCat : sortOnGroupMap.keySet()) {
            ArrayList<String> set = sortOnGroupMap.get(diseaseCat);
            Collections.sort(set);
            for (String fullName : set) {
                sortedArr[index] = fullName;
                index++;

            }
        }

        return sortedArr;

    }

    /**
     * Get the full quant dataset map the current active disease category
     *
     * @return map of quant dataset objects
     */
    public Map<Integer, QuantDataset> getActiveQuantDsMap() {
        Map<Integer, QuantDataset> quantDatasetsMap = new LinkedHashMap<>();
        if (activeQuantDatasetsIndexes == null) {
            activeData.keySet().forEach((key) -> {
                quantDatasetsMap.putAll(quantDatasetInitialInformationObject.get(key).getQuantDatasetsMap());
            });
        } else {
            activeData.keySet().forEach((key) -> {
                Map<Integer, QuantDataset> catQuantDatasetsMap = quantDatasetInitialInformationObject.get(key).getQuantDatasetsMap();
                activeQuantDatasetsIndexes.stream().filter((i) -> (catQuantDatasetsMap.containsKey(i))).forEachOrdered((i) -> {
                    quantDatasetsMap.put(i, catQuantDatasetsMap.get(i));
                });
            });
        }

        return quantDatasetsMap;

    }

    /**
     * Get the active data columns for the current active disease category
     *
     * @return boolean array of active column
     */
    public boolean[] getActiveDataColumns() {
        if (activeData == null) {
            return null;
        }
        return activeData.values().iterator().next().getActiveDatasetPieChartsFilters();
    }

    /**
     * Update and combine disease sub groups based on user selection
     *
     *
     * @param updatedGroupsNamesMap updated disease sub group names
     */
    public void updateCobinedGroups(Map<String, Map<String, String>> updatedGroupsNamesMap) {
        //if null reset to publication 
        if (updatedGroupsNamesMap == null) {
            this.inUse_DiseaseCat_DiseaseGroupMap = oreginal_DiseaseCat_DiseaseGroupMap;
        } else if (updatedGroupsNamesMap.isEmpty()) {
            this.inUse_DiseaseCat_DiseaseGroupMap = this.default_DiseaseCat_DiseaseGroupMap;
        } else {
            this.inUse_DiseaseCat_DiseaseGroupMap = sortDiseaseCategoryGroups(updatedGroupsNamesMap);
        }
        quantDataMap.keySet().stream().forEach((diseaseCategory) -> {
            updateGroupsNames(diseaseCategory, quantDataMap.get(diseaseCategory));
            InitialInformationObject initQuantData = quantDatasetInitialInformationObject.get(diseaseCategory);
            updateQuantDatasetsList(diseaseCategory, initQuantData.getQuantDatasetsMap());
        });

    }

    /**
     * Update disease sub-group names list based in user selection
     *
     * @param diseaseCategoryName Disease category name
     * @param quantDatasetsList Map of quant datasets
     */
    private void updateQuantDatasetsList(String diseaseCategoryName, Map<Integer, QuantDataset> quantDatasetsList) {

        if (!inUse_DiseaseCat_DiseaseGroupMap.containsKey(diseaseCategoryName)) {
            return;

        }
        Map<String, String> updatedNamesMap = inUse_DiseaseCat_DiseaseGroupMap.get(diseaseCategoryName);
        quantDatasetsList.values().stream().map((quantDataset) -> {
            quantDataset.setActiveDiseaseSubGroupI(updatedNamesMap.get(quantDataset.getDiseaseSubGroup1()));
            return quantDataset;
        }).forEach((quantDataset) -> {
            quantDataset.setActiveDiseaseSubGroupII(updatedNamesMap.get(quantDataset.getDiseaseSubGroup2()));
        });

    }

    /**
     * Update disease sub-group names based in user selection
     *
     * @param diseaseCategoryName Disease category name
     * @param updatingData Quant data contains all quant data information
     * required in the visualization layout.
     */
    private void updateGroupsNames(String diseaseCategoryName, QuantData updatingData) {
        if (!inUse_DiseaseCat_DiseaseGroupMap.containsKey(diseaseCategoryName)) {
            return;

        }
        Map<String, String> updatedNamesMap = inUse_DiseaseCat_DiseaseGroupMap.get(diseaseCategoryName);
        LinkedHashMap<String, HeatMapHeaderCellInformationBean> activeColumnIds = new LinkedHashMap<>();
        LinkedHashMap<String, HeatMapHeaderCellInformationBean> activeRowIds = new LinkedHashMap<>();
        updatingData.getOreginalColumnIds().values().stream().filter((header) -> !(!updatedNamesMap.containsKey(header.getDiseaseGroupOreginalName()))).forEach((header) -> {
            header.setDiseaseGroupName(updatedNamesMap.get(header.getDiseaseGroupOreginalName()));
            activeColumnIds.put(header.toString(), header);

        });
        updatingData.getOreginalRowIds().values().stream().filter((header) -> !(!updatedNamesMap.containsKey(header.getDiseaseGroupOreginalName()))).forEach((header) -> {
            header.setDiseaseGroupName(updatedNamesMap.get(header.getDiseaseGroupOreginalName()));
            activeRowIds.put(header.toString(), header);
        });
        Set<DiseaseGroupComparison> diseaseComparisonSet = updatingData.getDiseaseGroupComparisonsSet();
        Set<DiseaseGroupComparison> updatedDiseaseComparisonSet = new LinkedHashSet<>();
        diseaseComparisonSet.stream().map((comparison) -> {
            if (updatedNamesMap.containsKey(comparison.getOriginalDiseaseSubGroupI())) {
                comparison.setActiveDiseaseSubGroupI(updatedNamesMap.get(comparison.getOriginalDiseaseSubGroupI()));
            }
            return comparison;
        }).map((comparison) -> {
            if (updatedNamesMap.containsKey(comparison.getOriginalDiseaseSubGroupII())) {
                comparison.setActiveDiseaseSubGroupII(updatedNamesMap.get(comparison.getOriginalDiseaseSubGroupII()));
            }
            return comparison;
        }).forEach((comparison) -> {
            updatedDiseaseComparisonSet.add(comparison);
        });

        updatingData.setDiseaseComparisonSet(updatedDiseaseComparisonSet);
        updatingData.setActiveColumnIds(activeColumnIds);
        updatingData.setActiveRowIds(activeRowIds);

    }

    /**
     * Sort disease sub-group names for disease categories based on sorting maps
     *
     * @param mapToSort Map of disease category with its own disease sub-group
     * name map.
     */
    private Map<String, Map<String, String>> sortDiseaseCategoryGroups(Map<String, Map<String, String>> mapToSort) {
        Map<String, Map<String, String>> sortedMap = new LinkedHashMap<>();

        mapToSort.keySet().stream().forEach((String diseaseCategory) -> {
            QuantData quantData = quantDataMap.get(diseaseCategory);
            Map<String, String> subGroupMap = mapToSort.get(diseaseCategory);
            Map<String, String> sortedSubGroupMap = new LinkedHashMap<>();
            quantData.getOreginalRowIds().values().stream().forEach((group) -> {
                sortedSubGroupMap.put(group.getDiseaseGroupOreginalName(), subGroupMap.get(group.getDiseaseGroupOreginalName()));
            });
            sortedMap.put(diseaseCategory, sortedSubGroupMap);
        });

        Map<String, String> sortedAllSubGroupMap = new LinkedHashMap<>();
        sortedMap.values().stream().forEach((subGroupMap) -> {
            sortedAllSubGroupMap.putAll(subGroupMap);
        });
        sortedMap.put("All Diseases", sortedAllSubGroupMap);

        return sortedMap;
    }

    /**
     * Get full disease sub groups name list
     *
     * @return set of disease sub group name
     */
    public Set<String> getFullDiseaseGroupNameSet() {
        Set<String> fulldiseaseGroupsNameSet = new TreeSet<>();
        oreginal_DiseaseCat_DiseaseGroupMap.keySet().stream().forEach((key) -> {
            fulldiseaseGroupsNameSet.addAll(oreginal_DiseaseCat_DiseaseGroupMap.get(key).keySet());
        });
        return fulldiseaseGroupsNameSet;

    }

    /**
     * Get the current used map of disease category (MS,AD,PD..etc) information
     *
     * @return inUse_DiseaseCat_DiseaseGroupMap
     */
    public Map<String, Map<String, String>> getInUse_DiseaseCat_DiseaseGroupMap() {
        return inUse_DiseaseCat_DiseaseGroupMap;
    }

    /**
     * Activate searching layout sub-data handler
     *
     * @param searchSelection search selection data
     */
    public void switchToSearchingMode(QuantSearchSelection searchSelection) {
        activeData = null;
        activeQuantDatasetsIndexes = searchSelection.getQuantDatasetIndexes();
        Map<String, QuantData> searchingDataMap = new LinkedHashMap<>();
        activeDiseaseCategoryMap = new LinkedHashMap<>();

        if (searchSelection.getDiseaseCategories().contains("Multiple Sclerosis")) {
            QuantData searchingData = new QuantData();
            searchingData.setDiseaseCategories("Multiple Sclerosis");
            searchingData.setActiveColumnIds(new LinkedHashMap<>());
            searchingData.setActiveRowIds(new LinkedHashMap<>());
            searchingData.setDiseaseComparisonSet(new LinkedHashSet<>());
            searchingDataMap.put("Multiple Sclerosis", searchingData);
            DiseaseCategoryObject diseaseCategoryObject = searchCompareDiseaseCategoryMap.get("Multiple Sclerosis");
            diseaseCategoryObject.setDatasetNumber(0);
            activeDiseaseCategoryMap.put(diseaseCategoryObject.getDiseaseCategory(), diseaseCategoryObject);
        }
        if (searchSelection.getDiseaseCategories().contains("Alzheimer's")) {
            QuantData searchingData = new QuantData();
            searchingData.setDiseaseCategories("Alzheimer's");
            searchingData.setActiveColumnIds(new LinkedHashMap<>());
            searchingData.setActiveRowIds(new LinkedHashMap<>());
            searchingData.setDiseaseComparisonSet(new LinkedHashSet<>());
            searchingDataMap.put("Alzheimer's", searchingData);
            DiseaseCategoryObject diseaseCategoryObject = searchCompareDiseaseCategoryMap.get("Alzheimer's");
            diseaseCategoryObject.setDatasetNumber(0);
            activeDiseaseCategoryMap.put(diseaseCategoryObject.getDiseaseCategory(), diseaseCategoryObject);
        }
        if (searchSelection.getDiseaseCategories().contains("Parkinson's")) {
            QuantData searchingData = new QuantData();
            searchingData.setDiseaseCategories("Parkinson's");
            searchingData.setActiveRowIds(new LinkedHashMap<>());
            searchingData.setDiseaseComparisonSet(new LinkedHashSet<>());
            searchingData.setActiveColumnIds(new LinkedHashMap<>());
            searchingDataMap.put("Parkinson's", searchingData);
            DiseaseCategoryObject diseaseCategoryObject = searchCompareDiseaseCategoryMap.get("Parkinson's");
            diseaseCategoryObject.setDatasetNumber(0);
            activeDiseaseCategoryMap.put(diseaseCategoryObject.getDiseaseCategory(), diseaseCategoryObject);
        }
        if (searchSelection.getDiseaseCategories().contains("Amyotrophic Lateral Sclerosis")) {
            QuantData searchingData = new QuantData();
            searchingData.setDiseaseCategories("Amyotrophic Lateral Sclerosis");
            searchingData.setActiveRowIds(new LinkedHashMap<>());
            searchingData.setActiveColumnIds(new LinkedHashMap<>());
            searchingData.setDiseaseComparisonSet(new LinkedHashSet<>());
            searchingDataMap.put("Amyotrophic Lateral Sclerosis", searchingData);
            DiseaseCategoryObject diseaseCategoryObject = searchCompareDiseaseCategoryMap.get("Amyotrophic Lateral Sclerosis");
            diseaseCategoryObject.setDatasetNumber(0);
            activeDiseaseCategoryMap.put(diseaseCategoryObject.getDiseaseCategory(), diseaseCategoryObject);
        }

        DiseaseCategoryObject diseaseCategoryObject = searchCompareDiseaseCategoryMap.get("All Diseases");
        diseaseCategoryObject.setDatasetNumber(0);
        activeDiseaseCategoryMap.put(diseaseCategoryObject.getDiseaseCategory(), diseaseCategoryObject);

        QuantData refData = quantDataMap.get("All Diseases");
        for (int index : searchSelection.getQuantDatasetIndexes()) {
            DiseaseGroupComparison diseaseGroupComparison = refData.getIndexToComparisons().get(index);
            String keyI = this.inUse_DiseaseCat_DiseaseGroupMap.get(diseaseGroupComparison.getDiseaseCategoryI()).get(diseaseGroupComparison.getOriginalDiseaseSubGroupI()) + "__" + diseaseGroupComparison.getDiseaseCategoryI();
            if (refData.getActiveRowIds().containsKey(keyI)) {
                searchingDataMap.get(diseaseGroupComparison.getDiseaseCategoryI()).getActiveColumnIds().put(keyI, refData.getActiveRowIds().get(keyI));
                searchingDataMap.get(diseaseGroupComparison.getDiseaseCategoryI()).getActiveRowIds().put(keyI, refData.getActiveRowIds().get(keyI));

            }
            String keyII = this.inUse_DiseaseCat_DiseaseGroupMap.get(diseaseGroupComparison.getDiseaseCategoryII()).get(diseaseGroupComparison.getOriginalDiseaseSubGroupII()) + "__" + diseaseGroupComparison.getDiseaseCategoryII();
            if (refData.getActiveRowIds().containsKey(keyII)) {
                searchingDataMap.get(diseaseGroupComparison.getDiseaseCategoryII()).getActiveColumnIds().put(keyII, refData.getActiveRowIds().get(keyII));
                searchingDataMap.get(diseaseGroupComparison.getDiseaseCategoryII()).getActiveRowIds().put(keyII, refData.getActiveRowIds().get(keyII));
            }
            searchingDataMap.get(diseaseGroupComparison.getDiseaseCategoryI()).getDiseaseGroupComparisonsSet().add(diseaseGroupComparison);
            searchingDataMap.get(diseaseGroupComparison.getDiseaseCategoryII()).getDiseaseGroupComparisonsSet().add(diseaseGroupComparison);

            searchingDataMap.get(diseaseGroupComparison.getDiseaseCategoryI()).getIndexToComparisons().put(diseaseGroupComparison.getQuantDatasetIndex(), diseaseGroupComparison);
            searchingDataMap.get(diseaseGroupComparison.getDiseaseCategoryII()).getIndexToComparisons().put(diseaseGroupComparison.getQuantDatasetIndex(), diseaseGroupComparison);

            searchingDataMap.get(diseaseGroupComparison.getDiseaseCategoryI()).setActiveDatasetPieChartsFilters(refData.getActiveDatasetPieChartsFilters());
            searchingDataMap.get(diseaseGroupComparison.getDiseaseCategoryII()).setActiveDatasetPieChartsFilters(refData.getActiveDatasetPieChartsFilters());
            searchingDataMap.get(diseaseGroupComparison.getDiseaseCategoryI()).setOreginalColumnIds(refData.getOreginalColumnIds());
            searchingDataMap.get(diseaseGroupComparison.getDiseaseCategoryI()).setOreginalRowIds(refData.getOreginalRowIds());
            searchingDataMap.get(diseaseGroupComparison.getDiseaseCategoryII()).setOreginalColumnIds(refData.getOreginalColumnIds());
            searchingDataMap.get(diseaseGroupComparison.getDiseaseCategoryII()).setOreginalRowIds(refData.getOreginalRowIds());

        }

        for (String key : searchingDataMap.keySet()) {
            activeDiseaseCategoryMap.get(key).setDatasetNumber(searchingDataMap.get(key).getIndexToComparisons().size());

        }
        activeDiseaseCategoryMap.get("All Diseases").setDatasetNumber(searchSelection.getQuantDatasetIndexes().size());
        activeData = searchingDataMap;
    }

    private LinkedHashMap<String, HeatMapHeaderCellInformationBean> sortColumnsRowsInformation(LinkedHashMap<String, HeatMapHeaderCellInformationBean> activeColumnsRowsInformation) {
        LinkedHashMap<String, HeatMapHeaderCellInformationBean> sortedColumnsRowsInformation = new LinkedHashMap<>();
        Map<String, LinkedHashSet<String>> diseaseCategoryToSort = new LinkedHashMap<>();
        for (String key : activeColumnsRowsInformation.keySet()) {
            String diseaseCategory = key.split("__")[1];
            String subGroup = key.split("__")[0];
            if (!diseaseCategoryToSort.containsKey(diseaseCategory)) {
                diseaseCategoryToSort.put(diseaseCategory, new LinkedHashSet<>());
            }
            subGroup = subGroup + "__" + activeColumnsRowsInformation.get(key).getDiseaseCategory() + "__" + activeColumnsRowsInformation.get(key).getDiseaseStyleName();
            diseaseCategoryToSort.get(diseaseCategory).add(subGroup);
        }
        Map<String, LinkedHashSet<String>> sortedDiseaseCategory = sortGroups(diseaseCategoryToSort);
        sortedDiseaseCategory.keySet().forEach((diseaseCategory) -> {
            sortedDiseaseCategory.get(diseaseCategory).stream().map((subGroup) -> subGroup.split("__")[0] + "__" + diseaseCategory).forEachOrdered((key) -> {
                sortedColumnsRowsInformation.put(key, activeColumnsRowsInformation.get(key));
            });
        });

        return sortedColumnsRowsInformation;
    }

    /**
     * Sort the groups to be ready to view in the heat map
     *
     * @param subGroupNamesArray Array of sub-group names
     * @param diseaseCategory Disease category name.
     * @return updated array of string for the disease category labels (columns
     * and rows)
     */
    private Map<String, LinkedHashSet<String>> sortGroups(Map<String, LinkedHashSet<String>> diseaseCategoryToSort) {
        Map<String, LinkedHashSet<String>> sortedDiseaseCategory = new LinkedHashMap<>();
        if (diseaseCategoryToSort.containsKey("Multiple Sclerosis")) {
            sortedDiseaseCategory.put("Multiple Sclerosis", new LinkedHashSet<>());
        }
        if (diseaseCategoryToSort.containsKey("Alzheimer's")) {
            sortedDiseaseCategory.put("Alzheimer's", new LinkedHashSet<>());
        }
        if (diseaseCategoryToSort.containsKey("Parkinson's")) {
            sortedDiseaseCategory.put("Parkinson's", new LinkedHashSet<>());
        }
        if (diseaseCategoryToSort.containsKey("Amyotrophic Lateral Sclerosis")) {
            sortedDiseaseCategory.put("Amyotrophic Lateral Sclerosis", new LinkedHashSet<>());
        }

        sortedDiseaseCategory.keySet().forEach((diseaseCategory) -> {
            List<String> reindexMap;
            if (diseaseCategory.equalsIgnoreCase("Multiple Sclerosis")) {
                reindexMap = msReindexMap;
            } else if (diseaseCategory.equalsIgnoreCase("Alzheimer's")) {
                reindexMap = adReindexMap;
            } else if (diseaseCategory.equalsIgnoreCase("Parkinson's")) {
                reindexMap = pdReindexMap;
            } else {
                reindexMap = amyReindexMap;
            }
            sortAndClear(reindexMap, diseaseCategoryToSort.get(diseaseCategory), sortedDiseaseCategory.get(diseaseCategory));
        });
        return sortedDiseaseCategory;

    }

    private void sortAndClear(final List<String> reindexMap, final Set<String> toSortSet, final Set<String> sortedSet) {
        reindexMap.stream().filter((str) -> (toSortSet.contains(str))).map((str) -> {
            sortedSet.add(str);
            return str;
        }).forEachOrdered((str) -> {
            toSortSet.remove(str);
        });
        toSortSet.forEach((str) -> {
            sortedSet.add(str);
        });
        toSortSet.clear();
    }

    /**
     *Reset active data to default removing search and compare data
     */
    public void resetToDefault() {
        activeQuantDatasetsIndexes = null;
        activeData = quantDataMap;
        activeDiseaseCategoryMap = fullDiseaseCategoryMap;

    }

    private DiseaseCategoryObject cloneDiseaseCategoryObject(String diseaseCategory) {
        DiseaseCategoryObject diseaseCategoryObject = new DiseaseCategoryObject();
        diseaseCategoryObject.setDiseaseCategory(diseaseCategory);
        diseaseCategoryObject.setDiseaseStyleName(fullDiseaseCategoryMap.get(diseaseCategoryObject.getDiseaseCategory()).getDiseaseStyleName());
        diseaseCategoryObject.setDiseaseHashedColor(fullDiseaseCategoryMap.get(diseaseCategoryObject.getDiseaseCategory()).getDiseaseHashedColor());
        diseaseCategoryObject.setDiseaseAwtColor(fullDiseaseCategoryMap.get(diseaseCategoryObject.getDiseaseCategory()).getDiseaseAwtColor());
        diseaseCategoryObject.setDiseaseSubGroups(fullDiseaseCategoryMap.get(diseaseCategoryObject.getDiseaseCategory()).getDiseaseSubGroups());
        diseaseCategoryObject.setDiseaseSubGroupsToFullName(fullDiseaseCategoryMap.get(diseaseCategoryObject.getDiseaseCategory()).getDiseaseSubGroupsToFullName());
        return diseaseCategoryObject;
    }

}
