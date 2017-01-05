package com.snalopainen.data.api;

import com.snalopainen.data.models.Comment;

import java.util.ArrayList;

/**
 * @author snalopainen.
 */
public class CommentsResponse extends ListResponse {
    private ArrayList<Comment> comments;

    public ArrayList<Comment> getComments() {
        return comments;
    }
}
