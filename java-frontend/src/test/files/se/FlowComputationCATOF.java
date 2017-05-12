import java.util.*;

class A {

  private Object maybeNull() {
    return new Random().nextBoolean() ? null : new Object(); // flow@catof2a
  }

  private Object getNull() {
    return null;
  }

  public void catof1() {
    Object a = new Object(); // flow@catof1 {{Constructor implies 'non-null'.}} flow@catof1 {{'a' is assigned non-null.}}
    if (a == null) { // Noncompliant [[flows=catof1]] {{Change this condition so that it does not always evaluate to "false"}} flow@catof1 {{Expression is always false.}}
      System.out.println();
    }
  }

  public void catof2a() {
    Object foo = maybeNull(); // flow@catof2a {{'maybeNull()' can return non-null.}} flow@catof2a {{'foo' is assigned non-null.}}
    foo.getClass();  // Noncompliant
    if (foo == null) {  // Noncompliant [[flows=catof2a]] {{Change this condition so that it does not always evaluate to "false"}} flow@catof2a {{Expression is always false.}}
      log(foo.toString());
    } else {
      log(foo.getClass());
    }
  }

  public void catof2b() {
    Object foo = getNull(); // flow@catof2b {{'getNull()' returns null.}}  flow@catof2b {{'foo' is assigned null.}}
    if (foo == null) {  // Noncompliant [[flows=catof2b]] {{Change this condition so that it does not always evaluate to "true"}} flow@catof2b {{Expression is always true.}}
      log(foo.toString()); // Noncompliant NPE
    } else {
      log(foo.getClass());
    }
  }

  public void catof3() {
    Object c = null;
    Object foo = null; // flow@catof3 {{'foo' is assigned null.}}
    Object b = foo;   // flow@catof3 {{'b' is assigned null.}}
    if (b == null) { // Noncompliant [[flows=catof3]] flow@catof3 {{Expression is always true.}}
      log(foo.toString()); // Noncompliant NPE
    } else {
      log(foo.getClass());
    }
  }

  void catof4() {
    Object a = getNull(); // flow@catof4 {{'getNull()' returns null.}} flow@catof4 {{'a' is assigned null.}}
    Object b = getNull(); // flow@catof4 {{'getNull()' returns null.}} flow@catof4 {{'b' is assigned null.}}
    if (a == b == true) { // Noncompliant [[flows=catof4]] flow@catof4 {{Expression is always true.}}

    }
  }

}

