/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.listener;

import com.vaadin.server.VaadinSession;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import no.uib.probe.csf.pr.touch.database.DataBaseLayer;

/**
 *
 * @author y-mok
 */
public class CSFPR_ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String version = "";
        ServletContext scx = sce.getServletContext();
        String basepath = scx.getRealPath("/");        

//        File proteinsFile = new File(basepath + "VAADIN/prot-" + version + ".txt");
        File proteinsFile = new File(basepath + "VAADIN/csf_pr_available_prot_accs.txt");
        try {
            if (proteinsFile.exists()) {
                PrintWriter writer = new PrintWriter(proteinsFile);
                writer.print("");
                writer.close();
            }
//        else{
////            System.out.println("error: Create new file exist ");
////            proteinsFile.createNewFile();
//        }      

            initProteinsFile(scx, proteinsFile, version);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

    private void initProteinsFile(ServletContext scx, File file, String version) {
        String url = (scx.getInitParameter("url"));
        String dbName = (scx.getInitParameter("dbName"));
        String driver = (scx.getInitParameter("driver"));
        String userName = (scx.getInitParameter("userName"));
        String password = (scx.getInitParameter("password"));

        try {

            Class.forName(driver).newInstance();
            Connection conn = DriverManager.getConnection(url + dbName, userName, password);
            String selectIdProtQueryString = "SELECT `prot_accession` FROM `experiment_protein_table` where `valid` = 'TRUE' GROUP BY `prot_accession` ORDER BY `prot_accession`";
            if (conn == null) {
                System.out.println("at nconn is null ");
                return;
            }
            PreparedStatement selectIdProtQuery = conn.prepareStatement(selectIdProtQueryString);
            ResultSet rs = selectIdProtQuery.executeQuery();
            Set<String> protAccSet = new HashSet<>();
            while (rs.next()) {
                protAccSet.add((rs.getString("prot_accession")).trim());
            }
            rs.close();

            String selectquantProtQueryString = "SELECT  `uniprot_accession` FROM `quant_proteins_table` GROUP BY `uniprot_accession` ORDER BY `uniprot_accession`"; //SELECT `uniprot_accession` FROM `quantitative_proteins_table` where `string_p_value` = 'Significant' GROUP BY `uniprot_accession` ORDER BY `uniprot_accession`

            PreparedStatement selectQuantProtQuery = conn.prepareStatement(selectquantProtQueryString);
            rs = selectQuantProtQuery.executeQuery();
            while (rs.next()) {
                protAccSet.add((rs.getString("uniprot_accession")).trim());
            }
            rs.close();
            PrintWriter out1;
            FileWriter outFile = new FileWriter(file, true);
            out1 = new PrintWriter(outFile);
            String title = "CSF-PR Protein Accession List (" + version + ")";
            out1.append(title);
            out1.append('\n');
            protAccSet.stream().map((acc) -> {
                out1.append(acc);
                return acc;
            }).forEachOrdered((_item) -> {
                out1.append('\n');
            });

            out1.flush();
            out1.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
