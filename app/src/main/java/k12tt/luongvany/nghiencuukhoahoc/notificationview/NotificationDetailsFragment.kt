package k12tt.luongvany.nghiencuukhoahoc.notificationview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import k12tt.luongvany.nghiencuukhoahoc.MainActivity
import k12tt.luongvany.nghiencuukhoahoc.R
import k12tt.luongvany.nghiencuukhoahoc.common.BaseFragment
import k12tt.luongvany.nghiencuukhoahoc.common.Constants.EXTRA_NOTIFICATION
import k12tt.luongvany.nghiencuukhoahoc.databinding.FragmentNotificationDetailsBinding
import k12tt.luongvany.nghiencuukhoahoc.databinding.FragmentNotificationListBinding
import k12tt.luongvany.presentation.Router
import k12tt.luongvany.presentation.viewmodel.notification.NotificationDetailsViewModel
import k12tt.luongvany.presentation.binding.notification.NotificationBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class NotificationDetailsFragment: BottomSheetDialogFragment() {
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

    override fun onStart() {
        super.onStart()
        dataBinding.buttonGchatSend.setOnClickListener{
            viewModel.pushMessage()
        }
    }
    private fun initRecyclerView() {
        dataBinding.recyclerGchat.adapter = messageAdapter
        dataBinding.recyclerGchat.isNestedScrollingEnabled = false
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}