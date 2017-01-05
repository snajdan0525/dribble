package com.snalopainen.ui.shots;

import android.util.SparseArray;
import android.util.SparseIntArray;

import com.snalopainen.data.app.App;
import com.snalopainen.data.models.Comment;
import com.snalopainen.data.models.Shot;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * @author snalopainen.
 */
public class OpenedShotController {
    private static OpenedShotController instance;

    private SparseArray<OnCommentsLoadedListener> callbacks = new SparseArray<OnCommentsLoadedListener>();
    private SparseArray<ArrayList<Comment>> comments = new SparseArray<ArrayList<Comment>>();
    private SparseArray<Shot> shots = new SparseArray<Shot>();
    private SparseIntArray pages = new SparseIntArray();

    public static OpenedShotController getInstance(int shotId, OnCommentsLoadedListener callback) {
        if (instance == null) {
            instance = new OpenedShotController();
        }
        instance.init(shotId, callback);
        return instance;
    }

    private void init(int shotId, OnCommentsLoadedListener callback) {
        callbacks.put(shotId, callback);

        Shot shot = shots.get(shotId);
        if (shot != null) {
            if (callback != null) {
                callback.onShotLoaded(shot);
            }
        } else {
            loadShot(shotId);
        }

        ArrayList<Comment> comments = this.comments.get(shotId);
        if (comments != null) {
            if (callback != null) {
                callback.onCommentsLoaded(true, comments);
            }
        } else {
            loadComments(shotId);
        }
    }

    public void loadMore(int shotId) {
        int page = 1;
        try {
            page = pages.get(shotId);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        page++;
        pages.put(shotId, page);
        loadComments(shotId);
    }

    private void loadComments(final int shotId) {
        int page = pages.get(shotId);
        if (page == 0) {
            page = 1;
        }
        pages.put(shotId, page);
        App.getClientApi().getDribbbleService().comments(shotId, page).enqueue(new Callback<ArrayList<Comment>>() {
            @Override
            public void onResponse(Call<ArrayList<Comment>> call, Response<ArrayList<Comment>> response) {
                ArrayList<Comment> commentsList = comments.get(shotId);
                if (commentsList != null) {
                    commentsList.addAll(response.body());
                } else {
                    comments.put(shotId, response.body());
                }
                if (callbacks.get(shotId) != null) {
                    callbacks.get(shotId).onCommentsLoaded(response.body().size() > 0, comments.get(shotId)); // TODO
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Comment>> call, Throwable t) {

            }
        });

    }

    private void loadShot(final int shotId) {
        App.getClientApi().getDribbbleService().shot(shotId).enqueue(new Callback<Shot>() {
            @Override
            public void onResponse(Call<Shot> call, Response<Shot> response) {
                shots.put(shotId, response.body());

                if (callbacks.get(shotId) != null) {
                    callbacks.get(shotId).onShotLoaded(response.body());
                    callbacks.get(shotId).onCommentsLoaded(true, comments.get(shotId));
                }
            }

            @Override
            public void onFailure(Call<Shot> call, Throwable t) {

            }
        });
    }

    public interface OnCommentsLoadedListener {
        void onShotLoaded(Shot shot);

        void onCommentsLoaded(boolean shouldLoadMore, ArrayList<Comment> comments);
    }
}
