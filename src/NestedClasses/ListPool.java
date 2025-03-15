package NestedClasses;

public class ListPool { // top level class

    public void messageInListPool() {
        System.out.println("This is a ListPool object");
    }
    public static class MyLinkedList { // Static member class
        private static int maxNumOfLists = 100;
        private int currentNumOfLists;

        private interface ILink { // static member interface
            int MAX_NUM_OF_NODES = 2000; //
        }

        public static class Node implements ILink {
            private int max = MAX_NUM_OF_NODES;

            public void messageInNode() {
                int maxLists = maxNumOfLists; // Access static field in outer class
                int maxNode = max; // access instace field in member class

                messageInLinkedList(); // call a static method in outer class
            }
        }
        public static void messageInLinkedList() {
            System.out.println("This is MyLinkedList class.");
        }

        public static class BiNode implements ILink {
            public static void printSimpleName() { // static method
                System.out.println(BiNode.class.getSimpleName());
            }

            public void printName() { // instance method
                System.out.println(this.getClass().getName());
            }
        }
    }

    interface IBiLink extends MyLinkedList.ILink {
        class BiTraversal {}
    }

    public class SortedList {
        private static class SortCriteria {}
    }
}
