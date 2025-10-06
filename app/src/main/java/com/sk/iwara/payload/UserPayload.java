package com.sk.iwara.payload;

public class UserPayload {

    /**
     * user : {"id":"069276dc-ef0c-4675-a6d0-0611055412c3","name":"SKevenXu","username":"skevenxu","status":"active","role":"limited","followedBy":false,"following":false,"friend":false,"premium":false,"creatorProgram":false,"locale":"zh","seenAt":"2025-10-06T08:03:17.000Z","createdAt":"2025-10-02T04:45:33.000Z","updatedAt":"2025-10-02T04:46:28.000Z","email":"1990614908@qq.com"}
     */

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static class User {
        /**
         * id : 069276dc-ef0c-4675-a6d0-0611055412c3
         * name : SKevenXu
         * username : skevenxu
         * status : active
         * role : limited
         * followedBy : false
         * following : false
         * friend : false
         * premium : false
         * creatorProgram : false
         * locale : zh
         * seenAt : 2025-10-06T08:03:17.000Z
         * createdAt : 2025-10-02T04:45:33.000Z
         * updatedAt : 2025-10-02T04:46:28.000Z
         * email : 1990614908@qq.com
         */

        private String id;
        private String name;
        private String username;
        private String status;
        private String role;
        private boolean followedBy;
        private boolean following;
        private boolean friend;
        private boolean premium;
        private boolean creatorProgram;
        private String locale;
        private String seenAt;
        private String createdAt;
        private String updatedAt;
        private String email;
        private avatar avatar;

        public User.avatar getAvatar() {
            return avatar;
        }

        public void setAvatar(User.avatar avatar) {
            this.avatar = avatar;
        }

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

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public boolean isFollowedBy() {
            return followedBy;
        }

        public void setFollowedBy(boolean followedBy) {
            this.followedBy = followedBy;
        }

        public boolean isFollowing() {
            return following;
        }

        public void setFollowing(boolean following) {
            this.following = following;
        }

        public boolean isFriend() {
            return friend;
        }

        public void setFriend(boolean friend) {
            this.friend = friend;
        }

        public boolean isPremium() {
            return premium;
        }

        public void setPremium(boolean premium) {
            this.premium = premium;
        }

        public boolean isCreatorProgram() {
            return creatorProgram;
        }

        public void setCreatorProgram(boolean creatorProgram) {
            this.creatorProgram = creatorProgram;
        }

        public String getLocale() {
            return locale;
        }

        public void setLocale(String locale) {
            this.locale = locale;
        }

        public String getSeenAt() {
            return seenAt;
        }

        public void setSeenAt(String seenAt) {
            this.seenAt = seenAt;
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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
        public static class avatar {
            /**
             * id : 032c92f3-8ece-4534-9bac-6352ef4a0133
             * type : image
             * path : 2024/07/10
             * name : 032c92f3-8ece-4534-9bac-6352ef4a0133.png
             * mime : image/png
             * size : 641556
             * width : 828
             * height : 1021
             * duration : null
             * numThumbnails : null
             * animatedPreview : false
             * createdAt : 2024-07-10T15:19:17.000Z
             * updatedAt : 2024-07-10T15:19:26.000Z
             */

            private String id;
            private String type;
            private String path;
            private String name;
            private String mime;
            private int size;
            private int width;
            private int height;
            private boolean animatedPreview;
            private String createdAt;
            private String updatedAt;

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

            public String getPath() {
                return path;
            }

            public void setPath(String path) {
                this.path = path;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getMime() {
                return mime;
            }

            public void setMime(String mime) {
                this.mime = mime;
            }

            public int getSize() {
                return size;
            }

            public void setSize(int size) {
                this.size = size;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public boolean isAnimatedPreview() {
                return animatedPreview;
            }

            public void setAnimatedPreview(boolean animatedPreview) {
                this.animatedPreview = animatedPreview;
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
        }
    }
}
