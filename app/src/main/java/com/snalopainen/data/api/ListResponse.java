package com.snalopainen.data.api;

import com.google.gson.annotations.SerializedName;

/**
 * @author snalopainen.
 */
public class ListResponse {
    protected int page;
    protected int pages;
    @SerializedName("per_page")
    protected int perPage;
    protected int total;

    public int getPage() {
        return page;
    }

    public int getPages() {
        return pages;
    }

    public int getPerPage() {
        return perPage;
    }

    public int getTotal() {
        return total;
    }

    public boolean shouldLoadMore() {
        return page < pages;
    }
}
