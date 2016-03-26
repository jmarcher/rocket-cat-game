package uy.com.marcher.superjumper.Util.facebook;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import de.tomgrill.gdxfacebook.core.*;
import uy.com.marcher.superjumper.SuperJumper;
import uy.com.marcher.superjumper.Util.Constants;

/**
 * Created by gordo on 09/03/16.
 */
public class FacebookHelper {

    public SuperJumper game;

    public FacebookHelper(SuperJumper game) {
        this.game = game;
    }

    public void login(){
        Array<String> permissions = new Array<String>();
        permissions.add("email");
        permissions.add("public_profile");
        permissions.add("user_friends");

        game.facebook.signIn(SignInMode.READ, permissions, new GDXFacebookCallback<SignInResult>() {
            @Override
            public void onSuccess(SignInResult result) {
                doRequest();
            }

            @Override
            public void onError(GDXFacebookError error) {
                // Error handling
            }

            @Override
            public void onCancel() {
                // When the user cancels the login process
            }

            @Override
            public void onFail(Throwable t) {
                // When the login fails
            }
        });
    }

    public void getFriends(){
        GDXFacebookGraphRequest request = new GDXFacebookGraphRequest();
        request.setNode("me/friends");
        request.useCurrentAccessToken();

        game.facebook.newGraphRequest(request, new GDXFacebookCallback<JsonResult>() {
            @Override
            public void onSuccess(JsonResult result) {
                JsonValue jsonValue = result.getJsonValue();
                System.out.println(jsonValue.get("data").get(0).getString("name"));
            }

            @Override
            public void onError(GDXFacebookError error) {

            }

            @Override
            public void onFail(Throwable t) {

            }

            @Override
            public void onCancel() {

            }
        });
    }

    public void doRequest(){
        /* Building your request */
        GDXFacebookGraphRequest request = new GDXFacebookGraphRequest();
        request.setNode("me"); // call against the "me" node.
        request.useCurrentAccessToken(); // Set this if you want gdx-facebook to use the currently cached access token.


        /* Sending the request */
        game.facebook.newGraphRequest(request, new GDXFacebookCallback<JsonResult>() {

            @Override
            public void onSuccess(JsonResult result) {
                // Parse Facebook Graph API repsond
                JsonValue jsonValue = result.getJsonValue();
                // In a real project you should do some validation of the jsonValue. You never know what comes back :)
                String fbID = jsonValue.getString("id");
                String fbNickname = jsonValue.getString("name");
                registerUser(fbID,fbNickname);
            }

            @Override
            public void onError(GDXFacebookError error) {
                // Handle request errors here
            }

            @Override
            public void onCancel() {
                // Handle request cancels
            }

            @Override
            public void onFail(Throwable t) {
                // Handle request fails here
            }

        });
    }

    private void registerUser(String fbID, String fbNickname) {
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
        final Net.HttpRequest httpRequest = requestBuilder
                .newRequest()
                .method(Net.HttpMethods.POST)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept","*/*")
                .header("Cache-Control", "no-cache")
                .url(Constants.SERVER_URL+"/player")
                .content("id=" + fbID + "&facebookName="+fbNickname)
                .build();
        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                System.out.println(httpResponse.getResultAsString());
                System.out.println("Usuario registrado");
            }

            @Override
            public void failed(Throwable t) {

            }

            @Override
            public void cancelled() {

            }
        });

    }
}
