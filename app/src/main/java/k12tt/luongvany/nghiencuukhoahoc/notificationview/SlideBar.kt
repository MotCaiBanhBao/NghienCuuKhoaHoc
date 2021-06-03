package k12tt.luongvany.nghiencuukhoahoc.notificationview

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import k12tt.luongvany.nghiencuukhoahoc.R
import androidx.fragment.app.Fragment
import com.github.appintro.*
import k12tt.luongvany.nghiencuukhoahoc.MainActivity
import k12tt.luongvany.presentation.viewmodel.user.MainActivityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SlideBar: AppIntro2() {
    private val viewModel: MainActivityViewModel by viewModel()

    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.reSub()
        addSlide(AppIntroFragment.newInstance(
            title = getString(R.string.welcome),
            description = getString(R.string.welcome_des),
            imageDrawable = R.drawable.ic_udck,
            descriptionColor = resources.getColor(R.color.black),
            titleTypefaceFontRes = R.font.head_line,
            descriptionTypefaceFontRes = R.font.sf_ui_display_light,
            titleColor = resources.getColor(R.color.white),
            backgroundColor = resources.getColor(R.color.green_main),
        ))

        addSlide(AppIntroFragment.newInstance(
            title = getString(R.string.get_notification),
            description = getString(R.string.get_notification_des),
            imageDrawable = R.drawable.ic_noti,
            titleTypefaceFontRes = R.font.head_line,
            descriptionTypefaceFontRes = R.font.sf_ui_display_light,
            titleColor = resources.getColor(R.color.white),
            backgroundColor = resources.getColor(R.color.blue_main)
        ))

        addSlide(AppIntroFragment.newInstance(
            title = getString(R.string.subscribe),
            description = getString(R.string.get_sub_des),
            imageDrawable = R.drawable.ic_subcribe,
            titleTypefaceFontRes = R.font.head_line,
            descriptionTypefaceFontRes = R.font.sf_ui_display_light,
            titleColor = resources.getColor(R.color.white),
            backgroundColor = Color.parseColor("#f66754")
        ))
        addSlide(AppIntroFragment.newInstance(
            title = getString(R.string.multi_language),
            description = getString(R.string.get_localization_des),
            imageDrawable = R.drawable.ic_language,
            titleTypefaceFontRes = R.font.head_line,
            descriptionTypefaceFontRes = R.font.sf_ui_display_light,
            titleColor = resources.getColor(R.color.white),
            backgroundColor = Color.parseColor("#FFA561")
        ))

        setTransformer(AppIntroPageTransformerType.Parallax(
            titleParallaxFactor = 1.0,
            imageParallaxFactor = -1.0,
            descriptionParallaxFactor = 2.0
        ))
        isColorTransitionsEnabled = true
        isSkipButtonEnabled = false
        showStatusBar(true)
    }
    public override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        finish()
    }

    public override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        val intent = Intent(application, MainActivity::class.java).apply{
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra("init", "first")
        }
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        
    }
}