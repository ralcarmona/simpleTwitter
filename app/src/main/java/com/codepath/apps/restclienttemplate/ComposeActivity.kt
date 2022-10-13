package com.codepath.apps.restclienttemplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

lateinit var etCompose: EditText
lateinit var btnTweet: Button

lateinit var client:TwitterClient

class ComposeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        etCompose = findViewById(R.id.etTweetCompose)
        btnTweet = findViewById(R.id.btnTweet)

        client = TwitterApplication.getRestClient(this)

        //for counting characters while texting
        etCompose.addTextChangedListener(object: TextWatcher{
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Fires right as the text is being changed (even supplies the range of text)

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Fires right before text is changing
            }

            override fun afterTextChanged(s: Editable) {
                // Fires right after the text has changed
            }

        })

        //handling the tweet when the user clicks the button
        btnTweet.setOnClickListener {
            // grabs the contents of the text
            //makes api call to twitter to published
            val tweetContent = etCompose.text.toString()
            //make sure tweet is not empty
            if (tweetContent.isEmpty()){
                Toast.makeText(this, "empty tweet is not allowed", Toast.LENGTH_SHORT).show()
                }else
                    if (tweetContent.length > 140){
                        Toast.makeText(this, "Tweet is to long, limit is 140 characters", Toast.LENGTH_SHORT).show()
                    }else {
                        client.publishedTweet(tweetContent,object:JsonHttpResponseHandler() {
                            override fun onSuccess(
                                statusCode: Int,
                                headers: Headers?,
                                json: JSON?
                            ) {
                                //TODO send tweet back to timelineActivity
                                Log.i(TAG,"successfully published tweet")

                                val tweet = Tweet.fromJson(json!!.jsonObject)

                                val intent = Intent()
                                intent.putExtra("tweet", tweet)
                                setResult(RESULT_OK,intent)
                                finish()

                            }
                            override fun onFailure(
                                statusCode: Int,
                                headers: Headers?,
                                response: String?,
                                throwable: Throwable?
                            ) {
                                Log.i(TAG,"published tweet failed")
                            }
                        })
                    }
        }
    }
    companion object{
        val TAG = "ComposeActivity"
    }
}