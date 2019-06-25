package com.qinbang.quickrun.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.qinbang.quickrun.R
import com.qinbang.quickrun.utils.Constants
import com.qinbang.quickrun.utils.Glide4Engine
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.internal.entity.CaptureStrategy
import kotlinx.android.synthetic.main.activity_loss_report.*
import timber.log.Timber

/**
 * 损耗申报
 */
class LossReportActivity : BaseActivity() {

    companion object {
        fun goIn(context: Context, orderId: String? = null, shipmentNumber: String = "") {
            val intent = Intent(context, LossReportActivity::class.java)
            intent.putExtra("orderId", orderId)
            intent.putExtra("shipmentNumber", shipmentNumber)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loss_report)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val orderId = intent.getStringExtra("orderId")
        val shipmentNumber = intent.getStringExtra("shipmentNumber")

        editText.setText(shipmentNumber)
    }

    private val REQUEST_CODE_CHOOSE = 101

    fun onClick(view: View) {
        when (view.id) {
            imageView7.id -> {
                RxPermissions(this)
                    .request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                    .subscribe {
                        if (it) {
                            Matisse.from(this)
                                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG), true)
                                .countable(true)
                                .capture(true)
                                .maxSelectable(4)
                                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                                .thumbnailScale(0.85F)
                                .imageEngine(Glide4Engine())
                                .captureStrategy(CaptureStrategy(false, "com.qinbang.quickrun.fileProvider"))
                                .spanCount(3)
                                .showSingleMediaType(true)
                                .forResult(Constants.REQUEST_CODE_CHOOSE)
                        } else {
                            Timber.d("权限被拒绝")
                        }
                    }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_CODE_CHOOSE && resultCode == Activity.RESULT_OK) {
            val obtainResult = Matisse.obtainResult(data)
            Timber.d(obtainResult[0].toString())
        } else {
            Timber.d("获取图片失败")
        }
    }
}
