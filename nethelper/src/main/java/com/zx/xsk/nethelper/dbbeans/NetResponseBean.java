package com.zx.xsk.nethelper.dbbeans;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by sjy on 2017/10/12.
 */

@Entity
public class NetResponseBean {
    @Id
    private long id;
    private String key;//url+传参作为键
    private String responseContent;//response内容
    private long updateTime;//更新时间
    private long overtime;//过期时间

    @Generated(hash = 1982768171)
    public NetResponseBean(long id, String key, String responseContent,
            long updateTime, long overtime) {
        this.id = id;
        this.key = key;
        this.responseContent = responseContent;
        this.updateTime = updateTime;
        this.overtime = overtime;
    }

    @Generated(hash = 1443599972)
    public NetResponseBean() {
    }

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
