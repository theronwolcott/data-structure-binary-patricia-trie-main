package bpt;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Random;

/**
 * A jUnit test suite for {@link BinaryPatriciaTrie}.
 *
 * @author --- YOUR NAME HERE! ----.
 */
public class StudentTests {

    @Test
    public void testEmptyTrie() {
        BinaryPatriciaTrie trie = new BinaryPatriciaTrie();

        assertTrue("Trie should be empty", trie.isEmpty());
        assertEquals("Trie size should be 0", 0, trie.getSize());

        assertFalse("No string inserted so search should fail", trie.search("0101"));

    }

    @Test
    public void testInsertSearchBasic() {
        BinaryPatriciaTrie trie = new BinaryPatriciaTrie();

        assertTrue(trie.insert("0"));
        assertTrue(trie.insert("1"));

        assertFalse(trie.insert("0"));
        assertFalse(trie.insert("1"));

        assertTrue(trie.insert("00"));
        assertTrue(trie.insert("00001"));
        assertTrue(trie.insert("11100"));
        assertTrue(trie.insert("10"));

        assertTrue(trie.insert("111"));
        assertTrue(trie.insert("0000"));

        assertTrue(trie.getSize() == 8);

        assertTrue(trie.search("0"));
        assertTrue(trie.search("1"));
        assertTrue(trie.search("00"));
        assertTrue(trie.search("00001"));
        assertTrue(trie.search("11100"));
        assertTrue(trie.search("10"));
        assertTrue(trie.search("111"));
        assertTrue(trie.search("0000"));

        assertFalse(trie.insert("00"));
        assertFalse(trie.insert("00001"));
        assertFalse(trie.insert("11100"));
        assertFalse(trie.insert("10"));
        assertFalse(trie.insert("111"));
        assertFalse(trie.insert("0000"));

    }

    @Test
    public void testInsertSearchBasic2() {
        BinaryPatriciaTrie trie = new BinaryPatriciaTrie();

        assertTrue(trie.insert("000"));
        assertTrue(trie.insert("0000"));
        assertTrue(trie.insert("0001"));
        assertTrue(trie.insert("001"));

        assertTrue(trie.search("000"));
        assertTrue(trie.search("0000"));
        assertTrue(trie.search("0001"));
        assertTrue(trie.search("001"));
    }

    @Test
    public void testInsertSearchBasic3() {
        BinaryPatriciaTrie trie = new BinaryPatriciaTrie();

        assertTrue(trie.insert("000"));
        assertTrue(trie.insert("0000"));

        assertTrue(trie.search("000"));
        assertTrue(trie.search("0000"));

    }

    @Test
    public void testFewInsertionsWithSearch() {
        BinaryPatriciaTrie trie = new BinaryPatriciaTrie();

        assertTrue("String should be inserted successfully", trie.insert("00000"));
        assertTrue("String should be inserted successfully", trie.insert("00011"));
        assertFalse("Search should fail as string does not exist", trie.search("000"));

    }

    // testing isEmpty function
    @Test
    public void testFewInsertionsWithDeletion() {
        BinaryPatriciaTrie trie = new BinaryPatriciaTrie();

        trie.insert("000");
        trie.insert("001");
        trie.insert("011");
        trie.insert("1001");
        trie.insert("1");

        assertFalse("After inserting five strings, the trie should not be considered empty!", trie.isEmpty());
        assertEquals("After inserting five strings, the trie should report five strings stored.", 5, trie.getSize());

        trie.delete("0"); // Failed deletion; should affect exactly nothing.
        assertEquals("After inserting five strings and requesting the deletion of one not in the trie, the trie " +
                "should report five strings stored.", 5, trie.getSize());
        assertTrue(
                "After inserting five strings and requesting the deletion of one not in the trie, the trie had some junk in it!",
                trie.isJunkFree());

        trie.delete("011"); // Successful deletion
        assertEquals("After inserting five strings and deleting one of them, the trie should report 4 strings.", 4,
                trie.getSize());
        assertTrue("After inserting five strings and deleting one of them, the trie had some junk in it!",
                trie.isJunkFree());
    }

    BinaryPatriciaTrie trie;

    @Test
    public void constructorTest1() {
        trie = new BinaryPatriciaTrie();
        assertTrue(trie.isEmpty());
        assertEquals(0, trie.getSize());
        assertTrue(trie.isJunkFree());
    }

    @Test
    public void insertTest1() {
        // test CurrentIsPrefixOfKey
        constructorTest1();
        assertTrue(trie.insert("000"));
        assertTrue(trie.insert("0000"));
        assertTrue(trie.insert("0001"));
        assertEquals(3, trie.getSize());
        assertTrue(trie.isJunkFree());
    }

    @Test
    public void insertTest2() {
        // test KeyIsPrefixOfCurrent
        insertTest1();
        assertTrue(trie.insert("00"));
        assertEquals(4, trie.getSize());
        assertTrue(trie.isJunkFree());
    }

    @Test
    public void insertTest3() {
        // test CommonPrefix
        insertTest1();
        assertTrue(trie.insert("001"));
        assertEquals(4, trie.getSize());
        assertTrue(trie.isJunkFree());
    }

    @Test
    public void insertTest4() {
        // test Equal
        insertTest3();
        assertTrue(trie.insert("00"));
        assertEquals(5, trie.getSize());
        assertTrue(trie.isJunkFree());

        // duplicate
        assertFalse(trie.insert("00"));
        assertEquals(5, trie.getSize());
        assertTrue(trie.isJunkFree());
    }

    @Test
    public void insertTest5() {
        constructorTest1();
        assertTrue(trie.insert("1001001111"));
        assertEquals(1, trie.getSize());
        assertTrue(trie.isJunkFree());
        assertTrue(trie.insert("0000"));
        assertEquals(2, trie.getSize());
        assertTrue(trie.isJunkFree());
        assertTrue(trie.insert("111100"));
        assertEquals(3, trie.getSize());
        assertTrue(trie.isJunkFree());
        assertTrue(trie.insert("000100"));
        assertEquals(4, trie.getSize());
        assertTrue(trie.isJunkFree());
        assertTrue(trie.insert("110110"));
        assertEquals(5, trie.getSize());
        assertTrue(trie.isJunkFree());
    }

    @Test
    public void insertTest6() {
        constructorTest1();
        assertTrue(trie.insert("0000001"));
        assertEquals(1, trie.getSize());
        assertTrue(trie.isJunkFree());
        assertTrue(trie.insert("11000011"));
        assertEquals(2, trie.getSize());
        assertTrue(trie.isJunkFree());
        assertTrue(trie.insert("1100100"));
        assertEquals(3, trie.getSize());
        assertTrue(trie.insert("1110"));
        assertEquals(4, trie.getSize());
        assertTrue(trie.isJunkFree());
    }

    @Test
    public void deleteTest1() {
        // no children
        insertTest1();
        assertTrue(trie.delete("0000"));
        assertEquals(2, trie.getSize());
        assertTrue(trie.isJunkFree());

        // miss
        assertFalse(trie.delete("0000"));
        assertEquals(2, trie.getSize());
        assertTrue(trie.isJunkFree());
    }

    @Test
    public void deleteTest2() {
        // one child - left
        insertTest2();
        assertTrue(trie.delete("00"));
        assertEquals(3, trie.getSize());
        assertTrue(trie.isJunkFree());
    }

    @Test
    public void deleteTest3() {
        // one child - right
        deleteTest1();
        assertTrue(trie.delete("0001"));
        assertEquals(1, trie.getSize());
        assertTrue(trie.isJunkFree());
    }

    @Test
    public void deleteTest4() {
        // two children
        insertTest1();
        assertTrue(trie.delete("000"));
        assertEquals(2, trie.getSize());
        assertTrue(trie.isJunkFree());
    }

    @Test
    public void deleteTest5() {
        // found, but non-key
        insertTest3();
        assertFalse(trie.delete("00"));
        assertEquals(4, trie.getSize());
        assertTrue(trie.isJunkFree());
    }

    private String getRandomKey(int maxLength) {
        var random = new Random();
        String s = "";
        int length = random.nextInt(maxLength) + 1;
        for (int j = 0; j < length; j++) {
            s += (random.nextInt(2) < 1) ? "0" : "1";
        }
        return s;
    }

    @Test
    public void advancedTest1() {
        constructorTest1();
        int testSize = 1000;
        var set = new HashSet<String>();
        for (int i = 0; i < testSize; i++) {
            String s = getRandomKey(testSize / 100);
            if (set.add(s)) {
                // new keys
                assertTrue(trie.insert(s));
                assertTrue(trie.search(s));
            } else {
                // dupe keys
                assertFalse(trie.insert(s));
                assertTrue(trie.search(s));
            }
            assertEquals(set.size(), trie.getSize());
            assertTrue(trie.isJunkFree());

            // serach misses
            String miss = getRandomKey(testSize / 100);
            if (set.contains(miss)) {
                assertTrue(trie.search(miss));
            } else {
                assertFalse(trie.search(miss));
            }
        }
        for (var s : set) {
            assertTrue(trie.search(s));
        }

        var inOrderIterator = trie.inorderTraversal();
        var inOrderSet = new HashSet<String>();
        inOrderIterator.forEachRemaining(inOrderSet::add);
        // assertEquals(set, inOrderSet);

        // longest
        var trieLongest = trie.getLongest();
        var setLongest = set.stream()
                .reduce("", (winner, element) -> {
                    if (element.length() > winner.length())
                        return element;
                    if (element.length() == winner.length() && element.compareTo(winner) > 0)
                        return element;
                    return winner;
                });
        assertEquals(trieLongest, setLongest);

        // delete
        var size = set.size();
        for (var s : set) {
            assertTrue(trie.delete(s));
            assertEquals(--size, trie.getSize());
            assertTrue(trie.isJunkFree());
        }
        assertTrue(trie.isEmpty());
        assertEquals(0, trie.getSize());
        assertEquals("", trie.getLongest());

    }
}