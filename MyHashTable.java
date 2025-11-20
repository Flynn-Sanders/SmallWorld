public class MyHashTable<K,V> {
    private DLList<V>[] hashArray;
    private MyHashSet<K> keySet;

    @SuppressWarnings("unchecked")
    public MyHashTable() {
        hashArray = new DLList[1009]; 
        keySet = new MyHashSet<>();
    }

    private int index(K key) {
        return Math.abs(key.hashCode()) % hashArray.length;
    }

    public void put(K key, V value) {
        int idx = index(key);
        if (hashArray[idx] == null) hashArray[idx] = new DLList<>();
        hashArray[idx].add(value);
        keySet.add(key);
    }

    public DLList<V> get(Object key) {
        int idx = Math.abs(key.hashCode()) % hashArray.length;
        return hashArray[idx];
    }

    public void remove(Object key) {
        int idx = Math.abs(key.hashCode()) % hashArray.length;
        if (hashArray[idx] != null) hashArray[idx].remove(key);
        if (hashArray[idx] == null || hashArray[idx].isEmpty()) hashArray[idx] = null;
        keySet.remove(key);
    }

    public MyHashSet<K> keySet() { return keySet; }

    @Override
    public String toString() {
        String text = "";
        DLList<K> list = keySet.toLinkedList();
        for (int i = 0; i < list.size(); i++) {
            K key = list.get(i);
            int idx = index(key);
            text += key + " - " + hashArray[idx] + "\n";
        }
        return text;
    }
}
