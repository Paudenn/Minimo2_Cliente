package com.example.pruebaminimo2.models;

public class User {


    private static User userinstance;
    //Attributes
    public String login;
    public int public_repos;
    public int followers;
    public int following;
    public String followers_url;
    public String avatar_url;



    //------------Singleton--------------//
    public User(){}

    public static synchronized User getInstance(){
        if(userinstance == null) {
            userinstance = new User();
        }
        return userinstance;
    }

    public User(String login, int public_repos, int followers, int following, String followers_url, String avatar_url) {
        this.login = login;
        this.public_repos = public_repos;
        this.followers = followers;
        this.following = following;
        this.followers_url = followers_url;
        this.avatar_url = avatar_url;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getPublic_repos() {
        return public_repos;
    }

    public void setPublic_repos(int public_repos) {
        this.public_repos = public_repos;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public String getFollowers_url() {
        return followers_url;
    }

    public void setFollowers_url(String followers_url) {
        this.followers_url = followers_url;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }
}
