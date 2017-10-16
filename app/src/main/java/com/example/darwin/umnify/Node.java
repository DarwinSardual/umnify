package com.example.darwin.umnify;

public class Node<K, V>{

    private K key;
    private V value;

    private Node previous;
    private Node next;

    public Node(K key, V value){
        this. key = key;
        this.value = value;
        this.previous = null;
        this.next = null;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public Node getNext() {
        return next;
    }

    public Node getPrevious() {
        return previous;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public void setPrevious(Node previous) {
        this.previous = previous;
    }

    public void setNext(Node next) {
        this.next = next;
    }
}
