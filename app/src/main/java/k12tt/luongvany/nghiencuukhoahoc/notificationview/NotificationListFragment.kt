package k12tt.luongvany.nghiencuukhoahoc.notificationview

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
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
import k12tt.luongvany.nghiencuukhoahoc.databinding.FragmentNotificationListBinding
import k12tt.luongvany.presentation.viewmodel.notification.NotificationListViewModel
import k12tt.luongvany.presentation.ViewState
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationListFragment: BaseFragment() {

    private val viewModel: NotificationListViewModel by viewModel()
    private lateinit var dataBinding: FragmentNotificationListBinding

    private val notificationAdapter by lazy {
        NotificationAdapter ({ notification ->
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
            R.layout.fragment_notification_list,
            container,
            false) as FragmentNotificationListBinding


        dataBinding.run {
            lifecycleOwner = this@NotificationListFragment
            viewModel = this@NotificationListFragment.viewModel
            /* Init UI */
            initRecyclerView()
            initFab()
        }

        dataBinding.chipGroup.setOnCheckedChangeListener{group, checkedId ->
            when(checkedId){
                R.id.chip_all -> notificationAdapter.filter.filter("TATCA")
                R.id.chip_tb -> notificationAdapter.filter.filter("THONGBAO")
                R.id.chip_tkb -> notificationAdapter.filter.filter("THOIKHOABIEU")
            }
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

    private fun initFab() {
        dataBinding.sendNoti.setOnClickListener {
            val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
            val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
            if (isConnected)
                router.showNotificationForm(null)
            else{
                AlertDialog.Builder(requireContext())
                    .setMessage("Bạn không có kết nối Internet, làm ơn kết nối Internet trước khi dùng chức năng này")
                    .setTitle("Lỗi")
                    .setPositiveButton("Đã hiểu", null)
                    .show()
            }
        }
    }
}
