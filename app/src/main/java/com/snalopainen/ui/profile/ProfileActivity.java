package com.snalopainen.ui.profile;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.snalopainen.R;
import com.snalopainen.data.helpers.Utils;
import com.snalopainen.data.models.Shot;
import com.snalopainen.ui.SwipeBaseActivity;
import com.snalopainen.ui.shots.ShotsFragment;
/**
 * @author snalopainen.
 */
public class ProfileActivity extends SwipeBaseActivity
        implements ShotsFragment.OnShotClickedListener {
    public static final String PLAYER_ID = "player_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setLogo(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.activity_profile);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container,
                            ProfileFragment.newInstance(getIntent().getIntExtra(PLAYER_ID, 0)))
                    .commit();
        }
    }

    public void setTitle(String text) {
        getSupportActionBar().setTitle(text);
    }

    @Override
    public void onShotClicked(Shot shot) {
        Utils.openShot(this, shot);
    }
}
