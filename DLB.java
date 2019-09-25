// import ArrayList for the DLB
import java.util.ArrayList;

public class DLB
{
    // for DLB, the node has a next, a child, and data
    public class Node
    {
        public Node next;   // next is the alternative character to the current one
        public Node child;  // child is the next character in the current word
        public char data;   // the character held in the node
    }

    // the first node is null, so that the word(s) can be added to an existing node/trie
    private Node root = null;

    // insert input into the DLB
    public void insert(String input)
    {
        // start the insert at the root
        Node curr = root;

        // loop through the input one character at a time
        for(int i = 0; i < input.length(); i++)
        {
            // have a be the current character
            char a = input.charAt(i);
            // if the current is null, or if the current's data is not the current character
            if(curr == null || curr.data != a)
            {
                // while the current and next character are not null
                while(curr != null && curr.next != null)
                {
                    // if the next node is the current character
                    if(curr.next.data == a)
                    {
                        // break the while loop
                        break;
                    }

                    // set current to be the next node
                    curr = curr.next;
                }

                // if the root was null, when making a new DLB
                if(root == null)
                {
                    // create a new node for current to be
                    curr = new Node();
                    // set the new node (the new current) to be current character
                    curr.data = a;
                    // set root to the new current node
                    root = curr;
                }
                // if the curr.next is null
                else if(curr.next == null)
                {
                    // add a new node to be next to the current node
                    curr.next = new Node();
                    // set the new node (new next) to the current data
                    curr.next.data = a;
                    // set current to be the new next
                    curr = curr.next;
                }
                // if we found the node we want
                else if(curr.next.data == a)
                {
                    // then just set current to be next
                    curr = curr.next;
                }
            }
            // if the index is not one less than the length of the input
            // if the index hasn't reached the end of the input
            if(i != input.length()-1)
            {
                // if the current's child is null (doesn't exist yet)
                if(curr.child == null)
                {
                    // create a new node at the current's child
                    curr.child = new Node();
                    // set the new child's data to the input's next character
                    curr.child.data = input.charAt(i+1);
                }
                // set current to be it's child
                curr = curr.child;
            }
        }
        // if the current's child is null (doesn't exist)
        if(curr.child == null)
        {
            // create a new node at current's child
            curr.child = new Node();
            // make current's child the termination character
            curr.child.data = '$';
        }
        // if the current's child is not null
        else
        {
            // create a new end node
            Node end = new Node();
            // set end's data to be the termination character
            end.data = '$';
            // set end's next node to be the current's child (adding it to the linked-list of current's children)
            end.next = curr.child;
            // set current's child to be end
            curr.child = end;
        }
    }

    // check if a specific input is contained within the dlb
    public boolean contains(String input)
    {
        // set current to be the root node
        Node curr = root;
        //new way of writing loops
        // "outer" is a tag that I can use later when I am continuing the loop
        // character loop for each character in input + the termination character
        outer: for(char a: (input+'$').toCharArray())
        {
            // if current is null
            if(curr == null)
            {
                // return null, the input can not be contained in the dlb
                return false;
            }

            // while current is not null
            while(curr != null)
            {
                // if current's data is the character that is being searched for in this iteration of the loop
                if(curr.data == a)
                {
                    // set current to be its child
                    curr = curr.child;
                    // continue the outer loop
                    // this jumps to the next iteration of the outer loop
                    continue outer;
                }
                
                // if the data being searched is not found, then set current to next and do the next iteration of the inner (while) loop
                curr = curr.next;
            }
            // if the while loop terminates, then the input has not been found, return null
            return false;
        }
        // outer only terminates if the input has been found, return true
        return true;
    }

    // takes the user's current prefix and preficts 5 items for the user to choose from from the dlb
    public ArrayList<String> predict(String prefix)
    {
        // set current to be the root node
        Node curr = root;
        // create a new array list of strings to hold the predictions
        ArrayList<String> predictions = new ArrayList<String>();
        // same type of loop used in contains
        // "outer" is a tag that I can use later when I am continuing the loop
        // iterate through the prefix
        outer: for(int i = 0; i < prefix.length(); i++)
        {
            // set the character that we are searching for to be the current prefix character
            char a = prefix.charAt(i);
            // if the current node is null
            if(curr == null)
            {
                // just return predictions, nothing can be added to the array list
                return predictions;
            }

            // while current is not null
            while(curr != null)
            {
                // if current's data is the character that we are searching for
                if(curr.data == a)
                {
                    // if the current index is one less than the length of prefix
                    if(i == prefix.length()-1)
                    {
                        // break outer
                        break outer;
                    }
                    // if we have more prefix to search through, then we will
                    // set current to be its child
                    curr = curr.child;
                    // continue outer
                    continue outer;
                }
                // if current's data is not the character we are searching for, then set current to its next
                curr = curr.next;
            }
            // after the while loop terminates, return predictions
            return predictions;
        }
        // after outer terminates, recursively call predict with current's child being the new current, predictions, and prefix
        predict(curr.child, predictions, prefix);
        // after the recursve calls finish, return predictions
        return predictions;
    }

    // private helper method to help recursively call predict
    // takes the current node, the array list, and the profix the user entered
    private void predict(Node curr, ArrayList<String> predictions, String prefix)
    {
        // if the current node is null
        if(curr == null)
        {
            // then we have gotten to the end of the dlb, just return
            return;
        }

        // if the current's data is the termination character
        if(curr.data == '$')
        {
            // then add that word to predictions
            predictions.add(prefix);
        }

        //if all 5 predictions have already been found
        if(predictions.size() == 5)
        {
            // then all five necessary predictions have been found, jsut return
            return;
        }

        // recursively call predict with current's child being the new current and adding current's data to the prefix
        predict(curr.child, predictions, prefix+curr.data);
        // recursively call predict with current's next being the new current, and do not change predictions or prefix
        predict(curr.next, predictions, prefix);
    }
}