package com.example.rtmisq;


import com.example.rtmisq.StaticData.CurrentUser;

import java.util.Objects;
import java.util.ResourceBundle;

public class GetConfigure {
    private String Language;
    public ResourceBundle PropertiesConnection(){
        setLanguage(Objects.requireNonNullElse(CurrentUser.getUserLanguage(), "English"));
        return ResourceBundle.getBundle("com/example/rtmisq/Config/"+Language);
    }

    public void setLanguage(String language) {
        Language = language;
    }
}
