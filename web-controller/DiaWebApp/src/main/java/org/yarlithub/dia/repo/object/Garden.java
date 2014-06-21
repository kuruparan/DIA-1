package org.yarlithub.dia.repo.object;

/**
 * Project YIT DIA
 * Created by jaykrish on 5/25/14.
 */
public class Garden {
    private int id;
    private String gardenName;
    private String password;

    public Garden() {
        this.id = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGardenName() {
        return gardenName;
    }

    public void setGardenName(String gardenName) {
        this.gardenName = gardenName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
