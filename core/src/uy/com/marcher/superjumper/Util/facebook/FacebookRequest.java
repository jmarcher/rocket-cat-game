package uy.com.marcher.superjumper.Util.facebook;

import com.badlogic.gdx.utils.JsonValue;
import de.tomgrill.gdxfacebook.core.GDXFacebookCallback;
import de.tomgrill.gdxfacebook.core.GDXFacebookError;
import de.tomgrill.gdxfacebook.core.GDXFacebookGraphRequest;
import de.tomgrill.gdxfacebook.core.JsonResult;
import uy.com.marcher.superjumper.SuperJumper;

/**
 * Created by gordo on 09/03/16.
 */
public class FacebookRequest {

    public SuperJumper game;

    public FacebookRequest(SuperJumper game) {
        this.game = game;
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

                System.out.println("***** " + fbID);
                System.out.println("***** "+fbNickname);
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
}
