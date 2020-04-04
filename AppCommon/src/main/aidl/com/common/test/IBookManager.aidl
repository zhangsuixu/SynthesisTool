// IBookManager.aidl
package com.common.test;

import com.common.test.Book;

// Declare any non-default types here with import statements
interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
}
