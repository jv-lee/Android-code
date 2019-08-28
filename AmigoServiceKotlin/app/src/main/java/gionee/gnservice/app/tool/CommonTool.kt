package gionee.gnservice.app.tool

/**
 * @author jv.lee
 * @date 2019/8/28.
 * @description
 */
class CommonTool {

    companion object {

        fun videoTotalTime(time: Int): String {
            val builder = StringBuilder()
            if (time <= 60) {
                builder.append("00").append(":")
                if (time < 10) {
                    builder.append(0).append(time)
                } else {
                    builder.append(time)
                }
            } else {
                val fromat = if (time % 60 < 10) "0" + time % 60 else (time % 60).toString()
                if (time / 60 < 10) {
                    builder.append(0).append(time / 60).append(":").append(fromat)
                } else {
                    builder.append(time / 60).append(":").append(fromat)
                }
            }
            return builder.toString()
        }

    }

}