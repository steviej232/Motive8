package sjohns70.motive8;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.shephertz.app42.paas.sdk.android.App42API;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.game.Game;
import com.shephertz.app42.paas.sdk.android.game.GameService;
import com.shephertz.app42.paas.sdk.android.game.ScoreBoardService;

import org.json.JSONObject;

import java.math.BigDecimal;

/**
 * Created by Steven on 1/26/2017.
 */



public class App42Leaderboard extends AppCompatActivity{

    TextView text;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app42_leaderboard);
        FacebookSdk.sdkInitialize(getApplicationContext());

        text = (TextView)findViewById(R.id.text);
        //App42 API key, Secret Key
        String gameName = "Motive8";
        String description = "Facebook Leaderboard";
        App42API.initialize(getApplicationContext(), "15331199c2e7fb2bd752aa54c5ca8288d56c9b64e514c5860bef8d49a5bfae48",
                "0f4538245cf03a2f3fbafb9c380bfee4cebe5832971a72870d03ac99c2df5617");
        GameService gameService = App42API.buildGameService();
        ScoreBoardService scoreBoardService = App42API.buildScoreBoardService();

        gameService.createGame(gameName, description, new App42CallBack() {
            public void onSuccess(Object response)
            {
                Game game = (Game)response;
                System.out.println("gameName is " + game.getName());
                System.out.println("gameDescription is " + game.getDescription());
            }
            public void onException(Exception ex)
            {
                System.out.println("Exception Message"+ex.getMessage());
            }
        });
        String userName = "Steven";
        BigDecimal gameScore = new BigDecimal(3500);
        scoreBoardService.saveUserScore(gameName, userName, gameScore,new App42CallBack() {
            public void onSuccess(Object response)
            {
                Game game = (Game)response;
                System.out.println("Game Name is : "+game.getName());
                for(int i = 0;i<game.getScoreList().size();i++)
                {
                    System.out.println("userName is : " + game.getScoreList().get(i).getUserName());
                    System.out.println("score is : " + game.getScoreList().get(i).getValue());
                    System.out.println("scoreId is : " + game.getScoreList().get(i).getScoreId());
                    System.out.println("Created On is  :"+game.getScoreList().get(i).getCreatedOn());
                }
            }
            public void onException(Exception ex)
            {
                System.out.println("Exception Message"+ex.getMessage());
            }
        });

        scoreBoardService.getHighestScoreByUser(gameName, userName, new App42CallBack() {
            public void onSuccess(Object response)
            {
                Game game = (Game)response;
                System.out.println("Game Name is : "+game.getName());
                for(int i = 0;i<game.getScoreList().size();i++)
                {
                    System.out.println("userName is : " + game.getScoreList().get(i).getUserName());
                    System.out.println("highest score is : " + game.getScoreList().get(i).getValue());
                    System.out.println("scoreId is : " + game.getScoreList().get(i).getScoreId());
                    System.out.println("Created On is  :"+game.getScoreList().get(i).getCreatedOn());
                }
            }
            public void onException(Exception ex)
            {
                System.out.println("Exception Message"+ex.getMessage());
            }
        });


        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        System.out.println("AccessToken:)"+accessToken);

        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        // Application code
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link");
        request.setParameters(parameters);
        request.executeAsync();

        scoreBoardService.getTopNRankersFromFacebook(gameName, accessToken.getToken(), 10, new App42CallBack() {
            public void onSuccess(Object response)
            {
                Game game = (Game)response;
                System.out.println("Game Name is : "+game.getName());
                for(int i=0;i<game.getScoreList().size();i++)
                {
                    System.out.println("UserName is:"+game.getScoreList().get(i).getUserName());
                    System.out.println("Value is"+game.getScoreList().get(i).getValue());
                    System.out.println("Created On is: "+game.getScoreList().get(i).getCreatedOn());
                    System.out.println("ScoreId is: "+game.getScoreList().get(i).getScoreId());
                    System.out.println("id is :"+ game.getScoreList().get(i).getFacebookProfile().getId());
                    System.out.println("friend  name is "+ game.getScoreList().get(i).getFacebookProfile().getName());
                    System.out.println("picture url is "+ game.getScoreList().get(i).getFacebookProfile().getPicture());
                    Toast.makeText(getApplicationContext(),"Facebook",Toast.LENGTH_SHORT).show();
                }
            }
            public void onException(Exception ex)
            {
                System.out.println("Exception Message"+ex.getMessage());
            }
        });
    }
}
