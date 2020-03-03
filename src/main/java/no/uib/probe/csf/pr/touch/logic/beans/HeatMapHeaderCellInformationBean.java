package no.uib.probe.csf.pr.touch.logic.beans;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class provides all required information for drawing heat map cell.
 *
 * @author Yehia Farag
 */
public class HeatMapHeaderCellInformationBean implements Serializable {

    /**
     * Disease category short name (MS, AD, PD..etc).
     */
    private String shortDiseaseCategoryName;
    /**
     * Disease category name (MS, AD, PD..etc).
     */
    private String diseaseCategory;
    /**
     * The Disease sub group (currently used in the system).
     */
    private String diseaseGroupName;
    /**
     * Disease Style name at CSS file.
     */
    private String diseaseStyleName;
    /**
     * Disease HTML Color code.
     */
    private String diseaseHashedColor;
    /**
     * Disease sub group full name.
     */
    private String diseaseGroupFullName;
    /**
     * Disease sub group original name (from publication).
     */
    private String diseaseGroupOreginalName;

    private boolean collapseDiseseCategory;

    /**
     * Get disease sub group original name (from publication)
     *
     * @return diseaseGroupOreginalName Publication disease sub group name.
     */
    public String getDiseaseGroupOreginalName() {
        return diseaseGroupOreginalName;
    }

    /**
     * Set disease sub group original name (from publication)
     *
     * @param diseaseGroupOreginalName Publication disease sub group name.
     */
    public void setDiseaseGroupOreginalName(String diseaseGroupOreginalName) {
        this.diseaseGroupOreginalName = diseaseGroupOreginalName;
    }

    /**
     * Short disease category name (disease abbreviation)
     *
     * @return disease abbreviation
     */
    public String getShortDiseaseCategoryName() {
        return diseaseCategory.replace("Alzheimer's", "AD").replace("Multiple Sclerosis", "MS").replace("Parkinson's", "PD").replace("Amyotrophic Lateral Sclerosis", "ALS");
    }

    /**
     * Get disease category (MS,AD,PD...etc)
     *
     * @return diseaseCategory disease category name.
     */
    public String getDiseaseCategory() {
        return diseaseCategory;
    }

    /**
     * Set disease category (MS,AD,PD...etc)
     *
     * @param diseaseCategory Disease category name.
     */
    public void setDiseaseCategory(String diseaseCategory) {
        this.diseaseCategory = diseaseCategory;
    }

    /**
     * Get disease sub group (currently used in the system)
     *
     * @return diseaseGroupName disease group name.
     */
    public String getDiseaseGroupName() {
        return diseaseGroupName;
    }

    /**
     * Set disease sub group (currently used in the system)
     *
     * @param diseaseGroupName Disease group name.
     */
    public void setDiseaseGroupName(String diseaseGroupName) {
        this.diseaseGroupName = diseaseGroupName;
    }

    /**
     * Get disease style name (CSS name).
     *
     * @return diseaseStyleName CSS disease style name.
     */
    public String getDiseaseStyleName() {
        return diseaseStyleName;
    }

    /**
     * Set disease style name (CSS name)
     *
     * @param diseaseStyleName CSS disease style name.
     */
    public void setDiseaseStyleName(String diseaseStyleName) {
        this.diseaseStyleName = diseaseStyleName;
    }

    /**
     * Get disease category HTML color code
     *
     * @return diseaseHashedColor Disease HTML hashed color code.
     */
    public String getDiseaseHashedColor() {
        return diseaseHashedColor;
    }

    /**
     * Set disease category HTML color code
     *
     * @param diseaseHashedColor Disease HTML hashed color code.
     */
    public void setDiseaseHashedColor(String diseaseHashedColor) {
        this.diseaseHashedColor = diseaseHashedColor;
    }

    /**
     *Override toString to get the disease category and disease subgroup name
     * @return disease category and disease sub group name 
     */
    @Override
    public String toString() {
        return diseaseGroupName + "__" + diseaseCategory;
    }

    /**
     * Get disease sub group full name
     *
     * @return diseaseGroupFullName disease sub-group full name.
     */
    public String getDiseaseGroupFullName() {
        if (diseaseGroupFullName == null) {
            return diseaseGroupName;
        }
        return diseaseGroupFullName;
    }

    /**
     * Set disease sub group full name
     *
     * @param diseaseGroupFullName Disease sub-group full name.
     */
    public void setDiseaseGroupFullName(String diseaseGroupFullName) {
        this.diseaseGroupFullName = diseaseGroupFullName;
    }

    /**
     *Override equal method 
     * @param obj object to compare
     * @return  if ovects are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final HeatMapHeaderCellInformationBean other = (HeatMapHeaderCellInformationBean) obj;
        if (!Objects.equals(this.diseaseCategory, other.diseaseCategory)) {
            return false;
        }
        return Objects.equals(this.diseaseGroupName, other.diseaseGroupName);
    }

    /**
     *Override hash code of the object 
     * @return the new calculated hash code
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + Objects.hashCode(this.diseaseCategory);
        hash = 19 * hash + Objects.hashCode(this.diseaseGroupName);
        return hash;
    }

    /**
     *Check if the header is in collapsed mode
     * @return the header in collapsing mode
     */
    public boolean isCollapseDiseseCategory() {
        return collapseDiseseCategory;
    }

    /**
     *Set header of disease category to be collapsed
     * @param collapseDiseseCategory collapse the header
     */
    public void setCollapseDiseseCategory(boolean collapseDiseseCategory) {
        this.collapseDiseseCategory = collapseDiseseCategory;
    }

}
