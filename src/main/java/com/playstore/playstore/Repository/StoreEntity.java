package com.playstore.playstore.Repository;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "store")
public class StoreEntity {

    @Id
    @Column(name = "playstoreId" , unique = true , nullable = false)
    private String playstoreId;
    @Column(name = "playstore_name")
    private String playstoreName;
    @Column(name = "title")
    private String title;
    @Column(name = "icon_url")
    private String iconUrl;
    @Column(name = "screenshot_url")
    private String screenshotUrl;
    @Column(name = "description")
    private String description;
    @Column(name = "content_rating")
    private String contentRating;
    @Column(name = "downloads")
    private String downloads;
    @Column(name = "genre")
    private String genre;
    @Column(name = "author")
    private String author;
    @Column(name = "ratings")
    private String rating;

    public StoreEntity() {
    }

    public String getPlaystoreId() {
        return playstoreId;
    }

    public void setPlaystoreId(String playstoreId) {
        this.playstoreId = playstoreId;
    }

    public String getPlaystoreName() {
        return playstoreName;
    }

    public void setPlaystoreName(String playstoreName) {
        this.playstoreName = playstoreName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContentRating() {
        return contentRating;
    }

    public void setContentRating(String contentRating) {
        this.contentRating = contentRating;
    }

    public String getDownloads() {
        return downloads;
    }

    public void setDownloads(String downloads) {
        this.downloads = downloads;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating){
        this.rating = rating;
    }

    public String getScreenshotUrl() {
        return screenshotUrl;
    }

    public void setScreenshotUrl(String screenshotUrl) {
        this.screenshotUrl = screenshotUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StoreEntity that = (StoreEntity) o;
        return Objects.equals(playstoreId, that.playstoreId) && Objects.equals(playstoreName, that.playstoreName) && Objects.equals(title, that.title) && Objects.equals(iconUrl, that.iconUrl) && Objects.equals(description, that.description) && Objects.equals(contentRating, that.contentRating) && Objects.equals(downloads, that.downloads) && Objects.equals(genre, that.genre) && Objects.equals(author, that.author) && Objects.equals(rating, that.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playstoreId, playstoreName, title, iconUrl, description, contentRating, downloads, genre, author, rating);
    }

    @Override
    public String toString() {
        return "StoreEntity{" +
                ", playstoreId='" + playstoreId + '\'' +
                ", playstoreName='" + playstoreName + '\'' +
                ", title='" + title + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", description='" + description + '\'' +
                ", contentRating='" + contentRating + '\'' +
                ", downloads='" + downloads + '\'' +
                ", genre='" + genre + '\'' +
                ", author='" + author + '\'' +
                ", rating='" + rating + '\'' +
                '}';
    }
}
