package com.lee.code.draw

import android.support.annotation.IntDef
import com.lee.code.draw.PiecesType.Companion.BLACK_PIECES
import com.lee.code.draw.PiecesType.Companion.WHITE_PIECES
import java.util.*

/**
 * @author jv.lee
 * @date 2020/8/28
 * @description 亨元工厂：棋子工厂
 */
class PiecesFactor {

    private val chessPiecesMap = HashMap<Int, ChessPieces>()

    init {
        chessPiecesMap.put(WHITE_PIECES, WhitePieces())
        chessPiecesMap.put(BLACK_PIECES, BlackPieces())
    }

    fun getChessPieces(@PiecesType type: Int): ChessPieces? {
        return chessPiecesMap[type]
    }

}

@IntDef(WHITE_PIECES, BLACK_PIECES)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
annotation class PiecesType {
    companion object {
        const val WHITE_PIECES = 0x001
        const val BLACK_PIECES = 0x002
    }
}