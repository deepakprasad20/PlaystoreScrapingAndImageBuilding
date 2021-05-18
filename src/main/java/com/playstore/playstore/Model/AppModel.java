package com.playstore.playstore.Model;

import java.util.Arrays;
import java.util.Objects;

public class AppModel {
    private String playstorename;
    private String title;
    private String iconUrl;
    private String description;
    private String[] screenshots;
    private String contentRating;
    private String numOfDownloads;
    private String genres;
    private String author;
    private String ratings;

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getScreenshots() {
        return screenshots;
    }

    public void setScreenshots(String[] screenshots) {
        this.screenshots = screenshots;
    }

    public String getContentRating() {
        return contentRating;
    }

    public void setContentRating(String contentRating) {
        this.contentRating = contentRating;
    }

    public String getNumOfDownloads() {
        return numOfDownloads;
    }

    public void setNumOfDownloads(String numOfDownloads) {
        this.numOfDownloads = numOfDownloads;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String getPlaystorename() {
        return playstorename;
    }

    public void setPlaystorename(String playstorename) {
        this.playstorename = playstorename;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppModel appModel = (AppModel) o;
        return Objects.equals(title, appModel.title) && Objects.equals(description, appModel.description) && Arrays.equals(screenshots, appModel.screenshots) && Objects.equals(contentRating, appModel.contentRating) && Objects.equals(numOfDownloads, appModel.numOfDownloads) && Objects.equals(genres, appModel.genres) && Objects.equals(author, appModel.author) && Objects.equals(ratings, appModel.ratings);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(title, description, contentRating, numOfDownloads, genres, author, ratings);
        result = 31 * result + Arrays.hashCode(screenshots);
        return result;
    }

    @Override
    public String toString() {
        return "AppModel{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", screenshots=" + Arrays.toString(screenshots) +
                ", contentRating='" + contentRating + '\'' +
                ", numOfDownloads=" + numOfDownloads +
                ", genres='" + genres + '\'' +
                ", author='" + author + '\'' +
                ", ratings=" + ratings +
                '}';
    }
}
