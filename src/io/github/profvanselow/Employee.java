package io.github.profvanselow;

class Employee {

  StringBuilder name;
  String username;
  String password;
  String email;

  public Employee(String name, String pw) {
    this.name = new StringBuilder(name);
    if (checkName(name)) {
      setUsername(name);
      setEmail(name);
    } else {
      username = "default";
      email = "user@oracleacademy.Test";
    }
    if (isValidPassword(pw)) {
      password = pw;
    } else {
      password = "pw";
    }
  }

  private boolean isValidPassword(String password) {
    // must contain uppercase, lowercase, and special
    int lowerCount = 0;
    int upperCount = 0;
    int specialCount = 0;
    for (int i = 0; i < password.length(); i++) {
      if (Character.isLowerCase(password.charAt(i))) {
        lowerCount++;
      } else if (Character.isUpperCase(password.charAt(i))) {
        upperCount++;
      } else if (password.substring(i, i + 1).matches("[^A-Za-z0-9]")) {
        specialCount++;
      }
    } //endfor
    if (lowerCount > 0 && upperCount > 0 && specialCount > 0) {
      return true;
    } else {
      return false;
    }
  }

  private void setEmail(String name) {
    email = name.substring(0, name.indexOf(" ")).toLowerCase() + "." + name
        .substring(name.indexOf(" ") + 1).toLowerCase() + "@oracleacademy.Test";
  }

  private boolean checkName(String name) {
    if (name.contains(" ")) {
      return true;
    } else {
      return false;
    }
  }

  private void setUsername(String name) {
    username =
        name.substring(0, 1).toLowerCase() + name.substring(name.indexOf(" ") + 1).toLowerCase();
  }

  public String toString() {
    return "Employee Details"
        + "\nName : " + this.name
        + "\nUsername : " + this.username
        + "\nEmail : " + this.email
        + "\nInitial Password : " + this.password;
  }

}
