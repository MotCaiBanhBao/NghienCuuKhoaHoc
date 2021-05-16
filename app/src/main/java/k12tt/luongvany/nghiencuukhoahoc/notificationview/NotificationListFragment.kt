package k12tt.luongvany.nghiencuukhoahoc.notificationview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import k12tt.luongvany.nghiencuukhoahoc.R
import k12tt.luongvany.nghiencuukhoahoc.common.BaseFragment
import k12tt.luongvany.nghiencuukhoahoc.databinding.FragmentNotificationListBinding
import k12tt.luongvany.presentation.NotificationListViewModel
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
        attachSwipeToRecyclerView()
    }

    private fun initFab() {
        dataBinding.sendNoti.setOnClickListener {
            router.showNotificationForm(null)
        }
    }

    private fun attachSwipeToRecyclerView() {
        val swipe = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(
                viewHolder: RecyclerView.ViewHolder,
                direction: Int
            ) {
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipe)
        itemTouchHelper.attachToRecyclerView(dataBinding.rvNotifications)
    }


}
