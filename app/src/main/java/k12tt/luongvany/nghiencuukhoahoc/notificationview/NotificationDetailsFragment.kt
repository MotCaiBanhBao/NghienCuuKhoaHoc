package k12tt.luongvany.nghiencuukhoahoc.notificationview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import k12tt.luongvany.nghiencuukhoahoc.R
import k12tt.luongvany.nghiencuukhoahoc.common.BaseFragment
import k12tt.luongvany.nghiencuukhoahoc.common.Constants.EXTRA_NOTIFICATION
import k12tt.luongvany.nghiencuukhoahoc.databinding.FragmentNotificationDetailsBinding
import k12tt.luongvany.presentation.NotificationDetailsViewModel
import k12tt.luongvany.presentation.binding.NotificationBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class NotificationDetailsFragment: BaseFragment() {
    private lateinit var bottomNavigationView: BottomNavigationView

    private val viewModel: NotificationDetailsViewModel by viewModel {
        parametersOf(arguments?.getParcelable<NotificationBinding>(EXTRA_NOTIFICATION)?.id)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val parentAppBarLayout = activity?.findViewById<AppBarLayout>(R.id.appBarLayout)
        bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)!!
        parentAppBarLayout?.setExpanded(true)

        val binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_notification_details,
            container, false) as FragmentNotificationDetailsBinding

        return binding.run {
            lifecycleOwner = this@NotificationDetailsFragment
            viewModel = this@NotificationDetailsFragment.viewModel
            root
        }

    }


    override fun onDetach() {
        bottomNavigationView.let {
            it.animate().translationY(0F).duration = 200

            it.visibility = View.VISIBLE
        }
        super.onDetach()
    }
}