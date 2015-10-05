

import java.util.HashMap;

/**
 * Created by nikhan on 15/09/15.
 */
public  class AttributeNode {

    public void setValue(String value) {
        this.value = value;
    }

    private String value;
    private HashMap<String,AttributeNode> children;


    public int getPrecedence() {
        return precedence;
    }

    public void setPrecedence(int precedence) {
        this.precedence = precedence;
    }

    public int precedence;



    public int getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(int attributeType) {
        this.attributeType = attributeType;
    }

    public int attributeType=0;


    public boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public boolean isChecked=false;
    public AttributeNode(String ch,boolean isChecked,int attributeType)  {
        value = ch;
        children = new HashMap<>();
        this.isChecked = isChecked;
        this.attributeType = attributeType;


    }
    public AttributeNode(String word,boolean isChecked,int attributeType, int precedence)  {
        value = word;
        children = new HashMap<>();
        this.isChecked = isChecked;
        this.attributeType = attributeType;
        this.precedence=precedence;


    }
    public HashMap<String,AttributeNode> getChildren() {   return children;  }
    public String getValue()                           {   return value;     }


}
