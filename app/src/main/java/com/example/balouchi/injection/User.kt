package com.example.balouchi.injection

import com.example.balouchi.all_settings
import com.example.balouchi.injection.subComponent.login
import com.example.balouchi.injection.subComponent.phone
import com.example.balouchi.services.online
import com.example.balouchi.ui.forget.Forget_viewmodel
import com.example.balouchi.ui.hide_show.Hide_show_viewmodel
import com.example.balouchi.ui.home.home
import com.example.balouchi.user
import com.example.balouchi.ui.login.NoInternet
import com.example.balouchi.ui.product.product
import com.example.balouchi.ui.profile.Profile_viewmodel
import com.example.balouchi.ui.profile.profile
import com.example.balouchi.ui.register.Register_viewmodel
import com.example.balouchi.ui.settings.Settings_viewmodel
import com.example.balouchi.ui.verify.Verify_viewmodel
import com.example.balouchi.ui.verify.verify_email
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component
interface User {

    fun getLogin():login.build
    fun getPhone(): phone.build
    fun getInternetBroadcast(user: user)
    fun getInternetBroadcast(NoInternet: NoInternet)
    fun getInternetBroadcast(home: home)
    fun getAuth(verify_email: verify_email)
    fun Injectsettings(allSettings: all_settings)
    fun InjectProfile(profile: profile)
    fun InjectProduct(product:product)


}
