package net.pixeltk.glagol.fragment_my_book;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.pixeltk.glagol.R;
import net.pixeltk.glagol.api.getHttpGet;
import net.pixeltk.glagol.fargment_catalog.ChoiceItemList;
import net.pixeltk.glagol.fargment_catalog.OnBackPressedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by root on 07.10.16.
 */

public class Login extends Fragment implements OnBackPressedListener{

    public Login() {
        // Required empty public constructor
    }
    Button login, send_mail;
    EditText enter_mail, enter_pass;
    getHttpGet request = new getHttpGet();
    String mail, pass;
    String [] str_log;
    Fragment fragment = null;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ImageView back_arrow, logo;
    TextView name_frag;

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

        sharedPreferences = getActivity().getSharedPreferences("Sign", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        back_arrow = (ImageView) view.findViewById(R.id.back);
        logo = (ImageView) view.findViewById(R.id.logo);

        back_arrow.setVisibility(View.VISIBLE);
        logo.setVisibility(View.INVISIBLE);

        name_frag = (TextView) view.findViewById(R.id.name_frag);
        name_frag.setText("Вход");

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                back_arrow.setBackgroundResource(R.drawable.bakground_arrow);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mail = enter_mail.getText().toString();
                pass = enter_pass.getText().toString();

                if (mail != "" && pass != "") {
                    str_log = mail.split("@");
                    editor.putString("login", str_log[0]).apply();
                    fragment = new SuccessfulEnter();
                    if (android.os.Build.VERSION.SDK_INT > 9) {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                    }
                    try {
                        JSONArray data = new JSONArray(request.getHttpGet("http://glagolapp.ru/api/login?salt=df90sdfgl9854gjs54os59gjsogsdf&email=" + mail + "&password=" + pass));
                        String status = data.getJSONObject(0).getString("error");
                        if (status.equals("true"))
                        {
                            Toast toast = Toast.makeText(getActivity(),
                                    "Вы ввели неправильный логи или пароль!", Toast.LENGTH_LONG);
                            toast.show();
                        }
                        else {
                            String arr = data.getJSONObject(0).getString("data");
                            JSONObject jsonObj = new JSONObject(arr);
                            String id = jsonObj.getString("id");

                            if (!sharedPreferences.contains("id"))
                            {
                                editor.putString("id", id).apply();
                            }

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



        send_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new ResetPass();
                if (fragment != null) {
                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.main_frame, fragment).commit();
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
