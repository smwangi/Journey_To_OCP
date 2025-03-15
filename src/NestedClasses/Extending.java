package NestedClasses;

public class Extending {
    public static void main(String[] args) {
        //Outer instance passed explicitly in constructor call
        SubInner obj1 = new SubInner(new OuterA());
        System.out.println(obj1.getClass());

        // No outer instance passed explicitly in constructor call to InnerB
        OuterB.InnerB obj2 = new OuterB().new InnerB();
        System.out.println(obj2.getClass());
        Outer objRef = new Outer();
        System.out.println(objRef.createInner().getSecret());

        B.C obj = new B().new C();
    }
}
class Outer {
    private int secret;
    Outer() {
        secret = 123;
    }

    class Inner {
        int getSecret() {
            return secret;
        }
    }
    Inner createInner() {
        return new Inner();
    }
}

class A {
    int val;
    A(int v) {
        val = v;
    }
}

class B extends A {
    int val = 1;
    B() {
        super(2);
    }

    class C extends A {
        int val = 3;
        C() {
            super(4);
            System.out.println(B.this.val);
            System.out.println(C.this.val);
            System.out.println(super.val);
        }
    }
}
