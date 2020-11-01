package com.example.balouchi.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.location.Location
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.balouchi.R
import com.example.balouchi.custome_application
import com.example.balouchi.data.repository.login.user.product.DMS
import com.example.balouchi.data.repository.login.user.user.user_data
import com.example.balouchi.ui.home.home
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.formats.MediaView
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.ad.view.*
import kotlinx.android.synthetic.main.custome_toolbar.view.*
import kotlinx.android.synthetic.main.home.*
import kotlin.reflect.KClass
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds

fun Context?.toastr(text: String){
    val toast= Toast.makeText(this, text, Toast.LENGTH_SHORT)
    val v = toast.view.findViewById<TextView>(android.R.id.message)
    v.setTextColor(Color.RED)
    toast.show()
}
fun Activity.sound(){
    MediaPlayer.create(this, R.raw.juntos).start()
}
fun View.visibile(){
    this.visibility= View.VISIBLE
}
fun View.invisibile(){
    this.visibility= View.INVISIBLE
}
fun View.gone(){
    this.visibility= View.GONE
}
fun Context?.getSpots(): AlertDialog {
    return   SpotsDialog.Builder().setContext(this).setCancelable(true).setMessage("الرجاء الإنتظار").build()
}
fun Context?.toastg(text: String){
    val toast= Toast.makeText(this, text, Toast.LENGTH_SHORT)
    val v = toast.view.findViewById<TextView>(android.R.id.message)
    v.setTextColor(Color.GREEN)
    toast.show()
}
fun Context?.getvisibility():Boolean{
    val a= this!!.getSharedPreferences("internet", Context.MODE_PRIVATE)
    return a.getBoolean("internet", true)
}

fun Context?.setvisibility(internet: Boolean){
    val a= this!!.getSharedPreferences("internet", Context.MODE_PRIVATE)
    a.edit().putBoolean("internet", internet).apply()
}
fun Context?.existuser():Boolean{
    val a= this!!.getSharedPreferences("user", Context.MODE_PRIVATE)
    return (a.getString("email", "default")!="default")
}

fun Context?.user_login():Pair<String, String>{
    val a= this!!.getSharedPreferences("user", Context.MODE_PRIVATE)
    return Pair((a.getString("email", "default")!!), (a.getString("password", "default")!!))
}

fun Context?.save_chat(uid: String?=""){
    val a=this!!.getSharedPreferences("user", Context.MODE_PRIVATE)
    a.edit().putString("uid", uid).apply()
}

fun token(){
    FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            result ->
        FirebaseAuth.getInstance().currentUser?.uid?.let {
            FirebaseFirestore.getInstance().document("users/"+it).update(hashMapOf("token" to result.token) as Map<String, Any>)
        }
    }
}

fun Context?.load_chat():String?{
    val a= this!!.getSharedPreferences("user", Context.MODE_PRIVATE)
    return a.getString("uid","")
}

fun Context?.saveuser(email: String?, password: String?){
    val a=this!!.getSharedPreferences("user", Context.MODE_PRIVATE)
    a.edit().apply{
        putString("email", email).apply()
        putString("password", password).apply()
    }
}
fun Context?.getApp(): custome_application {
    return this!!.applicationContext as custome_application
}
fun isInternetAvailable(context: Context?): Boolean {
    var result = false
    val connectivityManager =
        context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw =connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        result = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } else {
        connectivityManager.run {
            connectivityManager.activeNetworkInfo?.run {
                result = when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }

            }
        }
    }

    return result
}
fun Context?.toast(text: String){
    Toast.makeText(this!!, text, Toast.LENGTH_LONG).show()
}
fun Context.loaduri(Uri: Any, image2: ImageView, fn: Unit? = null){
    Glide
        .with(this)
        .load(Uri).listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {

                fn?.let {
                    it
                }

                return false
            }
        })
        .transition(GenericTransitionOptions.with(R.anim.show_frame))
        .into(image2)
}
fun Context.openWebPage(id: Int) {
    val string= this.resources.getString(id)
    val webpage= Uri.parse(string)
    val intent = Intent(Intent.ACTION_VIEW, webpage)
    if (intent.resolveActivity(this.getPackageManager()) != null) {
        this.startActivity(intent)
    }
}
fun Activity?.toolbar_text(text: String){
    (this as home).tool.txt.text=text
}

fun isFacebook():Boolean{
    var res=false
    FirebaseAuth.getInstance().getCurrentUser()!!.getProviderData().forEach {
        if (it.providerId=="facebook.com")
            res=true
    }
    return res
}

fun calcule_distance(a: DMS, b: DMS):Double{
    val startPoint = Location("locationA")
    startPoint.apply {
        latitude = a.lat!!
        longitude = a.lng!!
    }
    val endPoint = Location("locationA")
    endPoint.apply {
        latitude = b.lat!!
        longitude = b.lng!!
    }
    return startPoint.distanceTo(endPoint).toDouble()/1000
}

fun isEmail():Boolean{
    var res=false
    FirebaseAuth.getInstance().getCurrentUser()!!.getProviderData().forEach {
        if (it.providerId=="password")
            res=true
    }
    return res
}

fun RecyclerView.Adapter<*>?.notify(a:Int, b:Int,inserted:Boolean?=true){
    this?.apply {
        inserted?.let {
            if (inserted)
                notifyItemInserted(a)
            else notifyItemChanged(a)
            notifyItemRangeChanged(a,b)
            return
        }
        notifyItemRemoved(a)
        notifyItemRangeChanged(a,b)
    }
}

fun <type> List<type>.slice(start:Int,end:Int):ArrayList<type>{
    var pos=start
    val result=ArrayList<type>()
    if (start>size-1)
        return result
    while (pos<size && pos<=end){
        result.add(get(pos++))
    }
    return result
}

@ExperimentalTime
fun Runnable.check_online(handler: Handler, uid: String, job:()->Unit){


           FirebaseFirestore.getInstance().document("users/"+uid).get().addOnSuccessListener {
               it.toObject(user_data::class.java)?.let {
                   it.lastlogin?.let {
                       if (((Timestamp.now().toDate().time.milliseconds.toLongMilliseconds()-it)<=60000)){
                           handler.postDelayed(this,60000)
                           return@addOnSuccessListener
                       }
                       job()
                       handler.removeCallbacks(this)
                   }

               }
           }
     }

fun Activity.myad():InterstitialAd{
    val mInterstitialAd = InterstitialAd(this)
    mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"
    mInterstitialAd.loadAd(AdRequest.Builder().build())
    mInterstitialAd.adListener = object : AdListener() {
        override fun onAdClosed() {
            mInterstitialAd.loadAd(AdRequest.Builder().build())
        }
    }
    return mInterstitialAd
}


fun Context?.save_new(go: Boolean){
    val a= this!!.getSharedPreferences("new", Context.MODE_PRIVATE)
    a.edit().putBoolean("new", go).apply()
}

fun Context?.load_new():Boolean?{
    val a= this!!.getSharedPreferences("new", Context.MODE_PRIVATE)
    return a.getBoolean("new", false)
}

fun TextInputEditText.filter():String{
    return text.toString().trim()
}

fun Context?.save_vue(path:String){
    val a= this!!.getSharedPreferences("vue", Context.MODE_PRIVATE)
    val res=Gson().fromJson<ArrayList<String>>(a.getString("vue", null), object : TypeToken<ArrayList<String?>?>() {}.getType())
    res.add(path)
    a.edit().putString("vue",Gson().toJson(res)).apply()
}

fun Context?.load_vue(path: String):Boolean{
    val a= this!!.getSharedPreferences("vue", Context.MODE_PRIVATE)
    a.getString("vue", null)?.let {
        return Gson().fromJson<ArrayList<String>>(it, object : TypeToken<ArrayList<String?>?>() {}.getType()).contains(path)
    }
    a.edit().putString("vue",Gson().toJson(ArrayList<String>())).apply()
    return false
}


fun log(text:Any?){
    Log.e("myapp",text.toString())
}

fun change_switch(switch: Switch){
    switch.isChecked=!switch.isChecked
}
fun <type>fromJson(hash: Any, to: KClass<*>):type{
    return (ObjectMapper().convertValue(hash, to.java) as type)
}

fun exist(){
    FirebaseAuth.getInstance().currentUser?.uid?.let {
        FirebaseFirestore.getInstance().document("users/"+it).update(hashMapOf("lastlogin" to null) as Map<String, Any>)
    }
}


fun <type> add_new(array: ArrayList<type?>?):ArrayList<type?>{

    val res=ArrayList<type?>()

    array!!.forEach {
        it?.let {
            if (array.indexOf(it)%3==0)
                res.add(null)
        }
        res.add(it)
    }
    return res
}


fun ToMap(myobject: Any):Map<String, Any>{
   return ObjectMapper().convertValue(myobject, object :
       TypeReference<Map<String, Any>>() {})
}

fun Context.go(where: KClass<*>, data: HashMap<String, String>? = null, other: Bundle? = null){
    val intent=Intent(this, where.java).apply {
        data?.apply {
            forEach{it.apply {  putExtra(key, value) }}
        }
    }
    startActivity(intent, other)
}

fun Context.go(where: KClass<*>, data: HashMap<String, Boolean>? = null){
    val intent=Intent(this, where.java)
    startActivity(intent).apply {
        data?.apply {
            data.forEach{it.apply {  intent.putExtra(key, value) }}
        }
    }
}

fun Context.permissions(vararg ids: String):Boolean{
    ids.forEach { if (ContextCompat.checkSelfPermission(this, it)!=PackageManager.PERMISSION_GRANTED) return false}
    return true
}
fun Activity.request_permissions(vararg ids: String){
    ActivityCompat.requestPermissions(this, ids, 120)
}

fun Context?.savenever(who:String,save: Boolean){
    val a=this!!.getSharedPreferences("never", Context.MODE_PRIVATE)
    a.edit().putBoolean(who, save).apply()
}

fun Context?.save_notification(who:String,save: String){
    val a=this!!.getSharedPreferences("never", Context.MODE_PRIVATE)
    a.edit().putString(who, save).apply()
}

fun Context?.load_notification(who:String):String?{
    val a= this!!.getSharedPreferences("never", Context.MODE_PRIVATE)
    return a.getString(who, null)
}
fun Context?.loadnever(who:String):Boolean?{
    val a= this!!.getSharedPreferences("never", Context.MODE_PRIVATE)
    return a.getBoolean(who, false)
}
fun random():String{
 return  FirebaseFirestore.getInstance().collection("test").document().id

}
fun View.isVisible():Boolean{
    return this.visibility==View.VISIBLE
}

fun Activity.prepare_snake(v: View, permission: String = ""): Snackbar? {

    val snake= Snackbar.make(v, " يجب أن تفعل صلاحية " + permission, Snackbar.LENGTH_LONG).apply {
        view.setBackgroundColor(Color.BLACK)
        setAction("وافق") {
            startActivity(
                Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + packageName)
                ).apply { setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) })
        }
        view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            .apply {
                typeface = ResourcesCompat.getFont(this@prepare_snake, R.font.et)
                gravity = Gravity.CENTER
            }

        setActionTextColor(Color.RED)

    }
    return snake
}

fun View.isGone():Boolean{
    return this.visibility==View.GONE
}

fun <type>remove_item(array: ArrayList<type?>, steps: Int = 4):ArrayList<type?>{
    val res=ArrayList<type?>()
    array.filter { it!=null }.apply {
        forEach {
            if (this.indexOf(it)%steps==0)
                res.add(null)
            res.add(it)
        }
    }
    return res
}
fun Context.anim(v: View, id: Int, end: (() -> Unit)? = null){
    v.startAnimation(AnimationUtils.loadAnimation(this, id).apply {
        setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationEnd(animation: Animation?) {
                end?.let {
                    it()
                }
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationStart(animation: Animation?) {

            }
        })
    })
}
fun ImageView.svg(id: Int){
    this.setImageDrawable(
        VectorDrawableCompat.create(resources, id, null)
    )
}
fun Context.svg(id: Int): VectorDrawableCompat {
    return  VectorDrawableCompat.create(resources, id, null)!!
}
fun Any.underline(){
    when(this){
        is TextView -> this.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        is TextInputEditText -> this.paintFlags = Paint.UNDERLINE_TEXT_FLAG
    }
}
fun String.inc(n: Int):String{
  return (this.toInt()+n).toString()
}
fun String.dec(n: Int):String{
    return (this.toInt()-n).toString()
}

fun max(vararg list: TextInputEditText, max: Int = 1){
    list.forEach {
        it.apply {
            if (isVisible())
                    addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(s: Editable?) {
                            text?.apply {
                                if (getLineCount() > max)
                                    delete(length - 1, length)
                            }


                        }

                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {

                        }

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {

                        }
                    })
        }
    }
}

fun check_inputs(vararg list: TextInputEditText):Boolean{
    var res=true
    list.forEach {
        it.apply {
            if (text.isNullOrEmpty()){
                if (res)
                    res=false.also { requestFocus() }
                error="لا يجب أن يكون فارغا "

            }
        }
    }
    return res
}

fun manage_edit(
    vararg list:TextInputEditText,
    edit:TextInputEditText?=null,
    b: ((String?) -> Unit)? = null,
    o: ((String?) -> Unit)? = null,
    a: ((String?) -> Unit)? = null
){

    if (list.size>0){
        list.forEach {
            it.addTextChangedListener(watcher(b,o,a))
        }
        return
    }
    edit?.apply {
        addTextChangedListener(watcher(b,o,a))
    }
}

fun watcher(
    b: ((String?) -> Unit)? = null,
    o: ((String?) -> Unit)? = null,
    a: ((String?) -> Unit)? = null
):TextWatcher{
    return object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            a?.let {
                it(s.toString())
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            b?.let {
                it(s.toString())
            }
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            o?.let {
                it(s.toString())
            }
        }
    }
}

fun SeekBar.manage_seekbar(fn: (Int) -> Unit){
    setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            fn(progress)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {

        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {

        }

    })
}

fun Context.refresh(card: MaterialCardView,uu:UnifiedNativeAd?){

    var u=uu

    val adLoader = AdLoader.Builder(applicationContext, "ca-app-pub-3940256099942544/2247696110")

    adLoader.forUnifiedNativeAd { ad : UnifiedNativeAd ->
        u?.let {
            u=ad
        }
        val unifiednativeadview=(LayoutInflater.from(applicationContext).inflate(R.layout.ad,null) as UnifiedNativeAdView)
        theAd(ad,unifiednativeadview)
        card.removeAllViews()
        card.addView(unifiednativeadview)
    }.build()

    val b=adLoader.withAdListener(object: AdListener(){
        override fun onAdFailedToLoad(p0: Int) {
            super.onAdFailedToLoad(p0)
        }
    }).build()
    b.loadAd(AdRequest.Builder().build())

}

fun theAd(nativead: UnifiedNativeAd, adview: UnifiedNativeAdView){
    adview.apply {
        headlineView=ad_headline
        advertiserView=ad_advertiser
        bodyView=ad_body_text
        starRatingView=star_rating
        mediaView=media_view as MediaView
        callToActionView=add_call_to_action
        iconView=adv_icon
        mediaView.setMediaContent(nativead.mediaContent)
        (headlineView as TextView).text=nativead.headline

        if (nativead.body==null)
            bodyView.invisibile()
        else
            (bodyView as TextView).text=nativead.body
        bodyView.visibile()

        if (nativead.advertiser==null)
            advertiserView.invisibile()

        else
            (advertiserView as TextView).text=nativead.advertiser
        advertiserView.visibile()


        if (nativead.starRating==null)
            starRatingView.invisibile()

        else {
            (starRatingView as RatingBar).rating = nativead.starRating.toFloat()
            starRatingView.visibile()
        }


        if (nativead.icon==null)
            iconView.invisibile()

        else {
            (iconView as ImageView).setImageDrawable(nativead.icon.drawable)
            iconView.visibile()
        }

        if (nativead.callToAction==null)
            callToActionView.invisibile()

        else {
            (callToActionView as Button).text=nativead.callToAction
            callToActionView.visibile()
        }

        this.setNativeAd(nativead)

    }
}
