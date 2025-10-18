package com.sk.iwara.payload;

import java.util.List;

/**
 * Created by 25140 on 2025/10/16 .
 */
public class TagPayload {

    /**
     * count : 140
     * limit : 32
     * page : 0
     * results : [{"id":"abigail_williams","type":"general","sensitive":false},{"id":"abracadabra","type":"general","sensitive":false},{"id":"absolute_duo","type":"general","sensitive":false},{"id":"accessory_download","type":"general","sensitive":false},{"id":"accidental_incest","type":"general","sensitive":false},{"id":"accidental_sex","type":"general","sensitive":false},{"id":"acerola","type":"general","sensitive":false},{"id":"acheron","type":"general","sensitive":false},{"id":"adachi_rei","type":"general","sensitive":false},{"id":"ada_wong","type":"general","sensitive":false},{"id":"addiction","type":"general","sensitive":false},{"id":"adios","type":"general","sensitive":false},{"id":"aerith_gainsborough","type":"general","sensitive":false},{"id":"aespa","type":"general","sensitive":false},{"id":"aether","type":"general","sensitive":false},{"id":"aether_gazer","type":"general","sensitive":false},{"id":"age_age_again","type":"general","sensitive":false},{"id":"age_difference","type":"general","sensitive":false},{"id":"aglaea","type":"general","sensitive":false},{"id":"ahegao","type":"general","sensitive":false},{"id":"ahri","type":"general","sensitive":false},{"id":"ai","type":"general","sensitive":false},{"id":"airani_iofiteen","type":"general","sensitive":false},{"id":"ai_art","type":"general","sensitive":false},{"id":"ai_generated","type":"general","sensitive":false},{"id":"ai_hoshino","type":"general","sensitive":false},{"id":"ajitani_hifumi","type":"general","sensitive":false},{"id":"akai_haato","type":"general","sensitive":false},{"id":"akamatsu_kaede","type":"general","sensitive":false},{"id":"akatsuki","type":"general","sensitive":false},{"id":"akatsuki_kirika","type":"general","sensitive":false},{"id":"akiha_ikebukuro","type":"general","sensitive":false}]
     */

    private int count;
    private int limit;
    private int page;
    private List<ResultsBean> results;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean {
        /**
         * id : abigail_williams
         * type : general
         * sensitive : false
         */

        private String id;
        private String type;
        private boolean sensitive;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public boolean isSensitive() {
            return sensitive;
        }

        public void setSensitive(boolean sensitive) {
            this.sensitive = sensitive;
        }
    }
}
