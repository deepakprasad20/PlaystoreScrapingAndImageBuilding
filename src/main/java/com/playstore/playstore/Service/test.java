package com.playstore.playstore.Service;

import com.darkprograms.speech.translator.GoogleTranslate;

import java.io.IOException;

class Base{
    public Base(){
        System.out.println("Base Constructor Called.");
    }

    public void baseMethod(){
        System.out.println("Base Method Called.");
    }
}

class Derived extends Base{

    public Derived(){
        System.out.println("Derived Constructor Called.");
    }

    public void baseMethod(){
        System.out.println("Derived Method Called.");
    }
}


public class test {
    public static void main(String...k) throws IOException {
        System.out.println(GoogleTranslate.translate("zh-CN", "how are you"));
    }
}
