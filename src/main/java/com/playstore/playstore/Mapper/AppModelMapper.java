package com.playstore.playstore.Mapper;

import com.playstore.playstore.Model.AppModel;
import com.playstore.playstore.Model.CreativesBody;
import com.playstore.playstore.Model.PlaystoreBody;
import com.playstore.playstore.Repository.StoreEntity;

public class AppModelMapper {

    public static AppModel map(CreativesBody creativesBody){
        AppModel appModel = new AppModel();
        appModel.setPlaystorename(creativesBody.getPlaystorename());
        appModel.setAuthor(creativesBody.getAuthor());
        appModel.setTitle(creativesBody.getTitle());
        appModel.setIconUrl(creativesBody.getIconUrl());
        appModel.setScreenshots(creativesBody.getScreenshots());
        return appModel;
    }

    public static PlaystoreBody mapForPlayStoreBody(CreativesBody creativesBody){
        PlaystoreBody playstoreBody = new PlaystoreBody();
        playstoreBody.setTemplateName(creativesBody.getTemplateName());
        playstoreBody.setCtaText(creativesBody.getCtaText());
        playstoreBody.setCtaTextColorList(creativesBody.getCtaTextColorList());
        playstoreBody.setCtaBoxColorList(creativesBody.getCtaBoxColorList());
        return playstoreBody;
    }

    public static AppModel map(StoreEntity storeEntity) {
        AppModel appModel = new AppModel();
        appModel.setPlaystorename(storeEntity.getPlaystoreName());
        appModel.setTitle(storeEntity.getTitle());
        appModel.setIconUrl(storeEntity.getIconUrl());
        appModel.setScreenshots(storeEntity.getScreenshotUrl().split(","));
        appModel.setDescription(storeEntity.getDescription());
        appModel.setContentRating(storeEntity.getContentRating());
        appModel.setNumOfDownloads(storeEntity.getDownloads());
        appModel.setGenres(storeEntity.getGenre());
        appModel.setAuthor(storeEntity.getAuthor());
        appModel.setRatings(storeEntity.getRating());
        return appModel;
    }

    public static StoreEntity map(AppModel appModel , String appId){
        StoreEntity storeEntity = new StoreEntity();
        storeEntity.setPlaystoreId(appId);
        storeEntity.setPlaystoreName(appModel.getPlaystorename());
        storeEntity.setTitle(appModel.getTitle());
        storeEntity.setIconUrl(appModel.getIconUrl());
        String[] scrshtArr = appModel.getScreenshots();
        StringBuffer scrsht = new StringBuffer();
        for(int i = 0 ; i < scrshtArr.length ; i++){
            if(i < scrshtArr.length-1){
                scrsht.append(scrshtArr[i] + ",");
            } else{
                scrsht.append(scrshtArr[i]);
            }
        }
        storeEntity.setScreenshotUrl(scrsht.toString());
        storeEntity.setDescription(appModel.getDescription());
        storeEntity.setContentRating(appModel.getContentRating());
        storeEntity.setDownloads(appModel.getNumOfDownloads());
        storeEntity.setGenre(appModel.getGenres());
        storeEntity.setAuthor(appModel.getAuthor());
        storeEntity.setRating(appModel.getRatings());
        return storeEntity;
    }
}
