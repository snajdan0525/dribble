package com.snalopainen.ui.profile;

import android.util.SparseArray;
import android.util.SparseIntArray;

import com.snalopainen.data.api.Api;
import com.snalopainen.data.app.App;
import com.snalopainen.data.models.Shot;
import com.snalopainen.data.models.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * @author snalopainen.
 */
public class ProfileController {
    private static ProfileController instance;

    private User userProfile;
    private OnPlayerDataListener userCallback;
    private SparseArray<User> players = new SparseArray<User>();
    private SparseArray<ArrayList<Shot>> shots = new SparseArray<ArrayList<Shot>>();
    private SparseArray<OnPlayerDataListener> callbacks = new SparseArray<OnPlayerDataListener>();
    private SparseIntArray pages = new SparseIntArray();

    public static ProfileController getInstance(int playerId, OnPlayerDataListener callback) {
        if (instance == null) {
            instance = new ProfileController();
        }
        instance.init(playerId, callback);
        return instance;
    }

    public static ProfileController getInstance(String playerName, OnPlayerDataListener callback) {
        if (instance == null) {
            instance = new ProfileController();
        }
        instance.init(playerName, callback);
        return instance;
    }

    private void init(String playerName, OnPlayerDataListener callback) {
        userCallback = callback;

        if (userProfile != null) {
            if (userCallback != null) {
                userCallback.onPlayerReceived(userProfile);
            }
        } else {
            loadProfile(playerName);
        }
    }

    private void init(int playerId, OnPlayerDataListener callback) {
        callbacks.put(playerId, callback);

        if (players.get(playerId) != null) {
            if (callbacks.get(playerId) != null) {
                callbacks.get(playerId).onPlayerReceived(players.get(playerId));
            }
        } else {
            loadProfile(playerId);
        }

        ArrayList<Shot> shots = this.shots.get(playerId);
        if (shots != null) {
            if (callback != null) {
                callback.onShotsReceived(true, shots);
            }
        } else {
            loadShots(playerId);
        }
    }

    public void loadMore(int playerId) {
        int page = 1;
        try {
            page = pages.get(playerId);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        page++;
        pages.put(playerId, page);
        loadShots(playerId);
    }

    private void loadProfile(final String playerId) {
        App.getClientApi().getDribbbleService().userProfile(playerId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                userProfile = response.body();
                if (userCallback != null) {
                    userCallback.onPlayerReceived(response.body());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
                if (userCallback != null) {
                    userCallback.onPlayerError();
                }
            }
        });

    }

    private void loadProfile(final int playerId) {
        App.getClientApi().getDribbbleService().userProfile(playerId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (callbacks.get(playerId) != null) {
                    callbacks.get(playerId).onPlayerReceived(response.body());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                if (callbacks.get(playerId) != null) {
                    callbacks.get(playerId).onPlayerError();
                }
                t.printStackTrace();
            }
        });
    }

    private void loadShots(final int playerId) {
        int page = pages.get(playerId);
        if (page == 0) {
            page = 1;
        }
        pages.put(playerId, page);

        App.getClientApi().getDribbbleService().userShots(playerId, page).enqueue(new Callback<ArrayList<Shot>>() {
            @Override
            public void onResponse(Call<ArrayList<Shot>> call, Response<ArrayList<Shot>> response) {
                ArrayList<Shot> shotsList = shots.get(playerId);
                if (shotsList != null) {
                    shotsList.addAll(response.body());
                } else {
                    shots.put(playerId, response.body());
                }
                if (callbacks.get(playerId) != null) {
                    callbacks.get(playerId).onShotsReceived(response.body().size() > 0, shots.get(playerId)); // TODO
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Shot>> call, Throwable t) {

            }
        });

    }

    public interface OnPlayerDataListener {
        void onPlayerReceived(User user);

        void onPlayerError();

        void onShotsReceived(boolean shouldLoadMore, ArrayList<Shot> shots);
    }
}
