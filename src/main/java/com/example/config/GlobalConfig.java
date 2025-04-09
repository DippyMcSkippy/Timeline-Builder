package com.example.config;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class GlobalConfig {
    public static LinkedList<String[]> universeLinkedList = new LinkedList<>();
    public static Map<String, LinkedList<String[]>> eventLinkedLists = new HashMap<>();

    // Method to add universe to the linked list based on priority
    public static void addUniverse(String name, int priority) {
        String[] newUniverse = {name, String.valueOf(priority)};
        int index = 0;
        for (String[] universe : universeLinkedList) {
            if (Integer.parseInt(universe[1]) > priority) {
                break;
            }
            index++;
        }
        universeLinkedList.add(index, newUniverse);
        // Create a new LinkedList for events for this universe
        String eventListName = name + "eventLinkedList";
        eventLinkedLists.put(eventListName, new LinkedList<>());
    }

    // Method to get the event LinkedList for a specific universe
    public static LinkedList<String[]> getEventLinkedList(String universeName) {
        return eventLinkedLists.get(universeName + "eventLinkedList");
    }
}