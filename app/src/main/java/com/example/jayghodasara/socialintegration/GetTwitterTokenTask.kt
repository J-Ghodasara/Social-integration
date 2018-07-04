package com.example.jayghodasara.socialintegration

import android.app.Dialog
import android.os.AsyncTask
import com.facebook.AccessToken
import twitter4j.Twitter
import twitter4j.TwitterException
import twitter4j.TwitterFactory
import twitter4j.auth.RequestToken


import java.lang.reflect.Constructor
import android.app.ProgressDialog
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.view.Window
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import kotlinx.android.synthetic.main.dialogview.*
import twitter4j.User


class GetTwitterTokenTask() : AsyncTask<String, Any, String>() {

    lateinit var activity: TwitterIntegration
    lateinit var twitter: Twitter
    var consumer_key: String = "1sD3tBp6UWNmIqOB105b4Ql2T"
    var consumer_secret: String = "faLRwqutUDNxt1CmG5y0D5yWxlH7K3bq1RhQmaEMDYGNFy2ppL"
    lateinit var accessToken:twitter4j.auth.AccessToken
    lateinit var requestToken:RequestToken
    lateinit var oAuthUrl:String
    var dialog: Dialog? = null
    lateinit var verifier:String


    var progressBar: ProgressDialog? = null



    constructor(activity: TwitterIntegration) : this() {
        this.activity=activity
        twitter= TwitterFactory().instance

        twitter.setOAuthConsumer(consumer_key,consumer_secret)

    }

    override fun doInBackground(vararg params: String?): String {

        try{
            requestToken= twitter.oAuthRequestToken
            oAuthUrl=requestToken.authorizationURL
         }catch (e:TwitterException){
            e.printStackTrace()
        }
        return oAuthUrl

    }



    override fun onPostExecute(result: String?) {
        if(result != null){

            dialog= Dialog(activity)
            dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog!!.setContentView(R.layout.dialogview)
            var web:WebView=dialog!!.webview
            web.settings.javaScriptEnabled=true
            dialog!!.dismiss()
            web.loadUrl(result)

            web.webViewClient= object : WebViewClient(){



                var authComplete = false

//                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
//                    super.onPageStarted(view, url, favicon)
//                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    if(url!!.contains("oauth_verifier") && authComplete == false){
                        authComplete=true

                        var uri:Uri=Uri.parse(url)
                        verifier=uri.getQueryParameter("oauth_verifier")

                        dialog!!.dismiss()
                        AccessTokenGet().execute()
                    }
                }
            }



        }
    }

    override fun onPreExecute() {
        super.onPreExecute()
        progressBar= ProgressDialog.show(activity,"Connecting","Fetching Data...")
        progressBar!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
    }

   inner class AccessTokenGet:AsyncTask<String,String, User>(){
        override fun doInBackground(vararg params: String?): User? {

            var user: User?=null
            try{
                accessToken= twitter.getOAuthAccessToken(requestToken,verifier)
                user=twitter.showUser(accessToken.userId)

            }catch (e:TwitterException){
                e.printStackTrace()
            }

            return  user
        }

       override fun onPostExecute(result: User?) {
           if(result==null){
               Log.i("User","Null")
           }else{
             // activity.callbackDataFromAsync(result)
           }
           progressBar!!.dismiss()
       }

       override fun onPreExecute() {
           super.onPreExecute()
           progressBar= ProgressDialog.show(activity,"Connecting","Fetching Data...")
           progressBar!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)

       }
   }
}


