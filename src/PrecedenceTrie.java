import java.util.*;

/**
 * Created by nikhan on 04/10/15.
 */
public class PrecedenceTrie {
    public AttributeNode getRoot() {
        return root;
    }

    private AttributeNode root;

    public PrecedenceTrie(AttributeNode root) {

    this.root=root;

    }

    // Method to insert a new sentence to Trie
    public void insert(LinkedList list,ArrayList<StripingOrderNode> stripingOrder) {

        int length = list.size();
        AttributeNode crawl = root;
        AttributeNode temp=null;

        // Traverse through all words
        for (int level = 0; level < length; level++) {
            HashMap<String, AttributeNode> child = crawl.getChildren();
            String word = (String) list.get(level);

            // If there is already a child for current node
            if (child.containsKey(word))
                crawl = child.get(word);
            else   // Else create a child
            {
                if (level % 2 == 0) {
                    int index = 0;

                    for (StripingOrderNode st : stripingOrder) {

                        if (st.getStripingKey().equals(word)) {
                            break;
                        }
                        index++;
                    }

                    temp = new AttributeNode(word, false, 1, index);
                } else temp = new AttributeNode(word, false, 0);
                child.put(word, temp);
                crawl = temp;
            }
        }
    }


    public void getMatchingConfigKeyValue(ArrayList<StripingOrderNode> stripingOrder) {

        String resultValue = "";
        AttributeNode currentNode = root;
        Queue<AttributeNode> probableMatches = new LinkedList();
        String currentValueFromReqContext = "";

        while(true)
        {

            HashMap<String, AttributeNode> child = currentNode.getChildren();
            Collection<AttributeNode> attributes= child.values();
            int maximumPrecedence=Integer.MAX_VALUE;
            AttributeNode maxPrecedenceNode=null;

            //Find attribute with max precedence acc. to striping order
            for (AttributeNode atrs : attributes)
            {
                if(atrs.getPrecedence()<maximumPrecedence && !atrs.getIsChecked()) {
                    maxPrecedenceNode = atrs;
                    maximumPrecedence=atrs.getPrecedence();
                }


            }
            //If it is a value node
            if(currentNode.equals(root) && maxPrecedenceNode.getValue().equals("value"))

            {
                //check to see if probableMatches queue is empty before returning value, if not, set currentnode as front of queue
                if(!probableMatches.isEmpty())
                {
                    currentNode=probableMatches.element();
                    continue;
                }
                //check to see if probableMatches queue is empty before returning value, if empty, add value to result and break
                else
                {

                    child = maxPrecedenceNode.getChildren();
                    resultValue += "--" + maxPrecedenceNode.getValue();
                    for (String key : child.keySet())
                    {
                        AttributeNode value = child.get(key);
                        resultValue += "--" + value.getValue();

                    }

                    break;
                }
            }
            //set attribute node with max precedence as current node and continue
            else
            {
                currentNode=maxPrecedenceNode;
            }


            resultValue += "--" +currentNode.getValue();
            currentNode.setIsChecked(true);
            child=currentNode.getChildren();

            //find striping from req. context value for current attribute node
            for(StripingOrderNode st: stripingOrder)
            {
                if(st.getStripingKey().equals(currentNode.getValue())) {
                    currentValueFromReqContext = st.getStripingValue();
                    break;
                }
            }


            if(currentNode.getValue().equals("value"))
            {
                resultValue += "--" + currentNode.getChildren().entrySet().iterator().next().getKey();
                break;

            }

            //check if attribute node had the request context value
            if(currentNode.getChildren().containsKey(currentValueFromReqContext))
            {
                resultValue += "--" + currentValueFromReqContext;
                currentNode = child.get(currentValueFromReqContext);
                //if current node has multiple children multiple children and is of value node type, add to probableMatches queue
                if(currentNode.getChildren().size()>1 && currentNode.getAttributeType()==0)
                {
                    probableMatches.add(currentNode);
                }
                continue;

            }
            //If current attribute node does not have request context value then this path will not give us a value, go back to root
            else
            {
                currentNode=root;
                resultValue="";
                continue;
            }

        }

        System.out.println(resultValue);

    }

}
