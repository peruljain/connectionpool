package org.example;

import org.example.util.DateUtil;

import java.util.List;

public class BoundedBlockingQueue<T> {

    private final List<T> objects;
    private final Integer size;

    public BoundedBlockingQueue(final List<T> objects, final Integer size) {
        this.objects = objects;
        this.size = size;
    }

    public void enqueue(T object) throws InterruptedException {
        synchronized (this) {
            while (objects.size() >= size) {
                System.out.println(DateUtil.getDate() + " " +Thread.currentThread().getName() + " Waiting in enqueue");
                System.out.println(DateUtil.getDate() + " " +Thread.currentThread().getName() + " " + objects.size());
                wait();
            }
            objects.add(object);
            notifyAll();
        }
    }

    public void enqueueWithoutWait(T object) throws InterruptedException {
        synchronized (this) {
            if (objects.size() < this.size) {
                objects.add(object);
                notifyAll();
            }
        }
    }

    public T dequeue() throws InterruptedException {
        synchronized (this) {
            while (objects.isEmpty()) {
                System.out.println(DateUtil.getDate() + " " +Thread.currentThread().getName() + " Waiting in deque");
                System.out.println(DateUtil.getDate() + " " +Thread.currentThread().getName() + " " + objects.size());
                wait();
            }
            T object = objects.remove(0);
            notifyAll();
            return object;
        }
    }



    public synchronized int size() {
        return objects.size();
    }

}
