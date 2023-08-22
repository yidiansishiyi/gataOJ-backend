package com.yidiansishiyi.gataoj.common;

public enum BucketName {
    Image("user");
    private String name;

    BucketName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
