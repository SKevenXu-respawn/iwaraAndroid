package com.sk.iwara.payload;

import java.util.List;

public class VideoPlayListPayload {

    /**
     * id : 8e4d932d-244e-4136-bb47-3ec259a8b088
     * name : 360
     * src : {"view":"//phoebe.iwara.tv/view?filename=c1895062-4696-4d76-abe4-072621029a8e_360.mp4&path=2025%2F10%2F03&expires=1759564195&hash=46346f800e16cf446a8c3873225721c014bd2fee743d6cf7ab5f44e89dbe05d4","download":"//phoebe.iwara.tv/download?filename=c1895062-4696-4d76-abe4-072621029a8e_360.mp4&path=2025%2F10%2F03&expires=1759564195&hash=46346f800e16cf446a8c3873225721c014bd2fee743d6cf7ab5f44e89dbe05d4"}
     * createdAt : 2025-10-03T13:07:08.000Z
     * updatedAt : 2025-10-03T13:07:08.000Z
     * type : video/mp4
     */

    private String id;
    private String name;
    private Src src;
    private String createdAt;
    private String updatedAt;
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Src getSrc() {
        return src;
    }

    public void setSrc(Src src) {
        this.src = src;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static class Src {
        /**
         * view : //phoebe.iwara.tv/view?filename=c1895062-4696-4d76-abe4-072621029a8e_360.mp4&path=2025%2F10%2F03&expires=1759564195&hash=46346f800e16cf446a8c3873225721c014bd2fee743d6cf7ab5f44e89dbe05d4
         * download : //phoebe.iwara.tv/download?filename=c1895062-4696-4d76-abe4-072621029a8e_360.mp4&path=2025%2F10%2F03&expires=1759564195&hash=46346f800e16cf446a8c3873225721c014bd2fee743d6cf7ab5f44e89dbe05d4
         */

        private String view;
        private String download;

        public String getView() {
            return view;
        }

        public void setView(String view) {
            this.view = view;
        }

        public String getDownload() {
            return download;
        }

        public void setDownload(String download) {
            this.download = download;
        }
    }
}
