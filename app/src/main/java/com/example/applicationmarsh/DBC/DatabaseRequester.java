package com.example.applicationmarsh.DBC;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.applicationmarsh.Activities.Insurances.CreateDroneActivity;
import com.example.applicationmarsh.Activities.LeftMenu.ProfileActivity;
import com.example.applicationmarsh.Activities.RegisterActivity;
import com.example.applicationmarsh.Activities.Claims.CreateCarActivity;
import com.example.applicationmarsh.Fragments.BottomMenu.ClaimsFragment;
import com.example.applicationmarsh.Fragments.BottomMenu.InsurancesFragment;
import com.example.applicationmarsh.R;
import com.example.applicationmarsh.Utilities.Entities.CarClaim;
import com.example.applicationmarsh.Utilities.Entities.DroneInsurance;
import com.example.applicationmarsh.Utilities.Global;
import com.example.applicationmarsh.Activities.LoginActivity;
import com.example.applicationmarsh.Activities.MainActivity;
import com.loopj.android.http.*;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;


import cz.msebera.android.httpclient.Header;

public class DatabaseRequester {

    private static AsyncHttpClient client;

    //Action enum
    public enum Action {
        Login, Register, ProfileChange, SelectClaim, RegisterCar, SelectInsurance, RegisterDrone
    }

    //Extract params from Map
    private static RequestParams initParams(final Map<String, String> reqParams, final Action action) {

        //Init params -> GET request on server
        RequestParams params = new RequestParams();

        //Add each param
        params.add("action", action.name());
        for (Map.Entry<String, String> entry : reqParams.entrySet()) {
            params.add(entry.getKey(), entry.getValue());
        }

        return params;
    }

    //LOGIN -> login server request
    public static void sendLoginRequest(final Map<String, String> reqParams, final Action action, final LoginActivity activity) {

        //Turn on loading bar
        Global.setLoadingVisible(activity.pb, true);

        //Init params
        RequestParams params = initParams(reqParams, action);

        //Create handler
        AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                try {
                    //Create JSON object from response
                    JSONObject jsonObject = new JSONObject(new String(response));

                    //Get response
                    switch (jsonObject.getString("response")) {
                        case "success":
                            //Init User Data
                            String email = jsonObject.getJSONObject("data").getString("email");
                            String firstname = jsonObject.getJSONObject("data").getString("firstname");
                            String lastname = jsonObject.getJSONObject("data").getString("lastname");
                            String passwordHash = jsonObject.getJSONObject("data").getString("passwordHash");
                            int policyNumber = jsonObject.getJSONObject("data").getInt("policyNumber");

                            //Create User object
                            Global.createUser(reqParams.get("username"), email, firstname, lastname, passwordHash, policyNumber);

                            //Start new activity -> Main
                            activity.startActivity(new Intent(activity, MainActivity.class));
                            break;
                        case "failure":
                            Toast.makeText(activity, activity.getString(R.string.message_login_error), Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Toast.makeText(activity, activity.getString(R.string.message_default_error) + jsonObject.getString("response"), Toast.LENGTH_LONG).show();
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Global.setLoadingVisible(activity.pb, false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Toast.makeText(activity, activity.getString(R.string.message_failure_error) + Arrays.toString(errorResponse), Toast.LENGTH_LONG).show();
                Global.setLoadingVisible(activity.pb, false);
            }
        };
        handler.setCharset("UTF-8");

        //Send request to server
        client = new AsyncHttpClient();
        client.post("http://johny.scalacubes.org/request.php", params, handler);
    }

    //REGISTER -> Register request
    public static void sendRegisterRequest(final Map<String, String> reqParams, final Action action, final RegisterActivity activity) {

        //Set progress_bar bar visible
        Global.setLoadingVisible(activity.pb, true);

        //Init params
        RequestParams params = initParams(reqParams, action);

        //Create handler
        AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                try {
                    //Create JSON object from response
                    JSONObject jsonObject = new JSONObject(new String(response));

                    //Get response
                    switch (jsonObject.getString("response")) {
                        case "success":
                            activity.finish();
                            break;
                        case "wrong_params":
                            Toast.makeText(activity, activity.getString(R.string.message_params_error), Toast.LENGTH_LONG).show();
                            break;
                        case "user_exists":
                            Toast.makeText(activity, activity.getString(R.string.message_user_exists_error), Toast.LENGTH_LONG).show();
                            break;
                        case "failure":
                            Toast.makeText(activity, activity.getString(R.string.message_register_error), Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Toast.makeText(activity, activity.getString(R.string.message_default_error), Toast.LENGTH_LONG).show();
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Set progress_bar bar invisible
                Global.setLoadingVisible(activity.pb, false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Toast.makeText(activity, activity.getString(R.string.message_failure_error) + Arrays.toString(errorResponse), Toast.LENGTH_LONG).show();
                Global.setLoadingVisible(activity.pb, false);
            }
        };
        handler.setCharset("UTF-8");

        //Send request to server
        client = new AsyncHttpClient();
        client.post("http://johny.scalacubes.org/request.php", params, handler);
    }

    //UPDATE -> Change request
    public static void sendChangeProfileRequest(final Map<String, String> reqParams, final Action action, final ProfileActivity activity, final ProfileActivity.UpdatedItem updatedItem) {

        //Set progress_bar bar visible
        Global.setLoadingVisible(activity.pb, true);

        //Init params
        final RequestParams params = initParams(reqParams, action);

        //Create handler
        AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                try {
                    //Create JSON object from response
                    JSONObject jsonObject = new JSONObject(new String(response));

                    //Get response
                    switch (jsonObject.getString("response")) {
                        case "success":
                            if (updatedItem == ProfileActivity.UpdatedItem.Email) {
                                Toast.makeText(activity, activity.getString(R.string.message_email_changed), Toast.LENGTH_LONG).show();
                                Global.loggedUser.setEmail(reqParams.get("email"));
                            }
                            if (updatedItem == ProfileActivity.UpdatedItem.Password)
                                Toast.makeText(activity, activity.getString(R.string.message_password_changed), Toast.LENGTH_LONG).show();
                            break;
                        case "wrong_password":
                            Toast.makeText(activity, activity.getString(R.string.message_password_error), Toast.LENGTH_LONG).show();
                            break;
                        case "user_not_exists":
                            Toast.makeText(activity, activity.getString(R.string.message_user_exists_error), Toast.LENGTH_LONG).show();
                            break;
                        case "failure":
                            Toast.makeText(activity, activity.getString(R.string.message_register_error), Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Toast.makeText(activity, activity.getString(R.string.message_default_error), Toast.LENGTH_LONG).show();
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Set progress_bar bar invisible
                Global.setLoadingVisible(activity.pb, false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Toast.makeText(activity, activity.getString(R.string.message_failure_error) + Arrays.toString(errorResponse), Toast.LENGTH_LONG).show();
                Global.setLoadingVisible(activity.pb, false);
            }
        };
        handler.setCharset("UTF-8");

        //Send request to server
        client = new AsyncHttpClient();
        client.post("http://johny.scalacubes.org/request.php", params, handler);
    }

    //CAR ACCIDENT -> register car accident request
    public static void sendRegisterCarRequest(final Map<String, String> reqParams, final Action action, final CreateCarActivity activity, final ProgressBar pb) {

        //Turn on loading bar
        Global.setLoadingVisible(pb, true);

        //Init params
        RequestParams params = initParams(reqParams, action);

        //Create handler
        AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                try {
                    //Create JSON object from response
                    JSONObject jsonObject = new JSONObject(new String(response));

                    //Get response
                    switch (jsonObject.getString("response")) {
                        case "success":
                            activity.finish();
                            break;
                        case "wrong_params":
                            Toast.makeText(activity, activity.getString(R.string.message_params_error), Toast.LENGTH_LONG).show();
                            break;
                        case "policy_id":
                            Toast.makeText(activity, activity.getString(R.string.message_car_policy_error), Toast.LENGTH_LONG).show();
                            break;
                        case "failure":
                            Toast.makeText(activity, activity.getString(R.string.message_register_error), Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Toast.makeText(activity, activity.getString(R.string.message_default_error) + jsonObject.getString("response"), Toast.LENGTH_LONG).show();
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Set progress_bar bar invisible
                Global.setLoadingVisible(pb, false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Toast.makeText(activity, activity.getString(R.string.message_failure_error) + Arrays.toString(errorResponse), Toast.LENGTH_LONG).show();
                Global.setLoadingVisible(pb, false);
            }
        };
        handler.setCharset("UTF-8");

        //Send request to server
        client = new AsyncHttpClient();
        client.post("http://johny.scalacubes.org/request.php", params, handler);

    }

    //DRONE INSURANCE -> register drone insurance request
    public static void sendRegisterDroneRequest(final Map<String, String> reqParams, final Action action, final CreateDroneActivity activity) {

        //Turn on loading bar
        Global.setLoadingVisible(activity.pb, true);

        //Init params
        RequestParams params = initParams(reqParams, action);

        //Create handler
        AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                try {
                    //Create JSON object from response
                    JSONObject jsonObject = new JSONObject(new String(response));

                    //Get response
                    switch (jsonObject.getString("response")) {
                        case "success":
                            activity.finish();
                            break;
                        case "wrong_params":
                            Toast.makeText(activity, activity.getString(R.string.message_params_error), Toast.LENGTH_LONG).show();
                            break;
                        case "policy_id":
                            Toast.makeText(activity, activity.getString(R.string.message_drone_policy_error), Toast.LENGTH_LONG).show();
                            break;
                        case "failure":
                            Toast.makeText(activity, activity.getString(R.string.message_register_error), Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Toast.makeText(activity, activity.getString(R.string.message_default_error) + jsonObject.getString("response"), Toast.LENGTH_LONG).show();
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Set progress_bar bar invisible
                Global.setLoadingVisible(activity.pb, false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Toast.makeText(activity, activity.getString(R.string.message_failure_error) + Arrays.toString(errorResponse), Toast.LENGTH_LONG).show();
                Global.setLoadingVisible(activity.pb, false);
            }
        };
        handler.setCharset("UTF-8");

        //Send request to server
        client = new AsyncHttpClient();
        client.post("http://johny.scalacubes.org/request.php", params, handler);

    }

    //LOAD -> Car Claims
    public static ArrayList<CarClaim> getCarClaims(final Map<String, String> reqParams, final Action action) {

        //Init params
        RequestParams params = initParams(reqParams, action);

        //Init array list
        final ArrayList<CarClaim> carClaims = new ArrayList<>();

        //Send request to server
        client = new AsyncHttpClient();
        client.post("http://johny.scalacubes.org/request.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                try {
                    //Create JSON object from response
                    JSONObject jsonObject = new JSONObject(new String(response));
                    Iterator<String> keys = jsonObject.keys();

                    //Get response
                    switch (jsonObject.getString("response")) {
                        case "success":
                            //Cycle throught claims
                            while (keys.hasNext()) {
                                String key = keys.next();
                                if (jsonObject.get(key) instanceof JSONObject) {
                                    //Create object
                                    JSONObject jsonTMP = (JSONObject) jsonObject.get(key);

                                    try {
                                        //Create car claim
                                        int ID = Integer.parseInt(key);
                                        String datum = jsonTMP.getString("datum");
                                        String place = new String(jsonTMP.getString("place").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                                        String spz = new String(jsonTMP.getString("SPZ").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                                        String carType = new String(jsonTMP.getString("carType").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                                        String conFirst = new String(jsonTMP.getString("contactFirstname").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                                        String conLast = new String(jsonTMP.getString("contactLastname").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                                        String conEmail = new String(jsonTMP.getString("contactEmail").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                                        String conPhone = new String(jsonTMP.getString("contactPhone").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                                        int policyNumber = jsonTMP.getInt("policyNumber");
                                        boolean damageCar = jsonTMP.getInt("damageCar") == 1;
                                        boolean damageOtherCar = jsonTMP.getInt("damageCarOther") == 1;
                                        boolean damageOther = jsonTMP.getInt("damageOther") == 1;

                                        //Photos
                                        Bitmap decodedfPhoto1 = null;
                                        Bitmap decodedfPhoto2 = null;
                                        Bitmap decodedfPhoto3 = null;
                                        Bitmap decodedfPhoto4 = null;

                                        if (jsonTMP.has("photo1")) {
                                            String photo1 = jsonTMP.getString("photo1");
                                            byte[] bytesPhoto1 = Base64.decode(photo1, Base64.DEFAULT);
                                            decodedfPhoto1 = BitmapFactory.decodeByteArray(bytesPhoto1, 0, bytesPhoto1.length);
                                        }
                                        if (jsonTMP.has("photo2")) {
                                            String photo2 = jsonTMP.getString("photo2");
                                            byte[] bytesPhoto2 = Base64.decode(photo2, Base64.DEFAULT);
                                            decodedfPhoto2 = BitmapFactory.decodeByteArray(bytesPhoto2, 0, bytesPhoto2.length);
                                        }
                                        if (jsonTMP.has("photo3")) {
                                            String photo3 = jsonTMP.getString("photo3");
                                            byte[] bytesPhoto3 = Base64.decode(photo3, Base64.DEFAULT);
                                            decodedfPhoto3 = BitmapFactory.decodeByteArray(bytesPhoto3, 0, bytesPhoto3.length);
                                        }
                                        if (jsonTMP.has("photo4")) {
                                            String photo4 = jsonTMP.getString("photo4");
                                            byte[] bytesPhoto4 = Base64.decode(photo4, Base64.DEFAULT);
                                            decodedfPhoto4 = BitmapFactory.decodeByteArray(bytesPhoto4, 0, bytesPhoto4.length);
                                        }

                                        //Add to list
                                        carClaims.add(new CarClaim(ID, datum, place, spz, carType, conFirst, conLast, conEmail, conPhone, policyNumber, damageCar, damageOtherCar, damageOther, decodedfPhoto1, decodedfPhoto2, decodedfPhoto3, decodedfPhoto4));
                                    } catch (Exception e) {
                                        Log.v("JSON_ERROR", Objects.requireNonNull(e.getMessage()));
                                    }
                                }
                            }
                            //Update item list
                            ClaimsFragment.updateItemList();
                            break;
                        case "failure":
                            Log.v("ErrorHTTP", "Failure");
                            break;
                        default:
                            Log.v("ErrorHTTP", "Wrong response");
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Log.v("ErrorHTTP", "An error has occured");
            }
        });

        return carClaims;
    }

    //LOAD -> Drone Insurances
    public static ArrayList<DroneInsurance> getDroneInsurances(final Map<String, String> reqParams, final Action action) {
        //Init params
        RequestParams params = initParams(reqParams, action);

        //Init array list
        final ArrayList<DroneInsurance> droneInsurances = new ArrayList<>();

        //Send request to server
        client = new AsyncHttpClient();
        client.post("http://johny.scalacubes.org/request.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                try {
                    //Create JSON object from response
                    JSONObject jsonObject = new JSONObject(new String(response));
                    Iterator<String> keys = jsonObject.keys();

                    //Get response
                    switch (jsonObject.getString("response")) {
                        case "success":
                            //Cycle throught insurances
                            while (keys.hasNext()) {
                                String key = keys.next();
                                if (jsonObject.get(key) instanceof JSONObject) {
                                    //Create object
                                    JSONObject jsonTMP = (JSONObject) jsonObject.get(key);

                                    try {
                                        //Create drone insruance
                                        int ID = Integer.parseInt(key);
                                        int policyNumber = jsonTMP.getInt("PolicyNumber");
                                        String datumOd = jsonTMP.getString("DatumOd");
                                        String datumDo = jsonTMP.getString("DatumDo");

                                        //Add to list
                                        droneInsurances.add(new DroneInsurance(ID, policyNumber, datumOd, datumDo));
                                    } catch (Exception e) {
                                        Log.v("JSON_ERROR", Objects.requireNonNull(e.getMessage()));
                                    }
                                }
                            }
                            //Update item list
                            InsurancesFragment.updateInsuranceList();
                            break;
                        case "failure":
                            Log.v("ErrorHTTP", "Failure");
                            break;
                        default:
                            Log.v("ErrorHTTP", "Wrong response");
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Log.v("ErrorHTTP", "An error has occured");
            }
        });

        return droneInsurances;
    }

}