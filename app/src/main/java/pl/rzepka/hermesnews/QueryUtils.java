package pl.rzepka.hermesnews;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    private static HttpURLConnection urlConnection = null;
    private static InputStream inputStream = null;

    private QueryUtils() {
    }

    public static List<Article> fetchArticlesFromServer(String apiUrl) {
        URL url = createUrl(apiUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException exception) {
            Log.e(LOG_TAG, "Problem making HTTP request", exception);
        }
        List<Article> articles = extractArticlesFromJson(jsonResponse);
        return articles;
    }

    private static List<Article> extractArticlesFromJson(String jsonResponse) {
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        List<Article> articles = new ArrayList<>();
        try {
            JSONObject jsonResponseObject = new JSONObject(jsonResponse);
            JSONObject jsonResults = jsonResponseObject.getJSONObject("response");
            JSONArray responseArray = jsonResults.getJSONArray("results");
            for (int i = 0; i < responseArray.length(); i++) {
                JSONObject article = responseArray.getJSONObject(i);
                String section = article.getString("sectionName");
                JSONObject fields = article.getJSONObject("fields");
                String title = fields.optString("headline");
                String trail = fields.optString("trailText");
                String author = fields.optString("byline");
                String published = fields.optString("firstPublicationDate");
                String url = fields.optString("shortUrl");
                Bitmap thumbnail = null;
                Log.v("thumbnail", fields.optString("thumbnail"));
                if (fields.optString("thumbnail") != "") {
                    try {
                        thumbnail = getThumbnail(fields.optString("thumbnail"));
                    } catch (IOException exception) {
                        Log.e(LOG_TAG, "Error getting thumbnail", exception);
                    }
                }
                if (thumbnail != null) {
                    articles.add(new Article(title, published, section, author, trail, thumbnail, url));
                }
            }
        } catch (JSONException exception) {
            Log.e(LOG_TAG, "Problem with parsing JSON", exception);
        }

        return articles;
    }

    public static TreeMap<String, String> fetchSectionsFromServer(String apiUrl) {
        URL url = createUrl(apiUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException exception) {
            Log.e(LOG_TAG, "Problem making HTTP request", exception);
        }
        TreeMap<String, String> sections = extractSectionsFromJson(jsonResponse);
        return sections;
    }

    private static TreeMap<String, String> extractSectionsFromJson(String jsonResponse) {
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        TreeMap<String, String> sections = new TreeMap<String, String>();
        try {
            JSONObject jsonResponseObject = new JSONObject(jsonResponse);
            JSONObject jsonResults = jsonResponseObject.getJSONObject("response");
            JSONArray responseArray = jsonResults.getJSONArray("results");
            for (int i = 0; i < responseArray.length(); i++) {
                JSONObject article = responseArray.getJSONObject(i);
                String section = article.getString("id");
                String sectionTitle = article.getString("webTitle");
                sections.put(section, sectionTitle);
            }
        } catch (JSONException exception) {
            Log.e(LOG_TAG, "Problem with parsing JSON", exception);
        }

        return sections;
    }


    private static URL createUrl(String inputUrl) {
        URL url = null;
        try {
            url = new URL(inputUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error building URL");
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error, response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException exception) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results", exception);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;

    }

    private static Bitmap getThumbnail(String downloadUrl) throws IOException {
        Bitmap thumbnail = null;
        if (downloadUrl == null) {
            return thumbnail;
        }
        try {
            URL url = createUrl(downloadUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            thumbnail = BitmapFactory.decodeStream(inputStream);
        } catch (IOException exception) {
            Log.e(LOG_TAG, "Error downloading thumbnail", exception);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return thumbnail;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
