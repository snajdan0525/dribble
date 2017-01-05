package com.snalopainen.data.api;

import com.google.gson.annotations.SerializedName;
import com.snalopainen.data.models.Shot;

import java.util.ArrayList;
/**
 * @author snalopainen.
 */
public class ShotsResponse extends ListResponse {
    @SerializedName("shots")
    private ArrayList<Shot> shots;

    public ArrayList<Shot> getShots() {
        return shots;
    }
}
