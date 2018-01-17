package com.framgia.englishconversation.screen.crawler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.framgia.englishconversation.R;
import com.framgia.englishconversation.data.model.MediaModel;
import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.data.model.UserModel;
import com.framgia.englishconversation.data.source.callback.DataCallback;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRemoteDataSource;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRepository;
import com.framgia.englishconversation.data.source.remote.timeline.TimelineRemoteDataSource;
import com.framgia.englishconversation.data.source.remote.timeline.TimelineRepository;
import com.framgia.englishconversation.utils.Utils;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class CrawlerActivity extends AppCompatActivity {
    private static final String TAG = "CrawlerActivity";
    private static final String URL = "http://www.manythings.org/voa/scripts/";
    private TimelineRepository mRepository;
    private UserModel mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crawler);
        mRepository = new TimelineRepository(new TimelineRemoteDataSource());
        AuthenicationRepository authenicationRepository =
                new AuthenicationRepository(new AuthenicationRemoteDataSource());
        authenicationRepository.getCurrentUser(new DataCallback<FirebaseUser>() {
            @Override
            public void onGetDataSuccess(FirebaseUser data) {
                mUser = new UserModel(data);
                mUser.setUserName("English Conversation");
                getData();
            }

            @Override
            public void onGetDataFailed(String msg) {
                Log.d(TAG, "onGetDataFailed: " + msg);
            }
        });
    }

    private void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                crawler(URL);
            }
        }).start();
    }


    public void crawler(String url) {
        MediaModel media;
        TimelineModel timeline;

        try {
            Document doc = Jsoup
                    .connect(url).data("query", "Java")
                    .userAgent("Chrome").cookie("auth", "token")
                    .timeout(5000).post();
            Elements elements = doc
                    .getElementsByClass("list")
                    .get(1).children();
            if (elements.size() == 0) {
                Log.d(TAG, "crawler: data null");
            }
            for (Element e : elements) {
                Elements list_a = e.getElementsByTag("a");
                for (Element a : list_a) {
                    String href_a = a.attr("href");
                    Document doc2 = Jsoup
                            .connect(href_a)
                            .data("query", "Java")
                            .userAgent("Chrome")
                            .cookie("auth", "token")
                            .timeout(5000)
                            .post();

                    Elements element2s = doc2.getElementsByClass("list");
                    for (Element element2 : element2s) {
                        Elements list_a2 = element2.select("a");
                        Elements list_backup = new Elements();
                        for (Element a2 : list_a2) {
                            String href_a2 = a2.attr("href");
                            if (href_a2.length() > 3) {
                                String check_href_a2 = href_a2.substring(0, 4);
                                if (!check_href_a2.equals("http")) {
                                    list_backup.add(a2);
                                }
                            } else {
                                list_backup.add(a2);
                            }
                        }
                        for (Element a3 : list_backup) {
                            Document doc3 = Jsoup
                                    .connect(a.attr("href") + a3.attr("href"))
                                    .data("query", "Java")
                                    .userAgent("Chrome")
                                    .cookie("auth", "token")
                                    .timeout(5000)
                                    .post();
                            Elements list_div = doc3.getElementsByClass("col-md-7");
                            for (Element e3 : list_div) {
                                String href_mp3 = null;
                                for (Element script : e3.select("script")) {
                                    if (script.attr("src").length() == 0) {
                                        for (DataNode node : script.dataNodes()) {
                                            href_mp3 = node.getWholeData();
                                        }
                                        break;
                                    }
                                }
                                for (Element e4 : e3.select("center")) {
                                    e4.remove();
                                }
                                for (Element e5 : e3.select("script")) {
                                    e5.remove();
                                }
                                for (Element e6 : e3.select("small")) {
                                    e6.remove();
                                }
                                for (Element e7 : e3.select("br")) {
                                    e7.remove();
                                }
                                Elements e3_childrens = e3.children();
                                try {
                                    Element h1 = e3.select("h1").get(0);
                                    String h1_value = h1.text();
                                    String e3_elements = "";
                                    for (Element e3_children : e3_childrens) {
                                        e3_elements = e3_elements + e3_children.toString();
                                    }
                                    if (href_mp3 != null) {
                                        int href_mp3_length = href_mp3.length() - 3;
                                        href_mp3 = href_mp3.substring(11, href_mp3_length);
                                        href_mp3 = validateLink(href_mp3);
                                        List<MediaModel> medias = new ArrayList<>();
                                        media = new MediaModel();
                                        media.setName(h1_value);
                                        media.setType(MediaModel.MediaType.AUDIO);
                                        media.setUrl(href_mp3);
                                        medias.add(media);

                                        timeline = new TimelineModel();
                                        timeline.setContent(e3_elements);
                                        timeline.setMedias(medias);
                                        timeline.setCreatedUser(mUser);

                                        timeline.setCreatedAt(Utils.generateOppositeNumber(System.currentTimeMillis()));

                                        mRepository.createNewPost(timeline)
                                                .subscribe();
                                        Log.d(TAG, "post data: " + media.getUrl());
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }

                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String validateLink(String url) {
        url = url.replace("\"", "")
                .replace(" ", "")
                .toLowerCase();

        return url;
    }
}
