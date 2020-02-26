/* Copyright 2013 Stephen Stacha
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.ubercode.ds.upload;

import io.ubercode.ds.config.ConnectionConfig;
import io.ubercode.ds.config.ConnectionHandler;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * User: sstacha
 * Date: Mar 5, 2013
 * handles parsing and uploading the connections provided in an xml file
 */
public class XMLConnectionUploader extends DefaultHandler {
    public static Logger log = Logger.getLogger(XMLConnectionUploader2.class);
    //    HttpServletRequest request;
    ConnectionConfig connection;
    StringBuilder characters = new StringBuilder(200);
    int imported = 0;
    int skipped = 0;

    @Override
    public void startElement(String namespace, String localName, String elementName, Attributes attributes) throws SAXException {
//        System.out.println("START ELEMENT:[" + namespace + "][" + localName + "][" + elementName + "]");
        if (elementName.equalsIgnoreCase("connections"))
            imported = 0;
        else if (elementName.equalsIgnoreCase("connection"))
            connection = new ConnectionConfig();
        else
            characters.setLength(0);
    }

    @Override
    public void endElement(String namespace, String localName, String elementName) throws SAXException {
//        System.out.println("END ELEMENT:[" + namespace + "][" + localName + "][" + elementName + "]");
//        System.out.println("END ELEMENT CHARS:" + characters.toString());
        if (elementName.equalsIgnoreCase("name"))
            connection.name = characters.toString().trim();
        else if (elementName.equalsIgnoreCase("type"))
            connection.type = characters.toString().trim();
        else if (elementName.equalsIgnoreCase("jndiContext"))
            connection.jndiContext = characters.toString().trim();
        else if (elementName.equalsIgnoreCase("jndiDatasource"))
            connection.jndiDatasource = characters.toString().trim();
        else if (elementName.equalsIgnoreCase("jdbcDriver"))
            connection.jdbcDriver = characters.toString().trim();
        else if (elementName.equalsIgnoreCase("jdbcUrl"))
            connection.jdbcUrl = characters.toString().trim();
        else if (elementName.equalsIgnoreCase("jdbcUserName"))
            connection.jdbcUserName = characters.toString().trim();
        else if (elementName.equalsIgnoreCase("jdbcPassword"))
            connection.jdbcPassword = characters.toString().trim();
        else if (elementName.equalsIgnoreCase("description"))
            connection.description = characters.toString().trim();
        else if (elementName.equalsIgnoreCase("connection")) {
            // using the current systems configurations save the data
            // for now just print it out
            log.info("    importing connection: " + connection.toString());
            boolean updated = false;
            try {updated = ConnectionHandler.updateConnection(connection);}
            catch (Exception ex) {System.out.println("got exception attempting to upload connection [" + connection.name + "]: " + ex);}
            if (updated)
                imported++;
            else
                skipped++;
        }
    }

    @Override
    public void characters(char[] buf, int start, int len) throws SAXException {
        //System.out.println("CHARACTERS buffer[" + Arrays.toString(buf) + "] start[" + start + "] len[" + len + "]");
        characters.append(buf, start, len);
    }

    public String getStatus() {
        return "imported [" + imported + "] records.\nskipped[" + skipped + "] records.";
    }

}
