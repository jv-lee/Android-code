package com.lee.db.update;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jv.lee
 * @date 2019/8/26.
 * @description xml文件updateDb节点
 */
public class UpdateStep {

    private String versionFrom;
    private String versionTo;
    private List<UpdateDb> updateDbs;

    public UpdateStep(Element element) {
        versionFrom = element.getAttribute("versionFrom");
        versionTo = element.getAttribute("versionTo");
        this.updateDbs = new ArrayList<>();
        NodeList dbs = element.getElementsByTagName("updateDb");
        for (int i = 0; i < dbs.getLength(); i++) {
            Element ele = (Element) dbs.item(i);
            UpdateDb updateDb = new UpdateDb(ele);
            this.updateDbs.add(updateDb);
        }
    }

    public String getVersionFrom() {
        return versionFrom;
    }

    public String getVersionTo() {
        return versionTo;
    }

    public List<UpdateDb> getUpdateDbs() {
        return updateDbs;
    }
}
