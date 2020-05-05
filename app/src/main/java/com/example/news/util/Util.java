package com.example.news.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Xml;
import com.example.news.model.Item;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    public static final String RSS_URL = "http://www.ynet.co.il/Integration/StoryRss2.xml";
    public static final int APP_MODE = 1;
    public static final int WIDGET_MODE = 2;


    public static List<Item> parseFeed(InputStream inputStream, int mode) throws XmlPullParserException, IOException {
        String title = null;
        String link = null;
        String description = null;
        String date = null;
        String image = null;
        Bitmap bitmap = null;
        boolean isItem = false;
        List<Item> items = new ArrayList<>();

        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);

            xmlPullParser.nextTag();
            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                int eventType = xmlPullParser.getEventType();

                String name = xmlPullParser.getName();
                if(name == null)
                    continue;

                if(eventType == XmlPullParser.END_TAG) {
                    if(name.equalsIgnoreCase("item")) {
                        isItem = false;
                    }
                    continue;
                }

                if (eventType == XmlPullParser.START_TAG) {
                    if(name.equalsIgnoreCase("item")) {
                        isItem = true;
                        continue;
                    }
                }

                String result = "";
                if (xmlPullParser.next() == XmlPullParser.TEXT ) {
                    result = xmlPullParser.getText();
                    xmlPullParser.nextTag();
                }

                if (name.equalsIgnoreCase("title") && isItem) {
                    title = result;
                } else if (name.equalsIgnoreCase("link") && isItem) {
                    link = result;
                } else if (name.equalsIgnoreCase("description") && isItem) {
                    if(result.contains("<img src=")){
                        Matcher m = Pattern.compile("<img src='(.+?)'").matcher(result);

                        while(m.find()) {
                            image = m.group(1);
                            URL url = null;
                            try {
                                url = new URL(image);
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                            try {
                                bitmap = BitmapFactory.decodeStream(url.openConnection() .getInputStream());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }

                    }
                    int p = result.indexOf("</div>");
                    String d = result.substring(p+6) + ".";

                    description = d;
                } else if (name.equalsIgnoreCase("pubDate") && isItem) {
                    String t = result.substring(0, result.length()-9);
                    date = t;
                }

                if (title != null && link != null && description != null && date != null && image != null) {
                    if(isItem) {
                        Item item = new Item(title, description, link, date, bitmap);
                        items.add(item);
                    }

                    title = null;
                    link = null;
                    description = null;
                    date = null;
                    image = null;
                    bitmap = null;
                    isItem = false;

                    if(mode == WIDGET_MODE){
                        if(items.size() >= 20){
                            break;
                        }
                    }

                }
            }
            return items;

        } finally {
            inputStream.close();
        }
    }

    public static InputStream getInputStream(URL url){
        try{
            return url.openConnection().getInputStream();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
