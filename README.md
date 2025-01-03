# Binary Patricia Trie

## Overview
This project implements a **Binary Patricia Trie** (also known as a Binary Prefix Tree), a highly efficient data structure for storing and managing binary strings. The primary goal of the project is to implement core operations such as insertion, deletion, search, and in-order traversal, and to maintain an efficient representation of binary strings in a trie-like structure.

## Features
- **Insertion**: Efficiently inserts binary strings into the trie.
- **Deletion**: Supports the removal of binary strings from the trie, ensuring the integrity of the data structure.
- **Lookup/Search**: Allows for quick search and retrieval of binary strings.
- **In-order Traversal**: Traverses the trie in lexicographical order, listing all stored keys.
- **Accessor Methods**: Provides methods to get the longest string in the trie, the size of the trie, and checks for an empty trie.
- **Efficient Memory Usage**: Uses a space-efficient structure, storing only necessary data at each node.

## Key Concepts

### Patricia Trie
A Patricia Trie (Practical Algorithm to Retrieve Information Coded in Alphanumeric) is a compressed version of a regular trie. Unlike a standard trie, where each node typically represents a single character of a key, a Patricia Trie uses paths (representing sequences of characters) and splits nodes where necessary, offering space and time efficiency.

### Binary Patricia Trie
In this implementation, the trie stores **binary strings** (strings consisting of only `0`s and `1`s). It follows the same principles as a regular Patricia Trie but tailored specifically for binary data.

### Operations

- **Insert**: Adds a binary string to the trie, creating or adjusting nodes as necessary to maintain the trie structure.
- **Delete**: Removes a binary string from the trie and re-adjusts the structure to maintain the correct state.
- **Search**: Finds if a given binary string exists in the trie.
- **In-order Traversal**: Lists the binary strings stored in the trie in lexicographical (sorted) order.
- **Longest String**: Retrieves the longest string present in the trie.
- **Size**: Returns the number of keys (strings) currently stored in the trie.
- **Empty Check**: Returns `true` if the trie contains no strings.

## Data Structure Design

The trie is implemented using **nodes** that contain:
- A **bit flag** indicating whether the node is a leaf or a splitter (internal) node.
- A **substring** of the key if the node is a splitter, or the full key if it is a leaf.
- **Child nodes**, representing further binary strings diverging from the current node.

### Node Representation
The binary strings are represented with splits at common prefixes, ensuring a compact representation without redundancy. Each internal node (splitter node) handles a common prefix, and leaf nodes store actual strings.
