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

public class ResetPass extends Fragment {

    public ResetPass() {
        // Required empty public constructor
    }

    Button get_pass;
    getHttpGet request = new getHttpGet();
    Fragment fragment = null;
    String mail;
    EditText enter_mail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_resetpass, container, false);

        get_pass = (Button) view.findViewById(R.id.get_pass);
        enter_mail = (EditText) view.findViewById(R.id.enter_mail);


        get_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mail = enter_mail.getText().toString();
                if (mail != "") {
                    if (android.os.Build.VERSION.SDK_INT > 9) {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                    }
                    try {
                        JSONArray data = new JSONArray(request.getHttpGet("http://glagolapp.ru/api/restorePass?salt=df90sdfgl9854gjs54os59gjsogsdf&email=" + mail));

                        String arr = data.getJSONObject(0).getString("data");
                        Log.d("myLogs", "arr " + arr);
                        JSONObject jsonObj = new JSONObject(arr);
                        Log.d("myLogs", "jsonObj " + jsonObj);
                        String id = jsonObj.getString("id");
                        Log.d("myLogs", "id " + id);

                    } catch (JSONException e) {
                        e.printStackTrace();
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
