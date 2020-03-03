package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.ui.Label;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a bubble shape layout.
 *
 * @author Yehia Farag
 */
public class BubbleComponent extends Label implements Comparable<Label> {

     /**
     * Map of parameters to store component information.
     */
    private final Map<String, Object> parametersMap = new HashMap<>();

    /**
     * Constructor to initialize the main attributes.
     */
    public BubbleComponent() {
        this.setValue("  ");
        this.setStyleName("transparent");
        this.addStyleName("cycle");
    }

    /**
     * Add parameter to the component.
     *
     * @param key Parameter key.
     * @param value Parameter value.
     */
    public void setParam(String key, Object value) {
        parametersMap.put(key, value);
    }

    /**
     * Get Parameter value.
     *
     * @param key Parameter key.
     * @return parameter value.
     */
    public Object getParam(String key) {
        return parametersMap.get(key);
    }

    /**
     * Select component (update component CSS style)
     *
     * @param selectAction The component is selected.
     */
    public void select(boolean selectAction) {
        if (selectAction) {
            this.removeStyleName("unselectbubble");
        } else {
            this.addStyleName("unselectbubble");
        }
    }

    /**
     * Override compare to method to sort the small components based on the
     * width this function is important for sorting component on charts where
     * small components add over the bigger one.
     *
     * @param other The bubble component(to compare with).
     * @return comparison results
     */
    @Override
    public int compareTo(Label other) {
        if (this.getWidth() > other.getWidth()) {
            return -1;
        } else {
            return 1;
        }
    }

}
