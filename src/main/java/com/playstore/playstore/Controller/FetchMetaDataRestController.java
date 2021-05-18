package com.playstore.playstore.Controller;


import com.playstore.playstore.Model.AppModel;
import com.playstore.playstore.Model.CreativesBody;
import com.playstore.playstore.Model.PlaystoreBody;
import com.playstore.playstore.Service.PlaystoreService;
import org.json.simple.parser.ParseException;
import com.playstore.playstore.Mapper.AppModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


import java.io.IOException;

@RestController
@RequestMapping("/app")
public class FetchMetaDataRestController {

    private final PlaystoreService playstoreService;

    @Autowired
    public FetchMetaDataRestController(final PlaystoreService playstoreService){
        this.playstoreService = playstoreService;
    }

    @GetMapping("/{playstoreName}/{appId}")
    public ResponseEntity<Object> fetchMetaData(@PathVariable String playstoreName ,
                                                @PathVariable String appId ,
                                                @RequestBody PlaystoreBody playstoreBody,
                                                @RequestParam(required = false) String info ) throws IOException, ParseException {
        if(playstoreName.equals("apple")) {

            return playstoreService.fetchMetaDataForApple(playstoreBody,playstoreName,appId);
        }
        else if(playstoreName.equals("zhushou")){
            String url = "http://zhushou.360.cn/detail/index/soft_id/" + appId;
            return playstoreService.fetchMetaDataForZhushou(url,playstoreBody,playstoreName,appId);

        }
        else if(playstoreName.equals("tencent")){
            //Document doc = Jsoup.connect("https://android.myapp.com/myapp/detail.htm?apkName=" + appId + "&info=" +info).get();
            String url = "https://android.myapp.com/myapp/detail.htm?apkName=" + appId + "&info=" +info;
            return playstoreService.fetchMetaDataForTencent(url , playstoreBody,playstoreName,appId);
        }

        else if(playstoreName.equals("huawei")){
            //https%253A%252F%252Fappgallery.huawei.com%252F%2523%252Fapp%252FC103805375&accessId=&appid=C103805375&zone=&locale=en
            String url = "https://appgallery.huawei.com/#/app/" + appId;
            playstoreService.fetchMetaDataForHuawei(url , playstoreBody);
        }

        return ResponseEntity.accepted().build();
    }

    @RequestMapping(value = "/buildCreatives", method = {RequestMethod.GET}, produces = "application/json")
    public ResponseEntity<Object> buildCreatives(@RequestBody CreativesBody creativesBody) throws IOException {
        AppModel appModel = AppModelMapper.map(creativesBody);
        PlaystoreBody playstoreBody = AppModelMapper.mapForPlayStoreBody(creativesBody);
        return playstoreService.buildCreatives(appModel,playstoreBody,creativesBody.getIndex());
    }
}
