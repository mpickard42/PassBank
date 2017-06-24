package pickard.marjorie.com.passbank;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link password_storage.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link password_storage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class password_storage extends Fragment {

    private OnFragmentInteractionListener mListener;

    Button btnReadFile;
    TextView txtViewOutput;
    Button btnDeleteFile;

    public password_storage() {
        // Required empty public constructor
    }

    public static password_storage newInstance() {
        password_storage fragment = new password_storage();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_password_storage, container, false);
        btnReadFile= (Button) view.findViewById(R.id.btnReadFile);
        txtViewOutput = (TextView) view.findViewById(R.id.text_display);
        btnDeleteFile= (Button) view.findViewById(R.id.btnDeleteFile);

        //When the ReadFile button is clicked
        btnReadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filename = "passwordList";
                String output = "";
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                String passwordArray = sharedPref.getString(filename, "[]");

                try{
                    JSONArray jsonArray = new JSONArray(passwordArray);
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject passwordObj = jsonArray.getJSONObject(i);
                        Iterator<?> keys = passwordObj.keys();
                        while( keys.hasNext() ) {
                            String key = (String) keys.next();
                            output = output + key + ": " + passwordObj.get(key) + "\n";
                        }
                        output = output + "\n";
                    }
                }
                catch (JSONException e) {
                    Log.e("jsonException", e.toString());
                }
                Resources res = getResources();
                txtViewOutput.setText(String.format(res.getString(R.string.password_display), output));
            }
        });


        btnDeleteFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filename = "passwordList";
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.remove(filename).apply();

            }
        });


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
