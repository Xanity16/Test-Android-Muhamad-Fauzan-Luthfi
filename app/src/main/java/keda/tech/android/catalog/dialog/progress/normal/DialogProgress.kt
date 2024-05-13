package keda.tech.android.catalog.dialog.progress.normal

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import keda.tech.android.catalog.R
import keda.tech.android.catalog.databinding.DialogProgressBinding

class DialogProgress(
    private var title: String
) : DialogFragment() {
    private var binding: DialogProgressBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogProgressBinding.inflate(inflater, container, false)
        setTitle(title)
        return binding?.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        isCancelable = false
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val width = ViewGroup.LayoutParams.WRAP_CONTENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            it.window?.setLayout(width, height)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogFragmentBlur)
    }

    fun setTitle(title: String) {
        binding?.tvTitle?.text = title
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}