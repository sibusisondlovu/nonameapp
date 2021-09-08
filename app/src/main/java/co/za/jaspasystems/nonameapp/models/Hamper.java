package co.za.jaspasystems.nonameapp.models;

public class Hamper {
    private int id;
    private String name;
    private String contents;
    private double price;
    private String image;

    public Hamper() {
    }

    public Hamper(int id, String name, String contents, double price, String image) {
        this.id = id;
        this.name = name;
        this.contents = contents;
        this.price = price;
        this.image = image;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
