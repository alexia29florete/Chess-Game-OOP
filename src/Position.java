public class Position implements Comparable<Position>
{
    private char x;
    private int y;
    public Position(char x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public char getX()
    {
        return x;
    }
    public int getY()
    {
        return y;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        Position obj = (Position) o;
        return this.x == obj.x && this.y == obj.y;
    }

    public int compareTo(Position p)
    {
        if(p.y != this.y)
        {
            return Integer.compare(this.y, p.y);
        }
        return Character.compare(this.x, p.x);
    }

    public Position(String position)
    {
        this.x = Character.toUpperCase(position.charAt(0));
        this.y = Character.getNumericValue(position.charAt(1));
    }

    public String toString()
    {
        return ""+ x + y;
    }
}
