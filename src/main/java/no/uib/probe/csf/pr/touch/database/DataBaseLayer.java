package no.uib.probe.csf.pr.touch.database;

import com.vaadin.server.VaadinSession;
import java.awt.Color;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseCategoryObject;
import no.uib.probe.csf.pr.touch.logic.beans.IdentificationProteinBean;
import no.uib.probe.csf.pr.touch.logic.beans.InitialInformationObject;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDataset;
import no.uib.probe.csf.pr.touch.logic.beans.QuantPeptide;
import no.uib.probe.csf.pr.touch.logic.beans.QuantProtein;
import no.uib.probe.csf.pr.touch.view.core.OverviewInfoBean;

/**
 * This class is an abstract for the database layer this class interact with
 * logic layer this class contains all the database SQL queries.
 *
 * @author Yehia Farag
 */
public class DataBaseLayer implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * Database connection.
     */
    private Connection conn;
    /**
     * Database connection input parameters.
     */
    private final String url, dbName, driver, userName, password;

    /**
     * Map of the disease categories style - disease category name to disease
     * category style.
     */
    private final Map<String, String> diseaseCategoryStyles = new HashMap<>();

    /**
     * Map of the disease categories color codes - disease category name to
     * disease category color code(HTML Color code).
     */
    private final Map<String, String> diseaseColorMap = new HashMap<>();

    /**
     * Constructor to initialize the database abstraction layer
     *
     * @param url database URL.
     * @param dbName database name.
     * @param driver database driver.
     * @param userName database username.
     * @param password database password.
     *
     */
    public DataBaseLayer(String url, String dbName, String driver, String userName, String password) {
        this.url = url;
        this.dbName = dbName;
        this.driver = driver;
        this.userName = userName;
        this.password = password;
        diseaseCategoryStyles.put("Multiple Sclerosis", "multiplesclerosisstyle");
        diseaseCategoryStyles.put("Alzheimer's", "alzheimerstyle");
        diseaseCategoryStyles.put("Parkinson's", "parkinsonstyle");
        diseaseCategoryStyles.put("Amyotrophic Lateral Sclerosis", "amyotrophicstyle");

        diseaseColorMap.put("Multiple Sclerosis", "#A52A2A");
        diseaseColorMap.put("Alzheimer's", "#4b7865");
        diseaseColorMap.put("Parkinson's", "#74716E");
        diseaseColorMap.put("Amyotrophic Lateral Sclerosis", "#1b699f");
        createQueryTempStoreTable();
        VaadinSession.getCurrent().getSession().setAttribute("CsrfToken", VaadinSession.getCurrent().getCsrfToken());

    }

    /**
     * Get the resource overview information.
     *
     * @return OverviewInfoBean resource information bean.
     */
    public OverviewInfoBean getResourceOverviewInformation() {
        OverviewInfoBean infoBean = new OverviewInfoBean();
        try {
            int numProteins;
            int numPeptides;

            //number here are static until update the database
            infoBean.setNumberOfIdProteins(3081);
            infoBean.setNumberOfIdPeptides(28811);
            infoBean.setNumberOfIdStudies(7);
            //quant data

            String selectQuantPublicationStudies = "SELECT COUNT( * ) AS  `Rows` ,  `pubmed_id` FROM  `quant_dataset_table` GROUP BY  `pubmed_id` ORDER BY  `pubmed_id` ";
            if (conn == null) {
                return null;
            }
            PreparedStatement selectQuantPublicationStudiesStat = conn.prepareStatement(selectQuantPublicationStudies);

            ResultSet rs = selectQuantPublicationStudiesStat.executeQuery();
            int numStudies = 0;
            int numPublications = 0;

            while (rs.next()) {

                numStudies += rs.getInt("Rows");
                numPublications++;

            }
            infoBean.setNumberOfQuantPublication(numPublications);
            infoBean.setNumberOfQuantDatasets(numStudies);

            rs.close();

            String selectQuantProteinsNumber = "SELECT COUNT( DISTINCT `uniprot_accession` ) AS  `Rows` FROM  `quant_proteins_table` ;";
            PreparedStatement selectQuantProteinsNumberStat = conn.prepareStatement(selectQuantProteinsNumber);

            rs = selectQuantProteinsNumberStat.executeQuery();
            numProteins = 0;

            while (rs.next()) {
                numProteins += rs.getInt("Rows");
            }            
            rs.close();
            selectQuantProteinsNumber = "SELECT COUNT( DISTINCT `publication_accession` ) AS  `Rows` FROM  `quant_proteins_table` WHERE `uniprot_accession`= '';";
            selectQuantProteinsNumberStat = conn.prepareStatement(selectQuantProteinsNumber);
            rs = selectQuantProteinsNumberStat.executeQuery();   
            while (rs.next()) {
                numProteins += rs.getInt("Rows");
            }
          
//            numProteins = 4192;
            infoBean.setNumberOfQuantProteins(numProteins);
            rs.close();
            
            

            String selectQuantPeptidesNumber = "SELECT COUNT( DISTINCT  `peptide_sequance` ) AS  `Rows` \n"
                    + "FROM  `quant_peptides_table`";
            PreparedStatement selectQuantPeptidesNumberStat = conn.prepareStatement(selectQuantPeptidesNumber);

            rs = selectQuantPeptidesNumberStat.executeQuery();
            numPeptides = 0;

            while (rs.next()) {
                numPeptides += rs.getInt("Rows");
            }
            infoBean.setNumberOfQuantPeptides(numPeptides);
            rs.close();

        } catch (SQLException e) {
            System.err.println("at error " + this.getClass().getName() + "  line 152  " + e.getLocalizedMessage());
            return null;

        }

        return infoBean;

    }

    /**
     * Create query store table to share query between csf-pr id and csf-pr
     * quant
     *
     */
    private void createQueryTempStoreTable() {
        String statment = "CREATE TABLE IF NOT EXISTS `temp_query_table` (\n"
                + "  `index` int(255) NOT NULL AUTO_INCREMENT,\n"
                + "  `csrf_token` varchar(100) NOT NULL,\n"
                + "  `search_by` varchar(50) NOT NULL,\n"
                + "  `search_key` text NOT NULL, PRIMARY KEY (`index`),\n"
                + "  KEY `index` (`index`)\n"
                + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";

        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            Statement st = conn.createStatement();
            st.executeUpdate(statment);
        } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException ex) {
            ex.printStackTrace();
            conn = null;
        }

    }

    /**
     * Get initial publication information
     *
     * @return list of publications available in the the resource
     */
    public Map<String, Object[]> getPublicationList() {

        Map<String, Object[]> publicationList = new LinkedHashMap<>();
        String selectStat = "SELECT DISTINCT `pubmed_id`,`year`,`author` FROM `quant_publication_table`  ORDER BY `year`  DESC ,`author`";//"SELECT * FROM  `publication_table` WHERE `active`='true' ORDER BY  `publication_table`.`year` DESC ,`publication_table`.`author` ";
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }

            PreparedStatement st = conn.prepareStatement(selectStat);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                if (rs.getString("pubmed_id").contains("MCP")) {
                    continue;
                }
                String author = rs.getString("author");
                author = author.replace("¢", "ó").replace("Borr…s", "Borràs");
                publicationList.put(rs.getString("pubmed_id"), new Object[]{author, Integer.parseInt(rs.getString("year"))});
            }
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            System.err.println("at error " + this.getClass().getName() + "  line 184  " + e.getLocalizedMessage());
        }
        return publicationList;

    }

    /**
     * Store query in database in order to share it with csf-pr id data
     *
     * @param query Query object to be stored
     * @return successful database transaction
     */
    public int storeQueryinDB(Query query) {

        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
        } catch (InstantiationException | IllegalAccessException | SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DataBaseLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        String statment = "INSERT INTO `temp_query_table` ( `csrf_token`, `search_by`, `search_key`) VALUES (?, ?, ?);";

        try (PreparedStatement preparedStatment = conn.prepareStatement(statment, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatment.setString(1, VaadinSession.getCurrent().getCsrfToken());
            preparedStatment.setString(2, query.getSearchBy().replace(" ", "*"));
            preparedStatment.setString(3, query.getSearchKeyWords());
            preparedStatment.executeUpdate();
            ResultSet rs = preparedStatment.getGeneratedKeys();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -100;
    }

    /**
     * Get initial datasets information
     *
     * @return set of datasets information available in the the resource
     */
    public Set<QuantDataset> getQuantDatasetList() {
        Set<QuantDataset> dsObjects = new TreeSet<>();
        Map<String, InitialInformationObject> diseaseCategoriesMap = getQuantDatasetInitialInformationObject();
        if (diseaseCategoriesMap == null) {
            return null;
        }
        diseaseCategoriesMap.values().stream().forEach((qi) -> {
            dsObjects.addAll(qi.getQuantDatasetsMap().values());
        });

        return dsObjects;

    }

    /**
     * Get available quantification datasets initial information object that
     * contains the available datasets list and the active columns (to hide them
     * if they are empty).
     *
     * @return InitialInformationObject.
     */
    public Map<String, InitialInformationObject> getQuantDatasetInitialInformationObject() {
        Map<String, InitialInformationObject> diseaseCategoriesMap = new LinkedHashMap<>();
        try {
            Map<String, Object[]> publicationList = this.getPublicationList();
            PreparedStatement selectStudiesStat;
            String selectStudies = "SELECT * FROM  `quant_dataset_table`  ";
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectStudiesStat = conn.prepareStatement(selectStudies);
            try (ResultSet rs = selectStudiesStat.executeQuery()) {
                while (rs.next()) {
                    String disease_category = rs.getString("disease_category").replace("/", "_");
                    for (String dc : disease_category.split("_")) {
                        if (!diseaseCategoriesMap.containsKey(dc) || (diseaseCategoriesMap.containsKey(dc) && diseaseCategoriesMap.get(dc) == null)) {

                            boolean[] activeHeaders = new boolean[27];
                            Set<String> diseaseCategories = new LinkedHashSet<>();
                            InitialInformationObject datasetObject = new InitialInformationObject();
                            Map<Integer, QuantDataset> updatedQuantDatasetObjectMap = new LinkedHashMap<>();
                            datasetObject.setQuantDatasetsMap(updatedQuantDatasetObjectMap);
                            datasetObject.setActiveDatasetPieChartsFilters(activeHeaders);
                            datasetObject.setDiseaseCategories(diseaseCategories);
                            diseaseCategoriesMap.put(dc, datasetObject);

                        }
//                    }
//                    for (String dc : disease_category.split("_")) {
                        InitialInformationObject datasetObject = diseaseCategoriesMap.get(dc);
                        boolean[] activeHeaders = datasetObject.getActiveDatasetPieChartsFilters();
                        Map<Integer, QuantDataset> updatedQuantDatasetObjectMap = datasetObject.getQuantDatasetsMap();
                        Set<String> diseaseCategories = datasetObject.getDiseaseCategories();

                        QuantDataset ds = new QuantDataset();
                        ds.setDiseaseHashedColor(diseaseColorMap.get(dc));
                        ds.setDiseaseStyleName(diseaseCategoryStyles.get(dc));

                        String pumed_id = rs.getString("pubmed_id");
                        if (!activeHeaders[17] && pumed_id != null && !pumed_id.equalsIgnoreCase("Not Available")) {
                            activeHeaders[17] = true;
                        }
                        ds.setPubMedId(pumed_id);

                        String author = publicationList.get(pumed_id)[0] + "";
                        if (!activeHeaders[0] && author != null && !author.equalsIgnoreCase("Not Available")) {
                            activeHeaders[0] = true;
                        }
                        ds.setAuthor(author);
                        int year = (Integer) publicationList.get(pumed_id)[1];
                        if (!activeHeaders[1] && year != 0) {
                            activeHeaders[1] = true;
                        }
                        ds.setYear(year);
//                        int identified_proteins_num = rs.getInt("identified_proteins_number");
//                        if (!activeHeaders[2] && identified_proteins_num != -1 && identified_proteins_num != 0) {
//                            activeHeaders[2] = true;
//                        }
//                        ds.setIdentifiedProteinsNumber(identified_proteins_num);

                        int quantified_proteins_number = rs.getInt("quantified_proteins_number");
                        if (!activeHeaders[3] && quantified_proteins_number != -1) {
                            activeHeaders[3] = true;
                        }
                        ds.setQuantifiedProteinsNumber(quantified_proteins_number);

                        String analytical_method = rs.getString("analytical_method");
                        if (!activeHeaders[4] && analytical_method != null && !analytical_method.equalsIgnoreCase("Not Available")) {
                            activeHeaders[4] = true;
                        }
                        ds.setAnalyticalMethod(analytical_method);

                        String raw_data_available = rs.getString("raw_data_available");
                        if (!activeHeaders[5] && raw_data_available != null && !raw_data_available.equalsIgnoreCase("false")) {
                            activeHeaders[5] = true;
                        }
                        ds.setRawDataUrl(raw_data_available);
                        String type_of_study = rs.getString("type_of_study");
                        if (!activeHeaders[7] && type_of_study != null && !type_of_study.equalsIgnoreCase("Not Available")) {
                            activeHeaders[7] = true;
                        }
                        ds.setTypeOfStudy(type_of_study);

                        String sample_type = rs.getString("sample_type");
                        if (!activeHeaders[8] && sample_type != null && !sample_type.equalsIgnoreCase("Not Available")) {
                            activeHeaders[8] = true;
                        }
                        ds.setSampleType(sample_type);

                        String sample_matching = rs.getString("sample_matching");
                        if (!activeHeaders[9] && sample_matching != null && !sample_matching.equalsIgnoreCase("Not Available")) {
                            activeHeaders[9] = true;
                        }
                        ds.setSampleMatching(sample_matching);

                        String shotgun_targeted = rs.getString("shotgun_targeted");
                        if (!activeHeaders[10] && shotgun_targeted != null && !shotgun_targeted.equalsIgnoreCase("Not Available")) {
                            activeHeaders[10] = true;
                        }
                        ds.setShotgunTargeted(shotgun_targeted);

                        String technology = rs.getString("technology");
                        if (!activeHeaders[11] && technology != null && !technology.equalsIgnoreCase("Not Available")) {
                            activeHeaders[11] = true;
                        }
                        ds.setTechnology(technology);

                        String analytical_approach = rs.getString("analytical_approach");
                        if (!activeHeaders[12] && analytical_approach != null && !analytical_approach.equalsIgnoreCase("Not Available")) {
                            activeHeaders[12] = true;
                        }
                        ds.setAnalyticalApproach(analytical_approach);

                        String enzyme = rs.getString("enzyme");
                        if (!activeHeaders[13] && enzyme != null && !enzyme.equalsIgnoreCase("Not Available")) {
                            activeHeaders[13] = true;
                        }
                        ds.setEnzyme(enzyme);

                        String quantification_basis = rs.getString("quantification_basis");
                        if (!activeHeaders[14] && quantification_basis != null && !quantification_basis.equalsIgnoreCase("Not Available")) {
                            activeHeaders[14] = true;
                        }

                        ds.setQuantificationBasis(quantification_basis);

//                        String quant_basis_comment = rs.getString("quant_basis_comment");
//                        if (!activeHeaders[15] && quant_basis_comment != null && !quant_basis_comment.equalsIgnoreCase("Not Available")) {
//                            activeHeaders[15] = true;
//                        }
//                        ds.setQuantBasisComment(quant_basis_comment);
                        int id = rs.getInt("study_index");
                        ds.setQuantDatasetIndex(id);

                        String normalization_strategy = rs.getString("normalization_strategy");
                        if (!activeHeaders[16] && normalization_strategy != null && !normalization_strategy.equalsIgnoreCase("Not Available")) {
                            activeHeaders[16] = true;
                        }
                        ds.setNormalizationStrategy(normalization_strategy);

                        String patient_group_i = rs.getString("patient_group_i");
                        if (!activeHeaders[18] && patient_group_i != null && !patient_group_i.equalsIgnoreCase("Not Available")) {
                            activeHeaders[18] = true;
                        }
                        ds.setDiseaseMainGroupI(patient_group_i);

                        int patients_group_i_number = rs.getInt("patients_group_i_number");
                        if (!activeHeaders[19] && patients_group_i_number != -1) {
                            activeHeaders[19] = true;
                        }
                        ds.setDiseaseMainGroup1Number(patients_group_i_number);

                        String patient_gr_i_comment = rs.getString("patient_gr_i_comment");
                        if (!activeHeaders[20] && patient_gr_i_comment != null && !patient_gr_i_comment.equalsIgnoreCase("Not Available")) {
                            activeHeaders[20] = true;
                        }
                        ds.setDiseaseMainGroup1Comm(patient_gr_i_comment);

                        String patient_sub_group_i = rs.getString("patient_sub_group_i");
                        if (!activeHeaders[21] && patient_sub_group_i != null && !patient_sub_group_i.equalsIgnoreCase("Not Available")) {
                            activeHeaders[21] = true;
                        }
                        ds.setDiseaseSubGroup1(patient_sub_group_i);

                        String patient_group_ii = rs.getString("patient_group_ii");
                        if (!activeHeaders[22] && patient_group_ii != null && !patient_group_ii.equalsIgnoreCase("Not Available")) {
                            activeHeaders[22] = true;
                        }
                        ds.setDiseaseMainGroup2(patient_group_ii);

                        int patients_group_ii_number = rs.getInt("patients_group_ii_number");
                        if (!activeHeaders[23] && patients_group_ii_number != -1) {
                            activeHeaders[23] = true;
                        }
                        ds.setDiseaseMainGroup2Number(patients_group_ii_number);

                        String patient_gr_ii_comment = rs.getString("patient_gr_ii_comment");
                        if (!activeHeaders[24] && patient_gr_ii_comment != null && !patient_gr_ii_comment.equalsIgnoreCase("Not Available")) {
                            activeHeaders[24] = true;
                        }
                        ds.setDiseaseMainGroup2Comm(patient_gr_ii_comment);

                        String patient_sub_group_ii = rs.getString("patient_sub_group_ii");
                        if (!activeHeaders[25] && patient_sub_group_ii != null && !patient_sub_group_ii.equalsIgnoreCase("Not Available")) {
                            activeHeaders[25] = true;
                        }
                        ds.setDiseaseSubGroup2(patient_sub_group_ii);
                        ds.setAdditionalcomments("Not Available");
                        if (disease_category.contains("_")) {
                            ds.setDiseaseCategory(disease_category.split("_")[0], disease_category.split("_")[1]);
                            ds.setCrossDisease(true);
                        } else {
                            ds.setDiseaseCategory(dc, dc);
                            ds.setCrossDisease(false);
                        }

//                        ds.setTotalProtNum(rs.getInt("total_prot_num"));
//                        ds.setUniqueProtNum(rs.getInt("uniq_prot_num"));
//                        ds.setTotalPepNum(rs.getInt("total_pept_num"));
//                        ds.setUniqePepNum(rs.getInt("uniq_pept_num"));
                        ds.setPooledSamples(Boolean.valueOf(rs.getString("pooledsamples")));

                        diseaseCategories.add(ds.getDiseaseCategoryI());
                        diseaseCategories.add(ds.getDiseaseCategoryII());
                        activeHeaders[26] = false;
                        updatedQuantDatasetObjectMap.put(ds.getQuantDatasetIndex(), ds);
                        datasetObject.setQuantDatasetsMap(updatedQuantDatasetObjectMap);
                        datasetObject.setActiveDatasetPieChartsFilters(activeHeaders);
                        datasetObject.setDiseaseCategories(diseaseCategories);
                        diseaseCategoriesMap.put(dc, datasetObject);

                    }
                }
            }

            return diseaseCategoriesMap;//   this.filterDiseaseCategoriesMap(diseaseCategoriesMap);
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            System.err.println("at error line 431 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        }

        System.gc();

        return null;

    }

    /**
     * Get the current available disease category list.
     *
     * @return set of disease category objects that has all disease category
     * information and styling information.
     */
    public LinkedHashMap<String, DiseaseCategoryObject> getDiseaseCategorySet() {

        LinkedHashMap<String, DiseaseCategoryObject> availableDiseaseCategory = new LinkedHashMap<>();
        String selectStat = "SELECT COUNT( * ) AS  `Rows` ,  `disease_category` FROM  `quant_dataset_table` GROUP BY  `disease_category` ORDER BY  `Rows` DESC ";
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }

            PreparedStatement st = conn.prepareStatement(selectStat);

            ResultSet rs = st.executeQuery();
            int total = 0;
            while (rs.next()) {
                DiseaseCategoryObject diseaseCategoryObject = new DiseaseCategoryObject();
                diseaseCategoryObject.setDiseaseCategory(rs.getString("disease_category").replace("/", "_"));
                diseaseCategoryObject.setDatasetNumber(rs.getInt("Rows"));
                if (diseaseCategoryObject.getDiseaseCategory().contains("_")) {
//                    continue;
                } else {
                    diseaseCategoryObject.setDiseaseStyleName(diseaseCategoryStyles.get(diseaseCategoryObject.getDiseaseCategory()));
                    diseaseCategoryObject.setDiseaseHashedColor(diseaseColorMap.get(diseaseCategoryObject.getDiseaseCategory()));
                    diseaseCategoryObject.setDiseaseAwtColor(Color.decode(diseaseCategoryObject.getDiseaseHashedColor()));

                }
                total += diseaseCategoryObject.getDatasetNumber();
                availableDiseaseCategory.put(diseaseCategoryObject.getDiseaseCategory(), diseaseCategoryObject);
            }
            LinkedHashMap<String, DiseaseCategoryObject> tempDiseaseCategory = new LinkedHashMap<>(availableDiseaseCategory);
            tempDiseaseCategory.keySet().stream().filter((category) -> (category.contains("_"))).map((category) -> {
                availableDiseaseCategory.remove(category);
                return category;
            }).forEachOrdered((String category) -> {
                for (String singleCat : category.split("_")) {
                    availableDiseaseCategory.get(singleCat).setDatasetNumber(availableDiseaseCategory.get(singleCat).getDatasetNumber() + tempDiseaseCategory.get(category).getDatasetNumber());
                }
            });

            DiseaseCategoryObject diseaseCategoryObject = new DiseaseCategoryObject();
            diseaseCategoryObject.setDiseaseCategory("All Diseases");
            diseaseCategoryObject.setDatasetNumber(total);
            diseaseCategoryObject.setDiseaseStyleName("alldiseasestyle");
            diseaseCategoryObject.setDiseaseHashedColor("#7E7E7E");
            diseaseCategoryObject.setDiseaseAwtColor(Color.decode("#7E7E7E"));
            availableDiseaseCategory.put(diseaseCategoryObject.getDiseaseCategory(), diseaseCategoryObject);

        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            System.err.println("at error " + this.getClass().getName() + "  line 470  " + e.getLocalizedMessage());
            return null;
        }

        return availableDiseaseCategory;
    }

    /**
     * Get active quantification pie charts filters (to hide them if they are
     * empty).
     *
     * @return boolean array for the active and not active pie chart filters
     * indexes.
     */
    public Map<String, boolean[]> getActivePieChartQuantFilters() {

        Map<String, boolean[]> activePieChartQuantFiltersDiseaseCategoryMap = new LinkedHashMap<>();
        List<String> disCatList = new LinkedList<>();
        try {

            PreparedStatement selectPumed_idStat;
            String selectPumed_id = "SELECT  `pubmed_id` ,  `disease_category` FROM  `quant_dataset_table` GROUP BY  `pubmed_id` ,  `disease_category` ORDER BY  `pubmed_id` ";
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectPumed_idStat = conn.prepareStatement(selectPumed_id);
            try (ResultSet rs = selectPumed_idStat.executeQuery()) {
                int pumed_id_index = 0;
                while (rs.next()) {
                    disCatList.add(pumed_id_index, rs.getString("disease_category"));
                    pumed_id_index++;
                }
            }
            /// check the colums one by one 
            boolean[] activeFilters = new boolean[]{false, false, false, false, true, true, false, true, true, true, false, true, false, false, false, false, false, false};
            disCatList.stream().forEach((str) -> {
                activePieChartQuantFiltersDiseaseCategoryMap.put(str, activeFilters);
            });
            activePieChartQuantFiltersDiseaseCategoryMap.put("All Diseases", activeFilters);

        } catch (ClassNotFoundException e) {
            System.err.println("at error line 2912 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            System.err.println("at error line 2915 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
        } catch (InstantiationException e) {
            System.err.println("at error line 2918 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
        } catch (SQLException e) {
            System.err.println("at error line 2921 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
        }
        System.gc();

        return activePieChartQuantFiltersDiseaseCategoryMap;

    }

    /**
     * Get active quantification pie charts filters within quant searching
     * proteins results (to hide them if they are empty).
     *
     * @param searchQuantificationProtList List of quant proteins.
     * @return boolean array for the active and not active pie chart filters
     * indexes.
     */
    public Map<String, boolean[]> getActivePieChartQuantFilters(List<QuantProtein> searchQuantificationProtList) {

        Map<String, boolean[]> activePieChartQuantFiltersDiseaseCategoryMap = new LinkedHashMap<>();
        List<String> disCatList = new LinkedList<>();
        try {

            Set<Integer> QuantDatasetIds = new HashSet<>();
            searchQuantificationProtList.stream().forEach((quantProt) -> {
                QuantDatasetIds.add(quantProt.getQuantDatasetIndex());
            });
            StringBuilder sb = new StringBuilder();

            QuantDatasetIds.stream().map((index) -> {
                sb.append("  `study_index` = ").append(index);
                return index;
            }).forEach((_item) -> {
                sb.append(" OR ");
            });
//            String stat = sb.toString().substring(0, sb.length() - 4);
            PreparedStatement selectPumed_idStat;
            String selectPumed_id = "SELECT  `pubmed_id` ,  `disease_category` FROM  `quant_dataset_table` GROUP BY  `pubmed_id` ,  `disease_category` ORDER BY  `pubmed_id` ";
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectPumed_idStat = conn.prepareStatement(selectPumed_id);
            try (ResultSet rs = selectPumed_idStat.executeQuery()) {
                int pumed_id_index = 0;
                while (rs.next()) {
//                    if (rs.getString("disease_category").contains("_")) {
//                        continue;
//                    }
                    disCatList.add(pumed_id_index, rs.getString("disease_category"));
                    pumed_id_index++;
                }
                rs.close();
                /// check the colums one by one 
            }

            boolean[] activeFilters = new boolean[]{false, false, false, false, true, true, false, true, true, true, false, true, false, false, false, false, false, false};
            disCatList.stream().forEach((str) -> {
                activePieChartQuantFiltersDiseaseCategoryMap.put(str, activeFilters);

            });
            activePieChartQuantFiltersDiseaseCategoryMap.put("All Diseases", activeFilters);
        } catch (ClassNotFoundException e) {
            System.err.println("at error line 2912 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            System.err.println("at error line 2915 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
        } catch (InstantiationException e) {
            System.err.println("at error line 2918 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
        } catch (SQLException e) {
            System.err.println("at error line 2921 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
        }
        System.gc();
        return activePieChartQuantFiltersDiseaseCategoryMap;
    }

    /**
     * Get set of disease groups names for special disease category
     *
     * @param diseaseCat disease category name
     * @return map of the short and long diseases names
     */
    public Set<String> getDiseaseGroupNameMap(String diseaseCat) {
        Set<String> diseaseNames = new HashSet<>();
        String selectPatient_sub_group_i = "SELECT `patient_sub_group_i` FROM `quant_dataset_table`  where `disease_category` like(?) GROUP BY `patient_sub_group_i` ORDER BY `patient_sub_group_i` ";

        String selectPatient_sub_group_ii = "SELECT `patient_sub_group_ii` FROM `quant_dataset_table`  where `disease_category` like(?) GROUP BY `patient_sub_group_ii` ORDER BY `patient_sub_group_ii` ";
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            PreparedStatement selectProStat = conn.prepareStatement(selectPatient_sub_group_i);
            selectProStat.setString(1, "%" + diseaseCat + "%");
            ResultSet rs = selectProStat.executeQuery();
            System.gc();
            while (rs.next()) {
                diseaseNames.add(rs.getString("patient_sub_group_i").trim());
            }
            rs.close();

            selectProStat = conn.prepareStatement(selectPatient_sub_group_ii);
            selectProStat.setString(1, "%" + diseaseCat + "%");
            rs = selectProStat.executeQuery();
            System.gc();
            while (rs.next()) {
                diseaseNames.add(rs.getString("patient_sub_group_ii").trim());
            }

        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            System.err.println("at error " + this.getClass().getName() + "  line 3167  " + e.getLocalizedMessage());
        }

        return diseaseNames;
    }

    /**
     * Get map for disease groups full name.
     *
     * @return map of the short and long diseases names
     */
    public Map<String, String> getDiseaseGroupsFullNameMap() {
        Map diseaseFullNameMap = new HashMap<>();
        String selectAllDiseFullName = "SELECT * FROM  `quant_disease_groups_tooltips_table` ";
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            PreparedStatement selectProStat = conn.prepareStatement(selectAllDiseFullName);

            ResultSet rs = selectProStat.executeQuery();
            System.gc();
            while (rs.next()) {
                diseaseFullNameMap.put(rs.getString("short").trim(), rs.getString("full").trim());
            }
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            System.err.println("at error " + this.getClass().getName() + "  line 3167  " + e.getLocalizedMessage());
        }

        return diseaseFullNameMap;
    }

    /**
     * Get quant proteins list for a number of quant datasets
     *
     * @param quantDatasetIds Array of quant dataset indexes.
     * @return quant proteins list.
     */
    public Set<QuantProtein> getQuantificationProteins(Object[] quantDatasetIds) {

        Set<QuantProtein> quantProtList = new HashSet<>();
        Set<QuantProtein> tquantProtList = new HashSet<>();
        try {
            StringBuilder sb = new StringBuilder();

            for (Object index : quantDatasetIds) {
                sb.append("  `study_index` = ").append(index);
                sb.append(" OR ");
            }
            String stat = sb.toString().substring(0, sb.length() - 4);
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            String selectDsGroupNum = "SELECT  `quantification_basis` ,`disease_category` ,`patients_group_i_number` , `patients_group_ii_number`,`patient_group_i`,`patient_group_ii`,`patient_sub_group_i`,`patient_sub_group_ii`,`study_index` FROM `quant_dataset_table` WHERE  " + stat + " ;";
            PreparedStatement selectselectDsGroupNumStat = conn.prepareStatement(selectDsGroupNum);
            Map<Integer, Object[]> datasetIdDesGrs;
            try (ResultSet rs = selectselectDsGroupNumStat.executeQuery()) {
                datasetIdDesGrs = new HashMap<>();
                while (rs.next()) {
                    datasetIdDesGrs.put(rs.getInt("study_index"), new Object[]{rs.getInt("patients_group_i_number"), rs.getInt("patients_group_ii_number"), rs.getString("patient_group_i").trim(), rs.getString("patient_group_ii").trim(), rs.getString("patient_sub_group_i").trim(), rs.getString("patient_sub_group_ii").trim(), rs.getString("disease_category"), rs.getString("quantification_basis")});
                }
            }
            sb = new StringBuilder();
            for (Object index : quantDatasetIds) {
                sb.append("  `study_index` = ").append(index);
                sb.append(" OR ");
            }
            stat = sb.toString().substring(0, sb.length() - 4);
            String selectQuantProt = "SELECT * FROM `quant_proteins_table`  WHERE  " + stat + " ;";
            PreparedStatement selectQuantProtStat = conn.prepareStatement(selectQuantProt);
            try (ResultSet rs1 = selectQuantProtStat.executeQuery()) {
                while (rs1.next()) {
                    QuantProtein quantProt = new QuantProtein();
                    quantProt.setProtIndex(rs1.getInt("protein_index"));
                    quantProt.setQuantDatasetIndex(rs1.getInt("study_index"));
                    String sequence = rs1.getString("sequance") + "";
                    quantProt.setSequence(sequence.replace("null", ""));
                    quantProt.setUniprotAccessionNumber(rs1.getString("uniprot_accession"));
                    String uniprotName = rs1.getString("uniprot_protein_name");
                    if (uniprotName.replace("(", "___").split("___").length > 2) {
                        String[] names = uniprotName.replace("(", "___").split("___");
                        uniprotName = "";
                        for (int x = 0; x < names.length - 1; x++) {
                            uniprotName += "(" + names[x];
                        }
                        uniprotName = uniprotName.substring(1);
                    } else {
                        uniprotName = uniprotName.replace("(", "___").split("___")[0];

                    }
                    quantProt.setUniprotProteinName(uniprotName);
                    quantProt.setPublicationAccessionNumber(rs1.getString("publication_accession"));
                    quantProt.setPublicationProteinName(rs1.getString("publication_protein_name").trim().replace("#NAME?", "N/A"));
                    quantProt.setQuantifiedPeptidesNumber(rs1.getInt("quantified_peptides_number"));
                    quantProt.setString_fc_value(rs1.getString("fold_change"));
                    quantProt.setString_p_value(rs1.getString("string_p_value"));
                    quantProt.setFc_value(rs1.getDouble("log_2_fold_change"));
                    quantProt.setRoc_auc(rs1.getDouble("roc_auc"));
                    quantProt.setP_value(rs1.getDouble("p_value"));
                    quantProt.setP_value_comments(rs1.getString("p_value_comments"));
                    quantProt.setPvalueSignificanceThreshold(rs1.getString("pvalue_significance_threshold"));
                    quantProt.setAdditionalComments(rs1.getString("additional_comments"));
                    quantProt.setQuantBasisComment(rs1.getString("quant_bases_comments"));
                    quantProtList.add(quantProt);
                }
            }
            System.gc();
            quantProtList.stream().map((qp) -> {
                qp.setDiseaseGroupIIPatientsNumber((Integer) datasetIdDesGrs.get(qp.getQuantDatasetIndex())[1]);
                return qp;
            }).map((qp) -> {
                qp.setDiseaseGroupIPatientsNumber((Integer) datasetIdDesGrs.get(qp.getQuantDatasetIndex())[0]);
                return qp;
            }).map((qp) -> {
                qp.setDiseaseMainGroupI(datasetIdDesGrs.get(qp.getQuantDatasetIndex())[2].toString());
                return qp;
            }).map((qp) -> {
                qp.setDiseaseMainGroupII(datasetIdDesGrs.get(qp.getQuantDatasetIndex())[3].toString());
                return qp;
            }).map((qp) -> {
                qp.setOriginalDiseaseSubGroupI(datasetIdDesGrs.get(qp.getQuantDatasetIndex())[4].toString());
                return qp;
            }).map((qp) -> {
                qp.setOriginalDiseaseSubGroupII(datasetIdDesGrs.get(qp.getQuantDatasetIndex())[5].toString());
                return qp;
            }).map((qp) -> {
                String diseaseCategories = datasetIdDesGrs.get(qp.getQuantDatasetIndex())[6].toString();
                String diseaseCatI;
                String diseaseCatII;
                if (diseaseCategories.contains("_")) {
                    diseaseCatI = diseaseCategories.split("_")[0];
                    diseaseCatII = diseaseCategories.split("_")[1];
                } else {
                    diseaseCatI = diseaseCategories;
                    diseaseCatII = diseaseCategories;
                }
                qp.setDiseaseCategoryII(diseaseCatII);
                qp.setDiseaseCategoryI(diseaseCatI);
                return qp;
            }).map((qp) -> {
                qp.setQuantificationBasis(datasetIdDesGrs.get(qp.getQuantDatasetIndex())[7].toString());
                return qp;
            }).forEach((qp) -> {
                tquantProtList.add(qp);
            });

        } catch (ClassNotFoundException exp) {
            System.out.println("at error  " + this.getClass().getName() + "   line 3521   " + exp.getLocalizedMessage());
            return null;
        } catch (IllegalAccessException exp) {
            System.out.println("at error  " + this.getClass().getName() + "   line 3524   " + exp.getLocalizedMessage());
            return null;
        } catch (InstantiationException exp) {
            System.out.println("at error  " + this.getClass().getName() + "   line 3527   " + exp.getLocalizedMessage());
            return null;
        } catch (SQLException exp) {
            System.out.println("at error  " + this.getClass().getName() + "   line 3530   " + exp.getLocalizedMessage());
            return null;
        }
        System.gc();
        return tquantProtList;

    }

    /**
     * Get quant peptides list for specific quant dataset
     *
     * @param quantDatasetIds Array of quant dataset ids (indexes)
     * @return quant peptides list
     */
    public Map<String, Set<QuantPeptide>> getQuantificationPeptides(Object[] quantDatasetIds) {
        Set<QuantPeptide> quantPeptidetList = new HashSet<>();
        try {
            StringBuilder sb = new StringBuilder();

            for (Object index : quantDatasetIds) {
                sb.append("  `study_index` = ").append(index);
                sb.append(" OR ");
            }
            String stat = sb.toString().substring(0, sb.length() - 4);

            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }

            String selectQuantProteinsIds = "SELECT `protein_index`,`study_index`, `publication_accession`,`uniprot_accession` FROM `quant_proteins_table` WHERE " + stat;
            PreparedStatement selectQuantProtStat = conn.prepareStatement(selectQuantProteinsIds);
            Map<Integer, Set<Integer>> studyIndex_proteinIndexMap = new HashMap<>();
            Map<Integer, String> proteinIndex_accessionMap = new HashMap<>();
            try (ResultSet rs1 = selectQuantProtStat.executeQuery()) {
                while (rs1.next()) {
                    int studyIndex = rs1.getInt("study_index");
                    int proteinIndex = rs1.getInt("protein_index");
                    if (!studyIndex_proteinIndexMap.containsKey(studyIndex)) {
                        studyIndex_proteinIndexMap.put(studyIndex, new HashSet<>());
                    }
                    String proteinAccession = rs1.getString("uniprot_accession");
                    if (proteinAccession.equalsIgnoreCase("")) {
                        proteinAccession = rs1.getString("publication_accession");
                    }
                    studyIndex_proteinIndexMap.get(studyIndex).add(proteinIndex);
                    proteinIndex_accessionMap.put(proteinIndex, proteinAccession);
                }
            }
            StringBuilder sb2 = new StringBuilder();

            studyIndex_proteinIndexMap.values().forEach(indexes -> {
                for (int index : indexes) {
                    sb2.append("  `protein_index` = ").append(index);
                    sb2.append(" OR ");
                }
            });
            stat = sb2.toString().substring(0, sb2.length() - 4);
            String selectQuantPeptides = "SELECT * FROM `quant_peptides_table` WHERE " + stat;
            PreparedStatement selectQuantPeptidesStat = conn.prepareStatement(selectQuantPeptides);
            try (ResultSet rs1 = selectQuantPeptidesStat.executeQuery()) {
                while (rs1.next()) {
                    QuantPeptide quantPeptide = new QuantPeptide();
                    quantPeptide.setProtIndex(rs1.getInt("protein_index"));
                    quantPeptide.setUniqueId(rs1.getInt("peptide_index"));
                    quantPeptide.setPeptideModification(rs1.getString("peptide_modification"));
                    quantPeptide.setPeptideSequence(rs1.getString("peptide_sequance"));
                    quantPeptide.setModification_comment(rs1.getString("modification_comment"));
                    quantPeptide.setString_fc_value(rs1.getString("string_fold_change_value"));
                    quantPeptide.setString_p_value(rs1.getString("string_p_value"));
                    quantPeptide.setP_value(rs1.getDouble("p_value"));
                    quantPeptide.setRoc_auc(rs1.getDouble("roc_auc"));
                    quantPeptide.setFc_value(rs1.getDouble("log_2_fold_change"));
                    quantPeptide.setP_value_comments(rs1.getString("p_value_comments"));
                    quantPeptide.setUniprotAccessionNumber(proteinIndex_accessionMap.get(quantPeptide.getProtIndex()));
                    quantPeptide.setSequenceAnnotated(rs1.getString("sequence_annotated"));
                    quantPeptide.setPvalueSignificanceThreshold(rs1.getString("pvalue_significance_threshold"));
                    quantPeptide.setPeptideCharge(rs1.getInt("peptide_charge"));
                    quantPeptide.setAdditionalComments(rs1.getString("additional_comments"));
                    quantPeptide.setQuantBasisComment(rs1.getString("quant_bases_comments"));
                    quantPeptide.setPeptideSignature("__" + quantPeptide.getProtIndex() + "__" + quantPeptide.getQuantDatasetIndex() + "__");
                    quantPeptidetList.add(quantPeptide);
                }
            }

            Map<String, Set<QuantPeptide>> quantProtPeptidetList = new HashMap<>();
            quantPeptidetList.stream().map((qp) -> {
                if (!quantProtPeptidetList.containsKey(qp.getPeptideSignature())) {
                    Set<QuantPeptide> quantPepProtSet = new HashSet<>();
                    quantProtPeptidetList.put(qp.getPeptideSignature(), quantPepProtSet);

                }
                return qp;
            }).forEach((qp) -> {
                Set<QuantPeptide> quantPepProtSet = quantProtPeptidetList.get(qp.getPeptideSignature());
                quantPepProtSet.add(qp);
                quantProtPeptidetList.put(qp.getPeptideSignature(), quantPepProtSet);
            });
            System.gc();

            return quantProtPeptidetList;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException exp) {
            System.err.println(exp.getLocalizedMessage());
            return null;
        }
    }

    /**
     * Search for quant proteins by keywords
     *
     * @param query query object that has all query information
     * @param toCompare quant comparing mode
     * @return Quant Proteins Searching List
     */
    public List<QuantProtein> searchQuantificationProteins(Query query, boolean toCompare) {

        StringBuilder sb = new StringBuilder();
        QueryConstractorHandler qhandler = new QueryConstractorHandler();
        PreparedStatement selectProStat;
        String selectPro;
        //main filters  
        if (query.getSearchKeyWords() != null && !query.getSearchKeyWords().equalsIgnoreCase("")) {
            String[] queryWordsArr = query.getSearchKeyWords().split("\n");
            HashSet<String> searchSet = new LinkedHashSet<>();
            for (String str : queryWordsArr) {
                if (str.trim().equalsIgnoreCase("")) {
                    continue;
                }
                searchSet.add(str.trim());
            }
            if (query.getSearchBy().equalsIgnoreCase("Protein Accession")) {
                int x = 0;
                for (String str : searchSet) {
                    if (x > 0) {
                        sb.append(" OR ");
                    }
                    if (toCompare) {
                        sb.append("`uniprot_accession` = ?");
                        qhandler.addQueryParam("String", str);
                    } else {
                        sb.append("`uniprot_accession` = ? OR `publication_accession` = ?");
                        qhandler.addQueryParam("String", str);
                        qhandler.addQueryParam("String", str);
                    }

                    x++;
                }
            } else if (query.getSearchBy().equalsIgnoreCase("Protein Name")) {
                int x = 0;
                for (String str : searchSet) {
                    if (x > 0) {
                        sb.append(" OR ");
                    }
                    sb.append("`uniprot_protein_name` LIKE(?) ");
                    qhandler.addQueryParam("String", "%" + str + "%");
                    x++;
                }
            } else {  //peptide sequance
                int x = 0;
                for (String str : searchSet) {
                    if (x > 0) {
                        sb.append(" OR ");
                    }
                    sb.append("`peptide_sequance` ='").append(str.trim()).append("'");//
                    x++;
                }

                String selectProteinIndex = "SELECT `protein_index` FROM `quant_peptides_table` where " + sb.toString();

                try {
                    if (conn == null || conn.isClosed()) {
                        Class.forName(driver).newInstance();
                        conn = DriverManager.getConnection(url + dbName, userName, password);
                    }
                    selectProStat = conn.prepareStatement(selectProteinIndex);
                    ResultSet rs = selectProStat.executeQuery();
                    Set<Integer> proteinsIndexSet = new LinkedHashSet<>();
                    while (rs.next()) {
                        proteinsIndexSet.add(rs.getInt("protein_index"));

                    }
                    sb = new StringBuilder();
                    x = 0;
                    for (int protIndex : proteinsIndexSet) {
                        if (x > 0) {
                            sb.append(" OR ");
                        }
                        sb.append("`protein_index` = ?");
                        qhandler.addQueryParam("Integer", protIndex + "");
                        x++;
                    }

                } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException exp) {
                    System.err.println("at error line 1052 " + this.getClass().getName() + "   " + exp.getLocalizedMessage());
                }
            }
        }

        selectPro = "SELECT * FROM   `quant_proteins_table`  Where " + (sb.toString());

        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectProStat = conn.prepareStatement(selectPro);
            selectProStat = qhandler.initStatment(selectProStat);

            ResultSet rs = selectProStat.executeQuery();
            List<QuantProtein> quantProtResultList = fillQuantProtData(rs);
//            System.gc();

            Set<Integer> quantDatasetIds = new HashSet<>();
            quantProtResultList.stream().forEach((quantProt) -> {
                quantDatasetIds.add(quantProt.getQuantDatasetIndex());
            });

            if (quantDatasetIds.isEmpty()) {
                return new ArrayList<>();
            }
            sb = new StringBuilder();
            for (Object index : quantDatasetIds) {
                sb.append("  `study_index` = ").append(index);
                sb.append(" OR ");

            }

            String stat = sb.toString().substring(0, sb.length() - 4);
            String selectDsGroupNum = "SELECT `disease_category`,`pubmed_id` ,`study_index` ,`patients_group_i_number` , `patients_group_ii_number`,`patient_group_i`,`patient_group_ii`,`patient_sub_group_i`,`patient_sub_group_ii` FROM `quant_dataset_table` Where  " + stat + ";"; //"SELECT `patients_group_i_number` , `patients_group_ii_number`,`patient_group_i`,`patient_group_ii`,`patient_sub_group_i`,`patient_sub_group_ii`,`index` FROM `quant_dataset_table` WHERE  " + stat + " ;";

            PreparedStatement selectselectDsGroupNumStat = conn.prepareStatement(selectDsGroupNum);
            rs = selectselectDsGroupNumStat.executeQuery();
            Map<Integer, Object[]> datasetIdDesGrs = new HashMap<>();
            while (rs.next()) {
//                if (rs.getString("disease_category").contains("_")) {
//                    continue;
//                }
                datasetIdDesGrs.put(rs.getInt("index"), new Object[]{rs.getInt("patients_group_i_number"), rs.getInt("patients_group_ii_number"), rs.getString("patient_group_i").trim(), rs.getString("patient_group_ii").trim(), rs.getString("patient_sub_group_i").trim(), rs.getString("patient_sub_group_ii").trim(), rs.getString("pubmed_id"), rs.getString("disease_category")});
            }
            rs.close();

            List<QuantProtein> updatedQuantProtResultList = new ArrayList<>();

            quantProtResultList.stream().filter((quantProt) -> (datasetIdDesGrs.containsKey(quantProt.getQuantDatasetIndex()))).map((quantProt) -> {
                Object[] grNumArr = datasetIdDesGrs.get(quantProt.getQuantDatasetIndex());
                quantProt.setDiseaseGroupIPatientsNumber((Integer) grNumArr[0]);
                quantProt.setDiseaseGroupIIPatientsNumber((Integer) grNumArr[1]);

                String diseaseCategories = grNumArr[7].toString();
                String diseaseCatI;
                String diseaseCatII;
                if (diseaseCategories.contains("_")) {
                    diseaseCatI = diseaseCategories.split("_")[0];
                    diseaseCatII = diseaseCategories.split("_")[1];
                } else {
                    diseaseCatI = diseaseCategories;
                    diseaseCatII = diseaseCategories;
                }

                quantProt.setDiseaseMainGroupI((String) grNumArr[2] + "\n" + diseaseCatI.replace(" ", "_").replace("'", "-") + "_Disease");
                quantProt.setDiseaseMainGroupII((String) grNumArr[3] + "\n" + diseaseCatII.replace(" ", "_").replace("'", "-") + "_Disease");
                quantProt.setOriginalDiseaseSubGroupI((String) grNumArr[4] + "\n" + diseaseCatI.replace(" ", "_").replace("'", "-") + "_Disease");
                quantProt.setOriginalDiseaseSubGroupII((String) grNumArr[5] + "\n" + diseaseCatII.replace(" ", "_").replace("'", "-") + "_Disease");
                quantProt.setDiseaseCategoryI(diseaseCatI);
                quantProt.setDiseaseCategoryII(diseaseCatII);
                quantProt.setPubMedId((String) grNumArr[6]);
                return quantProt;
            }).forEach((quantProt) -> {
                if (query.getDiseaseCategorys() == null || query.getDiseaseCategorys().isEmpty() || query.getDiseaseCategorys().contains(quantProt.getDiseaseCategoryI()) || query.getDiseaseCategorys().contains(quantProt.getDiseaseCategoryII())) {
                    updatedQuantProtResultList.add(quantProt);
                }
            });
            return updatedQuantProtResultList;

        } catch (ClassNotFoundException e) {
            System.err.println("at error line 3654 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
            return null;
        } catch (IllegalAccessException e) {
            System.err.println("at error line 3657 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

            return null;
        } catch (InstantiationException e) {
            System.err.println("at error line 3660 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

            return null;
        } catch (SQLException e) {
            System.err.println("at error line 3665 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

            return null;
        }

    }

    /**
     * Fill quant protein information from the result set
     *
     * @param resultSet results set to fill identification peptides data
     * @return quant proteins List
     */
    private List<QuantProtein> fillQuantProtData(ResultSet resultSet) {
        List<QuantProtein> quantProtResultList = new ArrayList<>();

        try {
            while (resultSet.next()) {

                QuantProtein quantProt = new QuantProtein();
                quantProt.setProtIndex(resultSet.getInt("index"));
                quantProt.setQuantDatasetIndex(resultSet.getInt("ds_ID"));
                quantProt.setSequence(resultSet.getString("sequance"));
                quantProt.setUniprotAccessionNumber(resultSet.getString("uniprot_accession"));

                quantProt.setUniprotProteinName(resultSet.getString("uniprot_protein_name").replace("(", "___").split("___")[0]);
                quantProt.setPublicationAccessionNumber(resultSet.getString("publication_accession"));
                quantProt.setPublicationProteinName(resultSet.getString("publication_protein_name"));
                quantProt.setQuantifiedPeptidesNumber(resultSet.getInt("quantified_peptides_number"));
                quantProt.setString_fc_value(resultSet.getString("fold_change"));
                quantProt.setString_p_value(resultSet.getString("string_p_value"));
                quantProt.setFc_value(resultSet.getDouble("log_2_fold_change"));
                quantProt.setRoc_auc(resultSet.getDouble("roc_auc"));
                quantProt.setP_value(resultSet.getDouble("p_value"));
                quantProt.setP_value_comments(resultSet.getString("p_value_comments"));
                quantProt.setPvalueSignificanceThreshold(resultSet.getString("pvalue_significance_threshold"));
                quantProt.setAdditionalComments(resultSet.getString("additional_comments"));
                quantProt.setQuantBasisComment(resultSet.getString("quant_bases_comments"));

                quantProtResultList.add(quantProt);

            }

        } catch (Exception exp) {
            System.err.println("at error line 2119 " + this.getClass().getName() + "   " + exp.getLocalizedMessage());
        }
        return quantProtResultList;
    }

    /**
     * Search for identification proteins by accession keywords
     *
     * @param searchSet set of query words
     * @param validatedOnly only validated proteins results
     * @return dataset Proteins Searching List
     */
    public Map<Integer, IdentificationProteinBean> searchIdentificationProteinAllDatasetsByAccession(Set<String> searchSet, boolean validatedOnly) {
        PreparedStatement selectProStat;
        String selectPro;

        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < searchSet.size(); x++) {
            if (x > 0) {
                sb.append(" OR ");
            }
            sb.append("`prot_key` LIKE (?)");

        }
        String sta = "";
        if (!sb.toString().trim().isEmpty()) {
            sta = "Where " + (sb.toString());
        }

        if (validatedOnly) {
            selectPro = "SELECT * FROM `experiment_protein_table`  " + sta + " AND `valid`=?;";
        } else {

            selectPro = "SELECT * FROM `experiment_protein_table` " + (sta);
        }
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            selectProStat = conn.prepareStatement(selectPro);
            int index = 1;
            for (String str : searchSet) {
                selectProStat.setString(index++, "%" + str.replace("'", "") + "%");
            }
            if (validatedOnly) {
                selectProStat.setString(index, "TRUE");
            }

            ResultSet rs = selectProStat.executeQuery();

            Map<Integer, IdentificationProteinBean> proteinsList = fillProteinInformation(rs);
            System.gc();
            return proteinsList;

        } catch (ClassNotFoundException e) {
            System.err.println("at error line 1595 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
            return new HashMap<>();
        } catch (IllegalAccessException e) {
            System.err.println("at error line 1598 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

            return new HashMap<>();
        } catch (InstantiationException e) {
            System.err.println("at error line 1602 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

            return new HashMap<>();
        } catch (SQLException e) {
            System.err.println("at error line 1606 " + this.getClass().getName() + "   " + e.getLocalizedMessage());
            return new HashMap<>();
        }

    }

    /**
     * Search for identification proteins by protein description keywords
     *
     * @param protSearchKeyword array of query words
     * @param validatedOnly only validated proteins results
     * @return datasetProteinsSearchList
     */
    public Map<Integer, IdentificationProteinBean> searchIdentificationProteinAllDatasetsByName(String protSearchKeyword, boolean validatedOnly) {
        PreparedStatement selectProStat;
        String selectPro;
        String[] queryWordsArr = protSearchKeyword.split("\n");
        Set<String> searchSet = new HashSet<>();
        for (String str : queryWordsArr) {
            if (str.trim().length() > 3) {
                searchSet.add(str.trim());
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < searchSet.size(); x++) {
            if (x > 0) {
                sb.append(" OR ");
            }
            sb.append("`description` LIKE(?)");

        }
        if (validatedOnly) {
            selectPro = "SELECT * FROM `experiment_protein_table` WHERE " + (sb.toString()) + " AND `valid`=?;";
        } else {
            selectPro = "SELECT * FROM `experiment_protein_table` WHERE " + (sb.toString());
        }
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }
            int index = 1;
            selectProStat = conn.prepareStatement(selectPro);
            for (String str : searchSet) {
                selectProStat.setString(index++, "%" + str + "%");
            }
            if (validatedOnly) {
                selectProStat.setString(index, "TRUE");
            }

            ResultSet rs = selectProStat.executeQuery();
            Map<Integer, IdentificationProteinBean> proteinsList = fillProteinInformation(rs);
            System.gc();
            return proteinsList;
        } catch (ClassNotFoundException e) {
            System.err.println("at error line 1875 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println("at error line 1878 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println("at error line 1881 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println("at error line 1884 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        }

        System.gc();
        return new HashMap<>();
    }

    /**
     * Search for identification proteins by peptide sequence keywords
     *
     * @param peptideSequenceKeyword array of query words
     * @param validatedOnly only validated proteins results
     * @return datasetProteinsSearchList
     */
    public Map<Integer, IdentificationProteinBean> SearchIdentificationProteinAllDatasetsByPeptideSequence(String peptideSequenceKeyword, boolean validatedOnly) {
        PreparedStatement selectPepIdStat;
        Map<Integer, IdentificationProteinBean> proteinsList;
        Map<Integer, IdentificationProteinBean> filteredProteinsList = new HashMap<>();

        String[] queryWordsArr = peptideSequenceKeyword.split("\n");
        Set<String> searchSet = new HashSet<>();
        for (String str : queryWordsArr) {
            if (!str.trim().equalsIgnoreCase("")) {
                searchSet.add(str.trim());
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < searchSet.size(); x++) {
            if (x > 0) {
                sb.append(" OR ");
            }
            sb.append(" `sequence` LIKE(?) ");

        }

        Set<String> protAccessionQuerySet = new LinkedHashSet<>();
        Set<Integer> expIds = new HashSet<>();
        String selectProtAccession = "SELECT  `protein` ,  `other_protein(s)` ,  `peptide_protein(s)` , `exp_id` FROM  `proteins_peptides_table`  WHERE " + (sb.toString()) + " ;";
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
            }

            selectPepIdStat = conn.prepareStatement(selectProtAccession);
            int index = 1;
            for (String str : searchSet) {

                selectPepIdStat.setString(index++, "%" + str.trim() + "%");
            }
            try (ResultSet rs = selectPepIdStat.executeQuery()) {
                while (rs.next()) {
                    String prot = rs.getString("protein");
                    if (prot != null && !prot.equalsIgnoreCase("") && !prot.equalsIgnoreCase("SHARED PEPTIDE")) {
                        protAccessionQuerySet.add(prot);
                    }
                    String otherProt = rs.getString("other_protein(s)");
                    if (otherProt != null && !otherProt.equalsIgnoreCase("")) {
                        protAccessionQuerySet.addAll(Arrays.asList(otherProt.split(",")));
                    }
                    String peptideProt = rs.getString("peptide_protein(s)");
                    if (peptideProt != null && !peptideProt.equalsIgnoreCase("")) {
                        protAccessionQuerySet.addAll(Arrays.asList(peptideProt.split(",")));
                    }
                    expIds.add(rs.getInt("exp_id"));

                }
            }
            proteinsList = this.searchIdentificationProteinAllDatasetsByAccession(protAccessionQuerySet, validatedOnly);
            if (proteinsList == null) {
                return new HashMap<>();
            }
            proteinsList.keySet().stream().forEach((key) -> {
                IdentificationProteinBean pb = proteinsList.get(key);
                if (expIds.contains(pb.getDatasetId())) {
                    filteredProteinsList.put(key, pb);
                }
            });
            System.gc();
            return filteredProteinsList;
        } catch (ClassNotFoundException e) {
            System.err.println("at error line 2087 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (IllegalAccessException e) {
            System.err.println("at error line 2081 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (InstantiationException e) {
            System.err.println("at error line 2074 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        } catch (SQLException e) {
            System.err.println("at error line 2078 " + this.getClass().getName() + "   " + e.getLocalizedMessage());

        }

        System.gc();
        return null;
    }

    /**
     * Fill identification protein information from the result set
     *
     * @param resultSet results set to fill identification peptides data
     * @return datasetProteinsList
     *
     */
    private Map<Integer, IdentificationProteinBean> fillProteinInformation(ResultSet rs) {
        Map<Integer, IdentificationProteinBean> proteinsList = new HashMap<>();
        try {
            while (rs.next()) {
                IdentificationProteinBean temPb = new IdentificationProteinBean();
                temPb.setDatasetId(rs.getInt("exp_id"));
                temPb.setAccession(rs.getString("prot_accession"));
                temPb.setDescription(rs.getString("description").trim());
                temPb.setOtherProteins(rs.getString("other_protein(s)"));
                temPb.setProteinInferenceClass(rs.getString("protein_inference_class"));
                temPb.setSequenceCoverage(rs.getDouble("sequence_coverage(%)"));
                temPb.setObservableCoverage(rs.getDouble("observable_coverage(%)"));
                temPb.setConfidentPtmSites(rs.getString("confident_ptm_sites"));
                temPb.setNumberConfident(rs.getString("number_confident"));
                temPb.setOtherPtmSites(rs.getString("other_ptm_sites"));
                temPb.setNumberOfOther(rs.getString("number_other"));
                temPb.setNumberValidatedPeptides(rs.getInt("number_validated_peptides"));
                temPb.setNumberValidatedSpectra(rs.getInt("number_validated_spectra"));
                temPb.setEmPai(rs.getDouble("em_pai"));
                temPb.setNsaf(rs.getDouble("nsaf"));
                temPb.setMw_kDa(rs.getDouble("mw_(kDa)"));
                temPb.setScore(rs.getDouble("score"));
                temPb.setConfidence(rs.getDouble("confidence"));
                temPb.setStarred(Boolean.valueOf(rs.getString("starred")));
                temPb.setNonEnzymaticPeptides(Boolean.valueOf(rs.getString("non_enzymatic_peptides").toUpperCase()));

                temPb.setSpectrumFractionSpread_lower_range_kDa(rs.getString("spectrum_fraction_spread_lower_range_kDa"));
                temPb.setSpectrumFractionSpread_upper_range_kDa(rs.getString("spectrum_fraction_spread_upper_range_kDa"));
                temPb.setPeptideFractionSpread_lower_range_kDa(rs.getString("peptide_fraction_spread_lower_range_kDa"));
                temPb.setPeptideFractionSpread_upper_range_kDa(rs.getString("peptide_fraction_spread_upper_range_kDa"));

                temPb.setGeneName(rs.getString("gene_name"));
                temPb.setChromosomeNumber(rs.getString("chromosome_number"));
                temPb.setValidated(Boolean.valueOf(rs.getString("valid")));
                temPb.setProtGroupId(rs.getInt("prot_group_id"));
                proteinsList.put(temPb.getProtGroupId(), temPb);

            }
            rs.close();
        } catch (SQLException sqlExcp) {
            System.err.println("at error line 1996 " + this.getClass().getName() + "   " + sqlExcp.getLocalizedMessage());
        }
        return proteinsList;

    }

}
