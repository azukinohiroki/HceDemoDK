package android.jwork.co.jp.hcedemodk

import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log

/**
 *
 * Created by hiroki on 2018/02/02.
 */

class MyHostApduService : HostApduService() {

	override fun processCommandApdu(commandApdu: ByteArray, extras: Bundle?): ByteArray {

		Log.d(TAG, "apdu: " + Utils.bytes2Hex(commandApdu))

		if (Utils.isEqual(SELECT, commandApdu)) {
			Log.i(TAG, "SELECT")
			return "Hello, HCE!!".toByteArray()
		}

		return byteArrayOf(0x90.toByte(), 0x00)
	}
	
	fun somethingHasDone() {
		sendResponseApdu(byteArrayOf(0x90.toByte(), 0x00))
	}

	override fun onDeactivated(reason: Int) {
		Log.i(TAG, "onDeactivated, reason=" + reason)
	}

	companion object {

		private const val TAG = "MyHostApduService"

		val SELECT = byteArrayOf(
				0x00, 0xA4.toByte(), 0x04, 0x00,
				0x07,
						0xF0.toByte(), 0x39, 0x41, 0x48, 0x14, 0x81.toByte(), 0x00,
				0x00
		)
	}
}

object Utils {

	fun bytes2Hex(bytes: ByteArray): String {
		val builder = StringBuilder()
		for (aByte in bytes) {
			builder.append(String.format("%02X", aByte))
		}
		return builder.toString()
	}

	fun isEqual(a: ByteArray, b: ByteArray): Boolean {
		if (a.size != b.size) {
			return false
		}

		var result = 0
		for (i in a.indices) {
			result = result or (a[i].toInt() xor b[i].toInt())
		}
		return result == 0
	}
}