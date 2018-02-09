package android.jwork.co.jp.hcedemodk

import android.nfc.NfcAdapter
import android.nfc.NfcAdapter.ReaderCallback
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.nfc.tech.NfcF
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_reader.*
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.charset.Charset

/**
 *
 * Created by hiroki on 2018/02/03.
 */

class ReaderFragment : Fragment(), ReaderCallback, View.OnClickListener {

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

		return View.inflate(activity, R.layout.fragment_reader, null)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		reader_nfc_f_button.setOnClickListener(this)
	}

	override fun onResume() {
		Log.d(TAG, "onResume: ")
		super.onResume()

		val adapter = NfcAdapter.getDefaultAdapter(activity)
		adapter.enableReaderMode(
				activity,
				this,
				NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,
				null)
	}

	override fun onPause() {
		Log.d(TAG, "onPause: ")
		super.onPause()

		NfcAdapter.getDefaultAdapter(activity)
				.disableReaderMode(activity)
	}
	
	override fun onClick(v: View?) {
		Log.d(TAG, "onClick: ")

		NfcAdapter.getDefaultAdapter(activity)
				.disableReaderMode(activity)

		val adapter = NfcAdapter.getDefaultAdapter(activity)
		adapter.enableReaderMode(
				activity,
				this,
				NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,
				null)
	}

	override fun onTagDiscovered(tag: Tag) {

		logd(TAG, "onTagDiscovered: ")

		val nfc = IsoDep.get(tag)
		if (nfc != null) {
			logd(TAG, nfc.tag.toString())
			try {
				nfc.connect()
				val res = nfc.transceive(MyHostApduService.SELECT)
				logd(TAG, String(res, Charset.forName("UTF-8")))

			} catch (e: IOException) {
				e.printStackTrace()
			}
		}
		
		val nfcf = NfcF.get(tag) ?: return

		try {
			nfcf.connect()
			logd(TAG, "connect has done")
			nfcf.transceive(byteArrayOf(0x06, 0x00, 0x40, 0x00, 0x01, 0x0F))
			logd(TAG, "polling has done")

			val msg = "hello, world".toByteArray()
			val buffer = ByteBuffer.allocate(msg.size + 1)
			buffer.put((msg.size + 1).toByte())
			buffer.put(msg)
			val res = nfcf.transceive(buffer.array())
			logd(TAG, String(res, Charset.forName("UTF-8")))

		} catch (e: IOException) {
			e.printStackTrace()
		}
	}

	private fun logd(tag: String, msg: String) {
		Log.d(tag, msg)

		val act = activity ?: return
		act.runOnUiThread(Runnable {
			val v = view ?: return@Runnable
			(v.findViewById<View>(R.id.textView) as TextView).text = msg
		})
	}

	companion object {

		private val TAG = ReaderFragment::class.java.simpleName
	}
}
