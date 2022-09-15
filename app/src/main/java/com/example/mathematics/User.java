package com.example.mathematics;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

public class User implements Serializable {
    public static boolean isIsGuest() {
        return isGuest;
    }

    public static void setIsGuest(boolean isGuest) {
        User.isGuest = isGuest;
    }

    public static boolean isIsVolume() {
        return isVolume;
    }

    public static void setIsVolume(boolean isVolume) {
        User.isVolume = isVolume;
    }

    public static boolean isIsAdmin() {
        return isAdmin;
    }

    public static void setIsAdmin(boolean isAdmin) {
        User.isAdmin = isAdmin;
    }

    private static boolean isAdmin=false;
    private static boolean isVolume=true;
    private static boolean isGuest;
    private static User loggedUser;
    private String fullName;
    private String mail;
    private String phone;

    public static void getAdminStatus(FirebaseUser user){
        user.getIdToken(false).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
            @Override
            public void onSuccess(GetTokenResult getTokenResult) {
                Object isAdminObject = getTokenResult.getClaims().get("admin");
                if (isAdminObject != null) {
                    User.setIsAdmin( (Boolean) isAdminObject);
                }
                else{
                    User.setIsAdmin(false);


                }
            }
        });
    }
    public int getAvatarNumber() {
        return avatarNumber;
    }

    public void setAvatarNumber(int avatarNumber) {
        this.avatarNumber = avatarNumber;
    }

    private int avatarNumber;
    private int points = 0;

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
    }

    private boolean isMale;
    private String uid;

    public User() {}

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public static User getLoggedUser() {
        return loggedUser;
    }

    public static void setLoggedUser(User loggedUser,boolean isGuest) {
        User.loggedUser = loggedUser;
        User.isGuest=isGuest;
    }
    public static boolean addPoints(int points) {
        if (loggedUser == null)
            return false;
        loggedUser.points += points;
        if(!isIsGuest())
        {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference url = database.getInstance().getReference();
            DatabaseReference usersUrl = url.child("Users");
            DatabaseReference specificUser = usersUrl.child(loggedUser.uid);
            // save the user into the DB
            specificUser.setValue(loggedUser);

            for (int i = 1; i <=Constants.numberOfSteps ; i++) {
                int avatarPoints=expo(i);
                if (loggedUser.points >= avatarPoints && loggedUser.points - points < avatarPoints) {
                    return true;
                }
            }
        }

        return false;

    }
    public static int getLoggedUserPoints() {
        if (loggedUser == null)
            return 0;
        return loggedUser.getPoints();
    }
    public static int expo(int avatarNumber){
        return (int)Math.pow(Constants.step,1.5*avatarNumber);
    }
}
