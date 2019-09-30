package me.naiimab.ratingapp;

import androidx.appcompat.app.AppCompatActivity;
import me.naiimab.smart_rating.SmartRating;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvTest = findViewById(R.id.test);


        final SmartRating smartRating = new SmartRating.Builder(this)
                //.icon(drawable)
                .session(7)
                .numOfStars(3)
                .title("Dialog Title")
                .titleTextColor(R.color.black)
                .positiveButtonText("Later")
                .negativeButtonText("Do not show")
                .positiveButtonTextColor(R.color.white)
                .negativeButtonTextColor(R.color.black)
                .formTitle("Feedback Title")
                .formHint("Feedback Edit Text Hint")
                .formSubmitText("Submit Text")
                .formCancelText("Cancel Text")
                .ratingBarColor(R.color.yellow)
                .playStoreUrl("Your play store URL")
                .onNumOfStarsPassed(new SmartRating.Builder.RatingNumOfStarsPassedListener() {
                    @Override
                    public void onNumOfStarsPassed(SmartRating ratingDialog, float rating, boolean numOfStarsPassed) {
                        ratingDialog.dismiss();
                    }
                })
                .onNumOfStarsFailed(new SmartRating.Builder.RatingNumOfStarsFailedListener() {
                    @Override
                    public void onNumOfStarsFailed(SmartRating ratingDialog, float rating, boolean numOfStarsPassed) {
                        ratingDialog.dismiss();
                    }
                })
                .onRatingChanged(new SmartRating.Builder.RatingSelectedListener() {
                    @Override
                    public void onRatingSelected(float rating, boolean thresholdCleared) {

                    }
                })
                .onFormSubmitted(new SmartRating.Builder.RatingFeedbackFormListener() {
                    @Override
                    public void onFormSubmitted(String feedback) {

                    }
                }).build();

        tvTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smartRating.show();
            }
        });


    }
}

