public class MyHashTable<K,V>{
    private DLList<V>[] hashArray;
    private MyHashSet<K> keySet;

    @SuppressWarnings("unchecked")
    public MyHashTable(){
        hashArray = new DLList[10000];
        keySet = new MyHashSet<>();
    }

    public void put(K key, V value){
        if(hashArray[key.hashCode()]==null){
            hashArray[key.hashCode()] = new DLList<V>();
        }
        if(value!=null){
            hashArray[key.hashCode()].add(value);
        }
        keySet.add(key);
        
        
        
    }
    
    public DLList<V> get(Object o){
        return hashArray[o.hashCode()];
    }

    public void remove(Object o){
        hashArray[o.hashCode()].remove(o);
        if(hashArray[o.hashCode()].isEmpty()){
            hashArray[o.hashCode()]=null;
        }
        keySet.remove(o);
    }

    public MyHashSet<K> keySet() { return keySet; }

    public String toString() {
        String text = "";
        DLList<K> list = keySet.toLinkedList();
        for (int i = 0; i < list.size(); i++) {
            K key = list.get(i); 
            int index = key.hashCode();
            text += key + " - " + hashArray[index] + "\n";
        }   
        return text;
    }
}