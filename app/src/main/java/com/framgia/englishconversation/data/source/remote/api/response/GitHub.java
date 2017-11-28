package com.framgia.englishconversation.data.source.remote.api.response;

import com.google.gson.annotations.SerializedName;

/**
 *
 */

public class GitHub {
    @SerializedName("signIn")
    private String mLogin;
    @SerializedName("blog")
    private String mBlog;
    @SerializedName("public_repos")
    private int mPublicRepos;

    public String getLogin() {
        return mLogin;
    }

    public void setLogin(String login) {
        mLogin = login;
    }

    public String getBlog() {
        return mBlog;
    }

    public void setBlog(String blog) {
        mBlog = blog;
    }

    public int getPublicRepos() {
        return mPublicRepos;
    }

    public void setPublicRepos(int publicRepos) {
        mPublicRepos = publicRepos;
    }

    @Override
    public String toString() {
        return "GitHub{"
                + "mLogin='"
                + mLogin
                + '\''
                + ", mBlog='"
                + mBlog
                + '\''
                + ", mPublicRepos="
                + mPublicRepos
                + '}';
    }
}
