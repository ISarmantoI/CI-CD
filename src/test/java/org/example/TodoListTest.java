package org.example;

import org.example.TodoList;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TodoListTest {

    @Test
    void addAndList() {
        TodoList t = new TodoList();
        t.add(" task1 ");
        assertEquals(1, t.size());
        assertEquals("task1", t.getAll().getFirst());
    }

    @Test
    void remove() {
        TodoList t = new TodoList();
        t.add("a");
        t.add("b");
        assertTrue(t.remove(0));
        assertEquals(1, t.size());
        assertFalse(t.remove(10));
    }

    @Test
    void addEmptyIgnored() {
        TodoList t = new TodoList();
        t.add(" ");
        assertEquals(0, t.size());
    }

    @Test
    void clear() {
        TodoList t = new TodoList();
        t.add("task 1");
        t.add("task 2");
        assertEquals(2, t.size());
        t.clear();
        assertEquals(0, t.size());
        assertTrue(t.getAll().isEmpty());
    }

    @Test
    void markDone() {
        TodoList t = new TodoList();
        t.add("task 1");
        assertFalse(t.isDone(0));
        assertTrue(t.markDone(0));
        assertTrue(t.isDone(0));
        assertFalse(t.markDone(99));
        assertEquals("[X] task 1", t.getAllFormatted().getFirst());
    }

    @Test
    void search() {
        TodoList t = new TodoList();
        t.add("Buy milk");
        t.add("Clean room");
        t.add("Buy bread");

        List<String> found = t.search("buy");
        assertEquals(2, found.size());
        assertTrue(found.get(0).contains("Buy milk"));
        assertTrue(found.get(1).contains("Buy bread"));

        List<String> notFound = t.search("nonexistent");
        assertEquals(0, notFound.size());
    }
}
