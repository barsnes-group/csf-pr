package no.uib.probe.csf.pr.touch.logic.beans;

import java.io.Serializable;
import java.util.Map;

/**
 * This class is used for re-indexing datasets in the heat-map layout
 *
 * @author Yehia Farag
 */
public class QuantDSIndexes implements Serializable {

    /**
     * Dataset index value.
     */
    private int value;
    /**
     * Datasets map (dataset index in the database to dataset object).
     */
    private Map<Integer, QuantDataset> datasetMap;

    /**
     * Get datasets map (dataset index in the database to dataset object)
     *
     * @return datasetMap Datasets map (dataset index in the database to dataset
     * object)
     */
    public Map<Integer, QuantDataset> getDatasetMap() {
        return datasetMap;
    }

    /**
     * Set datasets map (dataset index in the database to dataset object)
     *
     * @param datasetMap Datasets map (dataset index in the database to dataset
     * object)
     */
    public void setDatasetMap(Map<Integer, QuantDataset> datasetMap) {
        this.datasetMap = datasetMap;
    }

    /**
     * Get indexing value
     *
     * @return value the re-indexing value.
     */
    public int getValue() {
        return value;
    }

    /**
     * Set indexing value
     *
     * @param value The re-indexing value.
     */
    public void setValue(int value) {
        this.value = value;
    }
}
