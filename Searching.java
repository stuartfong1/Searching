import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.ArrayList; 
import java.util.LinkedList; 
import java.util.Queue; 
import java.util.Stack; 
import java.util.PriorityQueue; 
import java.util.Comparator; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Searching extends PApplet {








final int ERASE_BUTTON_X = 100;
final int WALL_BUTTON_X = 225;
final int START_BUTTON_X = 350;
final int END_BUTTON_X = 475;
final int SELECTED_TEXT_X = 600;
final int BUTTON_Y = 30;
final int SEARCHING_Y = 530;
final int BFS_BUTTON_X = 100;
final int DFS_BUTTON_X = 225;
final int DIJKSTRA_BUTTON_X = 350;
final int ASTAR_BUTTON_X = 475;
final int BUTTON_WIDTH = 100;
final int BUTTON_HEIGHT = 30;

final int NUM_ROWS = 20;
final int NUM_COLS = 30;

int[] start = {-1, -1};
int[] end = {-1, -1};
ArrayList<Node> path;

int[][] grid = new int[NUM_ROWS][NUM_COLS];

int selected = 0;

class Node
{
    private Node parent;
    private int x;
    private int y;
    private double f;
    private double g;
    private double h;

    // BFS / DFS Node
    public Node(Node newParent, int newX, int newY)
    {
        parent = newParent;
        x = newX;
        y = newY;
    }
    
    // Dijkstra Node
    public Node(Node newParent, int newX, int newY, double newG)
    {
        parent = newParent;
        x = newX;
        y = newY;
        g = newG;
    }
    
    // A* node
    public Node(Node newParent, int newX, int newY, double newG, int endX, int endY)
    {
        parent = newParent;
        x = newX;
        y = newY;
        g = newG;
        h = Math.sqrt(Math.pow(endY - y, 2) + Math.pow(endX - x, 2));
        f = g + h;
    }
    public Node getParent()
    {
        return parent;
    }
    public void setParent(Node newParent)
    {
        parent = newParent;
    }
    public int getX()
    {
        return x;
    }
    public int getY()
    {
        return y;
    }
    public double getF()
    {
        return f;
    }
    public double getG()
    {
        return g;
    }
    public void setG(double newG)
    {
        g = newG;
    }
    public void setF()
    {
        f = g + h;
    }
}

public ArrayList<Node> BFS(int[] start, int[] end)
{
    Queue<Node> open_list = new LinkedList<Node>();
    ArrayList<Node> closed_list = new ArrayList<Node>();
    ArrayList<Node> path = new ArrayList<Node>();
    open_list.add(new Node(null, start[1], start[0]));
    while (open_list.size() > 0)
    {
        Node currentNode = open_list.remove();
        closed_list.add(currentNode);

        if (currentNode.getX() == end[1] && currentNode.getY() == end[0])
        {
            currentNode = currentNode.getParent();
            while (currentNode.parent != null)
            {
                path.add(currentNode);
                currentNode = currentNode.getParent();
            }
            return path;
        }

        int[][] coords = {{-1, -1}, {0, -1}, {1, -1}, {-1, 0}, {1, 0}, {-1, 1}, {0, 1}, {1, 1}};
        for (int i = 0; i < coords.length; ++i)
        {
            int childX = currentNode.getX()+coords[i][0];
            int childY = currentNode.getY()+coords[i][1];
            if (childX < 0 || childX >= NUM_COLS || childY < 0 || childY >= NUM_ROWS || grid[childY][childX] == 1)
            {
                continue;
            }
            boolean exists = false;
            for (Node j: open_list)
            {
                if (j.getX() == childX && j.getY() == childY)
                {
                    exists = true;
                }
            }
            for (Node j: closed_list)
            {
                if (j.getX() == childX && j.getY() == childY)
                {
                    exists = true;
                }
            }
            if (exists)
            {
                continue;
            }
            Node child = new Node(currentNode, childX, childY);
            open_list.add(child);
        }
    }
    System.out.println("End not found.");
    return null;
}

public ArrayList<Node> DFS(int[] start, int[] end)
{
    Stack<Node> open_list = new Stack<Node>();
    ArrayList<Node> closed_list = new ArrayList<Node>();
    ArrayList<Node> path = new ArrayList<Node>();
    open_list.push(new Node(null, start[1], start[0]));
    while (open_list.size() > 0)
    {
        Node currentNode = open_list.pop();
        closed_list.add(currentNode);

        if (currentNode.getX() == end[1] && currentNode.getY() == end[0])
        {
            currentNode = currentNode.getParent();
            while (currentNode.parent != null)
            {
                path.add(currentNode);
                currentNode = currentNode.getParent();
            }
            return path;
        }

        int[][] coords = {{-1, -1}, {0, -1}, {1, -1}, {-1, 0}, {1, 0}, {-1, 1}, {0, 1}, {1, 1}};
        for (int i = 0; i < coords.length; ++i)
        {
            int childX = currentNode.getX()+coords[i][0];
            int childY = currentNode.getY()+coords[i][1];
            if (childX < 0 || childX >= NUM_COLS || childY < 0 || childY >= NUM_ROWS || grid[childY][childX] == 1)
            {
                continue;
            }
            boolean exists = false;
            for (Node j: open_list)
            {
                if (j.getX() == childX && j.getY() == childY)
                {
                    exists = true;
                }
            }
            for (Node j: closed_list)
            {
                if (j.getX() == childX && j.getY() == childY)
                {
                    exists = true;
                }
            }
            if (exists)
            {
                continue;
            }
            Node child = new Node(currentNode, childX, childY);
            open_list.push(child);
        }
    }
    System.out.println("End not found.");
    return null;
}

public ArrayList<Node> Dijkstra(int[] start, int[] end)
{
    Queue<Node> open_list = new LinkedList<Node>();
    ArrayList<Node> closed_list = new ArrayList<Node>();
    ArrayList<Node> path = new ArrayList<Node>();
    open_list.add(new Node(null, start[1], start[0], 0));
    while (open_list.size() > 0)
    {
        Node currentNode = open_list.remove();
        closed_list.add(currentNode);

        if (currentNode.getX() == end[1] && currentNode.getY() == end[0])
        {
            currentNode = currentNode.getParent();
            while (currentNode.parent != null)
            {
                path.add(currentNode);
                currentNode = currentNode.getParent();
            }
            return path;
        }

        int[][] coords = {{-1, -1}, {0, -1}, {1, -1}, {-1, 0}, {1, 0}, {-1, 1}, {0, 1}, {1, 1}};
        for (int i = 0; i < coords.length; ++i)
        {
            int childX = currentNode.getX()+coords[i][0];
            int childY = currentNode.getY()+coords[i][1];
            if (childX < 0 || childX >= NUM_COLS || childY < 0 || childY >= NUM_ROWS || grid[childY][childX] == 1)
            {
                continue;
            }
            boolean exists = false;
            for (Node j: open_list)
            {
                if (j.getX() == childX && j.getY() == childY)
                {
                    exists = true;
                    if(currentNode.getG() + Math.sqrt(abs(coords[i][0]) + abs(coords[i][1])) < j.getG())
                    {
                        j.setParent(currentNode);
                        j.setG(currentNode.getG() + Math.sqrt(abs(coords[i][0]) + abs(coords[i][1])));
                    }
                }
            }
            for (Node j: closed_list)
            {
                if (j.getX() == childX && j.getY() == childY)
                {
                    exists = true;
                    if(currentNode.getG() + Math.sqrt(abs(coords[i][0]) + abs(coords[i][1])) < j.getG())
                    {
                        j.setParent(currentNode);
                        j.setG(currentNode.getG() + Math.sqrt(abs(coords[i][0]) + abs(coords[i][1])));
                    }
                }
            }
            if (exists)
            {
                continue;
            }
            Node child = new Node(currentNode, childX, childY, currentNode.getG() + Math.sqrt(abs(coords[i][0])+ abs(coords[i][1])));
            open_list.add(child);
        }
    }
    System.out.println("End not found.");
    return null;
}

public ArrayList<Node> AStar(int[] start, int[] end)
{
    PriorityQueue<Node> open_list = new PriorityQueue<Node>(new Comparator<Node>(){
        public int compare(Node a, Node b){
            return Double.compare(a.getF(), b.getF());
        }
    });
    ArrayList<Node> closed_list = new ArrayList<Node>();
    ArrayList<Node> path = new ArrayList<Node>();
    open_list.add(new Node(null, start[1], start[0], 0, end[1], end[0]));
    while (open_list.size() > 0)
    {
        Node currentNode = open_list.poll();
        closed_list.add(currentNode);

        if (currentNode.getX() == end[1] && currentNode.getY() == end[0])
        {
            currentNode = currentNode.getParent();
            while (currentNode.parent != null)
            {
                path.add(currentNode);
                currentNode = currentNode.getParent();
            }
            return path;
        }

        int[][] coords = {{-1, -1}, {0, -1}, {1, -1}, {-1, 0}, {1, 0}, {-1, 1}, {0, 1}, {1, 1}};
        for (int i = 0; i < coords.length; ++i)
        {
            int childX = currentNode.getX()+coords[i][0];
            int childY = currentNode.getY()+coords[i][1];
            if (childX < 0 || childX >= NUM_COLS || childY < 0 || childY >= NUM_ROWS || grid[childY][childX] == 1)
            {
                continue;
            }
            boolean exists = false;
            for (Node j: open_list)
            {
                if (j.getX() == childX && j.getY() == childY)
                {
                    exists = true;
                    if(currentNode.getG() + Math.sqrt(abs(coords[i][0]) + abs(coords[i][1])) < j.getG())
                    {
                        j.setParent(currentNode);
                        j.setG(currentNode.getG() + Math.sqrt(abs(coords[i][0]) + abs(coords[i][1])));
                        j.setF();
                    }
                }
            }
            for (Node j: closed_list)
            {
                if (j.getX() == childX && j.getY() == childY)
                {
                    exists = true;
                    if(currentNode.getG() + Math.sqrt(abs(coords[i][0]) + abs(coords[i][1])) < j.getG())
                    {
                        j.setParent(currentNode);
                        j.setG(currentNode.getG() + Math.sqrt(abs(coords[i][0]) + abs(coords[i][1])));
                    }
                }
            }
            if (exists)
            {
                continue;
            }
            Node child = new Node(currentNode, childX, childY, currentNode.getG() + Math.sqrt(abs(coords[i][0]) + abs(coords[i][1])), end[1], end[0]);
            open_list.add(child);
        }
    }
    System.out.println("End not found.");
    return null;
}

public void setup()
{
    
    frameRate(30);
}
public void draw()
{
    background(240);
    textSize(12);

    // Draw Grid
    for (int i = 100; i <= 20 * NUM_COLS + 100; i += 20)
    {
        line(i, 100, i, 500);
    }
    for (int i = 100; i <= 20 * NUM_ROWS + 100; i += 20)
    {
        line(100, i, 700, i);
    }
    for (int row = 0; row < NUM_ROWS; ++row)
    {
        for (int col = 0; col < NUM_COLS; ++ col)
        {
            if (grid[row][col] == 1) 
            {
                fill(30);
            } 
            else if (grid[row][col] == 2) 
            {
                fill(0, 200, 0);
            } 
            else if (grid[row][col] == 3) 
            {
                fill(200, 0, 0);
            } 
            else if (grid[row][col] == 4)
            {
                fill(0, 0, 200);
            }
            else {
                fill(240);
            }
            rect(20 * col + 100, 20 * row + 100, 20, 20);
        }
    }

    // Erase Button
    fill(225);
    rect(ERASE_BUTTON_X, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
    fill(240);
    rect(ERASE_BUTTON_X+75, BUTTON_Y+7, BUTTON_HEIGHT-15, BUTTON_HEIGHT-15);
    fill(0);
    text("Erase", ERASE_BUTTON_X+15, BUTTON_Y + BUTTON_HEIGHT-10);

    // Wall Button
    fill(225);
    rect(WALL_BUTTON_X, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
    fill(30);
    rect(WALL_BUTTON_X+75, BUTTON_Y+7, BUTTON_HEIGHT-15, BUTTON_HEIGHT-15);
    fill(0);
    text("Wall", WALL_BUTTON_X+15, BUTTON_Y + BUTTON_HEIGHT-10);

    // Start Button
    fill(225);
    rect(START_BUTTON_X, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
    fill(0, 200, 0);
    rect(START_BUTTON_X+75, BUTTON_Y+7, BUTTON_HEIGHT-15, BUTTON_HEIGHT-15);
    fill(0);
    text("Start", START_BUTTON_X+15, BUTTON_Y + BUTTON_HEIGHT-10);

    // End Button
    fill(225);
    rect(END_BUTTON_X, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
    fill(200, 0, 0);
    rect(END_BUTTON_X+75, BUTTON_Y+7, BUTTON_HEIGHT-15, BUTTON_HEIGHT-15);
    fill(0);
    text("End", END_BUTTON_X+15, BUTTON_Y + BUTTON_HEIGHT-10);

    // Selected Text
    if (selected == 1) 
    {
        fill(30);
    } 
    else if (selected == 2) 
    {
        fill(0, 200, 0);
    } 
    else if (selected == 3) 
    {
        fill(200, 0, 0);
    } 
    else 
    {
        fill(240);
    }
    rect(SELECTED_TEXT_X+75, BUTTON_Y+7, BUTTON_HEIGHT-15, BUTTON_HEIGHT-15);
    fill(0);
    text("Selected:", SELECTED_TEXT_X+15, BUTTON_Y + BUTTON_HEIGHT-10);
    
    // BFS Button
    fill(225);
    rect(BFS_BUTTON_X, SEARCHING_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
    fill(0);
    text("BFS", BFS_BUTTON_X+40, SEARCHING_Y + BUTTON_HEIGHT-10);
    
    // DFS Button
    fill(225);
    rect(DFS_BUTTON_X, SEARCHING_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
    fill(0);
    text("DFS", DFS_BUTTON_X+40, SEARCHING_Y + BUTTON_HEIGHT-10);
    
    // Dijkstra Button
    fill(225);
    rect(DIJKSTRA_BUTTON_X, SEARCHING_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
    fill(0);
    text("Dijkstra", DIJKSTRA_BUTTON_X+25, SEARCHING_Y + BUTTON_HEIGHT-10);
    
    // A* Button
    fill(225);
    rect(ASTAR_BUTTON_X, SEARCHING_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
    fill(0);
    text("A*", ASTAR_BUTTON_X+40, SEARCHING_Y + BUTTON_HEIGHT-10);
}

public void mousePressed()
{
    if (ERASE_BUTTON_X <= mouseX  && mouseX <= ERASE_BUTTON_X + BUTTON_WIDTH && BUTTON_Y <= mouseY && mouseY <= BUTTON_Y + BUTTON_HEIGHT)
    {
        selected = 0;
    } 
    else if (WALL_BUTTON_X <= mouseX  && mouseX <= WALL_BUTTON_X + BUTTON_WIDTH && BUTTON_Y <= mouseY && mouseY <= BUTTON_Y + BUTTON_HEIGHT)
    {
        selected = 1;
    } 
    else if (START_BUTTON_X <= mouseX  && mouseX <= START_BUTTON_X + BUTTON_WIDTH && BUTTON_Y <= mouseY && mouseY <= BUTTON_Y + BUTTON_HEIGHT)
    {
        selected = 2;
    } 
    else if (END_BUTTON_X <= mouseX  && mouseX <= END_BUTTON_X + BUTTON_WIDTH && BUTTON_Y <= mouseY && mouseY <= BUTTON_Y + BUTTON_HEIGHT)
    {
        selected = 3;
    } 
    else if (100 <= mouseX  && mouseX <= 20 * NUM_COLS + 100 && 100 <= mouseY && mouseY <= 20 * NUM_ROWS + 100)
    {
        if (selected == 2)
        {
            if (start[0] != -1 && start[1] != -1)
            {
                grid[start[0]][start[1]] = -1;
            }
            start[0] = (mouseY - 100) / 20;
            start[1] = (mouseX - 100) / 20;
        } 
        else if (selected == 3)
        {
            if (end[0] != -1 && end[1] != -1)
            {
                grid[end[0]][end[1]] = -1;
            }
            end[0] = (mouseY - 100) / 20;
            end[1] = (mouseX - 100) / 20;
        }
        if (grid[(mouseY - 100) / 20][(mouseX - 100) / 20] == 2)
        {
            start[0] = -1;
            start[1] = -1;
        } 
        else if (grid[(mouseY - 100) / 20][(mouseX - 100) / 20] == 3)
        {
            end[0] = -1;
            end[1] = -1;
        }
        grid[(mouseY - 100) / 20][(mouseX - 100) / 20] = selected;
    }
    else if (BFS_BUTTON_X <= mouseX  && mouseX <= BFS_BUTTON_X + BUTTON_WIDTH && SEARCHING_Y <= mouseY && mouseY <= SEARCHING_Y + BUTTON_HEIGHT && start[0] > 0 && end[0] > 0)
    {
        if (path != null)
        {
            for(int i = 0; i < path.size(); ++i)
            {
                if (grid[path.get(i).getY()][path.get(i).getX()] == 4)
                {
                    grid[path.get(i).getY()][path.get(i).getX()] = 0;
                }
            }
        }
        path = BFS(start, end);
        if (path != null)
        {
            for(int i = 0; i < path.size(); ++i)
            {
                grid[path.get(i).getY()][path.get(i).getX()] = 4;
            }
        }
    } 
    else if (DFS_BUTTON_X <= mouseX  && mouseX <= DFS_BUTTON_X + BUTTON_WIDTH && SEARCHING_Y <= mouseY && mouseY <= SEARCHING_Y + BUTTON_HEIGHT && start[0] > 0 && end[0] > 0)
    {
        if (path != null)
        {
            for(int i = 0; i < path.size(); ++i)
            {
                if (grid[path.get(i).getY()][path.get(i).getX()] == 4)
                {
                    grid[path.get(i).getY()][path.get(i).getX()] = 0;
                }
            }
        }
        path = DFS(start, end);
        if (path != null)
        {
            for(int i = 0; i < path.size(); ++i)
            {
                grid[path.get(i).getY()][path.get(i).getX()] = 4;
            }
        }
    } 
    else if (DIJKSTRA_BUTTON_X <= mouseX  && mouseX <= DIJKSTRA_BUTTON_X + BUTTON_WIDTH && SEARCHING_Y <= mouseY && mouseY <= SEARCHING_Y + BUTTON_HEIGHT && start[0] > 0 && end[0] > 0)
    {
        if (path != null)
        {
            for(int i = 0; i < path.size(); ++i)
            {
                if (grid[path.get(i).getY()][path.get(i).getX()] == 4)
                {
                    grid[path.get(i).getY()][path.get(i).getX()] = 0;
                }
            }
        }
        path = Dijkstra(start, end);
        if (path != null)
        {
            for(int i = 0; i < path.size(); ++i)
            {
                grid[path.get(i).getY()][path.get(i).getX()] = 4;
            }
        }
    } 
    else if (ASTAR_BUTTON_X <= mouseX  && mouseX <= ASTAR_BUTTON_X + BUTTON_WIDTH && SEARCHING_Y <= mouseY && mouseY <= SEARCHING_Y + BUTTON_HEIGHT && start[0] > 0 && end[0] > 0)
    {
        if (path != null)
        {
            for(int i = 0; i < path.size(); ++i)
            {
                if (grid[path.get(i).getY()][path.get(i).getX()] == 4)
                {
                    grid[path.get(i).getY()][path.get(i).getX()] = 0;
                }
            }
        }
        path = AStar(start, end);
        if (path != null)
        {
            for(int i = 0; i < path.size(); ++i)
            {
                grid[path.get(i).getY()][path.get(i).getX()] = 4;
            }
        }
    } 
}
public void mouseDragged()
{
    if (100 < mouseX  && mouseX < 20 * NUM_COLS + 100 && 100 < mouseY && mouseY < 20 * NUM_ROWS + 100
        && selected < 2)
    {
        if (grid[(mouseY - 100) / 20][(mouseX - 100) / 20] == 2)
        {
            start[0] = -1;
            start[1] = -1;
        } 
        else if (grid[(mouseY - 100) / 20][(mouseX - 100) / 20] == 3)
        {
            end[0] = -1;
            end[1] = -1;
        }
        grid[(mouseY - 100) / 20][(mouseX - 100) / 20] = selected;   
    }
}
    public void settings() {  size(800, 600); }
    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[] { "Searching" };
        if (passedArgs != null) {
          PApplet.main(concat(appletArgs, passedArgs));
        } else {
          PApplet.main(appletArgs);
        }
    }
}
