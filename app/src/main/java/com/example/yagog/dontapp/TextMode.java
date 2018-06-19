package com.example.yagog.dontapp;

import com.google.firebase.database.Exclude;

public class TextMode {
    private String key, tag, content;

    @Exclude
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public TextMode(String tag, String content) {
        setTag(tag);
        setContent(content);

    }

    public TextMode(String key, String tag, String content){
        this(tag, content);
        setKey(key);
    }
    public TextMode(){

    }

    @Override
    public String toString() {
        return "TextMode{" +
                "key='" + key + '\'' +
                ", tag='" + tag + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
