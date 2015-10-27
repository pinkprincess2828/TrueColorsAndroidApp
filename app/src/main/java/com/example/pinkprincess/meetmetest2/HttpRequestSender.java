package com.example.pinkprincess.meetmetest2;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by pinkprincess on 25.10.15.
 */
public class HttpRequestSender implements HttpRequestInterface {

    private static String IpAdresse = "172.20.10.6";
    private static String port = "8087";
    private static URL requestUrl;
    //private Context callbackActivity;


    private static String getOtherUserLink = "meetmeserver/api/gps";
    private static String getUserMeetingLink = "meetmeserver/api/interact";
    private static String getTeamRanking = "meetmeserver/api/ranking/teamleaderboard";

    private String requestType;
    private Context mapsActivity;
    ArrayList<OtherUser> userArray;


    @Override
    public void doGetOtherUsers(Context context) {

        HttpResponseInterface activity = (HttpResponseInterface) context;
        new AsyncTask<String, String, ArrayList<OtherUser>>() {

            @Override
            protected ArrayList<OtherUser> doInBackground(String... params) {
                InputStream response = null;
                try {
                    requestUrl = new URL("http://"
                            + IpAdresse
                            + ":"
                            + port
                            + "/"
                            + getOtherUserLink
                            + "/"
                            + OwnUser.userName
                            + "/"
                            + OwnUser.ownLocation.latitude
                            + "/"
                            + OwnUser.ownLocation.longitude);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                if (requestUrl != null){
                    HttpURLConnection urlConnection = null;
                    try {
                        urlConnection = (HttpURLConnection) requestUrl.openConnection();
                        response = urlConnection.getInputStream();
                        if (response != null) {
                            ResponseImportierer mResponseImportierer = new ResponseImportierer(); //create new JSON Parser Object
                            try {
                                OwnUser.nearestUserArray = mResponseImportierer.readJsonStream(response); //store JSON Objects from JSON Array as OtherUser Objects in userArray
                            } catch (IOException e) {
                                e.printStackTrace();
                            }}
                        response.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        urlConnection.disconnect();
                    }
                    finally {
                        urlConnection.disconnect();
                    }


                }
                return OwnUser.nearestUserArray;
            }

            protected void onPostExecute(ArrayList<OtherUser> userArray) {
                HttpResponseInterface activity = (HttpResponseInterface) mapsActivity;
                activity.displayOtherUser(userArray);
            }

        }.execute();
    }

    @Override
    public void doGetUserMeeting(final Context context, final String otherUserName, final String verificationCode) {

        final HttpResponseInterface activity = (HttpResponseInterface) context;
        final String otherUser = otherUserName;
        final String code = verificationCode;
        new AsyncTask<String, Void, Boolean>(){

            @Override
            protected Boolean doInBackground(String... params) {
                Boolean validationResponse = false;
                InputStream response = null;
                try {
                    requestUrl = new URL("http://"
                            + IpAdresse
                            + ":"
                            + port
                            + "/"
                            + getUserMeetingLink
                            + "/"
                            + OwnUser.userName
                            + "/"
                            + otherUser
                            + "/"
                            + code);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                if (requestUrl != null){
                    HttpURLConnection urlConnection = null;
                    try {
                        urlConnection = (HttpURLConnection) requestUrl.openConnection();
                        response = urlConnection.getInputStream();
                        BufferedReader r = new BufferedReader(new InputStreamReader(response));
                        StringBuilder total = new StringBuilder();
                        String line;
                        while ((line = r.readLine()) != null) {
                            total.append(line);
                        }
                        if (total != null) {
                            String[] responseElements = new String[2];
                            int i = 0;
                            StringTokenizer seperator = new StringTokenizer(total.toString(),";");
                            while(seperator.hasMoreElements()) {
                                responseElements[i] = seperator.nextElement().toString();
                                i++;
                            }
                            validationResponse = Boolean.parseBoolean(responseElements[0]);
                            OwnUser.score = Integer.parseInt(responseElements[1]);
                        }
                        else {System.out.println("response is null!");}


                    } catch (IOException e) {
                        e.printStackTrace();}
                    finally {urlConnection.disconnect();}
                }
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                return validationResponse;
            }

            protected void onPostExecute(Boolean validationResponse){
                activity.userMeetingValidation(otherUser, validationResponse);
            }
            }.execute();
        }



    @Override
    public void doGetUserRanking(Context context) {



    }

    @Override
    public void doGetTeamRanking(Context context) {

        final HttpResponseInterface activity = (HttpResponseInterface) context;
        final String[][] responseStringArray = new String[2][2]; //currently only team german and not-german plus team scores
        new AsyncTask<String, String, String[][]>(){

            @Override
            protected String[][] doInBackground(String... params) {
                InputStream response = null;
                ArrayList<String[]> arrayListResponse = null;
                String[][] responseStringArray = null;
                try {
                    requestUrl = new URL("http://"
                            + IpAdresse
                            + ":"
                            + port
                            + "/"
                            + getTeamRanking);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                if (requestUrl != null){
                    HttpURLConnection urlConnection = null;
                    try {
                        urlConnection = (HttpURLConnection) requestUrl.openConnection();
                        response = urlConnection.getInputStream();
                        if (response != null) {
                            ResponseImportierer mResponseImportierer = new ResponseImportierer(); //create new JSON Parser Object
                            try {
                                 arrayListResponse = mResponseImportierer.<String[]>readJsonStream(response); //store JSON Objects from JSON Array as OtherUser Objects in userArray
                                responseStringArray = new String[arrayListResponse.size()][2];
                                for (int i = 0; i < arrayListResponse.size(); i++) {
                                    String[] current = arrayListResponse.get(i);
                                    responseStringArray[i][0] = current[0]; //nation
                                    responseStringArray[i][1] = current[1]; //score
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }}
                        response.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        urlConnection.disconnect();
                    }
                    finally {
                        urlConnection.disconnect();
                    }


                }
                return responseStringArray;
            }

            protected void onPostExecute(String[][] teamStatistics){
                activity.displayTeamRanking(teamStatistics);
            }
        }.execute();

    }

    @Override
    public void doGetFriends(Context context) {

    }


}
