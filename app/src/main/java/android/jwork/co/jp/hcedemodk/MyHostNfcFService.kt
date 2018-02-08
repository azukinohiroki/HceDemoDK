package android.jwork.co.jp.hcedemodk

import android.nfc.cardemulation.HostNfcFService
import android.os.Bundle
import android.util.Log

import java.io.UnsupportedEncodingException
import java.nio.ByteBuffer
import java.nio.charset.Charset

/**
 *
 * Created by hiroki on 2018/02/03.
 */

class MyHostNfcFService : HostNfcFService() {


	override fun processNfcFPacket(commandPacket: ByteArray, extras: Bundle?): ByteArray {

		Log.d(TAG, "processNfcFPacket: " + Utils.bytes2Hex(commandPacket))
		if (Utils.isEqual(commandPacket, POLLING)) {
			return byteArrayOf(0x02, 0x00)
		}

		try {
			Log.d(TAG, "processNfcFPacket: " + String(commandPacket, Charset.forName("UTF-8")))
		} catch (e: UnsupportedEncodingException) {
			e.printStackTrace()
		}

		val msg = "Hello, HCE-F!!".toByteArray()
		val buffer = ByteBuffer.allocate(msg.size + 2)
		buffer.put((msg.size + 2).toByte())
		buffer.put(0.toByte())
		buffer.put(msg)
		return buffer.array()
	}

	override fun onDeactivated(reason: Int) {
		Log.d(TAG, "onDeactivated: " + reason)
	}

	companion object {

		private val TAG = MyHostNfcFService::class.java.simpleName

		private val POLLING = byteArrayOf(0x0A, 0x0C, 0x02, 0xFE.toByte(), 0x00, 0x00, 0x00, 0x00, 0x00, 0x00)
	}
}