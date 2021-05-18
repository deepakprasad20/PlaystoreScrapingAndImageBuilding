package com.playstore.playstore.Service;

import com.playstore.playstore.Mapper.AppModelMapper;
import com.playstore.playstore.Model.AppModel;
import com.playstore.playstore.Model.PlaystoreBody;
import com.playstore.playstore.Repository.StoreEntity;
import com.playstore.playstore.Repository.StoreRepository;
import com.aspose.psd.*;
import com.aspose.psd.Color;
import com.aspose.psd.Graphics;
import com.aspose.psd.Image;
import com.aspose.psd.Point;
import com.aspose.psd.Rectangle;
import com.aspose.psd.fileformats.png.PngColorType;
import com.aspose.psd.fileformats.psd.PsdImage;
import com.aspose.psd.fileformats.psd.layers.TextLayer;
import com.aspose.psd.fileformats.psd.layers.filllayers.FillLayer;
import com.aspose.psd.fileformats.psd.layers.fillsettings.IColorFillSettings;
import com.aspose.psd.imageoptions.PngOptions;
import com.darkprograms.speech.translator.GoogleTranslate;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.jsoup.nodes.Document;
import com.aspose.psd.fileformats.psd.layers.Layer;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class PlaystoreService {

    private static final Logger log = Logger.getLogger(PlaystoreService.class.getName());

    private CustomRestExceptionHandler customRestExceptionHandler;
    private StoreRepository storeRepository;

    @Autowired
    public PlaystoreService(final CustomRestExceptionHandler customRestExceptionHandler,
                            final StoreRepository storeRepository){
        this.customRestExceptionHandler=customRestExceptionHandler;
        this.storeRepository = storeRepository;
    }



    public ResponseEntity<Object> fetchMetaDataForApple(PlaystoreBody playstoreBody, String playstoreName, String appId) throws ParseException, IOException {

        Optional<StoreEntity> storeEntity = storeRepository.findByPlaystoreId(appId);
        if(storeEntity.isPresent()){
            log.info("Fetching Data from Database");
            AppModel appModel = AppModelMapper.map(storeEntity.get());
            return buildCreatives(appModel, playstoreBody, 1);
        }else {
            log.info("Fetching Data for Apple store.");
            String url = "https://itunes.apple.com/lookup?id=" + appId;
            Document doc = Jsoup.connect(url).get();
            log.info("Document Generation completed.");
            AppModel appModel = new AppModel();
            appModel.setPlaystorename(playstoreName);
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(doc.text());
            JSONArray resultJson = (JSONArray) parser.parse(json.get("results").toString());
            json = (JSONObject) parser.parse(resultJson.get(0).toString());

            //Setting title to app model.
            String title = json.get("trackName").toString();
            appModel.setTitle(title);

            //setting app icon url.
            Object icon = json.get("artworkUrl100");
            appModel.setIconUrl(icon.toString());

            //Accessing screenshots url.
            JSONArray screenshots = (JSONArray) json.get("screenshotUrls");
            String[] screenshotArr = new String[screenshots.size()];
            for (int i = 0; i < screenshots.size(); i++) {
                screenshotArr[i] = screenshots.get(i).toString();
            }
            appModel.setScreenshots(screenshotArr);

            //Setting description.
            Object description = json.get("description");
            appModel.setDescription(description.toString());

            //Setting ContentRating.
            String contentRating = json.get("trackContentRating").toString();
            appModel.setContentRating(contentRating);

            //Setting User Rating.
            String userRating = json.get("averageUserRating").toString().substring(0, 4);
            appModel.setRatings(userRating);

            //setting Author to app model.
            String author = json.get("sellerName").toString();
            appModel.setAuthor(author);
            log.info("---------------------------------");
            log.info("Data fetching completed");
            log.info("---------------------------------");
            storeRepository.save(AppModelMapper.map(appModel,appId));
            //for(int i = 1 ; i <= appModel.getScreenshots().length;i++) {
            return buildCreatives(appModel, playstoreBody, 1);
        }
        //}
        //return ResponseEntity.accepted().build();
    }


    public ResponseEntity<Object> buildCreatives(AppModel appModel, PlaystoreBody playstoreBody,int i) throws IOException {
        //TODO: Need to give proper path.
        log.info("Starting building creatives.");
        String dataDir = getDataDir();

        // Initialize License Instance
        License license = new License();
        // Call setLicense method to set license
        license.setLicense(dataDir +"Aspose.PSD.Java.lic");


        PngOptions pngOptions = new PngOptions();
        //pngOptions.setDefaultReplacementFont("Calibri Italic");
        //JpegOptions jpegOptions = new JpegOptions();
        //jpegOptions.setDefaultReplacementFont("Calibri Italic");
        PsdImage psdImage = (PsdImage) Image.load(dataDir + playstoreBody.getTemplateName());

        Map<String,Layer> layerMap = findLayer(psdImage);

        Layer layerToReplace = layerMap.get("logo");//findLayer("App Icon Layer", psdImage);

        //Setting Playstorename Name
        Layer playstoreName = layerMap.get("appstoreName");//findLayer("Playstore name",psdImage);
        if(null != playstoreName){
            TextLayer playStoreTextLayer = (TextLayer) playstoreName;
            String playstrName= appModel.getPlaystorename().substring(0,1).toUpperCase().concat(appModel.getPlaystorename().substring(1));
            playStoreTextLayer.updateText(playstrName,new Point(1300,4300),playStoreTextLayer.getFont().getSize()+5);
        }

        //Setting CTA Text

        Layer CTALayer = layerMap.get("ctaText");//findLayer("Install CTA _text",psdImage);
        if(null != CTALayer && CTALayer instanceof TextLayer){
            TextLayer txtlyr = (TextLayer) CTALayer;
            String innerText = txtlyr.getInnerText();
            if(innerText.length() <= playstoreBody.getCtaText().length()){
                //TODO: need to throw an exception for length of cta text;
                return customRestExceptionHandler.handleCtaError(HttpStatus.BAD_REQUEST,"Please provide proper length of CTA Text","CTA length too Long");
            }
            Color color = Color.fromArgb(playstoreBody.getCtaTextColorList().get(0),playstoreBody.getCtaTextColorList().get(1),playstoreBody.getCtaTextColorList().get(2));//getColor(playstoreBody.getCtaTextColor());
            if(!color.equals(Color.getEmpty())){
                txtlyr.updateText(playstoreBody.getCtaText(),txtlyr.getFont().getSize() + 10,color);
            }else {
                txtlyr.updateText(playstoreBody.getCtaText(), txtlyr.getFont().getSize() + 10);
            }
        }

        //setting developed by
        Layer developerLayer = layerMap.get("developerName");//findLayer("Developed by: John Doe",psdImage);
        if(null != developerLayer && developerLayer instanceof TextLayer){
            TextLayer developerTextLayer = (TextLayer) developerLayer;
            if(appModel.getPlaystorename().equalsIgnoreCase("zhushou")){
                developerTextLayer.updateText(appModel.getAuthor());
            }else {
                developerTextLayer.updateText("Developed By:" + appModel.getAuthor());
            }
        }

        //Setting app name:
        Layer appNameLayer = layerMap.get("appName");//findLayer("App name something",psdImage);
        if(null != appNameLayer && appNameLayer instanceof TextLayer){
            TextLayer appNameTextLayer = (TextLayer) appNameLayer;
            List<String> titleList = getTitleList(appModel.getTitle());
            String appName="";
            int count = 0;
            for(String word : titleList){
                count++;
                if(count%2==0){
                    appName = appName +" "+ word + "\r";
                }else {
                    appName = appName +" "+ word;
                }
            }
            if(titleList.size() == 3){
                appName = appName + "\r";
            }
            appNameTextLayer.updateText(appName,appNameTextLayer.getFont().getSize()-16);
        }


        //Replacing Logo of app.
        if (null != layerToReplace) {
            URL url = new URL(appModel.getIconUrl());
            InputStream inputStream = url.openStream();
            //FileInputStream inputStream = new FileInputStream(dataDir + playstoreBody.getNewImageName());
            //Image image = (Image) ImageIO.read(inputStream);
            Layer newLayer = new Layer(inputStream);
            Graphics graphics = new Graphics(layerToReplace);
            //graphics.clear(Color.getEmpty());
            graphics.drawImage(newLayer, new Rectangle(new Point(), new Size(Math.abs(layerToReplace.getWidth()), Math.abs(layerToReplace.getHeight()))));

        }

        //Adding screenshot:Base
        //Layer scrshotLayer = findLayer("app_screenshot_rectangle",psdImage);
        Layer scrshotLayer = layerMap.get("appImage");//findLayer("Base",psdImage);
        if (null != scrshotLayer) {
            log.info("Replacing Screenshot Image");
            URL url = new URL(appModel.getScreenshots()[i-1]);
            InputStream inputStream = url.openStream();
            BufferedImage image = ImageIO.read(url);
            //FileInputStream inputStream = new FileInputStream(dataDir + playstoreBody.getNewImageName());
            Layer newLayer = new Layer(inputStream);
            Graphics graphics = new Graphics(scrshotLayer);
            //graphics.clear(Color.getEmpty());
            Double aspectRatio = image.getWidth()/(double)image.getHeight();
            int height = (int)Math.round(scrshotLayer.getWidth()/aspectRatio);
            graphics.drawImage(newLayer, new Rectangle(new Point(), new Size(scrshotLayer.getWidth(), height)));
            log.info("Screenshot Image Replaced.");
        }

        //replacing playstore icon:playstore_rectangle
        Layer playstoreIconLayer = layerMap.get("appstoreIcon");//findLayer("Playstore_icon",psdImage);
        if(null != playstoreIconLayer){
            //URL url = new URL(appModel.getScreenshots()[i-1]);
            //InputStream inputStream = url.openStream();
            FileInputStream inputStream = new FileInputStream(dataDir + appModel.getPlaystorename()+".png");
            Layer newLayer = new Layer(inputStream);
            //Layer newLayer = new Layer(is);
            Graphics graphics = new Graphics(playstoreIconLayer);
            //graphics.clear(Color.getEmpty());
            graphics.drawImage(newLayer, new Rectangle(new Point(), new Size(Math.abs(playstoreIconLayer.getWidth()), Math.abs(playstoreIconLayer.getHeight()))));

        }


        //changing cta box color
        Layer ctaBoxLayer = layerMap.get("ctaColor");//findLayer("Install_cta_rect",psdImage);
        if(null != ctaBoxLayer && ctaBoxLayer instanceof FillLayer){
            Color color = Color.fromArgb(playstoreBody.getCtaBoxColorList().get(0),playstoreBody.getCtaBoxColorList().get(1),playstoreBody.getCtaBoxColorList().get(2));//getColor(playstoreBody.getCtaTextColor());
            FillLayer recLayer = (FillLayer) ctaBoxLayer;
            IColorFillSettings settings = (IColorFillSettings) recLayer.getFillSettings();
            settings.setColor(color);
            recLayer.setFillSettings(settings);
            recLayer.update();
        }



        File file = new File(dataDir + appModel.getTitle()+i+".png");
        String outputFileName = dataDir + appModel.getTitle()+i+".png";
        String loggerName = appModel.getTitle()+i+".png";
        if(file.exists()){
            int f = 1;
            file = new File(dataDir + appModel.getTitle()+i +"(" + f+")" +".png");
            while(file.exists()){
                f++;
                file = new File(dataDir + appModel.getTitle()+i +"(" + f+")" +".png");
            }
            outputFileName = dataDir + appModel.getTitle()+i +"(" + f+")" +".png";
            loggerName = appModel.getTitle()+i + "(" + f + ")" +".png";
        }
        log.info("Saving Creatives with name " + loggerName);
        pngOptions.setColorType(PngColorType.TruecolorWithAlpha);
        //psdImage.resize(720,1280);
        psdImage.save(new FileOutputStream(outputFileName));
        psdImage.save(new FileOutputStream(outputFileName), pngOptions);
        //psdImage.save(new FileOutputStream(dataDir + appModel.getTitle()+ i +".jpeg"), jpegOptions);
        log.info("Creatives Build Successfully.");
        psdImage.dispose();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private List<String> getTitleList(String title) {
        List<String> titleList  = new ArrayList<>();
        StringBuffer str = new StringBuffer();
        for (int i = 0; i < title.length(); i++)
        {
            if ((title.charAt(i) == ' ') && (title.charAt(i + 1) != ' '))
            {
             titleList.add(str.toString());
             str = new StringBuffer();
            }else{
                str.append(title.charAt(i));
            }
        }
        titleList.add(str.toString());
        return titleList;
    }


    public static Map<String,Layer>  findLayer(PsdImage image) {
        Map<String,Layer> layerMap = new HashMap<>();
        Layer[] layers = image.getLayers();
        for (Layer layer : layers) {
            /*if (layer.getName().equals(layerName)) {
                return layer;
            }*/
            layerMap.put(layer.getName(),layer);
        }
        return layerMap;
    }

    public static String getDataDir(){
        File dir = new File(System.getProperty("user.dir"));
        dir = new File(dir, "src");
        dir = new File(dir, "main");
        dir = new File(dir, "resources");
        dir = new File(dir , "Input");
        return dir.toString() + File.separator;
    }

    public ResponseEntity<Object> fetchMetaDataForZhushou(String url, PlaystoreBody playstoreBody, String playstoreName, String appId) throws IOException {
        Optional<StoreEntity> storeEntity = storeRepository.findByPlaystoreId(appId);
        if(storeEntity.isPresent()){
            log.info("Fetching Data from Database");
            AppModel appModel = AppModelMapper.map(storeEntity.get());
            return new ResponseEntity<>(HttpStatus.OK);//buildCreatives(appModel, playstoreBody, 1);
        }else {
            log.info("------------------------------------");
            log.info("Fetching Data from Zhushou store.");
            Connection connection = Jsoup.connect(url);
            connection.timeout(15 * 1000);
            Document doc = connection.get();//Jsoup.connect(url).get();
            AppModel appModel = new AppModel();
            appModel.setPlaystorename(playstoreName);
            //Translator translate = Translator.getInstance();
            //String text = translate.translate("Hello!", Language.ENGLISH, Language.ROMANIAN);

            appModel.setTitle(GoogleTranslate.translate("en", doc.select("div.app-name").first().text()));
            //app-logo
            appModel.setIconUrl(doc.select("div.app-logo").first().select("img").attr("src"));
            List<Node> arrList = doc.select("div.app-image").first().childNodes().stream().filter(e -> !e.toString().equals(" ")).collect(Collectors.toList());
            String[] scrnshtArray = new String[arrList.size()];
            for (int i = 0; i < arrList.size(); i++) {
                scrnshtArray[i] = ((Element) arrList.get(i)).select("img").attr("src");
            }
            appModel.setScreenshots(scrnshtArray);
            appModel.setDescription(doc.select("div.app-about-shot").first().text());
            appModel.setRatings(doc.select("div.app-detail").first().select("span").first().text());
            //*[@id="authority-tg"]/font/font
            appModel.setNumOfDownloads(doc.select("div.app-detail").first().select("span").get(1).text());
            //app-moreinfo

            appModel.setAuthor(GoogleTranslate.translate("en", doc.select("div.app-moreinfo").first().childNodes().get(3).childNodes().get(0).toString()));
            log.info("---------------------------------");
            log.info("Data fetching completed");
            log.info("---------------------------------");
            log.info("Saving data to Database");
            log.info("---------------------------------");
            storeRepository.save(AppModelMapper.map(appModel,appId));
            log.info("Saving data completed");
            log.info("---------------------------------");
            //System.out.println(doc.select("div.app-detail").first());
            //System.out.println(translate("zh-CN","en","你好"));
            //for(int i = 1 ; i <= appModel.getScreenshots().length ; i++){

            //}
            return new ResponseEntity<>(HttpStatus.OK);//buildCreatives(appModel, playstoreBody, 1);
        }
    }

    public ResponseEntity<Object> fetchMetaDataForTencent(String url, PlaystoreBody playstoreBody, String playstoreName, String appId) throws IOException {
        Optional<StoreEntity> storeEntity = storeRepository.findByPlaystoreId(appId);
        if(storeEntity.isPresent()){
            log.info("Fetching Data from Database");
            AppModel appModel = AppModelMapper.map(storeEntity.get());
            return new ResponseEntity<>(HttpStatus.OK);//buildCreatives(appModel, playstoreBody, 1);
        }else {
            log.info("------------------------------------");
            log.info("Fetching Data from Tencent store.");
            Connection connection = Jsoup.connect(url);
            connection.timeout(20 * 1000);
            Document doc = connection.get();
            AppModel appModel = new AppModel();
            //Translate translate = TranslateOptions.getDefaultInstance().getService();
            //*[@id="J_DetDataContainer"]/div/div[1]/div[2]/div[1]/div[1]/font/font
            // fetching app name
            appModel.setPlaystorename(playstoreName);
            appModel.setTitle(GoogleTranslate.translate("zh-cn", "en", doc.select("div.det-ins-data").select("div.det-name").select("div.det-name-int").first().text()));
            appModel.setIconUrl(doc.select("div.det-icon").first().select("img").first().attributes().get("src"));
            List<Node> arrList = doc.select("span.pic-turn-img-box").first().childNodes().stream().filter(e -> !e.toString().equals(" ")).collect(Collectors.toList());
            String[] scrshts = new String[arrList.size()];
            for (int i = 0; i < arrList.size(); i++) {
                scrshts[i] = ((Element) arrList.get(i)).select("img").attr("data-src");
            }
            //setting scrsht array to app model:
            appModel.setScreenshots(scrshts);
            //contentRatig is not available

            //fetching num of downloads
            appModel.setNumOfDownloads(doc.select("div.det-ins-num").first().text());
            //setting genre of app;
            appModel.setGenres(doc.select("div.det-type-box").first().getElementsByTag("a").first().text());
            appModel.setAuthor(GoogleTranslate.translate("en", doc.select("div.det-othinfo-data").get(2).text()));
            appModel.setRatings(doc.select("div.com-blue-star-num").first().text());
            String rating = doc.select("div.com-blue-star-num").first().text();
            log.info("---------------------------------");
            log.info("Data fetching completed");
            log.info("---------------------------------");
            log.info("---------------------------------");
            log.info("Saving data to database.");
            log.info("---------------------------------");
            storeRepository.save(AppModelMapper.map(appModel,appId));
            log.info("---------------------------------");
            log.info("Data saving completed");
            log.info("---------------------------------");
            //for(int i = 1 ; i <= appModel.getScreenshots().length ; i++){
            return new ResponseEntity<>(HttpStatus.OK);//buildCreatives(appModel, playstoreBody, 1);
            //}
            //return ResponseEntity.accepted().build();
        }
    }



    public void fetchMetaDataForHuawei(String url, PlaystoreBody playstoreBody) throws IOException {
        WebClient webClient = new WebClient();
        webClient.getOptions().setJavaScriptEnabled(true);
        HtmlPage page = webClient.getPage(url);
        System.out.println(page.getPage());
    }
}
