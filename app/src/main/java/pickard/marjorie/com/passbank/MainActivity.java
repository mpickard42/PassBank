package pickard.marjorie.com.passbank;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //Global Variables
    Button btnGenerate;
    SeekBar lengthSeekBar;
    TextView lengthTextView;
    TextView passwordTextView;
    CheckBox capLetCheckBox;
    CheckBox numCheckBox;
    CheckBox symTextBox;
    CheckBox lowerCheckBox;
    boolean numChecked = false;
    boolean symChecked = false;
    boolean lowerChecked = false;
    boolean upperChecked = true;
    int passLength;
    String password;
    boolean numPresent = false;
    boolean capitalPresent = false;
    boolean lowercasePresent = false;
    boolean symbolPresent = false;
    ImageButton copyToClipboard;
    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_creation:
                    mTextMessage.setText(R.string.title_creation);
                    return true;
                case R.id.navigation_storage:
                    mTextMessage.setText(R.string.title_storage);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //initialize the button/bar/checkboxes/text
        lengthSeekBar = (SeekBar) findViewById(R.id.lengthSeekBar);
        lengthTextView = (TextView) findViewById(R.id.lengthTextView);
        passwordTextView = (TextView) findViewById(R.id.passwordTextView);
        capLetCheckBox=(CheckBox)findViewById(R.id.capLetCheckBox);
        lowerCheckBox=(CheckBox)findViewById(R.id.lowerCheckBox);
        numCheckBox=(CheckBox)findViewById(R.id.numCheckBox);
        symTextBox=(CheckBox)findViewById(R.id.symCheckBox);
        btnGenerate= (Button)findViewById(R.id.btnGenerate);
        copyToClipboard= (ImageButton)findViewById(R.id.copyToClipboardButton);

        passwordTextView.setText("Password Here");


        //Showing the password length initially and setting the initial password length to 8 characters
        lengthTextView.setText("Password Length: " + (lengthSeekBar.getProgress() + 8));
        passLength = 8;

        //waits for the user to change the password length slider
        lengthSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override

            //as the slider is moved it updates the length of the password
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                passLength = progress + 8;  //change the value of the password length
                lengthTextView.setText("Password Length: " + passLength);  //Change the shown value of the password length
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
                passwordTextView.setText(password);
            }
        });

        //Clipboard button
        copyToClipboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData passwordClip = ClipData.newPlainText("password", password);
                clipboard.setPrimaryClip(passwordClip);
                Toast.makeText(MainActivity.this, password, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public String passwordGenerate () {

        //Arrays of the characters arranged by type
        char[] charNum = getString(R.string.numbers).toCharArray();
        char[] charCap = getString(R.string.capital_letters).toCharArray();
        char[] charLower = getString(R.string.lowercase_letters).toCharArray();
        char[] charSym = "~`!@#$%^&*()-_=+[{]};:\\|'\",<.>/?".toCharArray(); //hardcoded because some characters uncompatible with xml

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
        char[] chars = str.toCharArray();




        StringBuilder password = new StringBuilder();
        Random random1 = new Random();

        //add characters to password one character at a time
        //TODO put in breakpoint so the whileloop does not go on forever or add one of each of the desired characters at the beginning
        int i = 1;
        while (i <= passLength) {
            char ci = chars[random1.nextInt(chars.length)];
            //Toast.makeText(MainActivity.this, "i: " + i + ci, Toast.LENGTH_SHORT).show();
            password.append(ci);

            //Booleans to check if the different types of characters are present
            String charString = Character.toString(ci);  //convert from character to string
            if (new String(charNum).contains(charString)) {
                numPresent = true;
            }
            if (new String(charCap).contains(charString)) {
                capitalPresent = true;

            }
            if (new String(charLower).contains(charString)) {
                lowercasePresent = true;
            }
            if (new String(charSym).contains(charString)) {
                symbolPresent = true;
            }

            //Check to see if values are present but not wanted
            if (i == passLength) {
                if (!capitalPresent && upperChecked) {
                    password.setLength(0);
                    i = 0;
                    setBooleansFalse();
                }

                if (!lowercasePresent && lowerChecked) {
                    password.setLength(0);
                    i = 0;
                    setBooleansFalse();
                }

                if (!numPresent && numChecked) {
                    password.setLength(0);
                    i = 0;
                    setBooleansFalse();
                }

                if (!symbolPresent && symChecked) {
                    password.setLength(0);
                    i = 0;
                    setBooleansFalse();
                }
            }

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

    public void setBooleansFalse() {
        numPresent = false;
        capitalPresent = false;
        lowercasePresent = false;
        symbolPresent = false;
    }

    public void onCheckboxClicked (View view) {
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
