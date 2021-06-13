package k12tt.luongvany.nghiencuukhoahoc.notificationview

import android.app.Activity
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface.OnShowListener
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import k12tt.luongvany.domain.entities.User
import k12tt.luongvany.nghiencuukhoahoc.R
import k12tt.luongvany.nghiencuukhoahoc.UserSingleTon
import k12tt.luongvany.nghiencuukhoahoc.common.Constants.EXTRA_NOTIFICATION
import k12tt.luongvany.nghiencuukhoahoc.databinding.FragmentNotificationDetailsBinding
import k12tt.luongvany.presentation.binding.notification.NotificationBinding
import k12tt.luongvany.presentation.viewmodel.notification.NotificationDetailsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class NotificationDetailsFragment: DialogFragment() {
    private val messageAdapter by lazy {
        MessageAdapter ()
    }
    private lateinit var dataBinding: FragmentNotificationDetailsBinding

    private val viewModel: NotificationDetailsViewModel by viewModel {
        parametersOf(arguments?.getParcelable<NotificationBinding>(EXTRA_NOTIFICATION)?.id)
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_notification_details,
            container, false) as FragmentNotificationDetailsBinding

        dataBinding.run {
            lifecycleOwner = this@NotificationDetailsFragment
            viewModel = this@NotificationDetailsFragment.viewModel
            initRecyclerView()
        }
        return dataBinding.root
    }


    val receiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            messageAdapter.updateTimes();
        }
    }


    override fun onResume() {
        super.onResume()
        val filter = IntentFilter(Intent.ACTION_TIME_TICK)
        requireContext().registerReceiver(receiver, filter)
    }


    override fun onPause() {
        super.onPause()
        requireContext().unregisterReceiver(receiver)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_FullScreenDialog)
    }

    override fun onStart() {
        super.onStart()
        dataBinding.buttonGchatSend.setOnClickListener{
            viewModel.pushMessage(
                User(uid = UserSingleTon.getUserUid(),
                    avatarUri = UserSingleTon.getUserPhotoUrl(),
                    name = UserSingleTon.getUserName(),
                    emailAdress = UserSingleTon.getEmail(),
                    language = UserSingleTon.getLanguageNumber())
            )
        }

    }
    private fun initRecyclerView() {
        dataBinding.recyclerGchat.apply {
            adapter = messageAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
        }
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}