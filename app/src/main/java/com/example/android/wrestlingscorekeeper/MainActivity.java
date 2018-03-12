package com.example.android.wrestlingscorekeeper;

import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements MyDialogFragment.DialogClickListener{

    //define four fragments
    private ButtonViewLayout takeDownFragment;
    private ButtonViewLayout escapeFragment;
    private ButtonViewLayout reversalFragment;
    private ButtonViewLayout nearfallFragment;
    private ButtonViewLayout[] fragmentArray = new ButtonViewLayout[4];

    //reset button
    private Button resetButton;

    //set button to be clicked only 3 times maximumly
    private int maxClick = 3;
    private int currentClickTime = 0;

    // total score for home and guest team
    private int homeTotalScore = 0;
    private int guestTotalScore = 0;

    // score for different scoring types
    private int tdHomeScore = 0;
    private int tdGuestScore = 0;

    private int esHomeScore = 0;
    private int esGuestScore = 0;

    private int rvHomeScore = 0;
    private int rvGuestScore = 0;

    private int nfHomeScore = 0;
    private int nfGuestScore = 0;

    //text views to show total scores
    private TextView homeTsView;
    private TextView guestTsView;

    //score table text views
    private TextView homeScoreR1;
    private TextView homeScoreR2;
    private TextView homeScoreR3;
    private TextView guestScoreR1;
    private TextView guestScoreR2;
    private TextView guestScoreR3;

    boolean buttonIsClicked = false;

    //calculate which team wins
    private int homeWins = 0;
    private int guestWins = 0;

    String messageToAlert;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //add table views
        //define score table views
        homeScoreR1 = (TextView) findViewById(R.id.home_score_R1);
        guestScoreR1 = (TextView) findViewById(R.id.guest_score_R1);
        homeScoreR2 = (TextView) findViewById(R.id.home_score_R2);
        guestScoreR2 = (TextView) findViewById(R.id.guest_score_R2);
        homeScoreR3 = (TextView) findViewById(R.id.home_score_R3);
        guestScoreR3 = (TextView) findViewById(R.id.guest_score_R3);

        //define two text views of home and guest scores
        homeTsView = (TextView) findViewById(R.id.home_total_score);
        guestTsView = (TextView) findViewById(R.id.guest_total_score);

        fragmentClickListener();
        onButtonClickListener();

    }
    //fragment click listener
    public void fragmentClickListener(){
        //add 4 fragments to main_activity
        takeDownFragment = (ButtonViewLayout) getFragmentManager().findFragmentById(R.id.takedown_fragment);
        escapeFragment = (ButtonViewLayout) getFragmentManager().findFragmentById(R.id.escape_fragment);
        reversalFragment = (ButtonViewLayout) getFragmentManager().findFragmentById(R.id.reversal_fragment);
        nearfallFragment = (ButtonViewLayout) getFragmentManager().findFragmentById(R.id.nearfall_fragment);

        //setListener for each fragment to catch score change
        takeDownFragment.setListener( new ButtonViewLayout.ButtonViewLayoutListener() {
            @Override
            public void getScore(int home, int guest) {
                tdHomeScore = home * 2;
                tdGuestScore = guest * 2;
                updateScoreView();
            }
        });

        escapeFragment.setListener(new ButtonViewLayout.ButtonViewLayoutListener() {
            @Override
            public void getScore(int home, int guest) {
                esHomeScore = home;
                esGuestScore = guest;
                updateScoreView();
            }
        });

        reversalFragment.setListener(new ButtonViewLayout.ButtonViewLayoutListener() {
            @Override
            public void getScore(int home, int guest) {
                rvHomeScore = home * 2;
                rvGuestScore = guest * 2;
                updateScoreView();
            }
        });

        nearfallFragment.setListener(new ButtonViewLayout.ButtonViewLayoutListener() {
            @Override
            public void getScore(int home, int guest) {
                nfHomeScore = home * 2;
                nfGuestScore = guest * 2;
                updateScoreView();
            }
        });

    }


    //reset button click listener
    public void onButtonClickListener(){
        //add 1 reset button
        resetButton = (Button) findViewById(R.id.reset_button);

        //fragment array
        fragmentArray[0] = takeDownFragment;
        fragmentArray[1] = escapeFragment;
        fragmentArray[2] = reversalFragment;
        fragmentArray[3] = nearfallFragment;

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonIsClicked = true;
                currentClickTime += 1;
                if (currentClickTime <= maxClick) {
                    switch (currentClickTime) {
                        case 1:
                            resetButtonIsClicked(homeScoreR1,guestScoreR1);
                            break;
                        case 2:
                            resetButtonIsClicked(homeScoreR2,guestScoreR2);
                            break;
                        case 3:
                            resetButtonIsClicked(homeScoreR3,guestScoreR3);
                            break;
                    }
                } else {
                    whoWins();
                    showDialog(messageToAlert);
                    Toast.makeText(MainActivity.this, "Game over", Toast.LENGTH_SHORT).show();
                    currentClickTime = 0;
                    updateTotalScore();
                    updateScoreView();
                    updateFragmentNumber(fragmentArray,buttonIsClicked);
                    updateNumberVariables();
                    resetButton.setEnabled(false);
                }
            }
        });
    }

    //helper methods
    //update each team's total score
    private void updateScoreView(){
        homeTotalScore =tdHomeScore + esHomeScore + rvHomeScore + nfHomeScore;
        guestTotalScore = tdGuestScore + esGuestScore + rvGuestScore + nfGuestScore;
        homeTsView.setText(String.valueOf(homeTotalScore));
        guestTsView.setText(String.valueOf(guestTotalScore));
    }

    //record each round's total score for each team
    private void updateScoreTable(TextView home, TextView guest){
        home.setText(String.valueOf(homeTotalScore));
        guest.setText(String.valueOf(guestTotalScore));
    }

    //reset main activity total score to 0
    private void updateTotalScore(){
        if (homeTotalScore > guestTotalScore){
            homeWins += 1;
        } else if (homeTotalScore < guestTotalScore){
            guestWins += 1;
        }
        updateNumberVariables();
        homeTsView.setText(String.valueOf(homeTotalScore));
        guestTsView.setText(String.valueOf(guestTotalScore));
    }

    //reset fragment score to 0
    private void updateFragmentNumber(ButtonViewLayout[] fragmentArray, boolean buttonIsClicked){
        if (buttonIsClicked) {
            for (ButtonViewLayout fragment : fragmentArray) {
                fragment.updateFragmentScore();
            }
        }
    }

    //helper method for reset button
    private void resetButtonIsClicked(TextView homeScoreR, TextView guestScoreR){
        updateFragmentNumber(fragmentArray,buttonIsClicked);
        updateScoreTable(homeScoreR, guestScoreR);
        updateTotalScore();
        buttonIsClicked = false;
    }

    public void showDialog(String messageToAlert){
        MyDialogFragment myDialogFragment = MyDialogFragment.newInstance(messageToAlert);
        myDialogFragment.setCancelable(false);
        myDialogFragment.show(getFragmentManager(),"game_over");
    }

    //reset every view to start a new game
    public void yesButtonClicked() {
        TextView[] scoreRoundArray = {homeScoreR1,homeScoreR2,homeScoreR3,guestScoreR1,guestScoreR2,guestScoreR3};
        cleanUpTableView(scoreRoundArray);
        resetButton.setEnabled(true);
        homeWins = 0;
        guestWins = 0;
    }

    @Override
    public void noButtonClicked() {
        resetButton.setEnabled(false);
        showDialog(messageToAlert);
    }

    //who wins the game after 3 rounds
    private void whoWins(){
        if (homeWins < guestWins){
            messageToAlert = "Guest Team";
        }else if (homeWins > guestWins){
            messageToAlert = "Home Team";
        }else if (homeWins == guestWins){
            messageToAlert = "It's a tie";
        }
    }

    private void cleanUpTableView(TextView[] scoreRoundArray){
        for (TextView scoreRound:scoreRoundArray){
            scoreRound.setText("0");
            updateNumberVariables();

        }
    }

    private void updateNumberVariables(){
        homeTotalScore = 0;
        guestTotalScore = 0;
        tdHomeScore = 0;
        tdGuestScore = 0;
        esHomeScore = 0;
        esGuestScore = 0;
        rvHomeScore = 0;
        rvGuestScore = 0;
        nfHomeScore = 0;
        nfGuestScore = 0;
    }
}
