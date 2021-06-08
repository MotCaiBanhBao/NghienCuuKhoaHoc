package k12tt.luongvany.nghiencuukhoahoc.notificationview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import k12tt.luongvany.domain.entities.Topics
import k12tt.luongvany.nghiencuukhoahoc.R
import k12tt.luongvany.nghiencuukhoahoc.common.BaseFragment
import k12tt.luongvany.nghiencuukhoahoc.databinding.FragmentNotificationFormBinding
import k12tt.luongvany.nghiencuukhoahoc.router.NotificationRouter
import k12tt.luongvany.nghiencuukhoahoc.router.NotificationRouter.Companion.HIDE_BOT
import k12tt.luongvany.nghiencuukhoahoc.router.NotificationRouter.Companion.SHOW_ALL
import k12tt.luongvany.presentation.ViewState
import k12tt.luongvany.presentation.viewmodel.notification.NotificationFormViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class NotificationFormFragment : BaseFragment(){
    private val viewModel: NotificationFormViewModel by viewModel()
    private lateinit var dataBinding: FragmentNotificationFormBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         dataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_notification_form,
            container, false) as FragmentNotificationFormBinding
        dataBinding.kythuatSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            dataBinding.khoaKyThuatChipGroup.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
        router.hideAppBarAndBottom(HIDE_BOT)
        dataBinding.kinhteSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            dataBinding.khoaKinhteChipGroup.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        dataBinding.suphamSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            dataBinding.khoaSuphamChipGroup.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
        return dataBinding.run {
            lifecycleOwner = this@NotificationFormFragment
            presenter = this@NotificationFormFragment
            viewModel = this@NotificationFormFragment.viewModel
            root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.addObserver(viewModel)
        init()
    }

    override fun onStart() {
        super.onStart()

        dataBinding.btnSave.setOnClickListener {
            viewModel.saveNotification(topics = convertToData())
        }
    }


    private fun init() {
        viewModel.stateData().observe(viewLifecycleOwner, Observer { event ->
            event?.let { state ->
                when (state.status) {
                    ViewState.Status.LOADING -> {
                    }
                    ViewState.Status.SUCCESS -> {
                        initSwitch()
                    }
                    ViewState.Status.ERROR -> {

                    }
                }
            }
        })
        viewModel.state().observe(viewLifecycleOwner, Observer { event ->
            event?.let { state ->
                when (state.status) {
                    ViewState.Status.LOADING -> Log.d(TAG, "Process is loading")
                    ViewState.Status.SUCCESS -> {
                        showMessageSuccess()
                        Log.d(TAG, "SUCCESS")
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
        private const val TAG = "NotificationForm"
    }
    private fun initSwitch(){
        dataBinding.khoaKyThuatChipGroup.visibility = if (dataBinding.kythuatSwitch.isChecked) View.VISIBLE else View.GONE
        dataBinding.khoaKinhteChipGroup.visibility = if (dataBinding.kinhteSwitch.isChecked) View.VISIBLE else View.GONE
        dataBinding.khoaSuphamChipGroup.visibility = if (dataBinding.suphamSwitch.isChecked) View.VISIBLE else View.GONE
        for (item in viewModel.stateData().value?.data!!){
            when(item.nameTopic){
                "KhoaKyThuat" -> {
                    dataBinding.khoaKyThuatChipGroup.removeAllViews()
                    for(topic in item.listTopic.sortedBy { it.keys.toString() }){
                        val chip = makeChip(topic, "KhoaKyThuat")
                        if(chip.text.equals("KhoaKyThuat")){
                            chip.text = "Khoa kỹ thuật"
                            dataBinding.khoaKyThuatChipGroup.addView(chip, 0)
                        }else{
                            dataBinding.khoaKyThuatChipGroup.addView(chip)
                        }
                    }
                }
                "KhoaKinhTe" -> {
                    dataBinding.khoaKinhteChipGroup.removeAllViews()
                    for(topic in item.listTopic){
                        val chip = makeChip(topic,  "KhoaKinhTe")
                        if(chip.text.equals( "KhoaKinhTe")){
                            chip.text = "Khoa kinh tế"
                            dataBinding.khoaKinhteChipGroup.addView(chip, 0)
                        }else{
                            dataBinding.khoaKinhteChipGroup.addView(chip)
                        }
                    }
                }
                "KhoaSuPham" -> {
                    dataBinding.khoaSuphamChipGroup.removeAllViews()
                    for(topic in item.listTopic){
                        val chip = makeChip(topic,  "KhoaSuPham")
                        if(chip.text.equals( "KhoaSuPham")){
                            chip.text = "Khoa sư phạm"
                            dataBinding.khoaSuphamChipGroup.addView(chip, 0)
                        }else{
                            dataBinding.khoaSuphamChipGroup.addView(chip)
                        }
                    }
                }
            }
        }
    }

    private fun makeChip(topic: Map<String, String>, kind: String): Chip {
        val chipDrawable = this.context?.let {
            ChipDrawable.createFromAttributes(
                it,
                null,
                0,
                R.style.Widget_MaterialComponents_Chip_Filter
            )
        }
        return Chip(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            this.text = topic.keys.toString().removePrefix("[").removeSuffix("]")
            if (chipDrawable != null) {
                setChipDrawable(chipDrawable)
            }
            isChecked = false
        }
    }

    fun convertToData(): List<Topics> {
        val kyThuatGroup = dataBinding.khoaKyThuatChipGroup
        val kyThuatChisChecked = kyThuatGroup.checkedChipIds
        val kinhTeGroup = dataBinding.khoaKinhteChipGroup
        val kinhTeChisChecked = kinhTeGroup.checkedChipIds
        val suPhamGroup = dataBinding.khoaSuphamChipGroup
        val suPhamChisChecked = suPhamGroup.checkedChipIds

        val topics: MutableList<Topics> = mutableListOf()
        val kyThuatTopics = Topics("KhoaKyThuat")
        val kinhTeTopics = Topics("KhoaKinhTe")
        val suPhamTopics = Topics("KhoaSuPham")
        if (kyThuatChisChecked.isNotEmpty()) {
            for (item in kyThuatChisChecked) {
                val chip: Chip = kyThuatGroup.findViewById(item)
                kyThuatTopics.topics.add(makeAddress(chip.text.toString()))
            }
        }
        if (kinhTeChisChecked.isNotEmpty()) {
            for (item in kinhTeChisChecked) {
                val chip: Chip = kinhTeGroup.findViewById(item)
                kinhTeTopics.topics.add(makeAddress(chip.text.toString()))
            }
        }
        if (suPhamChisChecked.isNotEmpty()) {
            for (item in suPhamChisChecked) {
                val chip: Chip = suPhamGroup.findViewById(item)
                suPhamTopics.topics.add(makeAddress(chip.text.toString()))
            }
        }

        return topics.apply {
            add(kyThuatTopics)
            add(kinhTeTopics)
            add(suPhamTopics)
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun makeAddress(name: String): Map<String, String>{
        return when (name){
            "Khoa kỹ thuật" -> mapOf("KhoaKyThuat" to "/topics/khoakythuat")
            "Khoa kinh tế" -> mapOf("KhoaKinhTe" to "/topics/khoakinhte")
            "Khoa sư phạm" -> mapOf("KhoaSuPham" to "/topics/khoasupham")
            else -> mapOf(name to "/topics/${name.lowercase()}")
        }
    }
    override fun onDetach() {
        super.onDetach()
        router.showAppBarAndBottom(SHOW_ALL)
    }
}
