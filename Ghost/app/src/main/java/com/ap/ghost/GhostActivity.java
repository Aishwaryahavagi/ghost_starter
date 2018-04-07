   package com.ap.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import static com.ap.ghost.R.id.ghostText;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    private TextView ghostWord ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AssetManager assetManager = getAssets();

        try
        {
            InputStream inputStream = assetManager.open("words.txt");
            dictionary = new SimpleDictionary(inputStream);

        } catch (IOException e)
        {
            Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG).show();
        }

        ghostWord = (TextView) findViewById(ghostText);
        findViewById(R.id.challengeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                challengeButtonHandler();

            }
        });

        findViewById(R.id.resetButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStart(v);
            }
        });

        onStart(null);
    }

    private void challengeButtonHandler()
    {
        TextView label = (TextView) findViewById(R.id.gameStatus);


        String ghostCurrentWord = ghostWord.getText().toString();

        if(ghostCurrentWord.length()>=4 && dictionary.isWord(ghostCurrentWord))
        {
            label.setText("User Won!!!");
        }
        else
        {
            String newWord = dictionary.getAnyWordStartingWith(ghostCurrentWord);
            Log.d("Ghost", "Word starting with " + ghostText + "is " + newWord);
            if(newWord == null)
            {
                label.setText("User Won!!");
            }
            else
            {
                label.setText("Computer Won!! \nPossible words: "+newWord);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public boolean onStart(View view)
    {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    private void computerTurn()
    {


        Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {

                TextView label = (TextView) findViewById(R.id.gameStatus);


                String ghostCurrentWord = ghostWord.getText().toString();

                if(ghostCurrentWord.length()>=4 && dictionary.isWord(ghostCurrentWord))
                {
                    label.setText("Computer Won!!!");
                }
                else
                {
                    String newWord = dictionary.getAnyWordStartingWith(ghostCurrentWord);
                    Log.d("Ghost", "Word starting with " + ghostText + "is " + newWord);
                    if(newWord == null)
                    {
                        label.setText("Computer Won!!");
                    }
                    else
                    {
                        ghostWord.append(newWord.charAt(ghostCurrentWord.length())+"");
                        userTurn = true;
                        label.setText(USER_TURN);
                    }
                }
            }
        }, 1500);





    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        //Code Implemented
        char keyPressed = (char) event.getUnicodeChar();
        //Log.d("Key Pressed: ", keyPressed+"hola "+keyCode+" "+event);

        if(Character.isLetter(keyPressed))
        {
            ghostWord.append(keyPressed+"");
            //ghostWord.setText(ghostWord.getText()+""+keyPressed);
            TextView label = (TextView) findViewById(R.id.gameStatus);
            label.setText(COMPUTER_TURN);
            computerTurn();
            return true;
        }




        return super.onKeyUp(keyCode, event);
    }
}