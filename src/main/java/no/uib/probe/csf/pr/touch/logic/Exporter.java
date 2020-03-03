package no.uib.probe.csf.pr.touch.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Set;

/**
 * This class is responsible for creating protein tables files to export.
 *
 * @author Yehia Farag
 */
public class Exporter implements Serializable {

    /**
     * Proteins exporting with large datasets as csv format.
     *
     * @param accessionList protein accession list to be exported
     * @return file byte array
     */
    public byte[] expotProteinAccessionListToCSV(Set<String> accessionList) {
        
      //new File("CSF-PR Protein Accession List.csv");
        PrintWriter out1 = null;
        try {  
            File csvText = File.createTempFile("CSF-PR Protein Accession List", "csv");
            if (csvText.exists()) {
                System.err.println("delet file " + csvText.delete());
            }
            csvText.createNewFile();
            FileWriter outFile = new FileWriter(csvText, true);
            out1 = new PrintWriter(outFile);
            String title = "CSF-PR Protein Accession List";
            out1.append(title);
            out1.append('\n');
            String header = "Accession";
            out1.append(header);
            out1.append('\n');
            for (String acc : accessionList) {
                out1.append(acc);
                out1.append('\n');

            }

            out1.flush();
            out1.close();

            byte[] crunchifyByteStream = new byte[(int) csvText.length()];

            try (FileInputStream crunchifyInputStream = new FileInputStream(csvText)) {
                crunchifyInputStream.read(crunchifyByteStream);
            }
            csvText.delete();

            return crunchifyByteStream;
        } catch (Exception exp) {
            System.err.println(exp.getLocalizedMessage());
            if (out1 != null) {
                out1.flush();
                out1.close();
            }
        }
        return null;

    }

}
