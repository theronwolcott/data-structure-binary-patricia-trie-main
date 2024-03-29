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
    private static class TrieNode {
        private TrieNode left, right;
        private String str;
        private boolean isKey;

        // Default constructor for your inner nodes.
        TrieNode() {
            this("", false);
        }

        // Non-default constructor.
        TrieNode(String str, boolean isKey) {
            left = right = null;
            this.str = str;
            this.isKey = isKey;
        }
    }

    private TrieNode root;
    private AtomicBoolean bool;

    /**
     * Simple constructor that will initialize the internals of {@code this}.
     */
    public BinaryPatriciaTrie() {
        root = new TrieNode("", false);
        bool = new AtomicBoolean();
    }

    /**
     * Searches the trie for a given key.
     *
     * @param key The input {@link String} key.
     * @return {@code true} if and only if key is in the trie, {@code false}
     *         otherwise.
     */
    public boolean search(String key) {
        bool.set(false);
        searchHelper(key, root);
        return bool.get();
    }

    private void searchHelper(String key, TrieNode node) {
        bool.set(false);
        if (node == null) {
            return;
        }
        if (node.str == "" && key.startsWith("0")) { // Root, go left
            searchHelper(key, node.left);
        } else if (node.str == "" && key.startsWith("1")) { // Root, go right
            searchHelper(key, node.right);
        } else if (key.equals(node.str) && node.isKey == true) { // Strings are equal, we found it
            bool.set(true);
            return;
        } else if (key.startsWith(node.str) && key.substring(node.str.length()).startsWith("0")) {
            // Node is a prefix of key, go left
            searchHelper(key.substring(node.str.length()), node.left);
        } else if (key.startsWith(node.str) && key.substring(node.str.length()).startsWith("1")) {
            // Node is a prefix of key, go left
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
        bool.set(false);
        root = insertHelper(key, root);
        return bool.get();
    }

    private TrieNode insertHelper(String key, TrieNode n) {
        if (n == null) {
            bool.set(true);
            return new TrieNode(key, true);
        }

        if (key.equals(n.str) && n.isKey) { // if key is equal to str
            return n;
        } else if (key.equals(n.str) && !n.isKey) {
            n.isKey = true;
            bool.set(true);
            return n;
        }
        if (key.startsWith(n.str) || n.str.equals("")) { // str is a prefix of key, chop off equal bit and recurse
            if (key.substring(n.str.length()).startsWith("0")) {
                n.left = insertHelper(key.substring(n.str.length()), n.left);
            } else {
                n.right = insertHelper(key.substring(n.str.length()), n.right);
            }
        } else if (n.str.startsWith(key)) { // key is a prefix of str, need a new node with equal part as child of n,
                                            // existing gets updated
            TrieNode newNode = new TrieNode(key, true); // the part that's equal
            if (n.str.substring(key.length()).startsWith("0")) { // if we need to go left
                newNode.left = n;

            } else { // go right
                newNode.right = n;
            }
            n.str = n.str.substring(key.length());
            bool.set(true);
            return newNode;
        } else { // common prefix
            int index = diffIndex(key, n.str);
            String prefix = key.substring(0, index);
            TrieNode newNode = new TrieNode(prefix, false);
            n.str = n.str.substring(index);
            if (key.substring(index).startsWith("0")) {
                newNode.left = new TrieNode(key.substring(index), true);
                newNode.right = n;
            } else {
                newNode.right = new TrieNode(key.substring(index), true);
                newNode.left = n;
            }
            bool.set(true);
            return newNode;
        }
        return n;
    }

    private int diffIndex(String key, String str) {
        for (int i = 0; i < key.length(); i++) {
            if (key.charAt(i) != str.charAt(i)) {
                return i;
            }
        }
        return -1; // never actually gets here
    }

    /**
     * Deletes key from the trie.
     *
     * @param key The {@link String} key to be deleted.
     * @return {@code true} if and only if key was contained by the trie before we
     *         attempted deletion, {@code false} otherwise.
     */
    public boolean delete(String key) {
        bool.set(false);
        root = deleteHelper(key, root);
        return bool.get();
    }

    private TrieNode deleteHelper(String key, TrieNode n) {
        if (n == null) {
            return n;
        }
        if (n.str == "" && key.startsWith("0")) { // Root, go left
            n.left = deleteHelper(key, n.left);
        } else if (n.str == "" && key.startsWith("1")) { // Root, go right
            n.right = deleteHelper(key, n.right);
        } else if (key.equals(n.str) && n.isKey == true) { // Strings are equal, we found it
            bool.set(true);
            if (n.left == null && n.right == null) { // if the node has no children, get rid of it
                n.isKey = false;
                return null;
            }
            if (n.left != null && n.right != null) { // if node has two children, set key false
                n.isKey = false;
                return n;
            }
            if (n.left != null || n.right != null) { // node has one child, merge
                if (n.left != null) { // merge with left child
                    n.left.str = n.str + n.left.str;
                    return n.left;
                }
                if (n.right != null) { // merge with right child
                    n.right.str = n.str + n.right.str;
                    return n.right;
                }
            }
        } else if (key.startsWith(n.str) && key.substring(n.str.length()).startsWith("0")) {
            // Node is a prefix of key, go left
            n.left = deleteHelper(key.substring(n.str.length()), n.left);
            if (n.left == null && !n.isKey) {
                n.right.str = n.str + n.right.str;
                return n.right;
            }
        } else if (key.startsWith(n.str) && key.substring(n.str.length()).startsWith("1")) {
            // Node is a prefix of key, go right
            n.right = deleteHelper(key.substring(n.str.length()), n.right);
            if (n.right == null && !n.isKey) {
                n.left.str = n.str + n.left.str;
                return n.left;
            }
        }
        return n;
    }

    /**
     * Queries the trie for emptiness.
     *
     * @return {@code true} if and only if {@link #getSize()} == 0, {@code false}
     *         otherwise.
     */
    public boolean isEmpty() {
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
        return getSizeHelper(root);
    }

    private int getSizeHelper(TrieNode n) {
        if (n == null) {
            return 0;
        }
        if (n.isKey) {
            return getSizeHelper(n.left) + getSizeHelper(n.right) + 1;
        } else {
            return getSizeHelper(n.left) + getSizeHelper(n.right) + 0;
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
        ArrayList<String> list = new ArrayList<>();
        fillArray(list, "", root);
        int originalSize = list.size();
        var listIterator = list.iterator();

        Iterator<String> iterator = new Iterator<String>() {

            @Override
            public boolean hasNext() {
                return listIterator.hasNext();
            }

            @Override
            public String next() {
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
            return;
        }
        String tempKey = key + n.str;

        if (n.isKey) {
            fillArray(l, tempKey, n.left);
            l.add(tempKey);
            fillArray(l, tempKey, n.right);
        } else {
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
        ArrayList<String> list = new ArrayList<>();
        fillArray(list, "", root);
        String longest = "";
        for (String s : list) {
            if (s.length() > longest.length()) {
                longest = s;
            } else if (s.length() == longest.length()) {
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
        return isEmpty() || (isJunkFree(root.left) && isJunkFree(root.right));
    }

    private boolean isJunkFree(TrieNode n) {
        if (n == null) { // Null subtrees trivially junk-free
            return true;
        }
        if (!n.isKey) { // Non-key nodes need to be strict splitter nodes
            return ((n.left != null) && (n.right != null) && isJunkFree(n.left) && isJunkFree(n.right));
        } else {
            return (isJunkFree(n.left) && isJunkFree(n.right)); // But key-containing nodes need not.
        }
    }
}
