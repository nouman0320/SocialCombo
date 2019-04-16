package pk.edu.nu.socialcombo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.util.Arrays;

//import com.twitter.sdk.android.core.identity.TwitterLoginButton;

public class SocialLoginFragment extends Fragment {

    TwitterLoginButton loginButton;

    LoginButton facebookLoginButton;

    Button fbLogoutBtn;
    Button twLogoutBtn;
    Button logoutAllBtn;

    Button continueBtn;

    TextView fbStatusTv;
    TextView twStatusTv;


    CallbackManager callbackManager;

    boolean fbLoggedIn = false;
    boolean twLoggedIn = false;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_social_login, container, false);

        fbLogoutBtn = view.findViewById(R.id.facebook_logout_button);
        twLogoutBtn = view.findViewById(R.id.twitter_logout_btn);
        logoutAllBtn = view.findViewById(R.id.logout_all_btn);

        loginButton = view.findViewById(R.id.loginButtonTwitter);

        continueBtn = view.findViewById(R.id.continueBtn);

        fbStatusTv = view.findViewById(R.id.facebook_status_tv);
        twStatusTv = view.findViewById(R.id.twitter_status_tv);


        if(AccessToken.getCurrentAccessToken() != null && AccessToken.isCurrentAccessTokenActive()){
            fbLoggedIn = true;
            fbStatusTv.setText("Yes");

        } else {
            fbStatusTv.setText("No");
            fbLoggedIn = false;

        }

        TwitterSession twitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession();

        if(twitterSession != null)
            Toast.makeText(getActivity(), "Twitter Sesssion Secret:\n"+ twitterSession.getAuthToken().secret+
                "\nTwitter Session Token: "+twitterSession.getAuthToken().token,
                Toast.LENGTH_LONG).show();


        if(twitterSession != null){
            twLoggedIn = true;
            twStatusTv.setText("Yes");
            loginButton.setEnabled(false);

        } else {
            twLoggedIn = false;
            twStatusTv.setText("No");
        }


        updateButtons();

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
            }
        });

        fbLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                fbStatusTv.setText("No");
                fbLoggedIn = false;
                updateButtons();
                Toast.makeText(getActivity(), "Logged out from facebook",
                        Toast.LENGTH_LONG).show();
            }
        });

        twLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwitterCore.getInstance().getSessionManager().clearActiveSession();
                twLoggedIn = false;
                twStatusTv.setText("No");
                loginButton.setEnabled(false);
                updateButtons();
                Toast.makeText(getActivity(), "Logged out from twitter",
                        Toast.LENGTH_LONG).show();
            }
        });


        logoutAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(twLoggedIn){
                    TwitterCore.getInstance().getSessionManager().clearActiveSession();
                    twLoggedIn = false;
                    twStatusTv.setText("No");
                    loginButton.setEnabled(false);
                    updateButtons();
                }

                if(fbLoggedIn){
                    LoginManager.getInstance().logOut();
                    fbStatusTv.setText("No");
                    fbLoggedIn = false;
                    updateButtons();
                }

                Toast.makeText(getActivity(), "Logged out",
                        Toast.LENGTH_LONG).show();
            }
        });


        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession for making API calls

                Log.d("ERROR", "Login Success");
                Toast.makeText(getActivity(), "Login Success",
                        Toast.LENGTH_LONG).show();

                TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();


                String token = authToken.token;
                String secret = authToken.secret;

                Log.d("TOKEN", token);
                Log.d("SECRET", secret);

                twLoggedIn = true;
                twStatusTv.setText("Yes");
                updateButtons();

                /*
                session = result.data;

                TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient(session);
                StatusesService statusesService = twitterApiClient.getStatusesService();


                TweetComposer.Builder builder = new TweetComposer.Builder(getContext())
                        .text("just setting up my Twitter Kit.");
                builder.show();*/


            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
                Log.d("ERROR", "Login Failed");
                Toast.makeText(getActivity(), "Login Failed",
                        Toast.LENGTH_LONG).show();

                twLoggedIn = false;
                twStatusTv.setText("No");
                updateButtons();
            }
        });


        // for facebook


        callbackManager = CallbackManager.Factory.create();

        String EMAIL = "email";

        facebookLoginButton = view.findViewById(R.id.loginButtonFacebook);
        facebookLoginButton.setReadPermissions(Arrays.asList(EMAIL));
        facebookLoginButton.setFragment(this);

        // Callback registration
        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Toast.makeText(getActivity(), "Facebook login success",
                        Toast.LENGTH_LONG).show();
                fbLoggedIn = true;
                fbStatusTv.setText("Yes");
                updateButtons();
            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(getActivity(), "Facebook login failed",
                        Toast.LENGTH_LONG).show();
                fbLoggedIn = false;
                fbStatusTv.setText("No");
                updateButtons();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code

                Log.e("Facebook", exception.toString());

                Toast.makeText(getActivity(), "Facebook login error",
                        Toast.LENGTH_LONG).show();

                fbLoggedIn = false;
                fbStatusTv.setText("No");

                updateButtons();
            }
        });

        AccessTokenTracker fbTracker;
        fbTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken accessToken, AccessToken accessToken2) {
                if (accessToken2 == null) {
                    Log.d("FB", "User Logged Out.");
                    fbLoggedIn = false;
                    fbStatusTv.setText("No");
                    updateButtons();
                }
            }
        };


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    void updateButtons(){
        if(fbLoggedIn){
            fbLogoutBtn.setEnabled(true);
        }
        else fbLogoutBtn.setEnabled(false);

        if(twLoggedIn) {
            twLogoutBtn.setEnabled(true);
            loginButton.setEnabled(false);
        }
        else {
            twLogoutBtn.setEnabled(false);
            loginButton.setEnabled(true);
        }

        if(!fbLoggedIn && !twLoggedIn)
            logoutAllBtn.setEnabled(false);
        else logoutAllBtn.setEnabled(true);
    }
}
