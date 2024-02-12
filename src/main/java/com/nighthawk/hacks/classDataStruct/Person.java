package com.nighthawk.hacks.classDataStruct;

/*
Adapted from Person POJO, Plain Old Java Object.
 */
public class Person extends Generics{
    // Class data
    private static String classType = "Person";
    public static KeyTypes key = KeyType.title;  // static initializer
	public static void setOrder(KeyTypes key) {Person.key = key;}
	public enum KeyType implements KeyTypes {title, uid, name, dob, age}

    // Instance data
    private String uid;  // user / person id
    private String password;
    private String name;
    private int csaPoints;
    private int cspPoints;
    private int profilePicInt;
    

    // Constructor with zero arguments
    public Person() {
        super.setType(classType);
    }

    // Constructor used when building object from an API
    public Person(String uid, String password, String name, int csaPoints, int cspPoints, int profilePicInt) {
        this();  // runs zero argument constructor
        this.uid = uid;
        this.password = password;
        this.name = name;
        this.csaPoints = csaPoints;
        this.cspPoints = cspPoints;
        this.profilePicInt = profilePicInt;
    }

    /* 'Generics' requires getKey to help enforce KeyTypes usage */
	@Override
	protected KeyTypes getKey() { return Person.key; }

    public String getUserID() {
        return uid;
    }

    /* 'Generics' requires toString override
	 * toString provides data based off of Static Key setting
	 */
    @Override
    public String toString() {		
        String output="";
        if (KeyType.uid.equals(this.getKey())) {
            output += this.uid;
        } else if (KeyType.name.equals(this.getKey())) {
            output += this.name;
        } else {
            output = super.getType() + ": " + this.uid + ", " + this.name;
            output += ", csaPoints=" + this.csaPoints;
            output += ", cspPoints=" + this.cspPoints;
            output += ", profilePicInt=" + this.profilePicInt;

        }
        return output;
    }
    

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCsaPoints() {
        return csaPoints;
    }
    
    public void setCsaPoints(int csaPoints) {
        this.csaPoints = csaPoints;
    }

    public int getCspPoints() {
        return cspPoints;
    }

    public void setCspPoints(int cspPoints) {
        this.cspPoints = cspPoints;
    }

    public int getProfilePicInt() {
        return profilePicInt;
    }

    public void setProfilePicInt(int profilePicInt) {
        this.profilePicInt = profilePicInt;
    }

    // Initialize static test data 
    public static Person[] init() {

        // basics of class construction
        Person p1 = new Person();
        p1.setName("Thomas Edison");
        p1.setUid("toby@gmail.com");
        p1.setPassword("123Toby!");
        p1.setCsaPoints(0);
        p1.setCspPoints(0);
        p1.setProfilePicInt(0);

        Person p2 = new Person();
        p2.setName("Alexander Graham Bell");
        p2.setUid("lexb@gmail.com");
        p2.setPassword("123LexB!");
        p2.setCsaPoints(0);
        p2.setCspPoints(0);
        p2.setProfilePicInt(0);

        Person p3 = new Person();
        p3.setName("Nikola Tesla");
        p3.setUid("niko@gmail.com");
        p3.setPassword("123Niko!");
        p3.setCsaPoints(0);
        p3.setCspPoints(0);
        p3.setProfilePicInt(0);

        Person p4 = null;
        Person p5 = null;
        try {
            p4 = new Person(
                "madam@gmail.com",
                "123Madam!",
                "Madam Currie",
                0,
                0,
                0
            );
    
            p5 = new Person(
                "jm1021@gmail.com",
                "123Qwerty!",
                "John Mortensen",
                0,
                0,
                0
            );
        } catch (Exception e) {
        }

        // Array definition and data initialization
        Person persons[] = {p1, p2, p3, p4, p5};
        return(persons);
    }

    public static void main(String[] args) {
        // obtain Person from initializer
        Person persons[] = init();
        Person.setOrder(Person.KeyType.title);

        // iterate using "enhanced for loop"
        for( Person person : persons ) {
            System.out.println(person);  // print object
        }
    }

}