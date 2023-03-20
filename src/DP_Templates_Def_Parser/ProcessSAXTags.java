package DP_Templates_Def_Parser;

import org.xml.sax.Attributes;

/**
 *
 * @author E.M. van Doorn
 */

public interface ProcessSAXTags {
    public void startElement(String qName, Attributes attributes);
    public ProcessSAXTags endElement(String qName);
}

