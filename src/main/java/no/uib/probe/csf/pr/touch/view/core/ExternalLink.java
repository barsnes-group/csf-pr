package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Link;
import org.vaadin.teemu.VaadinIcons;

/**
 * This class represents link object with external resource.
 *
 * @author Yehia Farag
 */
public class ExternalLink extends Link implements Comparable<ExternalLink> {

    /**
     * The link caption.
     */
    private final String caption;

    /**
     * Compare to based on the text caption.
     *
     * @param externalLink another external link object.
     * @return comparing value
     *
     */
    @Override
    public int compareTo(ExternalLink externalLink) {
        return (ExternalLink.this.caption.compareTo(externalLink.caption));
    }

    /**
     * Constructor to initialise main attributes.
     *
     * @param caption The link caption
     * @param resource The link resource.
     */
    public ExternalLink(String caption, ExternalResource resource) {
        super(caption.replace("(unreviewed)", "<font  style='color: #92cefa !important; margin-left: 2px;'>" + VaadinIcons.FILE.getHtml() + "</font>").replace("(Unreviewed)", "<font  style='color: #92cefa !important; margin-left: 2px;'>" + VaadinIcons.FILE.getHtml() + "</font>").replace("(Entry Deleted)", " <font color:'blue'>" + VaadinIcons.FILE.getHtml() + "</font>").replace("(Entry deleted)", "&#x2A;").replace("(Deleted)", "&#x2A;").replace("(Not retrieved)", "&#x2A;"), resource);
        super.setCaptionAsHtml(true);
        this.caption = caption;
        ExternalLink.this.setPrimaryStyleName("tablelink");
        if (!caption.startsWith("IPI") && resource != null && !resource.getURL().equalsIgnoreCase("")) {
            ExternalLink.this.setTargetName("_blank");
            ExternalLink.this.addStyleName("underline");
        } else {
            super.setEnabled(false);

        }
    }

    /**
     * Override to return the text caption which is used for table exporting.
     *
     * @return the link caption.
     */
    @Override
    public String toString() {
        return caption;
    }

}
