package me.naiimab.smart_rating;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialog;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

/**
 * Created by Naiim Ab. on 9/29/2019
 * Project: Rating App
 */
public class SmartRating extends AppCompatDialog implements RatingBar.OnRatingBarChangeListener, View.OnClickListener {

    private static final String PREF_SESSIONS_COUNT = "prefSessionCount";
    private static final String PREF_NEVER_SHOW = "prefNeverShow";

    private Context context;
    private Builder builder;
    private TextView dialogTitle, dialogNegative, dialogPositive, dialogFeedback, dialogSubmit, dialogCancel;
    private RatingBar ratingBar;
    private ImageView dialogIcon;
    private EditText dialogFeedbackET;
    private RelativeLayout ratingLayout, feedbackLayout;
    private TinyDB tinyDB;

    private float numOfStars;
    private int session;

    private SmartRating(Context context, Builder builder) {
        super(context);
        this.context = context;
        this.builder = builder;
        this.session = builder.session;
        this.numOfStars = builder.numOfStars;
        this.tinyDB = new TinyDB(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.dialog_layout);

        dialogTitle = findViewById(R.id.smart_rating_title);
        dialogNegative = findViewById(R.id.smart_rating_button_negative);
        dialogPositive = findViewById(R.id.smart_rating_button_positive);
        dialogFeedback = findViewById(R.id.smart_rating_feedback_title);
        dialogSubmit = findViewById(R.id.smart_rating_button_feedback_submit);
        dialogCancel = findViewById(R.id.smart_rating_button_feedback_cancel);
        ratingBar =  findViewById(R.id.smart_rating_rating_bar);
        dialogIcon = findViewById(R.id.smart_rating_icon);
        dialogFeedbackET =  findViewById(R.id.smart_rating_feedback_edit_text);
        ratingLayout = findViewById(R.id.smart_rating_layout);
        feedbackLayout = findViewById(R.id.smart_rating_feedback_layout);

        tinyDB = new TinyDB(context);

        init();
    }

    private void init() {

        dialogTitle.setText(builder.title);
        dialogPositive.setText(builder.positiveText);
        dialogNegative.setText(builder.negativeText);
        dialogFeedback.setText(builder.formTitle);
        dialogSubmit.setText(builder.submitText);
        dialogCancel.setText(builder.cancelText);
        dialogFeedbackET.setHint(builder.feedbackFormHint);

        dialogTitle.setTextColor(builder.titleTextColor != 0 ? ContextCompat.getColor(context, builder.titleTextColor) : ContextCompat.getColor(context, R.color.black));
        dialogPositive.setTextColor(builder.positiveTextColor != 0 ? ContextCompat.getColor(context, builder.positiveTextColor) : ContextCompat.getColor(context, R.color.white));
        dialogNegative.setTextColor(builder.negativeTextColor != 0 ? ContextCompat.getColor(context, builder.negativeTextColor) : ContextCompat.getColor(context, R.color.black));
        dialogFeedback.setTextColor(builder.titleTextColor != 0 ? ContextCompat.getColor(context, builder.titleTextColor) : ContextCompat.getColor(context, R.color.black));
        dialogSubmit.setTextColor(builder.positiveTextColor != 0 ? ContextCompat.getColor(context, builder.positiveTextColor) : ContextCompat.getColor(context, R.color.white));
        dialogCancel.setTextColor(builder.negativeTextColor != 0 ? ContextCompat.getColor(context, builder.negativeTextColor) : ContextCompat.getColor(context, R.color.black));

        if (builder.feedBackTextColor != 0) {
            dialogFeedbackET.setTextColor(ContextCompat.getColor(context, builder.feedBackTextColor));
        }

        if (builder.positiveBackgroundColor != 0) {
            dialogPositive.setBackgroundResource(builder.positiveBackgroundColor);
            dialogSubmit.setBackgroundResource(builder.positiveBackgroundColor);

        }
        if (builder.negativeBackgroundColor != 0) {
            dialogNegative.setBackgroundResource(builder.negativeBackgroundColor);
            dialogCancel.setBackgroundResource(builder.negativeBackgroundColor);
        }

        if (builder.ratingBarColor != 0) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
                stars.getDrawable(2).setColorFilter(ContextCompat.getColor(context, builder.ratingBarColor), PorterDuff.Mode.SRC_ATOP);
                stars.getDrawable(1).setColorFilter(ContextCompat.getColor(context, builder.ratingBarColor), PorterDuff.Mode.SRC_ATOP);
                int ratingBarBackgroundColor = builder.ratingBarBackgroundColor != 0 ? builder.ratingBarBackgroundColor : R.color.grey;
                stars.getDrawable(0).setColorFilter(ContextCompat.getColor(context, ratingBarBackgroundColor), PorterDuff.Mode.SRC_ATOP);
            } else {
                Drawable stars = ratingBar.getProgressDrawable();
                DrawableCompat.setTint(stars, ContextCompat.getColor(context, builder.ratingBarColor));
            }
        }

        Drawable d = ContextCompat.getDrawable(context, R.drawable.ic_rating_default);
        dialogIcon.setImageDrawable(builder.drawable != null ? builder.drawable : d);

        ratingBar.setOnRatingBarChangeListener(this);
        dialogPositive.setOnClickListener(this);
        dialogNegative.setOnClickListener(this);
        dialogSubmit.setOnClickListener(this);
        dialogCancel.setOnClickListener(this);

        if (!builder.showNeverMessage ) {
            dialogNegative.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.smart_rating_button_negative) {
            dismiss();
            showNever();

        } else if (view.getId() == R.id.smart_rating_button_positive) {

            dismiss();

        } else if (view.getId() == R.id.smart_rating_button_feedback_submit) {

            String feedback = dialogFeedbackET.getText().toString().trim();
            if (TextUtils.isEmpty(feedback)) {

                Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake);
                dialogFeedbackET.startAnimation(shake);
                return;
            }

            if (builder.ratingFeedbackFormListener != null) {
                builder.ratingFeedbackFormListener.onFormSubmitted(feedback);
            }

            dismiss();
            showNever();

        } else if (view.getId() == R.id.smart_rating_button_feedback_cancel) {

            dismiss();

        }

    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
        boolean numOfStarsPassed = true;
        if (ratingBar.getRating() >= numOfStars) {
            if (builder.ratingNumOfStarsPassedListener == null) {
                setRatingThresholdClearedListener();
            }
            builder.ratingNumOfStarsPassedListener.onNumOfStarsPassed(this, ratingBar.getRating(), numOfStarsPassed);

        } else {
            numOfStarsPassed = false;

            if (builder.ratingNumOfStarsFailedListener == null) {
                setRatingThresholdFailedListener();
            }
            builder.ratingNumOfStarsFailedListener.onNumOfStarsFailed(this, ratingBar.getRating(), numOfStarsPassed);
        }

        if (builder.ratingSelectedListener != null) {
            builder.ratingSelectedListener.onRatingSelected(ratingBar.getRating(), numOfStarsPassed);
        }
        showNever();
    }

    private void setRatingThresholdClearedListener() {
        builder.ratingNumOfStarsPassedListener = new Builder.RatingNumOfStarsPassedListener() {
            @Override
            public void onNumOfStarsPassed(SmartRating ratingDialog, float rating, boolean numOfStarsPassed) {
                openPlayStore(context);
                dismiss();
            }
        };
    }

    private void setRatingThresholdFailedListener() {
        builder.ratingNumOfStarsFailedListener = new Builder.RatingNumOfStarsFailedListener() {
            @Override
            public void onNumOfStarsFailed(SmartRating ratingDialog, float rating, boolean numOfStarsPassed) {
                openForm(true);
            }
        };
    }

    private void openForm(boolean open) {
        if(ratingLayout != null) ratingLayout.setVisibility(open ? View.GONE : View.VISIBLE);
        if(feedbackLayout != null) feedbackLayout.setVisibility(open ? View.VISIBLE : View.GONE);
    }

    private void openPlayStore(Context context) {
        final Uri marketUri = Uri.parse(builder.playStoreUrl);
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, marketUri));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "Couldn't find PlayStore on this device", Toast.LENGTH_SHORT).show();
        }
    }

    public TextView getTitleTextView() {
        return dialogTitle;
    }

    public TextView getPositiveButtonTextView() {
        return dialogPositive;
    }

    public TextView getNegativeButtonTextView() {
        return dialogNegative;
    }

    public TextView getFormTitleTextView() {
        return dialogFeedback;
    }

    public TextView getFormSumbitTextView() {
        return dialogSubmit;
    }

    public TextView getFormCancelTextView() {
        return dialogCancel;
    }

    public ImageView getIconImageView() {
        return dialogIcon;
    }

    public RatingBar getRatingBarView() {
        return ratingBar;
    }

    @Override
    public void show() {

        if (checkIfSessionMatches(session)) {
            openForm(false);
            super.show();
        }
    }

    private boolean checkIfSessionMatches(int session) {

        if (session == 1) {
            return true;
        }

        if(tinyDB.getBoolean(PREF_NEVER_SHOW)) {
            return false;
        }

        int count = tinyDB.getInt(PREF_SESSIONS_COUNT, 1);

        if (session == count) {
            tinyDB.putInt(PREF_SESSIONS_COUNT, 1);
            return true;
        } else if (session > count) {
            count++;
            tinyDB.putInt(PREF_SESSIONS_COUNT, count);
            return false;
        } else {
            tinyDB.putInt(PREF_SESSIONS_COUNT, 2);
            return false;
        }
    }

    private void showNever() {
        tinyDB.putBoolean(PREF_NEVER_SHOW, true);
    }

    public static class Builder {

        private final Context context;
        private String title, positiveText, negativeText, playStoreUrl;
        private String formTitle, submitText, cancelText, feedbackFormHint;
        private int positiveTextColor, negativeTextColor, titleTextColor, ratingBarColor, ratingBarBackgroundColor, feedBackTextColor;
        private int positiveBackgroundColor, negativeBackgroundColor;
        private RatingNumOfStarsPassedListener ratingNumOfStarsPassedListener;
        private RatingNumOfStarsFailedListener ratingNumOfStarsFailedListener;
        private RatingFeedbackFormListener ratingFeedbackFormListener;
        private RatingSelectedListener ratingSelectedListener;
        private Drawable drawable;
        private boolean showNeverMessage;

        private int session = 1;
        private float numOfStars = 1;

        public interface RatingNumOfStarsPassedListener {
            void onNumOfStarsPassed(SmartRating ratingDialog, float rating, boolean numOfStarsPassed);
        }

        public interface RatingNumOfStarsFailedListener {
            void onNumOfStarsFailed(SmartRating ratingDialog, float rating, boolean numOfStarsPassed);
        }

        public interface RatingFeedbackFormListener {
            void onFormSubmitted(String feedback);
        }

        public interface RatingSelectedListener {
            void onRatingSelected(float rating, boolean numOfStarsPassed);
        }

        public Builder(Context context) {
            this.context = context;
            // Set default PlayStore URL
            this.playStoreUrl = "market://details?id=" + context.getPackageName();
            this.showNeverMessage = true;
            initText();
        }

        private void initText() {
            title = context.getString(R.string.smart_rating_experience);
            positiveText = context.getString(R.string.smart_rating_later);
            negativeText = context.getString(R.string.smart_rating_never);
            formTitle = context.getString(R.string.smart_rating_feedback);
            submitText = context.getString(R.string.smart_rating_submit);
            cancelText = context.getString(R.string.smart_rating_cancel);
            feedbackFormHint = context.getString(R.string.smart_rating_suggestions);
        }

        public Builder session(int session) {
            this.session = session;
            return this;
        }

        public Builder numOfStars(float threshold) {
            this.numOfStars = threshold;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder icon(Drawable drawable) {
            this.drawable = drawable;
            return this;
        }

        public Builder positiveButtonText(String positiveText) {
            this.positiveText = positiveText;
            return this;
        }

        public Builder negativeButtonText(String negativeText) {
            this.negativeText = negativeText;
            return this;
        }

        public Builder titleTextColor(int titleTextColor) {
            this.titleTextColor = titleTextColor;
            return this;
        }

        public Builder positiveButtonTextColor(int positiveTextColor) {
            this.positiveTextColor = positiveTextColor;
            return this;
        }

        public Builder negativeButtonTextColor(int negativeTextColor) {
            this.negativeTextColor = negativeTextColor;
            return this;
        }

        public Builder positiveButtonBackgroundColor(int positiveBackgroundColor) {
            this.positiveBackgroundColor = positiveBackgroundColor;
            return this;
        }

        public Builder negativeButtonBackgroundColor(int negativeBackgroundColor) {
            this.negativeBackgroundColor = negativeBackgroundColor;
            return this;
        }

        public Builder onNumOfStarsPassed(RatingNumOfStarsPassedListener ratingThresholdClearedListener) {
            this.ratingNumOfStarsPassedListener = ratingThresholdClearedListener;
            return this;
        }

        public Builder onNumOfStarsFailed(RatingNumOfStarsFailedListener ratingThresholdFailedListener) {
            this.ratingNumOfStarsFailedListener = ratingThresholdFailedListener;
            return this;
        }

        public Builder onRatingChanged(RatingSelectedListener ratingDialogListener) {
            this.ratingSelectedListener = ratingDialogListener;
            return this;
        }

        public Builder onFormSubmitted(RatingFeedbackFormListener ratingDialogFormListener) {
            this.ratingFeedbackFormListener = ratingDialogFormListener;
            return this;
        }

        public Builder formTitle(String formTitle) {
            this.formTitle = formTitle;
            return this;
        }

        public Builder formHint(String formHint) {
            this.feedbackFormHint = formHint;
            return this;
        }

        public Builder formSubmitText(String submitText) {
            this.submitText = submitText;
            return this;
        }

        public Builder formCancelText(String cancelText) {
            this.cancelText = cancelText;
            return this;
        }

        public Builder ratingBarColor(int ratingBarColor) {
            this.ratingBarColor = ratingBarColor;
            return this;
        }

        public Builder ratingBarBackgroundColor(int ratingBarBackgroundColor) {
            this.ratingBarBackgroundColor = ratingBarBackgroundColor;
            return this;
        }

        public Builder feedbackTextColor(int feedBackTextColor) {
            this.feedBackTextColor = feedBackTextColor;
            return this;
        }

        public Builder playStoreUrl(String playStoreUrl) {
            this.playStoreUrl = playStoreUrl;
            return this;
        }

        public Builder showNeverMessage(boolean showNever){
            this.showNeverMessage = showNever;
            return this;
        }

        public SmartRating build() {
            return new SmartRating(context, this);
        }
    }
}