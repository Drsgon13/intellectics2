package proglife.com.ua.intellektiks.ui.contact

import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.StyleSpan
import android.text.style.URLSpan
import android.text.util.Linkify
import kotlinx.android.synthetic.main.activity_contact.*
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.ui.base.NavBaseActivity
import proglife.com.ua.intellektiks.utils.Controller
import ru.yandex.yandexmapkit.overlay.Overlay
import ru.yandex.yandexmapkit.overlay.OverlayItem
import ru.yandex.yandexmapkit.utils.GeoPoint


class ContactActivity: NavBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCustomView(R.layout.activity_contact)

        supportActionBar?.setTitle(R.string.nav_contacts)

        val email = SpannableString(getString(R.string.email))
        email.setSpan(StyleSpan(Typeface.BOLD), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        tvEmail.text = email

        val grah = SpannableString(getString(R.string.grah))
        grah.setSpan(StyleSpan(Typeface.BOLD), 0, 50, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        tvGrah.text = grah

        val web = SpannableString(getString(R.string.web))
        web.setSpan(StyleSpan(Typeface.BOLD), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        tvWeb.text = web

        val phone = SpannableString(getString(R.string.phone))

        phone.setSpan(StyleSpan(Typeface.BOLD), 0, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        phone.setSpan(URLSpan("tel:88005550484"), 8, 24, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        phone.setSpan(URLSpan("tel:74957246058"), 55, 73, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        tvPhone.text = phone
        tvPhone.setMovementMethod(LinkMovementMethod())
        val address = SpannableString(getString(R.string.address))
        address.setSpan(StyleSpan(Typeface.BOLD), 0, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        tvAddress.text = address

        initMap()
    }

    private fun  initMap(){
        val overlay = Overlay( mapView.mapController)
        val yandex = OverlayItem(GeoPoint(55.818877, 37.618354), ContextCompat.getDrawable(this, R.drawable.ic_marker))
        yandex.offsetY = 60
        overlay.addOverlayItem(yandex)
        val controller = Controller(mapView)
        controller.overlayManager.addOverlay(overlay)
        controller.setPositionAnimationTo(GeoPoint(55.818877, 37.618354), 17f)
    }
}