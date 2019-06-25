package dev.percula.rainydays.db.paging

import androidx.paging.PagedList
import dev.percula.rainydays.db.local.AppDatabase
import dev.percula.rainydays.db.network.NetworkAsyncs
import dev.percula.rainydays.db.network.NetworkService
import dev.percula.rainydays.model.Location
import dev.percula.rainydays.model.RainData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import timber.log.Timber
import java.util.concurrent.Executor

class RainDataBoundaryCallback(ioExecutor: Executor,
                               private val networkService: NetworkService,
                               private val database: AppDatabase,
                               val location: Location,
                               private val coroutineScope: CoroutineScope,
                               private val networkPageSize: Int): PagedList.BoundaryCallback<RainData>() {

    private val helper = PagingRequestHelper(ioExecutor)

    override fun onZeroItemsLoaded() {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
            fetchPrecipitation(
                startDate = LocalDate.now().minusDays(networkPageSize.toLong()),
                endDate = LocalDate.now()
            )
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: RainData) {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) {
            fetchPrecipitation(
                startDate = itemAtEnd.date.minusDays(networkPageSize.toLong()),
                endDate = itemAtEnd.date.minusDays(1)
            )
        }
    }

    override fun onItemAtFrontLoaded(itemAtFront: RainData) {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.BEFORE) {
            // Make sure we don't request future dates
            val startDate = itemAtFront.date.plusDays(1)
            if (startDate.isAfter(LocalDate.now())) return@runIfNotRunning
            val endDate =
                itemAtFront.date.plusDays(networkPageSize.toLong())?.takeUnless { it.isAfter(LocalDate.now()) }
                    ?: LocalDate.now()

            fetchPrecipitation(
                startDate = startDate,
                endDate = endDate
            )
        }
    }

    private fun fetchPrecipitation(startDate: LocalDate, endDate: LocalDate) {
        coroutineScope.launch {
            NetworkAsyncs.fetchPrecipitationAsync(
                networkService = networkService,
                startDate = startDate,
                endDate = endDate,
                location = location
            ).observeForever {
                it.doOnData {
                    coroutineScope.launch(context = Dispatchers.IO) {
                        try {
                            database.rainDataDao().insertAll(*it.toTypedArray())
                        } catch (e: Exception) {
                            Timber.e(e)
                        }
                    }
                }
            }
        }
    }

}