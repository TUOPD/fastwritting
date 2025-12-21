package org.example.pojo;

import java.math.BigInteger;
import java.time.LocalDate;

public class Articlenote {
    private BigInteger id;
    private String title;
    private LocalDate updatetime;

    public Articlenote(BigInteger id, String title, LocalDate updatetime) {
        this.id = id;
        this.title = title;
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

    public LocalDate getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(LocalDate updatetime) {
        this.updatetime = updatetime;
    }
}
