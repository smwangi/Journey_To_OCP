package NestedClasses;

public class SubInner extends OuterA.InnerA{

    // Mandatory non-zero argument constructor, sets up the relationships correctly between objects involved.
    SubInner(OuterA outerRef) {
        outerRef.super(); // explicit super call
    }
}
