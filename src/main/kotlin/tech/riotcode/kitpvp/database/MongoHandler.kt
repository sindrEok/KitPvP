package tech.riotcode.kitpvp.database

import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import com.mongodb.client.model.ReplaceOptions
import org.bson.Document
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.function.Consumer
import java.util.stream.StreamSupport
import kotlin.streams.toList


object MongoHandler {

    private val executorService: ExecutorService = Executors.newFixedThreadPool(2)

    private val replaceOptions: ReplaceOptions = ReplaceOptions().upsert(true)

    private val mongoDatabase: MongoDatabase = MongoClients.create().getDatabase("KitPvP")

    fun documentLoadAll(consumer: Consumer<List<Document?>>, collection: String, async: Boolean) {
        if (async) {
            executorService.execute {
                consumer.accept(
                    StreamSupport.stream(mongoDatabase.getCollection(collection).find().spliterator(), false).toList()
                )
            }
        } else {
            consumer.accept(
                StreamSupport.stream(mongoDatabase.getCollection(collection).find().spliterator(), false).toList()
            )
        }
    }

    fun documentLoad(consumer: Consumer<Document?>, collection: String, key: String, value: String, async: Boolean) {
        if (async) {
            executorService.execute {
                consumer.accept(
                    mongoDatabase.getCollection(collection).find(Filters.eq(key, value)).first()
                )
            }
        } else {
            consumer.accept(mongoDatabase.getCollection(collection).find(Filters.eq(key, value)).first())
        }
    }

    fun documentSave(collection: String, json: String, key: String, value: String, async: Boolean) {
        if (async) {
            executorService.execute {
                mongoDatabase.getCollection(collection)
                    .replaceOne(Filters.eq(key, value), Document.parse(json), replaceOptions)
            }
        } else {
            mongoDatabase.getCollection(collection)
                .replaceOne(Filters.eq(key, value), Document.parse(json), replaceOptions)
        }
    }
}