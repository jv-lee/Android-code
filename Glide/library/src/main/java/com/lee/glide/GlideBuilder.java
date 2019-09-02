package com.lee.glide;

/**
 * @author jv.lee
 * @date 2019/9/2.
 * @description
 */
class GlideBuilder {

    /**
     * 创建Glide
     *
     * @return
     */
    Glide build() {
        RequestManagerRetriever retriever = new RequestManagerRetriever();
        return new Glide(retriever);
    }
}
