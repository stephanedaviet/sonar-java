
class A {

  // test that we compute flow not only on relational SV both also on SVs from which relational SV was computed (recursively)
  void rel() {
    int c = 0; // flow@unary_rel,rel {{'c' is assigned non-null.}}
    int a = c;  // flow@unary_rel,rel {{'a' is assigned non-null.}}
    int b = 0;  // flow@unary_rel,rel {{'b' is assigned non-null.}}
    boolean cond = (b == a) == true; // see SONARJAVA-1911
    if (cond) { // Noncompliant [[flows=rel]] flow@rel {{Expression is always true.}} flow@unary_rel {{Implies 'cond' is true.}} flow@unary_rel {{Implies 'cond' is non-null.}}

    }

    if (!cond) { // Noncompliant [[flows=unary_rel]] flow@unary_rel {{Expression is always false.}}

    }
  }

  void catof1() {
    Object a = new Object(); // flow@catof1 {{Constructor implies 'non-null'.}} flow@catof1 {{'a' is assigned non-null.}}
    if (a == null) { // Noncompliant [[flows=catof1]] {{Change this condition so that it does not always evaluate to "false"}} flow@catof1 {{Expression is always false.}}
    System.out.println();
  }
}

  void catof2() {
    Object a = new Object(); // flow@catof2 {{Constructor implies 'non-null'.}} flow@catof2 {{'a' is assigned non-null.}}
    if ((a == null) == true) { // Noncompliant [[flows=catof2]] {{Change this condition so that it does not always evaluate to "false"}} flow@catof2 {{Expression is always false.}}
      System.out.println();
    }
  }

  void catof3() {
    Object a = new Object(); // flow@catof3 {{Constructor implies 'non-null'.}} flow@catof3 {{'a' is assigned non-null.}}
    Object b = null; // flow@catof3 {{'b' is assigned null.}}
    if ((a == b) == true) { // Noncompliant [[flows=catof3]] {{Change this condition so that it does not always evaluate to "false"}} flow@catof3 {{Expression is always false.}}
      System.out.println();
    }
  }

  void catof3b() {
    Object a = new Object(); // flow@catof3b {{Constructor implies 'non-null'.}} flow@catof3b {{'a' is assigned non-null.}}
    Object b = null; // flow@catof3b {{'b' is assigned null.}}
    boolean cond = a == b;  // no message here, because no constraint on 'a==b', it is not yet evaluated se SONARJAVA-1911
    b = new Object(); // no message here, b is not tracked yet
    a = null; // no message here, a is not tracked yet
    if (cond == true) { // Noncompliant [[flows=catof3b]] {{Change this condition so that it does not always evaluate to "false"}} flow@catof3b {{Expression is always false.}}
      System.out.println();
    }
  }

  void npe1(Object a) {
    Object b = null;
    // FIXME SONARJAVA-2272 Incorrect symbol is reported when constraint is learned on SV
    if (a == b) { // flow@npe1 {{Implies 'b' is null.}}
      a.toString(); // Noncompliant [[flows=npe1]] flow@npe1 {{'a' is dereferenced.}}
    }
  }

  void npe2(Object a, Object b) {
    if (a == b) {
      // FIXME SONARJAVA-2272
      if (b == null) { // flow@npe2 {{Implies 'b' is null.}}
        a.toString(); // Noncompliant [[flows=npe2]] flow@npe2 {{'a' is dereferenced.}}
      }
    }
  }

  void same_symbolic_value_referenced_with_different_symbols() {
    Object o = null; // flow@samesv {{'o' is assigned null.}}
    Object a = o; // flow@samesv {{'a' is assigned null.}}
    Object b = o; // flow@samesv {{'b' is assigned null.}}
    if (a == b) { // Noncompliant [[flows=samesv]] flow@samesv {{Expression is always true.}}

    }
  }

  void equalsSV() {
    Object o = true; // flow@equals {{'o' is assigned true.}} flow@equals {{'o' is assigned non-null.}}
    Object a = o; // flow@equals {{'a' is assigned true.}} flow@equals {{'a' is assigned non-null.}}
    Object b = o; // flow@equals {{'b' is assigned true.}} flow@equals {{'b' is assigned non-null.}}
    if (a.equals(b)) { // Noncompliant [[flows=equals]] flow@equals {{Expression is always true.}}

    }
  }

  void f() {
    boolean a = true;  // flow@nested {{'a' is assigned true.}} flow@nested {{'a' is assigned non-null.}}
    boolean b = true;  // flow@nested {{'b' is assigned true.}} flow@nested {{'b' is assigned non-null.}}
    if (a == b == true) { // Noncompliant [[flows=nested]] flow@nested {{Expression is always true.}}

    }
  }

}
