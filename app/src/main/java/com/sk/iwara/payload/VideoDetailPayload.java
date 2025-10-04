package com.sk.iwara.payload;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoDetailPayload {

    /**
     * id : W3gzoDouRLdPeo
     * slug : cipher-x-march-7th
     * title : Cipher x March 7th 🔞💦
     * body : Watch the [SFX] No Background Music 7-minute video version and 4k quality here ⬇️
     **PATREON:** https://patreon.com/nofaced1
     **FANBOX:** https://mrnofaced1.fanbox.cc/

     SD: Lazymelon, Nyanify, Sixpluswan, and I made of my own ❤️
     * status : active
     * rating : ecchi
     * private : false
     * unlisted : false
     * thumbnail : 3
     * embedUrl : null
     * liked : false
     * numLikes : 1727
     * numViews : 38172
     * numComments : 9
     * file : {"id":"c73cd2e4-7546-44e5-9b04-aa8b9621efb8","type":"video","path":"2025/10/01","name":"c73cd2e4-7546-44e5-9b04-aa8b9621efb8.mp4","mime":"video/mp4","size":216851599,"width":null,"height":null,"duration":262,"numThumbnails":12,"animatedPreview":true,"createdAt":"2025-10-01T13:28:13.000Z","updatedAt":"2025-10-01T13:34:21.000Z"}
     * customThumbnail : null
     * user : {"id":"0b1d3cc1-2445-4479-9509-699789531511","name":"Mr. No Face","username":"mr_noface","status":"active","role":"user","followedBy":false,"following":false,"friend":false,"premium":false,"creatorProgram":true,"locale":null,"seenAt":"2025-10-04T05:02:45.000Z","avatar":{"id":"1af0a4ae-b00e-439e-926e-7f5a987b6b2a","type":"image","path":"2025/01/31","name":"1af0a4ae-b00e-439e-926e-7f5a987b6b2a.png","mime":"image/png","size":912689,"width":1024,"height":1024,"duration":null,"numThumbnails":null,"animatedPreview":false,"createdAt":"2025-01-31T12:05:39.000Z","updatedAt":"2025-01-31T12:05:42.000Z"},"createdAt":"2024-04-11T09:05:56.000Z","updatedAt":"2025-10-04T06:37:47.000Z"}
     * tags : [{"id":"cipher","type":"general","sensitive":false},{"id":"honkai_star_rail","type":"general","sensitive":false},{"id":"koikatsu","type":"category","sensitive":false},{"id":"march_7th","type":"general","sensitive":false}]
     * createdAt : 2025-10-01T13:34:21.000Z
     * updatedAt : 2025-10-04T06:45:42.000Z
     * fileUrl : https://files.iwara.tv/file/c73cd2e4-7546-44e5-9b04-aa8b9621efb8?expires=1759563965330&hash=cf9a8ce5e30102f06e2acd4652175bd030886d6702c03c52048e7d62a0627e30
     */

    private String id;
    private String slug;
    private String title;
    private String body;
    private String status;
    private String rating;
    @SerializedName("private")
    private boolean privateX;
    private boolean unlisted;
    private int thumbnail;
    private boolean liked;
    private int numLikes;
    private int numViews;
    private int numComments;
    private File file;
    private User user;
    private String createdAt;
    private String updatedAt;
    private String fileUrl;
    private List<Tags> tags;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public boolean isPrivateX() {
        return privateX;
    }

    public void setPrivateX(boolean privateX) {
        this.privateX = privateX;
    }

    public boolean isUnlisted() {
        return unlisted;
    }

    public void setUnlisted(boolean unlisted) {
        this.unlisted = unlisted;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public int getNumLikes() {
        return numLikes;
    }

    public void setNumLikes(int numLikes) {
        this.numLikes = numLikes;
    }

    public int getNumViews() {
        return numViews;
    }

    public void setNumViews(int numViews) {
        this.numViews = numViews;
    }

    public int getNumComments() {
        return numComments;
    }

    public void setNumComments(int numComments) {
        this.numComments = numComments;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public List<Tags> getTags() {
        return tags;
    }

    public void setTags(List<Tags> tags) {
        this.tags = tags;
    }

    public static class File {
        /**
         * id : c73cd2e4-7546-44e5-9b04-aa8b9621efb8
         * type : video
         * path : 2025/10/01
         * name : c73cd2e4-7546-44e5-9b04-aa8b9621efb8.mp4
         * mime : video/mp4
         * size : 216851599
         * width : null
         * height : null
         * duration : 262
         * numThumbnails : 12
         * animatedPreview : true
         * createdAt : 2025-10-01T13:28:13.000Z
         * updatedAt : 2025-10-01T13:34:21.000Z
         */

        private String id;
        private String type;
        private String path;
        private String name;
        private String mime;
        private int size;
        private int duration;
        private int numThumbnails;
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

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public int getNumThumbnails() {
            return numThumbnails;
        }

        public void setNumThumbnails(int numThumbnails) {
            this.numThumbnails = numThumbnails;
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

    public static class User {
        /**
         * id : 0b1d3cc1-2445-4479-9509-699789531511
         * name : Mr. No Face
         * username : mr_noface
         * status : active
         * role : user
         * followedBy : false
         * following : false
         * friend : false
         * premium : false
         * creatorProgram : true
         * locale : null
         * seenAt : 2025-10-04T05:02:45.000Z
         * avatar : {"id":"1af0a4ae-b00e-439e-926e-7f5a987b6b2a","type":"image","path":"2025/01/31","name":"1af0a4ae-b00e-439e-926e-7f5a987b6b2a.png","mime":"image/png","size":912689,"width":1024,"height":1024,"duration":null,"numThumbnails":null,"animatedPreview":false,"createdAt":"2025-01-31T12:05:39.000Z","updatedAt":"2025-01-31T12:05:42.000Z"}
         * createdAt : 2024-04-11T09:05:56.000Z
         * updatedAt : 2025-10-04T06:37:47.000Z
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
        private String seenAt;
        private Avatar avatar;
        private String createdAt;
        private String updatedAt;

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

        public String getSeenAt() {
            return seenAt;
        }

        public void setSeenAt(String seenAt) {
            this.seenAt = seenAt;
        }

        public Avatar getAvatar() {
            return avatar;
        }

        public void setAvatar(Avatar avatar) {
            this.avatar = avatar;
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

        public static class Avatar {
            /**
             * id : 1af0a4ae-b00e-439e-926e-7f5a987b6b2a
             * type : image
             * path : 2025/01/31
             * name : 1af0a4ae-b00e-439e-926e-7f5a987b6b2a.png
             * mime : image/png
             * size : 912689
             * width : 1024
             * height : 1024
             * duration : null
             * numThumbnails : null
             * animatedPreview : false
             * createdAt : 2025-01-31T12:05:39.000Z
             * updatedAt : 2025-01-31T12:05:42.000Z
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

    public static class Tags {
        /**
         * id : cipher
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
