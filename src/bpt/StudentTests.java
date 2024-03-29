package bpt;

import org.junit.Test;

import static org.junit.Assert.*;

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
}