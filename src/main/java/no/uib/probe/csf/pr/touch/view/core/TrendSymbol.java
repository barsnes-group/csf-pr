package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.ui.VerticalLayout;
import java.util.HashMap;

/**
 * This class represents trend symbol layouts (arrow-up,diamond and arrow-down)
 * used in protein trend line chart and spark line in quant protein table.
 *
 * @author Yehia Farag
 */
public class TrendSymbol extends VerticalLayout implements Comparable<TrendSymbol> {

    /**
     * Parameters map.
     */
    private final HashMap<String, Object> parameterMap;
    /**
     * The trend value(0:100% Increased,1:less than 100% Increased
     * ,2:Equal,3:less than 100% Decreased,4:100% Increased,5:Quantified on
     * peptide level, or 6:Quant information not available).
     */
    private Integer trend;

    /**
     * Constructor to initialize the main attributes and layout.
     *
     * @param trend the trend value(0:100% Increased,1:less than 100% Increased
     * ,2:Equal,3:less than 100% Decreased,4:100% Increased,5:Quantified on
     * peptide level, or 6:Quant information not available).
     */
    public TrendSymbol(int trend) {
        parameterMap = new HashMap<>();
        this.trend = trend;
        this.addStyleName("slowtransition");
        switch (trend) {
            case 0:
                this.setStyleName("arrow-up100");
                break;
            case 1:
                this.setStyleName("arrow-upless100");
                break;
            case 2:
                this.setStyleName("diamond");
                break;
            case 3:
                this.setStyleName("arrow-downless100");

                break;
            case 4:
                this.setStyleName("arrow-down100");

                break;
            case 5:
                this.setStyleName("darkgraydiamond");
                break;
            case 6:
                this.setStyleName("graydiamond");
                break;

        }
    }

    /**
     * Set trend symbol.
     *
     * @param trend the trend value(0:100% Increased,1:less than 100% Increased
     * ,2:Equal,3:less than 100% Decreased,4:100% Increased,5:Quantified on
     * peptide level, or 6:Quant information not available).
     */
    public void setTrend(int trend) {
        this.trend = trend;
    }

    /**
     * Add parameter
     *
     * @param name parameter name
     * @param value parameter value
     */
    public void addParam(String name, Object value) {
        parameterMap.put(name, value);

    }

    /**
     * Get parameter value
     *
     * @param paramName parameter key
     * @return parameter value.
     */
    public Object getParam(String paramName) {
        return parameterMap.get(paramName);
    }

    /**
     * Override compare to method (comparing based on trend
     *
     * @param o object to compare with
     * @return comparison results
     */
    @Override
    public int compareTo(TrendSymbol o) {
        int i = this.trend.compareTo(o.trend);
        return i;
    }

}
