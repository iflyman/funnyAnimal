package com.funnyAnimal.api;

import java.util.List;

/**
 * Created by 青峰 on 2017/7/23.
 */

public class TopicEntity {
    public UserInfoSay userInfoSay;

    public class UserInfoSay{
        public Say say;
        public UserInfo userInfo;
    }

    public class Say{
        public String title;
        public String tid;
        public String uid;
        public String text;
        public List<String> imgs;
        public String timestamp;
        public String cityName;
        public String location;
        public String count;
    }
}
