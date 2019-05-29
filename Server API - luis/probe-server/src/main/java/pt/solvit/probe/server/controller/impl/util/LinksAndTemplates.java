package pt.solvit.probe.server.controller.impl.util;

import java.util.HashMap;


public class LinksAndTemplates {

    private HashMap<String, HashMap> linksMap = new HashMap<>();  //_links
    private HashMap<String, HashMap> templatesMap = new HashMap<>();  //_templates


    public LinksAndTemplates(){
    }

    public LinksAndTemplates(String selfURI){
        this();

        HashMap<String, String> selfMap = new HashMap<>();
        selfMap.put("href", selfURI);
        linksMap.put("self", selfMap);
    }


    public void addLink(String resource, String href, String title){
        HashMap<String, String> resourceMap = new HashMap<>();
        resourceMap.put("href", href);
        resourceMap.put("title", title);

        linksMap.put(resource, resourceMap);
    }

    public void addTemplate(String template, String href, boolean templated, String title, String method){
        //maybe TODO
    }

    public HashMap<String, HashMap> getAllLinksAndTemplates(){
        HashMap<String, HashMap> ret = new HashMap<>();
        if (!linksMap.isEmpty())
            ret.put("_links", linksMap);
        if (!templatesMap.isEmpty())
            ret.put("_templates", templatesMap);

        return ret;
    }

    /*
    "_links": {
        "self": {
            "href": "/links"
        },
        "I'm something": {
            "href": "I'm the href"
            "title": "I'm the title"
        }
    }
    */

}
