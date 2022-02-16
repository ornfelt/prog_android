package com.heyletscode.ihavetofly.Model;


//Pokemon object model class
public class Pokemon {

    //variables
    int id;
    String name;
    String spriteUrl;
    String spriteShinyUrl;

    //constructor with required parameters
    public Pokemon(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Pokemon{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", spriteUrl='" + spriteUrl + '\'' +
                ", spriteShinyUrl='" + spriteShinyUrl + '\'' +
                '}';
    }

    //empty constructor
    public Pokemon(){}

    /* Getters and setters */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getspriteUrl() {
        return spriteUrl;
    }

    public void setspriteUrl(String spriteUrl) {
        this.spriteUrl = spriteUrl;
    }

    public String getspriteShinyUrl() {
        return spriteShinyUrl;
    }

    public void setspriteShinyUrl(String spriteShinyUrl) {
        this.spriteShinyUrl = spriteShinyUrl;
    }
}
