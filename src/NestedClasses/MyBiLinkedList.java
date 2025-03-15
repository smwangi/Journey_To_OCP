package NestedClasses;

public class MyBiLinkedList implements ListPool.IBiLink {
    public static void main(String[] args) {
        ListPool.MyLinkedList.BiNode.printSimpleName();
        ListPool.MyLinkedList.BiNode node1 = new ListPool.MyLinkedList.BiNode();
        node1.printName();
    }
}
