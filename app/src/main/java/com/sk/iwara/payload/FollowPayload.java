package com.sk.iwara.payload;

import java.util.List;

/**
 * Created by 25140 on 2025/10/20 .
 */
public class FollowPayload {

    /**
     * count : 6
     * limit : 6
     * page : 0
     * results : [{"id":48362764,"createdAt":"2025-10-02T04:53:48.000Z","user":{"id":"e35273ce-d396-480f-8beb-75e7d23b7b0c","name":"咕嘿嘿","username":"user72115","status":"active","role":"user","followedBy":false,"following":false,"friend":false,"premium":false,"creatorProgram":false,"locale":null,"seenAt":"2025-10-10T14:27:28.000Z","avatar":{"id":"b3b852c7-b619-4e3d-afe6-17ee37e9d635","type":"image","path":"pictures","name":"picture-72115-1492173638.png","mime":"image/png","size":636335,"width":null,"height":null,"duration":null,"numThumbnails":null,"animatedPreview":false,"createdAt":"2017-04-14T12:40:38.000Z","updatedAt":"2017-04-14T12:40:38.000Z"},"createdAt":"2016-08-25T12:35:00.000Z","updatedAt":"2025-10-18T12:12:57.000Z"}},{"id":48362743,"createdAt":"2025-10-02T04:52:39.000Z","user":{"id":"231611e9-5d38-4e07-8898-c364852d3a6a","name":"ADLER","username":"user2124413","status":"active","role":"user","followedBy":false,"following":false,"friend":false,"premium":true,"creatorProgram":true,"locale":null,"seenAt":"2025-10-18T12:52:51.000Z","avatar":{"id":"9ccb94a1-75ea-417d-98a7-92522dae75b7","type":"image","path":"2024/07/21","name":"9ccb94a1-75ea-417d-98a7-92522dae75b7.png","mime":"image/png","size":190165,"width":350,"height":341,"duration":null,"numThumbnails":null,"animatedPreview":false,"createdAt":"2024-07-21T10:39:16.000Z","updatedAt":"2024-07-21T10:39:20.000Z"},"createdAt":"2021-08-10T10:22:30.000Z","updatedAt":"2025-10-18T12:57:34.000Z"}},{"id":48362672,"createdAt":"2025-10-02T04:49:31.000Z","user":{"id":"a189fb91-8e88-49ad-9f22-958f9537dcc0","name":"NaStay","username":"nastay","status":"active","role":"user","followedBy":false,"following":false,"friend":false,"premium":false,"creatorProgram":true,"locale":null,"seenAt":"2025-10-15T03:48:00.000Z","avatar":{"id":"3c25d451-9806-40de-86fc-835c08d6d85f","type":"image","path":"2024/03/22","name":"3c25d451-9806-40de-86fc-835c08d6d85f.png","mime":"image/png","size":944199,"width":800,"height":800,"duration":null,"numThumbnails":null,"animatedPreview":false,"createdAt":"2024-03-22T06:59:13.000Z","updatedAt":"2024-03-22T06:59:14.000Z"},"createdAt":"2019-06-28T17:36:40.000Z","updatedAt":"2025-10-18T12:57:05.000Z"}},{"id":48362631,"createdAt":"2025-10-02T04:47:54.000Z","user":{"id":"fb64d622-fdb0-411d-b709-85085da6790e","name":"yerrrg","username":"gerrry","status":"active","role":"user","followedBy":false,"following":false,"friend":false,"premium":false,"creatorProgram":true,"locale":null,"seenAt":"2025-10-18T08:43:08.000Z","avatar":{"id":"c18e1e2e-e00a-4dcf-a2f6-f7798816b0bd","type":"image","path":"2024/04/23","name":"c18e1e2e-e00a-4dcf-a2f6-f7798816b0bd.jpg","mime":"image/jpeg","size":23146,"width":640,"height":640,"duration":null,"numThumbnails":null,"animatedPreview":false,"createdAt":"2024-04-23T09:10:47.000Z","updatedAt":"2024-04-23T09:10:49.000Z"},"createdAt":"2021-06-06T09:38:20.000Z","updatedAt":"2025-10-18T12:59:00.000Z"}},{"id":48362626,"createdAt":"2025-10-02T04:47:45.000Z","user":{"id":"6645caa3-633b-4ef5-aeee-43e61ee58edd","name":"LikeHugeB","username":"user3207206","status":"active","role":"user","followedBy":false,"following":false,"friend":false,"premium":false,"creatorProgram":true,"locale":null,"seenAt":"2025-10-15T07:47:28.000Z","avatar":{"id":"032c92f3-8ece-4534-9bac-6352ef4a0133","type":"image","path":"2024/07/10","name":"032c92f3-8ece-4534-9bac-6352ef4a0133.png","mime":"image/png","size":641556,"width":828,"height":1021,"duration":null,"numThumbnails":null,"animatedPreview":false,"createdAt":"2024-07-10T15:19:17.000Z","updatedAt":"2024-07-10T15:19:26.000Z"},"createdAt":"2022-08-02T15:21:09.000Z","updatedAt":"2025-10-18T12:30:43.000Z"}},{"id":48362612,"createdAt":"2025-10-02T04:47:17.000Z","user":{"id":"355dc03b-5441-4c39-afa0-f0e60e77cf02","name":"突突兔","username":"lsb20020723","status":"active","role":"user","followedBy":false,"following":false,"friend":false,"premium":false,"creatorProgram":true,"locale":null,"seenAt":"2025-10-17T04:43:09.000Z","avatar":{"id":"a836c9b6-3072-4cf5-9645-e09ceb47ebce","type":"image","path":"2025/03/21","name":"a836c9b6-3072-4cf5-9645-e09ceb47ebce.png","mime":"image/png","size":171482,"width":508,"height":502,"duration":null,"numThumbnails":null,"animatedPreview":false,"createdAt":"2025-03-21T17:01:31.000Z","updatedAt":"2025-03-21T17:01:35.000Z"},"createdAt":"2023-06-10T03:28:57.000Z","updatedAt":"2025-10-18T12:15:54.000Z"}}]
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
         * id : 48362764
         * createdAt : 2025-10-02T04:53:48.000Z
         * user : {"id":"e35273ce-d396-480f-8beb-75e7d23b7b0c","name":"咕嘿嘿","username":"user72115","status":"active","role":"user","followedBy":false,"following":false,"friend":false,"premium":false,"creatorProgram":false,"locale":null,"seenAt":"2025-10-10T14:27:28.000Z","avatar":{"id":"b3b852c7-b619-4e3d-afe6-17ee37e9d635","type":"image","path":"pictures","name":"picture-72115-1492173638.png","mime":"image/png","size":636335,"width":null,"height":null,"duration":null,"numThumbnails":null,"animatedPreview":false,"createdAt":"2017-04-14T12:40:38.000Z","updatedAt":"2017-04-14T12:40:38.000Z"},"createdAt":"2016-08-25T12:35:00.000Z","updatedAt":"2025-10-18T12:12:57.000Z"}
         */

        private int id;
        private String createdAt;
        private UserBean user;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean {
            /**
             * id : e35273ce-d396-480f-8beb-75e7d23b7b0c
             * name : 咕嘿嘿
             * username : user72115
             * status : active
             * role : user
             * followedBy : false
             * following : false
             * friend : false
             * premium : false
             * creatorProgram : false
             * locale : null
             * seenAt : 2025-10-10T14:27:28.000Z
             * avatar : {"id":"b3b852c7-b619-4e3d-afe6-17ee37e9d635","type":"image","path":"pictures","name":"picture-72115-1492173638.png","mime":"image/png","size":636335,"width":null,"height":null,"duration":null,"numThumbnails":null,"animatedPreview":false,"createdAt":"2017-04-14T12:40:38.000Z","updatedAt":"2017-04-14T12:40:38.000Z"}
             * createdAt : 2016-08-25T12:35:00.000Z
             * updatedAt : 2025-10-18T12:12:57.000Z
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
            private AvatarBean avatar;
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

            public AvatarBean getAvatar() {
                return avatar;
            }

            public void setAvatar(AvatarBean avatar) {
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

            public static class AvatarBean {
                /**
                 * id : b3b852c7-b619-4e3d-afe6-17ee37e9d635
                 * type : image
                 * path : pictures
                 * name : picture-72115-1492173638.png
                 * mime : image/png
                 * size : 636335
                 * width : null
                 * height : null
                 * duration : null
                 * numThumbnails : null
                 * animatedPreview : false
                 * createdAt : 2017-04-14T12:40:38.000Z
                 * updatedAt : 2017-04-14T12:40:38.000Z
                 */

                private String id;
                private String type;
                private String path;
                private String name;
                private String mime;
                private int size;
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
}
