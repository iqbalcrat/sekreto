package app.com.sekreto.Items;

public class Item {

    private int mImageResource;
    private String line1;
    private String line2;

    public Item(int mImageResource, String line1, String line2) {
        this.mImageResource = mImageResource;
        this.line1 = line1;
        this.line2 = line2;
    }

    public int getmImageResource() {
        return mImageResource;
    }

    public String getLine1() {
        return line1;
    }

    public String getLine2() {
        return line2;
    }
}
