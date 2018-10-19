package com.funnyAnimal.api;

/**
 * Created by 青峰 on 2017/9/17.
 */

public class CommentEntity {

    public String publishId;
    public String publishLogo;
    public String publishName;
    public String publishGender;
    public String description;
    public String timestamp;
    public ParentPublish parentPublish;

    public class ParentPublish {
        public String publishId;
        public String publishLogo;
        public String publishName;
        public String publishGender;
        public String description;
        public String timestamp;
    }
}
