
package com.devalkasundra.recipesearch.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class DataResponse {

    @SerializedName("href")
    private String mHref;
    @SerializedName("results")
    private List<Recipe> recipes;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("version")
    private Double mVersion;

    public String getHref() {
        return mHref;
    }

    public void setHref(String href) {
        mHref = href;
    }

    public List<Recipe> getResults() {
        return recipes;
    }

    public void setResults(List<Recipe> results) {
        recipes = results;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Double getVersion() {
        return mVersion;
    }

    public void setVersion(Double version) {
        mVersion = version;
    }

}
