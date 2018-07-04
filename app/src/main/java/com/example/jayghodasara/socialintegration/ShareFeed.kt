package com.example.jayghodasara.socialintegration

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.model.SharePhoto
import com.facebook.share.model.SharePhotoContent
import com.facebook.share.widget.ShareDialog
import kotlinx.android.synthetic.main.activity_share_feed.*

class ShareFeed : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)
        setContentView(R.layout.activity_share_feed)


share.setOnClickListener {
    var title:String= Title.text.toString()
    var desc:String=Description.text.toString()
    var shareLinkContent:ShareLinkContent= ShareLinkContent.Builder()
            .setContentTitle(title)
            .setContentDescription(desc)
            .setContentUrl(Uri.parse("http://developers.facebook.com/android"))
            .build()
//var img:Bitmap= BitmapFactory.decodeResource(resources,R.drawable.b)
//    var photo:SharePhoto= SharePhoto.Builder().setBitmap(img).build()
//
//    var sharephoto:SharePhotoContent= SharePhotoContent.Builder().addPhoto(photo).build()

    ShareDialog.show(this,shareLinkContent)

}

    }
}
