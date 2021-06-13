package k12tt.luongvany.presentation.utils

import java.util.*

fun Long.dateMinusTo(value: Long): String{
    var result = ((value - this)/1000/60)
    var returnValue: String = ""
    if (result <= 0){
        returnValue = "ago"
    }else{
        if (result/60 <= 0){
            returnValue = result.toString() + "min"
        }else{
            result/=60
            if (result/24 <= 0){
                returnValue = result.toString() + "h"
            }else{
                result/=24
                if (result/7 <= 0){
                    returnValue = result.toString() + "d"
                }else{
                    result/=7
                    if (result/4 <= 0) {
                        returnValue = result.toString() + "w"
                    }else{
                        result/=4
                        if (result/12 <= 0){
                            returnValue = result.toString() + "m"
                        }else{
                            returnValue = result.toString() + "y"
                        }
                    }
                }
            }
        }
    }
    return  returnValue
}
