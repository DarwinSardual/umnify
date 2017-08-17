package com.example.darwin.umnify.feed;

import java.util.HashMap;

/**
 * Created by NS-Darwin on 8/18/2017.
 */

public abstract class DataActionWrapper {

    private HashMap<String, String> textDataOutput = null;
    private HashMap<String, byte[]> fileDataOutput = null;
    private HashMap<String, String> textData = null;
    private HashMap<String, byte[]> fileData = null;

    private boolean doInput;
    private boolean doOuput;
    private boolean useCaches;

    public DataActionWrapper(HashMap<String, String> textDataOutput,
                             HashMap<String, byte[]> fileDataOutput,
                             boolean doInput, boolean doOuput,
                             boolean useCaches){

        this.textDataOutput = textData;
        this.fileDataOutput = fileData;
        this.doInput = doInput;
        this.doOuput = doOuput;
        this.useCaches = useCaches;
    }

    public HashMap<String, String> getTextData(){
        return textData;
    }

    public HashMap<String, byte[]> getFileData(){
        return fileData;
    }

    public boolean isDoInput() {
        return doInput;
    }

    public boolean isDoOuput() {
        return doOuput;
    }

    public boolean isUseCaches() {
        return useCaches;
    }

    public abstract void onResultAction();
}
