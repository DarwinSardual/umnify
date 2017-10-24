package com.example.darwin.umnify;

import java.util.HashMap;

public class LeastRecentlyUsedCache<K, V>{

    private int maxSize;

    private HashMap<K, Node<K, V>> cache;
    private Node<K, V> leastUsed;
    private Node<K, V> mostUsed;
    private OnRemoveFromCache<K> onRemoveFromCache;


    public LeastRecentlyUsedCache(int maxSize){

        cache = new HashMap<>();
        this.maxSize = maxSize;

        leastUsed = null;
        mostUsed = null;
    }

    public void setOnRemoveFromCache(OnRemoveFromCache<K> onRemoveFromCache) {
        this.onRemoveFromCache = onRemoveFromCache;
    }

    public void put(K key, V value){

        if(cache.containsKey(key)){
            return;
        }

        Node<K, V> node = new Node<>(key, value);
        node.setPrevious(mostUsed);
        if(mostUsed != null){
            mostUsed.setNext(node);
        }
        mostUsed = node;

        if(isFull()){
            if(leastUsed != null){
                remove(leastUsed.getKey());
            }
        }else{
            if(isEmpty()){
                leastUsed = node;
            }
        }

        cache.put(key, node);
    }

    public V get(K key){

        Node<K, V> node = cache.get(key);
        if(node == null) {
            return null;
        }

        if(node.getKey() == mostUsed.getKey()){
            return node.getValue();
        }

        Node<K, V> prevNode = node.getPrevious();
        Node<K, V> nextNode = node.getNext();

        if(node.getKey() == leastUsed.getKey()){
            nextNode.setPrevious(null);
            leastUsed = nextNode;
        }else if(node.getKey() != leastUsed.getKey() && node.getKey() != mostUsed.getKey()){

            prevNode.setNext(nextNode);
            nextNode.setPrevious(prevNode);
        }

        node.setPrevious(mostUsed);
        mostUsed.setNext(node);
        mostUsed = node;
        mostUsed.setNext(null);

        return node.getValue();
    }

    public V remove(K key){

        Node<K, V> node = cache.get(key);
        if(node == null) return null;

        if(mostUsed != null && node.getKey() == mostUsed.getKey()){

            mostUsed = node.getPrevious();
            if(mostUsed != null)
                mostUsed.setNext(null);
        }else if(leastUsed != null && node.getKey() == leastUsed.getKey()){

            leastUsed = node.getNext();
            if(leastUsed != null)
                leastUsed.setPrevious(null);
        }else{

            Node<K, V> prevNode = node.getPrevious();
            Node<K, V> nextNode = node.getNext();

            prevNode.setNext(nextNode);
            nextNode.setPrevious(prevNode);
        }

        if(onRemoveFromCache != null){
            onRemoveFromCache.onRemove(key);
        }

        cache.remove(key);
        return node.getValue();
    }

    public int getMaxSize() {
        return maxSize;
    }

    public int getSize() {
        return cache.size();
    }

    public void evictAll(){
        mostUsed = null;
        leastUsed = null;

        cache.clear();
    }

    public boolean isEmpty(){
        return cache.size() == 0;
    }

    public boolean isFull(){
        return cache.size() >= maxSize;
    }

    public interface OnRemoveFromCache<K>{

        public void onRemove(K key);
    }
}
