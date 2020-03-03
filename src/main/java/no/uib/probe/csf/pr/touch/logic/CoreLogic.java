package no.uib.probe.csf.pr.touch.logic;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import no.uib.probe.csf.pr.touch.database.DataBaseLayer;
import no.uib.probe.csf.pr.touch.database.Query;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseCategoryObject;
import no.uib.probe.csf.pr.touch.logic.beans.IdentificationProteinBean;
import no.uib.probe.csf.pr.touch.logic.beans.QuantComparisonProtein;
import no.uib.probe.csf.pr.touch.logic.beans.InitialInformationObject;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDataset;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import no.uib.probe.csf.pr.touch.logic.beans.QuantPeptide;
import no.uib.probe.csf.pr.touch.logic.beans.QuantProtein;
import no.uib.probe.csf.pr.touch.view.core.OverviewInfoBean;

/**
 * This class contains the logic layer (main computing code) this class interact
 * with both the data handler and the data access layer.
 *
 * @author Yehia Farag
 */
public class CoreLogic implements Serializable {

    /**
     * Database layer.
     */
    private final DataBaseLayer database;
    /**
     * Data exporter component.
     */
    private final Exporter exporter;

    /**
     * Constructor to initialize the main attributes.
     *
     * @param url database URL.
     * @param dbName database name.
     * @param driver database driver.
     * @param userName database username.
     * @param password database password.
     */
    public CoreLogic(String url, String dbName, String driver, String userName, String password) {
        database = new DataBaseLayer(url, dbName, driver, userName, password);
        this.exporter = new Exporter();

    }

    /**
     * This method responsible for getting the resource overview information
     *
     * @return OverviewInfoBean resource information bean
     */
    public OverviewInfoBean getResourceOverviewInformation() {
        return database.getResourceOverviewInformation();

    }

    /**
     * This method responsible for getting initial publication information
     *
     * @return list of publications available in the the resource
     */
    public List<Object[]> getPublicationList() {

        return database.getPublicationList();

    }

    /**
     * This method responsible for getting initial datasets information
     *
     * @return set of datasets information available in the the resource
     */
    public Set<QuantDataset> getQuantDatasetList() {
        return database.getQuantDatasetList();

    }

    /**
     * This method responsible for getting initial datasets information for
     * searching layout
     *
     * @param datasetIds set of dataset indexes.
     * @return set of datasets information available in the the resource
     */
    public Set<QuantDataset> getQuantDatasetList(Set<Integer> datasetIds) {
        Set<QuantDataset> quantDatasetSet = new LinkedHashSet<>();
        for (QuantDataset qDsObject : database.getQuantDatasetList()) {
            if (datasetIds.contains(qDsObject.getQuantDatasetIndex())) {
                quantDatasetSet.add(qDsObject);
            }

        }

        return quantDatasetSet;

    }

    /**
     * Get the current available disease category list
     *
     * @return set of disease category objects that has all disease category
     * information and styling information
     */
    public LinkedHashMap<String, DiseaseCategoryObject> getDiseaseCategorySet() {
        LinkedHashMap<String, DiseaseCategoryObject> availableDiseaseCategory = database.getDiseaseCategorySet();

        return availableDiseaseCategory;
    }

    /**
     * get available quantification datasets initial information object that
     * contains the available datasets list and the active columns (to hide them
     * if they are empty)
     *
     * @return InitialInformationObject
     */
    public Map<String, InitialInformationObject> getQuantDatasetInitialInformationObject() {
        Map<String, InitialInformationObject> quantStudyInitInfoMap = database.getQuantDatasetInitialInformationObject();

        boolean[] activeHeaders = new boolean[27];
        Set<String> diseaseCategories = new LinkedHashSet<>();
        InitialInformationObject allDatasetObject = new InitialInformationObject();
        Map<Integer, QuantDataset> updatedQuantDatasetObjectMap = new LinkedHashMap<>();

        quantStudyInitInfoMap.keySet().stream().map((disease_category) -> quantStudyInitInfoMap.get(disease_category)).map((datasetObject) -> {
            boolean[] dataactiveHeader = datasetObject.getActiveDatasetPieChartsFilters();
            int counter = 0;
            for (boolean active : dataactiveHeader) {
                if (!activeHeaders[counter] && active) {
                    activeHeaders[counter] = true;
                }
                counter++;
            }
            updatedQuantDatasetObjectMap.putAll(datasetObject.getQuantDatasetsMap());
            return datasetObject;
        }).forEach((datasetObject) -> {
            diseaseCategories.addAll(datasetObject.getDiseaseCategories());
        });
        allDatasetObject.setQuantDatasetsMap(updatedQuantDatasetObjectMap);
        allDatasetObject.setActiveDatasetPieChartsFilters(activeHeaders);
        allDatasetObject.setDiseaseCategories(diseaseCategories);
        quantStudyInitInfoMap.put("All Diseases", allDatasetObject);

        return quantStudyInitInfoMap;

    }

    /**
     * get active quantification pie charts filters (to hide them if they are
     * empty)
     *
     * @return boolean array for the active and not active pie chart filters
     * indexes
     */
    public Map<String, boolean[]> getActivePieChartQuantFilters() {
        return database.getActivePieChartQuantFilters();

    }

    /**
     * get active quantification pie charts filters within quant searching
     * proteins results (to hide them if they are empty)
     *
     * @param searchQuantificationProtList List of quant proteins.
     * @return boolean array for the active and not active pie chart filters
     * indexes
     */
    public Map<String, boolean[]> getActivePieChartQuantFilters(List<QuantProtein> searchQuantificationProtList) {
        return database.getActivePieChartQuantFilters(searchQuantificationProtList);

    }

    /**
     * Get set of disease groups names for special disease category
     *
     * @param diseaseCat disease category name
     * @return map of the short and long diseases names
     */
    public Set<String> getDiseaseGroupNameMap(String diseaseCat) {
        return database.getDiseaseGroupNameMap(diseaseCat);

    }

    /**
     * Get map for disease groups full name
     *
     * @return map of the short and long diseases groups names
     */
    public Map<String, String> getDiseaseGroupsFullNameMap() {
        return database.getDiseaseGroupsFullNameMap();
    }
    private final Set<String> accessionDefList = new HashSet<>();

    /**
     * This method is responsible for update quant comparison proteins name map
     * for each comparison.
     *
     *
     * @param selectedQuantComparisonsList selected comparisons
     * @param inUse_DiseaseCat_DiseaseGroupMap updated disease comparison names
     * map
     * @return updated quant comparisons list
     */
    public Set<QuantDiseaseGroupsComparison> updateComparisonQuantProteins(Set<QuantDiseaseGroupsComparison> selectedQuantComparisonsList, Map<String, Map<String, String>> inUse_DiseaseCat_DiseaseGroupMap) {
        Map<String, String> correctorMap = new HashMap<>();
        Set<QuantDiseaseGroupsComparison> updatedSelectedComparisonList = new LinkedHashSet<>();
        Set<QuantProtein> fullComparisonProtMap = new HashSet<>();
        Map<Integer, QuantDiseaseGroupsComparison> dsIndexToComparisonsMap = new HashMap<>();
        Set<Integer> dsIdsList = new HashSet<>();
        selectedQuantComparisonsList.stream().filter((comparison) -> (comparison.getQuantComparisonProteinMap() == null || comparison.getQuantComparisonProteinMap().isEmpty())).forEach((comparison) -> {
            comparison.getDatasetMap().keySet().stream().map((datasetIndex) -> {
                dsIdsList.add(datasetIndex);
                return datasetIndex;
            }).forEach((datasetIndex) -> {
                dsIndexToComparisonsMap.put(datasetIndex, comparison);
            });
        });
        if (dsIdsList.isEmpty()) {
            return selectedQuantComparisonsList;
        }
        fullComparisonProtMap.addAll(database.getQuantificationProteins(dsIdsList.toArray()));
        fullComparisonProtMap.forEach((quant) -> {
            if (!quant.getUniprotAccessionNumber().trim().equalsIgnoreCase("") && !correctorMap.containsKey(quant.getUniprotAccessionNumber())) {
                if (!quant.getUniprotProteinName().trim().equalsIgnoreCase("")) {
                    correctorMap.put(quant.getUniprotAccessionNumber(), quant.getUniprotProteinName());
                } else {
                    correctorMap.put(quant.getUniprotAccessionNumber(), quant.getPublicationProteinName());
                }
            }
        });

        Map<String, Set<QuantPeptide>> fullComparisonPeptideMap = (database.getQuantificationPeptides(dsIdsList.toArray()));

        Map<String, Set<QuantProtein>> fullComparisonToProtMap = new HashMap<>();
        fullComparisonProtMap.stream().forEach((qprot) -> {
            String datasetIndex = dsIndexToComparisonsMap.get(qprot.getQuantDatasetIndex()).getComparisonHeader();
            if (!fullComparisonToProtMap.containsKey(datasetIndex)) {
                Set<QuantProtein> comparisonProtMap = new HashSet<>();
                fullComparisonToProtMap.put(datasetIndex, comparisonProtMap);

            }
            Set<QuantProtein> comparisonProtMap = fullComparisonToProtMap.get(datasetIndex);
            comparisonProtMap.add(qprot);
            fullComparisonToProtMap.put(datasetIndex, comparisonProtMap);

        });
        for (QuantDiseaseGroupsComparison comparison : selectedQuantComparisonsList) {
            Set<QuantProtein> comparisonProtMap = fullComparisonToProtMap.get(comparison.getComparisonHeader());
            if (comparisonProtMap == null) {
                continue;
            }

            Map<String, QuantComparisonProtein> comparProtList = new HashMap<>();
            String compGrI = comparison.getComparisonHeader().split(" / ")[0].split("__")[0];
            Set<String> subGroupSet = new HashSet<>();
            inUse_DiseaseCat_DiseaseGroupMap.get(comparison.getDiseaseCategory()).keySet().stream().filter((Key) -> (compGrI.equalsIgnoreCase(inUse_DiseaseCat_DiseaseGroupMap.get(comparison.getDiseaseCategory()).get(Key)))).forEachOrdered((Key) -> {
                subGroupSet.add(Key);
            });

            for (QuantProtein quant : comparisonProtMap) {
                boolean inverted = false;
                String dsPGrI = comparison.getDatasetMap().get(quant.getQuantDatasetIndex()).getDiseaseSubGroup1();
                String pGrI;
                String pGrII;
                if (subGroupSet.contains(dsPGrI)) {
                    pGrI = comparison.getDatasetMap().get(quant.getQuantDatasetIndex()).getDiseaseSubGroup1();
                    pGrII = comparison.getDatasetMap().get(quant.getQuantDatasetIndex()).getDiseaseSubGroup2();
                } else {
                    pGrI = comparison.getDatasetMap().get(quant.getQuantDatasetIndex()).getDiseaseSubGroup2();
                    pGrII = comparison.getDatasetMap().get(quant.getQuantDatasetIndex()).getDiseaseSubGroup1();

                }

                String protAcc = quant.getUniprotAccessionNumber();
                String url;
                if (protAcc.equalsIgnoreCase("")) {
                    System.out.println("********************* null acc " + protAcc + "  " + quant.getPublicationAccessionNumber() + "  " + comparProtList.containsKey(quant.getPublicationAccessionNumber()));
                }

                if (protAcc.trim().equalsIgnoreCase("") || protAcc.equalsIgnoreCase("Not Available") || protAcc.equalsIgnoreCase("Entry Deleted") || protAcc.equalsIgnoreCase("Entry Demerged") || protAcc.equalsIgnoreCase("NOT RETRIEVED") || protAcc.equalsIgnoreCase("DELETED") || protAcc.trim().equalsIgnoreCase("UNREVIEWED")) {
                    protAcc = protAcc.replace("Entry Deleted", "Deleted").replace("Entry deleted", "Deleted").replace("Deleted", "Entry Deleted");
                    accessionDefList.add(protAcc);
                    protAcc = quant.getPublicationAccessionNumber() + " (" + protAcc + ")";
                    if (protAcc.contains("()")) {
                        protAcc = protAcc.replace("()", "").trim();
                    }
                    url = null;

                } else {
                    url = "http://www.uniprot.org/uniprot/" + protAcc;
                }

                if (!comparProtList.containsKey(protAcc)) {
                    QuantComparisonProtein comProt = new QuantComparisonProtein();
                    comProt.setQuantPeptidesList(new HashSet<>());
                    comProt.setSequence(quant.getSequence());
                    comparProtList.put(protAcc, comProt);
                }

                QuantComparisonProtein comProt = comparProtList.get(protAcc);

                boolean significantPValue = true;
                if (quant.getString_p_value().equalsIgnoreCase("Not Significant") || quant.getString_p_value().equalsIgnoreCase("Not Available")) {
                    significantPValue = false;

                }

                if ((pGrI.equalsIgnoreCase(quant.getDiseaseMainGroupI()) || pGrI.equalsIgnoreCase(quant.getOriginalDiseaseSubGroupI())) && (pGrII.equalsIgnoreCase(quant.getDiseaseMainGroupII()) || pGrII.equalsIgnoreCase(quant.getOriginalDiseaseSubGroupII()))) {

                    if (quant.getString_fc_value().equalsIgnoreCase("Decreased") || quant.getString_fc_value().equalsIgnoreCase("Decrease")) {
                        comProt.addDecreasedProtein((quant.getDiseaseGroupIPatientsNumber() + quant.getDiseaseGroupIIPatientsNumber()), quant.getQuantDatasetIndex(), significantPValue);
                    } else if (quant.getString_fc_value().equalsIgnoreCase("Increased") || quant.getString_fc_value().equalsIgnoreCase("Increase")) {
                        comProt.addIncreasedProtein((quant.getDiseaseGroupIPatientsNumber() + quant.getDiseaseGroupIIPatientsNumber()), quant.getQuantDatasetIndex(), significantPValue);
                    } else if (quant.getString_fc_value().equalsIgnoreCase("Not Provided")) {
                        System.out.println("no.uib.probe.csf.pr.touch.logic.CoreLogic.updateComparisonQuantProteins()" + quant.getString_fc_value());
                        comProt.addNoValueProvided((quant.getDiseaseGroupIPatientsNumber() + quant.getDiseaseGroupIIPatientsNumber()), quant.getQuantDatasetIndex());
                    } else if (quant.getString_fc_value().equalsIgnoreCase("No change")) {
                        comProt.addEqualProtein((quant.getDiseaseGroupIPatientsNumber() + quant.getDiseaseGroupIIPatientsNumber()), quant.getQuantDatasetIndex());
                    }

                } else {
                    inverted = true;

                    if (quant.getString_fc_value().equalsIgnoreCase("Decreased") || quant.getString_fc_value().equalsIgnoreCase("Decrease")) {
                        comProt.addIncreasedProtein((quant.getDiseaseGroupIPatientsNumber() + quant.getDiseaseGroupIIPatientsNumber()), quant.getQuantDatasetIndex(), significantPValue);
                    } else if (quant.getString_fc_value().equalsIgnoreCase("Increased") || quant.getString_fc_value().equalsIgnoreCase("Increase")) {
                        comProt.addDecreasedProtein((quant.getDiseaseGroupIPatientsNumber() + quant.getDiseaseGroupIIPatientsNumber()), quant.getQuantDatasetIndex(), significantPValue);
                    } else if (quant.getString_fc_value().equalsIgnoreCase("Not Provided")) {
                        System.out.println("no.uib.probe.csf.pr.touch.logic.CoreLogic.updateComparisonQuantProteins()" + quant.getUniprotAccessionNumber());
                        comProt.addNoValueProvided((quant.getDiseaseGroupIPatientsNumber() + quant.getDiseaseGroupIIPatientsNumber()), quant.getQuantDatasetIndex());
                    } else if (quant.getString_fc_value().equalsIgnoreCase("No change")) {
                        comProt.addEqualProtein((quant.getDiseaseGroupIPatientsNumber() + quant.getDiseaseGroupIIPatientsNumber()), quant.getQuantDatasetIndex());
                    }
                }

                String uniprotAcc = quant.getUniprotAccessionNumber();
                String protName;
                String accession;
                String urlLink;

                if (uniprotAcc.trim().equalsIgnoreCase("") || uniprotAcc.equalsIgnoreCase("Not Available") || uniprotAcc.equalsIgnoreCase("Entry Deleted") || uniprotAcc.equalsIgnoreCase("Entry Demerged") || uniprotAcc.equalsIgnoreCase("NOT RETRIEVED") || uniprotAcc.equalsIgnoreCase("DELETED") || uniprotAcc.trim().equalsIgnoreCase("UNREVIEWED")) {
                    protName = quant.getPublicationProteinName();
                    accession = protAcc;
                    urlLink = null;

                } else {
                    protName = quant.getUniprotProteinName();
                    accession = quant.getUniprotAccessionNumber().trim();
                    urlLink = "http://www.uniprot.org/uniprot/" + protAcc.toUpperCase();
                }
                if (protName.trim().equalsIgnoreCase("")) {
                    protName = quant.getPublicationProteinName();
                }

                String keyWord = accession.split("\\(")[0].trim();
                if (correctorMap.containsKey(keyWord)) {
                    accession = keyWord;
                    protName = correctorMap.get(keyWord);
                    urlLink = "http://www.uniprot.org/uniprot/" + keyWord.toUpperCase();
                    url = urlLink;

                }

                quant.setURL(urlLink);
                comProt.setProteinName(protName);
                comProt.setProteinAccession(accession);
                comProt.setUrl(url);
                comProt.setSequence(quant.getSequence());

                Set<QuantPeptide> quantPeptidesList = comProt.getQuantPeptidesList();
                for (String key : fullComparisonPeptideMap.keySet()) {

                    if (key.equalsIgnoreCase("__" + (quant.getProtIndex()) + "__" + quant.getQuantDatasetIndex() + "__")) {
                        if (inverted) {
                            Set<QuantPeptide> updatedQuantPeptidesList = new HashSet<>();
                            fullComparisonPeptideMap.get(key).stream().map((quantPeptide) -> {
                                if (quantPeptide.getString_fc_value().equalsIgnoreCase("Increased") || quantPeptide.getString_fc_value().equalsIgnoreCase("Increase")) {

                                    quantPeptide.setString_fc_value("Decreased");

                                } else if (quantPeptide.getString_fc_value().equalsIgnoreCase("Decreased") || quantPeptide.getString_fc_value().equalsIgnoreCase("Decrease")) {
                                    quantPeptide.setString_fc_value("Increased");

                                }
                                return quantPeptide;
                            }).map((quantPeptide) -> {
                                if (quantPeptide.getFc_value() != -1000000000.0) {
                                    quantPeptide.setFc_value(quantPeptide.getFc_value() * -1.0);//((1.0 / quantPeptide.getFc_value()) * -1);
                                }
                                return quantPeptide;
                            }).forEach((quantPeptide) -> {
                                quantPeptide.setQuantificationBasis(quant.getQuantificationBasis());
                                updatedQuantPeptidesList.add(quantPeptide);
                            });
                            quantPeptidesList.addAll(updatedQuantPeptidesList);

                        } else {
                            fullComparisonPeptideMap.get(key).stream().map((quantPeptide) -> {
                                quantPeptide.setQuantificationBasis(quant.getQuantificationBasis());
                                return quantPeptide;
                            }).forEach((quantPeptide) -> {
                                quantPeptidesList.add(quantPeptide);
                            });

                        }

                    }
                }
                Map<String, QuantProtein> dsQuantProteinsMap = comProt.getDsQuantProteinsMap();
                if (!dsQuantProteinsMap.containsKey("_-_" + quant.getQuantDatasetIndex() + "_-_" + comProt.getProteinAccession() + "_-_")) {
                    if (inverted) {
                        if (quant.getString_fc_value().equalsIgnoreCase("Increased") || quant.getString_fc_value().equalsIgnoreCase("Increase")) {

                            quant.setString_fc_value("Decreased");

                        } else if (quant.getString_fc_value().equalsIgnoreCase("Decreased") || quant.getString_fc_value().equalsIgnoreCase("Decrease")) {
                            quant.setString_fc_value("Increased");

                        }
                        if (quant.getFc_value() != -1000000000.0) {
                            quant.setFc_value(quant.getFc_value() * -1.0);
                        }
                        String pgI = quant.getDiseaseMainGroupII();
                        String pSubGI = quant.getOriginalDiseaseSubGroupII();
                        String pGrIComm = quant.getDiseaseMainGroupIIComment();
                        int pGrINum = quant.getDiseaseGroupIIPatientsNumber();

                        quant.setDiseaseMainGroupII(quant.getDiseaseMainGroupI());
                        quant.setDiseaseMainGroupIIComment(quant.getDiseaseMainGroupIComment());
                        quant.setOriginalDiseaseSubGroupII(quant.getOriginalDiseaseSubGroupI());
                        quant.setDiseaseGroupIIPatientsNumber(quant.getDiseaseGroupIPatientsNumber());

                        quant.setDiseaseMainGroupI(pgI);
                        quant.setDiseaseMainGroupIComment(pSubGI);
                        quant.setOriginalDiseaseSubGroupI(pGrIComm);
                        quant.setDiseaseGroupIPatientsNumber(pGrINum);

                    }
                    dsQuantProteinsMap.put("_-_" + quant.getQuantDatasetIndex() + "_-_" + comProt.getProteinAccession() + "_-_", quant);

                } else {

                    /////for iso testing remove as soon as possible 
                    System.out.println("at major error in data dublicated keys " + ("_-_" + quant.getQuantDatasetIndex() + "_-_" + comProt.getProteinAccession() + "_-_"));
                    continue;
                }

                comProt.setDsQuantProteinsMap(dsQuantProteinsMap);
                comProt.setQuantPeptidesList(quantPeptidesList);
                comparProtList.put(protAcc, comProt);

            }

//            //init pep for quantProt
//            //sort the protiens map
            Map<String, QuantComparisonProtein> sortedcomparProtList = new TreeMap<>(Collections.reverseOrder());
            Map<Integer, Set<QuantComparisonProtein>> proteinsByTrendMap = new HashMap<>();
            proteinsByTrendMap.put(0, new HashSet<>());
            proteinsByTrendMap.put(1, new HashSet<>());
            proteinsByTrendMap.put(2, new HashSet<>());
            proteinsByTrendMap.put(2, new HashSet<>());
            proteinsByTrendMap.put(3, new HashSet<>());
            proteinsByTrendMap.put(4, new HashSet<>());
            proteinsByTrendMap.put(5, new HashSet<>());
            comparProtList.keySet().stream().forEach((Key) -> {
                QuantComparisonProtein temp = comparProtList.get(Key);
                sortedcomparProtList.put((temp.getSignificantTrindCategory() + "_" + Key), temp);
                temp.finalizeQuantData();
                Set<QuantComparisonProtein> set = proteinsByTrendMap.get(temp.getSignificantTrindCategory());

                set.add(temp);
                proteinsByTrendMap.put(temp.getSignificantTrindCategory(), set);
            });
            comparison.setQuantComparisonProteinMap(sortedcomparProtList);
            comparison.setProteinsByTrendMap(proteinsByTrendMap);
            updatedSelectedComparisonList.add(comparison);

        }
        return updatedSelectedComparisonList;

    }

    /**
     * search for proteins by description keywords
     *
     * @param query query words
     * @param toCompare Quant comparing mode.
     * @return datasetProteinsSearchList
     */
    public List<QuantProtein> searchQuantificationProteins(Query query, boolean toCompare) {

        if (query.getSearchDataType().equals("Quantification Data")) {
            List<QuantProtein> datasetQuantificationProteinsSearchList = database.searchQuantificationProteins(query, toCompare);
            return datasetQuantificationProteinsSearchList;
        }
        return null;

    }

    /**
     * Store query to share with csf-pr ID.
     *
     * @param query query object that has all query information.
     * @return db successful transaction
     */
    public int storeQuery(Query query) {
        return database.storeQueryinDB(query);

    }

    /**
     * This function to filter the quant search results based on keywords and
     * detect the not found keywords
     *
     * @param quantProteinstList list of found proteins
     * @param SearchingKeys keyword used for searching
     * @param searchBy searching method (accession,proteins name, or peptide
     * sequence )
     * @return not found keywords within the searching list
     */
    public String filterQuantSearchingKeywords(List<QuantProtein> quantProteinstList, String SearchingKeys, String searchBy) {
        if (quantProteinstList == null || quantProteinstList.isEmpty()) {
            return SearchingKeys;
        }
        HashSet<String> usedKeys = new HashSet<>();
        HashSet<String> tUsedKeys = new HashSet<>();
        for (String key : SearchingKeys.split("\n")) {
            if (key.trim().length() > 3) {
                usedKeys.add(key.toUpperCase());
            }
        }
        tUsedKeys.addAll(usedKeys);
        for (QuantProtein pb : quantProteinstList) {
            switch (searchBy) {
                case "Protein Accession":
                    if (usedKeys.contains(pb.getUniprotAccessionNumber().toUpperCase())) {
                        usedKeys.remove(pb.getUniprotAccessionNumber().toUpperCase());
                    } else if (usedKeys.contains(pb.getPublicationAccessionNumber().toUpperCase())) {
                        usedKeys.remove(pb.getPublicationAccessionNumber().toUpperCase());
                    }
                    if (usedKeys.isEmpty()) {
                        return "";
                    }
                    break;
                case "Protein Name":
                    for (String key : tUsedKeys) {
                        if (pb.getUniprotProteinName().toUpperCase().contains(key.toUpperCase())) {
                            usedKeys.remove(key.toUpperCase());
                        } else if (pb.getPublicationProteinName().toUpperCase().contains(key.toUpperCase())) {
                            usedKeys.remove(key.toUpperCase());
                        }
                        if (usedKeys.isEmpty()) {
                            return "";
                        }
                    }
                    break;
                default:
                    return "";
            }
        }
        return usedKeys.toString().replace("[", "").replace("]", "");

    }

    /**
     * This function to get the quant hits list from the searching results and
     * group the common proteins in separated lists
     *
     * @param quantProteinsList list of found proteins
     * @param searchBy searching method (accession,proteins name, or peptide
     * sequence )
     * @return list of quant hits results
     */
    public Map<String, Integer[]> getQuantHitsList(List<QuantProtein> quantProteinsList, String searchBy) {
        Map<String, Integer[]> quantHitsList = new TreeMap<>();

        if (quantProteinsList == null || quantProteinsList.isEmpty()) {

            return quantHitsList;
        }
        String key;

        for (QuantProtein quantProt : quantProteinsList) {

            String uniprotAcc = quantProt.getUniprotAccessionNumber();
            String protName;
            String accession;
            String url;
            if (uniprotAcc.trim().equalsIgnoreCase("") || uniprotAcc.equalsIgnoreCase("Not Available") || uniprotAcc.equalsIgnoreCase("Entry Deleted") || uniprotAcc.equalsIgnoreCase("Entry Demerged") || uniprotAcc.equalsIgnoreCase("NOT RETRIEVED") || uniprotAcc.equalsIgnoreCase("DELETED") || uniprotAcc.trim().equalsIgnoreCase("UNREVIEWED")) {
                protName = quantProt.getPublicationProteinName();
                accession = quantProt.getPublicationAccessionNumber() + " (" + uniprotAcc + ")";
                url = null;

            } else {
                protName = quantProt.getUniprotProteinName();
                accession = quantProt.getUniprotAccessionNumber();
                url = "http://www.uniprot.org/uniprot/" + accession;
            }
            if (protName.trim().equalsIgnoreCase("")) {
                protName = quantProt.getPublicationProteinName();
            }
            quantProt.setFinalAccession(accession);
            quantProt.setURL(url);
            key = accession.trim() + "__" + protName.trim();

            if (!quantHitsList.containsKey(key)) {
                quantHitsList.put(key, new Integer[]{0, 0, 0, 0, 0});
            }
            Integer[] valueArr = quantHitsList.get(key);
            if (quantProt.getDiseaseCategoryI().equalsIgnoreCase("Alzheimer's")) {
                valueArr[0] = valueArr[0] + 1;
            } else if (quantProt.getDiseaseCategoryI().equalsIgnoreCase("Multiple Sclerosis")) {
                valueArr[1] = valueArr[1] + 1;
            } else if (quantProt.getDiseaseCategoryI().equalsIgnoreCase("Parkinson's")) {
                valueArr[2] = valueArr[2] + 1;
            } else if (quantProt.getDiseaseCategoryI().equalsIgnoreCase("Amyotrophic Lateral Sclerosis")) {
                valueArr[3] = valueArr[3] + 1;
            }

            valueArr[4] = valueArr[4] + 1;
            quantHitsList.put(key, valueArr);

        }

        return quantHitsList;

    }

    /**
     * This function to get the quant comparison hits list from the searching
     * results and group the common proteins in separated lists
     *
     * @param quantProteinsList list of found proteins
     * @param searchBy searching method (accession,proteins name, or peptide
     * sequence )
     * @return list of quant hits results
     */
    public Integer[] getQuantComparisonHitsList(List<QuantProtein> quantProteinsList, String searchBy) {
        Integer[] quantHitsList = new Integer[]{0, 0, 0, 0, 0};

        if (quantProteinsList == null || quantProteinsList.isEmpty()) {

            return quantHitsList;
        }
        quantProteinsList.stream().map((quantProt) -> {
            if (quantProt.isCrossDiseases()) {
                if (quantProt.getDiseaseCategoryII().equalsIgnoreCase("Alzheimer's")) {
                    quantHitsList[0] = quantHitsList[0] + 1;
                } else if (quantProt.getDiseaseCategoryII().equalsIgnoreCase("Multiple Sclerosis")) {
                    quantHitsList[1] = quantHitsList[1] + 1;
                } else if (quantProt.getDiseaseCategoryII().equalsIgnoreCase("Parkinson's")) {
                    quantHitsList[2] = quantHitsList[2] + 1;
                } else if (quantProt.getDiseaseCategoryII().equalsIgnoreCase("Amyotrophic Lateral Sclerosis")) {
                    quantHitsList[3] = quantHitsList[3] + 1;
                }
            }

            if (quantProt.getDiseaseCategoryI().equalsIgnoreCase("Alzheimer's")) {
                quantHitsList[0] = quantHitsList[0] + 1;
            } else if (quantProt.getDiseaseCategoryI().equalsIgnoreCase("Multiple Sclerosis")) {
                quantHitsList[1] = quantHitsList[1] + 1;
            } else if (quantProt.getDiseaseCategoryI().equalsIgnoreCase("Parkinson's")) {
                quantHitsList[2] = quantHitsList[2] + 1;
            } else if (quantProt.getDiseaseCategoryI().equalsIgnoreCase("Amyotrophic Lateral Sclerosis")) {
                quantHitsList[3] = quantHitsList[3] + 1;
            }

            return quantProt;
        }).forEach((_item) -> {

            quantHitsList[4] = quantHitsList[4] + 1;
        });

        return quantHitsList;

    }

    /**
     * This function to get the quant comparison hits list from the searching
     * results and group the common proteins in separated lists
     *
     * @param quantProteinsList list of found proteins
     * @param searchBy searching method (accession,proteins name, or peptide
     * sequence )
     * @return list of quant hits results
     */
    public Integer[] getQuantComparisonProteinList(List<QuantProtein> quantProteinsList, String searchBy) {

        Set<String> msD = new HashSet<>();
        Set<String> alD = new HashSet<>();
        Set<String> parD = new HashSet<>();
        Set<String> alsD = new HashSet<>();

        if (quantProteinsList == null || quantProteinsList.isEmpty()) {

            return new Integer[]{};
        }
        quantProteinsList.stream().map((quantProt) -> {
            if (quantProt.getDiseaseCategoryI().equalsIgnoreCase("Alzheimer's")) {
                alD.add(quantProt.getUniprotAccessionNumber());
            } else if (quantProt.getDiseaseCategoryI().equalsIgnoreCase("Multiple Sclerosis")) {
                msD.add(quantProt.getUniprotAccessionNumber());
            } else if (quantProt.getDiseaseCategoryI().equalsIgnoreCase("Parkinson's")) {
                parD.add(quantProt.getUniprotAccessionNumber());
            } else if (quantProt.getDiseaseCategoryI().equalsIgnoreCase("Amyotrophic Lateral Sclerosis")) {
                alsD.add(quantProt.getUniprotAccessionNumber());
            }
            return quantProt;
        }).forEach((_item) -> {
        });
        Integer[] quantHitsList = new Integer[]{alD.size(), msD.size(), parD.size(), alsD.size(), alD.size() + msD.size() + parD.size() + alsD.size()};

        return quantHitsList;

    }

    /**
     * search for proteins by description keywords
     *
     * @param query query words
     * @return datasetProteinsSearchList
     */
    public String searchIdentficationProtein(Query query) {

        Map<Integer, IdentificationProteinBean> datasetProteinsSearchList = new HashMap<>();
        if (query.getSearchDataType().equals("Identification Data")) {
            if (query.getSearchDataset() == null || query.getSearchDataset().isEmpty())//search in all identification datasets
            {
                if (query.getSearchBy().equalsIgnoreCase("Protein Accession"))//"Protein Name" "Peptide Sequence"
                {
                    String[] queryWordsArr = query.getSearchKeyWords().split("\n");
                    Set<String> searchSet = new HashSet<>();
                    for (String str : queryWordsArr) {
                        if (str.trim().length() > 3) {
                            searchSet.add(str.trim());
                        }
                    }
                    datasetProteinsSearchList.putAll(database.searchIdentificationProteinAllDatasetsByAccession(searchSet, query.isValidatedProteins()));
                } else if (query.getSearchBy().equalsIgnoreCase("Protein Name")) {
                    datasetProteinsSearchList.putAll(database.searchIdentificationProteinAllDatasetsByName(query.getSearchKeyWords(), query.isValidatedProteins()));

                } else if (query.getSearchBy().equalsIgnoreCase("Peptide Sequence")) {
                    datasetProteinsSearchList.putAll(database.SearchIdentificationProteinAllDatasetsByPeptideSequence(query.getSearchKeyWords(), query.isValidatedProteins()));
                }

            }

        }
        Map<String, Integer> idHitsList = getIdentificationHitsList(datasetProteinsSearchList, query.getSearchBy(), query.getSearchKeyWords());

        Set<Integer> datasetIds = new HashSet<>();
        datasetProteinsSearchList.values().stream().forEach((idProt) -> {
            datasetIds.add(idProt.getDatasetId());
        });
        if (datasetProteinsSearchList.isEmpty()) {
            return null;
        }
        return "Proteins identification data is available ( #Protein Groups " + idHitsList.size() + "  |  #Datasets " + datasetIds.size() + "  |  #Hits " + datasetProteinsSearchList.size() + " )";

    }

    /**
     * This function to get the identification hits list from the searching
     * results and group the common proteins in separated lists
     *
     * @param identificationProteinsList list of found proteins
     * @param searchBy searching method (accession,proteins name, or peptide
     * sequence )
     * @param mainProt accession of leading protein.
     * @return list of identification hits results
     */
    public Map<String, Integer> getIdentificationHitsList(Map<Integer, IdentificationProteinBean> identificationProteinsList, String searchBy, String mainProt) {
        final TreeSet<String> usedKeys = new TreeSet<>();
        for (String key : mainProt.split("\n")) {
            if (key.trim().length() > 3) {
                usedKeys.add(key);
            }
        }

        Map<String, Integer> idHitsList = new TreeMap<>();
        if (identificationProteinsList == null || identificationProteinsList.isEmpty()) {
            return idHitsList;
        }
        String key;

        for (IdentificationProteinBean prot : identificationProteinsList.values()) {

            if (searchBy.equalsIgnoreCase("Protein Accession")) {
                key = prot.getAccession().trim() + "__" + prot.getOtherProteins().trim() + "__" + prot.getDescription().trim();

            } else {
                key = prot.getDescription().trim();
            }

            if (!idHitsList.containsKey(key)) {
                idHitsList.put(key, 0);
            }
            int value = idHitsList.get(key);
            value++;
            idHitsList.put(key, value);

        }
        if (!idHitsList.keySet().toArray()[0].toString().startsWith(usedKeys.pollFirst())) {
            Map<String, Integer> revIdHitsList = new TreeMap<>(Collections.reverseOrder());
            revIdHitsList.putAll(idHitsList);
            return revIdHitsList;
        }
        return idHitsList;

    }

    /**
     * Proteins exporting with large datasets as csv format.
     *
     * @param proteinsList protein accession list to be exported
     * @return file byte array
     */
    public byte[] exportProteinsListToCSV(Set<String> proteinsList) {
        return exporter.expotProteinAccessionListToCSV(proteinsList);

    }

    /**
     * Filter peptides and get un-mapped peptides for altered proteins sequence.
     *
     * @return set of un-mapped peptides.
     */
    public Set<QuantPeptide> getUnmappedPeptideSet() {

        Set<QuantPeptide> unmappedPeptideSet = new LinkedHashSet<>();
        Set<QuantDataset> quantDatasetSet = new TreeSet<>(getQuantDatasetList());
        Object[] dsIds = new Object[quantDatasetSet.size()];
        for (int i = 0; i < dsIds.length; i++) {
            dsIds[i] = ((QuantDataset) quantDatasetSet.toArray()[i]).getQuantDatasetIndex();
        }
        Set<QuantProtein> quantProteinSet = database.getQuantificationProteins(dsIds);
        Map<String, Set<QuantPeptide>> quantPeptSet = database.getQuantificationPeptides(dsIds);

        for (QuantProtein qProtein : quantProteinSet) {
            String sigKey = "__" + qProtein.getProtIndex() + "__" + qProtein.getQuantDatasetIndex() + "__";
            if (quantPeptSet.containsKey(sigKey)) {
                Set<QuantPeptide> peptidesSet = quantPeptSet.get(sigKey);
                if (qProtein.getSequence() != null && !qProtein.getSequence().trim().equalsIgnoreCase("")) {
                    for (QuantPeptide peptide : peptidesSet) {
                        if (!qProtein.getSequence().contains(peptide.getPeptideSequence())) {
                            peptide.setUniprotAccessionNumber(qProtein.getUniprotAccessionNumber());
                            peptide.setUniprotProteinName(qProtein.getUniprotProteinName());
                            peptide.setPublicationAccessionNumber(qProtein.getPublicationAccessionNumber());
                            peptide.setPublicationProteinName(qProtein.getPublicationProteinName());
                            unmappedPeptideSet.add(peptide);
                        }
                    }

                }
            }
        }

        return unmappedPeptideSet;

    }

}
