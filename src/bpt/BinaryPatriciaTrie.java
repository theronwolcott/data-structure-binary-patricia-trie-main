package bpt;

import bpt.UnimplementedMethodException;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <p>
 * {@code BinaryPatriciaTrie} is a Patricia Trie over the binary alphabet &#123;
 * 0, 1 &#125;. By restricting themselves
 * to this small but terrifically useful alphabet, Binary Patricia Tries combine
 * all the positive
 * aspects of Patricia Tries while shedding the storage cost typically
 * associated with tries that
 * deal with huge alphabets.
 * </p>
 *
 * @author YOUR NAME HERE!
 */
public class BinaryPatriciaTrie {

    /*
     * We are giving you this class as an example of what your inner node might look
     * like.
     * If you would prefer to use a size-2 array or hold other things in your nodes,
     * please feel free
     * to do so. We can *guarantee* that a *correct* implementation exists with
     * *exactly* this data
     * stored in the nodes.
     */
    // Inner class representing a node in the Patricia Trie
    private static class TrieNode {
        private TrieNode left, right; // Children nodes: left for '0', right for '1'
        private String str; // The string (bit sequence) stored at this node
        private boolean isKey; // True if this node represents a key in the trie

        // Default constructor for an empty node
        TrieNode() {
            this("", false);
        }

        // Constructor for a node with a specific string and key status
        TrieNode(String str, boolean isKey) {
            left = right = null;
            this.str = str;
            this.isKey = isKey;
        }
    }

    private TrieNode root; // Root node of the trie
    private AtomicBoolean bool; // Used to track results in recursive helpers

    /**
     * Simple constructor that will initialize the internals of {@code this}.
     */
    public BinaryPatriciaTrie() {
    root = new TrieNode("", false); // Initialize root as empty node
    bool = new AtomicBoolean(); // Used for returning results from helpers
    }

    /**
     * Searches the trie for a given key.
     *
     * @param key The input {@link String} key.
     * @return {@code true} if and only if key is in the trie, {@code false}
     *         otherwise.
     */
    public boolean search(String key) {
    bool.set(false); // Reset result flag
    searchHelper(key, root); // Start recursive search from root
    return bool.get(); // Return whether key was found
    }

    private void searchHelper(String key, TrieNode node) {
        bool.set(false); // Reset flag for each call
        if (node == null) {
            // Base case: reached a null node, key not found
            return;
        }
        // If at root, decide direction based on first bit
        // Empty str means we are at root
        if (node.str == "" && key.startsWith("0")) {
            searchHelper(key, node.left);
        } else if (node.str == "" && key.startsWith("1")) {
            searchHelper(key, node.right);
        } else if (key.equals(node.str) && node.isKey == true) {
            // Found the key at this node
            bool.set(true);
            return;
        } else if (key.startsWith(node.str) && key.substring(node.str.length()).startsWith("0")) {
            // Node's string is a prefix of key, go left
            searchHelper(key.substring(node.str.length()), node.left);
        } else if (key.startsWith(node.str) && key.substring(node.str.length()).startsWith("1")) {
            // Node's string is a prefix of key, go right
            searchHelper(key.substring(node.str.length()), node.right);
        }
    }

    /**
     * Inserts key into the trie.
     *
     * @param key The input {@link String} key.
     * @return {@code true} if and only if the key was not already in the trie,
     *         {@code false} otherwise.
     */
    public boolean insert(String key) {
    bool.set(false); // Reset result flag
    root = insertHelper(key, root); // Insert key starting from root
    return bool.get(); // Return true if key was newly inserted
    }

    private TrieNode insertHelper(String key, TrieNode n) {
        if (n == null) {
            // If current node is null, create new node for key
            bool.set(true); // Key was newly inserted
            return new TrieNode(key, true);
        }

        // If key matches node's string
        if (key.equals(n.str) && n.isKey) {
            // Key already exists
            return n;
        } else if (key.equals(n.str) && !n.isKey) {
            // Node exists but wasn't a key, mark as key
            n.isKey = true;
            bool.set(true);
            return n;
        }
        // If node's string is a prefix of key or node is root
        if (key.startsWith(n.str) || n.str.equals("")) {
            // Recurse left or right based on next bit
            if (key.substring(n.str.length()).startsWith("0")) {
                n.left = insertHelper(key.substring(n.str.length()), n.left);
            } else {
                n.right = insertHelper(key.substring(n.str.length()), n.right);
            }
        } else if (n.str.startsWith(key)) {
            // Key is a prefix of node's string, need to split node
            TrieNode newNode = new TrieNode(key, true); // New node for key
            if (n.str.substring(key.length()).startsWith("0")) {
                newNode.left = n; // Attach existing node as left child
            } else {
                newNode.right = n; // Attach existing node as right child
            }
            n.str = n.str.substring(key.length()); // Update existing node's string
            bool.set(true);
            return newNode;
        } else {
            // Find common prefix between key and node's string
            int index = diffIndex(key, n.str);
            String prefix = key.substring(0, index);
            TrieNode newNode = new TrieNode(prefix, false); // New splitter node
            n.str = n.str.substring(index); // Update node's string
            if (key.substring(index).startsWith("0")) {
                newNode.left = new TrieNode(key.substring(index), true); // Insert key left
                newNode.right = n; // Existing node right
            } else {
                newNode.right = new TrieNode(key.substring(index), true); // Insert key right
                newNode.left = n; // Existing node left
            }
            bool.set(true);
            return newNode;
        }
        return n; // Return current node
    }

    private int diffIndex(String key, String str) {
        // Find index where key and str differ
        for (int i = 0; i < key.length(); i++) {
            if (key.charAt(i) != str.charAt(i)) {
                return i;
            }
        }
        return -1; // Should not reach here if called correctly
    }

    /**
     * Deletes key from the trie.
     *
     * @param key The {@link String} key to be deleted.
     * @return {@code true} if and only if key was contained by the trie before we
     *         attempted deletion, {@code false} otherwise.
     */
    public boolean delete(String key) {
    bool.set(false); // Reset result flag
    root = deleteHelper(key, root); // Start deletion from root
    return bool.get(); // Return true if key was deleted
    }

    private TrieNode deleteHelper(String key, TrieNode n) {
        if (n == null) {
            // Base case: node not found
            return n;
        }
        // If at root, decide direction based on first bit
        if (n.str == "" && key.startsWith("0")) {
            n.left = deleteHelper(key, n.left);
        } else if (n.str == "" && key.startsWith("1")) {
            n.right = deleteHelper(key, n.right);
        } else if (key.equals(n.str) && n.isKey == true) {
            // Found the key to delete
            bool.set(true);
            if (n.left == null && n.right == null) {
                // Leaf node, remove it
                n.isKey = false;
                return null;
            }
            if (n.left != null && n.right != null) {
                // Node has two children, just mark as not a key
                n.isKey = false;
                return n;
            }
            if (n.left != null || n.right != null) {
                // Node has one child, merge with child
                if (n.left != null) {
                    n.left.str = n.str + n.left.str; // Merge strings
                    return n.left;
                }
                if (n.right != null) {
                    n.right.str = n.str + n.right.str; // Merge strings
                    return n.right;
                }
            }
        } else if (key.startsWith(n.str) && key.substring(n.str.length()).startsWith("0")) {
            // Node's string is a prefix of key, go left
            n.left = deleteHelper(key.substring(n.str.length()), n.left);
            // If left child is removed and current node isn't a key, merge with right
            if (n.left == null && !n.isKey) {
                n.right.str = n.str + n.right.str;
                return n.right;
            }
        } else if (key.startsWith(n.str) && key.substring(n.str.length()).startsWith("1")) {
            // Node's string is a prefix of key, go right
            n.right = deleteHelper(key.substring(n.str.length()), n.right);
            // If right child is removed and current node isn't a key, merge with left
            if (n.right == null && !n.isKey) {
                n.left.str = n.str + n.left.str;
                return n.left;
            }
        }
        return n; // Return current node
    }

    /**
     * Queries the trie for emptiness.
     *
     * @return {@code true} if and only if {@link #getSize()} == 0, {@code false}
     *         otherwise.
     */
    public boolean isEmpty() {
        // Trie is empty if size is zero
        if (getSize() == 0) {
            return true;
        }
        return false;
    }

    /**
     * Returns the number of keys in the tree.
     *
     * @return The number of keys in the tree.
     */
    public int getSize() {
    return getSizeHelper(root); // Start counting from root
    }

    private int getSizeHelper(TrieNode n) {
        if (n == null) {
            // Base case: null node
            return 0;
        }
        if (n.isKey) {
            // Count this node and recurse
            return getSizeHelper(n.left) + getSizeHelper(n.right) + 1;
        } else {
            // Not a key, just recurse
            return getSizeHelper(n.left) + getSizeHelper(n.right);
        }
    }

    /**
     * <p>
     * Performs an <i>inorder (symmetric) traversal</i> of the Binary Patricia Trie.
     * Remember from lecture that inorder
     * traversal in tries is NOT sorted traversal, unless all the stored keys have
     * the same length. This
     * is of course not required by your implementation, so you should make sure
     * that in your tests you
     * are not expecting this method to return keys in lexicographic order. We put
     * this method in the
     * interface because it helps us test your submission thoroughly and it helps
     * you debug your code!
     * </p>
     *
     * <p>
     * We <b>neither require nor test </b> whether the {@link Iterator} returned by
     * this method is fail-safe or fail-fast.
     * This means that you do <b>not</b> need to test for thrown
     * {@link java.util.ConcurrentModificationException}s and we do
     * <b>not</b> test your code for the possible occurrence of concurrent
     * modifications.
     * </p>
     *
     * <p>
     * We also assume that the {@link Iterator} is <em>immutable</em>, i,e we do
     * <b>not</b> test for the behavior
     * of {@link Iterator#remove()}. You can handle it any way you want for your own
     * application, yet <b>we</b> will
     * <b>not</b> test for it.
     * </p>
     *
     * @return An {@link Iterator} over the {@link String} keys stored in the trie,
     *         exposing the elements in <i>symmetric
     *         order</i>.
     */
    public Iterator<String> inorderTraversal() {
        ArrayList<String> list = new ArrayList<>(); // List to hold keys
        fillArray(list, "", root); // Fill list with keys using inorder traversal
        int originalSize = list.size(); // Used to detect concurrent modification
        var listIterator = list.iterator();

        // Return an iterator over the list
        Iterator<String> iterator = new Iterator<String>() {

            @Override
            public boolean hasNext() {
                return listIterator.hasNext();
            }

            @Override
            public String next() {
                // Check for concurrent modification
                if (originalSize != list.size()) {
                    throw new ConcurrentModificationException();
                }
                return listIterator.next();
            }

        };
        return iterator;
    }

    private void fillArray(ArrayList<String> l, String key, TrieNode n) {
        if (n == null) {
            // Base case: null node
            return;
        }
        String tempKey = key + n.str; // Build up the key as we traverse

        if (n.isKey) {
            // Inorder: left, self, right
            fillArray(l, tempKey, n.left);
            l.add(tempKey); // Add key if this node is a key
            fillArray(l, tempKey, n.right);
        } else {
            // Not a key, just traverse children
            fillArray(l, tempKey, n.left);
            fillArray(l, tempKey, n.right);
        }

        // // go left
        // fillArray(l, tempKey, n.left);
        // // self
        // if (n.isKey) {
        // l.add(key);
        // }
        // // go right
        // fillArray(l, tempKey, n.right);

    }

    /**
     * Finds the longest {@link String} stored in the Binary Patricia Trie.
     * 
     * @return
     *         <p>
     *         The longest {@link String} stored in this. If the trie is empty, the
     *         empty string &quot;&quot; should be
     *         returned. Careful: the empty string &quot;&quot;is <b>not</b> the
     *         same string as &quot; &quot;; the latter is a string
     *         consisting of a single <b>space character</b>! It is also <b>not the
     *         same as the</b> null <b>reference</b>!
     *         </p>
     *
     *         <p>
     *         Ties should be broken in terms of <b>value</b> of the bit string. For
     *         example, if our trie contained
     *         only the binary strings 01 and 11, <b>11</b> would be the longest
     *         string. If our trie contained
     *         only 001 and 010, <b>010</b> would be the longest string.
     *         </p>
     */
    public String getLongest() {
        ArrayList<String> list = new ArrayList<>(); // List to hold all keys
        fillArray(list, "", root); // Fill list with all keys
        String longest = "";
        for (String s : list) {
            // Find longest string
            if (s.length() > longest.length()) {
                longest = s;
            } else if (s.length() == longest.length()) {
                // If tie, pick lexicographically larger
                if (s.compareTo(longest) > 0) {
                    longest = s;
                }
            }
        }
        return longest;
    }

    /**
     * Makes sure that your trie doesn't have splitter nodes with a single child. In
     * a Patricia trie, those nodes should
     * be pruned.
     * 
     * @return {@code true} iff all nodes in the trie either denote stored strings
     *         or split into two subtrees, {@code false} otherwise.
     */
    public boolean isJunkFree() {
    // Trie is junk-free if empty or both subtrees are junk-free
    return isEmpty() || (isJunkFree(root.left) && isJunkFree(root.right));
    }

    private boolean isJunkFree(TrieNode n) {
        if (n == null) { // Null subtrees trivially junk-free
            return true;
        }
        if (!n.isKey) {
            // Non-key nodes must have two children and both must be junk-free
            return ((n.left != null) && (n.right != null) && isJunkFree(n.left) && isJunkFree(n.right));
        } else {
            // Key nodes can have any children, just check their junk-freeness
            return (isJunkFree(n.left) && isJunkFree(n.right));
        }
    }
}
