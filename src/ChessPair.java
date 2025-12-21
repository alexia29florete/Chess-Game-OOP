public class ChessPair<K extends Comparable<K>, V> implements Comparable<ChessPair<K, V>>
{
    private K key;
    private V value;
    public ChessPair(K key, V value)
    {
        this.key = key;
        this.value = value;
    }
    public K getKey()
    {
        return key;
    }
    public V getValue()
    {
        return value;
    }

    public int compareTo(ChessPair<K, V> obj)
    {
        return this.key.compareTo(obj.key);
    }

    public String toString()
    {
        return key.toString() + " " + value.toString();
    }
}
