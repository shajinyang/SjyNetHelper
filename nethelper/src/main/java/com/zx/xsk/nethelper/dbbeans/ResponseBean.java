package com.zx.xsk.nethelper.dbbeans;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 响应返回
 * Created by sjy on 2017/10/10.
 */

public class ResponseBean extends RealmObject {
    @PrimaryKey
    private long id;
    private String key;//url+传参作为键
    private String responseContent;//response内容
    private long updateTime;//更新时间
    private long overtime;//过期时间

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getResponseContent() {
        return responseContent;
    }

    public void setResponseContent(String responseContent) {
        this.responseContent = responseContent;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getOvertime() {
        return overtime;
    }

    public void setOvertime(long overtime) {
        this.overtime = overtime;
    }
}
