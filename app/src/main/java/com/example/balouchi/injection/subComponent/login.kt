package com.example.balouchi.injection.subComponent

import com.example.balouchi.ui.login.Login_viewmodel
import com.example.balouchi.ui.login.login_fragment
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent
interface login {

    fun getLogin(Login_viewmodel: Login_viewmodel)


    @Subcomponent.Builder
    interface build {

        @BindsInstance
        fun setlogin_Fragment(loginFragment: login_fragment): build

        @BindsInstance
        fun setLogin_viewmodel(Login_viewmodel: Login_viewmodel): build

        fun builder(): login

    }
}