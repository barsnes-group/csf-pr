package no.uib.probe.csf.pr.touch.selectionmanager;

/**
 * Filter interface that has the important abstracted methods
 *
 * @author Yehia Farag CSF
 */
public interface CSFListener {

    /**
     * Selection changed
     *
     * @param type of selection
     */
    public void selectionChanged(String type);

    /**
     * Get the listener id
     *
     * @return listener Id
     */
    public String getListenerId();

}
