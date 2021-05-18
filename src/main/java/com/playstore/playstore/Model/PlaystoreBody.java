package com.playstore.playstore.Model;

import java.util.List;

public class PlaystoreBody {
    private String templateName;
    private String ctaText;
    private List<Integer> ctaTextColorList;
    private List<Integer> ctaBoxColorList;

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
}
