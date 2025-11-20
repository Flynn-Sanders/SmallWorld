public class MyHashSet<E>{
    private Object[] hashArray;
    private int size;
    
    public MyHashSet(){
      size = 0;
      hashArray = new Object[1009];
    }
    public boolean contains(Object o){
      int idx = index(o);
        return hashArray[idx] != null && o.equals(hashArray[idx]);
    }
    private int index(Object o) {
      return Math.abs(o.hashCode()) % hashArray.length;
    }
    public void clear(){
      size = 0;
      hashArray = new Object[1009];
    }
    public boolean add(E e){
    if (contains(e)) return false;
    int idx = index(e);
    hashArray[idx] = e;
    size++;
    return true;
  }

  public boolean remove(Object o){
      if (!contains(o)) return false;
      int idx = index(o);
      hashArray[idx] = null;
      size--;
      return true;
  }
    public int size(){ return size; }
    
    @SuppressWarnings("unchecked")
  public DLList<E> toLinkedList(){
      DLList<E> dLList = new DLList<E>();
      for(Object o : hashArray){
        if(o!=null){
          dLList.add((E)o);
        }
      }
      return dLList;
    }
    
    
    
    
    
  }