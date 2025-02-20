package com.example.liantesclubthegame;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Collections;

public class GameActivity extends AppCompatActivity {

    private Button actionButton;
    private ImageView cardImage;
    private final int REVERSE = R.drawable.reverse;
    private final int NUM_CARDS = 24;
    private ArrayList<Integer> deck = new ArrayList<>();
    private int deckIndex = 0;
    private boolean cardFlipped = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        actionButton = findViewById(R.id.button);
        actionButton.setOnClickListener(v -> buttonClicked());

        cardImage = findViewById(R.id.imageView);

        loadDeck();

    }

    private void buttonClicked() {
        if (!cardFlipped){
            //Flip card
            flipCard(deck.get(deckIndex));
            cardFlipped = true;
            actionButton.setText(R.string.next_button);
        } else {
            //Next card
            cardImage.setImageResource(REVERSE);
            cardFlipped = false;
            actionButton.setText(R.string.flip_button);
        }

        deckIndex++;
        if (deckIndex >= NUM_CARDS) {
            deckIndex = 0;
            Collections.shuffle(deck);
        }
    }

    private void loadDeck() {
        for (int i = 1; i <= NUM_CARDS; i++) {
            int resID = getResources().getIdentifier("card_" + i, "drawable", getPackageName());
            deck.add(resID);
        }
        Collections.shuffle(deck);
    }

    private void flipCard(int newImageResource) {
        // Animación de giro en el eje Y
        ObjectAnimator animator_first_card = ObjectAnimator.ofFloat(cardImage, "scaleX", 1f, 0f);
        animator_first_card.setDuration(200);

        ObjectAnimator animator_second_card = ObjectAnimator.ofFloat(cardImage, "scaleX", 0f, 1f);
        animator_second_card.setDuration(200);

        animator_first_card.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                cardImage.setImageResource(newImageResource);
                animator_second_card.start();
            }
        });

        animator_first_card.start();
    }
}