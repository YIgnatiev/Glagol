package net.pixeltk.glagol.fragment_my_book;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.pixeltk.glagol.R;
import net.pixeltk.glagol.api.getHttpGet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by root on 07.10.16.
 */

public class Login extends Fragment {

    public Login() {
        // Required empty public constructor
    }
    Button login, send_mail;
    EditText enter_mail, enter_pass;
    getHttpGet request = new getHttpGet();
    String mail, pass;
    Fragment fragment = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        send_mail = (Button) view.findViewById(R.id.send_mail);
        login = (Button) view.findViewById(R.id.login);
        enter_mail = (EditText) view.findViewById(R.id.enter_mail);
        enter_pass = (EditText) view.findViewById(R.id.enter_password);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mail = enter_mail.getText().toString();
                pass = enter_pass.getText().toString();
                if (mail != "" && pass != "") {
                    fragment = new SuccessfulEnter();
                    if (android.os.Build.VERSION.SDK_INT > 9) {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                    }
                    try {
                        JSONArray data = new JSONArray(request.getHttpGet("http://glagolapp.ru/api/login?salt=df90sdfgl9854gjs54os59gjsogsdf&email=" + mail + "&password=" + pass));

                        String arr = data.getJSONObject(0).getString("data");
                        Log.d("myLogs", "arr " + arr);
                        JSONObject jsonObj = new JSONObject(arr);
                        Log.d("myLogs", "jsonObj " + jsonObj);
                        String id = jsonObj.getString("id");
                        Log.d("myLogs", "id " + id);

                        if (fragment != null) {
                            android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.frame_my_book, fragment).commit();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast toast = Toast.makeText(getActivity(),
                            "Заполните все поля!", Toast.LENGTH_LONG);
                    toast.show();

                }
            }
        });

        send_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new ResetPass();
                if (fragment != null) {
                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_my_book, fragment).commit();
                }
            }
        });

        return view;
    }
}