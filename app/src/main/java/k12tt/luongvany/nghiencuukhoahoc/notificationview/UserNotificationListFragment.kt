package k12tt.luongvany.nghiencuukhoahoc.notificationview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import k12tt.luongvany.nghiencuukhoahoc.R
import k12tt.luongvany.nghiencuukhoahoc.common.BaseFragment
import k12tt.luongvany.nghiencuukhoahoc.databinding.MyNotificationLayoutBinding
import k12tt.luongvany.presentation.ViewState
import k12tt.luongvany.presentation.viewmodel.user.UserNotificationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserNotificationListFragment: BaseFragment() {

    private val viewModel: UserNotificationViewModel by viewModel()
    private lateinit var dataBinding: MyNotificationLayoutBinding

    private val notificationAdapter by lazy {
        UserNotificationAdapter ({ notification ->
            router.showNotificationDetails(notification)
        }, this)
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
        dataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.my_notification_layout,
            container,
            false) as MyNotificationLayoutBinding


        dataBinding.run {
            lifecycleOwner = this@UserNotificationListFragment
            viewModel = this@UserNotificationListFragment.viewModel
            /* Init UI */
            initRecyclerView()
        }

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }


    private fun init() {
        viewModel.state().observe(viewLifecycleOwner, Observer { viewState ->
            viewState?.let {
                if (viewState.status == ViewState.Status.ERROR) {
                    Toast.makeText(requireContext(),
                        "Khong load dc",
                        Toast.LENGTH_SHORT).show()
                }
            }
        })
        lifecycle.addObserver(viewModel)
    }

    private fun initRecyclerView() {
        dataBinding.rvNotifications.adapter = notificationAdapter
    }
}
