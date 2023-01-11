package com.example.shifttestingproject

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.shifttestingproject.databinding.ActivityMainBinding
import com.example.shifttestingproject.db.BinDbModel
import com.example.shifttestingproject.fragments.dialog.WhatCallNumberDialogFragment
import com.example.shifttestingproject.fragments.history.HistoryFragment
import com.example.shifttestingproject.fragments.history.HistoryViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    var viewModel = MainViewModel()
    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var pLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        clearAllFragments()
        init()
        APP = this
        registerPermissionListener()
    }

    override fun onResume() {
        super.onResume()

        // подставляет полученные в запросе данные в поля
        viewModel.response.observe(this) {
            tv_main_scheme.text = it.body()?.scheme ?: NONE
            tv_main_brand.text = it.body()?.brand ?: NONE
            tv_main_type.text = it.body()?.type ?: NONE
            tv_main_prepaid.text = convertBooleanToString(it.body()?.prepaid)
            tv_main_length.text = it.body()?.number?.length?.toString() ?: NONE
            tv_main_luhn.text = convertBooleanToString(it.body()?.number?.luhn)
            tv_main_alpha2.text = it.body()?.country?.alpha2
            tv_main_numeric.text = it.body()?.country?.numeric
            tv_main_emoji.text = it.body()?.country?.emoji
            tv_main_country_name.text = it.body()?.country?.name
            tv_main_currency.text = it.body()?.country?.currency
            tv_main_latitude.text = it.body()?.country?.latitude?.toString() ?: NONE
            tv_main_longitude.text = it.body()?.country?.longitude?.toString() ?: NONE
            tv_main_bank_name.text = it.body()?.bank?.name
            tv_main_bank_city.text = it.body()?.bank?.city
            tv_main_bank_url.text = it.body()?.bank?.url
            tv_main_bank_phone.text = it.body()?.bank?.phone

            if (tv_main_bank_url.text != NONE) {
                tv_main_bank_url.setTextColor(getColor(R.color.blue))
            }
            if (tv_main_latitude.text != NONE && tv_main_longitude.text != NONE) {
                tv_main_latitude_header.setTextColor(getColor(R.color.blue))
                tv_main_latitude.setTextColor(getColor(R.color.blue))
                tv_main_longitude_header.setTextColor(getColor(R.color.blue))
                tv_main_longitude.setTextColor(getColor(R.color.blue))
            }
            if (tv_main_bank_phone.text != NONE) {
                tv_main_bank_phone.setTextColor(getColor(R.color.blue))
            }
        }

        // открывает историю запросов
        btn_main_open_history.setOnClickListener {
            setStandardTextColor()
            tv_main_hint.visibility = View.GONE
            ll_enter_bin.visibility = View.GONE
            fragment_main.visibility = View.VISIBLE
            setFragment(HistoryFragment())
        }

        // отправляет запрос на получение данных банка
        btn_main_get_bin_data.setOnClickListener {
            setStandardTextColor()
            if (et_bin.text.length != BIN_LENGTH) {
                Toast.makeText(
                    this, getString(R.string.toast_bin_error),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                viewModel.getBinData(et_bin.text.toString().toInt())
                tv_main_hint.visibility = View.GONE
                fragment_main.visibility = View.GONE
                clearAllFragments()
                Handler(Looper.getMainLooper()).postDelayed({
                    viewModel.parseResponseToBinModel(viewModel.response)
                    ll_enter_bin.visibility = View.VISIBLE
                }, 600)

            }
        }

        // открывает сайт банка
        tv_main_bank_url.setOnClickListener {
            if (tv_main_bank_url.text != NONE) {
                var url = tv_main_bank_url.text
                if (!url.startsWith("http://") && !url.startsWith("https://"))
                    url = "http://$url"
                val browserIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse(url.toString()))
                startActivity(browserIntent)
            }
        }

        // открывает звонок по указанному номеру
        tv_main_bank_phone.setOnClickListener {
            if (tv_main_bank_phone.text != NONE) {
                checkCallPermission(tv_main_bank_phone.text.toString())
            }
        }

        // открывает координаты банка
        ll_coordinates.setOnClickListener {
            if (tv_main_latitude.text != NONE && tv_main_longitude.text != NONE) {
                val mapIntent = Intent(
                    Intent.ACTION_VIEW, Uri.parse(
                        "geo:${tv_main_latitude.text},${tv_main_longitude.text}"
                    )
                )
                startActivity(mapIntent)
            }
        }
    }

    // подставляет нужный фрагмент
    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_main, fragment)
        }.commit()
    }

    // очищает фрагмент менеджер
    private fun clearAllFragments() {
        supportFragmentManager.fragments.forEach { fragment ->
            if (fragment != null) {
                supportFragmentManager.beginTransaction().remove(fragment).commit()
            }
        }
    }

    // отображает информацию о карте из базы данных
    fun showBinInfoFromHistory(bin: BinDbModel) {
        clearAllFragments()
        tv_main_hint.visibility = View.GONE
        fragment_main.visibility = View.GONE
        ll_enter_bin.visibility = View.VISIBLE
        tv_main_scheme.text = bin.scheme
        tv_main_brand.text = bin.brand
        tv_main_type.text = bin.type
        tv_main_prepaid.text = bin.prepaid
        tv_main_length.text = bin.length
        tv_main_luhn.text = bin.luhn
        tv_main_alpha2.text = bin.alpha2
        tv_main_numeric.text = bin.numeric
        tv_main_emoji.text = bin.emoji
        tv_main_country_name.text = bin.countryName
        tv_main_currency.text = bin.currency
        tv_main_latitude.text = bin.latitude
        tv_main_longitude.text = bin.longitude
        tv_main_bank_name.text = bin.bankName
        tv_main_bank_city.text = bin.city
        tv_main_bank_url.text = bin.url
        tv_main_bank_phone.text = bin.phone

        if (bin.latitude != NONE && bin.longitude != NONE) {
            tv_main_latitude_header.setTextColor(getColor(R.color.blue))
            tv_main_latitude.setTextColor(getColor(R.color.blue))
            tv_main_longitude_header.setTextColor(getColor(R.color.blue))
            tv_main_longitude.setTextColor(getColor(R.color.blue))
        }
        if (bin.phone != NONE) {
            tv_main_bank_phone.setTextColor(getColor(R.color.blue))
        }
        if (bin.url != NONE) {
            tv_main_bank_url.setTextColor(getColor(R.color.blue))
        }
    }

    // конвертирует значение boolean в значение string (yes/no/none)
    private fun convertBooleanToString(bool: Boolean?): String {
        return if (bool != null) {
            if (bool) YES else NO
        } else {
            NONE
        }
    }

    // инициализация
    private fun init() {
        historyViewModel = HistoryViewModel(application)
        historyViewModel.initDatabase()
    }

    // проверека разрешения доступа к звонкам
    private fun checkCallPermission(number: String) {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED -> {
                if (number.contains(",")) {
                    val phoneNumbers = number.split(", ")
                    WhatCallNumberDialogFragment(
                        getString(R.string.dialog_what_call_number),
                        phoneNumbers.toTypedArray()
                    ).show(supportFragmentManager, getString(R.string.dialog_what_call_number))
                } else {
                    val phoneIntent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$number"))
                    startActivity(phoneIntent)
                }
            }
            shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE) -> {
                Toast.makeText(
                    this, getString(R.string.need_call_permission),
                    Toast.LENGTH_LONG
                ).show()
                pLauncher.launch(Manifest.permission.CALL_PHONE)
            }
            else -> {
                pLauncher.launch(Manifest.permission.CALL_PHONE)
            }
        }
    }

    // запрашивает доступ к звонкам
    private fun registerPermissionListener() {
        pLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                Toast.makeText(
                    this, getString(R.string.call_permission_granted),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this, getString(R.string.call_premission_denied),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // устанавливает для текста стандартный цвет
    private fun setStandardTextColor() {
        tv_main_latitude_header.setTextColor(getColor(R.color.black))
        tv_main_latitude.setTextColor(getColor(R.color.black))
        tv_main_longitude_header.setTextColor(getColor(R.color.black))
        tv_main_longitude.setTextColor(getColor(R.color.black))
        tv_main_bank_url.setTextColor(getColor(R.color.black))
        tv_main_bank_phone.setTextColor(getColor(R.color.black))
    }
}