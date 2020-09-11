package com.gallery.core.expand

import com.gallery.scan.args.file.ScanFileEntity
import com.gallery.scan.types.SCAN_ALL
import com.gallery.scan.types.isScanAllExpand

//获取当前页的文件夹数据
//目标List为扫描成功之后的数据，返回Finder数据
fun ArrayList<ScanFileEntity>.findFinder(sdName: String, allName: String): ArrayList<ScanFileEntity> {
    val finderList = ArrayList<ScanFileEntity>()
    this.forEach { item ->
        if (finderList.find { it.parent == item.parent } == null) {
            finderList.add(item.copy(count = this.count { it.parent == item.parent }))
        }
    }
    if (finderList.isNotEmpty()) {
        finderList.add(0, finderList.first().copy(parent = SCAN_ALL, bucketDisplayName = allName, count = this.size))
        finderList.find { it.bucketDisplayName == "0" }?.let {
            finderList[finderList.indexOf(it)] = it.copy(bucketDisplayName = sdName)
        }
    }
    return finderList
}

//裁剪或者拍照之后更新文件夹数据
//如果现有的文件夹数据找不到parent相同的数据则是一个新的文件夹
//添加数据并更新第一条数据
//否则更新第一条数据和文件夹数据
fun ArrayList<ScanFileEntity>.updateResultFinder(scanEntity: ScanFileEntity, sortDesc: Boolean) {
    if (isEmpty()) {
        return
    }
    val find: ScanFileEntity? = find { it.parent == scanEntity.parent }
    if (find == null) {
        this.add(1, scanEntity.copy(count = 1))
        val first: ScanFileEntity = first()
        this[indexOf(first)] = first.copy(scanEntity, if (sortDesc) scanEntity.id else first.id, first.bucketDisplayName, first.parent, first.count + 1)
    } else {
        find { it.parent.isScanAllExpand() }?.let {
            this[indexOf(it)] = it.copy(scanEntity, if (sortDesc) scanEntity.id else it.id, it.bucketDisplayName, it.parent, count = it.count + 1)
        }
        find { it.parent == scanEntity.parent }?.let {
            this[indexOf(it)] = it.copy(scanEntity, if (sortDesc) scanEntity.id else it.id, scanEntity.bucketDisplayName, scanEntity.parent, count = it.count + 1)
        }
    }
}

internal fun ScanFileEntity.copy(source: ScanFileEntity, id: Long, bucketDisplayName: String, parent: Long, count: Int): ScanFileEntity {
    return copy(
            id = id,
            size = source.size,
            duration = source.duration,
            mimeType = source.mimeType,
            displayName = source.displayName,
            orientation = source.orientation,
            bucketId = source.bucketId,
            bucketDisplayName = bucketDisplayName,
            mediaType = source.mediaType,
            width = source.width,
            height = source.height,
            dateAdded = source.dateAdded,
            dateModified = source.dateModified,
            isSelected = source.isSelected,
            parent = parent,
            count = count
    )
}