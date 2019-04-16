package pk.edu.nu.socialcombo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // hiding actionbar
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}


        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_parent_layout, new FeedFragment(), "FeedFragment")
                .commit();

        setContentView(R.layout.activity_main);
    }
}
