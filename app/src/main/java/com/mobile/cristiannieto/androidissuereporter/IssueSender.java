package com.mobile.cristiannieto.androidissuereporter;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by cristiannieto on 8/1/17.
 */
public class IssueSender {

    private JSONObject getJson(String title, String description) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("title", title);
            jsonBody.put("body", description);
            return jsonBody;
        } catch (JSONException e) {
            Log.e("API", "Ocurrió un error con el formateo del JSON");
        }
        return null;
    }

    public String postApi(Context appContext, String title, String description) {
        JSONObject urlParameters = getJson(title, description);
        int timeout = 5000;
        URL url;
        HttpURLConnection connection = null;
        try {


            // Create connection
            url = new URL(getFileContent(R.raw.github_repo, appContext));
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/json");
            connection.setRequestProperty("Authorization",getFileContent(R.raw.github_token, appContext));
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);

            // Send request
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream());
            wr.writeBytes(urlParameters.toString());
            wr.flush();
            wr.close();

            // Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            Log.e("API",response.toString());
            return response.toString();

        } catch (SocketTimeoutException ex) {
            ex.printStackTrace();

        } catch (MalformedURLException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (connection != null) {
                connection.disconnect();
            }
        }
        Log.e("API", "Retorno Null");
        return null;
    }

    private String getFileContent(int rawFile, Context appContext) {
        //InputStream is = appContext.getResources().openRawResource(rawFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(appContext.getResources().openRawResource(rawFile)));
        try {
            String fileContent = reader.readLine();
            Log.e("RAW", fileContent);
            return fileContent;
        } catch (IOException e) {
            Log.e("RAW", e.toString());
        }
        return null;
    }

}
