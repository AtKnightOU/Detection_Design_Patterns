package DP_Templates_Def_Parser;

import DP_Templates_Def.TDef_Edge;
import General.Global_Feedback;
import org.xml.sax.Attributes;

/**
 *
 * @author E.M. van Doorn
 */

public class EdgeElement implements ProcessSAXTags {

    private TDef_Edge edge;

    EdgeElement() {
    }

    @Override
    public void startElement(String qName, Attributes attributes) {
        checkAttribute(qName);

        edge = new TDef_Edge(attributes.getValue("node1"),
                attributes.getValue("node2"),
                attributes.getValue("type"));
    }

    @Override
    public ProcessSAXTags endElement(String qName) {
        checkAttribute(qName);

        return null;
    }

    private void checkAttribute(String qName) {
        if (!qName.equals("edge")) {
            Global_Feedback.showOrWite(0, "Unexpected tag in EdgeElement: " + qName);
            System.exit(1);
        }
    }

    public TDef_Edge getEdgeDefinition() {
        return edge;
    }
}
