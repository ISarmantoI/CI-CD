package org.example;

import java.util.ArrayList;
import java.util.List;

public class TodoList {

    public static class Task {
        private final String title;
        private boolean done;

        public Task(String title) {
            this.title = title;
            this.done = false;
        }

        public String getTitle() {
            return title;
        }

        public boolean isDone() {
            return done;
        }

        public void setDone(boolean done) {
            this.done = done;
        }

        @Override
        public String toString() {
            return (done ? "[X] " : "[ ] ") + title;
        }
    }

    private final List<Task> items = new ArrayList<>();

    public void add(String item) {
        if (item != null) {
            item = item.trim();
            if (!item.isEmpty()) {
                items.add(new Task(item));
            }
        }
    }

    public boolean remove(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
            return true;
        }
        return false;
    }

    public List<String> getAll() {
        List<String> titles = new ArrayList<>();
        for (Task t : items) {
            titles.add(t.getTitle());
        }
        return titles;
    }

    public List<String> getAllFormatted() {
        List<String> formatted = new ArrayList<>();
        for (Task t : items) {
            formatted.add(t.toString());
        }
        return formatted;
    }

    public int size() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }

    public boolean markDone(int index) {
        if (index >= 0 && index < items.size()) {
            items.get(index).setDone(true);
            return true;
        }
        return false;
    }

    public boolean isDone(int index) {
        if (index >= 0 && index < items.size()) {
            return items.get(index).isDone();
        }
        return false;
    }

    public List<String> search(String query) {
        List<String> matches = new ArrayList<>();
        if (query == null || query.isBlank()) {
            return matches;
        }
        String lower = query.toLowerCase();
        for (Task t : items) {
            if (t.getTitle().toLowerCase().contains(lower)) {
                matches.add(t.toString());
            }
        }
        return matches;
    }
}
