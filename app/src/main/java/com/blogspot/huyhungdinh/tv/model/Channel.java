package com.blogspot.huyhungdinh.tv.model;

import java.io.Serializable;

/**
 * Created by HUNGDH on 5/8/2016.
 */
public class Channel implements Serializable{

    private int id;
    private String name;
    private String linkStream;
    private String linkImage;
    private int catalog;
    private boolean isFavorite;
    private String description;

    public Channel() {

    }

    public Channel(int id, String name, String linkStream, String linkImage, int catalog, boolean isFavorite, String description) {
        this.id = id;
        this.name = name;
        this.linkStream = linkStream;
        this.linkImage = linkImage;
        this.catalog = catalog;
        this.isFavorite = isFavorite;
        this.description = description;
    }

    public Channel(int id, String name, String linkStream, String linkImage, int catalog, int isFavorite, String description) {
        this.id = id;
        this.name = name;
        this.linkStream = linkStream;
        this.linkImage = linkImage;
        this.catalog = catalog;
        if (isFavorite == 0) {
            this.isFavorite = false;
        } else {
            this.isFavorite = true;
        }
        this.description = description;
    }

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

    public String getLinkStream() {
        return linkStream;
    }

    public void setLinkStream(String linkStream) {
        this.linkStream = linkStream;
    }

    public String getLinkImage() {
        return linkImage;
    }

    public void setLinkImage(String linkImage) {
        this.linkImage = linkImage;
    }

    public int getCatalog() {
        return catalog;
    }

    public void setCatalog(int catalog) {
        this.catalog = catalog;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
