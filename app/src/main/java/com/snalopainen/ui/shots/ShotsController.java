package com.snalopainen.ui.shots;

import com.snalopainen.data.api.Api;
import com.snalopainen.data.app.App;
import com.snalopainen.data.models.Shot;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author snalopainen.
 */
public class ShotsController {
    private static ShotsController instance;

    private HashMap<String, OnShotsLoadedListener> callbacks = new HashMap<String, OnShotsLoadedListener>();
    private HashMap<String, ArrayList<Shot>> shots = new HashMap<String, ArrayList<Shot>>();
    private HashMap<String, Integer> pages = new HashMap<String, Integer>();

    public static ShotsController getInstance(String reference, OnShotsLoadedListener callback) {
        if (instance == null) {
            instance = new ShotsController();
        }
        instance.init(reference, callback);
        return instance;
    }

    private void init(String reference, OnShotsLoadedListener callback) {
        callbacks.put(reference, callback);
        if (shots.containsKey(reference)) {
            if (callback != null) {
                callback.onShotsLoaded(shots.get(reference));
            }
        } else {
            loadShots(reference);
        }
    }

    public void loadMore(String reference) {
        int page = 1;
        try {
            page = pages.get(reference);
        } catch (NullPointerException ignored) {
        }
        page++;
        pages.put(reference, page);
        loadShots(reference);
    }

    private void loadShots(final String reference) {
        if (!pages.containsKey(reference)) {
            pages.put(reference, 1);
        }
        App.getClientApi().getDribbbleService().shots(reference, pages.get(reference)).enqueue(new Callback<ArrayList<Shot>>() {
            @Override
            public void onResponse(Call<ArrayList<Shot>> call, Response<ArrayList<Shot>> response) {
                if (shots.containsKey(reference)) {
                    shots.get(reference).addAll(response.body());
                } else {
                    shots.put(reference, response.body());
                }
                if (callbacks.get(reference) != null) {
                    callbacks.get(reference).onShotsLoaded(shots.get(reference));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Shot>> call, Throwable t) {
                if (callbacks.get(reference) != null) {
                    callbacks.get(reference).onShotsError();
                }
            }
        });

    }

    public interface OnShotsLoadedListener {
        void onShotsLoaded(ArrayList<Shot> shots);

        void onShotsError();
    }
}
