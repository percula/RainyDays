package dev.percula.rainydays.db

import androidx.paging.PagedList
import dev.percula.rainydays.model.RainData

class RainDataBoundaryCallback: PagedList.BoundaryCallback<RainData>() {

    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
    }

    override fun onItemAtEndLoaded(itemAtEnd: RainData) {
        super.onItemAtEndLoaded(itemAtEnd)
    }

    override fun onItemAtFrontLoaded(itemAtFront: RainData) {
        super.onItemAtFrontLoaded(itemAtFront)
    }

}