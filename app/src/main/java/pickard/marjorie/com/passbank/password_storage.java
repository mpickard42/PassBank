package pickard.marjorie.com.passbank;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


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

    Button btnWriteFile;
    Button btnReadFile;
    TextView txtViewOutput;

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
        btnWriteFile= (Button) view.findViewById(R.id.btnWriteFile);
        btnReadFile= (Button) view.findViewById(R.id.btnReadFile);
        txtViewOutput = (TextView) view.findViewById(R.id.text_display);

        //When the WriteFile button is clicked
        btnWriteFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filename = "myfile";
                String string = "Hello world2!";
                FileOutputStream outputStream;

                try {
                    outputStream = getContext().openFileOutput(filename, Context.MODE_PRIVATE);
                    outputStream.write(string.getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //When the ReadFile button is clicked
        btnReadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filename = "myfile";
                String string = "";



                try {
                    InputStream inputStream = getContext().openFileInput(filename);
                    if (inputStream != null){
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        String recieveString = "";
                        StringBuilder stringBuilder = new StringBuilder();

                        while( (recieveString = bufferedReader.readLine()) != null ){
                            stringBuilder.append(recieveString);
                        }

                        inputStream.close();
                        string = stringBuilder.toString();
                    }
                }
                catch (FileNotFoundException e) {
                    Log.e("login activity", "File not found: " + e.toString());
                }
                catch (IOException e) {
                    Log.e("login activity", "Can not read file: " + e.toString());
                }

                Resources res = getResources();
                txtViewOutput.setText(String.format(res.getString(R.string.password_display), string));
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
