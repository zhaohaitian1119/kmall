package com.kgc.kmall.bean;

import java.io.Serializable;

/**
 * @author shkstart
 * @create 2021-01-07 15:59
 */
public class PmsSearchCrumb implements Serializable {
    private String valueId;
    private String valueName;
    private String urlParam;

    public String getValueId() {
        return valueId;
    }

    public void setValueId(String valueId) {
        this.valueId = valueId;
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    public String getUrlParam() {
        return urlParam;
    }

    public void setUrlParam(String urlParam) {
        this.urlParam = urlParam;
    }
}
