package dev.percula.rainydays.db.paging

import android.annotation.SuppressLint
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.lifecycle.LiveData
import androidx.paging.Config
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import java.util.concurrent.Executor

/**
 * Copy/pasted from Paging library and modified to disable placeholders.
 *
 * Constructs a `LiveData<PagedList>`, from this `DataSource.Factory`, convenience for
 * [LivePagedListBuilder].
 *
 * No work (such as loading) is done immediately, the creation of the first PagedList is is
 * deferred until the LiveData is observed.
 *
 * @param pageSize Page size.
 * @param initialLoadKey Initial load key passed to the first PagedList/DataSource.
 * @param boundaryCallback The boundary callback for listening to PagedList load state.
 * @param fetchExecutor Executor for fetching data from DataSources.
 *
 * @see LivePagedListBuilder
 */
@SuppressLint("RestrictedApi")
fun <Key, Value> DataSource.Factory<Key, Value>.toLiveDataNoPlaceholders(
    pageSize: Int,
    initialLoadKey: Key? = null,
    boundaryCallback: PagedList.BoundaryCallback<Value>? = null,
    fetchExecutor: Executor = ArchTaskExecutor.getIOThreadExecutor()
): LiveData<PagedList<Value>> {
    return LivePagedListBuilder(this, Config(pageSize = pageSize, enablePlaceholders = false))
        .setInitialLoadKey(initialLoadKey)
        .setBoundaryCallback(boundaryCallback)
        .setFetchExecutor(fetchExecutor)
        .build()
}