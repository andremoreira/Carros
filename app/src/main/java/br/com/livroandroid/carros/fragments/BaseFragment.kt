package br.com.livroandroid.carros.fragments

import android.content.Context
import android.support.v4.app.Fragment

open class BaseFragment : Fragment() {

    override fun getContext(): Context {
        return super.getContext()!!
    }
}