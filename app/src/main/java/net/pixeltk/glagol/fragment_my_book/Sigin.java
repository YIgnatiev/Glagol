package net.pixeltk.glagol.fragment_my_book;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.pixeltk.glagol.R;
import net.pixeltk.glagol.api.Audio;
import net.pixeltk.glagol.api.getHttpGet;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 07.10.16.
 */

public class Sigin extends Fragment {

    public Sigin() {
        // Required empty public constructor
    }

    Button login, sign;
    EditText enter_mail;
    String mail;
    int id;
    getHttpGet request = new getHttpGet();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Fragment fragment = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign, container, false);
        sign = (Button) view.findViewById(R.id.sign);
        login = (Button) view.findViewById(R.id.login);
        enter_mail = (EditText) view.findViewById(R.id.enter_mail);

        sharedPreferences = getActivity().getSharedPreferences("Sign", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new Login();
                if (fragment != null) {
                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_my_book, fragment).commit();
                }
            }
        });

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mail = enter_mail.getText().toString();
                if (mail!="") {
                    try {

                        if (android.os.Build.VERSION.SDK_INT > 9) {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);
                        }

                        JSONArray data = new JSONArray(request.getHttpGet("http://glagolapp.ru/api/registration?salt=df90sdfgl9854gjs54os59gjsogsdf&email=" + mail));
                        int id=Integer.parseInt(data.toString().replaceAll("[\\D]", ""));
                        Log.d("myLogs","id " + id);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (id != 0) {
                        editor.putInt("id", id).apply();

                    }
                    else
                    {
                        Toast toast = Toast.makeText(getActivity(),
                                "Пользователь с таким e-mail существует!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    fragment = new Login();
                    if (fragment != null) {
                        android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.frame_my_book, fragment).commit();
                    }
                }
                else {
                    Toast toast = Toast.makeText(getActivity(),
                            "Заполните все поля!", Toast.LENGTH_LONG);
                    toast.show();

                }
            }
        });




        return view;
    }
}
