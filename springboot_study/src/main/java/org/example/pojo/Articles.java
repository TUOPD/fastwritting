package org.example.pojo;

import java.math.BigInteger;
import java.time.LocalDate;

public class Articles {
    private BigInteger id;
    private String title;
    private String context;
    private LocalDate createtime;
    private LocalDate updatetime;

    public Articles(BigInteger id, String title, String context, LocalDate createtime, LocalDate updatetime) {
        this.id = id;
        this.title = title;
        this.context = context;
        this.createtime = createtime;
        this.updatetime = updatetime;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public LocalDate getCreatetime() {
        return createtime;
    }

    public void setCreatetime(LocalDate createtime) {
        this.createtime = createtime;
    }

    public LocalDate getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(LocalDate updatetime) {
        this.updatetime = updatetime;
    }
}
