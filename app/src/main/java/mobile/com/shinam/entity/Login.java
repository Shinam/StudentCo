package mobile.com.shinam.entity;

public class Login {
int Person;
String Nom; String Prenom;
public int getPerson() { return Person;
}
public void setPerson(int Person) { this.Person = Person;
}
public String getNom() { return Nom;
}
public void setNom(String Nom) { this.Nom = Nom;
}
public String getPrenom() { return Prenom;
}
public void setPrenom(String Prenom) { this.Prenom = Prenom;
}
@Override
public String toString() { return this.Prenom+" "+this.Nom;
} }