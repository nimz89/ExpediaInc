/**
 * Created by nikhan on 04/10/15.
 */
public class StripingOrderNode {

    public String getStripingValue() {
        return StripingValue;
    }

    public void setStripingValue(String stripingValue) {
        StripingValue = stripingValue;
    }

    public String getStripingKey() {
        return StripingKey;
    }

    public void setStripingKey(String stripingKey) {
        StripingKey = stripingKey;
    }

    String StripingKey;
    String StripingValue;


    public StripingOrderNode(String StripingKey, String StripingValue)
    {
        this.StripingKey=StripingKey;
        this.StripingValue=StripingValue;

    }

    public StripingOrderNode(String StripingKey)
    {
        this.StripingKey=StripingKey;

    }

}

