package sjohns70.motive8;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Steven on 10/4/2016.
 */

@IgnoreExtraProperties
public class UserData {

    public String username;
    public String email;
    public int points_earned;
    public int count_remainder;
    public String password;

    public UserData() {
        // Default constructor required for calls to DataSnapshot.getValue(UserData.class)
    }

    public UserData(String email, String password) {
        // Default constructor required for calls to DataSnapshot.getValue(UserData.class)
        this.email = email;
        this.password = password;
    }

    public UserData(String username, String email, int points_earned, int count_remainder) {
        this.username = username;
        this.email = email;
        this.points_earned = points_earned;
        this.count_remainder = count_remainder;
    }

    public int getCount_remainder() {
        return count_remainder;
    }

    public void setCount_remainder(int count_remainder) {
        this.count_remainder = count_remainder;
    }

    public int getPoints_earned() {
        return points_earned;
    }

    public void setPoints_earned(int points_earned) {
        this.points_earned = points_earned;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
