package kky;

import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;


public class DoubleQueue {
    public static void main(String[] args) {
        Queue<String> queue1 = new LinkedList<>(); 
        Queue<Integer> queue2 = new LinkedList<>();

        String[] tasks = {"보고서.pdf / 10", "사진.jpg / 3", "발표자료.ppt / 25"};

        for (String task : tasks) {
            StringTokenizer tokenizer = new StringTokenizer(task, " /"); 
            String fileName = tokenizer.nextToken(); 
            int pages = Integer.parseInt(tokenizer.nextToken()); 

            queue1.add(fileName); 
            queue2.add(pages);    
        }

        while (!queue1.isEmpty() && !queue2.isEmpty()) {
            String fileName = queue1.poll();
            int pages = queue2.poll();
            System.out.println(fileName + " /" + pages);
        }
    }
}
