package DP_Templates_Def_Parser;

/**
 *
 * @author E.M. van Doorn
 */


import DP_Templates_Def.TDef_Templates;
import General.Constants;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;

public class TemplatesParser {
    private String fileName;

    public TemplatesParser(String fn) {
        fileName = fn;
    }

    public TDef_Templates parse() {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        Constants constants = new Constants();
        // Contains many constants which are needed at many placesa.

        try {
            InputStream xmlInput;
            SAXParser saxParser = factory.newSAXParser();
            SAXHandler handler = new SAXHandler();

            xmlInput = new FileInputStream(fileName);
            saxParser.parse(xmlInput, handler);

            return handler.getTemplates();

        } catch (IOException | ParserConfigurationException | SAXException err) {
            err.printStackTrace(); // not necessary; is done automatically
            System.exit(1);
        }

        return null;
    }
}
