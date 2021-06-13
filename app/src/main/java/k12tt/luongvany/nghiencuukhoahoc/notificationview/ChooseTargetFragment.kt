package k12tt.luongvany.nghiencuukhoahoc.notificationview

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnScrollChangedListener
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.snackbar.Snackbar
import k12tt.luongvany.domain.entities.Topics
import k12tt.luongvany.nghiencuukhoahoc.MainActivity
import k12tt.luongvany.nghiencuukhoahoc.R
import k12tt.luongvany.nghiencuukhoahoc.common.BaseFragment
import k12tt.luongvany.nghiencuukhoahoc.databinding.SelectTargetLayoutBinding
import k12tt.luongvany.nghiencuukhoahoc.router.NotificationRouter.Companion.HIDE_BOT
import k12tt.luongvany.nghiencuukhoahoc.router.NotificationRouter.Companion.HIDE_TOP
import k12tt.luongvany.nghiencuukhoahoc.router.NotificationRouter.Companion.SHOW_ALL
import k12tt.luongvany.nghiencuukhoahoc.router.NotificationRouter.Companion.SHOW_BOT
import k12tt.luongvany.presentation.Router
import k12tt.luongvany.presentation.TwoDataViewState
import k12tt.luongvany.presentation.ViewState
import k12tt.luongvany.presentation.viewmodel.topic.TopicsViewModel
import kotlinx.coroutines.internal.artificialFrame
import org.koin.androidx.viewmodel.ext.android.viewModel


class ChooseTargetFragment: BaseFragment() {
    private lateinit var dataBinding: SelectTargetLayoutBinding
    private val viewModel: TopicsViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.select_target_layout,
            container, false) as SelectTargetLayoutBinding
        Log.d("TEST", "ChooseTarget onCreateView")
        dataBinding.kythuatSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            dataBinding.khoaKyThuatChipGroup.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        dataBinding.kinhteSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            dataBinding.khoaKinhteChipGroup.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        dataBinding.suphamSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            dataBinding.khoaSuphamChipGroup.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
        return dataBinding.run {
            lifecycleOwner = this@ChooseTargetFragment
            viewModel = this@ChooseTargetFragment.viewModel
            root
        }
    }

    override fun onStart() {
        super.onStart()
        init()
        dataBinding.toolbar.title = "Choose your channel"
        dataBinding.toolbar.inflateMenu(R.menu.choose_target_menu)
        dataBinding.toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.action_save_choose -> {
                    viewModel.changeTopic(convertToData(), viewModel.state().value?.userData)
                    true
                }
                else -> {
                    true
                }
            }
        }
        Log.d("TEST", "ChooseTarget onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("TEST", "ChooseTarget onResume")
    }
    private fun showMessageSuccess() {
        AlertDialog.Builder(requireContext())
            .setTitle("Thành công")
            .setMessage("")
            .show()
    }

    private fun showErrorMessage() {
        AlertDialog.Builder(requireContext())
            .setTitle("Lỗi")
            .show()
    }

    private fun init() {
        viewModel.changeTopicState().observe(viewLifecycleOwner, Observer { event ->
            event?.let { state ->
                when (state.status) {
                    ViewState.Status.LOADING -> {
                    }
                    ViewState.Status.SUCCESS -> {
                        showMessageSuccess()
                    }
                    ViewState.Status.ERROR -> {
                        showErrorMessage()
                    }
                }
            }
        })
            viewModel.state().observe(viewLifecycleOwner, Observer { event ->
                event?.let { state ->
                    when (state.status) {
                        TwoDataViewState.Status.LOADING -> {

                        }
                        TwoDataViewState.Status.SUCCESS -> {

                            initSwitch()
                        }
                        TwoDataViewState.Status.ERROR -> {
                            showErrorMessage("Loi")
                        }
                    }
                }
            })
    }

    private fun showErrorMessage(message: String) {
        AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setTitle("Lỗi")
            .show()
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TEST", "cREATE")
        retainInstance = true
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.addObserver(viewModel)
        init()
        router.hideAppBarAndBottom(HIDE_TOP)

//        dataBinding.toolbar.setNavigationOnClickListener { v -> dismiss() }
    }

    @OptIn(ExperimentalStdlibApi::class)
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


    private fun initSwitch(){

        dataBinding.kythuatSwitch.isChecked = viewModel.isCheck(1)
        dataBinding.kinhteSwitch.isChecked = viewModel.isCheck(2)
        dataBinding.suphamSwitch.isChecked = viewModel.isCheck(3)

        dataBinding.khoaKyThuatChipGroup.visibility = if (dataBinding.kythuatSwitch.isChecked) View.VISIBLE else View.GONE
        dataBinding.khoaKinhteChipGroup.visibility = if (dataBinding.kinhteSwitch.isChecked) View.VISIBLE else View.GONE
        dataBinding.khoaSuphamChipGroup.visibility = if (dataBinding.suphamSwitch.isChecked) View.VISIBLE else View.GONE

        for (item in viewModel.state().value?.defaultData!!){
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

    private fun makeChip(topic: Map<String, String>, kind: String): Chip{
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
            val userTopics = viewModel.state().value?.userData?.filter { it.nameTopic == kind }
            if (chipDrawable != null) {
                setChipDrawable(chipDrawable)
            }
            isChecked = false
            if (userTopics != null) {
                for (item in userTopics){
                    if (item.listTopic.contains(topic)){
                        isChecked = true
                    }
                }
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        router.showAppBarAndBottom(SHOW_ALL)
    }
}