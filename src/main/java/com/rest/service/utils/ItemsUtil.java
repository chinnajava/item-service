package com.rest.service.utils;

import com.rest.service.model.Items;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class ItemsUtil {
    private static int MAX_SECONDS = 2;
    private static int MAX_RECORD_COUNT = 100;
    private Queue<Items> rateWindowQueue = new PriorityQueue<>();
    private SortedSet<Items> allItems = new ConcurrentSkipListSet();
    private SortedSet<Items> top100 = new ConcurrentSkipListSet();

    private ZoneId zoneId = ZoneId.systemDefault();
    private ReentrantLock lock = new ReentrantLock();



    public void add(Items items) {
        boolean isInvalidPostedTime = items.getItem().getTimestamp().isBefore(LocalDateTime.now());
        Assert.isTrue(isInvalidPostedTime, "posted time in the future");
        long diffSeconds = LocalDateTime.now().atZone(zoneId).toEpochSecond() - items.getItem().getTimestamp().atZone(zoneId).toEpochSecond();
        if (diffSeconds <= MAX_SECONDS) {
            allItems.add(items);
            rateWindowQueue.offer(items);
        }
        top100.add(items);
        if (top100.size() > MAX_RECORD_COUNT) {
            top100.remove(top100.last());
        }
    }

    public Set<Items> findItems() {
        if (rateWindowQueue.size() > MAX_RECORD_COUNT) {
            return allItems;
        }
        return top100;
    }

    @Scheduled(fixedRate = 1000)
    public void cleanUpItemsInQueuePostedMoreThanTwoSeconds() {
        boolean locked = false;
        try {
            locked = lock.tryLock(1, TimeUnit.SECONDS);
            if (locked) {
                Instant now = Instant.now();
                while (!rateWindowQueue.isEmpty()) {
                    LocalDateTime first = rateWindowQueue.peek().getItem().getTimestamp();
                    long diffSeconds = now.atZone(zoneId).toEpochSecond() - first.atZone(zoneId).toEpochSecond();
                    if (diffSeconds >= MAX_SECONDS) {
                        rateWindowQueue.poll();
                        allItems.remove(allItems.last());
                    } else {
                        break;
                    }
                }
            }
        } catch (InterruptedException e) {
            System.out.println(e);
        } finally {
            if (locked) lock.unlock();
        }

    }


}
