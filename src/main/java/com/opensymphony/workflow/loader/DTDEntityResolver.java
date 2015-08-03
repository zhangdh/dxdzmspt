package com.opensymphony.workflow.loader;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class DTDEntityResolver
  implements EntityResolver
{
  public InputSource resolveEntity(String publicId, String systemId)
    throws SAXException, IOException
  {
    if (systemId == null)
      return null;

    try
    {
      URL url = new URL(systemId);
      String file = url.getFile();

      if ((file != null) && (file.indexOf(47) > -1)) {
        file = file.substring(file.lastIndexOf(47) + 1);
      }

      if (("".equals(url.getHost())) && (systemId.endsWith(".dtd"))) {
       InputStream is = super.getClass().getResourceAsStream("/META-INF/" + file);

        if (is == null) {
          is = super.getClass().getResourceAsStream('/' + file);
        }

        if (is != null)
          return new InputSource(is);
      }

      if ("www.opensymphony.com".equals(url.getHost()) && systemId.endsWith(".dtd")) {
          InputStream is = getClass().getResourceAsStream("/META-INF/" + file);

          if (is == null) {
              is = getClass().getResourceAsStream('/' + file);
          }

          if (is != null) {
              return new InputSource(is);
          }
      }
    }
    catch (MalformedURLException e)
    {
      InputStream is = super.getClass().getResourceAsStream("/META-INF/" + systemId);

      if (is == null) {
        is = super.getClass().getResourceAsStream('/' + systemId);
      }

      if (is != null)
        return new InputSource(is);

    }

     return null;
  }
}