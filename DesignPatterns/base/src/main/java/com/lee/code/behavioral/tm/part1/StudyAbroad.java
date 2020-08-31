package com.lee.code.behavioral.tm.part1;

/**
 * @author jv.lee
 * @date 2020/8/31
 * @description 模板方法设计模式-用例1
 */
public abstract class StudyAbroad {
    /**
     * 模板方法
     */
    public void templateMethod() {
        lookingForSchool(); //申请签证
        applyForEnrol();    //体检、订机票、准备行装
        applyForPassport(); //索取学校资料
        applyForVisa();     //入学申请
        readGoAbroad();     //办理因私出国护照、出境卡和公证
        arriving();         //抵达
    }

    public void applyForPassport() {
        System.out.println("三.办理因私出国护照、出境卡和公证：");
        System.out.println("  1）持录取通知书、本人户口簿或身份证向户口所在地公安机关申请办理因私出国护照和出境卡。");
        System.out.println("  2）办理出生公证书，学历、学位和成绩公证，经历证书，亲属关系公证，经济担保公证。");
    }

    public void applyForVisa() {
        System.out.println("四.申请签证：");
        System.out.println("  1）准备申请国外境签证所需的各种资料，包括个人学历、成绩单、工作经历的证明；个人及家庭收入、资金和财产证明；家庭成员的关系证明等；");
        System.out.println("  2）向拟留学国家驻华使(领)馆申请入境签证。申请时需按要求填写有关表格，递交必需的证明材料，缴纳签证。有的国家(比如美国、英国、加拿大等)在申请签证时会要求申请人前往使(领)馆进行面试。");
    }

    public void readGoAbroad() {
        System.out.println("五.体检、订机票、准备行装：");
        System.out.println("  1）进行身体检查、免疫检查和接种传染病疫苗；");
        System.out.println("  2）确定机票时间、航班和转机地点。");
    }

    /**
     * 索取学校资料
     */
    public abstract void lookingForSchool();

    /**
     * 入学申请
     */
    public abstract void applyForEnrol();

    /**
     * 抵达
     */
    public abstract void arriving();
}
