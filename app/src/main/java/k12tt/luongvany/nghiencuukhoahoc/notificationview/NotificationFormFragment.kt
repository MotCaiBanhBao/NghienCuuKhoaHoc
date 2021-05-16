package k12tt.luongvany.nghiencuukhoahoc.notificationview

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import k12tt.luongvany.nghiencuukhoahoc.R
import k12tt.luongvany.nghiencuukhoahoc.common.BaseFragment
import k12tt.luongvany.nghiencuukhoahoc.common.FilePicker
import k12tt.luongvany.nghiencuukhoahoc.databinding.FragmentNotificationFormBinding
import k12tt.luongvany.presentation.NotificationFormViewModel
import k12tt.luongvany.presentation.ViewState
import org.koin.android.viewmodel.ext.android.viewModel

class NotificationFormFragment : BaseFragment() {

    private val viewModel: NotificationFormViewModel by viewModel()

    private val filePicker: FilePicker by lazy {
        FilePicker(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_notification_form,
            container, false) as FragmentNotificationFormBinding

        return binding.run {
            lifecycleOwner = this@NotificationFormFragment
            content.presenter = this@NotificationFormFragment
            content.viewModel = this@NotificationFormFragment.viewModel
            root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.addObserver(viewModel)
        init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


    }

    private fun init() {
        viewModel.state().observe(viewLifecycleOwner, Observer { event ->
            event?.let { state ->
                when (state.status) {
                    ViewState.Status.LOADING -> Log.d(TAG, "Process is loading")
                    ViewState.Status.SUCCESS -> {
                        showMessageSuccess()
                        Log.d(TAG, "SUCCESS")
                        router.back()
                    }
                    ViewState.Status.ERROR -> {
                        showErrorMessage(R.string.send_notification_error)
                    }
                }
            }
        })
    }

    private fun showMessageSuccess() {
        Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
    }

    private fun showErrorMessage(@StringRes message: Int) {
        AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setTitle("Lỗi")
            .show()
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val PICKERIMAGE = 1
        private const val TAG = "NotificationForm"
    }
}
