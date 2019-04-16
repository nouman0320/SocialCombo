package pk.edu.nu.socialcombo;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.parent_holder, new SocialLoginFragment(), "SocialLoginFragment")
                .commit();

        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(getResources().getString(R.string.TWITTER_CONSUMER_KEY), getResources().getString(R.string.TWITTER_CONSUMER_SECRET)))
                .debug(true)
                .build();

        Twitter.initialize(config);



        setContentView(R.layout.activity_test);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("TEST", "ONACTIVITYRESULT");

        // Pass the activity result to the fragment, which will then pass the result to the login
        // button.
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.parent_holder);
        if(fragment == null){
            Log.d("TEST", "FRAGMENT IS NULL");
        }
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
            Log.d("TEST", "FRAGMENT IS NOT NULL");
        }
    }
}
