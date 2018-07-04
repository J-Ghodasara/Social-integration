package com.example.jayghodasara.socialintegration

import android.app.DownloadManager
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.opengl.Visibility
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import com.androidquery.AQuery
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginResult
import kotlinx.android.synthetic.main.activity_main.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

import com.facebook.login.LoginManager
import com.facebook.login.widget.ProfilePictureView
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    lateinit var callbackManager: CallbackManager
    lateinit var aquery:AQuery



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)

twitter.setOnClickListener(View.OnClickListener {
    var intent2:Intent=Intent(applicationContext,TwitterIntegration::class.java)
    startActivity(intent2)
})

        share.setOnClickListener(View.OnClickListener{
         var intent:Intent= Intent(this,ShareFeed::class.java)
            startActivity(intent)
        })



        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired
        aquery= AQuery(this)

        if(!isLoggedIn){
            linear.visibility=View.GONE
        }


        callbackManager = CallbackManager.Factory.create()

        login_button.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                Toast.makeText(applicationContext, "Login Successfull", Toast.LENGTH_LONG).show()

                var request:GraphRequest= GraphRequest.newMeRequest(result!!.accessToken) { `object`, response ->
                    email.text=`object`.getString("email")
                    name.text=`object`.getString("name")
                  //  birthday.text=`object`.getString("birthday")
                  //  gender.text=`object`.getString("gender")
                    image.presetSize= ProfilePictureView.NORMAL
                    image.profileId=`object`.getString("id")
                    linear.visibility = View.VISIBLE
                }

                var bundle:Bundle= Bundle()
                bundle.putString("fields","id,name,email,gender,birthday")
                request.parameters=bundle
                request.executeAsync()
            }

            override fun onCancel() {
                Toast.makeText(applicationContext, "Login Cancelled", Toast.LENGTH_LONG).show()
            }

            override fun onError(error: FacebookException?) {
                Toast.makeText(applicationContext, "Login Failed", Toast.LENGTH_LONG).show()
            }
        })

        login_button.setReadPermissions(Arrays.asList("public_profile","email"))

        login_button.setOnClickListener(View.OnClickListener {
            if (!isLoggedIn) {
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"))
            }else{
                linear.visibility=View.GONE
            }

        })
        gmail.setOnClickListener(View.OnClickListener {
            var int:Intent=Intent(this,Googleplus::class.java)
            startActivity(int)
        })


        try {
            var info: PackageInfo = packageManager.getPackageInfo("com.example.jayghodasara.socialintegration", PackageManager.GET_SIGNATURES)
            for (signature: Signature in info.signatures) {
                var md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.i("KEY", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}
