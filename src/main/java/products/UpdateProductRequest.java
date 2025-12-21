package products;


// Request model for updating a product

public class UpdateProductRequest
{  private String title;
    private int price;

    public UpdateProductRequest() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}

