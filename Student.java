
public class Student {
    private int id;
    private String name;
    private String dob;
    private double score;
    private String contact;
    private String address;

    public Student(int id, String name, String dob, double score, String contact, String address) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.score = score;
        this.contact = contact;
        this.address = address;
    }

    public Student(String name, String dob, double score, String contact, String address) {
        this(-1, name, dob, score, contact, address);
    }

    public int getId() { 
        return id; 
    }
    
    public void setId(int id) { 
        this.id = id; 
    }

    public String getName() { 
        return name; 
    }
    
    public String getDob() { 
        return dob; 
    }
    
    public double getScore() { 
        return score; 
    }
    
    public String getContact() { 
        return contact; 
    }
    
    public String getAddress() { 
        return address; 
    }

    public void setName(String name) { 
        this.name = name; 
    }
    
    public void setDob(String dob) { 
        this.dob = dob; 
    }
    
    public void setScore(double score) { 
        this.score = score; 
    }
    
    public void setContact(String contact) { 
        this.contact = contact; 
    }
    
    public void setAddress(String address) { 
        this.address = address; 
    }

    @Override
    public String toString() {
        return String.format(
            "ID: %d, Name: %s, DOB: %s, Score: %.2f, Contact: %s, Address: %s",
            id, name, dob, score, contact, address
        );
    }
}
