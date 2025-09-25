package pt.ulusofona.deisi.a2020.cm.g7.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.OnClick
import kotlinx.android.synthetic.main.fragment_contacts.*
import pt.ulusofona.deisi.a2020.cm.g7.R

class ContactsFragment : Fragment() {

    @OnClick(R.id.button_telephone_contacts)
    fun onToggleTelephoneContacts() {
        if(telephone_contacts.visibility == View.VISIBLE) {
            telephone_contacts.visibility = View.GONE
            button_telephone_contacts.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_more, 0, 0, 0)
        } else {
            telephone_contacts.visibility = View.VISIBLE
            button_telephone_contacts.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_less, 0, 0, 0)
        }
    }

    @OnClick(R.id.button_digital_contacts)
    fun onToggleDigitalContacts() {
        if(digital_contacts.visibility == View.VISIBLE) {
            digital_contacts.visibility = View.GONE
            button_digital_contacts.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_more, 0, 0, 0)
        } else {
            digital_contacts.visibility = View.VISIBLE
            button_digital_contacts.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_less, 0, 0, 0)
        }
    }

    @OnClick(R.id.button_phone_sns24)
    fun onClickCallSNS24() {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:+351808242424")
        startActivity(intent)
    }

    @OnClick(R.id.button_phone_social_security)
    fun onClickCallSocialSecurity () {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:+351300502502")
        startActivity(intent)
    }

    @OnClick(R.id.button_phone_consular)
    fun onClickCallConsular() {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:+351217929714")
        startActivity(intent)
    }

    @OnClick(R.id.button_phone_consular2)
    fun onClickCallConsular2() {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:+351961706472")
        startActivity(intent)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_contacts, container, false)
        ButterKnife.bind(this, view)
        return view
    }
}