package com.example.balouchi.ui.login

import android.app.Activity
import android.app.AlertDialog
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.balouchi.R
import com.example.balouchi.data.repository.login.user.product.Pictures
import com.example.balouchi.data.repository.login.user.user.Notificationn
import com.example.balouchi.data.repository.login.user.user.Personal_Info
import com.example.balouchi.data.repository.login.user.user.auth.Authh
import com.example.balouchi.data.repository.login.user.user.auth.manage_user
import com.example.balouchi.data.repository.login.user.user.user_data
import com.example.balouchi.ui.home.home
import com.example.balouchi.user
import com.example.balouchi.util.*
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.Profile
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.login.view.*
import javax.inject.Inject


class Login_viewmodel() : ViewModel() {

    var email: String? = null
    var password: String? = null
    var Mycontext: Activity? = null

    lateinit var mydialog: AlertDialog
    lateinit var mynavController: NavController
    lateinit var lifecycleOwner: login_fragment
    lateinit var myauth: FirebaseAuth
    lateinit var My_Auth: Authh
    lateinit var My_manage_user: manage_user

    @Inject
    lateinit var gmail: gmail
    @Inject
    lateinit var facebook: facebook


    fun Click(V: View) {
        mynavController.apply {
        when (V.id) {
            R.id.signin -> SignIn()
            R.id.forget -> navigate(R.id.action_login_fragment_to_forget_fragment)
            R.id.mobile -> navigate(R.id.action_login_fragment_to_phone_fragment)
            R.id.signup -> navigate(R.id.action_login_fragment_to_register_fragment)
            R.id.gmail ->  gmail.signIn()
            R.id.fb ->     goFacebook()
        }
        }
    }

    fun SignIn() {
        Mycontext!!.apply {
            lifecycleOwner.apply {
                if (testinputs(
                        email_edit,
                        password_edit)
                ) {
                    if (!isEmailValid(email!!)) {
                        email_edit.apply {
                            error = "نمط الإيميل غير صحيح"
                            requestFocus()
                        }
                        toastr("نمط الإيميل غير صحيح")
                        return
                    }

                    mydialog.show()
                    My_Auth.login(email!!, password!!,true).observe(lifecycleOwner, Observer {

                        mydialog.dismiss()
                        if (it !is Boolean){
                            if (it is FirebaseAuthInvalidCredentialsException) {
                                toastr("كلمة المرور و الإيميل غير متطابقين")
                                email_edit.apply {
                                    error =  "كلمة المرور و الإيميل غير متطابقين"
                                    requestFocus()
                                }
                                password_edit.error = ""
                                return@Observer
                            }
                            if (it is FirebaseAuthInvalidUserException) {
                                when (it.errorCode) {
                                    "ERROR_USER_NOT_FOUND" -> {
                                        email_edit.apply {
                                            error ="إيميل خاطأ ، أو حساب غير موجود"
                                            requestFocus()
                                        }
                                        toastr("إيميل خاطأ ، أو حساب غير موجود")
                                    }
                                    "ERROR_WRONG_PASSWORD" -> {
                                        password_edit.apply {
                                            error = "كلمة السر خاطئة"
                                            requestFocus()
                                        }
                                        toastr("كلمة السر خاطئة")
                                    }
                                    "ERROR_USER_DISABLED" ->  toastr("الحساب مغلق من طرف الأدارة")
                                    else ->  toastr("آسف حاول لاحقا")
                                }
                                return@Observer
                            }
                        }
                        else {
                            toastg("مرحبا")
                            if (remember.isChecked)
                                saveuser(email, password)
                            if (it)
                                save_new(true)
                            go(home::class, hashMapOf("first_time" to "1"))
                            finish()
                            return@Observer
                        }
                        toastr("آسف حاول لاحقا")

                    })
                    return
                }
                toastr("أملأ البيانات")
            }
        }
    }

    fun goFacebook() {
            AccessToken.getCurrentAccessToken()?.let {
                if (!it.isExpired) {
                    val request = GraphRequest.newMeRequest(
                        it
                    ) {obj, response ->
                        firebaseAuth(it,false)
                    }

                    request.executeAsync()
                    return
                }
            }
            facebook.SignIn()
    }

    fun firebaseAuth(token:Any?=null,isGmail:Boolean=true) {
            Mycontext!!.apply {
                token?.let {
            myauth.signInWithCredential(if (isGmail) (GoogleAuthProvider.getCredential((it as GoogleSignInAccount).idToken,null)) else FacebookAuthProvider.getCredential((it as AccessToken).token))
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        if (task.result!!.additionalUserInfo!!.isNewUser){
                            save_new(true)
                            mydialog.show()
                            myauth.currentUser!!.apply {

                            My_manage_user.Function<Boolean>("Credential",
                                hashMapOf("info" to
                                        ToMap(user_data(uid = uid,personal_info = Personal_Info(
                                    Pictures(path =
                                    if (isGmail) photoUrl.toString() else "https://graph.facebook.com/${
                                    Profile.getCurrentProfile()
                                    .getId()}/picture?type=large&redirect=true&width=600&height=600"),displayName,email))),
                                    "notifications" to ToMap(Notificationn())
                                )
                            ).observe(lifecycleOwner,
                                Observer {
                                    if (it){
                                        mydialog.dismiss()
                                        toastg("مرحبا")
                                        go(home::class, hashMapOf("first_time" to "1"))
                                        finish()
                                        return@Observer
                                    }
                                    toastr("آسف حاول لاحقا")
                                })

                        }
                          return@addOnCompleteListener
                        }
                        toastg("مرحبا")
                        go(home::class, hashMapOf("first_time" to "1"))
                        finish()
                        return@addOnCompleteListener
                }
                    toastr("آسف حاول لاحقا")
                }
                }
            }
    }

    fun prepare() {
            (lifecycleOwner.requireActivity() as user).apply {
                Mycontext =this
                getApp().User.getLogin().setLogin_viewmodel(this@Login_viewmodel).setlogin_Fragment(lifecycleOwner).builder().getLogin(this@Login_viewmodel)
                mynavController = Navigation.findNavController(lifecycleOwner.binding.root)
                myauth=FirebaseAuth.getInstance()
                mydialog = getSpots()
                    My_Auth=Authh
                    My_manage_user=manage_user
            }
            lifecycleOwner.apply {
                max(email_edit,password_edit)
            }
        }

    }
