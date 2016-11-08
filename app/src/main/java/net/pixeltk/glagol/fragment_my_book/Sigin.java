package net.pixeltk.glagol.fragment_my_book;

import android.app.Dialog;
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
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.pixeltk.glagol.R;
import net.pixeltk.glagol.api.Audio;
import net.pixeltk.glagol.api.getHttpGet;
import net.pixeltk.glagol.fargment_catalog.ChoiceItemList;
import net.pixeltk.glagol.fargment_catalog.OnBackPressedListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 07.10.16.
 */

public class Sigin extends Fragment implements OnBackPressedListener{

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
    ImageView back_arrow, logo;
    TextView name_frag;
    String[] str_log;

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


        back_arrow = (ImageView) view.findViewById(R.id.back);
        logo = (ImageView) view.findViewById(R.id.logo);

        back_arrow.setVisibility(View.VISIBLE);
        logo.setVisibility(View.INVISIBLE);

        name_frag = (TextView) view.findViewById(R.id.name_frag);
        name_frag.setText("Авторизация");

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new Login();
                if (fragment != null) {
                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.main_frame, fragment).commit();
                }
            }
        });

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mail = enter_mail.getText().toString();
                if (!mail.trim().equals("")) {
                    try {

                        if (android.os.Build.VERSION.SDK_INT > 9) {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);
                        }

                        JSONArray data = new JSONArray(request.getHttpGet("http://glagolapp.ru/api/registration?salt=df90sdfgl9854gjs54os59gjsogsdf&email=" + mail));
                        String status = data.getJSONObject(0).getString("error");

                        if (status.equals("true"))
                        {
                            Toast toast = Toast.makeText(getActivity(),
                                    "Пользователь с таким e-mail существует!", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        else
                        {
                            str_log = mail.split("@");
                            editor.putString("login", str_log[0]);
                            int id=Integer.parseInt(data.toString().replaceAll("[\\D]", ""));
                            editor.putString("id", String.valueOf(id)).apply();
                            final Dialog dialog = new Dialog(getActivity());
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.customdialog);

                            Button dialogButtonCancel = (Button) dialog.findViewById(R.id.customDialogCancel);
                            dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });

                            dialog.show();
                            fragment = new SuccessfulEnter();
                            if (fragment != null) {
                                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.main_frame, fragment).commit();
                            }
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
        return view;
    }
    @Override
    public void onBackPressed() {
        fragment = new CheckLoginSign();
        editor.remove("idbook").apply();
        if (fragment != null) {
            android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_frame, fragment).commit();

        }


    }

}
