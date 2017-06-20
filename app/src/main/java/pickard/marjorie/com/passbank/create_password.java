package pickard.marjorie.com.passbank;

import android.content.ClipData;
import android.content.ClipboardManager;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;
import android.app.AlertDialog;
import android.content.DialogInterface;




/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link create_password.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link create_password#newInstance} factory method to
 * create an instance of this fragment.
 */
public class create_password extends Fragment implements View.OnClickListener{
    private OnFragmentInteractionListener mListener;
    //Global Variables
    Button btnGenerate;
    SeekBar lengthSeekBar;
    TextView lengthTextView;
    TextView passwordTextView;
    CheckBox capLetCheckBox;
    CheckBox numCheckBox;
    CheckBox symCheckBox;
    CheckBox lowerCheckBox;
    boolean numChecked = false;
    boolean symChecked = false;
    boolean lowerChecked = false;
    boolean upperChecked = true;
    int passLength;
    String password;
    Button copyToClipboard;
    private static final String TAG = "MyActivity";
    Button btnSave;
    String website = "";
    String username = "";


    public create_password() {
        // Required empty public constructor
    }

    public static create_password newInstance() {
        create_password fragment = new create_password();
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
        View view = inflater.inflate(R.layout.fragment_create_password, container, false);
        //initialize the button/bar/checkboxes/text
        lengthSeekBar = (SeekBar) view.findViewById(R.id.lengthSeekBar);
        lengthTextView = (TextView) view.findViewById(R.id.lengthTextView);
        passwordTextView = (TextView) view.findViewById(R.id.password_display);
        capLetCheckBox=(CheckBox) view.findViewById(R.id.capLetCheckBox);
        capLetCheckBox.setOnClickListener(this);
        lowerCheckBox=(CheckBox) view.findViewById(R.id.lowerCheckBox);
        lowerCheckBox.setOnClickListener(this);
        numCheckBox=(CheckBox) view.findViewById(R.id.numCheckBox);
        numCheckBox.setOnClickListener(this);
        symCheckBox=(CheckBox) view.findViewById(R.id.symCheckBox);
        symCheckBox.setOnClickListener(this);
        btnGenerate= (Button) view.findViewById(R.id.btnGenerate);
        copyToClipboard= (Button) view.findViewById(R.id.copyToClipboardButton);
        btnSave= (Button) view.findViewById(R.id.btnSave);


        Resources res = getResources();
        passwordTextView.setText(String.format(res.getString(R.string.password_display), "Password Here"));

        //Showing the password length initially and setting the initial password length to 8 characters
//        lengthTextView.setText("Password Length: " + (lengthSeekBar.getProgress() + 8));

        lengthTextView.setText(String.format(res.getString(R.string.Password_Length), (lengthSeekBar.getProgress() + 8)));
        passLength = 8;


        //waits for the user to change the password length slider
        lengthSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override

            //as the slider is moved it updates the length of the password
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                passLength = progress + 8;  //change the value of the password length
                Resources res = getResources();
                lengthTextView.setText(String.format(res.getString(R.string.Password_Length), passLength));  //Change the shown value of the password length
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Not used
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Not used
            }
        });
        //When the Generate Password button is clicked
        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if no checkboxes are checked, use only capital letters in the password
                if (!upperChecked && !lowerChecked && !numChecked && !symChecked) {
                    capitalsByDefault();
                }

                //generate the password
                password = passwordGenerate();

                //Show Password
                Resources res = getResources();
                passwordTextView.setText(String.format(res.getString(R.string.password_display), password));
            }
        });

        //Clipboard button
        copyToClipboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData passwordClip = ClipData.newPlainText("password", password);
                clipboard.setPrimaryClip(passwordClip);
                Toast.makeText(getActivity(), password, Toast.LENGTH_SHORT).show();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.fragment_save_password, null);
                dialogBuilder.setView(dialogView);
                final EditText enterWebsite = (EditText) dialogView.findViewById(R.id.enterWebsite);
                final EditText enterUsername = (EditText) dialogView.findViewById(R.id.enterUsername);

                dialogBuilder.setTitle("Save Password");
                dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        website = enterWebsite.getText().toString();
                        username = enterUsername.getText().toString();
                        savePassword();
                    }
                });
                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //pass
                    }
                });
                AlertDialog b = dialogBuilder.create();
                b.show();
            }
        });

        return view;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onActivityCreated(){


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

    public void savePassword(){
        String filename = "passwordList";
        String existingData = "";
        FileOutputStream outputStream;
        String fileInput = website + "," + username + "," + password + ";";

        //get data already stored
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
                stringBuilder.append(fileInput);
                existingData = stringBuilder.toString();


            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        }
        catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        //add in new data
        try {
            outputStream = getContext().openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(existingData.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(getActivity(), "saved", Toast.LENGTH_SHORT).show();
    }


    public String passwordGenerate () {
        //Arrays of the characters arranged by type
        char[] numberArray = getString(R.string.numbers).toCharArray();
        char[] capitalArray = getString(R.string.capital_letters).toCharArray();
        char[] lowercaseArray = getString(R.string.lowercase_letters).toCharArray();
        char[] symbolArray = "~`!@#$%^&*()-_=+[{]};:\\|'\",<.>/?".toCharArray(); //hardcoded because some characters uncompatible with xml

        StringBuilder characters = new StringBuilder();
        if (upperChecked)
            characters.append(getString(R.string.capital_letters));
        if (lowerChecked)
            characters.append(getString(R.string.lowercase_letters));
        if (numChecked)
            characters.append(getString(R.string.numbers));
        if(symChecked)
            characters.append("~`!@#$%^&*()-_=+[{]};:\\|'\",<.>/?");  //hardcoded because some characters uncompatible with xml

        String str = characters.toString();
        char[] allCharacters = str.toCharArray();


        StringBuilder password = new StringBuilder();
        Random rand = new Random();

        //add characters to password one character at a time
        int i = 1;
        char ci;
        while (i <= passLength) {
            if(i == 2 && upperChecked)
                ci = capitalArray[rand.nextInt(capitalArray.length)];
            else if (i == 4 && lowerChecked)
                ci = lowercaseArray[rand.nextInt(lowercaseArray.length)];
            else if (i == 6 && numChecked)
                ci = numberArray[rand.nextInt(numberArray.length)];
            else if (i == 8 && symChecked)
                ci = symbolArray[rand.nextInt(symbolArray.length)];
            else
                ci = allCharacters[rand.nextInt(allCharacters.length)];
            password.append(ci);

            i++;
        }
        //Show password
        return (password.toString());
    }

    public void capitalsByDefault() {
        //pretend that the user wanted capital leters
        upperChecked = true;
        //check the box in the UI
        capLetCheckBox.setChecked(true);
    }


    public void onClick(View view) {
        //Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

       /*check which checkbox was clicked and if it was checked,
       set the corresponding boolean to true, if it was unchecked, set the boolean to false*/
        switch(view.getId()) {
            case R.id.capLetCheckBox:
                if (checked)
                    upperChecked = true;
                else
                    upperChecked = false;
                break;
            case R.id.lowerCheckBox:
                if(checked)
                    lowerChecked = true;
                else
                    lowerChecked = false;
                break;
            case R.id.numCheckBox:
                if (checked)
                    numChecked = true;
                else
                    numChecked = false;
                break;
            case R.id.symCheckBox:
                if (checked)
                    symChecked = true;
                else
                    symChecked = false;
                break;

        }
    }
}
