package com.lxj.androidktx.base

enum class ListDataChange{
    Insert, Remove, Change
}

data class ChangeRange(
    var start: Int = 0,
    var count: Int = 0
)

class ListData<T> (
    var list: ArrayList<T> = arrayListOf(),
    var changeAction: ListDataChange = ListDataChange.Insert,
    var changeRange: ChangeRange = ChangeRange(),  //改变范围
){
    /**
     * 整体替换行为
     */
    fun fullReplace(newData: List<T>): ListData<T>{
        list.clear()
        list.addAll(newData)
        changeAction = ListDataChange.Change
        changeRange = ChangeRange(start = 0, count = list.size)
        return this
    }

    /**
     * 添加一个范围的数据
     */
    fun insertRange(newData: List<T>): ListData<T>{
        list.addAll(newData)
        changeAction = ListDataChange.Insert
        changeRange = ChangeRange(start = list.size-newData.size, count = newData.size)
        return this
    }

    /**
     * 添加单个数据行为
     */
    fun insertItem(newData: T): ListData<T>{
        list.add(newData)
        changeAction = ListDataChange.Insert
        changeRange = ChangeRange(start = list.size-1, count = 1)
        return this
    }

    /**
     * 移除单个数据
     */
    fun removeItem(index: Int): ListData<T>{
        list.removeAt(index)
        changeAction = ListDataChange.Remove
        changeRange = ChangeRange(start = index, count = 1)
        return this
    }

    /**
     * 移除范围数据
     */
    fun removeRange(range: IntRange): ListData<T>{
        range.forEach {
            if(list.size > it) list.removeAt(it)
        }
        changeAction = ListDataChange.Remove
        changeRange = ChangeRange(start = range.first, count = (range.last - range.first))
        return this
    }

    /**
     * 替换单个条目
     */
    fun changeItem(index: Int, newData: T): ListData<T>{
        changeAction = ListDataChange.Change
        list[index] = newData
        changeRange = ChangeRange(start = index, count = 1)
        return this
    }

    /**
     * 替换范围条目
     */
    fun changeRange(index: Int, newData: List<T>): ListData<T>{
        changeAction = ListDataChange.Change
        (index until (index+newData.size)).forEach {
            if(list.size > it) list[it] = newData[it-index]
        }
        changeRange = ChangeRange(start = index, count = newData.size)
        return this
    }

}
