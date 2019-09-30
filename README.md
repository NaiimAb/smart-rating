# Smart Rating Dialog


[![](https://jitpack.io/v/NaiimAb/smart-rating.svg)](https://jitpack.io/#NaiimAb/smart-rating)


Smart Rating is a library for Android built in Java to show rating dialog in your app
### Features:

  - Easy to implement
  - Changable style
  - Make the dialog appear on a defined app session
  - Opens Feedback form if the user rates below the minimum stars
  - Customizable title, positive button and negative button texts
  - Override dialog redirection to Google Play or Feedback form according to your needs
  - Different Languages


### Show on Sepecific Time

Use session() To show dialog on different time user opend the app. numOfStars() is used to defined the threshold of the app. If user shows less thant that number a feedback form will show, otherwise it will redirect user to your play store defined here : playStoreUrl()

### Installation
Use parameters as you wanted
The whole parameters are

```sh
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
```

You can override the redrection to Play Store and override the feedback form too

### Note

 - Don't use session() if you want to show the dialog on a click event.
 - Remove the threshold() from the builder if you don't want to show the feedback form to the user.
 - Use onThresholdCleared() to override the default redirection to Google Play.
- Use onThresholdFailed() to show your custom feedback form.


### Installation

##### Project Gradle

add to repositories
```sh
maven { url 'https://jitpack.io' }
```
##### App Gradle

Add to dependencies
```sh
implementation 'com.github.NaiimAb:smart-rating:1.0.0'
```
#### Show Dialog
```sh
smartRating.show();
```
#### Languages Available
For default text of the dialog, there is :
- English
- French
- Arabic
- German
- Spanish

### Author:
Naiim Abouhafs
Let us know if there is any bug or problem

License
----
``` sh
Copyright (C) 2016 Code My Brains Out

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

[//]: # (These are reference links used in the body of this note and get stripped out when the markdown processor does its job. There is no need to format nicely because it shouldn't be seen. Thanks SO - http://stackoverflow.com/questions/4823468/store-comments-in-markdown-syntax)


   [dill]: <https://github.com/joemccann/dillinger>
   [git-repo-url]: <https://github.com/joemccann/dillinger.git>
   [john gruber]: <http://daringfireball.net>
   [df1]: <http://daringfireball.net/projects/markdown/>
   [markdown-it]: <https://github.com/markdown-it/markdown-it>
   [Ace Editor]: <http://ace.ajax.org>
   [node.js]: <http://nodejs.org>
   [Twitter Bootstrap]: <http://twitter.github.com/bootstrap/>
   [jQuery]: <http://jquery.com>
   [@tjholowaychuk]: <http://twitter.com/tjholowaychuk>
   [express]: <http://expressjs.com>
   [AngularJS]: <http://angularjs.org>
   [Gulp]: <http://gulpjs.com>

   [PlDb]: <https://github.com/joemccann/dillinger/tree/master/plugins/dropbox/README.md>
   [PlGh]: <https://github.com/joemccann/dillinger/tree/master/plugins/github/README.md>
   [PlGd]: <https://github.com/joemccann/dillinger/tree/master/plugins/googledrive/README.md>
   [PlOd]: <https://github.com/joemccann/dillinger/tree/master/plugins/onedrive/README.md>
   [PlMe]: <https://github.com/joemccann/dillinger/tree/master/plugins/medium/README.md>
   [PlGa]: <https://github.com/RahulHP/dillinger/blob/master/plugins/googleanalytics/README.md>
