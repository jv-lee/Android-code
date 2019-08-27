package com.gionee.gnservice.module.setting.push;

public class PushEntity {

    /**
     * act : 1
     * data : {"url":"https://www.baidu.com","title":"签到活动","vtype":1,"sign":false}
     */

    private int act;
    private String title;
    private String des;
    private DataBean data;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public int getAct() {
        return act;
    }

    public void setAct(int act) {
        this.act = act;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * url : https://www.baidu.com
         * title : 签到活动
         * vtype : 1
         * sign : false
         */

        private String url;
        private String title;
        private String activity;
        private String intent;
        private String scheme;
        private int vtype;
        private boolean sign;

        public String getActivity() {
            return activity;
        }

        public void setActivity(String activity) {
            this.activity = activity;
        }

        public String getIntent() {
            return intent;
        }

        public void setIntent(String intent) {
            this.intent = intent;
        }

        public String getScheme() {
            return scheme;
        }

        public void setScheme(String scheme) {
            this.scheme = scheme;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getVtype() {
            return vtype;
        }

        public void setVtype(int vtype) {
            this.vtype = vtype;
        }

        public boolean isSign() {
            return sign;
        }

        public void setSign(boolean sign) {
            this.sign = sign;
        }
    }
}
