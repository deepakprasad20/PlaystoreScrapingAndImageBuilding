package com.playstore.playstore.Model;

import java.util.List;

public class CreativesBody {

    private String templateName;
    private String ctaText;
    private List<Integer> ctaTextColorList;
    private List<Integer> ctaBoxColorList;

    private String playstorename;
    private String title;
    private String iconUrl;
    private String[] screenshots;
    private String author;
    private int index;

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getCtaText() {
        return ctaText;
    }

    public void setCtaText(String ctaText) {
        this.ctaText = ctaText;
    }

    public List<Integer> getCtaTextColorList() {
        return ctaTextColorList;
    }

    public void setCtaTextColorList(List<Integer> ctaTextColorList) {
        this.ctaTextColorList = ctaTextColorList;
    }

    public List<Integer> getCtaBoxColorList() {
        return ctaBoxColorList;
    }

    public void setCtaBoxColorList(List<Integer> ctaBoxColorList) {
        this.ctaBoxColorList = ctaBoxColorList;
    }

    public String getPlaystorename() {
        return playstorename;
    }

    public void setPlaystorename(String playstorename) {
        this.playstorename = playstorename;
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

    public String[] getScreenshots() {
        return screenshots;
    }

    public void setScreenshots(String[] screenshots) {
        this.screenshots = screenshots;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
