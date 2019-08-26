package com.lee.db.update;

import org.w3c.dom.Element;

/**
 * @author jv.lee
 * @date 2019/8/26.
 * @description xml updateDb节点
 */
public class UpdateDb {

    private String sql_rename;
    private String sql_create;
    private String sql_insert;
    private String sql_delete;

    public UpdateDb(Element element) {
        this.sql_rename = element.getElementsByTagName("sql_rename").item(0).getTextContent();
        this.sql_create = element.getElementsByTagName("sql_create").item(0).getTextContent();
        this.sql_insert = element.getElementsByTagName("sql_insert").item(0).getTextContent();
        this.sql_delete = element.getElementsByTagName("sql_delete").item(0).getTextContent();
    }

    public String getSql_rename() {
        return sql_rename;
    }

    public String getSql_create() {
        return sql_create;
    }

    public String getSql_insert() {
        return sql_insert;
    }

    public String getSql_delete() {
        return sql_delete;
    }
}
