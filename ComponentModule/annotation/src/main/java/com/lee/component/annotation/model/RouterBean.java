package com.lee.component.annotation.model;

import javax.lang.model.element.Element;

/**
 * @author jv.lee
 * @date 2019/7/11.
 * @description 路径对象信息存放
 */
public class RouterBean {

    /**
     * 路径信息作用域
     */
    public enum Type {
        /**
         * 作用在Activity上作为跳转
         */
        ACTIVITY,
        /**
         * 阔模块的业务接口
         */
        CALL
    }

    /**
     * 枚举类型
     */
    private Type type;

    /**
     * 类节点
     */
    private Element element;

    /**
     * 被@ARouter注解的 类对象
     */
    private Class<?> clazz;

    /**
     * 路由的组名
     */
    private String group;

    /**
     * 路由的地址
     */
    private String path;

    private RouterBean(Builder builder) {
        this.element = builder.element;
        this.path = builder.path;
        this.group = builder.group;
    }

    public RouterBean(Type type, Class<?> clazz, String path, String group) {
        this.type = type;
        this.clazz = clazz;
        this.path = path;
        this.group = group;
    }

    public static RouterBean create(Type type, Class<?> clazz, String path, String group) {
        return new RouterBean(type, clazz, path, group);
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Type getType() {
        return type;
    }

    public Element getElement() {
        return element;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public String getGroup() {
        return group;
    }

    public String getPath() {
        return path;
    }

    public final static class Builder {
        /**
         * 类节点
         */
        private Element element;

        /**
         * 路由的组名
         */
        private String group;

        /**
         * 路由的地址
         */
        private String path;

        public Builder setElement(Element element) {
            this.element = element;
            return this;
        }

        public Builder setGroup(String group) {
            this.group = group;
            return this;
        }

        public Builder setPath(String path) {
            this.path = path;
            return this;
        }

        public RouterBean build() {
            if (path == null || path.length() == 0) {
                throw new IllegalArgumentException("path 必填项为空，如：/app/MainActivity");
            }
            return new RouterBean(this);
        }
    }

    @Override
    public String toString() {
        return "RouterBean{" +
                "group='" + group + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
