package com.example.jayghodasara.socialintegration

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.squareup.picasso.Picasso
import com.twitter.sdk.android.core.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_twitter_integration.*
import twitter4j.User
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken
import twitter4j.auth.RequestToken
import java.net.URL


class TwitterIntegration : AppCompatActivity() {


    lateinit var consumerkey:String
    lateinit var consumer_secret_key:String
    lateinit var callbackUrl:String
    lateinit var oAuthverifier:String
    lateinit var accessToken:twitter4j.auth.AccessToken
    lateinit var requestToken: RequestToken
    var progressBar: ProgressDialog? = null
    lateinit var oAuthUrl:String
    lateinit var twitter_lib: twitter4j.Twitter
    var dialog: Dialog? = null
    lateinit var verifier:String
    lateinit var token:String
    lateinit var secret:String
     var bitmap:Bitmap?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var config:TwitterConfig=TwitterConfig.Builder(this).logger(DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(TwitterAuthConfig(resources.getString(R.string.twitter_key),resources.getString(R.string.twitter_secret)))
                .debug(true)
                .build()
        Twitter.initialize(config)
        setContentView(R.layout.activity_twitter_integration)
        consumerkey=getString(R.string.twitter_key)
        consumer_secret_key=getString(R.string.twitter_secret)
        callbackUrl=getString(R.string.twitter_callback)
        oAuthverifier=getString(R.string.twitter_oauth_verifier)



        tweet.setOnClickListener(View.OnClickListener {

            var twitter4:twitter4j.Twitter=TwitterFactory().instance
            var accestoken:AccessToken= AccessToken(token,secret)
        twitter4.setOAuthConsumer(consumerkey,consumer_secret_key)
            twitter4.oAuthAccessToken=accestoken
            try{
                val t:Thread= Thread(Runnable {
                    twitter4.updateStatus(msg.text.toString())
                    Toast.makeText(applicationContext,"Tweet Successfull",Toast.LENGTH_LONG).show()
                })
                t.start()

            }catch (e:TwitterException){
                e.printStackTrace()
            }

        })




        default_twitter_login_button.callback=object:Callback<TwitterSession>(){
            override fun success(result: Result<TwitterSession>?) {
               Toast.makeText(applicationContext,"Login Successfull",Toast.LENGTH_LONG).show()
                val session = TwitterCore.getInstance().sessionManager.activeSession
                val authToken = session.authToken
                 token = authToken.token
                 secret = authToken.secret
                Toast.makeText(applicationContext,token.toString()+" "+secret.toString(),Toast.LENGTH_LONG).show()
                val authClient = TwitterAuthClient()
                authClient.requestEmail(session, object : Callback<String>() {
                    override fun success(result: Result<String>) {
                       t_email.text= result.data
                    }

                    override fun failure(exception: TwitterException) {
                        // Do something on failure
                    }
                })
                TwitterCore.getInstance().apiClient.accountService.verifyCredentials(true, false,false).enqueue(object:Callback<com.twitter.sdk.android.core.models.User>(){
                    override fun success(result: Result<com.twitter.sdk.android.core.models.User>?) {
                        var url_img:String= result!!.data.profileImageUrl
                        Picasso.get().load(url_img).into(img)




                        setimg()

                    }


                    override fun failure(e: TwitterException) {}
                })

                login(session)
            }

            fun setimg(){
                if(bitmap!=null){
                    img.setImageBitmap(bitmap)
                }

            }
            override fun failure(exception: TwitterException?) {
                Toast.makeText(applicationContext,"Login Failed",Toast.LENGTH_LONG).show()
            }

        }

//        default_twitter_login_button.setOnClickListener(View.OnClickListener {
//
//            GetTwitterTokenTask(this).execute()
//        })




    }

    fun login(session: TwitterSession) {
        val username = session.userName
        twitter_name.text=username
        id.text=session.userId.toString()
    }


//    fun login(){
//        if(!isTwitterLoggedIn)
//    }

//    fun callbackDataFromAsync(user: User){
//        twitter_name.text=user.name
//        id.text=user.id.toString()
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        default_twitter_login_button.onActivityResult(requestCode,resultCode,data)
        super.onActivityResult(requestCode, resultCode, data)


    }




}
