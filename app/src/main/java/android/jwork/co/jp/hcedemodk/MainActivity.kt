package android.jwork.co.jp.hcedemodk

import android.content.ComponentName
import android.nfc.NfcAdapter
import android.nfc.cardemulation.NfcFCardEmulation
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		mNfcAdapter = NfcAdapter.getDefaultAdapter(applicationContext)

		val service = ComponentName(this, MyHostNfcFService::class.java.name)
		val emu = NfcFCardEmulation.getInstance(mNfcAdapter)

		emu.setNfcid2ForService(service, "02FE000000000000")
		emu.registerSystemCodeForService(service, "4000")
	}

	private var mNfcAdapter: NfcAdapter? = null

	override fun onResume() {
		super.onResume()
		
		val service = ComponentName(this, MyHostNfcFService::class.java.name)
		val emu = NfcFCardEmulation.getInstance(mNfcAdapter)

		emu.enableService(this, service)

		val nfcid2 = emu.getNfcid2ForService(service)
		val syscode = emu.getSystemCodeForService(service)

		Log.d(TAG, nfcid2)
		Log.d(TAG, syscode)
	}

	override fun onPause() {
		super.onPause()
				NfcFCardEmulation
						.getInstance(mNfcAdapter)
						.disableService(this)
	}
	
	companion object {
		const val TAG = "debug"
	}
	
	fun readerButtonTapped(v: View) {
		Log.d(TAG, "readerButtonTapped")
		
		supportFragmentManager.popBackStackImmediate();
		
		supportFragmentManager.beginTransaction()
				.addToBackStack(ReaderFragment::javaClass.name)
				.add(R.id.frame,
						Fragment.instantiate(this, ReaderFragment::class.java.name),
						ReaderFragment::javaClass.name)
				.commit()
	}
}