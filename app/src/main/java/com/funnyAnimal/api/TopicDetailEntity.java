package com.funnyAnimal.api;

import java.util.List;

/**
 * Created by 青峰 on 2017/9/17.
 */

public class TopicDetailEntity {

    public Say say;
    public UserInfo userInfo;

    public class Say {
        public String title;
        public String tid;
        public String uid;
        public String text;
        public List<String> imgs;
        public String timestamp;
        public String cityName;
        public String location;
    }
}
