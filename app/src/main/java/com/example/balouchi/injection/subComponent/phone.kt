package com.example.balouchi.injection.subComponent

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.example.balouchi.ui.phone.Phone_viewmodel
import com.example.balouchi.ui.update_phone.UpdatePhone_Viewmodel
import dagger.BindsInstance
import dagger.Subcomponent


@Subcomponent
interface phone {

    fun getPhone(Phone_viewmodel:Phone_viewmodel)

    fun getPhone(UpdatePhone_Viewmodel: UpdatePhone_Viewmodel)


    @Subcomponent.Builder
    interface build {

        @BindsInstance
        fun setActivity(Activity:Activity): build

        @BindsInstance
        fun setNumber(number:String): build

        @BindsInstance
        fun setPhone_viewmodel(Phone_viewmodel: ViewModel): build

        fun builder(): phone

    }
}