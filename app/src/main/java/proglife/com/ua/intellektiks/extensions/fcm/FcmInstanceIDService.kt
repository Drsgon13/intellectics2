package proglife.com.ua.intellektiks.extensions.fcm

import com.google.firebase.iid.FirebaseInstanceIdService

/**
 * Created by Evhenyi Shcherbyna on 27.12.2017.
 */
class FcmInstanceIDService: FirebaseInstanceIdService() {

    override fun onTokenRefresh() {
        super.onTokenRefresh()
    }

}