package com.evozi.appsinspector;

/**
 *
 * @author sven
 */
public class Advertiser {
    public String ClassPrefix="com/evozi/appsinspector";
    public String ReferenceName="AppsInspector";
    public String ReferenceURL="http://www.google.com/q=Apps%20Inspector";
    public String[] ReferenceHosts={""};

    public Advertiser(String prefix, String name, String url, String[] hosts) {
       if(prefix!=null)
       {            
         ClassPrefix=prefix;
         ReferenceName=name;
         ReferenceURL=url;
         ReferenceHosts=hosts;
       }
    }
    
}
