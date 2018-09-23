package com.example.hahaha.Class;

/**
 * Created by 佳佳 on 9/23/2018.
 */

public class MapTools {
    private String toolName;
    private  int toolImageId;

    public  MapTools (String toolName,int toolsImageId) {
        this.toolImageId=toolsImageId;
        this.toolName=toolName;
    }

    public String getToolName() {
        return toolName;
    }

    public int getToolImageId() {
        return toolImageId;
    }
}
